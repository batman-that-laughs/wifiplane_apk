package processing.opengl;

import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import java.util.Arrays;
import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.Texture;

class FontTexture implements PConstants {
    protected TextureInfo[] glyphTexinfos;
    protected PImage[] images = null;
    protected boolean is3D;
    protected int lastTex;
    protected int lineHeight;
    protected int maxSize;
    protected int minSize;
    protected int offsetX;
    protected int offsetY;
    protected PGL pgl;
    protected HashMap<PFont.Glyph, TextureInfo> texinfoMap;
    protected Texture[] textures = null;

    class TextureInfo {
        int[] crop = new int[4];
        int height;
        int[] pixels;
        int texIndex;
        float u0;
        float u1;
        float v0;
        float v1;
        int width;

        TextureInfo(int i, int i2, int i3, int i4, int i5, int[] iArr) {
            this.texIndex = i;
            this.crop[0] = i2 + 1;
            this.crop[1] = ((i3 + 1) + i5) - 2;
            this.crop[2] = i4 - 2;
            this.crop[3] = (-i5) + 2;
            this.pixels = iArr;
            updateUV();
            updateTex();
        }

        /* access modifiers changed from: package-private */
        public void updateTex() {
            FontTexture.this.textures[this.texIndex].setNative(this.pixels, this.crop[0] - 1, (this.crop[1] + this.crop[3]) - 1, this.crop[2] + 2, (-this.crop[3]) + 2);
        }

        /* access modifiers changed from: package-private */
        public void updateUV() {
            this.width = FontTexture.this.textures[this.texIndex].glWidth;
            this.height = FontTexture.this.textures[this.texIndex].glHeight;
            this.u0 = ((float) this.crop[0]) / ((float) this.width);
            this.u1 = this.u0 + (((float) this.crop[2]) / ((float) this.width));
            this.v0 = ((float) (this.crop[1] + this.crop[3])) / ((float) this.height);
            this.v1 = this.v0 - (((float) this.crop[3]) / ((float) this.height));
        }
    }

    public FontTexture(PGraphicsOpenGL pGraphicsOpenGL, PFont pFont, boolean z) {
        this.pgl = pGraphicsOpenGL.pgl;
        this.is3D = z;
        initTexture(pGraphicsOpenGL, pFont);
    }

    public void addAllGlyphsToTexture(PGraphicsOpenGL pGraphicsOpenGL, PFont pFont) {
        for (int i = 0; i < pFont.getGlyphCount(); i++) {
            addToTexture(pGraphicsOpenGL, i, pFont.getGlyph(i));
        }
    }

    public boolean addTexture(PGraphicsOpenGL pGraphicsOpenGL) {
        int i;
        boolean z;
        int i2 = this.maxSize;
        if (-1 >= this.lastTex || this.textures[this.lastTex].glHeight >= this.maxSize) {
            i = this.minSize;
            z = false;
        } else {
            i = PApplet.min(this.textures[this.lastTex].glHeight * 2, this.maxSize);
            z = true;
        }
        Texture texture = this.is3D ? new Texture(pGraphicsOpenGL, i2, i, new Texture.Parameters(2, 4, false)) : new Texture(pGraphicsOpenGL, i2, i, new Texture.Parameters(2, 3, false));
        if (this.textures == null) {
            this.textures = new Texture[1];
            this.textures[0] = texture;
            this.images = new PImage[1];
            this.images[0] = pGraphicsOpenGL.wrapTexture(texture);
            this.lastTex = 0;
        } else if (z) {
            texture.put(this.textures[this.lastTex]);
            this.textures[this.lastTex] = texture;
            pGraphicsOpenGL.setCache(this.images[this.lastTex], texture);
            this.images[this.lastTex].width = texture.width;
            this.images[this.lastTex].height = texture.height;
        } else {
            this.lastTex = this.textures.length;
            Texture[] textureArr = new Texture[(this.lastTex + 1)];
            PApplet.arrayCopy(this.textures, textureArr, this.textures.length);
            textureArr[this.lastTex] = texture;
            this.textures = textureArr;
            PImage[] pImageArr = new PImage[this.textures.length];
            PApplet.arrayCopy(this.images, pImageArr, this.images.length);
            pImageArr[this.lastTex] = pGraphicsOpenGL.wrapTexture(texture);
            this.images = pImageArr;
        }
        texture.bind();
        return z;
    }

    public TextureInfo addToTexture(PGraphicsOpenGL pGraphicsOpenGL, PFont.Glyph glyph) {
        int length = this.glyphTexinfos.length;
        if (length == 0) {
            this.glyphTexinfos = new TextureInfo[1];
        }
        addToTexture(pGraphicsOpenGL, length, glyph);
        return this.glyphTexinfos[length];
    }

    /* access modifiers changed from: protected */
    public void addToTexture(PGraphicsOpenGL pGraphicsOpenGL, int i, PFont.Glyph glyph) {
        int i2 = glyph.width + 1 + 1;
        int i3 = glyph.height + 1 + 1;
        int[] iArr = new int[(i2 * i3)];
        int i4 = 0;
        if (PGL.BIG_ENDIAN) {
            Arrays.fill(iArr, 0, i2, InputDeviceCompat.SOURCE_ANY);
            int i5 = i2;
            for (int i6 = 0; i6 < glyph.height; i6++) {
                int i7 = i5 + 1;
                iArr[i5] = -256;
                int i8 = 0;
                int i9 = i7;
                while (i8 < glyph.width) {
                    iArr[i9] = glyph.image.pixels[i4] | InputDeviceCompat.SOURCE_ANY;
                    i8++;
                    i9++;
                    i4++;
                }
                i5 = i9 + 1;
                iArr[i9] = -256;
            }
            Arrays.fill(iArr, (i3 - 1) * i2, i3 * i2, InputDeviceCompat.SOURCE_ANY);
        } else {
            Arrays.fill(iArr, 0, i2, ViewCompat.MEASURED_SIZE_MASK);
            int i10 = i2;
            for (int i11 = 0; i11 < glyph.height; i11++) {
                int i12 = i10 + 1;
                iArr[i10] = 16777215;
                int i13 = 0;
                int i14 = i12;
                while (i13 < glyph.width) {
                    iArr[i14] = (glyph.image.pixels[i4] << 24) | ViewCompat.MEASURED_SIZE_MASK;
                    i13++;
                    i14++;
                    i4++;
                }
                i10 = i14 + 1;
                iArr[i14] = 16777215;
            }
            Arrays.fill(iArr, (i3 - 1) * i2, i3 * i2, ViewCompat.MEASURED_SIZE_MASK);
        }
        if (this.offsetX + i2 > this.textures[this.lastTex].glWidth) {
            this.offsetX = 0;
            this.offsetY += this.lineHeight;
        }
        this.lineHeight = Math.max(this.lineHeight, i3);
        if (this.offsetY + this.lineHeight > this.textures[this.lastTex].glHeight) {
            if (addTexture(pGraphicsOpenGL)) {
                updateGlyphsTexCoords();
            } else {
                this.offsetX = 0;
                this.offsetY = 0;
                this.lineHeight = 0;
            }
        }
        TextureInfo textureInfo = new TextureInfo(this.lastTex, this.offsetX, this.offsetY, i2, i3, iArr);
        this.offsetX += i2;
        if (i == this.glyphTexinfos.length) {
            TextureInfo[] textureInfoArr = new TextureInfo[(this.glyphTexinfos.length + 1)];
            System.arraycopy(this.glyphTexinfos, 0, textureInfoArr, 0, this.glyphTexinfos.length);
            this.glyphTexinfos = textureInfoArr;
        }
        this.glyphTexinfos[i] = textureInfo;
        this.texinfoMap.put(glyph, textureInfo);
    }

    /* access modifiers changed from: protected */
    public void allocate() {
    }

    public void begin() {
    }

    public boolean contextIsOutdated() {
        boolean z = false;
        for (Texture contextIsOutdated : this.textures) {
            if (contextIsOutdated.contextIsOutdated()) {
                z = true;
            }
        }
        if (z) {
            for (Texture dispose : this.textures) {
                dispose.dispose();
            }
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void dispose() {
        for (Texture dispose : this.textures) {
            dispose.dispose();
        }
    }

    public void end() {
        for (Texture texture : this.textures) {
            this.pgl.disableTexturing(texture.glTarget);
        }
    }

    public TextureInfo getTexInfo(PFont.Glyph glyph) {
        return this.texinfoMap.get(glyph);
    }

    public PImage getTexture(TextureInfo textureInfo) {
        return this.images[textureInfo.texIndex];
    }

    /* access modifiers changed from: protected */
    public void initTexture(PGraphicsOpenGL pGraphicsOpenGL, PFont pFont) {
        this.lastTex = -1;
        int nextPowerOfTwo = PGL.nextPowerOfTwo(pFont.getSize());
        this.minSize = PApplet.min(PGraphicsOpenGL.maxTextureSize, PApplet.max(PGL.MIN_FONT_TEX_SIZE, nextPowerOfTwo));
        this.maxSize = PApplet.min(PGraphicsOpenGL.maxTextureSize, PApplet.max(PGL.MAX_FONT_TEX_SIZE, nextPowerOfTwo * 2));
        if (this.maxSize < nextPowerOfTwo) {
            PGraphics.showWarning("The font size is too large to be properly displayed with OpenGL");
        }
        addTexture(pGraphicsOpenGL);
        this.offsetX = 0;
        this.offsetY = 0;
        this.lineHeight = 0;
        this.texinfoMap = new HashMap<>();
        this.glyphTexinfos = new TextureInfo[pFont.getGlyphCount()];
        addAllGlyphsToTexture(pGraphicsOpenGL, pFont);
    }

    public void updateGlyphsTexCoords() {
        for (TextureInfo textureInfo : this.glyphTexinfos) {
            if (textureInfo != null && textureInfo.texIndex == this.lastTex) {
                textureInfo.updateUV();
            }
        }
    }
}
