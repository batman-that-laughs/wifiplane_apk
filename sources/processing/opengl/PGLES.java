package processing.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.support.v4.view.InputDeviceCompat;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;
import processing.core.PApplet;
import processing.opengl.PGL;
import processing.opengl.tess.PGLU;
import processing.opengl.tess.PGLUtessellator;
import processing.opengl.tess.PGLUtessellatorCallbackAdapter;

public class PGLES extends PGL {
    protected static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    protected static final int EGL_COVERAGE_BUFFERS_NV = 12512;
    protected static final int EGL_COVERAGE_SAMPLES_NV = 12513;
    protected static final int EGL_OPENGL_ES2_BIT = 4;
    protected static final int GL_COVERAGE_BUFFER_BIT_NV = 32768;
    protected static AndroidConfigChooser configChooser;
    public static EGLContext context;
    public static GLSurfaceView glview;
    protected static int multisampleCount = 1;
    protected static AndroidRenderer renderer;
    protected static boolean usingCoverageMultisampling = false;
    protected static boolean usingMultisampling = false;
    public GL10 gl;
    public PGLU glu = new PGLU();

    protected class AndroidConfigChooser implements GLSurfaceView.EGLConfigChooser {
        public int alphaBits;
        public int alphaTarget;
        protected int[] attribsNoMSAA = {12352, 4, 12338, 0, 12344};
        public int blueBits;
        public int blueTarget;
        public int depthBits;
        public int depthTarget;
        public int greenBits;
        public int greenTarget;
        public int numSamples;
        public int redBits;
        public int redTarget;
        public int stencilBits;
        public int stencilTarget;
        public int[] tempValue = new int[1];

        public AndroidConfigChooser(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
            this.redTarget = i;
            this.greenTarget = i2;
            this.blueTarget = i3;
            this.alphaTarget = i4;
            this.depthTarget = i5;
            this.stencilTarget = i6;
            this.numSamples = i7;
        }

        public EGLConfig chooseBestConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr) {
            float f;
            EGLConfig eGLConfig = null;
            float f2 = Float.MAX_VALUE;
            int length = eGLConfigArr.length;
            int i = 0;
            while (i < length) {
                EGLConfig eGLConfig2 = eGLConfigArr[i];
                if ((findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12352, 0) & 4) != 0) {
                    int findConfigAttrib = findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12325, 0);
                    int findConfigAttrib2 = findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12326, 0);
                    int findConfigAttrib3 = findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12324, 0);
                    int findConfigAttrib4 = findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12323, 0);
                    int findConfigAttrib5 = findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12322, 0);
                    int findConfigAttrib6 = findConfigAttrib(egl10, eGLDisplay, eGLConfig2, 12321, 0);
                    f = (0.2f * ((float) PApplet.abs(findConfigAttrib3 - this.redTarget))) + (0.2f * ((float) PApplet.abs(findConfigAttrib4 - this.greenTarget))) + (0.2f * ((float) PApplet.abs(findConfigAttrib5 - this.blueTarget))) + (0.15f * ((float) PApplet.abs(findConfigAttrib6 - this.alphaTarget))) + (0.15f * ((float) PApplet.abs(findConfigAttrib - this.depthTarget))) + (0.1f * ((float) PApplet.abs(findConfigAttrib2 - this.stencilTarget)));
                    if (f < f2) {
                        this.redBits = findConfigAttrib3;
                        this.greenBits = findConfigAttrib4;
                        this.blueBits = findConfigAttrib5;
                        this.alphaBits = findConfigAttrib6;
                        this.depthBits = findConfigAttrib;
                        this.stencilBits = findConfigAttrib2;
                        i++;
                        f2 = f;
                        eGLConfig = eGLConfig2;
                    }
                }
                f = f2;
                eGLConfig2 = eGLConfig;
                i++;
                f2 = f;
                eGLConfig = eGLConfig2;
            }
            return eGLConfig;
        }

        public EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay) {
            EGLConfig[] chooseConfigWithAttribs;
            if (1 < this.numSamples) {
                chooseConfigWithAttribs = chooseConfigWithAttribs(egl10, eGLDisplay, new int[]{12352, 4, 12338, 1, 12337, this.numSamples, 12344});
                if (chooseConfigWithAttribs == null) {
                    chooseConfigWithAttribs = chooseConfigWithAttribs(egl10, eGLDisplay, new int[]{12352, 4, PGLES.EGL_COVERAGE_BUFFERS_NV, 1, PGLES.EGL_COVERAGE_SAMPLES_NV, this.numSamples, 12344});
                    if (chooseConfigWithAttribs == null) {
                        chooseConfigWithAttribs = chooseConfigWithAttribs(egl10, eGLDisplay, this.attribsNoMSAA);
                    } else {
                        PGLES.usingMultisampling = true;
                        PGLES.usingCoverageMultisampling = true;
                        PGLES.multisampleCount = this.numSamples;
                    }
                } else {
                    PGLES.usingMultisampling = true;
                    PGLES.usingCoverageMultisampling = false;
                    PGLES.multisampleCount = this.numSamples;
                }
            } else {
                chooseConfigWithAttribs = chooseConfigWithAttribs(egl10, eGLDisplay, this.attribsNoMSAA);
            }
            if (chooseConfigWithAttribs != null) {
                return chooseBestConfig(egl10, eGLDisplay, chooseConfigWithAttribs);
            }
            throw new IllegalArgumentException("No EGL configs match configSpec");
        }

        /* access modifiers changed from: protected */
        public EGLConfig[] chooseConfigWithAttribs(EGL10 egl10, EGLDisplay eGLDisplay, int[] iArr) {
            int[] iArr2 = new int[1];
            egl10.eglChooseConfig(eGLDisplay, iArr, (EGLConfig[]) null, 0, iArr2);
            int i = iArr2[0];
            if (i <= 0) {
                return null;
            }
            EGLConfig[] eGLConfigArr = new EGLConfig[i];
            egl10.eglChooseConfig(eGLDisplay, iArr, eGLConfigArr, i, iArr2);
            return eGLConfigArr;
        }

        /* access modifiers changed from: protected */
        public int findConfigAttrib(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
            return egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, this.tempValue) ? this.tempValue[0] : i2;
        }

        /* access modifiers changed from: protected */
        public String printConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
            int findConfigAttrib = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12324, 0);
            int findConfigAttrib2 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12323, 0);
            int findConfigAttrib3 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12322, 0);
            int findConfigAttrib4 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12321, 0);
            int findConfigAttrib5 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12325, 0);
            int findConfigAttrib6 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12326, 0);
            int findConfigAttrib7 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12352, 0);
            int findConfigAttrib8 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12333, 0);
            int findConfigAttrib9 = findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12320, 0);
            return String.format("EGLConfig rgba=%d%d%d%d depth=%d stencil=%d", new Object[]{Integer.valueOf(findConfigAttrib), Integer.valueOf(findConfigAttrib2), Integer.valueOf(findConfigAttrib3), Integer.valueOf(findConfigAttrib4), Integer.valueOf(findConfigAttrib5), Integer.valueOf(findConfigAttrib6)}) + " type=" + findConfigAttrib7 + " native=" + findConfigAttrib8 + " buffer size=" + findConfigAttrib9 + " buffer surface=" + findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12422, 0) + String.format(" caveat=0x%04x", new Object[]{Integer.valueOf(findConfigAttrib(egl10, eGLDisplay, eGLConfig, 12327, 0))});
        }
    }

    protected class AndroidContextFactory implements GLSurfaceView.EGLContextFactory {
        protected AndroidContextFactory() {
        }

        public EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
            return egl10.eglCreateContext(eGLDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, new int[]{PGLES.EGL_CONTEXT_CLIENT_VERSION, 2, 12344});
        }

        public void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
            egl10.eglDestroyContext(eGLDisplay, eGLContext);
        }
    }

    protected class AndroidRenderer implements GLSurfaceView.Renderer {
        public AndroidRenderer() {
        }

        public void onDrawFrame(GL10 gl10) {
            PGLES.this.gl = gl10;
            PGLES.this.glThread = Thread.currentThread();
            PGLES.this.sketch.handleDraw();
        }

        public void onSurfaceChanged(GL10 gl10, int i, int i2) {
            PGLES.this.gl = gl10;
            PGLES.this.sketch.displayHeight = i;
            PGLES.this.sketch.displayHeight = i2;
            PGLES.this.graphics.setSize(PGLES.this.sketch.sketchWidth(), PGLES.this.sketch.sketchHeight());
        }

        public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
            PGLES.this.gl = gl10;
            PGLES.context = ((EGL10) EGLContext.getEGL()).eglGetCurrentContext();
            PGLES.this.glContext = PGLES.context.hashCode();
            PGLES.this.glThread = Thread.currentThread();
            if (!PGLES.this.hasFBOs()) {
                throw new RuntimeException(PGL.MISSING_FBO_ERROR);
            } else if (!PGLES.this.hasShaders()) {
                throw new RuntimeException(PGL.MISSING_GLSL_ERROR);
            }
        }
    }

    protected class Tessellator implements PGL.Tessellator {
        protected PGL.TessellatorCallback callback;
        protected GLUCallback gluCallback = new GLUCallback();
        protected PGLUtessellator tess = PGLU.gluNewTess();

        protected class GLUCallback extends PGLUtessellatorCallbackAdapter {
            protected GLUCallback() {
            }

            public void begin(int i) {
                Tessellator.this.callback.begin(i);
            }

            public void combine(double[] dArr, Object[] objArr, float[] fArr, Object[] objArr2) {
                Tessellator.this.callback.combine(dArr, objArr, fArr, objArr2);
            }

            public void end() {
                Tessellator.this.callback.end();
            }

            public void error(int i) {
                Tessellator.this.callback.error(i);
            }

            public void vertex(Object obj) {
                Tessellator.this.callback.vertex(obj);
            }
        }

        public Tessellator(PGL.TessellatorCallback tessellatorCallback) {
            this.callback = tessellatorCallback;
            PGLU.gluTessCallback(this.tess, 100100, this.gluCallback);
            PGLU.gluTessCallback(this.tess, 100102, this.gluCallback);
            PGLU.gluTessCallback(this.tess, 100101, this.gluCallback);
            PGLU.gluTessCallback(this.tess, PGLU.GLU_TESS_COMBINE, this.gluCallback);
            PGLU.gluTessCallback(this.tess, 100103, this.gluCallback);
        }

        public void addVertex(double[] dArr) {
            PGLU.gluTessVertex(this.tess, dArr, 0, dArr);
        }

        public void beginContour() {
            PGLU.gluTessBeginContour(this.tess);
        }

        public void beginPolygon() {
            PGLU.gluTessBeginPolygon(this.tess, (Object) null);
        }

        public void endContour() {
            PGLU.gluTessEndContour(this.tess);
        }

        public void endPolygon() {
            PGLU.gluTessEndPolygon(this.tess);
        }

        public void setWindingRule(int i) {
            PGLU.gluTessProperty(this.tess, PGLU.GLU_TESS_WINDING_RULE, (double) i);
        }
    }

    static {
        SINGLE_BUFFERED = true;
        MIN_DIRECT_BUFFER_SIZE = 1;
        INDEX_TYPE = 5123;
        MIPMAPS_ENABLED = false;
        DEFAULT_IN_VERTICES = 16;
        DEFAULT_IN_EDGES = 32;
        DEFAULT_IN_TEXTURES = 16;
        DEFAULT_TESS_VERTICES = 16;
        DEFAULT_TESS_INDICES = 32;
        MIN_FONT_TEX_SIZE = 128;
        MAX_FONT_TEX_SIZE = 512;
        MAX_CAPS_JOINS_LENGTH = 1000;
        SHAPE_TEXT_SUPPORTED = false;
        FALSE = 0;
        TRUE = 1;
        INT = 5124;
        BYTE = 5120;
        SHORT = 5122;
        FLOAT = 5126;
        BOOL = 35670;
        UNSIGNED_INT = 5125;
        UNSIGNED_BYTE = 5121;
        UNSIGNED_SHORT = 5123;
        RGB = 6407;
        RGBA = 6408;
        ALPHA = 6406;
        LUMINANCE = 6409;
        LUMINANCE_ALPHA = 6410;
        UNSIGNED_SHORT_5_6_5 = 33635;
        UNSIGNED_SHORT_4_4_4_4 = 32819;
        UNSIGNED_SHORT_5_5_5_1 = 32820;
        RGBA4 = 32854;
        RGB5_A1 = 32855;
        RGB565 = 36194;
        RGB8 = 32849;
        RGBA8 = 32856;
        ALPHA8 = -1;
        READ_ONLY = -1;
        WRITE_ONLY = 35001;
        READ_WRITE = -1;
        TESS_WINDING_NONZERO = PGLU.GLU_TESS_WINDING_NONZERO;
        TESS_WINDING_ODD = PGLU.GLU_TESS_WINDING_ODD;
        GENERATE_MIPMAP_HINT = 33170;
        FASTEST = 4353;
        NICEST = 4354;
        DONT_CARE = 4352;
        VENDOR = 7936;
        RENDERER = 7937;
        VERSION = 7938;
        EXTENSIONS = 7939;
        SHADING_LANGUAGE_VERSION = 35724;
        MAX_SAMPLES = -1;
        SAMPLES = 32937;
        ALIASED_LINE_WIDTH_RANGE = 33902;
        ALIASED_POINT_SIZE_RANGE = 33901;
        DEPTH_BITS = 3414;
        STENCIL_BITS = 3415;
        CCW = 2305;
        CW = 2304;
        VIEWPORT = 2978;
        ARRAY_BUFFER = 34962;
        ELEMENT_ARRAY_BUFFER = 34963;
        MAX_VERTEX_ATTRIBS = 34921;
        STATIC_DRAW = 35044;
        DYNAMIC_DRAW = 35048;
        STREAM_DRAW = 35040;
        BUFFER_SIZE = 34660;
        BUFFER_USAGE = 34661;
        POINTS = 0;
        LINE_STRIP = 3;
        LINE_LOOP = 2;
        LINES = 1;
        TRIANGLE_FAN = 6;
        TRIANGLE_STRIP = 5;
        TRIANGLES = 4;
        CULL_FACE = 2884;
        FRONT = 1028;
        BACK = 1029;
        FRONT_AND_BACK = 1032;
        POLYGON_OFFSET_FILL = 32823;
        UNPACK_ALIGNMENT = 3317;
        PACK_ALIGNMENT = 3333;
        TEXTURE_2D = 3553;
        TEXTURE_RECTANGLE = -1;
        TEXTURE_BINDING_2D = 32873;
        TEXTURE_BINDING_RECTANGLE = -1;
        MAX_TEXTURE_SIZE = 3379;
        TEXTURE_MAX_ANISOTROPY = 34046;
        MAX_TEXTURE_MAX_ANISOTROPY = 34047;
        MAX_VERTEX_TEXTURE_IMAGE_UNITS = 35660;
        MAX_TEXTURE_IMAGE_UNITS = 34930;
        MAX_COMBINED_TEXTURE_IMAGE_UNITS = 35661;
        NUM_COMPRESSED_TEXTURE_FORMATS = 34466;
        COMPRESSED_TEXTURE_FORMATS = 34467;
        NEAREST = 9728;
        LINEAR = 9729;
        LINEAR_MIPMAP_NEAREST = 9985;
        LINEAR_MIPMAP_LINEAR = 9987;
        CLAMP_TO_EDGE = 33071;
        REPEAT = 10497;
        TEXTURE0 = 33984;
        TEXTURE1 = 33985;
        TEXTURE2 = 33986;
        TEXTURE3 = 33987;
        TEXTURE_MIN_FILTER = 10241;
        TEXTURE_MAG_FILTER = 10240;
        TEXTURE_WRAP_S = 10242;
        TEXTURE_WRAP_T = 10243;
        TEXTURE_WRAP_R = 32882;
        TEXTURE_CUBE_MAP = 34067;
        TEXTURE_CUBE_MAP_POSITIVE_X = 34069;
        TEXTURE_CUBE_MAP_POSITIVE_Y = 34071;
        TEXTURE_CUBE_MAP_POSITIVE_Z = 34073;
        TEXTURE_CUBE_MAP_NEGATIVE_X = 34070;
        TEXTURE_CUBE_MAP_NEGATIVE_Y = 34072;
        TEXTURE_CUBE_MAP_NEGATIVE_Z = 34074;
        VERTEX_SHADER = 35633;
        FRAGMENT_SHADER = 35632;
        INFO_LOG_LENGTH = 35716;
        SHADER_SOURCE_LENGTH = 35720;
        COMPILE_STATUS = 35713;
        LINK_STATUS = 35714;
        VALIDATE_STATUS = 35715;
        SHADER_TYPE = 35663;
        DELETE_STATUS = 35712;
        FLOAT_VEC2 = 35664;
        FLOAT_VEC3 = 35665;
        FLOAT_VEC4 = 35666;
        FLOAT_MAT2 = 35674;
        FLOAT_MAT3 = 35675;
        FLOAT_MAT4 = 35676;
        INT_VEC2 = 35667;
        INT_VEC3 = 35668;
        INT_VEC4 = 35669;
        BOOL_VEC2 = 35671;
        BOOL_VEC3 = 35672;
        BOOL_VEC4 = 35673;
        SAMPLER_2D = 35678;
        SAMPLER_CUBE = 35680;
        LOW_FLOAT = 36336;
        MEDIUM_FLOAT = 36337;
        HIGH_FLOAT = 36338;
        LOW_INT = 36339;
        MEDIUM_INT = 36340;
        HIGH_INT = 36341;
        CURRENT_VERTEX_ATTRIB = 34342;
        VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 34975;
        VERTEX_ATTRIB_ARRAY_ENABLED = 34338;
        VERTEX_ATTRIB_ARRAY_SIZE = 34339;
        VERTEX_ATTRIB_ARRAY_STRIDE = 34340;
        VERTEX_ATTRIB_ARRAY_TYPE = 34341;
        VERTEX_ATTRIB_ARRAY_NORMALIZED = 34922;
        VERTEX_ATTRIB_ARRAY_POINTER = 34373;
        BLEND = 3042;
        ONE = 1;
        ZERO = 0;
        SRC_ALPHA = 770;
        DST_ALPHA = 772;
        ONE_MINUS_SRC_ALPHA = 771;
        ONE_MINUS_DST_COLOR = 775;
        ONE_MINUS_SRC_COLOR = 769;
        DST_COLOR = 774;
        SRC_COLOR = 768;
        SAMPLE_ALPHA_TO_COVERAGE = 32926;
        SAMPLE_COVERAGE = 32928;
        KEEP = 7680;
        REPLACE = 7681;
        INCR = 7682;
        DECR = 7683;
        INVERT = 5386;
        INCR_WRAP = 34055;
        DECR_WRAP = 34056;
        NEVER = 512;
        ALWAYS = 519;
        EQUAL = 514;
        LESS = InputDeviceCompat.SOURCE_DPAD;
        LEQUAL = 515;
        GREATER = 516;
        GEQUAL = 518;
        NOTEQUAL = 517;
        FUNC_ADD = 32774;
        FUNC_MIN = 32775;
        FUNC_MAX = 32776;
        FUNC_REVERSE_SUBTRACT = 32779;
        FUNC_SUBTRACT = 32778;
        DITHER = 3024;
        CONSTANT_COLOR = 32769;
        CONSTANT_ALPHA = 32771;
        ONE_MINUS_CONSTANT_COLOR = 32770;
        ONE_MINUS_CONSTANT_ALPHA = 32772;
        SRC_ALPHA_SATURATE = 776;
        SCISSOR_TEST = 3089;
        STENCIL_TEST = 2960;
        DEPTH_TEST = 2929;
        DEPTH_WRITEMASK = 2930;
        COLOR_BUFFER_BIT = 16384;
        DEPTH_BUFFER_BIT = 256;
        STENCIL_BUFFER_BIT = 1024;
        FRAMEBUFFER = 36160;
        COLOR_ATTACHMENT0 = 36064;
        COLOR_ATTACHMENT1 = -1;
        COLOR_ATTACHMENT2 = -1;
        COLOR_ATTACHMENT3 = -1;
        RENDERBUFFER = 36161;
        DEPTH_ATTACHMENT = 36096;
        STENCIL_ATTACHMENT = 36128;
        READ_FRAMEBUFFER = -1;
        DRAW_FRAMEBUFFER = -1;
        DEPTH24_STENCIL8 = 35056;
        DEPTH_COMPONENT = 6402;
        DEPTH_COMPONENT16 = 33189;
        DEPTH_COMPONENT24 = 33190;
        DEPTH_COMPONENT32 = 33191;
        STENCIL_INDEX = 6401;
        STENCIL_INDEX1 = 36166;
        STENCIL_INDEX4 = 36167;
        STENCIL_INDEX8 = 36168;
        DEPTH_STENCIL = 34041;
        FRAMEBUFFER_COMPLETE = 36053;
        FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 36054;
        FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 36055;
        FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 36057;
        FRAMEBUFFER_INCOMPLETE_FORMATS = 36058;
        FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = -1;
        FRAMEBUFFER_INCOMPLETE_READ_BUFFER = -1;
        FRAMEBUFFER_UNSUPPORTED = 36061;
        FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 36048;
        FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 36049;
        FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 36050;
        FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 36051;
        RENDERBUFFER_WIDTH = 36162;
        RENDERBUFFER_HEIGHT = 36163;
        RENDERBUFFER_RED_SIZE = 36176;
        RENDERBUFFER_GREEN_SIZE = 36177;
        RENDERBUFFER_BLUE_SIZE = 36178;
        RENDERBUFFER_ALPHA_SIZE = 36179;
        RENDERBUFFER_DEPTH_SIZE = 36180;
        RENDERBUFFER_STENCIL_SIZE = 36181;
        RENDERBUFFER_INTERNAL_FORMAT = 36164;
        MULTISAMPLE = -1;
        LINE_SMOOTH = -1;
        POLYGON_SMOOTH = -1;
    }

    public PGLES(PGraphicsOpenGL pGraphicsOpenGL) {
        super(pGraphicsOpenGL);
    }

    /* access modifiers changed from: protected */
    public void activeTextureImpl(int i) {
        GLES20.glActiveTexture(i);
    }

    public void attachShader(int i, int i2) {
        GLES20.glAttachShader(i, i2);
    }

    public void bindAttribLocation(int i, int i2, String str) {
        GLES20.glBindAttribLocation(i, i2, str);
    }

    public void bindBuffer(int i, int i2) {
        GLES20.glBindBuffer(i, i2);
    }

    /* access modifiers changed from: protected */
    public void bindFramebufferImpl(int i, int i2) {
        GLES20.glBindFramebuffer(i, i2);
    }

    public void bindRenderbuffer(int i, int i2) {
        GLES20.glBindRenderbuffer(i, i2);
    }

    /* access modifiers changed from: protected */
    public void bindTextureImpl(int i, int i2) {
        GLES20.glBindTexture(i, i2);
    }

    public void blendColor(float f, float f2, float f3, float f4) {
        GLES20.glBlendColor(f, f2, f3, f4);
    }

    public void blendEquation(int i) {
        GLES20.glBlendEquation(i);
    }

    public void blendEquationSeparate(int i, int i2) {
        GLES20.glBlendEquationSeparate(i, i2);
    }

    public void blendFunc(int i, int i2) {
        GLES20.glBlendFunc(i, i2);
    }

    public void blendFuncSeparate(int i, int i2, int i3, int i4) {
        GLES20.glBlendFuncSeparate(i, i2, i3, i4);
    }

    public void blitFramebuffer(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
    }

    public void bufferData(int i, int i2, Buffer buffer, int i3) {
        GLES20.glBufferData(i, i2, buffer, i3);
    }

    public void bufferSubData(int i, int i2, int i3, Buffer buffer) {
        GLES20.glBufferSubData(i, i2, i3, buffer);
    }

    /* access modifiers changed from: protected */
    public boolean canDraw() {
        return true;
    }

    public int checkFramebufferStatus(int i) {
        return GLES20.glCheckFramebufferStatus(i);
    }

    public void clear(int i) {
        if (usingMultisampling && usingCoverageMultisampling) {
            i |= 32768;
        }
        GLES20.glClear(i);
    }

    public void clearColor(float f, float f2, float f3, float f4) {
        GLES20.glClearColor(f, f2, f3, f4);
    }

    public void clearDepth(float f) {
        GLES20.glClearDepthf(f);
    }

    public void clearStencil(int i) {
        GLES20.glClearStencil(i);
    }

    public int clientWaitSync(long j, int i, long j2) {
        return 0;
    }

    public void colorMask(boolean z, boolean z2, boolean z3, boolean z4) {
        GLES20.glColorMask(z, z2, z3, z4);
    }

    public void compileShader(int i) {
        GLES20.glCompileShader(i);
    }

    public void compressedTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, Buffer buffer) {
        GLES20.glCompressedTexImage2D(i, i2, i3, i4, i5, i6, i7, buffer);
    }

    public void compressedTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer) {
        GLES20.glCompressedTexSubImage2D(i, i2, i3, i4, i5, i6, i7, i8, buffer);
    }

    public void copyTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        GLES20.glCopyTexImage2D(i, i2, i3, i4, i5, i6, i7, i8);
    }

    public void copyTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        GLES20.glCopyTexSubImage2D(i, i2, i5, i6, i3, i4, i7, i8);
    }

    /* access modifiers changed from: protected */
    public PGL.FontOutline createFontOutline(char c, Object obj) {
        return null;
    }

    public int createProgram() {
        return GLES20.glCreateProgram();
    }

    public int createShader(int i) {
        return GLES20.glCreateShader(i);
    }

    /* access modifiers changed from: protected */
    public Tessellator createTessellator(PGL.TessellatorCallback tessellatorCallback) {
        return new Tessellator(tessellatorCallback);
    }

    public void cullFace(int i) {
        GLES20.glCullFace(i);
    }

    public void deleteBuffers(int i, IntBuffer intBuffer) {
        GLES20.glDeleteBuffers(i, intBuffer);
    }

    public void deleteFramebuffers(int i, IntBuffer intBuffer) {
        GLES20.glDeleteFramebuffers(i, intBuffer);
    }

    public void deleteProgram(int i) {
        GLES20.glDeleteProgram(i);
    }

    public void deleteRenderbuffers(int i, IntBuffer intBuffer) {
        GLES20.glDeleteRenderbuffers(i, intBuffer);
    }

    public void deleteShader(int i) {
        GLES20.glDeleteShader(i);
    }

    public void deleteSync(long j) {
    }

    public void deleteTextures(int i, IntBuffer intBuffer) {
        GLES20.glDeleteTextures(i, intBuffer);
    }

    public void depthFunc(int i) {
        GLES20.glDepthFunc(i);
    }

    public void depthMask(boolean z) {
        GLES20.glDepthMask(z);
    }

    public void depthRangef(float f, float f2) {
        GLES20.glDepthRangef(f, f2);
    }

    public void detachShader(int i, int i2) {
        GLES20.glDetachShader(i, i2);
    }

    public void disable(int i) {
        if (-1 < i) {
            GLES20.glDisable(i);
        }
    }

    public void disableVertexAttribArray(int i) {
        GLES20.glDisableVertexAttribArray(i);
    }

    public void drawArraysImpl(int i, int i2, int i3) {
        GLES20.glDrawArrays(i, i2, i3);
    }

    public void drawBuffer(int i) {
    }

    public void drawElementsImpl(int i, int i2, int i3, int i4) {
        GLES20.glDrawElements(i, i2, i3, i4);
    }

    public void enable(int i) {
        if (-1 < i) {
            GLES20.glEnable(i);
        }
    }

    public void enableVertexAttribArray(int i) {
        GLES20.glEnableVertexAttribArray(i);
    }

    public String errorString(int i) {
        return GLU.gluErrorString(i);
    }

    public long fenceSync(int i, int i2) {
        return 0;
    }

    public void finish() {
        GLES20.glFinish();
    }

    public void flush() {
        GLES20.glFlush();
    }

    public void framebufferRenderbuffer(int i, int i2, int i3, int i4) {
        GLES20.glFramebufferRenderbuffer(i, i2, i3, i4);
    }

    public void framebufferTexture2D(int i, int i2, int i3, int i4, int i5) {
        GLES20.glFramebufferTexture2D(i, i2, i3, i4, i5);
    }

    public void frontFace(int i) {
        GLES20.glFrontFace(i);
    }

    public void genBuffers(int i, IntBuffer intBuffer) {
        GLES20.glGenBuffers(i, intBuffer);
    }

    public void genFramebuffers(int i, IntBuffer intBuffer) {
        GLES20.glGenFramebuffers(i, intBuffer);
    }

    public void genRenderbuffers(int i, IntBuffer intBuffer) {
        GLES20.glGenRenderbuffers(i, intBuffer);
    }

    public void genTextures(int i, IntBuffer intBuffer) {
        GLES20.glGenTextures(i, intBuffer);
    }

    public void generateMipmap(int i) {
        GLES20.glGenerateMipmap(i);
    }

    public String getActiveAttrib(int i, int i2, IntBuffer intBuffer, IntBuffer intBuffer2) {
        int[] iArr = {0, 0, 0};
        byte[] bArr = new byte[1024];
        GLES20.glGetActiveAttrib(i, i2, 1024, iArr, 0, iArr, 1, iArr, 2, bArr, 0);
        intBuffer.put(iArr[1]);
        intBuffer2.put(iArr[2]);
        return new String(bArr, 0, iArr[0]);
    }

    public String getActiveUniform(int i, int i2, IntBuffer intBuffer, IntBuffer intBuffer2) {
        int[] iArr = {0, 0, 0};
        byte[] bArr = new byte[1024];
        GLES20.glGetActiveUniform(i, i2, 1024, iArr, 0, iArr, 1, iArr, 2, bArr, 0);
        intBuffer.put(iArr[1]);
        intBuffer2.put(iArr[2]);
        return new String(bArr, 0, iArr[0]);
    }

    public void getAttachedShaders(int i, int i2, IntBuffer intBuffer, IntBuffer intBuffer2) {
        GLES20.glGetAttachedShaders(i, i2, intBuffer, intBuffer2);
    }

    public int getAttribLocation(int i, String str) {
        return GLES20.glGetAttribLocation(i, str);
    }

    public void getBooleanv(int i, IntBuffer intBuffer) {
        if (-1 < i) {
            GLES20.glGetBooleanv(i, intBuffer);
        } else {
            fillIntBuffer(intBuffer, 0, intBuffer.capacity(), 0);
        }
    }

    public void getBufferParameteriv(int i, int i2, IntBuffer intBuffer) {
        GLES20.glGetBufferParameteriv(i, i2, intBuffer);
    }

    public AndroidConfigChooser getConfigChooser(int i) {
        configChooser = new AndroidConfigChooser(5, 6, 5, 4, 16, 1, i);
        return configChooser;
    }

    public AndroidConfigChooser getConfigChooser(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        configChooser = new AndroidConfigChooser(i, i2, i3, i4, i5, i6, i7);
        return configChooser;
    }

    public AndroidContextFactory getContextFactory() {
        return new AndroidContextFactory();
    }

    /* access modifiers changed from: protected */
    public int getDefaultDrawBuffer() {
        return this.fboLayerEnabled ? COLOR_ATTACHMENT0 : FRONT;
    }

    /* access modifiers changed from: protected */
    public int getDefaultReadBuffer() {
        return this.fboLayerEnabled ? COLOR_ATTACHMENT0 : FRONT;
    }

    /* access modifiers changed from: protected */
    public int getDepthBits() {
        this.intBuffer.rewind();
        getIntegerv(DEPTH_BITS, this.intBuffer);
        return this.intBuffer.get(0);
    }

    /* access modifiers changed from: protected */
    public Object getDerivedFont(Object obj, float f) {
        return null;
    }

    public int getError() {
        return GLES20.glGetError();
    }

    public void getFloatv(int i, FloatBuffer floatBuffer) {
        if (-1 < i) {
            GLES20.glGetFloatv(i, floatBuffer);
        } else {
            fillFloatBuffer(floatBuffer, 0, floatBuffer.capacity() - 1, 0.0f);
        }
    }

    /* access modifiers changed from: protected */
    public int getFontAscent(Object obj) {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getFontDescent(Object obj) {
        return 0;
    }

    public void getFramebufferAttachmentParameteriv(int i, int i2, int i3, IntBuffer intBuffer) {
        GLES20.glGetFramebufferAttachmentParameteriv(i, i2, i3, intBuffer);
    }

    /* access modifiers changed from: protected */
    public void getGL(PGL pgl) {
        PGLES pgles = (PGLES) pgl;
        this.gl = pgles.gl;
        setThread(pgles.glThread);
    }

    /* access modifiers changed from: protected */
    public int getGLSLVersion() {
        return 100;
    }

    public void getIntegerv(int i, IntBuffer intBuffer) {
        if (-1 < i) {
            GLES20.glGetIntegerv(i, intBuffer);
        } else {
            fillIntBuffer(intBuffer, 0, intBuffer.capacity() - 1, 0);
        }
    }

    public GLSurfaceView getNative() {
        return glview;
    }

    /* access modifiers changed from: protected */
    public float getPixelScale() {
        return 1.0f;
    }

    public String getProgramInfoLog(int i) {
        return GLES20.glGetProgramInfoLog(i);
    }

    public void getProgramiv(int i, int i2, IntBuffer intBuffer) {
        GLES20.glGetProgramiv(i, i2, intBuffer);
    }

    public void getRenderbufferParameteriv(int i, int i2, IntBuffer intBuffer) {
        GLES20.glGetRenderbufferParameteriv(i, i2, intBuffer);
    }

    public AndroidRenderer getRenderer() {
        renderer = new AndroidRenderer();
        return renderer;
    }

    public String getShaderInfoLog(int i) {
        return GLES20.glGetShaderInfoLog(i);
    }

    public void getShaderPrecisionFormat(int i, int i2, IntBuffer intBuffer, IntBuffer intBuffer2) {
        GLES20.glGetShaderPrecisionFormat(i, i2, intBuffer, intBuffer2);
    }

    public String getShaderSource(int i) {
        int[] iArr = {0};
        byte[] bArr = new byte[1024];
        GLES20.glGetShaderSource(i, 1024, iArr, 0, bArr, 0);
        return new String(bArr, 0, iArr[0]);
    }

    public void getShaderiv(int i, int i2, IntBuffer intBuffer) {
        GLES20.glGetShaderiv(i, i2, intBuffer);
    }

    /* access modifiers changed from: protected */
    public int getStencilBits() {
        this.intBuffer.rewind();
        getIntegerv(STENCIL_BITS, this.intBuffer);
        return this.intBuffer.get(0);
    }

    public String getString(int i) {
        return GLES20.glGetString(i);
    }

    public void getTexParameterfv(int i, int i2, FloatBuffer floatBuffer) {
        GLES20.glGetTexParameterfv(i, i2, floatBuffer);
    }

    public void getTexParameteriv(int i, int i2, IntBuffer intBuffer) {
        GLES20.glGetTexParameteriv(i, i2, intBuffer);
    }

    /* access modifiers changed from: protected */
    public int getTextWidth(Object obj, char[] cArr, int i, int i2) {
        return 0;
    }

    public int getUniformLocation(int i, String str) {
        return GLES20.glGetUniformLocation(i, str);
    }

    public void getUniformfv(int i, int i2, FloatBuffer floatBuffer) {
        GLES20.glGetUniformfv(i, i2, floatBuffer);
    }

    public void getUniformiv(int i, int i2, IntBuffer intBuffer) {
        GLES20.glGetUniformiv(i, i2, intBuffer);
    }

    public void getVertexAttribPointerv(int i, int i2, ByteBuffer byteBuffer) {
        throw new RuntimeException(String.format(PGL.MISSING_GLFUNC_ERROR, new Object[]{"glGetVertexAttribPointerv()"}));
    }

    public void getVertexAttribfv(int i, int i2, FloatBuffer floatBuffer) {
        GLES20.glGetVertexAttribfv(i, i2, floatBuffer);
    }

    public void getVertexAttribiv(int i, int i2, IntBuffer intBuffer) {
        GLES20.glGetVertexAttribiv(i, i2, intBuffer);
    }

    public void hint(int i, int i2) {
        GLES20.glHint(i, i2);
    }

    /* access modifiers changed from: protected */
    public void initFBOLayer() {
        if (this.sketch.frameCount > 0) {
            IntBuffer allocateDirectIntBuffer = allocateDirectIntBuffer(this.fboWidth * this.fboHeight);
            if (hasReadBuffer()) {
                readBuffer(BACK);
            }
            readPixelsImpl(0, 0, this.fboWidth, this.fboHeight, RGBA, UNSIGNED_BYTE, (Buffer) allocateDirectIntBuffer);
            bindTexture(TEXTURE_2D, this.glColorTex.get(this.frontTex));
            texSubImage2D(TEXTURE_2D, 0, 0, 0, this.fboWidth, this.fboHeight, RGBA, UNSIGNED_BYTE, allocateDirectIntBuffer);
            bindTexture(TEXTURE_2D, this.glColorTex.get(this.backTex));
            texSubImage2D(TEXTURE_2D, 0, 0, 0, this.fboWidth, this.fboHeight, RGBA, UNSIGNED_BYTE, allocateDirectIntBuffer);
            bindTexture(TEXTURE_2D, 0);
            bindFramebufferImpl(FRAMEBUFFER, 0);
        }
    }

    /* access modifiers changed from: protected */
    public void initSurface(int i) {
        glview = (GLSurfaceView) this.sketch.getSurfaceView();
        this.reqNumSamples = qualityToSamples(i);
        registerListeners();
    }

    public void isBuffer(int i) {
        GLES20.glIsBuffer(i);
    }

    public boolean isEnabled(int i) {
        return GLES20.glIsEnabled(i);
    }

    public boolean isFramebuffer(int i) {
        return GLES20.glIsFramebuffer(i);
    }

    public boolean isProgram(int i) {
        return GLES20.glIsProgram(i);
    }

    public boolean isRenderbuffer(int i) {
        return GLES20.glIsRenderbuffer(i);
    }

    public boolean isShader(int i) {
        return GLES20.glIsShader(i);
    }

    public boolean isTexture(int i) {
        return GLES20.glIsTexture(i);
    }

    public void lineWidth(float f) {
        GLES20.glLineWidth(f);
    }

    public void linkProgram(int i) {
        GLES20.glLinkProgram(i);
    }

    public ByteBuffer mapBuffer(int i, int i2) {
        throw new RuntimeException(String.format(PGL.MISSING_GLFUNC_ERROR, new Object[]{"glMapBuffer"}));
    }

    public ByteBuffer mapBufferRange(int i, int i2, int i3, int i4) {
        throw new RuntimeException(String.format(PGL.MISSING_GLFUNC_ERROR, new Object[]{"glMapBufferRange"}));
    }

    public void pixelStorei(int i, int i2) {
        GLES20.glPixelStorei(i, i2);
    }

    public void polygonOffset(float f, float f2) {
        GLES20.glPolygonOffset(f, f2);
    }

    public void readBuffer(int i) {
    }

    /* access modifiers changed from: protected */
    public void readPixelsImpl(int i, int i2, int i3, int i4, int i5, int i6, long j) {
    }

    public void readPixelsImpl(int i, int i2, int i3, int i4, int i5, int i6, Buffer buffer) {
        GLES20.glReadPixels(i, i2, i3, i4, i5, i6, buffer);
    }

    /* access modifiers changed from: protected */
    public void registerListeners() {
    }

    /* access modifiers changed from: protected */
    public void reinitSurface() {
    }

    public void releaseShaderCompiler() {
        GLES20.glReleaseShaderCompiler();
    }

    public void renderbufferStorage(int i, int i2, int i3, int i4) {
        GLES20.glRenderbufferStorage(i, i2, i3, i4);
    }

    public void renderbufferStorageMultisample(int i, int i2, int i3, int i4, int i5) {
    }

    /* access modifiers changed from: protected */
    public void requestDraw() {
        if (this.graphics.initialized && this.sketch.canDraw()) {
            glview.requestRender();
        }
    }

    /* access modifiers changed from: protected */
    public void requestFocus() {
    }

    public void sampleCoverage(float f, boolean z) {
        GLES20.glSampleCoverage(f, z);
    }

    public void scissor(int i, int i2, int i3, int i4) {
        GLES20.glScissor(i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public void setFrameRate(float f) {
    }

    public void shaderBinary(int i, IntBuffer intBuffer, int i2, Buffer buffer, int i3) {
        GLES20.glShaderBinary(i, intBuffer, i2, buffer, i3);
    }

    public void shaderSource(int i, String str) {
        GLES20.glShaderSource(i, str);
    }

    public void stencilFunc(int i, int i2, int i3) {
        GLES20.glStencilFunc(i, i2, i3);
    }

    public void stencilFuncSeparate(int i, int i2, int i3, int i4) {
        GLES20.glStencilFuncSeparate(i, i2, i3, i4);
    }

    public void stencilMask(int i) {
        GLES20.glStencilMask(i);
    }

    public void stencilMaskSeparate(int i, int i2) {
        GLES20.glStencilMaskSeparate(i, i2);
    }

    public void stencilOp(int i, int i2, int i3) {
        GLES20.glStencilOp(i, i2, i3);
    }

    public void stencilOpSeparate(int i, int i2, int i3, int i4) {
        GLES20.glStencilOpSeparate(i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public void swapBuffers() {
    }

    /* access modifiers changed from: protected */
    public String tessError(int i) {
        return PGLU.gluErrorString(i);
    }

    public void texImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer) {
        GLES20.glTexImage2D(i, i2, i3, i4, i5, i6, i7, i8, buffer);
    }

    public void texParameterf(int i, int i2, float f) {
        this.gl.glTexParameterf(i, i2, f);
    }

    public void texParameterfv(int i, int i2, FloatBuffer floatBuffer) {
        GLES20.glTexParameterfv(i, i2, floatBuffer);
    }

    public void texParameteri(int i, int i2, int i3) {
        GLES20.glTexParameteri(i, i2, i3);
    }

    public void texParameteriv(int i, int i2, IntBuffer intBuffer) {
        GLES20.glTexParameteriv(i, i2, intBuffer);
    }

    public void texSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer) {
        GLES20.glTexSubImage2D(i, i2, i3, i4, i5, i6, i7, i8, buffer);
    }

    public void uniform1f(int i, float f) {
        GLES20.glUniform1f(i, f);
    }

    public void uniform1fv(int i, int i2, FloatBuffer floatBuffer) {
        GLES20.glUniform1fv(i, i2, floatBuffer);
    }

    public void uniform1i(int i, int i2) {
        GLES20.glUniform1i(i, i2);
    }

    public void uniform1iv(int i, int i2, IntBuffer intBuffer) {
        GLES20.glUniform1iv(i, i2, intBuffer);
    }

    public void uniform2f(int i, float f, float f2) {
        GLES20.glUniform2f(i, f, f2);
    }

    public void uniform2fv(int i, int i2, FloatBuffer floatBuffer) {
        GLES20.glUniform2fv(i, i2, floatBuffer);
    }

    public void uniform2i(int i, int i2, int i3) {
        GLES20.glUniform2i(i, i2, i3);
    }

    public void uniform2iv(int i, int i2, IntBuffer intBuffer) {
        GLES20.glUniform2iv(i, i2, intBuffer);
    }

    public void uniform3f(int i, float f, float f2, float f3) {
        GLES20.glUniform3f(i, f, f2, f3);
    }

    public void uniform3fv(int i, int i2, FloatBuffer floatBuffer) {
        GLES20.glUniform3fv(i, i2, floatBuffer);
    }

    public void uniform3i(int i, int i2, int i3, int i4) {
        GLES20.glUniform3i(i, i2, i3, i4);
    }

    public void uniform3iv(int i, int i2, IntBuffer intBuffer) {
        GLES20.glUniform3iv(i, i2, intBuffer);
    }

    public void uniform4f(int i, float f, float f2, float f3, float f4) {
        GLES20.glUniform4f(i, f, f2, f3, f4);
    }

    public void uniform4fv(int i, int i2, FloatBuffer floatBuffer) {
        GLES20.glUniform4fv(i, i2, floatBuffer);
    }

    public void uniform4i(int i, int i2, int i3, int i4, int i5) {
        GLES20.glUniform4i(i, i2, i3, i4, i5);
    }

    public void uniform4iv(int i, int i2, IntBuffer intBuffer) {
        GLES20.glUniform4iv(i, i2, intBuffer);
    }

    public void uniformMatrix2fv(int i, int i2, boolean z, FloatBuffer floatBuffer) {
        GLES20.glUniformMatrix2fv(i, i2, z, floatBuffer);
    }

    public void uniformMatrix3fv(int i, int i2, boolean z, FloatBuffer floatBuffer) {
        GLES20.glUniformMatrix3fv(i, i2, z, floatBuffer);
    }

    public void uniformMatrix4fv(int i, int i2, boolean z, FloatBuffer floatBuffer) {
        GLES20.glUniformMatrix4fv(i, i2, z, floatBuffer);
    }

    public void unmapBuffer(int i) {
        throw new RuntimeException(String.format(PGL.MISSING_GLFUNC_ERROR, new Object[]{"glUnmapBuffer"}));
    }

    public void useProgram(int i) {
        GLES20.glUseProgram(i);
    }

    public void validateProgram(int i) {
        GLES20.glValidateProgram(i);
    }

    public void vertexAttrib1f(int i, float f) {
        GLES20.glVertexAttrib1f(i, f);
    }

    public void vertexAttrib1fv(int i, FloatBuffer floatBuffer) {
        GLES20.glVertexAttrib1fv(i, floatBuffer);
    }

    public void vertexAttrib2f(int i, float f, float f2) {
        GLES20.glVertexAttrib2f(i, f, f2);
    }

    public void vertexAttrib2fv(int i, FloatBuffer floatBuffer) {
        GLES20.glVertexAttrib2fv(i, floatBuffer);
    }

    public void vertexAttrib3f(int i, float f, float f2, float f3) {
        GLES20.glVertexAttrib3f(i, f, f2, f3);
    }

    public void vertexAttrib3fv(int i, FloatBuffer floatBuffer) {
        GLES20.glVertexAttrib3fv(i, floatBuffer);
    }

    public void vertexAttrib4f(int i, float f, float f2, float f3, float f4) {
        GLES20.glVertexAttrib4f(i, f, f2, f3, f4);
    }

    public void vertexAttrib4fv(int i, FloatBuffer floatBuffer) {
        GLES20.glVertexAttrib4fv(i, floatBuffer);
    }

    public void vertexAttribPointer(int i, int i2, int i3, boolean z, int i4, int i5) {
        GLES20.glVertexAttribPointer(i, i2, i3, z, i4, i5);
    }

    public void viewport(int i, int i2, int i3, int i4) {
        float pixelScale = getPixelScale();
        viewportImpl(((int) pixelScale) * i, (int) (((float) i2) * pixelScale), (int) (((float) i3) * pixelScale), (int) (pixelScale * ((float) i4)));
    }

    /* access modifiers changed from: protected */
    public void viewportImpl(int i, int i2, int i3, int i4) {
        this.gl.glViewport(i, i2, i3, i4);
    }
}
