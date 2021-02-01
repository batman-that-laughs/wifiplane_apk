package processing.opengl;

import processing.core.PGraphics;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.core.PShapeSVG;

public class PGraphics2D extends PGraphicsOpenGL {
    protected static boolean isSupportedExtension(String str) {
        return str.equals("svg") || str.equals("svgz");
    }

    protected static PShape loadShapeImpl(PGraphics pGraphics, String str, String str2) {
        if (!str2.equals("svg") && !str2.equals("svgz")) {
            return null;
        }
        return PShapeOpenGL.createShape((PGraphicsOpenGL) pGraphics, new PShapeSVG(pGraphics.parent.loadXML(str)));
    }

    public void ambientLight(float f, float f2, float f3) {
        showMethodWarning("ambientLight");
    }

    public void ambientLight(float f, float f2, float f3, float f4, float f5, float f6) {
        showMethodWarning("ambientLight");
    }

    public void applyMatrix(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        showVariationWarning("applyMatrix");
    }

    public void applyMatrix(PMatrix3D pMatrix3D) {
        showVariationWarning("applyMatrix");
    }

    /* access modifiers changed from: protected */
    public void begin2D() {
        pushProjection();
        defaultPerspective();
        pushMatrix();
        defaultCamera();
    }

    public void beginCamera() {
        showMethodWarning("beginCamera");
    }

    public void bezierVertex(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        showDepthWarningXYZ("bezierVertex");
    }

    public void box(float f, float f2, float f3) {
        showMethodWarning("box");
    }

    public void camera() {
        showMethodWarning("camera");
    }

    public void camera(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        showMethodWarning("camera");
    }

    public void curveVertex(float f, float f2, float f3) {
        showDepthWarningXYZ("curveVertex");
    }

    /* access modifiers changed from: protected */
    public void defaultCamera() {
        this.eyeDist = 1.0f;
        resetMatrix();
    }

    /* access modifiers changed from: protected */
    public void defaultPerspective() {
        super.ortho(0.0f, (float) this.width, (float) (-this.height), 0.0f, -1.0f, 1.0f);
    }

    public void directionalLight(float f, float f2, float f3, float f4, float f5, float f6) {
        showMethodWarning("directionalLight");
    }

    /* access modifiers changed from: protected */
    public void end2D() {
        popMatrix();
        popProjection();
    }

    public void endCamera() {
        showMethodWarning("endCamera");
    }

    public void frustum(float f, float f2, float f3, float f4, float f5, float f6) {
        showMethodWarning("frustum");
    }

    public PMatrix3D getMatrix(PMatrix3D pMatrix3D) {
        showVariationWarning("getMatrix");
        return pMatrix3D;
    }

    public void hint(int i) {
        if (i == 7) {
            showWarning("Strokes cannot be perspective-corrected in 2D.");
        } else {
            super.hint(i);
        }
    }

    public boolean is2D() {
        return true;
    }

    public boolean is3D() {
        return false;
    }

    public void lightFalloff(float f, float f2, float f3) {
        showMethodWarning("lightFalloff");
    }

    public void lightSpecular(float f, float f2, float f3) {
        showMethodWarning("lightSpecular");
    }

    public void lights() {
        showMethodWarning("lights");
    }

    public void noLights() {
        showMethodWarning("noLights");
    }

    public void ortho() {
        showMethodWarning("ortho");
    }

    public void ortho(float f, float f2, float f3, float f4) {
        showMethodWarning("ortho");
    }

    public void ortho(float f, float f2, float f3, float f4, float f5, float f6) {
        showMethodWarning("ortho");
    }

    public void perspective() {
        showMethodWarning("perspective");
    }

    public void perspective(float f, float f2, float f3, float f4) {
        showMethodWarning("perspective");
    }

    public void pointLight(float f, float f2, float f3, float f4, float f5, float f6) {
        showMethodWarning("pointLight");
    }

    public void quadraticVertex(float f, float f2, float f3, float f4, float f5, float f6) {
        showDepthWarningXYZ("quadVertex");
    }

    public void rotate(float f, float f2, float f3, float f4) {
        showVariationWarning("rotate");
    }

    public void rotateX(float f) {
        showDepthWarning("rotateX");
    }

    public void rotateY(float f) {
        showDepthWarning("rotateY");
    }

    public void rotateZ(float f) {
        showDepthWarning("rotateZ");
    }

    public void scale(float f, float f2, float f3) {
        showDepthWarningXYZ("scale");
    }

    public float screenX(float f, float f2, float f3) {
        showDepthWarningXYZ("screenX");
        return 0.0f;
    }

    public float screenY(float f, float f2, float f3) {
        showDepthWarningXYZ("screenY");
        return 0.0f;
    }

    public float screenZ(float f, float f2, float f3) {
        showDepthWarningXYZ("screenZ");
        return 0.0f;
    }

    public void setMatrix(PMatrix3D pMatrix3D) {
        showVariationWarning("setMatrix");
    }

    public void shape(PShape pShape) {
        if (pShape.is2D()) {
            super.shape(pShape);
        } else {
            showWarning("The shape object is not 2D, cannot be displayed with this renderer");
        }
    }

    public void shape(PShape pShape, float f, float f2) {
        if (pShape.is2D()) {
            super.shape(pShape, f, f2);
        } else {
            showWarning("The shape object is not 2D, cannot be displayed with this renderer");
        }
    }

    public void shape(PShape pShape, float f, float f2, float f3) {
        showDepthWarningXYZ("shape");
    }

    public void shape(PShape pShape, float f, float f2, float f3, float f4) {
        if (pShape.is2D()) {
            super.shape(pShape, f, f2, f3, f4);
        } else {
            showWarning("The shape object is not 2D, cannot be displayed with this renderer");
        }
    }

    public void shape(PShape pShape, float f, float f2, float f3, float f4, float f5, float f6) {
        showDepthWarningXYZ("shape");
    }

    public void sphere(float f) {
        showMethodWarning("sphere");
    }

    public void spotLight(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        showMethodWarning("spotLight");
    }

    public void translate(float f, float f2, float f3) {
        showDepthWarningXYZ("translate");
    }

    public void vertex(float f, float f2, float f3) {
        showDepthWarningXYZ("vertex");
    }

    public void vertex(float f, float f2, float f3, float f4, float f5) {
        showDepthWarningXYZ("vertex");
    }
}
