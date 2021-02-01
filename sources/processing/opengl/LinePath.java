package processing.opengl;

import processing.core.PConstants;
import processing.core.PMatrix2D;

public class LinePath {
    public static final int CAP_BUTT = 0;
    public static final int CAP_ROUND = 1;
    public static final int CAP_SQUARE = 2;
    static final int EXPAND_MAX = 500;
    static final int INIT_SIZE = 20;
    public static final int JOIN_BEVEL = 2;
    public static final int JOIN_MITER = 0;
    public static final int JOIN_ROUND = 1;
    public static final byte SEG_CLOSE = 2;
    public static final byte SEG_LINETO = 1;
    public static final byte SEG_MOVETO = 0;
    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    private static float defaultMiterlimit = 10.0f;
    private static PMatrix2D identity = new PMatrix2D();
    protected float[] floatCoords;
    protected int numCoords;
    protected int numTypes;
    protected int[] pointColors;
    protected byte[] pointTypes;
    protected int windingRule;

    public static class PathIterator {
        static final int[] curvecoords = {2, 2, 0};
        int colorIdx = 0;
        float[] floatCoords;
        LinePath path;
        int pointIdx = 0;
        int typeIdx;

        PathIterator(LinePath linePath) {
            this.path = linePath;
            this.floatCoords = linePath.floatCoords;
        }

        public int currentSegment(double[] dArr) {
            byte b = this.path.pointTypes[this.typeIdx];
            int i = curvecoords[b];
            if (i > 0) {
                for (int i2 = 0; i2 < i; i2++) {
                    dArr[i2] = (double) this.floatCoords[this.pointIdx + i2];
                }
                int i3 = this.path.pointColors[this.colorIdx];
                dArr[i + 0] = (double) ((i3 >> 24) & 255);
                dArr[i + 1] = (double) ((i3 >> 16) & 255);
                dArr[i + 2] = (double) ((i3 >> 8) & 255);
                dArr[i + 3] = (double) ((i3 >> 0) & 255);
            }
            return b;
        }

        public int currentSegment(float[] fArr) {
            byte b = this.path.pointTypes[this.typeIdx];
            int i = curvecoords[b];
            if (i > 0) {
                System.arraycopy(this.floatCoords, this.pointIdx, fArr, 0, i);
                int i2 = this.path.pointColors[this.colorIdx];
                fArr[i + 0] = (float) ((i2 >> 24) & 255);
                fArr[i + 1] = (float) ((i2 >> 16) & 255);
                fArr[i + 2] = (float) ((i2 >> 8) & 255);
                fArr[i + 3] = (float) ((i2 >> 0) & 255);
            }
            return b;
        }

        public int getWindingRule() {
            return this.path.getWindingRule();
        }

        public boolean isDone() {
            return this.typeIdx >= this.path.numTypes;
        }

        public void next() {
            byte[] bArr = this.path.pointTypes;
            int i = this.typeIdx;
            this.typeIdx = i + 1;
            byte b = bArr[i];
            if (curvecoords[b] > 0) {
                this.pointIdx = curvecoords[b] + this.pointIdx;
                this.colorIdx++;
            }
        }
    }

    public LinePath() {
        this(1, 20);
    }

    public LinePath(int i) {
        this(i, 20);
    }

    public LinePath(int i, int i2) {
        setWindingRule(i);
        this.pointTypes = new byte[i2];
        this.floatCoords = new float[(i2 * 2)];
        this.pointColors = new int[i2];
    }

    static int FloatToS15_16(float f) {
        float f2 = (65536.0f * f) + 0.5f;
        if (f2 <= -4.2949673E9f) {
            return Integer.MIN_VALUE;
        }
        return f2 >= 4.2949673E9f ? PConstants.MAX_INT : (int) Math.floor((double) f2);
    }

    static float S15_16ToFloat(int i) {
        return ((float) i) / 65536.0f;
    }

    public static byte[] copyOf(byte[] bArr, int i) {
        byte[] bArr2 = new byte[i];
        for (int i2 = 0; i2 < bArr2.length; i2++) {
            if (i2 > bArr.length - 1) {
                bArr2[i2] = 0;
            } else {
                bArr2[i2] = bArr[i2];
            }
        }
        return bArr2;
    }

    public static float[] copyOf(float[] fArr, int i) {
        float[] fArr2 = new float[i];
        for (int i2 = 0; i2 < fArr2.length; i2++) {
            if (i2 > fArr.length - 1) {
                fArr2[i2] = 0.0f;
            } else {
                fArr2[i2] = fArr[i2];
            }
        }
        return fArr2;
    }

    public static int[] copyOf(int[] iArr, int i) {
        int[] iArr2 = new int[i];
        for (int i2 = 0; i2 < iArr2.length; i2++) {
            if (i2 > iArr.length - 1) {
                iArr2[i2] = 0;
            } else {
                iArr2[i2] = iArr[i2];
            }
        }
        return iArr2;
    }

    public static LinePath createStrokedPath(LinePath linePath, float f, int i, int i2) {
        return createStrokedPath(linePath, f, i, i2, defaultMiterlimit, (PMatrix2D) null);
    }

    public static LinePath createStrokedPath(LinePath linePath, float f, int i, int i2, float f2) {
        return createStrokedPath(linePath, f, i, i2, f2, (PMatrix2D) null);
    }

    public static LinePath createStrokedPath(LinePath linePath, float f, int i, int i2, float f2, PMatrix2D pMatrix2D) {
        LinePath linePath2 = new LinePath();
        strokeTo(linePath, f, i, i2, f2, pMatrix2D, new LineStroker(linePath2) {
            final /* synthetic */ LinePath val$dest;

            {
                this.val$dest = r1;
            }

            public void close() {
                this.val$dest.closePath();
            }

            public void end() {
            }

            public void lineJoin() {
            }

            public void lineTo(int i, int i2, int i3) {
                this.val$dest.lineTo(LinePath.S15_16ToFloat(i), LinePath.S15_16ToFloat(i2), i3);
            }

            public void moveTo(int i, int i2, int i3) {
                this.val$dest.moveTo(LinePath.S15_16ToFloat(i), LinePath.S15_16ToFloat(i2), i3);
            }
        });
        return linePath2;
    }

    public static double hypot(double d, double d2) {
        return Math.sqrt((d * d) + (d2 * d2));
    }

    public static int hypot(int i, int i2) {
        return (int) ((lsqrt((((long) i) * ((long) i)) + (((long) i2) * ((long) i2))) + 128) >> 8);
    }

    public static long hypot(long j, long j2) {
        return (lsqrt((j * j) + (j2 * j2)) + 128) >> 8;
    }

    public static int isqrt(int i) {
        int i2 = 0;
        int i3 = 23;
        int i4 = 0;
        while (true) {
            i4 = (i4 << 2) | (i >>> 30);
            i <<= 2;
            i2 <<= 1;
            int i5 = (i2 << 1) + 1;
            if (i4 >= i5) {
                i4 -= i5;
                i2++;
            }
            int i6 = i3 - 1;
            if (i3 == 0) {
                return i2;
            }
            i3 = i6;
        }
    }

    public static long lsqrt(long j) {
        long j2 = 0;
        int i = 39;
        long j3 = 0;
        while (true) {
            j3 = (j3 << 2) | (j >>> 62);
            j <<= 2;
            j2 <<= 1;
            long j4 = (j2 << 1) + 1;
            if (j3 >= j4) {
                j3 -= j4;
                j2++;
            }
            int i2 = i - 1;
            if (i == 0) {
                return j2;
            }
            i = i2;
        }
    }

    private static void pathTo(PathIterator pathIterator, LineStroker lineStroker) {
        float[] fArr = new float[6];
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(fArr)) {
                case 0:
                    lineStroker.moveTo(FloatToS15_16(fArr[0]), FloatToS15_16(fArr[1]), (((int) fArr[2]) << 24) | (((int) fArr[3]) << 16) | (((int) fArr[4]) << 8) | ((int) fArr[5]));
                    break;
                case 1:
                    int i = (((int) fArr[2]) << 24) | (((int) fArr[3]) << 16) | (((int) fArr[4]) << 8) | ((int) fArr[5]);
                    lineStroker.lineJoin();
                    lineStroker.lineTo(FloatToS15_16(fArr[0]), FloatToS15_16(fArr[1]), i);
                    break;
                case 2:
                    lineStroker.lineJoin();
                    lineStroker.close();
                    break;
                default:
                    throw new InternalError("unknown flattened segment type");
            }
            pathIterator.next();
        }
        lineStroker.end();
    }

    private static void strokeTo(LinePath linePath, float f, int i, int i2, float f2, PMatrix2D pMatrix2D, LineStroker lineStroker) {
        pathTo(linePath.getPathIterator(), new LineStroker(lineStroker, FloatToS15_16(f), i, i2, FloatToS15_16(f2), pMatrix2D == null ? identity : pMatrix2D));
    }

    public final void closePath() {
        if (this.numTypes == 0 || this.pointTypes[this.numTypes - 1] != 2) {
            needRoom(false, 0);
            byte[] bArr = this.pointTypes;
            int i = this.numTypes;
            this.numTypes = i + 1;
            bArr[i] = 2;
        }
    }

    public PathIterator getPathIterator() {
        return new PathIterator(this);
    }

    public final int getWindingRule() {
        return this.windingRule;
    }

    public final void lineTo(float f, float f2, int i) {
        needRoom(true, 1);
        byte[] bArr = this.pointTypes;
        int i2 = this.numTypes;
        this.numTypes = i2 + 1;
        bArr[i2] = 1;
        float[] fArr = this.floatCoords;
        int i3 = this.numCoords;
        this.numCoords = i3 + 1;
        fArr[i3] = f;
        float[] fArr2 = this.floatCoords;
        int i4 = this.numCoords;
        this.numCoords = i4 + 1;
        fArr2[i4] = f2;
        this.pointColors[(this.numCoords / 2) - 1] = i;
    }

    public final void moveTo(float f, float f2, int i) {
        if (this.numTypes <= 0 || this.pointTypes[this.numTypes - 1] != 0) {
            needRoom(false, 1);
            byte[] bArr = this.pointTypes;
            int i2 = this.numTypes;
            this.numTypes = i2 + 1;
            bArr[i2] = 0;
            float[] fArr = this.floatCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            fArr[i3] = f;
            float[] fArr2 = this.floatCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            fArr2[i4] = f2;
            this.pointColors[(this.numCoords / 2) - 1] = i;
            return;
        }
        this.floatCoords[this.numCoords - 2] = f;
        this.floatCoords[this.numCoords - 1] = f2;
        this.pointColors[(this.numCoords / 2) - 1] = i;
    }

    /* access modifiers changed from: package-private */
    public void needRoom(boolean z, int i) {
        if (!z || this.numTypes != 0) {
            int length = this.pointTypes.length;
            if (this.numTypes >= length) {
                this.pointTypes = copyOf(this.pointTypes, (length > EXPAND_MAX ? EXPAND_MAX : length) + length);
            }
            int length2 = this.floatCoords.length;
            if (this.numCoords + (i * 2) > length2) {
                int i2 = length2 > 1000 ? 1000 : length2;
                if (i2 < i * 2) {
                    i2 = i * 2;
                }
                this.floatCoords = copyOf(this.floatCoords, i2 + length2);
            }
            int length3 = this.pointColors.length;
            if ((this.numCoords / 2) + i > length3) {
                int i3 = length3 > EXPAND_MAX ? EXPAND_MAX : length3;
                if (i3 >= i) {
                    i = i3;
                }
                this.pointColors = copyOf(this.pointColors, length3 + i);
                return;
            }
            return;
        }
        throw new RuntimeException("missing initial moveto in path definition");
    }

    public final void reset() {
        this.numCoords = 0;
        this.numTypes = 0;
    }

    public final void setWindingRule(int i) {
        if (i == 0 || i == 1) {
            this.windingRule = i;
            return;
        }
        throw new IllegalArgumentException("winding rule must be WIND_EVEN_ODD or WIND_NON_ZERO");
    }
}
