#!/usr/bin/env python3

import os
import json
import requests
import argparse
from pathlib import Path


def read_config_file(config_path):
    """Reads and parses the JSON configuration file."""
    try:
        with open(config_path, 'r') as file:
            return json.load(file)
    except FileNotFoundError:
        print(f"Error: Configuration file not found at {config_path}.")
        exit(1)
    except json.JSONDecodeError:
        print("Error: Configuration file contains invalid JSON.")
        exit(1)


def download_file(url, dest_path):
    """Downloads a file from the given URL to the specified destination, showing progress."""
    try:
        response = requests.get(url, stream=True, verify=False)
        response.raise_for_status()
        total_size = int(response.headers.get('content-length', 0))
        block_size = 8192  # 8 KB
        downloaded_size = 0

        with open(dest_path, 'wb') as file:
            for chunk in response.iter_content(chunk_size=block_size):
                downloaded_size += len(chunk)
                file.write(chunk)
                print(f"Downloading... {downloaded_size / total_size:.2%} complete", end='\r')
        print("Download complete.                    \n")

    except requests.RequestException as e:
        print(f"Error: Failed to download {url}. {e}")
        exit(1)


def notify_user():
    """Prints a message to notify the user about the available update."""
    print("\nUpdate is available. Run 'ota-updater.sh' to continue.")


def main():
    parser = argparse.ArgumentParser(description="OTA Checker Script")
    parser.add_argument("-f", "--force", action="store_true", help="Skip version check and force update check")
    args = parser.parse_args()

    CONFIG_PATH = "/etc/ota/ota-config.json"
    DOWNLOAD_DIR = "/update/downloads"

    # Read configuration file
    config = read_config_file(CONFIG_PATH)
    server_url = config.get("server_url", "").rstrip('/')
    cariq_node = config.get("cariq_node", "")
    build_type = config.get("build_type", "")
    current_sw_version = config.get("sw_version", "")
    config_machine_name = config.get("machine_nm", "")

    if not server_url or not cariq_node or not current_sw_version or not config_machine_name:
        print("Error: Invalid or incomplete configuration file.")
        exit(1)

    if build_type == "nirvana":
        btype = "nv"
    else:
        btype = "ss"

    # Construct the manifest URL
    print("Preparing to download the manifest file...")
    manifest_url = f"{server_url}/ota-pkg-{btype}-{cariq_node}/ota-pkg-manifest.json"

    # Download the manifest file
    manifest_path = os.path.join(DOWNLOAD_DIR, "ota-pkg-manifest.json")
    os.makedirs(DOWNLOAD_DIR, exist_ok=True)
    download_file(manifest_url, manifest_path)

    # Parse the manifest file
    manifest = read_config_file(manifest_path)
    manifest_sw_version = manifest.get("sw_version", "")
    allow_update = manifest.get("allow_updt", "").lower()
    if cariq_node in ["en1", "en2"]:
        url_bootfs = manifest.get("url_bootfs", "")
    url_kernel = manifest.get("url_kernel", "")
    url_rootfs = manifest.get("url_rootfs", "")
    manifest_machine_name = manifest.get("machine_nm", "")

    # Validate manifest
    if not args.force and manifest_sw_version == current_sw_version:
        print("No update required. Software version is up to date.\n\n")
        exit(0)

    if allow_update not in ["yes", "true"]:
        print("Update not allowed as per manifest configuration.\n\n")
        exit(0)

    if not url_kernel or not url_rootfs:
        print("Error: Invalid URLs in manifest file.\n\n")
        exit(1)

    if cariq_node in ["en1", "en2"] and not url_bootfs:
        print("Error: Invalid URLs in manifest file.\n\n")
        exit(1)

    if manifest_machine_name != config_machine_name:
        print(f"Error: Machine name mismatch. Config: {config_machine_name}, Manifest: {manifest_machine_name}\n\n")
        exit(1)

    # Download bootfiles
    if cariq_node in ["en1", "en2"]:
        bootfs_filename = os.path.basename(url_bootfs)
        bootfs_dest = os.path.join(DOWNLOAD_DIR, bootfs_filename)
        print("Downloading bootfiles...")
        download_file(url_bootfs, bootfs_dest)

    # Download kernel and rootfs using filenames from URLs
    kernel_filename = os.path.basename(url_kernel)
    rootfs_filename = os.path.basename(url_rootfs)

    kernel_dest = os.path.join(DOWNLOAD_DIR, kernel_filename)
    rootfs_dest = os.path.join(DOWNLOAD_DIR, rootfs_filename)

    print("Downloading kernel...")
    download_file(url_kernel, kernel_dest)

    print("Downloading root filesystem...")
    download_file(url_rootfs, rootfs_dest)

    # Notify the user
    notify_user()

    # Clean up the manifest file
    if os.path.exists(manifest_path):
        os.remove(manifest_path)
        print("Cleaned up package manifest file.\n\n")


if __name__ == "__main__":
    main()
