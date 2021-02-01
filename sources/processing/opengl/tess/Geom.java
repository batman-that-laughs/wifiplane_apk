package processing.opengl.tess;

class Geom {
    static final /* synthetic */ boolean $assertionsDisabled = (!Geom.class.desiredAssertionStatus());
    static final double EPSILON = 1.0E-5d;
    static final double ONE_MINUS_EPSILON = 0.99999d;

    private Geom() {
    }

    static double EdgeCos(GLUvertex gLUvertex, GLUvertex gLUvertex2, GLUvertex gLUvertex3) {
        double d = gLUvertex2.s - gLUvertex.s;
        double d2 = gLUvertex2.t - gLUvertex.t;
        double d3 = gLUvertex3.s - gLUvertex.s;
        double d4 = gLUvertex3.t - gLUvertex.t;
        double d5 = (d * d3) + (d2 * d4);
        double sqrt = Math.sqrt((d * d) + (d2 * d2)) * Math.sqrt((d3 * d3) + (d4 * d4));
        return sqrt > 0.0d ? d5 / sqrt : d5;
    }

    static double EdgeEval(GLUvertex gLUvertex, GLUvertex gLUvertex2, GLUvertex gLUvertex3) {
        if ($assertionsDisabled || (VertLeq(gLUvertex, gLUvertex2) && VertLeq(gLUvertex2, gLUvertex3))) {
            double d = gLUvertex2.s - gLUvertex.s;
            double d2 = gLUvertex3.s - gLUvertex2.s;
            if (d + d2 <= 0.0d) {
                return 0.0d;
            }
            if (d < d2) {
                return (gLUvertex2.t - gLUvertex.t) + ((d / (d2 + d)) * (gLUvertex.t - gLUvertex3.t));
            }
            return (gLUvertex2.t - gLUvertex3.t) + ((d2 / (d + d2)) * (gLUvertex3.t - gLUvertex.t));
        }
        throw new AssertionError();
    }

    static boolean EdgeGoesLeft(GLUhalfEdge gLUhalfEdge) {
        return VertLeq(gLUhalfEdge.Sym.Org, gLUhalfEdge.Org);
    }

    static boolean EdgeGoesRight(GLUhalfEdge gLUhalfEdge) {
        return VertLeq(gLUhalfEdge.Org, gLUhalfEdge.Sym.Org);
    }

    static void EdgeIntersect(GLUvertex gLUvertex, GLUvertex gLUvertex2, GLUvertex gLUvertex3, GLUvertex gLUvertex4, GLUvertex gLUvertex5) {
        if (VertLeq(gLUvertex, gLUvertex2)) {
            GLUvertex gLUvertex6 = gLUvertex2;
            gLUvertex2 = gLUvertex;
            gLUvertex = gLUvertex6;
        }
        if (VertLeq(gLUvertex3, gLUvertex4)) {
            GLUvertex gLUvertex7 = gLUvertex4;
            gLUvertex4 = gLUvertex3;
            gLUvertex3 = gLUvertex7;
        }
        if (VertLeq(gLUvertex2, gLUvertex4)) {
            GLUvertex gLUvertex8 = gLUvertex3;
            gLUvertex3 = gLUvertex;
            gLUvertex = gLUvertex8;
            GLUvertex gLUvertex9 = gLUvertex4;
            gLUvertex4 = gLUvertex2;
            gLUvertex2 = gLUvertex9;
        }
        if (!VertLeq(gLUvertex2, gLUvertex3)) {
            gLUvertex5.s = (gLUvertex2.s + gLUvertex3.s) / 2.0d;
        } else if (VertLeq(gLUvertex3, gLUvertex)) {
            double EdgeEval = EdgeEval(gLUvertex4, gLUvertex2, gLUvertex3);
            double EdgeEval2 = EdgeEval(gLUvertex2, gLUvertex3, gLUvertex);
            if (EdgeEval + EdgeEval2 < 0.0d) {
                EdgeEval = -EdgeEval;
                EdgeEval2 = -EdgeEval2;
            }
            gLUvertex5.s = Interpolate(EdgeEval, gLUvertex2.s, EdgeEval2, gLUvertex3.s);
        } else {
            double EdgeSign = EdgeSign(gLUvertex4, gLUvertex2, gLUvertex3);
            double d = -EdgeSign(gLUvertex4, gLUvertex, gLUvertex3);
            if (EdgeSign + d < 0.0d) {
                EdgeSign = -EdgeSign;
                d = -d;
            }
            gLUvertex5.s = Interpolate(EdgeSign, gLUvertex2.s, d, gLUvertex.s);
        }
        if (TransLeq(gLUvertex4, gLUvertex3)) {
            GLUvertex gLUvertex10 = gLUvertex3;
            gLUvertex3 = gLUvertex4;
            gLUvertex4 = gLUvertex10;
        }
        if (TransLeq(gLUvertex2, gLUvertex)) {
            GLUvertex gLUvertex11 = gLUvertex;
            gLUvertex = gLUvertex2;
            gLUvertex2 = gLUvertex11;
        }
        if (TransLeq(gLUvertex3, gLUvertex)) {
            GLUvertex gLUvertex12 = gLUvertex2;
            gLUvertex2 = gLUvertex4;
            gLUvertex4 = gLUvertex12;
            GLUvertex gLUvertex13 = gLUvertex;
            gLUvertex = gLUvertex3;
            gLUvertex3 = gLUvertex13;
        }
        if (!TransLeq(gLUvertex3, gLUvertex2)) {
            gLUvertex5.t = (gLUvertex3.t + gLUvertex2.t) / 2.0d;
        } else if (TransLeq(gLUvertex2, gLUvertex4)) {
            double TransEval = TransEval(gLUvertex, gLUvertex3, gLUvertex2);
            double TransEval2 = TransEval(gLUvertex3, gLUvertex2, gLUvertex4);
            if (TransEval + TransEval2 < 0.0d) {
                TransEval = -TransEval;
                TransEval2 = -TransEval2;
            }
            gLUvertex5.t = Interpolate(TransEval, gLUvertex3.t, TransEval2, gLUvertex2.t);
        } else {
            double TransSign = TransSign(gLUvertex, gLUvertex3, gLUvertex2);
            double d2 = -TransSign(gLUvertex, gLUvertex4, gLUvertex2);
            if (TransSign + d2 < 0.0d) {
                TransSign = -TransSign;
                d2 = -d2;
            }
            gLUvertex5.t = Interpolate(TransSign, gLUvertex3.t, d2, gLUvertex4.t);
        }
    }

    static double EdgeSign(GLUvertex gLUvertex, GLUvertex gLUvertex2, GLUvertex gLUvertex3) {
        if ($assertionsDisabled || (VertLeq(gLUvertex, gLUvertex2) && VertLeq(gLUvertex2, gLUvertex3))) {
            double d = gLUvertex2.s - gLUvertex.s;
            double d2 = gLUvertex3.s - gLUvertex2.s;
            if (d + d2 > 0.0d) {
                return ((gLUvertex2.t - gLUvertex3.t) * d) + ((gLUvertex2.t - gLUvertex.t) * d2);
            }
            return 0.0d;
        }
        throw new AssertionError();
    }

    static double Interpolate(double d, double d2, double d3, double d4) {
        if (d < 0.0d) {
            d = 0.0d;
        }
        if (d3 < 0.0d) {
            d3 = 0.0d;
        }
        return d <= d3 ? d3 == 0.0d ? (d2 + d4) / 2.0d : ((d4 - d2) * (d / (d + d3))) + d2 : ((d2 - d4) * (d3 / (d + d3))) + d4;
    }

    static double TransEval(GLUvertex gLUvertex, GLUvertex gLUvertex2, GLUvertex gLUvertex3) {
        if ($assertionsDisabled || (TransLeq(gLUvertex, gLUvertex2) && TransLeq(gLUvertex2, gLUvertex3))) {
            double d = gLUvertex2.t - gLUvertex.t;
            double d2 = gLUvertex3.t - gLUvertex2.t;
            if (d + d2 <= 0.0d) {
                return 0.0d;
            }
            if (d < d2) {
                return (gLUvertex2.s - gLUvertex.s) + ((d / (d2 + d)) * (gLUvertex.s - gLUvertex3.s));
            }
            return (gLUvertex2.s - gLUvertex3.s) + ((d2 / (d + d2)) * (gLUvertex3.s - gLUvertex.s));
        }
        throw new AssertionError();
    }

    static boolean TransLeq(GLUvertex gLUvertex, GLUvertex gLUvertex2) {
        return gLUvertex.t < gLUvertex2.t || (gLUvertex.t == gLUvertex2.t && gLUvertex.s <= gLUvertex2.s);
    }

    static double TransSign(GLUvertex gLUvertex, GLUvertex gLUvertex2, GLUvertex gLUvertex3) {
        if ($assertionsDisabled || (TransLeq(gLUvertex, gLUvertex2) && TransLeq(gLUvertex2, gLUvertex3))) {
            double d = gLUvertex2.t - gLUvertex.t;
            double d2 = gLUvertex3.t - gLUvertex2.t;
            if (d + d2 > 0.0d) {
                return ((gLUvertex2.s - gLUvertex3.s) * d) + ((gLUvertex2.s - gLUvertex.s) * d2);
            }
            return 0.0d;
        }
        throw new AssertionError();
    }

    static boolean VertCCW(GLUvertex gLUvertex, GLUvertex gLUvertex2, GLUvertex gLUvertex3) {
        return ((gLUvertex.s * (gLUvertex2.t - gLUvertex3.t)) + (gLUvertex2.s * (gLUvertex3.t - gLUvertex.t))) + (gLUvertex3.s * (gLUvertex.t - gLUvertex2.t)) >= 0.0d;
    }

    static boolean VertEq(GLUvertex gLUvertex, GLUvertex gLUvertex2) {
        return gLUvertex.s == gLUvertex2.s && gLUvertex.t == gLUvertex2.t;
    }

    static double VertL1dist(GLUvertex gLUvertex, GLUvertex gLUvertex2) {
        return Math.abs(gLUvertex.s - gLUvertex2.s) + Math.abs(gLUvertex.t - gLUvertex2.t);
    }

    static boolean VertLeq(GLUvertex gLUvertex, GLUvertex gLUvertex2) {
        return gLUvertex.s < gLUvertex2.s || (gLUvertex.s == gLUvertex2.s && gLUvertex.t <= gLUvertex2.t);
    }
}
