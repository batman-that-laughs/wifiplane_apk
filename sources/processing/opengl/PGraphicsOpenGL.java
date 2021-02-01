package processing.opengl;

import android.support.v4.view.ViewCompat;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.core.PVector;
import processing.opengl.FontTexture;
import processing.opengl.LinePath;
import processing.opengl.PGL;
import processing.opengl.Texture;

public class PGraphicsOpenGL extends PGraphics {
    static final String ALREADY_BEGAN_CONTOUR_ERROR = "Already called beginContour()";
    static final String BLEND_DRIVER_ERROR = "blendMode(%1$s) is not supported by this hardware (or driver)";
    static final String BLEND_RENDERER_ERROR = "blendMode(%1$s) is not supported by this renderer";
    protected static final int EDGE_CLOSE = -1;
    protected static final int EDGE_MIDDLE = 0;
    protected static final int EDGE_SINGLE = 3;
    protected static final int EDGE_START = 1;
    protected static final int EDGE_STOP = 2;
    protected static final int FB_STACK_DEPTH = 16;
    protected static final int FLUSH_CONTINUOUSLY = 0;
    protected static final int FLUSH_WHEN_FULL = 1;
    public static String GLSL_VERSION = null;
    static final String GL_THREAD_NOT_CURRENT = "You are trying to draw outside OpenGL's animation thread.\nPlace all drawing commands in the draw() function, or inside\nyour own functions as long as they are called from draw(),\nbut not in event handling functions such as keyPressed()\nor mousePressed().";
    protected static final int IMMEDIATE = 0;
    static final String INCONSISTENT_SHADER_TYPES = "The vertex and fragment shaders have different types";
    protected static final int INIT_INDEX_BUFFER_SIZE = 512;
    protected static final int INIT_VERTEX_BUFFER_SIZE = 256;
    static final String INVALID_FILTER_SHADER_ERROR = "Your shader cannot be used as a filter because is of type POINT or LINES";
    protected static final int MATRIX_STACK_DEPTH = 32;
    private static final int MAX_DRAIN_GLRES_ITERATIONS = 10;
    protected static final int MAX_POINT_ACCURACY = 200;
    protected static final int MIN_POINT_ACCURACY = 20;
    static final String MISSING_FRAGMENT_SHADER = "The fragment shader is missing, cannot create shader object";
    static final String MISSING_UV_TEXCOORDS_ERROR = "No uv texture coordinates supplied with vertex() call";
    static final String MISSING_VERTEX_SHADER = "The vertex shader is missing, cannot create shader object";
    static final String NO_BEGIN_CONTOUR_ERROR = "Need to call beginContour() first";
    static final String NO_COLOR_SHADER_ERROR = "Your shader needs to be of COLOR type to render this geometry properly, using default shader instead.";
    static final String NO_LIGHT_SHADER_ERROR = "Your shader needs to be of LIGHT type to render this geometry properly, using default shader instead.";
    static final String NO_TEXLIGHT_SHADER_ERROR = "Your shader needs to be of TEXLIGHT type to render this geometry properly, using default shader instead.";
    static final String NO_TEXTURE_SHADER_ERROR = "Your shader needs to be of TEXTURE type to render this geometry properly, using default shader instead.";
    public static String OPENGL_EXTENSIONS = null;
    public static String OPENGL_RENDERER = null;
    static final String OPENGL_THREAD_ERROR = "Cannot run the OpenGL renderer outside the main thread, change your code\nso the drawing calls are all inside the main thread, \nor use the default renderer instead.";
    public static String OPENGL_VENDOR = null;
    public static String OPENGL_VERSION = null;
    protected static final int OP_NONE = 0;
    protected static final int OP_READ = 1;
    protected static final int OP_WRITE = 2;
    protected static final float POINT_ACCURACY_FACTOR = 10.0f;
    protected static final float[][] QUAD_POINT_SIGNS = {new float[]{-1.0f, 1.0f}, new float[]{-1.0f, -1.0f}, new float[]{1.0f, -1.0f}, new float[]{1.0f, 1.0f}};
    protected static final int RETAINED = 1;
    static final String SHADER_NEED_LIGHT_ATTRIBS = "The provided shader needs light attributes (ambient, diffuse, etc.), but the current scene is unlit, so the default shader will be used instead";
    static final String TESSELLATION_ERROR = "Tessellation Error: %1$s";
    static final String TOO_MANY_SMOOTH_CALLS_ERROR = "The smooth/noSmooth functions are being called too often.\nThis results in screen flickering, so they will be disabled\nfor the rest of the sketch's execution";
    static final String UNKNOWN_SHADER_KIND_ERROR = "Unknown shader kind";
    static final String UNSUPPORTED_SHAPE_FORMAT_ERROR = "Unsupported shape format";
    static final String UNSUPPORTED_SMOOTH_ERROR = "Smooth is not supported by this hardware (or driver)";
    static final String UNSUPPORTED_SMOOTH_LEVEL_ERROR = "Smooth level %1$s is not available. Using %2$s instead";
    static final String WRONG_SHADER_TYPE_ERROR = "shader() called with a wrong shader";
    public static boolean anisoSamplingSupported;
    public static boolean autoMipmapGenSupported;
    public static boolean blendEqSupported;
    protected static URL defColorShaderFragURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/ColorFrag.glsl");
    protected static URL defColorShaderVertURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/ColorVert.glsl");
    protected static URL defLightShaderFragURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/LightFrag.glsl");
    protected static URL defLightShaderVertURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/LightVert.glsl");
    protected static URL defLineShaderFragURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/LineFrag.glsl");
    protected static URL defLineShaderVertURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/LineVert.glsl");
    protected static URL defPointShaderFragURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/PointFrag.glsl");
    protected static URL defPointShaderVertURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/PointVert.glsl");
    protected static URL defTexlightShaderFragURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/TexLightFrag.glsl");
    protected static URL defTexlightShaderVertURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/TexLightVert.glsl");
    protected static URL defTextureShaderFragURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/TexFrag.glsl");
    protected static URL defTextureShaderVertURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/TexVert.glsl");
    public static int depthBits;
    public static boolean drawBufferSupported;
    public static boolean fboMultisampleSupported;
    protected static FloatBuffer floatBuffer;
    protected static boolean glParamsRead = false;
    protected static PMatrix3D identity = new PMatrix3D();
    protected static IntBuffer intBuffer;
    protected static URL maskShaderFragURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/MaskFrag.glsl");
    public static float maxAnisoAmount;
    public static int maxSamples;
    public static int maxTextureSize;
    public static boolean npotTexSupported;
    protected static final Set<AsyncPixelReader> ongoingPixelTransfers = new HashSet();
    protected static final List<AsyncPixelReader> ongoingPixelTransfersIterable = new ArrayList();
    public static boolean packedDepthStencilSupported;
    public static boolean readBufferSupported;
    public static int stencilBits;
    protected AsyncPixelReader asyncPixelReader;
    protected boolean asyncPixelReaderInitialized;
    protected boolean breakShape = false;
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
    public PMatrix3D camera;
    public float cameraAspect;
    public float cameraFOV;
    public float cameraFar;
    public PMatrix3D cameraInv;
    protected float[][] cameraInvStack = ((float[][]) Array.newInstance(Float.TYPE, new int[]{32, 16}));
    public float cameraNear;
    protected float[][] cameraStack = ((float[][]) Array.newInstance(Float.TYPE, new int[]{32, 16}));
    public float cameraX;
    public float cameraY;
    public float cameraZ;
    protected boolean clip = false;
    protected int[] clipRect = {0, 0, 0, 0};
    protected FrameBuffer currentFramebuffer;
    public float currentLightFalloffConstant;
    public float currentLightFalloffLinear;
    public float currentLightFalloffQuadratic;
    public float[] currentLightSpecular;
    public PGraphicsOpenGL currentPG;
    protected PShader defColorShader;
    protected PShader defLightShader;
    protected PShader defLineShader;
    protected PShader defPointShader;
    protected PShader defTexlightShader;
    protected PShader defTextureShader;
    protected boolean defaultEdges = false;
    protected FrameBuffer drawFramebuffer;
    protected boolean drawing = false;
    protected float eyeDist;
    protected FrameBuffer[] fbStack;
    protected int fbStackDepth;
    protected PImage filterImage;
    protected Texture filterTexture = null;
    protected int flushMode = 1;
    protected WeakHashMap<PFont, FontTexture> fontMap;
    protected float[] glModelview;
    protected float[] glNormal;
    protected float[] glProjection;
    protected float[] glProjmodelview;
    protected InGeometry inGeo;
    public boolean initialized;
    protected boolean isDepthSortingEnabled;
    protected int lastBlendMode = -1;
    protected int lastSmoothCall = -10;
    public float[] lightAmbient;
    public int lightCount = 0;
    public float[] lightDiffuse;
    public float[] lightFalloffCoefficients;
    public float[] lightNormal;
    public float[] lightPosition;
    public float[] lightSpecular;
    public float[] lightSpotParameters;
    public int[] lightType;
    public boolean lights;
    protected int lineBuffersContext;
    protected boolean lineBuffersCreated = false;
    protected PShader lineShader;
    protected boolean manipulatingCamera;
    protected PShader maskShader;
    public PMatrix3D modelview;
    public PMatrix3D modelviewInv;
    protected float[][] modelviewInvStack = ((float[][]) Array.newInstance(Float.TYPE, new int[]{32, 16}));
    protected float[][] modelviewStack = ((float[][]) Array.newInstance(Float.TYPE, new int[]{32, 16}));
    protected int modelviewStackDepth;
    protected FrameBuffer multisampleFramebuffer;
    protected IntBuffer nativePixelBuffer;
    protected int[] nativePixels;
    protected FrameBuffer offscreenFramebuffer;
    protected boolean offscreenMultisample;
    protected boolean openContour = false;
    public PGL pgl = createPGL(this);
    protected boolean pixOpChangedFB;
    protected IntBuffer pixelBuffer;
    protected int pixelsOp = 0;
    protected int pointBuffersContext;
    protected boolean pointBuffersCreated = false;
    protected PShader pointShader;
    protected AttributeMap polyAttribs;
    protected int polyBuffersContext;
    protected boolean polyBuffersCreated = false;
    protected PShader polyShader;
    public PMatrix3D projection;
    protected float[][] projectionStack = ((float[][]) Array.newInstance(Float.TYPE, new int[]{32, 16}));
    protected int projectionStackDepth;
    public PMatrix3D projmodelview;
    protected Texture ptexture = null;
    protected FrameBuffer readFramebuffer;
    protected boolean sized;
    protected int smoothCallCount = 0;
    protected boolean smoothDisabled = false;
    protected DepthSorter sorter;
    protected TessGeometry tessGeo;
    protected Tessellator tessellator;
    protected TexCache texCache;
    FontTexture textTex;
    protected Texture texture = null;
    protected int textureSampling = 5;
    protected int textureWrap = 0;
    protected IntBuffer viewport;

    protected class AsyncPixelReader {
        static final int BUFFER_COUNT = 3;
        static final int OPENGL_NATIVE = -1;
        static final int OPENGL_NATIVE_OPAQUE = -2;
        boolean calledThisFrame;
        long[] fences;
        String[] filenames;
        int head;
        int[] heights;
        int[] pbos;
        int size;
        boolean supportsAsyncTransfers;
        int tail;
        final /* synthetic */ PGraphicsOpenGL this$0;
        int[] widths;

        public AsyncPixelReader(PGraphicsOpenGL pGraphicsOpenGL) {
            this.this$0 = pGraphicsOpenGL;
            this.supportsAsyncTransfers = pGraphicsOpenGL.pgl.hasPBOs() && pGraphicsOpenGL.pgl.hasSynchronization();
            if (this.supportsAsyncTransfers) {
                this.pbos = new int[3];
                this.fences = new long[3];
                this.filenames = new String[3];
                this.widths = new int[3];
                this.heights = new int[3];
                IntBuffer allocateIntBuffer = PGL.allocateIntBuffer(3);
                allocateIntBuffer.rewind();
                pGraphicsOpenGL.pgl.genBuffers(3, allocateIntBuffer);
                for (int i = 0; i < 3; i++) {
                    this.pbos[i] = allocateIntBuffer.get(i);
                }
            }
        }

        public void beginTransfer(String str) {
            if (!(this.widths[this.head] == this.this$0.pixelWidth && this.heights[this.head] == this.this$0.pixelHeight)) {
                if (this.widths[this.head] * this.heights[this.head] != this.this$0.pixelWidth * this.this$0.pixelHeight) {
                    this.this$0.pgl.bindBuffer(PGL.PIXEL_PACK_BUFFER, this.pbos[this.head]);
                    this.this$0.pgl.bufferData(PGL.PIXEL_PACK_BUFFER, this.this$0.pixelWidth * 4 * this.this$0.pixelHeight, (Buffer) null, PGL.STREAM_READ);
                }
                this.widths[this.head] = this.this$0.pixelWidth;
                this.heights[this.head] = this.this$0.pixelHeight;
                this.this$0.pgl.bindBuffer(PGL.PIXEL_PACK_BUFFER, 0);
            }
            this.this$0.pgl.bindBuffer(PGL.PIXEL_PACK_BUFFER, this.pbos[this.head]);
            this.this$0.pgl.readPixels(0, 0, this.this$0.pixelWidth, this.this$0.pixelHeight, PGL.RGBA, PGL.UNSIGNED_BYTE, 0);
            this.this$0.pgl.bindBuffer(PGL.PIXEL_PACK_BUFFER, 0);
            this.fences[this.head] = this.this$0.pgl.fenceSync(PGL.SYNC_GPU_COMMANDS_COMPLETE, 0);
            this.filenames[this.head] = str;
            this.head = (this.head + 1) % 3;
            this.size++;
        }

        /* access modifiers changed from: protected */
        public void completeAllTransfers() {
            if (this.size > 0) {
                boolean z = false;
                if (!this.this$0.drawing) {
                    this.this$0.beginDraw();
                    z = true;
                }
                while (this.size > 0) {
                    endTransfer();
                }
                PGraphicsOpenGL.ongoingPixelTransfers.remove(this);
                if (z) {
                    this.this$0.endDraw();
                }
            }
        }

        public void completeFinishedTransfers() {
            if (this.size > 0 && PGraphicsOpenGL.asyncImageSaver.hasAvailableTarget()) {
                boolean z = false;
                if (!this.this$0.drawing) {
                    this.this$0.beginDraw();
                    z = true;
                }
                while (PGraphicsOpenGL.asyncImageSaver.hasAvailableTarget() && isLastTransferComplete()) {
                    endTransfer();
                }
                if (this.size <= 0) {
                    PGraphicsOpenGL.ongoingPixelTransfers.remove(this);
                }
                if (z) {
                    this.this$0.endDraw();
                }
            }
        }

        public void dispose() {
            if (this.fences != null) {
                while (this.size > 0) {
                    this.this$0.pgl.deleteSync(this.fences[this.tail]);
                    this.size--;
                    this.tail = (this.tail + 1) % 3;
                }
                this.fences = null;
            }
            if (this.pbos != null) {
                for (int i = 0; i < 3; i++) {
                    this.this$0.pgl.deleteBuffers(3, PGL.allocateIntBuffer(this.pbos));
                }
                this.pbos = null;
            }
            this.filenames = null;
            this.widths = null;
            this.heights = null;
            this.size = 0;
            this.head = 0;
            this.tail = 0;
            this.calledThisFrame = false;
            PGraphicsOpenGL.ongoingPixelTransfers.remove(this);
        }

        public void endTransfer() {
            this.this$0.pgl.deleteSync(this.fences[this.tail]);
            this.this$0.pgl.bindBuffer(PGL.PIXEL_PACK_BUFFER, this.pbos[this.tail]);
            ByteBuffer mapBuffer = this.this$0.pgl.mapBuffer(PGL.PIXEL_PACK_BUFFER, PGL.READ_ONLY);
            if (mapBuffer != null) {
                PImage availableTarget = PGraphicsOpenGL.asyncImageSaver.getAvailableTarget(this.widths[this.tail], this.heights[this.tail], this.this$0.primaryGraphics ? -2 : -1);
                if (availableTarget != null) {
                    mapBuffer.rewind();
                    mapBuffer.asIntBuffer().get(availableTarget.pixels);
                    this.this$0.pgl.unmapBuffer(PGL.PIXEL_PACK_BUFFER);
                    PGraphicsOpenGL.asyncImageSaver.saveTargetAsync(this.this$0, availableTarget, this.filenames[this.tail]);
                } else {
                    return;
                }
            }
            this.this$0.pgl.bindBuffer(PGL.PIXEL_PACK_BUFFER, 0);
            this.size--;
            this.tail = (this.tail + 1) % 3;
        }

        public boolean isLastTransferComplete() {
            if (this.size <= 0) {
                return false;
            }
            int clientWaitSync = this.this$0.pgl.clientWaitSync(this.fences[this.tail], 0, 0);
            return clientWaitSync == PGL.ALREADY_SIGNALED || clientWaitSync == PGL.CONDITION_SATISFIED;
        }

        public void readAndSaveAsync(String str) {
            if (this.size > 0) {
                boolean z = this.size == 3;
                if (!z) {
                    z = isLastTransferComplete();
                }
                if (z) {
                    endTransfer();
                }
            } else {
                PGraphicsOpenGL.ongoingPixelTransfers.add(this);
            }
            beginTransfer(str);
            this.calledThisFrame = true;
        }
    }

    protected static class AttributeMap extends HashMap<String, VertexAttribute> {
        public ArrayList<String> names = new ArrayList<>();
        public int numComp = 0;

        protected AttributeMap() {
        }

        public VertexAttribute get(int i) {
            return (VertexAttribute) super.get(this.names.get(i));
        }

        public VertexAttribute put(String str, VertexAttribute vertexAttribute) {
            VertexAttribute vertexAttribute2 = (VertexAttribute) super.put(str, vertexAttribute);
            this.names.add(str);
            if (vertexAttribute.kind == 2) {
                this.numComp += 4;
            } else {
                this.numComp += vertexAttribute.size;
            }
            return vertexAttribute2;
        }
    }

    protected static class DepthSorter {
        static final int W = 3;
        static final int X = 0;
        static final int X0 = 0;
        static final int X1 = 3;
        static final int X2 = 6;
        static final int Y = 1;
        static final int Y0 = 1;
        static final int Y1 = 4;
        static final int Y2 = 7;
        static final int Z = 2;
        static final int Z0 = 2;
        static final int Z1 = 5;
        static final int Z2 = 8;
        BitSet marked = new BitSet();
        float[] maxXBuffer = new float[0];
        float[] maxYBuffer = new float[0];
        float[] maxZBuffer = new float[0];
        float[] minXBuffer = new float[0];
        float[] minYBuffer = new float[0];
        float[] minZBuffer = new float[0];
        PGraphicsOpenGL pg;
        float[] screenVertices = new float[0];
        BitSet swapped = new BitSet();
        int[] texMap = new int[0];
        float[] triA = new float[9];
        float[] triB = new float[9];
        int[] triangleIndices = new int[0];
        int[] voffsetMap = new int[0];

        DepthSorter(PGraphicsOpenGL pGraphicsOpenGL) {
            this.pg = pGraphicsOpenGL;
        }

        static float dot(float f, float f2, float f3, float f4, float f5, float f6) {
            return (f * f4) + (f2 * f5) + (f3 * f6);
        }

        static void fetchTriCoords(float[] fArr, int i, int[] iArr, int[] iArr2, float[] fArr2, short[] sArr) {
            int i2 = iArr[iArr2[i]];
            int i3 = (sArr[(i * 3) + 0] + i2) * 3;
            int i4 = (sArr[(i * 3) + 1] + i2) * 3;
            int i5 = (i2 + sArr[(i * 3) + 2]) * 3;
            fArr[0] = fArr2[i3 + 0];
            fArr[1] = fArr2[i3 + 1];
            fArr[2] = fArr2[i3 + 2];
            fArr[3] = fArr2[i4 + 0];
            fArr[4] = fArr2[i4 + 1];
            fArr[5] = fArr2[i4 + 2];
            fArr[6] = fArr2[i5 + 0];
            fArr[7] = fArr2[i5 + 1];
            fArr[8] = fArr2[i5 + 2];
        }

        static void rotateRight(int[] iArr, int i, int i2) {
            if (i != i2) {
                int i3 = iArr[i2];
                System.arraycopy(iArr, i, iArr, i + 1, i2 - i);
                iArr[i] = i3;
            }
        }

        static int side(float[] fArr, float[] fArr2, float f) {
            float f2 = fArr[3] - fArr[0];
            float f3 = fArr[6] - fArr[0];
            float f4 = fArr[4] - fArr[1];
            float f5 = fArr[7] - fArr[1];
            float f6 = fArr[5] - fArr[2];
            float f7 = fArr[8] - fArr[2];
            float f8 = (f4 * f7) - (f6 * f5);
            float f9 = (f6 * f3) - (f7 * f2);
            float f10 = (f2 * f5) - (f3 * f4);
            float sqrt = 1.0f / ((float) Math.sqrt((double) (((f8 * f8) + (f9 * f9)) + (f10 * f10))));
            float f11 = f8 * sqrt;
            float f12 = f9 * sqrt;
            float f13 = f10 * sqrt;
            float f14 = -dot(f11, f12, f13, fArr[0], fArr[1], fArr[2]);
            float dot = dot(f11, f12, f13, fArr[0], fArr[1], fArr[2] + (100.0f * f)) + f14;
            float dot2 = dot(f11, f12, f13, fArr2[0], fArr2[1], fArr2[2]) + f14;
            float dot3 = dot(f11, f12, f13, fArr2[3], fArr2[4], fArr2[5]) + f14;
            float dot4 = dot(f11, f12, f13, fArr2[6], fArr2[7], fArr2[8]) + f14;
            float abs = PApplet.abs(dot2);
            float abs2 = PApplet.abs(dot3);
            float abs3 = PApplet.abs(dot4);
            float max = PApplet.max(abs, abs2, abs3) * 0.1f;
            float f15 = (abs < max ? 0.0f : dot2) * dot;
            float f16 = (abs2 < max ? 0.0f : dot3) * dot;
            if (abs3 < max) {
                dot4 = 0.0f;
            }
            float f17 = dot4 * dot;
            boolean z = f15 >= 0.0f && f16 >= 0.0f && f17 >= 0.0f;
            boolean z2 = f15 <= 0.0f && f16 <= 0.0f && f17 <= 0.0f;
            if (z) {
                return 1;
            }
            return z2 ? -1 : 0;
        }

        static void sortByMinZ(int i, int i2, int[] iArr, float[] fArr) {
            swap(iArr, i, (i + i2) / 2);
            float f = fArr[iArr[i]];
            int i3 = i;
            for (int i4 = i + 1; i4 <= i2; i4++) {
                if (fArr[iArr[i4]] < f) {
                    i3++;
                    swap(iArr, i3, i4);
                }
            }
            swap(iArr, i, i3);
            if (i < i3 - 1) {
                sortByMinZ(i, i3 - 1, iArr, fArr);
            }
            if (i3 + 1 < i2) {
                sortByMinZ(i3 + 1, i2, iArr, fArr);
            }
        }

        static void swap(int[] iArr, int i, int i2) {
            int i3 = iArr[i];
            iArr[i] = iArr[i2];
            iArr[i2] = i3;
        }

        /* access modifiers changed from: package-private */
        public void checkIndexBuffers(int i) {
            if (this.triangleIndices.length < i) {
                int i2 = ((i / 4) + 1) * 5;
                this.triangleIndices = new int[i2];
                this.texMap = new int[i2];
                this.voffsetMap = new int[i2];
                this.minXBuffer = new float[i2];
                this.minYBuffer = new float[i2];
                this.minZBuffer = new float[i2];
                this.maxXBuffer = new float[i2];
                this.maxYBuffer = new float[i2];
                this.maxZBuffer = new float[i2];
            }
        }

        /* access modifiers changed from: package-private */
        public void checkVertexBuffer(int i) {
            int i2 = i * 3;
            if (this.screenVertices.length < i2) {
                this.screenVertices = new float[(((i2 / 4) + 1) * 5)];
            }
        }

        /* access modifiers changed from: package-private */
        public void sort(TessGeometry tessGeometry) {
            float f;
            float f2;
            float f3;
            int i;
            int i2;
            float f4;
            boolean z;
            int i3 = tessGeometry.polyIndexCount / 3;
            checkIndexBuffers(i3);
            int[] iArr = this.triangleIndices;
            int[] iArr2 = this.texMap;
            int[] iArr3 = this.voffsetMap;
            for (int i4 = 0; i4 < i3; i4++) {
                iArr[i4] = i4;
            }
            TexCache texCache = this.pg.texCache;
            IndexCache indexCache = tessGeometry.polyIndexCache;
            for (int i5 = 0; i5 < texCache.size; i5++) {
                int i6 = texCache.firstCache[i5];
                int i7 = texCache.lastCache[i5];
                int i8 = i6;
                while (i8 <= i7) {
                    int i9 = i8 == i6 ? texCache.firstIndex[i5] : indexCache.indexOffset[i8];
                    int i10 = i8 == i7 ? (texCache.lastIndex[i5] - i9) + 1 : (indexCache.indexOffset[i8] + indexCache.indexCount[i8]) - i9;
                    for (int i11 = i9 / 3; i11 < (i9 + i10) / 3; i11++) {
                        iArr2[i11] = i5;
                        iArr3[i11] = i8;
                    }
                    i8++;
                }
            }
            int i12 = tessGeometry.polyVertexCount;
            checkVertexBuffer(i12);
            float[] fArr = this.screenVertices;
            float[] fArr2 = tessGeometry.polyVertices;
            PMatrix3D pMatrix3D = this.pg.projection;
            for (int i13 = 0; i13 < i12; i13++) {
                float f5 = fArr2[(i13 * 4) + 0];
                float f6 = fArr2[(i13 * 4) + 1];
                float f7 = fArr2[(i13 * 4) + 2];
                float f8 = fArr2[(i13 * 4) + 3];
                float f9 = (pMatrix3D.m00 * f5) + (pMatrix3D.m01 * f6) + (pMatrix3D.m02 * f7) + (pMatrix3D.m03 * f8);
                float f10 = (pMatrix3D.m13 * f8) + (pMatrix3D.m10 * f5) + (pMatrix3D.m11 * f6) + (pMatrix3D.m12 * f7);
                float f11 = (pMatrix3D.m20 * f5) + (pMatrix3D.m21 * f6) + (pMatrix3D.m22 * f7) + (pMatrix3D.m23 * f8);
                float f12 = (f5 * pMatrix3D.m30) + (f6 * pMatrix3D.m31) + (pMatrix3D.m32 * f7) + (pMatrix3D.m33 * f8);
                if (PGraphicsOpenGL.nonZero(f12)) {
                    f9 /= f12;
                    f10 /= f12;
                    f11 /= f12;
                }
                fArr[(i13 * 3) + 0] = f9;
                fArr[(i13 * 3) + 1] = f10;
                fArr[(i13 * 3) + 2] = -f11;
            }
            float[] fArr3 = this.screenVertices;
            int[] iArr4 = tessGeometry.polyIndexCache.vertexOffset;
            short[] sArr = tessGeometry.polyIndices;
            float[] fArr4 = this.triA;
            float[] fArr5 = this.triB;
            for (int i14 = 0; i14 < i3; i14++) {
                fetchTriCoords(fArr4, i14, iArr4, iArr3, fArr3, sArr);
                this.minXBuffer[i14] = PApplet.min(fArr4[0], fArr4[3], fArr4[6]);
                this.maxXBuffer[i14] = PApplet.max(fArr4[0], fArr4[3], fArr4[6]);
                this.minYBuffer[i14] = PApplet.min(fArr4[1], fArr4[4], fArr4[7]);
                this.maxYBuffer[i14] = PApplet.max(fArr4[1], fArr4[4], fArr4[7]);
                this.minZBuffer[i14] = PApplet.min(fArr4[2], fArr4[5], fArr4[8]);
                this.maxZBuffer[i14] = PApplet.max(fArr4[2], fArr4[5], fArr4[8]);
            }
            sortByMinZ(0, i3 - 1, iArr, this.minZBuffer);
            int i15 = 0;
            BitSet bitSet = this.marked;
            BitSet bitSet2 = this.swapped;
            bitSet.clear();
            while (true) {
                int i16 = i15;
                if (i16 >= i3) {
                    break;
                }
                int i17 = i16 + 1;
                boolean z2 = false;
                bitSet2.clear();
                int i18 = iArr[i16];
                float f13 = this.minXBuffer[i18];
                float f14 = this.maxXBuffer[i18];
                float f15 = this.minYBuffer[i18];
                float f16 = this.maxYBuffer[i18];
                float f17 = this.maxZBuffer[i18];
                fetchTriCoords(fArr4, i18, iArr4, iArr3, fArr3, sArr);
                int i19 = i18;
                float f18 = f17;
                while (!z2 && i17 < i3) {
                    int i20 = iArr[i17];
                    if (f18 <= this.minZBuffer[i20] && !bitSet.get(i20)) {
                        f = f15;
                        f2 = f14;
                        f3 = f13;
                        i = i19;
                        i2 = i17;
                        f4 = f16;
                        z = true;
                    } else if (f14 <= this.minXBuffer[i20] || f16 <= this.minYBuffer[i20] || f13 >= this.maxXBuffer[i20] || f15 >= this.maxYBuffer[i20]) {
                        f = f15;
                        f2 = f14;
                        f3 = f13;
                        i = i19;
                        i2 = i17 + 1;
                        f4 = f16;
                        z = z2;
                    } else {
                        fetchTriCoords(fArr5, i20, iArr4, iArr3, fArr3, sArr);
                        if (side(fArr5, fArr4, -1.0f) > 0) {
                            f = f15;
                            f2 = f14;
                            f3 = f13;
                            i = i19;
                            i2 = i17 + 1;
                            f4 = f16;
                            z = z2;
                        } else if (side(fArr4, fArr5, 1.0f) > 0) {
                            f = f15;
                            f2 = f14;
                            f3 = f13;
                            i = i19;
                            i2 = i17 + 1;
                            f4 = f16;
                            z = z2;
                        } else if (!bitSet2.get(i20)) {
                            bitSet2.set(i19);
                            bitSet.set(i20);
                            rotateRight(iArr, i16, i17);
                            System.arraycopy(fArr5, 0, fArr4, 0, 9);
                            float f19 = this.minXBuffer[i20];
                            float f20 = this.maxXBuffer[i20];
                            float f21 = this.minYBuffer[i20];
                            float f22 = this.maxYBuffer[i20];
                            f18 = this.maxZBuffer[i20];
                            i2 = i16 + 1;
                            z = z2;
                            float f23 = f20;
                            f3 = f19;
                            i = i20;
                            f4 = f22;
                            f = f21;
                            f2 = f23;
                        } else {
                            f = f15;
                            f2 = f14;
                            f3 = f13;
                            i = i19;
                            i2 = i17 + 1;
                            f4 = f16;
                            z = z2;
                        }
                    }
                    f14 = f2;
                    f13 = f3;
                    i19 = i;
                    z2 = z;
                    i17 = i2;
                    f15 = f;
                    f16 = f4;
                }
                i15 = i16 + 1;
            }
            for (int i21 = 0; i21 < i3; i21++) {
                int i22 = iArr[i21];
                if (i21 != i22) {
                    short s = sArr[(i21 * 3) + 0];
                    short s2 = sArr[(i21 * 3) + 1];
                    short s3 = sArr[(i21 * 3) + 2];
                    int i23 = iArr2[i21];
                    int i24 = iArr3[i21];
                    int i25 = i21;
                    while (true) {
                        iArr[i25] = i25;
                        sArr[(i25 * 3) + 0] = sArr[(i22 * 3) + 0];
                        sArr[(i25 * 3) + 1] = sArr[(i22 * 3) + 1];
                        sArr[(i25 * 3) + 2] = sArr[(i22 * 3) + 2];
                        iArr2[i25] = iArr2[i22];
                        iArr3[i25] = iArr3[i22];
                        int i26 = iArr[i22];
                        if (i26 == i21) {
                            break;
                        }
                        int i27 = i26;
                        i25 = i22;
                        i22 = i27;
                    }
                    iArr[i22] = i22;
                    sArr[(i22 * 3) + 0] = s;
                    sArr[(i22 * 3) + 1] = s2;
                    sArr[(i22 * 3) + 2] = s3;
                    iArr2[i22] = i23;
                    iArr3[i22] = i24;
                }
            }
        }
    }

    protected static class GLResourceFrameBuffer extends WeakReference<FrameBuffer> {
        private static List<GLResourceFrameBuffer> refList = new ArrayList();
        private static ReferenceQueue<FrameBuffer> refQueue = new ReferenceQueue<>();
        private int context;
        int glDepth;
        int glDepthStencil;
        int glFbo;
        int glMultisample;
        int glStencil;
        private PGL pgl;

        public GLResourceFrameBuffer(FrameBuffer frameBuffer) {
            super(frameBuffer, refQueue);
            drainRefQueueBounded();
            this.pgl = frameBuffer.pg.getPrimaryPGL();
            if (!frameBuffer.screenFb) {
                this.pgl.genFramebuffers(1, PGraphicsOpenGL.intBuffer);
                frameBuffer.glFbo = PGraphicsOpenGL.intBuffer.get(0);
                if (frameBuffer.multisample) {
                    this.pgl.genRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                    frameBuffer.glMultisample = PGraphicsOpenGL.intBuffer.get(0);
                }
                if (frameBuffer.packedDepthStencil) {
                    this.pgl.genRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                    frameBuffer.glDepthStencil = PGraphicsOpenGL.intBuffer.get(0);
                } else {
                    if (frameBuffer.depthBits > 0) {
                        this.pgl.genRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                        frameBuffer.glDepth = PGraphicsOpenGL.intBuffer.get(0);
                    }
                    if (frameBuffer.stencilBits > 0) {
                        this.pgl.genRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                        frameBuffer.glStencil = PGraphicsOpenGL.intBuffer.get(0);
                    }
                }
                this.glFbo = frameBuffer.glFbo;
                this.glDepth = frameBuffer.glDepth;
                this.glStencil = frameBuffer.glStencil;
                this.glDepthStencil = frameBuffer.glDepthStencil;
                this.glMultisample = frameBuffer.glMultisample;
            }
            this.context = frameBuffer.context;
            refList.add(this);
        }

        private void disposeNative() {
            if (this.pgl != null) {
                if (this.glFbo != 0) {
                    PGraphicsOpenGL.intBuffer.put(0, this.glFbo);
                    this.pgl.deleteFramebuffers(1, PGraphicsOpenGL.intBuffer);
                    this.glFbo = 0;
                }
                if (this.glDepth != 0) {
                    PGraphicsOpenGL.intBuffer.put(0, this.glDepth);
                    this.pgl.deleteRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                    this.glDepth = 0;
                }
                if (this.glStencil != 0) {
                    PGraphicsOpenGL.intBuffer.put(0, this.glStencil);
                    this.pgl.deleteRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                    this.glStencil = 0;
                }
                if (this.glDepthStencil != 0) {
                    PGraphicsOpenGL.intBuffer.put(0, this.glDepthStencil);
                    this.pgl.deleteRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                    this.glDepthStencil = 0;
                }
                if (this.glMultisample != 0) {
                    PGraphicsOpenGL.intBuffer.put(0, this.glMultisample);
                    this.pgl.deleteRenderbuffers(1, PGraphicsOpenGL.intBuffer);
                    this.glMultisample = 0;
                }
                this.pgl = null;
            }
        }

        static void drainRefQueueBounded() {
            GLResourceFrameBuffer gLResourceFrameBuffer;
            ReferenceQueue<FrameBuffer> referenceQueue = referenceQueue();
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < 10 && (gLResourceFrameBuffer = (GLResourceFrameBuffer) referenceQueue.poll()) != null) {
                    gLResourceFrameBuffer.dispose();
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }

        static ReferenceQueue<FrameBuffer> referenceQueue() {
            return refQueue;
        }

        /* access modifiers changed from: package-private */
        public void dispose() {
            refList.remove(this);
            disposeNative();
        }

        public boolean equals(Object obj) {
            GLResourceFrameBuffer gLResourceFrameBuffer = (GLResourceFrameBuffer) obj;
            return gLResourceFrameBuffer.glFbo == this.glFbo && gLResourceFrameBuffer.glDepth == this.glDepth && gLResourceFrameBuffer.glStencil == this.glStencil && gLResourceFrameBuffer.glDepthStencil == this.glDepthStencil && gLResourceFrameBuffer.glMultisample == this.glMultisample && gLResourceFrameBuffer.context == this.context;
        }

        public int hashCode() {
            return ((((((((((this.glFbo + 527) * 31) + this.glDepth) * 31) + this.glStencil) * 31) + this.glDepthStencil) * 31) + this.glMultisample) * 31) + this.context;
        }
    }

    protected static class GLResourceShader extends WeakReference<PShader> {
        private static List<GLResourceShader> refList = new ArrayList();
        private static ReferenceQueue<PShader> refQueue = new ReferenceQueue<>();
        private int context;
        int glFragment;
        int glProgram;
        int glVertex;
        private PGL pgl;

        public GLResourceShader(PShader pShader) {
            super(pShader, refQueue);
            drainRefQueueBounded();
            this.pgl = pShader.pgl.graphics.getPrimaryPGL();
            pShader.glProgram = this.pgl.createProgram();
            pShader.glVertex = this.pgl.createShader(PGL.VERTEX_SHADER);
            pShader.glFragment = this.pgl.createShader(PGL.FRAGMENT_SHADER);
            this.glProgram = pShader.glProgram;
            this.glVertex = pShader.glVertex;
            this.glFragment = pShader.glFragment;
            this.context = pShader.context;
            refList.add(this);
        }

        private void disposeNative() {
            if (this.pgl != null) {
                if (this.glFragment != 0) {
                    this.pgl.deleteShader(this.glFragment);
                    this.glFragment = 0;
                }
                if (this.glVertex != 0) {
                    this.pgl.deleteShader(this.glVertex);
                    this.glVertex = 0;
                }
                if (this.glProgram != 0) {
                    this.pgl.deleteProgram(this.glProgram);
                    this.glProgram = 0;
                }
                this.pgl = null;
            }
        }

        static void drainRefQueueBounded() {
            GLResourceShader gLResourceShader;
            ReferenceQueue<PShader> referenceQueue = referenceQueue();
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < 10 && (gLResourceShader = (GLResourceShader) referenceQueue.poll()) != null) {
                    gLResourceShader.dispose();
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }

        static ReferenceQueue<PShader> referenceQueue() {
            return refQueue;
        }

        /* access modifiers changed from: package-private */
        public void dispose() {
            refList.remove(this);
            disposeNative();
        }

        public boolean equals(Object obj) {
            GLResourceShader gLResourceShader = (GLResourceShader) obj;
            return gLResourceShader.glProgram == this.glProgram && gLResourceShader.glVertex == this.glVertex && gLResourceShader.glFragment == this.glFragment && gLResourceShader.context == this.context;
        }

        public int hashCode() {
            return ((((((this.glProgram + 527) * 31) + this.glVertex) * 31) + this.glFragment) * 31) + this.context;
        }
    }

    protected static class GLResourceTexture extends WeakReference<Texture> {
        private static List<GLResourceTexture> refList = new ArrayList();
        private static ReferenceQueue<Texture> refQueue = new ReferenceQueue<>();
        private int context;
        int glName;
        private PGL pgl;

        public GLResourceTexture(Texture texture) {
            super(texture, refQueue);
            drainRefQueueBounded();
            this.pgl = texture.pg.getPrimaryPGL();
            this.pgl.genTextures(1, PGraphicsOpenGL.intBuffer);
            texture.glName = PGraphicsOpenGL.intBuffer.get(0);
            this.glName = texture.glName;
            this.context = texture.context;
            refList.add(this);
        }

        private void disposeNative() {
            if (this.pgl != null) {
                if (this.glName != 0) {
                    PGraphicsOpenGL.intBuffer.put(0, this.glName);
                    this.pgl.deleteTextures(1, PGraphicsOpenGL.intBuffer);
                    this.glName = 0;
                }
                this.pgl = null;
            }
        }

        static void drainRefQueueBounded() {
            GLResourceTexture gLResourceTexture;
            ReferenceQueue<Texture> referenceQueue = referenceQueue();
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < 10 && (gLResourceTexture = (GLResourceTexture) referenceQueue.poll()) != null) {
                    gLResourceTexture.dispose();
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }

        static ReferenceQueue<Texture> referenceQueue() {
            return refQueue;
        }

        /* access modifiers changed from: package-private */
        public void dispose() {
            refList.remove(this);
            disposeNative();
        }

        public boolean equals(Object obj) {
            GLResourceTexture gLResourceTexture = (GLResourceTexture) obj;
            return gLResourceTexture.glName == this.glName && gLResourceTexture.context == this.context;
        }

        public int hashCode() {
            return ((this.glName + 527) * 31) + this.context;
        }
    }

    protected static class GLResourceVertexBuffer extends WeakReference<VertexBuffer> {
        private static List<GLResourceVertexBuffer> refList = new ArrayList();
        private static ReferenceQueue<VertexBuffer> refQueue = new ReferenceQueue<>();
        private int context;
        int glId;
        private PGL pgl;

        public GLResourceVertexBuffer(VertexBuffer vertexBuffer) {
            super(vertexBuffer, refQueue);
            drainRefQueueBounded();
            this.pgl = vertexBuffer.pgl.graphics.getPrimaryPGL();
            this.pgl.genBuffers(1, PGraphicsOpenGL.intBuffer);
            vertexBuffer.glId = PGraphicsOpenGL.intBuffer.get(0);
            this.glId = vertexBuffer.glId;
            this.context = vertexBuffer.context;
            refList.add(this);
        }

        private void disposeNative() {
            if (this.pgl != null) {
                if (this.glId != 0) {
                    PGraphicsOpenGL.intBuffer.put(0, this.glId);
                    this.pgl.deleteBuffers(1, PGraphicsOpenGL.intBuffer);
                    this.glId = 0;
                }
                this.pgl = null;
            }
        }

        static void drainRefQueueBounded() {
            GLResourceVertexBuffer gLResourceVertexBuffer;
            ReferenceQueue<VertexBuffer> referenceQueue = referenceQueue();
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < 10 && (gLResourceVertexBuffer = (GLResourceVertexBuffer) referenceQueue.poll()) != null) {
                    gLResourceVertexBuffer.dispose();
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }

        static ReferenceQueue<VertexBuffer> referenceQueue() {
            return refQueue;
        }

        /* access modifiers changed from: package-private */
        public void dispose() {
            refList.remove(this);
            disposeNative();
        }

        public boolean equals(Object obj) {
            GLResourceVertexBuffer gLResourceVertexBuffer = (GLResourceVertexBuffer) obj;
            return gLResourceVertexBuffer.glId == this.glId && gLResourceVertexBuffer.context == this.context;
        }

        public int hashCode() {
            return ((this.glId + 527) * 31) + this.context;
        }
    }

    protected static class InGeometry {
        int[] ambient;
        int ambientColor;
        AttributeMap attribs;
        HashMap<String, byte[]> battribs;
        int codeCount;
        int[] codes;
        int[] colors;
        int edgeCount;
        int[][] edges;
        int[] emissive;
        int emissiveColor;
        HashMap<String, float[]> fattribs;
        int fillColor;
        HashMap<String, int[]> iattribs;
        float normalX;
        float normalY;
        float normalZ;
        float[] normals;
        PGraphicsOpenGL pg;
        int renderMode;
        float[] shininess;
        float shininessFactor;
        int[] specular;
        int specularColor;
        int strokeColor;
        int[] strokeColors;
        float strokeWeight;
        float[] strokeWeights;
        float[] texcoords;
        int vertexCount;
        float[] vertices;

        InGeometry(PGraphicsOpenGL pGraphicsOpenGL, AttributeMap attributeMap, int i) {
            this.pg = pGraphicsOpenGL;
            this.attribs = attributeMap;
            this.renderMode = i;
            allocate();
        }

        /* access modifiers changed from: package-private */
        public void addArc(float f, float f2, float f3, float f4, float f5, float f6, boolean z, boolean z2, int i) {
            int addVertex;
            float f7 = f3 / 2.0f;
            float f8 = f4 / 2.0f;
            float f9 = f + f7;
            float f10 = f2 + f8;
            int i2 = (int) (0.5f + ((f5 / 6.2831855f) * 720.0f));
            int i3 = (int) (0.5f + ((f6 / 6.2831855f) * 720.0f));
            int constrain = PApplet.constrain(i3 - i2, 0, 720);
            boolean z3 = constrain == 720;
            if (z3 && i == 2) {
                constrain--;
                i3--;
            }
            int i4 = i2 % 720;
            if (i4 < 0) {
                i4 += 720;
            }
            int i5 = i3 % 720;
            if (i5 < 0) {
                i5 += 720;
            }
            int addVertex2 = (i == 2 || i == 1) ? addVertex(((PGraphicsOpenGL.cosLUT[i4] + PGraphicsOpenGL.cosLUT[i5]) * 0.5f * f7) + f9, ((PGraphicsOpenGL.sinLUT[i5] + PGraphicsOpenGL.sinLUT[i4]) * 0.5f * f8) + f10, 0, true) : addVertex(f9, f10, 0, true);
            int max = PApplet.max(1, 720 / PApplet.min((int) PGraphicsOpenGL.MAX_POINT_ACCURACY, PApplet.max(20, (int) ((PApplet.dist(this.pg.screenX(f, f2), this.pg.screenY(f, f2), this.pg.screenX(f + f3, f2 + f4), this.pg.screenY(f + f3, f2 + f4)) * 6.2831855f) / PGraphicsOpenGL.POINT_ACCURACY_FACTOR))));
            int i6 = -max;
            int i7 = addVertex2;
            while (true) {
                int min = PApplet.min(i6 + max, constrain);
                int i8 = i4 + min;
                if (i8 >= 720) {
                    i8 -= 720;
                }
                addVertex = addVertex((PGraphicsOpenGL.cosLUT[i8] * f7) + f9, f10 + (PGraphicsOpenGL.sinLUT[i8] * f8), 0, min == 0 && !z);
                if (z2) {
                    if (i == 2 || i == 3) {
                        addEdge(i7, addVertex, min == 0, false);
                    } else if (min > 0) {
                        addEdge(i7, addVertex, min == PApplet.min(max, constrain), min == constrain && !z3);
                    }
                }
                if (min >= constrain) {
                    break;
                }
                i6 = min;
                i7 = addVertex;
            }
            if (!z2) {
                return;
            }
            if (i == 2 || i == 3) {
                addEdge(addVertex, addVertex2, false, false);
                closeEdge(addVertex, addVertex2);
            } else if (z3) {
                closeEdge(i7, addVertex);
            }
        }

        public void addBezierVertex(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, boolean z) {
            addVertex(f, f2, f3, 1, z);
            addVertex(f4, f5, f6, -1, false);
            addVertex(f7, f8, f9, -1, false);
        }

        /* access modifiers changed from: package-private */
        public void addBox(float f, float f2, float f3, boolean z, boolean z2) {
            boolean z3 = ((f2 > 0.0f ? 1 : (f2 == 0.0f ? 0 : -1)) > 0) != ((f3 > 0.0f ? 1 : (f3 == 0.0f ? 0 : -1)) > 0);
            boolean z4 = ((f > 0.0f ? 1 : (f == 0.0f ? 0 : -1)) > 0) != ((f3 > 0.0f ? 1 : (f3 == 0.0f ? 0 : -1)) > 0);
            boolean z5 = ((f > 0.0f ? 1 : (f == 0.0f ? 0 : -1)) > 0) != ((f2 > 0.0f ? 1 : (f2 == 0.0f ? 0 : -1)) > 0);
            int i = z3 ? -1 : 1;
            int i2 = z4 ? -1 : 1;
            int i3 = z5 ? -1 : 1;
            float f4 = (-f) / 2.0f;
            float f5 = f / 2.0f;
            float f6 = (-f2) / 2.0f;
            float f7 = f2 / 2.0f;
            float f8 = (-f3) / 2.0f;
            float f9 = f3 / 2.0f;
            if (z || z2) {
                setNormal(0.0f, 0.0f, (float) (-i3));
                int addVertex = addVertex(f4, f6, f8, 0.0f, 0.0f, 0, true);
                int addVertex2 = addVertex(f4, f7, f8, 0.0f, 1.0f, 0, false);
                int addVertex3 = addVertex(f5, f7, f8, 1.0f, 1.0f, 0, false);
                int addVertex4 = addVertex(f5, f6, f8, 1.0f, 0.0f, 0, false);
                if (z2) {
                    addEdge(addVertex, addVertex2, true, false);
                    addEdge(addVertex2, addVertex3, false, false);
                    addEdge(addVertex3, addVertex4, false, false);
                    addEdge(addVertex4, addVertex, false, false);
                    closeEdge(addVertex4, addVertex);
                }
                setNormal(0.0f, 0.0f, (float) i3);
                int addVertex5 = addVertex(f4, f7, f9, 1.0f, 1.0f, 0, false);
                int addVertex6 = addVertex(f4, f6, f9, 1.0f, 0.0f, 0, false);
                int addVertex7 = addVertex(f5, f6, f9, 0.0f, 0.0f, 0, false);
                int addVertex8 = addVertex(f5, f7, f9, 0.0f, 1.0f, 0, false);
                if (z2) {
                    addEdge(addVertex5, addVertex6, true, false);
                    addEdge(addVertex6, addVertex7, false, false);
                    addEdge(addVertex7, addVertex8, false, false);
                    addEdge(addVertex8, addVertex5, false, false);
                    closeEdge(addVertex8, addVertex5);
                }
                setNormal((float) i, 0.0f, 0.0f);
                int addVertex9 = addVertex(f5, f6, f8, 0.0f, 0.0f, 0, false);
                int addVertex10 = addVertex(f5, f7, f8, 0.0f, 1.0f, 0, false);
                int addVertex11 = addVertex(f5, f7, f9, 1.0f, 1.0f, 0, false);
                int addVertex12 = addVertex(f5, f6, f9, 1.0f, 0.0f, 0, false);
                if (z2) {
                    addEdge(addVertex9, addVertex10, true, false);
                    addEdge(addVertex10, addVertex11, false, false);
                    addEdge(addVertex11, addVertex12, false, false);
                    addEdge(addVertex12, addVertex9, false, false);
                    closeEdge(addVertex12, addVertex9);
                }
                setNormal((float) (-i), 0.0f, 0.0f);
                int addVertex13 = addVertex(f4, f7, f8, 1.0f, 1.0f, 0, false);
                int addVertex14 = addVertex(f4, f6, f8, 1.0f, 0.0f, 0, false);
                int addVertex15 = addVertex(f4, f6, f9, 0.0f, 0.0f, 0, false);
                int addVertex16 = addVertex(f4, f7, f9, 0.0f, 1.0f, 0, false);
                if (z2) {
                    addEdge(addVertex13, addVertex14, true, false);
                    addEdge(addVertex14, addVertex15, false, false);
                    addEdge(addVertex15, addVertex16, false, false);
                    addEdge(addVertex16, addVertex13, false, false);
                    closeEdge(addVertex16, addVertex13);
                }
                setNormal(0.0f, (float) (-i2), 0.0f);
                int addVertex17 = addVertex(f5, f6, f8, 1.0f, 1.0f, 0, false);
                int addVertex18 = addVertex(f5, f6, f9, 1.0f, 0.0f, 0, false);
                int addVertex19 = addVertex(f4, f6, f9, 0.0f, 0.0f, 0, false);
                int addVertex20 = addVertex(f4, f6, f8, 0.0f, 1.0f, 0, false);
                if (z2) {
                    addEdge(addVertex17, addVertex18, true, false);
                    addEdge(addVertex18, addVertex19, false, false);
                    addEdge(addVertex19, addVertex20, false, false);
                    addEdge(addVertex20, addVertex17, false, false);
                    closeEdge(addVertex20, addVertex17);
                }
                setNormal(0.0f, (float) i2, 0.0f);
                int addVertex21 = addVertex(f4, f7, f8, 0.0f, 0.0f, 0, false);
                int addVertex22 = addVertex(f4, f7, f9, 0.0f, 1.0f, 0, false);
                int addVertex23 = addVertex(f5, f7, f9, 1.0f, 1.0f, 0, false);
                int addVertex24 = addVertex(f5, f7, f8, 1.0f, 0.0f, 0, false);
                if (z2) {
                    addEdge(addVertex21, addVertex22, true, false);
                    addEdge(addVertex22, addVertex23, false, false);
                    addEdge(addVertex23, addVertex24, false, false);
                    addEdge(addVertex24, addVertex21, false, false);
                    closeEdge(addVertex24, addVertex21);
                }
            }
        }

        public void addCurveVertex(float f, float f2, float f3, boolean z) {
            addVertex(f, f2, f3, 3, z);
        }

        /* access modifiers changed from: package-private */
        public int addEdge(int i, int i2, boolean z, boolean z2) {
            int i3 = 1;
            edgeCheck();
            int[] iArr = this.edges[this.edgeCount];
            iArr[0] = i;
            iArr[1] = i2;
            int i4 = z ? 1 : 0;
            if (!z2) {
                i3 = 0;
            }
            iArr[2] = (i3 * 2) + i4;
            this.edgeCount++;
            return this.edgeCount - 1;
        }

        /* access modifiers changed from: package-private */
        public void addEllipse(float f, float f2, float f3, float f4, boolean z, boolean z2) {
            int i;
            float f5 = f3 / 2.0f;
            float f6 = f4 / 2.0f;
            float f7 = f + f5;
            float f8 = f2 + f6;
            int min = PApplet.min((int) PGraphicsOpenGL.MAX_POINT_ACCURACY, PApplet.max(20, (int) ((PApplet.dist(this.pg.screenX(f, f2), this.pg.screenY(f, f2), this.pg.screenX(f + f3, f2 + f4), this.pg.screenY(f + f3, f2 + f4)) * 6.2831855f) / PGraphicsOpenGL.POINT_ACCURACY_FACTOR)));
            float f9 = 720.0f / ((float) min);
            if (z) {
                addVertex(f7, f8, 0, true);
            }
            int i2 = 0;
            int i3 = 0;
            float f10 = 0.0f;
            int i4 = 0;
            int i5 = 0;
            while (i3 < min) {
                i2 = addVertex(f7 + (PGraphicsOpenGL.cosLUT[(int) f10] * f5), f8 + (PGraphicsOpenGL.sinLUT[(int) f10] * f6), 0, i3 == 0 && !z);
                f10 = (f10 + f9) % 720.0f;
                if (i3 <= 0) {
                    i = i2;
                } else if (z2) {
                    addEdge(i4, i2, i3 == 1, false);
                    i = i5;
                } else {
                    i = i5;
                }
                i3++;
                i4 = i2;
                i5 = i;
            }
            addVertex((PGraphicsOpenGL.cosLUT[0] * f5) + f7, (PGraphicsOpenGL.sinLUT[0] * f6) + f8, 0, false);
            if (z2) {
                addEdge(i2, i5, false, false);
                closeEdge(i2, i5);
            }
        }

        /* access modifiers changed from: package-private */
        public void addLine(float f, float f2, float f3, float f4, float f5, float f6, boolean z, boolean z2) {
            int addVertex = addVertex(f, f2, f3, 0, true);
            int addVertex2 = addVertex(f4, f5, f6, 0, false);
            if (z2) {
                addEdge(addVertex, addVertex2, true, true);
            }
        }

        /* access modifiers changed from: package-private */
        public void addPoint(float f, float f2, float f3, boolean z, boolean z2) {
            addVertex(f, f2, f3, 0, true);
        }

        /* access modifiers changed from: package-private */
        public void addQuad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, boolean z) {
            int addVertex = addVertex(f, f2, f3, 0.0f, 0.0f, 0, true);
            int addVertex2 = addVertex(f4, f5, f6, 1.0f, 0.0f, 0, false);
            int addVertex3 = addVertex(f7, f8, f9, 1.0f, 1.0f, 0, false);
            int addVertex4 = addVertex(f10, f11, f12, 0.0f, 1.0f, 0, false);
            if (z) {
                addEdge(addVertex, addVertex2, true, false);
                addEdge(addVertex2, addVertex3, false, false);
                addEdge(addVertex3, addVertex4, false, false);
                addEdge(addVertex4, addVertex, false, false);
                closeEdge(addVertex4, addVertex);
            }
        }

        /* access modifiers changed from: package-private */
        public void addQuadStripEdges() {
            for (int i = 1; i < this.vertexCount / 2; i++) {
                int i2 = (i - 1) * 2;
                int i3 = ((i - 1) * 2) + 1;
                int i4 = (i * 2) + 1;
                int i5 = i * 2;
                addEdge(i2, i3, true, false);
                addEdge(i3, i4, false, false);
                addEdge(i4, i5, false, false);
                addEdge(i5, i2, false, true);
                closeEdge(i5, i2);
            }
        }

        public void addQuadraticVertex(float f, float f2, float f3, float f4, float f5, float f6, boolean z) {
            addVertex(f, f2, f3, 2, z);
            addVertex(f4, f5, f6, -1, false);
        }

        /* access modifiers changed from: package-private */
        public void addQuadsEdges() {
            for (int i = 0; i < this.vertexCount / 4; i++) {
                int i2 = (i * 4) + 0;
                int i3 = (i * 4) + 1;
                int i4 = (i * 4) + 2;
                int i5 = (i * 4) + 3;
                addEdge(i2, i3, true, false);
                addEdge(i3, i4, false, false);
                addEdge(i4, i5, false, false);
                addEdge(i5, i2, false, false);
                closeEdge(i5, i2);
            }
        }

        /* access modifiers changed from: package-private */
        public void addRect(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, boolean z) {
            if (PGraphicsOpenGL.nonZero(f6)) {
                addVertex(f3 - f6, f2, 0, true);
                addQuadraticVertex(f3, f2, 0.0f, f3, f2 + f6, 0.0f, false);
            } else {
                addVertex(f3, f2, 0, true);
            }
            if (PGraphicsOpenGL.nonZero(f7)) {
                addVertex(f3, f4 - f7, 0, false);
                addQuadraticVertex(f3, f4, 0.0f, f3 - f7, f4, 0.0f, false);
            } else {
                addVertex(f3, f4, 0, false);
            }
            if (PGraphicsOpenGL.nonZero(f8)) {
                addVertex(f + f8, f4, 0, false);
                addQuadraticVertex(f, f4, 0.0f, f, f4 - f8, 0.0f, false);
            } else {
                addVertex(f, f4, 0, false);
            }
            if (PGraphicsOpenGL.nonZero(f5)) {
                addVertex(f, f2 + f5, 0, false);
                addQuadraticVertex(f, f2, 0.0f, f + f5, f2, 0.0f, false);
                return;
            }
            addVertex(f, f2, 0, false);
        }

        /* access modifiers changed from: package-private */
        public void addRect(float f, float f2, float f3, float f4, boolean z) {
            addQuad(f, f2, 0.0f, f3, f2, 0.0f, f3, f4, 0.0f, f, f4, 0.0f, z);
        }

        /* access modifiers changed from: package-private */
        public int[] addSphere(float f, int i, int i2, boolean z, boolean z2) {
            int[] iArr = new int[((i * 3) + (((i * 6) + 3) * (i2 - 2)) + (i * 3))];
            float f2 = 1.0f / ((float) i);
            float f3 = 1.0f / ((float) i2);
            float f4 = 1.0f;
            int i3 = 0;
            while (true) {
                int i4 = i3;
                if (i4 >= i) {
                    break;
                }
                setNormal(0.0f, 1.0f, 0.0f);
                addVertex(0.0f, f, 0.0f, f4, 1.0f, 0, true);
                f4 -= f2;
                i3 = i4 + 1;
            }
            float f5 = 1.0f;
            float f6 = 1.0f - f3;
            int i5 = 0;
            while (true) {
                int i6 = i5;
                if (i6 >= i) {
                    break;
                }
                setNormal(this.pg.sphereX[i6], this.pg.sphereY[i6], this.pg.sphereZ[i6]);
                addVertex(f * this.pg.sphereX[i6], f * this.pg.sphereY[i6], f * this.pg.sphereZ[i6], f5, f6, 0, false);
                f5 -= f2;
                i5 = i6 + 1;
            }
            setNormal(this.pg.sphereX[0], this.pg.sphereY[0], this.pg.sphereZ[0]);
            addVertex(f * this.pg.sphereX[0], f * this.pg.sphereY[0], f * this.pg.sphereZ[0], f5, f6, 0, false);
            int i7 = i + i + 1;
            for (int i8 = 0; i8 < i; i8++) {
                int i9 = i + i8;
                int i10 = (i + i8) - i;
                iArr[(i8 * 3) + 0] = i9;
                iArr[(i8 * 3) + 1] = i10;
                iArr[(i8 * 3) + 2] = i9 + 1;
                addEdge(i10, i9, true, true);
                addEdge(i9, i9 + 1, true, true);
            }
            int i11 = 2;
            int i12 = i;
            int i13 = 0 + (i * 3);
            int i14 = 0;
            while (i11 < i2) {
                int i15 = i14 + i;
                float f7 = 1.0f;
                f6 -= f3;
                int i16 = 0;
                while (true) {
                    int i17 = i16;
                    if (i17 >= i) {
                        break;
                    }
                    int i18 = i15 + i17;
                    setNormal(this.pg.sphereX[i18], this.pg.sphereY[i18], this.pg.sphereZ[i18]);
                    addVertex(this.pg.sphereX[i18] * f, this.pg.sphereY[i18] * f, f * this.pg.sphereZ[i18], f7, f6, 0, false);
                    f7 -= f2;
                    i16 = i17 + 1;
                }
                int i19 = i7 + i;
                setNormal(this.pg.sphereX[i15], this.pg.sphereY[i15], this.pg.sphereZ[i15]);
                addVertex(f * this.pg.sphereX[i15], f * this.pg.sphereY[i15], f * this.pg.sphereZ[i15], f7, f6, 0, false);
                int i20 = i19 + 1;
                for (int i21 = 0; i21 < i; i21++) {
                    int i22 = i7 + i21;
                    int i23 = ((i7 + i21) - i) - 1;
                    iArr[(i21 * 6) + i13 + 0] = i22;
                    iArr[(i21 * 6) + i13 + 1] = i23;
                    iArr[(i21 * 6) + i13 + 2] = i23 + 1;
                    iArr[(i21 * 6) + i13 + 3] = i22;
                    iArr[(i21 * 6) + i13 + 4] = i23 + 1;
                    iArr[(i21 * 6) + i13 + 5] = i22 + 1;
                    addEdge(i23, i22, true, true);
                    addEdge(i22, i22 + 1, true, true);
                    addEdge(i23 + 1, i22, true, true);
                }
                int i24 = (i * 6) + i13;
                iArr[i24 + 0] = i19;
                iArr[i24 + 1] = i19 - i;
                iArr[i24 + 2] = i19 - 1;
                i11++;
                i13 = i24 + 3;
                i14 = i15;
                i12 = i7;
                i7 = i20;
            }
            float f8 = 1.0f;
            int i25 = 0;
            while (true) {
                int i26 = i25;
                if (i26 >= i) {
                    break;
                }
                setNormal(0.0f, -1.0f, 0.0f);
                addVertex(0.0f, -f, 0.0f, f8, 0.0f, 0, false);
                f8 -= f2;
                i25 = i26 + 1;
            }
            int i27 = i7 + i;
            for (int i28 = 0; i28 < i; i28++) {
                int i29 = i12 + i28;
                int i30 = i12 + i28 + i + 1;
                iArr[(i28 * 3) + i13 + 0] = i30;
                iArr[(i28 * 3) + i13 + 1] = i29;
                iArr[(i28 * 3) + i13 + 2] = i29 + 1;
                addEdge(i29, i29 + 1, true, true);
                addEdge(i29, i30, true, true);
            }
            int i31 = (i * 3) + i13;
            return iArr;
        }

        /* access modifiers changed from: package-private */
        public void addTriangle(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, boolean z, boolean z2) {
            int addVertex = addVertex(f, f2, f3, 0, true);
            int addVertex2 = addVertex(f4, f5, f6, 0, false);
            int addVertex3 = addVertex(f7, f8, f9, 0, false);
            if (z2) {
                addEdge(addVertex, addVertex2, true, false);
                addEdge(addVertex2, addVertex3, false, false);
                addEdge(addVertex3, addVertex, false, false);
                closeEdge(addVertex3, addVertex);
            }
        }

        /* access modifiers changed from: package-private */
        public void addTriangleFanEdges() {
            for (int i = 1; i < this.vertexCount - 1; i++) {
                int i2 = i + 1;
                addEdge(0, i, true, false);
                addEdge(i, i2, false, false);
                addEdge(i2, 0, false, false);
                closeEdge(i2, 0);
            }
        }

        /* access modifiers changed from: package-private */
        public void addTriangleStripEdges() {
            int i;
            int i2;
            for (int i3 = 1; i3 < this.vertexCount - 1; i3++) {
                if (i3 % 2 == 0) {
                    i = i3 - 1;
                    i2 = i3 + 1;
                } else {
                    i = i3 + 1;
                    i2 = i3 - 1;
                }
                addEdge(i3, i, true, false);
                addEdge(i, i2, false, false);
                addEdge(i2, i3, false, false);
                closeEdge(i2, i3);
            }
        }

        /* access modifiers changed from: package-private */
        public void addTrianglesEdges() {
            for (int i = 0; i < this.vertexCount / 3; i++) {
                int i2 = (i * 3) + 0;
                int i3 = (i * 3) + 1;
                int i4 = (i * 3) + 2;
                addEdge(i2, i3, true, false);
                addEdge(i3, i4, false, false);
                addEdge(i4, i2, false, false);
                closeEdge(i4, i2);
            }
        }

        /* access modifiers changed from: package-private */
        public int addVertex(float f, float f2, float f3, float f4, float f5, int i, boolean z) {
            return addVertex(f, f2, f3, this.fillColor, this.normalX, this.normalY, this.normalZ, f4, f5, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininessFactor, i, z);
        }

        /* access modifiers changed from: package-private */
        public int addVertex(float f, float f2, float f3, float f4, float f5, boolean z) {
            return addVertex(f, f2, f3, this.fillColor, this.normalX, this.normalY, this.normalZ, f4, f5, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininessFactor, 0, z);
        }

        /* access modifiers changed from: package-private */
        public int addVertex(float f, float f2, float f3, float f4, int i, boolean z) {
            return addVertex(f, f2, 0.0f, this.fillColor, this.normalX, this.normalY, this.normalZ, f3, f4, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininessFactor, i, z);
        }

        /* access modifiers changed from: package-private */
        public int addVertex(float f, float f2, float f3, float f4, boolean z) {
            return addVertex(f, f2, 0.0f, this.fillColor, this.normalX, this.normalY, this.normalZ, f3, f4, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininessFactor, 0, z);
        }

        /* access modifiers changed from: package-private */
        public int addVertex(float f, float f2, float f3, int i, float f4, float f5, float f6, float f7, float f8, int i2, float f9, int i3, int i4, int i5, float f10, int i6, boolean z) {
            vertexCheck();
            int i7 = this.vertexCount * 3;
            int i8 = i7 + 1;
            this.vertices[i7] = f;
            this.vertices[i8] = f2;
            this.vertices[i8 + 1] = f3;
            this.colors[this.vertexCount] = PGL.javaToNativeARGB(i);
            int i9 = this.vertexCount * 3;
            int i10 = i9 + 1;
            this.normals[i9] = f4;
            this.normals[i10] = f5;
            this.normals[i10 + 1] = f6;
            int i11 = this.vertexCount * 2;
            this.texcoords[i11] = f7;
            this.texcoords[i11 + 1] = f8;
            this.strokeColors[this.vertexCount] = PGL.javaToNativeARGB(i2);
            this.strokeWeights[this.vertexCount] = f9;
            this.ambient[this.vertexCount] = PGL.javaToNativeARGB(i3);
            this.specular[this.vertexCount] = PGL.javaToNativeARGB(i4);
            this.emissive[this.vertexCount] = PGL.javaToNativeARGB(i5);
            this.shininess[this.vertexCount] = f10;
            for (String str : this.attribs.keySet()) {
                VertexAttribute vertexAttribute = (VertexAttribute) this.attribs.get(str);
                int i12 = vertexAttribute.size * this.vertexCount;
                if (vertexAttribute.type == PGL.FLOAT) {
                    vertexAttribute.add(this.fattribs.get(str), i12);
                } else if (vertexAttribute.type == PGL.INT) {
                    vertexAttribute.add(this.iattribs.get(str), i12);
                } else if (vertexAttribute.type == PGL.BOOL) {
                    vertexAttribute.add(this.battribs.get(str), i12);
                }
            }
            if (z || ((i6 == 0 && this.codes != null) || i6 == 1 || i6 == 2 || i6 == 3)) {
                if (this.codes == null) {
                    this.codes = new int[PApplet.max(PGL.DEFAULT_IN_VERTICES, this.vertexCount)];
                    Arrays.fill(this.codes, 0, this.vertexCount, 0);
                    this.codeCount = this.vertexCount;
                }
                if (z) {
                    codeCheck();
                    this.codes[this.codeCount] = 4;
                    this.codeCount++;
                }
                if (i6 != -1) {
                    codeCheck();
                    this.codes[this.codeCount] = i6;
                    this.codeCount++;
                }
            }
            this.vertexCount++;
            return this.vertexCount - 1;
        }

        /* access modifiers changed from: package-private */
        public int addVertex(float f, float f2, float f3, int i, boolean z) {
            return addVertex(f, f2, f3, this.fillColor, this.normalX, this.normalY, this.normalZ, 0.0f, 0.0f, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininessFactor, i, z);
        }

        /* access modifiers changed from: package-private */
        public int addVertex(float f, float f2, float f3, boolean z) {
            return addVertex(f, f2, f3, this.fillColor, this.normalX, this.normalY, this.normalZ, 0.0f, 0.0f, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininessFactor, 0, z);
        }

        /* access modifiers changed from: package-private */
        public int addVertex(float f, float f2, int i, boolean z) {
            return addVertex(f, f2, 0.0f, this.fillColor, this.normalX, this.normalY, this.normalZ, 0.0f, 0.0f, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininessFactor, i, z);
        }

        /* access modifiers changed from: package-private */
        public int addVertex(float f, float f2, boolean z) {
            return addVertex(f, f2, 0.0f, this.fillColor, this.normalX, this.normalY, this.normalZ, 0.0f, 0.0f, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininessFactor, 0, z);
        }

        /* access modifiers changed from: package-private */
        public void allocate() {
            this.vertices = new float[(PGL.DEFAULT_IN_VERTICES * 3)];
            this.colors = new int[PGL.DEFAULT_IN_VERTICES];
            this.normals = new float[(PGL.DEFAULT_IN_VERTICES * 3)];
            this.texcoords = new float[(PGL.DEFAULT_IN_VERTICES * 2)];
            this.strokeColors = new int[PGL.DEFAULT_IN_VERTICES];
            this.strokeWeights = new float[PGL.DEFAULT_IN_VERTICES];
            this.ambient = new int[PGL.DEFAULT_IN_VERTICES];
            this.specular = new int[PGL.DEFAULT_IN_VERTICES];
            this.emissive = new int[PGL.DEFAULT_IN_VERTICES];
            this.shininess = new float[PGL.DEFAULT_IN_VERTICES];
            this.edges = (int[][]) Array.newInstance(Integer.TYPE, new int[]{PGL.DEFAULT_IN_EDGES, 3});
            this.fattribs = new HashMap<>();
            this.iattribs = new HashMap<>();
            this.battribs = new HashMap<>();
            clear();
        }

        /* access modifiers changed from: package-private */
        public void calcQuadStripNormals() {
            for (int i = 1; i < this.vertexCount / 2; i++) {
                int i2 = ((i - 1) * 2) + 1;
                int i3 = i * 2;
                calcTriangleNormal((i - 1) * 2, i2, i3);
                calcTriangleNormal(i3, i2, (i * 2) + 1);
            }
        }

        /* access modifiers changed from: package-private */
        public void calcQuadsNormals() {
            for (int i = 0; i < this.vertexCount / 4; i++) {
                int i2 = (i * 4) + 0;
                int i3 = (i * 4) + 2;
                calcTriangleNormal(i2, (i * 4) + 1, i3);
                calcTriangleNormal(i3, (i * 4) + 3, i2);
            }
        }

        /* access modifiers changed from: package-private */
        public void calcTriangleFanNormals() {
            for (int i = 1; i < this.vertexCount - 1; i++) {
                calcTriangleNormal(0, i, i + 1);
            }
        }

        /* access modifiers changed from: package-private */
        public void calcTriangleNormal(int i, int i2, int i3) {
            int i4 = i * 3;
            int i5 = i4 + 1;
            float f = this.vertices[i4];
            float f2 = this.vertices[i5];
            float f3 = this.vertices[i5 + 1];
            int i6 = i2 * 3;
            int i7 = i6 + 1;
            float f4 = this.vertices[i6];
            float f5 = this.vertices[i7];
            float f6 = this.vertices[i7 + 1];
            int i8 = i3 * 3;
            int i9 = i8 + 1;
            float f7 = this.vertices[i8];
            float f8 = f7 - f4;
            float f9 = this.vertices[i9] - f5;
            float f10 = this.vertices[i9 + 1] - f6;
            float f11 = f - f4;
            float f12 = f2 - f5;
            float f13 = f3 - f6;
            float f14 = (f9 * f13) - (f12 * f10);
            float f15 = (f10 * f11) - (f13 * f8);
            float f16 = (f12 * f8) - (f11 * f9);
            float sqrt = PApplet.sqrt((f14 * f14) + (f15 * f15) + (f16 * f16));
            float f17 = f14 / sqrt;
            float f18 = f15 / sqrt;
            float f19 = f16 / sqrt;
            int i10 = i * 3;
            int i11 = i10 + 1;
            this.normals[i10] = f17;
            this.normals[i11] = f18;
            this.normals[i11 + 1] = f19;
            int i12 = i2 * 3;
            int i13 = i12 + 1;
            this.normals[i12] = f17;
            this.normals[i13] = f18;
            this.normals[i13 + 1] = f19;
            int i14 = i3 * 3;
            int i15 = i14 + 1;
            this.normals[i14] = f17;
            this.normals[i15] = f18;
            this.normals[i15 + 1] = f19;
        }

        /* access modifiers changed from: package-private */
        public void calcTriangleStripNormals() {
            int i;
            int i2;
            for (int i3 = 1; i3 < this.vertexCount - 1; i3++) {
                if (i3 % 2 == 1) {
                    i = i3 - 1;
                    i2 = i3 + 1;
                } else {
                    i = i3 + 1;
                    i2 = i3 - 1;
                }
                calcTriangleNormal(i, i3, i2);
            }
        }

        /* access modifiers changed from: package-private */
        public void calcTrianglesNormals() {
            for (int i = 0; i < this.vertexCount / 3; i++) {
                calcTriangleNormal((i * 3) + 0, (i * 3) + 1, (i * 3) + 2);
            }
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            this.vertexCount = 0;
            this.codeCount = 0;
            this.edgeCount = 0;
        }

        /* access modifiers changed from: package-private */
        public void clearEdges() {
            this.edgeCount = 0;
        }

        /* access modifiers changed from: package-private */
        public int closeEdge(int i, int i2) {
            edgeCheck();
            int[] iArr = this.edges[this.edgeCount];
            iArr[0] = i;
            iArr[1] = i2;
            iArr[2] = -1;
            this.edgeCount++;
            return this.edgeCount - 1;
        }

        /* access modifiers changed from: package-private */
        public void codeCheck() {
            if (this.codeCount == this.codes.length) {
                expandCodes(this.codeCount << 1);
            }
        }

        /* access modifiers changed from: package-private */
        public void edgeCheck() {
            if (this.edgeCount == this.edges.length) {
                expandEdges(this.edgeCount << 1);
            }
        }

        /* access modifiers changed from: package-private */
        public void expandAmbient(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.ambient, 0, iArr, 0, this.vertexCount);
            this.ambient = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandAttribs(int i) {
            for (String str : this.attribs.keySet()) {
                VertexAttribute vertexAttribute = (VertexAttribute) this.attribs.get(str);
                if (vertexAttribute.type == PGL.FLOAT) {
                    expandFloatAttrib(vertexAttribute, i);
                } else if (vertexAttribute.type == PGL.INT) {
                    expandIntAttrib(vertexAttribute, i);
                } else if (vertexAttribute.type == PGL.BOOL) {
                    expandBoolAttrib(vertexAttribute, i);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void expandBoolAttrib(VertexAttribute vertexAttribute, int i) {
            byte[] bArr = new byte[(vertexAttribute.size * i)];
            PApplet.arrayCopy(this.battribs.get(vertexAttribute.name), 0, bArr, 0, vertexAttribute.size * this.vertexCount);
            this.battribs.put(vertexAttribute.name, bArr);
        }

        /* access modifiers changed from: package-private */
        public void expandCodes(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.codes, 0, iArr, 0, this.codeCount);
            this.codes = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandColors(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.colors, 0, iArr, 0, this.vertexCount);
            this.colors = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandEdges(int i) {
            int[][] iArr = (int[][]) Array.newInstance(Integer.TYPE, new int[]{i, 3});
            PApplet.arrayCopy(this.edges, 0, iArr, 0, this.edgeCount);
            this.edges = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandEmissive(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.emissive, 0, iArr, 0, this.vertexCount);
            this.emissive = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandFloatAttrib(VertexAttribute vertexAttribute, int i) {
            float[] fArr = new float[(vertexAttribute.size * i)];
            PApplet.arrayCopy(this.fattribs.get(vertexAttribute.name), 0, fArr, 0, vertexAttribute.size * this.vertexCount);
            this.fattribs.put(vertexAttribute.name, fArr);
        }

        /* access modifiers changed from: package-private */
        public void expandIntAttrib(VertexAttribute vertexAttribute, int i) {
            int[] iArr = new int[(vertexAttribute.size * i)];
            PApplet.arrayCopy(this.iattribs.get(vertexAttribute.name), 0, iArr, 0, vertexAttribute.size * this.vertexCount);
            this.iattribs.put(vertexAttribute.name, iArr);
        }

        /* access modifiers changed from: package-private */
        public void expandNormals(int i) {
            float[] fArr = new float[(i * 3)];
            PApplet.arrayCopy(this.normals, 0, fArr, 0, this.vertexCount * 3);
            this.normals = fArr;
        }

        /* access modifiers changed from: package-private */
        public void expandShininess(int i) {
            float[] fArr = new float[i];
            PApplet.arrayCopy(this.shininess, 0, fArr, 0, this.vertexCount);
            this.shininess = fArr;
        }

        /* access modifiers changed from: package-private */
        public void expandSpecular(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.specular, 0, iArr, 0, this.vertexCount);
            this.specular = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandStrokeColors(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.strokeColors, 0, iArr, 0, this.vertexCount);
            this.strokeColors = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandStrokeWeights(int i) {
            float[] fArr = new float[i];
            PApplet.arrayCopy(this.strokeWeights, 0, fArr, 0, this.vertexCount);
            this.strokeWeights = fArr;
        }

        /* access modifiers changed from: package-private */
        public void expandTexCoords(int i) {
            float[] fArr = new float[(i * 2)];
            PApplet.arrayCopy(this.texcoords, 0, fArr, 0, this.vertexCount * 2);
            this.texcoords = fArr;
        }

        /* access modifiers changed from: package-private */
        public void expandVertices(int i) {
            float[] fArr = new float[(i * 3)];
            PApplet.arrayCopy(this.vertices, 0, fArr, 0, this.vertexCount * 3);
            this.vertices = fArr;
        }

        /* access modifiers changed from: package-private */
        public double[] getAttribVector(int i) {
            double[] dArr = new double[this.attribs.numComp];
            int i2 = 0;
            for (int i3 = 0; i3 < this.attribs.size(); i3++) {
                VertexAttribute vertexAttribute = this.attribs.get(i3);
                String str = vertexAttribute.name;
                int i4 = vertexAttribute.size * i;
                if (vertexAttribute.isColor()) {
                    int i5 = this.iattribs.get(str)[i4];
                    int i6 = i2 + 1;
                    dArr[i2] = (double) ((i5 >> 24) & 255);
                    int i7 = i6 + 1;
                    dArr[i6] = (double) ((i5 >> 16) & 255);
                    int i8 = i7 + 1;
                    dArr[i7] = (double) ((i5 >> 8) & 255);
                    dArr[i8] = (double) ((i5 >> 0) & 255);
                    i2 = i8 + 1;
                } else if (vertexAttribute.isFloat()) {
                    float[] fArr = this.fattribs.get(str);
                    int i9 = i2;
                    int i10 = 0;
                    while (i10 < vertexAttribute.size) {
                        dArr[i9] = (double) fArr[i4];
                        i10++;
                        i4++;
                        i9++;
                    }
                    i2 = i9;
                } else if (vertexAttribute.isInt()) {
                    int[] iArr = this.iattribs.get(str);
                    int i11 = i2;
                    int i12 = 0;
                    while (i12 < vertexAttribute.size) {
                        dArr[i11] = (double) iArr[i4];
                        i12++;
                        i4++;
                        i11++;
                    }
                    i2 = i11;
                } else if (vertexAttribute.isBool()) {
                    byte[] bArr = this.battribs.get(str);
                    int i13 = i2;
                    int i14 = 0;
                    while (i14 < vertexAttribute.size) {
                        dArr[i13] = (double) bArr[i4];
                        i14++;
                        i4++;
                        i13++;
                    }
                    i2 = i13;
                }
            }
            return dArr;
        }

        /* access modifiers changed from: package-private */
        public float getLastVertexX() {
            return this.vertices[((this.vertexCount - 1) * 3) + 0];
        }

        /* access modifiers changed from: package-private */
        public float getLastVertexY() {
            return this.vertices[((this.vertexCount - 1) * 3) + 1];
        }

        /* access modifiers changed from: package-private */
        public float getLastVertexZ() {
            return this.vertices[((this.vertexCount - 1) * 3) + 2];
        }

        /* access modifiers changed from: package-private */
        public int getNumEdgeClosures() {
            int i = 0;
            for (int i2 = 0; i2 < this.edgeCount; i2++) {
                if (this.edges[i2][2] == -1) {
                    i++;
                }
            }
            return i;
        }

        /* access modifiers changed from: package-private */
        public int getNumEdgeIndices(boolean z) {
            int i;
            int i2;
            int i3 = this.edgeCount;
            if (z) {
                i = i3;
                i2 = 0;
                for (int i4 = 0; i4 < this.edgeCount; i4++) {
                    int[] iArr = this.edges[i4];
                    if (iArr[2] == 0 || iArr[2] == 1) {
                        i2++;
                    }
                    if (iArr[2] == -1) {
                        i2++;
                        i--;
                    }
                }
            } else {
                i = i3 - getNumEdgeClosures();
                i2 = 0;
            }
            return (i + i2) * 6;
        }

        /* access modifiers changed from: package-private */
        public int getNumEdgeVertices(boolean z) {
            int i;
            int i2;
            int i3 = this.edgeCount;
            if (z) {
                i = i3;
                i2 = 0;
                for (int i4 = 0; i4 < this.edgeCount; i4++) {
                    int[] iArr = this.edges[i4];
                    if (iArr[2] == 0 || iArr[2] == 1) {
                        i2 += 3;
                    }
                    if (iArr[2] == -1) {
                        i2 += 5;
                        i--;
                    }
                }
            } else {
                i = i3 - getNumEdgeClosures();
                i2 = 0;
            }
            return (i * 4) + i2;
        }

        /* access modifiers changed from: package-private */
        public float[][] getVertexData() {
            float[][] fArr = (float[][]) Array.newInstance(Float.TYPE, new int[]{this.vertexCount, 37});
            for (int i = 0; i < this.vertexCount; i++) {
                float[] fArr2 = fArr[i];
                fArr2[0] = this.vertices[(i * 3) + 0];
                fArr2[1] = this.vertices[(i * 3) + 1];
                fArr2[2] = this.vertices[(i * 3) + 2];
                fArr2[3] = ((float) ((this.colors[i] >> 16) & 255)) / 255.0f;
                fArr2[4] = ((float) ((this.colors[i] >> 8) & 255)) / 255.0f;
                fArr2[5] = ((float) ((this.colors[i] >> 0) & 255)) / 255.0f;
                fArr2[6] = ((float) ((this.colors[i] >> 24) & 255)) / 255.0f;
                fArr2[7] = this.texcoords[(i * 2) + 0];
                fArr2[8] = this.texcoords[(i * 2) + 1];
                fArr2[9] = this.normals[(i * 3) + 0];
                fArr2[10] = this.normals[(i * 3) + 1];
                fArr2[11] = this.normals[(i * 3) + 2];
                fArr2[13] = ((float) ((this.strokeColors[i] >> 16) & 255)) / 255.0f;
                fArr2[14] = ((float) ((this.strokeColors[i] >> 8) & 255)) / 255.0f;
                fArr2[15] = ((float) ((this.strokeColors[i] >> 0) & 255)) / 255.0f;
                fArr2[16] = ((float) ((this.strokeColors[i] >> 24) & 255)) / 255.0f;
                fArr2[17] = this.strokeWeights[i];
            }
            return fArr;
        }

        /* access modifiers changed from: package-private */
        public void getVertexMax(PVector pVector) {
            for (int i = 0; i < this.vertexCount; i++) {
                int i2 = i * 4;
                int i3 = i2 + 1;
                pVector.x = PApplet.max(pVector.x, this.vertices[i2]);
                pVector.y = PApplet.max(pVector.y, this.vertices[i3]);
                pVector.z = PApplet.max(pVector.z, this.vertices[i3 + 1]);
            }
        }

        /* access modifiers changed from: package-private */
        public void getVertexMin(PVector pVector) {
            for (int i = 0; i < this.vertexCount; i++) {
                int i2 = i * 4;
                int i3 = i2 + 1;
                pVector.x = PApplet.min(pVector.x, this.vertices[i2]);
                pVector.y = PApplet.min(pVector.y, this.vertices[i3]);
                pVector.z = PApplet.min(pVector.z, this.vertices[i3 + 1]);
            }
        }

        /* access modifiers changed from: package-private */
        public int getVertexSum(PVector pVector) {
            for (int i = 0; i < this.vertexCount; i++) {
                int i2 = i * 4;
                int i3 = i2 + 1;
                pVector.x = this.vertices[i2] + pVector.x;
                pVector.y += this.vertices[i3];
                pVector.z += this.vertices[i3 + 1];
            }
            return this.vertexCount;
        }

        /* access modifiers changed from: package-private */
        public float getVertexX(int i) {
            return this.vertices[(i * 3) + 0];
        }

        /* access modifiers changed from: package-private */
        public float getVertexY(int i) {
            return this.vertices[(i * 3) + 1];
        }

        /* access modifiers changed from: package-private */
        public float getVertexZ(int i) {
            return this.vertices[(i * 3) + 2];
        }

        /* access modifiers changed from: package-private */
        public boolean hasBezierVertex() {
            for (int i = 0; i < this.codeCount; i++) {
                if (this.codes[i] == 1) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean hasCurveVertex() {
            for (int i = 0; i < this.codeCount; i++) {
                if (this.codes[i] == 3) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean hasQuadraticVertex() {
            for (int i = 0; i < this.codeCount; i++) {
                if (this.codes[i] == 2) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public void initAttrib(VertexAttribute vertexAttribute) {
            if (vertexAttribute.type == PGL.FLOAT) {
                this.fattribs.put(vertexAttribute.name, new float[(vertexAttribute.size * PGL.DEFAULT_IN_VERTICES)]);
            } else if (vertexAttribute.type == PGL.INT) {
                this.iattribs.put(vertexAttribute.name, new int[(vertexAttribute.size * PGL.DEFAULT_IN_VERTICES)]);
            } else if (vertexAttribute.type == PGL.BOOL) {
                this.battribs.put(vertexAttribute.name, new byte[(vertexAttribute.size * PGL.DEFAULT_IN_VERTICES)]);
            }
        }

        /* access modifiers changed from: package-private */
        public void setMaterial(int i, int i2, float f, int i3, int i4, int i5, float f2) {
            this.fillColor = i;
            this.strokeColor = i2;
            this.strokeWeight = f;
            this.ambientColor = i3;
            this.specularColor = i4;
            this.emissiveColor = i5;
            this.shininessFactor = f2;
        }

        /* access modifiers changed from: package-private */
        public void setNormal(float f, float f2, float f3) {
            this.normalX = f;
            this.normalY = f2;
            this.normalZ = f3;
        }

        /* access modifiers changed from: package-private */
        public void trim() {
            if (this.vertexCount > 0 && this.vertexCount < this.vertices.length / 3) {
                trimVertices();
                trimColors();
                trimNormals();
                trimTexCoords();
                trimStrokeColors();
                trimStrokeWeights();
                trimAmbient();
                trimSpecular();
                trimEmissive();
                trimShininess();
                trimAttribs();
            }
            if (this.codeCount > 0 && this.codeCount < this.codes.length) {
                trimCodes();
            }
            if (this.edgeCount > 0 && this.edgeCount < this.edges.length) {
                trimEdges();
            }
        }

        /* access modifiers changed from: package-private */
        public void trimAmbient() {
            int[] iArr = new int[this.vertexCount];
            PApplet.arrayCopy(this.ambient, 0, iArr, 0, this.vertexCount);
            this.ambient = iArr;
        }

        /* access modifiers changed from: package-private */
        public void trimAttribs() {
            for (String str : this.attribs.keySet()) {
                VertexAttribute vertexAttribute = (VertexAttribute) this.attribs.get(str);
                if (vertexAttribute.type == PGL.FLOAT) {
                    trimFloatAttrib(vertexAttribute);
                } else if (vertexAttribute.type == PGL.INT) {
                    trimIntAttrib(vertexAttribute);
                } else if (vertexAttribute.type == PGL.BOOL) {
                    trimBoolAttrib(vertexAttribute);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void trimBoolAttrib(VertexAttribute vertexAttribute) {
            byte[] bArr = new byte[(vertexAttribute.size * this.vertexCount)];
            PApplet.arrayCopy(this.battribs.get(vertexAttribute.name), 0, bArr, 0, vertexAttribute.size * this.vertexCount);
            this.battribs.put(vertexAttribute.name, bArr);
        }

        /* access modifiers changed from: package-private */
        public void trimCodes() {
            int[] iArr = new int[this.codeCount];
            PApplet.arrayCopy(this.codes, 0, iArr, 0, this.codeCount);
            this.codes = iArr;
        }

        /* access modifiers changed from: package-private */
        public void trimColors() {
            int[] iArr = new int[this.vertexCount];
            PApplet.arrayCopy(this.colors, 0, iArr, 0, this.vertexCount);
            this.colors = iArr;
        }

        /* access modifiers changed from: package-private */
        public void trimEdges() {
            int[][] iArr = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.edgeCount, 3});
            PApplet.arrayCopy(this.edges, 0, iArr, 0, this.edgeCount);
            this.edges = iArr;
        }

        /* access modifiers changed from: package-private */
        public void trimEmissive() {
            int[] iArr = new int[this.vertexCount];
            PApplet.arrayCopy(this.emissive, 0, iArr, 0, this.vertexCount);
            this.emissive = iArr;
        }

        /* access modifiers changed from: package-private */
        public void trimFloatAttrib(VertexAttribute vertexAttribute) {
            float[] fArr = new float[(vertexAttribute.size * this.vertexCount)];
            PApplet.arrayCopy(this.fattribs.get(vertexAttribute.name), 0, fArr, 0, vertexAttribute.size * this.vertexCount);
            this.fattribs.put(vertexAttribute.name, fArr);
        }

        /* access modifiers changed from: package-private */
        public void trimIntAttrib(VertexAttribute vertexAttribute) {
            int[] iArr = new int[(vertexAttribute.size * this.vertexCount)];
            PApplet.arrayCopy(this.iattribs.get(vertexAttribute.name), 0, iArr, 0, vertexAttribute.size * this.vertexCount);
            this.iattribs.put(vertexAttribute.name, iArr);
        }

        /* access modifiers changed from: package-private */
        public void trimNormals() {
            float[] fArr = new float[(this.vertexCount * 3)];
            PApplet.arrayCopy(this.normals, 0, fArr, 0, this.vertexCount * 3);
            this.normals = fArr;
        }

        /* access modifiers changed from: package-private */
        public void trimShininess() {
            float[] fArr = new float[this.vertexCount];
            PApplet.arrayCopy(this.shininess, 0, fArr, 0, this.vertexCount);
            this.shininess = fArr;
        }

        /* access modifiers changed from: package-private */
        public void trimSpecular() {
            int[] iArr = new int[this.vertexCount];
            PApplet.arrayCopy(this.specular, 0, iArr, 0, this.vertexCount);
            this.specular = iArr;
        }

        /* access modifiers changed from: package-private */
        public void trimStrokeColors() {
            int[] iArr = new int[this.vertexCount];
            PApplet.arrayCopy(this.strokeColors, 0, iArr, 0, this.vertexCount);
            this.strokeColors = iArr;
        }

        /* access modifiers changed from: package-private */
        public void trimStrokeWeights() {
            float[] fArr = new float[this.vertexCount];
            PApplet.arrayCopy(this.strokeWeights, 0, fArr, 0, this.vertexCount);
            this.strokeWeights = fArr;
        }

        /* access modifiers changed from: package-private */
        public void trimTexCoords() {
            float[] fArr = new float[(this.vertexCount * 2)];
            PApplet.arrayCopy(this.texcoords, 0, fArr, 0, this.vertexCount * 2);
            this.texcoords = fArr;
        }

        /* access modifiers changed from: package-private */
        public void trimVertices() {
            float[] fArr = new float[(this.vertexCount * 3)];
            PApplet.arrayCopy(this.vertices, 0, fArr, 0, this.vertexCount * 3);
            this.vertices = fArr;
        }

        /* access modifiers changed from: package-private */
        public void vertexCheck() {
            if (this.vertexCount == this.vertices.length / 3) {
                int i = this.vertexCount << 1;
                expandVertices(i);
                expandColors(i);
                expandNormals(i);
                expandTexCoords(i);
                expandStrokeColors(i);
                expandStrokeWeights(i);
                expandAmbient(i);
                expandSpecular(i);
                expandEmissive(i);
                expandShininess(i);
                expandAttribs(i);
            }
        }
    }

    protected static class IndexCache {
        int[] counter;
        int[] indexCount;
        int[] indexOffset;
        int size;
        int[] vertexCount;
        int[] vertexOffset;

        IndexCache() {
            allocate();
        }

        /* access modifiers changed from: package-private */
        public int addNew() {
            arrayCheck();
            init(this.size);
            this.size++;
            return this.size - 1;
        }

        /* access modifiers changed from: package-private */
        public int addNew(int i) {
            arrayCheck();
            this.indexCount[this.size] = this.indexCount[i];
            this.indexOffset[this.size] = this.indexOffset[i];
            this.vertexCount[this.size] = this.vertexCount[i];
            this.vertexOffset[this.size] = this.vertexOffset[i];
            this.size++;
            return this.size - 1;
        }

        /* access modifiers changed from: package-private */
        public void allocate() {
            this.size = 0;
            this.indexCount = new int[2];
            this.indexOffset = new int[2];
            this.vertexCount = new int[2];
            this.vertexOffset = new int[2];
            this.counter = null;
        }

        /* access modifiers changed from: package-private */
        public void arrayCheck() {
            if (this.size == this.indexCount.length) {
                int i = this.size << 1;
                expandIndexCount(i);
                expandIndexOffset(i);
                expandVertexCount(i);
                expandVertexOffset(i);
            }
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            this.size = 0;
        }

        /* access modifiers changed from: package-private */
        public void expandIndexCount(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.indexCount, 0, iArr, 0, this.size);
            this.indexCount = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandIndexOffset(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.indexOffset, 0, iArr, 0, this.size);
            this.indexOffset = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandVertexCount(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.vertexCount, 0, iArr, 0, this.size);
            this.vertexCount = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandVertexOffset(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.vertexOffset, 0, iArr, 0, this.size);
            this.vertexOffset = iArr;
        }

        /* access modifiers changed from: package-private */
        public int getLast() {
            if (this.size == 0) {
                arrayCheck();
                init(0);
                this.size = 1;
            }
            return this.size - 1;
        }

        /* access modifiers changed from: package-private */
        public void incCounts(int i, int i2, int i3) {
            int[] iArr = this.indexCount;
            iArr[i] = iArr[i] + i2;
            int[] iArr2 = this.vertexCount;
            iArr2[i] = iArr2[i] + i3;
            if (this.counter != null) {
                int[] iArr3 = this.counter;
                iArr3[0] = iArr3[0] + i2;
                int[] iArr4 = this.counter;
                iArr4[1] = iArr4[1] + i3;
            }
        }

        /* access modifiers changed from: package-private */
        public void init(int i) {
            if (i > 0) {
                this.indexOffset[i] = this.indexOffset[i - 1] + this.indexCount[i - 1];
                this.vertexOffset[i] = this.vertexOffset[i - 1] + this.vertexCount[i - 1];
            } else {
                this.indexOffset[i] = 0;
                this.vertexOffset[i] = 0;
            }
            this.indexCount[i] = 0;
            this.vertexCount[i] = 0;
        }

        /* access modifiers changed from: package-private */
        public void setCounter(int[] iArr) {
            this.counter = iArr;
        }
    }

    protected static class TessGeometry {
        HashMap<String, byte[]> bpolyAttribs = new HashMap<>();
        int firstLineIndex;
        int firstLineVertex;
        int firstPointIndex;
        int firstPointVertex;
        int firstPolyIndex;
        int firstPolyVertex;
        HashMap<String, float[]> fpolyAttribs = new HashMap<>();
        HashMap<String, int[]> ipolyAttribs = new HashMap<>();
        int lastLineIndex;
        int lastLineVertex;
        int lastPointIndex;
        int lastPointVertex;
        int lastPolyIndex;
        int lastPolyVertex;
        int[] lineColors;
        IntBuffer lineColorsBuffer;
        float[] lineDirections;
        FloatBuffer lineDirectionsBuffer;
        IndexCache lineIndexCache = new IndexCache();
        int lineIndexCount;
        short[] lineIndices;
        ShortBuffer lineIndicesBuffer;
        int lineVertexCount;
        float[] lineVertices;
        FloatBuffer lineVerticesBuffer;
        PGraphicsOpenGL pg;
        int[] pointColors;
        IntBuffer pointColorsBuffer;
        IndexCache pointIndexCache = new IndexCache();
        int pointIndexCount;
        short[] pointIndices;
        ShortBuffer pointIndicesBuffer;
        float[] pointOffsets;
        FloatBuffer pointOffsetsBuffer;
        int pointVertexCount;
        float[] pointVertices;
        FloatBuffer pointVerticesBuffer;
        int[] polyAmbient;
        IntBuffer polyAmbientBuffer;
        HashMap<String, Buffer> polyAttribBuffers = new HashMap<>();
        AttributeMap polyAttribs;
        int[] polyColors;
        IntBuffer polyColorsBuffer;
        int[] polyEmissive;
        IntBuffer polyEmissiveBuffer;
        IndexCache polyIndexCache = new IndexCache();
        int polyIndexCount;
        short[] polyIndices;
        ShortBuffer polyIndicesBuffer;
        float[] polyNormals;
        FloatBuffer polyNormalsBuffer;
        float[] polyShininess;
        FloatBuffer polyShininessBuffer;
        int[] polySpecular;
        IntBuffer polySpecularBuffer;
        float[] polyTexCoords;
        FloatBuffer polyTexCoordsBuffer;
        int polyVertexCount;
        float[] polyVertices;
        FloatBuffer polyVerticesBuffer;
        int renderMode;

        TessGeometry(PGraphicsOpenGL pGraphicsOpenGL, AttributeMap attributeMap, int i) {
            this.pg = pGraphicsOpenGL;
            this.polyAttribs = attributeMap;
            this.renderMode = i;
            allocate();
        }

        private void copyFewAttribs(InGeometry inGeometry, int i, int i2, int i3) {
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 < i3) {
                    int i6 = i + i5;
                    int i7 = this.firstPolyVertex + i5;
                    int i8 = i6 * 2;
                    int i9 = i8 + 1;
                    float f = inGeometry.texcoords[i8];
                    float f2 = inGeometry.texcoords[i9];
                    this.polyColors[i7] = inGeometry.colors[i6];
                    int i10 = i7 * 2;
                    this.polyTexCoords[i10] = f;
                    this.polyTexCoords[i10 + 1] = f2;
                    this.polyAmbient[i7] = inGeometry.ambient[i6];
                    this.polySpecular[i7] = inGeometry.specular[i6];
                    this.polyEmissive[i7] = inGeometry.emissive[i6];
                    this.polyShininess[i7] = inGeometry.shininess[i6];
                    for (String str : this.polyAttribs.keySet()) {
                        VertexAttribute vertexAttribute = (VertexAttribute) this.polyAttribs.get(str);
                        if (!vertexAttribute.isPosition() && !vertexAttribute.isNormal()) {
                            int i11 = vertexAttribute.size * i6;
                            int i12 = vertexAttribute.size * i7;
                            if (vertexAttribute.isFloat()) {
                                float[] fArr = inGeometry.fattribs.get(str);
                                float[] fArr2 = this.fpolyAttribs.get(str);
                                int i13 = 0;
                                int i14 = i11;
                                while (i13 < vertexAttribute.size) {
                                    fArr2[i12] = fArr[i14];
                                    i13++;
                                    i12++;
                                    i14++;
                                }
                            } else if (vertexAttribute.isInt()) {
                                int[] iArr = inGeometry.iattribs.get(str);
                                int[] iArr2 = this.ipolyAttribs.get(str);
                                int i15 = 0;
                                int i16 = i11;
                                while (i15 < vertexAttribute.size) {
                                    iArr2[i12] = iArr[i16];
                                    i15++;
                                    i12++;
                                    i16++;
                                }
                            } else if (vertexAttribute.isBool()) {
                                byte[] bArr = inGeometry.battribs.get(str);
                                byte[] bArr2 = this.bpolyAttribs.get(str);
                                int i17 = 0;
                                int i18 = i11;
                                while (i17 < vertexAttribute.size) {
                                    bArr2[i12] = bArr[i18];
                                    i17++;
                                    i12++;
                                    i18++;
                                }
                            }
                        }
                    }
                    i4 = i5 + 1;
                } else {
                    return;
                }
            }
        }

        private void copyFewCoords(InGeometry inGeometry, int i, int i2, int i3) {
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 < i3) {
                    int i6 = i + i5;
                    int i7 = this.firstPolyVertex + i5;
                    int i8 = i6 * 3;
                    int i9 = i8 + 1;
                    float f = inGeometry.vertices[i8];
                    float f2 = inGeometry.vertices[i9];
                    float f3 = inGeometry.vertices[i9 + 1];
                    int i10 = i6 * 3;
                    int i11 = i10 + 1;
                    float f4 = inGeometry.normals[i10];
                    float f5 = inGeometry.normals[i11];
                    float f6 = inGeometry.normals[i11 + 1];
                    int i12 = i7 * 4;
                    int i13 = i12 + 1;
                    this.polyVertices[i12] = f;
                    int i14 = i13 + 1;
                    this.polyVertices[i13] = f2;
                    this.polyVertices[i14] = f3;
                    this.polyVertices[i14 + 1] = 1.0f;
                    int i15 = i7 * 3;
                    int i16 = i15 + 1;
                    this.polyNormals[i15] = f4;
                    this.polyNormals[i16] = f5;
                    this.polyNormals[i16 + 1] = f6;
                    for (String str : this.polyAttribs.keySet()) {
                        VertexAttribute vertexAttribute = (VertexAttribute) this.polyAttribs.get(str);
                        if (!vertexAttribute.isColor() && !vertexAttribute.isOther()) {
                            float[] fArr = inGeometry.fattribs.get(str);
                            int i17 = i6 * 3;
                            int i18 = i17 + 1;
                            float f7 = fArr[i17];
                            int i19 = i18 + 1;
                            float f8 = fArr[i18];
                            float f9 = fArr[i19];
                            float[] fArr2 = this.fpolyAttribs.get(str);
                            if (vertexAttribute.isPosition()) {
                                int i20 = i7 * 4;
                                int i21 = i20 + 1;
                                fArr2[i20] = f7;
                                int i22 = i21 + 1;
                                fArr2[i21] = f8;
                                fArr2[i22] = f9;
                                fArr2[i22 + 1] = 1.0f;
                            } else {
                                int i23 = i7 * 3;
                                int i24 = i23 + 1;
                                fArr2[i23] = f7;
                                fArr2[i24] = f8;
                                fArr2[i24 + 1] = f9;
                            }
                        }
                    }
                    i4 = i5 + 1;
                } else {
                    return;
                }
            }
        }

        private void copyManyAttribs(InGeometry inGeometry, int i, int i2, int i3) {
            Object obj;
            Object obj2;
            PApplet.arrayCopy(inGeometry.colors, i, this.polyColors, this.firstPolyVertex, i3);
            PApplet.arrayCopy(inGeometry.texcoords, i * 2, this.polyTexCoords, this.firstPolyVertex * 2, i3 * 2);
            PApplet.arrayCopy(inGeometry.ambient, i, this.polyAmbient, this.firstPolyVertex, i3);
            PApplet.arrayCopy(inGeometry.specular, i, this.polySpecular, this.firstPolyVertex, i3);
            PApplet.arrayCopy(inGeometry.emissive, i, this.polyEmissive, this.firstPolyVertex, i3);
            PApplet.arrayCopy(inGeometry.shininess, i, this.polyShininess, this.firstPolyVertex, i3);
            for (String str : this.polyAttribs.keySet()) {
                VertexAttribute vertexAttribute = (VertexAttribute) this.polyAttribs.get(str);
                if (!vertexAttribute.isPosition() && !vertexAttribute.isNormal()) {
                    if (vertexAttribute.isFloat()) {
                        obj2 = inGeometry.fattribs.get(str);
                        obj = this.fpolyAttribs.get(str);
                    } else if (vertexAttribute.isInt()) {
                        obj2 = inGeometry.iattribs.get(str);
                        obj = this.ipolyAttribs.get(str);
                    } else if (vertexAttribute.isBool()) {
                        obj2 = inGeometry.battribs.get(str);
                        obj = this.bpolyAttribs.get(str);
                    } else {
                        obj = null;
                        obj2 = null;
                    }
                    PApplet.arrayCopy(obj2, vertexAttribute.size * i, obj, vertexAttribute.tessSize * this.firstPolyVertex, vertexAttribute.size * i3);
                }
            }
        }

        private void copyManyCoords(InGeometry inGeometry, int i, int i2, int i3) {
            for (int i4 = 0; i4 < i3; i4++) {
                int i5 = i + i4;
                int i6 = this.firstPolyVertex + i4;
                PApplet.arrayCopy(inGeometry.vertices, i5 * 3, this.polyVertices, i6 * 4, 3);
                this.polyVertices[(i6 * 4) + 3] = 1.0f;
                for (String str : this.polyAttribs.keySet()) {
                    if (((VertexAttribute) this.polyAttribs.get(str)).isPosition()) {
                        float[] fArr = this.fpolyAttribs.get(str);
                        PApplet.arrayCopy(inGeometry.fattribs.get(str), i5 * 3, fArr, i6 * 4, 3);
                        fArr[(i6 * 4) + 3] = 1.0f;
                    }
                }
            }
            PApplet.arrayCopy(inGeometry.normals, i * 3, this.polyNormals, this.firstPolyVertex * 3, i3 * 3);
            for (String str2 : this.polyAttribs.keySet()) {
                if (((VertexAttribute) this.polyAttribs.get(str2)).isNormal()) {
                    PApplet.arrayCopy(inGeometry.fattribs.get(str2), i * 3, this.fpolyAttribs.get(str2), this.firstPolyVertex * 3, i3 * 3);
                }
            }
        }

        private void modelviewCoords(InGeometry inGeometry, int i, int i2, int i3, boolean z) {
            int i4;
            int i5;
            PMatrix3D pMatrix3D = this.pg.modelview;
            PMatrix3D pMatrix3D2 = this.pg.modelviewInv;
            int i6 = 0;
            while (true) {
                int i7 = i6;
                if (i7 < i3) {
                    int i8 = i + i7;
                    int i9 = this.firstPolyVertex + i7;
                    int i10 = i8 * 3;
                    int i11 = i10 + 1;
                    float f = inGeometry.vertices[i10];
                    int i12 = i11 + 1;
                    float f2 = inGeometry.vertices[i11];
                    float f3 = inGeometry.vertices[i12];
                    int i13 = i8 * 3;
                    int i14 = i13 + 1;
                    float f4 = inGeometry.normals[i13];
                    int i15 = i14 + 1;
                    float f5 = inGeometry.normals[i14];
                    float f6 = inGeometry.normals[i15];
                    int i16 = i9 * 4;
                    if (z) {
                        int i17 = i16 + 1;
                        this.polyVertices[i16] = (float) PApplet.ceil((pMatrix3D.m00 * f) + (pMatrix3D.m01 * f2) + (pMatrix3D.m02 * f3) + pMatrix3D.m03);
                        i4 = i17 + 1;
                        this.polyVertices[i17] = (float) PApplet.ceil((pMatrix3D.m10 * f) + (pMatrix3D.m11 * f2) + (pMatrix3D.m12 * f3) + pMatrix3D.m13);
                    } else {
                        int i18 = i16 + 1;
                        this.polyVertices[i16] = (pMatrix3D.m00 * f) + (pMatrix3D.m01 * f2) + (pMatrix3D.m02 * f3) + pMatrix3D.m03;
                        i4 = i18 + 1;
                        this.polyVertices[i18] = (pMatrix3D.m10 * f) + (pMatrix3D.m11 * f2) + (pMatrix3D.m12 * f3) + pMatrix3D.m13;
                    }
                    this.polyVertices[i4] = (pMatrix3D.m20 * f) + (pMatrix3D.m21 * f2) + (pMatrix3D.m22 * f3) + pMatrix3D.m23;
                    this.polyVertices[i4 + 1] = (f * pMatrix3D.m30) + (f2 * pMatrix3D.m31) + (pMatrix3D.m32 * f3) + pMatrix3D.m33;
                    int i19 = i9 * 3;
                    int i20 = i19 + 1;
                    this.polyNormals[i19] = (pMatrix3D2.m00 * f4) + (pMatrix3D2.m10 * f5) + (pMatrix3D2.m20 * f6);
                    this.polyNormals[i20] = (pMatrix3D2.m01 * f4) + (pMatrix3D2.m11 * f5) + (pMatrix3D2.m21 * f6);
                    this.polyNormals[i20 + 1] = (pMatrix3D2.m02 * f4) + (pMatrix3D2.m12 * f5) + (pMatrix3D2.m22 * f6);
                    for (String str : this.polyAttribs.keySet()) {
                        VertexAttribute vertexAttribute = (VertexAttribute) this.polyAttribs.get(str);
                        if (!vertexAttribute.isColor() && !vertexAttribute.isOther()) {
                            float[] fArr = inGeometry.fattribs.get(str);
                            int i21 = i8 * 3;
                            int i22 = i21 + 1;
                            float f7 = fArr[i21];
                            int i23 = i22 + 1;
                            float f8 = fArr[i22];
                            float f9 = fArr[i23];
                            float[] fArr2 = this.fpolyAttribs.get(str);
                            if (vertexAttribute.isPosition()) {
                                int i24 = i9 * 4;
                                if (z) {
                                    int i25 = i24 + 1;
                                    fArr2[i24] = (float) PApplet.ceil((pMatrix3D.m00 * f7) + (pMatrix3D.m01 * f8) + (pMatrix3D.m02 * f9) + pMatrix3D.m03);
                                    i5 = i25 + 1;
                                    fArr2[i25] = (float) PApplet.ceil((pMatrix3D.m10 * f7) + (pMatrix3D.m11 * f8) + (pMatrix3D.m12 * f9) + pMatrix3D.m13);
                                } else {
                                    int i26 = i24 + 1;
                                    fArr2[i24] = (pMatrix3D.m00 * f7) + (pMatrix3D.m01 * f8) + (pMatrix3D.m02 * f9) + pMatrix3D.m03;
                                    i5 = i26 + 1;
                                    fArr2[i26] = (pMatrix3D.m10 * f7) + (pMatrix3D.m11 * f8) + (pMatrix3D.m12 * f9) + pMatrix3D.m13;
                                }
                                fArr2[i5] = (pMatrix3D.m20 * f7) + (pMatrix3D.m21 * f8) + (pMatrix3D.m22 * f9) + pMatrix3D.m23;
                                fArr2[i5 + 1] = (pMatrix3D.m30 * f7) + (pMatrix3D.m31 * f8) + (f9 * pMatrix3D.m32) + pMatrix3D.m33;
                            } else {
                                int i27 = i9 * 3;
                                int i28 = i27 + 1;
                                fArr2[i27] = (pMatrix3D2.m00 * f7) + (pMatrix3D2.m10 * f8) + (pMatrix3D2.m20 * f9);
                                fArr2[i28] = (pMatrix3D2.m01 * f7) + (pMatrix3D2.m11 * f8) + (pMatrix3D2.m21 * f9);
                                fArr2[i28 + 1] = (f9 * pMatrix3D2.m22) + (f7 * pMatrix3D2.m02) + (f8 * pMatrix3D2.m12);
                            }
                        }
                    }
                    i6 = i7 + 1;
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void addPolyVertex(float f, float f2, float f3, int i, float f4, float f5, float f6, float f7, float f8, int i2, int i3, int i4, float f9, boolean z) {
            polyVertexCheck();
            setPolyVertex(this.polyVertexCount - 1, f, f2, f3, i, f4, f5, f6, f7, f8, i2, i3, i4, f9, z);
        }

        /* access modifiers changed from: package-private */
        public void addPolyVertex(InGeometry inGeometry, int i, boolean z) {
            addPolyVertices(inGeometry, i, i, z);
        }

        /* access modifiers changed from: package-private */
        public void addPolyVertex(double[] dArr, boolean z) {
            int i;
            int i2;
            addPolyVertex((float) dArr[0], (float) dArr[1], (float) dArr[2], (((int) dArr[3]) << 24) | (((int) dArr[4]) << 16) | (((int) dArr[5]) << 8) | ((int) dArr[6]), (float) dArr[7], (float) dArr[8], (float) dArr[9], (float) dArr[10], (float) dArr[11], (((int) dArr[12]) << 24) | (((int) dArr[13]) << 16) | (((int) dArr[14]) << 8) | ((int) dArr[15]), (((int) dArr[16]) << 24) | (((int) dArr[17]) << 16) | (((int) dArr[18]) << 8) | ((int) dArr[19]), (((int) dArr[20]) << 24) | (((int) dArr[21]) << 16) | (((int) dArr[22]) << 8) | ((int) dArr[23]), (float) dArr[24], z);
            if (25 < dArr.length) {
                PMatrix3D pMatrix3D = this.pg.modelview;
                PMatrix3D pMatrix3D2 = this.pg.modelviewInv;
                int i3 = this.polyVertexCount - 1;
                int i4 = 0;
                int i5 = 25;
                while (true) {
                    int i6 = i4;
                    if (i6 < this.polyAttribs.size()) {
                        VertexAttribute vertexAttribute = this.polyAttribs.get(i6);
                        String str = vertexAttribute.name;
                        int i7 = vertexAttribute.tessSize * i3;
                        if (vertexAttribute.isColor()) {
                            this.ipolyAttribs.get(str)[i7] = (((int) dArr[i5 + 0]) << 24) | (((int) dArr[i5 + 1]) << 16) | (((int) dArr[i5 + 2]) << 8) | ((int) dArr[i5 + 3]);
                            i5 += 4;
                        } else if (vertexAttribute.isPosition()) {
                            float[] fArr = this.fpolyAttribs.get(str);
                            int i8 = i5 + 1;
                            float f = (float) dArr[i5];
                            int i9 = i8 + 1;
                            float f2 = (float) dArr[i8];
                            int i10 = i9 + 1;
                            float f3 = (float) dArr[i9];
                            if (this.renderMode == 0 && this.pg.flushMode == 1) {
                                if (z) {
                                    int i11 = i7 + 1;
                                    fArr[i7] = (float) PApplet.ceil((pMatrix3D.m00 * f) + (pMatrix3D.m01 * f2) + (pMatrix3D.m02 * f3) + pMatrix3D.m03);
                                    i2 = i11 + 1;
                                    fArr[i11] = (float) PApplet.ceil((pMatrix3D.m10 * f) + (pMatrix3D.m11 * f2) + (pMatrix3D.m12 * f3) + pMatrix3D.m13);
                                } else {
                                    int i12 = i7 + 1;
                                    fArr[i7] = (pMatrix3D.m00 * f) + (pMatrix3D.m01 * f2) + (pMatrix3D.m02 * f3) + pMatrix3D.m03;
                                    i2 = i12 + 1;
                                    fArr[i12] = (pMatrix3D.m10 * f) + (pMatrix3D.m11 * f2) + (pMatrix3D.m12 * f3) + pMatrix3D.m13;
                                }
                                fArr[i2] = (pMatrix3D.m20 * f) + (pMatrix3D.m21 * f2) + (pMatrix3D.m22 * f3) + pMatrix3D.m23;
                                fArr[i2 + 1] = (pMatrix3D.m30 * f) + (pMatrix3D.m31 * f2) + (pMatrix3D.m32 * f3) + pMatrix3D.m33;
                            } else {
                                int i13 = i7 + 1;
                                fArr[i7] = f;
                                int i14 = i13 + 1;
                                fArr[i13] = f2;
                                fArr[i14] = f3;
                                fArr[i14 + 1] = 1.0f;
                            }
                            i5 = i10;
                        } else if (vertexAttribute.isNormal()) {
                            float[] fArr2 = this.fpolyAttribs.get(str);
                            float f4 = (float) dArr[i5 + 0];
                            float f5 = (float) dArr[i5 + 1];
                            float f6 = (float) dArr[i5 + 2];
                            if (this.renderMode == 0 && this.pg.flushMode == 1) {
                                int i15 = i7 + 1;
                                fArr2[i7] = (pMatrix3D2.m00 * f4) + (pMatrix3D2.m10 * f5) + (pMatrix3D2.m20 * f6);
                                fArr2[i15] = (pMatrix3D2.m01 * f4) + (pMatrix3D2.m11 * f5) + (pMatrix3D2.m21 * f6);
                                fArr2[i15 + 1] = (f4 * pMatrix3D2.m02) + (f5 * pMatrix3D2.m12) + (pMatrix3D2.m22 * f6);
                            } else {
                                int i16 = i7 + 1;
                                fArr2[i7] = f4;
                                fArr2[i16] = f5;
                                fArr2[i16 + 1] = f6;
                            }
                            i5 += 3;
                        } else {
                            if (vertexAttribute.isFloat()) {
                                float[] fArr3 = this.fpolyAttribs.get(str);
                                int i17 = i7;
                                int i18 = i5;
                                int i19 = 0;
                                i = i18;
                                while (i19 < vertexAttribute.size) {
                                    fArr3[i17] = (float) dArr[i];
                                    i19++;
                                    i++;
                                    i17++;
                                }
                            } else if (vertexAttribute.isInt()) {
                                int[] iArr = this.ipolyAttribs.get(str);
                                int i20 = i7;
                                int i21 = i5;
                                int i22 = 0;
                                int i23 = i21;
                                while (i22 < vertexAttribute.size) {
                                    iArr[i20] = (int) dArr[i];
                                    i22++;
                                    i23 = i + 1;
                                    i20++;
                                }
                            } else if (vertexAttribute.isBool()) {
                                byte[] bArr = this.bpolyAttribs.get(str);
                                int i24 = i7;
                                int i25 = i5;
                                int i26 = 0;
                                int i27 = i25;
                                while (i26 < vertexAttribute.size) {
                                    bArr[i24] = (byte) ((int) dArr[i]);
                                    i26++;
                                    i27 = i + 1;
                                    i24++;
                                }
                            } else {
                                i = i5;
                            }
                            i5 = i + vertexAttribute.size;
                        }
                        i4 = i6 + 1;
                    } else {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void addPolyVertices(InGeometry inGeometry, int i, int i2, boolean z) {
            int i3 = (i2 - i) + 1;
            polyVertexCheck(i3);
            if (this.renderMode == 0 && this.pg.flushMode == 1) {
                modelviewCoords(inGeometry, i, 0, i3, z);
            } else if (i3 <= PGL.MIN_ARRAYCOPY_SIZE) {
                copyFewCoords(inGeometry, i, 0, i3);
            } else {
                copyManyCoords(inGeometry, i, 0, i3);
            }
            if (i3 <= PGL.MIN_ARRAYCOPY_SIZE) {
                copyFewAttribs(inGeometry, i, 0, i3);
            } else {
                copyManyAttribs(inGeometry, i, 0, i3);
            }
        }

        /* access modifiers changed from: package-private */
        public void addPolyVertices(InGeometry inGeometry, boolean z) {
            addPolyVertices(inGeometry, 0, inGeometry.vertexCount - 1, z);
        }

        /* access modifiers changed from: package-private */
        public void allocate() {
            this.polyVertices = new float[(PGL.DEFAULT_TESS_VERTICES * 4)];
            this.polyColors = new int[PGL.DEFAULT_TESS_VERTICES];
            this.polyNormals = new float[(PGL.DEFAULT_TESS_VERTICES * 3)];
            this.polyTexCoords = new float[(PGL.DEFAULT_TESS_VERTICES * 2)];
            this.polyAmbient = new int[PGL.DEFAULT_TESS_VERTICES];
            this.polySpecular = new int[PGL.DEFAULT_TESS_VERTICES];
            this.polyEmissive = new int[PGL.DEFAULT_TESS_VERTICES];
            this.polyShininess = new float[PGL.DEFAULT_TESS_VERTICES];
            this.polyIndices = new short[PGL.DEFAULT_TESS_VERTICES];
            this.lineVertices = new float[(PGL.DEFAULT_TESS_VERTICES * 4)];
            this.lineColors = new int[PGL.DEFAULT_TESS_VERTICES];
            this.lineDirections = new float[(PGL.DEFAULT_TESS_VERTICES * 4)];
            this.lineIndices = new short[PGL.DEFAULT_TESS_VERTICES];
            this.pointVertices = new float[(PGL.DEFAULT_TESS_VERTICES * 4)];
            this.pointColors = new int[PGL.DEFAULT_TESS_VERTICES];
            this.pointOffsets = new float[(PGL.DEFAULT_TESS_VERTICES * 2)];
            this.pointIndices = new short[PGL.DEFAULT_TESS_VERTICES];
            this.polyVerticesBuffer = PGL.allocateFloatBuffer(this.polyVertices);
            this.polyColorsBuffer = PGL.allocateIntBuffer(this.polyColors);
            this.polyNormalsBuffer = PGL.allocateFloatBuffer(this.polyNormals);
            this.polyTexCoordsBuffer = PGL.allocateFloatBuffer(this.polyTexCoords);
            this.polyAmbientBuffer = PGL.allocateIntBuffer(this.polyAmbient);
            this.polySpecularBuffer = PGL.allocateIntBuffer(this.polySpecular);
            this.polyEmissiveBuffer = PGL.allocateIntBuffer(this.polyEmissive);
            this.polyShininessBuffer = PGL.allocateFloatBuffer(this.polyShininess);
            this.polyIndicesBuffer = PGL.allocateShortBuffer(this.polyIndices);
            this.lineVerticesBuffer = PGL.allocateFloatBuffer(this.lineVertices);
            this.lineColorsBuffer = PGL.allocateIntBuffer(this.lineColors);
            this.lineDirectionsBuffer = PGL.allocateFloatBuffer(this.lineDirections);
            this.lineIndicesBuffer = PGL.allocateShortBuffer(this.lineIndices);
            this.pointVerticesBuffer = PGL.allocateFloatBuffer(this.pointVertices);
            this.pointColorsBuffer = PGL.allocateIntBuffer(this.pointColors);
            this.pointOffsetsBuffer = PGL.allocateFloatBuffer(this.pointOffsets);
            this.pointIndicesBuffer = PGL.allocateShortBuffer(this.pointIndices);
            clear();
        }

        /* access modifiers changed from: package-private */
        public void applyMatrixOnLineGeometry(PMatrix2D pMatrix2D, int i, int i2) {
            if (i < i2) {
                float matrixScale = PGraphicsOpenGL.matrixScale(pMatrix2D);
                while (i <= i2) {
                    int i3 = i * 4;
                    int i4 = i3 + 1;
                    float f = this.lineVertices[i3];
                    float f2 = this.lineVertices[i4];
                    int i5 = i * 4;
                    int i6 = i5 + 1;
                    float f3 = this.lineDirections[i5];
                    float f4 = this.lineDirections[i6];
                    int i7 = i * 4;
                    this.lineVertices[i7] = (pMatrix2D.m00 * f) + (pMatrix2D.m01 * f2) + pMatrix2D.m02;
                    this.lineVertices[i7 + 1] = (f * pMatrix2D.m10) + (f2 * pMatrix2D.m11) + pMatrix2D.m12;
                    int i8 = i * 4;
                    int i9 = i8 + 1;
                    this.lineDirections[i8] = (pMatrix2D.m00 * f3) + (pMatrix2D.m01 * f4);
                    this.lineDirections[i9] = (pMatrix2D.m10 * f3) + (pMatrix2D.m11 * f4);
                    float[] fArr = this.lineDirections;
                    int i10 = i9 + 2;
                    fArr[i10] = fArr[i10] * matrixScale;
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void applyMatrixOnLineGeometry(PMatrix3D pMatrix3D, int i, int i2) {
            if (i < i2) {
                float matrixScale = PGraphicsOpenGL.matrixScale(pMatrix3D);
                while (i <= i2) {
                    int i3 = i * 4;
                    int i4 = i3 + 1;
                    float f = this.lineVertices[i3];
                    int i5 = i4 + 1;
                    float f2 = this.lineVertices[i4];
                    float f3 = this.lineVertices[i5];
                    float f4 = this.lineVertices[i5 + 1];
                    int i6 = i * 4;
                    int i7 = i6 + 1;
                    float f5 = this.lineDirections[i6];
                    float f6 = this.lineDirections[i7];
                    float f7 = this.lineDirections[i7 + 1];
                    int i8 = i * 4;
                    int i9 = i8 + 1;
                    this.lineVertices[i8] = (pMatrix3D.m00 * f) + (pMatrix3D.m01 * f2) + (pMatrix3D.m02 * f3) + (pMatrix3D.m03 * f4);
                    int i10 = i9 + 1;
                    this.lineVertices[i9] = (pMatrix3D.m10 * f) + (pMatrix3D.m11 * f2) + (pMatrix3D.m12 * f3) + (pMatrix3D.m13 * f4);
                    this.lineVertices[i10] = (pMatrix3D.m20 * f) + (pMatrix3D.m21 * f2) + (pMatrix3D.m22 * f3) + (pMatrix3D.m23 * f4);
                    this.lineVertices[i10 + 1] = (f * pMatrix3D.m30) + (f2 * pMatrix3D.m31) + (pMatrix3D.m32 * f3) + (pMatrix3D.m33 * f4);
                    int i11 = i * 4;
                    int i12 = i11 + 1;
                    this.lineDirections[i11] = (pMatrix3D.m00 * f5) + (pMatrix3D.m01 * f6) + (pMatrix3D.m02 * f7);
                    int i13 = i12 + 1;
                    this.lineDirections[i12] = (pMatrix3D.m10 * f5) + (pMatrix3D.m11 * f6) + (pMatrix3D.m12 * f7);
                    int i14 = i13 + 1;
                    this.lineDirections[i13] = (pMatrix3D.m20 * f5) + (pMatrix3D.m21 * f6) + (pMatrix3D.m22 * f7);
                    float[] fArr = this.lineDirections;
                    fArr[i14] = fArr[i14] * matrixScale;
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void applyMatrixOnLineGeometry(PMatrix pMatrix, int i, int i2) {
            if (pMatrix instanceof PMatrix2D) {
                applyMatrixOnLineGeometry((PMatrix2D) pMatrix, i, i2);
            } else if (pMatrix instanceof PMatrix3D) {
                applyMatrixOnLineGeometry((PMatrix3D) pMatrix, i, i2);
            }
        }

        /* access modifiers changed from: package-private */
        public void applyMatrixOnPointGeometry(PMatrix2D pMatrix2D, int i, int i2) {
            if (i < i2) {
                float matrixScale = PGraphicsOpenGL.matrixScale(pMatrix2D);
                while (i <= i2) {
                    int i3 = i * 4;
                    int i4 = i3 + 1;
                    float f = this.pointVertices[i3];
                    float f2 = this.pointVertices[i4];
                    int i5 = i * 4;
                    this.pointVertices[i5] = (pMatrix2D.m00 * f) + (pMatrix2D.m01 * f2) + pMatrix2D.m02;
                    this.pointVertices[i5 + 1] = (f * pMatrix2D.m10) + (f2 * pMatrix2D.m11) + pMatrix2D.m12;
                    int i6 = i * 2;
                    float[] fArr = this.pointOffsets;
                    int i7 = i6 + 1;
                    fArr[i6] = fArr[i6] * matrixScale;
                    float[] fArr2 = this.pointOffsets;
                    fArr2[i7] = fArr2[i7] * matrixScale;
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void applyMatrixOnPointGeometry(PMatrix3D pMatrix3D, int i, int i2) {
            if (i < i2) {
                float matrixScale = PGraphicsOpenGL.matrixScale(pMatrix3D);
                while (i <= i2) {
                    int i3 = i * 4;
                    int i4 = i3 + 1;
                    float f = this.pointVertices[i3];
                    int i5 = i4 + 1;
                    float f2 = this.pointVertices[i4];
                    float f3 = this.pointVertices[i5];
                    float f4 = this.pointVertices[i5 + 1];
                    int i6 = i * 4;
                    int i7 = i6 + 1;
                    this.pointVertices[i6] = (pMatrix3D.m00 * f) + (pMatrix3D.m01 * f2) + (pMatrix3D.m02 * f3) + (pMatrix3D.m03 * f4);
                    int i8 = i7 + 1;
                    this.pointVertices[i7] = (pMatrix3D.m10 * f) + (pMatrix3D.m11 * f2) + (pMatrix3D.m12 * f3) + (pMatrix3D.m13 * f4);
                    this.pointVertices[i8] = (pMatrix3D.m20 * f) + (pMatrix3D.m21 * f2) + (pMatrix3D.m22 * f3) + (pMatrix3D.m23 * f4);
                    this.pointVertices[i8 + 1] = (f * pMatrix3D.m30) + (f2 * pMatrix3D.m31) + (pMatrix3D.m32 * f3) + (pMatrix3D.m33 * f4);
                    int i9 = i * 2;
                    float[] fArr = this.pointOffsets;
                    int i10 = i9 + 1;
                    fArr[i9] = fArr[i9] * matrixScale;
                    float[] fArr2 = this.pointOffsets;
                    fArr2[i10] = fArr2[i10] * matrixScale;
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void applyMatrixOnPointGeometry(PMatrix pMatrix, int i, int i2) {
            if (pMatrix instanceof PMatrix2D) {
                applyMatrixOnPointGeometry((PMatrix2D) pMatrix, i, i2);
            } else if (pMatrix instanceof PMatrix3D) {
                applyMatrixOnPointGeometry((PMatrix3D) pMatrix, i, i2);
            }
        }

        /* access modifiers changed from: package-private */
        public void applyMatrixOnPolyGeometry(PMatrix2D pMatrix2D, int i, int i2) {
            if (i < i2) {
                while (i <= i2) {
                    int i3 = i * 4;
                    int i4 = i3 + 1;
                    float f = this.polyVertices[i3];
                    float f2 = this.polyVertices[i4];
                    int i5 = i * 3;
                    int i6 = i5 + 1;
                    float f3 = this.polyNormals[i5];
                    float f4 = this.polyNormals[i6];
                    int i7 = i * 4;
                    this.polyVertices[i7] = (pMatrix2D.m00 * f) + (pMatrix2D.m01 * f2) + pMatrix2D.m02;
                    this.polyVertices[i7 + 1] = (f * pMatrix2D.m10) + (f2 * pMatrix2D.m11) + pMatrix2D.m12;
                    int i8 = i * 3;
                    this.polyNormals[i8] = (pMatrix2D.m00 * f3) + (pMatrix2D.m01 * f4);
                    this.polyNormals[i8 + 1] = (pMatrix2D.m10 * f3) + (pMatrix2D.m11 * f4);
                    for (String str : this.polyAttribs.keySet()) {
                        VertexAttribute vertexAttribute = (VertexAttribute) this.polyAttribs.get(str);
                        if (!vertexAttribute.isColor() && !vertexAttribute.isOther()) {
                            float[] fArr = this.fpolyAttribs.get(str);
                            if (vertexAttribute.isPosition()) {
                                int i9 = i * 4;
                                int i10 = i9 + 1;
                                float f5 = fArr[i9];
                                float f6 = fArr[i10];
                                int i11 = i * 4;
                                fArr[i11] = (pMatrix2D.m00 * f5) + (pMatrix2D.m01 * f6) + pMatrix2D.m02;
                                fArr[i11 + 1] = (f5 * pMatrix2D.m10) + (f6 * pMatrix2D.m11) + pMatrix2D.m12;
                            } else {
                                int i12 = i * 3;
                                int i13 = i12 + 1;
                                float f7 = fArr[i12];
                                float f8 = fArr[i13];
                                int i14 = i * 3;
                                fArr[i14] = (pMatrix2D.m00 * f7) + (pMatrix2D.m01 * f8);
                                fArr[i14 + 1] = (f7 * pMatrix2D.m10) + (f8 * pMatrix2D.m11);
                            }
                        }
                    }
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void applyMatrixOnPolyGeometry(PMatrix3D pMatrix3D, int i, int i2) {
            if (i < i2) {
                while (i <= i2) {
                    int i3 = i * 4;
                    int i4 = i3 + 1;
                    float f = this.polyVertices[i3];
                    int i5 = i4 + 1;
                    float f2 = this.polyVertices[i4];
                    float f3 = this.polyVertices[i5];
                    float f4 = this.polyVertices[i5 + 1];
                    int i6 = i * 3;
                    int i7 = i6 + 1;
                    float f5 = this.polyNormals[i6];
                    float f6 = this.polyNormals[i7];
                    float f7 = this.polyNormals[i7 + 1];
                    int i8 = i * 4;
                    int i9 = i8 + 1;
                    this.polyVertices[i8] = (pMatrix3D.m00 * f) + (pMatrix3D.m01 * f2) + (pMatrix3D.m02 * f3) + (pMatrix3D.m03 * f4);
                    int i10 = i9 + 1;
                    this.polyVertices[i9] = (pMatrix3D.m10 * f) + (pMatrix3D.m11 * f2) + (pMatrix3D.m12 * f3) + (pMatrix3D.m13 * f4);
                    this.polyVertices[i10] = (pMatrix3D.m20 * f) + (pMatrix3D.m21 * f2) + (pMatrix3D.m22 * f3) + (pMatrix3D.m23 * f4);
                    this.polyVertices[i10 + 1] = (f * pMatrix3D.m30) + (f2 * pMatrix3D.m31) + (pMatrix3D.m32 * f3) + (pMatrix3D.m33 * f4);
                    int i11 = i * 3;
                    int i12 = i11 + 1;
                    this.polyNormals[i11] = (pMatrix3D.m00 * f5) + (pMatrix3D.m01 * f6) + (pMatrix3D.m02 * f7);
                    this.polyNormals[i12] = (pMatrix3D.m10 * f5) + (pMatrix3D.m11 * f6) + (pMatrix3D.m12 * f7);
                    this.polyNormals[i12 + 1] = (pMatrix3D.m20 * f5) + (pMatrix3D.m21 * f6) + (pMatrix3D.m22 * f7);
                    for (String str : this.polyAttribs.keySet()) {
                        VertexAttribute vertexAttribute = (VertexAttribute) this.polyAttribs.get(str);
                        if (!vertexAttribute.isColor() && !vertexAttribute.isOther()) {
                            float[] fArr = this.fpolyAttribs.get(str);
                            if (vertexAttribute.isPosition()) {
                                int i13 = i * 4;
                                int i14 = i13 + 1;
                                float f8 = fArr[i13];
                                int i15 = i14 + 1;
                                float f9 = fArr[i14];
                                int i16 = i15 + 1;
                                float f10 = fArr[i15];
                                float f11 = fArr[i16];
                                int i17 = i * 4;
                                int i18 = i17 + 1;
                                fArr[i17] = (pMatrix3D.m00 * f8) + (pMatrix3D.m01 * f9) + (pMatrix3D.m02 * f10) + (pMatrix3D.m03 * f11);
                                int i19 = i18 + 1;
                                fArr[i18] = (pMatrix3D.m10 * f8) + (pMatrix3D.m11 * f9) + (pMatrix3D.m12 * f10) + (pMatrix3D.m13 * f11);
                                fArr[i19] = (pMatrix3D.m20 * f8) + (pMatrix3D.m21 * f9) + (pMatrix3D.m22 * f10) + (pMatrix3D.m23 * f11);
                                fArr[i19 + 1] = (f8 * pMatrix3D.m30) + (f9 * pMatrix3D.m31) + (pMatrix3D.m32 * f10) + (pMatrix3D.m33 * f11);
                            } else {
                                int i20 = i * 3;
                                int i21 = i20 + 1;
                                float f12 = fArr[i20];
                                int i22 = i21 + 1;
                                float f13 = fArr[i21];
                                float f14 = fArr[i22];
                                int i23 = i * 3;
                                int i24 = i23 + 1;
                                fArr[i23] = (pMatrix3D.m00 * f12) + (pMatrix3D.m01 * f13) + (pMatrix3D.m02 * f14);
                                fArr[i24] = (pMatrix3D.m10 * f12) + (pMatrix3D.m11 * f13) + (pMatrix3D.m12 * f14);
                                fArr[i24 + 1] = (f12 * pMatrix3D.m20) + (f13 * pMatrix3D.m21) + (pMatrix3D.m22 * f14);
                            }
                        }
                    }
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void applyMatrixOnPolyGeometry(PMatrix pMatrix, int i, int i2) {
            if (pMatrix instanceof PMatrix2D) {
                applyMatrixOnPolyGeometry((PMatrix2D) pMatrix, i, i2);
            } else if (pMatrix instanceof PMatrix3D) {
                applyMatrixOnPolyGeometry((PMatrix3D) pMatrix, i, i2);
            }
        }

        /* access modifiers changed from: package-private */
        public void calcPolyNormal(int i, int i2, int i3) {
            int i4 = i * 4;
            int i5 = i4 + 1;
            float f = this.polyVertices[i4];
            float f2 = this.polyVertices[i5];
            float f3 = this.polyVertices[i5 + 1];
            int i6 = i2 * 4;
            int i7 = i6 + 1;
            float f4 = this.polyVertices[i6];
            float f5 = this.polyVertices[i7];
            float f6 = this.polyVertices[i7 + 1];
            int i8 = i3 * 4;
            int i9 = i8 + 1;
            float f7 = this.polyVertices[i8];
            float f8 = f7 - f4;
            float f9 = this.polyVertices[i9] - f5;
            float f10 = this.polyVertices[i9 + 1] - f6;
            float f11 = f - f4;
            float f12 = f2 - f5;
            float f13 = f3 - f6;
            float f14 = (f9 * f13) - (f12 * f10);
            float f15 = (f10 * f11) - (f13 * f8);
            float f16 = (f12 * f8) - (f11 * f9);
            float sqrt = PApplet.sqrt((f14 * f14) + (f15 * f15) + (f16 * f16));
            float f17 = f14 / sqrt;
            float f18 = f15 / sqrt;
            float f19 = f16 / sqrt;
            int i10 = i * 3;
            int i11 = i10 + 1;
            this.polyNormals[i10] = f17;
            this.polyNormals[i11] = f18;
            this.polyNormals[i11 + 1] = f19;
            int i12 = i2 * 3;
            int i13 = i12 + 1;
            this.polyNormals[i12] = f17;
            this.polyNormals[i13] = f18;
            this.polyNormals[i13 + 1] = f19;
            int i14 = i3 * 3;
            int i15 = i14 + 1;
            this.polyNormals[i14] = f17;
            this.polyNormals[i15] = f18;
            this.polyNormals[i15 + 1] = f19;
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            this.polyVertexCount = 0;
            this.lastPolyVertex = 0;
            this.firstPolyVertex = 0;
            this.polyIndexCount = 0;
            this.lastPolyIndex = 0;
            this.firstPolyIndex = 0;
            this.lineVertexCount = 0;
            this.lastLineVertex = 0;
            this.firstLineVertex = 0;
            this.lineIndexCount = 0;
            this.lastLineIndex = 0;
            this.firstLineIndex = 0;
            this.pointVertexCount = 0;
            this.lastPointVertex = 0;
            this.firstPointVertex = 0;
            this.pointIndexCount = 0;
            this.lastPointIndex = 0;
            this.firstPointIndex = 0;
            this.polyIndexCache.clear();
            this.lineIndexCache.clear();
            this.pointIndexCache.clear();
        }

        /* access modifiers changed from: package-private */
        public void expandAttributes(int i) {
            for (String str : this.polyAttribs.keySet()) {
                VertexAttribute vertexAttribute = (VertexAttribute) this.polyAttribs.get(str);
                if (vertexAttribute.type == PGL.FLOAT) {
                    expandFloatAttribute(vertexAttribute, i);
                } else if (vertexAttribute.type == PGL.INT) {
                    expandIntAttribute(vertexAttribute, i);
                } else if (vertexAttribute.type == PGL.BOOL) {
                    expandBoolAttribute(vertexAttribute, i);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void expandBoolAttribute(VertexAttribute vertexAttribute, int i) {
            byte[] bArr = new byte[(vertexAttribute.tessSize * i)];
            PApplet.arrayCopy(this.bpolyAttribs.get(vertexAttribute.name), 0, bArr, 0, vertexAttribute.tessSize * this.polyVertexCount);
            this.bpolyAttribs.put(vertexAttribute.name, bArr);
            this.polyAttribBuffers.put(vertexAttribute.name, PGL.allocateByteBuffer(bArr));
        }

        /* access modifiers changed from: package-private */
        public void expandFloatAttribute(VertexAttribute vertexAttribute, int i) {
            float[] fArr = new float[(vertexAttribute.tessSize * i)];
            PApplet.arrayCopy(this.fpolyAttribs.get(vertexAttribute.name), 0, fArr, 0, vertexAttribute.tessSize * this.polyVertexCount);
            this.fpolyAttribs.put(vertexAttribute.name, fArr);
            this.polyAttribBuffers.put(vertexAttribute.name, PGL.allocateFloatBuffer(fArr));
        }

        /* access modifiers changed from: package-private */
        public void expandIntAttribute(VertexAttribute vertexAttribute, int i) {
            int[] iArr = new int[(vertexAttribute.tessSize * i)];
            PApplet.arrayCopy(this.ipolyAttribs.get(vertexAttribute.name), 0, iArr, 0, vertexAttribute.tessSize * this.polyVertexCount);
            this.ipolyAttribs.put(vertexAttribute.name, iArr);
            this.polyAttribBuffers.put(vertexAttribute.name, PGL.allocateIntBuffer(iArr));
        }

        /* access modifiers changed from: package-private */
        public void expandLineColors(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.lineColors, 0, iArr, 0, this.lineVertexCount);
            this.lineColors = iArr;
            this.lineColorsBuffer = PGL.allocateIntBuffer(this.lineColors);
        }

        /* access modifiers changed from: package-private */
        public void expandLineDirections(int i) {
            float[] fArr = new float[(i * 4)];
            PApplet.arrayCopy(this.lineDirections, 0, fArr, 0, this.lineVertexCount * 4);
            this.lineDirections = fArr;
            this.lineDirectionsBuffer = PGL.allocateFloatBuffer(this.lineDirections);
        }

        /* access modifiers changed from: package-private */
        public void expandLineIndices(int i) {
            short[] sArr = new short[i];
            PApplet.arrayCopy(this.lineIndices, 0, sArr, 0, this.lineIndexCount);
            this.lineIndices = sArr;
            this.lineIndicesBuffer = PGL.allocateShortBuffer(this.lineIndices);
        }

        /* access modifiers changed from: package-private */
        public void expandLineVertices(int i) {
            float[] fArr = new float[(i * 4)];
            PApplet.arrayCopy(this.lineVertices, 0, fArr, 0, this.lineVertexCount * 4);
            this.lineVertices = fArr;
            this.lineVerticesBuffer = PGL.allocateFloatBuffer(this.lineVertices);
        }

        /* access modifiers changed from: package-private */
        public void expandPointColors(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.pointColors, 0, iArr, 0, this.pointVertexCount);
            this.pointColors = iArr;
            this.pointColorsBuffer = PGL.allocateIntBuffer(this.pointColors);
        }

        /* access modifiers changed from: package-private */
        public void expandPointIndices(int i) {
            short[] sArr = new short[i];
            PApplet.arrayCopy(this.pointIndices, 0, sArr, 0, this.pointIndexCount);
            this.pointIndices = sArr;
            this.pointIndicesBuffer = PGL.allocateShortBuffer(this.pointIndices);
        }

        /* access modifiers changed from: package-private */
        public void expandPointOffsets(int i) {
            float[] fArr = new float[(i * 2)];
            PApplet.arrayCopy(this.pointOffsets, 0, fArr, 0, this.pointVertexCount * 2);
            this.pointOffsets = fArr;
            this.pointOffsetsBuffer = PGL.allocateFloatBuffer(this.pointOffsets);
        }

        /* access modifiers changed from: package-private */
        public void expandPointVertices(int i) {
            float[] fArr = new float[(i * 4)];
            PApplet.arrayCopy(this.pointVertices, 0, fArr, 0, this.pointVertexCount * 4);
            this.pointVertices = fArr;
            this.pointVerticesBuffer = PGL.allocateFloatBuffer(this.pointVertices);
        }

        /* access modifiers changed from: package-private */
        public void expandPolyAmbient(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.polyAmbient, 0, iArr, 0, this.polyVertexCount);
            this.polyAmbient = iArr;
            this.polyAmbientBuffer = PGL.allocateIntBuffer(this.polyAmbient);
        }

        /* access modifiers changed from: package-private */
        public void expandPolyColors(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.polyColors, 0, iArr, 0, this.polyVertexCount);
            this.polyColors = iArr;
            this.polyColorsBuffer = PGL.allocateIntBuffer(this.polyColors);
        }

        /* access modifiers changed from: package-private */
        public void expandPolyEmissive(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.polyEmissive, 0, iArr, 0, this.polyVertexCount);
            this.polyEmissive = iArr;
            this.polyEmissiveBuffer = PGL.allocateIntBuffer(this.polyEmissive);
        }

        /* access modifiers changed from: package-private */
        public void expandPolyIndices(int i) {
            short[] sArr = new short[i];
            PApplet.arrayCopy(this.polyIndices, 0, sArr, 0, this.polyIndexCount);
            this.polyIndices = sArr;
            this.polyIndicesBuffer = PGL.allocateShortBuffer(this.polyIndices);
        }

        /* access modifiers changed from: package-private */
        public void expandPolyNormals(int i) {
            float[] fArr = new float[(i * 3)];
            PApplet.arrayCopy(this.polyNormals, 0, fArr, 0, this.polyVertexCount * 3);
            this.polyNormals = fArr;
            this.polyNormalsBuffer = PGL.allocateFloatBuffer(this.polyNormals);
        }

        /* access modifiers changed from: package-private */
        public void expandPolyShininess(int i) {
            float[] fArr = new float[i];
            PApplet.arrayCopy(this.polyShininess, 0, fArr, 0, this.polyVertexCount);
            this.polyShininess = fArr;
            this.polyShininessBuffer = PGL.allocateFloatBuffer(this.polyShininess);
        }

        /* access modifiers changed from: package-private */
        public void expandPolySpecular(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.polySpecular, 0, iArr, 0, this.polyVertexCount);
            this.polySpecular = iArr;
            this.polySpecularBuffer = PGL.allocateIntBuffer(this.polySpecular);
        }

        /* access modifiers changed from: package-private */
        public void expandPolyTexCoords(int i) {
            float[] fArr = new float[(i * 2)];
            PApplet.arrayCopy(this.polyTexCoords, 0, fArr, 0, this.polyVertexCount * 2);
            this.polyTexCoords = fArr;
            this.polyTexCoordsBuffer = PGL.allocateFloatBuffer(this.polyTexCoords);
        }

        /* access modifiers changed from: package-private */
        public void expandPolyVertices(int i) {
            float[] fArr = new float[(i * 4)];
            PApplet.arrayCopy(this.polyVertices, 0, fArr, 0, this.polyVertexCount * 4);
            this.polyVertices = fArr;
            this.polyVerticesBuffer = PGL.allocateFloatBuffer(this.polyVertices);
        }

        /* access modifiers changed from: package-private */
        public void getLineVertexMax(PVector pVector, int i, int i2) {
            while (i <= i2) {
                int i3 = i * 4;
                int i4 = i3 + 1;
                pVector.x = PApplet.max(pVector.x, this.lineVertices[i3]);
                pVector.y = PApplet.max(pVector.y, this.lineVertices[i4]);
                pVector.z = PApplet.max(pVector.z, this.lineVertices[i4 + 1]);
                i++;
            }
        }

        /* access modifiers changed from: package-private */
        public void getLineVertexMin(PVector pVector, int i, int i2) {
            while (i <= i2) {
                int i3 = i * 4;
                int i4 = i3 + 1;
                pVector.x = PApplet.min(pVector.x, this.lineVertices[i3]);
                pVector.y = PApplet.min(pVector.y, this.lineVertices[i4]);
                pVector.z = PApplet.min(pVector.z, this.lineVertices[i4 + 1]);
                i++;
            }
        }

        /* access modifiers changed from: package-private */
        public int getLineVertexSum(PVector pVector, int i, int i2) {
            for (int i3 = i; i3 <= i2; i3++) {
                int i4 = i3 * 4;
                int i5 = i4 + 1;
                pVector.x = this.lineVertices[i4] + pVector.x;
                pVector.y += this.lineVertices[i5];
                pVector.z += this.lineVertices[i5 + 1];
            }
            return (i2 - i) + 1;
        }

        /* access modifiers changed from: package-private */
        public void getPointVertexMax(PVector pVector, int i, int i2) {
            while (i <= i2) {
                int i3 = i * 4;
                int i4 = i3 + 1;
                pVector.x = PApplet.max(pVector.x, this.pointVertices[i3]);
                pVector.y = PApplet.max(pVector.y, this.pointVertices[i4]);
                pVector.z = PApplet.max(pVector.z, this.pointVertices[i4 + 1]);
                i++;
            }
        }

        /* access modifiers changed from: package-private */
        public void getPointVertexMin(PVector pVector, int i, int i2) {
            while (i <= i2) {
                int i3 = i * 4;
                int i4 = i3 + 1;
                pVector.x = PApplet.min(pVector.x, this.pointVertices[i3]);
                pVector.y = PApplet.min(pVector.y, this.pointVertices[i4]);
                pVector.z = PApplet.min(pVector.z, this.pointVertices[i4 + 1]);
                i++;
            }
        }

        /* access modifiers changed from: package-private */
        public int getPointVertexSum(PVector pVector, int i, int i2) {
            for (int i3 = i; i3 <= i2; i3++) {
                int i4 = i3 * 4;
                int i5 = i4 + 1;
                pVector.x = this.pointVertices[i4] + pVector.x;
                pVector.y += this.pointVertices[i5];
                pVector.z += this.pointVertices[i5 + 1];
            }
            return (i2 - i) + 1;
        }

        /* access modifiers changed from: package-private */
        public void getPolyVertexMax(PVector pVector, int i, int i2) {
            while (i <= i2) {
                int i3 = i * 4;
                int i4 = i3 + 1;
                pVector.x = PApplet.max(pVector.x, this.polyVertices[i3]);
                pVector.y = PApplet.max(pVector.y, this.polyVertices[i4]);
                pVector.z = PApplet.max(pVector.z, this.polyVertices[i4 + 1]);
                i++;
            }
        }

        /* access modifiers changed from: package-private */
        public void getPolyVertexMin(PVector pVector, int i, int i2) {
            while (i <= i2) {
                int i3 = i * 4;
                int i4 = i3 + 1;
                pVector.x = PApplet.min(pVector.x, this.polyVertices[i3]);
                pVector.y = PApplet.min(pVector.y, this.polyVertices[i4]);
                pVector.z = PApplet.min(pVector.z, this.polyVertices[i4 + 1]);
                i++;
            }
        }

        /* access modifiers changed from: package-private */
        public int getPolyVertexSum(PVector pVector, int i, int i2) {
            for (int i3 = i; i3 <= i2; i3++) {
                int i4 = i3 * 4;
                int i5 = i4 + 1;
                pVector.x = this.polyVertices[i4] + pVector.x;
                pVector.y += this.polyVertices[i5];
                pVector.z += this.polyVertices[i5 + 1];
            }
            return (i2 - i) + 1;
        }

        /* access modifiers changed from: package-private */
        public void incLineIndices(int i, int i2, int i3) {
            while (i <= i2) {
                short[] sArr = this.lineIndices;
                sArr[i] = (short) (sArr[i] + i3);
                i++;
            }
        }

        /* access modifiers changed from: package-private */
        public void incPointIndices(int i, int i2, int i3) {
            while (i <= i2) {
                short[] sArr = this.pointIndices;
                sArr[i] = (short) (sArr[i] + i3);
                i++;
            }
        }

        /* access modifiers changed from: package-private */
        public void incPolyIndices(int i, int i2, int i3) {
            while (i <= i2) {
                short[] sArr = this.polyIndices;
                sArr[i] = (short) (sArr[i] + i3);
                i++;
            }
        }

        /* access modifiers changed from: package-private */
        public void initAttrib(VertexAttribute vertexAttribute) {
            if (vertexAttribute.type == PGL.FLOAT && !this.fpolyAttribs.containsKey(vertexAttribute.name)) {
                float[] fArr = new float[(vertexAttribute.tessSize * PGL.DEFAULT_TESS_VERTICES)];
                this.fpolyAttribs.put(vertexAttribute.name, fArr);
                this.polyAttribBuffers.put(vertexAttribute.name, PGL.allocateFloatBuffer(fArr));
            } else if (vertexAttribute.type == PGL.INT && !this.ipolyAttribs.containsKey(vertexAttribute.name)) {
                int[] iArr = new int[(vertexAttribute.tessSize * PGL.DEFAULT_TESS_VERTICES)];
                this.ipolyAttribs.put(vertexAttribute.name, iArr);
                this.polyAttribBuffers.put(vertexAttribute.name, PGL.allocateIntBuffer(iArr));
            } else if (vertexAttribute.type == PGL.BOOL && !this.bpolyAttribs.containsKey(vertexAttribute.name)) {
                byte[] bArr = new byte[(vertexAttribute.tessSize * PGL.DEFAULT_TESS_VERTICES)];
                this.bpolyAttribs.put(vertexAttribute.name, bArr);
                this.polyAttribBuffers.put(vertexAttribute.name, PGL.allocateByteBuffer(bArr));
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isFull() {
            return PGL.FLUSH_VERTEX_COUNT <= this.polyVertexCount || PGL.FLUSH_VERTEX_COUNT <= this.lineVertexCount || PGL.FLUSH_VERTEX_COUNT <= this.pointVertexCount;
        }

        /* access modifiers changed from: package-private */
        public void lineIndexCheck(int i) {
            int length = this.lineIndices.length;
            if (this.lineIndexCount + i > length) {
                expandLineIndices(PGraphicsOpenGL.expandArraySize(length, this.lineIndexCount + i));
            }
            this.firstLineIndex = this.lineIndexCount;
            this.lineIndexCount += i;
            this.lastLineIndex = this.lineIndexCount - 1;
        }

        /* access modifiers changed from: package-private */
        public void lineVertexCheck(int i) {
            int length = this.lineVertices.length / 4;
            if (this.lineVertexCount + i > length) {
                int expandArraySize = PGraphicsOpenGL.expandArraySize(length, this.lineVertexCount + i);
                expandLineVertices(expandArraySize);
                expandLineColors(expandArraySize);
                expandLineDirections(expandArraySize);
            }
            this.firstLineVertex = this.lineVertexCount;
            this.lineVertexCount += i;
            this.lastLineVertex = this.lineVertexCount - 1;
        }

        /* access modifiers changed from: package-private */
        public void pointIndexCheck(int i) {
            int length = this.pointIndices.length;
            if (this.pointIndexCount + i > length) {
                expandPointIndices(PGraphicsOpenGL.expandArraySize(length, this.pointIndexCount + i));
            }
            this.firstPointIndex = this.pointIndexCount;
            this.pointIndexCount += i;
            this.lastPointIndex = this.pointIndexCount - 1;
        }

        /* access modifiers changed from: package-private */
        public void pointVertexCheck(int i) {
            int length = this.pointVertices.length / 4;
            if (this.pointVertexCount + i > length) {
                int expandArraySize = PGraphicsOpenGL.expandArraySize(length, this.pointVertexCount + i);
                expandPointVertices(expandArraySize);
                expandPointColors(expandArraySize);
                expandPointOffsets(expandArraySize);
            }
            this.firstPointVertex = this.pointVertexCount;
            this.pointVertexCount += i;
            this.lastPointVertex = this.pointVertexCount - 1;
        }

        /* access modifiers changed from: package-private */
        public void polyIndexCheck() {
            if (this.polyIndexCount == this.polyIndices.length) {
                expandPolyIndices(this.polyIndexCount << 1);
            }
            this.firstPolyIndex = this.polyIndexCount;
            this.polyIndexCount++;
            this.lastPolyIndex = this.polyIndexCount - 1;
        }

        /* access modifiers changed from: package-private */
        public void polyIndexCheck(int i) {
            int length = this.polyIndices.length;
            if (this.polyIndexCount + i > length) {
                expandPolyIndices(PGraphicsOpenGL.expandArraySize(length, this.polyIndexCount + i));
            }
            this.firstPolyIndex = this.polyIndexCount;
            this.polyIndexCount += i;
            this.lastPolyIndex = this.polyIndexCount - 1;
        }

        /* access modifiers changed from: package-private */
        public void polyVertexCheck() {
            if (this.polyVertexCount == this.polyVertices.length / 4) {
                int i = this.polyVertexCount << 1;
                expandPolyVertices(i);
                expandPolyColors(i);
                expandPolyNormals(i);
                expandPolyTexCoords(i);
                expandPolyAmbient(i);
                expandPolySpecular(i);
                expandPolyEmissive(i);
                expandPolyShininess(i);
                expandAttributes(i);
            }
            this.firstPolyVertex = this.polyVertexCount;
            this.polyVertexCount++;
            this.lastPolyVertex = this.polyVertexCount - 1;
        }

        /* access modifiers changed from: package-private */
        public void polyVertexCheck(int i) {
            int length = this.polyVertices.length / 4;
            if (this.polyVertexCount + i > length) {
                int expandArraySize = PGraphicsOpenGL.expandArraySize(length, this.polyVertexCount + i);
                expandPolyVertices(expandArraySize);
                expandPolyColors(expandArraySize);
                expandPolyNormals(expandArraySize);
                expandPolyTexCoords(expandArraySize);
                expandPolyAmbient(expandArraySize);
                expandPolySpecular(expandArraySize);
                expandPolyEmissive(expandArraySize);
                expandPolyShininess(expandArraySize);
                expandAttributes(expandArraySize);
            }
            this.firstPolyVertex = this.polyVertexCount;
            this.polyVertexCount += i;
            this.lastPolyVertex = this.polyVertexCount - 1;
        }

        /* access modifiers changed from: package-private */
        public void setLineVertex(int i, float[] fArr, int i2, int i3) {
            int i4 = i2 * 3;
            int i5 = i4 + 1;
            float f = fArr[i4];
            int i6 = i5 + 1;
            float f2 = fArr[i5];
            float f3 = fArr[i6];
            if (this.renderMode == 0 && this.pg.flushMode == 1) {
                PMatrix3D pMatrix3D = this.pg.modelview;
                int i7 = i * 4;
                int i8 = i7 + 1;
                this.lineVertices[i7] = (pMatrix3D.m00 * f) + (pMatrix3D.m01 * f2) + (pMatrix3D.m02 * f3) + pMatrix3D.m03;
                int i9 = i8 + 1;
                this.lineVertices[i8] = (pMatrix3D.m10 * f) + (pMatrix3D.m11 * f2) + (pMatrix3D.m12 * f3) + pMatrix3D.m13;
                this.lineVertices[i9] = (pMatrix3D.m20 * f) + (pMatrix3D.m21 * f2) + (pMatrix3D.m22 * f3) + pMatrix3D.m23;
                this.lineVertices[i9 + 1] = (f * pMatrix3D.m30) + (f2 * pMatrix3D.m31) + (pMatrix3D.m32 * f3) + pMatrix3D.m33;
            } else {
                int i10 = i * 4;
                int i11 = i10 + 1;
                this.lineVertices[i10] = f;
                int i12 = i11 + 1;
                this.lineVertices[i11] = f2;
                this.lineVertices[i12] = f3;
                this.lineVertices[i12 + 1] = 1.0f;
            }
            this.lineColors[i] = i3;
            int i13 = i * 4;
            int i14 = i13 + 1;
            this.lineDirections[i13] = 0.0f;
            int i15 = i14 + 1;
            this.lineDirections[i14] = 0.0f;
            this.lineDirections[i15] = 0.0f;
            this.lineDirections[i15 + 1] = 0.0f;
        }

        /* access modifiers changed from: package-private */
        public void setLineVertex(int i, float[] fArr, int i2, int i3, int i4, float f) {
            int i5 = i2 * 3;
            int i6 = i5 + 1;
            float f2 = fArr[i5];
            int i7 = i6 + 1;
            float f3 = fArr[i6];
            float f4 = fArr[i7];
            int i8 = i3 * 3;
            int i9 = i8 + 1;
            float f5 = fArr[i8];
            int i10 = i9 + 1;
            float f6 = f5 - f2;
            float f7 = fArr[i9] - f3;
            float f8 = fArr[i10] - f4;
            if (this.renderMode == 0 && this.pg.flushMode == 1) {
                PMatrix3D pMatrix3D = this.pg.modelview;
                int i11 = i * 4;
                int i12 = i11 + 1;
                this.lineVertices[i11] = (pMatrix3D.m00 * f2) + (pMatrix3D.m01 * f3) + (pMatrix3D.m02 * f4) + pMatrix3D.m03;
                int i13 = i12 + 1;
                this.lineVertices[i12] = (pMatrix3D.m10 * f2) + (pMatrix3D.m11 * f3) + (pMatrix3D.m12 * f4) + pMatrix3D.m13;
                this.lineVertices[i13] = (pMatrix3D.m20 * f2) + (pMatrix3D.m21 * f3) + (pMatrix3D.m22 * f4) + pMatrix3D.m23;
                this.lineVertices[i13 + 1] = (f2 * pMatrix3D.m30) + (f3 * pMatrix3D.m31) + (pMatrix3D.m32 * f4) + pMatrix3D.m33;
                int i14 = i * 4;
                int i15 = i14 + 1;
                this.lineDirections[i14] = (pMatrix3D.m00 * f6) + (pMatrix3D.m01 * f7) + (pMatrix3D.m02 * f8);
                this.lineDirections[i15] = (pMatrix3D.m10 * f6) + (pMatrix3D.m11 * f7) + (pMatrix3D.m12 * f8);
                this.lineDirections[i15 + 1] = (pMatrix3D.m20 * f6) + (pMatrix3D.m21 * f7) + (pMatrix3D.m22 * f8);
            } else {
                int i16 = i * 4;
                int i17 = i16 + 1;
                this.lineVertices[i16] = f2;
                int i18 = i17 + 1;
                this.lineVertices[i17] = f3;
                this.lineVertices[i18] = f4;
                this.lineVertices[i18 + 1] = 1.0f;
                int i19 = i * 4;
                int i20 = i19 + 1;
                this.lineDirections[i19] = f6;
                this.lineDirections[i20] = f7;
                this.lineDirections[i20 + 1] = f8;
            }
            this.lineColors[i] = i4;
            this.lineDirections[(i * 4) + 3] = f;
        }

        /* access modifiers changed from: package-private */
        public void setPointVertex(int i, InGeometry inGeometry, int i2) {
            int i3 = i2 * 3;
            int i4 = i3 + 1;
            float f = inGeometry.vertices[i3];
            float f2 = inGeometry.vertices[i4];
            float f3 = inGeometry.vertices[i4 + 1];
            if (this.renderMode == 0 && this.pg.flushMode == 1) {
                PMatrix3D pMatrix3D = this.pg.modelview;
                int i5 = i * 4;
                int i6 = i5 + 1;
                this.pointVertices[i5] = (pMatrix3D.m00 * f) + (pMatrix3D.m01 * f2) + (pMatrix3D.m02 * f3) + pMatrix3D.m03;
                int i7 = i6 + 1;
                this.pointVertices[i6] = (pMatrix3D.m10 * f) + (pMatrix3D.m11 * f2) + (pMatrix3D.m12 * f3) + pMatrix3D.m13;
                this.pointVertices[i7] = (pMatrix3D.m20 * f) + (pMatrix3D.m21 * f2) + (pMatrix3D.m22 * f3) + pMatrix3D.m23;
                this.pointVertices[i7 + 1] = (f * pMatrix3D.m30) + (f2 * pMatrix3D.m31) + (pMatrix3D.m32 * f3) + pMatrix3D.m33;
            } else {
                int i8 = i * 4;
                int i9 = i8 + 1;
                this.pointVertices[i8] = f;
                int i10 = i9 + 1;
                this.pointVertices[i9] = f2;
                this.pointVertices[i10] = f3;
                this.pointVertices[i10 + 1] = 1.0f;
            }
            this.pointColors[i] = inGeometry.strokeColors[i2];
        }

        /* access modifiers changed from: package-private */
        public void setPolyVertex(int i, float f, float f2, float f3, int i2, float f4, float f5, float f6, float f7, float f8, int i3, int i4, int i5, float f9, boolean z) {
            int i6;
            if (this.renderMode == 0 && this.pg.flushMode == 1) {
                PMatrix3D pMatrix3D = this.pg.modelview;
                PMatrix3D pMatrix3D2 = this.pg.modelviewInv;
                int i7 = i * 4;
                if (z) {
                    int i8 = i7 + 1;
                    this.polyVertices[i7] = (float) PApplet.ceil((pMatrix3D.m00 * f) + (pMatrix3D.m01 * f2) + (pMatrix3D.m02 * f3) + pMatrix3D.m03);
                    i6 = i8 + 1;
                    this.polyVertices[i8] = (float) PApplet.ceil((pMatrix3D.m10 * f) + (pMatrix3D.m11 * f2) + (pMatrix3D.m12 * f3) + pMatrix3D.m13);
                } else {
                    int i9 = i7 + 1;
                    this.polyVertices[i7] = (pMatrix3D.m00 * f) + (pMatrix3D.m01 * f2) + (pMatrix3D.m02 * f3) + pMatrix3D.m03;
                    i6 = i9 + 1;
                    this.polyVertices[i9] = (pMatrix3D.m10 * f) + (pMatrix3D.m11 * f2) + (pMatrix3D.m12 * f3) + pMatrix3D.m13;
                }
                int i10 = i6 + 1;
                this.polyVertices[i6] = (pMatrix3D.m20 * f) + (pMatrix3D.m21 * f2) + (pMatrix3D.m22 * f3) + pMatrix3D.m23;
                this.polyVertices[i10] = pMatrix3D.m33 + (pMatrix3D.m30 * f) + (pMatrix3D.m31 * f2) + (pMatrix3D.m32 * f3);
                int i11 = i * 3;
                int i12 = i11 + 1;
                this.polyNormals[i11] = (pMatrix3D2.m00 * f4) + (pMatrix3D2.m10 * f5) + (pMatrix3D2.m20 * f6);
                this.polyNormals[i12] = (pMatrix3D2.m01 * f4) + (pMatrix3D2.m11 * f5) + (pMatrix3D2.m21 * f6);
                this.polyNormals[i12 + 1] = (pMatrix3D2.m22 * f6) + (pMatrix3D2.m02 * f4) + (pMatrix3D2.m12 * f5);
            } else {
                int i13 = i * 4;
                int i14 = i13 + 1;
                this.polyVertices[i13] = f;
                int i15 = i14 + 1;
                this.polyVertices[i14] = f2;
                this.polyVertices[i15] = f3;
                this.polyVertices[i15 + 1] = 1.0f;
                int i16 = i * 3;
                int i17 = i16 + 1;
                this.polyNormals[i16] = f4;
                this.polyNormals[i17] = f5;
                this.polyNormals[i17 + 1] = f6;
            }
            this.polyColors[i] = i2;
            int i18 = i * 2;
            this.polyTexCoords[i18] = f7;
            this.polyTexCoords[i18 + 1] = f8;
            this.polyAmbient[i] = i3;
            this.polySpecular[i] = i4;
            this.polyEmissive[i] = i5;
            this.polyShininess[i] = f9;
        }

        /* access modifiers changed from: package-private */
        public void setPolyVertex(int i, float f, float f2, float f3, int i2, boolean z) {
            setPolyVertex(i, f, f2, f3, i2, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0, 0, 0, 0.0f, z);
        }

        /* access modifiers changed from: package-private */
        public void trim() {
            if (this.polyVertexCount > 0 && this.polyVertexCount < this.polyVertices.length / 4) {
                trimPolyVertices();
                trimPolyColors();
                trimPolyNormals();
                trimPolyTexCoords();
                trimPolyAmbient();
                trimPolySpecular();
                trimPolyEmissive();
                trimPolyShininess();
                trimPolyAttributes();
            }
            if (this.polyIndexCount > 0 && this.polyIndexCount < this.polyIndices.length) {
                trimPolyIndices();
            }
            if (this.lineVertexCount > 0 && this.lineVertexCount < this.lineVertices.length / 4) {
                trimLineVertices();
                trimLineColors();
                trimLineDirections();
            }
            if (this.lineIndexCount > 0 && this.lineIndexCount < this.lineIndices.length) {
                trimLineIndices();
            }
            if (this.pointVertexCount > 0 && this.pointVertexCount < this.pointVertices.length / 4) {
                trimPointVertices();
                trimPointColors();
                trimPointOffsets();
            }
            if (this.pointIndexCount > 0 && this.pointIndexCount < this.pointIndices.length) {
                trimPointIndices();
            }
        }

        /* access modifiers changed from: package-private */
        public void trimBoolAttribute(VertexAttribute vertexAttribute) {
            byte[] bArr = new byte[(vertexAttribute.tessSize * this.polyVertexCount)];
            PApplet.arrayCopy(this.bpolyAttribs.get(vertexAttribute.name), 0, bArr, 0, vertexAttribute.tessSize * this.polyVertexCount);
            this.bpolyAttribs.put(vertexAttribute.name, bArr);
            this.polyAttribBuffers.put(vertexAttribute.name, PGL.allocateByteBuffer(bArr));
        }

        /* access modifiers changed from: package-private */
        public void trimFloatAttribute(VertexAttribute vertexAttribute) {
            float[] fArr = new float[(vertexAttribute.tessSize * this.polyVertexCount)];
            PApplet.arrayCopy(this.fpolyAttribs.get(vertexAttribute.name), 0, fArr, 0, vertexAttribute.tessSize * this.polyVertexCount);
            this.fpolyAttribs.put(vertexAttribute.name, fArr);
            this.polyAttribBuffers.put(vertexAttribute.name, PGL.allocateFloatBuffer(fArr));
        }

        /* access modifiers changed from: package-private */
        public void trimIntAttribute(VertexAttribute vertexAttribute) {
            int[] iArr = new int[(vertexAttribute.tessSize * this.polyVertexCount)];
            PApplet.arrayCopy(this.ipolyAttribs.get(vertexAttribute.name), 0, iArr, 0, vertexAttribute.tessSize * this.polyVertexCount);
            this.ipolyAttribs.put(vertexAttribute.name, iArr);
            this.polyAttribBuffers.put(vertexAttribute.name, PGL.allocateIntBuffer(iArr));
        }

        /* access modifiers changed from: package-private */
        public void trimLineColors() {
            int[] iArr = new int[this.lineVertexCount];
            PApplet.arrayCopy(this.lineColors, 0, iArr, 0, this.lineVertexCount);
            this.lineColors = iArr;
            this.lineColorsBuffer = PGL.allocateIntBuffer(this.lineColors);
        }

        /* access modifiers changed from: package-private */
        public void trimLineDirections() {
            float[] fArr = new float[(this.lineVertexCount * 4)];
            PApplet.arrayCopy(this.lineDirections, 0, fArr, 0, this.lineVertexCount * 4);
            this.lineDirections = fArr;
            this.lineDirectionsBuffer = PGL.allocateFloatBuffer(this.lineDirections);
        }

        /* access modifiers changed from: package-private */
        public void trimLineIndices() {
            short[] sArr = new short[this.lineIndexCount];
            PApplet.arrayCopy(this.lineIndices, 0, sArr, 0, this.lineIndexCount);
            this.lineIndices = sArr;
            this.lineIndicesBuffer = PGL.allocateShortBuffer(this.lineIndices);
        }

        /* access modifiers changed from: package-private */
        public void trimLineVertices() {
            float[] fArr = new float[(this.lineVertexCount * 4)];
            PApplet.arrayCopy(this.lineVertices, 0, fArr, 0, this.lineVertexCount * 4);
            this.lineVertices = fArr;
            this.lineVerticesBuffer = PGL.allocateFloatBuffer(this.lineVertices);
        }

        /* access modifiers changed from: package-private */
        public void trimPointColors() {
            int[] iArr = new int[this.pointVertexCount];
            PApplet.arrayCopy(this.pointColors, 0, iArr, 0, this.pointVertexCount);
            this.pointColors = iArr;
            this.pointColorsBuffer = PGL.allocateIntBuffer(this.pointColors);
        }

        /* access modifiers changed from: package-private */
        public void trimPointIndices() {
            short[] sArr = new short[this.pointIndexCount];
            PApplet.arrayCopy(this.pointIndices, 0, sArr, 0, this.pointIndexCount);
            this.pointIndices = sArr;
            this.pointIndicesBuffer = PGL.allocateShortBuffer(this.pointIndices);
        }

        /* access modifiers changed from: package-private */
        public void trimPointOffsets() {
            float[] fArr = new float[(this.pointVertexCount * 2)];
            PApplet.arrayCopy(this.pointOffsets, 0, fArr, 0, this.pointVertexCount * 2);
            this.pointOffsets = fArr;
            this.pointOffsetsBuffer = PGL.allocateFloatBuffer(this.pointOffsets);
        }

        /* access modifiers changed from: package-private */
        public void trimPointVertices() {
            float[] fArr = new float[(this.pointVertexCount * 4)];
            PApplet.arrayCopy(this.pointVertices, 0, fArr, 0, this.pointVertexCount * 4);
            this.pointVertices = fArr;
            this.pointVerticesBuffer = PGL.allocateFloatBuffer(this.pointVertices);
        }

        /* access modifiers changed from: package-private */
        public void trimPolyAmbient() {
            int[] iArr = new int[this.polyVertexCount];
            PApplet.arrayCopy(this.polyAmbient, 0, iArr, 0, this.polyVertexCount);
            this.polyAmbient = iArr;
            this.polyAmbientBuffer = PGL.allocateIntBuffer(this.polyAmbient);
        }

        /* access modifiers changed from: package-private */
        public void trimPolyAttributes() {
            for (String str : this.polyAttribs.keySet()) {
                VertexAttribute vertexAttribute = (VertexAttribute) this.polyAttribs.get(str);
                if (vertexAttribute.type == PGL.FLOAT) {
                    trimFloatAttribute(vertexAttribute);
                } else if (vertexAttribute.type == PGL.INT) {
                    trimIntAttribute(vertexAttribute);
                } else if (vertexAttribute.type == PGL.BOOL) {
                    trimBoolAttribute(vertexAttribute);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void trimPolyColors() {
            int[] iArr = new int[this.polyVertexCount];
            PApplet.arrayCopy(this.polyColors, 0, iArr, 0, this.polyVertexCount);
            this.polyColors = iArr;
            this.polyColorsBuffer = PGL.allocateIntBuffer(this.polyColors);
        }

        /* access modifiers changed from: package-private */
        public void trimPolyEmissive() {
            int[] iArr = new int[this.polyVertexCount];
            PApplet.arrayCopy(this.polyEmissive, 0, iArr, 0, this.polyVertexCount);
            this.polyEmissive = iArr;
            this.polyEmissiveBuffer = PGL.allocateIntBuffer(this.polyEmissive);
        }

        /* access modifiers changed from: package-private */
        public void trimPolyIndices() {
            short[] sArr = new short[this.polyIndexCount];
            PApplet.arrayCopy(this.polyIndices, 0, sArr, 0, this.polyIndexCount);
            this.polyIndices = sArr;
            this.polyIndicesBuffer = PGL.allocateShortBuffer(this.polyIndices);
        }

        /* access modifiers changed from: package-private */
        public void trimPolyNormals() {
            float[] fArr = new float[(this.polyVertexCount * 3)];
            PApplet.arrayCopy(this.polyNormals, 0, fArr, 0, this.polyVertexCount * 3);
            this.polyNormals = fArr;
            this.polyNormalsBuffer = PGL.allocateFloatBuffer(this.polyNormals);
        }

        /* access modifiers changed from: package-private */
        public void trimPolyShininess() {
            float[] fArr = new float[this.polyVertexCount];
            PApplet.arrayCopy(this.polyShininess, 0, fArr, 0, this.polyVertexCount);
            this.polyShininess = fArr;
            this.polyShininessBuffer = PGL.allocateFloatBuffer(this.polyShininess);
        }

        /* access modifiers changed from: package-private */
        public void trimPolySpecular() {
            int[] iArr = new int[this.polyVertexCount];
            PApplet.arrayCopy(this.polySpecular, 0, iArr, 0, this.polyVertexCount);
            this.polySpecular = iArr;
            this.polySpecularBuffer = PGL.allocateIntBuffer(this.polySpecular);
        }

        /* access modifiers changed from: package-private */
        public void trimPolyTexCoords() {
            float[] fArr = new float[(this.polyVertexCount * 2)];
            PApplet.arrayCopy(this.polyTexCoords, 0, fArr, 0, this.polyVertexCount * 2);
            this.polyTexCoords = fArr;
            this.polyTexCoordsBuffer = PGL.allocateFloatBuffer(this.polyTexCoords);
        }

        /* access modifiers changed from: package-private */
        public void trimPolyVertices() {
            float[] fArr = new float[(this.polyVertexCount * 4)];
            PApplet.arrayCopy(this.polyVertices, 0, fArr, 0, this.polyVertexCount * 4);
            this.polyVertices = fArr;
            this.polyVerticesBuffer = PGL.allocateFloatBuffer(this.polyVertices);
        }

        /* access modifiers changed from: protected */
        public void updateAttribBuffer(String str) {
            updateAttribBuffer(str, 0, this.polyVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updateAttribBuffer(String str, int i, int i2) {
            VertexAttribute vertexAttribute = (VertexAttribute) this.polyAttribs.get(str);
            if (vertexAttribute.type == PGL.FLOAT) {
                PGL.updateFloatBuffer((FloatBuffer) this.polyAttribBuffers.get(str), this.fpolyAttribs.get(str), vertexAttribute.tessSize * i, vertexAttribute.tessSize * i2);
            } else if (vertexAttribute.type == PGL.INT) {
                PGL.updateIntBuffer((IntBuffer) this.polyAttribBuffers.get(str), this.ipolyAttribs.get(str), vertexAttribute.tessSize * i, vertexAttribute.tessSize * i2);
            } else if (vertexAttribute.type == PGL.BOOL) {
                PGL.updateByteBuffer((ByteBuffer) this.polyAttribBuffers.get(str), this.bpolyAttribs.get(str), vertexAttribute.tessSize * i, vertexAttribute.tessSize * i2);
            }
        }

        /* access modifiers changed from: protected */
        public void updateLineColorsBuffer() {
            updateLineColorsBuffer(0, this.lineVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updateLineColorsBuffer(int i, int i2) {
            PGL.updateIntBuffer(this.lineColorsBuffer, this.lineColors, i, i2);
        }

        /* access modifiers changed from: protected */
        public void updateLineDirectionsBuffer() {
            updateLineDirectionsBuffer(0, this.lineVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updateLineDirectionsBuffer(int i, int i2) {
            PGL.updateFloatBuffer(this.lineDirectionsBuffer, this.lineDirections, i * 4, i2 * 4);
        }

        /* access modifiers changed from: protected */
        public void updateLineIndicesBuffer() {
            updateLineIndicesBuffer(0, this.lineIndexCount);
        }

        /* access modifiers changed from: protected */
        public void updateLineIndicesBuffer(int i, int i2) {
            PGL.updateShortBuffer(this.lineIndicesBuffer, this.lineIndices, i, i2);
        }

        /* access modifiers changed from: protected */
        public void updateLineVerticesBuffer() {
            updateLineVerticesBuffer(0, this.lineVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updateLineVerticesBuffer(int i, int i2) {
            PGL.updateFloatBuffer(this.lineVerticesBuffer, this.lineVertices, i * 4, i2 * 4);
        }

        /* access modifiers changed from: protected */
        public void updatePointColorsBuffer() {
            updatePointColorsBuffer(0, this.pointVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updatePointColorsBuffer(int i, int i2) {
            PGL.updateIntBuffer(this.pointColorsBuffer, this.pointColors, i, i2);
        }

        /* access modifiers changed from: protected */
        public void updatePointIndicesBuffer() {
            updatePointIndicesBuffer(0, this.pointIndexCount);
        }

        /* access modifiers changed from: protected */
        public void updatePointIndicesBuffer(int i, int i2) {
            PGL.updateShortBuffer(this.pointIndicesBuffer, this.pointIndices, i, i2);
        }

        /* access modifiers changed from: protected */
        public void updatePointOffsetsBuffer() {
            updatePointOffsetsBuffer(0, this.pointVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updatePointOffsetsBuffer(int i, int i2) {
            PGL.updateFloatBuffer(this.pointOffsetsBuffer, this.pointOffsets, i * 2, i2 * 2);
        }

        /* access modifiers changed from: protected */
        public void updatePointVerticesBuffer() {
            updatePointVerticesBuffer(0, this.pointVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updatePointVerticesBuffer(int i, int i2) {
            PGL.updateFloatBuffer(this.pointVerticesBuffer, this.pointVertices, i * 4, i2 * 4);
        }

        /* access modifiers changed from: protected */
        public void updatePolyAmbientBuffer() {
            updatePolyAmbientBuffer(0, this.polyVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updatePolyAmbientBuffer(int i, int i2) {
            PGL.updateIntBuffer(this.polyAmbientBuffer, this.polyAmbient, i, i2);
        }

        /* access modifiers changed from: protected */
        public void updatePolyColorsBuffer() {
            updatePolyColorsBuffer(0, this.polyVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updatePolyColorsBuffer(int i, int i2) {
            PGL.updateIntBuffer(this.polyColorsBuffer, this.polyColors, i, i2);
        }

        /* access modifiers changed from: protected */
        public void updatePolyEmissiveBuffer() {
            updatePolyEmissiveBuffer(0, this.polyVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updatePolyEmissiveBuffer(int i, int i2) {
            PGL.updateIntBuffer(this.polyEmissiveBuffer, this.polyEmissive, i, i2);
        }

        /* access modifiers changed from: protected */
        public void updatePolyIndicesBuffer() {
            updatePolyIndicesBuffer(0, this.polyIndexCount);
        }

        /* access modifiers changed from: protected */
        public void updatePolyIndicesBuffer(int i, int i2) {
            PGL.updateShortBuffer(this.polyIndicesBuffer, this.polyIndices, i, i2);
        }

        /* access modifiers changed from: protected */
        public void updatePolyNormalsBuffer() {
            updatePolyNormalsBuffer(0, this.polyVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updatePolyNormalsBuffer(int i, int i2) {
            PGL.updateFloatBuffer(this.polyNormalsBuffer, this.polyNormals, i * 3, i2 * 3);
        }

        /* access modifiers changed from: protected */
        public void updatePolyShininessBuffer() {
            updatePolyShininessBuffer(0, this.polyVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updatePolyShininessBuffer(int i, int i2) {
            PGL.updateFloatBuffer(this.polyShininessBuffer, this.polyShininess, i, i2);
        }

        /* access modifiers changed from: protected */
        public void updatePolySpecularBuffer() {
            updatePolySpecularBuffer(0, this.polyVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updatePolySpecularBuffer(int i, int i2) {
            PGL.updateIntBuffer(this.polySpecularBuffer, this.polySpecular, i, i2);
        }

        /* access modifiers changed from: protected */
        public void updatePolyTexCoordsBuffer() {
            updatePolyTexCoordsBuffer(0, this.polyVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updatePolyTexCoordsBuffer(int i, int i2) {
            PGL.updateFloatBuffer(this.polyTexCoordsBuffer, this.polyTexCoords, i * 2, i2 * 2);
        }

        /* access modifiers changed from: protected */
        public void updatePolyVerticesBuffer() {
            updatePolyVerticesBuffer(0, this.polyVertexCount);
        }

        /* access modifiers changed from: protected */
        public void updatePolyVerticesBuffer(int i, int i2) {
            PGL.updateFloatBuffer(this.polyVerticesBuffer, this.polyVertices, i * 4, i2 * 4);
        }
    }

    protected static class Tessellator {
        boolean accurate2DStrokes = true;
        int beginPath;
        TessellatorCallback callback;
        int dupCount;
        int[] dupIndices;
        boolean fill;
        int firstLineIndexCache;
        int firstPointIndexCache;
        int firstPolyIndexCache;
        int firstTexCache;
        int firstTexIndex;
        PGL.Tessellator gluTess;
        InGeometry in;
        boolean is2D = false;
        boolean is3D = true;
        int lastLineIndexCache;
        int lastPointIndexCache;
        int lastPolyIndexCache;
        PImage newTexImage;
        int[] pathColors;
        int pathVertexCount;
        float[] pathVertices;
        float[] pathWeights;
        protected PGraphicsOpenGL pg;
        PImage prevTexImage;
        int[] rawIndices = new int[512];
        int rawSize;
        boolean stroke;
        int strokeCap;
        int strokeColor;
        int[] strokeColors;
        int strokeJoin;
        float[] strokeVertices;
        float strokeWeight;
        float[] strokeWeights;
        TessGeometry tess;
        TexCache texCache;
        PMatrix transform = null;
        float transformScale;

        protected class TessellatorCallback implements PGL.TessellatorCallback {
            AttributeMap attribs;
            IndexCache cache;
            int cacheIndex;
            boolean calcNormals;
            boolean clampXY;
            int primitive;
            boolean strokeTess;
            int vertCount;
            int vertFirst;
            int vertOffset;

            public TessellatorCallback(AttributeMap attributeMap) {
                this.attribs = attributeMap;
            }

            private void normalize(double[] dArr, int i) {
                double sqrt = Math.sqrt((dArr[i] * dArr[i]) + (dArr[i + 1] * dArr[i + 1]) + (dArr[i + 2] * dArr[i + 2]));
                if (0.0d < sqrt) {
                    dArr[i] = dArr[i] / sqrt;
                    int i2 = i + 1;
                    dArr[i2] = dArr[i2] / sqrt;
                    int i3 = i + 2;
                    dArr[i3] = dArr[i3] / sqrt;
                }
            }

            /* access modifiers changed from: protected */
            public void addIndex(int i) {
                Tessellator.this.tess.polyIndexCheck();
                Tessellator.this.tess.polyIndices[Tessellator.this.tess.polyIndexCount - 1] = (short) (this.vertFirst + i);
            }

            public void begin(int i) {
                this.cacheIndex = this.cache.getLast();
                if (Tessellator.this.firstPolyIndexCache == -1) {
                    Tessellator.this.firstPolyIndexCache = this.cacheIndex;
                }
                if (this.strokeTess && Tessellator.this.firstLineIndexCache == -1) {
                    Tessellator.this.firstLineIndexCache = this.cacheIndex;
                }
                this.vertFirst = this.cache.vertexCount[this.cacheIndex];
                this.vertOffset = this.cache.vertexOffset[this.cacheIndex];
                this.vertCount = 0;
                if (i == PGL.TRIANGLE_FAN) {
                    this.primitive = 11;
                } else if (i == PGL.TRIANGLE_STRIP) {
                    this.primitive = 10;
                } else if (i == PGL.TRIANGLES) {
                    this.primitive = 9;
                }
            }

            /* access modifiers changed from: protected */
            public void calcTriNormal(int i, int i2, int i3) {
                Tessellator.this.tess.calcPolyNormal(this.vertFirst + this.vertOffset + i, this.vertFirst + this.vertOffset + i2, this.vertFirst + this.vertOffset + i3);
            }

            /* JADX WARNING: type inference failed for: r16v0, types: [java.lang.Object[]] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void combine(double[] r13, java.lang.Object[] r14, float[] r15, java.lang.Object[] r16) {
                /*
                    r12 = this;
                    r0 = 0
                    r0 = r14[r0]
                    double[] r0 = (double[]) r0
                    double[] r0 = (double[]) r0
                    int r3 = r0.length
                    double[] r4 = new double[r3]
                    r0 = 0
                    r1 = 0
                    r6 = r13[r1]
                    r4[r0] = r6
                    r0 = 1
                    r1 = 1
                    r6 = r13[r1]
                    r4[r0] = r6
                    r0 = 2
                    r1 = 2
                    r6 = r13[r1]
                    r4[r0] = r6
                    r0 = 3
                    r2 = r0
                L_0x001e:
                    if (r2 >= r3) goto L_0x0044
                    r0 = 0
                    r4[r2] = r0
                    r0 = 0
                    r1 = r0
                L_0x0026:
                    r0 = 4
                    if (r1 >= r0) goto L_0x0040
                    r0 = r14[r1]
                    double[] r0 = (double[]) r0
                    double[] r0 = (double[]) r0
                    if (r0 == 0) goto L_0x003c
                    r6 = r4[r2]
                    r5 = r15[r1]
                    double r8 = (double) r5
                    r10 = r0[r2]
                    double r8 = r8 * r10
                    double r6 = r6 + r8
                    r4[r2] = r6
                L_0x003c:
                    int r0 = r1 + 1
                    r1 = r0
                    goto L_0x0026
                L_0x0040:
                    int r0 = r2 + 1
                    r2 = r0
                    goto L_0x001e
                L_0x0044:
                    r0 = 7
                    r12.normalize(r4, r0)
                    r0 = 25
                    if (r0 >= r3) goto L_0x006f
                    r1 = 25
                    r0 = 0
                L_0x004f:
                    processing.opengl.PGraphicsOpenGL$AttributeMap r2 = r12.attribs
                    int r2 = r2.size()
                    if (r0 >= r2) goto L_0x006f
                    processing.opengl.PGraphicsOpenGL$AttributeMap r2 = r12.attribs
                    processing.opengl.PGraphicsOpenGL$VertexAttribute r2 = r2.get(r0)
                    boolean r3 = r2.isNormal()
                    if (r3 == 0) goto L_0x006b
                    r12.normalize(r4, r1)
                    int r1 = r1 + 3
                L_0x0068:
                    int r0 = r0 + 1
                    goto L_0x004f
                L_0x006b:
                    int r2 = r2.size
                    int r1 = r1 + r2
                    goto L_0x0068
                L_0x006f:
                    r0 = 0
                    r16[r0] = r4
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: processing.opengl.PGraphicsOpenGL.Tessellator.TessellatorCallback.combine(double[], java.lang.Object[], float[], java.lang.Object[]):void");
            }

            public void end() {
                int i = 1;
                int i2 = 0;
                if (PGL.MAX_VERTEX_INDEX1 <= this.vertFirst + this.vertCount) {
                    this.cacheIndex = this.cache.addNew();
                    this.vertFirst = this.cache.vertexCount[this.cacheIndex];
                    this.vertOffset = this.cache.vertexOffset[this.cacheIndex];
                }
                switch (this.primitive) {
                    case 9:
                        int i3 = this.vertCount;
                        for (int i4 = 0; i4 < this.vertCount; i4++) {
                            addIndex(i4);
                        }
                        if (this.calcNormals) {
                            while (i2 < this.vertCount / 3) {
                                calcTriNormal((i2 * 3) + 0, (i2 * 3) + 1, (i2 * 3) + 2);
                                i2++;
                            }
                        }
                        i2 = i3;
                        break;
                    case 10:
                        i2 = (this.vertCount - 2) * 3;
                        while (i < this.vertCount - 1) {
                            if (i % 2 == 0) {
                                addIndex(i + 1);
                                addIndex(i);
                                addIndex(i - 1);
                                if (this.calcNormals) {
                                    calcTriNormal(i + 1, i, i - 1);
                                }
                            } else {
                                addIndex(i - 1);
                                addIndex(i);
                                addIndex(i + 1);
                                if (this.calcNormals) {
                                    calcTriNormal(i - 1, i, i + 1);
                                }
                            }
                            i++;
                        }
                        break;
                    case 11:
                        int i5 = (this.vertCount - 2) * 3;
                        while (i < this.vertCount - 1) {
                            addIndex(0);
                            addIndex(i);
                            addIndex(i + 1);
                            if (this.calcNormals) {
                                calcTriNormal(0, i, i + 1);
                            }
                            i++;
                        }
                        i2 = i5;
                        break;
                }
                this.cache.incCounts(this.cacheIndex, i2, this.vertCount);
                Tessellator.this.lastPolyIndexCache = this.cacheIndex;
                if (this.strokeTess) {
                    Tessellator.this.lastLineIndexCache = this.cacheIndex;
                }
            }

            public void error(int i) {
                PGraphics.showWarning(PGraphicsOpenGL.TESSELLATION_ERROR, Tessellator.this.pg.pgl.tessError(i));
            }

            public void init(boolean z, boolean z2, boolean z3, boolean z4) {
                this.strokeTess = z2;
                this.calcNormals = z3;
                this.clampXY = z4;
                this.cache = Tessellator.this.tess.polyIndexCache;
                if (z) {
                    this.cache.addNew();
                }
            }

            public void vertex(Object obj) {
                if (obj instanceof double[]) {
                    double[] dArr = (double[]) obj;
                    if (dArr.length < 25) {
                        throw new RuntimeException("TessCallback vertex() data is too small");
                    } else if (this.vertCount < PGL.MAX_VERTEX_INDEX1) {
                        Tessellator.this.tess.addPolyVertex(dArr, this.clampXY);
                        this.vertCount++;
                    } else {
                        throw new RuntimeException("The tessellator is generating too many vertices, reduce complexity of shape.");
                    }
                } else {
                    throw new RuntimeException("TessCallback vertex() data not understood");
                }
            }
        }

        /* access modifiers changed from: package-private */
        public int addBevel3D(int i, int i2, int i3, int i4, int i5, short[] sArr, boolean z) {
            boolean z2;
            int i6;
            IndexCache indexCache = this.tess.lineIndexCache;
            int i7 = indexCache.vertexCount[i5];
            if (PGL.MAX_VERTEX_INDEX1 <= i7 + 3) {
                i5 = indexCache.addNew();
                z2 = true;
                i6 = 0;
            } else {
                z2 = false;
                i6 = i7;
            }
            int i8 = indexCache.indexOffset[i5] + indexCache.indexCount[i5];
            int i9 = indexCache.vertexOffset[i5] + indexCache.vertexCount[i5];
            int i10 = z ? this.strokeColor : this.strokeColors[i];
            float transformScale2 = (z ? this.strokeWeight : this.strokeWeights[i]) * transformScale();
            int i11 = i9 + 1;
            this.tess.setLineVertex(i9, this.strokeVertices, i, i10);
            int i12 = i11 + 1;
            this.tess.setLineVertex(i11, this.strokeVertices, i, i2, i10, transformScale2 / 2.0f);
            int i13 = i12 + 1;
            this.tess.setLineVertex(i12, this.strokeVertices, i, i2, i10, (-transformScale2) / 2.0f);
            int i14 = 0;
            if (z2 && -1 < i3 && -1 < i4) {
                int i15 = z ? this.strokeColor : this.strokeColors[i4];
                float transformScale3 = (z ? this.strokeWeight : this.strokeWeights[i4]) * transformScale();
                this.tess.setLineVertex(i13, this.strokeVertices, i4, i3, i15, (-transformScale3) / 2.0f);
                this.tess.setLineVertex(i13 + 1, this.strokeVertices, i4, i3, i15, transformScale3 / 2.0f);
                sArr[0] = (short) (i6 + 3);
                sArr[1] = (short) (i6 + 4);
                i14 = 2;
            }
            int i16 = i8 + 1;
            this.tess.lineIndices[i8] = (short) (i6 + 0);
            int i17 = i16 + 1;
            this.tess.lineIndices[i16] = sArr[0];
            int i18 = i17 + 1;
            this.tess.lineIndices[i17] = (short) (i6 + 1);
            int i19 = i18 + 1;
            this.tess.lineIndices[i18] = (short) (i6 + 0);
            this.tess.lineIndices[i19] = (short) (i6 + 2);
            this.tess.lineIndices[i19 + 1] = sArr[1];
            indexCache.incCounts(i5, 6, i14 + 3);
            return i5;
        }

        /* access modifiers changed from: package-private */
        public void addBezierVertex(int i) {
            int unused = this.pg.curveVertexCount = 0;
            this.pg.bezierInitCheck();
            this.pg.bezierVertexCheck(20, i);
            PMatrix3D access$4700 = this.pg.bezierDrawMatrix;
            int i2 = i - 1;
            float f = this.in.vertices[(i2 * 3) + 0];
            float f2 = this.in.vertices[(i2 * 3) + 1];
            float f3 = this.in.vertices[(i2 * 3) + 2];
            int i3 = 0;
            float f4 = 0.0f;
            if (this.stroke) {
                i3 = this.in.strokeColors[i];
                f4 = this.in.strokeWeights[i];
            }
            double[] collectVertexAttributes = this.fill ? collectVertexAttributes(i) : null;
            float f5 = this.in.vertices[(i * 3) + 0];
            float f6 = this.in.vertices[(i * 3) + 1];
            float f7 = this.in.vertices[(i * 3) + 2];
            float f8 = this.in.vertices[((i + 1) * 3) + 0];
            float f9 = this.in.vertices[((i + 1) * 3) + 1];
            float f10 = this.in.vertices[((i + 1) * 3) + 2];
            float f11 = this.in.vertices[((i + 2) * 3) + 0];
            float f12 = this.in.vertices[((i + 2) * 3) + 1];
            float f13 = this.in.vertices[((i + 2) * 3) + 2];
            float f14 = (access$4700.m10 * f) + (access$4700.m11 * f5) + (access$4700.m12 * f8) + (access$4700.m13 * f11);
            float f15 = (access$4700.m23 * f11) + (access$4700.m20 * f) + (access$4700.m21 * f5) + (access$4700.m22 * f8);
            float f16 = (f5 * access$4700.m31) + (access$4700.m30 * f) + (f8 * access$4700.m32) + (access$4700.m33 * f11);
            float f17 = (access$4700.m10 * f2) + (access$4700.m11 * f6) + (access$4700.m12 * f9) + (access$4700.m13 * f12);
            float f18 = (access$4700.m20 * f2) + (access$4700.m21 * f6) + (access$4700.m22 * f9) + (access$4700.m23 * f12);
            float f19 = (access$4700.m30 * f2) + (f6 * access$4700.m31) + (access$4700.m32 * f9) + (access$4700.m33 * f12);
            float f20 = (access$4700.m10 * f3) + (access$4700.m11 * f7) + (access$4700.m12 * f10) + (access$4700.m13 * f13);
            float f21 = (access$4700.m23 * f13) + (access$4700.m20 * f3) + (access$4700.m21 * f7) + (access$4700.m22 * f10);
            float f22 = (access$4700.m30 * f3) + (f7 * access$4700.m31) + (access$4700.m32 * f10) + (access$4700.m33 * f13);
            float f23 = f3;
            float f24 = f14;
            float f25 = f17;
            int i4 = 0;
            float f26 = f21;
            float f27 = f;
            float f28 = f20;
            float f29 = f15;
            float f30 = f28;
            while (i4 < this.pg.bezierDetail) {
                f27 += f24;
                float f31 = f24 + f29;
                float f32 = f29 + f16;
                float f33 = f2 + f25;
                float f34 = f25 + f18;
                float f35 = f18 + f19;
                float f36 = f23 + f30;
                float f37 = f30 + f26;
                float f38 = f26 + f22;
                if (this.fill) {
                    double[] copyOf = Arrays.copyOf(collectVertexAttributes, collectVertexAttributes.length);
                    copyOf[0] = (double) f27;
                    copyOf[1] = (double) f33;
                    copyOf[2] = (double) f36;
                    this.gluTess.addVertex(copyOf);
                }
                if (this.stroke) {
                    addStrokeVertex(f27, f33, f36, i3, f4);
                }
                i4++;
                f23 = f36;
                f18 = f35;
                f26 = f38;
                f24 = f31;
                f30 = f37;
                f25 = f34;
                f2 = f33;
                f29 = f32;
            }
        }

        /* access modifiers changed from: package-private */
        public void addCurveInitialVertex(int i, float f, float f2, float f3) {
            if (this.fill) {
                double[] collectVertexAttributes = collectVertexAttributes(i);
                collectVertexAttributes[0] = (double) f;
                collectVertexAttributes[1] = (double) f2;
                collectVertexAttributes[2] = (double) f3;
                this.gluTess.addVertex(collectVertexAttributes);
            }
            if (this.stroke) {
                addStrokeVertex(f, f2, f3, this.in.strokeColors[i], this.strokeWeight);
            }
        }

        /* access modifiers changed from: package-private */
        public void addCurveVertex(int i) {
            this.pg.curveVertexCheck(20);
            float[] fArr = this.pg.curveVertices[this.pg.curveVertexCount];
            fArr[0] = this.in.vertices[(i * 3) + 0];
            fArr[1] = this.in.vertices[(i * 3) + 1];
            fArr[2] = this.in.vertices[(i * 3) + 2];
            PGraphicsOpenGL.access$5508(this.pg);
            if (this.pg.curveVertexCount == 3) {
                float[] fArr2 = this.pg.curveVertices[this.pg.curveVertexCount - 2];
                addCurveInitialVertex(i, fArr2[0], fArr2[1], fArr2[2]);
            }
            if (this.pg.curveVertexCount > 3) {
                float[] fArr3 = this.pg.curveVertices[this.pg.curveVertexCount - 4];
                float[] fArr4 = this.pg.curveVertices[this.pg.curveVertexCount - 3];
                float[] fArr5 = this.pg.curveVertices[this.pg.curveVertexCount - 2];
                float[] fArr6 = this.pg.curveVertices[this.pg.curveVertexCount - 1];
                addCurveVertexSegment(i, fArr3[0], fArr3[1], fArr3[2], fArr4[0], fArr4[1], fArr4[2], fArr5[0], fArr5[1], fArr5[2], fArr6[0], fArr6[1], fArr6[2]);
            }
        }

        /* access modifiers changed from: package-private */
        public void addCurveVertexSegment(int i, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
            int i2 = 0;
            float f13 = 0.0f;
            if (this.stroke) {
                i2 = this.in.strokeColors[i];
                f13 = this.in.strokeWeights[i];
            }
            double[] collectVertexAttributes = this.fill ? collectVertexAttributes(i) : null;
            PMatrix3D access$6800 = this.pg.curveDrawMatrix;
            float f14 = (access$6800.m10 * f) + (access$6800.m11 * f4) + (access$6800.m12 * f7) + (access$6800.m13 * f10);
            float f15 = (access$6800.m20 * f) + (access$6800.m21 * f4) + (access$6800.m22 * f7) + (access$6800.m23 * f10);
            float f16 = (access$6800.m30 * f) + (access$6800.m31 * f4) + (access$6800.m32 * f7) + (access$6800.m33 * f10);
            float f17 = (access$6800.m10 * f2) + (access$6800.m11 * f5) + (access$6800.m12 * f8) + (access$6800.m13 * f11);
            float f18 = (access$6800.m20 * f2) + (access$6800.m21 * f5) + (access$6800.m22 * f8) + (access$6800.m23 * f11);
            float f19 = (access$6800.m30 * f2) + (access$6800.m31 * f5) + (access$6800.m32 * f8) + (access$6800.m33 * f11);
            float f20 = (access$6800.m13 * f12) + (access$6800.m10 * f3) + (access$6800.m11 * f6) + (access$6800.m12 * f9);
            float f21 = (access$6800.m20 * f3) + (access$6800.m21 * f6) + (access$6800.m22 * f9) + (access$6800.m23 * f12);
            float f22 = (access$6800.m30 * f3) + (access$6800.m31 * f6) + (access$6800.m32 * f9) + (access$6800.m33 * f12);
            float f23 = f14;
            float f24 = f17;
            int i3 = 0;
            float f25 = f21;
            float f26 = f15;
            float f27 = f20;
            float f28 = f26;
            while (i3 < this.pg.curveDetail) {
                float f29 = f4 + f23;
                float f30 = f23 + f28;
                float f31 = f28 + f16;
                float f32 = f5 + f24;
                float f33 = f24 + f18;
                float f34 = f18 + f19;
                float f35 = f6 + f27;
                float f36 = f27 + f25;
                float f37 = f25 + f22;
                if (this.fill) {
                    double[] copyOf = Arrays.copyOf(collectVertexAttributes, collectVertexAttributes.length);
                    copyOf[0] = (double) f29;
                    copyOf[1] = (double) f32;
                    copyOf[2] = (double) f35;
                    this.gluTess.addVertex(copyOf);
                }
                if (this.stroke) {
                    addStrokeVertex(f29, f32, f35, i2, f13);
                }
                i3++;
                f6 = f35;
                f5 = f32;
                f4 = f29;
                f28 = f31;
                f25 = f37;
                f18 = f34;
                f27 = f36;
                f23 = f30;
                f24 = f33;
            }
        }

        /* access modifiers changed from: package-private */
        public void addDupIndex(int i) {
            int i2 = 0;
            if (this.dupIndices == null) {
                this.dupIndices = new int[16];
            }
            if (this.dupIndices.length == this.dupCount) {
                int[] iArr = new int[(this.dupCount << 1)];
                PApplet.arrayCopy(this.dupIndices, 0, iArr, 0, this.dupCount);
                this.dupIndices = iArr;
            }
            if (i < this.dupIndices[0]) {
                for (int i3 = this.dupCount; i3 > 0; i3--) {
                    this.dupIndices[i3] = this.dupIndices[i3 - 1];
                }
                this.dupIndices[0] = i;
                this.dupCount++;
            } else if (this.dupIndices[this.dupCount - 1] < i) {
                this.dupIndices[this.dupCount] = i;
                this.dupCount++;
            } else {
                while (i2 < this.dupCount - 1 && this.dupIndices[i2] != i) {
                    if (this.dupIndices[i2] >= i || i >= this.dupIndices[i2 + 1]) {
                        i2++;
                    } else {
                        for (int i4 = this.dupCount; i4 > i2 + 1; i4--) {
                            this.dupIndices[i4] = this.dupIndices[i4 - 1];
                        }
                        this.dupIndices[i2 + 1] = i;
                        this.dupCount++;
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public int addLineSegment2D(int i, int i2, int i3, boolean z, boolean z2) {
            int i4;
            float f;
            float f2;
            float f3;
            float f4;
            float f5;
            float f6;
            IndexCache indexCache = this.tess.polyIndexCache;
            int i5 = indexCache.vertexCount[i3];
            if (PGL.MAX_VERTEX_INDEX1 <= i5 + 4) {
                i3 = indexCache.addNew();
                i4 = 0;
            } else {
                i4 = i5;
            }
            int i6 = indexCache.indexOffset[i3] + indexCache.indexCount[i3];
            int i7 = indexCache.vertexCount[i3] + indexCache.vertexOffset[i3];
            int i8 = z ? this.strokeColor : this.strokeColors[i];
            float f7 = z ? this.strokeWeight : this.strokeWeights[i];
            boolean z3 = subPixelStroke(f7) ? false : z2;
            float f8 = this.strokeVertices[(i * 3) + 0];
            float f9 = this.strokeVertices[(i * 3) + 1];
            float f10 = this.strokeVertices[(i2 * 3) + 0];
            float f11 = this.strokeVertices[(i2 * 3) + 1];
            float f12 = f10 - f8;
            float f13 = f11 - f9;
            float sqrt = PApplet.sqrt((f12 * f12) + (f13 * f13));
            if (PGraphicsOpenGL.nonZero(sqrt)) {
                float f14 = (-f13) / sqrt;
                float f15 = f12 / sqrt;
                float min = PApplet.min(0.75f, f7 / 2.0f) * (f12 / sqrt);
                float min2 = (f13 / sqrt) * PApplet.min(0.75f, f7 / 2.0f);
                f = min;
                f2 = f15;
                f3 = f14;
                f4 = min2;
            } else {
                f = 0.0f;
                f2 = 0.0f;
                f3 = 0.0f;
                f4 = 0.0f;
            }
            float f16 = (f3 * f7) / 2.0f;
            float f17 = (f7 * f2) / 2.0f;
            int i9 = i7 + 1;
            this.tess.setPolyVertex(i7, (f8 + f16) - f, (f9 + f17) - f4, 0.0f, i8, z3);
            int i10 = i6 + 1;
            this.tess.polyIndices[i6] = (short) (i4 + 0);
            int i11 = i9 + 1;
            this.tess.setPolyVertex(i9, (f8 - f16) - f, (f9 - f17) - f4, 0.0f, i8, z3);
            int i12 = i10 + 1;
            this.tess.polyIndices[i10] = (short) (i4 + 1);
            if (z3) {
                float f18 = this.tess.polyVertices[((i11 - 2) * 4) + 0];
                float f19 = this.tess.polyVertices[((i11 - 2) * 4) + 1];
                float f20 = this.tess.polyVertices[((i11 - 1) * 4) + 0];
                float f21 = this.tess.polyVertices[((i11 - 1) * 4) + 1];
                if (PGraphicsOpenGL.same(f18, f20) && PGraphicsOpenGL.same(f19, f21)) {
                    unclampLine2D(i11 - 2, (f8 + f16) - f, (f9 + f17) - f4);
                    unclampLine2D(i11 - 1, (f8 - f16) - f, (f9 - f17) - f4);
                }
            }
            if (!z) {
                i8 = this.strokeColors[i2];
                float f22 = this.strokeWeights[i2];
                float f23 = (f3 * f22) / 2.0f;
                float f24 = (f2 * f22) / 2.0f;
                if (subPixelStroke(f22)) {
                    z3 = false;
                    f5 = f24;
                    f6 = f23;
                } else {
                    f5 = f24;
                    f6 = f23;
                }
            } else {
                f5 = f17;
                f6 = f16;
            }
            int i13 = i11 + 1;
            this.tess.setPolyVertex(i11, (f10 - f6) + f, (f11 - f5) + f4, 0.0f, i8, z3);
            int i14 = i12 + 1;
            this.tess.polyIndices[i12] = (short) (i4 + 2);
            int i15 = i14 + 1;
            this.tess.polyIndices[i14] = (short) (i4 + 2);
            int i16 = i15 + 1;
            this.tess.polyIndices[i15] = (short) (i4 + 0);
            int i17 = i13 + 1;
            this.tess.setPolyVertex(i13, f10 + f6 + f, f11 + f5 + f4, 0.0f, i8, z3);
            int i18 = i16 + 1;
            this.tess.polyIndices[i16] = (short) (i4 + 3);
            if (z3) {
                float f25 = this.tess.polyVertices[((i17 - 2) * 4) + 0];
                float f26 = this.tess.polyVertices[((i17 - 2) * 4) + 1];
                float f27 = this.tess.polyVertices[((i17 - 1) * 4) + 0];
                float f28 = this.tess.polyVertices[((i17 - 1) * 4) + 1];
                if (PGraphicsOpenGL.same(f25, f27) && PGraphicsOpenGL.same(f26, f28)) {
                    unclampLine2D(i17 - 2, (f10 - f6) + f, (f11 - f5) + f4);
                    unclampLine2D(i17 - 1, f10 + f6 + f, f11 + f5 + f4);
                }
            }
            indexCache.incCounts(i3, 6, 4);
            return i3;
        }

        /* access modifiers changed from: package-private */
        public int addLineSegment3D(int i, int i2, int i3, int i4, int i5, short[] sArr, boolean z) {
            boolean z2;
            int i6;
            IndexCache indexCache = this.tess.lineIndexCache;
            int i7 = indexCache.vertexCount[i5];
            if (PGL.MAX_VERTEX_INDEX1 <= (sArr != null && -1 < sArr[0] && -1 < sArr[1] ? 1 : 0) + i7 + 4) {
                i5 = indexCache.addNew();
                z2 = true;
                i6 = 0;
            } else {
                z2 = false;
                i6 = i7;
            }
            int i8 = indexCache.indexOffset[i5] + indexCache.indexCount[i5];
            int i9 = indexCache.vertexCount[i5] + indexCache.vertexOffset[i5];
            int i10 = z ? this.strokeColor : this.strokeColors[i];
            float transformScale2 = (z ? this.strokeWeight : this.strokeWeights[i]) * transformScale();
            int i11 = i9 + 1;
            this.tess.setLineVertex(i9, this.strokeVertices, i, i2, i10, transformScale2 / 2.0f);
            int i12 = i8 + 1;
            this.tess.lineIndices[i8] = (short) (i6 + 0);
            int i13 = i11 + 1;
            this.tess.setLineVertex(i11, this.strokeVertices, i, i2, i10, (-transformScale2) / 2.0f);
            int i14 = i12 + 1;
            this.tess.lineIndices[i12] = (short) (i6 + 1);
            int i15 = z ? this.strokeColor : this.strokeColors[i2];
            float transformScale3 = (z ? this.strokeWeight : this.strokeWeights[i2]) * transformScale();
            int i16 = i13 + 1;
            this.tess.setLineVertex(i13, this.strokeVertices, i2, i, i15, (-transformScale3) / 2.0f);
            int i17 = i14 + 1;
            this.tess.lineIndices[i14] = (short) (i6 + 2);
            int i18 = i17 + 1;
            this.tess.lineIndices[i17] = (short) (i6 + 2);
            int i19 = i18 + 1;
            this.tess.lineIndices[i18] = (short) (i6 + 1);
            int i20 = i16 + 1;
            this.tess.setLineVertex(i16, this.strokeVertices, i2, i, i15, transformScale3 / 2.0f);
            int i21 = i19 + 1;
            this.tess.lineIndices[i19] = (short) (i6 + 3);
            indexCache.incCounts(i5, 6, 4);
            if (sArr != null) {
                if (-1 < sArr[0] && -1 < sArr[1]) {
                    if (!z2) {
                        this.tess.setLineVertex(i20, this.strokeVertices, i, i10);
                        int i22 = i21 + 1;
                        this.tess.lineIndices[i21] = (short) (i6 + 4);
                        int i23 = i22 + 1;
                        this.tess.lineIndices[i22] = sArr[0];
                        int i24 = i23 + 1;
                        this.tess.lineIndices[i23] = (short) (i6 + 0);
                        int i25 = i24 + 1;
                        this.tess.lineIndices[i24] = (short) (i6 + 4);
                        this.tess.lineIndices[i25] = sArr[1];
                        this.tess.lineIndices[i25 + 1] = (short) (i6 + 1);
                        indexCache.incCounts(i5, 6, 1);
                    } else if (-1 < i3 && -1 < i4) {
                        int i26 = z ? this.strokeColor : this.strokeColors[i3];
                        float transformScale4 = (z ? this.strokeWeight : this.strokeWeights[i3]) * transformScale();
                        int i27 = i20 + 1;
                        this.tess.setLineVertex(i20, this.strokeVertices, i4, i26);
                        this.tess.setLineVertex(i27, this.strokeVertices, i4, i3, i26, (-transformScale4) / 2.0f);
                        this.tess.setLineVertex(i27 + 1, this.strokeVertices, i4, i3, i26, transformScale4 / 2.0f);
                        int i28 = i21 + 1;
                        this.tess.lineIndices[i21] = (short) (i6 + 4);
                        int i29 = i28 + 1;
                        this.tess.lineIndices[i28] = (short) (i6 + 5);
                        int i30 = i29 + 1;
                        this.tess.lineIndices[i29] = (short) (i6 + 0);
                        int i31 = i30 + 1;
                        this.tess.lineIndices[i30] = (short) (i6 + 4);
                        this.tess.lineIndices[i31] = (short) (i6 + 6);
                        this.tess.lineIndices[i31 + 1] = (short) (i6 + 1);
                        indexCache.incCounts(i5, 6, 3);
                    }
                }
                sArr[0] = (short) (i6 + 2);
                sArr[1] = (short) (i6 + 3);
            }
            return i5;
        }

        /* access modifiers changed from: package-private */
        public void addQuadraticVertex(int i) {
            int unused = this.pg.curveVertexCount = 0;
            this.pg.bezierInitCheck();
            this.pg.bezierVertexCheck(20, i);
            PMatrix3D access$5100 = this.pg.bezierDrawMatrix;
            int i2 = i - 1;
            float f = this.in.vertices[(i2 * 3) + 0];
            float f2 = this.in.vertices[(i2 * 3) + 1];
            float f3 = this.in.vertices[(i2 * 3) + 2];
            int i3 = 0;
            float f4 = 0.0f;
            if (this.stroke) {
                i3 = this.in.strokeColors[i];
                f4 = this.in.strokeWeights[i];
            }
            double[] collectVertexAttributes = this.fill ? collectVertexAttributes(i) : null;
            float f5 = this.in.vertices[(i * 3) + 0];
            float f6 = this.in.vertices[(i * 3) + 1];
            float f7 = this.in.vertices[(i * 3) + 2];
            float f8 = this.in.vertices[((i + 1) * 3) + 0];
            float f9 = this.in.vertices[((i + 1) * 3) + 1];
            float f10 = this.in.vertices[((i + 1) * 3) + 2];
            float f11 = (((f5 - f) * 2.0f) / 3.0f) + f;
            float f12 = f2 + (((f6 - f2) * 2.0f) / 3.0f);
            float f13 = f3 + (((f7 - f3) * 2.0f) / 3.0f);
            float f14 = (((f5 - f8) * 2.0f) / 3.0f) + f8;
            float f15 = (((f6 - f9) * 2.0f) / 3.0f) + f9;
            float f16 = f10 + (((f7 - f10) * 2.0f) / 3.0f);
            float f17 = (access$5100.m10 * f) + (access$5100.m11 * f11) + (access$5100.m12 * f14) + (access$5100.m13 * f8);
            float f18 = (access$5100.m23 * f8) + (access$5100.m20 * f) + (access$5100.m21 * f11) + (access$5100.m22 * f14);
            float f19 = (f14 * access$5100.m32) + (access$5100.m30 * f) + (f11 * access$5100.m31) + (access$5100.m33 * f8);
            float f20 = (access$5100.m10 * f2) + (access$5100.m11 * f12) + (access$5100.m12 * f15) + (access$5100.m13 * f9);
            float f21 = (access$5100.m20 * f2) + (access$5100.m21 * f12) + (access$5100.m22 * f15) + (access$5100.m23 * f9);
            float f22 = (access$5100.m30 * f2) + (access$5100.m31 * f12) + (f15 * access$5100.m32) + (access$5100.m33 * f9);
            float f23 = (access$5100.m10 * f3) + (access$5100.m11 * f13) + (access$5100.m12 * f16) + (access$5100.m13 * f10);
            float f24 = (access$5100.m23 * f10) + (access$5100.m20 * f3) + (access$5100.m21 * f13) + (access$5100.m22 * f16);
            float f25 = (access$5100.m30 * f3) + (access$5100.m31 * f13) + (access$5100.m32 * f16) + (access$5100.m33 * f10);
            float f26 = f3;
            float f27 = f17;
            float f28 = f20;
            int i4 = 0;
            float f29 = f24;
            float f30 = f;
            float f31 = f23;
            float f32 = f18;
            float f33 = f31;
            while (i4 < this.pg.bezierDetail) {
                f30 += f27;
                float f34 = f27 + f32;
                float f35 = f32 + f19;
                float f36 = f2 + f28;
                float f37 = f28 + f21;
                float f38 = f21 + f22;
                float f39 = f26 + f33;
                float f40 = f33 + f29;
                float f41 = f29 + f25;
                if (this.fill) {
                    double[] copyOf = Arrays.copyOf(collectVertexAttributes, collectVertexAttributes.length);
                    copyOf[0] = (double) f30;
                    copyOf[1] = (double) f36;
                    copyOf[2] = (double) f39;
                    this.gluTess.addVertex(copyOf);
                }
                if (this.stroke) {
                    addStrokeVertex(f30, f36, f39, i3, f4);
                }
                i4++;
                f26 = f39;
                f21 = f38;
                f29 = f41;
                f27 = f34;
                f33 = f40;
                f28 = f37;
                f2 = f36;
                f32 = f35;
            }
        }

        /* access modifiers changed from: package-private */
        public void addStrokeVertex(float f, float f2, float f3, int i, float f4) {
            int i2 = this.pathVertexCount;
            if (this.beginPath + 1 < i2) {
                this.in.addEdge(i2 - 2, i2 - 1, this.beginPath == i2 + -2, false);
            }
            if (this.pathVertexCount == this.pathVertices.length / 3) {
                int i3 = this.pathVertexCount << 1;
                float[] fArr = new float[(i3 * 3)];
                PApplet.arrayCopy(this.pathVertices, 0, fArr, 0, this.pathVertexCount * 3);
                this.pathVertices = fArr;
                int[] iArr = new int[i3];
                PApplet.arrayCopy(this.pathColors, 0, iArr, 0, this.pathVertexCount);
                this.pathColors = iArr;
                float[] fArr2 = new float[i3];
                PApplet.arrayCopy(this.pathWeights, 0, fArr2, 0, this.pathVertexCount);
                this.pathWeights = fArr2;
            }
            this.pathVertices[(i2 * 3) + 0] = f;
            this.pathVertices[(i2 * 3) + 1] = f2;
            this.pathVertices[(i2 * 3) + 2] = f3;
            this.pathColors[i2] = i;
            this.pathWeights[i2] = f4;
            this.pathVertexCount++;
        }

        /* access modifiers changed from: package-private */
        public void addVertex(int i) {
            int unused = this.pg.curveVertexCount = 0;
            float f = this.in.vertices[(i * 3) + 0];
            float f2 = this.in.vertices[(i * 3) + 1];
            float f3 = this.in.vertices[(i * 3) + 2];
            if (this.fill) {
                double[] collectVertexAttributes = collectVertexAttributes(i);
                collectVertexAttributes[0] = (double) f;
                collectVertexAttributes[1] = (double) f2;
                collectVertexAttributes[2] = (double) f3;
                this.gluTess.addVertex(collectVertexAttributes);
            }
            if (this.stroke) {
                addStrokeVertex(f, f2, f3, this.in.strokeColors[i], this.in.strokeWeights[i]);
            }
        }

        /* access modifiers changed from: package-private */
        public void beginNoTex() {
            this.newTexImage = null;
            setFirstTexIndex(this.tess.polyIndexCount, this.tess.polyIndexCache.size - 1);
        }

        /* access modifiers changed from: package-private */
        public void beginPolygonStroke() {
            this.pathVertexCount = 0;
            if (this.pathVertices == null) {
                this.pathVertices = new float[(PGL.DEFAULT_IN_VERTICES * 3)];
                this.pathColors = new int[PGL.DEFAULT_IN_VERTICES];
                this.pathWeights = new float[PGL.DEFAULT_IN_VERTICES];
            }
        }

        /* access modifiers changed from: package-private */
        public void beginStrokePath() {
            this.beginPath = this.pathVertexCount;
        }

        /* access modifiers changed from: package-private */
        public void beginTex() {
            setFirstTexIndex(this.tess.polyIndexCount, this.tess.polyIndexCache.size - 1);
        }

        /* access modifiers changed from: package-private */
        public boolean clamp2D() {
            return this.is2D && this.tess.renderMode == 0 && PGraphicsOpenGL.zero(this.pg.modelview.m01) && PGraphicsOpenGL.zero(this.pg.modelview.m10);
        }

        /* access modifiers changed from: package-private */
        public boolean clampEdges2D() {
            boolean clamp2D = clamp2D();
            if (!clamp2D) {
                return clamp2D;
            }
            boolean z = clamp2D;
            for (int i = 0; i <= this.in.edgeCount - 1; i++) {
                int[] iArr = this.in.edges[i];
                if (iArr[2] != -1 && !(z = segmentIsAxisAligned(this.strokeVertices, iArr[0], iArr[1]))) {
                    return z;
                }
            }
            return z;
        }

        /* access modifiers changed from: package-private */
        public boolean clampLineLoop2D(int i) {
            boolean clamp2D = clamp2D();
            if (clamp2D) {
                for (int i2 = 0; i2 < i; i2++) {
                    clamp2D = segmentIsAxisAligned(0, i2 + 1);
                    if (!clamp2D) {
                        break;
                    }
                }
            }
            return clamp2D;
        }

        /* access modifiers changed from: package-private */
        public boolean clampLinePath() {
            return clamp2D() && this.strokeCap == 4 && this.strokeJoin == 32 && !subPixelStroke(this.strokeWeight);
        }

        /* access modifiers changed from: package-private */
        public boolean clampLineStrip2D(int i) {
            boolean clamp2D = clamp2D();
            if (clamp2D) {
                for (int i2 = 0; i2 < i; i2++) {
                    clamp2D = segmentIsAxisAligned(0, i2 + 1);
                    if (!clamp2D) {
                        break;
                    }
                }
            }
            return clamp2D;
        }

        /* access modifiers changed from: package-private */
        public boolean clampLines2D(int i) {
            boolean clamp2D = clamp2D();
            if (clamp2D) {
                for (int i2 = 0; i2 < i; i2++) {
                    clamp2D = segmentIsAxisAligned((i2 * 2) + 0, (i2 * 2) + 1);
                    if (!clamp2D) {
                        break;
                    }
                }
            }
            return clamp2D;
        }

        /* access modifiers changed from: package-private */
        public boolean clampPolygon() {
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean clampQuadStrip(int i) {
            boolean clamp2D = clamp2D();
            if (clamp2D) {
                for (int i2 = 1; i2 < i + 1; i2++) {
                    int i3 = ((i2 - 1) * 2) + 1;
                    int i4 = (i2 * 2) + 1;
                    clamp2D = segmentIsAxisAligned((i2 + -1) * 2, i3) && segmentIsAxisAligned(i3, i4) && segmentIsAxisAligned(i4, i2 * 2);
                    if (!clamp2D) {
                        break;
                    }
                }
            }
            return clamp2D;
        }

        /* access modifiers changed from: package-private */
        public boolean clampQuads(int i) {
            boolean clamp2D = clamp2D();
            if (clamp2D) {
                for (int i2 = 0; i2 < i; i2++) {
                    int i3 = (i2 * 4) + 1;
                    int i4 = (i2 * 4) + 2;
                    clamp2D = segmentIsAxisAligned((i2 * 4) + 0, i3) && segmentIsAxisAligned(i3, i4) && segmentIsAxisAligned(i4, (i2 * 4) + 3);
                    if (!clamp2D) {
                        break;
                    }
                }
            }
            return clamp2D;
        }

        /* access modifiers changed from: package-private */
        public boolean clampSquarePoints2D() {
            return clamp2D();
        }

        /* access modifiers changed from: package-private */
        public boolean clampTriangleFan() {
            boolean clamp2D = clamp2D();
            if (!clamp2D) {
                return clamp2D;
            }
            boolean z = clamp2D;
            for (int i = 1; i < this.in.vertexCount - 1; i++) {
                int i2 = i + 1;
                int i3 = segmentIsAxisAligned(0, i) ? 1 : 0;
                if (segmentIsAxisAligned(0, i2)) {
                    i3++;
                }
                if (segmentIsAxisAligned(i, i2)) {
                    i3++;
                }
                z = 1 < i3;
                if (!z) {
                    return z;
                }
            }
            return z;
        }

        /* access modifiers changed from: package-private */
        public boolean clampTriangleStrip() {
            int i;
            int i2;
            boolean clamp2D = clamp2D();
            if (!clamp2D) {
                return clamp2D;
            }
            boolean z = clamp2D;
            for (int i3 = 1; i3 < this.in.vertexCount - 1; i3++) {
                if (i3 % 2 == 0) {
                    i = i3 - 1;
                    i2 = i3 + 1;
                } else {
                    i = i3 + 1;
                    i2 = i3 - 1;
                }
                int i4 = segmentIsAxisAligned(i3, i) ? 1 : 0;
                if (segmentIsAxisAligned(i3, i2)) {
                    i4++;
                }
                if (segmentIsAxisAligned(i, i2)) {
                    i4++;
                }
                z = 1 < i4;
                if (!z) {
                    return z;
                }
            }
            return z;
        }

        /* access modifiers changed from: package-private */
        public boolean clampTriangles() {
            boolean clamp2D = clamp2D();
            if (clamp2D) {
                int i = this.in.vertexCount / 3;
                for (int i2 = 0; i2 < i; i2++) {
                    int i3 = (i2 * 3) + 0;
                    int i4 = (i2 * 3) + 1;
                    int i5 = (i2 * 3) + 2;
                    int i6 = segmentIsAxisAligned(i3, i4) ? 1 : 0;
                    if (segmentIsAxisAligned(i3, i5)) {
                        i6++;
                    }
                    if (segmentIsAxisAligned(i4, i5)) {
                        i6++;
                    }
                    clamp2D = 1 < i6;
                    if (!clamp2D) {
                        break;
                    }
                }
            }
            return clamp2D;
        }

        /* access modifiers changed from: package-private */
        public boolean clampTriangles(int[] iArr) {
            boolean clamp2D = clamp2D();
            if (clamp2D) {
                int length = iArr.length;
                for (int i = 0; i < length; i++) {
                    int i2 = iArr[(i * 3) + 0];
                    int i3 = iArr[(i * 3) + 1];
                    int i4 = iArr[(i * 3) + 2];
                    int i5 = segmentIsAxisAligned(i2, i3) ? 1 : 0;
                    if (segmentIsAxisAligned(i2, i4)) {
                        i5++;
                    }
                    if (segmentIsAxisAligned(i3, i4)) {
                        i5++;
                    }
                    clamp2D = 1 < i5;
                    if (!clamp2D) {
                        break;
                    }
                }
            }
            return clamp2D;
        }

        /* access modifiers changed from: package-private */
        public double[] collectVertexAttributes(int i) {
            double[] attribVector = this.in.getAttribVector(i);
            double[] dArr = new double[(attribVector.length + 25)];
            int i2 = this.in.colors[i];
            dArr[3] = (double) ((i2 >> 24) & 255);
            dArr[4] = (double) ((i2 >> 16) & 255);
            dArr[5] = (double) ((i2 >> 8) & 255);
            dArr[6] = (double) ((i2 >> 0) & 255);
            dArr[7] = (double) this.in.normals[(i * 3) + 0];
            dArr[8] = (double) this.in.normals[(i * 3) + 1];
            dArr[9] = (double) this.in.normals[(i * 3) + 2];
            dArr[10] = (double) this.in.texcoords[(i * 2) + 0];
            dArr[11] = (double) this.in.texcoords[(i * 2) + 1];
            int i3 = this.in.ambient[i];
            dArr[12] = (double) ((i3 >> 24) & 255);
            dArr[13] = (double) ((i3 >> 16) & 255);
            dArr[14] = (double) ((i3 >> 8) & 255);
            dArr[15] = (double) ((i3 >> 0) & 255);
            int i4 = this.in.specular[i];
            dArr[16] = (double) ((i4 >> 24) & 255);
            dArr[17] = (double) ((i4 >> 16) & 255);
            dArr[18] = (double) ((i4 >> 8) & 255);
            dArr[19] = (double) ((i4 >> 0) & 255);
            int i5 = this.in.emissive[i];
            dArr[20] = (double) ((i5 >> 24) & 255);
            dArr[21] = (double) ((i5 >> 16) & 255);
            dArr[22] = (double) ((i5 >> 8) & 255);
            dArr[23] = (double) ((i5 >> 0) & 255);
            dArr[24] = (double) this.in.shininess[i];
            System.arraycopy(attribVector, 0, dArr, 25, attribVector.length);
            return dArr;
        }

        /* access modifiers changed from: package-private */
        public int dupIndexPos(int i) {
            for (int i2 = 0; i2 < this.dupCount; i2++) {
                if (this.dupIndices[i2] == i) {
                    return i2;
                }
            }
            return 0;
        }

        /* access modifiers changed from: package-private */
        public void endNoTex() {
            setLastTexIndex(this.tess.lastPolyIndex, this.tess.polyIndexCache.size - 1);
        }

        /* access modifiers changed from: package-private */
        public void endPolygonStroke() {
        }

        /* access modifiers changed from: package-private */
        public void endStrokePath(boolean z) {
            boolean z2 = true;
            int i = this.pathVertexCount;
            if (this.beginPath + 1 < i) {
                boolean z3 = this.beginPath == i + -2;
                if (!z3 && z) {
                    z2 = false;
                }
                this.in.addEdge(i - 2, i - 1, z3, z2);
                if (!z2) {
                    this.in.addEdge(i - 1, this.beginPath, false, false);
                    this.in.closeEdge(i - 1, this.beginPath);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void endTex() {
            setLastTexIndex(this.tess.lastPolyIndex, this.tess.polyIndexCache.size - 1);
        }

        /* access modifiers changed from: package-private */
        public void expandRawIndices(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.rawIndices, 0, iArr, 0, this.rawSize);
            this.rawIndices = iArr;
        }

        /* access modifiers changed from: package-private */
        public void initGluTess() {
            if (this.gluTess == null) {
                this.callback = new TessellatorCallback(this.tess.polyAttribs);
                this.gluTess = this.pg.pgl.createTessellator(this.callback);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean noCapsJoins() {
            return this.tess.renderMode == 0 && transformScale() * this.strokeWeight < PGL.MIN_CAPS_JOINS_WEIGHT;
        }

        /* access modifiers changed from: package-private */
        public boolean noCapsJoins(int i) {
            if (this.accurate2DStrokes && PGL.MAX_CAPS_JOINS_LENGTH > i) {
                return noCapsJoins();
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public void resetCurveVertexCount() {
            int unused = this.pg.curveVertexCount = 0;
        }

        /* access modifiers changed from: package-private */
        public boolean segmentIsAxisAligned(int i, int i2) {
            return PGraphicsOpenGL.zero(this.in.vertices[(i * 3) + 0] - this.in.vertices[(i2 * 3) + 0]) || PGraphicsOpenGL.zero(this.in.vertices[(i * 3) + 1] - this.in.vertices[(i2 * 3) + 1]);
        }

        /* access modifiers changed from: package-private */
        public boolean segmentIsAxisAligned(float[] fArr, int i, int i2) {
            return PGraphicsOpenGL.zero(fArr[(i * 3) + 0] - fArr[(i2 * 3) + 0]) || PGraphicsOpenGL.zero(fArr[(i * 3) + 1] - fArr[(i2 * 3) + 1]);
        }

        /* access modifiers changed from: package-private */
        public void set3D(boolean z) {
            if (z) {
                this.is2D = false;
                this.is3D = true;
                return;
            }
            this.is2D = true;
            this.is3D = false;
        }

        /* access modifiers changed from: package-private */
        public void setAccurate2DStrokes(boolean z) {
            this.accurate2DStrokes = z;
        }

        /* access modifiers changed from: package-private */
        public void setFill(boolean z) {
            this.fill = z;
        }

        /* access modifiers changed from: package-private */
        public void setFirstTexIndex(int i, int i2) {
            if (this.texCache != null) {
                this.firstTexIndex = i;
                this.firstTexCache = PApplet.max(0, i2);
            }
        }

        /* access modifiers changed from: package-private */
        public void setInGeometry(InGeometry inGeometry) {
            this.in = inGeometry;
            this.firstPolyIndexCache = -1;
            this.lastPolyIndexCache = -1;
            this.firstLineIndexCache = -1;
            this.lastLineIndexCache = -1;
            this.firstPointIndexCache = -1;
            this.lastPointIndexCache = -1;
        }

        /* access modifiers changed from: package-private */
        public void setLastTexIndex(int i, int i2) {
            if (this.texCache != null) {
                if (this.prevTexImage != this.newTexImage || this.texCache.size == 0) {
                    this.texCache.addTexture(this.newTexImage, this.firstTexIndex, this.firstTexCache, i, i2);
                } else {
                    this.texCache.setLastIndex(i, i2);
                }
                this.prevTexImage = this.newTexImage;
            }
        }

        /* access modifiers changed from: package-private */
        public void setRawSize(int i) {
            int length = this.rawIndices.length;
            if (length < i) {
                expandRawIndices(PGraphicsOpenGL.expandArraySize(length, i));
            }
            this.rawSize = i;
        }

        /* access modifiers changed from: protected */
        public void setRenderer(PGraphicsOpenGL pGraphicsOpenGL) {
            this.pg = pGraphicsOpenGL;
        }

        /* access modifiers changed from: package-private */
        public void setStroke(boolean z) {
            this.stroke = z;
        }

        /* access modifiers changed from: package-private */
        public void setStrokeCap(int i) {
            this.strokeCap = i;
        }

        /* access modifiers changed from: package-private */
        public void setStrokeColor(int i) {
            this.strokeColor = PGL.javaToNativeARGB(i);
        }

        /* access modifiers changed from: package-private */
        public void setStrokeJoin(int i) {
            this.strokeJoin = i;
        }

        /* access modifiers changed from: package-private */
        public void setStrokeWeight(float f) {
            this.strokeWeight = f;
        }

        /* access modifiers changed from: package-private */
        public void setTessGeometry(TessGeometry tessGeometry) {
            this.tess = tessGeometry;
        }

        /* access modifiers changed from: package-private */
        public void setTexCache(TexCache texCache2, PImage pImage) {
            this.texCache = texCache2;
            this.newTexImage = pImage;
        }

        /* access modifiers changed from: package-private */
        public void setTransform(PMatrix pMatrix) {
            this.transform = pMatrix;
            this.transformScale = -1.0f;
        }

        /* access modifiers changed from: package-private */
        public void splitRawIndices(boolean z) {
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            this.tess.polyIndexCheck(this.rawSize);
            int i6 = this.tess.firstPolyIndex;
            int i7 = 0;
            int i8 = 0;
            this.dupCount = 0;
            IndexCache indexCache = this.tess.polyIndexCache;
            int addNew = this.in.renderMode == 1 ? indexCache.addNew() : indexCache.getLast();
            this.firstPolyIndexCache = addNew;
            int i9 = this.rawSize / 3;
            int i10 = 0;
            int i11 = -1;
            int i12 = 0;
            int i13 = 0;
            while (i10 < i9) {
                if (addNew == -1) {
                    addNew = indexCache.addNew();
                }
                int i14 = this.rawIndices[(i10 * 3) + 0];
                int i15 = this.rawIndices[(i10 * 3) + 1];
                int i16 = this.rawIndices[(i10 * 3) + 2];
                int i17 = i14 - i8;
                int i18 = i15 - i8;
                int i19 = i16 - i8;
                int i20 = indexCache.vertexCount[addNew];
                if (i17 < 0) {
                    addDupIndex(i17);
                    i = i17;
                } else {
                    i = i17 + i20;
                }
                if (i18 < 0) {
                    addDupIndex(i18);
                } else {
                    i18 += i20;
                }
                if (i19 < 0) {
                    addDupIndex(i19);
                } else {
                    i19 += i20;
                }
                this.tess.polyIndices[(i10 * 3) + i6 + 0] = (short) i;
                this.tess.polyIndices[(i10 * 3) + i6 + 1] = (short) i18;
                this.tess.polyIndices[(i10 * 3) + i6 + 2] = (short) i19;
                int i21 = (i10 * 3) + 2;
                i12 = PApplet.max(i12, PApplet.max(i14, i15, i16));
                int min = PApplet.min(i13, PApplet.min(i14, i15, i16));
                int max = PApplet.max(i11, PApplet.max(i, i18, i19));
                if ((PGL.MAX_VERTEX_INDEX1 - 3 > this.dupCount + max || this.dupCount + max >= PGL.MAX_VERTEX_INDEX1) && i10 != i9 - 1) {
                    i5 = max;
                    i4 = i8;
                    i3 = min;
                    i2 = i7;
                } else {
                    int i22 = 0;
                    if (this.dupCount > 0) {
                        for (int i23 = i7; i23 <= i21; i23++) {
                            short s = this.tess.polyIndices[i6 + i23];
                            if (s < 0) {
                                this.tess.polyIndices[i6 + i23] = (short) (dupIndexPos(s) + max + 1);
                            }
                        }
                        if (i8 <= i12) {
                            this.tess.addPolyVertices(this.in, i8, i12, z);
                            i22 = (i12 - i8) + 1;
                        }
                        for (int i24 = 0; i24 < this.dupCount; i24++) {
                            this.tess.addPolyVertex(this.in, this.dupIndices[i24] + i8, z);
                        }
                    } else {
                        this.tess.addPolyVertices(this.in, min, i12, z);
                        i22 = (i12 - min) + 1;
                    }
                    indexCache.incCounts(addNew, (i21 - i7) + 1, i22 + this.dupCount);
                    this.lastPolyIndexCache = addNew;
                    addNew = -1;
                    i5 = -1;
                    i4 = i12 + 1;
                    int i25 = i21 + 1;
                    if (this.dupIndices != null) {
                        Arrays.fill(this.dupIndices, 0, this.dupCount, 0);
                    }
                    this.dupCount = 0;
                    i2 = i25;
                    i3 = i4;
                }
                i10++;
                i11 = i5;
                i13 = i3;
                i8 = i4;
                i7 = i2;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean subPixelStroke(float f) {
            float transformScale2 = transformScale() * f;
            return PApplet.abs(transformScale2 - ((float) ((int) transformScale2))) > 0.0f;
        }

        /* access modifiers changed from: package-private */
        public void tessellateEdges() {
            if (this.stroke && this.in.edgeCount != 0) {
                this.strokeVertices = this.in.vertices;
                this.strokeColors = this.in.strokeColors;
                this.strokeWeights = this.in.strokeWeights;
                if (this.is3D) {
                    tessellateEdges3D();
                } else if (this.is2D) {
                    beginNoTex();
                    tessellateEdges2D();
                    endNoTex();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void tessellateEdges2D() {
            int numEdgeVertices = this.in.getNumEdgeVertices(false);
            if (noCapsJoins(numEdgeVertices)) {
                int numEdgeIndices = this.in.getNumEdgeIndices(false);
                this.tess.polyVertexCheck(numEdgeVertices);
                this.tess.polyIndexCheck(numEdgeIndices);
                int addNew = this.in.renderMode == 1 ? this.tess.polyIndexCache.addNew() : this.tess.polyIndexCache.getLast();
                this.firstLineIndexCache = addNew;
                if (this.firstPolyIndexCache == -1) {
                    this.firstPolyIndexCache = addNew;
                }
                boolean clampEdges2D = clampEdges2D();
                int i = addNew;
                for (int i2 = 0; i2 <= this.in.edgeCount - 1; i2++) {
                    int[] iArr = this.in.edges[i2];
                    if (iArr[2] != -1) {
                        i = addLineSegment2D(iArr[0], iArr[1], i, false, clampEdges2D);
                    }
                }
                this.lastPolyIndexCache = i;
                this.lastLineIndexCache = i;
                return;
            }
            LinePath linePath = new LinePath(1);
            for (int i3 = 0; i3 <= this.in.edgeCount - 1; i3++) {
                int[] iArr2 = this.in.edges[i3];
                int i4 = iArr2[0];
                int i5 = iArr2[1];
                switch (iArr2[2]) {
                    case -1:
                        linePath.closePath();
                        break;
                    case 0:
                        linePath.lineTo(this.strokeVertices[(i5 * 3) + 0], this.strokeVertices[(i5 * 3) + 1], this.strokeColors[i5]);
                        break;
                    case 1:
                        linePath.moveTo(this.strokeVertices[(i4 * 3) + 0], this.strokeVertices[(i4 * 3) + 1], this.strokeColors[i4]);
                        linePath.lineTo(this.strokeVertices[(i5 * 3) + 0], this.strokeVertices[(i5 * 3) + 1], this.strokeColors[i5]);
                        break;
                    case 2:
                        linePath.lineTo(this.strokeVertices[(i5 * 3) + 0], this.strokeVertices[(i5 * 3) + 1], this.strokeColors[i5]);
                        linePath.moveTo(this.strokeVertices[(i5 * 3) + 0], this.strokeVertices[(i5 * 3) + 1], this.strokeColors[i5]);
                        break;
                    case 3:
                        linePath.moveTo(this.strokeVertices[(i4 * 3) + 0], this.strokeVertices[(i4 * 3) + 1], this.strokeColors[i4]);
                        linePath.lineTo(this.strokeVertices[(i5 * 3) + 0], this.strokeVertices[(i5 * 3) + 1], this.strokeColors[i5]);
                        linePath.moveTo(this.strokeVertices[(i5 * 3) + 0], this.strokeVertices[(i5 * 3) + 1], this.strokeColors[i5]);
                        break;
                }
            }
            tessellateLinePath(linePath);
        }

        /* access modifiers changed from: package-private */
        public void tessellateEdges3D() {
            boolean z = !noCapsJoins();
            int numEdgeVertices = this.in.getNumEdgeVertices(z);
            int numEdgeIndices = this.in.getNumEdgeIndices(z);
            int i = this.tess.lineVertexCount;
            int i2 = this.tess.lineIndexCount;
            this.tess.lineVertexCheck(numEdgeVertices);
            this.tess.lineIndexCheck(numEdgeIndices);
            int addNew = this.in.renderMode == 1 ? this.tess.lineIndexCache.addNew() : this.tess.lineIndexCache.getLast();
            this.firstLineIndexCache = addNew;
            int i3 = 0;
            short[] sArr = {-1, -1};
            int i4 = -1;
            int i5 = -1;
            int[] iArr = {0, 0};
            this.tess.lineIndexCache.setCounter(iArr);
            int i6 = 0;
            int i7 = addNew;
            for (int i8 = 0; i8 <= this.in.edgeCount - 1; i8++) {
                int[] iArr2 = this.in.edges[i8];
                int i9 = iArr2[0];
                int i10 = iArr2[1];
                if (z) {
                    i7 = iArr2[2] == -1 ? addBevel3D(i6, i3, i4, i5, i7, sArr, false) : addLineSegment3D(i9, i10, i4, i5, i7, sArr, false);
                } else if (iArr2[2] != -1) {
                    i7 = addLineSegment3D(i9, i10, i4, i5, i7, (short[]) null, false);
                }
                if (iArr2[2] == 1) {
                    i3 = i10;
                    i6 = i9;
                }
                if (iArr2[2] == 2 || iArr2[2] == 3 || iArr2[2] == -1) {
                    sArr[1] = -1;
                    sArr[0] = -1;
                    i5 = -1;
                    i4 = -1;
                } else {
                    i5 = i10;
                    i4 = i9;
                }
            }
            this.tess.lineIndexCache.setCounter((int[]) null);
            this.tess.lineIndexCount = iArr[0] + i2;
            this.tess.lineVertexCount = iArr[1] + i;
            this.lastLineIndexCache = i7;
        }

        /* access modifiers changed from: package-private */
        public void tessellateLineLoop() {
            int i = this.in.vertexCount;
            if (this.stroke && 2 <= i) {
                this.strokeVertices = this.in.vertices;
                this.strokeColors = this.in.strokeColors;
                this.strokeWeights = this.in.strokeWeights;
                updateTex();
                if (this.is3D) {
                    tessellateLineLoop3D(i);
                } else if (this.is2D) {
                    beginNoTex();
                    tessellateLineLoop2D(i);
                    endNoTex();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void tessellateLineLoop2D(int i) {
            int i2 = i * 4;
            int i3 = i * 2 * 3;
            if (noCapsJoins(i2)) {
                this.tess.polyVertexCheck(i2);
                this.tess.polyIndexCheck(i3);
                int addNew = this.in.renderMode == 1 ? this.tess.polyIndexCache.addNew() : this.tess.polyIndexCache.getLast();
                this.firstLineIndexCache = addNew;
                if (this.firstPolyIndexCache == -1) {
                    this.firstPolyIndexCache = addNew;
                }
                boolean clampLineLoop2D = clampLineLoop2D(i);
                int i4 = 0;
                int i5 = 0;
                int i6 = addNew;
                while (i4 < i - 1) {
                    int i7 = i4 + 1;
                    i6 = addLineSegment2D(i5, i7, i6, false, clampLineLoop2D);
                    i4++;
                    i5 = i7;
                }
                int addLineSegment2D = addLineSegment2D(0, this.in.vertexCount - 1, i6, false, clampLineLoop2D);
                this.lastPolyIndexCache = addLineSegment2D;
                this.lastLineIndexCache = addLineSegment2D;
                return;
            }
            LinePath linePath = new LinePath(1);
            linePath.moveTo(this.in.vertices[0], this.in.vertices[1], this.in.strokeColors[0]);
            for (int i8 = 0; i8 < i - 1; i8++) {
                int i9 = i8 + 1;
                linePath.lineTo(this.in.vertices[(i9 * 3) + 0], this.in.vertices[(i9 * 3) + 1], this.in.strokeColors[i9]);
            }
            linePath.closePath();
            tessellateLinePath(linePath);
        }

        /* access modifiers changed from: package-private */
        public void tessellateLineLoop3D(int i) {
            int i2;
            int i3 = noCapsJoins() ? 0 : i;
            int i4 = this.tess.lineVertexCount;
            int i5 = this.tess.lineIndexCount;
            this.tess.lineVertexCheck((i * 4) + (i3 * 3));
            this.tess.lineIndexCheck((i * 2 * 3) + (i3 * 2 * 3));
            int addNew = this.in.renderMode == 1 ? this.tess.lineIndexCache.addNew() : this.tess.lineIndexCache.getLast();
            this.firstLineIndexCache = addNew;
            int i6 = -1;
            short[] sArr = {-1, -1};
            int[] iArr = {0, 0};
            this.tess.lineIndexCache.setCounter(iArr);
            int i7 = 0;
            int i8 = addNew;
            int i9 = 0;
            while (true) {
                i2 = i6;
                if (i7 >= i - 1) {
                    break;
                }
                i6 = i7 + 1;
                i7++;
                i8 = i3 > 0 ? addLineSegment3D(i9, i6, i6 - 2, i6 - 1, i8, sArr, false) : addLineSegment3D(i9, i6, i6 - 2, i6 - 1, i8, (short[]) null, false);
                i9 = i6;
            }
            int addLineSegment3D = addLineSegment3D(this.in.vertexCount - 1, 0, i2 - 2, i2 - 1, i8, sArr, false);
            if (i3 > 0) {
                addLineSegment3D = addBevel3D(0, 1, this.in.vertexCount - 1, 0, addLineSegment3D, sArr, false);
            }
            this.tess.lineIndexCache.setCounter((int[]) null);
            this.tess.lineIndexCount = iArr[0] + i5;
            this.tess.lineVertexCount = iArr[1] + i4;
            this.lastLineIndexCache = addLineSegment3D;
        }

        public void tessellateLinePath(LinePath linePath) {
            initGluTess();
            this.callback.init(this.in.renderMode == 1, true, false, clampLinePath());
            LinePath createStrokedPath = LinePath.createStrokedPath(linePath, this.strokeWeight, this.strokeCap == 2 ? 1 : this.strokeCap == 4 ? 2 : 0, this.strokeJoin == 2 ? 1 : this.strokeJoin == 32 ? 2 : 0);
            this.gluTess.beginPolygon();
            float[] fArr = new float[6];
            LinePath.PathIterator pathIterator = createStrokedPath.getPathIterator();
            switch (pathIterator.getWindingRule()) {
                case 0:
                    this.gluTess.setWindingRule(PGL.TESS_WINDING_ODD);
                    break;
                case 1:
                    this.gluTess.setWindingRule(PGL.TESS_WINDING_NONZERO);
                    break;
            }
            while (!pathIterator.isDone()) {
                switch (pathIterator.currentSegment(fArr)) {
                    case 0:
                        this.gluTess.beginContour();
                        break;
                    case 1:
                        break;
                    case 2:
                        this.gluTess.endContour();
                        continue;
                }
                this.gluTess.addVertex(new double[]{(double) fArr[0], (double) fArr[1], 0.0d, (double) fArr[2], (double) fArr[3], (double) fArr[4], (double) fArr[5], 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d});
                pathIterator.next();
            }
            this.gluTess.endPolygon();
        }

        /* access modifiers changed from: package-private */
        public void tessellateLineStrip() {
            int i = this.in.vertexCount;
            if (this.stroke && 2 <= i) {
                this.strokeVertices = this.in.vertices;
                this.strokeColors = this.in.strokeColors;
                this.strokeWeights = this.in.strokeWeights;
                updateTex();
                int i2 = i - 1;
                if (this.is3D) {
                    tessellateLineStrip3D(i2);
                } else if (this.is2D) {
                    beginNoTex();
                    tessellateLineStrip2D(i2);
                    endNoTex();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void tessellateLineStrip2D(int i) {
            int i2 = i * 4;
            int i3 = i * 2 * 3;
            if (noCapsJoins(i2)) {
                this.tess.polyVertexCheck(i2);
                this.tess.polyIndexCheck(i3);
                int addNew = this.in.renderMode == 1 ? this.tess.polyIndexCache.addNew() : this.tess.polyIndexCache.getLast();
                this.firstLineIndexCache = addNew;
                if (this.firstPolyIndexCache == -1) {
                    this.firstPolyIndexCache = addNew;
                }
                boolean clampLineStrip2D = clampLineStrip2D(i);
                int i4 = 0;
                int i5 = 0;
                int i6 = addNew;
                while (i4 < i) {
                    int i7 = i4 + 1;
                    i6 = addLineSegment2D(i5, i7, i6, false, clampLineStrip2D);
                    i4++;
                    i5 = i7;
                }
                this.lastPolyIndexCache = i6;
                this.lastLineIndexCache = i6;
                return;
            }
            LinePath linePath = new LinePath(1);
            linePath.moveTo(this.in.vertices[0], this.in.vertices[1], this.in.strokeColors[0]);
            for (int i8 = 0; i8 < i; i8++) {
                int i9 = i8 + 1;
                linePath.lineTo(this.in.vertices[(i9 * 3) + 0], this.in.vertices[(i9 * 3) + 1], this.in.strokeColors[i9]);
            }
            tessellateLinePath(linePath);
        }

        /* access modifiers changed from: package-private */
        public void tessellateLineStrip3D(int i) {
            int i2 = noCapsJoins() ? 0 : i - 1;
            int i3 = this.tess.lineVertexCount;
            int i4 = this.tess.lineIndexCount;
            this.tess.lineVertexCheck((i * 4) + (i2 * 3));
            this.tess.lineIndexCheck((i * 2 * 3) + (i2 * 2 * 3));
            int addNew = this.in.renderMode == 1 ? this.tess.lineIndexCache.addNew() : this.tess.lineIndexCache.getLast();
            this.firstLineIndexCache = addNew;
            int i5 = 0;
            short[] sArr = {-1, -1};
            int[] iArr = {0, 0};
            this.tess.lineIndexCache.setCounter(iArr);
            int i6 = 0;
            while (true) {
                int i7 = i6;
                int i8 = addNew;
                int i9 = i5;
                if (i7 < i) {
                    i5 = i7 + 1;
                    addNew = i2 > 0 ? addLineSegment3D(i9, i5, i5 - 2, i5 - 1, i8, sArr, false) : addLineSegment3D(i9, i5, i5 - 2, i5 - 1, i8, (short[]) null, false);
                    i6 = i7 + 1;
                } else {
                    this.tess.lineIndexCache.setCounter((int[]) null);
                    this.tess.lineIndexCount = iArr[0] + i4;
                    this.tess.lineVertexCount = iArr[1] + i3;
                    this.lastLineIndexCache = i8;
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void tessellateLines() {
            int i = this.in.vertexCount;
            if (this.stroke && 2 <= i) {
                this.strokeVertices = this.in.vertices;
                this.strokeColors = this.in.strokeColors;
                this.strokeWeights = this.in.strokeWeights;
                updateTex();
                int i2 = i / 2;
                if (this.is3D) {
                    tessellateLines3D(i2);
                } else if (this.is2D) {
                    beginNoTex();
                    tessellateLines2D(i2);
                    endNoTex();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void tessellateLines2D(int i) {
            int i2 = i * 4;
            int i3 = i * 2 * 3;
            if (noCapsJoins(i2)) {
                this.tess.polyVertexCheck(i2);
                this.tess.polyIndexCheck(i3);
                int addNew = this.in.renderMode == 1 ? this.tess.polyIndexCache.addNew() : this.tess.polyIndexCache.getLast();
                this.firstLineIndexCache = addNew;
                if (this.firstPolyIndexCache == -1) {
                    this.firstPolyIndexCache = addNew;
                }
                boolean clampLines2D = clampLines2D(i);
                int i4 = addNew;
                for (int i5 = 0; i5 < i; i5++) {
                    i4 = addLineSegment2D((i5 * 2) + 0, (i5 * 2) + 1, i4, false, clampLines2D);
                }
                this.lastPolyIndexCache = i4;
                this.lastLineIndexCache = i4;
                return;
            }
            LinePath linePath = new LinePath(1);
            for (int i6 = 0; i6 < i; i6++) {
                int i7 = (i6 * 2) + 0;
                int i8 = (i6 * 2) + 1;
                linePath.moveTo(this.in.vertices[(i7 * 3) + 0], this.in.vertices[(i7 * 3) + 1], this.in.strokeColors[i7]);
                linePath.lineTo(this.in.vertices[(i8 * 3) + 0], this.in.vertices[(i8 * 3) + 1], this.in.strokeColors[i8]);
            }
            tessellateLinePath(linePath);
        }

        /* access modifiers changed from: package-private */
        public void tessellateLines3D(int i) {
            int i2 = this.tess.lineVertexCount;
            int i3 = this.tess.lineIndexCount;
            this.tess.lineVertexCheck(i * 4);
            this.tess.lineIndexCheck(i * 2 * 3);
            int addNew = this.in.renderMode == 1 ? this.tess.lineIndexCache.addNew() : this.tess.lineIndexCache.getLast();
            this.firstLineIndexCache = addNew;
            int[] iArr = {0, 0};
            this.tess.lineIndexCache.setCounter(iArr);
            int i4 = addNew;
            for (int i5 = 0; i5 < i; i5++) {
                int i6 = (i5 * 2) + 0;
                int i7 = (i5 * 2) + 1;
                i4 = addLineSegment3D(i6, i7, i6 - 2, i7 - 1, i4, (short[]) null, false);
            }
            this.tess.lineIndexCache.setCounter((int[]) null);
            this.tess.lineIndexCount = iArr[0] + i3;
            this.tess.lineVertexCount = iArr[1] + i2;
            this.lastLineIndexCache = i4;
        }

        /* access modifiers changed from: package-private */
        public void tessellatePoints() {
            if (this.strokeCap == 2) {
                tessellateRoundPoints();
            } else {
                tessellateSquarePoints();
            }
        }

        /* access modifiers changed from: package-private */
        public void tessellatePolygon(boolean z, boolean z2, boolean z3) {
            boolean z4;
            int i;
            beginTex();
            if (3 <= this.in.vertexCount) {
                this.firstPolyIndexCache = -1;
                initGluTess();
                this.callback.init(this.in.renderMode == 1, false, z3, clampPolygon());
                if (this.fill) {
                    this.gluTess.beginPolygon();
                    if (z) {
                        this.gluTess.setWindingRule(PGL.TESS_WINDING_NONZERO);
                    } else {
                        this.gluTess.setWindingRule(PGL.TESS_WINDING_ODD);
                    }
                    this.gluTess.beginContour();
                }
                if (this.stroke) {
                    beginPolygonStroke();
                    beginStrokePath();
                }
                int i2 = 0;
                int i3 = 0;
                while (i3 < this.in.vertexCount) {
                    if (this.in.codes == null || i2 >= this.in.codeCount) {
                        z4 = false;
                        i = 0;
                    } else {
                        int i4 = i2 + 1;
                        int i5 = this.in.codes[i2];
                        if (i5 != 4 || i4 >= this.in.codeCount) {
                            z4 = false;
                            int i6 = i5;
                            i2 = i4;
                            i = i6;
                        } else {
                            int i7 = i4 + 1;
                            i = this.in.codes[i4];
                            i2 = i7;
                            z4 = true;
                        }
                    }
                    if (z4) {
                        if (this.stroke) {
                            endStrokePath(z2);
                            beginStrokePath();
                        }
                        if (this.fill) {
                            this.gluTess.endContour();
                            this.gluTess.beginContour();
                        }
                    }
                    if (i == 1) {
                        addBezierVertex(i3);
                        i3 += 3;
                    } else if (i == 2) {
                        addQuadraticVertex(i3);
                        i3 += 2;
                    } else if (i == 3) {
                        addCurveVertex(i3);
                        i3++;
                    } else {
                        addVertex(i3);
                        i3++;
                    }
                }
                if (this.stroke) {
                    endStrokePath(z2);
                    endPolygonStroke();
                }
                if (this.fill) {
                    this.gluTess.endContour();
                    this.gluTess.endPolygon();
                }
            }
            endTex();
            if (this.stroke) {
                tessellateStrokePath();
            }
        }

        /* access modifiers changed from: package-private */
        public void tessellateQuadStrip() {
            beginTex();
            int i = (this.in.vertexCount / 2) - 1;
            if (this.fill && 1 <= i) {
                setRawSize(i * 6);
                int i2 = 0;
                boolean clampQuadStrip = clampQuadStrip(i);
                for (int i3 = 1; i3 < i + 1; i3++) {
                    int i4 = ((i3 - 1) * 2) + 1;
                    int i5 = i3 * 2;
                    int i6 = i2 + 1;
                    this.rawIndices[i2] = (i3 - 1) * 2;
                    int i7 = i6 + 1;
                    this.rawIndices[i6] = i4;
                    int i8 = i7 + 1;
                    this.rawIndices[i7] = i5;
                    int i9 = i8 + 1;
                    this.rawIndices[i8] = i4;
                    int i10 = i9 + 1;
                    this.rawIndices[i9] = (i3 * 2) + 1;
                    i2 = i10 + 1;
                    this.rawIndices[i10] = i5;
                }
                splitRawIndices(clampQuadStrip);
            }
            endTex();
            tessellateEdges();
        }

        /* access modifiers changed from: package-private */
        public void tessellateQuads() {
            beginTex();
            int i = this.in.vertexCount / 4;
            if (this.fill && 1 <= i) {
                setRawSize(i * 6);
                boolean clampQuads = clampQuads(i);
                int i2 = 0;
                for (int i3 = 0; i3 < i; i3++) {
                    int i4 = (i3 * 4) + 0;
                    int i5 = (i3 * 4) + 2;
                    int i6 = i2 + 1;
                    this.rawIndices[i2] = i4;
                    int i7 = i6 + 1;
                    this.rawIndices[i6] = (i3 * 4) + 1;
                    int i8 = i7 + 1;
                    this.rawIndices[i7] = i5;
                    int i9 = i8 + 1;
                    this.rawIndices[i8] = i5;
                    int i10 = i9 + 1;
                    this.rawIndices[i9] = (i3 * 4) + 3;
                    i2 = i10 + 1;
                    this.rawIndices[i10] = i4;
                }
                splitRawIndices(clampQuads);
            }
            endTex();
            tessellateEdges();
        }

        /* access modifiers changed from: package-private */
        public void tessellateRoundPoints() {
            int i = this.in.vertexCount;
            if (this.stroke && 1 <= i) {
                int min = PApplet.min((int) PGraphicsOpenGL.MAX_POINT_ACCURACY, PApplet.max(20, (int) ((6.2831855f * this.strokeWeight) / PGraphicsOpenGL.POINT_ACCURACY_FACTOR))) + 1;
                if (PGL.MAX_VERTEX_INDEX1 <= min) {
                    throw new RuntimeException("Error in point tessellation.");
                }
                updateTex();
                int i2 = min * i;
                int i3 = i * (min - 1) * 3;
                if (this.is3D) {
                    tessellateRoundPoints3D(i2, i3, min);
                } else if (this.is2D) {
                    beginNoTex();
                    tessellateRoundPoints2D(i2, i3, min);
                    endNoTex();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void tessellateRoundPoints2D(int i, int i2, int i3) {
            int i4;
            int i5;
            int i6 = i3 - 1;
            this.tess.polyVertexCheck(i);
            this.tess.polyIndexCheck(i2);
            int i7 = this.tess.firstPolyVertex;
            int i8 = this.tess.firstPolyIndex;
            IndexCache indexCache = this.tess.polyIndexCache;
            int addNew = this.in.renderMode == 1 ? indexCache.addNew() : indexCache.getLast();
            this.firstPointIndexCache = addNew;
            if (this.firstPolyIndexCache == -1) {
                this.firstPolyIndexCache = addNew;
            }
            int i9 = 0;
            int i10 = i8;
            while (true) {
                int i11 = i7;
                if (i9 < this.in.vertexCount) {
                    int i12 = indexCache.vertexCount[addNew];
                    if (PGL.MAX_VERTEX_INDEX1 <= i12 + i3) {
                        i5 = indexCache.addNew();
                        i4 = 0;
                    } else {
                        i4 = i12;
                        i5 = addNew;
                    }
                    float f = this.in.vertices[(i9 * 3) + 0];
                    float f2 = this.in.vertices[(i9 * 3) + 1];
                    int i13 = this.in.strokeColors[i9];
                    float f3 = 720.0f / ((float) i6);
                    this.tess.setPolyVertex(i11, f, f2, 0.0f, i13, false);
                    i7 = i11 + 1;
                    float f4 = 0.0f;
                    for (int i14 = 0; i14 < i6; i14++) {
                        this.tess.setPolyVertex(i7, f + (0.5f * PGraphicsOpenGL.cosLUT[(int) f4] * this.strokeWeight), f2 + (0.5f * PGraphicsOpenGL.sinLUT[(int) f4] * this.strokeWeight), 0.0f, i13, false);
                        i7++;
                        f4 = (f4 + f3) % 720.0f;
                    }
                    for (int i15 = 1; i15 < i3 - 1; i15++) {
                        int i16 = i10 + 1;
                        this.tess.polyIndices[i10] = (short) (i4 + 0);
                        int i17 = i16 + 1;
                        this.tess.polyIndices[i16] = (short) (i4 + i15);
                        i10 = i17 + 1;
                        this.tess.polyIndices[i17] = (short) (i4 + i15 + 1);
                    }
                    int i18 = i10 + 1;
                    this.tess.polyIndices[i10] = (short) (i4 + 0);
                    int i19 = i18 + 1;
                    this.tess.polyIndices[i18] = (short) (i4 + 1);
                    this.tess.polyIndices[i19] = (short) ((i4 + i3) - 1);
                    indexCache.incCounts(i5, (i3 - 1) * 3, i3);
                    i9++;
                    i10 = i19 + 1;
                    addNew = i5;
                } else {
                    this.lastPolyIndexCache = addNew;
                    this.lastPointIndexCache = addNew;
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void tessellateRoundPoints3D(int i, int i2, int i3) {
            int i4 = i3 - 1;
            this.tess.pointVertexCheck(i);
            this.tess.pointIndexCheck(i2);
            int i5 = this.tess.firstPointVertex;
            int i6 = this.tess.firstPointVertex;
            int i7 = this.tess.firstPointIndex;
            IndexCache indexCache = this.tess.pointIndexCache;
            int addNew = this.in.renderMode == 1 ? indexCache.addNew() : indexCache.getLast();
            this.firstPointIndexCache = addNew;
            int i8 = addNew;
            for (int i9 = 0; i9 < this.in.vertexCount; i9++) {
                int i10 = indexCache.vertexCount[i8];
                if (PGL.MAX_VERTEX_INDEX1 <= i10 + i3) {
                    i8 = indexCache.addNew();
                    i10 = 0;
                }
                int i11 = 0;
                while (i11 < i3) {
                    this.tess.setPointVertex(i5, this.in, i9);
                    i11++;
                    i5++;
                }
                this.tess.pointOffsets[(i6 * 2) + 0] = 0.0f;
                this.tess.pointOffsets[(i6 * 2) + 1] = 0.0f;
                float f = 720.0f / ((float) i4);
                i6++;
                float f2 = 0.0f;
                int i12 = 0;
                while (i12 < i4) {
                    this.tess.pointOffsets[(i6 * 2) + 0] = 0.5f * PGraphicsOpenGL.cosLUT[(int) f2] * transformScale() * this.strokeWeight;
                    this.tess.pointOffsets[(i6 * 2) + 1] = 0.5f * PGraphicsOpenGL.sinLUT[(int) f2] * transformScale() * this.strokeWeight;
                    f2 = (f2 + f) % 720.0f;
                    i12++;
                    i6++;
                }
                int i13 = i7;
                for (int i14 = 1; i14 < i3 - 1; i14++) {
                    int i15 = i13 + 1;
                    this.tess.pointIndices[i13] = (short) (i10 + 0);
                    int i16 = i15 + 1;
                    this.tess.pointIndices[i15] = (short) (i10 + i14);
                    i13 = i16 + 1;
                    this.tess.pointIndices[i16] = (short) (i10 + i14 + 1);
                }
                int i17 = i13 + 1;
                this.tess.pointIndices[i13] = (short) (i10 + 0);
                int i18 = i17 + 1;
                this.tess.pointIndices[i17] = (short) (i10 + 1);
                i7 = i18 + 1;
                this.tess.pointIndices[i18] = (short) ((i10 + i3) - 1);
                indexCache.incCounts(i8, (i3 - 1) * 3, i3);
            }
            this.lastPointIndexCache = i8;
        }

        /* access modifiers changed from: package-private */
        public void tessellateSquarePoints() {
            int i = this.in.vertexCount;
            if (this.stroke && 1 <= i) {
                updateTex();
                int i2 = i * 5;
                int i3 = i * 12;
                if (this.is3D) {
                    tessellateSquarePoints3D(i2, i3);
                } else if (this.is2D) {
                    beginNoTex();
                    tessellateSquarePoints2D(i2, i3);
                    endNoTex();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void tessellateSquarePoints2D(int i, int i2) {
            int i3;
            int i4;
            this.tess.polyVertexCheck(i);
            this.tess.polyIndexCheck(i2);
            boolean clampSquarePoints2D = clampSquarePoints2D();
            int i5 = this.tess.firstPolyVertex;
            int i6 = this.tess.firstPolyIndex;
            IndexCache indexCache = this.tess.polyIndexCache;
            int addNew = this.in.renderMode == 1 ? indexCache.addNew() : indexCache.getLast();
            this.firstPointIndexCache = addNew;
            if (this.firstPolyIndexCache == -1) {
                this.firstPolyIndexCache = addNew;
            }
            int i7 = 0;
            int i8 = i6;
            while (true) {
                int i9 = i5;
                if (i7 < this.in.vertexCount) {
                    int i10 = indexCache.vertexCount[addNew];
                    if (PGL.MAX_VERTEX_INDEX1 <= i10 + 5) {
                        i4 = indexCache.addNew();
                        i3 = 0;
                    } else {
                        i3 = i10;
                        i4 = addNew;
                    }
                    float f = this.in.vertices[(i7 * 3) + 0];
                    float f2 = this.in.vertices[(i7 * 3) + 1];
                    int i11 = this.in.strokeColors[i7];
                    this.tess.setPolyVertex(i9, f, f2, 0.0f, i11, clampSquarePoints2D);
                    i5 = i9 + 1;
                    for (int i12 = 0; i12 < 4; i12++) {
                        this.tess.setPolyVertex(i5, f + (0.5f * PGraphicsOpenGL.QUAD_POINT_SIGNS[i12][0] * this.strokeWeight), f2 + (0.5f * PGraphicsOpenGL.QUAD_POINT_SIGNS[i12][1] * this.strokeWeight), 0.0f, i11, clampSquarePoints2D);
                        i5++;
                    }
                    for (int i13 = 1; i13 < 4; i13++) {
                        int i14 = i8 + 1;
                        this.tess.polyIndices[i8] = (short) (i3 + 0);
                        int i15 = i14 + 1;
                        this.tess.polyIndices[i14] = (short) (i3 + i13);
                        i8 = i15 + 1;
                        this.tess.polyIndices[i15] = (short) (i3 + i13 + 1);
                    }
                    int i16 = i8 + 1;
                    this.tess.polyIndices[i8] = (short) (i3 + 0);
                    int i17 = i16 + 1;
                    this.tess.polyIndices[i16] = (short) (i3 + 1);
                    this.tess.polyIndices[i17] = (short) ((i3 + 5) - 1);
                    indexCache.incCounts(i4, 12, 5);
                    i7++;
                    i8 = i17 + 1;
                    addNew = i4;
                } else {
                    this.lastPolyIndexCache = addNew;
                    this.lastPointIndexCache = addNew;
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void tessellateSquarePoints3D(int i, int i2) {
            this.tess.pointVertexCheck(i);
            this.tess.pointIndexCheck(i2);
            int i3 = this.tess.firstPointVertex;
            int i4 = this.tess.firstPointVertex;
            int i5 = this.tess.firstPointIndex;
            IndexCache indexCache = this.tess.pointIndexCache;
            int addNew = this.in.renderMode == 1 ? indexCache.addNew() : indexCache.getLast();
            this.firstPointIndexCache = addNew;
            int i6 = addNew;
            for (int i7 = 0; i7 < this.in.vertexCount; i7++) {
                int i8 = indexCache.vertexCount[i6];
                if (PGL.MAX_VERTEX_INDEX1 <= i8 + 5) {
                    i6 = indexCache.addNew();
                    i8 = 0;
                }
                int i9 = 0;
                while (i9 < 5) {
                    this.tess.setPointVertex(i3, this.in, i7);
                    i9++;
                    i3++;
                }
                this.tess.pointOffsets[(i4 * 2) + 0] = 0.0f;
                this.tess.pointOffsets[(i4 * 2) + 1] = 0.0f;
                i4++;
                int i10 = 0;
                while (i10 < 4) {
                    this.tess.pointOffsets[(i4 * 2) + 0] = 0.5f * PGraphicsOpenGL.QUAD_POINT_SIGNS[i10][0] * transformScale() * this.strokeWeight;
                    this.tess.pointOffsets[(i4 * 2) + 1] = 0.5f * PGraphicsOpenGL.QUAD_POINT_SIGNS[i10][1] * transformScale() * this.strokeWeight;
                    i10++;
                    i4++;
                }
                int i11 = i5;
                for (int i12 = 1; i12 < 4; i12++) {
                    int i13 = i11 + 1;
                    this.tess.pointIndices[i11] = (short) (i8 + 0);
                    int i14 = i13 + 1;
                    this.tess.pointIndices[i13] = (short) (i8 + i12);
                    i11 = i14 + 1;
                    this.tess.pointIndices[i14] = (short) (i8 + i12 + 1);
                }
                int i15 = i11 + 1;
                this.tess.pointIndices[i11] = (short) (i8 + 0);
                int i16 = i15 + 1;
                this.tess.pointIndices[i15] = (short) (i8 + 1);
                i5 = i16 + 1;
                this.tess.pointIndices[i16] = (short) ((i8 + 5) - 1);
                indexCache.incCounts(i6, 12, 5);
            }
            this.lastPointIndexCache = i6;
        }

        /* access modifiers changed from: package-private */
        public void tessellateStrokePath() {
            if (this.in.edgeCount != 0) {
                this.strokeVertices = this.pathVertices;
                this.strokeColors = this.pathColors;
                this.strokeWeights = this.pathWeights;
                if (this.is3D) {
                    tessellateEdges3D();
                } else if (this.is2D) {
                    beginNoTex();
                    tessellateEdges2D();
                    endNoTex();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void tessellateTriangleFan() {
            beginTex();
            int i = this.in.vertexCount;
            if (this.fill && 3 <= i) {
                setRawSize((i - 2) * 3);
                boolean clampTriangleFan = clampTriangleFan();
                int i2 = 0;
                for (int i3 = 1; i3 < this.in.vertexCount - 1; i3++) {
                    int i4 = i2 + 1;
                    this.rawIndices[i2] = 0;
                    int i5 = i4 + 1;
                    this.rawIndices[i4] = i3;
                    i2 = i5 + 1;
                    this.rawIndices[i5] = i3 + 1;
                }
                splitRawIndices(clampTriangleFan);
            }
            endTex();
            tessellateEdges();
        }

        /* access modifiers changed from: package-private */
        public void tessellateTriangleStrip() {
            beginTex();
            int i = this.in.vertexCount;
            if (this.fill && 3 <= i) {
                setRawSize((i - 2) * 3);
                int i2 = 0;
                boolean clampTriangleStrip = clampTriangleStrip();
                for (int i3 = 1; i3 < this.in.vertexCount - 1; i3++) {
                    int i4 = i2 + 1;
                    this.rawIndices[i2] = i3;
                    if (i3 % 2 == 0) {
                        int i5 = i4 + 1;
                        this.rawIndices[i4] = i3 - 1;
                        i2 = i5 + 1;
                        this.rawIndices[i5] = i3 + 1;
                    } else {
                        int i6 = i4 + 1;
                        this.rawIndices[i4] = i3 + 1;
                        i2 = i6 + 1;
                        this.rawIndices[i6] = i3 - 1;
                    }
                }
                splitRawIndices(clampTriangleStrip);
            }
            endTex();
            tessellateEdges();
        }

        /* access modifiers changed from: package-private */
        public void tessellateTriangles() {
            int i = 0;
            beginTex();
            int i2 = this.in.vertexCount / 3;
            if (this.fill && 1 <= i2) {
                setRawSize(i2 * 3);
                boolean clampTriangles = clampTriangles();
                int i3 = 0;
                while (i < i2 * 3) {
                    this.rawIndices[i3] = i;
                    i++;
                    i3++;
                }
                splitRawIndices(clampTriangles);
            }
            endTex();
            tessellateEdges();
        }

        /* access modifiers changed from: package-private */
        public void tessellateTriangles(int[] iArr) {
            beginTex();
            int i = this.in.vertexCount;
            if (this.fill && 3 <= i) {
                int length = iArr.length;
                setRawSize(length);
                PApplet.arrayCopy(iArr, this.rawIndices, length);
                splitRawIndices(clampTriangles(iArr));
            }
            endTex();
            tessellateEdges();
        }

        /* access modifiers changed from: package-private */
        public float transformScale() {
            if (-1.0f < this.transformScale) {
                return this.transformScale;
            }
            float matrixScale = PGraphicsOpenGL.matrixScale(this.transform);
            this.transformScale = matrixScale;
            return matrixScale;
        }

        /* access modifiers changed from: package-private */
        public void unclampLine2D(int i, float f, float f2) {
            PMatrix3D pMatrix3D = this.pg.modelview;
            int i2 = i * 4;
            int i3 = i2 + 1;
            this.tess.polyVertices[i2] = (pMatrix3D.m00 * f) + (pMatrix3D.m01 * f2) + pMatrix3D.m03;
            int i4 = i3 + 1;
            this.tess.polyVertices[i3] = pMatrix3D.m13 + (pMatrix3D.m10 * f) + (pMatrix3D.m11 * f2);
        }

        /* access modifiers changed from: package-private */
        public void updateTex() {
            beginTex();
            endTex();
        }
    }

    protected static class TexCache {
        int[] firstCache;
        int[] firstIndex;
        boolean hasTextures;
        int[] lastCache;
        int[] lastIndex;
        PGraphicsOpenGL pg;
        int size;
        PImage[] textures;

        TexCache(PGraphicsOpenGL pGraphicsOpenGL) {
            this.pg = pGraphicsOpenGL;
            allocate();
        }

        /* access modifiers changed from: package-private */
        public void addTexture(PImage pImage, int i, int i2, int i3, int i4) {
            arrayCheck();
            this.textures[this.size] = pImage;
            this.firstIndex[this.size] = i;
            this.lastIndex[this.size] = i3;
            this.firstCache[this.size] = i2;
            this.lastCache[this.size] = i4;
            this.hasTextures = (pImage != null) | this.hasTextures;
            this.size++;
        }

        /* access modifiers changed from: package-private */
        public void allocate() {
            this.textures = new PImage[PGL.DEFAULT_IN_TEXTURES];
            this.firstIndex = new int[PGL.DEFAULT_IN_TEXTURES];
            this.lastIndex = new int[PGL.DEFAULT_IN_TEXTURES];
            this.firstCache = new int[PGL.DEFAULT_IN_TEXTURES];
            this.lastCache = new int[PGL.DEFAULT_IN_TEXTURES];
            this.size = 0;
            this.hasTextures = false;
        }

        /* access modifiers changed from: package-private */
        public void arrayCheck() {
            if (this.size == this.textures.length) {
                int i = this.size << 1;
                expandTextures(i);
                expandFirstIndex(i);
                expandLastIndex(i);
                expandFirstCache(i);
                expandLastCache(i);
            }
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            Arrays.fill(this.textures, 0, this.size, (Object) null);
            this.size = 0;
            this.hasTextures = false;
        }

        /* access modifiers changed from: package-private */
        public boolean containsTexture(PImage pImage) {
            for (int i = 0; i < this.size; i++) {
                if (this.textures[i] == pImage) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public void expandFirstCache(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.firstCache, 0, iArr, 0, this.size);
            this.firstCache = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandFirstIndex(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.firstIndex, 0, iArr, 0, this.size);
            this.firstIndex = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandLastCache(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.lastCache, 0, iArr, 0, this.size);
            this.lastCache = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandLastIndex(int i) {
            int[] iArr = new int[i];
            PApplet.arrayCopy(this.lastIndex, 0, iArr, 0, this.size);
            this.lastIndex = iArr;
        }

        /* access modifiers changed from: package-private */
        public void expandTextures(int i) {
            PImage[] pImageArr = new PImage[i];
            PApplet.arrayCopy(this.textures, 0, pImageArr, 0, this.size);
            this.textures = pImageArr;
        }

        /* access modifiers changed from: package-private */
        public Texture getTexture(int i) {
            PImage pImage = this.textures[i];
            if (pImage != null) {
                return this.pg.getTexture(pImage);
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public PImage getTextureImage(int i) {
            return this.textures[i];
        }

        /* access modifiers changed from: package-private */
        public void setLastIndex(int i, int i2) {
            this.lastIndex[this.size - 1] = i;
            this.lastCache[this.size - 1] = i2;
        }
    }

    protected static class VertexAttribute {
        static final int COLOR = 2;
        static final int NORMAL = 1;
        static final int OTHER = 3;
        static final int POSITION = 0;
        boolean active;
        VertexBuffer buf;
        byte[] bvalues;
        int elementSize;
        int firstModified;
        float[] fvalues;
        int glLoc;
        int[] ivalues;
        int kind;
        int lastModified;
        boolean modified;
        String name;
        PGraphicsOpenGL pg;
        int size;
        int tessSize;
        int type;

        VertexAttribute(PGraphicsOpenGL pGraphicsOpenGL, String str, int i, int i2, int i3) {
            this.pg = pGraphicsOpenGL;
            this.name = str;
            this.kind = i;
            this.type = i2;
            this.size = i3;
            if (i == 0) {
                this.tessSize = 4;
            } else {
                this.tessSize = i3;
            }
            if (i2 == PGL.FLOAT) {
                this.elementSize = PGL.SIZEOF_FLOAT;
                this.fvalues = new float[i3];
            } else if (i2 == PGL.INT) {
                this.elementSize = PGL.SIZEOF_INT;
                this.ivalues = new int[i3];
            } else if (i2 == PGL.BOOL) {
                this.elementSize = PGL.SIZEOF_INT;
                this.bvalues = new byte[i3];
            }
            this.buf = null;
            this.glLoc = -1;
            this.modified = false;
            this.firstModified = PConstants.MAX_INT;
            this.lastModified = Integer.MIN_VALUE;
            this.active = true;
        }

        /* access modifiers changed from: package-private */
        public boolean active(PShader pShader) {
            if (this.active && this.glLoc == -1) {
                this.glLoc = pShader.getAttributeLoc(this.name);
                if (this.glLoc == -1) {
                    this.active = false;
                }
            }
            return this.active;
        }

        /* access modifiers changed from: package-private */
        public void add(byte[] bArr, int i) {
            PApplet.arrayCopy(this.bvalues, 0, bArr, i, this.size);
        }

        /* access modifiers changed from: package-private */
        public void add(float[] fArr, int i) {
            PApplet.arrayCopy(this.fvalues, 0, fArr, i, this.size);
        }

        /* access modifiers changed from: package-private */
        public void add(int[] iArr, int i) {
            PApplet.arrayCopy(this.ivalues, 0, iArr, i, this.size);
        }

        /* access modifiers changed from: package-private */
        public void bind(PGL pgl) {
            pgl.enableVertexAttribArray(this.glLoc);
        }

        /* access modifiers changed from: package-private */
        public boolean bufferCreated() {
            return this.buf != null && this.buf.glId > 0;
        }

        /* access modifiers changed from: package-private */
        public void createBuffer(PGL pgl) {
            this.buf = new VertexBuffer(this.pg, PGL.ARRAY_BUFFER, this.size, this.elementSize, false);
        }

        /* access modifiers changed from: package-private */
        public void deleteBuffer(PGL pgl) {
            if (this.buf.glId != 0) {
                PGraphicsOpenGL.intBuffer.put(0, this.buf.glId);
                if (pgl.threadIsCurrent()) {
                    pgl.deleteBuffers(1, PGraphicsOpenGL.intBuffer);
                }
            }
        }

        public boolean diff(VertexAttribute vertexAttribute) {
            return (this.name.equals(vertexAttribute.name) && this.kind == vertexAttribute.kind && this.type == vertexAttribute.type && this.size == vertexAttribute.size && this.tessSize == vertexAttribute.tessSize && this.elementSize == vertexAttribute.elementSize) ? false : true;
        }

        /* access modifiers changed from: package-private */
        public boolean isBool() {
            return this.type == PGL.BOOL;
        }

        /* access modifiers changed from: package-private */
        public boolean isColor() {
            return this.kind == 2;
        }

        /* access modifiers changed from: package-private */
        public boolean isFloat() {
            return this.type == PGL.FLOAT;
        }

        /* access modifiers changed from: package-private */
        public boolean isInt() {
            return this.type == PGL.INT;
        }

        /* access modifiers changed from: package-private */
        public boolean isNormal() {
            return this.kind == 1;
        }

        /* access modifiers changed from: package-private */
        public boolean isOther() {
            return this.kind == 3;
        }

        /* access modifiers changed from: package-private */
        public boolean isPosition() {
            return this.kind == 0;
        }

        /* access modifiers changed from: package-private */
        public void set(float f, float f2, float f3) {
            this.fvalues[0] = f;
            this.fvalues[1] = f2;
            this.fvalues[2] = f3;
        }

        /* access modifiers changed from: package-private */
        public void set(int i) {
            this.ivalues[0] = i;
        }

        /* access modifiers changed from: package-private */
        public void set(float[] fArr) {
            PApplet.arrayCopy(fArr, 0, this.fvalues, 0, this.size);
        }

        /* access modifiers changed from: package-private */
        public void set(int[] iArr) {
            PApplet.arrayCopy(iArr, 0, this.ivalues, 0, this.size);
        }

        /* access modifiers changed from: package-private */
        public void set(boolean[] zArr) {
            for (int i = 0; i < zArr.length; i++) {
                this.bvalues[i] = (byte) (zArr[i] ? 1 : 0);
            }
        }

        /* access modifiers changed from: package-private */
        public int sizeInBytes(int i) {
            return this.tessSize * i * this.elementSize;
        }

        /* access modifiers changed from: package-private */
        public void unbind(PGL pgl) {
            pgl.disableVertexAttribArray(this.glLoc);
        }
    }

    public PGraphicsOpenGL() {
        if (intBuffer == null) {
            intBuffer = PGL.allocateIntBuffer(2);
            floatBuffer = PGL.allocateFloatBuffer(2);
        }
        this.viewport = PGL.allocateIntBuffer(4);
        this.polyAttribs = newAttributeMap();
        this.inGeo = newInGeometry(this, this.polyAttribs, 0);
        this.tessGeo = newTessGeometry(this, this.polyAttribs, 0);
        this.texCache = newTexCache(this);
        this.projection = new PMatrix3D();
        this.camera = new PMatrix3D();
        this.cameraInv = new PMatrix3D();
        this.modelview = new PMatrix3D();
        this.modelviewInv = new PMatrix3D();
        this.projmodelview = new PMatrix3D();
        this.lightType = new int[PGL.MAX_LIGHTS];
        this.lightPosition = new float[(PGL.MAX_LIGHTS * 4)];
        this.lightNormal = new float[(PGL.MAX_LIGHTS * 3)];
        this.lightAmbient = new float[(PGL.MAX_LIGHTS * 3)];
        this.lightDiffuse = new float[(PGL.MAX_LIGHTS * 3)];
        this.lightSpecular = new float[(PGL.MAX_LIGHTS * 3)];
        this.lightFalloffCoefficients = new float[(PGL.MAX_LIGHTS * 3)];
        this.lightSpotParameters = new float[(PGL.MAX_LIGHTS * 2)];
        this.currentLightSpecular = new float[3];
        this.initialized = false;
    }

    static /* synthetic */ int access$5508(PGraphicsOpenGL pGraphicsOpenGL) {
        int i = pGraphicsOpenGL.curveVertexCount;
        pGraphicsOpenGL.curveVertexCount = i + 1;
        return i;
    }

    protected static void completeAllPixelTransfers() {
        ongoingPixelTransfersIterable.addAll(ongoingPixelTransfers);
        for (AsyncPixelReader completeAllTransfers : ongoingPixelTransfersIterable) {
            completeAllTransfers.completeAllTransfers();
        }
        ongoingPixelTransfersIterable.clear();
    }

    protected static void completeFinishedPixelTransfers() {
        ongoingPixelTransfersIterable.addAll(ongoingPixelTransfers);
        for (AsyncPixelReader next : ongoingPixelTransfersIterable) {
            if (!next.calledThisFrame) {
                next.completeFinishedTransfers();
            }
            next.calledThisFrame = false;
        }
        ongoingPixelTransfersIterable.clear();
    }

    protected static boolean diff(float f, float f2) {
        return PGL.FLOAT_EPS <= Math.abs(f - f2);
    }

    protected static int expandArraySize(int i, int i2) {
        while (i < i2) {
            i <<= 1;
        }
        return i;
    }

    private static void invRotate(PMatrix3D pMatrix3D, float f, float f2, float f3, float f4) {
        float cos = PApplet.cos(-f);
        float sin = PApplet.sin(-f);
        float f5 = 1.0f - cos;
        float f6 = (f5 * f4 * f4) + cos;
        PMatrix3D pMatrix3D2 = pMatrix3D;
        pMatrix3D2.preApply((f5 * f2 * f2) + cos, ((f5 * f2) * f3) - (sin * f4), (f5 * f2 * f4) + (sin * f3), 0.0f, (f5 * f2 * f3) + (sin * f4), (f5 * f3 * f3) + cos, ((f5 * f3) * f4) - (sin * f2), 0.0f, ((f5 * f2) * f4) - (sin * f3), (sin * f2) + (f5 * f3 * f4), f6, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    protected static void invScale(PMatrix3D pMatrix3D, float f, float f2, float f3) {
        pMatrix3D.preApply(1.0f / f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f / f2, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f / f3, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    protected static void invTranslate(PMatrix3D pMatrix3D, float f, float f2, float f3) {
        pMatrix3D.preApply(1.0f, 0.0f, 0.0f, -f, 0.0f, 1.0f, 0.0f, -f2, 0.0f, 0.0f, 1.0f, -f3, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    protected static float matrixScale(PMatrix pMatrix) {
        if (pMatrix == null) {
            return 1.0f;
        }
        if (pMatrix instanceof PMatrix2D) {
            PMatrix2D pMatrix2D = (PMatrix2D) pMatrix;
            return (float) Math.sqrt((double) Math.abs((pMatrix2D.m00 * pMatrix2D.m11) - (pMatrix2D.m01 * pMatrix2D.m10)));
        } else if (!(pMatrix instanceof PMatrix3D)) {
            return 1.0f;
        } else {
            PMatrix3D pMatrix3D = (PMatrix3D) pMatrix;
            return (float) Math.pow((double) Math.abs((pMatrix3D.m00 * ((pMatrix3D.m11 * pMatrix3D.m22) - (pMatrix3D.m12 * pMatrix3D.m21))) + (pMatrix3D.m01 * ((pMatrix3D.m12 * pMatrix3D.m20) - (pMatrix3D.m10 * pMatrix3D.m22))) + (pMatrix3D.m02 * ((pMatrix3D.m10 * pMatrix3D.m21) - (pMatrix3D.m11 * pMatrix3D.m20)))), 0.3333333432674408d);
        }
    }

    protected static AttributeMap newAttributeMap() {
        return new AttributeMap();
    }

    protected static InGeometry newInGeometry(PGraphicsOpenGL pGraphicsOpenGL, AttributeMap attributeMap, int i) {
        return new InGeometry(pGraphicsOpenGL, attributeMap, i);
    }

    protected static TessGeometry newTessGeometry(PGraphicsOpenGL pGraphicsOpenGL, AttributeMap attributeMap, int i) {
        return new TessGeometry(pGraphicsOpenGL, attributeMap, i);
    }

    protected static TexCache newTexCache(PGraphicsOpenGL pGraphicsOpenGL) {
        return new TexCache(pGraphicsOpenGL);
    }

    protected static boolean nonZero(float f) {
        return PGL.FLOAT_EPS <= Math.abs(f);
    }

    protected static boolean same(float f, float f2) {
        return Math.abs(f - f2) < PGL.FLOAT_EPS;
    }

    protected static boolean zero(float f) {
        return Math.abs(f) < PGL.FLOAT_EPS;
    }

    /* access modifiers changed from: protected */
    public Texture addTexture(PImage pImage) {
        return addTexture(pImage, new Texture.Parameters(2, this.textureSampling, getHint(-8), this.textureWrap));
    }

    /* access modifiers changed from: protected */
    public Texture addTexture(PImage pImage, Texture.Parameters parameters) {
        if (pImage.width == 0 || pImage.height == 0) {
            return null;
        }
        if (pImage.parent == null) {
            pImage.parent = this.parent;
        }
        Texture texture2 = new Texture(this, pImage.pixelWidth, pImage.pixelHeight, parameters);
        setCache(pImage, texture2);
        return texture2;
    }

    /* access modifiers changed from: protected */
    public void allocatePixels() {
        updatePixelSize();
        if (this.pixels == null || this.pixels.length != this.pixelWidth * this.pixelHeight) {
            this.pixels = new int[(this.pixelWidth * this.pixelHeight)];
            this.pixelBuffer = PGL.allocateIntBuffer(this.pixels);
            this.loaded = false;
        }
    }

    public void ambientLight(float f, float f2, float f3) {
        ambientLight(f, f2, f3, 0.0f, 0.0f, 0.0f);
    }

    public void ambientLight(float f, float f2, float f3, float f4, float f5, float f6) {
        enableLighting();
        if (this.lightCount == PGL.MAX_LIGHTS) {
            throw new RuntimeException("can only create " + PGL.MAX_LIGHTS + " lights");
        }
        this.lightType[this.lightCount] = 0;
        lightPosition(this.lightCount, f4, f5, f6, false);
        lightNormal(this.lightCount, 0.0f, 0.0f, 0.0f);
        lightAmbient(this.lightCount, f, f2, f3);
        noLightDiffuse(this.lightCount);
        noLightSpecular(this.lightCount);
        noLightSpot(this.lightCount);
        lightFalloff(this.lightCount, this.currentLightFalloffConstant, this.currentLightFalloffLinear, this.currentLightFalloffQuadratic);
        this.lightCount++;
    }

    public void applyMatrix(float f, float f2, float f3, float f4, float f5, float f6) {
        applyMatrixImpl(f, f2, 0.0f, f3, f4, f5, 0.0f, f6, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void applyMatrix(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        applyMatrixImpl(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16);
    }

    public void applyMatrix(PMatrix2D pMatrix2D) {
        applyMatrixImpl(pMatrix2D.m00, pMatrix2D.m01, 0.0f, pMatrix2D.m02, pMatrix2D.m10, pMatrix2D.m11, 0.0f, pMatrix2D.m12, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void applyMatrix(PMatrix3D pMatrix3D) {
        applyMatrixImpl(pMatrix3D.m00, pMatrix3D.m01, pMatrix3D.m02, pMatrix3D.m03, pMatrix3D.m10, pMatrix3D.m11, pMatrix3D.m12, pMatrix3D.m13, pMatrix3D.m20, pMatrix3D.m21, pMatrix3D.m22, pMatrix3D.m23, pMatrix3D.m30, pMatrix3D.m31, pMatrix3D.m32, pMatrix3D.m33);
    }

    /* access modifiers changed from: protected */
    public void applyMatrixImpl(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        this.modelview.apply(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16);
        this.modelviewInv.set((PMatrix) this.modelview);
        this.modelviewInv.invert();
        this.projmodelview.apply(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16);
    }

    public void applyProjection(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        flush();
        this.projection.apply(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16);
        updateProjmodelview();
    }

    public void applyProjection(PMatrix3D pMatrix3D) {
        flush();
        this.projection.apply(pMatrix3D);
        updateProjmodelview();
    }

    /* access modifiers changed from: protected */
    public void arcImpl(float f, float f2, float f3, float f4, float f5, float f6, int i) {
        beginShape(11);
        this.defaultEdges = false;
        this.normalMode = 1;
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addArc(f, f2, f3, f4, f5, f6, this.fill, this.stroke, i);
        endShape();
    }

    public void attrib(String str, float... fArr) {
        VertexAttribute attribImpl = attribImpl(str, 3, PGL.FLOAT, fArr.length);
        if (attribImpl != null) {
            attribImpl.set(fArr);
        }
    }

    public void attrib(String str, int... iArr) {
        VertexAttribute attribImpl = attribImpl(str, 3, PGL.INT, iArr.length);
        if (attribImpl != null) {
            attribImpl.set(iArr);
        }
    }

    public void attrib(String str, boolean... zArr) {
        VertexAttribute attribImpl = attribImpl(str, 3, PGL.BOOL, zArr.length);
        if (attribImpl != null) {
            attribImpl.set(zArr);
        }
    }

    public void attribColor(String str, int i) {
        VertexAttribute attribImpl = attribImpl(str, 2, PGL.INT, 1);
        if (attribImpl != null) {
            attribImpl.set(new int[]{i});
        }
    }

    /* access modifiers changed from: protected */
    public VertexAttribute attribImpl(String str, int i, int i2, int i3) {
        if (4 < i3) {
            PGraphics.showWarning("Vertex attributes cannot have more than 4 values");
            return null;
        }
        VertexAttribute vertexAttribute = (VertexAttribute) this.polyAttribs.get(str);
        if (vertexAttribute == null) {
            vertexAttribute = new VertexAttribute(this, str, i, i2, i3);
            this.polyAttribs.put(str, vertexAttribute);
            this.inGeo.initAttrib(vertexAttribute);
            this.tessGeo.initAttrib(vertexAttribute);
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
        VertexAttribute attribImpl = attribImpl(str, 1, PGL.FLOAT, 3);
        if (attribImpl != null) {
            attribImpl.set(f, f2, f3);
        }
    }

    public void attribPosition(String str, float f, float f2, float f3) {
        VertexAttribute attribImpl = attribImpl(str, 0, PGL.FLOAT, 3);
        if (attribImpl != null) {
            attribImpl.set(f, f2, f3);
        }
    }

    /* access modifiers changed from: protected */
    public void backgroundImpl() {
        flush();
        this.pgl.clearBackground(this.backgroundR, this.backgroundG, this.backgroundB, this.backgroundA, !this.hints[5]);
        this.loaded = false;
    }

    /* access modifiers changed from: protected */
    public void backgroundImpl(PImage pImage) {
        backgroundImpl();
        set(0, 0, pImage);
        this.backgroundA = 1.0f;
        this.loaded = false;
    }

    /* access modifiers changed from: protected */
    public void begin2D() {
    }

    /* access modifiers changed from: protected */
    public void beginBindFramebuffer(int i, int i2) {
    }

    public void beginCamera() {
        if (this.manipulatingCamera) {
            throw new RuntimeException("beginCamera() cannot be called again before endCamera()");
        }
        this.manipulatingCamera = true;
    }

    public void beginContour() {
        if (this.openContour) {
            PGraphics.showWarning(ALREADY_BEGAN_CONTOUR_ERROR);
            return;
        }
        this.openContour = true;
        this.breakShape = true;
    }

    public void beginDraw() {
        if (this.primaryGraphics) {
            if (!this.initialized) {
                initPrimary();
            }
            setCurrentPG(this);
        } else {
            this.pgl.getGL(getPrimaryPGL());
            getPrimaryPG().setCurrentPG(this);
        }
        if (!this.pgl.threadIsCurrent()) {
            PGraphics.showWarning(GL_THREAD_NOT_CURRENT);
            return;
        }
        report("top beginDraw()");
        if (checkGLThread() && !this.drawing) {
            if (!this.primaryGraphics && getPrimaryPG().texCache.containsTexture(this)) {
                getPrimaryPG().flush();
            }
            if (!glParamsRead) {
                getGLParameters();
            }
            setViewport();
            if (this.primaryGraphics) {
                beginOnscreenDraw();
            } else {
                beginOffscreenDraw();
            }
            checkSettings();
            this.drawing = true;
            report("bot beginDraw()");
        }
    }

    /* access modifiers changed from: protected */
    public void beginOffscreenDraw() {
        if (!this.initialized) {
            initOffscreen();
        } else {
            FrameBuffer frameBuffer = this.offscreenFramebuffer;
            FrameBuffer frameBuffer2 = this.multisampleFramebuffer;
            boolean z = frameBuffer != null && frameBuffer.contextIsOutdated();
            boolean z2 = frameBuffer2 != null && frameBuffer2.contextIsOutdated();
            if (z || z2) {
                restartPGL();
                initOffscreen();
            } else {
                swapOffscreenTextures();
            }
        }
        pushFramebuffer();
        if (this.offscreenMultisample) {
            FrameBuffer frameBuffer3 = this.multisampleFramebuffer;
            if (frameBuffer3 != null) {
                setFramebuffer(frameBuffer3);
            }
        } else {
            FrameBuffer frameBuffer4 = this.offscreenFramebuffer;
            if (frameBuffer4 != null) {
                setFramebuffer(frameBuffer4);
            }
        }
        drawPTexture();
        if (this.clip) {
            this.pgl.enable(PGL.SCISSOR_TEST);
            this.pgl.scissor(this.clipRect[0], this.clipRect[1], this.clipRect[2], this.clipRect[3]);
            return;
        }
        this.pgl.disable(PGL.SCISSOR_TEST);
    }

    /* access modifiers changed from: protected */
    public void beginOnscreenDraw() {
        updatePixelSize();
        this.pgl.beginRender();
        if (this.drawFramebuffer == null) {
            this.drawFramebuffer = new FrameBuffer(this, this.pixelWidth, this.pixelHeight, true);
        }
        this.drawFramebuffer.setFBO(this.pgl.getDrawFramebuffer());
        if (this.readFramebuffer == null) {
            this.readFramebuffer = new FrameBuffer(this, this.pixelWidth, this.pixelHeight, true);
        }
        this.readFramebuffer.setFBO(this.pgl.getReadFramebuffer());
        if (this.currentFramebuffer == null) {
            setFramebuffer(this.drawFramebuffer);
        }
        if (this.pgl.isFBOBacked()) {
            this.texture = this.pgl.wrapBackTexture(this.texture);
            this.ptexture = this.pgl.wrapFrontTexture(this.ptexture);
        }
    }

    public PGL beginPGL() {
        flush();
        this.pgl.beginGL();
        return this.pgl;
    }

    /* access modifiers changed from: protected */
    public void beginPixelsOp(int i) {
        FrameBuffer frameBuffer;
        if (!this.primaryGraphics) {
            frameBuffer = this.offscreenFramebuffer;
            FrameBuffer frameBuffer2 = this.multisampleFramebuffer;
            if (i != 1) {
                if (i == 2) {
                    if (!this.offscreenMultisample) {
                        frameBuffer2 = frameBuffer;
                    }
                    frameBuffer = frameBuffer2;
                }
                frameBuffer = null;
            } else if (this.offscreenMultisample) {
                int i2 = PGL.COLOR_BUFFER_BIT;
                if (this.hints[10]) {
                    i2 |= PGL.DEPTH_BUFFER_BIT | PGL.STENCIL_BUFFER_BIT;
                }
                if (!(frameBuffer == null || frameBuffer2 == null)) {
                    frameBuffer2.copy(frameBuffer, i2);
                }
            }
        } else if (i != 1) {
            if (i == 2) {
                frameBuffer = this.drawFramebuffer;
            }
            frameBuffer = null;
        } else if (!this.pgl.isFBOBacked() || !this.pgl.isMultisampled()) {
            frameBuffer = this.drawFramebuffer;
        } else {
            this.pgl.syncBackTexture();
            frameBuffer = this.readFramebuffer;
        }
        if (!(frameBuffer == null || frameBuffer == getCurrentFB())) {
            pushFramebuffer();
            setFramebuffer(frameBuffer);
            this.pixOpChangedFB = true;
        }
        if (i == 1) {
            if (readBufferSupported) {
                this.pgl.readBuffer(getCurrentFB().getDefaultDrawBuffer());
            }
        } else if (i == 2 && drawBufferSupported) {
            this.pgl.drawBuffer(getCurrentFB().getDefaultDrawBuffer());
        }
        this.pixelsOp = i;
    }

    /* access modifiers changed from: protected */
    public void beginReadPixels() {
        beginPixelsOp(1);
    }

    public void beginShape(int i) {
        this.shape = i;
        this.inGeo.clear();
        this.curveVertexCount = 0;
        this.breakShape = false;
        this.defaultEdges = true;
        super.noTexture();
        this.normalMode = 0;
    }

    public void bezierVertex(float f, float f2, float f3, float f4, float f5, float f6) {
        bezierVertexImpl(f, f2, 0.0f, f3, f4, 0.0f, f5, f6, 0.0f);
    }

    public void bezierVertex(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        bezierVertexImpl(f, f2, f3, f4, f5, f6, f7, f8, f9);
    }

    /* access modifiers changed from: protected */
    public void bezierVertexImpl(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        bezierVertexCheck(this.shape, this.inGeo.vertexCount);
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addBezierVertex(f, f2, f3, f4, f5, f6, f7, f8, f9, vertexBreak());
    }

    /* access modifiers changed from: protected */
    public void bindFrontTexture() {
        if (this.primaryGraphics) {
            this.pgl.bindFrontTexture();
            return;
        }
        if (this.ptexture == null) {
            createPTexture();
        }
        this.ptexture.bind();
    }

    /* access modifiers changed from: protected */
    public void blendModeImpl() {
        if (this.blendMode != this.lastBlendMode) {
            flush();
        }
        this.pgl.enable(PGL.BLEND);
        if (this.blendMode == 0) {
            if (blendEqSupported) {
                this.pgl.blendEquation(PGL.FUNC_ADD);
            }
            this.pgl.blendFunc(PGL.ONE, PGL.ZERO);
        } else if (this.blendMode == 1) {
            if (blendEqSupported) {
                this.pgl.blendEquationSeparate(PGL.FUNC_ADD, PGL.FUNC_ADD);
            }
            this.pgl.blendFuncSeparate(PGL.SRC_ALPHA, PGL.ONE_MINUS_SRC_ALPHA, PGL.ONE, PGL.ONE);
        } else if (this.blendMode == 2) {
            if (blendEqSupported) {
                this.pgl.blendEquationSeparate(PGL.FUNC_ADD, PGL.FUNC_ADD);
            }
            this.pgl.blendFuncSeparate(PGL.SRC_ALPHA, PGL.ONE, PGL.ONE, PGL.ONE);
        } else if (this.blendMode == 4) {
            if (blendEqSupported) {
                this.pgl.blendEquationSeparate(PGL.FUNC_REVERSE_SUBTRACT, PGL.FUNC_ADD);
                this.pgl.blendFuncSeparate(PGL.SRC_ALPHA, PGL.ONE, PGL.ONE, PGL.ONE);
            } else {
                PGraphics.showWarning(BLEND_DRIVER_ERROR, "SUBTRACT");
            }
        } else if (this.blendMode == 8) {
            if (blendEqSupported) {
                this.pgl.blendEquationSeparate(PGL.FUNC_MAX, PGL.FUNC_ADD);
                this.pgl.blendFuncSeparate(PGL.ONE, PGL.ONE, PGL.ONE, PGL.ONE);
            } else {
                PGraphics.showWarning(BLEND_DRIVER_ERROR, "LIGHTEST");
            }
        } else if (this.blendMode == 16) {
            if (blendEqSupported) {
                this.pgl.blendEquationSeparate(PGL.FUNC_MIN, PGL.FUNC_ADD);
                this.pgl.blendFuncSeparate(PGL.ONE, PGL.ONE, PGL.ONE, PGL.ONE);
            } else {
                PGraphics.showWarning(BLEND_DRIVER_ERROR, "DARKEST");
            }
        } else if (this.blendMode == 64) {
            if (blendEqSupported) {
                this.pgl.blendEquationSeparate(PGL.FUNC_ADD, PGL.FUNC_ADD);
            }
            this.pgl.blendFuncSeparate(PGL.ONE_MINUS_DST_COLOR, PGL.ONE_MINUS_SRC_COLOR, PGL.ONE, PGL.ONE);
        } else if (this.blendMode == 128) {
            if (blendEqSupported) {
                this.pgl.blendEquationSeparate(PGL.FUNC_ADD, PGL.FUNC_ADD);
            }
            this.pgl.blendFuncSeparate(PGL.ZERO, PGL.SRC_COLOR, PGL.ONE, PGL.ONE);
        } else if (this.blendMode == 256) {
            if (blendEqSupported) {
                this.pgl.blendEquationSeparate(PGL.FUNC_ADD, PGL.FUNC_ADD);
            }
            this.pgl.blendFuncSeparate(PGL.ONE_MINUS_DST_COLOR, PGL.ONE, PGL.ONE, PGL.ONE);
        } else if (this.blendMode == 32) {
            PGraphics.showWarning(BLEND_RENDERER_ERROR, "DIFFERENCE");
        } else if (this.blendMode == 512) {
            PGraphics.showWarning(BLEND_RENDERER_ERROR, "OVERLAY");
        } else if (this.blendMode == 1024) {
            PGraphics.showWarning(BLEND_RENDERER_ERROR, "HARD_LIGHT");
        } else if (this.blendMode == 2048) {
            PGraphics.showWarning(BLEND_RENDERER_ERROR, "SOFT_LIGHT");
        } else if (this.blendMode == 4096) {
            PGraphics.showWarning(BLEND_RENDERER_ERROR, "DODGE");
        } else if (this.blendMode == 8192) {
            PGraphics.showWarning(BLEND_RENDERER_ERROR, "BURN");
        }
        this.lastBlendMode = this.blendMode;
    }

    public void box(float f, float f2, float f3) {
        beginShape(17);
        this.defaultEdges = false;
        this.normalMode = 2;
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.addBox(f, f2, f3, this.fill, this.stroke);
        endShape();
    }

    public void camera() {
        camera(this.cameraX, this.cameraY, this.cameraZ, this.cameraX, this.cameraY, 0.0f, 0.0f, 1.0f, 0.0f);
    }

    public void camera(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        float f10 = f - f4;
        float f11 = f2 - f5;
        float f12 = f3 - f6;
        this.eyeDist = PApplet.sqrt((f10 * f10) + (f11 * f11) + (f12 * f12));
        if (nonZero(this.eyeDist)) {
            f10 /= this.eyeDist;
            f11 /= this.eyeDist;
            f12 /= this.eyeDist;
        }
        float f13 = (f8 * f12) - (f9 * f11);
        float f14 = (f9 * f10) + ((-f7) * f12);
        float f15 = (f7 * f11) - (f8 * f10);
        float f16 = (f11 * f15) - (f12 * f14);
        float f17 = ((-f10) * f15) + (f12 * f13);
        float f18 = (f10 * f14) - (f11 * f13);
        float sqrt = PApplet.sqrt((f13 * f13) + (f14 * f14) + (f15 * f15));
        if (nonZero(sqrt)) {
            f13 /= sqrt;
            f14 /= sqrt;
            f15 /= sqrt;
        }
        float sqrt2 = PApplet.sqrt((f16 * f16) + (f17 * f17) + (f18 * f18));
        if (nonZero(sqrt2)) {
            f16 /= sqrt2;
            f17 /= sqrt2;
            f18 /= sqrt2;
        }
        this.modelview.set(f13, f14, f15, 0.0f, f16, f17, f18, 0.0f, f10, f11, f12, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
        this.modelview.translate(-f, -f2, -f3);
        this.modelviewInv.set((PMatrix) this.modelview);
        this.modelviewInv.invert();
        this.camera.set((PMatrix) this.modelview);
        this.cameraInv.set((PMatrix) this.modelviewInv);
        updateProjmodelview();
    }

    public boolean canDraw() {
        return this.pgl.canDraw();
    }

    /* access modifiers changed from: protected */
    public boolean checkGLThread() {
        if (this.pgl.threadIsCurrent()) {
            return true;
        }
        PGraphics.showWarning(OPENGL_THREAD_ERROR);
        return false;
    }

    /* access modifiers changed from: protected */
    public void checkSettings() {
        super.checkSettings();
        setGLSettings();
    }

    /* access modifiers changed from: protected */
    public void checkTexture(Texture texture2) {
        if (!texture2.colorBuffer() && (texture2.usingMipmaps == this.hints[8] || texture2.currentSampling() != this.textureSampling)) {
            if (this.hints[8]) {
                texture2.usingMipmaps(false, this.textureSampling);
            } else {
                texture2.usingMipmaps(true, this.textureSampling);
            }
        }
        if ((texture2.usingRepeat && this.textureWrap == 0) || (!texture2.usingRepeat && this.textureWrap == 1)) {
            if (this.textureWrap == 0) {
                texture2.usingRepeat(false);
            } else {
                texture2.usingRepeat(true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void clipImpl(float f, float f2, float f3, float f4) {
        flush();
        this.pgl.enable(PGL.SCISSOR_TEST);
        float f5 = f4 - f2;
        this.clipRect[0] = (int) f;
        this.clipRect[1] = (int) ((((float) this.height) - f2) - f5);
        this.clipRect[2] = (int) (f3 - f);
        this.clipRect[3] = (int) f5;
        this.pgl.scissor(this.clipRect[0], this.clipRect[1], this.clipRect[2], this.clipRect[3]);
        this.clip = true;
    }

    public void copy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (this.primaryGraphics) {
            this.pgl.enableFBOLayer();
        }
        loadTexture();
        if (this.filterTexture == null || this.filterTexture.contextIsOutdated()) {
            this.filterTexture = new Texture(this, this.texture.width, this.texture.height, this.texture.getParameters());
            this.filterTexture.invertedY(true);
            this.filterImage = wrapTexture(this.filterTexture);
        }
        this.filterTexture.put(this.texture, i, this.height - (i2 + i4), i3, this.height - i2);
        copy(this.filterImage, i, i2, i3, i4, i5, i6, i7, i8);
    }

    public void copy(PImage pImage, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        boolean z;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        if (!this.drawing) {
            beginDraw();
            z = true;
        } else {
            z = false;
        }
        flush();
        Texture texture2 = getTexture(pImage);
        boolean invertedX = texture2.invertedX();
        boolean invertedY = texture2.invertedY();
        if (invertedX) {
            i10 = i5 + i7;
            i9 = i5;
        } else {
            i9 = i5 + i7;
            i10 = i5;
        }
        int i15 = i + i3;
        if (invertedY) {
            i11 = this.height - (i6 + i8);
            i12 = this.height - i6;
            i14 = texture2.height - (i2 + i4);
            i13 = texture2.height - i2;
        } else {
            i11 = this.height - i6;
            i12 = this.height - (i6 + i8);
            i13 = i2 + i4;
            i14 = i2;
        }
        this.pgl.drawTexture(texture2.glTarget, texture2.glName, texture2.glWidth, texture2.glHeight, 0, 0, this.width, this.height, i, i14, i15, i13, i10, i11, i9, i12);
        if (z) {
            endDraw();
        }
    }

    /* access modifiers changed from: protected */
    public void createLineBuffers() {
        if (!this.lineBuffersCreated || lineBufferContextIsOutdated()) {
            this.lineBuffersContext = this.pgl.getCurrentContext();
            this.bufLineVertex = new VertexBuffer(this, PGL.ARRAY_BUFFER, 3, PGL.SIZEOF_FLOAT);
            this.bufLineColor = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
            this.bufLineAttrib = new VertexBuffer(this, PGL.ARRAY_BUFFER, 4, PGL.SIZEOF_FLOAT);
            this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
            this.bufLineIndex = new VertexBuffer(this, PGL.ELEMENT_ARRAY_BUFFER, 1, PGL.SIZEOF_INDEX, true);
            this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
            this.lineBuffersCreated = true;
        }
    }

    /* access modifiers changed from: protected */
    public PGL createPGL(PGraphicsOpenGL pGraphicsOpenGL) {
        return new PGLES(pGraphicsOpenGL);
    }

    /* access modifiers changed from: protected */
    public void createPTexture() {
        updatePixelSize();
        if (this.texture != null) {
            this.ptexture = new Texture(this, this.pixelWidth, this.pixelHeight, this.texture.getParameters());
            this.ptexture.invertedY(true);
            this.ptexture.colorBuffer(true);
        }
    }

    /* access modifiers changed from: protected */
    public void createPointBuffers() {
        if (!this.pointBuffersCreated || pointBuffersContextIsOutdated()) {
            this.pointBuffersContext = this.pgl.getCurrentContext();
            this.bufPointVertex = new VertexBuffer(this, PGL.ARRAY_BUFFER, 3, PGL.SIZEOF_FLOAT);
            this.bufPointColor = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
            this.bufPointAttrib = new VertexBuffer(this, PGL.ARRAY_BUFFER, 2, PGL.SIZEOF_FLOAT);
            this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
            this.bufPointIndex = new VertexBuffer(this, PGL.ELEMENT_ARRAY_BUFFER, 1, PGL.SIZEOF_INDEX, true);
            this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
            this.pointBuffersCreated = true;
        }
    }

    /* access modifiers changed from: protected */
    public void createPolyBuffers() {
        boolean z;
        if (!this.polyBuffersCreated || polyBuffersContextIsOutdated()) {
            this.polyBuffersContext = this.pgl.getCurrentContext();
            this.bufPolyVertex = new VertexBuffer(this, PGL.ARRAY_BUFFER, 3, PGL.SIZEOF_FLOAT);
            this.bufPolyColor = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
            this.bufPolyNormal = new VertexBuffer(this, PGL.ARRAY_BUFFER, 3, PGL.SIZEOF_FLOAT);
            this.bufPolyTexcoord = new VertexBuffer(this, PGL.ARRAY_BUFFER, 2, PGL.SIZEOF_FLOAT);
            this.bufPolyAmbient = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
            this.bufPolySpecular = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
            this.bufPolyEmissive = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_INT);
            this.bufPolyShininess = new VertexBuffer(this, PGL.ARRAY_BUFFER, 1, PGL.SIZEOF_FLOAT);
            this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
            this.bufPolyIndex = new VertexBuffer(this, PGL.ELEMENT_ARRAY_BUFFER, 1, PGL.SIZEOF_INDEX, true);
            this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
            this.polyBuffersCreated = true;
        }
        boolean z2 = false;
        for (String str : this.polyAttribs.keySet()) {
            VertexAttribute vertexAttribute = (VertexAttribute) this.polyAttribs.get(str);
            if (!vertexAttribute.bufferCreated() || polyBuffersContextIsOutdated()) {
                vertexAttribute.createBuffer(this.pgl);
                z = true;
            } else {
                z = z2;
            }
            z2 = z;
        }
        if (z2) {
            this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        }
    }

    /* access modifiers changed from: protected */
    public PShape createShapeFamily(int i) {
        PShapeOpenGL pShapeOpenGL = new PShapeOpenGL(this, i);
        if (is3D()) {
            pShapeOpenGL.set3D(true);
        }
        return pShapeOpenGL;
    }

    /* access modifiers changed from: protected */
    public PShape createShapePrimitive(int i, float... fArr) {
        PShapeOpenGL pShapeOpenGL = new PShapeOpenGL(this, i, fArr);
        if (is3D()) {
            pShapeOpenGL.set3D(true);
        }
        return pShapeOpenGL;
    }

    public void curveVertex(float f, float f2) {
        curveVertexImpl(f, f2, 0.0f);
    }

    public void curveVertex(float f, float f2, float f3) {
        curveVertexImpl(f, f2, f3);
    }

    /* access modifiers changed from: protected */
    public void curveVertexImpl(float f, float f2, float f3) {
        curveVertexCheck(this.shape);
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addCurveVertex(f, f2, f3, vertexBreak());
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
    public void defaultSettings() {
        super.defaultSettings();
        this.manipulatingCamera = false;
        textureMode(2);
        ambient(255);
        specular(125);
        emissive(0);
        shininess(1.0f);
        this.setAmbient = false;
    }

    /* access modifiers changed from: protected */
    public void deleteSurfaceTextures() {
        if (this.texture != null) {
            this.texture.dispose();
        }
        if (this.ptexture != null) {
            this.ptexture.dispose();
        }
        if (this.filterTexture != null) {
            this.filterTexture.dispose();
        }
    }

    public void directionalLight(float f, float f2, float f3, float f4, float f5, float f6) {
        enableLighting();
        if (this.lightCount == PGL.MAX_LIGHTS) {
            throw new RuntimeException("can only create " + PGL.MAX_LIGHTS + " lights");
        }
        this.lightType[this.lightCount] = 1;
        lightPosition(this.lightCount, 0.0f, 0.0f, 0.0f, true);
        lightNormal(this.lightCount, f4, f5, f6);
        noLightAmbient(this.lightCount);
        lightDiffuse(this.lightCount, f, f2, f3);
        lightSpecular(this.lightCount, this.currentLightSpecular[0], this.currentLightSpecular[1], this.currentLightSpecular[2]);
        noLightSpot(this.lightCount);
        noLightFalloff(this.lightCount);
        this.lightCount++;
    }

    /* access modifiers changed from: protected */
    public void disableLighting() {
        flush();
        this.lights = false;
    }

    public void dispose() {
        if (this.asyncPixelReader != null) {
            this.asyncPixelReader.dispose();
            this.asyncPixelReader = null;
        }
        if (!this.primaryGraphics) {
            deleteSurfaceTextures();
            FrameBuffer frameBuffer = this.offscreenFramebuffer;
            FrameBuffer frameBuffer2 = this.multisampleFramebuffer;
            if (frameBuffer != null) {
                frameBuffer.dispose();
            }
            if (frameBuffer2 != null) {
                frameBuffer2.dispose();
            }
        }
        this.pgl.dispose();
        super.dispose();
    }

    /* access modifiers changed from: protected */
    public void drawPTexture() {
        if (this.ptexture != null) {
            this.pgl.disable(PGL.BLEND);
            this.pgl.drawTexture(this.ptexture.glTarget, this.ptexture.glName, this.ptexture.glWidth, this.ptexture.glHeight, 0, 0, this.width, this.height);
            this.pgl.enable(PGL.BLEND);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:41:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void drawPixels(int r11, int r12, int r13, int r14) {
        /*
            r10 = this;
            r9 = 2
            r0 = 0
            processing.opengl.PGL r1 = r10.pgl
            float r1 = r1.getPixelScale()
            int r8 = (int) r1
            int r1 = r8 * r13
            int r1 = r1 * r8
            int r1 = r1 * r14
            int[] r2 = r10.nativePixels
            if (r2 == 0) goto L_0x0016
            int[] r2 = r10.nativePixels
            int r2 = r2.length
            if (r2 >= r1) goto L_0x0022
        L_0x0016:
            int[] r2 = new int[r1]
            r10.nativePixels = r2
            int[] r2 = r10.nativePixels
            java.nio.IntBuffer r2 = processing.opengl.PGL.allocateIntBuffer((int[]) r2)
            r10.nativePixelBuffer = r2
        L_0x0022:
            if (r11 > 0) goto L_0x002e
            if (r12 > 0) goto L_0x002e
            int r2 = r10.width     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00fb }
            if (r13 < r2) goto L_0x002e
            int r2 = r10.height     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00fb }
            if (r14 >= r2) goto L_0x0050
        L_0x002e:
            int r1 = r10.width     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00fb }
            int r1 = r1 * r12
            int r1 = r1 + r11
            int r2 = r8 * r1
            int r1 = r8 * r12
            r3 = r2
            r2 = r0
        L_0x0038:
            int r4 = r12 + r14
            int r4 = r4 * r8
            if (r1 >= r4) goto L_0x0059
            int[] r4 = r10.pixels     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00fb }
            int[] r5 = r10.nativePixels     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00fb }
            int r6 = r8 * r13
            java.lang.System.arraycopy(r4, r3, r5, r2, r6)     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00fb }
            int r4 = r10.width     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00fb }
            int r4 = r4 * r8
            int r3 = r3 + r4
            int r4 = r8 * r13
            int r2 = r2 + r4
            int r1 = r1 + 1
            goto L_0x0038
        L_0x0050:
            int[] r2 = r10.pixels     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00fb }
            r3 = 0
            int[] r4 = r10.nativePixels     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00fb }
            r5 = 0
            processing.core.PApplet.arrayCopy(r2, r3, r4, r5, r1)     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00fb }
        L_0x0059:
            int[] r1 = r10.nativePixels     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00fb }
            int r2 = r8 * r13
            int r3 = r8 * r14
            processing.opengl.PGL.javaToNativeARGB(r1, r2, r3)     // Catch:{ ArrayIndexOutOfBoundsException -> 0x00fb }
        L_0x0062:
            java.nio.IntBuffer r1 = r10.nativePixelBuffer
            int[] r2 = r10.nativePixels
            processing.opengl.PGL.putIntArray(r1, r2)
            boolean r1 = r10.primaryGraphics
            if (r1 == 0) goto L_0x0078
            processing.opengl.PGL r1 = r10.pgl
            boolean r1 = r1.isFBOBacked()
            if (r1 != 0) goto L_0x0078
            r10.loadTextureImpl(r9, r0)
        L_0x0078:
            boolean r1 = r10.primaryGraphics
            if (r1 == 0) goto L_0x0094
            processing.opengl.PGL r1 = r10.pgl
            boolean r1 = r1.isFBOBacked()
            if (r1 == 0) goto L_0x0098
            processing.opengl.PGL r1 = r10.pgl
            boolean r1 = r1.isFBOBacked()
            if (r1 == 0) goto L_0x0094
            processing.opengl.PGL r1 = r10.pgl
            boolean r1 = r1.isMultisampled()
            if (r1 != 0) goto L_0x0098
        L_0x0094:
            boolean r1 = r10.offscreenMultisample
            if (r1 == 0) goto L_0x0099
        L_0x0098:
            r0 = 1
        L_0x0099:
            processing.opengl.Texture r1 = r10.texture
            if (r1 != 0) goto L_0x009e
        L_0x009d:
            return
        L_0x009e:
            if (r0 == 0) goto L_0x00db
            processing.opengl.Texture r0 = r10.texture
            int r0 = r0.glWidth
            int r1 = r8 * r11
            int r0 = r0 - r1
            int r1 = r8 * r13
            int r6 = processing.core.PApplet.min((int) r0, (int) r1)
            processing.opengl.Texture r0 = r10.texture
            int r0 = r0.glHeight
            int r1 = r8 * r12
            int r0 = r0 - r1
            int r1 = r8 * r14
            int r7 = processing.core.PApplet.min((int) r0, (int) r1)
            processing.opengl.PGL r0 = r10.pgl
            processing.opengl.Texture r1 = r10.texture
            int r1 = r1.glTarget
            processing.opengl.Texture r2 = r10.texture
            int r2 = r2.glFormat
            processing.opengl.Texture r3 = r10.texture
            int r3 = r3.glName
            int r4 = r8 * r11
            int r5 = r8 * r12
            java.nio.IntBuffer r8 = r10.nativePixelBuffer
            r0.copyToTexture((int) r1, (int) r2, (int) r3, (int) r4, (int) r5, (int) r6, (int) r7, (java.nio.IntBuffer) r8)
            r10.beginPixelsOp(r9)
            r10.drawTexture(r11, r12, r13, r14)
            r10.endPixelsOp()
            goto L_0x009d
        L_0x00db:
            processing.opengl.PGL r0 = r10.pgl
            processing.opengl.Texture r1 = r10.texture
            int r1 = r1.glTarget
            processing.opengl.Texture r2 = r10.texture
            int r2 = r2.glFormat
            processing.opengl.Texture r3 = r10.texture
            int r3 = r3.glName
            int r4 = r8 * r11
            int r5 = r10.height
            int r6 = r12 + r14
            int r5 = r5 - r6
            int r5 = r5 * r8
            int r6 = r8 * r13
            int r7 = r8 * r14
            java.nio.IntBuffer r8 = r10.nativePixelBuffer
            r0.copyToTexture((int) r1, (int) r2, (int) r3, (int) r4, (int) r5, (int) r6, (int) r7, (java.nio.IntBuffer) r8)
            goto L_0x009d
        L_0x00fb:
            r1 = move-exception
            goto L_0x0062
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.opengl.PGraphicsOpenGL.drawPixels(int, int, int, int):void");
    }

    /* access modifiers changed from: protected */
    public void drawTexture() {
        if (this.texture != null) {
            this.pgl.disable(PGL.BLEND);
            this.pgl.drawTexture(this.texture.glTarget, this.texture.glName, this.texture.glWidth, this.texture.glHeight, 0, 0, this.width, this.height);
            this.pgl.enable(PGL.BLEND);
        }
    }

    /* access modifiers changed from: protected */
    public void drawTexture(int i, int i2, int i3, int i4) {
        if (this.texture != null) {
            this.pgl.disable(PGL.BLEND);
            int i5 = i;
            int i6 = i2;
            int i7 = i;
            this.pgl.drawTexture(this.texture.glTarget, this.texture.glName, this.texture.glWidth, this.texture.glHeight, 0, 0, this.width, this.height, i5, i6, i + i3, i2 + i4, i7, this.height - (i2 + i4), i + i3, this.height - i2);
            this.pgl.enable(PGL.BLEND);
        }
    }

    public void ellipseImpl(float f, float f2, float f3, float f4) {
        beginShape(11);
        this.defaultEdges = false;
        this.normalMode = 1;
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addEllipse(f, f2, f3, f4, this.fill, this.stroke);
        endShape();
    }

    /* access modifiers changed from: protected */
    public void enableLighting() {
        flush();
        this.lights = true;
    }

    /* access modifiers changed from: protected */
    public void end2D() {
    }

    /* access modifiers changed from: protected */
    public void endBindFramebuffer(int i, int i2) {
        FrameBuffer currentFB = getCurrentFB();
        if (i2 == 0 && currentFB != null && currentFB.glFbo != 0) {
            currentFB.bind();
        }
    }

    public void endCamera() {
        if (!this.manipulatingCamera) {
            throw new RuntimeException("Cannot call endCamera() without first calling beginCamera()");
        }
        this.camera.set((PMatrix) this.modelview);
        this.cameraInv.set((PMatrix) this.modelviewInv);
        this.manipulatingCamera = false;
    }

    public void endContour() {
        if (!this.openContour) {
            PGraphics.showWarning(NO_BEGIN_CONTOUR_ERROR);
        } else {
            this.openContour = false;
        }
    }

    public void endDraw() {
        report("top endDraw()");
        if (this.drawing) {
            flush();
            if (this.primaryGraphics) {
                endOnscreenDraw();
            } else {
                endOffscreenDraw();
            }
            if (this.primaryGraphics) {
                setCurrentPG((PGraphicsOpenGL) null);
            } else {
                getPrimaryPG().setCurrentPG();
            }
            this.drawing = false;
            report("bot endDraw()");
        }
    }

    /* access modifiers changed from: protected */
    public void endOffscreenDraw() {
        if (this.offscreenMultisample) {
            FrameBuffer frameBuffer = this.offscreenFramebuffer;
            FrameBuffer frameBuffer2 = this.multisampleFramebuffer;
            if (!(frameBuffer == null || frameBuffer2 == null)) {
                frameBuffer2.copyColor(frameBuffer);
            }
        }
        popFramebuffer();
        if (this.backgroundA == 1.0f) {
            this.pgl.colorMask(false, false, false, true);
            this.pgl.clearColor(0.0f, 0.0f, 0.0f, this.backgroundA);
            this.pgl.clear(PGL.COLOR_BUFFER_BIT);
            this.pgl.colorMask(true, true, true, true);
        }
        if (this.texture != null) {
            this.texture.updateTexels();
        }
        getPrimaryPG().restoreGL();
    }

    /* access modifiers changed from: protected */
    public void endOnscreenDraw() {
        this.pgl.endRender(this.parent.sketchWindowColor());
    }

    public void endPGL() {
        this.pgl.endGL();
        restoreGL();
    }

    /* access modifiers changed from: protected */
    public void endPixelsOp() {
        if (this.pixOpChangedFB) {
            popFramebuffer();
            this.pixOpChangedFB = false;
        }
        if (readBufferSupported) {
            this.pgl.readBuffer(getCurrentFB().getDefaultReadBuffer());
        }
        if (drawBufferSupported) {
            this.pgl.drawBuffer(getCurrentFB().getDefaultDrawBuffer());
        }
        this.pixelsOp = 0;
    }

    /* access modifiers changed from: protected */
    public void endReadPixels() {
        endPixelsOp();
    }

    public void endShape(int i) {
        tessellate(i);
        if (this.flushMode == 0 || (this.flushMode == 1 && this.tessGeo.isFull())) {
            flush();
        } else {
            this.loaded = false;
        }
    }

    /* access modifiers changed from: protected */
    public void endShape(int[] iArr) {
        if (this.shape == 8 || this.shape == 9) {
            tessellate(iArr);
            if (this.flushMode == 0 || (this.flushMode == 1 && this.tessGeo.isFull())) {
                flush();
            } else {
                this.loaded = false;
            }
        } else {
            throw new RuntimeException("Indices and edges can only be set for TRIANGLE shapes");
        }
    }

    /* access modifiers changed from: protected */
    public void fillFromCalc() {
        super.fillFromCalc();
        if (!this.setAmbient) {
            ambientFromCalc();
            this.setAmbient = false;
        }
    }

    public void filter(int i) {
        PImage pImage = get();
        pImage.filter(i);
        set(0, 0, pImage);
    }

    public void filter(int i, float f) {
        PImage pImage = get();
        pImage.filter(i, f);
        set(0, 0, pImage);
    }

    public void filter(PShader pShader) {
        boolean z;
        if (!pShader.isPolyShader()) {
            PGraphics.showWarning(INVALID_FILTER_SHADER_ERROR);
            return;
        }
        if (this.primaryGraphics) {
            this.pgl.enableFBOLayer();
            z = false;
        } else if (!this.drawing) {
            beginDraw();
            z = true;
        } else {
            z = false;
        }
        loadTexture();
        if (this.filterTexture == null || this.filterTexture.contextIsOutdated()) {
            this.filterTexture = new Texture(this, this.texture.width, this.texture.height, this.texture.getParameters());
            this.filterTexture.invertedY(true);
            this.filterImage = wrapTexture(this.filterTexture);
        }
        this.filterTexture.set(this.texture);
        this.pgl.depthMask(false);
        this.pgl.disable(PGL.DEPTH_TEST);
        begin2D();
        boolean z2 = this.lights;
        this.lights = false;
        int i = this.textureMode;
        this.textureMode = 1;
        boolean z3 = this.stroke;
        this.stroke = false;
        int i2 = this.blendMode;
        blendMode(0);
        PShader pShader2 = this.polyShader;
        this.polyShader = pShader;
        beginShape(17);
        texture(this.filterImage);
        vertex(0.0f, 0.0f, 0.0f, 0.0f);
        vertex((float) this.width, 0.0f, 1.0f, 0.0f);
        vertex((float) this.width, (float) this.height, 1.0f, 1.0f);
        vertex(0.0f, (float) this.height, 0.0f, 1.0f);
        endShape();
        end2D();
        this.polyShader = pShader2;
        this.stroke = z3;
        this.lights = z2;
        this.textureMode = i;
        blendMode(i2);
        if (!this.hints[2]) {
            this.pgl.enable(PGL.DEPTH_TEST);
        }
        if (!this.hints[5]) {
            this.pgl.depthMask(true);
        }
        if (z) {
            endDraw();
        }
    }

    public void flush() {
        PMatrix3D pMatrix3D;
        PMatrix3D pMatrix3D2 = null;
        boolean z = this.tessGeo.polyVertexCount > 0 && this.tessGeo.polyIndexCount > 0;
        boolean z2 = this.tessGeo.lineVertexCount > 0 && this.tessGeo.lineIndexCount > 0;
        boolean z3 = this.tessGeo.pointVertexCount > 0 && this.tessGeo.pointIndexCount > 0;
        if (this.modified && this.pixels != null) {
            flushPixels();
        }
        if (z3 || z2 || z) {
            if (this.flushMode == 1) {
                pMatrix3D = this.modelview;
                pMatrix3D2 = this.modelviewInv;
                PMatrix3D pMatrix3D3 = identity;
                this.modelviewInv = pMatrix3D3;
                this.modelview = pMatrix3D3;
                this.projmodelview.set((PMatrix) this.projection);
            } else {
                pMatrix3D = null;
            }
            if (z && !this.isDepthSortingEnabled) {
                flushPolys();
                if (this.raw != null) {
                    rawPolys();
                }
            }
            if (is3D()) {
                if (z2) {
                    flushLines();
                    if (this.raw != null) {
                        rawLines();
                    }
                }
                if (z3) {
                    flushPoints();
                    if (this.raw != null) {
                        rawPoints();
                    }
                }
            }
            if (z && this.isDepthSortingEnabled) {
                flushSortedPolys();
                if (this.raw != null) {
                    rawSortedPolys();
                }
            }
            if (this.flushMode == 1) {
                this.modelview = pMatrix3D;
                this.modelviewInv = pMatrix3D2;
                updateProjmodelview();
            }
            this.loaded = false;
        }
        this.tessGeo.clear();
        this.texCache.clear();
    }

    /* access modifiers changed from: protected */
    public void flushLines() {
        updateLineBuffers();
        PShader lineShader2 = getLineShader();
        lineShader2.bind();
        IndexCache indexCache = this.tessGeo.lineIndexCache;
        for (int i = 0; i < indexCache.size; i++) {
            int i2 = indexCache.indexOffset[i];
            int i3 = indexCache.indexCount[i];
            int i4 = indexCache.vertexOffset[i];
            lineShader2.setVertexAttribute(this.bufLineVertex.glId, 4, PGL.FLOAT, 0, i4 * 4 * PGL.SIZEOF_FLOAT);
            lineShader2.setColorAttribute(this.bufLineColor.glId, 4, PGL.UNSIGNED_BYTE, 0, i4 * 4 * PGL.SIZEOF_BYTE);
            lineShader2.setLineAttribute(this.bufLineAttrib.glId, 4, PGL.FLOAT, 0, i4 * 4 * PGL.SIZEOF_FLOAT);
            lineShader2.draw(this.bufLineIndex.glId, i3, i2);
        }
        lineShader2.unbind();
        unbindLineBuffers();
    }

    /* access modifiers changed from: protected */
    public void flushPixels() {
        drawPixels(this.mx1, this.my1, this.mx2 - this.mx1, this.my2 - this.my1);
        this.modified = false;
    }

    /* access modifiers changed from: protected */
    public void flushPoints() {
        updatePointBuffers();
        PShader pointShader2 = getPointShader();
        pointShader2.bind();
        IndexCache indexCache = this.tessGeo.pointIndexCache;
        for (int i = 0; i < indexCache.size; i++) {
            int i2 = indexCache.indexOffset[i];
            int i3 = indexCache.indexCount[i];
            int i4 = indexCache.vertexOffset[i];
            pointShader2.setVertexAttribute(this.bufPointVertex.glId, 4, PGL.FLOAT, 0, i4 * 4 * PGL.SIZEOF_FLOAT);
            pointShader2.setColorAttribute(this.bufPointColor.glId, 4, PGL.UNSIGNED_BYTE, 0, i4 * 4 * PGL.SIZEOF_BYTE);
            pointShader2.setPointAttribute(this.bufPointAttrib.glId, 2, PGL.FLOAT, 0, i4 * 2 * PGL.SIZEOF_FLOAT);
            pointShader2.draw(this.bufPointIndex.glId, i3, i2);
        }
        pointShader2.unbind();
        unbindPointBuffers();
    }

    /* access modifiers changed from: protected */
    public void flushPolys() {
        boolean z = this.polyShader != null;
        boolean accessNormals = z ? this.polyShader.accessNormals() : false;
        boolean accessTexCoords = z ? this.polyShader.accessTexCoords() : false;
        updatePolyBuffers(this.lights, this.texCache.hasTextures, accessNormals, accessTexCoords);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < this.texCache.size) {
                Texture texture2 = this.texCache.getTexture(i2);
                PShader polyShader2 = getPolyShader(this.lights, texture2 != null);
                polyShader2.bind();
                int i3 = this.texCache.firstCache[i2];
                int i4 = this.texCache.lastCache[i2];
                IndexCache indexCache = this.tessGeo.polyIndexCache;
                int i5 = i3;
                while (i5 <= i4) {
                    int i6 = i5 == i3 ? this.texCache.firstIndex[i2] : indexCache.indexOffset[i5];
                    int i7 = i5 == i4 ? (this.texCache.lastIndex[i2] - i6) + 1 : (indexCache.indexOffset[i5] + indexCache.indexCount[i5]) - i6;
                    int i8 = indexCache.vertexOffset[i5];
                    polyShader2.setVertexAttribute(this.bufPolyVertex.glId, 4, PGL.FLOAT, 0, i8 * 4 * PGL.SIZEOF_FLOAT);
                    polyShader2.setColorAttribute(this.bufPolyColor.glId, 4, PGL.UNSIGNED_BYTE, 0, i8 * 4 * PGL.SIZEOF_BYTE);
                    if (this.lights) {
                        polyShader2.setNormalAttribute(this.bufPolyNormal.glId, 3, PGL.FLOAT, 0, i8 * 3 * PGL.SIZEOF_FLOAT);
                        polyShader2.setAmbientAttribute(this.bufPolyAmbient.glId, 4, PGL.UNSIGNED_BYTE, 0, i8 * 4 * PGL.SIZEOF_BYTE);
                        polyShader2.setSpecularAttribute(this.bufPolySpecular.glId, 4, PGL.UNSIGNED_BYTE, 0, i8 * 4 * PGL.SIZEOF_BYTE);
                        polyShader2.setEmissiveAttribute(this.bufPolyEmissive.glId, 4, PGL.UNSIGNED_BYTE, 0, i8 * 4 * PGL.SIZEOF_BYTE);
                        polyShader2.setShininessAttribute(this.bufPolyShininess.glId, 1, PGL.FLOAT, 0, PGL.SIZEOF_FLOAT * i8);
                    }
                    if (this.lights || accessNormals) {
                        polyShader2.setNormalAttribute(this.bufPolyNormal.glId, 3, PGL.FLOAT, 0, i8 * 3 * PGL.SIZEOF_FLOAT);
                    }
                    if (texture2 != null || accessTexCoords) {
                        polyShader2.setTexcoordAttribute(this.bufPolyTexcoord.glId, 2, PGL.FLOAT, 0, i8 * 2 * PGL.SIZEOF_FLOAT);
                        polyShader2.setTexture(texture2);
                    }
                    for (VertexAttribute vertexAttribute : this.polyAttribs.values()) {
                        if (vertexAttribute.active(polyShader2)) {
                            vertexAttribute.bind(this.pgl);
                            polyShader2.setAttributeVBO(vertexAttribute.glLoc, vertexAttribute.buf.glId, vertexAttribute.tessSize, vertexAttribute.type, vertexAttribute.isColor(), 0, vertexAttribute.sizeInBytes(i8));
                        }
                    }
                    polyShader2.draw(this.bufPolyIndex.glId, i7, i6);
                    i5++;
                }
                for (VertexAttribute vertexAttribute2 : this.polyAttribs.values()) {
                    if (vertexAttribute2.active(polyShader2)) {
                        vertexAttribute2.unbind(this.pgl);
                    }
                }
                polyShader2.unbind();
                i = i2 + 1;
            } else {
                unbindPolyBuffers();
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void flushSortedPolys() {
        int i;
        boolean z = this.polyShader != null;
        boolean accessNormals = z ? this.polyShader.accessNormals() : false;
        boolean accessTexCoords = z ? this.polyShader.accessTexCoords() : false;
        this.sorter.sort(this.tessGeo);
        int i2 = this.tessGeo.polyIndexCount / 3;
        int[] iArr = this.sorter.texMap;
        int[] iArr2 = this.sorter.voffsetMap;
        int[] iArr3 = this.tessGeo.polyIndexCache.vertexOffset;
        updatePolyBuffers(this.lights, this.texCache.hasTextures, accessNormals, accessTexCoords);
        int i3 = 0;
        while (i3 < i2) {
            int i4 = iArr[i3];
            int i5 = iArr2[i3];
            int i6 = i3;
            while (true) {
                i = i6 + 1;
                if (i < i2 && i4 == iArr[i] && i5 == iArr2[i]) {
                    i6 = i;
                } else {
                    Texture texture2 = this.texCache.getTexture(i4);
                    int i7 = iArr3[i5];
                    int i8 = i3 * 3;
                    int i9 = (i - i3) * 3;
                }
            }
            Texture texture22 = this.texCache.getTexture(i4);
            int i72 = iArr3[i5];
            int i82 = i3 * 3;
            int i92 = (i - i3) * 3;
            PShader polyShader2 = getPolyShader(this.lights, texture22 != null);
            polyShader2.bind();
            polyShader2.setVertexAttribute(this.bufPolyVertex.glId, 4, PGL.FLOAT, 0, i72 * 4 * PGL.SIZEOF_FLOAT);
            polyShader2.setColorAttribute(this.bufPolyColor.glId, 4, PGL.UNSIGNED_BYTE, 0, i72 * 4 * PGL.SIZEOF_BYTE);
            if (this.lights) {
                polyShader2.setNormalAttribute(this.bufPolyNormal.glId, 3, PGL.FLOAT, 0, i72 * 3 * PGL.SIZEOF_FLOAT);
                polyShader2.setAmbientAttribute(this.bufPolyAmbient.glId, 4, PGL.UNSIGNED_BYTE, 0, i72 * 4 * PGL.SIZEOF_BYTE);
                polyShader2.setSpecularAttribute(this.bufPolySpecular.glId, 4, PGL.UNSIGNED_BYTE, 0, i72 * 4 * PGL.SIZEOF_BYTE);
                polyShader2.setEmissiveAttribute(this.bufPolyEmissive.glId, 4, PGL.UNSIGNED_BYTE, 0, i72 * 4 * PGL.SIZEOF_BYTE);
                polyShader2.setShininessAttribute(this.bufPolyShininess.glId, 1, PGL.FLOAT, 0, PGL.SIZEOF_FLOAT * i72);
            }
            if (this.lights || accessNormals) {
                polyShader2.setNormalAttribute(this.bufPolyNormal.glId, 3, PGL.FLOAT, 0, i72 * 3 * PGL.SIZEOF_FLOAT);
            }
            if (texture22 != null || accessTexCoords) {
                polyShader2.setTexcoordAttribute(this.bufPolyTexcoord.glId, 2, PGL.FLOAT, 0, i72 * 2 * PGL.SIZEOF_FLOAT);
                polyShader2.setTexture(texture22);
            }
            for (VertexAttribute vertexAttribute : this.polyAttribs.values()) {
                if (vertexAttribute.active(polyShader2)) {
                    vertexAttribute.bind(this.pgl);
                    polyShader2.setAttributeVBO(vertexAttribute.glLoc, vertexAttribute.buf.glId, vertexAttribute.tessSize, vertexAttribute.type, vertexAttribute.isColor(), 0, vertexAttribute.sizeInBytes(i72));
                }
            }
            polyShader2.draw(this.bufPolyIndex.glId, i92, i82);
            for (VertexAttribute vertexAttribute2 : this.polyAttribs.values()) {
                if (vertexAttribute2.active(polyShader2)) {
                    vertexAttribute2.unbind(this.pgl);
                }
            }
            polyShader2.unbind();
            i3 = i;
        }
        unbindPolyBuffers();
    }

    public void frustum(float f, float f2, float f3, float f4, float f5, float f6) {
        flush();
        float f7 = 2.0f * f5;
        float f8 = f2 - f;
        float f9 = f4 - f3;
        float f10 = f6 - f5;
        this.projection.set(f7 / f8, 0.0f, (f2 + f) / f8, 0.0f, 0.0f, (-f7) / f9, (f4 + f3) / f9, 0.0f, 0.0f, 0.0f, (-(f6 + f5)) / f10, (-(f7 * f6)) / f10, 0.0f, 0.0f, -1.0f, 0.0f);
        updateProjmodelview();
    }

    public int get(int i, int i2) {
        loadPixels();
        return super.get(i, i2);
    }

    public Object getCache(PImage pImage) {
        Object obj = getPrimaryPG().cacheMap.get(pImage);
        return (obj == null || obj.getClass() != WeakReference.class) ? obj : ((WeakReference) obj).get();
    }

    /* access modifiers changed from: protected */
    public FrameBuffer getCurrentFB() {
        return getPrimaryPG().currentFramebuffer;
    }

    /* access modifiers changed from: protected */
    public PGraphicsOpenGL getCurrentPG() {
        return this.currentPG;
    }

    /* access modifiers changed from: protected */
    public FontTexture getFontTexture(PFont pFont) {
        return getPrimaryPG().fontMap.get(pFont);
    }

    public FrameBuffer getFrameBuffer() {
        return getFrameBuffer(false);
    }

    public FrameBuffer getFrameBuffer(boolean z) {
        return z ? this.multisampleFramebuffer : this.offscreenFramebuffer;
    }

    /* access modifiers changed from: protected */
    public void getGLParameters() {
        OPENGL_VENDOR = this.pgl.getString(PGL.VENDOR);
        OPENGL_RENDERER = this.pgl.getString(PGL.RENDERER);
        OPENGL_VERSION = this.pgl.getString(PGL.VERSION);
        OPENGL_EXTENSIONS = this.pgl.getString(PGL.EXTENSIONS);
        GLSL_VERSION = this.pgl.getString(PGL.SHADING_LANGUAGE_VERSION);
        npotTexSupported = this.pgl.hasNpotTexSupport();
        autoMipmapGenSupported = this.pgl.hasAutoMipmapGenSupport();
        fboMultisampleSupported = this.pgl.hasFboMultisampleSupport();
        packedDepthStencilSupported = this.pgl.hasPackedDepthStencilSupport();
        anisoSamplingSupported = this.pgl.hasAnisoSamplingSupport();
        readBufferSupported = this.pgl.hasReadBuffer();
        drawBufferSupported = this.pgl.hasDrawBuffer();
        try {
            this.pgl.blendEquation(PGL.FUNC_ADD);
            blendEqSupported = true;
        } catch (Exception e) {
            blendEqSupported = false;
        }
        depthBits = this.pgl.getDepthBits();
        stencilBits = this.pgl.getStencilBits();
        this.pgl.getIntegerv(PGL.MAX_TEXTURE_SIZE, intBuffer);
        maxTextureSize = intBuffer.get(0);
        if (!OPENGL_RENDERER.equals("VideoCore IV HW")) {
            this.pgl.getIntegerv(PGL.MAX_SAMPLES, intBuffer);
            maxSamples = intBuffer.get(0);
        }
        if (anisoSamplingSupported) {
            this.pgl.getFloatv(PGL.MAX_TEXTURE_MAX_ANISOTROPY, floatBuffer);
            maxAnisoAmount = floatBuffer.get(0);
        }
        if (OPENGL_RENDERER.equals("VideoCore IV HW") || OPENGL_RENDERER.equals("Gallium 0.4 on VC4")) {
            defLightShaderVertURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/LightVert-vc4.glsl");
            defTexlightShaderVertURL = PGraphicsOpenGL.class.getResource("/processing/opengl/shaders/TexLightVert-vc4.glsl");
        }
        glParamsRead = true;
    }

    /* access modifiers changed from: protected */
    public boolean getHint(int i) {
        return i > 0 ? this.hints[i] : !this.hints[-i];
    }

    /* access modifiers changed from: protected */
    public void getImpl(int i, int i2, int i3, int i4, PImage pImage, int i5, int i6) {
        loadPixels();
        super.getImpl(i, i2, i3, i4, pImage, i5, i6);
    }

    /* access modifiers changed from: protected */
    public PShader getLineShader() {
        PShader pShader;
        PGraphicsOpenGL primaryPG = getPrimaryPG();
        if (this.lineShader == null) {
            if (primaryPG.defLineShader == null) {
                primaryPG.defLineShader = new PShader(this.parent, this.pgl.loadVertexShader(defLineShaderVertURL), this.pgl.loadFragmentShader(defLineShaderFragURL));
            }
            pShader = primaryPG.defLineShader;
        } else {
            pShader = this.lineShader;
        }
        pShader.setRenderer(this);
        pShader.loadAttributes();
        pShader.loadUniforms();
        return pShader;
    }

    public PMatrix3D getMatrix(PMatrix3D pMatrix3D) {
        if (pMatrix3D == null) {
            pMatrix3D = new PMatrix3D();
        }
        pMatrix3D.set((PMatrix) this.modelview);
        return pMatrix3D;
    }

    public PMatrix getMatrix() {
        return this.modelview.get();
    }

    /* access modifiers changed from: protected */
    public PShader getPointShader() {
        PShader pShader;
        PGraphicsOpenGL primaryPG = getPrimaryPG();
        if (this.pointShader == null) {
            if (primaryPG.defPointShader == null) {
                primaryPG.defPointShader = new PShader(this.parent, this.pgl.loadVertexShader(defPointShaderVertURL), this.pgl.loadFragmentShader(defPointShaderFragURL));
            }
            pShader = primaryPG.defPointShader;
        } else {
            pShader = this.pointShader;
        }
        pShader.setRenderer(this);
        pShader.loadAttributes();
        pShader.loadUniforms();
        return pShader;
    }

    /* access modifiers changed from: protected */
    public PShader getPolyShader(boolean z, boolean z2) {
        PShader pShader;
        PGraphicsOpenGL primaryPG = getPrimaryPG();
        boolean z3 = this.polyShader == null;
        if (this.polyShader != null) {
            this.polyShader.setRenderer(this);
            this.polyShader.loadAttributes();
            this.polyShader.loadUniforms();
        }
        if (!z) {
            if (this.polyShader != null && this.polyShader.accessLightAttribs()) {
                PGraphics.showWarning(SHADER_NEED_LIGHT_ATTRIBS);
                z3 = true;
            }
            if (z2) {
                if (z3 || !this.polyShader.checkPolyType(5)) {
                    if (primaryPG.defTextureShader == null) {
                        primaryPG.defTextureShader = new PShader(this.parent, this.pgl.loadVertexShader(defTextureShaderVertURL), this.pgl.loadFragmentShader(defTextureShaderFragURL));
                    }
                    pShader = primaryPG.defTextureShader;
                } else {
                    pShader = this.polyShader;
                }
            } else if (z3 || !this.polyShader.checkPolyType(3)) {
                if (primaryPG.defColorShader == null) {
                    primaryPG.defColorShader = new PShader(this.parent, this.pgl.loadVertexShader(defColorShaderVertURL), this.pgl.loadFragmentShader(defColorShaderFragURL));
                }
                pShader = primaryPG.defColorShader;
            } else {
                pShader = this.polyShader;
            }
        } else if (z2) {
            if (z3 || !this.polyShader.checkPolyType(6)) {
                if (primaryPG.defTexlightShader == null) {
                    primaryPG.defTexlightShader = new PShader(this.parent, this.pgl.loadVertexShader(defTexlightShaderVertURL), this.pgl.loadFragmentShader(defTexlightShaderFragURL));
                }
                pShader = primaryPG.defTexlightShader;
            } else {
                pShader = this.polyShader;
            }
        } else if (z3 || !this.polyShader.checkPolyType(4)) {
            if (primaryPG.defLightShader == null) {
                primaryPG.defLightShader = new PShader(this.parent, this.pgl.loadVertexShader(defLightShaderVertURL), this.pgl.loadFragmentShader(defLightShaderFragURL));
            }
            pShader = primaryPG.defLightShader;
        } else {
            pShader = this.polyShader;
        }
        if (pShader != this.polyShader) {
            pShader.setRenderer(this);
            pShader.loadAttributes();
            pShader.loadUniforms();
        }
        return pShader;
    }

    /* access modifiers changed from: protected */
    public PGraphicsOpenGL getPrimaryPG() {
        return this.primaryGraphics ? this : (PGraphicsOpenGL) this.parent.g;
    }

    /* access modifiers changed from: protected */
    public PGL getPrimaryPGL() {
        return this.primaryGraphics ? this.pgl : ((PGraphicsOpenGL) this.parent.g).pgl;
    }

    public Texture getTexture() {
        return getTexture(true);
    }

    public Texture getTexture(PImage pImage) {
        Texture texture2 = (Texture) initCache(pImage);
        if (texture2 == null) {
            return null;
        }
        if (pImage.isModified()) {
            if (!(pImage.width == texture2.width && pImage.height == texture2.height)) {
                texture2.init(pImage.width, pImage.height);
            }
            updateTexture(pImage, texture2);
        }
        if (texture2.hasBuffers()) {
            texture2.bufferUpdate();
        }
        checkTexture(texture2);
        return texture2;
    }

    public Texture getTexture(boolean z) {
        if (z) {
            loadTexture();
        }
        return this.texture;
    }

    /* access modifiers changed from: protected */
    public void handleTextSize(float f) {
        Object obj = this.textFont.getNative();
        if (obj != null) {
            this.textFont.setNative(this.pgl.getDerivedFont(obj, f));
        }
        super.handleTextSize(f);
    }

    public void hint(int i) {
        boolean z = this.hints[PApplet.abs(i)];
        super.hint(i);
        if (z != this.hints[PApplet.abs(i)]) {
            if (i == 2) {
                flush();
                this.pgl.disable(PGL.DEPTH_TEST);
            } else if (i == -2) {
                flush();
                this.pgl.enable(PGL.DEPTH_TEST);
            } else if (i == 5) {
                flush();
                this.pgl.depthMask(false);
            } else if (i == -5) {
                flush();
                this.pgl.depthMask(true);
            } else if (i == -6) {
                flush();
                setFlushMode(1);
            } else if (i == 6) {
                if (is2D()) {
                    PGraphics.showWarning("Optimized strokes can only be disabled in 3D");
                    return;
                }
                flush();
                setFlushMode(0);
            } else if (i == -7) {
                if (this.tessGeo.lineVertexCount > 0 && this.tessGeo.lineIndexCount > 0) {
                    flush();
                }
            } else if (i == 7) {
                if (this.tessGeo.lineVertexCount > 0 && this.tessGeo.lineIndexCount > 0) {
                    flush();
                }
            } else if (i == 3) {
                if (is3D()) {
                    flush();
                    if (this.sorter == null) {
                        this.sorter = new DepthSorter(this);
                    }
                    this.isDepthSortingEnabled = true;
                    return;
                }
                PGraphics.showWarning("Depth sorting can only be enabled in 3D");
            } else if (i == -3) {
                if (is3D()) {
                    flush();
                    this.isDepthSortingEnabled = false;
                }
            } else if (i == 10) {
                restartPGL();
            } else if (i == -10) {
                restartPGL();
            }
        }
    }

    /* access modifiers changed from: protected */
    public Object initCache(PImage pImage) {
        if (!checkGLThread()) {
            return null;
        }
        Texture texture2 = (Texture) getCache(pImage);
        if (texture2 != null && !texture2.contextIsOutdated()) {
            return texture2;
        }
        Texture addTexture = addTexture(pImage);
        if (addTexture == null) {
            return addTexture;
        }
        pImage.loadPixels();
        addTexture.set(pImage.pixels, pImage.format);
        pImage.setModified();
        return addTexture;
    }

    /* access modifiers changed from: protected */
    public void initOffscreen() {
        FrameBuffer frameBuffer;
        loadTextureImpl(this.textureSampling, false);
        FrameBuffer frameBuffer2 = this.offscreenFramebuffer;
        FrameBuffer frameBuffer3 = this.multisampleFramebuffer;
        if (frameBuffer2 != null) {
            frameBuffer2.dispose();
        }
        if (frameBuffer3 != null) {
            frameBuffer3.dispose();
        }
        boolean z = depthBits == 24 && stencilBits == 8 && packedDepthStencilSupported;
        if (!fboMultisampleSupported || 1 >= PGL.smoothToSamples(this.smooth)) {
            this.smooth = 0;
            frameBuffer = new FrameBuffer(this, this.texture.glWidth, this.texture.glHeight, 1, 1, depthBits, stencilBits, z, false);
            this.offscreenMultisample = false;
        } else {
            FrameBuffer frameBuffer4 = new FrameBuffer(this, this.texture.glWidth, this.texture.glHeight, PGL.smoothToSamples(this.smooth), 0, depthBits, stencilBits, z, false);
            frameBuffer4.clear();
            this.multisampleFramebuffer = frameBuffer4;
            this.offscreenMultisample = true;
            frameBuffer = this.hints[10] ? new FrameBuffer(this, this.texture.glWidth, this.texture.glHeight, 1, 1, depthBits, stencilBits, z, false) : new FrameBuffer(this, this.texture.glWidth, this.texture.glHeight, 1, 1, 0, 0, false, false);
        }
        frameBuffer.setColorBuffer(this.texture);
        frameBuffer.clear();
        this.offscreenFramebuffer = frameBuffer;
        this.initialized = true;
    }

    /* access modifiers changed from: protected */
    public void initPrimary() {
        this.pgl.initSurface(this.smooth);
        if (this.texture != null) {
            removeCache(this);
            this.texture = null;
            this.ptexture = null;
        }
        this.initialized = true;
    }

    public boolean isGL() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void lightAmbient(int i, float f, float f2, float f3) {
        colorCalc(f, f2, f3);
        this.lightAmbient[(i * 3) + 0] = this.calcR;
        this.lightAmbient[(i * 3) + 1] = this.calcG;
        this.lightAmbient[(i * 3) + 2] = this.calcB;
    }

    /* access modifiers changed from: protected */
    public void lightDiffuse(int i, float f, float f2, float f3) {
        colorCalc(f, f2, f3);
        this.lightDiffuse[(i * 3) + 0] = this.calcR;
        this.lightDiffuse[(i * 3) + 1] = this.calcG;
        this.lightDiffuse[(i * 3) + 2] = this.calcB;
    }

    public void lightFalloff(float f, float f2, float f3) {
        this.currentLightFalloffConstant = f;
        this.currentLightFalloffLinear = f2;
        this.currentLightFalloffQuadratic = f3;
    }

    /* access modifiers changed from: protected */
    public void lightFalloff(int i, float f, float f2, float f3) {
        this.lightFalloffCoefficients[(i * 3) + 0] = f;
        this.lightFalloffCoefficients[(i * 3) + 1] = f2;
        this.lightFalloffCoefficients[(i * 3) + 2] = f3;
    }

    /* access modifiers changed from: protected */
    public void lightNormal(int i, float f, float f2, float f3) {
        float f4 = (this.modelviewInv.m00 * f) + (this.modelviewInv.m10 * f2) + (this.modelviewInv.m20 * f3);
        float f5 = (this.modelviewInv.m01 * f) + (this.modelviewInv.m11 * f2) + (this.modelviewInv.m21 * f3);
        float f6 = (this.modelviewInv.m02 * f) + (this.modelviewInv.m12 * f2) + (this.modelviewInv.m22 * f3);
        float dist = PApplet.dist(0.0f, 0.0f, 0.0f, f4, f5, f6);
        if (0.0f < dist) {
            float f7 = 1.0f / dist;
            this.lightNormal[(i * 3) + 0] = f4 * f7;
            this.lightNormal[(i * 3) + 1] = f7 * f5;
            this.lightNormal[(i * 3) + 2] = f7 * f6;
            return;
        }
        this.lightNormal[(i * 3) + 0] = 0.0f;
        this.lightNormal[(i * 3) + 1] = 0.0f;
        this.lightNormal[(i * 3) + 2] = 0.0f;
    }

    /* access modifiers changed from: protected */
    public void lightPosition(int i, float f, float f2, float f3, boolean z) {
        this.lightPosition[(i * 4) + 0] = (this.modelview.m00 * f) + (this.modelview.m01 * f2) + (this.modelview.m02 * f3) + this.modelview.m03;
        this.lightPosition[(i * 4) + 1] = (this.modelview.m10 * f) + (this.modelview.m11 * f2) + (this.modelview.m12 * f3) + this.modelview.m13;
        this.lightPosition[(i * 4) + 2] = (this.modelview.m20 * f) + (this.modelview.m21 * f2) + (this.modelview.m22 * f3) + this.modelview.m23;
        this.lightPosition[(i * 4) + 3] = z ? 0.0f : 1.0f;
    }

    public void lightSpecular(float f, float f2, float f3) {
        colorCalc(f, f2, f3);
        this.currentLightSpecular[0] = this.calcR;
        this.currentLightSpecular[1] = this.calcG;
        this.currentLightSpecular[2] = this.calcB;
    }

    /* access modifiers changed from: protected */
    public void lightSpecular(int i, float f, float f2, float f3) {
        this.lightSpecular[(i * 3) + 0] = f;
        this.lightSpecular[(i * 3) + 1] = f2;
        this.lightSpecular[(i * 3) + 2] = f3;
    }

    /* access modifiers changed from: protected */
    public void lightSpot(int i, float f, float f2) {
        this.lightSpotParameters[(i * 2) + 0] = Math.max(0.0f, PApplet.cos(f));
        this.lightSpotParameters[(i * 2) + 1] = f2;
    }

    public void lights() {
        enableLighting();
        this.lightCount = 0;
        int i = this.colorMode;
        this.colorMode = 1;
        lightFalloff(1.0f, 0.0f, 0.0f);
        lightSpecular(0.0f, 0.0f, 0.0f);
        ambientLight(this.colorModeX * 0.5f, this.colorModeY * 0.5f, this.colorModeZ * 0.5f);
        directionalLight(this.colorModeX * 0.5f, this.colorModeY * 0.5f, 0.5f * this.colorModeZ, 0.0f, 0.0f, -1.0f);
        this.colorMode = i;
    }

    public void line(float f, float f2, float f3, float f4) {
        lineImpl(f, f2, 0.0f, f3, f4, 0.0f);
    }

    public void line(float f, float f2, float f3, float f4, float f5, float f6) {
        lineImpl(f, f2, f3, f4, f5, f6);
    }

    /* access modifiers changed from: protected */
    public boolean lineBufferContextIsOutdated() {
        return !this.pgl.contextIsCurrent(this.lineBuffersContext);
    }

    /* access modifiers changed from: protected */
    public void lineImpl(float f, float f2, float f3, float f4, float f5, float f6) {
        beginShape(5);
        this.defaultEdges = false;
        this.normalMode = 1;
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addLine(f, f2, f3, f4, f5, f6, this.fill, this.stroke);
        endShape();
    }

    public void loadPixels() {
        if (!this.primaryGraphics || !this.sized) {
            boolean z = false;
            if (!this.drawing) {
                beginDraw();
                z = true;
            }
            if (!this.loaded) {
                flush();
            }
            allocatePixels();
            if (!this.loaded) {
                readPixels();
            }
            this.loaded = true;
            if (z) {
                endDraw();
            }
        }
    }

    public PShader loadShader(String str) {
        if (str == null || str.equals("")) {
            PGraphics.showWarning(MISSING_FRAGMENT_SHADER);
            return null;
        }
        int shaderType = PShader.getShaderType(this.parent.loadStrings(str), 2);
        PShader pShader = new PShader(this.parent);
        pShader.setType(shaderType);
        pShader.setFragmentShader(str);
        if (shaderType == 0) {
            pShader.setVertexShader(this.pgl.loadVertexShader(defPointShaderVertURL));
            return pShader;
        } else if (shaderType == 1) {
            pShader.setVertexShader(this.pgl.loadVertexShader(defLineShaderVertURL));
            return pShader;
        } else if (shaderType == 6) {
            pShader.setVertexShader(this.pgl.loadVertexShader(defTexlightShaderVertURL));
            return pShader;
        } else if (shaderType == 4) {
            pShader.setVertexShader(this.pgl.loadVertexShader(defLightShaderVertURL));
            return pShader;
        } else if (shaderType == 5) {
            pShader.setVertexShader(this.pgl.loadVertexShader(defTextureShaderVertURL));
            return pShader;
        } else if (shaderType == 3) {
            pShader.setVertexShader(this.pgl.loadVertexShader(defColorShaderVertURL));
            return pShader;
        } else {
            pShader.setVertexShader(this.pgl.loadVertexShader(defTextureShaderVertURL));
            return pShader;
        }
    }

    public PShader loadShader(String str, String str2) {
        if (str == null || str.equals("")) {
            PGraphics.showWarning(MISSING_FRAGMENT_SHADER);
            return null;
        } else if (str2 != null && !str2.equals("")) {
            return new PShader(this.parent, str2, str);
        } else {
            PGraphics.showWarning(MISSING_VERTEX_SHADER);
            return null;
        }
    }

    public PShape loadShape(String str) {
        String extension = PApplet.getExtension(str);
        if (PGraphics2D.isSupportedExtension(extension)) {
            return PGraphics2D.loadShapeImpl(this, str, extension);
        }
        if (PGraphics3D.isSupportedExtension(extension)) {
            return PGraphics3D.loadShapeImpl(this, str, extension);
        }
        PGraphics.showWarning(UNSUPPORTED_SHAPE_FORMAT_ERROR);
        return null;
    }

    public void loadTexture() {
        boolean z;
        if (!this.drawing) {
            beginDraw();
            z = true;
        } else {
            z = false;
        }
        flush();
        if (this.primaryGraphics) {
            updatePixelSize();
            if (this.pgl.isFBOBacked()) {
                this.pgl.syncBackTexture();
            } else {
                loadTextureImpl(2, false);
                if (this.nativePixels == null || this.nativePixels.length < this.pixelWidth * this.pixelHeight) {
                    this.nativePixels = new int[(this.pixelWidth * this.pixelHeight)];
                    this.nativePixelBuffer = PGL.allocateIntBuffer(this.nativePixels);
                }
                beginPixelsOp(1);
                try {
                    this.pgl.readPixelsImpl(0, 0, this.pixelWidth, this.pixelHeight, PGL.RGBA, PGL.UNSIGNED_BYTE, (Buffer) this.nativePixelBuffer);
                } catch (IndexOutOfBoundsException e) {
                }
                endPixelsOp();
                if (this.texture != null) {
                    this.texture.setNative(this.nativePixelBuffer, 0, 0, this.pixelWidth, this.pixelHeight);
                }
            }
        } else if (this.offscreenMultisample) {
            FrameBuffer frameBuffer = this.offscreenFramebuffer;
            FrameBuffer frameBuffer2 = this.multisampleFramebuffer;
            if (!(frameBuffer == null || frameBuffer2 == null)) {
                frameBuffer2.copyColor(frameBuffer);
            }
        }
        if (z) {
            endDraw();
        }
    }

    /* access modifiers changed from: protected */
    public void loadTextureImpl(int i, boolean z) {
        updatePixelSize();
        if (this.pixelWidth != 0 && this.pixelHeight != 0) {
            if (this.texture == null || this.texture.contextIsOutdated()) {
                this.texture = new Texture(this, this.pixelWidth, this.pixelHeight, new Texture.Parameters(2, i, z));
                this.texture.invertedY(true);
                this.texture.colorBuffer(true);
                setCache(this, this.texture);
            }
        }
    }

    public void mask(PImage pImage) {
        updatePixelSize();
        if (pImage.width == this.pixelWidth && pImage.height == this.pixelHeight) {
            PGraphicsOpenGL primaryPG = getPrimaryPG();
            if (primaryPG.maskShader == null) {
                primaryPG.maskShader = new PShader(this.parent, defTextureShaderVertURL, maskShaderFragURL);
            }
            primaryPG.maskShader.set("mask", pImage);
            filter(primaryPG.maskShader);
            return;
        }
        throw new RuntimeException("The PImage used with mask() must be the same size as the applet.");
    }

    public float modelX(float f, float f2, float f3) {
        float f4 = this.modelview.m03 + (this.modelview.m00 * f) + (this.modelview.m01 * f2) + (this.modelview.m02 * f3);
        float f5 = this.modelview.m13 + (this.modelview.m10 * f) + (this.modelview.m11 * f2) + (this.modelview.m12 * f3);
        float f6 = this.modelview.m23 + (this.modelview.m20 * f) + (this.modelview.m21 * f2) + (this.modelview.m22 * f3);
        float f7 = this.modelview.m33 + (this.modelview.m30 * f) + (this.modelview.m31 * f2) + (this.modelview.m32 * f3);
        float f8 = (this.cameraInv.m00 * f4) + (this.cameraInv.m01 * f5) + (this.cameraInv.m02 * f6) + (this.cameraInv.m03 * f7);
        float f9 = (f4 * this.cameraInv.m30) + (f5 * this.cameraInv.m31) + (this.cameraInv.m32 * f6) + (this.cameraInv.m33 * f7);
        return nonZero(f9) ? f8 / f9 : f8;
    }

    public float modelY(float f, float f2, float f3) {
        float f4 = this.modelview.m03 + (this.modelview.m00 * f) + (this.modelview.m01 * f2) + (this.modelview.m02 * f3);
        float f5 = this.modelview.m13 + (this.modelview.m10 * f) + (this.modelview.m11 * f2) + (this.modelview.m12 * f3);
        float f6 = this.modelview.m23 + (this.modelview.m20 * f) + (this.modelview.m21 * f2) + (this.modelview.m22 * f3);
        float f7 = this.modelview.m33 + (this.modelview.m30 * f) + (this.modelview.m31 * f2) + (this.modelview.m32 * f3);
        float f8 = (this.cameraInv.m10 * f4) + (this.cameraInv.m11 * f5) + (this.cameraInv.m12 * f6) + (this.cameraInv.m13 * f7);
        float f9 = (f4 * this.cameraInv.m30) + (f5 * this.cameraInv.m31) + (this.cameraInv.m32 * f6) + (this.cameraInv.m33 * f7);
        return nonZero(f9) ? f8 / f9 : f8;
    }

    public float modelZ(float f, float f2, float f3) {
        float f4 = this.modelview.m03 + (this.modelview.m00 * f) + (this.modelview.m01 * f2) + (this.modelview.m02 * f3);
        float f5 = this.modelview.m13 + (this.modelview.m10 * f) + (this.modelview.m11 * f2) + (this.modelview.m12 * f3);
        float f6 = this.modelview.m23 + (this.modelview.m20 * f) + (this.modelview.m21 * f2) + (this.modelview.m22 * f3);
        float f7 = this.modelview.m33 + (this.modelview.m30 * f) + (this.modelview.m31 * f2) + (this.modelview.m32 * f3);
        float f8 = (this.cameraInv.m20 * f4) + (this.cameraInv.m21 * f5) + (this.cameraInv.m22 * f6) + (this.cameraInv.m23 * f7);
        float f9 = (f4 * this.cameraInv.m30) + (f5 * this.cameraInv.m31) + (this.cameraInv.m32 * f6) + (this.cameraInv.m33 * f7);
        return nonZero(f9) ? f8 / f9 : f8;
    }

    public void noClip() {
        if (this.clip) {
            flush();
            this.pgl.disable(PGL.SCISSOR_TEST);
            this.clip = false;
        }
    }

    /* access modifiers changed from: protected */
    public void noLightAmbient(int i) {
        this.lightAmbient[(i * 3) + 0] = 0.0f;
        this.lightAmbient[(i * 3) + 1] = 0.0f;
        this.lightAmbient[(i * 3) + 2] = 0.0f;
    }

    /* access modifiers changed from: protected */
    public void noLightDiffuse(int i) {
        this.lightDiffuse[(i * 3) + 0] = 0.0f;
        this.lightDiffuse[(i * 3) + 1] = 0.0f;
        this.lightDiffuse[(i * 3) + 2] = 0.0f;
    }

    /* access modifiers changed from: protected */
    public void noLightFalloff(int i) {
        this.lightFalloffCoefficients[(i * 3) + 0] = 1.0f;
        this.lightFalloffCoefficients[(i * 3) + 1] = 0.0f;
        this.lightFalloffCoefficients[(i * 3) + 2] = 0.0f;
    }

    /* access modifiers changed from: protected */
    public void noLightSpecular(int i) {
        this.lightSpecular[(i * 3) + 0] = 0.0f;
        this.lightSpecular[(i * 3) + 1] = 0.0f;
        this.lightSpecular[(i * 3) + 2] = 0.0f;
    }

    /* access modifiers changed from: protected */
    public void noLightSpot(int i) {
        this.lightSpotParameters[(i * 2) + 0] = 0.0f;
        this.lightSpotParameters[(i * 2) + 1] = 0.0f;
    }

    public void noLights() {
        disableLighting();
        this.lightCount = 0;
    }

    /* access modifiers changed from: protected */
    public boolean nonOrthoProjection() {
        return nonZero(this.projection.m01) || nonZero(this.projection.m02) || nonZero(this.projection.m10) || nonZero(this.projection.m12) || nonZero(this.projection.m20) || nonZero(this.projection.m21) || nonZero(this.projection.m30) || nonZero(this.projection.m31) || nonZero(this.projection.m32) || diff(this.projection.m33, 1.0f);
    }

    public void ortho() {
        ortho(((float) (-this.width)) / 2.0f, ((float) this.width) / 2.0f, ((float) (-this.height)) / 2.0f, ((float) this.height) / 2.0f, 0.0f, POINT_ACCURACY_FACTOR * this.eyeDist);
    }

    public void ortho(float f, float f2, float f3, float f4) {
        ortho(f, f2, f3, f4, 0.0f, this.eyeDist * POINT_ACCURACY_FACTOR);
    }

    public void ortho(float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = f2 - f;
        float f8 = f4 - f3;
        float f9 = f6 - f5;
        flush();
        PMatrix3D pMatrix3D = this.projection;
        float f10 = -(2.0f / f8);
        pMatrix3D.set(2.0f / f7, 0.0f, 0.0f, (-(f2 + f)) / f7, 0.0f, f10, 0.0f, (-(f4 + f3)) / f8, 0.0f, 0.0f, -2.0f / f9, (-(f6 + f5)) / f9, 0.0f, 0.0f, 0.0f, 1.0f);
        updateProjmodelview();
    }

    /* access modifiers changed from: protected */
    public boolean orthoProjection() {
        return zero(this.projection.m01) && zero(this.projection.m02) && zero(this.projection.m10) && zero(this.projection.m12) && zero(this.projection.m20) && zero(this.projection.m21) && zero(this.projection.m30) && zero(this.projection.m31) && zero(this.projection.m32) && same(this.projection.m33, 1.0f);
    }

    public void perspective() {
        perspective(this.cameraFOV, this.cameraAspect, this.cameraNear, this.cameraFar);
    }

    public void perspective(float f, float f2, float f3, float f4) {
        float tan = f3 * ((float) Math.tan((double) (f / 2.0f)));
        float f5 = -tan;
        frustum(f5 * f2, tan * f2, f5, tan, f3, f4);
    }

    public void point(float f, float f2) {
        pointImpl(f, f2, 0.0f);
    }

    public void point(float f, float f2, float f3) {
        pointImpl(f, f2, f3);
    }

    /* access modifiers changed from: protected */
    public boolean pointBuffersContextIsOutdated() {
        return !this.pgl.contextIsCurrent(this.pointBuffersContext);
    }

    /* access modifiers changed from: protected */
    public void pointImpl(float f, float f2, float f3) {
        beginShape(3);
        this.defaultEdges = false;
        this.normalMode = 1;
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addPoint(f, f2, f3, this.fill, this.stroke);
        endShape();
    }

    public void pointLight(float f, float f2, float f3, float f4, float f5, float f6) {
        enableLighting();
        if (this.lightCount == PGL.MAX_LIGHTS) {
            throw new RuntimeException("can only create " + PGL.MAX_LIGHTS + " lights");
        }
        this.lightType[this.lightCount] = 2;
        lightPosition(this.lightCount, f4, f5, f6, false);
        lightNormal(this.lightCount, 0.0f, 0.0f, 0.0f);
        noLightAmbient(this.lightCount);
        lightDiffuse(this.lightCount, f, f2, f3);
        lightSpecular(this.lightCount, this.currentLightSpecular[0], this.currentLightSpecular[1], this.currentLightSpecular[2]);
        noLightSpot(this.lightCount);
        lightFalloff(this.lightCount, this.currentLightFalloffConstant, this.currentLightFalloffLinear, this.currentLightFalloffQuadratic);
        this.lightCount++;
    }

    /* access modifiers changed from: protected */
    public boolean polyBuffersContextIsOutdated() {
        return !this.pgl.contextIsCurrent(this.polyBuffersContext);
    }

    /* access modifiers changed from: protected */
    public void popFramebuffer() {
        PGraphicsOpenGL primaryPG = getPrimaryPG();
        if (primaryPG.fbStackDepth == 0) {
            throw new RuntimeException("popFramebuffer call is unbalanced.");
        }
        primaryPG.fbStackDepth--;
        FrameBuffer frameBuffer = primaryPG.fbStack[primaryPG.fbStackDepth];
        if (primaryPG.currentFramebuffer != frameBuffer) {
            primaryPG.currentFramebuffer.finish();
            primaryPG.currentFramebuffer = frameBuffer;
            if (primaryPG.currentFramebuffer != null) {
                primaryPG.currentFramebuffer.bind();
            }
        }
    }

    public void popMatrix() {
        if (this.modelviewStackDepth == 0) {
            throw new RuntimeException(PConstants.ERROR_PUSHMATRIX_UNDERFLOW);
        }
        this.modelviewStackDepth--;
        this.modelview.set(this.modelviewStack[this.modelviewStackDepth]);
        this.modelviewInv.set(this.modelviewInvStack[this.modelviewStackDepth]);
        this.camera.set(this.cameraStack[this.modelviewStackDepth]);
        this.cameraInv.set(this.cameraInvStack[this.modelviewStackDepth]);
        updateProjmodelview();
    }

    public void popProjection() {
        flush();
        if (this.projectionStackDepth == 0) {
            throw new RuntimeException(PConstants.ERROR_PUSHMATRIX_UNDERFLOW);
        }
        this.projectionStackDepth--;
        this.projection.set(this.projectionStack[this.projectionStackDepth]);
        updateProjmodelview();
    }

    public void popStyle() {
        boolean z = this.setAmbient;
        super.popStyle();
        if (!z) {
            this.setAmbient = false;
        }
    }

    public void printCamera() {
        this.camera.print();
    }

    public void printMatrix() {
        this.modelview.print();
    }

    public void printProjection() {
        this.projection.print();
    }

    /* access modifiers changed from: protected */
    public void processImageBeforeAsyncSave(PImage pImage) {
        if (pImage.format == -1) {
            PGL.nativeToJavaARGB(pImage.pixels, pImage.width, pImage.height);
            pImage.format = 2;
        } else if (pImage.format == -2) {
            PGL.nativeToJavaRGB(pImage.pixels, pImage.width, pImage.height);
            pImage.format = 1;
        }
    }

    /* access modifiers changed from: protected */
    public void pushFramebuffer() {
        PGraphicsOpenGL primaryPG = getPrimaryPG();
        if (primaryPG.fbStackDepth == 16) {
            throw new RuntimeException("Too many pushFramebuffer calls");
        }
        primaryPG.fbStack[primaryPG.fbStackDepth] = primaryPG.currentFramebuffer;
        primaryPG.fbStackDepth++;
    }

    public void pushMatrix() {
        if (this.modelviewStackDepth == 32) {
            throw new RuntimeException(PConstants.ERROR_PUSHMATRIX_OVERFLOW);
        }
        this.modelview.get(this.modelviewStack[this.modelviewStackDepth]);
        this.modelviewInv.get(this.modelviewInvStack[this.modelviewStackDepth]);
        this.camera.get(this.cameraStack[this.modelviewStackDepth]);
        this.cameraInv.get(this.cameraInvStack[this.modelviewStackDepth]);
        this.modelviewStackDepth++;
    }

    public void pushProjection() {
        if (this.projectionStackDepth == 32) {
            throw new RuntimeException(PConstants.ERROR_PUSHMATRIX_OVERFLOW);
        }
        this.projection.get(this.projectionStack[this.projectionStackDepth]);
        this.projectionStackDepth++;
    }

    public void quad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        beginShape(17);
        this.defaultEdges = false;
        this.normalMode = 1;
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addQuad(f, f2, 0.0f, f3, f4, 0.0f, f5, f6, 0.0f, f7, f8, 0.0f, this.stroke);
        endShape();
    }

    public void quadraticVertex(float f, float f2, float f3, float f4) {
        quadraticVertexImpl(f, f2, 0.0f, f3, f4, 0.0f);
    }

    public void quadraticVertex(float f, float f2, float f3, float f4, float f5, float f6) {
        quadraticVertexImpl(f, f2, f3, f4, f5, f6);
    }

    /* access modifiers changed from: protected */
    public void quadraticVertexImpl(float f, float f2, float f3, float f4, float f5, float f6) {
        bezierVertexCheck(this.shape, this.inGeo.vertexCount);
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addQuadraticVertex(f, f2, f3, f4, f5, f6, vertexBreak());
    }

    /* access modifiers changed from: package-private */
    public void rawLines() {
        this.raw.colorMode(1);
        this.raw.noFill();
        this.raw.strokeCap(this.strokeCap);
        this.raw.strokeJoin(this.strokeJoin);
        this.raw.beginShape(5);
        float[] fArr = this.tessGeo.lineVertices;
        int[] iArr = this.tessGeo.lineColors;
        float[] fArr2 = this.tessGeo.lineDirections;
        short[] sArr = this.tessGeo.lineIndices;
        IndexCache indexCache = this.tessGeo.lineIndexCache;
        for (int i = 0; i < indexCache.size; i++) {
            int i2 = indexCache.indexOffset[i];
            int i3 = indexCache.indexCount[i];
            int i4 = indexCache.vertexOffset[i];
            for (int i5 = i2 / 6; i5 < (i2 + i3) / 6; i5++) {
                int i6 = sArr[(i5 * 6) + 0] + i4;
                int i7 = sArr[(i5 * 6) + 5] + i4;
                float f = 2.0f * fArr2[(i6 * 4) + 3];
                float f2 = 2.0f * fArr2[(i7 * 4) + 3];
                if (!zero(f)) {
                    float[] fArr3 = {0.0f, 0.0f, 0.0f, 0.0f};
                    float[] fArr4 = {0.0f, 0.0f, 0.0f, 0.0f};
                    int nativeToJavaARGB = PGL.nativeToJavaARGB(iArr[i6]);
                    int nativeToJavaARGB2 = PGL.nativeToJavaARGB(iArr[i7]);
                    if (this.flushMode == 0) {
                        float[] fArr5 = {0.0f, 0.0f, 0.0f, 0.0f};
                        float[] fArr6 = {0.0f, 0.0f, 0.0f, 0.0f};
                        PApplet.arrayCopy(fArr, i6 * 4, fArr5, 0, 4);
                        PApplet.arrayCopy(fArr, i7 * 4, fArr6, 0, 4);
                        this.modelview.mult(fArr5, fArr3);
                        this.modelview.mult(fArr6, fArr4);
                    } else {
                        PApplet.arrayCopy(fArr, i6 * 4, fArr3, 0, 4);
                        PApplet.arrayCopy(fArr, i7 * 4, fArr4, 0, 4);
                    }
                    if (this.raw.is3D()) {
                        this.raw.strokeWeight(f);
                        this.raw.stroke(nativeToJavaARGB);
                        this.raw.vertex(fArr3[0], fArr3[1], fArr3[2]);
                        this.raw.strokeWeight(f2);
                        this.raw.stroke(nativeToJavaARGB2);
                        this.raw.vertex(fArr4[0], fArr4[1], fArr4[2]);
                    } else if (this.raw.is2D()) {
                        float screenXImpl = screenXImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                        float screenYImpl = screenYImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                        float screenXImpl2 = screenXImpl(fArr4[0], fArr4[1], fArr4[2], fArr4[3]);
                        float screenYImpl2 = screenYImpl(fArr4[0], fArr4[1], fArr4[2], fArr4[3]);
                        this.raw.strokeWeight(f);
                        this.raw.stroke(nativeToJavaARGB);
                        this.raw.vertex(screenXImpl, screenYImpl);
                        this.raw.strokeWeight(f2);
                        this.raw.stroke(nativeToJavaARGB2);
                        this.raw.vertex(screenXImpl2, screenYImpl2);
                    }
                }
            }
        }
        this.raw.endShape();
    }

    /* access modifiers changed from: package-private */
    public void rawPoints() {
        float f;
        int i;
        this.raw.colorMode(1);
        this.raw.noFill();
        this.raw.strokeCap(this.strokeCap);
        this.raw.beginShape(3);
        float[] fArr = this.tessGeo.pointVertices;
        int[] iArr = this.tessGeo.pointColors;
        float[] fArr2 = this.tessGeo.pointOffsets;
        short[] sArr = this.tessGeo.pointIndices;
        IndexCache indexCache = this.tessGeo.pointIndexCache;
        for (int i2 = 0; i2 < indexCache.size; i2++) {
            int i3 = indexCache.indexOffset[i2];
            int i4 = indexCache.indexCount[i2];
            int i5 = indexCache.vertexOffset[i2];
            int i6 = i3;
            while (i6 < (i3 + i4) / 3) {
                float f2 = fArr2[(i6 * 2) + 2];
                if (0.0f < f2) {
                    f = f2 / 0.5f;
                    i = PApplet.min((int) MAX_POINT_ACCURACY, PApplet.max(20, (int) ((6.2831855f * f) / POINT_ACCURACY_FACTOR))) + 1;
                } else {
                    f = (-f2) / 0.5f;
                    i = 5;
                }
                int i7 = sArr[i6 * 3] + i5;
                int nativeToJavaARGB = PGL.nativeToJavaARGB(iArr[i7]);
                float[] fArr3 = {0.0f, 0.0f, 0.0f, 0.0f};
                if (this.flushMode == 0) {
                    float[] fArr4 = {0.0f, 0.0f, 0.0f, 0.0f};
                    PApplet.arrayCopy(fArr, i7 * 4, fArr4, 0, 4);
                    this.modelview.mult(fArr4, fArr3);
                } else {
                    PApplet.arrayCopy(fArr, i7 * 4, fArr3, 0, 4);
                }
                if (this.raw.is3D()) {
                    this.raw.strokeWeight(f);
                    this.raw.stroke(nativeToJavaARGB);
                    this.raw.vertex(fArr3[0], fArr3[1], fArr3[2]);
                } else if (this.raw.is2D()) {
                    float screenXImpl = screenXImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                    float screenYImpl = screenYImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                    this.raw.strokeWeight(f);
                    this.raw.stroke(nativeToJavaARGB);
                    this.raw.vertex(screenXImpl, screenYImpl);
                }
                i6 += i;
            }
        }
        this.raw.endShape();
    }

    /* access modifiers changed from: package-private */
    public void rawPolys() {
        this.raw.colorMode(1);
        this.raw.noStroke();
        this.raw.beginShape(9);
        float[] fArr = this.tessGeo.polyVertices;
        int[] iArr = this.tessGeo.polyColors;
        float[] fArr2 = this.tessGeo.polyTexCoords;
        short[] sArr = this.tessGeo.polyIndices;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < this.texCache.size) {
                PImage textureImage = this.texCache.getTextureImage(i2);
                int i3 = this.texCache.firstCache[i2];
                int i4 = this.texCache.lastCache[i2];
                IndexCache indexCache = this.tessGeo.polyIndexCache;
                int i5 = i3;
                while (i5 <= i4) {
                    int i6 = i5 == i3 ? this.texCache.firstIndex[i2] : indexCache.indexOffset[i5];
                    int i7 = i5 == i4 ? (this.texCache.lastIndex[i2] - i6) + 1 : (indexCache.indexOffset[i5] + indexCache.indexCount[i5]) - i6;
                    int i8 = indexCache.vertexOffset[i5];
                    int i9 = i6 / 3;
                    while (true) {
                        int i10 = i9;
                        if (i10 >= (i6 + i7) / 3) {
                            break;
                        }
                        int i11 = i8 + sArr[(i10 * 3) + 0];
                        int i12 = i8 + sArr[(i10 * 3) + 1];
                        int i13 = i8 + sArr[(i10 * 3) + 2];
                        float[] fArr3 = {0.0f, 0.0f, 0.0f, 0.0f};
                        float[] fArr4 = {0.0f, 0.0f, 0.0f, 0.0f};
                        float[] fArr5 = {0.0f, 0.0f, 0.0f, 0.0f};
                        int nativeToJavaARGB = PGL.nativeToJavaARGB(iArr[i11]);
                        int nativeToJavaARGB2 = PGL.nativeToJavaARGB(iArr[i12]);
                        int nativeToJavaARGB3 = PGL.nativeToJavaARGB(iArr[i13]);
                        if (this.flushMode == 0) {
                            float[] fArr6 = {0.0f, 0.0f, 0.0f, 0.0f};
                            float[] fArr7 = {0.0f, 0.0f, 0.0f, 0.0f};
                            float[] fArr8 = {0.0f, 0.0f, 0.0f, 0.0f};
                            PApplet.arrayCopy(fArr, i11 * 4, fArr6, 0, 4);
                            PApplet.arrayCopy(fArr, i12 * 4, fArr7, 0, 4);
                            PApplet.arrayCopy(fArr, i13 * 4, fArr8, 0, 4);
                            this.modelview.mult(fArr6, fArr3);
                            this.modelview.mult(fArr7, fArr4);
                            this.modelview.mult(fArr8, fArr5);
                        } else {
                            PApplet.arrayCopy(fArr, i11 * 4, fArr3, 0, 4);
                            PApplet.arrayCopy(fArr, i12 * 4, fArr4, 0, 4);
                            PApplet.arrayCopy(fArr, i13 * 4, fArr5, 0, 4);
                        }
                        if (textureImage != null) {
                            this.raw.texture(textureImage);
                            if (this.raw.is3D()) {
                                this.raw.fill(nativeToJavaARGB);
                                this.raw.vertex(fArr3[0], fArr3[1], fArr3[2], fArr2[(i11 * 2) + 0], fArr2[(i11 * 2) + 1]);
                                this.raw.fill(nativeToJavaARGB2);
                                this.raw.vertex(fArr4[0], fArr4[1], fArr4[2], fArr2[(i12 * 2) + 0], fArr2[(i12 * 2) + 1]);
                                this.raw.fill(nativeToJavaARGB3);
                                this.raw.vertex(fArr5[0], fArr5[1], fArr5[2], fArr2[(i13 * 2) + 0], fArr2[(i13 * 2) + 1]);
                            } else if (this.raw.is2D()) {
                                float screenXImpl = screenXImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                                float screenYImpl = screenYImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                                float screenXImpl2 = screenXImpl(fArr4[0], fArr4[1], fArr4[2], fArr4[3]);
                                float screenYImpl2 = screenYImpl(fArr4[0], fArr4[1], fArr4[2], fArr4[3]);
                                float screenXImpl3 = screenXImpl(fArr5[0], fArr5[1], fArr5[2], fArr5[3]);
                                float screenYImpl3 = screenYImpl(fArr5[0], fArr5[1], fArr5[2], fArr5[3]);
                                this.raw.fill(nativeToJavaARGB);
                                this.raw.vertex(screenXImpl, screenYImpl, fArr2[(i11 * 2) + 0], fArr2[(i11 * 2) + 1]);
                                this.raw.fill(nativeToJavaARGB2);
                                this.raw.vertex(screenXImpl2, screenYImpl2, fArr2[(i12 * 2) + 0], fArr2[(i12 * 2) + 1]);
                                this.raw.fill(nativeToJavaARGB2);
                                this.raw.vertex(screenXImpl3, screenYImpl3, fArr2[(i13 * 2) + 0], fArr2[(i13 * 2) + 1]);
                            }
                        } else if (this.raw.is3D()) {
                            this.raw.fill(nativeToJavaARGB);
                            this.raw.vertex(fArr3[0], fArr3[1], fArr3[2]);
                            this.raw.fill(nativeToJavaARGB2);
                            this.raw.vertex(fArr4[0], fArr4[1], fArr4[2]);
                            this.raw.fill(nativeToJavaARGB3);
                            this.raw.vertex(fArr5[0], fArr5[1], fArr5[2]);
                        } else if (this.raw.is2D()) {
                            float screenXImpl4 = screenXImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                            float screenYImpl4 = screenYImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                            float screenXImpl5 = screenXImpl(fArr4[0], fArr4[1], fArr4[2], fArr4[3]);
                            float screenYImpl5 = screenYImpl(fArr4[0], fArr4[1], fArr4[2], fArr4[3]);
                            float screenXImpl6 = screenXImpl(fArr5[0], fArr5[1], fArr5[2], fArr5[3]);
                            float screenYImpl6 = screenYImpl(fArr5[0], fArr5[1], fArr5[2], fArr5[3]);
                            this.raw.fill(nativeToJavaARGB);
                            this.raw.vertex(screenXImpl4, screenYImpl4);
                            this.raw.fill(nativeToJavaARGB2);
                            this.raw.vertex(screenXImpl5, screenYImpl5);
                            this.raw.fill(nativeToJavaARGB3);
                            this.raw.vertex(screenXImpl6, screenYImpl6);
                        }
                        i9 = i10 + 1;
                    }
                    i5++;
                }
                i = i2 + 1;
            } else {
                this.raw.endShape();
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void rawSortedPolys() {
        this.raw.colorMode(1);
        this.raw.noStroke();
        this.raw.beginShape(9);
        float[] fArr = this.tessGeo.polyVertices;
        int[] iArr = this.tessGeo.polyColors;
        float[] fArr2 = this.tessGeo.polyTexCoords;
        short[] sArr = this.tessGeo.polyIndices;
        this.sorter.sort(this.tessGeo);
        int[] iArr2 = this.sorter.triangleIndices;
        int[] iArr3 = this.sorter.texMap;
        int[] iArr4 = this.sorter.voffsetMap;
        int[] iArr5 = this.tessGeo.polyIndexCache.vertexOffset;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < this.tessGeo.polyIndexCount / 3) {
                int i3 = iArr2[i2];
                PImage textureImage = this.texCache.getTextureImage(iArr3[i3]);
                int i4 = iArr5[iArr4[i3]];
                int i5 = i4 + sArr[(i3 * 3) + 0];
                int i6 = i4 + sArr[(i3 * 3) + 1];
                int i7 = i4 + sArr[(i3 * 3) + 2];
                float[] fArr3 = {0.0f, 0.0f, 0.0f, 0.0f};
                float[] fArr4 = {0.0f, 0.0f, 0.0f, 0.0f};
                float[] fArr5 = {0.0f, 0.0f, 0.0f, 0.0f};
                int nativeToJavaARGB = PGL.nativeToJavaARGB(iArr[i5]);
                int nativeToJavaARGB2 = PGL.nativeToJavaARGB(iArr[i6]);
                int nativeToJavaARGB3 = PGL.nativeToJavaARGB(iArr[i7]);
                if (this.flushMode == 0) {
                    float[] fArr6 = {0.0f, 0.0f, 0.0f, 0.0f};
                    float[] fArr7 = {0.0f, 0.0f, 0.0f, 0.0f};
                    float[] fArr8 = {0.0f, 0.0f, 0.0f, 0.0f};
                    PApplet.arrayCopy(fArr, i5 * 4, fArr6, 0, 4);
                    PApplet.arrayCopy(fArr, i6 * 4, fArr7, 0, 4);
                    PApplet.arrayCopy(fArr, i7 * 4, fArr8, 0, 4);
                    this.modelview.mult(fArr6, fArr3);
                    this.modelview.mult(fArr7, fArr4);
                    this.modelview.mult(fArr8, fArr5);
                } else {
                    PApplet.arrayCopy(fArr, i5 * 4, fArr3, 0, 4);
                    PApplet.arrayCopy(fArr, i6 * 4, fArr4, 0, 4);
                    PApplet.arrayCopy(fArr, i7 * 4, fArr5, 0, 4);
                }
                if (textureImage != null) {
                    this.raw.texture(textureImage);
                    if (this.raw.is3D()) {
                        this.raw.fill(nativeToJavaARGB);
                        this.raw.vertex(fArr3[0], fArr3[1], fArr3[2], fArr2[(i5 * 2) + 0], fArr2[(i5 * 2) + 1]);
                        this.raw.fill(nativeToJavaARGB2);
                        this.raw.vertex(fArr4[0], fArr4[1], fArr4[2], fArr2[(i6 * 2) + 0], fArr2[(i6 * 2) + 1]);
                        this.raw.fill(nativeToJavaARGB3);
                        this.raw.vertex(fArr5[0], fArr5[1], fArr5[2], fArr2[(i7 * 2) + 0], fArr2[(i7 * 2) + 1]);
                    } else if (this.raw.is2D()) {
                        float screenXImpl = screenXImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                        float screenYImpl = screenYImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                        float screenXImpl2 = screenXImpl(fArr4[0], fArr4[1], fArr4[2], fArr4[3]);
                        float screenYImpl2 = screenYImpl(fArr4[0], fArr4[1], fArr4[2], fArr4[3]);
                        float screenXImpl3 = screenXImpl(fArr5[0], fArr5[1], fArr5[2], fArr5[3]);
                        float screenYImpl3 = screenYImpl(fArr5[0], fArr5[1], fArr5[2], fArr5[3]);
                        this.raw.fill(nativeToJavaARGB);
                        this.raw.vertex(screenXImpl, screenYImpl, fArr2[(i5 * 2) + 0], fArr2[(i5 * 2) + 1]);
                        this.raw.fill(nativeToJavaARGB2);
                        this.raw.vertex(screenXImpl2, screenYImpl2, fArr2[(i6 * 2) + 0], fArr2[(i6 * 2) + 1]);
                        this.raw.fill(nativeToJavaARGB2);
                        this.raw.vertex(screenXImpl3, screenYImpl3, fArr2[(i7 * 2) + 0], fArr2[(i7 * 2) + 1]);
                    }
                } else if (this.raw.is3D()) {
                    this.raw.fill(nativeToJavaARGB);
                    this.raw.vertex(fArr3[0], fArr3[1], fArr3[2]);
                    this.raw.fill(nativeToJavaARGB2);
                    this.raw.vertex(fArr4[0], fArr4[1], fArr4[2]);
                    this.raw.fill(nativeToJavaARGB3);
                    this.raw.vertex(fArr5[0], fArr5[1], fArr5[2]);
                } else if (this.raw.is2D()) {
                    float screenXImpl4 = screenXImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                    float screenYImpl4 = screenYImpl(fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                    float screenXImpl5 = screenXImpl(fArr4[0], fArr4[1], fArr4[2], fArr4[3]);
                    float screenYImpl5 = screenYImpl(fArr4[0], fArr4[1], fArr4[2], fArr4[3]);
                    float screenXImpl6 = screenXImpl(fArr5[0], fArr5[1], fArr5[2], fArr5[3]);
                    float screenYImpl6 = screenYImpl(fArr5[0], fArr5[1], fArr5[2], fArr5[3]);
                    this.raw.fill(nativeToJavaARGB);
                    this.raw.vertex(screenXImpl4, screenYImpl4);
                    this.raw.fill(nativeToJavaARGB2);
                    this.raw.vertex(screenXImpl5, screenYImpl5);
                    this.raw.fill(nativeToJavaARGB3);
                    this.raw.vertex(screenXImpl6, screenYImpl6);
                }
                i = i2 + 1;
            } else {
                this.raw.endShape();
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void readPixels() {
        updatePixelSize();
        beginPixelsOp(1);
        try {
            this.pgl.readPixelsImpl(0, 0, this.pixelWidth, this.pixelHeight, PGL.RGBA, PGL.UNSIGNED_BYTE, (Buffer) this.pixelBuffer);
        } catch (IndexOutOfBoundsException e) {
        }
        endPixelsOp();
        try {
            PGL.getIntArray(this.pixelBuffer, this.pixels);
            PGL.nativeToJavaARGB(this.pixels, this.pixelWidth, this.pixelHeight);
        } catch (ArrayIndexOutOfBoundsException e2) {
        }
    }

    /* access modifiers changed from: protected */
    public void rectImpl(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        beginShape(20);
        this.defaultEdges = false;
        this.normalMode = 1;
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addRect(f, f2, f3, f4, f5, f6, f7, f8, this.stroke);
        endShape(2);
    }

    public void removeCache(PImage pImage) {
        getPrimaryPG().cacheMap.remove(pImage);
    }

    /* access modifiers changed from: protected */
    public void removeFontTexture(PFont pFont) {
        getPrimaryPG().fontMap.remove(pFont);
    }

    /* access modifiers changed from: protected */
    public void report(String str) {
        int error;
        if (!this.hints[4] && (error = this.pgl.getError()) != 0) {
            PGraphics.showWarning("OpenGL error " + error + " at " + str + ": " + this.pgl.errorString(error));
        }
    }

    public void requestDraw() {
        if (!this.primaryGraphics) {
            return;
        }
        if (this.initialized) {
            if (this.sized) {
                this.pgl.reinitSurface();
            }
            if (this.parent.canDraw()) {
                this.pgl.requestDraw();
                return;
            }
            return;
        }
        initPrimary();
    }

    public void resetMatrix() {
        this.modelview.reset();
        this.modelviewInv.reset();
        this.projmodelview.set((PMatrix) this.projection);
        this.camera.reset();
        this.cameraInv.reset();
    }

    public void resetProjection() {
        flush();
        this.projection.reset();
        updateProjmodelview();
    }

    public void resetShader() {
        resetShader(9);
    }

    public void resetShader(int i) {
        flush();
        if (i == 9 || i == 17 || i == 20) {
            this.polyShader = null;
        } else if (i == 5) {
            this.lineShader = null;
        } else if (i == 3) {
            this.pointShader = null;
        } else {
            PGraphics.showWarning(UNKNOWN_SHADER_KIND_ERROR);
        }
    }

    public void resize(int i, int i2) {
        PGraphics.showMethodWarning("resize");
    }

    /* access modifiers changed from: protected */
    public void restartPGL() {
        this.initialized = false;
    }

    /* access modifiers changed from: protected */
    public void restoreGL() {
        blendMode(this.blendMode);
        if (this.hints[2]) {
            this.pgl.disable(PGL.DEPTH_TEST);
        } else {
            this.pgl.enable(PGL.DEPTH_TEST);
        }
        this.pgl.depthFunc(PGL.LEQUAL);
        if (this.smooth < 1) {
            this.pgl.disable(PGL.MULTISAMPLE);
        } else {
            this.pgl.enable(PGL.MULTISAMPLE);
            this.pgl.disable(PGL.POLYGON_SMOOTH);
        }
        this.pgl.viewport(this.viewport.get(0), this.viewport.get(1), this.viewport.get(2), this.viewport.get(3));
        if (this.clip) {
            this.pgl.enable(PGL.SCISSOR_TEST);
            this.pgl.scissor(this.clipRect[0], this.clipRect[1], this.clipRect[2], this.clipRect[3]);
        } else {
            this.pgl.disable(PGL.SCISSOR_TEST);
        }
        this.pgl.frontFace(PGL.CW);
        this.pgl.disable(PGL.CULL_FACE);
        this.pgl.activeTexture(PGL.TEXTURE0);
        if (this.hints[5]) {
            this.pgl.depthMask(false);
        } else {
            this.pgl.depthMask(true);
        }
        FrameBuffer currentFB = getCurrentFB();
        if (currentFB != null) {
            currentFB.bind();
            if (drawBufferSupported) {
                this.pgl.drawBuffer(currentFB.getDefaultDrawBuffer());
            }
        }
    }

    public void rotate(float f) {
        rotateImpl(f, 0.0f, 0.0f, 1.0f);
    }

    public void rotate(float f, float f2, float f3, float f4) {
        rotateImpl(f, f2, f3, f4);
    }

    /* access modifiers changed from: protected */
    public void rotateImpl(float f, float f2, float f3, float f4) {
        float f5 = (f2 * f2) + (f3 * f3) + (f4 * f4);
        if (!zero(f5)) {
            if (diff(f5, 1.0f)) {
                float sqrt = PApplet.sqrt(f5);
                f2 /= sqrt;
                f3 /= sqrt;
                f4 /= sqrt;
            }
            this.modelview.rotate(f, f2, f3, f4);
            invRotate(this.modelviewInv, f, f2, f3, f4);
            updateProjmodelview();
        }
    }

    public void rotateX(float f) {
        rotateImpl(f, 1.0f, 0.0f, 0.0f);
    }

    public void rotateY(float f) {
        rotateImpl(f, 0.0f, 1.0f, 0.0f);
    }

    public void rotateZ(float f) {
        rotateImpl(f, 0.0f, 0.0f, 1.0f);
    }

    public boolean save(String str) {
        return saveImpl(str);
    }

    public boolean saveImpl(String str) {
        return super.save(str);
    }

    public void scale(float f) {
        scaleImpl(f, f, f);
    }

    public void scale(float f, float f2) {
        scaleImpl(f, f2, 1.0f);
    }

    public void scale(float f, float f2, float f3) {
        scaleImpl(f, f2, f3);
    }

    /* access modifiers changed from: protected */
    public void scaleImpl(float f, float f2, float f3) {
        this.modelview.scale(f, f2, f3);
        invScale(this.modelviewInv, f, f2, f3);
        this.projmodelview.scale(f, f2, f3);
    }

    public float screenX(float f, float f2) {
        return screenXImpl(f, f2, 0.0f);
    }

    public float screenX(float f, float f2, float f3) {
        return screenXImpl(f, f2, f3);
    }

    /* access modifiers changed from: protected */
    public float screenXImpl(float f, float f2, float f3) {
        return screenXImpl((this.modelview.m00 * f) + (this.modelview.m01 * f2) + (this.modelview.m02 * f3) + this.modelview.m03, (this.modelview.m10 * f) + (this.modelview.m11 * f2) + (this.modelview.m12 * f3) + this.modelview.m13, (this.modelview.m20 * f) + (this.modelview.m21 * f2) + (this.modelview.m22 * f3) + this.modelview.m23, (this.modelview.m30 * f) + (this.modelview.m31 * f2) + (this.modelview.m32 * f3) + this.modelview.m33);
    }

    /* access modifiers changed from: protected */
    public float screenXImpl(float f, float f2, float f3, float f4) {
        float f5 = (this.projection.m00 * f) + (this.projection.m01 * f2) + (this.projection.m02 * f3) + (this.projection.m03 * f4);
        float f6 = (this.projection.m30 * f) + (this.projection.m31 * f2) + (this.projection.m32 * f3) + (this.projection.m33 * f4);
        if (nonZero(f6)) {
            f5 /= f6;
        }
        return ((f5 + 1.0f) * ((float) this.width)) / 2.0f;
    }

    public float screenY(float f, float f2) {
        return screenYImpl(f, f2, 0.0f);
    }

    public float screenY(float f, float f2, float f3) {
        return screenYImpl(f, f2, f3);
    }

    /* access modifiers changed from: protected */
    public float screenYImpl(float f, float f2, float f3) {
        return screenYImpl((this.modelview.m00 * f) + (this.modelview.m01 * f2) + (this.modelview.m02 * f3) + this.modelview.m03, (this.modelview.m10 * f) + (this.modelview.m11 * f2) + (this.modelview.m12 * f3) + this.modelview.m13, (this.modelview.m20 * f) + (this.modelview.m21 * f2) + (this.modelview.m22 * f3) + this.modelview.m23, (this.modelview.m30 * f) + (this.modelview.m31 * f2) + (this.modelview.m32 * f3) + this.modelview.m33);
    }

    /* access modifiers changed from: protected */
    public float screenYImpl(float f, float f2, float f3, float f4) {
        float f5 = (this.projection.m10 * f) + (this.projection.m11 * f2) + (this.projection.m12 * f3) + (this.projection.m13 * f4);
        float f6 = (this.projection.m30 * f) + (this.projection.m31 * f2) + (this.projection.m32 * f3) + (this.projection.m33 * f4);
        if (nonZero(f6)) {
            f5 /= f6;
        }
        float f7 = f5 + 1.0f;
        return ((float) this.height) - ((f7 * ((float) this.height)) / 2.0f);
    }

    public float screenZ(float f, float f2, float f3) {
        return screenZImpl(f, f2, f3);
    }

    /* access modifiers changed from: protected */
    public float screenZImpl(float f, float f2, float f3) {
        return screenZImpl((this.modelview.m00 * f) + (this.modelview.m01 * f2) + (this.modelview.m02 * f3) + this.modelview.m03, (this.modelview.m10 * f) + (this.modelview.m11 * f2) + (this.modelview.m12 * f3) + this.modelview.m13, (this.modelview.m20 * f) + (this.modelview.m21 * f2) + (this.modelview.m22 * f3) + this.modelview.m23, (this.modelview.m30 * f) + (this.modelview.m31 * f2) + (this.modelview.m32 * f3) + this.modelview.m33);
    }

    /* access modifiers changed from: protected */
    public float screenZImpl(float f, float f2, float f3, float f4) {
        float f5 = (this.projection.m20 * f) + (this.projection.m21 * f2) + (this.projection.m22 * f3) + (this.projection.m23 * f4);
        float f6 = (this.projection.m30 * f) + (this.projection.m31 * f2) + (this.projection.m32 * f3) + (this.projection.m33 * f4);
        if (nonZero(f6)) {
            f5 /= f6;
        }
        return (f5 + 1.0f) / 2.0f;
    }

    public void set(int i, int i2, int i3) {
        loadPixels();
        super.set(i, i2, i3);
    }

    public void setCache(PImage pImage, Object obj) {
        if (pImage instanceof PGraphicsOpenGL) {
            getPrimaryPG().cacheMap.put(pImage, new WeakReference(obj));
        } else {
            getPrimaryPG().cacheMap.put(pImage, obj);
        }
    }

    /* access modifiers changed from: protected */
    public void setCurrentPG() {
        this.currentPG = this;
    }

    /* access modifiers changed from: protected */
    public void setCurrentPG(PGraphicsOpenGL pGraphicsOpenGL) {
        this.currentPG = pGraphicsOpenGL;
    }

    /* access modifiers changed from: protected */
    public void setFlushMode(int i) {
        this.flushMode = i;
    }

    /* access modifiers changed from: protected */
    public void setFontTexture(PFont pFont, FontTexture fontTexture) {
        getPrimaryPG().fontMap.put(pFont, fontTexture);
    }

    public void setFrameRate(float f) {
        this.pgl.setFrameRate(f);
    }

    /* access modifiers changed from: protected */
    public void setFramebuffer(FrameBuffer frameBuffer) {
        PGraphicsOpenGL primaryPG = getPrimaryPG();
        if (primaryPG.currentFramebuffer != frameBuffer) {
            primaryPG.currentFramebuffer = frameBuffer;
            if (primaryPG.currentFramebuffer != null) {
                primaryPG.currentFramebuffer.bind();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setGLSettings() {
        this.inGeo.clear();
        this.tessGeo.clear();
        this.texCache.clear();
        super.noTexture();
        blendModeImpl();
        if (this.hints[2]) {
            this.pgl.disable(PGL.DEPTH_TEST);
        } else {
            this.pgl.enable(PGL.DEPTH_TEST);
        }
        this.pgl.depthFunc(PGL.LEQUAL);
        if (this.hints[6]) {
            this.flushMode = 0;
        } else {
            this.flushMode = 1;
        }
        if (this.primaryGraphics) {
        }
        if (this.smooth < 1) {
            this.pgl.disable(PGL.MULTISAMPLE);
        } else if (!OPENGL_RENDERER.equals("VideoCore IV HW")) {
            this.pgl.enable(PGL.MULTISAMPLE);
        }
        if (!OPENGL_RENDERER.equals("VideoCore IV HW")) {
            this.pgl.disable(PGL.POLYGON_SMOOTH);
        }
        if (this.sized) {
            if (this.primaryGraphics) {
                background(this.backgroundColor);
            } else {
                background((this.backgroundColor & ViewCompat.MEASURED_SIZE_MASK) | 0);
            }
            defaultPerspective();
            defaultCamera();
            this.sized = false;
        } else {
            this.modelview.set((PMatrix) this.camera);
            this.modelviewInv.set((PMatrix) this.cameraInv);
            updateProjmodelview();
        }
        if (is3D()) {
            noLights();
            lightFalloff(1.0f, 0.0f, 0.0f);
            lightSpecular(0.0f, 0.0f, 0.0f);
        }
        this.pgl.frontFace(PGL.CW);
        this.pgl.disable(PGL.CULL_FACE);
        this.pgl.activeTexture(PGL.TEXTURE0);
        this.normalY = 0.0f;
        this.normalX = 0.0f;
        this.normalZ = 1.0f;
        this.pgl.depthMask(true);
        this.pgl.clearDepth(1.0f);
        this.pgl.clearStencil(0);
        this.pgl.clear(PGL.DEPTH_BUFFER_BIT | PGL.STENCIL_BUFFER_BIT);
        if (this.hints[5]) {
            this.pgl.depthMask(false);
        } else {
            this.pgl.depthMask(true);
        }
        this.pixelsOp = 0;
        this.modified = false;
        this.loaded = false;
    }

    /* access modifiers changed from: protected */
    public void setImpl(PImage pImage, int i, int i2, int i3, int i4, int i5, int i6) {
        updatePixelSize();
        loadPixels();
        int i7 = (pImage.pixelWidth * i2) + i;
        int i8 = (this.pixelWidth * i6) + i5;
        for (int i9 = i2; i9 < i2 + i4; i9++) {
            System.arraycopy(pImage.pixels, i7, this.pixels, i8, i3);
            i7 += pImage.pixelWidth;
            i8 += this.pixelWidth;
        }
        copy(pImage, i, i2, i3, i4, i5, i6, i3, i4);
    }

    public void setMatrix(PMatrix2D pMatrix2D) {
        resetMatrix();
        applyMatrix(pMatrix2D);
    }

    public void setMatrix(PMatrix3D pMatrix3D) {
        resetMatrix();
        applyMatrix(pMatrix3D);
    }

    public void setParent(PApplet pApplet) {
        super.setParent(pApplet);
        if (this.pgl != null) {
            this.pgl.sketch = pApplet;
        }
    }

    public void setPrimary(boolean z) {
        super.setPrimary(z);
        this.pgl.setPrimary(z);
        this.format = 2;
        if (z) {
            this.fbStack = new FrameBuffer[16];
            this.fontMap = new WeakHashMap<>();
            this.tessellator = new Tessellator();
            return;
        }
        this.tessellator = getPrimaryPG().tessellator;
    }

    public void setProjection(PMatrix3D pMatrix3D) {
        flush();
        this.projection.set((PMatrix) pMatrix3D);
        updateProjmodelview();
    }

    public void setSize(int i, int i2) {
        this.width = i;
        this.height = i2;
        updatePixelSize();
        this.cameraFOV = 1.0471976f;
        this.cameraX = ((float) this.width) / 2.0f;
        this.cameraY = ((float) this.height) / 2.0f;
        this.cameraZ = this.cameraY / ((float) Math.tan((double) (this.cameraFOV / 2.0f)));
        this.cameraNear = this.cameraZ / POINT_ACCURACY_FACTOR;
        this.cameraFar = this.cameraZ * POINT_ACCURACY_FACTOR;
        this.cameraAspect = ((float) this.width) / ((float) this.height);
        this.sized = true;
    }

    /* access modifiers changed from: protected */
    public void setViewport() {
        this.viewport.put(0, 0);
        this.viewport.put(1, 0);
        this.viewport.put(2, this.width);
        this.viewport.put(3, this.height);
        this.pgl.viewport(this.viewport.get(0), this.viewport.get(1), this.viewport.get(2), this.viewport.get(3));
    }

    public void shader(PShader pShader) {
        flush();
        if (pShader != null) {
            pShader.init();
        }
        if (pShader.isPolyShader()) {
            this.polyShader = pShader;
        } else if (pShader.isLineShader()) {
            this.lineShader = pShader;
        } else if (pShader.isPointShader()) {
            this.pointShader = pShader;
        } else {
            PGraphics.showWarning(UNKNOWN_SHADER_KIND_ERROR);
        }
    }

    public void shader(PShader pShader, int i) {
        flush();
        if (pShader != null) {
            pShader.init();
        }
        if (i == 9) {
            this.polyShader = pShader;
        } else if (i == 5) {
            this.lineShader = pShader;
        } else if (i == 3) {
            this.pointShader = pShader;
        } else {
            PGraphics.showWarning(UNKNOWN_SHADER_KIND_ERROR);
        }
    }

    /* access modifiers changed from: protected */
    public void shape(PShape pShape, float f, float f2, float f3) {
        if (pShape.isVisible()) {
            flush();
            pushMatrix();
            if (this.shapeMode == 3) {
                translate(f - (pShape.getWidth() / 2.0f), f2 - (pShape.getHeight() / 2.0f), f3 - (pShape.getDepth() / 2.0f));
            } else if (this.shapeMode == 0 || this.shapeMode == 1) {
                translate(f, f2, f3);
            }
            pShape.draw(this);
            popMatrix();
        }
    }

    /* access modifiers changed from: protected */
    public void shape(PShape pShape, float f, float f2, float f3, float f4, float f5, float f6) {
        if (pShape.isVisible()) {
            flush();
            pushMatrix();
            if (this.shapeMode == 3) {
                translate(f - (f4 / 2.0f), f2 - (f5 / 2.0f), f3 - (f6 / 2.0f));
                scale(f4 / pShape.getWidth(), f5 / pShape.getHeight(), f6 / pShape.getDepth());
            } else if (this.shapeMode == 0) {
                translate(f, f2, f3);
                scale(f4 / pShape.getWidth(), f5 / pShape.getHeight(), f6 / pShape.getDepth());
            } else if (this.shapeMode == 1) {
                translate(f, f2, f3);
                scale((f4 - f) / pShape.getWidth(), (f5 - f2) / pShape.getHeight(), (f6 - f3) / pShape.getDepth());
            }
            pShape.draw(this);
            popMatrix();
        }
    }

    public void shearX(float f) {
        applyMatrixImpl(1.0f, (float) Math.tan((double) f), 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void shearY(float f) {
        applyMatrixImpl(1.0f, 0.0f, 0.0f, 0.0f, (float) Math.tan((double) f), 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void sphere(float f) {
        if (this.sphereDetailU < 3 || this.sphereDetailV < 2) {
            sphereDetail(30);
        }
        beginShape(9);
        this.defaultEdges = false;
        this.normalMode = 2;
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        endShape(this.inGeo.addSphere(f, this.sphereDetailU, this.sphereDetailV, this.fill, this.stroke));
    }

    public void spotLight(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        enableLighting();
        if (this.lightCount == PGL.MAX_LIGHTS) {
            throw new RuntimeException("can only create " + PGL.MAX_LIGHTS + " lights");
        }
        this.lightType[this.lightCount] = 3;
        lightPosition(this.lightCount, f4, f5, f6, false);
        lightNormal(this.lightCount, f7, f8, f9);
        noLightAmbient(this.lightCount);
        lightDiffuse(this.lightCount, f, f2, f3);
        lightSpecular(this.lightCount, this.currentLightSpecular[0], this.currentLightSpecular[1], this.currentLightSpecular[2]);
        lightSpot(this.lightCount, f10, f11);
        lightFalloff(this.lightCount, this.currentLightFalloffConstant, this.currentLightFalloffLinear, this.currentLightFalloffQuadratic);
        this.lightCount++;
    }

    public void strokeCap(int i) {
        this.strokeCap = i;
    }

    public void strokeJoin(int i) {
        this.strokeJoin = i;
    }

    public void strokeWeight(float f) {
        this.strokeWeight = f;
    }

    /* access modifiers changed from: protected */
    public void swapOffscreenTextures() {
        FrameBuffer frameBuffer = this.offscreenFramebuffer;
        if (this.texture != null && this.ptexture != null && frameBuffer != null) {
            int i = this.texture.glName;
            this.texture.glName = this.ptexture.glName;
            this.ptexture.glName = i;
            frameBuffer.setColorBuffer(this.texture);
        }
    }

    /* access modifiers changed from: protected */
    public void tessellate(int i) {
        boolean z = false;
        this.tessellator.setInGeometry(this.inGeo);
        this.tessellator.setTessGeometry(this.tessGeo);
        this.tessellator.setFill(this.fill || this.textureImage != null);
        this.tessellator.setTexCache(this.texCache, this.textureImage);
        this.tessellator.setStroke(this.stroke);
        this.tessellator.setStrokeColor(this.strokeColor);
        this.tessellator.setStrokeWeight(this.strokeWeight);
        this.tessellator.setStrokeCap(this.strokeCap);
        this.tessellator.setStrokeJoin(this.strokeJoin);
        this.tessellator.setRenderer(this);
        this.tessellator.setTransform(this.modelview);
        this.tessellator.set3D(is3D());
        if (this.shape == 3) {
            this.tessellator.tessellatePoints();
        } else if (this.shape == 5) {
            this.tessellator.tessellateLines();
        } else if (this.shape == 50) {
            this.tessellator.tessellateLineStrip();
        } else if (this.shape == 51) {
            this.tessellator.tessellateLineLoop();
        } else if (this.shape == 8 || this.shape == 9) {
            if (this.stroke && this.defaultEdges) {
                this.inGeo.addTrianglesEdges();
            }
            if (this.normalMode == 0) {
                this.inGeo.calcTrianglesNormals();
            }
            this.tessellator.tessellateTriangles();
        } else if (this.shape == 11) {
            if (this.stroke && this.defaultEdges) {
                this.inGeo.addTriangleFanEdges();
            }
            if (this.normalMode == 0) {
                this.inGeo.calcTriangleFanNormals();
            }
            this.tessellator.tessellateTriangleFan();
        } else if (this.shape == 10) {
            if (this.stroke && this.defaultEdges) {
                this.inGeo.addTriangleStripEdges();
            }
            if (this.normalMode == 0) {
                this.inGeo.calcTriangleStripNormals();
            }
            this.tessellator.tessellateTriangleStrip();
        } else if (this.shape == 16 || this.shape == 17) {
            if (this.stroke && this.defaultEdges) {
                this.inGeo.addQuadsEdges();
            }
            if (this.normalMode == 0) {
                this.inGeo.calcQuadsNormals();
            }
            this.tessellator.tessellateQuads();
        } else if (this.shape == 18) {
            if (this.stroke && this.defaultEdges) {
                this.inGeo.addQuadStripEdges();
            }
            if (this.normalMode == 0) {
                this.inGeo.calcQuadStripNormals();
            }
            this.tessellator.tessellateQuadStrip();
        } else if (this.shape == 20) {
            Tessellator tessellator2 = this.tessellator;
            boolean z2 = i == 2;
            if (this.normalMode == 0) {
                z = true;
            }
            tessellator2.tessellatePolygon(true, z2, z);
        }
    }

    /* access modifiers changed from: protected */
    public void tessellate(int[] iArr) {
        this.tessellator.setInGeometry(this.inGeo);
        this.tessellator.setTessGeometry(this.tessGeo);
        this.tessellator.setFill(this.fill || this.textureImage != null);
        this.tessellator.setStroke(this.stroke);
        this.tessellator.setStrokeColor(this.strokeColor);
        this.tessellator.setStrokeWeight(this.strokeWeight);
        this.tessellator.setStrokeCap(this.strokeCap);
        this.tessellator.setStrokeJoin(this.strokeJoin);
        this.tessellator.setTexCache(this.texCache, this.textureImage);
        this.tessellator.setTransform(this.modelview);
        this.tessellator.set3D(is3D());
        if (this.stroke && this.defaultEdges) {
            this.inGeo.addTrianglesEdges();
        }
        if (this.normalMode == 0) {
            this.inGeo.calcTrianglesNormals();
        }
        this.tessellator.tessellateTriangles(iArr);
    }

    public float textAscent() {
        if (this.textFont == null) {
            defaultFontOrDeath("textAscent");
        }
        Object obj = this.textFont.getNative();
        float fontAscent = obj != null ? (float) this.pgl.getFontAscent(obj) : 0.0f;
        return fontAscent == 0.0f ? super.textAscent() : fontAscent;
    }

    /* access modifiers changed from: protected */
    public void textCharImpl(char c, float f, float f2) {
        PFont.Glyph glyph = this.textFont.getGlyph(c);
        if (glyph == null) {
            return;
        }
        if (this.textMode == 4) {
            FontTexture.TextureInfo texInfo = this.textTex.getTexInfo(glyph);
            if (texInfo == null) {
                texInfo = this.textTex.addToTexture(this, glyph);
            }
            float size = ((float) glyph.height) / ((float) this.textFont.getSize());
            float size2 = ((float) glyph.width) / ((float) this.textFont.getSize());
            float size3 = ((float) glyph.leftExtent) / ((float) this.textFont.getSize());
            float size4 = ((float) glyph.topExtent) / ((float) this.textFont.getSize());
            float f3 = (size3 * this.textSize) + f;
            float f4 = f2 - (size4 * this.textSize);
            textCharModelImpl(texInfo, f3, f4, f3 + (this.textSize * size2), f4 + (this.textSize * size));
        } else if (this.textMode == 5) {
            textCharShapeImpl(c, f, f2);
        }
    }

    /* access modifiers changed from: protected */
    public void textCharModelImpl(FontTexture.TextureInfo textureInfo, float f, float f2, float f3, float f4) {
        beginShape(17);
        texture(this.textTex.getTexture(textureInfo));
        vertex(f, f2, textureInfo.u0, textureInfo.v0);
        vertex(f3, f2, textureInfo.u1, textureInfo.v0);
        vertex(f3, f4, textureInfo.u1, textureInfo.v1);
        vertex(f, f4, textureInfo.u0, textureInfo.v1);
        endShape();
    }

    /* access modifiers changed from: protected */
    public void textCharShapeImpl(char c, float f, float f2) {
        boolean z;
        boolean z2 = this.stroke;
        this.stroke = false;
        PGL.FontOutline createFontOutline = this.pgl.createFontOutline(c, this.textFont.getNative());
        float[] fArr = new float[6];
        float f3 = 0.0f;
        float f4 = 0.0f;
        boolean z3 = false;
        beginShape();
        while (!createFontOutline.isDone()) {
            int currentSegment = createFontOutline.currentSegment(fArr);
            if (!z3) {
                beginContour();
                z = true;
            } else {
                z = z3;
            }
            if (currentSegment == PGL.SEG_MOVETO || currentSegment == PGL.SEG_LINETO) {
                vertex(fArr[0] + f, fArr[1] + f2);
                f3 = fArr[0];
                f4 = fArr[1];
            } else if (currentSegment == PGL.SEG_QUADTO) {
                int i = 1;
                while (true) {
                    int i2 = i;
                    if (i2 >= this.bezierDetail) {
                        break;
                    }
                    float f5 = ((float) i2) / ((float) this.bezierDetail);
                    vertex(bezierPoint(f3, f3 + ((float) (((double) ((fArr[0] - f3) * 2.0f)) / 3.0d)), ((float) (((double) ((fArr[0] - fArr[2]) * 2.0f)) / 3.0d)) + fArr[2], fArr[2], f5) + f, bezierPoint(f4, f4 + ((float) (((double) ((fArr[1] - f4) * 2.0f)) / 3.0d)), fArr[3] + ((float) (((double) ((fArr[1] - fArr[3]) * 2.0f)) / 3.0d)), fArr[3], f5) + f2);
                    i = i2 + 1;
                }
                f3 = fArr[2];
                f4 = fArr[3];
            } else if (currentSegment == PGL.SEG_CUBICTO) {
                int i3 = 1;
                while (true) {
                    int i4 = i3;
                    if (i4 >= this.bezierDetail) {
                        break;
                    }
                    float f6 = ((float) i4) / ((float) this.bezierDetail);
                    vertex(bezierPoint(f3, fArr[0], fArr[2], fArr[4], f6) + f, bezierPoint(f4, fArr[1], fArr[3], fArr[5], f6) + f2);
                    i3 = i4 + 1;
                }
                f3 = fArr[4];
                f4 = fArr[5];
            } else if (currentSegment == PGL.SEG_CLOSE) {
                endContour();
                z = false;
            }
            z3 = z;
            createFontOutline.next();
        }
        endShape();
        this.stroke = z2;
    }

    public float textDescent() {
        if (this.textFont == null) {
            defaultFontOrDeath("textDescent");
        }
        Object obj = this.textFont.getNative();
        float fontDescent = obj != null ? (float) this.pgl.getFontDescent(obj) : 0.0f;
        return fontDescent == 0.0f ? super.textDescent() : fontDescent;
    }

    /* access modifiers changed from: protected */
    public void textLineImpl(char[] cArr, int i, int i2, float f, float f2) {
        if (this.textMode == 4) {
            this.textTex = getFontTexture(this.textFont);
            if (this.textTex == null || this.textTex.contextIsOutdated()) {
                this.textTex = new FontTexture(this, this.textFont, is3D());
                setFontTexture(this.textFont, this.textTex);
            }
            this.textTex.begin();
            int i3 = this.textureMode;
            boolean z = this.stroke;
            float f3 = this.normalX;
            float f4 = this.normalY;
            float f5 = this.normalZ;
            boolean z2 = this.tint;
            int i4 = this.tintColor;
            int i5 = this.blendMode;
            this.textureMode = 1;
            this.stroke = false;
            this.normalX = 0.0f;
            this.normalY = 0.0f;
            this.normalZ = 1.0f;
            this.tint = true;
            this.tintColor = this.fillColor;
            blendMode(1);
            super.textLineImpl(cArr, i, i2, f, f2);
            this.textureMode = i3;
            this.stroke = z;
            this.normalX = f3;
            this.normalY = f4;
            this.normalZ = f5;
            this.tint = z2;
            this.tintColor = i4;
            blendMode(i5);
            this.textTex.end();
        } else if (this.textMode == 5) {
            super.textLineImpl(cArr, i, i2, f, f2);
        }
    }

    /* access modifiers changed from: protected */
    public boolean textModeCheck(int i) {
        return i == 4 || (i == 5 && PGL.SHAPE_TEXT_SUPPORTED);
    }

    /* access modifiers changed from: protected */
    public float textWidthImpl(char[] cArr, int i, int i2) {
        Object obj = this.textFont.getNative();
        float textWidth = obj != null ? (float) this.pgl.getTextWidth(obj, cArr, i, i2) : 0.0f;
        return textWidth == 0.0f ? super.textWidthImpl(cArr, i, i2) : textWidth;
    }

    public void textureSampling(int i) {
        this.textureSampling = i;
    }

    public void textureWrap(int i) {
        this.textureWrap = i;
    }

    public void translate(float f, float f2) {
        translateImpl(f, f2, 0.0f);
    }

    public void translate(float f, float f2, float f3) {
        translateImpl(f, f2, f3);
    }

    /* access modifiers changed from: protected */
    public void translateImpl(float f, float f2, float f3) {
        this.modelview.translate(f, f2, f3);
        invTranslate(this.modelviewInv, f, f2, f3);
        this.projmodelview.translate(f, f2, f3);
    }

    public void triangle(float f, float f2, float f3, float f4, float f5, float f6) {
        beginShape(9);
        this.defaultEdges = false;
        this.normalMode = 1;
        this.inGeo.setMaterial(this.fillColor, this.strokeColor, this.strokeWeight, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess);
        this.inGeo.setNormal(this.normalX, this.normalY, this.normalZ);
        this.inGeo.addTriangle(f, f2, 0.0f, f3, f4, 0.0f, f5, f6, 0.0f, this.fill, this.stroke);
        endShape();
    }

    /* access modifiers changed from: protected */
    public void unbindFrontTexture() {
        if (this.primaryGraphics) {
            this.pgl.unbindFrontTexture();
        } else {
            this.ptexture.unbind();
        }
    }

    /* access modifiers changed from: protected */
    public void unbindLineBuffers() {
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void unbindPointBuffers() {
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void unbindPolyBuffers() {
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    public void updateDisplay() {
        flush();
        beginPixelsOp(2);
        drawTexture();
        endPixelsOp();
    }

    /* access modifiers changed from: protected */
    public void updateGLModelview() {
        if (this.glModelview == null) {
            this.glModelview = new float[16];
        }
        this.glModelview[0] = this.modelview.m00;
        this.glModelview[1] = this.modelview.m10;
        this.glModelview[2] = this.modelview.m20;
        this.glModelview[3] = this.modelview.m30;
        this.glModelview[4] = this.modelview.m01;
        this.glModelview[5] = this.modelview.m11;
        this.glModelview[6] = this.modelview.m21;
        this.glModelview[7] = this.modelview.m31;
        this.glModelview[8] = this.modelview.m02;
        this.glModelview[9] = this.modelview.m12;
        this.glModelview[10] = this.modelview.m22;
        this.glModelview[11] = this.modelview.m32;
        this.glModelview[12] = this.modelview.m03;
        this.glModelview[13] = this.modelview.m13;
        this.glModelview[14] = this.modelview.m23;
        this.glModelview[15] = this.modelview.m33;
    }

    /* access modifiers changed from: protected */
    public void updateGLNormal() {
        if (this.glNormal == null) {
            this.glNormal = new float[9];
        }
        this.glNormal[0] = this.modelviewInv.m00;
        this.glNormal[1] = this.modelviewInv.m01;
        this.glNormal[2] = this.modelviewInv.m02;
        this.glNormal[3] = this.modelviewInv.m10;
        this.glNormal[4] = this.modelviewInv.m11;
        this.glNormal[5] = this.modelviewInv.m12;
        this.glNormal[6] = this.modelviewInv.m20;
        this.glNormal[7] = this.modelviewInv.m21;
        this.glNormal[8] = this.modelviewInv.m22;
    }

    /* access modifiers changed from: protected */
    public void updateGLProjection() {
        if (this.glProjection == null) {
            this.glProjection = new float[16];
        }
        this.glProjection[0] = this.projection.m00;
        this.glProjection[1] = this.projection.m10;
        this.glProjection[2] = this.projection.m20;
        this.glProjection[3] = this.projection.m30;
        this.glProjection[4] = this.projection.m01;
        this.glProjection[5] = this.projection.m11;
        this.glProjection[6] = this.projection.m21;
        this.glProjection[7] = this.projection.m31;
        this.glProjection[8] = this.projection.m02;
        this.glProjection[9] = this.projection.m12;
        this.glProjection[10] = this.projection.m22;
        this.glProjection[11] = this.projection.m32;
        this.glProjection[12] = this.projection.m03;
        this.glProjection[13] = this.projection.m13;
        this.glProjection[14] = this.projection.m23;
        this.glProjection[15] = this.projection.m33;
    }

    /* access modifiers changed from: protected */
    public void updateGLProjmodelview() {
        if (this.glProjmodelview == null) {
            this.glProjmodelview = new float[16];
        }
        this.glProjmodelview[0] = this.projmodelview.m00;
        this.glProjmodelview[1] = this.projmodelview.m10;
        this.glProjmodelview[2] = this.projmodelview.m20;
        this.glProjmodelview[3] = this.projmodelview.m30;
        this.glProjmodelview[4] = this.projmodelview.m01;
        this.glProjmodelview[5] = this.projmodelview.m11;
        this.glProjmodelview[6] = this.projmodelview.m21;
        this.glProjmodelview[7] = this.projmodelview.m31;
        this.glProjmodelview[8] = this.projmodelview.m02;
        this.glProjmodelview[9] = this.projmodelview.m12;
        this.glProjmodelview[10] = this.projmodelview.m22;
        this.glProjmodelview[11] = this.projmodelview.m32;
        this.glProjmodelview[12] = this.projmodelview.m03;
        this.glProjmodelview[13] = this.projmodelview.m13;
        this.glProjmodelview[14] = this.projmodelview.m23;
        this.glProjmodelview[15] = this.projmodelview.m33;
    }

    /* access modifiers changed from: protected */
    public void updateLineBuffers() {
        createLineBuffers();
        int i = this.tessGeo.lineVertexCount;
        int i2 = PGL.SIZEOF_FLOAT * i;
        int i3 = i * PGL.SIZEOF_INT;
        this.tessGeo.updateLineVerticesBuffer();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufLineVertex.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 4, this.tessGeo.lineVerticesBuffer, PGL.STATIC_DRAW);
        this.tessGeo.updateLineColorsBuffer();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufLineColor.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i3, this.tessGeo.lineColorsBuffer, PGL.STATIC_DRAW);
        this.tessGeo.updateLineDirectionsBuffer();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufLineAttrib.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 4, this.tessGeo.lineDirectionsBuffer, PGL.STATIC_DRAW);
        this.tessGeo.updateLineIndicesBuffer();
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, this.bufLineIndex.glId);
        this.pgl.bufferData(PGL.ELEMENT_ARRAY_BUFFER, this.tessGeo.lineIndexCount * PGL.SIZEOF_INDEX, this.tessGeo.lineIndicesBuffer, PGL.STATIC_DRAW);
    }

    /* access modifiers changed from: protected */
    public void updatePixelSize() {
        float pixelScale = this.pgl.getPixelScale();
        this.pixelWidth = (int) (((float) this.width) * pixelScale);
        this.pixelHeight = (int) (pixelScale * ((float) this.height));
    }

    /* access modifiers changed from: protected */
    public void updatePointBuffers() {
        createPointBuffers();
        int i = this.tessGeo.pointVertexCount;
        int i2 = PGL.SIZEOF_FLOAT * i;
        int i3 = i * PGL.SIZEOF_INT;
        this.tessGeo.updatePointVerticesBuffer();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPointVertex.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 4, this.tessGeo.pointVerticesBuffer, PGL.STATIC_DRAW);
        this.tessGeo.updatePointColorsBuffer();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPointColor.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i3, this.tessGeo.pointColorsBuffer, PGL.STATIC_DRAW);
        this.tessGeo.updatePointOffsetsBuffer();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPointAttrib.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 2, this.tessGeo.pointOffsetsBuffer, PGL.STATIC_DRAW);
        this.tessGeo.updatePointIndicesBuffer();
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, this.bufPointIndex.glId);
        this.pgl.bufferData(PGL.ELEMENT_ARRAY_BUFFER, this.tessGeo.pointIndexCount * PGL.SIZEOF_INDEX, this.tessGeo.pointIndicesBuffer, PGL.STATIC_DRAW);
    }

    /* access modifiers changed from: protected */
    public void updatePolyBuffers(boolean z, boolean z2, boolean z3, boolean z4) {
        createPolyBuffers();
        int i = this.tessGeo.polyVertexCount;
        int i2 = PGL.SIZEOF_FLOAT * i;
        int i3 = PGL.SIZEOF_INT * i;
        this.tessGeo.updatePolyVerticesBuffer();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyVertex.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 4, this.tessGeo.polyVerticesBuffer, PGL.STATIC_DRAW);
        this.tessGeo.updatePolyColorsBuffer();
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyColor.glId);
        this.pgl.bufferData(PGL.ARRAY_BUFFER, i3, this.tessGeo.polyColorsBuffer, PGL.STATIC_DRAW);
        if (z) {
            this.tessGeo.updatePolyAmbientBuffer();
            this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyAmbient.glId);
            this.pgl.bufferData(PGL.ARRAY_BUFFER, i3, this.tessGeo.polyAmbientBuffer, PGL.STATIC_DRAW);
            this.tessGeo.updatePolySpecularBuffer();
            this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolySpecular.glId);
            this.pgl.bufferData(PGL.ARRAY_BUFFER, i3, this.tessGeo.polySpecularBuffer, PGL.STATIC_DRAW);
            this.tessGeo.updatePolyEmissiveBuffer();
            this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyEmissive.glId);
            this.pgl.bufferData(PGL.ARRAY_BUFFER, i3, this.tessGeo.polyEmissiveBuffer, PGL.STATIC_DRAW);
            this.tessGeo.updatePolyShininessBuffer();
            this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyShininess.glId);
            this.pgl.bufferData(PGL.ARRAY_BUFFER, i2, this.tessGeo.polyShininessBuffer, PGL.STATIC_DRAW);
        }
        if (z || z3) {
            this.tessGeo.updatePolyNormalsBuffer();
            this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyNormal.glId);
            this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 3, this.tessGeo.polyNormalsBuffer, PGL.STATIC_DRAW);
        }
        if (z2 || z4) {
            this.tessGeo.updatePolyTexCoordsBuffer();
            this.pgl.bindBuffer(PGL.ARRAY_BUFFER, this.bufPolyTexcoord.glId);
            this.pgl.bufferData(PGL.ARRAY_BUFFER, i2 * 2, this.tessGeo.polyTexCoordsBuffer, PGL.STATIC_DRAW);
        }
        for (String str : this.polyAttribs.keySet()) {
            VertexAttribute vertexAttribute = (VertexAttribute) this.polyAttribs.get(str);
            this.tessGeo.updateAttribBuffer(str);
            this.pgl.bindBuffer(PGL.ARRAY_BUFFER, vertexAttribute.buf.glId);
            this.pgl.bufferData(PGL.ARRAY_BUFFER, vertexAttribute.sizeInBytes(i), this.tessGeo.polyAttribBuffers.get(str), PGL.STATIC_DRAW);
        }
        this.tessGeo.updatePolyIndicesBuffer();
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, this.bufPolyIndex.glId);
        this.pgl.bufferData(PGL.ELEMENT_ARRAY_BUFFER, this.tessGeo.polyIndexCount * PGL.SIZEOF_INDEX, this.tessGeo.polyIndicesBuffer, PGL.STATIC_DRAW);
    }

    public void updateProjmodelview() {
        this.projmodelview.set((PMatrix) this.projection);
        this.projmodelview.apply(this.modelview);
    }

    public void updateTexture() {
        if (this.texture != null) {
            this.texture.updateTexels();
        }
    }

    public void updateTexture(int i, int i2, int i3, int i4) {
        if (this.texture != null) {
            this.texture.updateTexels(i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: protected */
    public void updateTexture(PImage pImage, Texture texture2) {
        if (texture2 != null && pImage.isModified()) {
            int modifiedX1 = pImage.getModifiedX1();
            int modifiedY1 = pImage.getModifiedY1();
            Texture texture3 = texture2;
            texture3.set(pImage.pixels, modifiedX1, modifiedY1, pImage.getModifiedX2() - modifiedX1, pImage.getModifiedY2() - modifiedY1, pImage.format);
        }
        pImage.setModified(false);
    }

    public void vertex(float f, float f2) {
        vertexImpl(f, f2, 0.0f, 0.0f, 0.0f);
        if (this.textureImage != null) {
            PGraphics.showWarning(MISSING_UV_TEXCOORDS_ERROR);
        }
    }

    public void vertex(float f, float f2, float f3) {
        vertexImpl(f, f2, f3, 0.0f, 0.0f);
        if (this.textureImage != null) {
            PGraphics.showWarning(MISSING_UV_TEXCOORDS_ERROR);
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
        boolean z = this.textureImage != null;
        int i = 0;
        if (this.fill || z) {
            i = !z ? this.fillColor : this.tint ? this.tintColor : -1;
        }
        int i2 = 0;
        float f8 = 0.0f;
        if (this.stroke) {
            i2 = this.strokeColor;
            f8 = this.strokeWeight;
        }
        if (!z || this.textureMode != 2) {
            f6 = f5;
            f7 = f4;
        } else {
            float f9 = f4 / ((float) this.textureImage.width);
            f6 = f5 / ((float) this.textureImage.height);
            f7 = f9;
        }
        this.inGeo.addVertex(f, f2, f3, i, this.normalX, this.normalY, this.normalZ, f7, f6, i2, f8, this.ambientColor, this.specularColor, this.emissiveColor, this.shininess, 0, vertexBreak());
    }

    /* access modifiers changed from: protected */
    public PImage wrapTexture(Texture texture2) {
        PImage pImage = new PImage();
        pImage.parent = this.parent;
        pImage.width = texture2.width;
        pImage.height = texture2.height;
        pImage.format = 2;
        setCache(pImage, texture2);
        return pImage;
    }
}
