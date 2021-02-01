package processing.opengl.tess;

public class PGLU {
    public static final int GLU_BEGIN = 100100;
    public static final int GLU_CCW = 100121;
    public static final int GLU_CW = 100120;
    public static final int GLU_EDGE_FLAG = 100104;
    public static final int GLU_END = 100102;
    public static final int GLU_ERROR = 100103;
    public static final int GLU_EXTERIOR = 100123;
    public static final int GLU_FALSE = 0;
    public static final int GLU_FILL = 100012;
    public static final int GLU_FLAT = 100001;
    public static final int GLU_INSIDE = 100021;
    public static final int GLU_INTERIOR = 100122;
    public static final int GLU_INVALID_ENUM = 100900;
    public static final int GLU_INVALID_OPERATION = 100904;
    public static final int GLU_INVALID_VALUE = 100901;
    public static final int GLU_LINE = 100011;
    public static final int GLU_NONE = 100002;
    public static final int GLU_OUTSIDE = 100020;
    public static final int GLU_OUT_OF_MEMORY = 100902;
    public static final int GLU_POINT = 100010;
    public static final int GLU_SILHOUETTE = 100013;
    public static final int GLU_SMOOTH = 100000;
    public static final int GLU_TESS_AVOID_DEGENERATE_TRIANGLES = 100149;
    public static final int GLU_TESS_BEGIN = 100100;
    public static final int GLU_TESS_BEGIN_DATA = 100106;
    public static final int GLU_TESS_BOUNDARY_ONLY = 100141;
    public static final int GLU_TESS_COMBINE = 100105;
    public static final int GLU_TESS_COMBINE_DATA = 100111;
    public static final int GLU_TESS_COORD_TOO_LARGE = 100155;
    public static final int GLU_TESS_EDGE_FLAG = 100104;
    public static final int GLU_TESS_EDGE_FLAG_DATA = 100110;
    public static final int GLU_TESS_END = 100102;
    public static final int GLU_TESS_END_DATA = 100108;
    public static final int GLU_TESS_ERROR = 100103;
    public static final int GLU_TESS_ERROR1 = 100151;
    public static final int GLU_TESS_ERROR2 = 100152;
    public static final int GLU_TESS_ERROR3 = 100153;
    public static final int GLU_TESS_ERROR4 = 100154;
    public static final int GLU_TESS_ERROR5 = 100155;
    public static final int GLU_TESS_ERROR6 = 100156;
    public static final int GLU_TESS_ERROR7 = 100157;
    public static final int GLU_TESS_ERROR8 = 100158;
    public static final int GLU_TESS_ERROR_DATA = 100109;
    public static final double GLU_TESS_MAX_COORD = 1.0E150d;
    public static final int GLU_TESS_MISSING_BEGIN_CONTOUR = 100152;
    public static final int GLU_TESS_MISSING_BEGIN_POLYGON = 100151;
    public static final int GLU_TESS_MISSING_END_CONTOUR = 100154;
    public static final int GLU_TESS_MISSING_END_POLYGON = 100153;
    public static final int GLU_TESS_NEED_COMBINE_CALLBACK = 100156;
    public static final int GLU_TESS_TOLERANCE = 100142;
    public static final int GLU_TESS_VERTEX = 100101;
    public static final int GLU_TESS_VERTEX_DATA = 100107;
    public static final int GLU_TESS_WINDING_ABS_GEQ_TWO = 100134;
    public static final int GLU_TESS_WINDING_NEGATIVE = 100133;
    public static final int GLU_TESS_WINDING_NONZERO = 100131;
    public static final int GLU_TESS_WINDING_ODD = 100130;
    public static final int GLU_TESS_WINDING_POSITIVE = 100132;
    public static final int GLU_TESS_WINDING_RULE = 100140;
    public static final int GLU_TRUE = 1;
    public static final int GLU_UNKNOWN = 100124;
    public static final int GLU_VERTEX = 100101;
    private static String[] glErrorStrings = {"invalid enumerant", "invalid value", "invalid operation", "stack overflow", "stack underflow", "out of memory", "invalid framebuffer operation"};
    private static String[] gluErrorStrings = {"invalid enumerant", "invalid value", "out of memory", "", "invalid operation"};
    private static String[] gluTessErrors = {" ", "gluTessBeginPolygon() must precede a gluTessEndPolygon", "gluTessBeginContour() must precede a gluTessEndContour()", "gluTessEndPolygon() must follow a gluTessBeginPolygon()", "gluTessEndContour() must follow a gluTessBeginContour()", "a coordinate is too large", "need combine callback"};

    public static String gluErrorString(int i) {
        return i == 0 ? "no error" : (i < 1280 || i > 1286) ? i == 32817 ? "table too large" : (i < 100900 || i > 100904) ? (i < 100151 || i > 100158) ? "error (" + i + ")" : gluTessErrors[i - 100150] : gluErrorStrings[i - GLU_INVALID_ENUM] : glErrorStrings[i - 1280];
    }

    public static final PGLUtessellator gluNewTess() {
        return GLUtessellatorImpl.gluNewTess();
    }

    public static final void gluTessBeginContour(PGLUtessellator pGLUtessellator) {
        ((GLUtessellatorImpl) pGLUtessellator).gluTessBeginContour();
    }

    public static final void gluTessBeginPolygon(PGLUtessellator pGLUtessellator, Object obj) {
        ((GLUtessellatorImpl) pGLUtessellator).gluTessBeginPolygon(obj);
    }

    public static final void gluTessCallback(PGLUtessellator pGLUtessellator, int i, PGLUtessellatorCallback pGLUtessellatorCallback) {
        ((GLUtessellatorImpl) pGLUtessellator).gluTessCallback(i, pGLUtessellatorCallback);
    }

    public static final void gluTessEndContour(PGLUtessellator pGLUtessellator) {
        ((GLUtessellatorImpl) pGLUtessellator).gluTessEndContour();
    }

    public static final void gluTessEndPolygon(PGLUtessellator pGLUtessellator) {
        ((GLUtessellatorImpl) pGLUtessellator).gluTessEndPolygon();
    }

    public static final void gluTessProperty(PGLUtessellator pGLUtessellator, int i, double d) {
        ((GLUtessellatorImpl) pGLUtessellator).gluTessProperty(i, d);
    }

    public static final void gluTessVertex(PGLUtessellator pGLUtessellator, double[] dArr, int i, Object obj) {
        ((GLUtessellatorImpl) pGLUtessellator).gluTessVertex(dArr, i, obj);
    }
}
