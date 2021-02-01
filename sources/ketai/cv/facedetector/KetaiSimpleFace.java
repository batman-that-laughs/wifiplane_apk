package ketai.cv.facedetector;

import android.graphics.PointF;
import android.media.FaceDetector;
import processing.core.PApplet;
import processing.core.PVector;

public class KetaiSimpleFace {
    public float confidence;
    public float distance;
    public PVector location;

    public KetaiSimpleFace(FaceDetector.Face f) {
        PointF p = new PointF();
        f.getMidPoint(p);
        this.location = new PVector(p.x, p.y);
        this.distance = f.eyesDistance();
    }

    public KetaiSimpleFace(FaceDetector.Face f, int landscapeWidth, int landscapeHeight) {
        PointF p = new PointF();
        f.getMidPoint(p);
        this.location = new PVector(p.y, PApplet.map(p.x, 0.0f, (float) landscapeHeight, (float) landscapeHeight, 0.0f));
        this.distance = f.eyesDistance();
    }
}
