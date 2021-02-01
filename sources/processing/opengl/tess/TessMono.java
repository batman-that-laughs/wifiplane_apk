package processing.opengl.tess;

class TessMono {
    static final /* synthetic */ boolean $assertionsDisabled = (!TessMono.class.desiredAssertionStatus());

    TessMono() {
    }

    public static void __gl_meshDiscardExterior(GLUmesh gLUmesh) {
        GLUface gLUface = gLUmesh.fHead.next;
        while (gLUface != gLUmesh.fHead) {
            GLUface gLUface2 = gLUface.next;
            if (!gLUface.inside) {
                Mesh.__gl_meshZapFace(gLUface);
            }
            gLUface = gLUface2;
        }
    }

    public static boolean __gl_meshSetWindingNumber(GLUmesh gLUmesh, int i, boolean z) {
        GLUhalfEdge gLUhalfEdge = gLUmesh.eHead.next;
        while (gLUhalfEdge != gLUmesh.eHead) {
            GLUhalfEdge gLUhalfEdge2 = gLUhalfEdge.next;
            if (gLUhalfEdge.Sym.Lface.inside != gLUhalfEdge.Lface.inside) {
                gLUhalfEdge.winding = gLUhalfEdge.Lface.inside ? i : -i;
            } else if (!z) {
                gLUhalfEdge.winding = 0;
            } else if (!Mesh.__gl_meshDelete(gLUhalfEdge)) {
                return false;
            }
            gLUhalfEdge = gLUhalfEdge2;
        }
        return true;
    }

    public static boolean __gl_meshTessellateInterior(GLUmesh gLUmesh, boolean z) {
        GLUface gLUface = gLUmesh.fHead.next;
        while (gLUface != gLUmesh.fHead) {
            GLUface gLUface2 = gLUface.next;
            if (gLUface.inside && !__gl_meshTessellateMonoRegion(gLUface, z)) {
                return false;
            }
            gLUface = gLUface2;
        }
        return true;
    }

    static boolean __gl_meshTessellateMonoRegion(GLUface gLUface, boolean z) {
        GLUhalfEdge gLUhalfEdge = gLUface.anEdge;
        if ($assertionsDisabled || !(gLUhalfEdge.Lnext == gLUhalfEdge || gLUhalfEdge.Lnext.Lnext == gLUhalfEdge)) {
            while (Geom.VertLeq(gLUhalfEdge.Sym.Org, gLUhalfEdge.Org)) {
                gLUhalfEdge = gLUhalfEdge.Onext.Sym;
            }
            while (Geom.VertLeq(gLUhalfEdge.Org, gLUhalfEdge.Sym.Org)) {
                gLUhalfEdge = gLUhalfEdge.Lnext;
            }
            GLUhalfEdge gLUhalfEdge2 = gLUhalfEdge.Onext.Sym;
            GLUhalfEdge gLUhalfEdge3 = gLUhalfEdge;
            boolean z2 = false;
            while (gLUhalfEdge3.Lnext != gLUhalfEdge2) {
                if (z && !z2) {
                    if (Geom.EdgeCos(gLUhalfEdge2.Lnext.Org, gLUhalfEdge2.Org, gLUhalfEdge2.Lnext.Lnext.Org) <= -0.99999d) {
                        while (true) {
                            gLUhalfEdge2 = gLUhalfEdge2.Onext.Sym;
                            if (gLUhalfEdge3.Lnext != gLUhalfEdge2) {
                                if (Geom.EdgeCos(gLUhalfEdge2.Lnext.Org, gLUhalfEdge2.Org, gLUhalfEdge2.Lnext.Lnext.Org) > -0.99999d) {
                                    z2 = true;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    } else if (Geom.EdgeCos(gLUhalfEdge3.Onext.Sym.Org, gLUhalfEdge3.Org, gLUhalfEdge3.Onext.Sym.Onext.Sym.Org) <= -0.99999d) {
                        do {
                            gLUhalfEdge3 = gLUhalfEdge3.Lnext;
                            if (gLUhalfEdge3.Lnext == gLUhalfEdge2) {
                                break;
                            }
                        } while (Geom.EdgeCos(gLUhalfEdge3.Onext.Sym.Org, gLUhalfEdge3.Org, gLUhalfEdge3.Onext.Sym.Onext.Sym.Org) > -0.99999d);
                        z2 = true;
                    }
                    if (gLUhalfEdge3.Lnext == gLUhalfEdge2) {
                        break;
                    }
                }
                if (Geom.VertLeq(gLUhalfEdge3.Sym.Org, gLUhalfEdge2.Org)) {
                    while (gLUhalfEdge2.Lnext != gLUhalfEdge3 && (Geom.EdgeGoesLeft(gLUhalfEdge2.Lnext) || Geom.EdgeSign(gLUhalfEdge2.Org, gLUhalfEdge2.Sym.Org, gLUhalfEdge2.Lnext.Sym.Org) <= 0.0d)) {
                        GLUhalfEdge __gl_meshConnect = Mesh.__gl_meshConnect(gLUhalfEdge2.Lnext, gLUhalfEdge2);
                        if (__gl_meshConnect == null) {
                            return false;
                        }
                        gLUhalfEdge2 = __gl_meshConnect.Sym;
                        z2 = false;
                    }
                    gLUhalfEdge2 = gLUhalfEdge2.Onext.Sym;
                } else {
                    while (gLUhalfEdge2.Lnext != gLUhalfEdge3 && (Geom.EdgeGoesRight(gLUhalfEdge3.Onext.Sym) || Geom.EdgeSign(gLUhalfEdge3.Sym.Org, gLUhalfEdge3.Org, gLUhalfEdge3.Onext.Sym.Org) >= 0.0d)) {
                        GLUhalfEdge __gl_meshConnect2 = Mesh.__gl_meshConnect(gLUhalfEdge3, gLUhalfEdge3.Onext.Sym);
                        if (__gl_meshConnect2 == null) {
                            return false;
                        }
                        gLUhalfEdge3 = __gl_meshConnect2.Sym;
                        z2 = false;
                    }
                    gLUhalfEdge3 = gLUhalfEdge3.Lnext;
                }
            }
            if ($assertionsDisabled || gLUhalfEdge2.Lnext != gLUhalfEdge3) {
                while (gLUhalfEdge2.Lnext.Lnext != gLUhalfEdge3) {
                    GLUhalfEdge __gl_meshConnect3 = Mesh.__gl_meshConnect(gLUhalfEdge2.Lnext, gLUhalfEdge2);
                    if (__gl_meshConnect3 == null) {
                        return false;
                    }
                    gLUhalfEdge2 = __gl_meshConnect3.Sym;
                }
                return true;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }
}
