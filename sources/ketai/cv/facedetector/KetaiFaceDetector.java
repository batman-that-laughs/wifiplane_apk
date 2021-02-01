package ketai.cv.facedetector;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.FaceDetector;
import java.util.ArrayList;
import ketai.camera.KetaiCamera;
import processing.core.PImage;

public class KetaiFaceDetector {
    public static KetaiSimpleFace[] findFaces(PImage _image, int MAX_FACES) {
        ArrayList<KetaiSimpleFace> foundFaces = new ArrayList<>();
        int numberOfFaces = 0;
        _image.loadPixels();
        Bitmap _bitmap = Bitmap.createBitmap(_image.pixels, _image.width, _image.height, Bitmap.Config.RGB_565);
        if (_bitmap != null) {
            FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
            numberOfFaces = new FaceDetector(_image.width, _image.height, MAX_FACES).findFaces(_bitmap, faces);
            for (int i = 0; i < numberOfFaces; i++) {
                foundFaces.add(new KetaiSimpleFace(faces[i]));
            }
        }
        KetaiSimpleFace[] f = new KetaiSimpleFace[numberOfFaces];
        for (int i2 = 0; i2 < numberOfFaces; i2++) {
            f[i2] = foundFaces.get(i2);
        }
        return f;
    }

    public static KetaiSimpleFace[] findFaces(KetaiCamera _camera, int MAX_FACES) {
        KetaiSimpleFace[] f;
        ArrayList<KetaiSimpleFace> foundFaces = new ArrayList<>();
        int numberOfFaces = 0;
        _camera.loadPixels();
        Bitmap _bitmap = Bitmap.createBitmap(_camera.pixels, _camera.width, _camera.height, Bitmap.Config.RGB_565);
        if (_camera.requestedPortraitImage) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90.0f);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(_bitmap, _camera.width, _camera.height, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            if (rotatedBitmap != null) {
                FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
                numberOfFaces = new FaceDetector(rotatedBitmap.getWidth(), rotatedBitmap.getHeight(), MAX_FACES).findFaces(rotatedBitmap, faces);
                for (int i = 0; i < numberOfFaces; i++) {
                    foundFaces.add(new KetaiSimpleFace(faces[i], _camera.width, _camera.height));
                }
            }
            f = new KetaiSimpleFace[numberOfFaces];
            for (int i2 = 0; i2 < numberOfFaces; i2++) {
                f[i2] = foundFaces.get(i2);
            }
        } else {
            if (_bitmap != null) {
                FaceDetector.Face[] faces2 = new FaceDetector.Face[MAX_FACES];
                numberOfFaces = new FaceDetector(_camera.width, _camera.height, MAX_FACES).findFaces(_bitmap, faces2);
                for (int i3 = 0; i3 < numberOfFaces; i3++) {
                    foundFaces.add(new KetaiSimpleFace(faces2[i3]));
                }
            }
            f = new KetaiSimpleFace[numberOfFaces];
            for (int i4 = 0; i4 < numberOfFaces; i4++) {
                f[i4] = foundFaces.get(i4);
            }
        }
        return f;
    }

    public static KetaiSimpleFace[] findFaces(KetaiCamera _image) {
        return findFaces(_image, 5);
    }
}
