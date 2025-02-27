#!/usr/bin/env python3
import os
import sys
import json
import subprocess
import argparse
import shutil


# List of Yocto releases
yocto_releases = [
    "styhead",
    "scarthgap",
    "nanbield",
    "mickledore",
    "langdale",
    "kirkstone"
]


def print_usage(parser):
    parser.print_help()


def clone_repo(repo_path, git_url, branch):
    print(f"Cloning repository {repo_path} from {git_url} on branch {branch} with --depth=1...")
    try:
        subprocess.run(["git", "clone", "--depth", "1", "-b", branch, git_url, repo_path], check=True)
    except subprocess.CalledProcessError:
        print(f"Error: Failed to clone repository {repo_path} from {git_url}.")
        sys.exit(1)


def checkout_branch(repo_path, branch):
    print(f"Checking out branch '{branch}' in repository {repo_path}...")
    try:
        subprocess.run(["git", "checkout", branch], cwd=repo_path, check=True)
    except subprocess.CalledProcessError:
        print(f"Error: Failed to checkout branch '{branch}' in {repo_path}.")
        sys.exit(1)


def move_repo(current_path, new_path):
    print(f"Moving repository from {current_path} to {new_path}...")
    try:
        # Ensure the parent directory of new_path exists
        os.makedirs(os.path.dirname(new_path), exist_ok=True)
        shutil.move(current_path, new_path)
    except Exception as e:
        print(f"Error: Failed to move repository from {current_path} to {new_path}: {e}")
        sys.exit(1)


def main():
    # Set up argument parser
    parser = argparse.ArgumentParser(
        description="Setup script for cloning Yocto layers from a JSON file.",
        usage="%(prog)s [-j JSON_FILE] [-f]"
    )
    parser.add_argument(
        "-j", "--json-file",
        help="Path to the JSON file containing layer information (with 'layers' array of 'repo', 'git', and 'branch').",
        required=True
    )
    parser.add_argument(
        "-f", "--force-checkout",
        action="store_true",
        help="Force checkout of the specified branch in existing layer directories."
    )

    # Parse arguments
    args = parser.parse_args()
    layer_json = args.json_file
    force_checkout = args.force_checkout

    # Check if the JSON file exists
    if not os.path.isfile(layer_json):
        print(f"Error: File '{layer_json}' does not exist.")
        print_usage(parser)
        sys.exit(1)

    # Read the JSON file
    with open(layer_json, 'r') as f:
        try:
            data = json.load(f)
        except json.JSONDecodeError:
            print("Error: Invalid JSON format in the input file.")
            sys.exit(1)

    # Check if "layers" property exists and is a list
    if "layers" not in data or not isinstance(data["layers"], list):
        print("Error: The JSON file must contain a 'layers' array.")
        print_usage(parser)
        sys.exit(1)

    # Check for the "yocto" folder in the current directory
    if not os.path.isdir("yocto"):
        print("Creating directory 'yocto'...")
        os.makedirs("yocto", exist_ok=True)

    # Loop through each layer
    for layer in data["layers"]:
        if not all(k in layer for k in ("repo", "git", "branch")):
            print("Error: Each layer must contain 'repo', 'git', and 'branch' properties.")
            sys.exit(1)

        repo_name = layer["repo"]
        git_url = layer["git"]
        branch = layer["branch"]

        # Repository path is always in ./yocto
        repo_path = os.path.join("yocto", repo_name)

        # Check if the repository folder already exists at the final path
        if os.path.isdir(repo_path):
            print(f"Repository '{repo_name}' already exists at {repo_path}.")
            if force_checkout:
                checkout_branch(repo_path, branch)
        else:
            # Clone the repository to the determined path
            clone_repo(repo_path, git_url, branch)

    print("Setup complete.")


if __name__ == "__main__":
    main()
