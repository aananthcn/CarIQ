#!/usr/bin/env python3

import os
import sys
import shutil
import json
from datetime import datetime


def read_local_conf(local_conf_path, key):
    """Reads the value of a specific key from the local.conf file."""
    try:
        with open(local_conf_path, 'r') as file:
            for line in file:
                if line.startswith(key):
                    return line.split('=')[1].strip().strip('"')
    except FileNotFoundError:
        print(f"Error: {local_conf_path} not found.")
        sys.exit(1)
    return None


def create_pkg_manifest(pkg_version, cariq_node, sw_version, kernel_url, rootfs_url, machine_name):
    """Creates the package manifest JSON."""
    return {
        "machine_nm": machine_name,
        "sw_version": sw_version,
        "url_kernel": kernel_url,
        "url_rootfs": rootfs_url,
        "allow_updt": "YES"
    }


def main():
    if len(sys.argv) != 3:
        print("Usage: create-update-pkg.py <pkg_version> <cariq_node>")
        sys.exit(1)

    pkg_version = sys.argv[1]
    cariq_node = sys.argv[2]
    valid_nodes = ["ccn", "en1", "en2"]

    if cariq_node not in valid_nodes:
        print(f"Error: Invalid cariq_node. Valid options are: {', '.join(valid_nodes)}")
        sys.exit(1)

    # Determine paths
    script_path = os.path.dirname(os.path.abspath(__file__))
    build_path = os.path.abspath(os.path.join(script_path, f"../..", f"build-{cariq_node}"))
    local_conf_path = os.path.join(build_path, "conf/local.conf")

    # Read local.conf values
    machine_name = read_local_conf(local_conf_path, "MACHINE")
    deploy_path = os.path.join(build_path, f"tmp/deploy/images/{machine_name}")
    sw_version = read_local_conf(local_conf_path, "CARIQ_SW_VERSION")
    ota_srv_url = read_local_conf(local_conf_path, "CARIQ_OTASRV_URL").rstrip('/')

    if not sw_version or not ota_srv_url or not machine_name:
        print("Error: Missing required values in local.conf.")
        sys.exit(1)

    if sw_version != pkg_version:
        print("Error: pkg_version does not match CARIQ_SW_VERSION in local.conf.")
        sys.exit(1)

    # Prepare update package folder
    ota_pkg_folder = os.path.join(deploy_path, f"ota-pkg-{cariq_node}")
    if os.path.exists(ota_pkg_folder):
        shutil.rmtree(ota_pkg_folder)
    os.makedirs(ota_pkg_folder)

    # Copy kernel and rootfs
    kernel_file = os.path.join(deploy_path, "fitImage")
    rootfs_file = os.path.join(deploy_path, f"cariq-{cariq_node}-image-{machine_name}.tar.bz2")

    if not os.path.exists(kernel_file):
        print(f"Error: Kernel file not found at {kernel_file}.")
        sys.exit(1)

    if not os.path.exists(rootfs_file):
        print(f"Error: Rootfs file not found at {rootfs_file}.")
        sys.exit(1)

    shutil.copy(kernel_file, os.path.join(ota_pkg_folder, "fitImage"))
    shutil.copy(rootfs_file, os.path.join(ota_pkg_folder, f"cariq-{cariq_node}-image-{machine_name}.tar.bz2"))

    # Create ota-pkg-manifest.json
    kernel_url = f"{ota_srv_url}/ota-pkg-{cariq_node}/fitImage"
    rootfs_url = f"{ota_srv_url}/ota-pkg-{cariq_node}/cariq-{cariq_node}-image-{machine_name}.tar.bz2"
    pkg_manifest = create_pkg_manifest(pkg_version, cariq_node, sw_version, kernel_url, rootfs_url, machine_name)

    with open(os.path.join(ota_pkg_folder, "ota-pkg-manifest.json"), 'w') as manifest_file:
        json.dump(pkg_manifest, manifest_file, indent=4)

    # Move package to deploy_path
    dest_folder = os.path.join(deploy_path, f"ota-pkg-{cariq_node}")

    shutil.move(ota_pkg_folder, dest_folder)

    print(f"Update package for {cariq_node} created successfully at {dest_folder}.")


if __name__ == "__main__":
    main()
