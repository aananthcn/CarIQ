import os
import sys
import json
import subprocess

def print_usage():
    print("Usage: setup_car_iq.py <layer_json>")
    print("The <layer_json> should contain the 'layers' property with an array of dictionaries specifying 'repo', 'git', and 'branch'.")

def validate_arguments():
    if len(sys.argv) != 2:
        print("Error: Incorrect number of arguments.")
        print_usage()
        sys.exit(1)

    layer_json = sys.argv[1]
    if not os.path.isfile(layer_json):
        print(f"Error: File '{layer_json}' does not exist.")
        print_usage()
        sys.exit(1)
    
    return layer_json

def clone_repo(repo_name, git_url, branch):
    print(f"Cloning repository {repo_name} from {git_url} on branch {branch} with --depth=1...")
    try:
        subprocess.run(["git", "clone", "--depth", "1", "-b", branch, git_url, repo_name], check=True)
    except subprocess.CalledProcessError as e:
        print(f"Error: Failed to clone repository {repo_name} from {git_url}.")
        sys.exit(1)

def main():
    # Validate the input argument and check if the file exists
    layer_json = validate_arguments()

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
        print_usage()
        sys.exit(1)

    # Check for the "yocto" folder in the current directory
    if not os.path.isdir("yocto"):
        print("Error: 'yocto' directory not found in the current directory.")
        sys.exit(1)

    # Loop through each layer and check if the repository folder exists
    for layer in data["layers"]:
        if not all(k in layer for k in ("repo", "git", "branch")):
            print("Error: Each layer must contain 'repo', 'git', and 'branch' properties.")
            sys.exit(1)

        repo_name = layer["repo"]
        git_url = layer["git"]
        branch = layer["branch"]

        # Check if the repository folder already exists
        repo_path = os.path.join("yocto", repo_name)
        if os.path.isdir(repo_path):
            print(f"Repository '{repo_name}' already exists in the 'yocto' directory.")
        else:
            # Clone the repository if it does not exist, with depth=1
            clone_repo(repo_path, git_url, branch)

    print("Setup complete.")

if __name__ == "__main__":
    main()
