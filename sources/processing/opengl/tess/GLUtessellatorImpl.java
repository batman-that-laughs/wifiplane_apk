package processing.opengl.tess;

public class GLUtessellatorImpl implements PGLUtessellator {
    static final /* synthetic */ boolean $assertionsDisabled = (!GLUtessellatorImpl.class.desiredAssertionStatus());
    private static final double GLU_TESS_DEFAULT_TOLERANCE = 0.0d;
    private static PGLUtessellatorCallback NULL_CB = new PGLUtessellatorCallbackAdapter();
    public static final int TESS_MAX_CACHE = 100;
    boolean avoidDegenerateTris;
    boolean boundaryOnly;
    CachedVertex[] cache = new CachedVertex[100];
    int cacheCount;
    private PGLUtessellatorCallback callBegin;
    private PGLUtessellatorCallback callBeginData;
    private PGLUtessellatorCallback callCombine;
    private PGLUtessellatorCallback callCombineData;
    private PGLUtessellatorCallback callEdgeFlag;
    private PGLUtessellatorCallback callEdgeFlagData;
    private PGLUtessellatorCallback callEnd;
    private PGLUtessellatorCallback callEndData;
    private PGLUtessellatorCallback callError;
    private PGLUtessellatorCallback callErrorData;
    private PGLUtessellatorCallback callVertex;
    private PGLUtessellatorCallback callVertexData;
    Dict dict;
    GLUvertex event;
    boolean fatalError;
    boolean flagBoundary;
    private boolean flushCacheOnNextVertex;
    private GLUhalfEdge lastEdge;
    GLUface lonelyTriList;
    GLUmesh mesh;
    double[] normal = new double[3];
    private Object polygonData;
    PriorityQ pq;
    private double relTolerance;
    double[] sUnit = new double[3];
    private int state = 0;
    double[] tUnit = new double[3];
    int windingRule;

    private GLUtessellatorImpl() {
        this.normal[0] = 0.0d;
        this.normal[1] = 0.0d;
        this.normal[2] = 0.0d;
        this.relTolerance = GLU_TESS_DEFAULT_TOLERANCE;
        this.windingRule = PGLU.GLU_TESS_WINDING_ODD;
        this.flagBoundary = false;
        this.boundaryOnly = false;
        this.callBegin = NULL_CB;
        this.callEdgeFlag = NULL_CB;
        this.callVertex = NULL_CB;
        this.callEnd = NULL_CB;
        this.callError = NULL_CB;
        this.callCombine = NULL_CB;
        this.callBeginData = NULL_CB;
        this.callEdgeFlagData = NULL_CB;
        this.callVertexData = NULL_CB;
        this.callEndData = NULL_CB;
        this.callErrorData = NULL_CB;
        this.callCombineData = NULL_CB;
        this.polygonData = null;
        for (int i = 0; i < this.cache.length; i++) {
            this.cache[i] = new CachedVertex();
        }
    }

    private boolean addVertex(double[] dArr, Object obj) {
        GLUhalfEdge gLUhalfEdge;
        GLUhalfEdge gLUhalfEdge2 = this.lastEdge;
        if (gLUhalfEdge2 == null) {
            gLUhalfEdge = Mesh.__gl_meshMakeEdge(this.mesh);
            if (gLUhalfEdge == null) {
                return false;
            }
            if (!Mesh.__gl_meshSplice(gLUhalfEdge, gLUhalfEdge.Sym)) {
                return false;
            }
        } else if (Mesh.__gl_meshSplitEdge(gLUhalfEdge2) == null) {
            return false;
        } else {
            gLUhalfEdge = gLUhalfEdge2.Lnext;
        }
        gLUhalfEdge.Org.data = obj;
        gLUhalfEdge.Org.coords[0] = dArr[0];
        gLUhalfEdge.Org.coords[1] = dArr[1];
        gLUhalfEdge.Org.coords[2] = dArr[2];
        gLUhalfEdge.winding = 1;
        gLUhalfEdge.Sym.winding = -1;
        this.lastEdge = gLUhalfEdge;
        return true;
    }

    private void cacheVertex(double[] dArr, Object obj) {
        if (this.cache[this.cacheCount] == null) {
            this.cache[this.cacheCount] = new CachedVertex();
        }
        CachedVertex cachedVertex = this.cache[this.cacheCount];
        cachedVertex.data = obj;
        cachedVertex.coords[0] = dArr[0];
        cachedVertex.coords[1] = dArr[1];
        cachedVertex.coords[2] = dArr[2];
        this.cacheCount++;
    }

    private boolean flushCache() {
        CachedVertex[] cachedVertexArr = this.cache;
        this.mesh = Mesh.__gl_meshNewMesh();
        if (this.mesh == null) {
            return false;
        }
        for (int i = 0; i < this.cacheCount; i++) {
            CachedVertex cachedVertex = cachedVertexArr[i];
            if (!addVertex(cachedVertex.coords, cachedVertex.data)) {
                return false;
            }
        }
        this.cacheCount = 0;
        this.flushCacheOnNextVertex = false;
        return true;
    }

    public static PGLUtessellator gluNewTess() {
        return new GLUtessellatorImpl();
    }

    private void gotoState(int i) {
        while (this.state != i) {
            if (this.state < i) {
                if (this.state == 0) {
                    callErrorOrErrorData(100151);
                    gluTessBeginPolygon((Object) null);
                } else if (this.state == 1) {
                    callErrorOrErrorData(100152);
                    gluTessBeginContour();
                }
            } else if (this.state == 2) {
                callErrorOrErrorData(100154);
                gluTessEndContour();
            } else if (this.state == 1) {
                callErrorOrErrorData(100153);
                makeDormant();
            }
        }
    }

    private void makeDormant() {
        if (this.mesh != null) {
            Mesh.__gl_meshDeleteMesh(this.mesh);
        }
        this.state = 0;
        this.lastEdge = null;
        this.mesh = null;
    }

    private void requireState(int i) {
        if (this.state != i) {
            gotoState(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void callBeginOrBeginData(int i) {
        if (this.callBeginData != NULL_CB) {
            this.callBeginData.beginData(i, this.polygonData);
        } else {
            this.callBegin.begin(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void callCombineOrCombineData(double[] dArr, Object[] objArr, float[] fArr, Object[] objArr2) {
        if (this.callCombineData != NULL_CB) {
            this.callCombineData.combineData(dArr, objArr, fArr, objArr2, this.polygonData);
            return;
        }
        this.callCombine.combine(dArr, objArr, fArr, objArr2);
    }

    /* access modifiers changed from: package-private */
    public void callEdgeFlagOrEdgeFlagData(boolean z) {
        if (this.callEdgeFlagData != NULL_CB) {
            this.callEdgeFlagData.edgeFlagData(z, this.polygonData);
        } else {
            this.callEdgeFlag.edgeFlag(z);
        }
    }

    /* access modifiers changed from: package-private */
    public void callEndOrEndData() {
        if (this.callEndData != NULL_CB) {
            this.callEndData.endData(this.polygonData);
        } else {
            this.callEnd.end();
        }
    }

    /* access modifiers changed from: package-private */
    public void callErrorOrErrorData(int i) {
        if (this.callErrorData != NULL_CB) {
            this.callErrorData.errorData(i, this.polygonData);
        } else {
            this.callError.error(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void callVertexOrVertexData(Object obj) {
        if (this.callVertexData != NULL_CB) {
            this.callVertexData.vertexData(obj, this.polygonData);
        } else {
            this.callVertex.vertex(obj);
        }
    }

    public void gluBeginPolygon() {
        gluTessBeginPolygon((Object) null);
        gluTessBeginContour();
    }

    public void gluDeleteTess() {
        requireState(0);
    }

    public void gluEndPolygon() {
        gluTessEndContour();
        gluTessEndPolygon();
    }

    public void gluGetTessProperty(int i, double[] dArr, int i2) {
        double d = 1.0d;
        switch (i) {
            case PGLU.GLU_TESS_WINDING_RULE:
                if ($assertionsDisabled || this.windingRule == 100130 || this.windingRule == 100131 || this.windingRule == 100132 || this.windingRule == 100133 || this.windingRule == 100134) {
                    dArr[i2] = (double) this.windingRule;
                    return;
                }
                throw new AssertionError();
            case PGLU.GLU_TESS_BOUNDARY_ONLY:
                if ($assertionsDisabled || this.boundaryOnly || !this.boundaryOnly) {
                    if (!this.boundaryOnly) {
                        d = 0.0d;
                    }
                    dArr[i2] = d;
                    return;
                }
                throw new AssertionError();
            case PGLU.GLU_TESS_TOLERANCE:
                if ($assertionsDisabled || (GLU_TESS_DEFAULT_TOLERANCE <= this.relTolerance && this.relTolerance <= 1.0d)) {
                    dArr[i2] = this.relTolerance;
                    return;
                }
                throw new AssertionError();
            case PGLU.GLU_TESS_AVOID_DEGENERATE_TRIANGLES:
                if (!this.avoidDegenerateTris) {
                    d = 0.0d;
                }
                dArr[i2] = d;
                return;
            default:
                dArr[i2] = 0.0d;
                callErrorOrErrorData(PGLU.GLU_INVALID_ENUM);
                return;
        }
    }

    public void gluNextContour(int i) {
        gluTessEndContour();
        gluTessBeginContour();
    }

    public void gluTessBeginContour() {
        requireState(1);
        this.state = 2;
        this.lastEdge = null;
        if (this.cacheCount > 0) {
            this.flushCacheOnNextVertex = true;
        }
    }

    public void gluTessBeginPolygon(Object obj) {
        requireState(0);
        this.state = 1;
        this.cacheCount = 0;
        this.flushCacheOnNextVertex = false;
        this.mesh = null;
        this.polygonData = obj;
    }

    public void gluTessCallback(int i, PGLUtessellatorCallback pGLUtessellatorCallback) {
        boolean z = true;
        switch (i) {
            case 100100:
                if (pGLUtessellatorCallback == null) {
                    pGLUtessellatorCallback = NULL_CB;
                }
                this.callBegin = pGLUtessellatorCallback;
                return;
            case 100101:
                if (pGLUtessellatorCallback == null) {
                    pGLUtessellatorCallback = NULL_CB;
                }
                this.callVertex = pGLUtessellatorCallback;
                return;
            case 100102:
                if (pGLUtessellatorCallback == null) {
                    pGLUtessellatorCallback = NULL_CB;
                }
                this.callEnd = pGLUtessellatorCallback;
                return;
            case 100103:
                if (pGLUtessellatorCallback == null) {
                    pGLUtessellatorCallback = NULL_CB;
                }
                this.callError = pGLUtessellatorCallback;
                return;
            case 100104:
                this.callEdgeFlag = pGLUtessellatorCallback == null ? NULL_CB : pGLUtessellatorCallback;
                this.flagBoundary = pGLUtessellatorCallback != null;
                return;
            case PGLU.GLU_TESS_COMBINE:
                if (pGLUtessellatorCallback == null) {
                    pGLUtessellatorCallback = NULL_CB;
                }
                this.callCombine = pGLUtessellatorCallback;
                return;
            case PGLU.GLU_TESS_BEGIN_DATA:
                if (pGLUtessellatorCallback == null) {
                    pGLUtessellatorCallback = NULL_CB;
                }
                this.callBeginData = pGLUtessellatorCallback;
                return;
            case PGLU.GLU_TESS_VERTEX_DATA:
                if (pGLUtessellatorCallback == null) {
                    pGLUtessellatorCallback = NULL_CB;
                }
                this.callVertexData = pGLUtessellatorCallback;
                return;
            case PGLU.GLU_TESS_END_DATA:
                if (pGLUtessellatorCallback == null) {
                    pGLUtessellatorCallback = NULL_CB;
                }
                this.callEndData = pGLUtessellatorCallback;
                return;
            case PGLU.GLU_TESS_ERROR_DATA:
                if (pGLUtessellatorCallback == null) {
                    pGLUtessellatorCallback = NULL_CB;
                }
                this.callErrorData = pGLUtessellatorCallback;
                return;
            case PGLU.GLU_TESS_EDGE_FLAG_DATA:
                PGLUtessellatorCallback pGLUtessellatorCallback2 = pGLUtessellatorCallback == null ? NULL_CB : pGLUtessellatorCallback;
                this.callBegin = pGLUtessellatorCallback2;
                this.callEdgeFlagData = pGLUtessellatorCallback2;
                if (pGLUtessellatorCallback == null) {
                    z = false;
                }
                this.flagBoundary = z;
                return;
            case PGLU.GLU_TESS_COMBINE_DATA:
                if (pGLUtessellatorCallback == null) {
                    pGLUtessellatorCallback = NULL_CB;
                }
                this.callCombineData = pGLUtessellatorCallback;
                return;
            default:
                callErrorOrErrorData(PGLU.GLU_INVALID_ENUM);
                return;
        }
    }

    public void gluTessEndContour() {
        requireState(2);
        this.state = 1;
    }

    public void gluTessEndPolygon() {
        try {
            requireState(1);
            this.state = 0;
            if (this.mesh == null) {
                if (!this.flagBoundary && Render.__gl_renderCache(this)) {
                    this.polygonData = null;
                    return;
                } else if (!flushCache()) {
                    throw new RuntimeException();
                }
            }
            Normal.__gl_projectPolygon(this);
            if (!Sweep.__gl_computeInterior(this)) {
                throw new RuntimeException();
            }
            GLUmesh gLUmesh = this.mesh;
            if (!this.fatalError) {
                if (!(this.boundaryOnly ? TessMono.__gl_meshSetWindingNumber(gLUmesh, 1, true) : TessMono.__gl_meshTessellateInterior(gLUmesh, this.avoidDegenerateTris))) {
                    throw new RuntimeException();
                }
                Mesh.__gl_meshCheckMesh(gLUmesh);
                if (!(this.callBegin == NULL_CB && this.callEnd == NULL_CB && this.callVertex == NULL_CB && this.callEdgeFlag == NULL_CB && this.callBeginData == NULL_CB && this.callEndData == NULL_CB && this.callVertexData == NULL_CB && this.callEdgeFlagData == NULL_CB)) {
                    if (this.boundaryOnly) {
                        Render.__gl_renderBoundary(this, gLUmesh);
                    } else {
                        Render.__gl_renderMesh(this, gLUmesh);
                    }
                }
            }
            Mesh.__gl_meshDeleteMesh(gLUmesh);
            this.polygonData = null;
        } catch (Exception e) {
            e.printStackTrace();
            callErrorOrErrorData(PGLU.GLU_OUT_OF_MEMORY);
        }
    }

    public void gluTessNormal(double d, double d2, double d3) {
        this.normal[0] = d;
        this.normal[1] = d2;
        this.normal[2] = d3;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002d, code lost:
        if (r10 == GLU_TESS_DEFAULT_TOLERANCE) goto L_0x0035;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002f, code lost:
        r8.boundaryOnly = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0035, code lost:
        r0 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0018, code lost:
        callErrorOrErrorData(processing.opengl.tess.PGLU.GLU_INVALID_VALUE);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void gluTessProperty(int r9, double r10) {
        /*
            r8 = this;
            r0 = 1
            r1 = 0
            r6 = 0
            switch(r9) {
                case 100140: goto L_0x0022;
                case 100141: goto L_0x002b;
                case 100142: goto L_0x000e;
                case 100149: goto L_0x0037;
                default: goto L_0x0007;
            }
        L_0x0007:
            r0 = 100900(0x18a24, float:1.41391E-40)
            r8.callErrorOrErrorData(r0)
        L_0x000d:
            return
        L_0x000e:
            int r0 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1))
            if (r0 < 0) goto L_0x0018
            r0 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r0 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
            if (r0 <= 0) goto L_0x001f
        L_0x0018:
            r0 = 100901(0x18a25, float:1.41392E-40)
            r8.callErrorOrErrorData(r0)
            goto L_0x000d
        L_0x001f:
            r8.relTolerance = r10
            goto L_0x000d
        L_0x0022:
            int r2 = (int) r10
            double r4 = (double) r2
            int r3 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1))
            if (r3 != 0) goto L_0x0018
            switch(r2) {
                case 100130: goto L_0x0032;
                case 100131: goto L_0x0032;
                case 100132: goto L_0x0032;
                case 100133: goto L_0x0032;
                case 100134: goto L_0x0032;
                default: goto L_0x002b;
            }
        L_0x002b:
            int r2 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1))
            if (r2 == 0) goto L_0x0035
        L_0x002f:
            r8.boundaryOnly = r0
            goto L_0x000d
        L_0x0032:
            r8.windingRule = r2
            goto L_0x000d
        L_0x0035:
            r0 = r1
            goto L_0x002f
        L_0x0037:
            int r2 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1))
            if (r2 == 0) goto L_0x003e
        L_0x003b:
            r8.avoidDegenerateTris = r0
            goto L_0x000d
        L_0x003e:
            r0 = r1
            goto L_0x003b
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.opengl.tess.GLUtessellatorImpl.gluTessProperty(int, double):void");
    }

    public void gluTessVertex(double[] dArr, int i, Object obj) {
        boolean z = false;
        double[] dArr2 = new double[3];
        requireState(2);
        if (this.flushCacheOnNextVertex) {
            if (!flushCache()) {
                callErrorOrErrorData(PGLU.GLU_OUT_OF_MEMORY);
                return;
            }
            this.lastEdge = null;
        }
        for (int i2 = 0; i2 < 3; i2++) {
            double d = dArr[i2 + i];
            if (d < -1.0E150d) {
                d = -1.0E150d;
                z = true;
            }
            if (d > 1.0E150d) {
                d = 1.0E150d;
                z = true;
            }
            dArr2[i2] = d;
        }
        if (z) {
            callErrorOrErrorData(100155);
        }
        if (this.mesh == null) {
            if (this.cacheCount < 100) {
                cacheVertex(dArr2, obj);
                return;
            } else if (!flushCache()) {
                callErrorOrErrorData(PGLU.GLU_OUT_OF_MEMORY);
                return;
            }
        }
        if (!addVertex(dArr2, obj)) {
            callErrorOrErrorData(PGLU.GLU_OUT_OF_MEMORY);
        }
    }
}
