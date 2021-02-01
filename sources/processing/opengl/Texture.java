package processing.opengl;

import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.opengl.PGraphicsOpenGL;

public class Texture implements PConstants {
    protected static final int BILINEAR = 4;
    protected static final int LINEAR = 3;
    public static final int MAX_BUFFER_CACHE_SIZE = 3;
    protected static final int MAX_UPDATES = 10;
    protected static final int MIN_MEMORY = 5;
    protected static final int POINT = 2;
    protected static final int TEX2D = 0;
    protected static final int TEXRECT = 1;
    protected static final int TRILINEAR = 5;
    protected boolean bound;
    protected LinkedList<BufferData> bufferCache;
    protected Object bufferSource;
    protected boolean colorBuffer;
    protected int context;
    protected Method disposeBufferMethod;
    protected IntBuffer edgeBuffer;
    protected int[] edgePixels;
    public int glFormat;
    public int glHeight;
    public int glMagFilter;
    public int glMinFilter;
    public int glName;
    public int glTarget;
    public int glWidth;
    public int glWrapS;
    public int glWrapT;
    private PGraphicsOpenGL.GLResourceTexture glres;
    public int height;
    protected boolean invertedX;
    protected boolean invertedY;
    protected float maxTexcoordU;
    protected float maxTexcoordV;
    protected boolean modified;
    protected int mx1;
    protected int mx2;
    protected int my1;
    protected int my2;
    protected PGraphicsOpenGL pg;
    protected PGL pgl;
    protected int pixBufUpdateCount;
    protected IntBuffer pixelBuffer;
    protected int rgbaPixUpdateCount;
    protected int[] rgbaPixels;
    protected FrameBuffer tempFbo;
    protected LinkedList<BufferData> usedBuffers;
    protected boolean usingMipmaps;
    protected boolean usingRepeat;
    public int width;

    protected class BufferData {
        int h;
        Object natBuf;
        IntBuffer rgbBuf;
        int w;

        BufferData(Object obj, IntBuffer intBuffer, int i, int i2) {
            this.natBuf = obj;
            this.rgbBuf = intBuffer;
            this.w = i;
            this.h = i2;
        }

        /* access modifiers changed from: package-private */
        public void dispose() {
            try {
                Texture.this.disposeBufferMethod.invoke(Texture.this.bufferSource, new Object[]{this.natBuf});
                this.natBuf = null;
                this.rgbBuf = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class Parameters {
        public int format;
        public boolean mipmaps;
        public int sampling;
        public int target;
        public int wrapU;
        public int wrapV;

        public Parameters() {
            this.target = 0;
            this.format = 2;
            this.sampling = 4;
            this.mipmaps = true;
            this.wrapU = 0;
            this.wrapV = 0;
        }

        public Parameters(int i) {
            this.target = 0;
            this.format = i;
            this.sampling = 4;
            this.mipmaps = true;
            this.wrapU = 0;
            this.wrapV = 0;
        }

        public Parameters(int i, int i2) {
            this.target = 0;
            this.format = i;
            this.sampling = i2;
            this.mipmaps = true;
            this.wrapU = 0;
            this.wrapV = 0;
        }

        public Parameters(int i, int i2, boolean z) {
            this.target = 0;
            this.format = i;
            this.mipmaps = z;
            if (i2 != 5 || z) {
                this.sampling = i2;
            } else {
                this.sampling = 4;
            }
            this.wrapU = 0;
            this.wrapV = 0;
        }

        public Parameters(int i, int i2, boolean z, int i3) {
            this.target = 0;
            this.format = i;
            this.mipmaps = z;
            if (i2 != 5 || z) {
                this.sampling = i2;
            } else {
                this.sampling = 4;
            }
            this.wrapU = i3;
            this.wrapV = i3;
        }

        public Parameters(Parameters parameters) {
            set(parameters);
        }

        public void set(int i) {
            this.format = i;
        }

        public void set(int i, int i2) {
            this.format = i;
            this.sampling = i2;
        }

        public void set(int i, int i2, boolean z) {
            this.format = i;
            this.sampling = i2;
            this.mipmaps = z;
        }

        public void set(Parameters parameters) {
            this.target = parameters.target;
            this.format = parameters.format;
            this.sampling = parameters.sampling;
            this.mipmaps = parameters.mipmaps;
            this.wrapU = parameters.wrapU;
            this.wrapV = parameters.wrapV;
        }
    }

    public Texture(PGraphicsOpenGL pGraphicsOpenGL) {
        this.rgbaPixels = null;
        this.pixelBuffer = null;
        this.edgePixels = null;
        this.edgeBuffer = null;
        this.tempFbo = null;
        this.pixBufUpdateCount = 0;
        this.rgbaPixUpdateCount = 0;
        this.bufferCache = null;
        this.usedBuffers = null;
        this.pg = pGraphicsOpenGL;
        this.pgl = pGraphicsOpenGL.pgl;
        this.context = this.pgl.createEmptyContext();
        this.colorBuffer = false;
        this.glName = 0;
    }

    public Texture(PGraphicsOpenGL pGraphicsOpenGL, int i, int i2) {
        this(pGraphicsOpenGL, i, i2, new Parameters());
    }

    public Texture(PGraphicsOpenGL pGraphicsOpenGL, int i, int i2, Object obj) {
        this.rgbaPixels = null;
        this.pixelBuffer = null;
        this.edgePixels = null;
        this.edgeBuffer = null;
        this.tempFbo = null;
        this.pixBufUpdateCount = 0;
        this.rgbaPixUpdateCount = 0;
        this.bufferCache = null;
        this.usedBuffers = null;
        this.pg = pGraphicsOpenGL;
        this.pgl = pGraphicsOpenGL.pgl;
        this.context = this.pgl.createEmptyContext();
        this.colorBuffer = false;
        this.glName = 0;
        init(i, i2, (Parameters) obj);
    }

    /* access modifiers changed from: protected */
    public void allocate() {
        boolean z;
        dispose();
        if (!this.pgl.texturingIsEnabled(this.glTarget)) {
            this.pgl.enableTexturing(this.glTarget);
            z = true;
        } else {
            z = false;
        }
        this.context = this.pgl.getCurrentContext();
        this.glres = new PGraphicsOpenGL.GLResourceTexture(this);
        this.pgl.bindTexture(this.glTarget, this.glName);
        this.pgl.texParameteri(this.glTarget, PGL.TEXTURE_MIN_FILTER, this.glMinFilter);
        this.pgl.texParameteri(this.glTarget, PGL.TEXTURE_MAG_FILTER, this.glMagFilter);
        this.pgl.texParameteri(this.glTarget, PGL.TEXTURE_WRAP_S, this.glWrapS);
        this.pgl.texParameteri(this.glTarget, PGL.TEXTURE_WRAP_T, this.glWrapT);
        if (PGraphicsOpenGL.anisoSamplingSupported) {
            this.pgl.texParameterf(this.glTarget, PGL.TEXTURE_MAX_ANISOTROPY, PGraphicsOpenGL.maxAnisoAmount);
        }
        this.pgl.texImage2D(this.glTarget, 0, this.glFormat, this.glWidth, this.glHeight, 0, PGL.RGBA, PGL.UNSIGNED_BYTE, (Buffer) null);
        this.pgl.initTexture(this.glTarget, PGL.RGBA, this.width, this.height);
        this.pgl.bindTexture(this.glTarget, 0);
        if (z) {
            this.pgl.disableTexturing(this.glTarget);
        }
        this.bound = false;
    }

    public boolean available() {
        return this.glName > 0;
    }

    public void bind() {
        if (!this.pgl.texturingIsEnabled(this.glTarget)) {
            this.pgl.enableTexturing(this.glTarget);
        }
        this.pgl.bindTexture(this.glTarget, this.glName);
        this.bound = true;
    }

    public boolean bound() {
        return this.bound;
    }

    /* access modifiers changed from: protected */
    public boolean bufferUpdate() {
        BufferData bufferData;
        try {
            bufferData = this.bufferCache.remove(0);
        } catch (NoSuchElementException e) {
            PGraphics.showWarning("Don't have pixel data to copy to texture");
            bufferData = null;
        }
        if (bufferData == null) {
            return false;
        }
        if (!(bufferData.w == this.width && bufferData.h == this.height)) {
            init(bufferData.w, bufferData.h);
        }
        bufferData.rgbBuf.rewind();
        setNative(bufferData.rgbBuf, 0, 0, this.width, this.height);
        if (this.usedBuffers == null) {
            this.usedBuffers = new LinkedList<>();
        }
        this.usedBuffers.add(bufferData);
        return true;
    }

    public void colorBuffer(boolean z) {
        this.colorBuffer = z;
    }

    public boolean colorBuffer() {
        return this.colorBuffer;
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
    public void convertToARGB(int[] iArr) {
        if (PGL.BIG_ENDIAN) {
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < this.height; i3++) {
                int i4 = 0;
                while (i4 < this.width) {
                    int i5 = i + 1;
                    int i6 = iArr[i];
                    iArr[i2] = ((i6 << 24) & -16777216) | (i6 >>> 8);
                    i4++;
                    i2++;
                    i = i5;
                }
            }
            return;
        }
        int i7 = 0;
        int i8 = 0;
        for (int i9 = 0; i9 < this.height; i9++) {
            int i10 = 0;
            while (i10 < this.width) {
                int i11 = i7 + 1;
                int i12 = iArr[i7];
                iArr[i8] = (i12 & -16711936) | ((i12 & 255) << 16) | ((16711680 & i12) >> 16);
                i10++;
                i8++;
                i7 = i11;
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void convertToRGBA(int[] r7, int r8, int r9, int r10) {
        /*
            r6 = this;
            r5 = 16711680(0xff0000, float:2.3418052E-38)
            r0 = 0
            boolean r1 = processing.opengl.PGL.BIG_ENDIAN
            if (r1 == 0) goto L_0x0042
            switch(r8) {
                case 1: goto L_0x001f;
                case 2: goto L_0x002f;
                case 3: goto L_0x000a;
                case 4: goto L_0x0011;
                default: goto L_0x000a;
            }
        L_0x000a:
            int r0 = r6.rgbaPixUpdateCount
            int r0 = r0 + 1
            r6.rgbaPixUpdateCount = r0
            return
        L_0x0011:
            int r1 = r7.length
            if (r0 >= r1) goto L_0x000a
            int[] r1 = r6.rgbaPixels
            r2 = r7[r0]
            r2 = r2 | -256(0xffffffffffffff00, float:NaN)
            r1[r0] = r2
            int r0 = r0 + 1
            goto L_0x0011
        L_0x001f:
            int r1 = r7.length
            if (r0 >= r1) goto L_0x000a
            r1 = r7[r0]
            int[] r2 = r6.rgbaPixels
            int r1 = r1 << 8
            r1 = r1 | 255(0xff, float:3.57E-43)
            r2[r0] = r1
            int r0 = r0 + 1
            goto L_0x001f
        L_0x002f:
            int r1 = r7.length
            if (r0 >= r1) goto L_0x000a
            r1 = r7[r0]
            int[] r2 = r6.rgbaPixels
            int r3 = r1 << 8
            int r1 = r1 >> 24
            r1 = r1 & 255(0xff, float:3.57E-43)
            r1 = r1 | r3
            r2[r0] = r1
            int r0 = r0 + 1
            goto L_0x002f
        L_0x0042:
            switch(r8) {
                case 1: goto L_0x0046;
                case 2: goto L_0x0075;
                case 3: goto L_0x0045;
                case 4: goto L_0x0063;
                default: goto L_0x0045;
            }
        L_0x0045:
            goto L_0x000a
        L_0x0046:
            int r1 = r7.length
            if (r0 >= r1) goto L_0x000a
            r1 = r7[r0]
            int[] r2 = r6.rgbaPixels
            r3 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r4 = r1 & 255(0xff, float:3.57E-43)
            int r4 = r4 << 16
            r3 = r3 | r4
            r4 = r1 & r5
            int r4 = r4 >> 16
            r3 = r3 | r4
            r4 = 65280(0xff00, float:9.1477E-41)
            r1 = r1 & r4
            r1 = r1 | r3
            r2[r0] = r1
            int r0 = r0 + 1
            goto L_0x0046
        L_0x0063:
            int r1 = r7.length
            if (r0 >= r1) goto L_0x000a
            int[] r1 = r6.rgbaPixels
            r2 = r7[r0]
            int r2 = r2 << 24
            r3 = 16777215(0xffffff, float:2.3509886E-38)
            r2 = r2 | r3
            r1[r0] = r2
            int r0 = r0 + 1
            goto L_0x0063
        L_0x0075:
            int r1 = r7.length
            if (r0 >= r1) goto L_0x000a
            r1 = r7[r0]
            int[] r2 = r6.rgbaPixels
            r3 = r1 & 255(0xff, float:3.57E-43)
            int r3 = r3 << 16
            r4 = r1 & r5
            int r4 = r4 >> 16
            r3 = r3 | r4
            r4 = -16711936(0xffffffffff00ff00, float:-1.7146522E38)
            r1 = r1 & r4
            r1 = r1 | r3
            r2[r0] = r1
            int r0 = r0 + 1
            goto L_0x0075
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.opengl.Texture.convertToRGBA(int[], int, int, int):void");
    }

    public void copyBufferFromSource(Object obj, ByteBuffer byteBuffer, int i, int i2) {
        if (this.bufferCache == null) {
            this.bufferCache = new LinkedList<>();
        }
        if (this.bufferCache.size() + 1 <= 3) {
            this.bufferCache.add(new BufferData(obj, byteBuffer.asIntBuffer(), i, i2));
            return;
        }
        try {
            this.usedBuffers.add(new BufferData(obj, byteBuffer.asIntBuffer(), i, i2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void copyObject(Texture texture) {
        dispose();
        this.width = texture.width;
        this.height = texture.height;
        this.glName = texture.glName;
        this.glTarget = texture.glTarget;
        this.glFormat = texture.glFormat;
        this.glMinFilter = texture.glMinFilter;
        this.glMagFilter = texture.glMagFilter;
        this.glWidth = texture.glWidth;
        this.glHeight = texture.glHeight;
        this.usingMipmaps = texture.usingMipmaps;
        this.usingRepeat = texture.usingRepeat;
        this.maxTexcoordU = texture.maxTexcoordU;
        this.maxTexcoordV = texture.maxTexcoordV;
        this.invertedX = texture.invertedX;
        this.invertedY = texture.invertedY;
    }

    /* access modifiers changed from: protected */
    public void copyTexture(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        if (this.tempFbo == null) {
            this.tempFbo = new FrameBuffer(this.pg, this.glWidth, this.glHeight);
        }
        this.tempFbo.setColorBuffer(this);
        this.tempFbo.disableDepthTest();
        this.pg.pushFramebuffer();
        this.pg.setFramebuffer(this.tempFbo);
        this.pg.pushStyle();
        this.pg.blendMode(0);
        if (z) {
            this.pgl.drawTexture(i, i2, i3, i4, 0, 0, this.tempFbo.width, this.tempFbo.height, i5, i6, i7, i8, 0, 0, this.width, this.height);
        } else {
            this.pgl.drawTexture(i, i2, i3, i4, 0, 0, this.tempFbo.width, this.tempFbo.height, i5, i6, i7, i8, i5, i6, i7, i8);
        }
        this.pgl.flush();
        this.pg.popStyle();
        this.pg.popFramebuffer();
        updateTexels(i5, i6, i7, i8);
    }

    /* access modifiers changed from: protected */
    public void copyTexture(Texture texture, int i, int i2, int i3, int i4, boolean z) {
        if (texture == null) {
            throw new RuntimeException("Source texture is null");
        }
        if (this.tempFbo == null) {
            this.tempFbo = new FrameBuffer(this.pg, this.glWidth, this.glHeight);
        }
        this.tempFbo.setColorBuffer(this);
        this.tempFbo.disableDepthTest();
        this.pg.pushFramebuffer();
        this.pg.setFramebuffer(this.tempFbo);
        this.pg.pushStyle();
        this.pg.blendMode(0);
        if (z) {
            this.pgl.drawTexture(texture.glTarget, texture.glName, texture.glWidth, texture.glHeight, 0, 0, this.tempFbo.width, this.tempFbo.height, 1, i, i2, i + i3, i2 + i4, 0, 0, this.width, this.height);
        } else {
            this.pgl.drawTexture(texture.glTarget, texture.glName, texture.glWidth, texture.glHeight, 0, 0, this.tempFbo.width, this.tempFbo.height, 1, i, i2, i + i3, i2 + i4, i, i2, i + i3, i2 + i4);
        }
        this.pgl.flush();
        this.pg.popStyle();
        this.pg.popFramebuffer();
        updateTexels(i, i2, i3, i4);
    }

    public int currentSampling() {
        if (this.glMagFilter == PGL.NEAREST && this.glMinFilter == PGL.NEAREST) {
            return 2;
        }
        if (this.glMagFilter == PGL.NEAREST) {
            if (this.glMinFilter == (PGL.MIPMAPS_ENABLED ? PGL.LINEAR_MIPMAP_NEAREST : PGL.LINEAR)) {
                return 3;
            }
        }
        if (this.glMagFilter == PGL.LINEAR) {
            if (this.glMinFilter == (PGL.MIPMAPS_ENABLED ? PGL.LINEAR_MIPMAP_NEAREST : PGL.LINEAR)) {
                return 4;
            }
        }
        return (this.glMagFilter == PGL.LINEAR && this.glMinFilter == PGL.LINEAR_MIPMAP_LINEAR) ? 5 : -1;
    }

    /* access modifiers changed from: protected */
    public void dispose() {
        if (this.glres != null) {
            this.glres.dispose();
            this.glres = null;
            this.glName = 0;
        }
    }

    public void disposeSourceBuffer() {
        BufferData bufferData;
        if (this.usedBuffers != null) {
            while (this.usedBuffers.size() > 0) {
                try {
                    bufferData = this.usedBuffers.remove(0);
                } catch (NoSuchElementException e) {
                    PGraphics.showWarning("Cannot remove used buffer");
                    bufferData = null;
                }
                if (bufferData != null) {
                    bufferData.dispose();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void fillEdges(int i, int i2, int i3, int i4) {
        if (this.width >= this.glWidth && this.height >= this.glHeight) {
            return;
        }
        if (i + i3 == this.width || i2 + i4 == this.height) {
            if (i + i3 == this.width) {
                int i5 = this.glWidth - this.width;
                this.edgePixels = new int[(i4 * i5)];
                for (int i6 = 0; i6 < i4; i6++) {
                    Arrays.fill(this.edgePixels, i6 * i5, (i6 + 1) * i5, this.rgbaPixels[(i6 * i3) + (i3 - 1)]);
                }
                this.edgeBuffer = PGL.updateIntBuffer(this.edgeBuffer, this.edgePixels, true);
                this.pgl.texSubImage2D(this.glTarget, 0, this.width, i2, i5, i4, PGL.RGBA, PGL.UNSIGNED_BYTE, this.edgeBuffer);
            }
            if (i2 + i4 == this.height) {
                int i7 = this.glHeight - this.height;
                this.edgePixels = new int[(i7 * i3)];
                for (int i8 = 0; i8 < i7; i8++) {
                    System.arraycopy(this.rgbaPixels, (i4 - 1) * i3, this.edgePixels, i8 * i3, i3);
                }
                this.edgeBuffer = PGL.updateIntBuffer(this.edgeBuffer, this.edgePixels, true);
                this.pgl.texSubImage2D(this.glTarget, 0, i, this.height, i3, i7, PGL.RGBA, PGL.UNSIGNED_BYTE, this.edgeBuffer);
            }
            if (i + i3 == this.width && i2 + i4 == this.height) {
                int i9 = this.glWidth - this.width;
                int i10 = this.glHeight - this.height;
                int i11 = this.rgbaPixels[(i3 * i4) - 1];
                this.edgePixels = new int[(i10 * i9)];
                Arrays.fill(this.edgePixels, 0, i10 * i9, i11);
                this.edgeBuffer = PGL.updateIntBuffer(this.edgeBuffer, this.edgePixels, true);
                this.pgl.texSubImage2D(this.glTarget, 0, this.width, this.height, i9, i10, PGL.RGBA, PGL.UNSIGNED_BYTE, this.edgeBuffer);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void flipArrayOnX(int[] iArr, int i) {
        int i2 = (this.width - 1) * i;
        int i3 = 0;
        for (int i4 = 0; i4 < this.width / 2; i4++) {
            for (int i5 = 0; i5 < this.height; i5++) {
                int i6 = i3 + (i * i5 * this.width);
                int i7 = (i * i5 * this.width) + i2;
                for (int i8 = 0; i8 < i; i8++) {
                    int i9 = iArr[i6];
                    iArr[i6] = iArr[i7];
                    iArr[i7] = i9;
                    i6++;
                    i7++;
                }
            }
            i3 += i;
            i2 -= i;
        }
    }

    /* access modifiers changed from: protected */
    public void flipArrayOnY(int[] iArr, int i) {
        int i2 = (this.height - 1) * i * this.width;
        int i3 = 0;
        for (int i4 = 0; i4 < this.height / 2; i4++) {
            int i5 = i2;
            int i6 = 0;
            while (i6 < this.width * i) {
                int i7 = iArr[i3];
                iArr[i3] = iArr[i5];
                iArr[i5] = i7;
                i6++;
                i5++;
                i3++;
            }
            i2 = i5 - ((this.width * i) * 2);
        }
    }

    public void get(int[] iArr) {
        if (iArr == null) {
            throw new RuntimeException("Trying to copy texture to null pixels array");
        } else if (iArr.length != this.width * this.height) {
            throw new RuntimeException("Trying to copy texture to pixels array of wrong size");
        } else {
            if (this.tempFbo == null) {
                this.tempFbo = new FrameBuffer(this.pg, this.glWidth, this.glHeight);
            }
            this.tempFbo.setColorBuffer(this);
            this.pg.pushFramebuffer();
            this.pg.setFramebuffer(this.tempFbo);
            this.tempFbo.readPixels();
            this.pg.popFramebuffer();
            this.tempFbo.getPixels(iArr);
            convertToARGB(iArr);
            if (this.invertedX) {
                flipArrayOnX(iArr, 1);
            }
            if (this.invertedY) {
                flipArrayOnY(iArr, 1);
            }
        }
    }

    public void getBufferPixels(int[] iArr) {
        BufferData bufferData = null;
        if (this.usedBuffers != null && this.usedBuffers.size() > 0) {
            bufferData = this.usedBuffers.getLast();
        } else if (this.bufferCache != null && this.bufferCache.size() > 0) {
            bufferData = this.bufferCache.getLast();
        }
        if (bufferData != null) {
            if (!(bufferData.w == this.width && bufferData.h == this.height)) {
                init(bufferData.w, bufferData.h);
            }
            bufferData.rgbBuf.rewind();
            bufferData.rgbBuf.get(iArr);
            convertToARGB(iArr);
            if (this.usedBuffers == null) {
                this.usedBuffers = new LinkedList<>();
            }
            while (this.bufferCache.size() > 0) {
                this.usedBuffers.add(this.bufferCache.remove(0));
            }
        }
    }

    public int getModifiedX1() {
        return this.mx1;
    }

    public int getModifiedX2() {
        return this.mx2;
    }

    public int getModifiedY1() {
        return this.my1;
    }

    public int getModifiedY2() {
        return this.my2;
    }

    public Parameters getParameters() {
        Parameters parameters = new Parameters();
        if (this.glTarget == PGL.TEXTURE_2D) {
            parameters.target = 0;
        }
        if (this.glFormat == PGL.RGB) {
            parameters.format = 1;
        } else if (this.glFormat == PGL.RGBA) {
            parameters.format = 2;
        } else if (this.glFormat == PGL.ALPHA) {
            parameters.format = 4;
        }
        if (this.glMagFilter == PGL.NEAREST && this.glMinFilter == PGL.NEAREST) {
            parameters.sampling = 2;
            parameters.mipmaps = false;
        } else if (this.glMagFilter == PGL.NEAREST && this.glMinFilter == PGL.LINEAR) {
            parameters.sampling = 3;
            parameters.mipmaps = false;
        } else if (this.glMagFilter == PGL.NEAREST && this.glMinFilter == PGL.LINEAR_MIPMAP_NEAREST) {
            parameters.sampling = 3;
            parameters.mipmaps = true;
        } else if (this.glMagFilter == PGL.LINEAR && this.glMinFilter == PGL.LINEAR) {
            parameters.sampling = 4;
            parameters.mipmaps = false;
        } else if (this.glMagFilter == PGL.LINEAR && this.glMinFilter == PGL.LINEAR_MIPMAP_NEAREST) {
            parameters.sampling = 4;
            parameters.mipmaps = true;
        } else if (this.glMagFilter == PGL.LINEAR && this.glMinFilter == PGL.LINEAR_MIPMAP_LINEAR) {
            parameters.sampling = 5;
            parameters.mipmaps = true;
        }
        if (this.glWrapS == PGL.CLAMP_TO_EDGE) {
            parameters.wrapU = 0;
        } else if (this.glWrapS == PGL.REPEAT) {
            parameters.wrapU = 1;
        }
        if (this.glWrapT == PGL.CLAMP_TO_EDGE) {
            parameters.wrapV = 0;
        } else if (this.glWrapT == PGL.REPEAT) {
            parameters.wrapV = 1;
        }
        return parameters;
    }

    /* access modifiers changed from: protected */
    public void getSourceMethods() {
        try {
            this.disposeBufferMethod = this.bufferSource.getClass().getMethod("disposeBuffer", new Class[]{Object.class});
        } catch (Exception e) {
            throw new RuntimeException("Provided source object doesn't have a disposeBuffer method.");
        }
    }

    public boolean hasBufferSource() {
        return this.bufferSource != null;
    }

    public boolean hasBuffers() {
        return (this.bufferSource == null || this.bufferCache == null || this.bufferCache.size() <= 0) ? false : true;
    }

    public void init(int i, int i2) {
        init(i, i2, this.glName > 0 ? getParameters() : new Parameters());
    }

    public void init(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11) {
        boolean z = false;
        this.width = i;
        this.height = i2;
        this.glName = i3;
        this.glTarget = i4;
        this.glFormat = i5;
        this.glWidth = i6;
        this.glHeight = i7;
        this.glMinFilter = i8;
        this.glMagFilter = i9;
        this.glWrapS = i10;
        this.glWrapT = i11;
        this.maxTexcoordU = ((float) i) / ((float) i6);
        this.maxTexcoordV = ((float) i2) / ((float) i7);
        this.usingMipmaps = i8 == PGL.LINEAR_MIPMAP_NEAREST || i8 == PGL.LINEAR_MIPMAP_LINEAR;
        if (i10 == PGL.REPEAT || i11 == PGL.REPEAT) {
            z = true;
        }
        this.usingRepeat = z;
    }

    public void init(int i, int i2, Parameters parameters) {
        setParameters(parameters);
        setSize(i, i2);
        allocate();
    }

    public void invertedX(boolean z) {
        this.invertedX = z;
    }

    public boolean invertedX() {
        return this.invertedX;
    }

    public void invertedY(boolean z) {
        this.invertedY = z;
    }

    public boolean invertedY() {
        return this.invertedY;
    }

    public boolean isModified() {
        return this.modified;
    }

    /* access modifiers changed from: protected */
    public void loadPixels(int i) {
        if (this.rgbaPixels == null || this.rgbaPixels.length < i) {
            this.rgbaPixels = new int[i];
        }
    }

    /* access modifiers changed from: protected */
    public void manualMipmap() {
    }

    public float maxTexcoordU() {
        return this.maxTexcoordU;
    }

    public float maxTexcoordV() {
        return this.maxTexcoordV;
    }

    public void put(int i, int i2, int i3, int i4, int i5, int i6) {
        copyTexture(i, i2, i3, i4, 0, 0, i5, i6, false);
    }

    public void put(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        copyTexture(i, i2, i3, i4, i7, i8, i9, i10, false);
    }

    public void put(Texture texture) {
        copyTexture(texture, 0, 0, texture.width, texture.height, false);
    }

    public void put(Texture texture, int i, int i2, int i3, int i4) {
        copyTexture(texture, i, i2, i3, i4, false);
    }

    /* access modifiers changed from: protected */
    public void releasePixelBuffer() {
        double freeMemory = ((double) Runtime.getRuntime().freeMemory()) / 1000000.0d;
        if (this.pixBufUpdateCount < 10 || freeMemory < 5.0d) {
            this.pixelBuffer = null;
        }
    }

    /* access modifiers changed from: protected */
    public void releaseRGBAPixels() {
        double freeMemory = ((double) Runtime.getRuntime().freeMemory()) / 1000000.0d;
        if (this.rgbaPixUpdateCount < 10 || freeMemory < 5.0d) {
            this.rgbaPixels = null;
        }
    }

    public void resize(int i, int i2) {
        dispose();
        Texture texture = new Texture(this.pg, i, i2, getParameters());
        texture.set(this);
        copyObject(texture);
        this.tempFbo = null;
    }

    public void set(int i, int i2, int i3, int i4, int i5, int i6) {
        copyTexture(i, i2, i3, i4, 0, 0, i5, i6, true);
    }

    public void set(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        copyTexture(i, i2, i3, i4, i7, i8, i9, i10, true);
    }

    public void set(Texture texture) {
        copyTexture(texture, 0, 0, texture.width, texture.height, true);
    }

    public void set(Texture texture, int i, int i2, int i3, int i4) {
        copyTexture(texture, i, i2, i3, i4, true);
    }

    public void set(int[] iArr) {
        set(iArr, 0, 0, this.width, this.height, 2);
    }

    public void set(int[] iArr, int i) {
        set(iArr, 0, 0, this.width, this.height, i);
    }

    public void set(int[] iArr, int i, int i2, int i3, int i4) {
        set(iArr, i, i2, i3, i4, 2);
    }

    public void set(int[] iArr, int i, int i2, int i3, int i4, int i5) {
        boolean z;
        if (iArr == null) {
            PGraphics.showWarning("The pixels array is null.");
        } else if (iArr.length < i3 * i4) {
            PGraphics.showWarning("The pixel array has a length of " + iArr.length + ", but it should be at least " + (i3 * i4));
        } else if (iArr.length != 0 && i3 != 0 && i4 != 0) {
            if (!this.pgl.texturingIsEnabled(this.glTarget)) {
                this.pgl.enableTexturing(this.glTarget);
                z = true;
            } else {
                z = false;
            }
            this.pgl.bindTexture(this.glTarget, this.glName);
            loadPixels(i3 * i4);
            convertToRGBA(iArr, i5, i3, i4);
            updatePixelBuffer(this.rgbaPixels);
            this.pgl.texSubImage2D(this.glTarget, 0, i, i2, i3, i4, PGL.RGBA, PGL.UNSIGNED_BYTE, this.pixelBuffer);
            fillEdges(i, i2, i3, i4);
            if (this.usingMipmaps) {
                if (PGraphicsOpenGL.autoMipmapGenSupported) {
                    this.pgl.generateMipmap(this.glTarget);
                } else {
                    manualMipmap();
                }
            }
            this.pgl.bindTexture(this.glTarget, 0);
            if (z) {
                this.pgl.disableTexturing(this.glTarget);
            }
            releasePixelBuffer();
            releaseRGBAPixels();
            updateTexels(i, i2, i3, i4);
        }
    }

    public void setBufferSource(Object obj) {
        this.bufferSource = obj;
        getSourceMethods();
    }

    public void setModified() {
        this.modified = true;
    }

    public void setModified(boolean z) {
        this.modified = z;
    }

    public void setNative(IntBuffer intBuffer, int i, int i2, int i3, int i4) {
        boolean z;
        if (intBuffer == null) {
            PGraphics.showWarning("The pixel buffer is null.");
        } else if (intBuffer.capacity() < i3 * i4) {
            PGraphics.showWarning("The pixel bufer has a length of " + intBuffer.capacity() + ", but it should be at least " + (i3 * i4));
        } else if (intBuffer.capacity() != 0) {
            if (!this.pgl.texturingIsEnabled(this.glTarget)) {
                this.pgl.enableTexturing(this.glTarget);
                z = true;
            } else {
                z = false;
            }
            this.pgl.bindTexture(this.glTarget, this.glName);
            this.pgl.texSubImage2D(this.glTarget, 0, i, i2, i3, i4, PGL.RGBA, PGL.UNSIGNED_BYTE, intBuffer);
            fillEdges(i, i2, i3, i4);
            if (this.usingMipmaps) {
                if (PGraphicsOpenGL.autoMipmapGenSupported) {
                    this.pgl.generateMipmap(this.glTarget);
                } else {
                    manualMipmap();
                }
            }
            this.pgl.bindTexture(this.glTarget, 0);
            if (z) {
                this.pgl.disableTexturing(this.glTarget);
            }
            updateTexels(i, i2, i3, i4);
        }
    }

    public void setNative(int[] iArr) {
        setNative(iArr, 0, 0, this.width, this.height);
    }

    public void setNative(int[] iArr, int i, int i2, int i3, int i4) {
        updatePixelBuffer(iArr);
        setNative(this.pixelBuffer, i, i2, i3, i4);
        releasePixelBuffer();
    }

    /* access modifiers changed from: protected */
    public void setParameters(Parameters parameters) {
        boolean z = true;
        if (parameters.target == 0) {
            this.glTarget = PGL.TEXTURE_2D;
            if (parameters.format == 1) {
                this.glFormat = PGL.RGB;
            } else if (parameters.format == 2) {
                this.glFormat = PGL.RGBA;
            } else if (parameters.format == 4) {
                this.glFormat = PGL.ALPHA;
            } else {
                throw new RuntimeException("Unknown texture format");
            }
            boolean z2 = parameters.mipmaps && PGL.MIPMAPS_ENABLED;
            if (z2 && !PGraphicsOpenGL.autoMipmapGenSupported) {
                PGraphics.showWarning("Mipmaps were requested but automatic mipmap generation is not supported and manual generation still not implemented, so mipmaps will be disabled.");
                z2 = false;
            }
            if (parameters.sampling == 2) {
                this.glMagFilter = PGL.NEAREST;
                this.glMinFilter = PGL.NEAREST;
            } else if (parameters.sampling == 3) {
                this.glMagFilter = PGL.NEAREST;
                this.glMinFilter = z2 ? PGL.LINEAR_MIPMAP_NEAREST : PGL.LINEAR;
            } else if (parameters.sampling == 4) {
                this.glMagFilter = PGL.LINEAR;
                this.glMinFilter = z2 ? PGL.LINEAR_MIPMAP_NEAREST : PGL.LINEAR;
            } else if (parameters.sampling == 5) {
                this.glMagFilter = PGL.LINEAR;
                this.glMinFilter = z2 ? PGL.LINEAR_MIPMAP_LINEAR : PGL.LINEAR;
            } else {
                throw new RuntimeException("Unknown texture filtering mode");
            }
            if (parameters.wrapU == 0) {
                this.glWrapS = PGL.CLAMP_TO_EDGE;
            } else if (parameters.wrapU == 1) {
                this.glWrapS = PGL.REPEAT;
            } else {
                throw new RuntimeException("Unknown wrapping mode");
            }
            if (parameters.wrapV == 0) {
                this.glWrapT = PGL.CLAMP_TO_EDGE;
            } else if (parameters.wrapV == 1) {
                this.glWrapT = PGL.REPEAT;
            } else {
                throw new RuntimeException("Unknown wrapping mode");
            }
            this.usingMipmaps = this.glMinFilter == PGL.LINEAR_MIPMAP_NEAREST || this.glMinFilter == PGL.LINEAR_MIPMAP_LINEAR;
            if (!(this.glWrapS == PGL.REPEAT || this.glWrapT == PGL.REPEAT)) {
                z = false;
            }
            this.usingRepeat = z;
            this.invertedX = false;
            this.invertedY = false;
            return;
        }
        throw new RuntimeException("Unknown texture target");
    }

    /* access modifiers changed from: protected */
    public void setSize(int i, int i2) {
        this.width = i;
        this.height = i2;
        if (PGraphicsOpenGL.npotTexSupported) {
            this.glWidth = i;
            this.glHeight = i2;
        } else {
            this.glWidth = PGL.nextPowerOfTwo(i);
            this.glHeight = PGL.nextPowerOfTwo(i2);
        }
        if (this.glWidth > PGraphicsOpenGL.maxTextureSize || this.glHeight > PGraphicsOpenGL.maxTextureSize) {
            this.glHeight = 0;
            this.glWidth = 0;
            throw new RuntimeException("Image width and height cannot be larger than " + PGraphicsOpenGL.maxTextureSize + " with this graphics card.");
        }
        this.maxTexcoordU = ((float) this.width) / ((float) this.glWidth);
        this.maxTexcoordV = ((float) this.height) / ((float) this.glHeight);
    }

    public void unbind() {
        if (this.pgl.textureIsBound(this.glTarget, this.glName)) {
            if (!this.pgl.texturingIsEnabled(this.glTarget)) {
                this.pgl.enableTexturing(this.glTarget);
                this.pgl.bindTexture(this.glTarget, 0);
                this.pgl.disableTexturing(this.glTarget);
            } else {
                this.pgl.bindTexture(this.glTarget, 0);
            }
        }
        this.bound = false;
    }

    /* access modifiers changed from: protected */
    public void updatePixelBuffer(int[] iArr) {
        this.pixelBuffer = PGL.updateIntBuffer(this.pixelBuffer, iArr, true);
        this.pixBufUpdateCount++;
    }

    public void updateTexels() {
        updateTexelsImpl(0, 0, this.width, this.height);
    }

    public void updateTexels(int i, int i2, int i3, int i4) {
        updateTexelsImpl(i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public void updateTexelsImpl(int i, int i2, int i3, int i4) {
        int i5 = i + i3;
        int i6 = i2 + i4;
        if (!this.modified) {
            this.mx1 = PApplet.max(0, i);
            this.mx2 = PApplet.min(this.width - 1, i5);
            this.my1 = PApplet.max(0, i2);
            this.my2 = PApplet.min(this.height - 1, i6);
            this.modified = true;
            return;
        }
        if (i < this.mx1) {
            this.mx1 = PApplet.max(0, i);
        }
        if (i > this.mx2) {
            this.mx2 = PApplet.min(this.width - 1, i);
        }
        if (i2 < this.my1) {
            this.my1 = PApplet.max(0, i2);
        }
        if (i2 > this.my2) {
            this.my2 = i2;
        }
        if (i5 < this.mx1) {
            this.mx1 = PApplet.max(0, i5);
        }
        if (i5 > this.mx2) {
            this.mx2 = PApplet.min(this.width - 1, i5);
        }
        if (i6 < this.my1) {
            this.my1 = PApplet.max(0, i6);
        }
        if (i6 > this.my2) {
            this.my2 = PApplet.min(this.height - 1, i6);
        }
    }

    public void usingMipmaps(boolean z, int i) {
        int i2 = this.glMagFilter;
        int i3 = this.glMinFilter;
        if (!z) {
            this.usingMipmaps = false;
            if (i == 2) {
                this.glMagFilter = PGL.NEAREST;
                this.glMinFilter = PGL.NEAREST;
            } else if (i == 3) {
                this.glMagFilter = PGL.NEAREST;
                this.glMinFilter = PGL.LINEAR;
            } else if (i == 4 || i == 5) {
                this.glMagFilter = PGL.LINEAR;
                this.glMinFilter = PGL.LINEAR;
            } else {
                throw new RuntimeException("Unknown texture filtering mode");
            }
        } else if (i == 2) {
            this.glMagFilter = PGL.NEAREST;
            this.glMinFilter = PGL.NEAREST;
            this.usingMipmaps = false;
        } else if (i == 3) {
            this.glMagFilter = PGL.NEAREST;
            this.glMinFilter = PGL.MIPMAPS_ENABLED ? PGL.LINEAR_MIPMAP_NEAREST : PGL.LINEAR;
            this.usingMipmaps = true;
        } else if (i == 4) {
            this.glMagFilter = PGL.LINEAR;
            this.glMinFilter = PGL.MIPMAPS_ENABLED ? PGL.LINEAR_MIPMAP_NEAREST : PGL.LINEAR;
            this.usingMipmaps = true;
        } else if (i == 5) {
            this.glMagFilter = PGL.LINEAR;
            this.glMinFilter = PGL.MIPMAPS_ENABLED ? PGL.LINEAR_MIPMAP_LINEAR : PGL.LINEAR;
            this.usingMipmaps = true;
        } else {
            throw new RuntimeException("Unknown texture filtering mode");
        }
        if (i2 != this.glMagFilter || i3 != this.glMinFilter) {
            bind();
            this.pgl.texParameteri(this.glTarget, PGL.TEXTURE_MIN_FILTER, this.glMinFilter);
            this.pgl.texParameteri(this.glTarget, PGL.TEXTURE_MAG_FILTER, this.glMagFilter);
            if (this.usingMipmaps) {
                if (PGraphicsOpenGL.autoMipmapGenSupported) {
                    this.pgl.generateMipmap(this.glTarget);
                } else {
                    manualMipmap();
                }
            }
            unbind();
        }
    }

    public boolean usingMipmaps() {
        return this.usingMipmaps;
    }

    public void usingRepeat(boolean z) {
        if (z) {
            this.glWrapS = PGL.REPEAT;
            this.glWrapT = PGL.REPEAT;
            this.usingRepeat = true;
        } else {
            this.glWrapS = PGL.CLAMP_TO_EDGE;
            this.glWrapT = PGL.CLAMP_TO_EDGE;
            this.usingRepeat = false;
        }
        bind();
        this.pgl.texParameteri(this.glTarget, PGL.TEXTURE_WRAP_S, this.glWrapS);
        this.pgl.texParameteri(this.glTarget, PGL.TEXTURE_WRAP_T, this.glWrapT);
        unbind();
    }

    public boolean usingRepeat() {
        return this.usingRepeat;
    }
}
