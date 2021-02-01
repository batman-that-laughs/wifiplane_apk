package processing.opengl.tess;

class Mesh {
    static final /* synthetic */ boolean $assertionsDisabled = (!Mesh.class.desiredAssertionStatus());

    private Mesh() {
    }

    static void KillEdge(GLUhalfEdge gLUhalfEdge) {
        if (!gLUhalfEdge.first) {
            gLUhalfEdge = gLUhalfEdge.Sym;
        }
        GLUhalfEdge gLUhalfEdge2 = gLUhalfEdge.next;
        GLUhalfEdge gLUhalfEdge3 = gLUhalfEdge.Sym.next;
        gLUhalfEdge2.Sym.next = gLUhalfEdge3;
        gLUhalfEdge3.Sym.next = gLUhalfEdge2;
    }

    static void KillFace(GLUface gLUface, GLUface gLUface2) {
        GLUhalfEdge gLUhalfEdge = gLUface.anEdge;
        GLUhalfEdge gLUhalfEdge2 = gLUhalfEdge;
        do {
            gLUhalfEdge2.Lface = gLUface2;
            gLUhalfEdge2 = gLUhalfEdge2.Lnext;
        } while (gLUhalfEdge2 != gLUhalfEdge);
        GLUface gLUface3 = gLUface.prev;
        GLUface gLUface4 = gLUface.next;
        gLUface4.prev = gLUface3;
        gLUface3.next = gLUface4;
    }

    static void KillVertex(GLUvertex gLUvertex, GLUvertex gLUvertex2) {
        GLUhalfEdge gLUhalfEdge = gLUvertex.anEdge;
        GLUhalfEdge gLUhalfEdge2 = gLUhalfEdge;
        do {
            gLUhalfEdge2.Org = gLUvertex2;
            gLUhalfEdge2 = gLUhalfEdge2.Onext;
        } while (gLUhalfEdge2 != gLUhalfEdge);
        GLUvertex gLUvertex3 = gLUvertex.prev;
        GLUvertex gLUvertex4 = gLUvertex.next;
        gLUvertex4.prev = gLUvertex3;
        gLUvertex3.next = gLUvertex4;
    }

    static GLUhalfEdge MakeEdge(GLUhalfEdge gLUhalfEdge) {
        GLUhalfEdge gLUhalfEdge2 = new GLUhalfEdge(true);
        GLUhalfEdge gLUhalfEdge3 = new GLUhalfEdge(false);
        if (!gLUhalfEdge.first) {
            gLUhalfEdge = gLUhalfEdge.Sym;
        }
        GLUhalfEdge gLUhalfEdge4 = gLUhalfEdge.Sym.next;
        gLUhalfEdge3.next = gLUhalfEdge4;
        gLUhalfEdge4.Sym.next = gLUhalfEdge2;
        gLUhalfEdge2.next = gLUhalfEdge;
        gLUhalfEdge.Sym.next = gLUhalfEdge3;
        gLUhalfEdge2.Sym = gLUhalfEdge3;
        gLUhalfEdge2.Onext = gLUhalfEdge2;
        gLUhalfEdge2.Lnext = gLUhalfEdge3;
        gLUhalfEdge2.Org = null;
        gLUhalfEdge2.Lface = null;
        gLUhalfEdge2.winding = 0;
        gLUhalfEdge2.activeRegion = null;
        gLUhalfEdge3.Sym = gLUhalfEdge2;
        gLUhalfEdge3.Onext = gLUhalfEdge3;
        gLUhalfEdge3.Lnext = gLUhalfEdge2;
        gLUhalfEdge3.Org = null;
        gLUhalfEdge3.Lface = null;
        gLUhalfEdge3.winding = 0;
        gLUhalfEdge3.activeRegion = null;
        return gLUhalfEdge2;
    }

    static void MakeFace(GLUface gLUface, GLUhalfEdge gLUhalfEdge, GLUface gLUface2) {
        if ($assertionsDisabled || gLUface != null) {
            GLUface gLUface3 = gLUface2.prev;
            gLUface.prev = gLUface3;
            gLUface3.next = gLUface;
            gLUface.next = gLUface2;
            gLUface2.prev = gLUface;
            gLUface.anEdge = gLUhalfEdge;
            gLUface.data = null;
            gLUface.trail = null;
            gLUface.marked = false;
            gLUface.inside = gLUface2.inside;
            GLUhalfEdge gLUhalfEdge2 = gLUhalfEdge;
            do {
                gLUhalfEdge2.Lface = gLUface;
                gLUhalfEdge2 = gLUhalfEdge2.Lnext;
            } while (gLUhalfEdge2 != gLUhalfEdge);
            return;
        }
        throw new AssertionError();
    }

    static void MakeVertex(GLUvertex gLUvertex, GLUhalfEdge gLUhalfEdge, GLUvertex gLUvertex2) {
        if ($assertionsDisabled || gLUvertex != null) {
            GLUvertex gLUvertex3 = gLUvertex2.prev;
            gLUvertex.prev = gLUvertex3;
            gLUvertex3.next = gLUvertex;
            gLUvertex.next = gLUvertex2;
            gLUvertex2.prev = gLUvertex;
            gLUvertex.anEdge = gLUhalfEdge;
            gLUvertex.data = null;
            GLUhalfEdge gLUhalfEdge2 = gLUhalfEdge;
            do {
                gLUhalfEdge2.Org = gLUvertex;
                gLUhalfEdge2 = gLUhalfEdge2.Onext;
            } while (gLUhalfEdge2 != gLUhalfEdge);
            return;
        }
        throw new AssertionError();
    }

    static void Splice(GLUhalfEdge gLUhalfEdge, GLUhalfEdge gLUhalfEdge2) {
        GLUhalfEdge gLUhalfEdge3 = gLUhalfEdge.Onext;
        GLUhalfEdge gLUhalfEdge4 = gLUhalfEdge2.Onext;
        gLUhalfEdge3.Sym.Lnext = gLUhalfEdge2;
        gLUhalfEdge4.Sym.Lnext = gLUhalfEdge;
        gLUhalfEdge.Onext = gLUhalfEdge4;
        gLUhalfEdge2.Onext = gLUhalfEdge3;
    }

    static GLUhalfEdge __gl_meshAddEdgeVertex(GLUhalfEdge gLUhalfEdge) {
        GLUhalfEdge MakeEdge = MakeEdge(gLUhalfEdge);
        GLUhalfEdge gLUhalfEdge2 = MakeEdge.Sym;
        Splice(MakeEdge, gLUhalfEdge.Lnext);
        MakeEdge.Org = gLUhalfEdge.Sym.Org;
        MakeVertex(new GLUvertex(), gLUhalfEdge2, MakeEdge.Org);
        GLUface gLUface = gLUhalfEdge.Lface;
        gLUhalfEdge2.Lface = gLUface;
        MakeEdge.Lface = gLUface;
        return MakeEdge;
    }

    public static void __gl_meshCheckMesh(GLUmesh gLUmesh) {
        GLUface gLUface = gLUmesh.fHead;
        GLUvertex gLUvertex = gLUmesh.vHead;
        GLUhalfEdge gLUhalfEdge = gLUmesh.eHead;
        GLUface gLUface2 = gLUface;
        while (true) {
            GLUface gLUface3 = gLUface2.next;
            if (gLUface3 != gLUface) {
                if ($assertionsDisabled || gLUface3.prev == gLUface2) {
                    GLUhalfEdge gLUhalfEdge2 = gLUface3.anEdge;
                    do {
                        if (!$assertionsDisabled && gLUhalfEdge2.Sym == gLUhalfEdge2) {
                            throw new AssertionError();
                        } else if (!$assertionsDisabled && gLUhalfEdge2.Sym.Sym != gLUhalfEdge2) {
                            throw new AssertionError();
                        } else if (!$assertionsDisabled && gLUhalfEdge2.Lnext.Onext.Sym != gLUhalfEdge2) {
                            throw new AssertionError();
                        } else if (!$assertionsDisabled && gLUhalfEdge2.Onext.Sym.Lnext != gLUhalfEdge2) {
                            throw new AssertionError();
                        } else if ($assertionsDisabled || gLUhalfEdge2.Lface == gLUface3) {
                            gLUhalfEdge2 = gLUhalfEdge2.Lnext;
                        } else {
                            throw new AssertionError();
                        }
                    } while (gLUhalfEdge2 != gLUface3.anEdge);
                    gLUface2 = gLUface3;
                } else {
                    throw new AssertionError();
                }
            } else if ($assertionsDisabled || (gLUface3.prev == gLUface2 && gLUface3.anEdge == null && gLUface3.data == null)) {
                GLUvertex gLUvertex2 = gLUvertex;
                while (true) {
                    GLUvertex gLUvertex3 = gLUvertex2.next;
                    if (gLUvertex3 != gLUvertex) {
                        if ($assertionsDisabled || gLUvertex3.prev == gLUvertex2) {
                            GLUhalfEdge gLUhalfEdge3 = gLUvertex3.anEdge;
                            do {
                                if (!$assertionsDisabled && gLUhalfEdge3.Sym == gLUhalfEdge3) {
                                    throw new AssertionError();
                                } else if (!$assertionsDisabled && gLUhalfEdge3.Sym.Sym != gLUhalfEdge3) {
                                    throw new AssertionError();
                                } else if (!$assertionsDisabled && gLUhalfEdge3.Lnext.Onext.Sym != gLUhalfEdge3) {
                                    throw new AssertionError();
                                } else if (!$assertionsDisabled && gLUhalfEdge3.Onext.Sym.Lnext != gLUhalfEdge3) {
                                    throw new AssertionError();
                                } else if ($assertionsDisabled || gLUhalfEdge3.Org == gLUvertex3) {
                                    gLUhalfEdge3 = gLUhalfEdge3.Onext;
                                } else {
                                    throw new AssertionError();
                                }
                            } while (gLUhalfEdge3 != gLUvertex3.anEdge);
                            gLUvertex2 = gLUvertex3;
                        } else {
                            throw new AssertionError();
                        }
                    } else if ($assertionsDisabled || (gLUvertex3.prev == gLUvertex2 && gLUvertex3.anEdge == null && gLUvertex3.data == null)) {
                        GLUhalfEdge gLUhalfEdge4 = gLUhalfEdge;
                        while (true) {
                            GLUhalfEdge gLUhalfEdge5 = gLUhalfEdge4.next;
                            if (gLUhalfEdge5 != gLUhalfEdge) {
                                if (!$assertionsDisabled && gLUhalfEdge5.Sym.next != gLUhalfEdge4.Sym) {
                                    throw new AssertionError();
                                } else if (!$assertionsDisabled && gLUhalfEdge5.Sym == gLUhalfEdge5) {
                                    throw new AssertionError();
                                } else if (!$assertionsDisabled && gLUhalfEdge5.Sym.Sym != gLUhalfEdge5) {
                                    throw new AssertionError();
                                } else if (!$assertionsDisabled && gLUhalfEdge5.Org == null) {
                                    throw new AssertionError();
                                } else if (!$assertionsDisabled && gLUhalfEdge5.Sym.Org == null) {
                                    throw new AssertionError();
                                } else if (!$assertionsDisabled && gLUhalfEdge5.Lnext.Onext.Sym != gLUhalfEdge5) {
                                    throw new AssertionError();
                                } else if ($assertionsDisabled || gLUhalfEdge5.Onext.Sym.Lnext == gLUhalfEdge5) {
                                    gLUhalfEdge4 = gLUhalfEdge5;
                                } else {
                                    throw new AssertionError();
                                }
                            } else if ($assertionsDisabled) {
                                return;
                            } else {
                                if (gLUhalfEdge5.Sym.next != gLUhalfEdge4.Sym || gLUhalfEdge5.Sym != gLUmesh.eHeadSym || gLUhalfEdge5.Sym.Sym != gLUhalfEdge5 || gLUhalfEdge5.Org != null || gLUhalfEdge5.Sym.Org != null || gLUhalfEdge5.Lface != null || gLUhalfEdge5.Sym.Lface != null) {
                                    throw new AssertionError();
                                }
                                return;
                            }
                        }
                    } else {
                        throw new AssertionError();
                    }
                }
            } else {
                throw new AssertionError();
            }
        }
    }

    static GLUhalfEdge __gl_meshConnect(GLUhalfEdge gLUhalfEdge, GLUhalfEdge gLUhalfEdge2) {
        boolean z = false;
        GLUhalfEdge MakeEdge = MakeEdge(gLUhalfEdge);
        GLUhalfEdge gLUhalfEdge3 = MakeEdge.Sym;
        if (gLUhalfEdge2.Lface != gLUhalfEdge.Lface) {
            z = true;
            KillFace(gLUhalfEdge2.Lface, gLUhalfEdge.Lface);
        }
        Splice(MakeEdge, gLUhalfEdge.Lnext);
        Splice(gLUhalfEdge3, gLUhalfEdge2);
        MakeEdge.Org = gLUhalfEdge.Sym.Org;
        gLUhalfEdge3.Org = gLUhalfEdge2.Org;
        GLUface gLUface = gLUhalfEdge.Lface;
        gLUhalfEdge3.Lface = gLUface;
        MakeEdge.Lface = gLUface;
        gLUhalfEdge.Lface.anEdge = gLUhalfEdge3;
        if (!z) {
            MakeFace(new GLUface(), MakeEdge, gLUhalfEdge.Lface);
        }
        return MakeEdge;
    }

    static boolean __gl_meshDelete(GLUhalfEdge gLUhalfEdge) {
        GLUhalfEdge gLUhalfEdge2 = gLUhalfEdge.Sym;
        boolean z = false;
        if (gLUhalfEdge.Lface != gLUhalfEdge.Sym.Lface) {
            KillFace(gLUhalfEdge.Lface, gLUhalfEdge.Sym.Lface);
            z = true;
        }
        if (gLUhalfEdge.Onext == gLUhalfEdge) {
            KillVertex(gLUhalfEdge.Org, (GLUvertex) null);
        } else {
            gLUhalfEdge.Sym.Lface.anEdge = gLUhalfEdge.Sym.Lnext;
            gLUhalfEdge.Org.anEdge = gLUhalfEdge.Onext;
            Splice(gLUhalfEdge, gLUhalfEdge.Sym.Lnext);
            if (!z) {
                MakeFace(new GLUface(), gLUhalfEdge, gLUhalfEdge.Lface);
            }
        }
        if (gLUhalfEdge2.Onext == gLUhalfEdge2) {
            KillVertex(gLUhalfEdge2.Org, (GLUvertex) null);
            KillFace(gLUhalfEdge2.Lface, (GLUface) null);
        } else {
            gLUhalfEdge.Lface.anEdge = gLUhalfEdge2.Sym.Lnext;
            gLUhalfEdge2.Org.anEdge = gLUhalfEdge2.Onext;
            Splice(gLUhalfEdge2, gLUhalfEdge2.Sym.Lnext);
        }
        KillEdge(gLUhalfEdge);
        return true;
    }

    public static void __gl_meshDeleteMesh(GLUmesh gLUmesh) {
        for (GLUface gLUface = gLUmesh.fHead.next; gLUface != gLUmesh.fHead; gLUface = gLUface.next) {
        }
        for (GLUvertex gLUvertex = gLUmesh.vHead.next; gLUvertex != gLUmesh.vHead; gLUvertex = gLUvertex.next) {
        }
        for (GLUhalfEdge gLUhalfEdge = gLUmesh.eHead.next; gLUhalfEdge != gLUmesh.eHead; gLUhalfEdge = gLUhalfEdge.next) {
        }
    }

    static void __gl_meshDeleteMeshZap(GLUmesh gLUmesh) {
        GLUface gLUface = gLUmesh.fHead;
        while (gLUface.next != gLUface) {
            __gl_meshZapFace(gLUface.next);
        }
        if (!$assertionsDisabled && gLUmesh.vHead.next != gLUmesh.vHead) {
            throw new AssertionError();
        }
    }

    public static GLUhalfEdge __gl_meshMakeEdge(GLUmesh gLUmesh) {
        GLUvertex gLUvertex = new GLUvertex();
        GLUvertex gLUvertex2 = new GLUvertex();
        GLUface gLUface = new GLUface();
        GLUhalfEdge MakeEdge = MakeEdge(gLUmesh.eHead);
        if (MakeEdge == null) {
            return null;
        }
        MakeVertex(gLUvertex, MakeEdge, gLUmesh.vHead);
        MakeVertex(gLUvertex2, MakeEdge.Sym, gLUmesh.vHead);
        MakeFace(gLUface, MakeEdge, gLUmesh.fHead);
        return MakeEdge;
    }

    public static GLUmesh __gl_meshNewMesh() {
        GLUmesh gLUmesh = new GLUmesh();
        GLUvertex gLUvertex = gLUmesh.vHead;
        GLUface gLUface = gLUmesh.fHead;
        GLUhalfEdge gLUhalfEdge = gLUmesh.eHead;
        GLUhalfEdge gLUhalfEdge2 = gLUmesh.eHeadSym;
        gLUvertex.prev = gLUvertex;
        gLUvertex.next = gLUvertex;
        gLUvertex.anEdge = null;
        gLUvertex.data = null;
        gLUface.prev = gLUface;
        gLUface.next = gLUface;
        gLUface.anEdge = null;
        gLUface.data = null;
        gLUface.trail = null;
        gLUface.marked = false;
        gLUface.inside = false;
        gLUhalfEdge.next = gLUhalfEdge;
        gLUhalfEdge.Sym = gLUhalfEdge2;
        gLUhalfEdge.Onext = null;
        gLUhalfEdge.Lnext = null;
        gLUhalfEdge.Org = null;
        gLUhalfEdge.Lface = null;
        gLUhalfEdge.winding = 0;
        gLUhalfEdge.activeRegion = null;
        gLUhalfEdge2.next = gLUhalfEdge2;
        gLUhalfEdge2.Sym = gLUhalfEdge;
        gLUhalfEdge2.Onext = null;
        gLUhalfEdge2.Lnext = null;
        gLUhalfEdge2.Org = null;
        gLUhalfEdge2.Lface = null;
        gLUhalfEdge2.winding = 0;
        gLUhalfEdge2.activeRegion = null;
        return gLUmesh;
    }

    public static boolean __gl_meshSplice(GLUhalfEdge gLUhalfEdge, GLUhalfEdge gLUhalfEdge2) {
        boolean z;
        boolean z2 = false;
        if (gLUhalfEdge != gLUhalfEdge2) {
            if (gLUhalfEdge2.Org != gLUhalfEdge.Org) {
                KillVertex(gLUhalfEdge2.Org, gLUhalfEdge.Org);
                z = true;
            } else {
                z = false;
            }
            if (gLUhalfEdge2.Lface != gLUhalfEdge.Lface) {
                KillFace(gLUhalfEdge2.Lface, gLUhalfEdge.Lface);
                z2 = true;
            }
            Splice(gLUhalfEdge2, gLUhalfEdge);
            if (!z) {
                MakeVertex(new GLUvertex(), gLUhalfEdge2, gLUhalfEdge.Org);
                gLUhalfEdge.Org.anEdge = gLUhalfEdge;
            }
            if (!z2) {
                MakeFace(new GLUface(), gLUhalfEdge2, gLUhalfEdge.Lface);
                gLUhalfEdge.Lface.anEdge = gLUhalfEdge;
            }
        }
        return true;
    }

    public static GLUhalfEdge __gl_meshSplitEdge(GLUhalfEdge gLUhalfEdge) {
        GLUhalfEdge gLUhalfEdge2 = __gl_meshAddEdgeVertex(gLUhalfEdge).Sym;
        Splice(gLUhalfEdge.Sym, gLUhalfEdge.Sym.Sym.Lnext);
        Splice(gLUhalfEdge.Sym, gLUhalfEdge2);
        gLUhalfEdge.Sym.Org = gLUhalfEdge2.Org;
        gLUhalfEdge2.Sym.Org.anEdge = gLUhalfEdge2.Sym;
        gLUhalfEdge2.Sym.Lface = gLUhalfEdge.Sym.Lface;
        gLUhalfEdge2.winding = gLUhalfEdge.winding;
        gLUhalfEdge2.Sym.winding = gLUhalfEdge.Sym.winding;
        return gLUhalfEdge2;
    }

    static GLUmesh __gl_meshUnion(GLUmesh gLUmesh, GLUmesh gLUmesh2) {
        GLUface gLUface = gLUmesh.fHead;
        GLUvertex gLUvertex = gLUmesh.vHead;
        GLUhalfEdge gLUhalfEdge = gLUmesh.eHead;
        GLUface gLUface2 = gLUmesh2.fHead;
        GLUvertex gLUvertex2 = gLUmesh2.vHead;
        GLUhalfEdge gLUhalfEdge2 = gLUmesh2.eHead;
        if (gLUface2.next != gLUface2) {
            gLUface.prev.next = gLUface2.next;
            gLUface2.next.prev = gLUface.prev;
            gLUface2.prev.next = gLUface;
            gLUface.prev = gLUface2.prev;
        }
        if (gLUvertex2.next != gLUvertex2) {
            gLUvertex.prev.next = gLUvertex2.next;
            gLUvertex2.next.prev = gLUvertex.prev;
            gLUvertex2.prev.next = gLUvertex;
            gLUvertex.prev = gLUvertex2.prev;
        }
        if (gLUhalfEdge2.next != gLUhalfEdge2) {
            gLUhalfEdge.Sym.next.Sym.next = gLUhalfEdge2.next;
            gLUhalfEdge2.next.Sym.next = gLUhalfEdge.Sym.next;
            gLUhalfEdge2.Sym.next.Sym.next = gLUhalfEdge;
            gLUhalfEdge.Sym.next = gLUhalfEdge2.Sym.next;
        }
        return gLUmesh;
    }

    static void __gl_meshZapFace(GLUface gLUface) {
        GLUhalfEdge gLUhalfEdge = gLUface.anEdge;
        GLUhalfEdge gLUhalfEdge2 = gLUhalfEdge.Lnext;
        while (true) {
            GLUhalfEdge gLUhalfEdge3 = gLUhalfEdge2.Lnext;
            gLUhalfEdge2.Lface = null;
            if (gLUhalfEdge2.Sym.Lface == null) {
                if (gLUhalfEdge2.Onext == gLUhalfEdge2) {
                    KillVertex(gLUhalfEdge2.Org, (GLUvertex) null);
                } else {
                    gLUhalfEdge2.Org.anEdge = gLUhalfEdge2.Onext;
                    Splice(gLUhalfEdge2, gLUhalfEdge2.Sym.Lnext);
                }
                GLUhalfEdge gLUhalfEdge4 = gLUhalfEdge2.Sym;
                if (gLUhalfEdge4.Onext == gLUhalfEdge4) {
                    KillVertex(gLUhalfEdge4.Org, (GLUvertex) null);
                } else {
                    gLUhalfEdge4.Org.anEdge = gLUhalfEdge4.Onext;
                    Splice(gLUhalfEdge4, gLUhalfEdge4.Sym.Lnext);
                }
                KillEdge(gLUhalfEdge2);
            }
            if (gLUhalfEdge2 == gLUhalfEdge) {
                GLUface gLUface2 = gLUface.prev;
                GLUface gLUface3 = gLUface.next;
                gLUface3.prev = gLUface2;
                gLUface2.next = gLUface3;
                return;
            }
            gLUhalfEdge2 = gLUhalfEdge3;
        }
    }
}
