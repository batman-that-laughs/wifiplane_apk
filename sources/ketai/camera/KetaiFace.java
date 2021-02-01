package ketai.camera;

import android.hardware.Camera;
import processing.core.PApplet;
import processing.core.PVector;

public class KetaiFace extends Camera.Face {
    public PVector center;
    public int height;
    public int id;
    public PVector leftEye;
    public PVector mouth;
    public PVector rightEye;
    public int score;
    public int width;

    public KetaiFace(Camera.Face f, int frameWidth, int frameHeight) {
        this.leftEye = new PVector(PApplet.map((float) f.leftEye.x, -1000.0f, 1000.0f, 0.0f, (float) frameWidth), PApplet.map((float) f.leftEye.y, -1000.0f, 1000.0f, 0.0f, (float) frameHeight));
        this.rightEye = new PVector(PApplet.map((float) f.rightEye.x, -1000.0f, 1000.0f, 0.0f, (float) frameWidth), PApplet.map((float) f.rightEye.y, -1000.0f, 1000.0f, 0.0f, (float) frameHeight));
        this.mouth = new PVector(PApplet.map((float) f.mouth.x, -1000.0f, 1000.0f, 0.0f, (float) frameWidth), PApplet.map((float) f.mouth.y, -1000.0f, 1000.0f, 0.0f, (float) frameHeight));
        this.id = f.id;
        this.score = f.score;
        this.center = new PVector(PApplet.map(f.rect.exactCenterX(), -1000.0f, 1000.0f, 0.0f, (float) frameWidth), PApplet.map(f.rect.exactCenterY(), -1000.0f, 1000.0f, 0.0f, (float) frameHeight));
        this.width = f.rect.width();
        this.height = f.rect.height();
    }
}
