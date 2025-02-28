# libedgetpu_16.0TF2.16.1-1.bb

## References
 * https://github.com/feranick/libedgetpu
 * Also check the following:
    * **meta-cariq/recipes-npu/tflite-runtime/tflite-runtime_2.16.1.bb**
    * **meta-cariq/recipes-npu/tensorflow-lite/tensorflow-lite_2.16.1.bb**

<br>

# How to build `libedgetpu.so`
## Build setup:
 * Clone libedgetpu
   * `git clone https://github.com/feranick/libedgetpu.git`
   * `git checkout 16.0TF2.16.1-1`


## Steps to build:
 * Change directory to `libedgetpu` (the cloned git folder)
 * Then, just follow the build instruction provided by feranick
 * `DOCKER_CPUS="aarch64" DOCKER_IMAGE="debian:bookworm" DOCKER_TARGETS=libedgetpu make docker-build`


## Installation guide:
 * Change directory to `tensorflow` (the cloned git folder)
 * copy files from `out/direct/aarch64/*` to the `files` folder of this recipe.



<br><br>
<span style="color:red">Note</span>: following packages are related and their version needs to be carefully upgraded

| S.No  | Recipe Name      | Version / Tag     |
|-------|-----------------|-------------------|
| 1     | libedgetpu      | 16.0TF2.16.1-1    |
| 2     | tensorflow-lite | v2.16.1           |
| 3     | tflite-runtime  | v2.16.1           |