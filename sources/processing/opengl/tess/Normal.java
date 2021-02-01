package processing.opengl.tess;

class Normal {
    static final /* synthetic */ boolean $assertionsDisabled = (!Normal.class.desiredAssertionStatus());
    static boolean SLANTED_SWEEP = false;
    static double S_UNIT_X;
    static double S_UNIT_Y;
    private static final boolean TRUE_PROJECT = false;

    static {
        if (SLANTED_SWEEP) {
            S_UNIT_X = 0.5094153956495538d;
            S_UNIT_Y = 0.8605207462201063d;
            return;
        }
        S_UNIT_X = 1.0d;
        S_UNIT_Y = 0.0d;
    }

    private Normal() {
    }

    static void CheckOrientation(GLUtessellatorImpl gLUtessellatorImpl) {
        GLUface gLUface = gLUtessellatorImpl.mesh.fHead;
        GLUvertex gLUvertex = gLUtessellatorImpl.mesh.vHead;
        double d = 0.0d;
        for (GLUface gLUface2 = gLUface.next; gLUface2 != gLUface; gLUface2 = gLUface2.next) {
            GLUhalfEdge gLUhalfEdge = gLUface2.anEdge;
            if (gLUhalfEdge.winding > 0) {
                do {
                    d += (gLUhalfEdge.Org.s - gLUhalfEdge.Sym.Org.s) * (gLUhalfEdge.Org.t + gLUhalfEdge.Sym.Org.t);
                    gLUhalfEdge = gLUhalfEdge.Lnext;
                } while (gLUhalfEdge != gLUface2.anEdge);
            }
        }
        if (d < 0.0d) {
            for (GLUvertex gLUvertex2 = gLUvertex.next; gLUvertex2 != gLUvertex; gLUvertex2 = gLUvertex2.next) {
                gLUvertex2.t = -gLUvertex2.t;
            }
            gLUtessellatorImpl.tUnit[0] = -gLUtessellatorImpl.tUnit[0];
            gLUtessellatorImpl.tUnit[1] = -gLUtessellatorImpl.tUnit[1];
            gLUtessellatorImpl.tUnit[2] = -gLUtessellatorImpl.tUnit[2];
        }
    }

    static void ComputeNormal(GLUtessellatorImpl gLUtessellatorImpl, double[] dArr) {
        GLUvertex gLUvertex = gLUtessellatorImpl.mesh.vHead;
        double[] dArr2 = new double[3];
        double[] dArr3 = new double[3];
        GLUvertex[] gLUvertexArr = new GLUvertex[3];
        GLUvertex[] gLUvertexArr2 = new GLUvertex[3];
        double[] dArr4 = new double[3];
        double[] dArr5 = new double[3];
        double[] dArr6 = new double[3];
        dArr2[2] = -2.0E150d;
        dArr2[1] = -2.0E150d;
        dArr2[0] = -2.0E150d;
        dArr3[2] = 2.0E150d;
        dArr3[1] = 2.0E150d;
        dArr3[0] = 2.0E150d;
        for (GLUvertex gLUvertex2 = gLUvertex.next; gLUvertex2 != gLUvertex; gLUvertex2 = gLUvertex2.next) {
            for (int i = 0; i < 3; i++) {
                double d = gLUvertex2.coords[i];
                if (d < dArr3[i]) {
                    dArr3[i] = d;
                    gLUvertexArr[i] = gLUvertex2;
                }
                if (d > dArr2[i]) {
                    dArr2[i] = d;
                    gLUvertexArr2[i] = gLUvertex2;
                }
            }
        }
        char c = 0;
        if (dArr2[1] - dArr3[1] > dArr2[0] - dArr3[0]) {
            c = 1;
        }
        if (dArr2[2] - dArr3[2] > dArr2[c] - dArr3[c]) {
            c = 2;
        }
        if (dArr3[c] >= dArr2[c]) {
            dArr[0] = 0.0d;
            dArr[1] = 0.0d;
            dArr[2] = 1.0d;
            return;
        }
        double d2 = 0.0d;
        GLUvertex gLUvertex3 = gLUvertexArr[c];
        GLUvertex gLUvertex4 = gLUvertexArr2[c];
        dArr4[0] = gLUvertex3.coords[0] - gLUvertex4.coords[0];
        dArr4[1] = gLUvertex3.coords[1] - gLUvertex4.coords[1];
        dArr4[2] = gLUvertex3.coords[2] - gLUvertex4.coords[2];
        GLUvertex gLUvertex5 = gLUvertex.next;
        while (gLUvertex5 != gLUvertex) {
            dArr5[0] = gLUvertex5.coords[0] - gLUvertex4.coords[0];
            dArr5[1] = gLUvertex5.coords[1] - gLUvertex4.coords[1];
            dArr5[2] = gLUvertex5.coords[2] - gLUvertex4.coords[2];
            dArr6[0] = (dArr4[1] * dArr5[2]) - (dArr4[2] * dArr5[1]);
            dArr6[1] = (dArr4[2] * dArr5[0]) - (dArr4[0] * dArr5[2]);
            dArr6[2] = (dArr4[0] * dArr5[1]) - (dArr4[1] * dArr5[0]);
            double d3 = (dArr6[0] * dArr6[0]) + (dArr6[1] * dArr6[1]) + (dArr6[2] * dArr6[2]);
            if (d3 > d2) {
                dArr[0] = dArr6[0];
                dArr[1] = dArr6[1];
                dArr[2] = dArr6[2];
            } else {
                d3 = d2;
            }
            gLUvertex5 = gLUvertex5.next;
            d2 = d3;
        }
        if (d2 <= 0.0d) {
            dArr[2] = 0.0d;
            dArr[1] = 0.0d;
            dArr[0] = 0.0d;
            dArr[LongAxis(dArr4)] = 1.0d;
        }
    }

    private static double Dot(double[] dArr, double[] dArr2) {
        return (dArr[0] * dArr2[0]) + (dArr[1] * dArr2[1]) + (dArr[2] * dArr2[2]);
    }

    static int LongAxis(double[] dArr) {
        int i = 1;
        if (Math.abs(dArr[1]) <= Math.abs(dArr[0])) {
            i = 0;
        }
        if (Math.abs(dArr[2]) > Math.abs(dArr[i])) {
            return 2;
        }
        return i;
    }

    static void Normalize(double[] dArr) {
        double d = (dArr[0] * dArr[0]) + (dArr[1] * dArr[1]) + (dArr[2] * dArr[2]);
        if ($assertionsDisabled || d > 0.0d) {
            double sqrt = Math.sqrt(d);
            dArr[0] = dArr[0] / sqrt;
            dArr[1] = dArr[1] / sqrt;
            dArr[2] = dArr[2] / sqrt;
            return;
        }
        throw new AssertionError();
    }

    public static void __gl_projectPolygon(GLUtessellatorImpl gLUtessellatorImpl) {
        boolean z = true;
        GLUvertex gLUvertex = gLUtessellatorImpl.mesh.vHead;
        double[] dArr = {gLUtessellatorImpl.normal[0], gLUtessellatorImpl.normal[1], gLUtessellatorImpl.normal[2]};
        if (dArr[0] == 0.0d && dArr[1] == 0.0d && dArr[2] == 0.0d) {
            ComputeNormal(gLUtessellatorImpl, dArr);
        } else {
            z = false;
        }
        double[] dArr2 = gLUtessellatorImpl.sUnit;
        double[] dArr3 = gLUtessellatorImpl.tUnit;
        int LongAxis = LongAxis(dArr);
        dArr2[LongAxis] = 0.0d;
        dArr2[(LongAxis + 1) % 3] = S_UNIT_X;
        dArr2[(LongAxis + 2) % 3] = S_UNIT_Y;
        dArr3[LongAxis] = 0.0d;
        dArr3[(LongAxis + 1) % 3] = dArr[LongAxis] > 0.0d ? -S_UNIT_Y : S_UNIT_Y;
        dArr3[(LongAxis + 2) % 3] = dArr[LongAxis] > 0.0d ? S_UNIT_X : -S_UNIT_X;
        for (GLUvertex gLUvertex2 = gLUvertex.next; gLUvertex2 != gLUvertex; gLUvertex2 = gLUvertex2.next) {
            gLUvertex2.s = Dot(gLUvertex2.coords, dArr2);
            gLUvertex2.t = Dot(gLUvertex2.coords, dArr3);
        }
        if (z) {
            CheckOrientation(gLUtessellatorImpl);
        }
    }
}
