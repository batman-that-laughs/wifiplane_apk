package processing.opengl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.regex.Pattern;
import processing.core.PApplet;
import processing.core.PGraphics;

public abstract class PGL {
    public static int ALIASED_LINE_WIDTH_RANGE = 0;
    public static int ALIASED_POINT_SIZE_RANGE = 0;
    public static int ALPHA = 0;
    public static int ALPHA8 = 0;
    public static int ALREADY_SIGNALED = 0;
    public static int ALWAYS = 0;
    public static int ARRAY_BUFFER = 0;
    public static int BACK = 0;
    protected static boolean BIG_ENDIAN = false;
    public static int BLEND = 0;
    public static int BOOL = 0;
    public static int BOOL_VEC2 = 0;
    public static int BOOL_VEC3 = 0;
    public static int BOOL_VEC4 = 0;
    public static int BUFFER_SIZE = 0;
    public static int BUFFER_USAGE = 0;
    public static int BYTE = 0;
    public static int CCW = 0;
    public static int CLAMP_TO_EDGE = 0;
    public static int COLOR_ATTACHMENT0 = 0;
    public static int COLOR_ATTACHMENT1 = 0;
    public static int COLOR_ATTACHMENT2 = 0;
    public static int COLOR_ATTACHMENT3 = 0;
    public static int COLOR_BUFFER_BIT = 0;
    public static int COMPILE_STATUS = 0;
    public static int COMPRESSED_TEXTURE_FORMATS = 0;
    public static int CONDITION_SATISFIED = 0;
    public static int CONSTANT_ALPHA = 0;
    public static int CONSTANT_COLOR = 0;
    public static int CULL_FACE = 0;
    public static int CURRENT_VERTEX_ATTRIB = 0;
    public static int CW = 0;
    public static int DECR = 0;
    public static int DECR_WRAP = 0;
    protected static int DEFAULT_IN_EDGES = 128;
    protected static int DEFAULT_IN_TEXTURES = 64;
    protected static int DEFAULT_IN_VERTICES = 64;
    protected static int DEFAULT_TESS_INDICES = 128;
    protected static int DEFAULT_TESS_VERTICES = 64;
    public static int DELETE_STATUS = 0;
    public static int DEPTH24_STENCIL8 = 0;
    public static int DEPTH_ATTACHMENT = 0;
    public static int DEPTH_BITS = 0;
    public static int DEPTH_BUFFER_BIT = 0;
    public static int DEPTH_COMPONENT = 0;
    public static int DEPTH_COMPONENT16 = 0;
    public static int DEPTH_COMPONENT24 = 0;
    public static int DEPTH_COMPONENT32 = 0;
    protected static final String DEPTH_READING_NOT_ENABLED_ERROR = "Reading depth and stencil values from this multisampled buffer is not enabled. You can enable it by calling hint(ENABLE_DEPTH_READING) once. If your sketch becomes too slow, disable multisampling with noSmooth() instead.";
    public static int DEPTH_STENCIL = 0;
    public static int DEPTH_TEST = 0;
    public static int DEPTH_WRITEMASK = 0;
    public static int DITHER = 0;
    public static int DONT_CARE = 0;
    public static int DRAW_FRAMEBUFFER = 0;
    public static int DST_ALPHA = 0;
    public static int DST_COLOR = 0;
    public static int DYNAMIC_DRAW = 0;
    public static int ELEMENT_ARRAY_BUFFER = 0;
    public static int EQUAL = 0;
    public static int EXTENSIONS = 0;
    public static int FALSE = 0;
    public static int FASTEST = 0;
    public static int FLOAT = 0;
    protected static float FLOAT_EPS = 0.0f;
    public static int FLOAT_MAT2 = 0;
    public static int FLOAT_MAT3 = 0;
    public static int FLOAT_MAT4 = 0;
    public static int FLOAT_VEC2 = 0;
    public static int FLOAT_VEC3 = 0;
    public static int FLOAT_VEC4 = 0;
    protected static int FLUSH_VERTEX_COUNT = MAX_VERTEX_INDEX1;
    public static int FRAGMENT_SHADER = 0;
    public static int FRAMEBUFFER = 0;
    public static int FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0;
    public static int FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0;
    public static int FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0;
    public static int FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0;
    public static int FRAMEBUFFER_COMPLETE = 0;
    public static final String FRAMEBUFFER_ERROR = "Framebuffer error (%1$s), rendering will probably not work as expected Read http://wiki.processing.org/w/OpenGL_Issues for help.";
    public static int FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0;
    public static int FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0;
    public static int FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 0;
    public static int FRAMEBUFFER_INCOMPLETE_FORMATS = 0;
    public static int FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0;
    public static int FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 0;
    public static int FRAMEBUFFER_UNSUPPORTED = 0;
    public static int FRONT = 0;
    public static int FRONT_AND_BACK = 0;
    public static int FUNC_ADD = 0;
    public static int FUNC_MAX = 0;
    public static int FUNC_MIN = 0;
    public static int FUNC_REVERSE_SUBTRACT = 0;
    public static int FUNC_SUBTRACT = 0;
    public static int GENERATE_MIPMAP_HINT = 0;
    public static int GEQUAL = 0;
    protected static final String GLSL_FN_REGEX = "(?<![0-9A-Z_a-z])(%s)(?=\\s*\\()";
    protected static final String GLSL_ID_REGEX = "(?<![0-9A-Z_a-z])(%s)(?![0-9A-Z_a-z]|\\s*\\()";
    public static int GREATER = 0;
    public static int HIGH_FLOAT = 0;
    public static int HIGH_INT = 0;
    public static int INCR = 0;
    public static int INCR_WRAP = 0;
    protected static int INDEX_TYPE = 5123;
    public static int INFO_LOG_LENGTH = 0;
    public static int INT = 0;
    public static int INT_VEC2 = 0;
    public static int INT_VEC3 = 0;
    public static int INT_VEC4 = 0;
    public static int INVERT = 0;
    public static int KEEP = 0;
    public static int LEQUAL = 0;
    public static int LESS = 0;
    public static int LINEAR = 0;
    public static int LINEAR_MIPMAP_LINEAR = 0;
    public static int LINEAR_MIPMAP_NEAREST = 0;
    public static int LINES = 0;
    public static int LINE_LOOP = 0;
    public static int LINE_SMOOTH = 0;
    public static int LINE_STRIP = 0;
    public static int LINK_STATUS = 0;
    public static int LOW_FLOAT = 0;
    public static int LOW_INT = 0;
    public static int LUMINANCE = 0;
    public static int LUMINANCE_ALPHA = 0;
    protected static int MAX_CAPS_JOINS_LENGTH = 5000;
    public static int MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0;
    protected static int MAX_FONT_TEX_SIZE = 1024;
    protected static int MAX_LIGHTS = 8;
    public static int MAX_SAMPLES = 0;
    public static int MAX_TEXTURE_IMAGE_UNITS = 0;
    public static int MAX_TEXTURE_MAX_ANISOTROPY = 0;
    public static int MAX_TEXTURE_SIZE = 0;
    public static int MAX_VERTEX_ATTRIBS = 0;
    protected static int MAX_VERTEX_INDEX = 32767;
    protected static int MAX_VERTEX_INDEX1 = (MAX_VERTEX_INDEX + 1);
    public static int MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0;
    public static int MEDIUM_FLOAT = 0;
    public static int MEDIUM_INT = 0;
    protected static int MIN_ARRAYCOPY_SIZE = 2;
    protected static float MIN_CAPS_JOINS_WEIGHT = 2.0f;
    protected static int MIN_DIRECT_BUFFER_SIZE = 1;
    protected static int MIN_FONT_TEX_SIZE = 256;
    protected static boolean MIPMAPS_ENABLED = true;
    public static final String MISSING_FBO_ERROR = "Framebuffer objects are not supported by this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.";
    public static final String MISSING_GLFUNC_ERROR = "GL function %1$s is not available on this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.";
    public static final String MISSING_GLSL_ERROR = "GLSL shaders are not supported by this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.";
    public static int MULTISAMPLE = 0;
    public static int NEAREST = 0;
    public static int NEVER = 0;
    public static int NICEST = 0;
    public static final String NONPRIMARY_ERROR = "The renderer is trying to call a PGL function that can only be called on a primary PGL. This is most likely due to a bug in the renderer's code, please report it with an issue on Processing's github page https://github.com/processing/processing/issues?state=open if using any of the built-in OpenGL renderers. If you are using a contributed library, contact the library's developers.";
    public static int NOTEQUAL = 0;
    public static int NUM_COMPRESSED_TEXTURE_FORMATS = 0;
    public static int ONE = 0;
    public static int ONE_MINUS_CONSTANT_ALPHA = 0;
    public static int ONE_MINUS_CONSTANT_COLOR = 0;
    public static int ONE_MINUS_DST_COLOR = 0;
    public static int ONE_MINUS_SRC_ALPHA = 0;
    public static int ONE_MINUS_SRC_COLOR = 0;
    public static int PACK_ALIGNMENT = 0;
    public static int PIXEL_PACK_BUFFER = 0;
    public static int POINTS = 0;
    public static int POLYGON_OFFSET_FILL = 0;
    public static int POLYGON_SMOOTH = 0;
    public static int READ_FRAMEBUFFER = 0;
    public static int READ_ONLY = 0;
    public static int READ_WRITE = 0;
    public static int RENDERBUFFER = 0;
    public static int RENDERBUFFER_ALPHA_SIZE = 0;
    public static int RENDERBUFFER_BLUE_SIZE = 0;
    public static int RENDERBUFFER_DEPTH_SIZE = 0;
    public static int RENDERBUFFER_GREEN_SIZE = 0;
    public static int RENDERBUFFER_HEIGHT = 0;
    public static int RENDERBUFFER_INTERNAL_FORMAT = 0;
    public static int RENDERBUFFER_RED_SIZE = 0;
    public static int RENDERBUFFER_STENCIL_SIZE = 0;
    public static int RENDERBUFFER_WIDTH = 0;
    public static int RENDERER = 0;
    public static int REPEAT = 0;
    public static int REPLACE = 0;
    public static int REQUESTED_ALPHA_BITS = 8;
    public static int REQUESTED_DEPTH_BITS = 24;
    public static int REQUESTED_STENCIL_BITS = 8;
    public static int RGB = 0;
    public static int RGB565 = 0;
    public static int RGB5_A1 = 0;
    public static int RGB8 = 0;
    public static int RGBA = 0;
    public static int RGBA4 = 0;
    public static int RGBA8 = 0;
    public static int SAMPLER_2D = 0;
    public static int SAMPLER_CUBE = 0;
    public static int SAMPLES = 0;
    public static int SAMPLE_ALPHA_TO_COVERAGE = 0;
    public static int SAMPLE_COVERAGE = 0;
    public static int SCISSOR_TEST = 0;
    protected static int SEG_CLOSE = 0;
    protected static int SEG_CUBICTO = 0;
    protected static int SEG_LINETO = 0;
    protected static int SEG_MOVETO = 0;
    protected static int SEG_QUADTO = 0;
    protected static final String SHADER_PREPROCESSOR_DIRECTIVE = "#ifdef GL_ES\nprecision mediump float;\nprecision mediump int;\n#endif\n";
    public static int SHADER_SOURCE_LENGTH = 0;
    public static int SHADER_TYPE = 0;
    public static int SHADING_LANGUAGE_VERSION = 0;
    protected static boolean SHAPE_TEXT_SUPPORTED = false;
    public static int SHORT = 0;
    protected static boolean SINGLE_BUFFERED = false;
    protected static int SIZEOF_BYTE = 1;
    protected static int SIZEOF_FLOAT = 4;
    protected static int SIZEOF_INDEX = SIZEOF_SHORT;
    protected static int SIZEOF_INT = 4;
    protected static int SIZEOF_SHORT = 2;
    public static int SRC_ALPHA = 0;
    public static int SRC_ALPHA_SATURATE = 0;
    public static int SRC_COLOR = 0;
    public static int STATIC_DRAW = 0;
    public static int STENCIL_ATTACHMENT = 0;
    public static int STENCIL_BITS = 0;
    public static int STENCIL_BUFFER_BIT = 0;
    public static int STENCIL_INDEX = 0;
    public static int STENCIL_INDEX1 = 0;
    public static int STENCIL_INDEX4 = 0;
    public static int STENCIL_INDEX8 = 0;
    public static int STENCIL_TEST = 0;
    public static int STREAM_DRAW = 0;
    public static int STREAM_READ = 0;
    protected static float STROKE_DISPLACEMENT = 0.999f;
    public static int SYNC_GPU_COMMANDS_COMPLETE = 0;
    public static int TESS_WINDING_NONZERO = 0;
    public static int TESS_WINDING_ODD = 0;
    public static int TEXTURE0 = 0;
    public static int TEXTURE1 = 0;
    public static int TEXTURE2 = 0;
    public static int TEXTURE3 = 0;
    public static int TEXTURE_2D = 0;
    public static int TEXTURE_BINDING_2D = 0;
    public static int TEXTURE_BINDING_RECTANGLE = 0;
    public static int TEXTURE_CUBE_MAP = 0;
    public static int TEXTURE_CUBE_MAP_NEGATIVE_X = 0;
    public static int TEXTURE_CUBE_MAP_NEGATIVE_Y = 0;
    public static int TEXTURE_CUBE_MAP_NEGATIVE_Z = 0;
    public static int TEXTURE_CUBE_MAP_POSITIVE_X = 0;
    public static int TEXTURE_CUBE_MAP_POSITIVE_Y = 0;
    public static int TEXTURE_CUBE_MAP_POSITIVE_Z = 0;
    public static int TEXTURE_MAG_FILTER = 0;
    public static int TEXTURE_MAX_ANISOTROPY = 0;
    public static int TEXTURE_MIN_FILTER = 0;
    public static int TEXTURE_RECTANGLE = 0;
    public static int TEXTURE_WRAP_R = 0;
    public static int TEXTURE_WRAP_S = 0;
    public static int TEXTURE_WRAP_T = 0;
    public static final String TEXUNIT_ERROR = "Number of texture units not supported by this hardware (or driver) Read http://wiki.processing.org/w/OpenGL_Issues for help.";
    public static int TRIANGLES = 0;
    public static int TRIANGLE_FAN = 0;
    public static int TRIANGLE_STRIP = 0;
    public static int TRUE = 0;
    public static int UNPACK_ALIGNMENT = 0;
    public static int UNSIGNED_BYTE = 0;
    public static int UNSIGNED_INT = 0;
    public static int UNSIGNED_SHORT = 0;
    public static int UNSIGNED_SHORT_4_4_4_4 = 0;
    public static int UNSIGNED_SHORT_5_5_5_1 = 0;
    public static int UNSIGNED_SHORT_5_6_5 = 0;
    public static final String UNSUPPORTED_GLPROF_ERROR = "Unsupported OpenGL profile.";
    protected static boolean USE_DIRECT_BUFFERS = true;
    public static int VALIDATE_STATUS = 0;
    public static int VENDOR = 0;
    public static int VERSION = 0;
    public static int VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0;
    public static int VERTEX_ATTRIB_ARRAY_ENABLED = 0;
    public static int VERTEX_ATTRIB_ARRAY_NORMALIZED = 0;
    public static int VERTEX_ATTRIB_ARRAY_POINTER = 0;
    public static int VERTEX_ATTRIB_ARRAY_SIZE = 0;
    public static int VERTEX_ATTRIB_ARRAY_STRIDE = 0;
    public static int VERTEX_ATTRIB_ARRAY_TYPE = 0;
    public static int VERTEX_SHADER = 0;
    public static int VIEWPORT = 0;
    public static final String WIKI = " Read http://wiki.processing.org/w/OpenGL_Issues for help.";
    public static int WRITE_ONLY;
    public static int ZERO;
    protected static int[] closeButtonPix = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, 0, 0, 0, -1, -1, -1, -1, -1, 0, 0, 0, -1, -1, -1, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, 0, -1, 0, 0, 0, 0, -1, -1, 0, -1, -1, 0, 0, -1, -1, 0, -1, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, -1, -1, -1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, -1, 0, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, 0, -1, 0, 0, 0, 0, -1, -1, 0, -1, -1, 0, 0, -1, -1, 0, -1, -1, 0, 0, 0, -1, -1, -1, 0, 0, 0, 0, 0, -1, -1, -1, 0, 0, 0, -1, -1, -1, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0};
    protected static String[] tex2DFragShaderSource = {SHADER_PREPROCESSOR_DIRECTIVE, "uniform sampler2D texMap;", "varying vec2 vertTexCoord;", "void main() {", "  gl_FragColor = texture2D(texMap, vertTexCoord.st);", "}"};
    protected static String[] texRectFragShaderSource = {SHADER_PREPROCESSOR_DIRECTIVE, "uniform sampler2DRect texMap;", "varying vec2 vertTexCoord;", "void main() {", "  gl_FragColor = texture2DRect(texMap, vertTexCoord.st);", "}"};
    protected static String[] texVertShaderSource = {"attribute vec2 position;", "attribute vec2 texCoord;", "varying vec2 vertTexCoord;", "void main() {", "  gl_Position = vec4(position, 0, 1);", "  vertTexCoord = texCoord;", "}"};
    protected int activeTexUnit = 0;
    protected int backTex;
    protected int[][] boundTextures;
    protected ByteBuffer byteBuffer;
    protected boolean clearColor = false;
    protected IntBuffer closeButtonTex;
    protected int closeButtonY = 21;
    protected IntBuffer colorBuffer;
    protected float currentFps = 60.0f;
    protected FloatBuffer depthBuffer;
    protected int fboHeight;
    protected boolean fboLayerCreated = false;
    protected boolean fboLayerDisableReq = false;
    protected boolean fboLayerEnabled = false;
    protected boolean fboLayerEnabledReq = false;
    protected int fboWidth;
    protected boolean fbolayerResetReq = false;
    protected IntBuffer firstFrame;
    protected int frontTex;
    protected int geomCount = 0;
    protected IntBuffer glColorFbo;
    protected IntBuffer glColorTex;
    protected int glContext;
    protected IntBuffer glDepth;
    protected IntBuffer glDepthStencil;
    protected IntBuffer glMultiColor;
    protected IntBuffer glMultiDepth;
    protected IntBuffer glMultiDepthStencil;
    protected IntBuffer glMultiFbo;
    protected IntBuffer glMultiStencil;
    protected IntBuffer glStencil;
    protected Thread glThread;
    protected PGraphicsOpenGL graphics;
    protected IntBuffer intBuffer;
    protected boolean loadedTex2DShader = false;
    protected boolean loadedTexRectShader = false;
    protected int maxTexUnits;
    protected boolean needSepFrontTex = false;
    protected int numSamples;
    protected boolean pclearColor;
    protected int pgeomCount;
    protected boolean presentMode = false;
    public float presentX;
    public float presentY;
    public boolean primaryPGL;
    public int reqNumSamples;
    protected boolean setFps = false;
    protected boolean showStopButton = true;
    protected PApplet sketch;
    protected ByteBuffer stencilBuffer;
    protected int stopButtonColor;
    protected int stopButtonHeight = 12;
    protected int stopButtonWidth = 28;
    protected int stopButtonX = 21;
    protected float targetFps = 60.0f;
    protected int tex2DFragShader;
    protected int tex2DGeoVBO;
    protected int tex2DSamplerLoc;
    protected int tex2DShaderContext;
    protected int tex2DShaderProgram;
    protected int tex2DTCoordLoc;
    protected int tex2DVertLoc;
    protected int tex2DVertShader;
    protected float[] texCoords = {-1.0f, -1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    protected FloatBuffer texData;
    protected int texRectFragShader;
    protected int texRectGeoVBO;
    protected int texRectSamplerLoc;
    protected int texRectShaderContext;
    protected int texRectShaderProgram;
    protected int texRectTCoordLoc;
    protected int texRectVertLoc;
    protected int texRectVertShader;
    protected boolean[] texturingTargets = {false, false};
    protected boolean usingFrontTex = false;
    protected IntBuffer viewBuffer;

    protected interface FontOutline {
        int currentSegment(float[] fArr);

        boolean isDone();

        void next();
    }

    protected interface Tessellator {
        void addVertex(double[] dArr);

        void beginContour();

        void beginPolygon();

        void endContour();

        void endPolygon();

        void setWindingRule(int i);
    }

    protected interface TessellatorCallback {
        void begin(int i);

        void combine(double[] dArr, Object[] objArr, float[] fArr, Object[] objArr2);

        void end();

        void error(int i);

        void vertex(Object obj);
    }

    static {
        boolean z = true;
        FLOAT_EPS = Float.MIN_VALUE;
        float f = 1.0f;
        do {
            f /= 2.0f;
        } while (((double) ((float) (1.0d + (((double) f) / 2.0d)))) != 1.0d);
        FLOAT_EPS = f;
        if (ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN) {
            z = false;
        }
        BIG_ENDIAN = z;
    }

    public PGL() {
    }

    public PGL(PGraphicsOpenGL pGraphicsOpenGL) {
        this.graphics = pGraphicsOpenGL;
        if (this.glColorTex == null) {
            this.glColorFbo = allocateIntBuffer(1);
            this.glColorTex = allocateIntBuffer(2);
            this.glDepthStencil = allocateIntBuffer(1);
            this.glDepth = allocateIntBuffer(1);
            this.glStencil = allocateIntBuffer(1);
            this.glMultiFbo = allocateIntBuffer(1);
            this.glMultiColor = allocateIntBuffer(1);
            this.glMultiDepthStencil = allocateIntBuffer(1);
            this.glMultiDepth = allocateIntBuffer(1);
            this.glMultiStencil = allocateIntBuffer(1);
        }
        this.byteBuffer = allocateByteBuffer(1);
        this.intBuffer = allocateIntBuffer(1);
        this.viewBuffer = allocateIntBuffer(4);
    }

    protected static ByteBuffer allocateByteBuffer(int i) {
        return USE_DIRECT_BUFFERS ? allocateDirectByteBuffer(i) : ByteBuffer.allocate(i);
    }

    protected static ByteBuffer allocateByteBuffer(byte[] bArr) {
        if (!USE_DIRECT_BUFFERS) {
            return ByteBuffer.wrap(bArr);
        }
        ByteBuffer allocateDirectByteBuffer = allocateDirectByteBuffer(bArr.length);
        allocateDirectByteBuffer.put(bArr);
        allocateDirectByteBuffer.position(0);
        return allocateDirectByteBuffer;
    }

    protected static ByteBuffer allocateDirectByteBuffer(int i) {
        return ByteBuffer.allocateDirect(PApplet.max(MIN_DIRECT_BUFFER_SIZE, i) * SIZEOF_BYTE).order(ByteOrder.nativeOrder());
    }

    protected static FloatBuffer allocateDirectFloatBuffer(int i) {
        return ByteBuffer.allocateDirect(PApplet.max(MIN_DIRECT_BUFFER_SIZE, i) * SIZEOF_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    protected static IntBuffer allocateDirectIntBuffer(int i) {
        return ByteBuffer.allocateDirect(PApplet.max(MIN_DIRECT_BUFFER_SIZE, i) * SIZEOF_INT).order(ByteOrder.nativeOrder()).asIntBuffer();
    }

    protected static ShortBuffer allocateDirectShortBuffer(int i) {
        return ByteBuffer.allocateDirect(PApplet.max(MIN_DIRECT_BUFFER_SIZE, i) * SIZEOF_SHORT).order(ByteOrder.nativeOrder()).asShortBuffer();
    }

    protected static FloatBuffer allocateFloatBuffer(int i) {
        return USE_DIRECT_BUFFERS ? allocateDirectFloatBuffer(i) : FloatBuffer.allocate(i);
    }

    protected static FloatBuffer allocateFloatBuffer(float[] fArr) {
        if (!USE_DIRECT_BUFFERS) {
            return FloatBuffer.wrap(fArr);
        }
        FloatBuffer allocateDirectFloatBuffer = allocateDirectFloatBuffer(fArr.length);
        allocateDirectFloatBuffer.put(fArr);
        allocateDirectFloatBuffer.position(0);
        return allocateDirectFloatBuffer;
    }

    protected static IntBuffer allocateIntBuffer(int i) {
        return USE_DIRECT_BUFFERS ? allocateDirectIntBuffer(i) : IntBuffer.allocate(i);
    }

    protected static IntBuffer allocateIntBuffer(int[] iArr) {
        if (!USE_DIRECT_BUFFERS) {
            return IntBuffer.wrap(iArr);
        }
        IntBuffer allocateDirectIntBuffer = allocateDirectIntBuffer(iArr.length);
        allocateDirectIntBuffer.put(iArr);
        allocateDirectIntBuffer.position(0);
        return allocateDirectIntBuffer;
    }

    protected static ShortBuffer allocateShortBuffer(int i) {
        return USE_DIRECT_BUFFERS ? allocateDirectShortBuffer(i) : ShortBuffer.allocate(i);
    }

    protected static ShortBuffer allocateShortBuffer(short[] sArr) {
        if (!USE_DIRECT_BUFFERS) {
            return ShortBuffer.wrap(sArr);
        }
        ShortBuffer allocateDirectShortBuffer = allocateDirectShortBuffer(sArr.length);
        allocateDirectShortBuffer.put(sArr);
        allocateDirectShortBuffer.position(0);
        return allocateDirectShortBuffer;
    }

    protected static boolean containsVersionDirective(String[] strArr) {
        for (String contains : strArr) {
            if (contains.contains("#version")) {
                return true;
            }
        }
        return false;
    }

    private void createDepthAndStencilBuffer(boolean z, int i, int i2, boolean z2) {
        if (z2 && i == 24 && i2 == 8) {
            IntBuffer intBuffer2 = z ? this.glMultiDepthStencil : this.glDepthStencil;
            genRenderbuffers(1, intBuffer2);
            bindRenderbuffer(RENDERBUFFER, intBuffer2.get(0));
            if (z) {
                renderbufferStorageMultisample(RENDERBUFFER, this.numSamples, DEPTH24_STENCIL8, this.fboWidth, this.fboHeight);
            } else {
                renderbufferStorage(RENDERBUFFER, DEPTH24_STENCIL8, this.fboWidth, this.fboHeight);
            }
            framebufferRenderbuffer(FRAMEBUFFER, DEPTH_ATTACHMENT, RENDERBUFFER, intBuffer2.get(0));
            framebufferRenderbuffer(FRAMEBUFFER, STENCIL_ATTACHMENT, RENDERBUFFER, intBuffer2.get(0));
            return;
        }
        if (i > 0) {
            int i3 = DEPTH_COMPONENT16;
            if (i == 32) {
                i3 = DEPTH_COMPONENT32;
            } else if (i == 24) {
                i3 = DEPTH_COMPONENT24;
            } else if (i == 16) {
                i3 = DEPTH_COMPONENT16;
            }
            IntBuffer intBuffer3 = z ? this.glMultiDepth : this.glDepth;
            genRenderbuffers(1, intBuffer3);
            bindRenderbuffer(RENDERBUFFER, intBuffer3.get(0));
            if (z) {
                renderbufferStorageMultisample(RENDERBUFFER, this.numSamples, i3, this.fboWidth, this.fboHeight);
            } else {
                renderbufferStorage(RENDERBUFFER, i3, this.fboWidth, this.fboHeight);
            }
            framebufferRenderbuffer(FRAMEBUFFER, DEPTH_ATTACHMENT, RENDERBUFFER, intBuffer3.get(0));
        }
        if (i2 > 0) {
            int i4 = STENCIL_INDEX1;
            if (i2 == 8) {
                i4 = STENCIL_INDEX8;
            } else if (i2 == 4) {
                i4 = STENCIL_INDEX4;
            } else if (i2 == 1) {
                i4 = STENCIL_INDEX1;
            }
            IntBuffer intBuffer4 = z ? this.glMultiStencil : this.glStencil;
            genRenderbuffers(1, intBuffer4);
            bindRenderbuffer(RENDERBUFFER, intBuffer4.get(0));
            if (z) {
                renderbufferStorageMultisample(RENDERBUFFER, this.numSamples, i4, this.fboWidth, this.fboHeight);
            } else {
                renderbufferStorage(RENDERBUFFER, i4, this.fboWidth, this.fboHeight);
            }
            framebufferRenderbuffer(FRAMEBUFFER, STENCIL_ATTACHMENT, RENDERBUFFER, intBuffer4.get(0));
        }
    }

    private void createFBOLayer() {
        float pixelScale = getPixelScale();
        if (hasNpotTexSupport()) {
            this.fboWidth = (int) (((float) this.graphics.width) * pixelScale);
            this.fboHeight = (int) (pixelScale * ((float) this.graphics.height));
        } else {
            this.fboWidth = nextPowerOfTwo((int) (((float) this.graphics.width) * pixelScale));
            this.fboHeight = nextPowerOfTwo((int) (pixelScale * ((float) this.graphics.height)));
        }
        if (hasFboMultisampleSupport()) {
            this.numSamples = PApplet.min(this.reqNumSamples, maxSamples());
        } else {
            this.numSamples = 1;
        }
        boolean z = 1 < this.numSamples;
        boolean hasPackedDepthStencilSupport = hasPackedDepthStencilSupport();
        int min = PApplet.min(REQUESTED_DEPTH_BITS, getDepthBits());
        int min2 = PApplet.min(REQUESTED_STENCIL_BITS, getStencilBits());
        genTextures(2, this.glColorTex);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= 2) {
                break;
            }
            bindTexture(TEXTURE_2D, this.glColorTex.get(i2));
            texParameteri(TEXTURE_2D, TEXTURE_MIN_FILTER, NEAREST);
            texParameteri(TEXTURE_2D, TEXTURE_MAG_FILTER, NEAREST);
            texParameteri(TEXTURE_2D, TEXTURE_WRAP_S, CLAMP_TO_EDGE);
            texParameteri(TEXTURE_2D, TEXTURE_WRAP_T, CLAMP_TO_EDGE);
            texImage2D(TEXTURE_2D, 0, RGBA, this.fboWidth, this.fboHeight, 0, RGBA, UNSIGNED_BYTE, (Buffer) null);
            initTexture(TEXTURE_2D, RGBA, this.fboWidth, this.fboHeight, this.graphics.backgroundColor);
            i = i2 + 1;
        }
        bindTexture(TEXTURE_2D, 0);
        this.backTex = 0;
        this.frontTex = 1;
        genFramebuffers(1, this.glColorFbo);
        bindFramebufferImpl(FRAMEBUFFER, this.glColorFbo.get(0));
        framebufferTexture2D(FRAMEBUFFER, COLOR_ATTACHMENT0, TEXTURE_2D, this.glColorTex.get(this.backTex), 0);
        if (!z || this.graphics.getHint(10)) {
            createDepthAndStencilBuffer(false, min, min2, hasPackedDepthStencilSupport);
        }
        if (z) {
            genFramebuffers(1, this.glMultiFbo);
            bindFramebufferImpl(FRAMEBUFFER, this.glMultiFbo.get(0));
            genRenderbuffers(1, this.glMultiColor);
            bindRenderbuffer(RENDERBUFFER, this.glMultiColor.get(0));
            renderbufferStorageMultisample(RENDERBUFFER, this.numSamples, RGBA8, this.fboWidth, this.fboHeight);
            framebufferRenderbuffer(FRAMEBUFFER, COLOR_ATTACHMENT0, RENDERBUFFER, this.glMultiColor.get(0));
            createDepthAndStencilBuffer(true, min, min2, hasPackedDepthStencilSupport);
        }
        validateFramebuffer();
        clearDepth(1.0f);
        clearStencil(0);
        int i3 = this.graphics.backgroundColor;
        clearColor(((float) ((i3 >> 16) & 255)) / 255.0f, ((float) ((i3 >> 8) & 255)) / 255.0f, ((float) (i3 & 255)) / 255.0f, ((float) ((i3 >> 24) & 255)) / 255.0f);
        clear(DEPTH_BUFFER_BIT | STENCIL_BUFFER_BIT | COLOR_BUFFER_BIT);
        bindFramebufferImpl(FRAMEBUFFER, 0);
        initFBOLayer();
        this.fboLayerCreated = true;
    }

    protected static void fillByteBuffer(ByteBuffer byteBuffer2, int i, int i2, byte b) {
        int i3 = i2 - i;
        byte[] bArr = new byte[i3];
        Arrays.fill(bArr, 0, i3, b);
        byteBuffer2.position(i);
        byteBuffer2.put(bArr, 0, i3);
        byteBuffer2.rewind();
    }

    protected static void fillFloatBuffer(FloatBuffer floatBuffer, int i, int i2, float f) {
        int i3 = i2 - i;
        float[] fArr = new float[i3];
        Arrays.fill(fArr, 0, i3, f);
        floatBuffer.position(i);
        floatBuffer.put(fArr, 0, i3);
        floatBuffer.rewind();
    }

    protected static void fillIntBuffer(IntBuffer intBuffer2, int i, int i2, int i3) {
        int i4 = i2 - i;
        int[] iArr = new int[i4];
        Arrays.fill(iArr, 0, i4, i3);
        intBuffer2.position(i);
        intBuffer2.put(iArr, 0, i4);
        intBuffer2.rewind();
    }

    protected static void fillShortBuffer(ShortBuffer shortBuffer, int i, int i2, short s) {
        int i3 = i2 - i;
        short[] sArr = new short[i3];
        Arrays.fill(sArr, 0, i3, s);
        shortBuffer.position(i);
        shortBuffer.put(sArr, 0, i3);
        shortBuffer.rewind();
    }

    protected static void getByteArray(ByteBuffer byteBuffer2, byte[] bArr) {
        if (!byteBuffer2.hasArray() || byteBuffer2.array() != bArr) {
            byteBuffer2.position(0);
            byteBuffer2.get(bArr);
            byteBuffer2.rewind();
        }
    }

    protected static void getFloatArray(FloatBuffer floatBuffer, float[] fArr) {
        if (!floatBuffer.hasArray() || floatBuffer.array() != fArr) {
            floatBuffer.position(0);
            floatBuffer.get(fArr);
            floatBuffer.rewind();
        }
    }

    protected static void getIntArray(IntBuffer intBuffer2, int[] iArr) {
        if (!intBuffer2.hasArray() || intBuffer2.array() != iArr) {
            intBuffer2.position(0);
            intBuffer2.get(iArr);
            intBuffer2.rewind();
        }
    }

    protected static void getShortArray(ShortBuffer shortBuffer, short[] sArr) {
        if (!shortBuffer.hasArray() || shortBuffer.array() != sArr) {
            shortBuffer.position(0);
            shortBuffer.get(sArr);
            shortBuffer.rewind();
        }
    }

    protected static boolean isPowerOfTwo(int i) {
        return ((i + -1) & i) == 0;
    }

    protected static int javaToNativeARGB(int i) {
        if (BIG_ENDIAN) {
            return (i >>> 24) | (i << 8);
        }
        int i2 = 16711935 & i;
        return (i2 >> 16) | (-16711936 & i) | (i2 << 16);
    }

    protected static void javaToNativeARGB(int[] iArr, int i, int i2) {
        int i3 = (i2 - 1) * i;
        int i4 = 0;
        for (int i5 = 0; i5 < i2 / 2; i5++) {
            int i6 = i3;
            int i7 = 0;
            while (i7 < i) {
                int i8 = iArr[i6];
                int i9 = iArr[i4];
                if (BIG_ENDIAN) {
                    iArr[i4] = (i8 << 8) | (i8 >>> 24);
                    iArr[i6] = (i9 >>> 24) | (i9 << 8);
                } else {
                    int i10 = i9 & 16711935;
                    int i11 = i8 & 16711935;
                    iArr[i4] = (i8 & -16711936) | (i11 << 16) | (i11 >> 16);
                    iArr[i6] = (i9 & -16711936) | (i10 << 16) | (i10 >> 16);
                }
                i7++;
                i6++;
                i4++;
            }
            i3 = i6 - (i * 2);
        }
        if (i2 % 2 == 1) {
            int i12 = (i2 / 2) * i;
            for (int i13 = 0; i13 < i; i13++) {
                int i14 = iArr[i12];
                if (BIG_ENDIAN) {
                    iArr[i12] = (i14 << 8) | (i14 >>> 24);
                } else {
                    int i15 = i14 & 16711935;
                    iArr[i12] = (i14 & -16711936) | (i15 << 16) | (i15 >> 16);
                }
                i12++;
            }
        }
    }

    protected static int javaToNativeRGB(int i) {
        if (BIG_ENDIAN) {
            return (i << 8) | 255;
        }
        int i2 = 16711935 & i;
        return (i2 >> 16) | -16777216 | (i2 << 16) | (65280 & i);
    }

    protected static void javaToNativeRGB(int[] iArr, int i, int i2) {
        int i3 = (i2 - 1) * i;
        int i4 = 0;
        for (int i5 = 0; i5 < i2 / 2; i5++) {
            int i6 = i3;
            int i7 = 0;
            while (i7 < i) {
                int i8 = iArr[i6];
                int i9 = iArr[i4];
                if (BIG_ENDIAN) {
                    iArr[i4] = (i8 << 8) | 255;
                    iArr[i6] = (i9 << 8) | 255;
                } else {
                    int i10 = i9 & 16711935;
                    int i11 = i8 & 16711935;
                    iArr[i4] = (i8 & 65280) | (i11 << 16) | -16777216 | (i11 >> 16);
                    iArr[i6] = (i10 << 16) | -16777216 | (i9 & 65280) | (i10 >> 16);
                }
                i7++;
                i6++;
                i4++;
            }
            i3 = i6 - (i * 2);
        }
        if (i2 % 2 == 1) {
            int i12 = (i2 / 2) * i;
            for (int i13 = 0; i13 < i; i13++) {
                int i14 = iArr[i12];
                if (BIG_ENDIAN) {
                    iArr[i12] = (i14 << 8) | 255;
                } else {
                    int i15 = i14 & 16711935;
                    iArr[i12] = (i14 & 65280) | (i15 << 16) | -16777216 | (i15 >> 16);
                }
                i12++;
            }
        }
    }

    protected static int nativeToJavaARGB(int i) {
        if (BIG_ENDIAN) {
            return (i >>> 8) | (i << 24);
        }
        int i2 = 16711935 & i;
        return (i2 >> 16) | (-16711936 & i) | (i2 << 16);
    }

    protected static void nativeToJavaARGB(int[] iArr, int i, int i2) {
        int i3 = (i2 - 1) * i;
        int i4 = 0;
        for (int i5 = 0; i5 < i2 / 2; i5++) {
            int i6 = i3;
            int i7 = 0;
            while (i7 < i) {
                int i8 = iArr[i6];
                int i9 = iArr[i4];
                if (BIG_ENDIAN) {
                    iArr[i4] = (i8 << 24) | (i8 >>> 8);
                    iArr[i6] = (i9 >>> 8) | (i9 << 24);
                } else {
                    int i10 = i9 & 16711935;
                    int i11 = i8 & 16711935;
                    iArr[i4] = (i8 & -16711936) | (i11 << 16) | (i11 >> 16);
                    iArr[i6] = (i9 & -16711936) | (i10 << 16) | (i10 >> 16);
                }
                i7++;
                i6++;
                i4++;
            }
            i3 = i6 - (i * 2);
        }
        if (i2 % 2 == 1) {
            int i12 = (i2 / 2) * i;
            for (int i13 = 0; i13 < i; i13++) {
                int i14 = iArr[i12];
                if (BIG_ENDIAN) {
                    iArr[i12] = (i14 << 24) | (i14 >>> 8);
                } else {
                    int i15 = i14 & 16711935;
                    iArr[i12] = (i14 & -16711936) | (i15 << 16) | (i15 >> 16);
                }
                i12++;
            }
        }
    }

    protected static int nativeToJavaRGB(int i) {
        if (BIG_ENDIAN) {
            return (i >>> 8) | -16777216;
        }
        int i2 = 16711935 & i;
        return (i2 >> 16) | (i2 << 16) | -16777216 | (65280 & i);
    }

    protected static void nativeToJavaRGB(int[] iArr, int i, int i2) {
        int i3 = (i2 - 1) * i;
        int i4 = 0;
        for (int i5 = 0; i5 < i2 / 2; i5++) {
            int i6 = i3;
            int i7 = 0;
            while (i7 < i) {
                int i8 = iArr[i6];
                int i9 = iArr[i4];
                if (BIG_ENDIAN) {
                    iArr[i4] = (i8 >>> 8) | -16777216;
                    iArr[i6] = (i9 >>> 8) | -16777216;
                } else {
                    int i10 = i9 & 16711935;
                    int i11 = i8 & 16711935;
                    iArr[i4] = (i8 & 65280) | (i11 << 16) | -16777216 | (i11 >> 16);
                    iArr[i6] = (i10 << 16) | -16777216 | (i9 & 65280) | (i10 >> 16);
                }
                i7++;
                i6++;
                i4++;
            }
            i3 = i6 - (i * 2);
        }
        if (i2 % 2 == 1) {
            int i12 = (i2 / 2) * i;
            for (int i13 = 0; i13 < i; i13++) {
                int i14 = iArr[i12];
                if (BIG_ENDIAN) {
                    iArr[i12] = (i14 >>> 8) | -16777216;
                } else {
                    int i15 = i14 & 16711935;
                    iArr[i12] = (i14 & 65280) | (i15 << 16) | -16777216 | (i15 >> 16);
                }
                i12++;
            }
        }
    }

    protected static int nextPowerOfTwo(int i) {
        int i2 = 1;
        while (i2 < i) {
            i2 <<= 1;
        }
        return i2;
    }

    protected static String[] preprocessFragmentSource(String[] strArr, int i) {
        String[] preprocessShaderSource;
        if (containsVersionDirective(strArr)) {
            return strArr;
        }
        if (i < 130) {
            preprocessShaderSource = preprocessShaderSource(strArr, new Pattern[0], new String[0], 1);
            preprocessShaderSource[0] = "#version " + i;
        } else {
            preprocessShaderSource = preprocessShaderSource(strArr, new Pattern[]{Pattern.compile(String.format(GLSL_ID_REGEX, new Object[]{"varying|attribute"})), Pattern.compile(String.format(GLSL_ID_REGEX, new Object[]{"texture"})), Pattern.compile(String.format(GLSL_FN_REGEX, new Object[]{"textureRect|texture2D|texture3D|textureCube"})), Pattern.compile(String.format(GLSL_ID_REGEX, new Object[]{"gl_FragColor"}))}, new String[]{"in", "texMap", "texture", "_fragColor"}, 2);
            preprocessShaderSource[0] = "#version " + i;
            preprocessShaderSource[1] = "out vec4 _fragColor;";
        }
        return preprocessShaderSource;
    }

    protected static String[] preprocessShaderSource(String[] strArr, Pattern[] patternArr, String[] strArr2, int i) {
        String[] strArr3 = new String[(strArr.length + i)];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            if (str.contains("#version")) {
                str = "";
            }
            String str2 = str;
            for (int i3 = 0; i3 < patternArr.length; i3++) {
                str2 = patternArr[i3].matcher(str2).replaceAll(strArr2[i3]);
            }
            strArr3[i2 + i] = str2;
        }
        return strArr3;
    }

    protected static String[] preprocessVertexSource(String[] strArr, int i) {
        String[] preprocessShaderSource;
        if (containsVersionDirective(strArr)) {
            return strArr;
        }
        if (i < 130) {
            preprocessShaderSource = preprocessShaderSource(strArr, new Pattern[0], new String[0], 1);
            preprocessShaderSource[0] = "#version " + i;
        } else {
            preprocessShaderSource = preprocessShaderSource(strArr, new Pattern[]{Pattern.compile(String.format(GLSL_ID_REGEX, new Object[]{"varying"})), Pattern.compile(String.format(GLSL_ID_REGEX, new Object[]{"attribute"})), Pattern.compile(String.format(GLSL_ID_REGEX, new Object[]{"texture"})), Pattern.compile(String.format(GLSL_FN_REGEX, new Object[]{"textureRect|texture2D|texture3D|textureCube"}))}, new String[]{"out", "in", "texMap", "texture"}, 1);
            preprocessShaderSource[0] = "#version " + i;
        }
        return preprocessShaderSource;
    }

    protected static void putByteArray(ByteBuffer byteBuffer2, byte[] bArr) {
        if (!byteBuffer2.hasArray() || byteBuffer2.array() != bArr) {
            byteBuffer2.position(0);
            byteBuffer2.put(bArr);
            byteBuffer2.rewind();
        }
    }

    protected static void putFloatArray(FloatBuffer floatBuffer, float[] fArr) {
        if (!floatBuffer.hasArray() || floatBuffer.array() != fArr) {
            floatBuffer.position(0);
            floatBuffer.put(fArr);
            floatBuffer.rewind();
        }
    }

    protected static void putIntArray(IntBuffer intBuffer2, int[] iArr) {
        if (!intBuffer2.hasArray() || intBuffer2.array() != iArr) {
            intBuffer2.position(0);
            intBuffer2.put(iArr);
            intBuffer2.rewind();
        }
    }

    protected static void putShortArray(ShortBuffer shortBuffer, short[] sArr) {
        if (!shortBuffer.hasArray() || shortBuffer.array() != sArr) {
            shortBuffer.position(0);
            shortBuffer.put(sArr);
            shortBuffer.rewind();
        }
    }

    protected static int qualityToSamples(int i) {
        if (i <= 1) {
            return 1;
        }
        return (i / 2) * 2;
    }

    public static int smoothToSamples(int i) {
        if (i == 0) {
            return 1;
        }
        if (i == 1) {
            return 2;
        }
        return i;
    }

    protected static ByteBuffer updateByteBuffer(ByteBuffer byteBuffer2, byte[] bArr, boolean z) {
        if (USE_DIRECT_BUFFERS) {
            if (byteBuffer2 == null || byteBuffer2.capacity() < bArr.length) {
                byteBuffer2 = allocateDirectByteBuffer(bArr.length);
            }
            byteBuffer2.position(0);
            byteBuffer2.put(bArr);
            byteBuffer2.rewind();
            return byteBuffer2;
        } else if (z) {
            return ByteBuffer.wrap(bArr);
        } else {
            if (byteBuffer2 == null || byteBuffer2.capacity() < bArr.length) {
                byteBuffer2 = ByteBuffer.allocate(bArr.length);
            }
            byteBuffer2.position(0);
            byteBuffer2.put(bArr);
            byteBuffer2.rewind();
            return byteBuffer2;
        }
    }

    protected static void updateByteBuffer(ByteBuffer byteBuffer2, byte[] bArr, int i, int i2) {
        if (USE_DIRECT_BUFFERS || (byteBuffer2.hasArray() && byteBuffer2.array() != bArr)) {
            byteBuffer2.position(i);
            byteBuffer2.put(bArr, i, i2);
            byteBuffer2.rewind();
        }
    }

    protected static FloatBuffer updateFloatBuffer(FloatBuffer floatBuffer, float[] fArr, boolean z) {
        if (USE_DIRECT_BUFFERS) {
            if (floatBuffer == null || floatBuffer.capacity() < fArr.length) {
                floatBuffer = allocateDirectFloatBuffer(fArr.length);
            }
            floatBuffer.position(0);
            floatBuffer.put(fArr);
            floatBuffer.rewind();
            return floatBuffer;
        } else if (z) {
            return FloatBuffer.wrap(fArr);
        } else {
            if (floatBuffer == null || floatBuffer.capacity() < fArr.length) {
                floatBuffer = FloatBuffer.allocate(fArr.length);
            }
            floatBuffer.position(0);
            floatBuffer.put(fArr);
            floatBuffer.rewind();
            return floatBuffer;
        }
    }

    protected static void updateFloatBuffer(FloatBuffer floatBuffer, float[] fArr, int i, int i2) {
        if (USE_DIRECT_BUFFERS || (floatBuffer.hasArray() && floatBuffer.array() != fArr)) {
            floatBuffer.position(i);
            floatBuffer.put(fArr, i, i2);
            floatBuffer.rewind();
        }
    }

    protected static IntBuffer updateIntBuffer(IntBuffer intBuffer2, int[] iArr, boolean z) {
        if (USE_DIRECT_BUFFERS) {
            if (intBuffer2 == null || intBuffer2.capacity() < iArr.length) {
                intBuffer2 = allocateDirectIntBuffer(iArr.length);
            }
            intBuffer2.position(0);
            intBuffer2.put(iArr);
            intBuffer2.rewind();
            return intBuffer2;
        } else if (z) {
            return IntBuffer.wrap(iArr);
        } else {
            if (intBuffer2 == null || intBuffer2.capacity() < iArr.length) {
                intBuffer2 = IntBuffer.allocate(iArr.length);
            }
            intBuffer2.position(0);
            intBuffer2.put(iArr);
            intBuffer2.rewind();
            return intBuffer2;
        }
    }

    protected static void updateIntBuffer(IntBuffer intBuffer2, int[] iArr, int i, int i2) {
        if (USE_DIRECT_BUFFERS || (intBuffer2.hasArray() && intBuffer2.array() != iArr)) {
            intBuffer2.position(i);
            intBuffer2.put(iArr, i, i2);
            intBuffer2.rewind();
        }
    }

    protected static ShortBuffer updateShortBuffer(ShortBuffer shortBuffer, short[] sArr, boolean z) {
        if (USE_DIRECT_BUFFERS) {
            if (shortBuffer == null || shortBuffer.capacity() < sArr.length) {
                shortBuffer = allocateDirectShortBuffer(sArr.length);
            }
            shortBuffer.position(0);
            shortBuffer.put(sArr);
            shortBuffer.rewind();
            return shortBuffer;
        } else if (z) {
            return ShortBuffer.wrap(sArr);
        } else {
            if (shortBuffer == null || shortBuffer.capacity() < sArr.length) {
                shortBuffer = ShortBuffer.allocate(sArr.length);
            }
            shortBuffer.position(0);
            shortBuffer.put(sArr);
            shortBuffer.rewind();
            return shortBuffer;
        }
    }

    protected static void updateShortBuffer(ShortBuffer shortBuffer, short[] sArr, int i, int i2) {
        if (USE_DIRECT_BUFFERS || (shortBuffer.hasArray() && shortBuffer.array() != sArr)) {
            shortBuffer.position(i);
            shortBuffer.put(sArr, i, i2);
            shortBuffer.rewind();
        }
    }

    public void activeTexture(int i) {
        this.activeTexUnit = i - TEXTURE0;
        activeTextureImpl(i);
    }

    /* access modifiers changed from: protected */
    public abstract void activeTextureImpl(int i);

    public abstract void attachShader(int i, int i2);

    /* access modifiers changed from: protected */
    public void beginGL() {
    }

    /* access modifiers changed from: protected */
    public void beginRender() {
        if (this.sketch == null) {
            this.sketch = this.graphics.parent;
        }
        this.pgeomCount = this.geomCount;
        this.geomCount = 0;
        this.pclearColor = this.clearColor;
        this.clearColor = false;
        if (SINGLE_BUFFERED && this.sketch.frameCount == 1) {
            restoreFirstFrame();
        }
        if (this.fboLayerEnabledReq) {
            this.fboLayerEnabled = true;
            this.fboLayerEnabledReq = false;
        }
        if (this.fboLayerEnabled) {
            if (this.fbolayerResetReq) {
                destroyFBOLayer();
                this.fbolayerResetReq = false;
            }
            if (!this.fboLayerCreated) {
                createFBOLayer();
            }
            bindFramebufferImpl(FRAMEBUFFER, this.glColorFbo.get(0));
            framebufferTexture2D(FRAMEBUFFER, COLOR_ATTACHMENT0, TEXTURE_2D, this.glColorTex.get(this.backTex), 0);
            if (1 < this.numSamples) {
                bindFramebufferImpl(FRAMEBUFFER, this.glMultiFbo.get(0));
            }
            if (this.sketch.frameCount == 0) {
                int i = this.graphics.backgroundColor;
                clearColor(((float) ((i >> 16) & 255)) / 255.0f, ((float) ((i >> 8) & 255)) / 255.0f, ((float) (i & 255)) / 255.0f, ((float) ((i >> 24) & 255)) / 255.0f);
                clear(COLOR_BUFFER_BIT);
            } else if (!this.pclearColor || !this.sketch.isLooping()) {
                int i2 = 0;
                int i3 = 0;
                if (this.presentMode) {
                    i2 = (int) this.presentX;
                    i3 = (int) this.presentY;
                }
                float pixelScale = getPixelScale();
                drawTexture(TEXTURE_2D, this.glColorTex.get(this.frontTex), this.fboWidth, this.fboHeight, i2, i3, this.graphics.width, this.graphics.height, 0, 0, (int) (((float) this.graphics.width) * pixelScale), (int) (pixelScale * ((float) this.graphics.height)), 0, 0, this.graphics.width, this.graphics.height);
            }
        }
    }

    public abstract void bindAttribLocation(int i, int i2, String str);

    public abstract void bindBuffer(int i, int i2);

    public void bindFramebuffer(int i, int i2) {
        this.graphics.beginBindFramebuffer(i, i2);
        bindFramebufferImpl(i, i2);
        this.graphics.endBindFramebuffer(i, i2);
    }

    /* access modifiers changed from: protected */
    public abstract void bindFramebufferImpl(int i, int i2);

    /* access modifiers changed from: protected */
    public void bindFrontTexture() {
        this.usingFrontTex = true;
        if (!texturingIsEnabled(TEXTURE_2D)) {
            enableTexturing(TEXTURE_2D);
        }
        bindTexture(TEXTURE_2D, this.glColorTex.get(this.frontTex));
    }

    public abstract void bindRenderbuffer(int i, int i2);

    public void bindTexture(int i, int i2) {
        bindTextureImpl(i, i2);
        if (this.boundTextures == null) {
            this.maxTexUnits = getMaxTexUnits();
            this.boundTextures = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.maxTexUnits, 2});
        }
        if (this.maxTexUnits <= this.activeTexUnit) {
            throw new RuntimeException(TEXUNIT_ERROR);
        } else if (i == TEXTURE_2D) {
            this.boundTextures[this.activeTexUnit][0] = i2;
        } else if (i == TEXTURE_RECTANGLE) {
            this.boundTextures[this.activeTexUnit][1] = i2;
        }
    }

    /* access modifiers changed from: protected */
    public abstract void bindTextureImpl(int i, int i2);

    public abstract void blendColor(float f, float f2, float f3, float f4);

    public abstract void blendEquation(int i);

    public abstract void blendEquationSeparate(int i, int i2);

    public abstract void blendFunc(int i, int i2);

    public abstract void blendFuncSeparate(int i, int i2, int i3, int i4);

    public abstract void blitFramebuffer(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    public abstract void bufferData(int i, int i2, Buffer buffer, int i3);

    public abstract void bufferSubData(int i, int i2, int i3, Buffer buffer);

    /* access modifiers changed from: protected */
    public abstract boolean canDraw();

    public abstract int checkFramebufferStatus(int i);

    public abstract void clear(int i);

    /* access modifiers changed from: protected */
    public void clearBackground(float f, float f2, float f3, float f4, boolean z) {
        if (z) {
            clearDepth(1.0f);
            clear(DEPTH_BUFFER_BIT);
        }
        clearColor(f, f2, f3, f4);
        clear(COLOR_BUFFER_BIT);
        if (this.sketch.frameCount > 0) {
            this.clearColor = true;
        }
    }

    public abstract void clearColor(float f, float f2, float f3, float f4);

    public abstract void clearDepth(float f);

    public abstract void clearStencil(int i);

    public abstract int clientWaitSync(long j, int i, long j2);

    public abstract void colorMask(boolean z, boolean z2, boolean z3, boolean z4);

    public abstract void compileShader(int i);

    /* access modifiers changed from: protected */
    public boolean compiled(int i) {
        this.intBuffer.rewind();
        getShaderiv(i, COMPILE_STATUS, this.intBuffer);
        return this.intBuffer.get(0) != 0;
    }

    public abstract void compressedTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, Buffer buffer);

    public abstract void compressedTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer);

    /* access modifiers changed from: protected */
    public boolean contextIsCurrent(int i) {
        return i == -1 || i == this.glContext;
    }

    public abstract void copyTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    public abstract void copyTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    /* access modifiers changed from: protected */
    public void copyToTexture(int i, int i2, int i3, int i4, int i5, int i6, int i7, IntBuffer intBuffer2) {
        boolean z;
        activeTexture(TEXTURE0);
        if (!texturingIsEnabled(i)) {
            enableTexturing(i);
            z = true;
        } else {
            z = false;
        }
        bindTexture(i, i3);
        texSubImage2D(i, 0, i4, i5, i6, i7, i2, UNSIGNED_BYTE, intBuffer2);
        bindTexture(i, 0);
        if (z) {
            disableTexturing(i);
        }
    }

    /* access modifiers changed from: protected */
    public void copyToTexture(int i, int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        copyToTexture(i, i2, i3, i4, i5, i6, i7, IntBuffer.wrap(iArr));
    }

    /* access modifiers changed from: protected */
    public int createEmptyContext() {
        return -1;
    }

    /* access modifiers changed from: protected */
    public abstract FontOutline createFontOutline(char c, Object obj);

    public abstract int createProgram();

    /* access modifiers changed from: protected */
    public int createProgram(int i, int i2) {
        int createProgram = createProgram();
        if (createProgram == 0) {
            return createProgram;
        }
        attachShader(createProgram, i);
        attachShader(createProgram, i2);
        linkProgram(createProgram);
        if (linked(createProgram)) {
            return createProgram;
        }
        System.err.println("Could not link program: ");
        System.err.println(getProgramInfoLog(createProgram));
        deleteProgram(createProgram);
        return 0;
    }

    public abstract int createShader(int i);

    /* access modifiers changed from: protected */
    public int createShader(int i, String str) {
        int createShader = createShader(i);
        if (createShader == 0) {
            return createShader;
        }
        shaderSource(createShader, str);
        compileShader(createShader);
        if (compiled(createShader)) {
            return createShader;
        }
        System.err.println("Could not compile shader " + i + ":");
        System.err.println(getShaderInfoLog(createShader));
        deleteShader(createShader);
        return 0;
    }

    /* access modifiers changed from: protected */
    public abstract Tessellator createTessellator(TessellatorCallback tessellatorCallback);

    public abstract void cullFace(int i);

    public abstract void deleteBuffers(int i, IntBuffer intBuffer2);

    public abstract void deleteFramebuffers(int i, IntBuffer intBuffer2);

    public abstract void deleteProgram(int i);

    public abstract void deleteRenderbuffers(int i, IntBuffer intBuffer2);

    public abstract void deleteShader(int i);

    public abstract void deleteSync(long j);

    public abstract void deleteTextures(int i, IntBuffer intBuffer2);

    public abstract void depthFunc(int i);

    public abstract void depthMask(boolean z);

    public abstract void depthRangef(float f, float f2);

    /* access modifiers changed from: protected */
    public void destroyFBOLayer() {
        if (threadIsCurrent() && this.fboLayerCreated) {
            deleteFramebuffers(1, this.glColorFbo);
            deleteTextures(2, this.glColorTex);
            deleteRenderbuffers(1, this.glDepthStencil);
            deleteRenderbuffers(1, this.glDepth);
            deleteRenderbuffers(1, this.glStencil);
            deleteFramebuffers(1, this.glMultiFbo);
            deleteRenderbuffers(1, this.glMultiColor);
            deleteRenderbuffers(1, this.glMultiDepthStencil);
            deleteRenderbuffers(1, this.glMultiDepth);
            deleteRenderbuffers(1, this.glMultiStencil);
        }
        this.fboLayerCreated = false;
    }

    public abstract void detachShader(int i, int i2);

    public abstract void disable(int i);

    public void disableFBOLayer() {
        this.fboLayerDisableReq = true;
    }

    /* access modifiers changed from: protected */
    public void disableTexturing(int i) {
        if (i == TEXTURE_2D) {
            this.texturingTargets[0] = false;
        } else if (i == TEXTURE_RECTANGLE) {
            this.texturingTargets[1] = false;
        }
    }

    public abstract void disableVertexAttribArray(int i);

    public void dispose() {
        this.graphics = null;
        this.sketch = null;
        destroyFBOLayer();
    }

    public void drawArrays(int i, int i2, int i3) {
        this.geomCount += i3;
        drawArraysImpl(i, i2, i3);
    }

    public abstract void drawArraysImpl(int i, int i2, int i3);

    public abstract void drawBuffer(int i);

    public void drawElements(int i, int i2, int i3, int i4) {
        this.geomCount += i2;
        drawElementsImpl(i, i2, i3, i4);
    }

    public abstract void drawElementsImpl(int i, int i2, int i3, int i4);

    public void drawTexture(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        drawTexture(i, i2, i3, i4, 0, 0, i3, i4, 1, i5, i6, i7, i8, i5, i6, i7, i8);
    }

    public void drawTexture(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16) {
        drawTexture(i, i2, i3, i4, i5, i6, i7, i8, (int) getPixelScale(), i9, i10, i11, i12, i13, i14, i15, i16);
    }

    public void drawTexture(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
        if (i == TEXTURE_2D) {
            drawTexture2D(i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16, i17);
        } else if (i == TEXTURE_RECTANGLE) {
            drawTextureRect(i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16, i17);
        }
    }

    /* access modifiers changed from: protected */
    public void drawTexture2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16) {
        boolean z;
        PGL initTex2DShader = initTex2DShader();
        if (initTex2DShader.tex2DShaderProgram > 0) {
            boolean depthTest = getDepthTest();
            disable(DEPTH_TEST);
            boolean depthWriteMask = getDepthWriteMask();
            depthMask(false);
            this.viewBuffer.rewind();
            getIntegerv(VIEWPORT, this.viewBuffer);
            viewportImpl(i8 * i4, i8 * i5, i8 * i6, i8 * i7);
            useProgram(initTex2DShader.tex2DShaderProgram);
            enableVertexAttribArray(initTex2DShader.tex2DVertLoc);
            enableVertexAttribArray(initTex2DShader.tex2DTCoordLoc);
            this.texCoords[0] = ((2.0f * ((float) i13)) / ((float) i6)) - 1.0f;
            this.texCoords[1] = ((2.0f * ((float) i14)) / ((float) i7)) - 1.0f;
            this.texCoords[2] = ((float) i9) / ((float) i2);
            this.texCoords[3] = ((float) i10) / ((float) i3);
            this.texCoords[4] = ((2.0f * ((float) i15)) / ((float) i6)) - 1.0f;
            this.texCoords[5] = ((2.0f * ((float) i14)) / ((float) i7)) - 1.0f;
            this.texCoords[6] = ((float) i11) / ((float) i2);
            this.texCoords[7] = ((float) i10) / ((float) i3);
            this.texCoords[8] = ((2.0f * ((float) i13)) / ((float) i6)) - 1.0f;
            this.texCoords[9] = ((2.0f * ((float) i16)) / ((float) i7)) - 1.0f;
            this.texCoords[10] = ((float) i9) / ((float) i2);
            this.texCoords[11] = ((float) i12) / ((float) i3);
            this.texCoords[12] = ((2.0f * ((float) i15)) / ((float) i6)) - 1.0f;
            this.texCoords[13] = ((2.0f * ((float) i16)) / ((float) i7)) - 1.0f;
            this.texCoords[14] = ((float) i11) / ((float) i2);
            this.texCoords[15] = ((float) i12) / ((float) i3);
            this.texData.rewind();
            this.texData.put(this.texCoords);
            activeTexture(TEXTURE0);
            if (!texturingIsEnabled(TEXTURE_2D)) {
                enableTexturing(TEXTURE_2D);
                z = true;
            } else {
                z = false;
            }
            bindTexture(TEXTURE_2D, i);
            uniform1i(initTex2DShader.tex2DSamplerLoc, 0);
            this.texData.position(0);
            bindBuffer(ARRAY_BUFFER, initTex2DShader.tex2DGeoVBO);
            bufferData(ARRAY_BUFFER, SIZEOF_FLOAT * 16, this.texData, STATIC_DRAW);
            vertexAttribPointer(initTex2DShader.tex2DVertLoc, 2, FLOAT, false, SIZEOF_FLOAT * 4, 0);
            vertexAttribPointer(initTex2DShader.tex2DTCoordLoc, 2, FLOAT, false, SIZEOF_FLOAT * 4, SIZEOF_FLOAT * 2);
            drawArrays(TRIANGLE_STRIP, 0, 4);
            bindBuffer(ARRAY_BUFFER, 0);
            bindTexture(TEXTURE_2D, 0);
            if (z) {
                disableTexturing(TEXTURE_2D);
            }
            disableVertexAttribArray(initTex2DShader.tex2DVertLoc);
            disableVertexAttribArray(initTex2DShader.tex2DTCoordLoc);
            useProgram(0);
            if (depthTest) {
                enable(DEPTH_TEST);
            } else {
                disable(DEPTH_TEST);
            }
            depthMask(depthWriteMask);
            viewportImpl(this.viewBuffer.get(0), this.viewBuffer.get(1), this.viewBuffer.get(2), this.viewBuffer.get(3));
        }
    }

    /* access modifiers changed from: protected */
    public void drawTextureRect(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16) {
        boolean z;
        PGL initTexRectShader = initTexRectShader();
        if (this.texData == null) {
            this.texData = allocateDirectFloatBuffer(this.texCoords.length);
        }
        if (initTexRectShader.texRectShaderProgram > 0) {
            boolean depthTest = getDepthTest();
            disable(DEPTH_TEST);
            boolean depthWriteMask = getDepthWriteMask();
            depthMask(false);
            this.viewBuffer.rewind();
            getIntegerv(VIEWPORT, this.viewBuffer);
            viewportImpl(i8 * i4, i8 * i5, i8 * i6, i8 * i7);
            useProgram(initTexRectShader.texRectShaderProgram);
            enableVertexAttribArray(initTexRectShader.texRectVertLoc);
            enableVertexAttribArray(initTexRectShader.texRectTCoordLoc);
            this.texCoords[0] = ((2.0f * ((float) i13)) / ((float) i6)) - 1.0f;
            this.texCoords[1] = ((2.0f * ((float) i14)) / ((float) i7)) - 1.0f;
            this.texCoords[2] = (float) i9;
            this.texCoords[3] = (float) i10;
            this.texCoords[4] = ((2.0f * ((float) i15)) / ((float) i6)) - 1.0f;
            this.texCoords[5] = ((2.0f * ((float) i14)) / ((float) i7)) - 1.0f;
            this.texCoords[6] = (float) i11;
            this.texCoords[7] = (float) i10;
            this.texCoords[8] = ((2.0f * ((float) i13)) / ((float) i6)) - 1.0f;
            this.texCoords[9] = ((2.0f * ((float) i16)) / ((float) i7)) - 1.0f;
            this.texCoords[10] = (float) i9;
            this.texCoords[11] = (float) i12;
            this.texCoords[12] = ((2.0f * ((float) i15)) / ((float) i6)) - 1.0f;
            this.texCoords[13] = ((2.0f * ((float) i16)) / ((float) i7)) - 1.0f;
            this.texCoords[14] = (float) i11;
            this.texCoords[15] = (float) i12;
            this.texData.rewind();
            this.texData.put(this.texCoords);
            activeTexture(TEXTURE0);
            if (!texturingIsEnabled(TEXTURE_RECTANGLE)) {
                enableTexturing(TEXTURE_RECTANGLE);
                z = true;
            } else {
                z = false;
            }
            bindTexture(TEXTURE_RECTANGLE, i);
            uniform1i(initTexRectShader.texRectSamplerLoc, 0);
            this.texData.position(0);
            bindBuffer(ARRAY_BUFFER, initTexRectShader.texRectGeoVBO);
            bufferData(ARRAY_BUFFER, SIZEOF_FLOAT * 16, this.texData, STATIC_DRAW);
            vertexAttribPointer(initTexRectShader.texRectVertLoc, 2, FLOAT, false, SIZEOF_FLOAT * 4, 0);
            vertexAttribPointer(initTexRectShader.texRectTCoordLoc, 2, FLOAT, false, SIZEOF_FLOAT * 4, SIZEOF_FLOAT * 2);
            drawArrays(TRIANGLE_STRIP, 0, 4);
            bindBuffer(ARRAY_BUFFER, 0);
            bindTexture(TEXTURE_RECTANGLE, 0);
            if (z) {
                disableTexturing(TEXTURE_RECTANGLE);
            }
            disableVertexAttribArray(initTexRectShader.texRectVertLoc);
            disableVertexAttribArray(initTexRectShader.texRectTCoordLoc);
            useProgram(0);
            if (depthTest) {
                enable(DEPTH_TEST);
            } else {
                disable(DEPTH_TEST);
            }
            depthMask(depthWriteMask);
            viewportImpl(this.viewBuffer.get(0), this.viewBuffer.get(1), this.viewBuffer.get(2), this.viewBuffer.get(3));
        }
    }

    public abstract void enable(int i);

    public void enableFBOLayer() {
        this.fboLayerEnabledReq = true;
    }

    /* access modifiers changed from: protected */
    public void enableTexturing(int i) {
        if (i == TEXTURE_2D) {
            this.texturingTargets[0] = true;
        } else if (i == TEXTURE_RECTANGLE) {
            this.texturingTargets[1] = true;
        }
    }

    public abstract void enableVertexAttribArray(int i);

    /* access modifiers changed from: protected */
    public void endGL() {
    }

    /* access modifiers changed from: protected */
    public void endRender(int i) {
        if (this.fboLayerEnabled) {
            syncBackTexture();
            bindFramebufferImpl(FRAMEBUFFER, 0);
            if (this.presentMode) {
                clearDepth(1.0f);
                clearColor(((float) ((i >> 16) & 255)) / 255.0f, ((float) ((i >> 8) & 255)) / 255.0f, ((float) (i & 255)) / 255.0f, ((float) ((i >> 24) & 255)) / 255.0f);
                clear(COLOR_BUFFER_BIT | DEPTH_BUFFER_BIT);
                if (this.showStopButton) {
                    if (this.closeButtonTex == null) {
                        this.closeButtonTex = allocateIntBuffer(1);
                        genTextures(1, this.closeButtonTex);
                        bindTexture(TEXTURE_2D, this.closeButtonTex.get(0));
                        texParameteri(TEXTURE_2D, TEXTURE_MIN_FILTER, NEAREST);
                        texParameteri(TEXTURE_2D, TEXTURE_MAG_FILTER, NEAREST);
                        texParameteri(TEXTURE_2D, TEXTURE_WRAP_S, CLAMP_TO_EDGE);
                        texParameteri(TEXTURE_2D, TEXTURE_WRAP_T, CLAMP_TO_EDGE);
                        texImage2D(TEXTURE_2D, 0, RGBA, this.stopButtonWidth, this.stopButtonHeight, 0, RGBA, UNSIGNED_BYTE, (Buffer) null);
                        int[] iArr = new int[closeButtonPix.length];
                        PApplet.arrayCopy(closeButtonPix, iArr);
                        float f = ((float) ((this.stopButtonColor >> 24) & 255)) / 255.0f;
                        float f2 = ((float) ((this.stopButtonColor >> 16) & 255)) / 255.0f;
                        float f3 = ((float) ((this.stopButtonColor >> 8) & 255)) / 255.0f;
                        float f4 = ((float) ((this.stopButtonColor >> 0) & 255)) / 255.0f;
                        for (int i2 = 0; i2 < iArr.length; i2++) {
                            int i3 = closeButtonPix[i2];
                            iArr[i2] = javaToNativeARGB(((int) (((float) ((i3 >> 0) & 255)) * f4)) | (((int) (((float) ((i3 >> 24) & 255)) * f)) << 24) | (((int) (((float) ((i3 >> 16) & 255)) * f2)) << 16) | (((int) (((float) ((i3 >> 8) & 255)) * f3)) << 8));
                        }
                        copyToTexture(TEXTURE_2D, RGBA, this.closeButtonTex.get(0), 0, 0, this.stopButtonWidth, this.stopButtonHeight, allocateIntBuffer(iArr));
                        bindTexture(TEXTURE_2D, 0);
                    }
                    drawTexture(TEXTURE_2D, this.closeButtonTex.get(0), this.stopButtonWidth, this.stopButtonHeight, 0, 0, this.stopButtonWidth + this.stopButtonX, this.stopButtonHeight + this.closeButtonY, 0, this.stopButtonHeight, this.stopButtonWidth, 0, this.stopButtonX, this.closeButtonY, this.stopButtonWidth + this.stopButtonX, this.stopButtonHeight + this.closeButtonY);
                }
            } else {
                clearDepth(1.0f);
                clearColor(0.0f, 0.0f, 0.0f, 0.0f);
                clear(COLOR_BUFFER_BIT | DEPTH_BUFFER_BIT);
            }
            disable(BLEND);
            int i4 = 0;
            int i5 = 0;
            if (this.presentMode) {
                i4 = (int) this.presentX;
                i5 = (int) this.presentY;
            }
            float pixelScale = getPixelScale();
            drawTexture(TEXTURE_2D, this.glColorTex.get(this.backTex), this.fboWidth, this.fboHeight, i4, i5, this.graphics.width, this.graphics.height, 0, 0, (int) (((float) this.graphics.width) * pixelScale), (int) (pixelScale * ((float) this.graphics.height)), 0, 0, this.graphics.width, this.graphics.height);
            int i6 = this.frontTex;
            this.frontTex = this.backTex;
            this.backTex = i6;
            if (this.fboLayerDisableReq) {
                this.fboLayerEnabled = false;
                this.fboLayerDisableReq = false;
                return;
            }
            return;
        }
        if (SINGLE_BUFFERED && this.sketch.frameCount == 0) {
            saveFirstFrame();
        }
        if ((!this.clearColor && this.sketch.frameCount > 0) || !this.sketch.isLooping()) {
            enableFBOLayer();
            if (SINGLE_BUFFERED) {
                createFBOLayer();
            }
        }
    }

    public abstract String errorString(int i);

    public abstract long fenceSync(int i, int i2);

    public abstract void finish();

    public abstract void flush();

    public abstract void framebufferRenderbuffer(int i, int i2, int i3, int i4);

    public abstract void framebufferTexture2D(int i, int i2, int i3, int i4, int i5);

    public abstract void frontFace(int i);

    public abstract void genBuffers(int i, IntBuffer intBuffer2);

    public abstract void genFramebuffers(int i, IntBuffer intBuffer2);

    public abstract void genRenderbuffers(int i, IntBuffer intBuffer2);

    public abstract void genTextures(int i, IntBuffer intBuffer2);

    public abstract void generateMipmap(int i);

    public abstract String getActiveAttrib(int i, int i2, IntBuffer intBuffer2, IntBuffer intBuffer3);

    public abstract String getActiveUniform(int i, int i2, IntBuffer intBuffer2, IntBuffer intBuffer3);

    public abstract void getAttachedShaders(int i, int i2, IntBuffer intBuffer2, IntBuffer intBuffer3);

    public abstract int getAttribLocation(int i, String str);

    public abstract void getBooleanv(int i, IntBuffer intBuffer2);

    public abstract void getBufferParameteriv(int i, int i2, IntBuffer intBuffer2);

    /* access modifiers changed from: protected */
    public int getColorValue(int i, int i2) {
        if (this.colorBuffer == null) {
            this.colorBuffer = IntBuffer.allocate(1);
        }
        this.colorBuffer.rewind();
        readPixels(i, (this.graphics.height - i2) - 1, 1, 1, RGBA, UNSIGNED_BYTE, (Buffer) this.colorBuffer);
        return this.colorBuffer.get();
    }

    /* access modifiers changed from: protected */
    public int getCurrentContext() {
        return this.glContext;
    }

    /* access modifiers changed from: protected */
    public int getDefaultDrawBuffer() {
        return this.fboLayerEnabled ? COLOR_ATTACHMENT0 : BACK;
    }

    /* access modifiers changed from: protected */
    public int getDefaultReadBuffer() {
        return this.fboLayerEnabled ? COLOR_ATTACHMENT0 : FRONT;
    }

    /* access modifiers changed from: protected */
    public abstract int getDepthBits();

    /* access modifiers changed from: protected */
    public boolean getDepthTest() {
        this.intBuffer.rewind();
        getBooleanv(DEPTH_TEST, this.intBuffer);
        return this.intBuffer.get(0) != 0;
    }

    /* access modifiers changed from: protected */
    public float getDepthValue(int i, int i2) {
        if (this.depthBuffer == null) {
            this.depthBuffer = FloatBuffer.allocate(1);
        }
        this.depthBuffer.rewind();
        readPixels(i, (this.graphics.height - i2) - 1, 1, 1, DEPTH_COMPONENT, FLOAT, (Buffer) this.depthBuffer);
        return this.depthBuffer.get(0);
    }

    /* access modifiers changed from: protected */
    public boolean getDepthWriteMask() {
        this.intBuffer.rewind();
        getBooleanv(DEPTH_WRITEMASK, this.intBuffer);
        return this.intBuffer.get(0) != 0;
    }

    /* access modifiers changed from: protected */
    public abstract Object getDerivedFont(Object obj, float f);

    /* access modifiers changed from: protected */
    public int getDrawFramebuffer() {
        if (this.fboLayerEnabled) {
            return 1 < this.numSamples ? this.glMultiFbo.get(0) : this.glColorFbo.get(0);
        }
        return 0;
    }

    public abstract int getError();

    public abstract void getFloatv(int i, FloatBuffer floatBuffer);

    /* access modifiers changed from: protected */
    public abstract int getFontAscent(Object obj);

    /* access modifiers changed from: protected */
    public abstract int getFontDescent(Object obj);

    public abstract void getFramebufferAttachmentParameteriv(int i, int i2, int i3, IntBuffer intBuffer2);

    /* access modifiers changed from: protected */
    public abstract void getGL(PGL pgl);

    /* access modifiers changed from: protected */
    public abstract int getGLSLVersion();

    /* access modifiers changed from: protected */
    public int[] getGLVersion() {
        String lowerCase = getString(VERSION).trim().toLowerCase();
        int indexOf = lowerCase.indexOf("opengl es");
        if (indexOf >= 0) {
            lowerCase = lowerCase.substring("opengl es".length() + indexOf).trim();
        }
        int[] iArr = {0, 0, 0};
        String[] split = lowerCase.split(" ");
        int i = 0;
        while (true) {
            if (i >= split.length) {
                break;
            } else if (split[i].indexOf(".") > 0) {
                String[] split2 = split[i].split("\\.");
                try {
                    iArr[0] = Integer.parseInt(split2[0]);
                } catch (NumberFormatException e) {
                }
                if (1 < split2.length) {
                    try {
                        iArr[1] = Integer.parseInt(split2[1]);
                    } catch (NumberFormatException e2) {
                    }
                }
                if (2 < split2.length) {
                    try {
                        iArr[2] = Integer.parseInt(split2[2]);
                    } catch (NumberFormatException e3) {
                    }
                }
            } else {
                i++;
            }
        }
        return iArr;
    }

    public abstract void getIntegerv(int i, IntBuffer intBuffer2);

    /* access modifiers changed from: protected */
    public int getMaxTexUnits() {
        this.intBuffer.rewind();
        getIntegerv(MAX_TEXTURE_IMAGE_UNITS, this.intBuffer);
        return this.intBuffer.get(0);
    }

    public abstract Object getNative();

    /* access modifiers changed from: protected */
    public abstract float getPixelScale();

    public abstract String getProgramInfoLog(int i);

    public abstract void getProgramiv(int i, int i2, IntBuffer intBuffer2);

    /* access modifiers changed from: protected */
    public int getReadFramebuffer() {
        if (this.fboLayerEnabled) {
            return this.glColorFbo.get(0);
        }
        return 0;
    }

    public abstract void getRenderbufferParameteriv(int i, int i2, IntBuffer intBuffer2);

    public abstract String getShaderInfoLog(int i);

    public abstract void getShaderPrecisionFormat(int i, int i2, IntBuffer intBuffer2, IntBuffer intBuffer3);

    public abstract String getShaderSource(int i);

    public abstract void getShaderiv(int i, int i2, IntBuffer intBuffer2);

    /* access modifiers changed from: protected */
    public abstract int getStencilBits();

    /* access modifiers changed from: protected */
    public byte getStencilValue(int i, int i2) {
        if (this.stencilBuffer == null) {
            this.stencilBuffer = ByteBuffer.allocate(1);
        }
        this.stencilBuffer.rewind();
        readPixels(i, (this.graphics.height - i2) - 1, 1, 1, STENCIL_INDEX, UNSIGNED_BYTE, (Buffer) this.stencilBuffer);
        return this.stencilBuffer.get(0);
    }

    public abstract String getString(int i);

    public abstract void getTexParameterfv(int i, int i2, FloatBuffer floatBuffer);

    public abstract void getTexParameteriv(int i, int i2, IntBuffer intBuffer2);

    /* access modifiers changed from: protected */
    public abstract int getTextWidth(Object obj, char[] cArr, int i, int i2);

    public abstract int getUniformLocation(int i, String str);

    public abstract void getUniformfv(int i, int i2, FloatBuffer floatBuffer);

    public abstract void getUniformiv(int i, int i2, IntBuffer intBuffer2);

    public abstract void getVertexAttribPointerv(int i, int i2, ByteBuffer byteBuffer2);

    public abstract void getVertexAttribfv(int i, int i2, FloatBuffer floatBuffer);

    public abstract void getVertexAttribiv(int i, int i2, IntBuffer intBuffer2);

    /* access modifiers changed from: protected */
    public boolean hasAnisoSamplingSupport() {
        return getGLVersion()[0] >= 3 || -1 < getString(EXTENSIONS).indexOf("_texture_filter_anisotropic");
    }

    /* access modifiers changed from: protected */
    public boolean hasAutoMipmapGenSupport() {
        return getGLVersion()[0] >= 3 || -1 < getString(EXTENSIONS).indexOf("_generate_mipmap");
    }

    /* access modifiers changed from: protected */
    public boolean hasDrawBuffer() {
        int[] gLVersion = getGLVersion();
        return isES() ? gLVersion[0] >= 3 : gLVersion[0] >= 2;
    }

    /* access modifiers changed from: protected */
    public boolean hasFBOs() {
        if (getGLVersion()[0] >= 2) {
            return true;
        }
        String string = getString(EXTENSIONS);
        return (string.indexOf("_framebuffer_object") == -1 || string.indexOf("_vertex_shader") == -1 || string.indexOf("_shader_objects") == -1 || string.indexOf("_shading_language") == -1) ? false : true;
    }

    /* access modifiers changed from: protected */
    public boolean hasFboMultisampleSupport() {
        return getGLVersion()[0] >= 3 || -1 < getString(EXTENSIONS).indexOf("_framebuffer_multisample");
    }

    /* access modifiers changed from: protected */
    public boolean hasNpotTexSupport() {
        if (getGLVersion()[0] >= 3) {
            return true;
        }
        String string = getString(EXTENSIONS);
        return isES() ? -1 < string.indexOf("_texture_npot") : -1 < string.indexOf("_texture_non_power_of_two");
    }

    /* access modifiers changed from: protected */
    public boolean hasPBOs() {
        boolean z = false;
        int[] gLVersion = getGLVersion();
        if (isES()) {
            return gLVersion[0] >= 3;
        }
        if (gLVersion[0] > 2 || (gLVersion[0] == 2 && gLVersion[1] >= 1)) {
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public boolean hasPackedDepthStencilSupport() {
        return getGLVersion()[0] >= 3 || -1 < getString(EXTENSIONS).indexOf("_packed_depth_stencil");
    }

    /* access modifiers changed from: protected */
    public boolean hasReadBuffer() {
        int[] gLVersion = getGLVersion();
        return isES() ? gLVersion[0] >= 3 : gLVersion[0] >= 2;
    }

    /* access modifiers changed from: protected */
    public boolean hasShaders() {
        if (getGLVersion()[0] >= 2) {
            return true;
        }
        String string = getString(EXTENSIONS);
        return (string.indexOf("_fragment_shader") == -1 || string.indexOf("_vertex_shader") == -1 || string.indexOf("_shader_objects") == -1 || string.indexOf("_shading_language") == -1) ? false : true;
    }

    /* access modifiers changed from: protected */
    public boolean hasSynchronization() {
        boolean z = false;
        int[] gLVersion = getGLVersion();
        if (isES()) {
            return gLVersion[0] >= 3;
        }
        if (gLVersion[0] > 3 || (gLVersion[0] == 3 && gLVersion[1] >= 2)) {
            z = true;
        }
        return z;
    }

    public abstract void hint(int i, int i2);

    /* access modifiers changed from: protected */
    public abstract void initFBOLayer();

    public void initPresentMode(float f, float f2, int i) {
        boolean z = true;
        this.presentMode = true;
        if (i == 0) {
            z = false;
        }
        this.showStopButton = z;
        this.stopButtonColor = i;
        this.presentX = f;
        this.presentY = f2;
        enableFBOLayer();
    }

    /* access modifiers changed from: protected */
    public abstract void initSurface(int i);

    /* access modifiers changed from: protected */
    public PGL initTex2DShader() {
        PGL primaryPGL2 = this.primaryPGL ? this : this.graphics.getPrimaryPGL();
        if (!primaryPGL2.loadedTex2DShader || primaryPGL2.tex2DShaderContext != primaryPGL2.glContext) {
            String join = PApplet.join(preprocessVertexSource(texVertShaderSource, getGLSLVersion()), "\n");
            String join2 = PApplet.join(preprocessFragmentSource(tex2DFragShaderSource, getGLSLVersion()), "\n");
            primaryPGL2.tex2DVertShader = createShader(VERTEX_SHADER, join);
            primaryPGL2.tex2DFragShader = createShader(FRAGMENT_SHADER, join2);
            if (primaryPGL2.tex2DVertShader > 0 && primaryPGL2.tex2DFragShader > 0) {
                primaryPGL2.tex2DShaderProgram = createProgram(primaryPGL2.tex2DVertShader, primaryPGL2.tex2DFragShader);
            }
            if (primaryPGL2.tex2DShaderProgram > 0) {
                primaryPGL2.tex2DVertLoc = getAttribLocation(primaryPGL2.tex2DShaderProgram, "position");
                primaryPGL2.tex2DTCoordLoc = getAttribLocation(primaryPGL2.tex2DShaderProgram, "texCoord");
                primaryPGL2.tex2DSamplerLoc = getUniformLocation(primaryPGL2.tex2DShaderProgram, "texMap");
            }
            primaryPGL2.loadedTex2DShader = true;
            primaryPGL2.tex2DShaderContext = primaryPGL2.glContext;
            genBuffers(1, this.intBuffer);
            primaryPGL2.tex2DGeoVBO = this.intBuffer.get(0);
            bindBuffer(ARRAY_BUFFER, primaryPGL2.tex2DGeoVBO);
            bufferData(ARRAY_BUFFER, SIZEOF_FLOAT * 16, (Buffer) null, STATIC_DRAW);
        }
        if (this.texData == null) {
            this.texData = allocateDirectFloatBuffer(this.texCoords.length);
        }
        return primaryPGL2;
    }

    /* access modifiers changed from: protected */
    public PGL initTexRectShader() {
        PGL primaryPGL2 = this.primaryPGL ? this : this.graphics.getPrimaryPGL();
        if (!primaryPGL2.loadedTexRectShader || primaryPGL2.texRectShaderContext != primaryPGL2.glContext) {
            String join = PApplet.join(preprocessVertexSource(texVertShaderSource, getGLSLVersion()), "\n");
            String join2 = PApplet.join(preprocessFragmentSource(texRectFragShaderSource, getGLSLVersion()), "\n");
            primaryPGL2.texRectVertShader = createShader(VERTEX_SHADER, join);
            primaryPGL2.texRectFragShader = createShader(FRAGMENT_SHADER, join2);
            if (primaryPGL2.texRectVertShader > 0 && primaryPGL2.texRectFragShader > 0) {
                primaryPGL2.texRectShaderProgram = createProgram(primaryPGL2.texRectVertShader, primaryPGL2.texRectFragShader);
            }
            if (primaryPGL2.texRectShaderProgram > 0) {
                primaryPGL2.texRectVertLoc = getAttribLocation(primaryPGL2.texRectShaderProgram, "position");
                primaryPGL2.texRectTCoordLoc = getAttribLocation(primaryPGL2.texRectShaderProgram, "texCoord");
                primaryPGL2.texRectSamplerLoc = getUniformLocation(primaryPGL2.texRectShaderProgram, "texMap");
            }
            primaryPGL2.loadedTexRectShader = true;
            primaryPGL2.texRectShaderContext = primaryPGL2.glContext;
            genBuffers(1, this.intBuffer);
            primaryPGL2.texRectGeoVBO = this.intBuffer.get(0);
            bindBuffer(ARRAY_BUFFER, primaryPGL2.texRectGeoVBO);
            bufferData(ARRAY_BUFFER, SIZEOF_FLOAT * 16, (Buffer) null, STATIC_DRAW);
        }
        return primaryPGL2;
    }

    /* access modifiers changed from: protected */
    public void initTexture(int i, int i2, int i3, int i4) {
        initTexture(i, i2, i3, i4, 0);
    }

    /* access modifiers changed from: protected */
    public void initTexture(int i, int i2, int i3, int i4, int i5) {
        int[] iArr = new int[256];
        Arrays.fill(iArr, javaToNativeARGB(i5));
        IntBuffer allocateDirectIntBuffer = allocateDirectIntBuffer(256);
        allocateDirectIntBuffer.put(iArr);
        allocateDirectIntBuffer.rewind();
        for (int i6 = 0; i6 < i4; i6 += 16) {
            int min = PApplet.min(16, i4 - i6);
            for (int i7 = 0; i7 < i3; i7 += 16) {
                texSubImage2D(i, 0, i7, i6, PApplet.min(16, i3 - i7), min, i2, UNSIGNED_BYTE, allocateDirectIntBuffer);
            }
        }
    }

    public boolean insideStopButton(float f, float f2) {
        return this.showStopButton && ((float) this.stopButtonX) < f && f < ((float) (this.stopButtonX + this.stopButtonWidth)) && ((float) (-(this.closeButtonY + this.stopButtonHeight))) < f2 && f2 < ((float) (-this.closeButtonY));
    }

    public abstract void isBuffer(int i);

    /* access modifiers changed from: protected */
    public boolean isES() {
        return getString(VERSION).trim().toLowerCase().contains("opengl es");
    }

    public abstract boolean isEnabled(int i);

    /* access modifiers changed from: protected */
    public boolean isFBOBacked() {
        return this.fboLayerEnabled;
    }

    public abstract boolean isFramebuffer(int i);

    /* access modifiers changed from: protected */
    public boolean isMultisampled() {
        return 1 < this.numSamples;
    }

    public abstract boolean isProgram(int i);

    public abstract boolean isRenderbuffer(int i);

    public abstract boolean isShader(int i);

    public abstract boolean isTexture(int i);

    public abstract void lineWidth(float f);

    public abstract void linkProgram(int i);

    /* access modifiers changed from: protected */
    public boolean linked(int i) {
        this.intBuffer.rewind();
        getProgramiv(i, LINK_STATUS, this.intBuffer);
        return this.intBuffer.get(0) != 0;
    }

    /* access modifiers changed from: protected */
    public String[] loadFragmentShader(String str) {
        return this.sketch.loadStrings(str);
    }

    /* access modifiers changed from: protected */
    public String[] loadFragmentShader(String str, int i) {
        return loadFragmentShader(str);
    }

    /* access modifiers changed from: protected */
    public String[] loadFragmentShader(URL url) {
        try {
            return PApplet.loadStrings(url.openStream());
        } catch (IOException e) {
            PGraphics.showException("Cannot load fragment shader " + url.getFile());
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public String[] loadFragmentShader(URL url, int i) {
        return loadFragmentShader(url);
    }

    /* access modifiers changed from: protected */
    public String[] loadVertexShader(String str) {
        return this.sketch.loadStrings(str);
    }

    /* access modifiers changed from: protected */
    public String[] loadVertexShader(String str, int i) {
        return loadVertexShader(str);
    }

    /* access modifiers changed from: protected */
    public String[] loadVertexShader(URL url) {
        try {
            return PApplet.loadStrings(url.openStream());
        } catch (IOException e) {
            PGraphics.showException("Cannot load vertex shader " + url.getFile());
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public String[] loadVertexShader(URL url, int i) {
        return loadVertexShader(url);
    }

    public abstract ByteBuffer mapBuffer(int i, int i2);

    public abstract ByteBuffer mapBufferRange(int i, int i2, int i3, int i4);

    /* access modifiers changed from: protected */
    public int maxSamples() {
        this.intBuffer.rewind();
        getIntegerv(MAX_SAMPLES, this.intBuffer);
        return this.intBuffer.get(0);
    }

    public abstract void pixelStorei(int i, int i2);

    public abstract void polygonOffset(float f, float f2);

    public boolean presentMode() {
        return this.presentMode;
    }

    public float presentX() {
        return this.presentX;
    }

    public float presentY() {
        return this.presentY;
    }

    public abstract void readBuffer(int i);

    public void readPixels(int i, int i2, int i3, int i4, int i5, int i6, long j) {
        boolean z = false;
        boolean z2 = isMultisampled() || this.graphics.offscreenMultisample;
        boolean hint = this.graphics.getHint(10);
        if (i5 == STENCIL_INDEX || i5 == DEPTH_COMPONENT || i5 == DEPTH_STENCIL) {
            z = true;
        }
        if (!z2 || !z || hint) {
            this.graphics.beginReadPixels();
            readPixelsImpl(i, i2, i3, i4, i5, i6, j);
            this.graphics.endReadPixels();
            return;
        }
        PGraphics.showWarning(DEPTH_READING_NOT_ENABLED_ERROR);
    }

    public void readPixels(int i, int i2, int i3, int i4, int i5, int i6, Buffer buffer) {
        boolean z = false;
        boolean z2 = isMultisampled() || this.graphics.offscreenMultisample;
        boolean hint = this.graphics.getHint(10);
        if (i5 == STENCIL_INDEX || i5 == DEPTH_COMPONENT || i5 == DEPTH_STENCIL) {
            z = true;
        }
        if (!z2 || !z || hint) {
            this.graphics.beginReadPixels();
            readPixelsImpl(i, i2, i3, i4, i5, i6, buffer);
            this.graphics.endReadPixels();
            return;
        }
        PGraphics.showWarning(DEPTH_READING_NOT_ENABLED_ERROR);
    }

    /* access modifiers changed from: protected */
    public abstract void readPixelsImpl(int i, int i2, int i3, int i4, int i5, int i6, long j);

    /* access modifiers changed from: protected */
    public abstract void readPixelsImpl(int i, int i2, int i3, int i4, int i5, int i6, Buffer buffer);

    /* access modifiers changed from: protected */
    public abstract void registerListeners();

    /* access modifiers changed from: protected */
    public abstract void reinitSurface();

    public abstract void releaseShaderCompiler();

    public abstract void renderbufferStorage(int i, int i2, int i3, int i4);

    public abstract void renderbufferStorageMultisample(int i, int i2, int i3, int i4, int i5);

    /* access modifiers changed from: protected */
    public abstract void requestDraw();

    @Deprecated
    public void requestFBOLayer() {
        enableFBOLayer();
    }

    /* access modifiers changed from: protected */
    public abstract void requestFocus();

    public void resetFBOLayer() {
        this.fbolayerResetReq = true;
    }

    /* access modifiers changed from: protected */
    public void restoreFirstFrame() {
        int nextPowerOfTwo;
        int nextPowerOfTwo2;
        if (this.firstFrame != null) {
            IntBuffer allocateIntBuffer = allocateIntBuffer(1);
            genTextures(1, allocateIntBuffer);
            float pixelScale = getPixelScale();
            if (hasNpotTexSupport()) {
                nextPowerOfTwo = (int) (((float) this.graphics.width) * pixelScale);
                nextPowerOfTwo2 = (int) (((float) this.graphics.height) * pixelScale);
            } else {
                nextPowerOfTwo = nextPowerOfTwo((int) (((float) this.graphics.width) * pixelScale));
                nextPowerOfTwo2 = nextPowerOfTwo((int) (((float) this.graphics.height) * pixelScale));
            }
            bindTexture(TEXTURE_2D, allocateIntBuffer.get(0));
            texParameteri(TEXTURE_2D, TEXTURE_MIN_FILTER, NEAREST);
            texParameteri(TEXTURE_2D, TEXTURE_MAG_FILTER, NEAREST);
            texParameteri(TEXTURE_2D, TEXTURE_WRAP_S, CLAMP_TO_EDGE);
            texParameteri(TEXTURE_2D, TEXTURE_WRAP_T, CLAMP_TO_EDGE);
            texImage2D(TEXTURE_2D, 0, RGBA, nextPowerOfTwo, nextPowerOfTwo2, 0, RGBA, UNSIGNED_BYTE, (Buffer) null);
            texSubImage2D(TEXTURE_2D, 0, 0, 0, this.graphics.width, this.graphics.height, RGBA, UNSIGNED_BYTE, this.firstFrame);
            drawTexture(TEXTURE_2D, allocateIntBuffer.get(0), nextPowerOfTwo, nextPowerOfTwo2, 0, 0, this.graphics.width, this.graphics.height, 0, 0, (int) (((float) this.graphics.width) * pixelScale), (int) (((float) this.graphics.height) * pixelScale), 0, 0, this.graphics.width, this.graphics.height);
            deleteTextures(1, allocateIntBuffer);
            this.firstFrame.clear();
            this.firstFrame = null;
        }
    }

    public abstract void sampleCoverage(float f, boolean z);

    /* access modifiers changed from: protected */
    public void saveFirstFrame() {
        this.firstFrame = allocateDirectIntBuffer(this.graphics.width * this.graphics.height);
        if (hasReadBuffer()) {
            readBuffer(BACK);
        }
        readPixelsImpl(0, 0, this.graphics.width, this.graphics.height, RGBA, UNSIGNED_BYTE, (Buffer) this.firstFrame);
    }

    public abstract void scissor(int i, int i2, int i3, int i4);

    /* access modifiers changed from: protected */
    public abstract void setFrameRate(float f);

    public void setPrimary(boolean z) {
        this.primaryPGL = z;
    }

    public void setThread(Thread thread) {
        this.glThread = thread;
    }

    public abstract void shaderBinary(int i, IntBuffer intBuffer2, int i2, Buffer buffer, int i3);

    public abstract void shaderSource(int i, String str);

    public abstract void stencilFunc(int i, int i2, int i3);

    public abstract void stencilFuncSeparate(int i, int i2, int i3, int i4);

    public abstract void stencilMask(int i);

    public abstract void stencilMaskSeparate(int i, int i2);

    public abstract void stencilOp(int i, int i2, int i3);

    public abstract void stencilOpSeparate(int i, int i2, int i3, int i4);

    /* access modifiers changed from: protected */
    public abstract void swapBuffers();

    /* access modifiers changed from: protected */
    public void syncBackTexture() {
        if (this.usingFrontTex) {
            this.needSepFrontTex = true;
        }
        if (1 < this.numSamples) {
            bindFramebufferImpl(READ_FRAMEBUFFER, this.glMultiFbo.get(0));
            bindFramebufferImpl(DRAW_FRAMEBUFFER, this.glColorFbo.get(0));
            int i = COLOR_BUFFER_BIT;
            if (this.graphics.getHint(10)) {
                i |= DEPTH_BUFFER_BIT | STENCIL_BUFFER_BIT;
            }
            blitFramebuffer(0, 0, this.fboWidth, this.fboHeight, 0, 0, this.fboWidth, this.fboHeight, i, NEAREST);
        }
    }

    /* access modifiers changed from: protected */
    public String tessError(int i) {
        return "";
    }

    public abstract void texImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer);

    public abstract void texParameterf(int i, int i2, float f);

    public abstract void texParameterfv(int i, int i2, FloatBuffer floatBuffer);

    public abstract void texParameteri(int i, int i2, int i3);

    public abstract void texParameteriv(int i, int i2, IntBuffer intBuffer2);

    public abstract void texSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer);

    /* access modifiers changed from: protected */
    public boolean textureIsBound(int i, int i2) {
        boolean z = true;
        if (this.boundTextures == null) {
            return false;
        }
        if (i == TEXTURE_2D) {
            if (this.boundTextures[this.activeTexUnit][0] != i2) {
                z = false;
            }
            return z;
        } else if (i != TEXTURE_RECTANGLE) {
            return false;
        } else {
            if (this.boundTextures[this.activeTexUnit][1] != i2) {
                z = false;
            }
            return z;
        }
    }

    /* access modifiers changed from: protected */
    public boolean texturingIsEnabled(int i) {
        if (i == TEXTURE_2D) {
            return this.texturingTargets[0];
        }
        if (i == TEXTURE_RECTANGLE) {
            return this.texturingTargets[1];
        }
        return false;
    }

    public boolean threadIsCurrent() {
        return Thread.currentThread() == this.glThread;
    }

    /* access modifiers changed from: protected */
    public void unbindFrontTexture() {
        if (!textureIsBound(TEXTURE_2D, this.glColorTex.get(this.frontTex))) {
            return;
        }
        if (!texturingIsEnabled(TEXTURE_2D)) {
            enableTexturing(TEXTURE_2D);
            bindTexture(TEXTURE_2D, 0);
            disableTexturing(TEXTURE_2D);
            return;
        }
        bindTexture(TEXTURE_2D, 0);
    }

    public abstract void uniform1f(int i, float f);

    public abstract void uniform1fv(int i, int i2, FloatBuffer floatBuffer);

    public abstract void uniform1i(int i, int i2);

    public abstract void uniform1iv(int i, int i2, IntBuffer intBuffer2);

    public abstract void uniform2f(int i, float f, float f2);

    public abstract void uniform2fv(int i, int i2, FloatBuffer floatBuffer);

    public abstract void uniform2i(int i, int i2, int i3);

    public abstract void uniform2iv(int i, int i2, IntBuffer intBuffer2);

    public abstract void uniform3f(int i, float f, float f2, float f3);

    public abstract void uniform3fv(int i, int i2, FloatBuffer floatBuffer);

    public abstract void uniform3i(int i, int i2, int i3, int i4);

    public abstract void uniform3iv(int i, int i2, IntBuffer intBuffer2);

    public abstract void uniform4f(int i, float f, float f2, float f3, float f4);

    public abstract void uniform4fv(int i, int i2, FloatBuffer floatBuffer);

    public abstract void uniform4i(int i, int i2, int i3, int i4, int i5);

    public abstract void uniform4iv(int i, int i2, IntBuffer intBuffer2);

    public abstract void uniformMatrix2fv(int i, int i2, boolean z, FloatBuffer floatBuffer);

    public abstract void uniformMatrix3fv(int i, int i2, boolean z, FloatBuffer floatBuffer);

    public abstract void uniformMatrix4fv(int i, int i2, boolean z, FloatBuffer floatBuffer);

    public abstract void unmapBuffer(int i);

    public abstract void useProgram(int i);

    /* access modifiers changed from: protected */
    public boolean validateFramebuffer() {
        int checkFramebufferStatus = checkFramebufferStatus(FRAMEBUFFER);
        if (checkFramebufferStatus == FRAMEBUFFER_COMPLETE) {
            return true;
        }
        if (checkFramebufferStatus == FRAMEBUFFER_INCOMPLETE_ATTACHMENT) {
            System.err.println(String.format(FRAMEBUFFER_ERROR, new Object[]{"incomplete attachment"}));
        } else if (checkFramebufferStatus == FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT) {
            System.err.println(String.format(FRAMEBUFFER_ERROR, new Object[]{"incomplete missing attachment"}));
        } else if (checkFramebufferStatus == FRAMEBUFFER_INCOMPLETE_DIMENSIONS) {
            System.err.println(String.format(FRAMEBUFFER_ERROR, new Object[]{"incomplete dimensions"}));
        } else if (checkFramebufferStatus == FRAMEBUFFER_INCOMPLETE_FORMATS) {
            System.err.println(String.format(FRAMEBUFFER_ERROR, new Object[]{"incomplete formats"}));
        } else if (checkFramebufferStatus == FRAMEBUFFER_UNSUPPORTED) {
            System.err.println(String.format(FRAMEBUFFER_ERROR, new Object[]{"framebuffer unsupported"}));
        } else {
            System.err.println(String.format(FRAMEBUFFER_ERROR, new Object[]{"unknown error"}));
        }
        return false;
    }

    public abstract void validateProgram(int i);

    public abstract void vertexAttrib1f(int i, float f);

    public abstract void vertexAttrib1fv(int i, FloatBuffer floatBuffer);

    public abstract void vertexAttrib2f(int i, float f, float f2);

    public abstract void vertexAttrib2fv(int i, FloatBuffer floatBuffer);

    public abstract void vertexAttrib3f(int i, float f, float f2, float f3);

    public abstract void vertexAttrib3fv(int i, FloatBuffer floatBuffer);

    public abstract void vertexAttrib4f(int i, float f, float f2, float f3, float f4);

    public abstract void vertexAttrib4fv(int i, FloatBuffer floatBuffer);

    public abstract void vertexAttribPointer(int i, int i2, int i3, boolean z, int i4, int i5);

    public abstract void viewport(int i, int i2, int i3, int i4);

    /* access modifiers changed from: protected */
    public abstract void viewportImpl(int i, int i2, int i3, int i4);

    /* access modifiers changed from: protected */
    public Texture wrapBackTexture(Texture texture) {
        if (texture == null) {
            Texture texture2 = new Texture(this.graphics);
            texture2.init(this.graphics.width, this.graphics.height, this.glColorTex.get(this.backTex), TEXTURE_2D, RGBA, this.fboWidth, this.fboHeight, NEAREST, NEAREST, CLAMP_TO_EDGE, CLAMP_TO_EDGE);
            texture2.invertedY(true);
            texture2.colorBuffer(true);
            this.graphics.setCache(this.graphics, texture2);
            return texture2;
        }
        texture.glName = this.glColorTex.get(this.backTex);
        return texture;
    }

    /* access modifiers changed from: protected */
    public Texture wrapFrontTexture(Texture texture) {
        if (texture == null) {
            Texture texture2 = new Texture(this.graphics);
            texture2.init(this.graphics.width, this.graphics.height, this.glColorTex.get(this.frontTex), TEXTURE_2D, RGBA, this.fboWidth, this.fboHeight, NEAREST, NEAREST, CLAMP_TO_EDGE, CLAMP_TO_EDGE);
            texture2.invertedY(true);
            texture2.colorBuffer(true);
            return texture2;
        }
        texture.glName = this.glColorTex.get(this.frontTex);
        return texture;
    }
}
