package processing.opengl.tess;

class GLUmesh {
    GLUhalfEdge eHead = new GLUhalfEdge(true);
    GLUhalfEdge eHeadSym = new GLUhalfEdge(false);
    GLUface fHead = new GLUface();
    GLUvertex vHead = new GLUvertex();

    GLUmesh() {
    }
}
