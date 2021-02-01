package processing.core;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class PShape implements PConstants {
    public static final int GEOMETRY = 3;
    public static final String INSIDE_BEGIN_END_ERROR = "%1$s can only be called outside beginShape() and endShape()";
    public static final String NOT_A_SIMPLE_VERTEX = "%1$s can not be called on quadratic or bezier vertices";
    public static final String NO_SUCH_VERTEX_ERROR = "%1$s vertex index does not exist";
    public static final String NO_VERTICES_ERROR = "getVertexCount() only works with PATH or GEOMETRY shapes";
    public static final String OUTSIDE_BEGIN_END_ERROR = "%1$s can only be called between beginShape() and endShape()";
    public static final int PATH = 2;
    public static final int PRIMITIVE = 1;
    protected int ambientColor;
    protected float calcA;
    protected int calcAi;
    protected boolean calcAlpha;
    protected float calcB;
    protected int calcBi;
    protected int calcColor;
    protected float calcG;
    protected int calcGi;
    protected float calcR;
    protected int calcRi;
    protected int childCount;
    protected PShape[] children;
    protected boolean close;
    public int colorMode;
    public float colorModeA;
    boolean colorModeDefault;
    boolean colorModeScale;
    public float colorModeX;
    public float colorModeY;
    public float colorModeZ;
    public float depth;
    protected int ellipseMode;
    protected int emissiveColor;
    protected int family;
    protected boolean fill;
    protected int fillColor;
    PGraphics g;
    public float height;
    protected PImage image;
    protected boolean is3D;
    protected int kind;
    protected PMatrix matrix;
    protected String name;
    protected Map<String, PShape> nameTable;
    protected boolean openContour;
    protected boolean openShape;
    protected float[] params;
    protected PShape parent;
    protected boolean perVertexStyles;
    protected int rectMode;
    protected boolean setAmbient;
    protected float shininess;
    protected int specularColor;
    protected int sphereDetailU;
    protected int sphereDetailV;
    protected boolean stroke;
    protected int strokeCap;
    protected int strokeColor;
    protected int strokeJoin;
    protected float strokeWeight;
    protected boolean style;
    protected int textureMode;
    protected boolean tint;
    protected int tintColor;
    protected int vertexCodeCount;
    protected int[] vertexCodes;
    protected int vertexCount;
    protected float[][] vertices;
    protected boolean visible;
    public float width;

    public PShape() {
        this.visible = true;
        this.openShape = false;
        this.openContour = false;
        this.style = true;
        this.is3D = false;
        this.perVertexStyles = false;
        this.family = 0;
    }

    public PShape(int i) {
        this.visible = true;
        this.openShape = false;
        this.openContour = false;
        this.style = true;
        this.is3D = false;
        this.perVertexStyles = false;
        this.family = i;
    }

    public PShape(PGraphics pGraphics, int i) {
        this.visible = true;
        this.openShape = false;
        this.openContour = false;
        this.style = true;
        this.is3D = false;
        this.perVertexStyles = false;
        this.g = pGraphics;
        this.family = i;
        this.textureMode = pGraphics.textureMode;
        colorMode(pGraphics.colorMode, pGraphics.colorModeX, pGraphics.colorModeY, pGraphics.colorModeZ, pGraphics.colorModeA);
        this.fill = pGraphics.fill;
        this.fillColor = pGraphics.fillColor;
        this.stroke = pGraphics.stroke;
        this.strokeColor = pGraphics.strokeColor;
        this.strokeWeight = pGraphics.strokeWeight;
        this.strokeCap = pGraphics.strokeCap;
        this.strokeJoin = pGraphics.strokeJoin;
        this.tint = pGraphics.tint;
        this.tintColor = pGraphics.tintColor;
        this.setAmbient = pGraphics.setAmbient;
        this.ambientColor = pGraphics.ambientColor;
        this.specularColor = pGraphics.specularColor;
        this.emissiveColor = pGraphics.emissiveColor;
        this.shininess = pGraphics.shininess;
        this.sphereDetailU = pGraphics.sphereDetailU;
        this.sphereDetailV = pGraphics.sphereDetailV;
        this.rectMode = 0;
        this.ellipseMode = 0;
        if (i == 0) {
        }
    }

    public PShape(PGraphics pGraphics, int i, float... fArr) {
        this(pGraphics, 1);
        setKind(i);
        setParams(fArr);
    }

    protected static void copyGeometry(PShape pShape, PShape pShape2) {
        pShape2.beginShape(pShape.getKind());
        copyMatrix(pShape, pShape2);
        copyStyles(pShape, pShape2);
        copyImage(pShape, pShape2);
        if (pShape.style) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= pShape.vertexCount) {
                    break;
                }
                float[] fArr = pShape.vertices[i2];
                pShape2.fill((((int) (fArr[6] * 255.0f)) << 24) | (((int) (fArr[3] * 255.0f)) << 16) | (((int) (fArr[4] * 255.0f)) << 8) | ((int) (fArr[5] * 255.0f)));
                if (0.0f < PApplet.dist(fArr[9], fArr[10], fArr[11], 0.0f, 0.0f, 0.0f)) {
                    pShape2.normal(fArr[9], fArr[10], fArr[11]);
                }
                pShape2.vertex(fArr[0], fArr[1], fArr[2], fArr[7], fArr[8]);
                i = i2 + 1;
            }
        } else {
            for (int i3 = 0; i3 < pShape.vertexCount; i3++) {
                float[] fArr2 = pShape.vertices[i3];
                if (fArr2[2] == 0.0f) {
                    pShape2.vertex(fArr2[0], fArr2[1]);
                } else {
                    pShape2.vertex(fArr2[0], fArr2[1], fArr2[2]);
                }
            }
        }
        pShape2.endShape();
    }

    protected static void copyGroup(PApplet pApplet, PShape pShape, PShape pShape2) {
        copyMatrix(pShape, pShape2);
        copyStyles(pShape, pShape2);
        copyImage(pShape, pShape2);
        for (int i = 0; i < pShape.childCount; i++) {
            pShape2.addChild(createShape(pApplet, pShape.children[i]));
        }
    }

    protected static void copyImage(PShape pShape, PShape pShape2) {
        if (pShape.image != null) {
            pShape2.texture(pShape.image);
        }
    }

    protected static void copyMatrix(PShape pShape, PShape pShape2) {
        if (pShape.matrix != null) {
            pShape2.applyMatrix(pShape.matrix);
        }
    }

    protected static void copyPath(PShape pShape, PShape pShape2) {
        copyMatrix(pShape, pShape2);
        copyStyles(pShape, pShape2);
        copyImage(pShape, pShape2);
        pShape2.close = pShape.close;
        pShape2.setPath(pShape.vertexCount, pShape.vertices, pShape.vertexCodeCount, pShape.vertexCodes);
    }

    protected static void copyPrimitive(PShape pShape, PShape pShape2) {
        copyMatrix(pShape, pShape2);
        copyStyles(pShape, pShape2);
        copyImage(pShape, pShape2);
    }

    protected static void copyStyles(PShape pShape, PShape pShape2) {
        if (pShape.stroke) {
            pShape2.stroke = true;
            pShape2.strokeColor = pShape.strokeColor;
            pShape2.strokeWeight = pShape.strokeWeight;
            pShape2.strokeCap = pShape.strokeCap;
            pShape2.strokeJoin = pShape.strokeJoin;
        } else {
            pShape2.stroke = false;
        }
        if (pShape.fill) {
            pShape2.fill = true;
            pShape2.fillColor = pShape.fillColor;
            return;
        }
        pShape2.fill = false;
    }

    protected static PShape createShape(PApplet pApplet, PShape pShape) {
        PShape pShape2 = null;
        if (pShape.family == 0) {
            pShape2 = pApplet.createShape(0);
            copyGroup(pApplet, pShape, pShape2);
        } else if (pShape.family == 1) {
            pShape2 = pApplet.createShape(pShape.kind, pShape.params);
            copyPrimitive(pShape, pShape2);
        } else if (pShape.family == 3) {
            pShape2 = pApplet.createShape(pShape.kind);
            copyGeometry(pShape, pShape2);
        } else if (pShape.family == 2) {
            pShape2 = pApplet.createShape(2);
            copyPath(pShape, pShape2);
        }
        pShape2.setName(pShape.name);
        return pShape2;
    }

    public void addChild(PShape pShape) {
        if (this.children == null) {
            this.children = new PShape[1];
        }
        if (this.childCount == this.children.length) {
            this.children = (PShape[]) PApplet.expand((Object) this.children);
        }
        PShape[] pShapeArr = this.children;
        int i = this.childCount;
        this.childCount = i + 1;
        pShapeArr[i] = pShape;
        pShape.parent = this;
        if (pShape.getName() != null) {
            addName(pShape.getName(), pShape);
        }
    }

    public void addChild(PShape pShape, int i) {
        if (i < this.childCount) {
            if (this.childCount == this.children.length) {
                this.children = (PShape[]) PApplet.expand((Object) this.children);
            }
            for (int i2 = this.childCount - 1; i2 >= i; i2--) {
                this.children[i2 + 1] = this.children[i2];
            }
            this.childCount++;
            this.children[i] = pShape;
            pShape.parent = this;
            if (pShape.getName() != null) {
                addName(pShape.getName(), pShape);
            }
        }
    }

    public void addName(String str, PShape pShape) {
        if (this.parent != null) {
            this.parent.addName(str, pShape);
            return;
        }
        if (this.nameTable == null) {
            this.nameTable = new HashMap();
        }
        this.nameTable.put(str, pShape);
    }

    public void ambient(float f) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "ambient()");
            return;
        }
        this.setAmbient = true;
        colorCalc(f);
        this.ambientColor = this.calcColor;
    }

    public void ambient(float f, float f2, float f3) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "ambient()");
            return;
        }
        this.setAmbient = true;
        colorCalc(f, f2, f3);
        this.ambientColor = this.calcColor;
    }

    public void ambient(int i) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "ambient()");
            return;
        }
        this.setAmbient = true;
        colorCalc(i);
        this.ambientColor = this.calcColor;
    }

    public void applyMatrix(float f, float f2, float f3, float f4, float f5, float f6) {
        checkMatrix(2);
        this.matrix.apply(f, f2, f3, f4, f5, f6);
    }

    public void applyMatrix(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        checkMatrix(3);
        this.matrix.apply(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16);
    }

    public void applyMatrix(PMatrix2D pMatrix2D) {
        applyMatrix(pMatrix2D.m00, pMatrix2D.m01, 0.0f, pMatrix2D.m02, pMatrix2D.m10, pMatrix2D.m11, 0.0f, pMatrix2D.m12, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void applyMatrix(PMatrix3D pMatrix3D) {
        applyMatrix(pMatrix3D.m00, pMatrix3D.m01, pMatrix3D.m02, pMatrix3D.m03, pMatrix3D.m10, pMatrix3D.m11, pMatrix3D.m12, pMatrix3D.m13, pMatrix3D.m20, pMatrix3D.m21, pMatrix3D.m22, pMatrix3D.m23, pMatrix3D.m30, pMatrix3D.m31, pMatrix3D.m32, pMatrix3D.m33);
    }

    public void applyMatrix(PMatrix pMatrix) {
        if (pMatrix instanceof PMatrix2D) {
            applyMatrix((PMatrix2D) pMatrix);
        } else if (pMatrix instanceof PMatrix3D) {
            applyMatrix((PMatrix3D) pMatrix);
        }
    }

    public void attrib(String str, float... fArr) {
    }

    public void attrib(String str, int... iArr) {
    }

    public void attrib(String str, boolean... zArr) {
    }

    public void attribColor(String str, int i) {
    }

    public void attribNormal(String str, float f, float f2, float f3) {
    }

    public void attribPosition(String str, float f, float f2, float f3) {
    }

    public void beginContour() {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "beginContour()");
        } else if (this.family == 0) {
            PGraphics.showWarning("Cannot begin contour in GROUP shapes");
        } else if (this.openContour) {
            PGraphics.showWarning("Already called beginContour().");
        } else {
            this.openContour = true;
            beginContourImpl();
        }
    }

    /* access modifiers changed from: protected */
    public void beginContourImpl() {
        if (this.vertexCodes == null) {
            this.vertexCodes = new int[10];
        } else if (this.vertexCodes.length == this.vertexCodeCount) {
            this.vertexCodes = PApplet.expand(this.vertexCodes);
        }
        int[] iArr = this.vertexCodes;
        int i = this.vertexCodeCount;
        this.vertexCodeCount = i + 1;
        iArr[i] = 4;
    }

    public void beginShape() {
        beginShape(20);
    }

    public void beginShape(int i) {
        this.kind = i;
        this.openShape = true;
    }

    public void bezierDetail(int i) {
    }

    public void bezierVertex(float f, float f2, float f3, float f4, float f5, float f6) {
        if (this.vertices == null) {
            this.vertices = new float[10][];
        } else if (this.vertexCount + 2 >= this.vertices.length) {
            this.vertices = (float[][]) PApplet.expand((Object) this.vertices);
        }
        float[][] fArr = this.vertices;
        int i = this.vertexCount;
        this.vertexCount = i + 1;
        fArr[i] = new float[]{f, f2};
        float[][] fArr2 = this.vertices;
        int i2 = this.vertexCount;
        this.vertexCount = i2 + 1;
        fArr2[i2] = new float[]{f3, f4};
        float[][] fArr3 = this.vertices;
        int i3 = this.vertexCount;
        this.vertexCount = i3 + 1;
        fArr3[i3] = new float[]{f5, f6};
        if (this.vertexCodes.length == this.vertexCodeCount) {
            this.vertexCodes = PApplet.expand(this.vertexCodes);
        }
        int[] iArr = this.vertexCodes;
        int i4 = this.vertexCodeCount;
        this.vertexCodeCount = i4 + 1;
        iArr[i4] = 1;
        if (f5 > this.width) {
            this.width = f5;
        }
        if (f6 > this.height) {
            this.height = f6;
        }
    }

    public void bezierVertex(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
    }

    /* access modifiers changed from: protected */
    public void checkMatrix(int i) {
        if (this.matrix == null) {
            if (i == 2) {
                this.matrix = new PMatrix2D();
            } else {
                this.matrix = new PMatrix3D();
            }
        } else if (i == 3 && (this.matrix instanceof PMatrix2D)) {
            this.matrix = new PMatrix3D(this.matrix);
        }
    }

    /* access modifiers changed from: protected */
    public void colorCalc(float f) {
        colorCalc(f, this.colorModeA);
    }

    /* access modifiers changed from: protected */
    public void colorCalc(float f, float f2) {
        float f3 = 0.0f;
        if (f > this.colorModeX) {
            f = this.colorModeX;
        }
        float f4 = f2 > this.colorModeA ? this.colorModeA : f2;
        float f5 = f < 0.0f ? 0.0f : f;
        if (f4 >= 0.0f) {
            f3 = f4;
        }
        this.calcR = this.colorModeScale ? f5 / this.colorModeX : f5;
        this.calcG = this.calcR;
        this.calcB = this.calcR;
        if (this.colorModeScale) {
            f3 /= this.colorModeA;
        }
        this.calcA = f3;
        this.calcRi = (int) (this.calcR * 255.0f);
        this.calcGi = (int) (this.calcG * 255.0f);
        this.calcBi = (int) (this.calcB * 255.0f);
        this.calcAi = (int) (this.calcA * 255.0f);
        this.calcColor = (this.calcAi << 24) | (this.calcRi << 16) | (this.calcGi << 8) | this.calcBi;
        this.calcAlpha = this.calcAi != 255;
    }

    /* access modifiers changed from: protected */
    public void colorCalc(float f, float f2, float f3) {
        colorCalc(f, f2, f3, this.colorModeA);
    }

    /* access modifiers changed from: protected */
    public void colorCalc(float f, float f2, float f3, float f4) {
        if (f > this.colorModeX) {
            f = this.colorModeX;
        }
        if (f2 > this.colorModeY) {
            f2 = this.colorModeY;
        }
        if (f3 > this.colorModeZ) {
            f3 = this.colorModeZ;
        }
        float f5 = f4 > this.colorModeA ? this.colorModeA : f4;
        if (f < 0.0f) {
            f = 0.0f;
        }
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        if (f5 < 0.0f) {
            f5 = 0.0f;
        }
        switch (this.colorMode) {
            case 1:
                if (!this.colorModeScale) {
                    this.calcR = f;
                    this.calcG = f2;
                    this.calcB = f3;
                    this.calcA = f5;
                    break;
                } else {
                    this.calcR = f / this.colorModeX;
                    this.calcG = f2 / this.colorModeY;
                    this.calcB = f3 / this.colorModeZ;
                    this.calcA = f5 / this.colorModeA;
                    break;
                }
            case 3:
                float f6 = f / this.colorModeX;
                float f7 = f2 / this.colorModeY;
                float f8 = f3 / this.colorModeZ;
                if (this.colorModeScale) {
                    f5 /= this.colorModeA;
                }
                this.calcA = f5;
                if (f7 != 0.0f) {
                    float f9 = (f6 - ((float) ((int) f6))) * 6.0f;
                    float f10 = f9 - ((float) ((int) f9));
                    float f11 = (1.0f - f7) * f8;
                    float f12 = (1.0f - (f7 * f10)) * f8;
                    float f13 = (1.0f - ((1.0f - f10) * f7)) * f8;
                    switch ((int) f9) {
                        case 0:
                            this.calcR = f8;
                            this.calcG = f13;
                            this.calcB = f11;
                            break;
                        case 1:
                            this.calcR = f12;
                            this.calcG = f8;
                            this.calcB = f11;
                            break;
                        case 2:
                            this.calcR = f11;
                            this.calcG = f8;
                            this.calcB = f13;
                            break;
                        case 3:
                            this.calcR = f11;
                            this.calcG = f12;
                            this.calcB = f8;
                            break;
                        case 4:
                            this.calcR = f13;
                            this.calcG = f11;
                            this.calcB = f8;
                            break;
                        case 5:
                            this.calcR = f8;
                            this.calcG = f11;
                            this.calcB = f12;
                            break;
                    }
                } else {
                    this.calcB = f8;
                    this.calcG = f8;
                    this.calcR = f8;
                    break;
                }
        }
        this.calcRi = (int) (this.calcR * 255.0f);
        this.calcGi = (int) (this.calcG * 255.0f);
        this.calcBi = (int) (this.calcB * 255.0f);
        this.calcAi = (int) (this.calcA * 255.0f);
        this.calcColor = (this.calcAi << 24) | (this.calcRi << 16) | (this.calcGi << 8) | this.calcBi;
        this.calcAlpha = this.calcAi != 255;
    }

    /* access modifiers changed from: protected */
    public void colorCalc(int i) {
        if ((-16777216 & i) != 0 || ((float) i) > this.colorModeX) {
            colorCalcARGB(i, this.colorModeA);
        } else {
            colorCalc((float) i);
        }
    }

    /* access modifiers changed from: protected */
    public void colorCalc(int i, float f) {
        if ((-16777216 & i) != 0 || ((float) i) > this.colorModeX) {
            colorCalcARGB(i, f);
        } else {
            colorCalc((float) i, f);
        }
    }

    /* access modifiers changed from: protected */
    public void colorCalcARGB(int i, float f) {
        if (f == this.colorModeA) {
            this.calcAi = (i >> 24) & 255;
            this.calcColor = i;
        } else {
            this.calcAi = (int) (((float) ((i >> 24) & 255)) * (f / this.colorModeA));
            this.calcColor = (this.calcAi << 24) | (16777215 & i);
        }
        this.calcRi = (i >> 16) & 255;
        this.calcGi = (i >> 8) & 255;
        this.calcBi = i & 255;
        this.calcA = ((float) this.calcAi) / 255.0f;
        this.calcR = ((float) this.calcRi) / 255.0f;
        this.calcG = ((float) this.calcGi) / 255.0f;
        this.calcB = ((float) this.calcBi) / 255.0f;
        this.calcAlpha = this.calcAi != 255;
    }

    public void colorMode(int i) {
        colorMode(i, this.colorModeX, this.colorModeY, this.colorModeZ, this.colorModeA);
    }

    public void colorMode(int i, float f) {
        colorMode(i, f, f, f, f);
    }

    public void colorMode(int i, float f, float f2, float f3) {
        colorMode(i, f, f2, f3, this.colorModeA);
    }

    public void colorMode(int i, float f, float f2, float f3, float f4) {
        boolean z = true;
        this.colorMode = i;
        this.colorModeX = f;
        this.colorModeY = f2;
        this.colorModeZ = f3;
        this.colorModeA = f4;
        this.colorModeScale = (f4 == 1.0f && f == f2 && f2 == f3 && f3 == f4) ? false : true;
        if (!(this.colorMode == 1 && this.colorModeA == 255.0f && this.colorModeX == 255.0f && this.colorModeY == 255.0f && this.colorModeZ == 255.0f)) {
            z = false;
        }
        this.colorModeDefault = z;
    }

    public boolean contains(float f, float f2) {
        if (this.family == 2) {
            int i = this.vertexCount - 1;
            int i2 = 0;
            boolean z = false;
            while (i2 < this.vertexCount) {
                if ((this.vertices[i2][1] > f2) != (this.vertices[i][1] > f2) && f < (((this.vertices[i][0] - this.vertices[i2][0]) * (f2 - this.vertices[i2][1])) / (this.vertices[i][1] - this.vertices[i2][1])) + this.vertices[i2][0]) {
                    z = !z;
                }
                int i3 = i2;
                i2++;
                i = i3;
            }
            return z;
        }
        throw new IllegalArgumentException("The contains() method is only implemented for paths.");
    }

    /* access modifiers changed from: protected */
    public void crop() {
        if (this.children.length != this.childCount) {
            this.children = (PShape[]) PApplet.subset((Object) this.children, 0, this.childCount);
        }
    }

    public void curveDetail(int i) {
    }

    public void curveTightness(float f) {
    }

    public void curveVertex(float f, float f2) {
    }

    public void curveVertex(float f, float f2, float f3) {
    }

    public void disableStyle() {
        this.style = false;
        for (int i = 0; i < this.childCount; i++) {
            this.children[i].disableStyle();
        }
    }

    public void draw(PGraphics pGraphics) {
        if (this.visible) {
            pre(pGraphics);
            drawImpl(pGraphics);
            post(pGraphics);
        }
    }

    /* access modifiers changed from: protected */
    public void drawGeometry(PGraphics pGraphics) {
        pGraphics.beginShape(this.kind);
        if (this.style) {
            for (int i = 0; i < this.vertexCount; i++) {
                pGraphics.vertex(this.vertices[i]);
            }
        } else {
            for (int i2 = 0; i2 < this.vertexCount; i2++) {
                float[] fArr = this.vertices[i2];
                if (fArr[2] == 0.0f) {
                    pGraphics.vertex(fArr[0], fArr[1]);
                } else {
                    pGraphics.vertex(fArr[0], fArr[1], fArr[2]);
                }
            }
        }
        pGraphics.endShape(this.close ? 2 : 1);
    }

    /* access modifiers changed from: protected */
    public void drawGroup(PGraphics pGraphics) {
        for (int i = 0; i < this.childCount; i++) {
            this.children[i].draw(pGraphics);
        }
    }

    /* access modifiers changed from: protected */
    public void drawImpl(PGraphics pGraphics) {
        if (this.family == 0) {
            drawGroup(pGraphics);
        } else if (this.family == 1) {
            drawPrimitive(pGraphics);
        } else if (this.family == 3) {
            drawPath(pGraphics);
        } else if (this.family == 2) {
            drawPath(pGraphics);
        }
    }

    /* access modifiers changed from: protected */
    public void drawPath(PGraphics pGraphics) {
        if (this.vertices != null) {
            boolean z = false;
            pGraphics.beginShape();
            if (this.vertexCodeCount != 0) {
                int i = 0;
                if (this.vertices[0].length == 2) {
                    int i2 = 0;
                    while (true) {
                        int i3 = i2;
                        int i4 = i;
                        boolean z2 = z;
                        if (i3 < this.vertexCodeCount) {
                            switch (this.vertexCodes[i3]) {
                                case 0:
                                    pGraphics.vertex(this.vertices[i4][0], this.vertices[i4][1]);
                                    i4++;
                                    break;
                                case 1:
                                    pGraphics.bezierVertex(this.vertices[i4 + 0][0], this.vertices[i4 + 0][1], this.vertices[i4 + 1][0], this.vertices[i4 + 1][1], this.vertices[i4 + 2][0], this.vertices[i4 + 2][1]);
                                    i4 += 3;
                                    break;
                                case 2:
                                    pGraphics.quadraticVertex(this.vertices[i4 + 0][0], this.vertices[i4 + 0][1], this.vertices[i4 + 1][0], this.vertices[i4 + 1][1]);
                                    i4 += 2;
                                    break;
                                case 3:
                                    pGraphics.curveVertex(this.vertices[i4][0], this.vertices[i4][1]);
                                    i4++;
                                    break;
                                case 4:
                                    if (z2) {
                                        pGraphics.endContour();
                                    }
                                    pGraphics.beginContour();
                                    z2 = true;
                                    break;
                            }
                            i = i4;
                            z = z2;
                            i2 = i3 + 1;
                        } else {
                            z = z2;
                        }
                    }
                } else {
                    int i5 = 0;
                    while (true) {
                        int i6 = i5;
                        int i7 = i;
                        boolean z3 = z;
                        if (i6 < this.vertexCodeCount) {
                            switch (this.vertexCodes[i6]) {
                                case 0:
                                    pGraphics.vertex(this.vertices[i7][0], this.vertices[i7][1], this.vertices[i7][2]);
                                    i7++;
                                    break;
                                case 1:
                                    pGraphics.bezierVertex(this.vertices[i7 + 0][0], this.vertices[i7 + 0][1], this.vertices[i7 + 0][2], this.vertices[i7 + 1][0], this.vertices[i7 + 1][1], this.vertices[i7 + 1][2], this.vertices[i7 + 2][0], this.vertices[i7 + 2][1], this.vertices[i7 + 2][2]);
                                    i7 += 3;
                                    break;
                                case 2:
                                    pGraphics.quadraticVertex(this.vertices[i7 + 0][0], this.vertices[i7 + 0][1], this.vertices[i7 + 0][2], this.vertices[i7 + 1][0], this.vertices[i7 + 1][1], this.vertices[i7 + 0][2]);
                                    i7 += 2;
                                    break;
                                case 3:
                                    pGraphics.curveVertex(this.vertices[i7][0], this.vertices[i7][1], this.vertices[i7][2]);
                                    i7++;
                                    break;
                                case 4:
                                    if (z3) {
                                        pGraphics.endContour();
                                    }
                                    pGraphics.beginContour();
                                    z3 = true;
                                    break;
                            }
                            i = i7;
                            z = z3;
                            i5 = i6 + 1;
                        } else {
                            z = z3;
                        }
                    }
                }
            } else if (this.vertices[0].length == 2) {
                for (int i8 = 0; i8 < this.vertexCount; i8++) {
                    pGraphics.vertex(this.vertices[i8][0], this.vertices[i8][1]);
                }
            } else {
                for (int i9 = 0; i9 < this.vertexCount; i9++) {
                    pGraphics.vertex(this.vertices[i9][0], this.vertices[i9][1], this.vertices[i9][2]);
                }
            }
            if (z) {
                pGraphics.endContour();
            }
            pGraphics.endShape(this.close ? 2 : 1);
        }
    }

    /* access modifiers changed from: protected */
    public void drawPrimitive(PGraphics pGraphics) {
        if (this.kind == 2) {
            pGraphics.point(this.params[0], this.params[1]);
        } else if (this.kind == 4) {
            if (this.params.length == 4) {
                pGraphics.line(this.params[0], this.params[1], this.params[2], this.params[3]);
                return;
            }
            pGraphics.line(this.params[0], this.params[1], this.params[2], this.params[3], this.params[4], this.params[5]);
        } else if (this.kind == 8) {
            pGraphics.triangle(this.params[0], this.params[1], this.params[2], this.params[3], this.params[4], this.params[5]);
        } else if (this.kind == 16) {
            pGraphics.quad(this.params[0], this.params[1], this.params[2], this.params[3], this.params[4], this.params[5], this.params[6], this.params[7]);
        } else if (this.kind == 30) {
            if (this.image != null) {
                pGraphics.imageMode(0);
                pGraphics.image(this.image, this.params[0], this.params[1], this.params[2], this.params[3]);
                return;
            }
            if (this.params.length != 5) {
                pGraphics.rectMode(0);
            } else {
                pGraphics.rectMode((int) this.params[4]);
            }
            pGraphics.rect(this.params[0], this.params[1], this.params[2], this.params[3]);
        } else if (this.kind == 31) {
            pGraphics.ellipseMode(0);
            pGraphics.ellipse(this.params[0], this.params[1], this.params[2], this.params[3]);
        } else if (this.kind == 32) {
            pGraphics.ellipseMode(0);
            pGraphics.arc(this.params[0], this.params[1], this.params[2], this.params[3], this.params[4], this.params[5]);
        } else if (this.kind == 41) {
            if (this.params.length == 1) {
                pGraphics.box(this.params[0]);
            } else {
                pGraphics.box(this.params[0], this.params[1], this.params[2]);
            }
        } else if (this.kind == 40) {
            pGraphics.sphere(this.params[0]);
        }
    }

    public void emissive(float f) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "emissive()");
            return;
        }
        colorCalc(f);
        this.emissiveColor = this.calcColor;
    }

    public void emissive(float f, float f2, float f3) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "emissive()");
            return;
        }
        colorCalc(f, f2, f3);
        this.emissiveColor = this.calcColor;
    }

    public void emissive(int i) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "emissive()");
            return;
        }
        colorCalc(i);
        this.emissiveColor = this.calcColor;
    }

    public void enableStyle() {
        this.style = true;
        for (int i = 0; i < this.childCount; i++) {
            this.children[i].enableStyle();
        }
    }

    public void endContour() {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "endContour()");
        } else if (this.family == 0) {
            PGraphics.showWarning("Cannot end contour in GROUP shapes");
        } else if (!this.openContour) {
            PGraphics.showWarning("Need to call beginContour() first.");
        } else {
            endContourImpl();
            this.openContour = false;
        }
    }

    /* access modifiers changed from: protected */
    public void endContourImpl() {
    }

    public void endShape() {
        endShape(1);
    }

    public void endShape(int i) {
        if (this.family == 0) {
            PGraphics.showWarning("Cannot end GROUP shape");
        } else if (!this.openShape) {
            PGraphics.showWarning("Need to call beginShape() first");
        } else {
            this.close = i == 2;
            this.openShape = false;
        }
    }

    public void fill(float f) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "fill()");
            return;
        }
        this.fill = true;
        colorCalc(f);
        this.fillColor = this.calcColor;
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public void fill(float f, float f2) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "fill()");
            return;
        }
        this.fill = true;
        colorCalc(f, f2);
        this.fillColor = this.calcColor;
        if (!this.setAmbient) {
            ambient(this.fillColor);
            this.setAmbient = false;
        }
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public void fill(float f, float f2, float f3) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "fill()");
            return;
        }
        this.fill = true;
        colorCalc(f, f2, f3);
        this.fillColor = this.calcColor;
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public void fill(float f, float f2, float f3, float f4) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "fill()");
            return;
        }
        this.fill = true;
        colorCalc(f, f2, f3, f4);
        this.fillColor = this.calcColor;
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public void fill(int i) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "fill()");
            return;
        }
        this.fill = true;
        colorCalc(i);
        this.fillColor = this.calcColor;
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public void fill(int i, float f) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "fill()");
            return;
        }
        this.fill = true;
        colorCalc(i, f);
        this.fillColor = this.calcColor;
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public PShape findChild(String str) {
        return this.parent == null ? getChild(str) : this.parent.findChild(str);
    }

    public int getAmbient(int i) {
        if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "getAmbient()");
            return this.ambientColor;
        }
        return (((int) (this.vertices[i][25] * 255.0f)) << 16) | -16777216 | (((int) (this.vertices[i][26] * 255.0f)) << 8) | ((int) (this.vertices[i][27] * 255.0f));
    }

    public PShape getChild(int i) {
        crop();
        return this.children[i];
    }

    public PShape getChild(String str) {
        PShape pShape;
        if (this.name != null && this.name.equals(str)) {
            return this;
        }
        if (this.nameTable != null && (pShape = this.nameTable.get(str)) != null) {
            return pShape;
        }
        for (int i = 0; i < this.childCount; i++) {
            PShape child = this.children[i].getChild(str);
            if (child != null) {
                return child;
            }
        }
        return null;
    }

    public int getChildCount() {
        return this.childCount;
    }

    public int getChildIndex(PShape pShape) {
        for (int i = 0; i < this.childCount; i++) {
            if (this.children[i] == pShape) {
                return i;
            }
        }
        return -1;
    }

    public PShape[] getChildren() {
        crop();
        return this.children;
    }

    public float getDepth() {
        return this.depth;
    }

    public int getEmissive(int i) {
        if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "getEmissive()");
            return this.emissiveColor;
        }
        return (((int) (this.vertices[i][32] * 255.0f)) << 16) | -16777216 | (((int) (this.vertices[i][33] * 255.0f)) << 8) | ((int) (this.vertices[i][34] * 255.0f));
    }

    public int getFamily() {
        return this.family;
    }

    public int getFill(int i) {
        if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "getFill()");
            return this.fillColor;
        } else if (this.image != null) {
            return 0;
        } else {
            return (((int) (this.vertices[i][6] * 255.0f)) << 24) | (((int) (this.vertices[i][3] * 255.0f)) << 16) | (((int) (this.vertices[i][4] * 255.0f)) << 8) | ((int) (this.vertices[i][5] * 255.0f));
        }
    }

    public float getHeight() {
        return this.height;
    }

    public int getKind() {
        return this.kind;
    }

    public String getName() {
        return this.name;
    }

    public PVector getNormal(int i) {
        return getNormal(i, (PVector) null);
    }

    public PVector getNormal(int i, PVector pVector) {
        if (pVector == null) {
            pVector = new PVector();
        }
        pVector.x = this.vertices[i][9];
        pVector.y = this.vertices[i][10];
        pVector.z = this.vertices[i][11];
        return pVector;
    }

    public float getNormalX(int i) {
        return this.vertices[i][9];
    }

    public float getNormalY(int i) {
        return this.vertices[i][10];
    }

    public float getNormalZ(int i) {
        return this.vertices[i][11];
    }

    public float getParam(int i) {
        return this.params[i];
    }

    public float[] getParams() {
        return getParams((float[]) null);
    }

    public float[] getParams(float[] fArr) {
        if (fArr == null || fArr.length != this.params.length) {
            fArr = new float[this.params.length];
        }
        PApplet.arrayCopy(this.params, fArr);
        return fArr;
    }

    public PShape getParent() {
        return this.parent;
    }

    public float getShininess(int i) {
        if (this.vertices != null && i < this.vertices.length) {
            return this.vertices[i][31];
        }
        PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "getShininess()");
        return this.shininess;
    }

    public int getSpecular(int i) {
        if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "getSpecular()");
            return this.specularColor;
        }
        return (((int) (this.vertices[i][28] * 255.0f)) << 16) | -16777216 | (((int) (this.vertices[i][29] * 255.0f)) << 8) | ((int) (this.vertices[i][30] * 255.0f));
    }

    public int getStroke(int i) {
        if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "getStroke()");
            return this.strokeColor;
        }
        return (((int) (this.vertices[i][16] * 255.0f)) << 24) | (((int) (this.vertices[i][13] * 255.0f)) << 16) | (((int) (this.vertices[i][14] * 255.0f)) << 8) | ((int) (this.vertices[i][15] * 255.0f));
    }

    public float getStrokeWeight(int i) {
        if (this.vertices != null && i < this.vertices.length) {
            return this.vertices[i][17];
        }
        PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "getStrokeWeight()");
        return this.strokeWeight;
    }

    public PShape getTessellation() {
        return null;
    }

    public float getTextureU(int i) {
        return this.vertices[i][7];
    }

    public float getTextureV(int i) {
        return this.vertices[i][8];
    }

    public int getTint(int i) {
        if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "getTint()");
            return this.tintColor;
        } else if (this.image == null) {
            return 0;
        } else {
            return (((int) (this.vertices[i][6] * 255.0f)) << 24) | (((int) (this.vertices[i][3] * 255.0f)) << 16) | (((int) (this.vertices[i][4] * 255.0f)) << 8) | ((int) (this.vertices[i][5] * 255.0f));
        }
    }

    public PVector getVertex(int i) {
        return getVertex(i, (PVector) null);
    }

    public PVector getVertex(int i, PVector pVector) {
        if (pVector == null) {
            pVector = new PVector();
        }
        float[] fArr = this.vertices[i];
        pVector.x = fArr[0];
        pVector.y = fArr[1];
        if (fArr.length > 2) {
            pVector.z = fArr[2];
        } else {
            pVector.z = 0.0f;
        }
        return pVector;
    }

    public int getVertexCode(int i) {
        return this.vertexCodes[i];
    }

    public int getVertexCodeCount() {
        return this.vertexCodeCount;
    }

    public int[] getVertexCodes() {
        if (this.vertexCodes == null) {
            return null;
        }
        if (this.vertexCodes.length != this.vertexCodeCount) {
            this.vertexCodes = PApplet.subset(this.vertexCodes, 0, this.vertexCodeCount);
        }
        return this.vertexCodes;
    }

    public int getVertexCount() {
        if (this.family == 0 || this.family == 1) {
            PGraphics.showWarning(NO_VERTICES_ERROR);
        }
        return this.vertexCount;
    }

    public float getVertexX(int i) {
        return this.vertices[i][0];
    }

    public float getVertexY(int i) {
        return this.vertices[i][1];
    }

    public float getVertexZ(int i) {
        return this.vertices[i][2];
    }

    public float getWidth() {
        return this.width;
    }

    public boolean is2D() {
        return !this.is3D;
    }

    public boolean is3D() {
        return this.is3D;
    }

    public boolean isClosed() {
        return this.close;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void noFill() {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "noFill()");
            return;
        }
        this.fill = false;
        this.fillColor = 0;
        if (!this.setAmbient) {
            this.ambientColor = this.fillColor;
        }
    }

    public void noStroke() {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "noStroke()");
            return;
        }
        this.stroke = false;
    }

    public void noTexture() {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "noTexture()");
            return;
        }
        this.image = null;
    }

    public void noTint() {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "noTint()");
            return;
        }
        this.tint = false;
    }

    public void normal(float f, float f2, float f3) {
    }

    /* access modifiers changed from: protected */
    public void post(PGraphics pGraphics) {
        if (this.matrix != null) {
            pGraphics.popMatrix();
        }
        if (this.style) {
            pGraphics.popStyle();
        }
    }

    /* access modifiers changed from: protected */
    public void pre(PGraphics pGraphics) {
        if (this.matrix != null) {
            pGraphics.pushMatrix();
            pGraphics.applyMatrix(this.matrix);
        }
        if (this.style) {
            pGraphics.pushStyle();
            styles(pGraphics);
        }
    }

    public void quadraticVertex(float f, float f2, float f3, float f4) {
        if (this.vertices == null) {
            this.vertices = new float[10][];
        } else if (this.vertexCount + 1 >= this.vertices.length) {
            this.vertices = (float[][]) PApplet.expand((Object) this.vertices);
        }
        float[][] fArr = this.vertices;
        int i = this.vertexCount;
        this.vertexCount = i + 1;
        fArr[i] = new float[]{f, f2};
        float[][] fArr2 = this.vertices;
        int i2 = this.vertexCount;
        this.vertexCount = i2 + 1;
        fArr2[i2] = new float[]{f3, f4};
        if (this.vertexCodes.length == this.vertexCodeCount) {
            this.vertexCodes = PApplet.expand(this.vertexCodes);
        }
        int[] iArr = this.vertexCodes;
        int i3 = this.vertexCodeCount;
        this.vertexCodeCount = i3 + 1;
        iArr[i3] = 2;
        if (f3 > this.width) {
            this.width = f3;
        }
        if (f4 > this.height) {
            this.height = f4;
        }
    }

    public void quadraticVertex(float f, float f2, float f3, float f4, float f5, float f6) {
    }

    public void removeChild(int i) {
        if (i < this.childCount) {
            PShape pShape = this.children[i];
            while (i < this.childCount - 1) {
                this.children[i] = this.children[i + 1];
                i++;
            }
            this.childCount--;
            if (pShape.getName() != null && this.nameTable != null) {
                this.nameTable.remove(pShape.getName());
            }
        }
    }

    public void resetMatrix() {
        checkMatrix(2);
        this.matrix.reset();
    }

    public void rotate(float f) {
        checkMatrix(2);
        this.matrix.rotate(f);
    }

    public void rotate(float f, float f2, float f3, float f4) {
        checkMatrix(3);
        float f5 = (f2 * f2) + (f3 * f3) + (f4 * f4);
        if (Math.abs(f5 - 1.0f) > 1.0E-4f) {
            float sqrt = PApplet.sqrt(f5);
            f2 /= sqrt;
            f3 /= sqrt;
            f4 /= sqrt;
        }
        this.matrix.rotate(f, f2, f3, f4);
    }

    public void rotateX(float f) {
        rotate(f, 1.0f, 0.0f, 0.0f);
    }

    public void rotateY(float f) {
        rotate(f, 0.0f, 1.0f, 0.0f);
    }

    public void rotateZ(float f) {
        rotate(f, 0.0f, 0.0f, 1.0f);
    }

    public void scale(float f) {
        checkMatrix(2);
        this.matrix.scale(f);
    }

    public void scale(float f, float f2) {
        checkMatrix(2);
        this.matrix.scale(f, f2);
    }

    public void scale(float f, float f2, float f3) {
        checkMatrix(3);
        this.matrix.scale(f, f2, f3);
    }

    public void set3D(boolean z) {
        this.is3D = z;
    }

    public void setAmbient(int i) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setAmbient()");
            return;
        }
        this.ambientColor = i;
        if (this.vertices != null) {
            for (int i2 = 0; i2 < this.vertices.length; i2++) {
                setAmbient(i2, i);
            }
        }
    }

    public void setAmbient(int i, int i2) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setAmbient()");
        } else if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "setAmbient()");
        } else {
            this.vertices[i][25] = ((float) ((i2 >> 16) & 255)) / 255.0f;
            this.vertices[i][26] = ((float) ((i2 >> 8) & 255)) / 255.0f;
            this.vertices[i][27] = ((float) ((i2 >> 0) & 255)) / 255.0f;
        }
    }

    public void setAttrib(String str, int i, float... fArr) {
    }

    public void setAttrib(String str, int i, int... iArr) {
    }

    public void setAttrib(String str, int i, boolean... zArr) {
    }

    public void setEmissive(int i) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setEmissive()");
            return;
        }
        this.emissiveColor = i;
        if (this.vertices != null) {
            for (int i2 = 0; i2 < this.vertices.length; i2++) {
                setEmissive(i2, i);
            }
        }
    }

    public void setEmissive(int i, int i2) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setEmissive()");
        } else if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "setEmissive()");
        } else {
            this.vertices[i][32] = ((float) ((i2 >> 16) & 255)) / 255.0f;
            this.vertices[i][33] = ((float) ((i2 >> 8) & 255)) / 255.0f;
            this.vertices[i][34] = ((float) ((i2 >> 0) & 255)) / 255.0f;
        }
    }

    public void setFamily(int i) {
        this.family = i;
    }

    public void setFill(int i) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setFill()");
            return;
        }
        this.fillColor = i;
        if (this.vertices != null) {
            for (int i2 = 0; i2 < this.vertices.length; i2++) {
                setFill(i2, i);
            }
        }
    }

    public void setFill(int i, int i2) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setFill()");
        } else if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "getFill()");
        } else if (this.image == null) {
            this.vertices[i][6] = ((float) ((i2 >> 24) & 255)) / 255.0f;
            this.vertices[i][3] = ((float) ((i2 >> 16) & 255)) / 255.0f;
            this.vertices[i][4] = ((float) ((i2 >> 8) & 255)) / 255.0f;
            this.vertices[i][5] = ((float) ((i2 >> 0) & 255)) / 255.0f;
        }
    }

    public void setFill(boolean z) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setFill()");
            return;
        }
        this.fill = z;
    }

    public void setKind(int i) {
        this.kind = i;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setNormal(int i, float f, float f2, float f3) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setNormal()");
            return;
        }
        this.vertices[i][9] = f;
        this.vertices[i][10] = f2;
        this.vertices[i][11] = f3;
    }

    /* access modifiers changed from: protected */
    public void setParams(float[] fArr) {
        if (this.params == null) {
            this.params = new float[fArr.length];
        }
        if (fArr.length != this.params.length) {
            PGraphics.showWarning("Wrong number of parameters");
        } else {
            PApplet.arrayCopy(fArr, this.params);
        }
    }

    public void setPath(int i, float[][] fArr) {
        setPath(i, fArr, 0, (int[]) null);
    }

    /* access modifiers changed from: protected */
    public void setPath(int i, float[][] fArr, int i2, int[] iArr) {
        if (fArr != null && fArr.length >= i) {
            if (i2 <= 0 || (iArr != null && iArr.length >= i2)) {
                int length = fArr[0].length;
                this.vertexCount = i;
                this.vertices = (float[][]) Array.newInstance(Float.TYPE, new int[]{this.vertexCount, length});
                for (int i3 = 0; i3 < this.vertexCount; i3++) {
                    PApplet.arrayCopy(fArr[i3], this.vertices[i3]);
                }
                this.vertexCodeCount = i2;
                if (this.vertexCodeCount > 0) {
                    this.vertexCodes = new int[this.vertexCodeCount];
                    PApplet.arrayCopy(iArr, this.vertexCodes, this.vertexCodeCount);
                }
            }
        }
    }

    public void setShininess(float f) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setShininess()");
            return;
        }
        this.shininess = f;
        if (this.vertices != null) {
            for (int i = 0; i < this.vertices.length; i++) {
                setShininess(i, f);
            }
        }
    }

    public void setShininess(int i, float f) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setShininess()");
        } else if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "setShininess()");
        } else {
            this.vertices[i][31] = f;
        }
    }

    public void setSpecular(int i) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setSpecular()");
            return;
        }
        this.specularColor = i;
        if (this.vertices != null) {
            for (int i2 = 0; i2 < this.vertices.length; i2++) {
                setSpecular(i2, i);
            }
        }
    }

    public void setSpecular(int i, int i2) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setSpecular()");
        } else if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "setSpecular()");
        } else {
            this.vertices[i][28] = ((float) ((i2 >> 16) & 255)) / 255.0f;
            this.vertices[i][29] = ((float) ((i2 >> 8) & 255)) / 255.0f;
            this.vertices[i][30] = ((float) ((i2 >> 0) & 255)) / 255.0f;
        }
    }

    public void setStroke(int i) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStroke()");
            return;
        }
        this.strokeColor = i;
        if (this.vertices != null) {
            for (int i2 = 0; i2 < this.vertices.length; i2++) {
                setStroke(i2, i);
            }
        }
    }

    public void setStroke(int i, int i2) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStroke()");
        } else if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "setStroke()");
        } else {
            this.vertices[i][16] = ((float) ((i2 >> 24) & 255)) / 255.0f;
            this.vertices[i][13] = ((float) ((i2 >> 16) & 255)) / 255.0f;
            this.vertices[i][14] = ((float) ((i2 >> 8) & 255)) / 255.0f;
            this.vertices[i][15] = ((float) ((i2 >> 0) & 255)) / 255.0f;
        }
    }

    public void setStroke(boolean z) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStroke()");
            return;
        }
        this.stroke = z;
    }

    public void setStrokeCap(int i) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStrokeCap()");
            return;
        }
        this.strokeCap = i;
    }

    public void setStrokeJoin(int i) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStrokeJoin()");
            return;
        }
        this.strokeJoin = i;
    }

    public void setStrokeWeight(float f) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStrokeWeight()");
            return;
        }
        this.strokeWeight = f;
        if (this.vertices != null) {
            for (int i = 0; i < this.vertices.length; i++) {
                setStrokeWeight(i, f);
            }
        }
    }

    public void setStrokeWeight(int i, float f) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setStrokeWeight()");
        } else if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "setStrokeWeight()");
        } else {
            this.vertices[i][17] = f;
        }
    }

    public void setTexture(PImage pImage) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setTexture()");
            return;
        }
        this.image = pImage;
    }

    public void setTextureMode(int i) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setTextureMode()");
            return;
        }
        this.textureMode = i;
    }

    public void setTextureUV(int i, float f, float f2) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setTextureUV()");
        } else if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "setTextureUV()");
        } else {
            this.vertices[i][7] = f;
            this.vertices[i][8] = f2;
        }
    }

    public void setTint(int i) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setTint()");
            return;
        }
        this.tintColor = i;
        if (this.vertices != null) {
            for (int i2 = 0; i2 < this.vertices.length; i2++) {
                setFill(i2, i);
            }
        }
    }

    public void setTint(int i, int i2) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setTint()");
        } else if (this.vertices == null || i >= this.vertices.length) {
            PGraphics.showWarning("%1$s vertex index does not exist (" + i + ")", "setTint()");
        } else if (this.image != null) {
            this.vertices[i][6] = ((float) ((i2 >> 24) & 255)) / 255.0f;
            this.vertices[i][3] = ((float) ((i2 >> 16) & 255)) / 255.0f;
            this.vertices[i][4] = ((float) ((i2 >> 8) & 255)) / 255.0f;
            this.vertices[i][5] = ((float) ((i2 >> 0) & 255)) / 255.0f;
        }
    }

    public void setTint(boolean z) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setTint()");
            return;
        }
        this.tint = z;
    }

    public void setVertex(int i, float f, float f2) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setVertex()");
            return;
        }
        this.vertices[i][0] = f;
        this.vertices[i][1] = f2;
    }

    public void setVertex(int i, float f, float f2, float f3) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setVertex()");
            return;
        }
        this.vertices[i][0] = f;
        this.vertices[i][1] = f2;
        this.vertices[i][2] = f3;
    }

    public void setVertex(int i, PVector pVector) {
        if (this.openShape) {
            PGraphics.showWarning(INSIDE_BEGIN_END_ERROR, "setVertex()");
            return;
        }
        this.vertices[i][0] = pVector.x;
        this.vertices[i][1] = pVector.y;
        if (this.vertices[i].length > 2) {
            this.vertices[i][2] = pVector.z;
        } else if (pVector.z != 0.0f && pVector.z == pVector.z) {
            throw new IllegalArgumentException("Cannot set a z-coordinate on a 2D shape");
        }
    }

    public void setVisible(boolean z) {
        this.visible = z;
    }

    public void shininess(float f) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "shininess()");
            return;
        }
        this.shininess = f;
    }

    /* access modifiers changed from: protected */
    public void solid(boolean z) {
    }

    public void specular(float f) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "specular()");
            return;
        }
        colorCalc(f);
        this.specularColor = this.calcColor;
    }

    public void specular(float f, float f2, float f3) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "specular()");
            return;
        }
        colorCalc(f, f2, f3);
        this.specularColor = this.calcColor;
    }

    public void specular(int i) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "specular()");
            return;
        }
        colorCalc(i);
        this.specularColor = this.calcColor;
    }

    public void stroke(float f) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "stroke()");
            return;
        }
        this.stroke = true;
        colorCalc(f);
        this.strokeColor = this.calcColor;
    }

    public void stroke(float f, float f2) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "stroke()");
            return;
        }
        this.stroke = true;
        colorCalc(f, f2);
        this.strokeColor = this.calcColor;
    }

    public void stroke(float f, float f2, float f3) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "stroke()");
            return;
        }
        this.stroke = true;
        colorCalc(f, f2, f3);
        this.strokeColor = this.calcColor;
    }

    public void stroke(float f, float f2, float f3, float f4) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "stroke()");
            return;
        }
        this.stroke = true;
        colorCalc(f, f2, f3, f4);
        this.strokeColor = this.calcColor;
    }

    public void stroke(int i) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "stroke()");
            return;
        }
        this.stroke = true;
        colorCalc(i);
        this.strokeColor = this.calcColor;
    }

    public void stroke(int i, float f) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "stroke()");
            return;
        }
        this.stroke = true;
        colorCalc(i, f);
        this.strokeColor = this.calcColor;
    }

    public void strokeCap(int i) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "strokeCap()");
            return;
        }
        this.strokeCap = i;
    }

    public void strokeJoin(int i) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "strokeJoin()");
            return;
        }
        this.strokeJoin = i;
    }

    public void strokeWeight(float f) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "strokeWeight()");
            return;
        }
        this.strokeWeight = f;
    }

    /* access modifiers changed from: protected */
    public void styles(PGraphics pGraphics) {
        if (this.stroke) {
            pGraphics.stroke(this.strokeColor);
            pGraphics.strokeWeight(this.strokeWeight);
            pGraphics.strokeCap(this.strokeCap);
            pGraphics.strokeJoin(this.strokeJoin);
        } else {
            pGraphics.noStroke();
        }
        if (this.fill) {
            pGraphics.fill(this.fillColor);
        } else {
            pGraphics.noFill();
        }
    }

    public void texture(PImage pImage) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "texture()");
            return;
        }
        this.image = pImage;
    }

    public void textureMode(int i) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "textureMode()");
            return;
        }
        this.textureMode = i;
    }

    public void tint(float f) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "tint()");
            return;
        }
        this.tint = true;
        colorCalc(f);
        this.tintColor = this.calcColor;
    }

    public void tint(float f, float f2) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "tint()");
            return;
        }
        this.tint = true;
        colorCalc(f, f2);
        this.tintColor = this.calcColor;
    }

    public void tint(float f, float f2, float f3) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "tint()");
            return;
        }
        this.tint = true;
        colorCalc(f, f2, f3);
        this.tintColor = this.calcColor;
    }

    public void tint(float f, float f2, float f3, float f4) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "tint()");
            return;
        }
        this.tint = true;
        colorCalc(f, f2, f3, f4);
        this.tintColor = this.calcColor;
    }

    public void tint(int i) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "tint()");
            return;
        }
        this.tint = true;
        colorCalc(i);
        this.tintColor = this.calcColor;
    }

    public void tint(int i, float f) {
        if (!this.openShape) {
            PGraphics.showWarning(OUTSIDE_BEGIN_END_ERROR, "tint()");
            return;
        }
        this.tint = true;
        colorCalc(i, f);
        this.tintColor = this.calcColor;
    }

    public void translate(float f, float f2) {
        checkMatrix(2);
        this.matrix.translate(f, f2);
    }

    public void translate(float f, float f2, float f3) {
        checkMatrix(3);
        this.matrix.translate(f, f2, f3);
    }

    public void vertex(float f, float f2) {
        if (this.vertices == null) {
            this.vertices = (float[][]) Array.newInstance(Float.TYPE, new int[]{10, 2});
        } else if (this.vertices.length == this.vertexCount) {
            this.vertices = (float[][]) PApplet.expand((Object) this.vertices);
        }
        float[][] fArr = this.vertices;
        int i = this.vertexCount;
        this.vertexCount = i + 1;
        fArr[i] = new float[]{f, f2};
        if (this.vertexCodes == null) {
            this.vertexCodes = new int[10];
        } else if (this.vertexCodes.length == this.vertexCodeCount) {
            this.vertexCodes = PApplet.expand(this.vertexCodes);
        }
        int[] iArr = this.vertexCodes;
        int i2 = this.vertexCodeCount;
        this.vertexCodeCount = i2 + 1;
        iArr[i2] = 0;
        if (f > this.width) {
            this.width = f;
        }
        if (f2 > this.height) {
            this.height = f2;
        }
    }

    public void vertex(float f, float f2, float f3) {
        vertex(f, f2);
    }

    public void vertex(float f, float f2, float f3, float f4) {
    }

    public void vertex(float f, float f2, float f3, float f4, float f5) {
    }
}
