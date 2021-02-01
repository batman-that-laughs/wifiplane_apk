package processing.opengl;

import processing.core.PGraphics;
import processing.core.PMatrix;
import processing.core.PShape;
import processing.core.PShapeOBJ;

public class PGraphics3D extends PGraphicsOpenGL {
    protected static boolean isSupportedExtension(String str) {
        return str.equals("obj");
    }

    protected static PShape loadShapeImpl(PGraphics pGraphics, String str, String str2) {
        if (!str2.equals("obj")) {
            return null;
        }
        PShapeOBJ pShapeOBJ = new PShapeOBJ(pGraphics.parent, str);
        int i = pGraphics.textureMode;
        pGraphics.textureMode = 1;
        PShapeOpenGL createShape = PShapeOpenGL.createShape((PGraphicsOpenGL) pGraphics, pShapeOBJ);
        pGraphics.textureMode = i;
        return createShape;
    }

    /* access modifiers changed from: protected */
    public void begin2D() {
        pushProjection();
        ortho(((float) (-this.width)) / 2.0f, ((float) this.width) / 2.0f, ((float) (-this.height)) / 2.0f, ((float) this.height) / 2.0f);
        pushMatrix();
        this.modelview.reset();
        this.modelview.translate(-(((float) this.width) / 2.0f), -(((float) this.height) / 2.0f));
        this.modelviewInv.set((PMatrix) this.modelview);
        this.modelviewInv.invert();
        this.camera.set((PMatrix) this.modelview);
        this.cameraInv.set((PMatrix) this.modelviewInv);
        updateProjmodelview();
    }

    /* access modifiers changed from: protected */
    public void defaultCamera() {
        camera();
    }

    /* access modifiers changed from: protected */
    public void defaultPerspective() {
        perspective();
    }

    /* access modifiers changed from: protected */
    public void end2D() {
        popMatrix();
        popProjection();
    }

    public boolean is2D() {
        return false;
    }

    public boolean is3D() {
        return true;
    }
}
