package processing.opengl;

import java.nio.Buffer;
import java.nio.IntBuffer;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.opengl.PGraphicsOpenGL;

public class FrameBuffer implements PConstants {
    protected Texture[] colorBufferTex;
    protected int context;
    protected int depthBits;
    public int glDepth;
    public int glDepthStencil;
    public int glFbo;
    public int glMultisample;
    public int glStencil;
    private PGraphicsOpenGL.GLResourceFrameBuffer glres;
    public int height;
    protected boolean multisample;
    protected boolean noDepth;
    protected int nsamples;
    protected int numColorBuffers;
    protected boolean packedDepthStencil;
    protected PGraphicsOpenGL pg;
    protected PGL pgl;
    protected IntBuffer pixelBuffer;
    protected boolean screenFb;
    protected int stencilBits;
    public int width;

    FrameBuffer(PGraphicsOpenGL pGraphicsOpenGL) {
        this.pg = pGraphicsOpenGL;
        this.pgl = pGraphicsOpenGL.pgl;
        this.context = this.pgl.createEmptyContext();
    }

    FrameBuffer(PGraphicsOpenGL pGraphicsOpenGL, int i, int i2) {
        this(pGraphicsOpenGL, i, i2, 1, 1, 0, 0, false, false);
    }

    FrameBuffer(PGraphicsOpenGL pGraphicsOpenGL, int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2) {
        this(pGraphicsOpenGL);
        this.glFbo = 0;
        this.glDepth = 0;
        this.glStencil = 0;
        this.glDepthStencil = 0;
        this.glMultisample = 0;
        if (z2) {
            i6 = 0;
            i5 = 0;
            i4 = 0;
            i3 = 0;
        }
        this.width = i;
        this.height = i2;
        if (1 < i3) {
            this.multisample = true;
            this.nsamples = i3;
        } else {
            this.multisample = false;
            this.nsamples = 1;
        }
        this.numColorBuffers = i4;
        this.colorBufferTex = new Texture[this.numColorBuffers];
        for (int i7 = 0; i7 < this.numColorBuffers; i7++) {
            this.colorBufferTex[i7] = null;
        }
        if (i5 < 1 && i6 < 1) {
            this.depthBits = 0;
            this.stencilBits = 0;
            this.packedDepthStencil = false;
        } else if (z) {
            this.depthBits = 24;
            this.stencilBits = 8;
            this.packedDepthStencil = true;
        } else {
            this.depthBits = i5;
            this.stencilBits = i6;
            this.packedDepthStencil = false;
        }
        this.screenFb = z2;
        allocate();
        this.noDepth = false;
        this.pixelBuffer = null;
    }

    FrameBuffer(PGraphicsOpenGL pGraphicsOpenGL, int i, int i2, boolean z) {
        this(pGraphicsOpenGL, i, i2, 1, 1, 0, 0, false, z);
    }

    /* access modifiers changed from: protected */
    public void allocate() {
        dispose();
        this.context = this.pgl.getCurrentContext();
        this.glres = new PGraphicsOpenGL.GLResourceFrameBuffer(this);
        if (this.screenFb) {
            this.glFbo = 0;
            return;
        }
        if (this.multisample) {
            initColorBufferMultisample();
        }
        if (this.packedDepthStencil) {
            initPackedDepthStencilBuffer();
            return;
        }
        if (this.depthBits > 0) {
            initDepthBuffer();
        }
        if (this.stencilBits > 0) {
            initStencilBuffer();
        }
    }

    public void bind() {
        this.pgl.bindFramebufferImpl(PGL.FRAMEBUFFER, this.glFbo);
    }

    public void clear() {
        this.pg.pushFramebuffer();
        this.pg.setFramebuffer(this);
        this.pgl.clearDepth(1.0f);
        this.pgl.clearStencil(0);
        this.pgl.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.pgl.clear(PGL.DEPTH_BUFFER_BIT | PGL.STENCIL_BUFFER_BIT | PGL.COLOR_BUFFER_BIT);
        this.pg.popFramebuffer();
    }

    /* access modifiers changed from: protected */
    public boolean contextIsOutdated() {
        if (this.screenFb) {
            return false;
        }
        boolean z = !this.pgl.contextIsCurrent(this.context);
        if (z) {
            dispose();
            for (int i = 0; i < this.numColorBuffers; i++) {
                this.colorBufferTex[i] = null;
            }
        }
        return z;
    }

    public void copy(FrameBuffer frameBuffer, int i) {
        this.pgl.bindFramebufferImpl(PGL.READ_FRAMEBUFFER, this.glFbo);
        this.pgl.bindFramebufferImpl(PGL.DRAW_FRAMEBUFFER, frameBuffer.glFbo);
        this.pgl.blitFramebuffer(0, 0, this.width, this.height, 0, 0, frameBuffer.width, frameBuffer.height, i, PGL.NEAREST);
        this.pgl.bindFramebufferImpl(PGL.READ_FRAMEBUFFER, this.pg.getCurrentFB().glFbo);
        this.pgl.bindFramebufferImpl(PGL.DRAW_FRAMEBUFFER, this.pg.getCurrentFB().glFbo);
    }

    public void copyColor(FrameBuffer frameBuffer) {
        copy(frameBuffer, PGL.COLOR_BUFFER_BIT);
    }

    public void copyDepth(FrameBuffer frameBuffer) {
        copy(frameBuffer, PGL.DEPTH_BUFFER_BIT);
    }

    public void copyStencil(FrameBuffer frameBuffer) {
        copy(frameBuffer, PGL.STENCIL_BUFFER_BIT);
    }

    /* access modifiers changed from: protected */
    public void createPixelBuffer() {
        this.pixelBuffer = IntBuffer.allocate(this.width * this.height);
        this.pixelBuffer.rewind();
    }

    public void disableDepthTest() {
        this.noDepth = true;
    }

    /* access modifiers changed from: protected */
    public void dispose() {
        if (!this.screenFb && this.glres != null) {
            this.glres.dispose();
            this.glFbo = 0;
            this.glDepth = 0;
            this.glStencil = 0;
            this.glMultisample = 0;
            this.glDepthStencil = 0;
            this.glres = null;
        }
    }

    public void finish() {
        if (!this.noDepth) {
            return;
        }
        if (this.pg.getHint(-2)) {
            this.pgl.enable(PGL.DEPTH_TEST);
        } else {
            this.pgl.disable(PGL.DEPTH_TEST);
        }
    }

    public int getDefaultDrawBuffer() {
        return this.screenFb ? this.pgl.getDefaultDrawBuffer() : PGL.COLOR_ATTACHMENT0;
    }

    public int getDefaultReadBuffer() {
        return this.screenFb ? this.pgl.getDefaultReadBuffer() : PGL.COLOR_ATTACHMENT0;
    }

    public IntBuffer getPixelBuffer() {
        return this.pixelBuffer;
    }

    public void getPixels(int[] iArr) {
        if (this.pixelBuffer != null) {
            this.pixelBuffer.get(iArr, 0, iArr.length);
            this.pixelBuffer.rewind();
        }
    }

    public boolean hasDepthBuffer() {
        return this.depthBits > 0;
    }

    public boolean hasStencilBuffer() {
        return this.stencilBits > 0;
    }

    /* access modifiers changed from: protected */
    public void initColorBufferMultisample() {
        if (!this.screenFb) {
            this.pg.pushFramebuffer();
            this.pg.setFramebuffer(this);
            this.pgl.bindRenderbuffer(PGL.RENDERBUFFER, this.glMultisample);
            this.pgl.renderbufferStorageMultisample(PGL.RENDERBUFFER, this.nsamples, PGL.RGBA8, this.width, this.height);
            this.pgl.framebufferRenderbuffer(PGL.FRAMEBUFFER, PGL.COLOR_ATTACHMENT0, PGL.RENDERBUFFER, this.glMultisample);
            this.pg.popFramebuffer();
        }
    }

    /* access modifiers changed from: protected */
    public void initDepthBuffer() {
        if (!this.screenFb) {
            if (this.width == 0 || this.height == 0) {
                throw new RuntimeException("PFramebuffer: size undefined.");
            }
            this.pg.pushFramebuffer();
            this.pg.setFramebuffer(this);
            this.pgl.bindRenderbuffer(PGL.RENDERBUFFER, this.glDepth);
            int i = PGL.DEPTH_COMPONENT16;
            if (this.depthBits == 16) {
                i = PGL.DEPTH_COMPONENT16;
            } else if (this.depthBits == 24) {
                i = PGL.DEPTH_COMPONENT24;
            } else if (this.depthBits == 32) {
                i = PGL.DEPTH_COMPONENT32;
            }
            if (this.multisample) {
                this.pgl.renderbufferStorageMultisample(PGL.RENDERBUFFER, this.nsamples, i, this.width, this.height);
            } else {
                this.pgl.renderbufferStorage(PGL.RENDERBUFFER, i, this.width, this.height);
            }
            this.pgl.framebufferRenderbuffer(PGL.FRAMEBUFFER, PGL.DEPTH_ATTACHMENT, PGL.RENDERBUFFER, this.glDepth);
            this.pg.popFramebuffer();
        }
    }

    /* access modifiers changed from: protected */
    public void initPackedDepthStencilBuffer() {
        if (!this.screenFb) {
            if (this.width == 0 || this.height == 0) {
                throw new RuntimeException("PFramebuffer: size undefined.");
            }
            this.pg.pushFramebuffer();
            this.pg.setFramebuffer(this);
            this.pgl.bindRenderbuffer(PGL.RENDERBUFFER, this.glDepthStencil);
            if (this.multisample) {
                this.pgl.renderbufferStorageMultisample(PGL.RENDERBUFFER, this.nsamples, PGL.DEPTH24_STENCIL8, this.width, this.height);
            } else {
                this.pgl.renderbufferStorage(PGL.RENDERBUFFER, PGL.DEPTH24_STENCIL8, this.width, this.height);
            }
            this.pgl.framebufferRenderbuffer(PGL.FRAMEBUFFER, PGL.DEPTH_ATTACHMENT, PGL.RENDERBUFFER, this.glDepthStencil);
            this.pgl.framebufferRenderbuffer(PGL.FRAMEBUFFER, PGL.STENCIL_ATTACHMENT, PGL.RENDERBUFFER, this.glDepthStencil);
            this.pg.popFramebuffer();
        }
    }

    /* access modifiers changed from: protected */
    public void initStencilBuffer() {
        if (!this.screenFb) {
            if (this.width == 0 || this.height == 0) {
                throw new RuntimeException("PFramebuffer: size undefined.");
            }
            this.pg.pushFramebuffer();
            this.pg.setFramebuffer(this);
            this.pgl.bindRenderbuffer(PGL.RENDERBUFFER, this.glStencil);
            int i = PGL.STENCIL_INDEX1;
            if (this.stencilBits == 1) {
                i = PGL.STENCIL_INDEX1;
            } else if (this.stencilBits == 4) {
                i = PGL.STENCIL_INDEX4;
            } else if (this.stencilBits == 8) {
                i = PGL.STENCIL_INDEX8;
            }
            if (this.multisample) {
                this.pgl.renderbufferStorageMultisample(PGL.RENDERBUFFER, this.nsamples, i, this.width, this.height);
            } else {
                this.pgl.renderbufferStorage(PGL.RENDERBUFFER, i, this.width, this.height);
            }
            this.pgl.framebufferRenderbuffer(PGL.FRAMEBUFFER, PGL.STENCIL_ATTACHMENT, PGL.RENDERBUFFER, this.glStencil);
            this.pg.popFramebuffer();
        }
    }

    public void readPixels() {
        if (this.pixelBuffer == null) {
            createPixelBuffer();
        }
        this.pixelBuffer.rewind();
        this.pgl.readPixels(0, 0, this.width, this.height, PGL.RGBA, PGL.UNSIGNED_BYTE, (Buffer) this.pixelBuffer);
    }

    public void setColorBuffer(Texture texture) {
        setColorBuffers(new Texture[]{texture}, 1);
    }

    public void setColorBuffers(Texture[] textureArr) {
        setColorBuffers(textureArr, textureArr.length);
    }

    public void setColorBuffers(Texture[] textureArr, int i) {
        if (!this.screenFb) {
            if (this.numColorBuffers != PApplet.min(i, textureArr.length)) {
                throw new RuntimeException("Wrong number of textures to set the color buffers.");
            }
            for (int i2 = 0; i2 < this.numColorBuffers; i2++) {
                this.colorBufferTex[i2] = textureArr[i2];
            }
            this.pg.pushFramebuffer();
            this.pg.setFramebuffer(this);
            for (int i3 = 0; i3 < this.numColorBuffers; i3++) {
                this.pgl.framebufferTexture2D(PGL.FRAMEBUFFER, PGL.COLOR_ATTACHMENT0 + i3, PGL.TEXTURE_2D, 0, 0);
            }
            for (int i4 = 0; i4 < this.numColorBuffers; i4++) {
                this.pgl.framebufferTexture2D(PGL.FRAMEBUFFER, PGL.COLOR_ATTACHMENT0 + i4, this.colorBufferTex[i4].glTarget, this.colorBufferTex[i4].glName, 0);
            }
            this.pgl.validateFramebuffer();
            this.pg.popFramebuffer();
        }
    }

    public void setFBO(int i) {
        if (this.screenFb) {
            this.glFbo = i;
        }
    }

    public void swapColorBuffers() {
        for (int i = 0; i < this.numColorBuffers - 1; i++) {
            int i2 = i + 1;
            Texture texture = this.colorBufferTex[i];
            this.colorBufferTex[i] = this.colorBufferTex[i2];
            this.colorBufferTex[i2] = texture;
        }
        this.pg.pushFramebuffer();
        this.pg.setFramebuffer(this);
        for (int i3 = 0; i3 < this.numColorBuffers; i3++) {
            this.pgl.framebufferTexture2D(PGL.FRAMEBUFFER, PGL.COLOR_ATTACHMENT0 + i3, this.colorBufferTex[i3].glTarget, this.colorBufferTex[i3].glName, 0);
        }
        this.pgl.validateFramebuffer();
        this.pg.popFramebuffer();
    }
}
