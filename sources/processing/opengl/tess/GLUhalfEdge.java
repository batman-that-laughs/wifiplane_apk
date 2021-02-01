package processing.opengl.tess;

class GLUhalfEdge {
    public GLUface Lface;
    public GLUhalfEdge Lnext;
    public GLUhalfEdge Onext;
    public GLUvertex Org;
    public GLUhalfEdge Sym;
    public ActiveRegion activeRegion;
    public boolean first;
    public GLUhalfEdge next;
    public int winding;

    public GLUhalfEdge(boolean z) {
        this.first = z;
    }
}
