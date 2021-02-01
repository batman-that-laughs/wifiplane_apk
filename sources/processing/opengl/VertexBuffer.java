package processing.opengl;

import java.nio.Buffer;
import processing.opengl.PGraphicsOpenGL;

public class VertexBuffer {
    protected static final int INIT_INDEX_BUFFER_SIZE = 512;
    protected static final int INIT_VERTEX_BUFFER_SIZE = 256;
    protected int context;
    int elementSize;
    public int glId;
    private PGraphicsOpenGL.GLResourceVertexBuffer glres;
    boolean index;
    int ncoords;
    protected PGL pgl;
    int target;

    VertexBuffer(PGraphicsOpenGL pGraphicsOpenGL, int i, int i2, int i3) {
        this(pGraphicsOpenGL, i, i2, i3, false);
    }

    VertexBuffer(PGraphicsOpenGL pGraphicsOpenGL, int i, int i2, int i3, boolean z) {
        this.pgl = pGraphicsOpenGL.pgl;
        this.context = this.pgl.createEmptyContext();
        this.target = i;
        this.ncoords = i2;
        this.elementSize = i3;
        this.index = z;
        create();
        init();
    }

    /* access modifiers changed from: protected */
    public boolean contextIsOutdated() {
        boolean z = !this.pgl.contextIsCurrent(this.context);
        if (z) {
            dispose();
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void create() {
        this.context = this.pgl.getCurrentContext();
        this.glres = new PGraphicsOpenGL.GLResourceVertexBuffer(this);
    }

    /* access modifiers changed from: protected */
    public void dispose() {
        if (this.glres != null) {
            this.glres.dispose();
            this.glId = 0;
            this.glres = null;
        }
    }

    /* access modifiers changed from: protected */
    public void init() {
        int i = this.index ? this.ncoords * 512 * this.elementSize : this.ncoords * 256 * this.elementSize;
        this.pgl.bindBuffer(this.target, this.glId);
        this.pgl.bufferData(this.target, i, (Buffer) null, PGL.STATIC_DRAW);
    }
}
