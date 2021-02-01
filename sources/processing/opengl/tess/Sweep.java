package processing.opengl.tess;

import processing.opengl.tess.Dict;
import processing.opengl.tess.PriorityQ;

class Sweep {
    static final /* synthetic */ boolean $assertionsDisabled = (!Sweep.class.desiredAssertionStatus());
    private static final double SENTINEL_COORD = 4.0E150d;
    private static final boolean TOLERANCE_NONZERO = false;

    private Sweep() {
    }

    static ActiveRegion AddRegionBelow(GLUtessellatorImpl gLUtessellatorImpl, ActiveRegion activeRegion, GLUhalfEdge gLUhalfEdge) {
        ActiveRegion activeRegion2 = new ActiveRegion();
        if (activeRegion2 == null) {
            throw new RuntimeException();
        }
        activeRegion2.eUp = gLUhalfEdge;
        activeRegion2.nodeUp = Dict.dictInsertBefore(gLUtessellatorImpl.dict, activeRegion.nodeUp, activeRegion2);
        if (activeRegion2.nodeUp == null) {
            throw new RuntimeException();
        }
        activeRegion2.fixUpperEdge = false;
        activeRegion2.sentinel = false;
        activeRegion2.dirty = false;
        gLUhalfEdge.activeRegion = activeRegion2;
        return activeRegion2;
    }

    static void AddRightEdges(GLUtessellatorImpl gLUtessellatorImpl, ActiveRegion activeRegion, GLUhalfEdge gLUhalfEdge, GLUhalfEdge gLUhalfEdge2, GLUhalfEdge gLUhalfEdge3, boolean z) {
        do {
            if ($assertionsDisabled || Geom.VertLeq(gLUhalfEdge.Org, gLUhalfEdge.Sym.Org)) {
                AddRegionBelow(gLUtessellatorImpl, activeRegion, gLUhalfEdge.Sym);
                gLUhalfEdge = gLUhalfEdge.Onext;
            } else {
                throw new AssertionError();
            }
        } while (gLUhalfEdge != gLUhalfEdge2);
        if (gLUhalfEdge3 == null) {
            gLUhalfEdge3 = RegionBelow(activeRegion).eUp.Sym.Onext;
        }
        boolean z2 = true;
        while (true) {
            ActiveRegion RegionBelow = RegionBelow(activeRegion);
            GLUhalfEdge gLUhalfEdge4 = RegionBelow.eUp.Sym;
            if (gLUhalfEdge4.Org != gLUhalfEdge3.Org) {
                activeRegion.dirty = true;
                if (!$assertionsDisabled && activeRegion.windingNumber - gLUhalfEdge4.winding != RegionBelow.windingNumber) {
                    throw new AssertionError();
                } else if (z) {
                    WalkDirtyRegions(gLUtessellatorImpl, activeRegion);
                    return;
                } else {
                    return;
                }
            } else {
                if (gLUhalfEdge4.Onext != gLUhalfEdge3) {
                    if (!Mesh.__gl_meshSplice(gLUhalfEdge4.Sym.Lnext, gLUhalfEdge4)) {
                        throw new RuntimeException();
                    } else if (!Mesh.__gl_meshSplice(gLUhalfEdge3.Sym.Lnext, gLUhalfEdge4)) {
                        throw new RuntimeException();
                    }
                }
                RegionBelow.windingNumber = activeRegion.windingNumber - gLUhalfEdge4.winding;
                RegionBelow.inside = IsWindingInside(gLUtessellatorImpl, RegionBelow.windingNumber);
                activeRegion.dirty = true;
                if (!z2 && CheckForRightSplice(gLUtessellatorImpl, activeRegion)) {
                    AddWinding(gLUhalfEdge4, gLUhalfEdge3);
                    DeleteRegion(gLUtessellatorImpl, activeRegion);
                    if (!Mesh.__gl_meshDelete(gLUhalfEdge3)) {
                        throw new RuntimeException();
                    }
                }
                z2 = false;
                gLUhalfEdge3 = gLUhalfEdge4;
                activeRegion = RegionBelow;
            }
        }
    }

    static void AddSentinel(GLUtessellatorImpl gLUtessellatorImpl, double d) {
        ActiveRegion activeRegion = new ActiveRegion();
        if (activeRegion == null) {
            throw new RuntimeException();
        }
        GLUhalfEdge __gl_meshMakeEdge = Mesh.__gl_meshMakeEdge(gLUtessellatorImpl.mesh);
        if (__gl_meshMakeEdge == null) {
            throw new RuntimeException();
        }
        __gl_meshMakeEdge.Org.s = SENTINEL_COORD;
        __gl_meshMakeEdge.Org.t = d;
        __gl_meshMakeEdge.Sym.Org.s = -4.0E150d;
        __gl_meshMakeEdge.Sym.Org.t = d;
        gLUtessellatorImpl.event = __gl_meshMakeEdge.Sym.Org;
        activeRegion.eUp = __gl_meshMakeEdge;
        activeRegion.windingNumber = 0;
        activeRegion.inside = false;
        activeRegion.fixUpperEdge = false;
        activeRegion.sentinel = true;
        activeRegion.dirty = false;
        activeRegion.nodeUp = Dict.dictInsert(gLUtessellatorImpl.dict, activeRegion);
        if (activeRegion.nodeUp == null) {
            throw new RuntimeException();
        }
    }

    private static void AddWinding(GLUhalfEdge gLUhalfEdge, GLUhalfEdge gLUhalfEdge2) {
        gLUhalfEdge.winding += gLUhalfEdge2.winding;
        gLUhalfEdge.Sym.winding += gLUhalfEdge2.Sym.winding;
    }

    static void CallCombine(GLUtessellatorImpl gLUtessellatorImpl, GLUvertex gLUvertex, Object[] objArr, float[] fArr, boolean z) {
        Object[] objArr2 = new Object[1];
        gLUtessellatorImpl.callCombineOrCombineData(new double[]{gLUvertex.coords[0], gLUvertex.coords[1], gLUvertex.coords[2]}, objArr, fArr, objArr2);
        gLUvertex.data = objArr2[0];
        if (gLUvertex.data != null) {
            return;
        }
        if (!z) {
            gLUvertex.data = objArr[0];
        } else if (!gLUtessellatorImpl.fatalError) {
            gLUtessellatorImpl.callErrorOrErrorData(100156);
            gLUtessellatorImpl.fatalError = true;
        }
    }

    static boolean CheckForIntersect(GLUtessellatorImpl gLUtessellatorImpl, ActiveRegion activeRegion) {
        ActiveRegion RegionBelow = RegionBelow(activeRegion);
        GLUhalfEdge gLUhalfEdge = activeRegion.eUp;
        GLUhalfEdge gLUhalfEdge2 = RegionBelow.eUp;
        GLUvertex gLUvertex = gLUhalfEdge.Org;
        GLUvertex gLUvertex2 = gLUhalfEdge2.Org;
        GLUvertex gLUvertex3 = gLUhalfEdge.Sym.Org;
        GLUvertex gLUvertex4 = gLUhalfEdge2.Sym.Org;
        GLUvertex gLUvertex5 = new GLUvertex();
        if (!$assertionsDisabled && Geom.VertEq(gLUvertex4, gLUvertex3)) {
            throw new AssertionError();
        } else if (!$assertionsDisabled && Geom.EdgeSign(gLUvertex3, gLUtessellatorImpl.event, gLUvertex) > 0.0d) {
            throw new AssertionError();
        } else if (!$assertionsDisabled && Geom.EdgeSign(gLUvertex4, gLUtessellatorImpl.event, gLUvertex2) < 0.0d) {
            throw new AssertionError();
        } else if (!$assertionsDisabled && (gLUvertex == gLUtessellatorImpl.event || gLUvertex2 == gLUtessellatorImpl.event)) {
            throw new AssertionError();
        } else if (!$assertionsDisabled && (activeRegion.fixUpperEdge || RegionBelow.fixUpperEdge)) {
            throw new AssertionError();
        } else if (gLUvertex == gLUvertex2 || Math.min(gLUvertex.t, gLUvertex3.t) > Math.max(gLUvertex2.t, gLUvertex4.t)) {
            return false;
        } else {
            if (Geom.VertLeq(gLUvertex, gLUvertex2)) {
                if (Geom.EdgeSign(gLUvertex4, gLUvertex, gLUvertex2) > 0.0d) {
                    return false;
                }
            } else if (Geom.EdgeSign(gLUvertex3, gLUvertex2, gLUvertex) < 0.0d) {
                return false;
            }
            DebugEvent(gLUtessellatorImpl);
            Geom.EdgeIntersect(gLUvertex3, gLUvertex, gLUvertex4, gLUvertex2, gLUvertex5);
            if (!$assertionsDisabled && Math.min(gLUvertex.t, gLUvertex3.t) > gLUvertex5.t) {
                throw new AssertionError();
            } else if (!$assertionsDisabled && gLUvertex5.t > Math.max(gLUvertex2.t, gLUvertex4.t)) {
                throw new AssertionError();
            } else if (!$assertionsDisabled && Math.min(gLUvertex4.s, gLUvertex3.s) > gLUvertex5.s) {
                throw new AssertionError();
            } else if ($assertionsDisabled || gLUvertex5.s <= Math.max(gLUvertex2.s, gLUvertex.s)) {
                if (Geom.VertLeq(gLUvertex5, gLUtessellatorImpl.event)) {
                    gLUvertex5.s = gLUtessellatorImpl.event.s;
                    gLUvertex5.t = gLUtessellatorImpl.event.t;
                }
                GLUvertex gLUvertex6 = Geom.VertLeq(gLUvertex, gLUvertex2) ? gLUvertex : gLUvertex2;
                if (Geom.VertLeq(gLUvertex6, gLUvertex5)) {
                    gLUvertex5.s = gLUvertex6.s;
                    gLUvertex5.t = gLUvertex6.t;
                }
                if (Geom.VertEq(gLUvertex5, gLUvertex) || Geom.VertEq(gLUvertex5, gLUvertex2)) {
                    CheckForRightSplice(gLUtessellatorImpl, activeRegion);
                    return false;
                } else if ((Geom.VertEq(gLUvertex3, gLUtessellatorImpl.event) || Geom.EdgeSign(gLUvertex3, gLUtessellatorImpl.event, gLUvertex5) < 0.0d) && (Geom.VertEq(gLUvertex4, gLUtessellatorImpl.event) || Geom.EdgeSign(gLUvertex4, gLUtessellatorImpl.event, gLUvertex5) > 0.0d)) {
                    if (Mesh.__gl_meshSplitEdge(gLUhalfEdge.Sym) == null) {
                        throw new RuntimeException();
                    } else if (Mesh.__gl_meshSplitEdge(gLUhalfEdge2.Sym) == null) {
                        throw new RuntimeException();
                    } else if (!Mesh.__gl_meshSplice(gLUhalfEdge2.Sym.Lnext, gLUhalfEdge)) {
                        throw new RuntimeException();
                    } else {
                        gLUhalfEdge.Org.s = gLUvertex5.s;
                        gLUhalfEdge.Org.t = gLUvertex5.t;
                        gLUhalfEdge.Org.pqHandle = gLUtessellatorImpl.pq.pqInsert(gLUhalfEdge.Org);
                        if (((long) gLUhalfEdge.Org.pqHandle) == Long.MAX_VALUE) {
                            gLUtessellatorImpl.pq.pqDeletePriorityQ();
                            gLUtessellatorImpl.pq = null;
                            throw new RuntimeException();
                        }
                        GetIntersectData(gLUtessellatorImpl, gLUhalfEdge.Org, gLUvertex, gLUvertex3, gLUvertex2, gLUvertex4);
                        ActiveRegion RegionAbove = RegionAbove(activeRegion);
                        RegionBelow.dirty = true;
                        activeRegion.dirty = true;
                        RegionAbove.dirty = true;
                        return false;
                    }
                } else if (gLUvertex4 == gLUtessellatorImpl.event) {
                    if (Mesh.__gl_meshSplitEdge(gLUhalfEdge.Sym) == null) {
                        throw new RuntimeException();
                    } else if (!Mesh.__gl_meshSplice(gLUhalfEdge2.Sym, gLUhalfEdge)) {
                        throw new RuntimeException();
                    } else {
                        ActiveRegion TopLeftRegion = TopLeftRegion(activeRegion);
                        if (TopLeftRegion == null) {
                            throw new RuntimeException();
                        }
                        GLUhalfEdge gLUhalfEdge3 = RegionBelow(TopLeftRegion).eUp;
                        FinishLeftRegions(gLUtessellatorImpl, RegionBelow(TopLeftRegion), RegionBelow);
                        AddRightEdges(gLUtessellatorImpl, TopLeftRegion, gLUhalfEdge3.Sym.Lnext, gLUhalfEdge3, gLUhalfEdge3, true);
                        return true;
                    }
                } else if (gLUvertex3 != gLUtessellatorImpl.event) {
                    if (Geom.EdgeSign(gLUvertex3, gLUtessellatorImpl.event, gLUvertex5) >= 0.0d) {
                        ActiveRegion RegionAbove2 = RegionAbove(activeRegion);
                        activeRegion.dirty = true;
                        RegionAbove2.dirty = true;
                        if (Mesh.__gl_meshSplitEdge(gLUhalfEdge.Sym) == null) {
                            throw new RuntimeException();
                        }
                        gLUhalfEdge.Org.s = gLUtessellatorImpl.event.s;
                        gLUhalfEdge.Org.t = gLUtessellatorImpl.event.t;
                    }
                    if (Geom.EdgeSign(gLUvertex4, gLUtessellatorImpl.event, gLUvertex5) <= 0.0d) {
                        RegionBelow.dirty = true;
                        activeRegion.dirty = true;
                        if (Mesh.__gl_meshSplitEdge(gLUhalfEdge2.Sym) == null) {
                            throw new RuntimeException();
                        }
                        gLUhalfEdge2.Org.s = gLUtessellatorImpl.event.s;
                        gLUhalfEdge2.Org.t = gLUtessellatorImpl.event.t;
                    }
                    return false;
                } else if (Mesh.__gl_meshSplitEdge(gLUhalfEdge2.Sym) == null) {
                    throw new RuntimeException();
                } else if (!Mesh.__gl_meshSplice(gLUhalfEdge.Lnext, gLUhalfEdge2.Sym.Lnext)) {
                    throw new RuntimeException();
                } else {
                    ActiveRegion TopRightRegion = TopRightRegion(activeRegion);
                    GLUhalfEdge gLUhalfEdge4 = RegionBelow(TopRightRegion).eUp.Sym.Onext;
                    activeRegion.eUp = gLUhalfEdge2.Sym.Lnext;
                    AddRightEdges(gLUtessellatorImpl, TopRightRegion, FinishLeftRegions(gLUtessellatorImpl, activeRegion, (ActiveRegion) null).Onext, gLUhalfEdge.Sym.Onext, gLUhalfEdge4, true);
                    return true;
                }
            } else {
                throw new AssertionError();
            }
        }
    }

    static boolean CheckForLeftSplice(GLUtessellatorImpl gLUtessellatorImpl, ActiveRegion activeRegion) {
        ActiveRegion RegionBelow = RegionBelow(activeRegion);
        GLUhalfEdge gLUhalfEdge = activeRegion.eUp;
        GLUhalfEdge gLUhalfEdge2 = RegionBelow.eUp;
        if ($assertionsDisabled || !Geom.VertEq(gLUhalfEdge.Sym.Org, gLUhalfEdge2.Sym.Org)) {
            if (Geom.VertLeq(gLUhalfEdge.Sym.Org, gLUhalfEdge2.Sym.Org)) {
                if (Geom.EdgeSign(gLUhalfEdge.Sym.Org, gLUhalfEdge2.Sym.Org, gLUhalfEdge.Org) < 0.0d) {
                    return false;
                }
                ActiveRegion RegionAbove = RegionAbove(activeRegion);
                activeRegion.dirty = true;
                RegionAbove.dirty = true;
                GLUhalfEdge __gl_meshSplitEdge = Mesh.__gl_meshSplitEdge(gLUhalfEdge);
                if (__gl_meshSplitEdge == null) {
                    throw new RuntimeException();
                } else if (!Mesh.__gl_meshSplice(gLUhalfEdge2.Sym, __gl_meshSplitEdge)) {
                    throw new RuntimeException();
                } else {
                    __gl_meshSplitEdge.Lface.inside = activeRegion.inside;
                }
            } else if (Geom.EdgeSign(gLUhalfEdge2.Sym.Org, gLUhalfEdge.Sym.Org, gLUhalfEdge2.Org) > 0.0d) {
                return false;
            } else {
                RegionBelow.dirty = true;
                activeRegion.dirty = true;
                GLUhalfEdge __gl_meshSplitEdge2 = Mesh.__gl_meshSplitEdge(gLUhalfEdge2);
                if (__gl_meshSplitEdge2 == null) {
                    throw new RuntimeException();
                } else if (!Mesh.__gl_meshSplice(gLUhalfEdge.Lnext, gLUhalfEdge2.Sym)) {
                    throw new RuntimeException();
                } else {
                    __gl_meshSplitEdge2.Sym.Lface.inside = activeRegion.inside;
                }
            }
            return true;
        }
        throw new AssertionError();
    }

    static boolean CheckForRightSplice(GLUtessellatorImpl gLUtessellatorImpl, ActiveRegion activeRegion) {
        ActiveRegion RegionBelow = RegionBelow(activeRegion);
        GLUhalfEdge gLUhalfEdge = activeRegion.eUp;
        GLUhalfEdge gLUhalfEdge2 = RegionBelow.eUp;
        if (Geom.VertLeq(gLUhalfEdge.Org, gLUhalfEdge2.Org)) {
            if (Geom.EdgeSign(gLUhalfEdge2.Sym.Org, gLUhalfEdge.Org, gLUhalfEdge2.Org) > 0.0d) {
                return false;
            }
            if (!Geom.VertEq(gLUhalfEdge.Org, gLUhalfEdge2.Org)) {
                if (Mesh.__gl_meshSplitEdge(gLUhalfEdge2.Sym) == null) {
                    throw new RuntimeException();
                } else if (!Mesh.__gl_meshSplice(gLUhalfEdge, gLUhalfEdge2.Sym.Lnext)) {
                    throw new RuntimeException();
                } else {
                    RegionBelow.dirty = true;
                    activeRegion.dirty = true;
                }
            } else if (gLUhalfEdge.Org != gLUhalfEdge2.Org) {
                gLUtessellatorImpl.pq.pqDelete(gLUhalfEdge.Org.pqHandle);
                SpliceMergeVertices(gLUtessellatorImpl, gLUhalfEdge2.Sym.Lnext, gLUhalfEdge);
            }
        } else if (Geom.EdgeSign(gLUhalfEdge.Sym.Org, gLUhalfEdge2.Org, gLUhalfEdge.Org) < 0.0d) {
            return false;
        } else {
            ActiveRegion RegionAbove = RegionAbove(activeRegion);
            activeRegion.dirty = true;
            RegionAbove.dirty = true;
            if (Mesh.__gl_meshSplitEdge(gLUhalfEdge.Sym) == null) {
                throw new RuntimeException();
            } else if (!Mesh.__gl_meshSplice(gLUhalfEdge2.Sym.Lnext, gLUhalfEdge)) {
                throw new RuntimeException();
            }
        }
        return true;
    }

    static void ComputeWinding(GLUtessellatorImpl gLUtessellatorImpl, ActiveRegion activeRegion) {
        activeRegion.windingNumber = RegionAbove(activeRegion).windingNumber + activeRegion.eUp.winding;
        activeRegion.inside = IsWindingInside(gLUtessellatorImpl, activeRegion.windingNumber);
    }

    static void ConnectLeftDegenerate(GLUtessellatorImpl gLUtessellatorImpl, ActiveRegion activeRegion, GLUvertex gLUvertex) {
        GLUhalfEdge gLUhalfEdge = activeRegion.eUp;
        if (Geom.VertEq(gLUhalfEdge.Org, gLUvertex)) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
            SpliceMergeVertices(gLUtessellatorImpl, gLUhalfEdge, gLUvertex.anEdge);
        } else if (!Geom.VertEq(gLUhalfEdge.Sym.Org, gLUvertex)) {
            if (Mesh.__gl_meshSplitEdge(gLUhalfEdge.Sym) == null) {
                throw new RuntimeException();
            }
            if (activeRegion.fixUpperEdge) {
                if (!Mesh.__gl_meshDelete(gLUhalfEdge.Onext)) {
                    throw new RuntimeException();
                }
                activeRegion.fixUpperEdge = false;
            }
            if (!Mesh.__gl_meshSplice(gLUvertex.anEdge, gLUhalfEdge)) {
                throw new RuntimeException();
            }
            SweepEvent(gLUtessellatorImpl, gLUvertex);
        } else if (!$assertionsDisabled) {
            throw new AssertionError();
        } else {
            ActiveRegion TopRightRegion = TopRightRegion(activeRegion);
            ActiveRegion RegionBelow = RegionBelow(TopRightRegion);
            GLUhalfEdge gLUhalfEdge2 = RegionBelow.eUp.Sym;
            GLUhalfEdge gLUhalfEdge3 = gLUhalfEdge2.Onext;
            if (RegionBelow.fixUpperEdge) {
                if ($assertionsDisabled || gLUhalfEdge3 != gLUhalfEdge2) {
                    DeleteRegion(gLUtessellatorImpl, RegionBelow);
                    if (!Mesh.__gl_meshDelete(gLUhalfEdge2)) {
                        throw new RuntimeException();
                    }
                    gLUhalfEdge2 = gLUhalfEdge3.Sym.Lnext;
                } else {
                    throw new AssertionError();
                }
            }
            if (!Mesh.__gl_meshSplice(gLUvertex.anEdge, gLUhalfEdge2)) {
                throw new RuntimeException();
            }
            AddRightEdges(gLUtessellatorImpl, TopRightRegion, gLUhalfEdge2.Onext, gLUhalfEdge3, !Geom.EdgeGoesLeft(gLUhalfEdge3) ? null : gLUhalfEdge3, true);
        }
    }

    static void ConnectLeftVertex(GLUtessellatorImpl gLUtessellatorImpl, GLUvertex gLUvertex) {
        GLUhalfEdge gLUhalfEdge;
        ActiveRegion activeRegion = new ActiveRegion();
        activeRegion.eUp = gLUvertex.anEdge.Sym;
        ActiveRegion activeRegion2 = (ActiveRegion) Dict.dictKey(Dict.dictSearch(gLUtessellatorImpl.dict, activeRegion));
        ActiveRegion RegionBelow = RegionBelow(activeRegion2);
        GLUhalfEdge gLUhalfEdge2 = activeRegion2.eUp;
        GLUhalfEdge gLUhalfEdge3 = RegionBelow.eUp;
        if (Geom.EdgeSign(gLUhalfEdge2.Sym.Org, gLUvertex, gLUhalfEdge2.Org) == 0.0d) {
            ConnectLeftDegenerate(gLUtessellatorImpl, activeRegion2, gLUvertex);
            return;
        }
        if (Geom.VertLeq(gLUhalfEdge3.Sym.Org, gLUhalfEdge2.Sym.Org)) {
            RegionBelow = activeRegion2;
        }
        if (activeRegion2.inside || RegionBelow.fixUpperEdge) {
            if (RegionBelow == activeRegion2) {
                gLUhalfEdge = Mesh.__gl_meshConnect(gLUvertex.anEdge.Sym, gLUhalfEdge2.Lnext);
                if (gLUhalfEdge == null) {
                    throw new RuntimeException();
                }
            } else {
                GLUhalfEdge __gl_meshConnect = Mesh.__gl_meshConnect(gLUhalfEdge3.Sym.Onext.Sym, gLUvertex.anEdge);
                if (__gl_meshConnect == null) {
                    throw new RuntimeException();
                }
                gLUhalfEdge = __gl_meshConnect.Sym;
            }
            if (!RegionBelow.fixUpperEdge) {
                ComputeWinding(gLUtessellatorImpl, AddRegionBelow(gLUtessellatorImpl, activeRegion2, gLUhalfEdge));
            } else if (!FixUpperEdge(RegionBelow, gLUhalfEdge)) {
                throw new RuntimeException();
            }
            SweepEvent(gLUtessellatorImpl, gLUvertex);
            return;
        }
        AddRightEdges(gLUtessellatorImpl, activeRegion2, gLUvertex.anEdge, gLUvertex.anEdge, (GLUhalfEdge) null, true);
    }

    static void ConnectRightVertex(GLUtessellatorImpl gLUtessellatorImpl, ActiveRegion activeRegion, GLUhalfEdge gLUhalfEdge) {
        boolean z;
        ActiveRegion activeRegion2;
        GLUhalfEdge gLUhalfEdge2 = gLUhalfEdge.Onext;
        ActiveRegion RegionBelow = RegionBelow(activeRegion);
        GLUhalfEdge gLUhalfEdge3 = activeRegion.eUp;
        GLUhalfEdge gLUhalfEdge4 = RegionBelow.eUp;
        if (gLUhalfEdge3.Sym.Org != gLUhalfEdge4.Sym.Org) {
            CheckForIntersect(gLUtessellatorImpl, activeRegion);
        }
        if (!Geom.VertEq(gLUhalfEdge3.Org, gLUtessellatorImpl.event)) {
            z = false;
            activeRegion2 = activeRegion;
        } else if (!Mesh.__gl_meshSplice(gLUhalfEdge2.Sym.Lnext, gLUhalfEdge3)) {
            throw new RuntimeException();
        } else {
            activeRegion2 = TopLeftRegion(activeRegion);
            if (activeRegion2 == null) {
                throw new RuntimeException();
            }
            gLUhalfEdge2 = RegionBelow(activeRegion2).eUp;
            FinishLeftRegions(gLUtessellatorImpl, RegionBelow(activeRegion2), RegionBelow);
            z = true;
        }
        if (Geom.VertEq(gLUhalfEdge4.Org, gLUtessellatorImpl.event)) {
            if (!Mesh.__gl_meshSplice(gLUhalfEdge, gLUhalfEdge4.Sym.Lnext)) {
                throw new RuntimeException();
            }
            gLUhalfEdge = FinishLeftRegions(gLUtessellatorImpl, RegionBelow, (ActiveRegion) null);
            z = true;
        }
        if (z) {
            AddRightEdges(gLUtessellatorImpl, activeRegion2, gLUhalfEdge.Onext, gLUhalfEdge2, gLUhalfEdge2, true);
            return;
        }
        GLUhalfEdge __gl_meshConnect = Mesh.__gl_meshConnect(gLUhalfEdge.Onext.Sym, Geom.VertLeq(gLUhalfEdge4.Org, gLUhalfEdge3.Org) ? gLUhalfEdge4.Sym.Lnext : gLUhalfEdge3);
        if (__gl_meshConnect == null) {
            throw new RuntimeException();
        }
        AddRightEdges(gLUtessellatorImpl, activeRegion2, __gl_meshConnect, __gl_meshConnect.Onext, __gl_meshConnect.Onext, false);
        __gl_meshConnect.Sym.activeRegion.fixUpperEdge = true;
        WalkDirtyRegions(gLUtessellatorImpl, activeRegion2);
    }

    private static void DebugEvent(GLUtessellatorImpl gLUtessellatorImpl) {
    }

    static void DeleteRegion(GLUtessellatorImpl gLUtessellatorImpl, ActiveRegion activeRegion) {
        if (!activeRegion.fixUpperEdge || $assertionsDisabled || activeRegion.eUp.winding == 0) {
            activeRegion.eUp.activeRegion = null;
            Dict.dictDelete(gLUtessellatorImpl.dict, activeRegion.nodeUp);
            return;
        }
        throw new AssertionError();
    }

    static void DoneEdgeDict(GLUtessellatorImpl gLUtessellatorImpl) {
        int i = 0;
        while (true) {
            ActiveRegion activeRegion = (ActiveRegion) Dict.dictKey(Dict.dictMin(gLUtessellatorImpl.dict));
            if (activeRegion != null) {
                if (!activeRegion.sentinel) {
                    if (!$assertionsDisabled && !activeRegion.fixUpperEdge) {
                        throw new AssertionError();
                    } else if (!$assertionsDisabled && (i = i + 1) != 1) {
                        throw new AssertionError();
                    }
                }
                if ($assertionsDisabled || activeRegion.windingNumber == 0) {
                    DeleteRegion(gLUtessellatorImpl, activeRegion);
                } else {
                    throw new AssertionError();
                }
            } else {
                Dict.dictDeleteDict(gLUtessellatorImpl.dict);
                return;
            }
        }
    }

    static void DonePriorityQ(GLUtessellatorImpl gLUtessellatorImpl) {
        gLUtessellatorImpl.pq.pqDeletePriorityQ();
    }

    static boolean EdgeLeq(GLUtessellatorImpl gLUtessellatorImpl, ActiveRegion activeRegion, ActiveRegion activeRegion2) {
        GLUvertex gLUvertex = gLUtessellatorImpl.event;
        GLUhalfEdge gLUhalfEdge = activeRegion.eUp;
        GLUhalfEdge gLUhalfEdge2 = activeRegion2.eUp;
        return gLUhalfEdge.Sym.Org == gLUvertex ? gLUhalfEdge2.Sym.Org == gLUvertex ? Geom.VertLeq(gLUhalfEdge.Org, gLUhalfEdge2.Org) ? Geom.EdgeSign(gLUhalfEdge2.Sym.Org, gLUhalfEdge.Org, gLUhalfEdge2.Org) <= 0.0d : Geom.EdgeSign(gLUhalfEdge.Sym.Org, gLUhalfEdge2.Org, gLUhalfEdge.Org) >= 0.0d : Geom.EdgeSign(gLUhalfEdge2.Sym.Org, gLUvertex, gLUhalfEdge2.Org) <= 0.0d : gLUhalfEdge2.Sym.Org == gLUvertex ? Geom.EdgeSign(gLUhalfEdge.Sym.Org, gLUvertex, gLUhalfEdge.Org) >= 0.0d : Geom.EdgeEval(gLUhalfEdge.Sym.Org, gLUvertex, gLUhalfEdge.Org) >= Geom.EdgeEval(gLUhalfEdge2.Sym.Org, gLUvertex, gLUhalfEdge2.Org);
    }

    static GLUhalfEdge FinishLeftRegions(GLUtessellatorImpl gLUtessellatorImpl, ActiveRegion activeRegion, ActiveRegion activeRegion2) {
        GLUhalfEdge gLUhalfEdge = activeRegion.eUp;
        while (true) {
            if (activeRegion == activeRegion2) {
                break;
            }
            activeRegion.fixUpperEdge = false;
            ActiveRegion RegionBelow = RegionBelow(activeRegion);
            GLUhalfEdge gLUhalfEdge2 = RegionBelow.eUp;
            if (gLUhalfEdge2.Org != gLUhalfEdge.Org) {
                if (!RegionBelow.fixUpperEdge) {
                    FinishRegion(gLUtessellatorImpl, activeRegion);
                    break;
                }
                gLUhalfEdge2 = Mesh.__gl_meshConnect(gLUhalfEdge.Onext.Sym, gLUhalfEdge2.Sym);
                if (gLUhalfEdge2 == null) {
                    throw new RuntimeException();
                } else if (!FixUpperEdge(RegionBelow, gLUhalfEdge2)) {
                    throw new RuntimeException();
                }
            }
            if (gLUhalfEdge.Onext != gLUhalfEdge2) {
                if (!Mesh.__gl_meshSplice(gLUhalfEdge2.Sym.Lnext, gLUhalfEdge2)) {
                    throw new RuntimeException();
                } else if (!Mesh.__gl_meshSplice(gLUhalfEdge, gLUhalfEdge2)) {
                    throw new RuntimeException();
                }
            }
            FinishRegion(gLUtessellatorImpl, activeRegion);
            gLUhalfEdge = RegionBelow.eUp;
            activeRegion = RegionBelow;
        }
        return gLUhalfEdge;
    }

    static void FinishRegion(GLUtessellatorImpl gLUtessellatorImpl, ActiveRegion activeRegion) {
        GLUhalfEdge gLUhalfEdge = activeRegion.eUp;
        GLUface gLUface = gLUhalfEdge.Lface;
        gLUface.inside = activeRegion.inside;
        gLUface.anEdge = gLUhalfEdge;
        DeleteRegion(gLUtessellatorImpl, activeRegion);
    }

    static boolean FixUpperEdge(ActiveRegion activeRegion, GLUhalfEdge gLUhalfEdge) {
        if (!$assertionsDisabled && !activeRegion.fixUpperEdge) {
            throw new AssertionError();
        } else if (!Mesh.__gl_meshDelete(activeRegion.eUp)) {
            return false;
        } else {
            activeRegion.fixUpperEdge = false;
            activeRegion.eUp = gLUhalfEdge;
            gLUhalfEdge.activeRegion = activeRegion;
            return true;
        }
    }

    static void GetIntersectData(GLUtessellatorImpl gLUtessellatorImpl, GLUvertex gLUvertex, GLUvertex gLUvertex2, GLUvertex gLUvertex3, GLUvertex gLUvertex4, GLUvertex gLUvertex5) {
        float[] fArr = new float[4];
        float[] fArr2 = new float[2];
        float[] fArr3 = new float[2];
        Object[] objArr = {gLUvertex2.data, gLUvertex3.data, gLUvertex4.data, gLUvertex5.data};
        double[] dArr = gLUvertex.coords;
        double[] dArr2 = gLUvertex.coords;
        gLUvertex.coords[2] = 0.0d;
        dArr2[1] = 0.0d;
        dArr[0] = 0.0d;
        VertexWeights(gLUvertex, gLUvertex2, gLUvertex3, fArr2);
        VertexWeights(gLUvertex, gLUvertex4, gLUvertex5, fArr3);
        System.arraycopy(fArr2, 0, fArr, 0, 2);
        System.arraycopy(fArr3, 0, fArr, 2, 2);
        CallCombine(gLUtessellatorImpl, gLUvertex, objArr, fArr, true);
    }

    static void InitEdgeDict(final GLUtessellatorImpl gLUtessellatorImpl) {
        gLUtessellatorImpl.dict = Dict.dictNewDict(gLUtessellatorImpl, new Dict.DictLeq() {
            public boolean leq(Object obj, Object obj2, Object obj3) {
                return Sweep.EdgeLeq(gLUtessellatorImpl, (ActiveRegion) obj2, (ActiveRegion) obj3);
            }
        });
        if (gLUtessellatorImpl.dict == null) {
            throw new RuntimeException();
        }
        AddSentinel(gLUtessellatorImpl, -4.0E150d);
        AddSentinel(gLUtessellatorImpl, SENTINEL_COORD);
    }

    static boolean InitPriorityQ(GLUtessellatorImpl gLUtessellatorImpl) {
        PriorityQ pqNewPriorityQ = PriorityQ.pqNewPriorityQ(new PriorityQ.Leq() {
            public boolean leq(Object obj, Object obj2) {
                return Geom.VertLeq((GLUvertex) obj, (GLUvertex) obj2);
            }
        });
        gLUtessellatorImpl.pq = pqNewPriorityQ;
        if (pqNewPriorityQ == null) {
            return false;
        }
        GLUvertex gLUvertex = gLUtessellatorImpl.mesh.vHead;
        GLUvertex gLUvertex2 = gLUvertex.next;
        while (gLUvertex2 != gLUvertex) {
            gLUvertex2.pqHandle = pqNewPriorityQ.pqInsert(gLUvertex2);
            if (((long) gLUvertex2.pqHandle) == Long.MAX_VALUE) {
                break;
            }
            gLUvertex2 = gLUvertex2.next;
        }
        if (gLUvertex2 == gLUvertex && pqNewPriorityQ.pqInit()) {
            return true;
        }
        gLUtessellatorImpl.pq.pqDeletePriorityQ();
        gLUtessellatorImpl.pq = null;
        return false;
    }

    static boolean IsWindingInside(GLUtessellatorImpl gLUtessellatorImpl, int i) {
        boolean z = false;
        switch (gLUtessellatorImpl.windingRule) {
            case PGLU.GLU_TESS_WINDING_ODD:
                return (i & 1) != 0;
            case PGLU.GLU_TESS_WINDING_NONZERO:
                return i != 0;
            case PGLU.GLU_TESS_WINDING_POSITIVE:
                return i > 0;
            case PGLU.GLU_TESS_WINDING_NEGATIVE:
                return i < 0;
            case PGLU.GLU_TESS_WINDING_ABS_GEQ_TWO:
                if (i >= 2 || i <= -2) {
                    z = true;
                }
                return z;
            default:
                throw new InternalError();
        }
    }

    private static ActiveRegion RegionAbove(ActiveRegion activeRegion) {
        return (ActiveRegion) Dict.dictKey(Dict.dictSucc(activeRegion.nodeUp));
    }

    private static ActiveRegion RegionBelow(ActiveRegion activeRegion) {
        return (ActiveRegion) Dict.dictKey(Dict.dictPred(activeRegion.nodeUp));
    }

    static void RemoveDegenerateEdges(GLUtessellatorImpl gLUtessellatorImpl) {
        GLUhalfEdge gLUhalfEdge;
        GLUhalfEdge gLUhalfEdge2 = gLUtessellatorImpl.mesh.eHead;
        GLUhalfEdge gLUhalfEdge3 = gLUhalfEdge2.next;
        while (gLUhalfEdge3 != gLUhalfEdge2) {
            GLUhalfEdge gLUhalfEdge4 = gLUhalfEdge3.next;
            GLUhalfEdge gLUhalfEdge5 = gLUhalfEdge3.Lnext;
            if (!Geom.VertEq(gLUhalfEdge3.Org, gLUhalfEdge3.Sym.Org) || gLUhalfEdge3.Lnext.Lnext == gLUhalfEdge3) {
                GLUhalfEdge gLUhalfEdge6 = gLUhalfEdge5;
                gLUhalfEdge5 = gLUhalfEdge3;
                gLUhalfEdge = gLUhalfEdge6;
            } else {
                SpliceMergeVertices(gLUtessellatorImpl, gLUhalfEdge5, gLUhalfEdge3);
                if (!Mesh.__gl_meshDelete(gLUhalfEdge3)) {
                    throw new RuntimeException();
                }
                gLUhalfEdge = gLUhalfEdge5.Lnext;
            }
            if (gLUhalfEdge.Lnext == gLUhalfEdge5) {
                if (gLUhalfEdge != gLUhalfEdge5) {
                    if (gLUhalfEdge == gLUhalfEdge4 || gLUhalfEdge == gLUhalfEdge4.Sym) {
                        gLUhalfEdge4 = gLUhalfEdge4.next;
                    }
                    if (!Mesh.__gl_meshDelete(gLUhalfEdge)) {
                        throw new RuntimeException();
                    }
                }
                gLUhalfEdge3 = gLUhalfEdge4;
                if (gLUhalfEdge5 == gLUhalfEdge3 || gLUhalfEdge5 == gLUhalfEdge3.Sym) {
                    gLUhalfEdge3 = gLUhalfEdge3.next;
                }
                if (!Mesh.__gl_meshDelete(gLUhalfEdge5)) {
                    throw new RuntimeException();
                }
            } else {
                gLUhalfEdge3 = gLUhalfEdge4;
            }
        }
    }

    static boolean RemoveDegenerateFaces(GLUmesh gLUmesh) {
        GLUface gLUface = gLUmesh.fHead.next;
        while (gLUface != gLUmesh.fHead) {
            GLUface gLUface2 = gLUface.next;
            GLUhalfEdge gLUhalfEdge = gLUface.anEdge;
            if ($assertionsDisabled || gLUhalfEdge.Lnext != gLUhalfEdge) {
                if (gLUhalfEdge.Lnext.Lnext == gLUhalfEdge) {
                    AddWinding(gLUhalfEdge.Onext, gLUhalfEdge);
                    if (!Mesh.__gl_meshDelete(gLUhalfEdge)) {
                        return false;
                    }
                }
                gLUface = gLUface2;
            } else {
                throw new AssertionError();
            }
        }
        return true;
    }

    static void SpliceMergeVertices(GLUtessellatorImpl gLUtessellatorImpl, GLUhalfEdge gLUhalfEdge, GLUhalfEdge gLUhalfEdge2) {
        Object[] objArr = new Object[4];
        objArr[0] = gLUhalfEdge.Org.data;
        objArr[1] = gLUhalfEdge2.Org.data;
        CallCombine(gLUtessellatorImpl, gLUhalfEdge.Org, objArr, new float[]{0.5f, 0.5f, 0.0f, 0.0f}, false);
        if (!Mesh.__gl_meshSplice(gLUhalfEdge, gLUhalfEdge2)) {
            throw new RuntimeException();
        }
    }

    static void SweepEvent(GLUtessellatorImpl gLUtessellatorImpl, GLUvertex gLUvertex) {
        gLUtessellatorImpl.event = gLUvertex;
        DebugEvent(gLUtessellatorImpl);
        GLUhalfEdge gLUhalfEdge = gLUvertex.anEdge;
        while (gLUhalfEdge.activeRegion == null) {
            gLUhalfEdge = gLUhalfEdge.Onext;
            if (gLUhalfEdge == gLUvertex.anEdge) {
                ConnectLeftVertex(gLUtessellatorImpl, gLUvertex);
                return;
            }
        }
        ActiveRegion TopLeftRegion = TopLeftRegion(gLUhalfEdge.activeRegion);
        if (TopLeftRegion == null) {
            throw new RuntimeException();
        }
        ActiveRegion RegionBelow = RegionBelow(TopLeftRegion);
        GLUhalfEdge gLUhalfEdge2 = RegionBelow.eUp;
        GLUhalfEdge FinishLeftRegions = FinishLeftRegions(gLUtessellatorImpl, RegionBelow, (ActiveRegion) null);
        if (FinishLeftRegions.Onext == gLUhalfEdge2) {
            ConnectRightVertex(gLUtessellatorImpl, TopLeftRegion, FinishLeftRegions);
            return;
        }
        AddRightEdges(gLUtessellatorImpl, TopLeftRegion, FinishLeftRegions.Onext, gLUhalfEdge2, gLUhalfEdge2, true);
    }

    static ActiveRegion TopLeftRegion(ActiveRegion activeRegion) {
        GLUvertex gLUvertex = activeRegion.eUp.Org;
        do {
            activeRegion = RegionAbove(activeRegion);
        } while (activeRegion.eUp.Org == gLUvertex);
        if (!activeRegion.fixUpperEdge) {
            return activeRegion;
        }
        GLUhalfEdge __gl_meshConnect = Mesh.__gl_meshConnect(RegionBelow(activeRegion).eUp.Sym, activeRegion.eUp.Lnext);
        if (__gl_meshConnect == null) {
            return null;
        }
        if (!FixUpperEdge(activeRegion, __gl_meshConnect)) {
            return null;
        }
        return RegionAbove(activeRegion);
    }

    static ActiveRegion TopRightRegion(ActiveRegion activeRegion) {
        GLUvertex gLUvertex = activeRegion.eUp.Sym.Org;
        do {
            activeRegion = RegionAbove(activeRegion);
        } while (activeRegion.eUp.Sym.Org == gLUvertex);
        return activeRegion;
    }

    static void VertexWeights(GLUvertex gLUvertex, GLUvertex gLUvertex2, GLUvertex gLUvertex3, float[] fArr) {
        double VertL1dist = Geom.VertL1dist(gLUvertex2, gLUvertex);
        double VertL1dist2 = Geom.VertL1dist(gLUvertex3, gLUvertex);
        fArr[0] = (float) ((0.5d * VertL1dist2) / (VertL1dist + VertL1dist2));
        fArr[1] = (float) ((0.5d * VertL1dist) / (VertL1dist + VertL1dist2));
        double[] dArr = gLUvertex.coords;
        dArr[0] = dArr[0] + (((double) fArr[0]) * gLUvertex2.coords[0]) + (((double) fArr[1]) * gLUvertex3.coords[0]);
        double[] dArr2 = gLUvertex.coords;
        dArr2[1] = dArr2[1] + (((double) fArr[0]) * gLUvertex2.coords[1]) + (((double) fArr[1]) * gLUvertex3.coords[1]);
        double[] dArr3 = gLUvertex.coords;
        dArr3[2] = dArr3[2] + (((double) fArr[0]) * gLUvertex2.coords[2]) + (((double) fArr[1]) * gLUvertex3.coords[2]);
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00c6  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x009e A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void WalkDirtyRegions(processing.opengl.tess.GLUtessellatorImpl r6, processing.opengl.tess.ActiveRegion r7) {
        /*
            processing.opengl.tess.ActiveRegion r0 = RegionBelow(r7)
        L_0x0004:
            boolean r1 = r0.dirty
            if (r1 == 0) goto L_0x000f
            processing.opengl.tess.ActiveRegion r1 = RegionBelow(r0)
            r7 = r0
            r0 = r1
            goto L_0x0004
        L_0x000f:
            boolean r1 = r7.dirty
            if (r1 != 0) goto L_0x001e
            processing.opengl.tess.ActiveRegion r0 = RegionAbove(r7)
            if (r0 == 0) goto L_0x001d
            boolean r1 = r0.dirty
            if (r1 != 0) goto L_0x0021
        L_0x001d:
            return
        L_0x001e:
            r5 = r0
            r0 = r7
            r7 = r5
        L_0x0021:
            r1 = 0
            r0.dirty = r1
            processing.opengl.tess.GLUhalfEdge r2 = r0.eUp
            processing.opengl.tess.GLUhalfEdge r1 = r7.eUp
            processing.opengl.tess.GLUhalfEdge r3 = r2.Sym
            processing.opengl.tess.GLUvertex r3 = r3.Org
            processing.opengl.tess.GLUhalfEdge r4 = r1.Sym
            processing.opengl.tess.GLUvertex r4 = r4.Org
            if (r3 == r4) goto L_0x00cc
            boolean r3 = CheckForLeftSplice(r6, r0)
            if (r3 == 0) goto L_0x00cc
            boolean r3 = r7.fixUpperEdge
            if (r3 == 0) goto L_0x00a4
            DeleteRegion(r6, r7)
            boolean r1 = processing.opengl.tess.Mesh.__gl_meshDelete(r1)
            if (r1 != 0) goto L_0x004b
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            r0.<init>()
            throw r0
        L_0x004b:
            processing.opengl.tess.ActiveRegion r7 = RegionBelow(r0)
            processing.opengl.tess.GLUhalfEdge r1 = r7.eUp
            r5 = r7
            r7 = r0
            r0 = r5
        L_0x0054:
            processing.opengl.tess.GLUvertex r3 = r2.Org
            processing.opengl.tess.GLUvertex r4 = r1.Org
            if (r3 == r4) goto L_0x0082
            processing.opengl.tess.GLUhalfEdge r3 = r2.Sym
            processing.opengl.tess.GLUvertex r3 = r3.Org
            processing.opengl.tess.GLUhalfEdge r4 = r1.Sym
            processing.opengl.tess.GLUvertex r4 = r4.Org
            if (r3 == r4) goto L_0x00c2
            boolean r3 = r7.fixUpperEdge
            if (r3 != 0) goto L_0x00c2
            boolean r3 = r0.fixUpperEdge
            if (r3 != 0) goto L_0x00c2
            processing.opengl.tess.GLUhalfEdge r3 = r2.Sym
            processing.opengl.tess.GLUvertex r3 = r3.Org
            processing.opengl.tess.GLUvertex r4 = r6.event
            if (r3 == r4) goto L_0x007c
            processing.opengl.tess.GLUhalfEdge r3 = r1.Sym
            processing.opengl.tess.GLUvertex r3 = r3.Org
            processing.opengl.tess.GLUvertex r4 = r6.event
            if (r3 != r4) goto L_0x00c2
        L_0x007c:
            boolean r3 = CheckForIntersect(r6, r7)
            if (r3 != 0) goto L_0x001d
        L_0x0082:
            processing.opengl.tess.GLUvertex r3 = r2.Org
            processing.opengl.tess.GLUvertex r4 = r1.Org
            if (r3 != r4) goto L_0x0004
            processing.opengl.tess.GLUhalfEdge r3 = r2.Sym
            processing.opengl.tess.GLUvertex r3 = r3.Org
            processing.opengl.tess.GLUhalfEdge r4 = r1.Sym
            processing.opengl.tess.GLUvertex r4 = r4.Org
            if (r3 != r4) goto L_0x0004
            AddWinding(r1, r2)
            DeleteRegion(r6, r7)
            boolean r1 = processing.opengl.tess.Mesh.__gl_meshDelete(r2)
            if (r1 != 0) goto L_0x00c6
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            r0.<init>()
            throw r0
        L_0x00a4:
            boolean r3 = r0.fixUpperEdge
            if (r3 == 0) goto L_0x00cc
            DeleteRegion(r6, r0)
            boolean r0 = processing.opengl.tess.Mesh.__gl_meshDelete(r2)
            if (r0 != 0) goto L_0x00b7
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            r0.<init>()
            throw r0
        L_0x00b7:
            processing.opengl.tess.ActiveRegion r2 = RegionAbove(r7)
            processing.opengl.tess.GLUhalfEdge r0 = r2.eUp
            r5 = r0
            r0 = r7
            r7 = r2
            r2 = r5
            goto L_0x0054
        L_0x00c2:
            CheckForRightSplice(r6, r7)
            goto L_0x0082
        L_0x00c6:
            processing.opengl.tess.ActiveRegion r7 = RegionAbove(r0)
            goto L_0x0004
        L_0x00cc:
            r5 = r7
            r7 = r0
            r0 = r5
            goto L_0x0054
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.opengl.tess.Sweep.WalkDirtyRegions(processing.opengl.tess.GLUtessellatorImpl, processing.opengl.tess.ActiveRegion):void");
    }

    public static boolean __gl_computeInterior(GLUtessellatorImpl gLUtessellatorImpl) {
        gLUtessellatorImpl.fatalError = false;
        RemoveDegenerateEdges(gLUtessellatorImpl);
        if (!InitPriorityQ(gLUtessellatorImpl)) {
            return false;
        }
        InitEdgeDict(gLUtessellatorImpl);
        while (true) {
            GLUvertex gLUvertex = (GLUvertex) gLUtessellatorImpl.pq.pqExtractMin();
            if (gLUvertex == null) {
                break;
            }
            while (true) {
                GLUvertex gLUvertex2 = (GLUvertex) gLUtessellatorImpl.pq.pqMinimum();
                if (gLUvertex2 == null || !Geom.VertEq(gLUvertex2, gLUvertex)) {
                    SweepEvent(gLUtessellatorImpl, gLUvertex);
                } else {
                    SpliceMergeVertices(gLUtessellatorImpl, gLUvertex.anEdge, ((GLUvertex) gLUtessellatorImpl.pq.pqExtractMin()).anEdge);
                }
            }
            SweepEvent(gLUtessellatorImpl, gLUvertex);
        }
        gLUtessellatorImpl.event = ((ActiveRegion) Dict.dictKey(Dict.dictMin(gLUtessellatorImpl.dict))).eUp.Org;
        DebugEvent(gLUtessellatorImpl);
        DoneEdgeDict(gLUtessellatorImpl);
        DonePriorityQ(gLUtessellatorImpl);
        if (!RemoveDegenerateFaces(gLUtessellatorImpl.mesh)) {
            return false;
        }
        Mesh.__gl_meshCheckMesh(gLUtessellatorImpl.mesh);
        return true;
    }
}
