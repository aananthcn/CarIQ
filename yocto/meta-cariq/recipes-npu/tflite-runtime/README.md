# python3-tflite-runtime_2.16.1.bb
## References
 * https://github.com/feranick/pycoral
 * https://github.com/feranick/TFlite-builds
 * Also check recipe: **meta-cariq/recipes-npu/tensorflow-lite/tensorflow-lite_2.16.1.bb**

<br>

# How to build this package `tflite-runtime`
## Build setup:
 * Follow steps in section **Building - Docker** of the README file in https://github.com/feranick/TFlite-builds
   * Install docker.io
 * Clone tensorflow
   * `git clone https://github.com/feranick/tensorflow.git`
   * `git checkout v2.16.1`
 * Modify docker file as given in the website above, but compare it against the `Dockerfile.py3` placed in this folder.
 * Modify Makefile for **Python 3.12** (`tensorflow/lite/tools/pip_package/Makefile`) as in the web page.

## Steps to build:
 * Change directory to `tensorflow` (the cloned git folder)
 * Compile using following command
   * `BUILD_NUM_JOBS=4 make -C tensorflow/lite/tools/pip_package docker-build TENSORFLOW_TARGET=aarch64 PYTHON_VERSION=3.12`
 * Compilation must not take more than 10 mins and the output can be seen in `tensorflow/lite/tools/pip_package/gen/tflite_pip/python3.12/tflite_runtime`

## Installation guide:
 * Change directory to `tensorflow` (the cloned git folder)
 * `ls tensorflow/lite/tools/pip_package/gen/tflite_pip/python3.12/dist/tflite_runtime-2.16.1-cp312-cp312-linux_aarch64.whl`
   * You should see the file
 * Copy the above files into the `files` folder of this recipe.
