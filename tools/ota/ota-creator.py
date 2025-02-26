#!/usr/bin/env python3

import os
import sys
import shutil
import json
import tarfile
from datetime import datetime


# See the note above main function below.

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


def create_pkg_manifest(pkg_version, cariq_node, sw_version, kernel_url, rootfs_url, bootfs_url, machine_name):
    """Creates the package manifest JSON."""
    return {
        "machine_nm": machine_name,
        "sw_version": sw_version,
        "url_kernel": kernel_url,
        "url_rootfs": rootfs_url,
        "url_bootfs": bootfs_url,
        "allow_updt": "YES"
    }


def create_bootfiles_tar(deploy_path, ota_pkg_folder):
    """Creates a tarball of boot files for EN1 and EN2 nodes."""
    bootfiles_path = os.path.join(deploy_path, "bootfiles")
    bootfiles_tar = os.path.join(ota_pkg_folder, "bootfiles.tar.gz")

    if os.path.exists(bootfiles_path):
        with tarfile.open(bootfiles_tar, "w:gz") as tar:
            tar.add(bootfiles_path, arcname="boot")
        print(f"Boot files archived: {bootfiles_tar}")
    else:
        print("[ERROR]: Boot files directory not found, skipping tar creation.")


# In addition to the command line arguments, this script also parses local.conf file
# of each node of CarIQ platform.
def main():
    if len(sys.argv) != 4:
        print("Usage: create-update-pkg.py <pkg_version> <cariq_node> <btype>")
        print("btype options: nv (Nirvana) or ss (Samsara)")
        sys.exit(1)

    pkg_version = sys.argv[1]
    cariq_node = sys.argv[2]
    btype = sys.argv[3]
    valid_nodes = ["ccn", "en1", "en2"]
    valid_btypes = ["nv", "ss"]

    if cariq_node not in valid_nodes:
        print(f"Error: Invalid cariq_node. Valid options are: {', '.join(valid_nodes)}")
        sys.exit(1)

    if btype not in valid_btypes:
        print(f"Error: Invalid btype. Valid options are: {', '.join(valid_btypes)}")
        sys.exit(1)

    # Map abbreviated btype to expanded build type
    build_type_expanded = "nirvana" if btype == "nv" else "samsara"

    # Determine paths
    script_path = os.path.dirname(os.path.abspath(__file__))
    build_path = os.path.abspath(os.path.join(script_path, f"../..", f"build-{btype}-{cariq_node}"))
    local_conf_path = os.path.join(build_path, "conf/local.conf")
    release_cf_path = os.path.join(script_path, f"../release", "cariq-release.conf")

    # Read local.conf values
    machine_name = read_local_conf(local_conf_path, "MACHINE")
    deploy_path = os.path.join(build_path, f"tmp/deploy/images/{machine_name}")
    sw_version = read_local_conf(release_cf_path, "CARIQ_RELEASE_VER")
    ota_srv_url = read_local_conf(local_conf_path, "CARIQ_OTASRV_URL").rstrip('/')

    if not sw_version or not ota_srv_url or not machine_name:
        print("Error: Missing required values in local.conf.")
        sys.exit(1)

    if sw_version != pkg_version:
        print(f"Error: release version ({pkg_version}) passed does not match CARIQ_SW_VERSION in local.conf.")
        print(f"CARIQ_SW_VERSION set is: {sw_version}. Either change cariq-release.conf file or pass correctly!")
        sys.exit(1)

    # Define OTA package name and folder
    ota_pkg_name = f"ota-pkg-{btype}-{cariq_node}"
    ota_pkg_folder = os.path.join(deploy_path, ota_pkg_name)
    if os.path.exists(ota_pkg_folder):
        shutil.rmtree(ota_pkg_folder)
    os.makedirs(ota_pkg_folder)

    # Determine kernel file name based on cariq_node
    kernel_file_name = "fitImage" if cariq_node == "ccn" else "Image"
    kernel_file = os.path.join(deploy_path, kernel_file_name)

    # Determine rootfs file name based on cariq_node and expanded build type
    if cariq_node == "ccn":
        rootfs_file_name = f"cariq-{cariq_node}-{build_type_expanded}-{machine_name}.tar.bz2"
    else:
        rootfs_file_name = f"cariq-{cariq_node}-{build_type_expanded}-{machine_name}.rootfs.tar.bz2"

    rootfs_file = os.path.join(deploy_path, rootfs_file_name)

    if not os.path.exists(kernel_file):
        print(f"Error: Kernel file not found at {kernel_file}.")
        sys.exit(1)

    if not os.path.exists(rootfs_file):
        print(f"Error: Rootfs file not found at {rootfs_file}.")
        sys.exit(1)

    # Copy kernel and rootfs
    if cariq_node in ["en1", "en2"]:
        shutil.copy(kernel_file, os.path.join(ota_pkg_folder, "kernel_2712.img"))
    else:
        shutil.copy(kernel_file, os.path.join(ota_pkg_folder, kernel_file_name))

    shutil.copy(rootfs_file, os.path.join(ota_pkg_folder, rootfs_file_name))

    # Create bootfiles tarball for EN1 and EN2
    if cariq_node in ["en1", "en2"]:
        create_bootfiles_tar(deploy_path, ota_pkg_folder)

    # Create ota-pkg-manifest.json with updated URLs
    kernel_url = f"{ota_srv_url}/{ota_pkg_name}/"
    kernel_url += "kernel_2712.img" if cariq_node in ["en1", "en2"] else kernel_file_name
    rootfs_url = f"{ota_srv_url}/{ota_pkg_name}/{rootfs_file_name}"
    bootfs_url = f"{ota_srv_url}/{ota_pkg_name}/bootfiles.tar.gz"
    pkg_manifest = create_pkg_manifest(pkg_version, cariq_node, sw_version, kernel_url, rootfs_url, bootfs_url, machine_name)

    with open(os.path.join(ota_pkg_folder, "ota-pkg-manifest.json"), 'w') as manifest_file:
        json.dump(pkg_manifest, manifest_file, indent=4)

    # Move package to deploy_path with abbreviated btype
    dest_folder = os.path.join(deploy_path, ota_pkg_name)
    shutil.move(ota_pkg_folder, dest_folder)

    print(f"Update package for {cariq_node} created successfully at {dest_folder}.")


if __name__ == "__main__":
    main()
