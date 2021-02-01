package processing.opengl.tess;

class GLUface {
    public GLUhalfEdge anEdge;
    public Object data;
    public boolean inside;
    public boolean marked;
    public GLUface next;
    public GLUface prev;
    public GLUface trail;

    GLUface() {
    }
}
