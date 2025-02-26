#!/usr/bin/env python3

import os
import sys
import subprocess

# List of build folders to process
BUILD_FOLDERS = [
    "build-nv-ccn", "build-nv-en1", "build-nv-en2",
    "build-ss-ccn", "build-ss-en1", "build-ss-en2"
]


def extract_node_and_type(build_folder):
    """Extract cariq_node and build_type from the build folder name."""
    parts = build_folder.split('-')
    build_type = parts[1]  # 'nv' or 'ss'
    cariq_node = parts[2]  # 'ccn', 'en1', or 'en2'
    build_variant = "nirvana" if build_type == "nv" else "samsara"
    return cariq_node, build_type, build_variant


def build_folder(folder):
    """Perform the build steps for a given folder."""
    try:
        # Step 1: Change directory to the build folder
        os.chdir(folder)
        print(f"Entered directory: {folder}")

        # Step 2 & 3: Source the environment and run bitbake in the same shell
        cariq_node, _, build_variant = extract_node_and_type(folder)
        combined_cmd = f"source ../yocto/poky/oe-init-build-env . && bitbake cariq-{cariq_node}-{build_variant}"
        subprocess.run(combined_cmd, shell=True, executable="/bin/bash", check=True)
        print(f"Environment sourced and bitbake completed for: cariq-{cariq_node}-{build_variant}")

        # Step 4: Change back to the parent directory
        os.chdir("..")
        print(f"Returned to parent directory from: {folder}")

    except subprocess.CalledProcessError as e:
        print(f"Error during build process in {folder}: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"Unexpected error in {folder}: {e}")
        sys.exit(1)


def read_release_version():
    """Read the CARIQ_RELEASE_VER from cariq-release.conf without requiring INI format."""
    config_path = "tools/release/cariq-release.conf"
    if not os.path.exists(config_path):
        print(f"Error: Configuration file {config_path} not found.")
        sys.exit(1)

    try:
        with open(config_path, 'r') as f:
            for line in f:
                if line.strip().startswith("CARIQ_RELEASE_VER"):
                    key, value = line.split("=", 1)  # Split on first '=' only
                    return value.strip().strip('"')  # Remove whitespace and quotes
        print(f"Error: CARIQ_RELEASE_VER not found in {config_path}")
        sys.exit(1)
    except Exception as e:
        print(f"Error reading {config_path}: {e}")
        sys.exit(1)


def release_folder(folder, release_text):
    """Perform the release step for a given folder."""
    try:
        cariq_node, build_type, _ = extract_node_and_type(folder)
        release_cmd = [
            "tools/release/make-release.py",
            "-n", cariq_node,
            "-r", release_text,
            "-b", build_type
        ]
        subprocess.run(release_cmd, check=True)
        print(f"Release completed for: {folder} with version {release_text}")

    except subprocess.CalledProcessError as e:
        print(f"Error during release process for {folder}: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"Unexpected error during release for {folder}: {e}")
        sys.exit(1)


def main():
    # Check if script is run from the correct directory
    current_dir = os.path.basename(os.getcwd())
    if current_dir != "CarIQ":
        print("Error: This script must be executed from the CarIQ folder.")
        sys.exit(1)

    # Verify all build folders exist
    for folder in BUILD_FOLDERS:
        if not os.path.isdir(folder):
            print(f"Error: Build folder {folder} not found.")
            sys.exit(1)

    # Step 1: Build all folders
    print("Starting build process for all folders...")
    for folder in BUILD_FOLDERS:
        print(f"\n\n\nProcessing build folder: {folder}")
        build_folder(folder)  # Call the function with the folder string
    print("\nAll builds completed successfully!")

    # Step 2: Read release version
    release_text = read_release_version()
    print(f"Release version from config: {release_text}")

    # Step 3: Release all folders
    print("\nStarting release process for all folders...")
    for folder in BUILD_FOLDERS:
        print(f"\nProcessing release for: {folder}")
        release_folder(folder, release_text)
    print("\nAll releases completed successfully!")


if __name__ == "__main__":
    main()