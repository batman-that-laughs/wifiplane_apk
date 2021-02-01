package processing.opengl.tess;

class Render {
    static final /* synthetic */ boolean $assertionsDisabled = (!Render.class.desiredAssertionStatus());
    private static final int SIGN_INCONSISTENT = 2;
    private static final boolean USE_OPTIMIZED_CODE_PATH = false;
    private static final RenderFan renderFan = new RenderFan();
    private static final RenderStrip renderStrip = new RenderStrip();
    private static final RenderTriangle renderTriangle = new RenderTriangle();

    private static class FaceCount {
        GLUhalfEdge eStart;
        renderCallBack render;
        long size;

        public FaceCount() {
        }

        public FaceCount(long j, GLUhalfEdge gLUhalfEdge, renderCallBack rendercallback) {
            this.size = j;
            this.eStart = gLUhalfEdge;
            this.render = rendercallback;
        }
    }

    private static class RenderFan implements renderCallBack {
        static final /* synthetic */ boolean $assertionsDisabled = (!Render.class.desiredAssertionStatus());

        private RenderFan() {
        }

        public void render(GLUtessellatorImpl gLUtessellatorImpl, GLUhalfEdge gLUhalfEdge, long j) {
            gLUtessellatorImpl.callBeginOrBeginData(6);
            gLUtessellatorImpl.callVertexOrVertexData(gLUhalfEdge.Org.data);
            gLUtessellatorImpl.callVertexOrVertexData(gLUhalfEdge.Sym.Org.data);
            while (!Render.Marked(gLUhalfEdge.Lface)) {
                gLUhalfEdge.Lface.marked = true;
                j--;
                gLUhalfEdge = gLUhalfEdge.Onext;
                gLUtessellatorImpl.callVertexOrVertexData(gLUhalfEdge.Sym.Org.data);
            }
            if ($assertionsDisabled || j == 0) {
                gLUtessellatorImpl.callEndOrEndData();
                return;
            }
            throw new AssertionError();
        }
    }

    private static class RenderStrip implements renderCallBack {
        static final /* synthetic */ boolean $assertionsDisabled = (!Render.class.desiredAssertionStatus());

        private RenderStrip() {
        }

        public void render(GLUtessellatorImpl gLUtessellatorImpl, GLUhalfEdge gLUhalfEdge, long j) {
            gLUtessellatorImpl.callBeginOrBeginData(5);
            gLUtessellatorImpl.callVertexOrVertexData(gLUhalfEdge.Org.data);
            gLUtessellatorImpl.callVertexOrVertexData(gLUhalfEdge.Sym.Org.data);
            while (!Render.Marked(gLUhalfEdge.Lface)) {
                gLUhalfEdge.Lface.marked = true;
                j--;
                GLUhalfEdge gLUhalfEdge2 = gLUhalfEdge.Lnext.Sym;
                gLUtessellatorImpl.callVertexOrVertexData(gLUhalfEdge2.Org.data);
                if (Render.Marked(gLUhalfEdge2.Lface)) {
                    break;
                }
                gLUhalfEdge2.Lface.marked = true;
                j--;
                gLUhalfEdge = gLUhalfEdge2.Onext;
                gLUtessellatorImpl.callVertexOrVertexData(gLUhalfEdge.Sym.Org.data);
            }
            if ($assertionsDisabled || j == 0) {
                gLUtessellatorImpl.callEndOrEndData();
                return;
            }
            throw new AssertionError();
        }
    }

    private static class RenderTriangle implements renderCallBack {
        static final /* synthetic */ boolean $assertionsDisabled = (!Render.class.desiredAssertionStatus());

        private RenderTriangle() {
        }

        public void render(GLUtessellatorImpl gLUtessellatorImpl, GLUhalfEdge gLUhalfEdge, long j) {
            if ($assertionsDisabled || j == 1) {
                gLUtessellatorImpl.lonelyTriList = Render.AddToTrail(gLUhalfEdge.Lface, gLUtessellatorImpl.lonelyTriList);
                return;
            }
            throw new AssertionError();
        }
    }

    private interface renderCallBack {
        void render(GLUtessellatorImpl gLUtessellatorImpl, GLUhalfEdge gLUhalfEdge, long j);
    }

    private Render() {
    }

    /* access modifiers changed from: private */
    public static GLUface AddToTrail(GLUface gLUface, GLUface gLUface2) {
        gLUface.trail = gLUface2;
        gLUface.marked = true;
        return gLUface;
    }

    static int ComputeNormal(GLUtessellatorImpl gLUtessellatorImpl, double[] dArr, boolean z) {
        CachedVertex[] cachedVertexArr = gLUtessellatorImpl.cache;
        int i = gLUtessellatorImpl.cacheCount;
        double[] dArr2 = new double[3];
        int i2 = 0;
        if (!z) {
            dArr[2] = 0.0d;
            dArr[1] = 0.0d;
            dArr[0] = 0.0d;
        }
        int i3 = 1;
        double d = cachedVertexArr[1].coords[0] - cachedVertexArr[0].coords[0];
        double d2 = cachedVertexArr[1].coords[1] - cachedVertexArr[0].coords[1];
        double d3 = cachedVertexArr[1].coords[2] - cachedVertexArr[0].coords[2];
        double d4 = d;
        double d5 = d2;
        while (true) {
            i3++;
            if (i3 >= i) {
                return i2;
            }
            double d6 = cachedVertexArr[i3].coords[0] - cachedVertexArr[0].coords[0];
            double d7 = cachedVertexArr[i3].coords[1] - cachedVertexArr[0].coords[1];
            double d8 = cachedVertexArr[i3].coords[2] - cachedVertexArr[0].coords[2];
            dArr2[0] = (d5 * d8) - (d3 * d7);
            dArr2[1] = (d3 * d6) - (d4 * d8);
            dArr2[2] = (d4 * d7) - (d5 * d6);
            double d9 = (dArr2[0] * dArr[0]) + (dArr2[1] * dArr[1]) + (dArr2[2] * dArr[2]);
            if (!z) {
                if (d9 >= 0.0d) {
                    dArr[0] = dArr[0] + dArr2[0];
                    dArr[1] = dArr[1] + dArr2[1];
                    dArr[2] = dArr[2] + dArr2[2];
                    d3 = d8;
                    d5 = d7;
                    d4 = d6;
                } else {
                    dArr[0] = dArr[0] - dArr2[0];
                    dArr[1] = dArr[1] - dArr2[1];
                    dArr[2] = dArr[2] - dArr2[2];
                    d3 = d8;
                    d5 = d7;
                    d4 = d6;
                }
            } else if (d9 == 0.0d) {
                d3 = d8;
                d5 = d7;
                d4 = d6;
            } else if (d9 > 0.0d) {
                if (i2 < 0) {
                    return 2;
                }
                i2 = 1;
                d3 = d8;
                d5 = d7;
                d4 = d6;
            } else if (i2 > 0) {
                return 2;
            } else {
                i2 = -1;
                d3 = d8;
                d5 = d7;
                d4 = d6;
            }
        }
    }

    private static void FreeTrail(GLUface gLUface) {
        while (gLUface != null) {
            gLUface.marked = false;
            gLUface = gLUface.trail;
        }
    }

    private static boolean IsEven(long j) {
        return (1 & j) == 0;
    }

    /* access modifiers changed from: private */
    public static boolean Marked(GLUface gLUface) {
        return !gLUface.inside || gLUface.marked;
    }

    static FaceCount MaximumFan(GLUhalfEdge gLUhalfEdge) {
        FaceCount faceCount = new FaceCount(0, (GLUhalfEdge) null, renderFan);
        GLUface gLUface = null;
        for (GLUhalfEdge gLUhalfEdge2 = gLUhalfEdge; !Marked(gLUhalfEdge2.Lface); gLUhalfEdge2 = gLUhalfEdge2.Onext) {
            gLUface = AddToTrail(gLUhalfEdge2.Lface, gLUface);
            faceCount.size++;
        }
        while (!Marked(gLUhalfEdge.Sym.Lface)) {
            gLUface = AddToTrail(gLUhalfEdge.Sym.Lface, gLUface);
            faceCount.size++;
            gLUhalfEdge = gLUhalfEdge.Sym.Lnext;
        }
        faceCount.eStart = gLUhalfEdge;
        FreeTrail(gLUface);
        return faceCount;
    }

    static FaceCount MaximumStrip(GLUhalfEdge gLUhalfEdge) {
        long j = 0;
        FaceCount faceCount = new FaceCount(0, (GLUhalfEdge) null, renderStrip);
        GLUface gLUface = null;
        long j2 = 0;
        GLUhalfEdge gLUhalfEdge2 = gLUhalfEdge;
        while (!Marked(gLUhalfEdge2.Lface)) {
            gLUface = AddToTrail(gLUhalfEdge2.Lface, gLUface);
            j2++;
            gLUhalfEdge2 = gLUhalfEdge2.Lnext.Sym;
            if (Marked(gLUhalfEdge2.Lface)) {
                break;
            }
            gLUface = AddToTrail(gLUhalfEdge2.Lface, gLUface);
            j2++;
            gLUhalfEdge2 = gLUhalfEdge2.Onext;
        }
        while (!Marked(gLUhalfEdge.Sym.Lface)) {
            gLUface = AddToTrail(gLUhalfEdge.Sym.Lface, gLUface);
            j++;
            gLUhalfEdge = gLUhalfEdge.Sym.Lnext;
            if (Marked(gLUhalfEdge.Sym.Lface)) {
                break;
            }
            gLUface = AddToTrail(gLUhalfEdge.Sym.Lface, gLUface);
            j++;
            gLUhalfEdge = gLUhalfEdge.Sym.Onext.Sym;
        }
        faceCount.size = j2 + j;
        if (IsEven(j2)) {
            faceCount.eStart = gLUhalfEdge2.Sym;
        } else if (IsEven(j)) {
            faceCount.eStart = gLUhalfEdge;
        } else {
            faceCount.size--;
            faceCount.eStart = gLUhalfEdge.Onext;
        }
        FreeTrail(gLUface);
        return faceCount;
    }

    static void RenderLonelyTriangles(GLUtessellatorImpl gLUtessellatorImpl, GLUface gLUface) {
        char c = 65535;
        gLUtessellatorImpl.callBeginOrBeginData(4);
        while (gLUface != null) {
            GLUhalfEdge gLUhalfEdge = gLUface.anEdge;
            do {
                if (gLUtessellatorImpl.flagBoundary) {
                    char c2 = !gLUhalfEdge.Sym.Lface.inside ? (char) 1 : 0;
                    if (c != c2) {
                        gLUtessellatorImpl.callEdgeFlagOrEdgeFlagData(c2 != 0);
                        c = c2;
                    }
                }
                gLUtessellatorImpl.callVertexOrVertexData(gLUhalfEdge.Org.data);
                gLUhalfEdge = gLUhalfEdge.Lnext;
            } while (gLUhalfEdge != gLUface.anEdge);
            gLUface = gLUface.trail;
        }
        gLUtessellatorImpl.callEndOrEndData();
    }

    static void RenderMaximumFaceGroup(GLUtessellatorImpl gLUtessellatorImpl, GLUface gLUface) {
        FaceCount faceCount;
        GLUhalfEdge gLUhalfEdge = gLUface.anEdge;
        FaceCount faceCount2 = new FaceCount();
        new FaceCount();
        faceCount2.size = 1;
        faceCount2.eStart = gLUhalfEdge;
        faceCount2.render = renderTriangle;
        if (!gLUtessellatorImpl.flagBoundary) {
            faceCount = MaximumFan(gLUhalfEdge);
            if (faceCount.size <= faceCount2.size) {
                faceCount = faceCount2;
            }
            FaceCount MaximumFan = MaximumFan(gLUhalfEdge.Lnext);
            if (MaximumFan.size > faceCount.size) {
                faceCount = MaximumFan;
            }
            FaceCount MaximumFan2 = MaximumFan(gLUhalfEdge.Onext.Sym);
            if (MaximumFan2.size > faceCount.size) {
                faceCount = MaximumFan2;
            }
            FaceCount MaximumStrip = MaximumStrip(gLUhalfEdge);
            if (MaximumStrip.size > faceCount.size) {
                faceCount = MaximumStrip;
            }
            FaceCount MaximumStrip2 = MaximumStrip(gLUhalfEdge.Lnext);
            if (MaximumStrip2.size > faceCount.size) {
                faceCount = MaximumStrip2;
            }
            FaceCount MaximumStrip3 = MaximumStrip(gLUhalfEdge.Onext.Sym);
            if (MaximumStrip3.size > faceCount.size) {
                faceCount = MaximumStrip3;
            }
        } else {
            faceCount = faceCount2;
        }
        faceCount.render.render(gLUtessellatorImpl, faceCount.eStart, faceCount.size);
    }

    public static void __gl_renderBoundary(GLUtessellatorImpl gLUtessellatorImpl, GLUmesh gLUmesh) {
        for (GLUface gLUface = gLUmesh.fHead.next; gLUface != gLUmesh.fHead; gLUface = gLUface.next) {
            if (gLUface.inside) {
                gLUtessellatorImpl.callBeginOrBeginData(2);
                GLUhalfEdge gLUhalfEdge = gLUface.anEdge;
                do {
                    gLUtessellatorImpl.callVertexOrVertexData(gLUhalfEdge.Org.data);
                    gLUhalfEdge = gLUhalfEdge.Lnext;
                } while (gLUhalfEdge != gLUface.anEdge);
                gLUtessellatorImpl.callEndOrEndData();
            }
        }
    }

    public static boolean __gl_renderCache(GLUtessellatorImpl gLUtessellatorImpl) {
        CachedVertex[] cachedVertexArr = gLUtessellatorImpl.cache;
        int i = gLUtessellatorImpl.cacheCount;
        double[] dArr = new double[3];
        if (gLUtessellatorImpl.cacheCount < 3) {
            return true;
        }
        dArr[0] = gLUtessellatorImpl.normal[0];
        dArr[1] = gLUtessellatorImpl.normal[1];
        dArr[2] = gLUtessellatorImpl.normal[2];
        if (dArr[0] == 0.0d && dArr[1] == 0.0d && dArr[2] == 0.0d) {
            ComputeNormal(gLUtessellatorImpl, dArr, false);
        }
        int ComputeNormal = ComputeNormal(gLUtessellatorImpl, dArr, true);
        if (ComputeNormal == 2) {
            return false;
        }
        return ComputeNormal == 0;
    }

    public static void __gl_renderMesh(GLUtessellatorImpl gLUtessellatorImpl, GLUmesh gLUmesh) {
        gLUtessellatorImpl.lonelyTriList = null;
        for (GLUface gLUface = gLUmesh.fHead.next; gLUface != gLUmesh.fHead; gLUface = gLUface.next) {
            gLUface.marked = false;
        }
        for (GLUface gLUface2 = gLUmesh.fHead.next; gLUface2 != gLUmesh.fHead; gLUface2 = gLUface2.next) {
            if (gLUface2.inside && !gLUface2.marked) {
                RenderMaximumFaceGroup(gLUtessellatorImpl, gLUface2);
                if (!$assertionsDisabled && !gLUface2.marked) {
                    throw new AssertionError();
                }
            }
        }
        if (gLUtessellatorImpl.lonelyTriList != null) {
            RenderLonelyTriangles(gLUtessellatorImpl, gLUtessellatorImpl.lonelyTriList);
            gLUtessellatorImpl.lonelyTriList = null;
        }
    }
}
