package processing.opengl;

import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;

public class PShader implements PConstants {
    protected static final int COLOR = 3;
    protected static final int LIGHT = 4;
    protected static final int LINE = 1;
    protected static final int POINT = 0;
    protected static final int POLY = 2;
    protected static final int TEXLIGHT = 6;
    protected static final int TEXTURE = 5;
    protected static String colorShaderDefRegexp = "#define *PROCESSING_COLOR_SHADER";
    protected static String lightShaderDefRegexp = "#define *PROCESSING_LIGHT_SHADER";
    protected static String lineShaderAttrRegexp = "attribute *vec4 *direction";
    protected static String lineShaderDefRegexp = "#define *PROCESSING_LINE_SHADER";
    protected static String pointShaderAttrRegexp = "attribute *vec2 *offset";
    protected static String pointShaderDefRegexp = "#define *PROCESSING_POINT_SHADER";
    protected static String polyShaderDefRegexp = "#define *PROCESSING_POLYGON_SHADER";
    protected static String quadShaderAttrRegexp = "#define *PROCESSING_QUADS_SHADER";
    protected static String texShaderDefRegexp = "#define *PROCESSING_TEXTURE_SHADER";
    protected static String texlightShaderDefRegexp = "#define *PROCESSING_TEXLIGHT_SHADER";
    protected static String triShaderAttrRegexp = "#define *PROCESSING_TRIANGLES_SHADER";
    protected int ambientLoc;
    protected boolean bound;
    protected int colorLoc;
    protected int context;
    protected PGraphicsOpenGL currentPG;
    protected int directionLoc;
    protected int emissiveLoc;
    protected FloatBuffer floatBuffer;
    protected String fragmentFilename;
    protected String[] fragmentShaderSource;
    protected URL fragmentURL;
    public int glFragment;
    public int glProgram;
    public int glVertex;
    private PGraphicsOpenGL.GLResourceShader glres;
    protected IntBuffer intBuffer;
    protected int lightAmbientLoc;
    protected int lightCountLoc;
    protected int lightDiffuseLoc;
    protected int lightFalloffLoc;
    protected int lightNormalLoc;
    protected int lightPositionLoc;
    protected int lightSpecularLoc;
    protected int lightSpotLoc;
    protected boolean loadedAttributes;
    protected boolean loadedUniforms;
    protected int modelviewMatLoc;
    protected int normalLoc;
    protected int normalMatLoc;
    protected int offsetLoc;
    protected PApplet parent;
    protected int perspectiveLoc;
    protected PGL pgl;
    protected int ppixelsLoc;
    protected int ppixelsUnit;
    protected PGraphicsOpenGL primaryPG;
    protected int projectionMatLoc;
    protected int scaleLoc;
    protected int shininessLoc;
    protected int specularLoc;
    protected float[] tcmat;
    protected int texCoordLoc;
    protected int texMatrixLoc;
    protected int texOffsetLoc;
    protected int texUnit;
    protected HashMap<Integer, Integer> texUnits;
    protected Texture texture;
    protected int textureLoc;
    protected HashMap<Integer, Texture> textures;
    protected int transformMatLoc;
    protected int type;
    protected HashMap<Integer, UniformValue> uniformValues;
    protected String vertexFilename;
    protected int vertexLoc;
    protected String[] vertexShaderSource;
    protected URL vertexURL;
    protected int viewportLoc;

    protected static class UniformValue {
        static final int FLOAT1 = 4;
        static final int FLOAT1VEC = 12;
        static final int FLOAT2 = 5;
        static final int FLOAT2VEC = 13;
        static final int FLOAT3 = 6;
        static final int FLOAT3VEC = 14;
        static final int FLOAT4 = 7;
        static final int FLOAT4VEC = 15;
        static final int INT1 = 0;
        static final int INT1VEC = 8;
        static final int INT2 = 1;
        static final int INT2VEC = 9;
        static final int INT3 = 2;
        static final int INT3VEC = 10;
        static final int INT4 = 3;
        static final int INT4VEC = 11;
        static final int MAT2 = 16;
        static final int MAT3 = 17;
        static final int MAT4 = 18;
        static final int SAMPLER2D = 19;
        int type;
        Object value;

        UniformValue(int i, Object obj) {
            this.type = i;
            this.value = obj;
        }
    }

    public PShader() {
        this.uniformValues = null;
        this.loadedAttributes = false;
        this.loadedUniforms = false;
        this.parent = null;
        this.pgl = null;
        this.context = -1;
        this.vertexURL = null;
        this.fragmentURL = null;
        this.vertexFilename = null;
        this.fragmentFilename = null;
        this.glProgram = 0;
        this.glVertex = 0;
        this.glFragment = 0;
        this.intBuffer = PGL.allocateIntBuffer(1);
        this.floatBuffer = PGL.allocateFloatBuffer(1);
        this.bound = false;
        this.type = -1;
    }

    public PShader(PApplet pApplet) {
        this();
        this.parent = pApplet;
        this.primaryPG = (PGraphicsOpenGL) pApplet.g;
        this.pgl = this.primaryPG.pgl;
        this.context = this.pgl.createEmptyContext();
    }

    public PShader(PApplet pApplet, String str, String str2) {
        this.uniformValues = null;
        this.loadedAttributes = false;
        this.loadedUniforms = false;
        this.parent = pApplet;
        this.primaryPG = (PGraphicsOpenGL) pApplet.g;
        this.pgl = this.primaryPG.pgl;
        this.vertexURL = null;
        this.fragmentURL = null;
        this.vertexFilename = str;
        this.fragmentFilename = str2;
        this.fragmentShaderSource = this.pgl.loadFragmentShader(str2);
        this.vertexShaderSource = this.pgl.loadVertexShader(str);
        this.glProgram = 0;
        this.glVertex = 0;
        this.glFragment = 0;
        this.intBuffer = PGL.allocateIntBuffer(1);
        this.floatBuffer = PGL.allocateFloatBuffer(1);
        int shaderType = getShaderType(this.vertexShaderSource, -1);
        int shaderType2 = getShaderType(this.fragmentShaderSource, -1);
        if (shaderType == -1 && shaderType2 == -1) {
            this.type = 2;
        } else if (shaderType == -1) {
            this.type = shaderType2;
        } else if (shaderType2 == -1) {
            this.type = shaderType;
        } else if (shaderType2 == shaderType) {
            this.type = shaderType;
        } else {
            PGraphics.showWarning("The vertex and fragment shaders have different types");
        }
    }

    public PShader(PApplet pApplet, URL url, URL url2) {
        this.uniformValues = null;
        this.loadedAttributes = false;
        this.loadedUniforms = false;
        this.parent = pApplet;
        this.primaryPG = (PGraphicsOpenGL) pApplet.g;
        this.pgl = this.primaryPG.pgl;
        this.vertexURL = url;
        this.fragmentURL = url2;
        this.vertexFilename = null;
        this.fragmentFilename = null;
        this.fragmentShaderSource = this.pgl.loadFragmentShader(url2);
        this.vertexShaderSource = this.pgl.loadVertexShader(url);
        this.glProgram = 0;
        this.glVertex = 0;
        this.glFragment = 0;
        this.intBuffer = PGL.allocateIntBuffer(1);
        this.floatBuffer = PGL.allocateFloatBuffer(1);
        int shaderType = getShaderType(this.vertexShaderSource, -1);
        int shaderType2 = getShaderType(this.fragmentShaderSource, -1);
        if (shaderType == -1 && shaderType2 == -1) {
            this.type = 2;
        } else if (shaderType == -1) {
            this.type = shaderType2;
        } else if (shaderType2 == -1) {
            this.type = shaderType;
        } else if (shaderType2 == shaderType) {
            this.type = shaderType;
        } else {
            PGraphics.showWarning("The vertex and fragment shaders have different types");
        }
    }

    public PShader(PApplet pApplet, String[] strArr, String[] strArr2) {
        this.uniformValues = null;
        this.loadedAttributes = false;
        this.loadedUniforms = false;
        this.parent = pApplet;
        this.primaryPG = (PGraphicsOpenGL) pApplet.g;
        this.pgl = this.primaryPG.pgl;
        this.vertexURL = null;
        this.fragmentURL = null;
        this.vertexFilename = null;
        this.fragmentFilename = null;
        this.vertexShaderSource = strArr;
        this.fragmentShaderSource = strArr2;
        this.glProgram = 0;
        this.glVertex = 0;
        this.glFragment = 0;
        this.intBuffer = PGL.allocateIntBuffer(1);
        this.floatBuffer = PGL.allocateFloatBuffer(1);
        int shaderType = getShaderType(this.vertexShaderSource, -1);
        int shaderType2 = getShaderType(this.fragmentShaderSource, -1);
        if (shaderType == -1 && shaderType2 == -1) {
            this.type = 2;
        } else if (shaderType == -1) {
            this.type = shaderType2;
        } else if (shaderType2 == -1) {
            this.type = shaderType;
        } else if (shaderType2 == shaderType) {
            this.type = shaderType;
        } else {
            PGraphics.showWarning("The vertex and fragment shaders have different types");
        }
    }

    protected static int getShaderType(String[] strArr, int i) {
        for (String trim : strArr) {
            String trim2 = trim.trim();
            if (PApplet.match(trim2, pointShaderAttrRegexp) != null) {
                return 0;
            }
            if (PApplet.match(trim2, lineShaderAttrRegexp) != null) {
                return 1;
            }
            if (PApplet.match(trim2, pointShaderDefRegexp) != null) {
                return 0;
            }
            if (PApplet.match(trim2, lineShaderDefRegexp) != null) {
                return 1;
            }
            if (PApplet.match(trim2, colorShaderDefRegexp) != null) {
                return 3;
            }
            if (PApplet.match(trim2, lightShaderDefRegexp) != null) {
                return 4;
            }
            if (PApplet.match(trim2, texShaderDefRegexp) != null) {
                return 5;
            }
            if (PApplet.match(trim2, texlightShaderDefRegexp) != null) {
                return 6;
            }
            if (PApplet.match(trim2, polyShaderDefRegexp) != null) {
                return 2;
            }
            if (PApplet.match(trim2, triShaderAttrRegexp) != null) {
                return 2;
            }
            if (PApplet.match(trim2, quadShaderAttrRegexp) != null) {
                return 2;
            }
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public boolean accessLightAttribs() {
        return -1 < this.ambientLoc || -1 < this.specularLoc || -1 < this.emissiveLoc || -1 < this.shininessLoc;
    }

    /* access modifiers changed from: protected */
    public boolean accessNormals() {
        return -1 < this.normalLoc;
    }

    /* access modifiers changed from: protected */
    public boolean accessTexCoords() {
        return -1 < this.texCoordLoc;
    }

    public void bind() {
        init();
        if (!this.bound) {
            this.pgl.useProgram(this.glProgram);
            this.bound = true;
            consumeUniforms();
            bindTextures();
        }
        if (hasType()) {
            bindTyped();
        }
    }

    /* access modifiers changed from: protected */
    public void bindTextures() {
        if (this.textures != null && this.texUnits != null) {
            for (Integer intValue : this.textures.keySet()) {
                int intValue2 = intValue.intValue();
                Texture texture2 = this.textures.get(Integer.valueOf(intValue2));
                Integer num = this.texUnits.get(Integer.valueOf(intValue2));
                if (num != null) {
                    this.pgl.activeTexture(num.intValue() + PGL.TEXTURE0);
                    texture2.bind();
                } else {
                    throw new RuntimeException("Cannot find unit for texture " + texture2);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void bindTyped() {
        if (this.currentPG == null) {
            setRenderer(this.primaryPG.getCurrentPG());
            loadAttributes();
            loadUniforms();
        }
        setCommonUniforms();
        if (-1 < this.vertexLoc) {
            this.pgl.enableVertexAttribArray(this.vertexLoc);
        }
        if (-1 < this.colorLoc) {
            this.pgl.enableVertexAttribArray(this.colorLoc);
        }
        if (-1 < this.texCoordLoc) {
            this.pgl.enableVertexAttribArray(this.texCoordLoc);
        }
        if (-1 < this.normalLoc) {
            this.pgl.enableVertexAttribArray(this.normalLoc);
        }
        if (-1 < this.normalMatLoc) {
            this.currentPG.updateGLNormal();
            setUniformMatrix(this.normalMatLoc, this.currentPG.glNormal);
        }
        if (-1 < this.ambientLoc) {
            this.pgl.enableVertexAttribArray(this.ambientLoc);
        }
        if (-1 < this.specularLoc) {
            this.pgl.enableVertexAttribArray(this.specularLoc);
        }
        if (-1 < this.emissiveLoc) {
            this.pgl.enableVertexAttribArray(this.emissiveLoc);
        }
        if (-1 < this.shininessLoc) {
            this.pgl.enableVertexAttribArray(this.shininessLoc);
        }
        int i = this.currentPG.lightCount;
        setUniformValue(this.lightCountLoc, i);
        if (i > 0) {
            setUniformVector(this.lightPositionLoc, this.currentPG.lightPosition, 4, i);
            setUniformVector(this.lightNormalLoc, this.currentPG.lightNormal, 3, i);
            setUniformVector(this.lightAmbientLoc, this.currentPG.lightAmbient, 3, i);
            setUniformVector(this.lightDiffuseLoc, this.currentPG.lightDiffuse, 3, i);
            setUniformVector(this.lightSpecularLoc, this.currentPG.lightSpecular, 3, i);
            setUniformVector(this.lightFalloffLoc, this.currentPG.lightFalloffCoefficients, 3, i);
            setUniformVector(this.lightSpotLoc, this.currentPG.lightSpotParameters, 2, i);
        }
        if (-1 < this.directionLoc) {
            this.pgl.enableVertexAttribArray(this.directionLoc);
        }
        if (-1 < this.offsetLoc) {
            this.pgl.enableVertexAttribArray(this.offsetLoc);
        }
        if (-1 < this.perspectiveLoc) {
            if (!this.currentPG.getHint(7) || !this.currentPG.nonOrthoProjection()) {
                setUniformValue(this.perspectiveLoc, 0);
            } else {
                setUniformValue(this.perspectiveLoc, 1);
            }
        }
        if (-1 >= this.scaleLoc) {
            return;
        }
        if (this.currentPG.getHint(6)) {
            setUniformValue(this.scaleLoc, 1.0f, 1.0f, 1.0f);
            return;
        }
        float f = PGL.STROKE_DISPLACEMENT;
        if (this.currentPG.orthoProjection()) {
            setUniformValue(this.scaleLoc, 1.0f, 1.0f, f);
        } else {
            setUniformValue(this.scaleLoc, f, f, f);
        }
    }

    public boolean bound() {
        return this.bound;
    }

    /* access modifiers changed from: protected */
    public boolean checkPolyType(int i) {
        if (getType() == 2 || getType() == i) {
            return true;
        }
        if (i == 6) {
            PGraphics.showWarning("Your shader needs to be of TEXLIGHT type to render this geometry properly, using default shader instead.");
        } else if (i == 4) {
            PGraphics.showWarning("Your shader needs to be of LIGHT type to render this geometry properly, using default shader instead.");
        } else if (i == 5) {
            PGraphics.showWarning("Your shader needs to be of TEXTURE type to render this geometry properly, using default shader instead.");
        } else if (i == 3) {
            PGraphics.showWarning("Your shader needs to be of COLOR type to render this geometry properly, using default shader instead.");
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean compile() {
        boolean z;
        boolean z2;
        if (hasVertexShader()) {
            z = compileVertexShader();
        } else {
            PGraphics.showException("Doesn't have a vertex shader");
            z = true;
        }
        if (hasFragmentShader()) {
            z2 = compileFragmentShader();
        } else {
            PGraphics.showException("Doesn't have a fragment shader");
            z2 = true;
        }
        return z && z2;
    }

    /* access modifiers changed from: protected */
    public boolean compileFragmentShader() {
        this.pgl.shaderSource(this.glFragment, PApplet.join(this.fragmentShaderSource, "\n"));
        this.pgl.compileShader(this.glFragment);
        this.pgl.getShaderiv(this.glFragment, PGL.COMPILE_STATUS, this.intBuffer);
        if (this.intBuffer.get(0) != 0) {
            return true;
        }
        PGraphics.showException("Cannot compile fragment shader:\n" + this.pgl.getShaderInfoLog(this.glFragment));
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean compileVertexShader() {
        this.pgl.shaderSource(this.glVertex, PApplet.join(this.vertexShaderSource, "\n"));
        this.pgl.compileShader(this.glVertex);
        this.pgl.getShaderiv(this.glVertex, PGL.COMPILE_STATUS, this.intBuffer);
        if (this.intBuffer.get(0) != 0) {
            return true;
        }
        PGraphics.showException("Cannot compile vertex shader:\n" + this.pgl.getShaderInfoLog(this.glVertex));
        return false;
    }

    /* access modifiers changed from: protected */
    public void consumeUniforms() {
        if (this.uniformValues != null && this.uniformValues.size() > 0) {
            int i = 0;
            for (Integer next : this.uniformValues.keySet()) {
                UniformValue uniformValue = this.uniformValues.get(next);
                if (uniformValue.type == 0) {
                    this.pgl.uniform1i(next.intValue(), ((int[]) uniformValue.value)[0]);
                } else if (uniformValue.type == 1) {
                    int[] iArr = (int[]) uniformValue.value;
                    this.pgl.uniform2i(next.intValue(), iArr[0], iArr[1]);
                } else if (uniformValue.type == 2) {
                    int[] iArr2 = (int[]) uniformValue.value;
                    this.pgl.uniform3i(next.intValue(), iArr2[0], iArr2[1], iArr2[2]);
                } else if (uniformValue.type == 3) {
                    int[] iArr3 = (int[]) uniformValue.value;
                    this.pgl.uniform4i(next.intValue(), iArr3[0], iArr3[1], iArr3[2], iArr3[3]);
                } else if (uniformValue.type == 4) {
                    this.pgl.uniform1f(next.intValue(), ((float[]) uniformValue.value)[0]);
                } else if (uniformValue.type == 5) {
                    float[] fArr = (float[]) uniformValue.value;
                    this.pgl.uniform2f(next.intValue(), fArr[0], fArr[1]);
                } else if (uniformValue.type == 6) {
                    float[] fArr2 = (float[]) uniformValue.value;
                    this.pgl.uniform3f(next.intValue(), fArr2[0], fArr2[1], fArr2[2]);
                } else if (uniformValue.type == 7) {
                    float[] fArr3 = (float[]) uniformValue.value;
                    this.pgl.uniform4f(next.intValue(), fArr3[0], fArr3[1], fArr3[2], fArr3[3]);
                } else if (uniformValue.type == 8) {
                    int[] iArr4 = (int[]) uniformValue.value;
                    updateIntBuffer(iArr4);
                    this.pgl.uniform1iv(next.intValue(), iArr4.length, this.intBuffer);
                } else if (uniformValue.type == 9) {
                    int[] iArr5 = (int[]) uniformValue.value;
                    updateIntBuffer(iArr5);
                    this.pgl.uniform2iv(next.intValue(), iArr5.length / 2, this.intBuffer);
                } else if (uniformValue.type == 10) {
                    int[] iArr6 = (int[]) uniformValue.value;
                    updateIntBuffer(iArr6);
                    this.pgl.uniform3iv(next.intValue(), iArr6.length / 3, this.intBuffer);
                } else if (uniformValue.type == 11) {
                    int[] iArr7 = (int[]) uniformValue.value;
                    updateIntBuffer(iArr7);
                    this.pgl.uniform4iv(next.intValue(), iArr7.length / 4, this.intBuffer);
                } else if (uniformValue.type == 12) {
                    float[] fArr4 = (float[]) uniformValue.value;
                    updateFloatBuffer(fArr4);
                    this.pgl.uniform1fv(next.intValue(), fArr4.length, this.floatBuffer);
                } else if (uniformValue.type == 13) {
                    float[] fArr5 = (float[]) uniformValue.value;
                    updateFloatBuffer(fArr5);
                    this.pgl.uniform2fv(next.intValue(), fArr5.length / 2, this.floatBuffer);
                } else if (uniformValue.type == 14) {
                    float[] fArr6 = (float[]) uniformValue.value;
                    updateFloatBuffer(fArr6);
                    this.pgl.uniform3fv(next.intValue(), fArr6.length / 3, this.floatBuffer);
                } else if (uniformValue.type == 15) {
                    float[] fArr7 = (float[]) uniformValue.value;
                    updateFloatBuffer(fArr7);
                    this.pgl.uniform4fv(next.intValue(), fArr7.length / 4, this.floatBuffer);
                } else if (uniformValue.type == 16) {
                    updateFloatBuffer((float[]) uniformValue.value);
                    this.pgl.uniformMatrix2fv(next.intValue(), 1, false, this.floatBuffer);
                } else if (uniformValue.type == 17) {
                    updateFloatBuffer((float[]) uniformValue.value);
                    this.pgl.uniformMatrix3fv(next.intValue(), 1, false, this.floatBuffer);
                } else if (uniformValue.type == 18) {
                    updateFloatBuffer((float[]) uniformValue.value);
                    this.pgl.uniformMatrix4fv(next.intValue(), 1, false, this.floatBuffer);
                } else if (uniformValue.type == 19) {
                    Texture texture2 = this.currentPG.getTexture((PImage) uniformValue.value);
                    if (this.textures == null) {
                        this.textures = new HashMap<>();
                    }
                    this.textures.put(next, texture2);
                    if (this.texUnits == null) {
                        this.texUnits = new HashMap<>();
                    }
                    if (this.texUnits.containsKey(next)) {
                        i = this.texUnits.get(next).intValue();
                        this.pgl.uniform1i(next.intValue(), i);
                    } else {
                        this.texUnits.put(next, Integer.valueOf(i));
                        this.pgl.uniform1i(next.intValue(), i);
                    }
                    i++;
                }
                i = i;
            }
            this.uniformValues.clear();
        }
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
        this.glres = new PGraphicsOpenGL.GLResourceShader(this);
    }

    /* access modifiers changed from: protected */
    public void dispose() {
        if (this.glres != null) {
            this.glres.dispose();
            this.glVertex = 0;
            this.glFragment = 0;
            this.glProgram = 0;
            this.glres = null;
        }
    }

    /* access modifiers changed from: protected */
    public void draw(int i, int i2, int i3) {
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, i);
        this.pgl.drawElements(PGL.TRIANGLES, i2, PGL.INDEX_TYPE, PGL.SIZEOF_INDEX * i3);
        this.pgl.bindBuffer(PGL.ELEMENT_ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public int getAttributeLoc(String str) {
        init();
        return this.pgl.getAttribLocation(this.glProgram, str);
    }

    /* access modifiers changed from: protected */
    public int getLastTexUnit() {
        if (this.texUnits == null) {
            return -1;
        }
        return this.texUnits.size() - 1;
    }

    /* access modifiers changed from: protected */
    public int getType() {
        return this.type;
    }

    /* access modifiers changed from: protected */
    public int getUniformLoc(String str) {
        init();
        return this.pgl.getUniformLocation(this.glProgram, str);
    }

    /* access modifiers changed from: protected */
    public boolean hasFragmentShader() {
        return this.fragmentShaderSource != null && this.fragmentShaderSource.length > 0;
    }

    /* access modifiers changed from: protected */
    public boolean hasType() {
        return this.type >= 0 && this.type <= 6;
    }

    /* access modifiers changed from: protected */
    public boolean hasVertexShader() {
        return this.vertexShaderSource != null && this.vertexShaderSource.length > 0;
    }

    public void init() {
        if (this.glProgram == 0 || contextIsOutdated()) {
            create();
            if (compile()) {
                this.pgl.attachShader(this.glProgram, this.glVertex);
                this.pgl.attachShader(this.glProgram, this.glFragment);
                setup();
                this.pgl.linkProgram(this.glProgram);
                validate();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isLineShader() {
        return this.type == 1;
    }

    /* access modifiers changed from: protected */
    public boolean isPointShader() {
        return this.type == 0;
    }

    /* access modifiers changed from: protected */
    public boolean isPolyShader() {
        return 2 <= this.type && this.type <= 6;
    }

    /* access modifiers changed from: protected */
    public void loadAttributes() {
        if (!this.loadedAttributes) {
            this.vertexLoc = getAttributeLoc("vertex");
            if (this.vertexLoc == -1) {
                this.vertexLoc = getAttributeLoc("position");
            }
            this.colorLoc = getAttributeLoc("color");
            this.texCoordLoc = getAttributeLoc("texCoord");
            this.normalLoc = getAttributeLoc("normal");
            this.ambientLoc = getAttributeLoc("ambient");
            this.specularLoc = getAttributeLoc("specular");
            this.emissiveLoc = getAttributeLoc("emissive");
            this.shininessLoc = getAttributeLoc("shininess");
            this.directionLoc = getAttributeLoc("direction");
            this.offsetLoc = getAttributeLoc("offset");
            this.directionLoc = getAttributeLoc("direction");
            this.offsetLoc = getAttributeLoc("offset");
            this.loadedAttributes = true;
        }
    }

    /* access modifiers changed from: protected */
    public void loadUniforms() {
        if (!this.loadedUniforms) {
            this.transformMatLoc = getUniformLoc("transform");
            if (this.transformMatLoc == -1) {
                this.transformMatLoc = getUniformLoc("transformMatrix");
            }
            this.modelviewMatLoc = getUniformLoc("modelview");
            if (this.modelviewMatLoc == -1) {
                this.modelviewMatLoc = getUniformLoc("modelviewMatrix");
            }
            this.projectionMatLoc = getUniformLoc("projection");
            if (this.projectionMatLoc == -1) {
                this.projectionMatLoc = getUniformLoc("projectionMatrix");
            }
            this.viewportLoc = getUniformLoc("viewport");
            this.ppixelsLoc = getUniformLoc("ppixels");
            this.normalMatLoc = getUniformLoc("normalMatrix");
            this.lightCountLoc = getUniformLoc("lightCount");
            this.lightPositionLoc = getUniformLoc("lightPosition");
            this.lightNormalLoc = getUniformLoc("lightNormal");
            this.lightAmbientLoc = getUniformLoc("lightAmbient");
            this.lightDiffuseLoc = getUniformLoc("lightDiffuse");
            this.lightSpecularLoc = getUniformLoc("lightSpecular");
            this.lightFalloffLoc = getUniformLoc("lightFalloff");
            this.lightSpotLoc = getUniformLoc("lightSpot");
            this.textureLoc = getUniformLoc("texture");
            if (this.textureLoc == -1) {
                this.textureLoc = getUniformLoc("texMap");
            }
            this.texMatrixLoc = getUniformLoc("texMatrix");
            this.texOffsetLoc = getUniformLoc("texOffset");
            this.perspectiveLoc = getUniformLoc("perspective");
            this.scaleLoc = getUniformLoc("scale");
            this.loadedUniforms = true;
        }
    }

    public void set(String str, float f) {
        setUniformImpl(str, 4, new float[]{f});
    }

    public void set(String str, float f, float f2) {
        setUniformImpl(str, 5, new float[]{f, f2});
    }

    public void set(String str, float f, float f2, float f3) {
        setUniformImpl(str, 6, new float[]{f, f2, f3});
    }

    public void set(String str, float f, float f2, float f3, float f4) {
        setUniformImpl(str, 7, new float[]{f, f2, f3, f4});
    }

    public void set(String str, int i) {
        setUniformImpl(str, 0, new int[]{i});
    }

    public void set(String str, int i, int i2) {
        setUniformImpl(str, 1, new int[]{i, i2});
    }

    public void set(String str, int i, int i2, int i3) {
        setUniformImpl(str, 2, new int[]{i, i2, i3});
    }

    public void set(String str, int i, int i2, int i3, int i4) {
        setUniformImpl(str, 3, new int[]{i, i2, i3, i4});
    }

    public void set(String str, PImage pImage) {
        setUniformImpl(str, 19, pImage);
    }

    public void set(String str, PMatrix2D pMatrix2D) {
        setUniformImpl(str, 16, new float[]{pMatrix2D.m00, pMatrix2D.m01, pMatrix2D.m10, pMatrix2D.m11});
    }

    public void set(String str, PMatrix3D pMatrix3D) {
        set(str, pMatrix3D, false);
    }

    public void set(String str, PMatrix3D pMatrix3D, boolean z) {
        if (z) {
            setUniformImpl(str, 17, new float[]{pMatrix3D.m00, pMatrix3D.m01, pMatrix3D.m02, pMatrix3D.m10, pMatrix3D.m11, pMatrix3D.m12, pMatrix3D.m20, pMatrix3D.m21, pMatrix3D.m22});
            return;
        }
        setUniformImpl(str, 18, new float[]{pMatrix3D.m00, pMatrix3D.m01, pMatrix3D.m02, pMatrix3D.m03, pMatrix3D.m10, pMatrix3D.m11, pMatrix3D.m12, pMatrix3D.m13, pMatrix3D.m20, pMatrix3D.m21, pMatrix3D.m22, pMatrix3D.m23, pMatrix3D.m30, pMatrix3D.m31, pMatrix3D.m32, pMatrix3D.m33});
    }

    public void set(String str, PVector pVector) {
        setUniformImpl(str, 6, new float[]{pVector.x, pVector.y, pVector.z});
    }

    public void set(String str, boolean z) {
        int i = 1;
        int[] iArr = new int[1];
        if (!z) {
            i = 0;
        }
        iArr[0] = i;
        setUniformImpl(str, 0, iArr);
    }

    public void set(String str, boolean z, boolean z2) {
        int i = 0;
        int[] iArr = new int[2];
        iArr[0] = z ? 1 : 0;
        if (z2) {
            i = 1;
        }
        iArr[1] = i;
        setUniformImpl(str, 1, iArr);
    }

    public void set(String str, boolean z, boolean z2, boolean z3) {
        int i = 1;
        int[] iArr = new int[3];
        iArr[0] = z ? 1 : 0;
        iArr[1] = z2 ? 1 : 0;
        if (!z3) {
            i = 0;
        }
        iArr[2] = i;
        setUniformImpl(str, 2, iArr);
    }

    public void set(String str, boolean z, boolean z2, boolean z3, boolean z4) {
        int i = 1;
        int[] iArr = new int[4];
        iArr[0] = z ? 1 : 0;
        iArr[1] = z2 ? 1 : 0;
        iArr[2] = z3 ? 1 : 0;
        if (!z4) {
            i = 0;
        }
        iArr[3] = i;
        setUniformImpl(str, 3, iArr);
    }

    public void set(String str, float[] fArr) {
        set(str, fArr, 1);
    }

    public void set(String str, float[] fArr, int i) {
        if (i == 1) {
            setUniformImpl(str, 12, fArr);
        } else if (i == 2) {
            setUniformImpl(str, 13, fArr);
        } else if (i == 3) {
            setUniformImpl(str, 14, fArr);
        } else if (i == 4) {
            setUniformImpl(str, 15, fArr);
        } else if (4 < i) {
            PGraphics.showWarning("Only up to 4 coordinates per element are supported.");
        } else {
            PGraphics.showWarning("Wrong number of coordinates: it is negative!");
        }
    }

    public void set(String str, int[] iArr) {
        set(str, iArr, 1);
    }

    public void set(String str, int[] iArr, int i) {
        if (i == 1) {
            setUniformImpl(str, 8, iArr);
        } else if (i == 2) {
            setUniformImpl(str, 9, iArr);
        } else if (i == 3) {
            setUniformImpl(str, 10, iArr);
        } else if (i == 4) {
            setUniformImpl(str, 11, iArr);
        } else if (4 < i) {
            PGraphics.showWarning("Only up to 4 coordinates per element are supported.");
        } else {
            PGraphics.showWarning("Wrong number of coordinates: it is negative!");
        }
    }

    public void set(String str, boolean[] zArr) {
        set(str, zArr, 1);
    }

    public void set(String str, boolean[] zArr, int i) {
        int[] iArr = new int[zArr.length];
        for (int i2 = 0; i2 < zArr.length; i2++) {
            iArr[i2] = zArr[i2] ? 1 : 0;
        }
        set(str, iArr, i);
    }

    /* access modifiers changed from: protected */
    public void setAmbientAttribute(int i, int i2, int i3, int i4, int i5) {
        setAttributeVBO(this.ambientLoc, i, i2, i3, true, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void setAttributeVBO(int i, int i2, int i3, int i4, boolean z, int i5, int i6) {
        if (-1 < i) {
            this.pgl.bindBuffer(PGL.ARRAY_BUFFER, i2);
            this.pgl.vertexAttribPointer(i, i3, i4, z, i5, i6);
        }
    }

    /* access modifiers changed from: protected */
    public void setColorAttribute(int i, int i2, int i3, int i4, int i5) {
        setAttributeVBO(this.colorLoc, i, i2, i3, true, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void setCommonUniforms() {
        if (-1 < this.transformMatLoc) {
            this.currentPG.updateGLProjmodelview();
            setUniformMatrix(this.transformMatLoc, this.currentPG.glProjmodelview);
        }
        if (-1 < this.modelviewMatLoc) {
            this.currentPG.updateGLModelview();
            setUniformMatrix(this.modelviewMatLoc, this.currentPG.glModelview);
        }
        if (-1 < this.projectionMatLoc) {
            this.currentPG.updateGLProjection();
            setUniformMatrix(this.projectionMatLoc, this.currentPG.glProjection);
        }
        if (-1 < this.viewportLoc) {
            setUniformValue(this.viewportLoc, (float) this.currentPG.viewport.get(0), (float) this.currentPG.viewport.get(1), (float) this.currentPG.viewport.get(2), (float) this.currentPG.viewport.get(3));
        }
        if (-1 < this.ppixelsLoc) {
            this.ppixelsUnit = getLastTexUnit() + 1;
            setUniformValue(this.ppixelsLoc, this.ppixelsUnit);
            this.pgl.activeTexture(PGL.TEXTURE0 + this.ppixelsUnit);
            this.currentPG.bindFrontTexture();
            return;
        }
        this.ppixelsUnit = -1;
    }

    /* access modifiers changed from: protected */
    public void setEmissiveAttribute(int i, int i2, int i3, int i4, int i5) {
        setAttributeVBO(this.emissiveLoc, i, i2, i3, true, i4, i5);
    }

    public void setFragmentShader(String str) {
        this.fragmentFilename = str;
        this.fragmentShaderSource = this.pgl.loadFragmentShader(str);
    }

    public void setFragmentShader(URL url) {
        this.fragmentURL = url;
        this.fragmentShaderSource = this.pgl.loadFragmentShader(url);
    }

    public void setFragmentShader(String[] strArr) {
        this.fragmentShaderSource = strArr;
    }

    /* access modifiers changed from: protected */
    public void setLineAttribute(int i, int i2, int i3, int i4, int i5) {
        setAttributeVBO(this.directionLoc, i, i2, i3, false, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void setNormalAttribute(int i, int i2, int i3, int i4, int i5) {
        setAttributeVBO(this.normalLoc, i, i2, i3, false, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void setPointAttribute(int i, int i2, int i3, int i4, int i5) {
        setAttributeVBO(this.offsetLoc, i, i2, i3, false, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void setRenderer(PGraphicsOpenGL pGraphicsOpenGL) {
        this.currentPG = pGraphicsOpenGL;
    }

    /* access modifiers changed from: protected */
    public void setShininessAttribute(int i, int i2, int i3, int i4, int i5) {
        setAttributeVBO(this.shininessLoc, i, i2, i3, false, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void setSpecularAttribute(int i, int i2, int i3, int i4, int i5) {
        setAttributeVBO(this.specularLoc, i, i2, i3, true, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void setTexcoordAttribute(int i, int i2, int i3, int i4, int i5) {
        setAttributeVBO(this.texCoordLoc, i, i2, i3, false, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void setTexture(Texture texture2) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        this.texture = texture2;
        if (texture2 != null) {
            if (texture2.invertedX()) {
                f5 = 1.0f;
                f6 = -1.0f;
            } else {
                f5 = 0.0f;
                f6 = 1.0f;
            }
            if (texture2.invertedY()) {
                f8 = -1.0f;
                f7 = 1.0f;
            } else {
                f7 = 0.0f;
                f8 = 1.0f;
            }
            float maxTexcoordU = f6 * texture2.maxTexcoordU();
            float maxTexcoordU2 = f5 * texture2.maxTexcoordU();
            float maxTexcoordV = f8 * texture2.maxTexcoordV();
            float maxTexcoordV2 = f7 * texture2.maxTexcoordV();
            setUniformValue(this.texOffsetLoc, 1.0f / ((float) texture2.width), 1.0f / ((float) texture2.height));
            if (-1 < this.textureLoc) {
                this.texUnit = -1 < this.ppixelsUnit ? this.ppixelsUnit + 1 : getLastTexUnit() + 1;
                setUniformValue(this.textureLoc, this.texUnit);
                this.pgl.activeTexture(PGL.TEXTURE0 + this.texUnit);
                texture2.bind();
            }
            f = maxTexcoordV2;
            f4 = maxTexcoordU2;
            f3 = maxTexcoordU;
            f2 = maxTexcoordV;
        } else {
            f = 0.0f;
            f2 = 1.0f;
            f3 = 1.0f;
            f4 = 0.0f;
        }
        if (-1 < this.texMatrixLoc) {
            if (this.tcmat == null) {
                this.tcmat = new float[16];
            }
            this.tcmat[0] = f3;
            this.tcmat[4] = 0.0f;
            this.tcmat[8] = 0.0f;
            this.tcmat[12] = f4;
            this.tcmat[1] = 0.0f;
            this.tcmat[5] = f2;
            this.tcmat[9] = 0.0f;
            this.tcmat[13] = f;
            this.tcmat[2] = 0.0f;
            this.tcmat[6] = 0.0f;
            this.tcmat[10] = 0.0f;
            this.tcmat[14] = 0.0f;
            this.tcmat[3] = 0.0f;
            this.tcmat[7] = 0.0f;
            this.tcmat[11] = 0.0f;
            this.tcmat[15] = 0.0f;
            setUniformMatrix(this.texMatrixLoc, this.tcmat);
        }
    }

    /* access modifiers changed from: protected */
    public void setType(int i) {
        this.type = i;
    }

    /* access modifiers changed from: protected */
    public void setUniformImpl(String str, int i, Object obj) {
        int uniformLoc = getUniformLoc(str);
        if (-1 < uniformLoc) {
            if (this.uniformValues == null) {
                this.uniformValues = new HashMap<>();
            }
            this.uniformValues.put(Integer.valueOf(uniformLoc), new UniformValue(i, obj));
            return;
        }
        PGraphics.showWarning("The shader doesn't have a uniform called \"" + str + "\" OR the uniform was removed during compilation because it was unused.");
    }

    /* access modifiers changed from: protected */
    public void setUniformMatrix(int i, float[] fArr) {
        if (-1 < i) {
            updateFloatBuffer(fArr);
            if (fArr.length == 4) {
                this.pgl.uniformMatrix2fv(i, 1, false, this.floatBuffer);
            } else if (fArr.length == 9) {
                this.pgl.uniformMatrix3fv(i, 1, false, this.floatBuffer);
            } else if (fArr.length == 16) {
                this.pgl.uniformMatrix4fv(i, 1, false, this.floatBuffer);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setUniformTex(int i, Texture texture2) {
        if (this.texUnits != null) {
            Integer num = this.texUnits.get(Integer.valueOf(i));
            if (num != null) {
                this.pgl.activeTexture(num.intValue() + PGL.TEXTURE0);
                texture2.bind();
                return;
            }
            throw new RuntimeException("Cannot find unit for texture " + texture2);
        }
    }

    /* access modifiers changed from: protected */
    public void setUniformValue(int i, float f) {
        if (-1 < i) {
            this.pgl.uniform1f(i, f);
        }
    }

    /* access modifiers changed from: protected */
    public void setUniformValue(int i, float f, float f2) {
        if (-1 < i) {
            this.pgl.uniform2f(i, f, f2);
        }
    }

    /* access modifiers changed from: protected */
    public void setUniformValue(int i, float f, float f2, float f3) {
        if (-1 < i) {
            this.pgl.uniform3f(i, f, f2, f3);
        }
    }

    /* access modifiers changed from: protected */
    public void setUniformValue(int i, float f, float f2, float f3, float f4) {
        if (-1 < i) {
            this.pgl.uniform4f(i, f, f2, f3, f4);
        }
    }

    /* access modifiers changed from: protected */
    public void setUniformValue(int i, int i2) {
        if (-1 < i) {
            this.pgl.uniform1i(i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public void setUniformValue(int i, int i2, int i3) {
        if (-1 < i) {
            this.pgl.uniform2i(i, i2, i3);
        }
    }

    /* access modifiers changed from: protected */
    public void setUniformValue(int i, int i2, int i3, int i4) {
        if (-1 < i) {
            this.pgl.uniform3i(i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: protected */
    public void setUniformValue(int i, int i2, int i3, int i4, int i5) {
        if (-1 < i) {
            this.pgl.uniform4i(i, i2, i3, i4, i5);
        }
    }

    /* access modifiers changed from: protected */
    public void setUniformVector(int i, float[] fArr, int i2, int i3) {
        if (-1 < i) {
            updateFloatBuffer(fArr);
            if (i2 == 1) {
                this.pgl.uniform1fv(i, i3, this.floatBuffer);
            } else if (i2 == 2) {
                this.pgl.uniform2fv(i, i3, this.floatBuffer);
            } else if (i2 == 3) {
                this.pgl.uniform3fv(i, i3, this.floatBuffer);
            } else if (i2 == 4) {
                this.pgl.uniform4fv(i, i3, this.floatBuffer);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setUniformVector(int i, int[] iArr, int i2, int i3) {
        if (-1 < i) {
            updateIntBuffer(iArr);
            if (i2 == 1) {
                this.pgl.uniform1iv(i, i3, this.intBuffer);
            } else if (i2 == 2) {
                this.pgl.uniform2iv(i, i3, this.intBuffer);
            } else if (i2 == 3) {
                this.pgl.uniform3iv(i, i3, this.intBuffer);
            } else if (i2 == 4) {
                this.pgl.uniform3iv(i, i3, this.intBuffer);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setVertexAttribute(int i, int i2, int i3, int i4, int i5) {
        setAttributeVBO(this.vertexLoc, i, i2, i3, false, i4, i5);
    }

    public void setVertexShader(String str) {
        this.vertexFilename = str;
        this.vertexShaderSource = this.pgl.loadVertexShader(str);
    }

    public void setVertexShader(URL url) {
        this.vertexURL = url;
        this.vertexShaderSource = this.pgl.loadVertexShader(url);
    }

    public void setVertexShader(String[] strArr) {
        this.vertexShaderSource = strArr;
    }

    /* access modifiers changed from: protected */
    public void setup() {
    }

    /* access modifiers changed from: protected */
    public boolean supportLighting() {
        return -1 < this.lightCountLoc || -1 < this.lightPositionLoc || -1 < this.lightNormalLoc;
    }

    /* access modifiers changed from: protected */
    public boolean supportsTexturing() {
        return -1 < this.textureLoc;
    }

    public void unbind() {
        if (hasType()) {
            unbindTyped();
        }
        if (this.bound) {
            unbindTextures();
            this.pgl.useProgram(0);
            this.bound = false;
        }
    }

    /* access modifiers changed from: protected */
    public void unbindTextures() {
        if (this.textures != null && this.texUnits != null) {
            for (Integer intValue : this.textures.keySet()) {
                int intValue2 = intValue.intValue();
                Texture texture2 = this.textures.get(Integer.valueOf(intValue2));
                Integer num = this.texUnits.get(Integer.valueOf(intValue2));
                if (num != null) {
                    this.pgl.activeTexture(num.intValue() + PGL.TEXTURE0);
                    texture2.unbind();
                } else {
                    throw new RuntimeException("Cannot find unit for texture " + texture2);
                }
            }
            this.pgl.activeTexture(PGL.TEXTURE0);
        }
    }

    /* access modifiers changed from: protected */
    public void unbindTyped() {
        if (-1 < this.offsetLoc) {
            this.pgl.disableVertexAttribArray(this.offsetLoc);
        }
        if (-1 < this.directionLoc) {
            this.pgl.disableVertexAttribArray(this.directionLoc);
        }
        if (-1 < this.textureLoc && this.texture != null) {
            this.pgl.activeTexture(PGL.TEXTURE0 + this.texUnit);
            this.texture.unbind();
            this.pgl.activeTexture(PGL.TEXTURE0);
            this.texture = null;
        }
        if (-1 < this.ambientLoc) {
            this.pgl.disableVertexAttribArray(this.ambientLoc);
        }
        if (-1 < this.specularLoc) {
            this.pgl.disableVertexAttribArray(this.specularLoc);
        }
        if (-1 < this.emissiveLoc) {
            this.pgl.disableVertexAttribArray(this.emissiveLoc);
        }
        if (-1 < this.shininessLoc) {
            this.pgl.disableVertexAttribArray(this.shininessLoc);
        }
        if (-1 < this.vertexLoc) {
            this.pgl.disableVertexAttribArray(this.vertexLoc);
        }
        if (-1 < this.colorLoc) {
            this.pgl.disableVertexAttribArray(this.colorLoc);
        }
        if (-1 < this.texCoordLoc) {
            this.pgl.disableVertexAttribArray(this.texCoordLoc);
        }
        if (-1 < this.normalLoc) {
            this.pgl.disableVertexAttribArray(this.normalLoc);
        }
        if (-1 < this.ppixelsLoc) {
            this.pgl.enableFBOLayer();
            this.pgl.activeTexture(PGL.TEXTURE0 + this.ppixelsUnit);
            this.currentPG.unbindFrontTexture();
            this.pgl.activeTexture(PGL.TEXTURE0);
        }
        this.pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
    }

    /* access modifiers changed from: protected */
    public void updateFloatBuffer(float[] fArr) {
        this.floatBuffer = PGL.updateFloatBuffer(this.floatBuffer, fArr, false);
    }

    /* access modifiers changed from: protected */
    public void updateIntBuffer(int[] iArr) {
        this.intBuffer = PGL.updateIntBuffer(this.intBuffer, iArr, false);
    }

    /* access modifiers changed from: protected */
    public void validate() {
        boolean z = false;
        this.pgl.getProgramiv(this.glProgram, PGL.LINK_STATUS, this.intBuffer);
        if (!(this.intBuffer.get(0) != 0)) {
            PGraphics.showException("Cannot link shader program:\n" + this.pgl.getProgramInfoLog(this.glProgram));
        }
        this.pgl.validateProgram(this.glProgram);
        this.pgl.getProgramiv(this.glProgram, PGL.VALIDATE_STATUS, this.intBuffer);
        if (this.intBuffer.get(0) != 0) {
            z = true;
        }
        if (!z) {
            PGraphics.showException("Cannot validate shader program:\n" + this.pgl.getProgramInfoLog(this.glProgram));
        }
    }
}
