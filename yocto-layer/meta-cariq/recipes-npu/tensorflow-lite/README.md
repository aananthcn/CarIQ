# tensorflow-lite_2.16.1.bb

## References
 * https://github.com/feranick/tensorflow
 * https://github.com/feranick/TFlite-builds
 * Also check **meta-cariq/recipes-npu/tflite-runtime/tflite-runtime_2.16.1.bb**

<br>

# How to build `libtensorflowlite.so`
## Build setup:
 * Clone tensorflow
   * `git clone https://github.com/feranick/tensorflow.git`
   * `git checkout v2.16.1`
 * Download the right version of bazel (for Tensorflow v2.16.1, bazel 6.5.0 is the best version)
   * `wget https://github.com/bazelbuild/bazel/releases/download/6.5.0/bazel-6.5.0-installer-linux-x86_64.sh`
 * Install bazel
   * `bash bazel-6.5.0-installer-linux-x86_64.sh --user`
   * The above step will install bazel in ~/bin folder.


## Steps to build:
 * Change directory to `tensorflow` (the cloned git folder)
 * `export PATH="$HOME/bin:$PATH"`
 * `export TF_PYTHON_VERSION=3.12`
 * For release build
   * `bazel build //tensorflow/lite:libtensorflowlite.so --config=elinux_aarch64`
 * For debug build
   * `bazel build //tensorflow/lite:libtensorflowlite.so --config=elinux_aarch64 --copt="-g"`


## Installation guide:
 * Change directory to `tensorflow` (the cloned git folder)
 * `ls bazel-bin/tensorflow/lite/`
 * You should see your `libtensorflowlite.so` file there, copy to the `files` folder of this recipe.


<br><br>
<span style="color:red">Note</span>: following packages are related and their version needs to be carefully upgraded

| S.No  | Recipe Name      | Version / Tag     |
|-------|-----------------|-------------------|
| 1     | libedgetpu      | 16.0TF2.16.1-1    |
| 2     | tensorflow-lite | v2.16.1           |
| 3     | tflite-runtime  | v2.16.1           |