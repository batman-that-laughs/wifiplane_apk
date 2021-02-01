package processing.opengl;

import processing.core.PMatrix2D;

public class LineStroker {
    private static final long ROUND_JOIN_INTERNAL_THRESHOLD = 1000000000;
    private static final long ROUND_JOIN_THRESHOLD = 100000000;
    private int capStyle;
    private int color0;
    private int[] join;
    boolean joinSegment = false;
    private int joinStyle;
    private boolean joinToOrigin;
    private boolean lineToOrigin;
    private int lineWidth2;
    private int m00;
    private double m00_2_m01_2;
    private double m00_m10_m01_m11;
    private int m01;
    private int m10;
    private double m10_2_m11_2;
    private int m11;
    private int[] miter = new int[2];
    private long miterLimitSq;
    private int mx0;
    private int my0;
    private int numPenSegments;
    private int[] offset = new int[2];
    private int omx;
    private int omy;
    private LineStroker output;
    private int pcolor0;
    private boolean[] penIncluded;
    private int[] pen_dx;
    private int[] pen_dy;
    private int prev;
    private int px0;
    private int py0;
    private int[] reverse = new int[100];
    private int rindex;
    private long scaledLineWidth2;
    private int scolor0;
    private boolean started;
    private int sx0;
    private int sx1;
    private int sy0;
    private int sy1;
    private int x0;
    private int y0;

    public LineStroker() {
    }

    public LineStroker(LineStroker lineStroker, int i, int i2, int i3, int i4, PMatrix2D pMatrix2D) {
        setOutput(lineStroker);
        setParameters(i, i2, i3, i4, pMatrix2D);
    }

    private void computeMiter(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int[] iArr) {
        long j = (long) i;
        long j2 = (long) i2;
        long j3 = (long) i5;
        long j4 = (long) i6;
        long j5 = (long) i7;
        long j6 = (long) i8;
        long j7 = ((long) i3) - j;
        long j8 = ((long) i4) - j2;
        long j9 = j6 - j4;
        long j10 = ((j7 * j9) - ((j5 - j3) * j8)) >> 16;
        if (j10 == 0) {
            iArr[0] = i;
            iArr[1] = i2;
            return;
        }
        long j11 = ((j3 * (j6 - j2)) + (((j2 - j4) * j5) - (j * j9))) >> 16;
        iArr[0] = (int) (j + ((j7 * j11) / j10));
        iArr[1] = (int) (j2 + ((j11 * j8) / j10));
    }

    private void computeOffset(int i, int i2, int i3, int i4, int[] iArr) {
        int i5;
        int i6;
        long j = ((long) i3) - ((long) i);
        long j2 = ((long) i4) - ((long) i2);
        if (this.m00 > 0 && this.m00 == this.m11) {
            if ((this.m01 == 0) && (this.m10 == 0)) {
                long hypot = LinePath.hypot(j, j2);
                if (hypot == 0) {
                    i6 = 0;
                    i5 = 0;
                } else {
                    i5 = (int) ((this.scaledLineWidth2 * j2) / hypot);
                    i6 = (int) ((-(j * this.scaledLineWidth2)) / hypot);
                }
                iArr[0] = i5;
                iArr[1] = i6;
            }
        }
        double d = (double) (i3 - i);
        double d2 = (double) (i4 - i2);
        double hypot2 = ((double) (((((double) this.m00) * ((double) this.m11)) - (((double) this.m01) * ((double) this.m10)) > 0.0d ? 1 : -1) * this.lineWidth2)) / (LinePath.hypot((((double) this.m00) * d2) - (((double) this.m10) * d), (((double) this.m01) * d2) - (((double) this.m11) * d)) * 65536.0d);
        i5 = (int) (((this.m00_2_m01_2 * d2) - (this.m00_m10_m01_m11 * d)) * hypot2);
        i6 = (int) (((d2 * this.m00_m10_m01_m11) - (d * this.m10_2_m11_2)) * hypot2);
        iArr[0] = i5;
        iArr[1] = i6;
    }

    private int computeRoundJoin(int i, int i2, int i3, int i4, int i5, int i6, int i7, boolean z, int[] iArr) {
        boolean z2;
        int i8 = 0;
        if (i7 == 0) {
            z2 = side(i, i2, i3, i4, i5, i6);
        } else {
            z2 = i7 == 1;
        }
        int i9 = 0;
        while (true) {
            int i10 = i9;
            if (i10 >= this.numPenSegments) {
                break;
            }
            if (side(i + this.pen_dx[i10], i2 + this.pen_dy[i10], i3, i4, i5, i6) != z2) {
                this.penIncluded[i10] = true;
            } else {
                this.penIncluded[i10] = false;
            }
            i9 = i10 + 1;
        }
        int i11 = -1;
        int i12 = -1;
        for (int i13 = 0; i13 < this.numPenSegments; i13++) {
            if (this.penIncluded[i13] && !this.penIncluded[((this.numPenSegments + i13) - 1) % this.numPenSegments]) {
                i11 = i13;
            }
            if (this.penIncluded[i13] && !this.penIncluded[(i13 + 1) % this.numPenSegments]) {
                i12 = i13;
            }
        }
        int i14 = i12 < i11 ? i12 + this.numPenSegments : i12;
        if (!(i11 == -1 || i14 == -1)) {
            long j = (long) ((this.pen_dx[i11] + i) - i3);
            long j2 = (long) ((this.pen_dy[i11] + i2) - i4);
            long j3 = (long) ((this.pen_dx[i11] + i) - i5);
            long j4 = (long) ((this.pen_dy[i11] + i2) - i6);
            boolean z3 = (j * j) + (j2 * j2) > (j3 * j3) + (j4 * j4);
            int i15 = z3 ? i14 : i11;
            int i16 = z3 ? -1 : 1;
            int i17 = 0;
            while (true) {
                int i18 = i15 % this.numPenSegments;
                int i19 = i17 + 1;
                iArr[i17] = this.pen_dx[i18] + i;
                i17 = i19 + 1;
                iArr[i19] = this.pen_dy[i18] + i2;
                if (i15 == (z3 ? i11 : i14)) {
                    break;
                }
                i15 += i16;
            }
            i8 = i17;
        }
        return i8 / 2;
    }

    private void drawMiter(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, boolean z) {
        if (i9 != i7 || i10 != i8) {
            if (i != i3 || i2 != i4) {
                if (i3 != i5 || i4 != i6) {
                    if (z) {
                        i7 = -i7;
                        i8 = -i8;
                        i9 = -i9;
                        i10 = -i10;
                    }
                    computeMiter(i + i7, i2 + i8, i3 + i7, i4 + i8, i3 + i9, i4 + i10, i5 + i9, i6 + i10, this.miter);
                    long j = ((long) this.miter[0]) - ((long) i3);
                    long j2 = ((long) this.miter[1]) - ((long) i4);
                    long j3 = ((((long) this.m00) * j2) - (((long) this.m10) * j)) >> 16;
                    long j4 = ((j2 * ((long) this.m01)) - (j * ((long) this.m11))) >> 16;
                    if ((j4 * j4) + (j3 * j3) < this.miterLimitSq) {
                        emitLineTo(this.miter[0], this.miter[1], i11, z);
                    }
                }
            }
        }
    }

    private void drawRoundJoin(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z, boolean z2, long j) {
        if (i3 != 0 || i4 != 0) {
            if (i5 != 0 || i6 != 0) {
                long j2 = ((long) i3) - ((long) i5);
                long j3 = ((long) i4) - ((long) i6);
                if ((j2 * j2) + (j3 * j3) >= j) {
                    if (z2) {
                        i3 = -i3;
                        i4 = -i4;
                        i5 = -i5;
                        i6 = -i6;
                    }
                    int computeRoundJoin = computeRoundJoin(i, i2, i + i3, i2 + i4, i + i5, i2 + i6, i7, z, this.join);
                    for (int i9 = 0; i9 < computeRoundJoin; i9++) {
                        emitLineTo(this.join[i9 * 2], this.join[(i9 * 2) + 1], i8, z2);
                    }
                }
            }
        }
    }

    private void emitClose() {
        this.output.close();
    }

    private void emitLineTo(int i, int i2, int i3) {
        this.output.lineTo(i, i2, i3);
    }

    private void emitLineTo(int i, int i2, int i3, boolean z) {
        if (z) {
            ensureCapacity(this.rindex + 3);
            int[] iArr = this.reverse;
            int i4 = this.rindex;
            this.rindex = i4 + 1;
            iArr[i4] = i;
            int[] iArr2 = this.reverse;
            int i5 = this.rindex;
            this.rindex = i5 + 1;
            iArr2[i5] = i2;
            int[] iArr3 = this.reverse;
            int i6 = this.rindex;
            this.rindex = i6 + 1;
            iArr3[i6] = i3;
            return;
        }
        emitLineTo(i, i2, i3);
    }

    private void emitMoveTo(int i, int i2, int i3) {
        this.output.moveTo(i, i2, i3);
    }

    private void ensureCapacity(int i) {
        if (this.reverse.length < i) {
            int[] iArr = new int[Math.max(i, (this.reverse.length * 6) / 5)];
            System.arraycopy(this.reverse, 0, iArr, 0, this.rindex);
            this.reverse = iArr;
        }
    }

    private void finish() {
        if (this.capStyle == 1) {
            drawRoundJoin(this.x0, this.y0, this.omx, this.omy, -this.omx, -this.omy, 1, this.color0, false, false, ROUND_JOIN_THRESHOLD);
        } else if (this.capStyle == 2) {
            long j = (long) (this.px0 - this.x0);
            long j2 = (long) (this.py0 - this.y0);
            long lineLength = lineLength(j, j2);
            if (0 < lineLength) {
                long j3 = (((long) this.lineWidth2) * 65536) / lineLength;
                int i = this.x0 - ((int) ((j * j3) >> 16));
                int i2 = this.y0 - ((int) ((j2 * j3) >> 16));
                emitLineTo(this.omx + i, this.omy + i2, this.color0);
                emitLineTo(i - this.omx, i2 - this.omy, this.color0);
            }
        }
        for (int i3 = this.rindex - 3; i3 >= 0; i3 -= 3) {
            emitLineTo(this.reverse[i3], this.reverse[i3 + 1], this.reverse[i3 + 2]);
        }
        this.rindex = 0;
        if (this.capStyle == 1) {
            drawRoundJoin(this.sx0, this.sy0, -this.mx0, -this.my0, this.mx0, this.my0, 1, this.scolor0, false, false, ROUND_JOIN_THRESHOLD);
        } else if (this.capStyle == 2) {
            long j4 = (long) (this.sx1 - this.sx0);
            long j5 = (long) (this.sy1 - this.sy0);
            long lineLength2 = lineLength(j4, j5);
            if (0 < lineLength2) {
                long j6 = (((long) this.lineWidth2) * 65536) / lineLength2;
                int i4 = this.sx0 - ((int) ((j4 * j6) >> 16));
                int i5 = this.sy0 - ((int) ((j5 * j6) >> 16));
                emitLineTo(i4 - this.mx0, i5 - this.my0, this.scolor0);
                emitLineTo(i4 + this.mx0, i5 + this.my0, this.scolor0);
            }
        }
        emitClose();
        this.joinSegment = false;
    }

    private boolean isCCW(int i, int i2, int i3, int i4, int i5, int i6) {
        return ((long) (i3 - i)) * ((long) (i6 - i4)) < ((long) (i4 - i2)) * ((long) (i5 - i3));
    }

    private void lineToImpl(int i, int i2, int i3, boolean z) {
        computeOffset(this.x0, this.y0, i, i2, this.offset);
        int i4 = this.offset[0];
        int i5 = this.offset[1];
        if (!this.started) {
            emitMoveTo(this.x0 + i4, this.y0 + i5, this.color0);
            this.sx1 = i;
            this.sy1 = i2;
            this.mx0 = i4;
            this.my0 = i5;
            this.started = true;
        } else {
            boolean isCCW = isCCW(this.px0, this.py0, this.x0, this.y0, i, i2);
            if (!z) {
                drawRoundJoin(this.x0, this.y0, this.omx, this.omy, i4, i5, 0, this.color0, false, isCCW, ROUND_JOIN_INTERNAL_THRESHOLD);
            } else if (this.joinStyle == 0) {
                drawMiter(this.px0, this.py0, this.x0, this.y0, i, i2, this.omx, this.omy, i4, i5, this.color0, isCCW);
            } else if (this.joinStyle == 1) {
                drawRoundJoin(this.x0, this.y0, this.omx, this.omy, i4, i5, 0, this.color0, false, isCCW, ROUND_JOIN_THRESHOLD);
            }
            emitLineTo(this.x0, this.y0, this.color0, !isCCW);
        }
        emitLineTo(this.x0 + i4, this.y0 + i5, this.color0, false);
        emitLineTo(i + i4, i2 + i5, i3, false);
        emitLineTo(this.x0 - i4, this.y0 - i5, this.color0, true);
        emitLineTo(i - i4, i2 - i5, i3, true);
        this.omx = i4;
        this.omy = i5;
        this.px0 = this.x0;
        this.py0 = this.y0;
        this.pcolor0 = this.color0;
        this.x0 = i;
        this.y0 = i2;
        this.color0 = i3;
        this.prev = 1;
    }

    private boolean side(int i, int i2, int i3, int i4, int i5, int i6) {
        long j = (long) i3;
        long j2 = (long) i4;
        long j3 = (long) i5;
        long j4 = (long) i6;
        return ((((long) i) * (j2 - j4)) + (((long) i2) * (j3 - j))) + ((j * j4) - (j3 * j2)) > 0;
    }

    public void close() {
        if (this.lineToOrigin) {
            this.lineToOrigin = false;
        }
        if (!this.started) {
            finish();
            return;
        }
        computeOffset(this.x0, this.y0, this.sx0, this.sy0, this.offset);
        int i = this.offset[0];
        int i2 = this.offset[1];
        boolean isCCW = isCCW(this.px0, this.py0, this.x0, this.y0, this.sx0, this.sy0);
        if (!this.joinSegment) {
            drawRoundJoin(this.x0, this.y0, this.omx, this.omy, i, i2, 0, this.color0, false, isCCW, ROUND_JOIN_INTERNAL_THRESHOLD);
        } else if (this.joinStyle == 0) {
            drawMiter(this.px0, this.py0, this.x0, this.y0, this.sx0, this.sy0, this.omx, this.omy, i, i2, this.pcolor0, isCCW);
        } else if (this.joinStyle == 1) {
            drawRoundJoin(this.x0, this.y0, this.omx, this.omy, i, i2, 0, this.color0, false, isCCW, ROUND_JOIN_THRESHOLD);
        }
        emitLineTo(this.x0 + i, this.y0 + i2, this.color0);
        emitLineTo(this.sx0 + i, this.sy0 + i2, this.scolor0);
        boolean isCCW2 = isCCW(this.x0, this.y0, this.sx0, this.sy0, this.sx1, this.sy1);
        if (!isCCW2) {
            if (this.joinStyle == 0) {
                drawMiter(this.x0, this.y0, this.sx0, this.sy0, this.sx1, this.sy1, i, i2, this.mx0, this.my0, this.color0, false);
            } else if (this.joinStyle == 1) {
                drawRoundJoin(this.sx0, this.sy0, i, i2, this.mx0, this.my0, 0, this.scolor0, false, false, ROUND_JOIN_THRESHOLD);
            }
        }
        emitLineTo(this.sx0 + this.mx0, this.sy0 + this.my0, this.scolor0);
        emitLineTo(this.sx0 - this.mx0, this.sy0 - this.my0, this.scolor0);
        if (isCCW2) {
            if (this.joinStyle == 0) {
                drawMiter(this.x0, this.y0, this.sx0, this.sy0, this.sx1, this.sy1, -i, -i2, -this.mx0, -this.my0, this.color0, false);
            } else if (this.joinStyle == 1) {
                drawRoundJoin(this.sx0, this.sy0, -i, -i2, -this.mx0, -this.my0, 0, this.scolor0, true, false, ROUND_JOIN_THRESHOLD);
            }
        }
        emitLineTo(this.sx0 - i, this.sy0 - i2, this.scolor0);
        emitLineTo(this.x0 - i, this.y0 - i2, this.color0);
        for (int i3 = this.rindex - 3; i3 >= 0; i3 -= 3) {
            emitLineTo(this.reverse[i3], this.reverse[i3 + 1], this.reverse[i3 + 2]);
        }
        this.x0 = this.sx0;
        this.y0 = this.sy0;
        this.rindex = 0;
        this.started = false;
        this.joinSegment = false;
        this.prev = 2;
        emitClose();
    }

    public void end() {
        if (this.lineToOrigin) {
            lineToImpl(this.sx0, this.sy0, this.scolor0, this.joinToOrigin);
            this.lineToOrigin = false;
        }
        if (this.prev == 1) {
            finish();
        }
        this.output.end();
        this.joinSegment = false;
        this.prev = 0;
    }

    public void lineJoin() {
        this.joinSegment = true;
    }

    /* access modifiers changed from: package-private */
    public long lineLength(long j, long j2) {
        long j3 = ((((long) this.m00) * ((long) this.m11)) - (((long) this.m01) * ((long) this.m10))) >> 16;
        return (long) ((int) LinePath.hypot(((((long) this.m00) * j2) - (((long) this.m10) * j)) / j3, ((((long) this.m01) * j2) - (((long) this.m11) * j)) / j3));
    }

    public void lineTo(int i, int i2, int i3) {
        if (this.lineToOrigin) {
            if (i != this.sx0 || i2 != this.sy0) {
                lineToImpl(this.sx0, this.sy0, this.scolor0, this.joinToOrigin);
                this.lineToOrigin = false;
            } else {
                return;
            }
        } else if (i != this.x0 || i2 != this.y0) {
            if (i == this.sx0 && i2 == this.sy0) {
                this.lineToOrigin = true;
                this.joinToOrigin = this.joinSegment;
                this.joinSegment = false;
                return;
            }
        } else {
            return;
        }
        lineToImpl(i, i2, i3, this.joinSegment);
        this.joinSegment = false;
    }

    public void moveTo(int i, int i2, int i3) {
        if (this.lineToOrigin) {
            lineToImpl(this.sx0, this.sy0, this.scolor0, this.joinToOrigin);
            this.lineToOrigin = false;
        }
        if (this.prev == 1) {
            finish();
        }
        this.x0 = i;
        this.sx0 = i;
        this.y0 = i2;
        this.sy0 = i2;
        this.color0 = i3;
        this.scolor0 = i3;
        this.rindex = 0;
        this.started = false;
        this.joinSegment = false;
        this.prev = 0;
    }

    public void setOutput(LineStroker lineStroker) {
        this.output = lineStroker;
    }

    public void setParameters(int i, int i2, int i3, int i4, PMatrix2D pMatrix2D) {
        this.m00 = LinePath.FloatToS15_16(pMatrix2D.m00);
        this.m01 = LinePath.FloatToS15_16(pMatrix2D.m01);
        this.m10 = LinePath.FloatToS15_16(pMatrix2D.m10);
        this.m11 = LinePath.FloatToS15_16(pMatrix2D.m11);
        this.lineWidth2 = i >> 1;
        this.scaledLineWidth2 = (((long) this.m00) * ((long) this.lineWidth2)) >> 16;
        this.capStyle = i2;
        this.joinStyle = i3;
        this.m00_2_m01_2 = (((double) this.m00) * ((double) this.m00)) + (((double) this.m01) * ((double) this.m01));
        this.m10_2_m11_2 = (((double) this.m10) * ((double) this.m10)) + (((double) this.m11) * ((double) this.m11));
        this.m00_m10_m01_m11 = (((double) this.m00) * ((double) this.m10)) + (((double) this.m01) * ((double) this.m11));
        double d = ((double) this.m00) / 65536.0d;
        double d2 = ((double) this.m01) / 65536.0d;
        double d3 = ((double) this.m10) / 65536.0d;
        double d4 = ((double) this.m11) / 65536.0d;
        double d5 = (d * d4) - (d2 * d3);
        if (i3 == 0) {
            double d6 = d5 * (((double) i4) / 65536.0d) * (((double) this.lineWidth2) / 65536.0d);
            this.miterLimitSq = (long) (d6 * d6 * 65536.0d * 65536.0d);
        }
        this.numPenSegments = (int) ((3.14159f * ((float) i)) / 65536.0f);
        if (this.pen_dx == null || this.pen_dx.length < this.numPenSegments) {
            this.pen_dx = new int[this.numPenSegments];
            this.pen_dy = new int[this.numPenSegments];
            this.penIncluded = new boolean[this.numPenSegments];
            this.join = new int[(this.numPenSegments * 2)];
        }
        for (int i5 = 0; i5 < this.numPenSegments; i5++) {
            double d7 = ((double) i) / 2.0d;
            double d8 = (((double) (i5 * 2)) * 3.141592653589793d) / ((double) this.numPenSegments);
            double cos = Math.cos(d8);
            double sin = Math.sin(d8);
            this.pen_dx[i5] = (int) (((d * cos) + (d2 * sin)) * d7);
            this.pen_dy[i5] = (int) (d7 * ((sin * d4) + (cos * d3)));
        }
        this.prev = 2;
        this.rindex = 0;
        this.started = false;
        this.lineToOrigin = false;
    }
}
