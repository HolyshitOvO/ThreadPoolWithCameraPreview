# ThreadPool with Camera Preview
-------------------------------

### Update Gradle 8.0
### Update use androidx
### Update project dependencies
Camera preview is landscape

---

This project demonstrate how to use `HandlerThread`s and `ThreadPool`s to burst capture 
images from Android's Camera API on a background thread.
The images are then:

- Converted from YUV `byte[]` to RGB `int[]`.
- Converted from RGB `int[]` to `Bitmap`.
- Bitmap is rotated if needed to fix orientation.
- A Thumbnail size Bitmap is made for displaying.
- Full size bitmap is written to disk as a Jpeg.
- Jpeg image is uploaded to a server.

The CameraPreview YUV `byte[]` is captured on a background `HandlerThread` and then 
ThreadPools are used to perform the operations above to improve speed and performance.

Performance Before TheadPool
=================
[![Performance before ThreadPool](http://img.youtube.com/vi/YmU8ogom_5g/0.jpg)](http://www.youtube.com/watch?v=YmU8ogom_5g)


Performance After ThreadPool
=================
[![Performance after ThreadPool](http://img.youtube.com/vi/77Lh9XpXArw/0.jpg)](http://www.youtube.com/watch?v=77Lh9XpXArw)
