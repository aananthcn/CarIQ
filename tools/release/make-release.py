#!/usr/bin/env python3

import os
import sys
import json
import shutil
import subprocess


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



def create_release(cariq_node):
    try:
        script_path = os.path.dirname(os.path.abspath(__file__))
        build_path = os.path.abspath(os.path.join(script_path, "../..", f"build-{cariq_node}"))
        local_conf_path = os.path.join(build_path, "conf/local.conf")

        # Read configurations
        ota_srv_url = read_local_conf(local_conf_path, "CARIQ_OTASRV_URL").rstrip('/')
        machine_name = read_local_conf(local_conf_path, "MACHINE")
        deploy_path = os.path.join(build_path, "tmp/deploy/images", machine_name)
        release_folder = os.path.join(deploy_path, f"ota-pkg-{cariq_node}")

        if not ota_srv_url or not machine_name:
            print("Error: Missing CARIQ_OTASRV_URL or MACHINE in local.conf.")
            print("\nRelease operation failed.")
            sys.exit(1)

        if not os.path.exists(release_folder):
            print(f"Error: OTA package not found at {release_folder}. Please run ota-creator.py first.")
            print("\nRelease operation failed.")
            sys.exit(1)

        # Determine the server path
        server_path = f"/var/www/html/{ota_srv_url.split('/', 3)[-1]}/ota-pkg-{cariq_node}"
        
        # Delete old package (if exists, otherwise continue)
        delete_old_package(server_path)
        
        # Move new package to server location
        move_package_to_server(release_folder, server_path)
        
        # Determine image file name and path
        if cariq_node.lower() in ["en1", "en2"]:
            image_file_name = f"cariq-{cariq_node}-image-{machine_name}.rootfs.wic.bz2"
        else:
            image_file_name = f"cariq-{cariq_node}-image-{machine_name}.wic.bz2"
        
        image_file_path = os.path.join(deploy_path, image_file_name)
        
        if os.path.exists(image_file_path):
            copy_image_to_server(image_file_path, os.path.join(server_path, image_file_name))
        else:
            print(f"Warning: Image file not found at {image_file_path}, skipping image copy.")
        
        print("\nRelease operation completed successfully!")
    
    except Exception as e:
        print(f"\nRelease operation failed due to an error: {str(e)}")
        sys.exit(1)



def main():
    if len(sys.argv) != 2:
        print("Usage: make-release.py <cariq_node>")
        print("\nRelease operation failed.")
        sys.exit(1)
    cariq_node = sys.argv[1]
    validate_cariq_node(cariq_node)
    create_release(cariq_node)



if __name__ == "__main__":
    main()
