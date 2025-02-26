#!/usr/bin/env python3

import os
import sys
import json
import shutil
import subprocess
import argparse


def validate_cariq_node(cariq_node):
    valid_nodes = ["ccn", "en1", "en2"]
    if cariq_node.lower() not in valid_nodes:
        print(f"Error: Invalid cariq_node. Valid options are: {', '.join(valid_nodes)}")
        print("\nRelease operation failed.")
        sys.exit(1)


def read_local_conf(local_conf_path, key):
    try:
        with open(local_conf_path, 'r') as file:
            for line in file:
                if line.startswith(key):
                    return line.split('=')[1].strip().strip('"')
    except FileNotFoundError:
        print(f"Error: {local_conf_path} not found.")
        print("\nRelease operation failed.")
        sys.exit(1)
    return None


def delete_old_package(server_path):
    if os.path.exists(server_path):
        print(f"Requesting sudo permission to delete {server_path}")
        subprocess.run(["sudo", "rm", "-rf", server_path], check=True)
        print(f"Deleted old package at {server_path}")
    else:
        print(f"No previous package found at {server_path}, proceeding with deployment.")


def move_package_to_server(source, destination):
    print(f"Requesting sudo permission to move {source} to {destination}")
    subprocess.run(["sudo", "mv", source, destination], check=True)
    print(f"Successfully moved {source} to {destination}")


def copy_image_to_server(source, destination):
    print(f"Requesting sudo permission to copy {source} to {destination}")
    subprocess.run(["sudo", "cp", source, destination], check=True)
    print(f"Successfully copied {source} to {destination}")


def create_release(cariq_node, release_tag, btype):
    try:
        script_path = os.path.dirname(os.path.abspath(__file__))
        build_path = os.path.abspath(os.path.join(script_path, "../../", f"build-{btype}-{cariq_node}"))
        local_conf_path = os.path.join(build_path, "conf/local.conf")
        release_cf_path = os.path.join(script_path, f"../release", "cariq-release.conf")

        # Read configurations
        ota_srv_url = read_local_conf(release_cf_path, "CARIQ_RELEASE_URL").rstrip('/')
        machine_name = read_local_conf(local_conf_path, "MACHINE")
        deploy_path = os.path.join(build_path, "tmp/deploy/images", machine_name)

        # Define OTA package name consistent with ota-creator.py
        ota_pkg_name = f"ota-pkg-{btype}-{cariq_node}"
        release_folder = os.path.join(deploy_path, ota_pkg_name)

        if not ota_srv_url or not machine_name:
            print("Error: Missing CARIQ_RELEASE_URL or MACHINE in local.conf.")
            print("\nRelease operation failed.")
            sys.exit(1)

        if not os.path.exists(release_folder):
            print(f"OTA package not found at {release_folder}. Running ota-creator.py...")
            ota_creator_path = os.path.abspath(os.path.join(script_path, "../ota/ota-creator.py"))
            # Use positional arguments as expected by ota-creator.py
            subprocess.run([ota_creator_path, release_tag, cariq_node, btype], check=True)
            if not os.path.exists(release_folder):
                print("Error: OTA package creation failed. Please check for errors.")
                print("\nRelease operation failed.")
                sys.exit(1)

        # Determine the server path with btype included
        server_path_ota = f"/var/www/html/{ota_srv_url.split('/', 3)[-1]}/{ota_pkg_name}"
        server_path_img = server_path_ota + "/../"
        
        # Delete old package (if exists, otherwise continue)
        delete_old_package(server_path_ota)
        
        # Move new package to server location
        move_package_to_server(release_folder, server_path_ota)
        
        # Determine image file name and path based on btype
        build_suffix = "samsara" if btype == "ss" else "nirvana"
        if cariq_node.lower() in ["en1", "en2"]:
            image_file_name = f"cariq-{cariq_node}-{build_suffix}-{machine_name}.rootfs.wic.bz2"
        else:
            image_file_name = f"cariq-{cariq_node}-{build_suffix}-{machine_name}.wic.bz2"
        
        image_file_path = os.path.join(deploy_path, image_file_name)
        
        if os.path.exists(image_file_path):
            copy_image_to_server(image_file_path, os.path.join(server_path_img, image_file_name))
        else:
            print(f"Warning: Image file not found at {image_file_path}, skipping image copy.")
        
        print("\nRelease operation completed successfully!")
    
    except Exception as e:
        print(f"\nRelease operation failed due to an error: {str(e)}")
        sys.exit(1)


def main():
    parser = argparse.ArgumentParser(description="Create a release package for CarIQ nodes.")
    parser.add_argument("-n", "--node", required=True, help="Specify the cariq_node (ccn, en1, en2)")
    parser.add_argument("-r", "--release", required=True, help="Specify the release tag")
    parser.add_argument("-b", "--build-type", required=True, choices=["nv", "ss"], 
                        help="Specify the build type (nv for Nirvana, ss for Samsara)")
    args = parser.parse_args()

    validate_cariq_node(args.node)
    create_release(args.node, args.release, args.build_type)


if __name__ == "__main__":
    main()
