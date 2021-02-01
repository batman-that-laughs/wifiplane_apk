package processing.opengl.tess;

class GLUvertex {
    public GLUhalfEdge anEdge;
    public double[] coords = new double[3];
    public Object data;
    public GLUvertex next;
    public int pqHandle;
    public GLUvertex prev;
    public double s;
    public double t;

    GLUvertex() {
    }
}
