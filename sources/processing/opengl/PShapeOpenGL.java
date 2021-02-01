package processing.opengl;

import java.nio.Buffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;

public class PShapeOpenGL extends PShape {
    public static final int DIRECTION = 3;
    protected static final int MATRIX = 3;
    public static final int NORMAL = 1;
    protected static final int NORMAL_MODE_AUTO = 0;
    protected static final int NORMAL_MODE_SHAPE = 1;
    protected static final int NORMAL_MODE_VERTEX = 2;
    public static final int OFFSET = 4;
    public static final int POSITION = 0;
    protected static final int ROTATE = 1;
    protected static final int SCALE = 2;
    public static final int TEXCOORD = 2;
    protected static final int TRANSLATE = 0;
    protected int bezierDetail;
    protected boolean breakShape;
    protected VertexBuffer bufLineAttrib;
    protected VertexBuffer bufLineColor;
    protected VertexBuffer bufLineIndex;
    protected VertexBuffer bufLineVertex;
    protected VertexBuffer bufPointAttrib;
    protected VertexBuffer bufPointColor;
    protected VertexBuffer bufPointIndex;
    protected VertexBuffer bufPointVertex;
    protected VertexBuffer bufPolyAmbient;
    protected VertexBuffer bufPolyColor;
    protected VertexBuffer bufPolyEmissive;
    protected VertexBuffer bufPolyIndex;
    protected VertexBuffer bufPolyNormal;
    protected VertexBuffer bufPolyShininess;
    protected VertexBuffer bufPolySpecular;
    protected VertexBuffer bufPolyTexcoord;
    protected VertexBuffer bufPolyVertex;
    protected int context;
    protected int curveDetail;
    protected float curveTightness;
    protected int firstLineIndexCache;
    protected int firstLineVertex;
    protected int firstModifiedLineAttribute;
    protected int firstModifiedLineColor;
    protected int firstModifiedLineVertex;
    protected int firstModifiedPointAttribute;
    protected int firstModifiedPointColor;
    protected int firstModifiedPointVertex;
    protected int firstModifiedPolyAmbient;
    protected int firstModifiedPolyColor;
    protected int firstModifiedPolyEmissive;
    protected int firstModifiedPolyNormal;
    protected int firstModifiedPolyShininess;
    protected int firstModifiedPolySpecular;
    protected int firstModifiedPolyTexcoord;
    protected int firstModifiedPolyVertex;
    protected int firstPointIndexCache;
    protected int firstPointVertex;
    protected int firstPolyIndexCache;
    protected int firstPolyVertex;
    public int glUsage;
    protected boolean hasLines;
    protected boolean hasPoints;
    protected boolean hasPolys;
    protected PGraphicsOpenGL.InGeometry inGeo;
    protected int lastLineIndexCache;
    protected int lastLineVertex;
    protected int lastModifiedLineAttribute;
    protected int lastModifiedLineColor;
    protected int lastModifiedLineVertex;
    protected int lastModifiedPointAttribute;
    protected int lastModifiedPointColor;
    protected int lastModifiedPointVertex;
    protected int lastModifiedPolyAmbient;
    protected int lastModifiedPolyColor;
    protected int lastModifiedPolyEmissive;
    protected int lastModifiedPolyNormal;
    protected int lastModifiedPolyShininess;
    protected int lastModifiedPolySpecular;
    protected int lastModifiedPolyTexcoord;
    protected int lastModifiedPolyVertex;
    protected int lastPointIndexCache;
    protected int lastPointVertex;
    protected int lastPolyIndexCache;
    protected int lastPolyVertex;
    protected int lineIndCopyOffset;
    protected int lineIndexOffset;
    protected int lineVertCopyOffset;
    protected int lineVertexAbs;
    protected int lineVertexOffset;
    protected int lineVertexRel;
    protected boolean modified;
    protected boolean modifiedLineAttributes;
    protected boolean modifiedLineColors;
    protected boolean modifiedLineVertices;
    protected boolean modifiedPointAttributes;
    protected boolean modifiedPointColors;
    protected boolean modifiedPointVertices;
    protected boolean modifiedPolyAmbient;
    protected boolean modifiedPolyColors;
    protected boolean modifiedPolyEmissive;
    protected boolean modifiedPolyNormals;
    protected boolean modifiedPolyShininess;
    protected boolean modifiedPolySpecular;
    protected boolean modifiedPolyTexCoords;
    protected boolean modifiedPolyVertices;
    protected boolean needBufferInit;
    protected int normalMode;
    protected float normalX;
    protected float normalY;
    protected float normalZ;
    protected PGraphicsOpenGL pg;
    protected PGL pgl;
    protected int pointIndCopyOffset;
    protected int pointIndexOffset;
    protected int pointVertCopyOffset;
    protected int pointVertexAbs;
    protected int pointVertexOffset;
    protected int pointVertexRel;
    protected PGraphicsOpenGL.AttributeMap polyAttribs;
    protected int polyIndCopyOffset;
    protected int polyIndexOffset;
    protected int polyVertCopyOffset;
    protected int polyVertexAbs;
    protected int polyVertexOffset;
    protected int polyVertexRel;
    protected PShapeOpenGL root;
    protected int savedAmbientColor;
    protected int savedBezierDetail;
    protected int savedCurveDetail;
    protected float savedCurveTightness;
    protected int savedEmissiveColor;
    protected boolean savedFill;
    protected int savedFillColor;
    protected float savedShininess;
    protected int savedSpecularColor;
    protected boolean savedStroke;
    protected int savedStrokeCap;
    protected int savedStrokeColor;
    protected int savedStrokeJoin;
    protected float savedStrokeWeight;
    protected int savedTextureMode;
    protected boolean savedTint;
    protected int savedTintColor;
    protected boolean shapeCreated;
    protected boolean solid;
    protected boolean strokedTexture;
    protected PGraphicsOpenGL.TessGeometry tessGeo;
    protected boolean tessellated;
    protected PGraphicsOpenGL.Tessellator tessellator;
    protected HashSet<PImage> textures;
    protected PMatrix transform;
    protected Stack<PMatrix> transformStack;
    protected boolean untexChild;

    PShapeOpenGL() {
        this.glUsage = PGL.STATIC_DRAW;
        this.needBufferInit = false;
        this.solid = true;
        this.breakShape = false;
        this.shapeCreated = false;
    }

    public PShapeOpenGL(PGraphicsOpenGL pGraphicsOpenGL, int i) {
        this.glUsage = PGL.STATIC_DRAW;
        this.needBufferInit = false;
        this.solid = true;
        this.breakShape = false;
        this.shapeCreated = false;
        this.pg = pGraphicsOpenGL;
        this.family = i;
        this.pgl = pGraphicsOpenGL.pgl;
        this.context = this.pgl.createEmptyContext();
        this.bufPolyVertex = null;
        this.bufPolyColor = null;
        this.bufPolyNormal = null;
        this.bufPolyTexcoord = null;
        this.bufPolyAmbient = null;
        this.bufPolySpecular = null;
        this.bufPolyEmissive = null;
        this.bufPolyShininess = null;
        this.bufPolyIndex = null;
        this.bufLineVertex = null;
        this.bufLineColor = null;
        this.bufLineAttrib = null;
        this.bufLineIndex = null;
        this.bufPointVertex = null;
        this.bufPointColor = null;
        this.bufPointAttrib = null;
        this.bufPointIndex = null;
        this.tessellator = pGraphicsOpenGL.tessellator;
        this.root = this;
        this.parent = null;
        this.tessellated = false;
        if (i == 3 || i == 1 || i == 2) {
            this.polyAttribs = PGraphicsOpenGL.newAttributeMap();
            this.inGeo = PGraphicsOpenGL.newInGeometry(pGraphicsOpenGL, this.polyAttribs, 1);
        }
        this.textureMode = pGraphicsOpenGL.textureMode;
        colorMode(pGraphicsOpenGL.colorMode, pGraphicsOpenGL.colorModeX, pGraphicsOpenGL.colorModeY, pGraphicsOpenGL.colorModeZ, pGraphicsOpenGL.colorModeA);
        this.fill = pGraphicsOpenGL.fill;
        this.fillColor = pGraphicsOpenGL.fillColor;
        this.stroke = pGraphicsOpenGL.stroke;
        this.strokeColor = pGraphicsOpenGL.strokeColor;
        this.strokeWeight = pGraphicsOpenGL.strokeWeight;
        this.strokeCap = pGraphicsOpenGL.strokeCap;
        this.strokeJoin = pGraphicsOpenGL.strokeJoin;
        this.tint = pGraphicsOpenGL.tint;
        this.tintColor = pGraphicsOpenGL.tintColor;
        this.setAmbient = pGraphicsOpenGL.setAmbient;
        this.ambientColor = pGraphicsOpenGL.ambientColor;
        this.specularColor = pGraphicsOpenGL.specularColor;
        this.emissiveColor = pGraphicsOpenGL.emissiveColor;
        this.shininess = pGraphicsOpenGL.shininess;
        this.sphereDetailU = pGraphicsOpenGL.sphereDetailU;
        this.sphereDetailV = pGraphicsOpenGL.sphereDetailV;
        this.bezierDetail = pGraphicsOpenGL.bezierDetail;
        this.curveDetail = pGraphicsOpenGL.curveDetail;
        this.curveTightness = pGraphicsOpenGL.curveTightness;
        this.rectMode = pGraphicsOpenGL.rectMode;
        this.ellipseMode = pGraphicsOpenGL.ellipseMode;
        this.normalY = 0.0f;
        this.normalX = 0.0f;
        this.normalZ = 1.0f;
        this.normalMode = 0;
        this.breakShape = false;
        if (i == 0) {
            this.shapeCreated = true;
        }
        this.perVertexStyles = true;
    }

    public PShapeOpenGL(PGraphicsOpenGL pGraphicsOpenGL, int i, float... fArr) {
        this(pGraphicsOpenGL, 1);
        setKind(i);
        setParams(fArr);
    }

    public static void copyGroup(PGraphicsOpenGL pGraphicsOpenGL, PShape pShape, PShape pShape2) {
        copyMatrix(pShape, pShape2);
        copyStyles(pShape, pShape2);
        copyImage(pShape, pShape2);
        for (int i = 0; i < pShape.getChildCount(); i++) {
            pShape2.addChild(createShape(pGraphicsOpenGL, pShape.getChild(i)));
        }
    }

    public static PShapeOpenGL createShape(PGraphicsOpenGL pGraphicsOpenGL, PShape pShape) {
        PShapeOpenGL pShapeOpenGL = null;
        if (pShape.getFamily() == 0) {
            pShapeOpenGL = (PShapeOpenGL) pGraphicsOpenGL.createShapeFamily(0);
            copyGroup(pGraphicsOpenGL, pShape, pShapeOpenGL);
        } else if (pShape.getFamily() == 1) {
            pShapeOpenGL = (PShapeOpenGL) pGraphicsOpenGL.createShapePrimitive(pShape.getKind(), pShape.getParams());
            PShape.copyPrimitive(pShape, pShapeOpenGL);
        } else if (pShape.getFamily() == 3) {
            pShapeOpenGL = (PShapeOpenGL) pGraphicsOpenGL.createShapeFamily(3);
            PShape.copyGeometry(pShape, pShapeOpenGL);
        } else if (pShape.getFamily() == 2) {
            pShapeOpenGL = (PShapeOpenGL) pGraphicsOpenGL.createShapeFamily(2);
            PShape.copyPath(pShape, pShapeOpenGL);
        }
        pShapeOpenGL.setName(pShape.getName());
        pShapeOpenGL.width = pShape.width;
        pShapeOpenGL.height = pShape.height;
        pShapeOpenGL.depth = pShape.depth;
        return pShapeOpenGL;
    }

    public void addChild(PShape pShape) {
        if (!(pShape instanceof PShapeOpenGL)) {
            PGraphics.showWarning("Shape must be OpenGL to be added to the group.");
        } else if (this.family == 0) {
            PShapeOpenGL pShapeOpenGL = (PShapeOpenGL) pShape;
            super.addChild(pShapeOpenGL);
            pShapeOpenGL.updateRoot(this.root);
            markForTessellation();
            if (pShapeOpenGL.family == 0) {
                if (pShapeOpenGL.textures != null) {
                    Iterator<PImage> it = pShapeOpenGL.textures.iterator();
                    while (it.hasNext()) {
                        addTexture(it.next());
                    }
                } else {
                    untexChild(true);
                }
                if (pShapeOpenGL.strokedTexture) {
                    strokedTexture(true);
                }
            } else if (pShapeOpenGL.image != null) {
                addTexture(pShapeOpenGL.image);
                if (pShapeOpenGL.stroke) {
                    strokedTexture(true);
                }
            } else {
                untexChild(true);
            }
        } else {
            PGraphics.showWarning("Cannot add child shape to non-group shape.");
        }
    }

    public void addChild(PShape pShape, int i) {
        if (!(pShape instanceof PShapeOpenGL)) {
            PGraphics.showWarning("Shape must be OpenGL to be added to the group.");
        } else if (this.family == 0) {
            PShapeOpenGL pShapeOpenGL = (PShapeOpenGL) pShape;
            super.addChild(pShapeOpenGL, i);
            pShapeOpenGL.updateRoot(this.root);
            markForTessellation();
            if (pShapeOpenGL.family == 0) {
                if (pShapeOpenGL.textures != null) {
                    Iterator<PImage> it = pShapeOpenGL.textures.iterator();
                    while (it.hasNext()) {
                        addTexture(it.next());
                    }
                } else {
                    untexChild(true);
                }
                if (pShapeOpenGL.strokedTexture) {
                    strokedTexture(true);
                }
            } else if (pShapeOpenGL.image != null) {
                addTexture(pShapeOpenGL.image);
                if (pShapeOpenGL.stroke) {
                    strokedTexture(true);
                }
            } else {
                untexChild(true);
            }
        } else {
            PGraphics.showWarning("Cannot add child shape to non-group shape.");
        }
    }

    /* access modifiers changed from: protected */
    public void addTexture(PImage pImage) {
        if (this.textures == null) {
            this.textures = new HashSet<>();
        }
        this.textures.add(pImage);
        if (this.parent != null) {
            ((PShapeOpenGL) this.parent).addTexture(pImage);
        }
    }

    /* access modifiers changed from: protected */
    public void aggregate() {
        if (this.root == this && this.parent == null) {
            this.polyIndexOffset = 0;
            this.polyVertexOffset = 0;
            this.polyVertexAbs = 0;
            this.polyVertexRel = 0;
            this.lineIndexOffset = 0;
            this.lineVertexOffset = 0;
            this.lineVertexAbs = 0;
            this.lineVertexRel = 0;
            this.pointIndexOffset = 0;
            this.pointVertexOffset = 0;
            this.pointVertexAbs = 0;
            this.pointVertexRel = 0;
            aggregateImpl();
        }
    }

    /* access modifiers changed from: protected */
    public void aggregateImpl() {
        boolean z = true;
        int i = 0;
        if (this.family == 0) {
            this.hasPolys = false;
            this.hasLines = false;
            this.hasPoints = false;
            while (true) {
                int i2 = i;
                if (i2 >= this.childCount) {
                    break;
                }
                PShapeOpenGL pShapeOpenGL = (PShapeOpenGL) this.children[i2];
                pShapeOpenGL.aggregateImpl();
                this.hasPolys |= pShapeOpenGL.hasPolys;
                this.hasLines |= pShapeOpenGL.hasLines;
                this.hasPoints = pShapeOpenGL.hasPoints | this.hasPoints;
                i = i2 + 1;
            }
        } else {
            this.hasPolys = -1 < this.firstPolyIndexCache && -1 < this.lastPolyIndexCache;
            this.hasLines = -1 < this.firstLineIndexCache && -1 < this.lastLineIndexCache;
            if (-1 >= this.firstPointIndexCache || -1 >= this.lastPointIndexCache) {
                z = false;
            }
            this.hasPoints = z;
        }
        if (this.hasPolys) {
            updatePolyIndexCache();
        }
        if (is3D()) {
            if (this.hasLines) {
                updateLineIndexCache();
            }
            if (this.hasPoints) {
                updatePointIndexCache();
            }
        }
        if (this.matrix != null) {
            if (this.hasPolys) {
                this.tessGeo.applyMatrixOnPolyGeometry(this.matrix, this.firstPolyVertex, this.lastPolyVertex);
            }
            if (is3D()) {
                if (this.hasLines) {
                    this.tessGeo.applyMatrixOnLineGeometry(this.matrix, this.firstLineVertex, this.lastLineVertex);
                }
                if (this.hasPoints) {
                    this.tessGeo.applyMatrixOnPointGeometry(this.matrix, this.firstPointVertex, this.lastPointVertex);
                }
            }
        }
    }

    public void applyMatrix(float f, float f2, float f3, float f4, float f5, float f6) {
        transform(3, f, f2, f3, f4, f5, f6);
    }

    public void applyMatrix(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        transform(3, f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16);
    }

    public void applyMatrix(PMatrix2D pMatrix2D) {
        transform(3, pMatrix2D.m00, pMatrix2D.m01, pMatrix2D.m02, pMatrix2D.m10, pMatrix2D.m11, pMatrix2D.m12);
    }

    /* access modifiers changed from: protected */
    public void applyMatrixImpl(PMatrix pMatrix) {
        if (this.hasPolys) {
            this.tessGeo.applyMatrixOnPolyGeometry(pMatrix, this.firstPolyVertex, this.lastPolyVertex);
            this.root.setModifiedPolyVertices(this.firstPolyVertex, this.lastPolyVertex);
            this.root.setModifiedPolyNormals(this.firstPolyVertex, this.lastPolyVertex);
            for (PGraphicsOpenGL.VertexAttribute vertexAttribute : this.polyAttribs.values()) {
                if (vertexAttribute.isPosition() || vertexAttribute.isNormal()) {
                    this.root.setModifiedPolyAttrib(vertexAttribute, this.firstPolyVertex, this.lastPolyVertex);
                }
            }
        }
        if (is3D()) {
            if (this.hasLines) {
                this.tessGeo.applyMatrixOnLineGeometry(pMatrix, this.firstLineVertex, this.lastLineVertex);
                this.root.setModifiedLineVertices(this.firstLineVertex, this.lastLineVertex);
                this.root.setModifiedLineAttributes(this.firstLineVertex, this.lastLineVertex);
            }
            if (this.hasPoints) {
                this.tessGeo.applyMatrixOnPointGeometry(pMatrix, this.firstPointVertex, this.lastPointVertex);
                this.root.setModifiedPointVertices(this.firstPointVertex, this.lastPointVertex);
                this.root.setModifiedPointAttributes(this.firstPointVertex, this.lastPointVertex);
            }
        }
    }

    public void attrib(String str, float... fArr) {
        PGraphicsOpenGL.VertexAttribute attribImpl = attribImpl(str, 3, PGL.FLOAT, fArr.length);
        if (attribImpl != null) {
            attribImpl.set(fArr);
        }
    }

    public void attrib(String str, int... iArr) {
        PGraphicsOpenGL.VertexAttribute attribImpl = attribImpl(str, 3, PGL.INT, iArr.length);
        if (attribImpl != null) {
            attribImpl.set(iArr);
        }
    }

    public void attrib(String str, boolean... zArr) {
        PGraphicsOpenGL.VertexAttribute attribImpl = attribImpl(str, 3, PGL.BOOL, zArr.length);
        if (attribImpl != null) {
            attribImpl.set(zArr);
        }
    }

    public void attribColor(String str, int i) {
        PGraphicsOpenGL.VertexAttribute attribImpl = attribImpl(str, 2, PGL.INT, 1);
        if (attribImpl != null) {
            attribImpl.set(new int[]{i});
        }
    }

    /* access modifiers changed from: protected */
    public PGraphicsOpenGL.VertexAttribute attribImpl(String str, int i, int i2, int i3) {
        if (4 < i3) {
            PGraphics.showWarning("Vertex attributes cannot have more than 4 values");
            return null;
        }
        PGraphicsOpenGL.VertexAttribute vertexAttribute = (PGraphicsOpenGL.VertexAttribute) this.polyAttribs.get(str);
        if (vertexAttribute == null) {
            vertexAttribute = new PGraphicsOpenGL.VertexAttribute(this.pg, str, i, i2, i3);
            this.polyAttribs.put(str, vertexAttribute);
            this.inGeo.initAttrib(vertexAttribute);
        }
        if (vertexAttribute.kind != i) {
            PGraphics.showWarning("The attribute kind cannot be changed after creation");
            return null;
        } else if (vertexAttribute.type != i2) {
            PGraphics.showWarning("The attribute type cannot be changed after creation");
            return null;
        } else if (vertexAttribute.size == i3) {
            return vertexAttribute;
        } else {
            PGraphics.showWarning("New value for vertex attribute has wrong number of values");
            return null;
        }
    }

    public void attribNormal(String str, float f, float f2, float f3) {
        PGraphicsOpenGL.VertexAttribute attribImpl = attribImpl(str, 1, PGL.FLOAT, 3);
        if (attribImpl != null) {
            attribImpl.set(f, f2, f3);
        }
    }

    public void attribPosition(String str, float f, float f2, float f3) {
        PGraphicsOpenGL.VertexAttribute attribImpl = attribImpl(str, 0, PGL.FLOAT, 3);
        if (attribImpl != null) {
            attribImpl.set(f, f2, f3);
        }
    }

    /* access modifiers changed from: protected */
    public void beginContourImpl() {
        this.breakShape = true;
    }

    public void bezierDetail(int i) {
        this.bezierDetail = i;
        if (this.inGeo.codeCount > 0) {
            markForTessellation();
        }
    }

    public void bezierVertex(float f, float f2, float f3, float f4, float f5, float f6) {
        bezierVertexImpl(f, f2, 0.0f, f3, f4, 0.0f, f5, f6, 0.0f);
    }

    public void bezierVertex(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        bezierVertexImpl(f, f2, f3, f4, f5, f6, f7, f8, f9);
    }

    /* access modifiers changed from: protected */
    public void bezierVertexImpl(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addBezierVertex(f, f2, f3, f4, f5, f6, f7, f8, f9, vertexBreak());
    }

    /* access modifiers changed from: protected */
    public void collectPolyAttribs() {
        int i = 0;
        PGraphicsOpenGL.AttributeMap attributeMap = this.root.polyAttribs;
        if (this.family == 0) {
            while (true) {
                int i2 = i;
                if (i2 < this.childCount) {
                    ((PShapeOpenGL) this.children[i2]).collectPolyAttribs();
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else {
            while (true) {
                int i3 = i;
                if (i3 < this.polyAttribs.size()) {
                    PGraphicsOpenGL.VertexAttribute vertexAttribute = this.polyAttribs.get(i3);
                    this.tessGeo.initAttrib(vertexAttribute);
                    if (!attributeMap.containsKey(vertexAttribute.name)) {
                        attributeMap.put(vertexAttribute.name, vertexAttribute);
                    } else if (((PGraphicsOpenGL.VertexAttribute) attributeMap.get(vertexAttribute.name)).diff(vertexAttribute)) {
                        throw new RuntimeException("Children shapes cannot have different attributes with same name");
                    }
                    i = i3 + 1;
                } else {
                    return;
                }
            }
        }
    }

    public boolean contains(float f, float f2) {
        if (this.family == 2) {
            int i = this.inGeo.vertexCount - 1;
            int i2 = 0;
            boolean z = false;
            while (i2 < this.inGeo.vertexCount) {
                if ((this.inGeo.vertices[(i2 * 3) + 1] > f2) != (this.inGeo.vertices[(i * 3) + 1] > f2) && f < (((this.inGeo.vertices[i * 3] - this.inGeo.vertices[i2 * 3]) * (f2 - this.inGeo.vertices[(i2 * 3) + 1])) / (this.inGeo.vertices[(i * 3) + 1] - this.inGeo.vertices[(i2 * 3) + 1])) + this.inGeo.vertices[i2 * 3]) {
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
    public boolean contextIsOutdated() {
        boolean z = !this.pgl.contextIsCurrent(this.context);
        if (z) {
            this.bufPolyVertex.dispose();
            this.bufPolyColor.dispose();
            this.bufPolyNormal.dispose();
            this.bufPolyTexcoord.dispose();
            this.bufPolyAmbient.dispose();
            this.bufPolySpecular.dispose();
            this.bufPolyEmissive.dispose();
            this.bufPolyShininess.dispose();
            for (PGraphicsOpenGL.VertexAttribute vertexAttribute : this.polyAttribs.values()) {
                vertexAttribute.buf.dispose();
            }
            this.bufPolyIndex.dispose();
            this.bufLineVertex.dispose();
            this.bufLineColor.dispose();
            this.bufLineAttrib.dispose();
            this.bufLineIndex.dispose();
            this.bufPointVertex.dispose();
            this.bufPointColor.dispose();
            this.bufPointAttrib.dispose();
            this.bufPointIndex.dispose();
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void copyLineAttributes(int i, int i2) {
        this.tessGeo.updateLineDirectionsBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufLineAttrib.glId);
        this.tessGeo.lineDirectionsBuffer.position(i * 4);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 4 * PGL.SIZEOF_FLOAT, i2 * 4 * PGL.SIZEOF_FLOAT, this.tessGeo.lineDirectionsBuffer);
        this.tessGeo.lineDirectionsBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyLineColors(int i, int i2) {
        this.tessGeo.updateLineColorsBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufLineColor.glId);
        this.tessGeo.lineColorsBuffer.position(i);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_INT * i, PGL.SIZEOF_INT * i2, this.tessGeo.lineColorsBuffer);
        this.tessGeo.lineColorsBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyLineVertices(int i, int i2) {
        this.tessGeo.updateLineVerticesBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufLineVertex.glId);
        this.tessGeo.lineVerticesBuffer.position(i * 4);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 4 * PGL.SIZEOF_FLOAT, i2 * 4 * PGL.SIZEOF_FLOAT, this.tessGeo.lineVerticesBuffer);
        this.tessGeo.lineVerticesBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyPointAttributes(int i, int i2) {
        this.tessGeo.updatePointOffsetsBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPointAttrib.glId);
        this.tessGeo.pointOffsetsBuffer.position(i * 2);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 2 * PGL.SIZEOF_FLOAT, i2 * 2 * PGL.SIZEOF_FLOAT, this.tessGeo.pointOffsetsBuffer);
        this.tessGeo.pointOffsetsBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyPointColors(int i, int i2) {
        this.tessGeo.updatePointColorsBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPointColor.glId);
        this.tessGeo.pointColorsBuffer.position(i);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_INT * i, PGL.SIZEOF_INT * i2, this.tessGeo.pointColorsBuffer);
        this.tessGeo.pointColorsBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyPointVertices(int i, int i2) {
        this.tessGeo.updatePointVerticesBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPointVertex.glId);
        this.tessGeo.pointVerticesBuffer.position(i * 4);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 4 * PGL.SIZEOF_FLOAT, i2 * 4 * PGL.SIZEOF_FLOAT, this.tessGeo.pointVerticesBuffer);
        this.tessGeo.pointVerticesBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyPolyAmbient(int i, int i2) {
        this.tessGeo.updatePolyAmbientBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyAmbient.glId);
        this.tessGeo.polyAmbientBuffer.position(i);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_INT * i, PGL.SIZEOF_INT * i2, this.tessGeo.polyAmbientBuffer);
        this.tessGeo.polyAmbientBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyPolyAttrib(PGraphicsOpenGL.VertexAttribute vertexAttribute, int i, int i2) {
        this.tessGeo.updateAttribBuffer(vertexAttribute.name, i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, vertexAttribute.buf.glId);
        Buffer buffer = this.tessGeo.polyAttribBuffers.get(vertexAttribute.name);
        buffer.position(vertexAttribute.size * i);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, vertexAttribute.sizeInBytes(i), vertexAttribute.sizeInBytes(i2), buffer);
        buffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyPolyColors(int i, int i2) {
        this.tessGeo.updatePolyColorsBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyColor.glId);
        this.tessGeo.polyColorsBuffer.position(i);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_INT * i, PGL.SIZEOF_INT * i2, this.tessGeo.polyColorsBuffer);
        this.tessGeo.polyColorsBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyPolyEmissive(int i, int i2) {
        this.tessGeo.updatePolyEmissiveBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyEmissive.glId);
        this.tessGeo.polyEmissiveBuffer.position(i);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_INT * i, PGL.SIZEOF_INT * i2, this.tessGeo.polyEmissiveBuffer);
        this.tessGeo.polyEmissiveBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyPolyNormals(int i, int i2) {
        this.tessGeo.updatePolyNormalsBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyNormal.glId);
        this.tessGeo.polyNormalsBuffer.position(i * 3);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 3 * PGL.SIZEOF_FLOAT, i2 * 3 * PGL.SIZEOF_FLOAT, this.tessGeo.polyNormalsBuffer);
        this.tessGeo.polyNormalsBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyPolyShininess(int i, int i2) {
        this.tessGeo.updatePolyShininessBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyShininess.glId);
        this.tessGeo.polyShininessBuffer.position(i);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_FLOAT * i, PGL.SIZEOF_FLOAT * i2, this.tessGeo.polyShininessBuffer);
        this.tessGeo.polyShininessBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyPolySpecular(int i, int i2) {
        this.tessGeo.updatePolySpecularBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolySpecular.glId);
        this.tessGeo.polySpecularBuffer.position(i);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, PGL.SIZEOF_INT * i, PGL.SIZEOF_INT * i2, this.tessGeo.polySpecularBuffer);
        this.tessGeo.polySpecularBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyPolyTexCoords(int i, int i2) {
        this.tessGeo.updatePolyTexCoordsBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyTexcoord.glId);
        this.tessGeo.polyTexCoordsBuffer.position(i * 2);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 2 * PGL.SIZEOF_FLOAT, i2 * 2 * PGL.SIZEOF_FLOAT, this.tessGeo.polyTexCoordsBuffer);
        this.tessGeo.polyTexCoordsBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void copyPolyVertices(int i, int i2) {
        this.tessGeo.updatePolyVerticesBuffer(i, i2);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyVertex.glId);
        this.tessGeo.polyVerticesBuffer.position(i * 4);
        this.pgl.bufferSubData(PGL.ARRAY_BUFFER, i * 4 * PGL.SIZEOF_FLOAT, i2 * 4 * PGL.SIZEOF_FLOAT, this.tessGeo.polyVerticesBuffer);
        this.tessGeo.polyVerticesBuffer.rewind();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    public void curveDetail(int i) {
        this.curveDetail = i;
        if (this.inGeo.codeCount > 0) {
            markForTessellation();
        }
    }

    public void curveTightness(float f) {
        this.curveTightness = f;
        if (this.inGeo.codeCount > 0) {
            markForTessellation();
        }
    }

    public void curveVertex(float f, float f2) {
        curveVertexImpl(f, f2, 0.0f);
    }

    public void curveVertex(float f, float f2, float f3) {
        curveVertexImpl(f, f2, f3);
    }

    /* access modifiers changed from: protected */
    public void curveVertexImpl(float f, float f2, float f3) {
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addCurveVertex(f, f2, f3, vertexBreak());
    }

    public void disableStyle() {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "disableStyle()");
            return;
        }
        this.savedStroke = this.stroke;
        this.savedStrokeColor = this.strokeColor;
        this.savedStrokeWeight = this.strokeWeight;
        this.savedStrokeCap = this.strokeCap;
        this.savedStrokeJoin = this.strokeJoin;
        this.savedFill = this.fill;
        this.savedFillColor = this.fillColor;
        this.savedTint = this.tint;
        this.savedTintColor = this.tintColor;
        this.savedAmbientColor = this.ambientColor;
        this.savedSpecularColor = this.specularColor;
        this.savedEmissiveColor = this.emissiveColor;
        this.savedShininess = this.shininess;
        this.savedTextureMode = this.textureMode;
        super.disableStyle();
    }

    public void draw(PGraphics pGraphics) {
        int i = 0;
        if (pGraphics instanceof PGraphicsOpenGL) {
            PGraphicsOpenGL pGraphicsOpenGL = (PGraphicsOpenGL) pGraphics;
            if (this.visible) {
                pre(pGraphicsOpenGL);
                updateTessellation();
                updateGeometry();
                if (this.family != 0) {
                    render(pGraphicsOpenGL, this.image);
                } else if (fragmentedGroup(pGraphicsOpenGL)) {
                    while (true) {
                        int i2 = i;
                        if (i2 >= this.childCount) {
                            break;
                        }
                        ((PShapeOpenGL) this.children[i2]).draw(pGraphicsOpenGL);
                        i = i2 + 1;
                    }
                } else {
                    render(pGraphicsOpenGL, (this.textures == null || this.textures.size() != 1) ? null : (PImage) this.textures.toArray()[0]);
                }
                post(pGraphicsOpenGL);
                return;
            }
            return;
        }
        super.draw(pGraphics);
    }

    /* access modifiers changed from: protected */
    public void drawGeometry(PGraphics pGraphics) {
        this.vertexCount = this.inGeo.vertexCount;
        this.vertices = this.inGeo.getVertexData();
        super.drawGeometry(pGraphics);
        this.vertexCount = 0;
        this.vertices = null;
    }

    public void enableStyle() {
        if (this.savedStroke) {
            setStroke(true);
            setStroke(this.savedStrokeColor);
            setStrokeWeight(this.savedStrokeWeight);
            setStrokeCap(this.savedStrokeCap);
            setStrokeJoin(this.savedStrokeJoin);
        } else {
            setStroke(false);
        }
        if (this.savedFill) {
            setFill(true);
            setFill(this.savedFillColor);
        } else {
            setFill(false);
        }
        if (this.savedTint) {
            setTint(true);
            setTint(this.savedTintColor);
        }
        setAmbient(this.savedAmbientColor);
        setSpecular(this.savedSpecularColor);
        setEmissive(this.savedEmissiveColor);
        setShininess(this.savedShininess);
        if (this.image != null) {
            setTextureMode(this.savedTextureMode);
        }
        super.enableStyle();
    }

    /* access modifiers changed from: protected */
    public void endContourImpl() {
    }

    public void endShape(int i) {
        super.endShape(i);
        this.inGeo.trim();
        this.close = i == 2;
        markForTessellation();
        this.shapeCreated = true;
    }

    /* access modifiers changed from: protected */
    public boolean fragmentedGroup(PGraphicsOpenGL pGraphicsOpenGL) {
        if (!pGraphicsOpenGL.getHint(6)) {
            return (this.textures != null && (1 < this.textures.size() || this.untexChild)) || this.strokedTexture;
        }
        return true;
    }

    public int getAmbient(int i) {
        if (this.family != 0) {
            return PGL.nativeToJavaARGB(this.inGeo.ambient[i]);
        }
        return 0;
    }

    public float getDepth() {
        PVector pVector = new PVector(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        PVector pVector2 = new PVector(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        if (this.shapeCreated) {
            getVertexMin(pVector);
            getVertexMax(pVector2);
        }
        this.depth = pVector2.z - pVector.z;
        return this.depth;
    }

    public int getEmissive(int i) {
        if (this.family == 0) {
            return PGL.nativeToJavaARGB(this.inGeo.emissive[i]);
        }
        return 0;
    }

    public int getFill(int i) {
        if (this.family == 0 || this.image != null) {
            return 0;
        }
        return PGL.nativeToJavaARGB(this.inGeo.colors[i]);
    }

    public float getHeight() {
        PVector pVector = new PVector(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        PVector pVector2 = new PVector(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        if (this.shapeCreated) {
            getVertexMin(pVector);
            getVertexMax(pVector2);
        }
        this.height = pVector2.y - pVector.y;
        return this.height;
    }

    public PVector getNormal(int i, PVector pVector) {
        if (pVector == null) {
            pVector = new PVector();
        }
        pVector.x = this.inGeo.normals[(i * 3) + 0];
        pVector.y = this.inGeo.normals[(i * 3) + 1];
        pVector.z = this.inGeo.normals[(i * 3) + 2];
        return pVector;
    }

    public float getNormalX(int i) {
        return this.inGeo.normals[(i * 3) + 0];
    }

    public float getNormalY(int i) {
        return this.inGeo.normals[(i * 3) + 1];
    }

    public float getNormalZ(int i) {
        return this.inGeo.normals[(i * 3) + 2];
    }

    public float getShininess(int i) {
        if (this.family == 0) {
            return this.inGeo.shininess[i];
        }
        return 0.0f;
    }

    public int getSpecular(int i) {
        if (this.family == 0) {
            return PGL.nativeToJavaARGB(this.inGeo.specular[i]);
        }
        return 0;
    }

    public int getStroke(int i) {
        if (this.family != 0) {
            return PGL.nativeToJavaARGB(this.inGeo.strokeColors[i]);
        }
        return 0;
    }

    public float getStrokeWeight(int i) {
        if (this.family != 0) {
            return this.inGeo.strokeWeights[i];
        }
        return 0.0f;
    }

    public PShape getTessellation() {
        updateTessellation();
        float[] fArr = this.tessGeo.polyVertices;
        float[] fArr2 = this.tessGeo.polyNormals;
        int[] iArr = this.tessGeo.polyColors;
        float[] fArr3 = this.tessGeo.polyTexCoords;
        short[] sArr = this.tessGeo.polyIndices;
        PShape createShapeFamily = this.pg.createShapeFamily(3);
        createShapeFamily.set3D(this.is3D);
        createShapeFamily.beginShape(9);
        createShapeFamily.noStroke();
        PGraphicsOpenGL.IndexCache indexCache = this.tessGeo.polyIndexCache;
        int i = this.firstPolyIndexCache;
        while (true) {
            int i2 = i;
            if (i2 <= this.lastPolyIndexCache) {
                int i3 = indexCache.indexOffset[i2];
                int i4 = indexCache.indexCount[i2];
                int i5 = indexCache.vertexOffset[i2];
                int i6 = i3 / 3;
                while (true) {
                    int i7 = i6;
                    if (i7 >= (i3 + i4) / 3) {
                        break;
                    }
                    int i8 = i5 + sArr[(i7 * 3) + 0];
                    int i9 = i5 + sArr[(i7 * 3) + 1];
                    int i10 = i5 + sArr[(i7 * 3) + 2];
                    if (is3D()) {
                        float f = fArr[(i8 * 4) + 0];
                        float f2 = fArr[(i8 * 4) + 1];
                        float f3 = fArr[(i8 * 4) + 2];
                        float f4 = fArr[(i9 * 4) + 0];
                        float f5 = fArr[(i9 * 4) + 1];
                        float f6 = fArr[(i9 * 4) + 2];
                        float f7 = fArr[(i10 * 4) + 0];
                        float f8 = fArr[(i10 * 4) + 1];
                        float f9 = fArr[(i10 * 4) + 2];
                        float f10 = fArr2[(i8 * 3) + 0];
                        float f11 = fArr2[(i8 * 3) + 1];
                        float f12 = fArr2[(i8 * 3) + 2];
                        float f13 = fArr2[(i9 * 3) + 0];
                        float f14 = fArr2[(i9 * 3) + 1];
                        float f15 = fArr2[(i9 * 3) + 2];
                        float f16 = fArr2[(i10 * 3) + 0];
                        float f17 = fArr2[(i10 * 3) + 1];
                        float f18 = fArr2[(i10 * 3) + 2];
                        int nativeToJavaARGB = PGL.nativeToJavaARGB(iArr[i8]);
                        int nativeToJavaARGB2 = PGL.nativeToJavaARGB(iArr[i9]);
                        int nativeToJavaARGB3 = PGL.nativeToJavaARGB(iArr[i10]);
                        createShapeFamily.fill(nativeToJavaARGB);
                        createShapeFamily.normal(f10, f11, f12);
                        createShapeFamily.vertex(f, f2, f3, fArr3[(i8 * 2) + 0], fArr3[(i8 * 2) + 1]);
                        createShapeFamily.fill(nativeToJavaARGB2);
                        createShapeFamily.normal(f13, f14, f15);
                        createShapeFamily.vertex(f4, f5, f6, fArr3[(i9 * 2) + 0], fArr3[(i9 * 2) + 1]);
                        createShapeFamily.fill(nativeToJavaARGB3);
                        createShapeFamily.normal(f16, f17, f18);
                        createShapeFamily.vertex(f7, f8, f9, fArr3[(i10 * 2) + 0], fArr3[(i10 * 2) + 1]);
                    } else if (is2D()) {
                        float f19 = fArr[(i8 * 4) + 0];
                        float f20 = fArr[(i8 * 4) + 1];
                        float f21 = fArr[(i9 * 4) + 0];
                        float f22 = fArr[(i9 * 4) + 1];
                        float f23 = fArr[(i10 * 4) + 0];
                        float f24 = fArr[(i10 * 4) + 1];
                        int nativeToJavaARGB4 = PGL.nativeToJavaARGB(iArr[i8]);
                        int nativeToJavaARGB5 = PGL.nativeToJavaARGB(iArr[i9]);
                        int nativeToJavaARGB6 = PGL.nativeToJavaARGB(iArr[i10]);
                        createShapeFamily.fill(nativeToJavaARGB4);
                        createShapeFamily.vertex(f19, f20, fArr3[(i8 * 2) + 0], fArr3[(i8 * 2) + 1]);
                        createShapeFamily.fill(nativeToJavaARGB5);
                        createShapeFamily.vertex(f21, f22, fArr3[(i9 * 2) + 0], fArr3[(i9 * 2) + 1]);
                        createShapeFamily.fill(nativeToJavaARGB6);
                        createShapeFamily.vertex(f23, f24, fArr3[(i10 * 2) + 0], fArr3[(i10 * 2) + 1]);
                    }
                    i6 = i7 + 1;
                }
                i = i2 + 1;
            } else {
                createShapeFamily.endShape();
                return createShapeFamily;
            }
        }
    }

    public float[] getTessellation(int i, int i2) {
        updateTessellation();
        if (i == 9) {
            if (i2 == 0) {
                if (is3D()) {
                    this.root.setModifiedPolyVertices(this.firstPolyVertex, this.lastPolyVertex);
                } else if (is2D()) {
                    int i3 = this.lastPolyVertex + 1;
                    if (-1 < this.firstLineVertex) {
                        i3 = this.firstLineVertex;
                    }
                    if (-1 < this.firstPointVertex) {
                        i3 = this.firstPointVertex;
                    }
                    this.root.setModifiedPolyVertices(this.firstPolyVertex, i3 - 1);
                }
                return this.tessGeo.polyVertices;
            } else if (i2 == 1) {
                if (is3D()) {
                    this.root.setModifiedPolyNormals(this.firstPolyVertex, this.lastPolyVertex);
                } else if (is2D()) {
                    int i4 = this.lastPolyVertex + 1;
                    if (-1 < this.firstLineVertex) {
                        i4 = this.firstLineVertex;
                    }
                    if (-1 < this.firstPointVertex) {
                        i4 = this.firstPointVertex;
                    }
                    this.root.setModifiedPolyNormals(this.firstPolyVertex, i4 - 1);
                }
                return this.tessGeo.polyNormals;
            } else if (i2 == 2) {
                if (is3D()) {
                    this.root.setModifiedPolyTexCoords(this.firstPolyVertex, this.lastPolyVertex);
                } else if (is2D()) {
                    int i5 = this.lastPolyVertex + 1;
                    if (-1 < this.firstLineVertex) {
                        i5 = this.firstLineVertex;
                    }
                    if (-1 < this.firstPointVertex) {
                        i5 = this.firstPointVertex;
                    }
                    this.root.setModifiedPolyTexCoords(this.firstPolyVertex, i5 - 1);
                }
                return this.tessGeo.polyTexCoords;
            }
        } else if (i == 5) {
            if (i2 == 0) {
                if (is3D()) {
                    this.root.setModifiedLineVertices(this.firstLineVertex, this.lastLineVertex);
                } else if (is2D()) {
                    this.root.setModifiedPolyVertices(this.firstLineVertex, this.lastLineVertex);
                }
                return this.tessGeo.lineVertices;
            } else if (i2 == 3) {
                if (is2D()) {
                    this.root.setModifiedLineAttributes(this.firstLineVertex, this.lastLineVertex);
                }
                return this.tessGeo.lineDirections;
            }
        } else if (i == 3) {
            if (i2 == 0) {
                if (is3D()) {
                    this.root.setModifiedPointVertices(this.firstPointVertex, this.lastPointVertex);
                } else if (is2D()) {
                    this.root.setModifiedPolyVertices(this.firstPointVertex, this.lastPointVertex);
                }
                return this.tessGeo.pointVertices;
            } else if (i2 == 4) {
                if (is2D()) {
                    this.root.setModifiedPointAttributes(this.firstPointVertex, this.lastPointVertex);
                }
                return this.tessGeo.pointOffsets;
            }
        }
        return null;
    }

    public float getTextureU(int i) {
        return this.inGeo.texcoords[(i * 2) + 0];
    }

    public float getTextureV(int i) {
        return this.inGeo.texcoords[(i * 2) + 1];
    }

    public int getTint(int i) {
        if (this.family == 0 || this.image == null) {
            return 0;
        }
        return PGL.nativeToJavaARGB(this.inGeo.colors[i]);
    }

    public PVector getVertex(int i, PVector pVector) {
        if (pVector == null) {
            pVector = new PVector();
        }
        pVector.x = this.inGeo.vertices[(i * 3) + 0];
        pVector.y = this.inGeo.vertices[(i * 3) + 1];
        pVector.z = this.inGeo.vertices[(i * 3) + 2];
        return pVector;
    }

    public int getVertexCode(int i) {
        return this.inGeo.codes[i];
    }

    public int getVertexCodeCount() {
        if (this.family == 0) {
            return 0;
        }
        if (this.family == 1 || this.family == 2) {
            updateTessellation();
        }
        return this.inGeo.codeCount;
    }

    public int[] getVertexCodes() {
        if (this.family == 0) {
            return null;
        }
        if (this.family == 1 || this.family == 2) {
            updateTessellation();
        }
        if (this.inGeo.codes != null) {
            return this.inGeo.codes;
        }
        return null;
    }

    public int getVertexCount() {
        if (this.family == 0) {
            return 0;
        }
        if (this.family == 1 || this.family == 2) {
            updateTessellation();
        }
        return this.inGeo.vertexCount;
    }

    /* access modifiers changed from: protected */
    public void getVertexMax(PVector pVector) {
        updateTessellation();
        if (this.family == 0) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.childCount) {
                    ((PShapeOpenGL) this.children[i2]).getVertexMax(pVector);
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else {
            if (this.hasPolys) {
                this.tessGeo.getPolyVertexMax(pVector, this.firstPolyVertex, this.lastPolyVertex);
            }
            if (is3D()) {
                if (this.hasLines) {
                    this.tessGeo.getLineVertexMax(pVector, this.firstLineVertex, this.lastLineVertex);
                }
                if (this.hasPoints) {
                    this.tessGeo.getPointVertexMax(pVector, this.firstPointVertex, this.lastPointVertex);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void getVertexMin(PVector pVector) {
        updateTessellation();
        if (this.family == 0) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.childCount) {
                    ((PShapeOpenGL) this.children[i2]).getVertexMin(pVector);
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else {
            if (this.hasPolys) {
                this.tessGeo.getPolyVertexMin(pVector, this.firstPolyVertex, this.lastPolyVertex);
            }
            if (is3D()) {
                if (this.hasLines) {
                    this.tessGeo.getLineVertexMin(pVector, this.firstLineVertex, this.lastLineVertex);
                }
                if (this.hasPoints) {
                    this.tessGeo.getPointVertexMin(pVector, this.firstPointVertex, this.lastPointVertex);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public int getVertexSum(PVector pVector, int i) {
        updateTessellation();
        if (this.family == 0) {
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 >= this.childCount) {
                    return i;
                }
                i += ((PShapeOpenGL) this.children[i3]).getVertexSum(pVector, i);
                i2 = i3 + 1;
            }
        } else {
            if (this.hasPolys) {
                i += this.tessGeo.getPolyVertexSum(pVector, this.firstPolyVertex, this.lastPolyVertex);
            }
            if (!is3D()) {
                return i;
            }
            if (this.hasLines) {
                i += this.tessGeo.getLineVertexSum(pVector, this.firstLineVertex, this.lastLineVertex);
            }
            return this.hasPoints ? i + this.tessGeo.getPointVertexSum(pVector, this.firstPointVertex, this.lastPointVertex) : i;
        }
    }

    public float getVertexX(int i) {
        return this.inGeo.vertices[(i * 3) + 0];
    }

    public float getVertexY(int i) {
        return this.inGeo.vertices[(i * 3) + 1];
    }

    public float getVertexZ(int i) {
        return this.inGeo.vertices[(i * 3) + 2];
    }

    public float getWidth() {
        PVector pVector = new PVector(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        PVector pVector2 = new PVector(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        if (this.shapeCreated) {
            getVertexMin(pVector);
            getVertexMax(pVector2);
        }
        this.width = pVector2.x - pVector.x;
        return this.width;
    }

    /* access modifiers changed from: protected */
    public boolean hasStrokedTexture() {
        return this.family == 0 ? this.strokedTexture : this.image != null && this.stroke;
    }

    /* access modifiers changed from: protected */
    public boolean hasTexture() {
        return this.family == 0 ? this.textures != null && this.textures.size() > 0 : this.image != null;
    }

    /* access modifiers changed from: protected */
    public boolean hasTexture(PImage pImage) {
        return this.family == 0 ? this.textures != null && this.textures.contains(pImage) : this.image == pImage;
    }

    /* access modifiers changed from: protected */
    public void initBuffers() {
        boolean contextIsOutdated = contextIsOutdated();
        this.context = this.pgl.getCurrentContext();
        if (this.hasPolys && (this.needBufferInit || contextIsOutdated)) {
            initPolyBuffers();
        }
        if (this.hasLines && (this.needBufferInit || contextIsOutdated)) {
            initLineBuffers();
        }
        if (this.hasPoints && (this.needBufferInit || contextIsOutdated)) {
            initPointBuffers();
        }
        this.needBufferInit = false;
    }

    /* access modifiers changed from: protected */
    public void initLineBuffers() {
        int i = this.tessGeo.lineVertexCount;
        int i2 = PGL.SIZEOF_FLOAT * i;
        int i3 = i * PGL.SIZEOF_INT;
        this.tessGeo.updateLineVerticesBuffer();
        if (this.bufLineVertex == null) {
            this.bufLineVertex = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 4, PGL.SIZEOF_FLOAT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufLineVertex.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 4, this.tessGeo.lineVerticesBuffer, this.glUsage);
        this.tessGeo.updateLineColorsBuffer();
        if (this.bufLineColor == null) {
            this.bufLineColor = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufLineColor.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i3, this.tessGeo.lineColorsBuffer, this.glUsage);
        this.tessGeo.updateLineDirectionsBuffer();
        if (this.bufLineAttrib == null) {
            this.bufLineAttrib = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 4, PGL.SIZEOF_FLOAT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufLineAttrib.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 4, this.tessGeo.lineDirectionsBuffer, this.glUsage);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        this.tessGeo.updateLineIndicesBuffer();
        if (this.bufLineIndex == null) {
            this.bufLineIndex = new VertexBuffer(this.pg, PGL.ELEMENT_ARRAY_BUFFER, 1, PGL.SIZEOF_INDEX, true);
        }
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, this.bufLineIndex.glId);
        this.pgl.bufferData(PGL.ELEMENT_ARRAY_BUFFER, this.tessGeo.lineIndexCount * PGL.SIZEOF_INDEX, this.tessGeo.lineIndicesBuffer, this.glUsage);
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void initModified() {
        this.modified = false;
        this.modifiedPolyVertices = false;
        this.modifiedPolyColors = false;
        this.modifiedPolyNormals = false;
        this.modifiedPolyTexCoords = false;
        this.modifiedPolyAmbient = false;
        this.modifiedPolySpecular = false;
        this.modifiedPolyEmissive = false;
        this.modifiedPolyShininess = false;
        this.modifiedLineVertices = false;
        this.modifiedLineColors = false;
        this.modifiedLineAttributes = false;
        this.modifiedPointVertices = false;
        this.modifiedPointColors = false;
        this.modifiedPointAttributes = false;
        this.firstModifiedPolyVertex = PConstants.MAX_INT;
        this.lastModifiedPolyVertex = Integer.MIN_VALUE;
        this.firstModifiedPolyColor = PConstants.MAX_INT;
        this.lastModifiedPolyColor = Integer.MIN_VALUE;
        this.firstModifiedPolyNormal = PConstants.MAX_INT;
        this.lastModifiedPolyNormal = Integer.MIN_VALUE;
        this.firstModifiedPolyTexcoord = PConstants.MAX_INT;
        this.lastModifiedPolyTexcoord = Integer.MIN_VALUE;
        this.firstModifiedPolyAmbient = PConstants.MAX_INT;
        this.lastModifiedPolyAmbient = Integer.MIN_VALUE;
        this.firstModifiedPolySpecular = PConstants.MAX_INT;
        this.lastModifiedPolySpecular = Integer.MIN_VALUE;
        this.firstModifiedPolyEmissive = PConstants.MAX_INT;
        this.lastModifiedPolyEmissive = Integer.MIN_VALUE;
        this.firstModifiedPolyShininess = PConstants.MAX_INT;
        this.lastModifiedPolyShininess = Integer.MIN_VALUE;
        this.firstModifiedLineVertex = PConstants.MAX_INT;
        this.lastModifiedLineVertex = Integer.MIN_VALUE;
        this.firstModifiedLineColor = PConstants.MAX_INT;
        this.lastModifiedLineColor = Integer.MIN_VALUE;
        this.firstModifiedLineAttribute = PConstants.MAX_INT;
        this.lastModifiedLineAttribute = Integer.MIN_VALUE;
        this.firstModifiedPointVertex = PConstants.MAX_INT;
        this.lastModifiedPointVertex = Integer.MIN_VALUE;
        this.firstModifiedPointColor = PConstants.MAX_INT;
        this.lastModifiedPointColor = Integer.MIN_VALUE;
        this.firstModifiedPointAttribute = PConstants.MAX_INT;
        this.lastModifiedPointAttribute = Integer.MIN_VALUE;
    }

    /* access modifiers changed from: protected */
    public void initPointBuffers() {
        int i = this.tessGeo.pointVertexCount;
        int i2 = PGL.SIZEOF_FLOAT * i;
        int i3 = i * PGL.SIZEOF_INT;
        this.tessGeo.updatePointVerticesBuffer();
        if (this.bufPointVertex == null) {
            this.bufPointVertex = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 4, PGL.SIZEOF_FLOAT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPointVertex.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 4, this.tessGeo.pointVerticesBuffer, this.glUsage);
        this.tessGeo.updatePointColorsBuffer();
        if (this.bufPointColor == null) {
            this.bufPointColor = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPointColor.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i3, this.tessGeo.pointColorsBuffer, this.glUsage);
        this.tessGeo.updatePointOffsetsBuffer();
        if (this.bufPointAttrib == null) {
            this.bufPointAttrib = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 2, PGL.SIZEOF_FLOAT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPointAttrib.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 2, this.tessGeo.pointOffsetsBuffer, this.glUsage);
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        this.tessGeo.updatePointIndicesBuffer();
        if (this.bufPointIndex == null) {
            this.bufPointIndex = new VertexBuffer(this.pg, PGL.ELEMENT_ARRAY_BUFFER, 1, PGL.SIZEOF_INDEX, true);
        }
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, this.bufPointIndex.glId);
        this.pgl.bufferData(PGL.ELEMENT_ARRAY_BUFFER, this.tessGeo.pointIndexCount * PGL.SIZEOF_INDEX, this.tessGeo.pointIndicesBuffer, this.glUsage);
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void initPolyBuffers() {
        int i = this.tessGeo.polyVertexCount;
        int i2 = PGL.SIZEOF_FLOAT * i;
        int i3 = PGL.SIZEOF_INT * i;
        this.tessGeo.updatePolyVerticesBuffer();
        if (this.bufPolyVertex == null) {
            this.bufPolyVertex = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 4, PGL.SIZEOF_FLOAT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyVertex.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 4, this.tessGeo.polyVerticesBuffer, this.glUsage);
        this.tessGeo.updatePolyColorsBuffer();
        if (this.bufPolyColor == null) {
            this.bufPolyColor = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyColor.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i3, this.tessGeo.polyColorsBuffer, this.glUsage);
        this.tessGeo.updatePolyNormalsBuffer();
        if (this.bufPolyNormal == null) {
            this.bufPolyNormal = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 3, PGL.SIZEOF_FLOAT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyNormal.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 3, this.tessGeo.polyNormalsBuffer, this.glUsage);
        this.tessGeo.updatePolyTexCoordsBuffer();
        if (this.bufPolyTexcoord == null) {
            this.bufPolyTexcoord = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 2, PGL.SIZEOF_FLOAT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyTexcoord.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 2, this.tessGeo.polyTexCoordsBuffer, this.glUsage);
        this.tessGeo.updatePolyAmbientBuffer();
        if (this.bufPolyAmbient == null) {
            this.bufPolyAmbient = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyAmbient.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i3, this.tessGeo.polyAmbientBuffer, this.glUsage);
        this.tessGeo.updatePolySpecularBuffer();
        if (this.bufPolySpecular == null) {
            this.bufPolySpecular = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolySpecular.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i3, this.tessGeo.polySpecularBuffer, this.glUsage);
        this.tessGeo.updatePolyEmissiveBuffer();
        if (this.bufPolyEmissive == null) {
            this.bufPolyEmissive = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyEmissive.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i3, this.tessGeo.polyEmissiveBuffer, this.glUsage);
        this.tessGeo.updatePolyShininessBuffer();
        if (this.bufPolyShininess == null) {
            this.bufPolyShininess = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_FLOAT);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyShininess.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i2, this.tessGeo.polyShininessBuffer, this.glUsage);
        for (String str : this.polyAttribs.keySet()) {
            PGraphicsOpenGL.VertexAttribute vertexAttribute = (PGraphicsOpenGL.VertexAttribute) this.polyAttribs.get(str);
            this.tessGeo.updateAttribBuffer(vertexAttribute.name);
            if (!vertexAttribute.bufferCreated()) {
                vertexAttribute.createBuffer(this.pgl);
            }
            this.pgl.bindBuffer(PGL.ARRAY_BUFFER, vertexAttribute.buf.glId);
            this.pgl.bufferData(PGL.ARRAY_BUFFER, vertexAttribute.sizeInBytes(i), this.tessGeo.polyAttribBuffers.get(str), this.glUsage);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        this.tessGeo.updatePolyIndicesBuffer();
        if (this.bufPolyIndex == null) {
            this.bufPolyIndex = new VertexBuffer(this.pg, PGL.ELEMENT_ARRAY_BUFFER, 1, PGL.SIZEOF_INDEX, true);
        }
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, this.bufPolyIndex.glId);
        this.pgl.bufferData(PGL.ELEMENT_ARRAY_BUFFER, this.tessGeo.polyIndexCount * PGL.SIZEOF_INDEX, this.tessGeo.polyIndicesBuffer, this.glUsage);
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void markForTessellation() {
        this.root.tessellated = false;
        this.tessellated = false;
    }

    public void normal(float f, float f2, float f3) {
        if (!this.openShape) {
            PGraphics.showWarning(PShape.OUTSIDE_BEGIN_END_ERROR, "normal()");
        } else if (this.family == 0) {
            PGraphics.showWarning("Cannot set normal in GROUP shape");
        } else {
            this.normalX = f;
            this.normalY = f2;
            this.normalZ = f3;
            if (this.normalMode == 0) {
                this.normalMode = 1;
            } else if (this.normalMode == 1) {
                this.normalMode = 2;
            }
        }
    }

    /* access modifiers changed from: protected */
    public PMatrix popTransform() {
        if (this.transformStack == null || this.transformStack.size() == 0) {
            return null;
        }
        return this.transformStack.pop();
    }

    /* access modifiers changed from: protected */
    public void post(PGraphics pGraphics) {
        if (!(pGraphics instanceof PGraphicsOpenGL)) {
            super.post(pGraphics);
        }
    }

    /* access modifiers changed from: protected */
    public void pre(PGraphics pGraphics) {
        if (!(pGraphics instanceof PGraphicsOpenGL)) {
            super.pre(pGraphics);
        } else if (!this.style) {
            styles(pGraphics);
        }
    }

    /* access modifiers changed from: protected */
    public void pushTransform() {
        if (this.transformStack == null) {
            this.transformStack = new Stack<>();
        }
        PMatrix pMatrix2D = this.transform instanceof PMatrix2D ? new PMatrix2D() : new PMatrix3D();
        pMatrix2D.set(this.transform);
        this.transformStack.push(pMatrix2D);
    }

    public void quadraticVertex(float f, float f2, float f3, float f4) {
        quadraticVertexImpl(f, f2, 0.0f, f3, f4, 0.0f);
    }

    public void quadraticVertex(float f, float f2, float f3, float f4, float f5, float f6) {
        quadraticVertexImpl(f, f2, f3, f4, f5, f6);
    }

    /* access modifiers changed from: protected */
    public void quadraticVertexImpl(float f, float f2, float f3, float f4, float f5, float f6) {
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addQuadraticVertex(f, f2, f3, f4, f5, f6, vertexBreak());
    }

    /* access modifiers changed from: protected */
    public void rawLines(PGraphicsOpenGL pGraphicsOpenGL) {
        PGraphics raw = pGraphicsOpenGL.getRaw();
        raw.colorMode(1);
        raw.noFill();
        raw.strokeCap(this.strokeCap);
        raw.strokeJoin(this.strokeJoin);
        raw.beginShape(5);
        float[] fArr = this.tessGeo.lineVertices;
        int[] iArr = this.tessGeo.lineColors;
        float[] fArr2 = this.tessGeo.lineDirections;
        short[] sArr = this.tessGeo.lineIndices;
        PGraphicsOpenGL.IndexCache indexCache = this.tessGeo.lineIndexCache;
        for (int i = this.firstLineIndexCache; i <= this.lastLineIndexCache; i++) {
            int i2 = indexCache.indexOffset[i];
            int i3 = indexCache.indexCount[i];
            int i4 = indexCache.vertexOffset[i];
            for (int i5 = i2 / 6; i5 < (i2 + i3) / 6; i5++) {
                int i6 = sArr[(i5 * 6) + 0] + i4;
                int i7 = sArr[(i5 * 6) + 5] + i4;
                float f = 2.0f * fArr2[(i6 * 4) + 3];
                float f2 = 2.0f * fArr2[(i7 * 4) + 3];
                if (!PGraphicsOpenGL.zero(f)) {
                    float[] fArr3 = {0.0f, 0.0f, 0.0f, 0.0f};
                    float[] fArr4 = {0.0f, 0.0f, 0.0f, 0.0f};
                    float[] fArr5 = {0.0f, 0.0f, 0.0f, 0.0f};
                    float[] fArr6 = {0.0f, 0.0f, 0.0f, 0.0f};
                    int nativeToJavaARGB = PGL.nativeToJavaARGB(iArr[i6]);
                    int nativeToJavaARGB2 = PGL.nativeToJavaARGB(iArr[i7]);
                    PApplet.arrayCopy(fArr, i6 * 4, fArr3, 0, 4);
                    PApplet.arrayCopy(fArr, i7 * 4, fArr4, 0, 4);
                    pGraphicsOpenGL.modelview.mult(fArr3, fArr5);
                    pGraphicsOpenGL.modelview.mult(fArr4, fArr6);
                    if (raw.is3D()) {
                        raw.strokeWeight(f);
                        raw.stroke(nativeToJavaARGB);
                        raw.vertex(fArr5[0], fArr5[1], fArr5[2]);
                        raw.strokeWeight(f2);
                        raw.stroke(nativeToJavaARGB2);
                        raw.vertex(fArr6[0], fArr6[1], fArr6[2]);
                    } else if (raw.is2D()) {
                        float screenXImpl = pGraphicsOpenGL.screenXImpl(fArr5[0], fArr5[1], fArr5[2], fArr5[3]);
                        float screenYImpl = pGraphicsOpenGL.screenYImpl(fArr5[0], fArr5[1], fArr5[2], fArr5[3]);
                        float screenXImpl2 = pGraphicsOpenGL.screenXImpl(fArr6[0], fArr6[1], fArr6[2], fArr6[3]);
                        float screenYImpl2 = pGraphicsOpenGL.screenYImpl(fArr6[0], fArr6[1], fArr6[2], fArr6[3]);
                        raw.strokeWeight(f);
                        raw.stroke(nativeToJavaARGB);
                        raw.vertex(screenXImpl, screenYImpl);
                        raw.strokeWeight(f2);
                        raw.stroke(nativeToJavaARGB2);
                        raw.vertex(screenXImpl2, screenYImpl2);
                    }
                }
            }
        }
        raw.endShape();
    }

    /* access modifiers changed from: protected */
    public void rawPoints(PGraphicsOpenGL pGraphicsOpenGL) {
        float f;
        int i;
        PGraphics raw = pGraphicsOpenGL.getRaw();
        raw.colorMode(1);
        raw.noFill();
        raw.strokeCap(this.strokeCap);
        raw.beginShape(3);
        float[] fArr = this.tessGeo.pointVertices;
        int[] iArr = this.tessGeo.pointColors;
        float[] fArr2 = this.tessGeo.pointOffsets;
        short[] sArr = this.tessGeo.pointIndices;
        PGraphicsOpenGL.IndexCache indexCache = this.tessGeo.pointIndexCache;
        for (int i2 = 0; i2 < indexCache.size; i2++) {
            int i3 = indexCache.indexOffset[i2];
            int i4 = indexCache.indexCount[i2];
            int i5 = indexCache.vertexOffset[i2];
            int i6 = i3;
            while (i6 < (i3 + i4) / 3) {
                float f2 = fArr2[(i6 * 2) + 2];
                if (0.0f < f2) {
                    f = f2 / 0.5f;
                    i = PApplet.min(200, PApplet.max(20, (int) ((6.2831855f * f) / 10.0f))) + 1;
                } else {
                    f = (-f2) / 0.5f;
                    i = 5;
                }
                int i7 = sArr[i6 * 3] + i5;
                int nativeToJavaARGB = PGL.nativeToJavaARGB(iArr[i7]);
                float[] fArr3 = {0.0f, 0.0f, 0.0f, 0.0f};
                float[] fArr4 = {0.0f, 0.0f, 0.0f, 0.0f};
                PApplet.arrayCopy(fArr, i7 * 4, fArr4, 0, 4);
                pGraphicsOpenGL.modelview.mult(fArr4, fArr3);
                if (raw.is3D()) {
                    raw.strokeWeight(f);
                    raw.stroke(nativeToJavaARGB);
                    raw.vertex(fArr3[0], fArr3[1], fArr3[2]);
                } else if (raw.is2D()) {
                    float screenXImpl = pGraphicsOpenGL.screenXImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                    float screenYImpl = pGraphicsOpenGL.screenYImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                    raw.strokeWeight(f);
                    raw.stroke(nativeToJavaARGB);
                    raw.vertex(screenXImpl, screenYImpl);
                }
                i6 += i;
            }
        }
        raw.endShape();
    }

    /* access modifiers changed from: protected */
    public void rawPolys(PGraphicsOpenGL pGraphicsOpenGL, PImage pImage) {
        PGraphics raw = pGraphicsOpenGL.getRaw();
        raw.colorMode(1);
        raw.noStroke();
        raw.beginShape(9);
        float[] fArr = this.tessGeo.polyVertices;
        int[] iArr = this.tessGeo.polyColors;
        float[] fArr2 = this.tessGeo.polyTexCoords;
        short[] sArr = this.tessGeo.polyIndices;
        PGraphicsOpenGL.IndexCache indexCache = this.tessGeo.polyIndexCache;
        int i = this.firstPolyIndexCache;
        while (true) {
            int i2 = i;
            if (i2 <= this.lastPolyIndexCache) {
                int i3 = indexCache.indexOffset[i2];
                int i4 = indexCache.indexCount[i2];
                int i5 = indexCache.vertexOffset[i2];
                int i6 = i3 / 3;
                while (true) {
                    int i7 = i6;
                    if (i7 >= (i3 + i4) / 3) {
                        break;
                    }
                    int i8 = i5 + sArr[(i7 * 3) + 0];
                    int i9 = i5 + sArr[(i7 * 3) + 1];
                    int i10 = i5 + sArr[(i7 * 3) + 2];
                    float[] fArr3 = {0.0f, 0.0f, 0.0f, 0.0f};
                    float[] fArr4 = {0.0f, 0.0f, 0.0f, 0.0f};
                    float[] fArr5 = {0.0f, 0.0f, 0.0f, 0.0f};
                    float[] fArr6 = {0.0f, 0.0f, 0.0f, 0.0f};
                    float[] fArr7 = {0.0f, 0.0f, 0.0f, 0.0f};
                    float[] fArr8 = {0.0f, 0.0f, 0.0f, 0.0f};
                    int nativeToJavaARGB = PGL.nativeToJavaARGB(iArr[i8]);
                    int nativeToJavaARGB2 = PGL.nativeToJavaARGB(iArr[i9]);
                    int nativeToJavaARGB3 = PGL.nativeToJavaARGB(iArr[i10]);
                    PApplet.arrayCopy(fArr, i8 * 4, fArr3, 0, 4);
                    PApplet.arrayCopy(fArr, i9 * 4, fArr4, 0, 4);
                    PApplet.arrayCopy(fArr, i10 * 4, fArr5, 0, 4);
                    pGraphicsOpenGL.modelview.mult(fArr3, fArr6);
                    pGraphicsOpenGL.modelview.mult(fArr4, fArr7);
                    pGraphicsOpenGL.modelview.mult(fArr5, fArr8);
                    if (pImage != null) {
                        raw.texture(pImage);
                        if (raw.is3D()) {
                            raw.fill(nativeToJavaARGB);
                            raw.vertex(fArr6[0], fArr6[1], fArr6[2], fArr2[(i8 * 2) + 0], fArr2[(i8 * 2) + 1]);
                            raw.fill(nativeToJavaARGB2);
                            raw.vertex(fArr7[0], fArr7[1], fArr7[2], fArr2[(i9 * 2) + 0], fArr2[(i9 * 2) + 1]);
                            raw.fill(nativeToJavaARGB3);
                            raw.vertex(fArr8[0], fArr8[1], fArr8[2], fArr2[(i10 * 2) + 0], fArr2[(i10 * 2) + 1]);
                        } else if (raw.is2D()) {
                            float screenXImpl = pGraphicsOpenGL.screenXImpl(fArr6[0], fArr6[1], fArr6[2], fArr6[3]);
                            PGraphicsOpenGL pGraphicsOpenGL2 = pGraphicsOpenGL;
                            float screenYImpl = pGraphicsOpenGL2.screenYImpl(fArr6[0], fArr6[1], fArr6[2], fArr6[3]);
                            float screenXImpl2 = pGraphicsOpenGL.screenXImpl(fArr7[0], fArr7[1], fArr7[2], fArr7[3]);
                            float screenYImpl2 = pGraphicsOpenGL.screenYImpl(fArr7[0], fArr7[1], fArr7[2], fArr7[3]);
                            float screenXImpl3 = pGraphicsOpenGL.screenXImpl(fArr8[0], fArr8[1], fArr8[2], fArr8[3]);
                            float screenYImpl3 = pGraphicsOpenGL.screenYImpl(fArr8[0], fArr8[1], fArr8[2], fArr8[3]);
                            raw.fill(nativeToJavaARGB);
                            raw.vertex(screenXImpl, screenYImpl, fArr2[(i8 * 2) + 0], fArr2[(i8 * 2) + 1]);
                            raw.fill(nativeToJavaARGB2);
                            raw.vertex(screenXImpl2, screenYImpl2, fArr2[(i9 * 2) + 0], fArr2[(i9 * 2) + 1]);
                            raw.fill(nativeToJavaARGB2);
                            raw.vertex(screenXImpl3, screenYImpl3, fArr2[(i10 * 2) + 0], fArr2[(i10 * 2) + 1]);
                        }
                    } else if (raw.is3D()) {
                        raw.fill(nativeToJavaARGB);
                        raw.vertex(fArr6[0], fArr6[1], fArr6[2]);
                        raw.fill(nativeToJavaARGB2);
                        raw.vertex(fArr7[0], fArr7[1], fArr7[2]);
                        raw.fill(nativeToJavaARGB3);
                        raw.vertex(fArr8[0], fArr8[1], fArr8[2]);
                    } else if (raw.is2D()) {
                        float screenXImpl4 = pGraphicsOpenGL.screenXImpl(fArr6[0], fArr6[1], fArr6[2], fArr6[3]);
                        float screenYImpl4 = pGraphicsOpenGL.screenYImpl(fArr6[0], fArr6[1], fArr6[2], fArr6[3]);
                        float screenXImpl5 = pGraphicsOpenGL.screenXImpl(fArr7[0], fArr7[1], fArr7[2], fArr7[3]);
                        float screenYImpl5 = pGraphicsOpenGL.screenYImpl(fArr7[0], fArr7[1], fArr7[2], fArr7[3]);
                        float screenXImpl6 = pGraphicsOpenGL.screenXImpl(fArr8[0], fArr8[1], fArr8[2], fArr8[3]);
                        float screenYImpl6 = pGraphicsOpenGL.screenYImpl(fArr8[0], fArr8[1], fArr8[2], fArr8[3]);
                        raw.fill(nativeToJavaARGB);
                        raw.vertex(screenXImpl4, screenYImpl4);
                        raw.fill(nativeToJavaARGB2);
                        raw.vertex(screenXImpl5, screenYImpl5);
                        raw.fill(nativeToJavaARGB3);
                        raw.vertex(screenXImpl6, screenYImpl6);
                    }
                    i6 = i7 + 1;
                }
                i = i2 + 1;
            } else {
                raw.endShape();
                return;
            }
        }
    }

    public void removeChild(int i) {
        super.removeChild(i);
        strokedTexture(false);
        untexChild(false);
        markForTessellation();
    }

    /* access modifiers changed from: protected */
    public void removeTexture(PImage pImage, PShapeOpenGL pShapeOpenGL) {
        boolean z = false;
        if (this.textures != null && this.textures.contains(pImage)) {
            int i = 0;
            while (true) {
                if (i >= this.childCount) {
                    break;
                }
                PShapeOpenGL pShapeOpenGL2 = (PShapeOpenGL) this.children[i];
                if (pShapeOpenGL2 != pShapeOpenGL && pShapeOpenGL2.hasTexture(pImage)) {
                    z = true;
                    break;
                }
                i++;
            }
            if (!z) {
                this.textures.remove(pImage);
                if (this.textures.size() == 0) {
                    this.textures = null;
                }
            }
            if (this.parent != null) {
                ((PShapeOpenGL) this.parent).removeTexture(pImage, this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void render(PGraphicsOpenGL pGraphicsOpenGL, PImage pImage) {
        if (this.root == null) {
            throw new RuntimeException("Error rendering PShapeOpenGL, root shape is null");
        }
        if (this.hasPolys) {
            renderPolys(pGraphicsOpenGL, pImage);
            if (pGraphicsOpenGL.haveRaw()) {
                rawPolys(pGraphicsOpenGL, pImage);
            }
        }
        if (is3D()) {
            if (this.hasLines) {
                renderLines(pGraphicsOpenGL);
                if (pGraphicsOpenGL.haveRaw()) {
                    rawLines(pGraphicsOpenGL);
                }
            }
            if (this.hasPoints) {
                renderPoints(pGraphicsOpenGL);
                if (pGraphicsOpenGL.haveRaw()) {
                    rawPoints(pGraphicsOpenGL);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void renderLines(PGraphicsOpenGL pGraphicsOpenGL) {
        PShader lineShader = pGraphicsOpenGL.getLineShader();
        lineShader.bind();
        PGraphicsOpenGL.IndexCache indexCache = this.tessGeo.lineIndexCache;
        int i = this.firstLineIndexCache;
        while (true) {
            int i2 = i;
            if (i2 <= this.lastLineIndexCache) {
                int i3 = indexCache.indexOffset[i2];
                int i4 = indexCache.indexCount[i2];
                int i5 = indexCache.vertexOffset[i2];
                lineShader.setVertexAttribute(this.root.bufLineVertex.glId, 4, PGL.FLOAT, 0, i5 * 4 * PGL.SIZEOF_FLOAT);
                lineShader.setColorAttribute(this.root.bufLineColor.glId, 4, PGL.UNSIGNED_BYTE, 0, i5 * 4 * PGL.SIZEOF_BYTE);
                lineShader.setLineAttribute(this.root.bufLineAttrib.glId, 4, PGL.FLOAT, 0, i5 * 4 * PGL.SIZEOF_FLOAT);
                lineShader.draw(this.root.bufLineIndex.glId, i4, i3);
                i = i2 + 1;
            } else {
                lineShader.unbind();
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void renderPoints(PGraphicsOpenGL pGraphicsOpenGL) {
        PShader pointShader = pGraphicsOpenGL.getPointShader();
        pointShader.bind();
        PGraphicsOpenGL.IndexCache indexCache = this.tessGeo.pointIndexCache;
        int i = this.firstPointIndexCache;
        while (true) {
            int i2 = i;
            if (i2 <= this.lastPointIndexCache) {
                int i3 = indexCache.indexOffset[i2];
                int i4 = indexCache.indexCount[i2];
                int i5 = indexCache.vertexOffset[i2];
                pointShader.setVertexAttribute(this.root.bufPointVertex.glId, 4, PGL.FLOAT, 0, i5 * 4 * PGL.SIZEOF_FLOAT);
                pointShader.setColorAttribute(this.root.bufPointColor.glId, 4, PGL.UNSIGNED_BYTE, 0, i5 * 4 * PGL.SIZEOF_BYTE);
                pointShader.setPointAttribute(this.root.bufPointAttrib.glId, 2, PGL.FLOAT, 0, i5 * 2 * PGL.SIZEOF_FLOAT);
                pointShader.draw(this.root.bufPointIndex.glId, i4, i3);
                i = i2 + 1;
            } else {
                pointShader.unbind();
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void renderPolys(PGraphicsOpenGL pGraphicsOpenGL, PImage pImage) {
        boolean z;
        boolean z2;
        Texture texture;
        PShader pShader;
        boolean z3 = pGraphicsOpenGL.polyShader != null;
        boolean accessNormals = z3 ? pGraphicsOpenGL.polyShader.accessNormals() : false;
        boolean accessTexCoords = z3 ? pGraphicsOpenGL.polyShader.accessTexCoords() : false;
        Texture texture2 = pImage != null ? pGraphicsOpenGL.getTexture(pImage) : null;
        PGraphicsOpenGL.IndexCache indexCache = this.tessGeo.polyIndexCache;
        int i = this.firstPolyIndexCache;
        Texture texture3 = texture2;
        boolean z4 = false;
        PShader pShader2 = null;
        boolean z5 = false;
        while (i <= this.lastPolyIndexCache) {
            if (is3D() || (texture3 != null && ((this.firstLineIndexCache == -1 || i < this.firstLineIndexCache) && (this.firstPointIndexCache == -1 || i < this.firstPointIndexCache)))) {
                if (!z4) {
                    pShader = pGraphicsOpenGL.getPolyShader(pGraphicsOpenGL.lights, texture3 != null);
                    pShader.bind();
                    z = z5;
                    z2 = true;
                    texture = texture3;
                }
                z = z5;
                z2 = z4;
                texture = texture3;
                pShader = pShader2;
            } else {
                if (!z5) {
                    if (texture3 != null) {
                        texture3.unbind();
                        texture3 = null;
                    }
                    if (pShader2 != null && pShader2.bound()) {
                        pShader2.unbind();
                    }
                    pShader = pGraphicsOpenGL.getPolyShader(pGraphicsOpenGL.lights, false);
                    pShader.bind();
                    z = true;
                    z2 = false;
                    texture = texture3;
                }
                z = z5;
                z2 = z4;
                texture = texture3;
                pShader = pShader2;
            }
            int i2 = indexCache.indexOffset[i];
            int i3 = indexCache.indexCount[i];
            int i4 = indexCache.vertexOffset[i];
            pShader.setVertexAttribute(this.root.bufPolyVertex.glId, 4, PGL.FLOAT, 0, i4 * 4 * PGL.SIZEOF_FLOAT);
            pShader.setColorAttribute(this.root.bufPolyColor.glId, 4, PGL.UNSIGNED_BYTE, 0, i4 * 4 * PGL.SIZEOF_BYTE);
            if (pGraphicsOpenGL.lights) {
                pShader.setNormalAttribute(this.root.bufPolyNormal.glId, 3, PGL.FLOAT, 0, i4 * 3 * PGL.SIZEOF_FLOAT);
                pShader.setAmbientAttribute(this.root.bufPolyAmbient.glId, 4, PGL.UNSIGNED_BYTE, 0, i4 * 4 * PGL.SIZEOF_BYTE);
                pShader.setSpecularAttribute(this.root.bufPolySpecular.glId, 4, PGL.UNSIGNED_BYTE, 0, i4 * 4 * PGL.SIZEOF_BYTE);
                pShader.setEmissiveAttribute(this.root.bufPolyEmissive.glId, 4, PGL.UNSIGNED_BYTE, 0, i4 * 4 * PGL.SIZEOF_BYTE);
                pShader.setShininessAttribute(this.root.bufPolyShininess.glId, 1, PGL.FLOAT, 0, PGL.SIZEOF_FLOAT * i4);
            }
            if (pGraphicsOpenGL.lights || accessNormals) {
                pShader.setNormalAttribute(this.root.bufPolyNormal.glId, 3, PGL.FLOAT, 0, i4 * 3 * PGL.SIZEOF_FLOAT);
            }
            if (texture != null || accessTexCoords) {
                pShader.setTexcoordAttribute(this.root.bufPolyTexcoord.glId, 2, PGL.FLOAT, 0, i4 * 2 * PGL.SIZEOF_FLOAT);
                pShader.setTexture(texture);
            }
            for (PGraphicsOpenGL.VertexAttribute vertexAttribute : this.polyAttribs.values()) {
                if (vertexAttribute.active(pShader)) {
                    vertexAttribute.bind(this.pgl);
                    pShader.setAttributeVBO(vertexAttribute.glLoc, vertexAttribute.buf.glId, vertexAttribute.tessSize, vertexAttribute.type, vertexAttribute.isColor(), 0, vertexAttribute.sizeInBytes(i4));
                }
            }
            pShader.draw(this.root.bufPolyIndex.glId, i3, i2);
            i++;
            z5 = z;
            texture3 = texture;
            pShader2 = pShader;
            z4 = z2;
        }
        for (PGraphicsOpenGL.VertexAttribute vertexAttribute2 : this.polyAttribs.values()) {
            if (vertexAttribute2.active(pShader2)) {
                vertexAttribute2.unbind(this.pgl);
            }
        }
        if (pShader2 != null && pShader2.bound()) {
            pShader2.unbind();
        }
    }

    public void resetMatrix() {
        if (this.shapeCreated && this.matrix != null && this.transformStack != null) {
            if (this.family == 0) {
                updateTessellation();
            }
            if (this.tessellated) {
                PMatrix popTransform = popTransform();
                while (popTransform != null) {
                    if (popTransform.invert()) {
                        applyMatrixImpl(popTransform);
                    } else {
                        PGraphics.showWarning("Transformation applied on the shape cannot be inverted");
                    }
                    popTransform = popTransform();
                }
            }
            this.matrix.reset();
            this.transformStack.clear();
        }
    }

    /* access modifiers changed from: protected */
    public void restoreBezierVertexSettings() {
        if (this.savedBezierDetail != this.bezierDetail) {
            this.pg.bezierDetail(this.savedBezierDetail);
        }
    }

    /* access modifiers changed from: protected */
    public void restoreCurveVertexSettings() {
        if (this.savedCurveDetail != this.curveDetail) {
            this.pg.curveDetail(this.savedCurveDetail);
        }
        if (this.savedCurveTightness != this.curveTightness) {
            this.pg.curveTightness(this.savedCurveTightness);
        }
    }

    public void rotate(float f) {
        transform(1, f);
    }

    public void rotate(float f, float f2, float f3, float f4) {
        transform(1, f, f2, f3, f4);
    }

    public void rotateX(float f) {
        rotate(f, 1.0f, 0.0f, 0.0f);
    }

    public void rotateY(float f) {
        rotate(f, 0.0f, 1.0f, 0.0f);
    }

    public void rotateZ(float f) {
        transform(1, f);
    }

    /* access modifiers changed from: protected */
    public void saveBezierVertexSettings() {
        this.savedBezierDetail = this.pg.bezierDetail;
        if (this.pg.bezierDetail != this.bezierDetail) {
            this.pg.bezierDetail(this.bezierDetail);
        }
    }

    /* access modifiers changed from: protected */
    public void saveCurveVertexSettings() {
        this.savedCurveDetail = this.pg.curveDetail;
        this.savedCurveTightness = this.pg.curveTightness;
        if (this.pg.curveDetail != this.curveDetail) {
            this.pg.curveDetail(this.curveDetail);
        }
        if (this.pg.curveTightness != this.curveTightness) {
            this.pg.curveTightness(this.curveTightness);
        }
    }

    public void scale(float f) {
        if (this.is3D) {
            transform(2, f, f, f);
            return;
        }
        transform(2, f, f);
    }

    public void scale(float f, float f2) {
        if (this.is3D) {
            transform(2, f, f2, 1.0f);
            return;
        }
        transform(2, f, f2);
    }

    public void scale(float f, float f2, float f3) {
        transform(2, f, f2, f3);
    }

    /* access modifiers changed from: protected */
    public void scaleTextureUV(float f, float f2) {
        int i;
        if (!PGraphicsOpenGL.same(f, 1.0f) || !PGraphicsOpenGL.same(f2, 1.0f)) {
            for (int i2 = 0; i2 < this.inGeo.vertexCount; i2++) {
                float f3 = this.inGeo.texcoords[(i2 * 2) + 0];
                float f4 = this.inGeo.texcoords[(i2 * 2) + 1];
                this.inGeo.texcoords[(i2 * 2) + 0] = PApplet.min(1.0f, f3 * f);
                this.inGeo.texcoords[(i2 * 2) + 1] = PApplet.min(1.0f, f4 * f);
            }
            if (this.shapeCreated && this.tessellated && this.hasPolys) {
                if (is3D()) {
                    i = this.lastPolyVertex + 1;
                } else if (is2D()) {
                    i = this.lastPolyVertex + 1;
                    if (-1 < this.firstLineVertex) {
                        i = this.firstLineVertex;
                    }
                    if (-1 < this.firstPointVertex) {
                        i = this.firstPointVertex;
                    }
                } else {
                    i = 0;
                }
                for (int i3 = this.firstLineVertex; i3 < i; i3++) {
                    float f5 = this.tessGeo.polyTexCoords[(i3 * 2) + 0];
                    float f6 = this.tessGeo.polyTexCoords[(i3 * 2) + 1];
                    this.tessGeo.polyTexCoords[(i3 * 2) + 0] = PApplet.min(1.0f, f5 * f);
                    this.tessGeo.polyTexCoords[(i3 * 2) + 1] = PApplet.min(1.0f, f6 * f);
                }
                this.root.setModifiedPolyTexCoords(this.firstPolyVertex, i - 1);
            }
        }
    }

    public void setAmbient(int i) {
        int i2 = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setAmbient()");
        } else if (this.family == 0) {
            while (true) {
                int i3 = i2;
                if (i3 < this.childCount) {
                    ((PShapeOpenGL) this.children[i3]).setAmbient(i);
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        } else {
            setAmbientImpl(i);
        }
    }

    public void setAmbient(int i, int i2) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setAmbient()");
            return;
        }
        this.inGeo.ambient[i] = PGL.javaToNativeARGB(i2);
        markForTessellation();
        this.setAmbient = true;
    }

    /* access modifiers changed from: protected */
    public void setAmbientImpl(int i) {
        if (this.ambientColor != i) {
            this.ambientColor = i;
            Arrays.fill(this.inGeo.ambient, 0, this.inGeo.vertexCount, PGL.javaToNativeARGB(this.ambientColor));
            if (this.shapeCreated && this.tessellated && this.hasPolys) {
                if (is3D()) {
                    Arrays.fill(this.tessGeo.polyAmbient, this.firstPolyVertex, this.lastPolyVertex + 1, PGL.javaToNativeARGB(this.ambientColor));
                    this.root.setModifiedPolyAmbient(this.firstPolyVertex, this.lastPolyVertex);
                } else if (is2D()) {
                    int i2 = this.lastPolyVertex + 1;
                    if (-1 < this.firstLineVertex) {
                        i2 = this.firstLineVertex;
                    }
                    if (-1 < this.firstPointVertex) {
                        i2 = this.firstPointVertex;
                    }
                    Arrays.fill(this.tessGeo.polyAmbient, this.firstPolyVertex, i2, PGL.javaToNativeARGB(this.ambientColor));
                    this.root.setModifiedPolyColors(this.firstPolyVertex, i2 - 1);
                }
            }
            this.setAmbient = true;
        }
    }

    public void setAttrib(String str, int i, float... fArr) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setNormal()");
            return;
        }
        PGraphicsOpenGL.VertexAttribute vertexAttribute = (PGraphicsOpenGL.VertexAttribute) this.polyAttribs.get(str);
        float[] fArr2 = this.inGeo.fattribs.get(str);
        for (float f : fArr) {
            fArr2[(vertexAttribute.size * i) + 0] = f;
        }
        markForTessellation();
    }

    public void setAttrib(String str, int i, int... iArr) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setNormal()");
            return;
        }
        PGraphicsOpenGL.VertexAttribute vertexAttribute = (PGraphicsOpenGL.VertexAttribute) this.polyAttribs.get(str);
        int[] iArr2 = this.inGeo.iattribs.get(str);
        for (int i2 : iArr) {
            iArr2[(vertexAttribute.size * i) + 0] = i2;
        }
        markForTessellation();
    }

    public void setAttrib(String str, int i, boolean... zArr) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setNormal()");
            return;
        }
        PGraphicsOpenGL.VertexAttribute vertexAttribute = (PGraphicsOpenGL.VertexAttribute) this.polyAttribs.get(str);
        byte[] bArr = this.inGeo.battribs.get(str);
        for (int i2 = 0; i2 < zArr.length; i2++) {
            bArr[(vertexAttribute.size * i) + 0] = (byte) (zArr[i2] ? 1 : 0);
        }
        markForTessellation();
    }

    public void setEmissive(int i) {
        int i2 = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setEmissive()");
        } else if (this.family == 0) {
            while (true) {
                int i3 = i2;
                if (i3 < this.childCount) {
                    ((PShapeOpenGL) this.children[i3]).setEmissive(i);
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        } else {
            setEmissiveImpl(i);
        }
    }

    public void setEmissive(int i, int i2) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setEmissive()");
            return;
        }
        this.inGeo.emissive[i] = PGL.javaToNativeARGB(i2);
        markForTessellation();
    }

    /* access modifiers changed from: protected */
    public void setEmissiveImpl(int i) {
        if (this.emissiveColor != i) {
            this.emissiveColor = i;
            Arrays.fill(this.inGeo.emissive, 0, this.inGeo.vertexCount, PGL.javaToNativeARGB(this.emissiveColor));
            if (this.shapeCreated && this.tessellated && this.tessGeo.polyVertexCount > 0) {
                if (is3D()) {
                    Arrays.fill(this.tessGeo.polyEmissive, this.firstPolyVertex, this.lastPolyVertex + 1, PGL.javaToNativeARGB(this.emissiveColor));
                    this.root.setModifiedPolyEmissive(this.firstPolyVertex, this.lastPolyVertex);
                } else if (is2D()) {
                    int i2 = this.lastPolyVertex + 1;
                    if (-1 < this.firstLineVertex) {
                        i2 = this.firstLineVertex;
                    }
                    if (-1 < this.firstPointVertex) {
                        i2 = this.firstPointVertex;
                    }
                    Arrays.fill(this.tessGeo.polyEmissive, this.firstPolyVertex, i2, PGL.javaToNativeARGB(this.emissiveColor));
                    this.root.setModifiedPolyColors(this.firstPolyVertex, i2 - 1);
                }
            }
        }
    }

    public void setFill(int i) {
        int i2 = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setFill()");
        } else if (this.family == 0) {
            while (true) {
                int i3 = i2;
                if (i3 < this.childCount) {
                    ((PShapeOpenGL) this.children[i3]).setFill(i);
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        } else {
            setFillImpl(i);
        }
    }

    public void setFill(int i, int i2) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setFill()");
        } else if (this.image == null) {
            this.inGeo.colors[i] = PGL.javaToNativeARGB(i2);
            markForTessellation();
        }
    }

    public void setFill(boolean z) {
        int i = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setFill()");
            return;
        }
        if (this.family == 0) {
            while (true) {
                int i2 = i;
                if (i2 >= this.childCount) {
                    break;
                }
                ((PShapeOpenGL) this.children[i2]).setFill(z);
                i = i2 + 1;
            }
        } else if (this.fill != z) {
            markForTessellation();
        }
        this.fill = z;
    }

    /* access modifiers changed from: protected */
    public void setFillImpl(int i) {
        if (this.fillColor != i) {
            this.fillColor = i;
            if (this.image == null) {
                Arrays.fill(this.inGeo.colors, 0, this.inGeo.vertexCount, PGL.javaToNativeARGB(this.fillColor));
                if (this.shapeCreated && this.tessellated && this.hasPolys) {
                    if (is3D()) {
                        Arrays.fill(this.tessGeo.polyColors, this.firstPolyVertex, this.lastPolyVertex + 1, PGL.javaToNativeARGB(this.fillColor));
                        this.root.setModifiedPolyColors(this.firstPolyVertex, this.lastPolyVertex);
                    } else if (is2D()) {
                        int i2 = this.lastPolyVertex + 1;
                        if (-1 < this.firstLineVertex) {
                            i2 = this.firstLineVertex;
                        }
                        if (-1 < this.firstPointVertex) {
                            i2 = this.firstPointVertex;
                        }
                        Arrays.fill(this.tessGeo.polyColors, this.firstPolyVertex, i2, PGL.javaToNativeARGB(this.fillColor));
                        this.root.setModifiedPolyColors(this.firstPolyVertex, i2 - 1);
                    }
                }
            }
            if (!this.setAmbient) {
                setAmbientImpl(i);
                this.setAmbient = false;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setFirstStrokeVertex(int i, int i2) {
        if (i == this.firstLineIndexCache && this.firstLineVertex == -1) {
            this.lastLineVertex = i2;
            this.firstLineVertex = i2;
        }
        if (i == this.firstPointIndexCache && this.firstPointVertex == -1) {
            this.lastPointVertex = i2;
            this.firstPointVertex = i2;
        }
    }

    /* access modifiers changed from: protected */
    public void setLastStrokeVertex(int i) {
        if (-1 < this.lastLineVertex) {
            this.lastLineVertex = i;
        }
        if (-1 < this.lastPointVertex) {
            this.lastPointVertex += i;
        }
    }

    /* access modifiers changed from: protected */
    public void setModifiedLineAttributes(int i, int i2) {
        if (i < this.firstModifiedLineAttribute) {
            this.firstModifiedLineAttribute = i;
        }
        if (i2 > this.lastModifiedLineAttribute) {
            this.lastModifiedLineAttribute = i2;
        }
        this.modifiedLineAttributes = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedLineColors(int i, int i2) {
        if (i < this.firstModifiedLineColor) {
            this.firstModifiedLineColor = i;
        }
        if (i2 > this.lastModifiedLineColor) {
            this.lastModifiedLineColor = i2;
        }
        this.modifiedLineColors = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedLineVertices(int i, int i2) {
        if (i < this.firstModifiedLineVertex) {
            this.firstModifiedLineVertex = i;
        }
        if (i2 > this.lastModifiedLineVertex) {
            this.lastModifiedLineVertex = i2;
        }
        this.modifiedLineVertices = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedPointAttributes(int i, int i2) {
        if (i < this.firstModifiedPointAttribute) {
            this.firstModifiedPointAttribute = i;
        }
        if (i2 > this.lastModifiedPointAttribute) {
            this.lastModifiedPointAttribute = i2;
        }
        this.modifiedPointAttributes = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedPointColors(int i, int i2) {
        if (i < this.firstModifiedPointColor) {
            this.firstModifiedPointColor = i;
        }
        if (i2 > this.lastModifiedPointColor) {
            this.lastModifiedPointColor = i2;
        }
        this.modifiedPointColors = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedPointVertices(int i, int i2) {
        if (i < this.firstModifiedPointVertex) {
            this.firstModifiedPointVertex = i;
        }
        if (i2 > this.lastModifiedPointVertex) {
            this.lastModifiedPointVertex = i2;
        }
        this.modifiedPointVertices = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedPolyAmbient(int i, int i2) {
        if (i < this.firstModifiedPolyAmbient) {
            this.firstModifiedPolyAmbient = i;
        }
        if (i2 > this.lastModifiedPolyAmbient) {
            this.lastModifiedPolyAmbient = i2;
        }
        this.modifiedPolyAmbient = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedPolyAttrib(PGraphicsOpenGL.VertexAttribute vertexAttribute, int i, int i2) {
        if (i < vertexAttribute.firstModified) {
            vertexAttribute.firstModified = i;
        }
        if (i2 > vertexAttribute.lastModified) {
            vertexAttribute.lastModified = i2;
        }
        vertexAttribute.modified = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedPolyColors(int i, int i2) {
        if (i < this.firstModifiedPolyColor) {
            this.firstModifiedPolyColor = i;
        }
        if (i2 > this.lastModifiedPolyColor) {
            this.lastModifiedPolyColor = i2;
        }
        this.modifiedPolyColors = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedPolyEmissive(int i, int i2) {
        if (i < this.firstModifiedPolyEmissive) {
            this.firstModifiedPolyEmissive = i;
        }
        if (i2 > this.lastModifiedPolyEmissive) {
            this.lastModifiedPolyEmissive = i2;
        }
        this.modifiedPolyEmissive = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedPolyNormals(int i, int i2) {
        if (i < this.firstModifiedPolyNormal) {
            this.firstModifiedPolyNormal = i;
        }
        if (i2 > this.lastModifiedPolyNormal) {
            this.lastModifiedPolyNormal = i2;
        }
        this.modifiedPolyNormals = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedPolyShininess(int i, int i2) {
        if (i < this.firstModifiedPolyShininess) {
            this.firstModifiedPolyShininess = i;
        }
        if (i2 > this.lastModifiedPolyShininess) {
            this.lastModifiedPolyShininess = i2;
        }
        this.modifiedPolyShininess = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedPolySpecular(int i, int i2) {
        if (i < this.firstModifiedPolySpecular) {
            this.firstModifiedPolySpecular = i;
        }
        if (i2 > this.lastModifiedPolySpecular) {
            this.lastModifiedPolySpecular = i2;
        }
        this.modifiedPolySpecular = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedPolyTexCoords(int i, int i2) {
        if (i < this.firstModifiedPolyTexcoord) {
            this.firstModifiedPolyTexcoord = i;
        }
        if (i2 > this.lastModifiedPolyTexcoord) {
            this.lastModifiedPolyTexcoord = i2;
        }
        this.modifiedPolyTexCoords = true;
        this.modified = true;
    }

    /* access modifiers changed from: protected */
    public void setModifiedPolyVertices(int i, int i2) {
        if (i < this.firstModifiedPolyVertex) {
            this.firstModifiedPolyVertex = i;
        }
        if (i2 > this.lastModifiedPolyVertex) {
            this.lastModifiedPolyVertex = i2;
        }
        this.modifiedPolyVertices = true;
        this.modified = true;
    }

    public void setNormal(int i, float f, float f2, float f3) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setNormal()");
            return;
        }
        this.inGeo.normals[(i * 3) + 0] = f;
        this.inGeo.normals[(i * 3) + 1] = f2;
        this.inGeo.normals[(i * 3) + 2] = f3;
        markForTessellation();
    }

    public void setParams(float[] fArr) {
        if (this.family != 1) {
            PGraphics.showWarning("Parameters can only be set to PRIMITIVE shapes");
            return;
        }
        super.setParams(fArr);
        markForTessellation();
        this.shapeCreated = true;
    }

    public void setPath(int i, float[][] fArr, int i2, int[] iArr) {
        if (this.family != 2) {
            PGraphics.showWarning("Vertex coordinates and codes can only be set to PATH shapes");
            return;
        }
        super.setPath(i, fArr, i2, iArr);
        markForTessellation();
        this.shapeCreated = true;
    }

    public void setShininess(float f) {
        int i = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setShininess()");
        } else if (this.family == 0) {
            while (true) {
                int i2 = i;
                if (i2 < this.childCount) {
                    ((PShapeOpenGL) this.children[i2]).setShininess(f);
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else {
            setShininessImpl(f);
        }
    }

    public void setShininess(int i, float f) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setShininess()");
            return;
        }
        this.inGeo.shininess[i] = f;
        markForTessellation();
    }

    /* access modifiers changed from: protected */
    public void setShininessImpl(float f) {
        if (!PGraphicsOpenGL.same(this.shininess, f)) {
            this.shininess = f;
            Arrays.fill(this.inGeo.shininess, 0, this.inGeo.vertexCount, f);
            if (this.shapeCreated && this.tessellated && this.hasPolys) {
                if (is3D()) {
                    Arrays.fill(this.tessGeo.polyShininess, this.firstPolyVertex, this.lastPolyVertex + 1, f);
                    this.root.setModifiedPolyShininess(this.firstPolyVertex, this.lastPolyVertex);
                } else if (is2D()) {
                    int i = this.lastPolyVertex + 1;
                    if (-1 < this.firstLineVertex) {
                        i = this.firstLineVertex;
                    }
                    if (-1 < this.firstPointVertex) {
                        i = this.firstPointVertex;
                    }
                    Arrays.fill(this.tessGeo.polyShininess, this.firstPolyVertex, i, f);
                    this.root.setModifiedPolyColors(this.firstPolyVertex, i - 1);
                }
            }
        }
    }

    public void setSpecular(int i) {
        int i2 = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setSpecular()");
        } else if (this.family == 0) {
            while (true) {
                int i3 = i2;
                if (i3 < this.childCount) {
                    ((PShapeOpenGL) this.children[i3]).setSpecular(i);
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        } else {
            setSpecularImpl(i);
        }
    }

    public void setSpecular(int i, int i2) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setSpecular()");
            return;
        }
        this.inGeo.specular[i] = PGL.javaToNativeARGB(i2);
        markForTessellation();
    }

    /* access modifiers changed from: protected */
    public void setSpecularImpl(int i) {
        if (this.specularColor != i) {
            this.specularColor = i;
            Arrays.fill(this.inGeo.specular, 0, this.inGeo.vertexCount, PGL.javaToNativeARGB(this.specularColor));
            if (this.shapeCreated && this.tessellated && this.hasPolys) {
                if (is3D()) {
                    Arrays.fill(this.tessGeo.polySpecular, this.firstPolyVertex, this.lastPolyVertex + 1, PGL.javaToNativeARGB(this.specularColor));
                    this.root.setModifiedPolySpecular(this.firstPolyVertex, this.lastPolyVertex);
                } else if (is2D()) {
                    int i2 = this.lastPolyVertex + 1;
                    if (-1 < this.firstLineVertex) {
                        i2 = this.firstLineVertex;
                    }
                    if (-1 < this.firstPointVertex) {
                        i2 = this.firstPointVertex;
                    }
                    Arrays.fill(this.tessGeo.polySpecular, this.firstPolyVertex, i2, PGL.javaToNativeARGB(this.specularColor));
                    this.root.setModifiedPolyColors(this.firstPolyVertex, i2 - 1);
                }
            }
        }
    }

    public void setStroke(int i) {
        int i2 = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setStroke()");
        } else if (this.family == 0) {
            while (true) {
                int i3 = i2;
                if (i3 < this.childCount) {
                    ((PShapeOpenGL) this.children[i3]).setStroke(i);
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        } else {
            setStrokeImpl(i);
        }
    }

    public void setStroke(int i, int i2) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setStroke()");
            return;
        }
        this.inGeo.strokeColors[i] = PGL.javaToNativeARGB(i2);
        markForTessellation();
    }

    public void setStroke(boolean z) {
        int i = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setStroke()");
        } else if (this.family == 0) {
            while (true) {
                int i2 = i;
                if (i2 < this.childCount) {
                    ((PShapeOpenGL) this.children[i2]).setStroke(z);
                    i = i2 + 1;
                } else {
                    this.stroke = z;
                    return;
                }
            }
        } else {
            setStrokeImpl(z);
        }
    }

    public void setStrokeCap(int i) {
        int i2 = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setStrokeCap()");
        } else if (this.family == 0) {
            while (true) {
                int i3 = i2;
                if (i3 < this.childCount) {
                    ((PShapeOpenGL) this.children[i3]).setStrokeCap(i);
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        } else {
            if (is2D() && this.strokeCap != i) {
                markForTessellation();
            }
            this.strokeCap = i;
        }
    }

    /* access modifiers changed from: protected */
    public void setStrokeImpl(int i) {
        if (this.strokeColor != i) {
            this.strokeColor = i;
            Arrays.fill(this.inGeo.strokeColors, 0, this.inGeo.vertexCount, PGL.javaToNativeARGB(this.strokeColor));
            if (this.shapeCreated && this.tessellated) {
                if (this.hasLines || this.hasPoints) {
                    if (this.hasLines) {
                        if (is3D()) {
                            Arrays.fill(this.tessGeo.lineColors, this.firstLineVertex, this.lastLineVertex + 1, PGL.javaToNativeARGB(this.strokeColor));
                            this.root.setModifiedLineColors(this.firstLineVertex, this.lastLineVertex);
                        } else if (is2D()) {
                            Arrays.fill(this.tessGeo.polyColors, this.firstLineVertex, this.lastLineVertex + 1, PGL.javaToNativeARGB(this.strokeColor));
                            this.root.setModifiedPolyColors(this.firstLineVertex, this.lastLineVertex);
                        }
                    }
                    if (!this.hasPoints) {
                        return;
                    }
                    if (is3D()) {
                        Arrays.fill(this.tessGeo.pointColors, this.firstPointVertex, this.lastPointVertex + 1, PGL.javaToNativeARGB(this.strokeColor));
                        this.root.setModifiedPointColors(this.firstPointVertex, this.lastPointVertex);
                    } else if (is2D()) {
                        Arrays.fill(this.tessGeo.polyColors, this.firstPointVertex, this.lastPointVertex + 1, PGL.javaToNativeARGB(this.strokeColor));
                        this.root.setModifiedPolyColors(this.firstPointVertex, this.lastPointVertex);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setStrokeImpl(boolean z) {
        if (this.stroke != z) {
            if (z) {
                int i = this.strokeColor;
                this.strokeColor++;
                setStrokeImpl(i);
            }
            markForTessellation();
            if (is2D() && this.parent != null) {
                ((PShapeOpenGL) this.parent).strokedTexture(z && this.image != null);
            }
            this.stroke = z;
        }
    }

    public void setStrokeJoin(int i) {
        int i2 = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setStrokeJoin()");
        } else if (this.family == 0) {
            while (true) {
                int i3 = i2;
                if (i3 < this.childCount) {
                    ((PShapeOpenGL) this.children[i3]).setStrokeJoin(i);
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        } else {
            if (is2D() && this.strokeJoin != i) {
                markForTessellation();
            }
            this.strokeJoin = i;
        }
    }

    public void setStrokeWeight(float f) {
        int i = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setStrokeWeight()");
        } else if (this.family == 0) {
            while (true) {
                int i2 = i;
                if (i2 < this.childCount) {
                    ((PShapeOpenGL) this.children[i2]).setStrokeWeight(f);
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else {
            setStrokeWeightImpl(f);
        }
    }

    public void setStrokeWeight(int i, float f) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setStrokeWeight()");
            return;
        }
        this.inGeo.strokeWeights[i] = f;
        markForTessellation();
    }

    /* access modifiers changed from: protected */
    public void setStrokeWeightImpl(float f) {
        if (!PGraphicsOpenGL.same(this.strokeWeight, f)) {
            float f2 = this.strokeWeight;
            this.strokeWeight = f;
            Arrays.fill(this.inGeo.strokeWeights, 0, this.inGeo.vertexCount, this.strokeWeight);
            if (this.shapeCreated && this.tessellated) {
                if (this.hasLines || this.hasPoints) {
                    float f3 = f / f2;
                    if (this.hasLines) {
                        if (is3D()) {
                            for (int i = this.firstLineVertex; i <= this.lastLineVertex; i++) {
                                float[] fArr = this.tessGeo.lineDirections;
                                int i2 = (i * 4) + 3;
                                fArr[i2] = fArr[i2] * f3;
                            }
                            this.root.setModifiedLineAttributes(this.firstLineVertex, this.lastLineVertex);
                        } else if (is2D()) {
                            markForTessellation();
                        }
                    }
                    if (!this.hasPoints) {
                        return;
                    }
                    if (is3D()) {
                        for (int i3 = this.firstPointVertex; i3 <= this.lastPointVertex; i3++) {
                            float[] fArr2 = this.tessGeo.pointOffsets;
                            int i4 = (i3 * 2) + 0;
                            fArr2[i4] = fArr2[i4] * f3;
                            float[] fArr3 = this.tessGeo.pointOffsets;
                            int i5 = (i3 * 2) + 1;
                            fArr3[i5] = fArr3[i5] * f3;
                        }
                        this.root.setModifiedPointAttributes(this.firstPointVertex, this.lastPointVertex);
                    } else if (is2D()) {
                        markForTessellation();
                    }
                }
            }
        }
    }

    public void setTexture(PImage pImage) {
        int i = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setTexture()");
        } else if (this.family == 0) {
            while (true) {
                int i2 = i;
                if (i2 < this.childCount) {
                    ((PShapeOpenGL) this.children[i2]).setTexture(pImage);
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else {
            setTextureImpl(pImage);
        }
    }

    /* access modifiers changed from: protected */
    public void setTextureImpl(PImage pImage) {
        float f;
        float f2 = 1.0f;
        PImage pImage2 = this.image;
        this.image = pImage;
        if (this.textureMode == 2 && pImage2 != this.image) {
            if (this.image != null) {
                f = 1.0f / ((float) this.image.width);
                f2 = 1.0f / ((float) this.image.height);
            } else {
                f = 1.0f;
            }
            if (pImage2 != null) {
                f *= (float) pImage2.width;
                f2 *= (float) pImage2.height;
            }
            scaleTextureUV(f, f2);
        }
        if (!(pImage2 == pImage || this.parent == null)) {
            ((PShapeOpenGL) this.parent).removeTexture(pImage2, this);
        }
        if (this.parent != null) {
            ((PShapeOpenGL) this.parent).addTexture(this.image);
            if (is2D() && this.stroke) {
                ((PShapeOpenGL) this.parent).strokedTexture(true);
            }
        }
    }

    public void setTextureMode(int i) {
        int i2 = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setTextureMode()");
        } else if (this.family == 0) {
            while (true) {
                int i3 = i2;
                if (i3 < this.childCount) {
                    ((PShapeOpenGL) this.children[i3]).setTextureMode(i);
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        } else {
            setTextureModeImpl(i);
        }
    }

    /* access modifiers changed from: protected */
    public void setTextureModeImpl(int i) {
        if (this.textureMode != i) {
            this.textureMode = i;
            if (this.image != null) {
                float f = (float) this.image.width;
                float f2 = (float) this.image.height;
                if (this.textureMode == 1) {
                    f = 1.0f / f;
                    f2 = 1.0f / f2;
                }
                scaleTextureUV(f, f2);
            }
        }
    }

    public void setTextureUV(int i, float f, float f2) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setTextureUV()");
            return;
        }
        if (this.textureMode == 2 && this.image != null) {
            f /= (float) this.image.width;
            f2 /= (float) this.image.height;
        }
        this.inGeo.texcoords[(i * 2) + 0] = f;
        this.inGeo.texcoords[(i * 2) + 1] = f2;
        markForTessellation();
    }

    public void setTint(int i) {
        int i2 = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setTint()");
        } else if (this.family == 0) {
            while (true) {
                int i3 = i2;
                if (i3 < this.childCount) {
                    ((PShapeOpenGL) this.children[i3]).setTint(i);
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        } else {
            setTintImpl(i);
        }
    }

    public void setTint(int i, int i2) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setTint()");
        } else if (this.image != null) {
            this.inGeo.colors[i] = PGL.javaToNativeARGB(i2);
            markForTessellation();
        }
    }

    public void setTint(boolean z) {
        int i = 0;
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setTint()");
            return;
        }
        if (this.family == 0) {
            while (true) {
                int i2 = i;
                if (i2 >= this.childCount) {
                    break;
                }
                ((PShapeOpenGL) this.children[i2]).setTint(this.fill);
                i = i2 + 1;
            }
        } else if (this.tint && !z) {
            setTintImpl(-1);
        }
        this.tint = z;
    }

    /* access modifiers changed from: protected */
    public void setTintImpl(int i) {
        if (this.tintColor != i) {
            this.tintColor = i;
            if (this.image != null) {
                Arrays.fill(this.inGeo.colors, 0, this.inGeo.vertexCount, PGL.javaToNativeARGB(this.tintColor));
                if (this.shapeCreated && this.tessellated && this.hasPolys) {
                    if (is3D()) {
                        Arrays.fill(this.tessGeo.polyColors, this.firstPolyVertex, this.lastPolyVertex + 1, PGL.javaToNativeARGB(this.tintColor));
                        this.root.setModifiedPolyColors(this.firstPolyVertex, this.lastPolyVertex);
                    } else if (is2D()) {
                        int i2 = this.lastPolyVertex + 1;
                        if (-1 < this.firstLineVertex) {
                            i2 = this.firstLineVertex;
                        }
                        if (-1 < this.firstPointVertex) {
                            i2 = this.firstPointVertex;
                        }
                        Arrays.fill(this.tessGeo.polyColors, this.firstPolyVertex, i2, PGL.javaToNativeARGB(this.tintColor));
                        this.root.setModifiedPolyColors(this.firstPolyVertex, i2 - 1);
                    }
                }
            }
        }
    }

    public void setVertex(int i, float f, float f2) {
        setVertex(i, f, f2, 0.0f);
    }

    public void setVertex(int i, float f, float f2, float f3) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setVertex()");
            return;
        }
        if (this.family != 2) {
            this.inGeo.vertices[(i * 3) + 0] = f;
            this.inGeo.vertices[(i * 3) + 1] = f2;
            this.inGeo.vertices[(i * 3) + 2] = f3;
        } else if (this.vertexCodes == null || this.vertexCodeCount <= 0 || this.vertexCodes[i] == 0) {
            this.vertices[i][0] = f;
            this.vertices[i][1] = f2;
            if (this.is3D) {
                this.vertices[i][2] = f3;
            }
        } else {
            PGraphics.showWarning(PShape.NOT_A_SIMPLE_VERTEX, "setVertex()");
            return;
        }
        markForTessellation();
    }

    public void setVertex(int i, PVector pVector) {
        if (this.openShape) {
            PGraphics.showWarning(PShape.INSIDE_BEGIN_END_ERROR, "setVertex()");
            return;
        }
        this.inGeo.vertices[(i * 3) + 0] = pVector.x;
        this.inGeo.vertices[(i * 3) + 1] = pVector.y;
        this.inGeo.vertices[(i * 3) + 2] = pVector.z;
        markForTessellation();
    }

    public void solid(boolean z) {
        if (this.family == 0) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.childCount) {
                    ((PShapeOpenGL) this.children[i2]).solid(z);
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else {
            this.solid = z;
        }
    }

    /* access modifiers changed from: protected */
    public boolean startStrokedTex(int i) {
        return this.image != null && (i == this.firstLineIndexCache || i == this.firstPointIndexCache);
    }

    /* access modifiers changed from: protected */
    public void strokedTexture(boolean z) {
        strokedTexture(z, (PShapeOpenGL) null);
    }

    /* access modifiers changed from: protected */
    public void strokedTexture(boolean z, PShapeOpenGL pShapeOpenGL) {
        int i = 0;
        if (this.strokedTexture != z) {
            if (z) {
                this.strokedTexture = true;
            } else {
                this.strokedTexture = false;
                while (true) {
                    int i2 = i;
                    if (i2 >= this.childCount) {
                        break;
                    }
                    PShapeOpenGL pShapeOpenGL2 = (PShapeOpenGL) this.children[i2];
                    if (pShapeOpenGL2 != pShapeOpenGL && pShapeOpenGL2.hasStrokedTexture()) {
                        this.strokedTexture = true;
                        break;
                    }
                    i = i2 + 1;
                }
            }
            if (this.parent != null) {
                ((PShapeOpenGL) this.parent).strokedTexture(z, this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void styles(PGraphics pGraphics) {
        if (pGraphics instanceof PGraphicsOpenGL) {
            if (pGraphics.stroke) {
                setStroke(true);
                setStroke(pGraphics.strokeColor);
                setStrokeWeight(pGraphics.strokeWeight);
                setStrokeCap(pGraphics.strokeCap);
                setStrokeJoin(pGraphics.strokeJoin);
            } else {
                setStroke(false);
            }
            if (pGraphics.fill) {
                setFill(true);
                setFill(pGraphics.fillColor);
            } else {
                setFill(false);
            }
            if (pGraphics.tint) {
                setTint(true);
                setTint(pGraphics.tintColor);
            }
            setAmbient(pGraphics.ambientColor);
            setSpecular(pGraphics.specularColor);
            setEmissive(pGraphics.emissiveColor);
            setShininess(pGraphics.shininess);
            if (this.image != null) {
                setTextureMode(pGraphics.textureMode);
                return;
            }
            return;
        }
        super.styles(pGraphics);
    }

    /* access modifiers changed from: protected */
    public void tessellate() {
        if (this.root == this && this.parent == null) {
            if (this.polyAttribs == null) {
                this.polyAttribs = PGraphicsOpenGL.newAttributeMap();
                collectPolyAttribs();
            }
            if (this.tessGeo == null) {
                this.tessGeo = PGraphicsOpenGL.newTessGeometry(this.pg, this.polyAttribs, 1);
            }
            this.tessGeo.clear();
            for (int i = 0; i < this.polyAttribs.size(); i++) {
                this.tessGeo.initAttrib(this.polyAttribs.get(i));
            }
            tessellateImpl();
            this.tessGeo.trim();
        }
    }

    /* access modifiers changed from: protected */
    public void tessellateArc() {
        float f;
        float f2;
        float f3;
        float f4;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        float f8 = 0.0f;
        float f9 = 0.0f;
        float f10 = 0.0f;
        int i = this.ellipseMode;
        int i2 = 0;
        if (6 <= this.params.length) {
            f5 = this.params[0];
            f6 = this.params[1];
            f7 = this.params[2];
            f8 = this.params[3];
            f9 = this.params[4];
            f10 = this.params[5];
            if (this.params.length == 7) {
                i2 = (int) this.params[6];
            }
        }
        if (i == 1) {
            f = f8 - f6;
            f2 = f7 - f5;
            f3 = f6;
            f4 = f5;
        } else if (i == 2) {
            f = f8 * 2.0f;
            f2 = f7 * 2.0f;
            f3 = f6 - f8;
            f4 = f5 - f7;
        } else if (i == 3) {
            f = f8;
            f2 = f7;
            f3 = f6 - (f8 / 2.0f);
            f4 = f5 - (f7 / 2.0f);
        } else {
            f = f8;
            f2 = f7;
            f3 = f6;
            f4 = f5;
        }
        if (!Float.isInfinite(f9) && !Float.isInfinite(f10) && f10 > f9) {
            float f11 = f10;
            float f12 = f9;
            while (f12 < 0.0f) {
                f11 += 6.2831855f;
                f12 = 6.2831855f + f12;
            }
            float f13 = f11 - f12 > 6.2831855f ? 6.2831855f + f12 : f11;
            this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
            this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
            this.inGeo.addArc(f4, f3, f2, f, f12, f13, this.fill, this.stroke, i2);
            this.tessellator.tessellateTriangleFan();
        }
    }

    /* access modifiers changed from: protected */
    public void tessellateBox() {
        float f;
        float f2;
        float f3;
        if (this.params.length == 1) {
            float f4 = this.params[0];
            f = f4;
            f2 = f4;
            f3 = f4;
        } else if (this.params.length == 3) {
            float f5 = this.params[0];
            float f6 = this.params[1];
            f = this.params[2];
            f2 = f6;
            f3 = f5;
        } else {
            f = 0.0f;
            f2 = 0.0f;
            f3 = 0.0f;
        }
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.addBox(f3, f2, f, this.fill, this.stroke);
        this.tessellator.tessellateQuads();
    }

    /* access modifiers changed from: protected */
    public void tessellateEllipse() {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        int i = this.ellipseMode;
        if (4 <= this.params.length) {
            f4 = this.params[0];
            f3 = this.params[1];
            f2 = this.params[2];
            f = this.params[3];
        } else {
            f = 0.0f;
            f2 = 0.0f;
            f3 = 0.0f;
            f4 = 0.0f;
        }
        if (i == 1) {
            f2 -= f4;
            f -= f3;
        } else if (i == 2) {
            f4 -= f2;
            f3 -= f;
            f2 *= 2.0f;
            f *= 2.0f;
        } else if (i == 3) {
            f4 -= f2 / 2.0f;
            f3 -= f / 2.0f;
        }
        if (f2 < 0.0f) {
            f5 = -f2;
            f6 = f4 + f2;
        } else {
            f5 = f2;
            f6 = f4;
        }
        if (f < 0.0f) {
            f7 = -f;
            f8 = f3 + f;
        } else {
            f7 = f;
            f8 = f3;
        }
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addEllipse(f6, f8, f5, f7, this.fill, this.stroke);
        this.tessellator.tessellateTriangleFan();
    }

    /* access modifiers changed from: protected */
    public void tessellateImpl() {
        boolean z = false;
        this.tessGeo = this.root.tessGeo;
        this.firstPolyIndexCache = -1;
        this.lastPolyIndexCache = -1;
        this.firstLineIndexCache = -1;
        this.lastLineIndexCache = -1;
        this.firstPointIndexCache = -1;
        this.lastPointIndexCache = -1;
        if (this.family == 0) {
            if (this.polyAttribs == null) {
                this.polyAttribs = PGraphicsOpenGL.newAttributeMap();
                collectPolyAttribs();
            }
            while (true) {
                int i = z;
                if (i >= this.childCount) {
                    break;
                }
                ((PShapeOpenGL) this.children[i]).tessellateImpl();
                z = i + 1;
            }
        } else if (this.shapeCreated) {
            this.inGeo.clearEdges();
            this.tessellator.setInGeometry(this.inGeo);
            this.tessellator.setTessGeometry(this.tessGeo);
            this.tessellator.setFill(this.fill || this.image != null);
            this.tessellator.setTexCache((PGraphicsOpenGL.TexCache) null, (PImage) null);
            this.tessellator.setStroke(this.stroke);
            this.tessellator.setStrokeColor(this.strokeColor);
            this.tessellator.setStrokeWeight(this.strokeWeight);
            this.tessellator.setStrokeCap(this.strokeCap);
            this.tessellator.setStrokeJoin(this.strokeJoin);
            this.tessellator.setRenderer(this.pg);
            this.tessellator.setTransform(this.matrix);
            this.tessellator.set3D(is3D());
            if (this.family == 3) {
                if (this.kind == 3) {
                    this.tessellator.tessellatePoints();
                } else if (this.kind == 5) {
                    this.tessellator.tessellateLines();
                } else if (this.kind == 50) {
                    this.tessellator.tessellateLineStrip();
                } else if (this.kind == 51) {
                    this.tessellator.tessellateLineLoop();
                } else if (this.kind == 8 || this.kind == 9) {
                    if (this.stroke) {
                        this.inGeo.addTrianglesEdges();
                    }
                    if (this.normalMode == 0) {
                        this.inGeo.calcTrianglesNormals();
                    }
                    this.tessellator.tessellateTriangles();
                } else if (this.kind == 11) {
                    if (this.stroke) {
                        this.inGeo.addTriangleFanEdges();
                    }
                    if (this.normalMode == 0) {
                        this.inGeo.calcTriangleFanNormals();
                    }
                    this.tessellator.tessellateTriangleFan();
                } else if (this.kind == 10) {
                    if (this.stroke) {
                        this.inGeo.addTriangleStripEdges();
                    }
                    if (this.normalMode == 0) {
                        this.inGeo.calcTriangleStripNormals();
                    }
                    this.tessellator.tessellateTriangleStrip();
                } else if (this.kind == 16 || this.kind == 17) {
                    if (this.stroke) {
                        this.inGeo.addQuadsEdges();
                    }
                    if (this.normalMode == 0) {
                        this.inGeo.calcQuadsNormals();
                    }
                    this.tessellator.tessellateQuads();
                } else if (this.kind == 18) {
                    if (this.stroke) {
                        this.inGeo.addQuadStripEdges();
                    }
                    if (this.normalMode == 0) {
                        this.inGeo.calcQuadStripNormals();
                    }
                    this.tessellator.tessellateQuadStrip();
                } else if (this.kind == 20) {
                    boolean hasBezierVertex = this.inGeo.hasBezierVertex();
                    boolean hasQuadraticVertex = this.inGeo.hasQuadraticVertex();
                    boolean hasCurveVertex = this.inGeo.hasCurveVertex();
                    if (hasBezierVertex || hasQuadraticVertex) {
                        saveBezierVertexSettings();
                    }
                    if (hasCurveVertex) {
                        saveCurveVertexSettings();
                        this.tessellator.resetCurveVertexCount();
                    }
                    PGraphicsOpenGL.Tessellator tessellator2 = this.tessellator;
                    boolean z2 = this.solid;
                    boolean z3 = this.close;
                    if (this.normalMode == 0) {
                        z = true;
                    }
                    tessellator2.tessellatePolygon(z2, z3, z);
                    if (hasBezierVertex || hasQuadraticVertex) {
                        restoreBezierVertexSettings();
                    }
                    if (hasCurveVertex) {
                        restoreCurveVertexSettings();
                    }
                }
            } else if (this.family == 1) {
                this.inGeo.clear();
                if (this.kind == 2) {
                    tessellatePoint();
                } else if (this.kind == 4) {
                    tessellateLine();
                } else if (this.kind == 8) {
                    tessellateTriangle();
                } else if (this.kind == 16) {
                    tessellateQuad();
                } else if (this.kind == 30) {
                    tessellateRect();
                } else if (this.kind == 31) {
                    tessellateEllipse();
                } else if (this.kind == 32) {
                    tessellateArc();
                } else if (this.kind == 41) {
                    tessellateBox();
                } else if (this.kind == 40) {
                    tessellateSphere();
                }
            } else if (this.family == 2) {
                this.inGeo.clear();
                tessellatePath();
            }
            if (!(this.image == null || this.parent == null)) {
                ((PShapeOpenGL) this.parent).addTexture(this.image);
            }
            this.firstPolyIndexCache = this.tessellator.firstPolyIndexCache;
            this.lastPolyIndexCache = this.tessellator.lastPolyIndexCache;
            this.firstLineIndexCache = this.tessellator.firstLineIndexCache;
            this.lastLineIndexCache = this.tessellator.lastLineIndexCache;
            this.firstPointIndexCache = this.tessellator.firstPointIndexCache;
            this.lastPointIndexCache = this.tessellator.lastPointIndexCache;
        }
        this.lastPolyVertex = -1;
        this.firstPolyVertex = -1;
        this.lastLineVertex = -1;
        this.firstLineVertex = -1;
        this.lastPointVertex = -1;
        this.firstPointVertex = -1;
        this.tessellated = true;
    }

    /* access modifiers changed from: protected */
    public void tessellateLine() {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        if (this.params.length == 4) {
            float f7 = this.params[0];
            float f8 = this.params[1];
            float f9 = this.params[2];
            f = 0.0f;
            f2 = this.params[3];
            f3 = f9;
            f4 = 0.0f;
            f5 = f8;
            f6 = f7;
        } else if (this.params.length == 6) {
            float f10 = this.params[0];
            float f11 = this.params[1];
            float f12 = this.params[2];
            float f13 = this.params[3];
            float f14 = this.params[4];
            f = this.params[5];
            f2 = f14;
            f3 = f13;
            f4 = f12;
            f5 = f11;
            f6 = f10;
        } else {
            f = 0.0f;
            f2 = 0.0f;
            f3 = 0.0f;
            f4 = 0.0f;
            f5 = 0.0f;
            f6 = 0.0f;
        }
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addLine(f6, f5, f4, f3, f2, f, this.fill, this.stroke);
        this.tessellator.tessellateLines();
    }

    /* access modifiers changed from: protected */
    public void tessellatePath() {
        if (this.vertices != null) {
            this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
            if (this.vertexCodeCount == 0) {
                if (this.vertices[0].length != 2) {
                    int i = 0;
                    while (true) {
                        int i2 = i;
                        if (i2 >= this.vertexCount) {
                            break;
                        }
                        this.inGeo.addVertex(this.vertices[i2][0], this.vertices[i2][1], this.vertices[i2][2], 0, false);
                        i = i2 + 1;
                    }
                } else {
                    for (int i3 = 0; i3 < this.vertexCount; i3++) {
                        this.inGeo.addVertex(this.vertices[i3][0], this.vertices[i3][1], 0, false);
                    }
                }
            } else {
                int i4 = 0;
                boolean z = true;
                if (this.vertices[0].length == 2) {
                    int i5 = 0;
                    while (true) {
                        int i6 = i5;
                        int i7 = i4;
                        if (i6 < this.vertexCodeCount) {
                            switch (this.vertexCodes[i6]) {
                                case 0:
                                    this.inGeo.addVertex(this.vertices[i7][0], this.vertices[i7][1], 0, z);
                                    z = false;
                                    i7++;
                                    break;
                                case 1:
                                    this.inGeo.addBezierVertex(this.vertices[i7 + 0][0], this.vertices[i7 + 0][1], 0.0f, this.vertices[i7 + 1][0], this.vertices[i7 + 1][1], 0.0f, this.vertices[i7 + 2][0], this.vertices[i7 + 2][1], 0.0f, z);
                                    z = false;
                                    i7 += 3;
                                    break;
                                case 2:
                                    this.inGeo.addQuadraticVertex(this.vertices[i7 + 0][0], this.vertices[i7 + 0][1], 0.0f, this.vertices[i7 + 1][0], this.vertices[i7 + 1][1], 0.0f, z);
                                    z = false;
                                    i7 += 2;
                                    break;
                                case 3:
                                    this.inGeo.addCurveVertex(this.vertices[i7][0], this.vertices[i7][1], 0.0f, z);
                                    z = false;
                                    i7++;
                                    break;
                                case 4:
                                    z = true;
                                    break;
                            }
                            i4 = i7;
                            i5 = i6 + 1;
                        }
                    }
                } else {
                    int i8 = 0;
                    while (true) {
                        int i9 = i8;
                        int i10 = i4;
                        if (i9 < this.vertexCodeCount) {
                            switch (this.vertexCodes[i9]) {
                                case 0:
                                    this.inGeo.addVertex(this.vertices[i10][0], this.vertices[i10][1], this.vertices[i10][2], z);
                                    z = false;
                                    i10++;
                                    break;
                                case 1:
                                    this.inGeo.addBezierVertex(this.vertices[i10 + 0][0], this.vertices[i10 + 0][1], this.vertices[i10 + 0][2], this.vertices[i10 + 1][0], this.vertices[i10 + 1][1], this.vertices[i10 + 1][2], this.vertices[i10 + 2][0], this.vertices[i10 + 2][1], this.vertices[i10 + 2][2], z);
                                    z = false;
                                    i10 += 3;
                                    break;
                                case 2:
                                    this.inGeo.addQuadraticVertex(this.vertices[i10 + 0][0], this.vertices[i10 + 0][1], this.vertices[i10 + 0][2], this.vertices[i10 + 1][0], this.vertices[i10 + 1][1], this.vertices[i10 + 0][2], z);
                                    z = false;
                                    i10 += 2;
                                    break;
                                case 3:
                                    this.inGeo.addCurveVertex(this.vertices[i10][0], this.vertices[i10][1], this.vertices[i10][2], z);
                                    z = false;
                                    i10++;
                                    break;
                                case 4:
                                    z = true;
                                    break;
                            }
                            i4 = i10;
                            i8 = i9 + 1;
                        }
                    }
                }
            }
            boolean hasBezierVertex = this.inGeo.hasBezierVertex();
            boolean hasQuadraticVertex = this.inGeo.hasQuadraticVertex();
            boolean hasCurveVertex = this.inGeo.hasCurveVertex();
            if (hasBezierVertex || hasQuadraticVertex) {
                saveBezierVertexSettings();
            }
            if (hasCurveVertex) {
                saveCurveVertexSettings();
                this.tessellator.resetCurveVertexCount();
            }
            this.tessellator.tessellatePolygon(true, this.close, true);
            if (hasBezierVertex || hasQuadraticVertex) {
                restoreBezierVertexSettings();
            }
            if (hasCurveVertex) {
                restoreCurveVertexSettings();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void tessellatePoint() {
        float f;
        float f2;
        float f3;
        if (this.params.length == 2) {
            float f4 = this.params[0];
            f = 0.0f;
            f2 = this.params[1];
            f3 = f4;
        } else if (this.params.length == 3) {
            float f5 = this.params[0];
            float f6 = this.params[1];
            f = this.params[2];
            f2 = f6;
            f3 = f5;
        } else {
            f = 0.0f;
            f2 = 0.0f;
            f3 = 0.0f;
        }
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addPoint(f3, f2, f, this.fill, this.stroke);
        this.tessellator.tessellatePoints();
    }

    /* access modifiers changed from: protected */
    public void tessellateQuad() {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6 = 0.0f;
        float f7 = 0.0f;
        float f8 = 0.0f;
        if (this.params.length == 8) {
            float f9 = this.params[0];
            float f10 = this.params[1];
            float f11 = this.params[2];
            float f12 = this.params[3];
            float f13 = this.params[4];
            f6 = this.params[5];
            f7 = this.params[6];
            f8 = this.params[7];
            f = f13;
            f2 = f12;
            f3 = f11;
            f4 = f10;
            f5 = f9;
        } else {
            f = 0.0f;
            f2 = 0.0f;
            f3 = 0.0f;
            f4 = 0.0f;
            f5 = 0.0f;
        }
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addQuad(f5, f4, 0.0f, f3, f2, 0.0f, f, f6, 0.0f, f7, f8, 0.0f, this.stroke);
        this.tessellator.tessellateQuads();
    }

    /* access modifiers changed from: protected */
    public void tessellateRect() {
        boolean z;
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7 = 0.0f;
        float f8 = 0.0f;
        float f9 = 0.0f;
        float f10 = 0.0f;
        float f11 = 0.0f;
        float f12 = 0.0f;
        float f13 = 0.0f;
        float f14 = 0.0f;
        boolean z2 = false;
        int i = this.rectMode;
        if (this.params.length == 4 || this.params.length == 5) {
            f7 = this.params[0];
            f8 = this.params[1];
            f9 = this.params[2];
            f10 = this.params[3];
            z2 = false;
            if (this.params.length == 5) {
                f11 = this.params[4];
                f12 = this.params[4];
                f13 = this.params[4];
                f14 = this.params[4];
                z = true;
            }
            z = z2;
        } else {
            if (this.params.length == 8) {
                f7 = this.params[0];
                f8 = this.params[1];
                f9 = this.params[2];
                f10 = this.params[3];
                f11 = this.params[4];
                f12 = this.params[5];
                f13 = this.params[6];
                f14 = this.params[7];
                z = true;
            }
            z = z2;
        }
        switch (i) {
            case 0:
                f9 += f7;
                f10 += f8;
                f2 = f8;
                f = f7;
                break;
            case 1:
                f2 = f8;
                f = f7;
                break;
            case 2:
                f2 = f8 - f10;
                f10 = f8 + f10;
                f = f7 - f9;
                f9 = f7 + f9;
                break;
            case 3:
                float f15 = f9 / 2.0f;
                float f16 = f10 / 2.0f;
                f9 = f7 + f15;
                f10 = f8 + f16;
                f = f7 - f15;
                f2 = f8 - f16;
                break;
            default:
                f2 = f8;
                f = f7;
                break;
        }
        if (f > f9) {
            f3 = f;
            f4 = f9;
        } else {
            f3 = f9;
            f4 = f;
        }
        if (f2 > f10) {
            f5 = f2;
            f6 = f10;
        } else {
            f5 = f10;
            f6 = f2;
        }
        float min = PApplet.min((f3 - f4) / 2.0f, (f5 - f6) / 2.0f);
        float f17 = f11 > min ? min : f11;
        float f18 = f12 > min ? min : f12;
        float f19 = f13 > min ? min : f13;
        if (f14 <= min) {
            min = f14;
        }
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        if (z) {
            saveBezierVertexSettings();
            this.inGeo.addRect(f4, f6, f3, f5, f17, f18, f19, min, this.stroke);
            this.tessellator.tessellatePolygon(true, true, true);
            restoreBezierVertexSettings();
            return;
        }
        this.inGeo.addRect(f4, f6, f3, f5, this.stroke);
        this.tessellator.tessellateQuads();
    }

    /* access modifiers changed from: protected */
    public void tessellateSphere() {
        float f;
        int i;
        int i2;
        int i3;
        float f2 = 0.0f;
        int i4 = this.sphereDetailU;
        int i5 = this.sphereDetailV;
        if (1 <= this.params.length) {
            f2 = this.params[0];
            if (this.params.length == 2) {
                i5 = (int) this.params[1];
                i4 = i5;
                f = f2;
            } else if (this.params.length == 3) {
                i4 = (int) this.params[1];
                i5 = (int) this.params[2];
                f = f2;
            }
            if (i4 >= 3 || i5 < 2) {
                i2 = 30;
                i = 30;
            } else {
                i2 = i5;
                i = i4;
            }
            i3 = this.pg.sphereDetailU;
            int i6 = this.pg.sphereDetailV;
            if (!(this.pg.sphereDetailU == i && this.pg.sphereDetailV == i2)) {
                this.pg.sphereDetail(i, i2);
            }
            this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
            this.tessellator.tessellateTriangles(this.inGeo.addSphere(f, i, i2, this.fill, this.stroke));
            if ((i3 <= 0 && i3 != i) || (i6 > 0 && i6 != i2)) {
                this.pg.sphereDetail(i3, i6);
                return;
            }
            return;
        }
        f = f2;
        if (i4 >= 3) {
        }
        i2 = 30;
        i = 30;
        i3 = this.pg.sphereDetailU;
        int i62 = this.pg.sphereDetailV;
        this.pg.sphereDetail(i, i2);
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.tessellator.tessellateTriangles(this.inGeo.addSphere(f, i, i2, this.fill, this.stroke));
        if (i3 <= 0) {
        }
    }

    /* access modifiers changed from: protected */
    public void tessellateTriangle() {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6 = 0.0f;
        if (this.params.length == 6) {
            float f7 = this.params[0];
            float f8 = this.params[1];
            float f9 = this.params[2];
            float f10 = this.params[3];
            float f11 = this.params[4];
            f6 = this.params[5];
            f = f11;
            f2 = f10;
            f3 = f9;
            f4 = f8;
            f5 = f7;
        } else {
            f = 0.0f;
            f2 = 0.0f;
            f3 = 0.0f;
            f4 = 0.0f;
            f5 = 0.0f;
        }
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addTriangle(f5, f4, 0.0f, f3, f2, 0.0f, f, f6, 0.0f, this.fill, this.stroke);
        this.tessellator.tessellateTriangles();
    }

    /* access modifiers changed from: protected */
    public void transform(int i, float... fArr) {
        int i2 = this.is3D ? 3 : 2;
        checkMatrix(i2);
        if (this.transform != null) {
            this.transform.reset();
        } else if (i2 == 2) {
            this.transform = new PMatrix2D();
        } else {
            this.transform = new PMatrix3D();
        }
        int length = fArr.length;
        if (i == 1) {
            length = fArr.length == 1 ? 2 : 3;
        } else if (i == 3) {
            length = fArr.length == 6 ? 2 : 3;
        }
        switch (i) {
            case 0:
                if (length != 3) {
                    this.transform.translate(fArr[0], fArr[1]);
                    break;
                } else {
                    this.transform.translate(fArr[0], fArr[1], fArr[2]);
                    break;
                }
            case 1:
                if (length != 3) {
                    this.transform.rotate(fArr[0]);
                    break;
                } else {
                    this.transform.rotate(fArr[0], fArr[1], fArr[2], fArr[3]);
                    break;
                }
            case 2:
                if (length != 3) {
                    this.transform.scale(fArr[0], fArr[1]);
                    break;
                } else {
                    this.transform.scale(fArr[0], fArr[1], fArr[2]);
                    break;
                }
            case 3:
                if (length != 3) {
                    this.transform.set(fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5]);
                    break;
                } else {
                    this.transform.set(fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5], fArr[6], fArr[7], fArr[8], fArr[9], fArr[10], fArr[11], fArr[12], fArr[13], fArr[14], fArr[15]);
                    break;
                }
        }
        this.matrix.apply(this.transform);
        pushTransform();
        if (this.tessellated) {
            applyMatrixImpl(this.transform);
        }
    }

    public void translate(float f, float f2) {
        if (this.is3D) {
            transform(0, f, f2, 0.0f);
            return;
        }
        transform(0, f, f2);
    }

    public void translate(float f, float f2, float f3) {
        transform(0, f, f2, f3);
    }

    /* access modifiers changed from: protected */
    public void untexChild(boolean z) {
        untexChild(z, (PShapeOpenGL) null);
    }

    /* access modifiers changed from: protected */
    public void untexChild(boolean z, PShapeOpenGL pShapeOpenGL) {
        int i = 0;
        if (this.untexChild != z) {
            if (z) {
                this.untexChild = true;
            } else {
                this.untexChild = false;
                while (true) {
                    int i2 = i;
                    if (i2 >= this.childCount) {
                        break;
                    }
                    PShapeOpenGL pShapeOpenGL2 = (PShapeOpenGL) this.children[i2];
                    if (pShapeOpenGL2 != pShapeOpenGL && !pShapeOpenGL2.hasTexture()) {
                        this.untexChild = true;
                        break;
                    }
                    i = i2 + 1;
                }
            }
            if (this.parent != null) {
                ((PShapeOpenGL) this.parent).untexChild(z, this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateGeometry() {
        this.root.initBuffers();
        if (this.root.modified) {
            this.root.updateGeometryImpl();
        }
    }

    /* access modifiers changed from: protected */
    public void updateGeometryImpl() {
        if (this.modifiedPolyVertices) {
            int i = this.firstModifiedPolyVertex;
            copyPolyVertices(i, (this.lastModifiedPolyVertex - i) + 1);
            this.modifiedPolyVertices = false;
            this.firstModifiedPolyVertex = PConstants.MAX_INT;
            this.lastModifiedPolyVertex = Integer.MIN_VALUE;
        }
        if (this.modifiedPolyColors) {
            int i2 = this.firstModifiedPolyColor;
            copyPolyColors(i2, (this.lastModifiedPolyColor - i2) + 1);
            this.modifiedPolyColors = false;
            this.firstModifiedPolyColor = PConstants.MAX_INT;
            this.lastModifiedPolyColor = Integer.MIN_VALUE;
        }
        if (this.modifiedPolyNormals) {
            int i3 = this.firstModifiedPolyNormal;
            copyPolyNormals(i3, (this.lastModifiedPolyNormal - i3) + 1);
            this.modifiedPolyNormals = false;
            this.firstModifiedPolyNormal = PConstants.MAX_INT;
            this.lastModifiedPolyNormal = Integer.MIN_VALUE;
        }
        if (this.modifiedPolyTexCoords) {
            int i4 = this.firstModifiedPolyTexcoord;
            copyPolyTexCoords(i4, (this.lastModifiedPolyTexcoord - i4) + 1);
            this.modifiedPolyTexCoords = false;
            this.firstModifiedPolyTexcoord = PConstants.MAX_INT;
            this.lastModifiedPolyTexcoord = Integer.MIN_VALUE;
        }
        if (this.modifiedPolyAmbient) {
            int i5 = this.firstModifiedPolyAmbient;
            copyPolyAmbient(i5, (this.lastModifiedPolyAmbient - i5) + 1);
            this.modifiedPolyAmbient = false;
            this.firstModifiedPolyAmbient = PConstants.MAX_INT;
            this.lastModifiedPolyAmbient = Integer.MIN_VALUE;
        }
        if (this.modifiedPolySpecular) {
            int i6 = this.firstModifiedPolySpecular;
            copyPolySpecular(i6, (this.lastModifiedPolySpecular - i6) + 1);
            this.modifiedPolySpecular = false;
            this.firstModifiedPolySpecular = PConstants.MAX_INT;
            this.lastModifiedPolySpecular = Integer.MIN_VALUE;
        }
        if (this.modifiedPolyEmissive) {
            int i7 = this.firstModifiedPolyEmissive;
            copyPolyEmissive(i7, (this.lastModifiedPolyEmissive - i7) + 1);
            this.modifiedPolyEmissive = false;
            this.firstModifiedPolyEmissive = PConstants.MAX_INT;
            this.lastModifiedPolyEmissive = Integer.MIN_VALUE;
        }
        if (this.modifiedPolyShininess) {
            int i8 = this.firstModifiedPolyShininess;
            copyPolyShininess(i8, (this.lastModifiedPolyShininess - i8) + 1);
            this.modifiedPolyShininess = false;
            this.firstModifiedPolyShininess = PConstants.MAX_INT;
            this.lastModifiedPolyShininess = Integer.MIN_VALUE;
        }
        for (String str : this.polyAttribs.keySet()) {
            PGraphicsOpenGL.VertexAttribute vertexAttribute = (PGraphicsOpenGL.VertexAttribute) this.polyAttribs.get(str);
            if (vertexAttribute.modified) {
                int i9 = this.firstModifiedPolyVertex;
                copyPolyAttrib(vertexAttribute, i9, (this.lastModifiedPolyVertex - i9) + 1);
                vertexAttribute.modified = false;
                vertexAttribute.firstModified = PConstants.MAX_INT;
                vertexAttribute.lastModified = Integer.MIN_VALUE;
            }
        }
        if (this.modifiedLineVertices) {
            int i10 = this.firstModifiedLineVertex;
            copyLineVertices(i10, (this.lastModifiedLineVertex - i10) + 1);
            this.modifiedLineVertices = false;
            this.firstModifiedLineVertex = PConstants.MAX_INT;
            this.lastModifiedLineVertex = Integer.MIN_VALUE;
        }
        if (this.modifiedLineColors) {
            int i11 = this.firstModifiedLineColor;
            copyLineColors(i11, (this.lastModifiedLineColor - i11) + 1);
            this.modifiedLineColors = false;
            this.firstModifiedLineColor = PConstants.MAX_INT;
            this.lastModifiedLineColor = Integer.MIN_VALUE;
        }
        if (this.modifiedLineAttributes) {
            int i12 = this.firstModifiedLineAttribute;
            copyLineAttributes(i12, (this.lastModifiedLineAttribute - i12) + 1);
            this.modifiedLineAttributes = false;
            this.firstModifiedLineAttribute = PConstants.MAX_INT;
            this.lastModifiedLineAttribute = Integer.MIN_VALUE;
        }
        if (this.modifiedPointVertices) {
            int i13 = this.firstModifiedPointVertex;
            copyPointVertices(i13, (this.lastModifiedPointVertex - i13) + 1);
            this.modifiedPointVertices = false;
            this.firstModifiedPointVertex = PConstants.MAX_INT;
            this.lastModifiedPointVertex = Integer.MIN_VALUE;
        }
        if (this.modifiedPointColors) {
            int i14 = this.firstModifiedPointColor;
            copyPointColors(i14, (this.lastModifiedPointColor - i14) + 1);
            this.modifiedPointColors = false;
            this.firstModifiedPointColor = PConstants.MAX_INT;
            this.lastModifiedPointColor = Integer.MIN_VALUE;
        }
        if (this.modifiedPointAttributes) {
            int i15 = this.firstModifiedPointAttribute;
            copyPointAttributes(i15, (this.lastModifiedPointAttribute - i15) + 1);
            this.modifiedPointAttributes = false;
            this.firstModifiedPointAttribute = PConstants.MAX_INT;
            this.lastModifiedPointAttribute = Integer.MIN_VALUE;
        }
        this.modified = false;
    }

    /* access modifiers changed from: protected */
    public void updateLineIndexCache() {
        PGraphicsOpenGL.IndexCache indexCache = this.tessGeo.lineIndexCache;
        if (this.family == 0) {
            this.lastLineIndexCache = -1;
            this.firstLineIndexCache = -1;
            int i = -1;
            for (int i2 = 0; i2 < this.childCount; i2++) {
                PShapeOpenGL pShapeOpenGL = (PShapeOpenGL) this.children[i2];
                int i3 = pShapeOpenGL.firstLineIndexCache;
                int i4 = -1 < i3 ? (pShapeOpenGL.lastLineIndexCache - i3) + 1 : -1;
                for (int i5 = i3; i5 < i3 + i4; i5++) {
                    if (i == -1) {
                        i = indexCache.addNew(i5);
                        this.firstLineIndexCache = i;
                    } else if (indexCache.vertexOffset[i] == indexCache.vertexOffset[i5]) {
                        indexCache.incCounts(i, indexCache.indexCount[i5], indexCache.vertexCount[i5]);
                    } else {
                        i = indexCache.addNew(i5);
                    }
                }
                if (-1 < pShapeOpenGL.firstLineVertex) {
                    if (this.firstLineVertex == -1) {
                        this.firstLineVertex = PConstants.MAX_INT;
                    }
                    this.firstLineVertex = PApplet.min(this.firstLineVertex, pShapeOpenGL.firstLineVertex);
                }
                if (-1 < pShapeOpenGL.lastLineVertex) {
                    this.lastLineVertex = PApplet.max(this.lastLineVertex, pShapeOpenGL.lastLineVertex);
                }
            }
            this.lastLineIndexCache = i;
            return;
        }
        int i6 = indexCache.vertexOffset[this.firstLineIndexCache];
        this.lastLineVertex = i6;
        this.firstLineVertex = i6;
        for (int i7 = this.firstLineIndexCache; i7 <= this.lastLineIndexCache; i7++) {
            int i8 = indexCache.indexOffset[i7];
            int i9 = indexCache.indexCount[i7];
            int i10 = indexCache.vertexCount[i7];
            if (PGL.MAX_VERTEX_INDEX1 <= this.root.lineVertexRel + i10) {
                this.root.lineVertexRel = 0;
                this.root.lineVertexOffset = this.root.lineVertexAbs;
                indexCache.indexOffset[i7] = this.root.lineIndexOffset;
            } else {
                this.tessGeo.incLineIndices(i8, (i8 + i9) - 1, this.root.lineVertexRel);
            }
            indexCache.vertexOffset[i7] = this.root.lineVertexOffset;
            PShapeOpenGL pShapeOpenGL2 = this.root;
            pShapeOpenGL2.lineIndexOffset = i9 + pShapeOpenGL2.lineIndexOffset;
            this.root.lineVertexAbs += i10;
            this.root.lineVertexRel += i10;
            this.lastLineVertex += i10;
        }
        this.lastLineVertex--;
    }

    /* access modifiers changed from: protected */
    public void updatePointIndexCache() {
        PGraphicsOpenGL.IndexCache indexCache = this.tessGeo.pointIndexCache;
        if (this.family == 0) {
            this.lastPointIndexCache = -1;
            this.firstPointIndexCache = -1;
            int i = -1;
            for (int i2 = 0; i2 < this.childCount; i2++) {
                PShapeOpenGL pShapeOpenGL = (PShapeOpenGL) this.children[i2];
                int i3 = pShapeOpenGL.firstPointIndexCache;
                int i4 = -1 < i3 ? (pShapeOpenGL.lastPointIndexCache - i3) + 1 : -1;
                for (int i5 = i3; i5 < i3 + i4; i5++) {
                    if (i == -1) {
                        i = indexCache.addNew(i5);
                        this.firstPointIndexCache = i;
                    } else if (indexCache.vertexOffset[i] == indexCache.vertexOffset[i5]) {
                        indexCache.incCounts(i, indexCache.indexCount[i5], indexCache.vertexCount[i5]);
                    } else {
                        i = indexCache.addNew(i5);
                    }
                }
                if (-1 < pShapeOpenGL.firstPointVertex) {
                    if (this.firstPointVertex == -1) {
                        this.firstPointVertex = PConstants.MAX_INT;
                    }
                    this.firstPointVertex = PApplet.min(this.firstPointVertex, pShapeOpenGL.firstPointVertex);
                }
                if (-1 < pShapeOpenGL.lastPointVertex) {
                    this.lastPointVertex = PApplet.max(this.lastPointVertex, pShapeOpenGL.lastPointVertex);
                }
            }
            this.lastPointIndexCache = i;
            return;
        }
        int i6 = indexCache.vertexOffset[this.firstPointIndexCache];
        this.lastPointVertex = i6;
        this.firstPointVertex = i6;
        for (int i7 = this.firstPointIndexCache; i7 <= this.lastPointIndexCache; i7++) {
            int i8 = indexCache.indexOffset[i7];
            int i9 = indexCache.indexCount[i7];
            int i10 = indexCache.vertexCount[i7];
            if (PGL.MAX_VERTEX_INDEX1 <= this.root.pointVertexRel + i10) {
                this.root.pointVertexRel = 0;
                this.root.pointVertexOffset = this.root.pointVertexAbs;
                indexCache.indexOffset[i7] = this.root.pointIndexOffset;
            } else {
                this.tessGeo.incPointIndices(i8, (i8 + i9) - 1, this.root.pointVertexRel);
            }
            indexCache.vertexOffset[i7] = this.root.pointVertexOffset;
            PShapeOpenGL pShapeOpenGL2 = this.root;
            pShapeOpenGL2.pointIndexOffset = i9 + pShapeOpenGL2.pointIndexOffset;
            this.root.pointVertexAbs += i10;
            this.root.pointVertexRel += i10;
            this.lastPointVertex += i10;
        }
        this.lastPointVertex--;
    }

    /* access modifiers changed from: protected */
    public void updatePolyIndexCache() {
        PGraphicsOpenGL.IndexCache indexCache = this.tessGeo.polyIndexCache;
        if (this.family == 0) {
            this.lastPolyIndexCache = -1;
            this.firstPolyIndexCache = -1;
            int i = -1;
            for (int i2 = 0; i2 < this.childCount; i2++) {
                PShapeOpenGL pShapeOpenGL = (PShapeOpenGL) this.children[i2];
                int i3 = pShapeOpenGL.firstPolyIndexCache;
                int i4 = -1 < i3 ? (pShapeOpenGL.lastPolyIndexCache - i3) + 1 : -1;
                for (int i5 = i3; i5 < i3 + i4; i5++) {
                    if (i == -1) {
                        i = indexCache.addNew(i5);
                        this.firstPolyIndexCache = i;
                    } else if (indexCache.vertexOffset[i] == indexCache.vertexOffset[i5]) {
                        indexCache.incCounts(i, indexCache.indexCount[i5], indexCache.vertexCount[i5]);
                    } else {
                        i = indexCache.addNew(i5);
                    }
                }
                if (-1 < pShapeOpenGL.firstPolyVertex) {
                    if (this.firstPolyVertex == -1) {
                        this.firstPolyVertex = PConstants.MAX_INT;
                    }
                    this.firstPolyVertex = PApplet.min(this.firstPolyVertex, pShapeOpenGL.firstPolyVertex);
                }
                if (-1 < pShapeOpenGL.lastPolyVertex) {
                    this.lastPolyVertex = PApplet.max(this.lastPolyVertex, pShapeOpenGL.lastPolyVertex);
                }
            }
            this.lastPolyIndexCache = i;
            return;
        }
        int i6 = indexCache.vertexOffset[this.firstPolyIndexCache];
        this.lastPolyVertex = i6;
        this.firstPolyVertex = i6;
        for (int i7 = this.firstPolyIndexCache; i7 <= this.lastPolyIndexCache; i7++) {
            int i8 = indexCache.indexOffset[i7];
            int i9 = indexCache.indexCount[i7];
            int i10 = indexCache.vertexCount[i7];
            if (PGL.MAX_VERTEX_INDEX1 <= this.root.polyVertexRel + i10 || (is2D() && startStrokedTex(i7))) {
                this.root.polyVertexRel = 0;
                this.root.polyVertexOffset = this.root.polyVertexAbs;
                indexCache.indexOffset[i7] = this.root.polyIndexOffset;
            } else {
                this.tessGeo.incPolyIndices(i8, (i8 + i9) - 1, this.root.polyVertexRel);
            }
            indexCache.vertexOffset[i7] = this.root.polyVertexOffset;
            if (is2D()) {
                setFirstStrokeVertex(i7, this.lastPolyVertex);
            }
            PShapeOpenGL pShapeOpenGL2 = this.root;
            pShapeOpenGL2.polyIndexOffset = i9 + pShapeOpenGL2.polyIndexOffset;
            this.root.polyVertexAbs += i10;
            this.root.polyVertexRel += i10;
            this.lastPolyVertex += i10;
        }
        this.lastPolyVertex--;
        if (is2D()) {
            setLastStrokeVertex(this.lastPolyVertex);
        }
    }

    /* access modifiers changed from: protected */
    public void updateRoot(PShape pShape) {
        this.root = (PShapeOpenGL) pShape;
        if (this.family == 0) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.childCount) {
                    ((PShapeOpenGL) this.children[i2]).updateRoot(pShape);
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateTessellation() {
        if (!this.root.tessellated) {
            this.root.tessellate();
            this.root.aggregate();
            this.root.initModified();
            this.root.needBufferInit = true;
        }
    }

    public void vertex(float f, float f2) {
        vertexImpl(f, f2, 0.0f, 0.0f, 0.0f);
        if (this.image != null) {
            PGraphics.showWarning("No uv texture coordinates supplied with vertex() call");
        }
    }

    public void vertex(float f, float f2, float f3) {
        vertexImpl(f, f2, f3, 0.0f, 0.0f);
        if (this.image != null) {
            PGraphics.showWarning("No uv texture coordinates supplied with vertex() call");
        }
    }

    public void vertex(float f, float f2, float f3, float f4) {
        vertexImpl(f, f2, 0.0f, f3, f4);
    }

    public void vertex(float f, float f2, float f3, float f4, float f5) {
        vertexImpl(f, f2, f3, f4, f5);
    }

    /* access modifiers changed from: protected */
    public boolean vertexBreak() {
        if (!this.breakShape) {
            return false;
        }
        this.breakShape = false;
        return true;
    }

    /* access modifiers changed from: protected */
    public void vertexImpl(float f, float f2, float f3, float f4, float f5) {
        float f6;
        float f7;
        if (!this.openShape) {
            PGraphics.showWarning(PShape.OUTSIDE_BEGIN_END_ERROR, "vertex()");
        } else if (this.family == 0) {
            PGraphics.showWarning("Cannot add vertices to GROUP shape");
        } else {
            boolean z = this.image != null;
            int i = 0;
            if (this.fill || z) {
                i = !z ? this.fillColor : this.tint ? this.tintColor : -1;
            }
            if (this.textureMode != 2 || this.image == null) {
                f6 = f5;
                f7 = f4;
            } else {
                float f8 = f4 / ((float) this.image.width);
                f6 = f5 / ((float) this.image.height);
                f7 = f8;
            }
            int i2 = 0;
            float f9 = 0.0f;
            if (this.stroke) {
                i2 = this.strokeColor;
                f9 = this.strokeWeight;
            }
            this.inGeo.addVertex(f, f2, f3, i, this.normalX, this.normalY, this.normalZ, f7, f6, i2, f9, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess, 0, vertexBreak());
            markForTessellation();
        }
    }
}
