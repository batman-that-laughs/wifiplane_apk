package ketai.camera;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import processing.core.PApplet;
import processing.core.PImage;

public class KetaiCamera extends PImage {
    String SAVE_DIR = "";
    private Camera.AutoFocusCallback autofocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean result, Camera c) {
            PApplet.println("Autofocus result: " + result);
        }
    };
    boolean available = false;
    public Object callbackdelegate;
    /* access modifiers changed from: private */
    public Camera camera;
    /* access modifiers changed from: private */
    public int cameraFPS;
    private int cameraID;
    public boolean enableFlash;
    /* access modifiers changed from: private */
    public int frameHeight;
    /* access modifiers changed from: private */
    public int frameWidth;
    public boolean isRGBPreviewSupported;
    public boolean isStarted;
    private Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            PApplet.println("pictureCallback entered...");
            if (camera != null) {
                try {
                    PApplet.println("Saving image: " + KetaiCamera.this.savePhotoPath);
                    FileOutputStream outStream = new FileOutputStream(KetaiCamera.this.savePhotoPath);
                    try {
                        outStream.write(data);
                        outStream.close();
                        if (!(KetaiCamera.this.onSavePhotoEventMethod == null || KetaiCamera.this.myPixels == null || KetaiCamera.this.savePhotoPath == null)) {
                            try {
                                KetaiCamera.this.onSavePhotoEventMethod.invoke(KetaiCamera.this.parent, new Object[]{KetaiCamera.this.savePhotoPath});
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e2) {
                                e2.printStackTrace();
                            }
                        }
                        camera.startPreview();
                        FileOutputStream fileOutputStream = outStream;
                    } catch (FileNotFoundException e3) {
                        e = e3;
                        FileOutputStream fileOutputStream2 = outStream;
                    } catch (IOException e4) {
                        e = e4;
                        FileOutputStream fileOutputStream3 = outStream;
                        e.printStackTrace();
                    } catch (RuntimeException e5) {
                        FileOutputStream fileOutputStream4 = outStream;
                    }
                } catch (FileNotFoundException e6) {
                    e = e6;
                    e.printStackTrace();
                } catch (IOException e7) {
                    e = e7;
                    e.printStackTrace();
                } catch (RuntimeException e8) {
                }
            }
        }
    };
    int lastProcessedFrame = 0;
    /* access modifiers changed from: private */
    public Vector<Method> listeners = new Vector<>();
    SurfaceTexture mTexture;
    /* access modifiers changed from: private */
    public int[] myPixels;
    private MediaScannerConnection.OnScanCompletedListener myScannerCallback = new MediaScannerConnection.OnScanCompletedListener() {
        public void onScanCompleted(String arg0, Uri arg1) {
            PApplet.println("Media Scanner returned: " + arg1.toString() + " => " + arg0);
        }
    };
    protected Method onFaceDetectionEventMethod;
    protected Method onPreviewEventMethod;
    protected Method onPreviewEventMethodPImage;
    protected Method onSavePhotoEventMethod;
    private int photoHeight;
    private int photoWidth;
    Camera.PreviewCallback previewcallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (camera != null && KetaiCamera.this.isStarted) {
                if (KetaiCamera.this.myPixels == null || KetaiCamera.this.myPixels.length != KetaiCamera.this.frameWidth * KetaiCamera.this.frameHeight) {
                    KetaiCamera.this.myPixels = new int[(KetaiCamera.this.frameWidth * KetaiCamera.this.frameHeight)];
                }
                KetaiCamera.this.decodeYUV420SP(data);
                if (KetaiCamera.this.parent.millis() - KetaiCamera.this.lastProcessedFrame >= 1000 / KetaiCamera.this.cameraFPS) {
                    KetaiCamera.this.lastProcessedFrame = KetaiCamera.this.parent.millis();
                    if (!(KetaiCamera.this.onPreviewEventMethod == null || KetaiCamera.this.myPixels == null)) {
                        try {
                            KetaiCamera.this.onPreviewEventMethod.invoke(KetaiCamera.this.callbackdelegate, new Object[0]);
                        } catch (Exception e) {
                            PApplet.println(" onCameraPreviewEvent() had  an error:" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    if (!(KetaiCamera.this.onPreviewEventMethodPImage == null || KetaiCamera.this.myPixels == null)) {
                        try {
                            KetaiCamera.this.onPreviewEventMethodPImage.invoke(KetaiCamera.this.callbackdelegate, new Object[]{KetaiCamera.this.self});
                        } catch (Exception e2) {
                            PApplet.println("Disabling onCameraPreviewEvent(KetaiCamera) because of an error:" + e2.getMessage());
                            e2.printStackTrace();
                            KetaiCamera.this.onPreviewEventMethodPImage = null;
                        }
                    }
                    Iterator it = KetaiCamera.this.listeners.iterator();
                    while (it.hasNext()) {
                        Method m = (Method) it.next();
                        try {
                            m.invoke(KetaiCamera.this.callbackdelegate, new Object[]{KetaiCamera.this.self});
                        } catch (Exception e3) {
                            PApplet.println("Disabling onCameraPreviewEvent(KetaiCamera) because of an error:" + e3.getMessage());
                            e3.printStackTrace();
                        }
                    }
                }
            }
        }
    };
    public boolean requestedPortraitImage = false;
    public boolean requestedStart;
    /* access modifiers changed from: private */
    public String savePhotoPath = "";
    KetaiCamera self;
    boolean supportsFaceDetection = false;

    public KetaiCamera(PApplet pParent, int _width, int _height, int _framesPerSecond) {
        super(_width, _height, 2);
        ApplicationInfo ai;
        this.parent = pParent;
        this.parent.requestPermission("android.permission.CAMERA", "onPermissionResult", this);
        this.bitmap = Bitmap.createBitmap(this.pixels, this.width, this.height, Bitmap.Config.ARGB_8888);
        this.frameWidth = _width;
        this.frameHeight = _height;
        this.photoWidth = this.frameWidth;
        this.photoHeight = this.frameHeight;
        this.cameraFPS = _framesPerSecond;
        this.isStarted = false;
        this.requestedStart = false;
        this.myPixels = new int[(_width * _height)];
        this.self = this;
        this.isRGBPreviewSupported = false;
        this.enableFlash = false;
        this.cameraID = 0;
        this.callbackdelegate = this.parent;
        determineObjectIntentions(pParent);
        PackageManager pm = this.parent.getActivity().getApplicationContext().getPackageManager();
        try {
            ai = pm.getApplicationInfo(this.parent.getActivity().getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            ai = null;
        }
        this.SAVE_DIR = (String) (ai != null ? pm.getApplicationLabel(ai) : "unknownApp");
        this.parent.registerMethod("resume", this);
        this.parent.registerMethod("pause", this);
        this.parent.registerMethod("dispose", this);
        read();
    }

    public void onPermissionResult(boolean granted) {
        if (!granted) {
            PApplet.println("User did not grant camera permission.  Camera is disabled.");
        }
    }

    private void determineObjectIntentions(Object o) {
        try {
            this.onPreviewEventMethod = o.getClass().getMethod("onCameraPreviewEvent", new Class[0]);
            PApplet.println("Found onCameraPreviewEvent ");
        } catch (NoSuchMethodException e) {
            this.onPreviewEventMethod = null;
        }
        try {
            this.onPreviewEventMethodPImage = o.getClass().getMethod("onCameraPreviewEvent", new Class[]{KetaiCamera.class});
        } catch (NoSuchMethodException e2) {
            this.onPreviewEventMethodPImage = null;
        }
        try {
            this.onFaceDetectionEventMethod = o.getClass().getMethod("onFaceDetectionEvent", new Class[]{KetaiFace[].class});
        } catch (NoSuchMethodException e3) {
            this.onFaceDetectionEventMethod = null;
        }
        try {
            this.onSavePhotoEventMethod = o.getClass().getMethod("onSavePhotoEvent", new Class[]{String.class});
        } catch (NoSuchMethodException e4) {
            this.onSavePhotoEventMethod = null;
        }
    }

    public void manualSettings() {
        if (this.camera != null) {
            Camera.Parameters cameraParameters = this.camera.getParameters();
            if (cameraParameters.isAutoExposureLockSupported()) {
                cameraParameters.setAutoExposureLock(true);
            }
            if (!cameraParameters.isAutoWhiteBalanceLockSupported()) {
                Iterator<String> it = cameraParameters.getSupportedWhiteBalance().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    String s = it.next();
                    if (s.equalsIgnoreCase("cloudy-daylight")) {
                        cameraParameters.setWhiteBalance(s);
                        break;
                    }
                }
            } else {
                cameraParameters.setAutoWhiteBalanceLock(true);
            }
            for (String s2 : cameraParameters.getSupportedFocusModes()) {
                if (s2.equalsIgnoreCase("fixed")) {
                    cameraParameters.setFocusMode("fixed");
                }
            }
            try {
                this.camera.setParameters(cameraParameters);
            } catch (RuntimeException x) {
                PApplet.println("Failed to set parameters to manual." + x.getMessage());
            }
        }
    }

    public void setZoom(int _zoom) {
        if (this.camera != null) {
            Camera.Parameters cameraParameters = this.camera.getParameters();
            if (_zoom > cameraParameters.getMaxZoom()) {
                _zoom = cameraParameters.getMaxZoom();
            } else if (_zoom < 0) {
                _zoom = 0;
            }
            cameraParameters.setZoom(_zoom);
            this.camera.setParameters(cameraParameters);
        }
    }

    public int getZoom() {
        if (this.camera == null) {
            return 0;
        }
        return this.camera.getParameters().getZoom();
    }

    public void autoSettings() {
        if (this.camera != null) {
            Camera.Parameters cameraParameters = this.camera.getParameters();
            if (cameraParameters.isAutoExposureLockSupported()) {
                cameraParameters.setAutoExposureLock(false);
            }
            if (cameraParameters.isAutoWhiteBalanceLockSupported()) {
                cameraParameters.setAutoWhiteBalanceLock(false);
            }
            for (String s : cameraParameters.getSupportedFocusModes()) {
                if (s.equalsIgnoreCase("continuous-picture")) {
                    cameraParameters.setFocusMode("continuous-picture");
                }
            }
            this.camera.setParameters(cameraParameters);
            this.camera.autoFocus(this.autofocusCB);
        }
    }

    public String dump() {
        String result;
        String result2;
        if (this.camera == null) {
            return "";
        }
        Camera.Parameters p = this.camera.getParameters();
        String result3 = String.valueOf(String.valueOf("") + "Zoom: " + p.getZoom() + "\n") + "White Balance: " + p.getWhiteBalance() + "\n";
        if (p.isAutoWhiteBalanceLockSupported()) {
            result = String.valueOf(result3) + "\t Lock supported, state: " + p.getAutoWhiteBalanceLock() + "\n";
        } else {
            result = String.valueOf(result3) + "\t Lock NOT supported\n";
        }
        float[] f = new float[3];
        String fd = "";
        p.getFocusDistances(f);
        for (int i = 0; i < f.length; i++) {
            fd = String.valueOf(fd) + String.valueOf(f[i]) + " ";
        }
        String result4 = String.valueOf(String.valueOf(String.valueOf(String.valueOf(result) + "Focal Distances: " + fd + " \n") + "Focal Depth: " + p.getFocalLength() + "\n") + "Focus Mode: " + p.getFocusMode() + "\n") + "Exposure: " + p.getExposureCompensation() + "\n";
        if (p.isAutoExposureLockSupported()) {
            result2 = String.valueOf(result4) + "\t Lock supported, state: " + p.getAutoExposureLock() + "\n";
        } else {
            result2 = String.valueOf(result4) + "\t Lock NOT supported\n";
        }
        return String.valueOf(result2) + "Native camera face detection support: " + this.supportsFaceDetection;
    }

    public void setSaveDirectory(String _dirname) {
        this.SAVE_DIR = _dirname;
    }

    public int getPhotoWidth() {
        return this.photoWidth;
    }

    public int getPhotoHeight() {
        return this.photoHeight;
    }

    public void setPhotoSize(int width, int height) {
        this.photoWidth = width;
        this.photoHeight = height;
        determineCameraParameters();
    }

    public void enableFlash() {
        this.enableFlash = true;
        if (this.camera != null) {
            Camera.Parameters cameraParameters = this.camera.getParameters();
            cameraParameters.setFlashMode("torch");
            try {
                this.camera.setParameters(cameraParameters);
            } catch (RuntimeException e) {
            }
        }
    }

    public void disableFlash() {
        this.enableFlash = false;
        if (this.camera != null) {
            Camera.Parameters cameraParameters = this.camera.getParameters();
            cameraParameters.setFlashMode("off");
            try {
                this.camera.setParameters(cameraParameters);
            } catch (RuntimeException e) {
            }
        }
    }

    public void setCameraID(int _id) {
        boolean isActive = this.isStarted;
        if (_id < Camera.getNumberOfCameras() && this.cameraID != _id) {
            if (isActive) {
                stop();
            }
            this.cameraID = _id;
        }
        if (isActive) {
            start();
        }
    }

    public int getCameraID() {
        return this.cameraID;
    }

    public boolean start() {
        int result;
        this.requestedStart = true;
        if (this.isStarted) {
            return true;
        }
        try {
            if (this.camera == null) {
                try {
                    this.camera = Camera.open(this.cameraID);
                } catch (Exception x) {
                    PApplet.println("Failed to open camera for camera ID: " + this.cameraID + ":" + x.getMessage());
                    return false;
                }
            }
            Camera.Parameters cameraParameters = this.camera.getParameters();
            for (Integer i : cameraParameters.getSupportedPreviewFormats()) {
                if (i.intValue() == 4) {
                    this.isRGBPreviewSupported = true;
                }
                PApplet.println("\t" + i);
            }
            if (this.isRGBPreviewSupported) {
                cameraParameters.setPreviewFormat(4);
            }
            PApplet.println("default imageformat:" + cameraParameters.getPreviewFormat());
            List<String> flashmodes = cameraParameters.getSupportedFlashModes();
            if (flashmodes == null || flashmodes.size() <= 0) {
                PApplet.println("No flash support.");
            } else {
                for (String s : flashmodes) {
                    PApplet.println("supported flashmode: " + s);
                }
                if (this.enableFlash) {
                    cameraParameters.setFlashMode("torch");
                } else {
                    cameraParameters.setFlashMode("off");
                }
            }
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(this.cameraID, info);
            int degrees = 0;
            switch (this.parent.getActivity().getWindowManager().getDefaultDisplay().getRotation()) {
                case 0:
                    degrees = 0;
                    break;
                case 1:
                    degrees = 90;
                    break;
                case 2:
                    degrees = 180;
                    break;
                case 3:
                    degrees = 270;
                    break;
            }
            int cameraRotationOffset = 0;
            switch (info.orientation) {
                case 0:
                    cameraRotationOffset = 0;
                    break;
                case 1:
                    cameraRotationOffset = 90;
                    break;
                case 2:
                    cameraRotationOffset = 180;
                    break;
                case 3:
                    cameraRotationOffset = 270;
                    break;
            }
            if (info.facing == 1) {
                this.requestedPortraitImage = true;
                result = (360 - ((cameraRotationOffset + degrees) % 360)) % 360;
            } else {
                result = ((cameraRotationOffset - degrees) + 360) % 360;
            }
            this.camera.setDisplayOrientation(result);
            PApplet.println("Rotation reported: " + degrees);
            PApplet.println("camera: setting display orientation to: " + result + " degrees");
            this.camera.setDisplayOrientation(result);
            this.camera.setParameters(cameraParameters);
            this.camera.setPreviewCallback(this.previewcallback);
            determineCameraParameters();
            try {
                this.parent.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        int[] textures = new int[1];
                        GLES20.glGenTextures(1, textures, 0);
                        GLES20.glBindTexture(3553, textures[0]);
                        int texture_id = textures[0];
                        KetaiCamera.this.mTexture = new SurfaceTexture(texture_id);
                        try {
                            KetaiCamera.this.camera.setPreviewTexture(KetaiCamera.this.mTexture);
                        } catch (IOException iox) {
                            PApplet.println("Something bad happened trying set the texture when trying to open the preview: " + iox.getMessage());
                        }
                    }
                });
                this.camera.startPreview();
            } catch (NoClassDefFoundError x2) {
                this.camera.startPreview();
                PApplet.println("Something bad happened trying to open the preview: " + x2.getMessage());
            }
            this.isStarted = true;
            PApplet.println("Using preview format: " + this.camera.getParameters().getPreviewFormat());
            PApplet.println("Preview size: " + this.frameWidth + "x" + this.frameHeight + "," + this.cameraFPS);
            PApplet.println("Photo size: " + this.photoWidth + "x" + this.photoHeight);
            return true;
        } catch (RuntimeException x3) {
            x3.printStackTrace();
            if (this.camera != null) {
                this.camera.release();
            }
            PApplet.println("Exception caught while trying to connect to camera service.  Please check your sketch permissions or that another application is not using the camera.");
            return false;
        }
    }

    public boolean isFlashEnabled() {
        return this.enableFlash;
    }

    public boolean savePhoto() {
        if (this.camera == null || !isStarted()) {
            return false;
        }
        this.savePhotoPath = "";
        return savePhoto(this.savePhotoPath);
    }

    public boolean savePhoto(String _filename) {
        String filename;
        if (_filename.startsWith(File.separator)) {
            this.savePhotoPath = _filename;
        } else {
            if (_filename.equalsIgnoreCase("")) {
                filename = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
            } else {
                filename = _filename;
            }
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), this.SAVE_DIR);
            if (mediaStorageDir.exists() || mediaStorageDir.mkdirs()) {
                this.savePhotoPath = String.valueOf(mediaStorageDir.getAbsolutePath()) + File.separator + filename;
            } else {
                PApplet.println("failed to create directory to save photo: " + mediaStorageDir.getAbsolutePath());
                return false;
            }
        }
        PApplet.println("Calculated photo path: " + this.savePhotoPath);
        try {
            FileOutputStream outStream = new FileOutputStream(this.savePhotoPath);
            outStream.write(1);
            outStream.close();
            if (!new File(this.savePhotoPath).delete()) {
                PApplet.println("Failed to remove temp photoFile while testing permissions..oops");
            }
            if (this.camera != null && isStarted()) {
                this.camera.takePicture((Camera.ShutterCallback) null, (Camera.PictureCallback) null, this.jpegCallback);
            }
            return true;
        } catch (FileNotFoundException x) {
            PApplet.println("Failed to save photo to " + this.savePhotoPath + "\n" + x.getMessage());
            return false;
        } catch (IOException e) {
            PApplet.println("Failed to save photo to " + this.savePhotoPath + "\n" + e.getMessage());
            return false;
        }
    }

    public void resume() {
        if (this.camera != null) {
            this.camera = Camera.open(this.cameraID);
            if (!this.isStarted && this.requestedStart) {
                start();
            }
        }
    }

    public synchronized void read() {
        if (this.pixels.length != this.frameWidth * this.frameHeight) {
            this.pixels = new int[(this.frameWidth * this.frameHeight)];
        }
        synchronized (this.pixels) {
            System.arraycopy(this.myPixels, 0, this.pixels, 0, this.frameWidth * this.frameHeight);
            this.available = false;
            updatePixels();
        }
    }

    public boolean isStarted() {
        return this.isStarted;
    }

    public void addToMediaLibrary(String _file) {
        MediaScannerConnection.scanFile(this.parent.getActivity().getApplicationContext(), new String[]{_file}, (String[]) null, this.myScannerCallback);
    }

    public void pause() {
        if (this.camera != null && this.isStarted) {
            this.isStarted = false;
            this.camera.stopPreview();
            this.camera.setPreviewCallback((Camera.PreviewCallback) null);
            this.camera.release();
            this.camera = null;
        }
        this.isStarted = false;
    }

    public void stop() {
        PApplet.println("Stopping Camera...");
        this.requestedStart = false;
        if (this.camera != null && this.isStarted) {
            this.isStarted = false;
            this.camera.stopPreview();
            this.camera.setPreviewCallback((Camera.PreviewCallback) null);
            this.camera.release();
            this.camera = null;
        }
    }

    public void dispose() {
        stop();
    }

    public void decodeYUV420SP(byte[] yuv420sp) {
        int frameSize = this.width * this.height;
        int yp = 0;
        for (int j = 0; j < this.height; j++) {
            int uvp = frameSize + ((j >> 1) * this.width);
            int u = 0;
            int v = 0;
            int i = 0;
            while (i < this.width) {
                int y = (yuv420sp[yp] & 255) - 16;
                if (y < 0) {
                    y = 0;
                }
                if ((i & 1) == 0) {
                    int uvp2 = uvp + 1;
                    v = (yuv420sp[uvp] & 255) - 128;
                    uvp = uvp2 + 1;
                    u = (yuv420sp[uvp2] & 255) - 128;
                }
                int y1192 = y * 1192;
                int r = y1192 + (v * 1634);
                int g = (y1192 - (v * 833)) - (u * 400);
                int b = y1192 + (u * 2066);
                if (r < 0) {
                    r = 0;
                } else if (r > 262143) {
                    r = 262143;
                }
                if (g < 0) {
                    g = 0;
                } else if (g > 262143) {
                    g = 262143;
                }
                if (b < 0) {
                    b = 0;
                } else if (b > 262143) {
                    b = 262143;
                }
                this.myPixels[yp] = -16777216 | ((r << 6) & PImage.RED_MASK) | ((g >> 2) & 65280) | ((b >> 10) & 255);
                i++;
                yp++;
            }
        }
    }

    public int getNumberOfCameras() {
        return Camera.getNumberOfCameras();
    }

    public Collection<? extends String> list() {
        String facing;
        Vector<String> list = new Vector<>();
        int count = Camera.getNumberOfCameras();
        for (int i = 0; i < count; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == 0) {
                facing = "backfacing";
            } else {
                facing = "frontfacing";
            }
            list.add("camera id [" + i + "] facing:" + facing);
            PApplet.println("camera id[" + i + "] facing:" + facing);
        }
        return list;
    }

    private void determineCameraParameters() {
        if (this.camera != null) {
            PApplet.println("Requested camera parameters as (w,h,fps):" + this.frameWidth + "," + this.frameHeight + "," + this.cameraFPS);
            Camera.Parameters cameraParameters = this.camera.getParameters();
            boolean foundSupportedSize = false;
            Camera.Size nearestRequestedSize = null;
            for (Camera.Size s : cameraParameters.getSupportedPreviewSizes()) {
                PApplet.println("Checking supported preview size:" + s.width + "," + s.height);
                if (nearestRequestedSize == null) {
                    nearestRequestedSize = s;
                }
                if (!foundSupportedSize) {
                    if (s.width == this.frameWidth && s.height == this.frameHeight) {
                        PApplet.println("Found matching camera size");
                        nearestRequestedSize = s;
                        foundSupportedSize = true;
                    } else {
                        if (Math.abs((this.frameWidth * this.frameHeight) - (s.height * s.width)) < Math.abs((this.frameWidth * this.frameHeight) - (nearestRequestedSize.height * nearestRequestedSize.width))) {
                            nearestRequestedSize = s;
                        }
                    }
                }
            }
            if (nearestRequestedSize != null) {
                this.frameWidth = nearestRequestedSize.width;
                this.frameHeight = nearestRequestedSize.height;
            }
            cameraParameters.setPreviewSize(this.frameWidth, this.frameHeight);
            boolean foundSupportedSize2 = false;
            Camera.Size nearestRequestedSize2 = null;
            for (Camera.Size s2 : cameraParameters.getSupportedPictureSizes()) {
                if (!foundSupportedSize2) {
                    if (s2.width == this.photoWidth && s2.height == this.photoHeight) {
                        nearestRequestedSize2 = s2;
                        foundSupportedSize2 = true;
                    } else if (this.photoWidth <= s2.width) {
                        nearestRequestedSize2 = s2;
                    }
                }
            }
            if (nearestRequestedSize2 != null) {
                this.photoWidth = nearestRequestedSize2.width;
                this.photoHeight = nearestRequestedSize2.height;
            }
            cameraParameters.setPictureSize(this.photoWidth, this.photoHeight);
            int nearestFPS = 0;
            for (Integer intValue : cameraParameters.getSupportedPreviewFrameRates()) {
                int r = intValue.intValue();
                PApplet.println("Supported preview FPS: " + r);
                if (nearestFPS == 0) {
                    nearestFPS = r;
                }
                if (Math.abs(this.cameraFPS - r) > Math.abs(this.cameraFPS - nearestFPS)) {
                    nearestFPS = r;
                }
            }
            PApplet.println("calculated preview FPS: " + nearestFPS);
            cameraParameters.setPreviewFrameRate(nearestFPS);
            this.camera.setParameters(cameraParameters);
            Camera.Parameters cameraParameters2 = this.camera.getParameters();
            this.frameHeight = cameraParameters2.getPreviewSize().height;
            this.frameWidth = cameraParameters2.getPreviewSize().width;
            if (this.cameraFPS == cameraParameters2.getPreviewFrameRate()) {
                this.cameraFPS = cameraParameters2.getPreviewFrameRate();
            }
            PApplet.println("Calculated camera parameters as (w,h,fps):" + this.frameWidth + "," + this.frameHeight + "," + this.cameraFPS);
            if (cameraParameters2.getMaxNumDetectedFaces() > 0) {
                PApplet.println("Face detection supported!");
                this.supportsFaceDetection = true;
            }
            loadPixels();
            resize(this.frameWidth, this.frameHeight);
        }
    }

    public void onFrameAvailable(SurfaceTexture arg0) {
        PApplet.print(".");
    }

    public void register(Object o) {
        this.callbackdelegate = o;
        determineObjectIntentions(o);
    }
}
