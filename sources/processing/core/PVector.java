package processing.core;

import java.io.Serializable;

public class PVector implements Serializable {
    private static final long serialVersionUID = -6717872085945400694L;
    protected transient float[] array;
    public float x;
    public float y;
    public float z;

    public PVector() {
    }

    public PVector(float f, float f2) {
        this.x = f;
        this.y = f2;
        this.z = 0.0f;
    }

    public PVector(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.z = f3;
    }

    public static PVector add(PVector pVector, PVector pVector2) {
        return add(pVector, pVector2, (PVector) null);
    }

    public static PVector add(PVector pVector, PVector pVector2, PVector pVector3) {
        if (pVector3 == null) {
            return new PVector(pVector.x + pVector2.x, pVector.y + pVector2.y, pVector.z + pVector2.z);
        }
        pVector3.set(pVector.x + pVector2.x, pVector.y + pVector2.y, pVector.z + pVector2.z);
        return pVector3;
    }

    public static float angleBetween(PVector pVector, PVector pVector2) {
        if (pVector.x == 0.0f && pVector.y == 0.0f) {
            return 0.0f;
        }
        if (pVector2.x == 0.0f && pVector2.y == 0.0f) {
            return 0.0f;
        }
        double sqrt = ((double) (((pVector.x * pVector2.x) + (pVector.y * pVector2.y)) + (pVector.z * pVector2.z))) / (Math.sqrt((double) (((pVector.x * pVector.x) + (pVector.y * pVector.y)) + (pVector.z * pVector.z))) * Math.sqrt((double) (((pVector2.x * pVector2.x) + (pVector2.y * pVector2.y)) + (pVector2.z * pVector2.z))));
        if (sqrt <= -1.0d) {
            return 3.1415927f;
        }
        if (sqrt < 1.0d) {
            return (float) Math.acos(sqrt);
        }
        return 0.0f;
    }

    public static PVector cross(PVector pVector, PVector pVector2, PVector pVector3) {
        float f = (pVector.y * pVector2.z) - (pVector2.y * pVector.z);
        float f2 = (pVector.z * pVector2.x) - (pVector2.z * pVector.x);
        float f3 = (pVector.x * pVector2.y) - (pVector2.x * pVector.y);
        if (pVector3 == null) {
            return new PVector(f, f2, f3);
        }
        pVector3.set(f, f2, f3);
        return pVector3;
    }

    public static float dist(PVector pVector, PVector pVector2) {
        float f = pVector.x - pVector2.x;
        float f2 = pVector.y - pVector2.y;
        float f3 = pVector.z - pVector2.z;
        return (float) Math.sqrt((double) ((f * f) + (f2 * f2) + (f3 * f3)));
    }

    public static PVector div(PVector pVector, float f) {
        return div(pVector, f, (PVector) null);
    }

    public static PVector div(PVector pVector, float f, PVector pVector2) {
        if (pVector2 == null) {
            return new PVector(pVector.x / f, pVector.y / f, pVector.z / f);
        }
        pVector2.set(pVector.x / f, pVector.y / f, pVector.z / f);
        return pVector2;
    }

    public static float dot(PVector pVector, PVector pVector2) {
        return (pVector.x * pVector2.x) + (pVector.y * pVector2.y) + (pVector.z * pVector2.z);
    }

    public static PVector fromAngle(float f) {
        return fromAngle(f, (PVector) null);
    }

    public static PVector fromAngle(float f, PVector pVector) {
        if (pVector == null) {
            return new PVector((float) Math.cos((double) f), (float) Math.sin((double) f), 0.0f);
        }
        pVector.set((float) Math.cos((double) f), (float) Math.sin((double) f), 0.0f);
        return pVector;
    }

    public static PVector lerp(PVector pVector, PVector pVector2, float f) {
        PVector pVector3 = pVector.get();
        pVector3.lerp(pVector2, f);
        return pVector3;
    }

    public static PVector mult(PVector pVector, float f) {
        return mult(pVector, f, (PVector) null);
    }

    public static PVector mult(PVector pVector, float f, PVector pVector2) {
        if (pVector2 == null) {
            return new PVector(pVector.x * f, pVector.y * f, pVector.z * f);
        }
        pVector2.set(pVector.x * f, pVector.y * f, pVector.z * f);
        return pVector2;
    }

    public static PVector random2D() {
        return random2D((PVector) null, (PApplet) null);
    }

    public static PVector random2D(PApplet pApplet) {
        return random2D((PVector) null, pApplet);
    }

    public static PVector random2D(PVector pVector) {
        return random2D(pVector, (PApplet) null);
    }

    public static PVector random2D(PVector pVector, PApplet pApplet) {
        return pApplet == null ? fromAngle((float) (Math.random() * 3.141592653589793d * 2.0d), pVector) : fromAngle(pApplet.random(6.2831855f), pVector);
    }

    public static PVector random3D() {
        return random3D((PVector) null, (PApplet) null);
    }

    public static PVector random3D(PApplet pApplet) {
        return random3D((PVector) null, pApplet);
    }

    public static PVector random3D(PVector pVector) {
        return random3D(pVector, (PApplet) null);
    }

    public static PVector random3D(PVector pVector, PApplet pApplet) {
        float random;
        float random2;
        if (pApplet == null) {
            random = (float) (Math.random() * 3.141592653589793d * 2.0d);
            random2 = (float) ((Math.random() * 2.0d) - 1.0d);
        } else {
            random = pApplet.random(6.2831855f);
            random2 = pApplet.random(-1.0f, 1.0f);
        }
        float sqrt = (float) (Math.sqrt((double) (1.0f - (random2 * random2))) * Math.cos((double) random));
        float sqrt2 = (float) (Math.sqrt((double) (1.0f - (random2 * random2))) * Math.sin((double) random));
        if (pVector == null) {
            return new PVector(sqrt, sqrt2, random2);
        }
        pVector.set(sqrt, sqrt2, random2);
        return pVector;
    }

    public static PVector sub(PVector pVector, PVector pVector2) {
        return sub(pVector, pVector2, (PVector) null);
    }

    public static PVector sub(PVector pVector, PVector pVector2, PVector pVector3) {
        if (pVector3 == null) {
            return new PVector(pVector.x - pVector2.x, pVector.y - pVector2.y, pVector.z - pVector2.z);
        }
        pVector3.set(pVector.x - pVector2.x, pVector.y - pVector2.y, pVector.z - pVector2.z);
        return pVector3;
    }

    public void add(float f, float f2, float f3) {
        this.x += f;
        this.y += f2;
        this.z += f3;
    }

    public void add(PVector pVector) {
        this.x += pVector.x;
        this.y += pVector.y;
        this.z += pVector.z;
    }

    public float[] array() {
        if (this.array == null) {
            this.array = new float[3];
        }
        this.array[0] = this.x;
        this.array[1] = this.y;
        this.array[2] = this.z;
        return this.array;
    }

    public PVector cross(PVector pVector) {
        return cross(pVector, (PVector) null);
    }

    public PVector cross(PVector pVector, PVector pVector2) {
        float f = (this.y * pVector.z) - (pVector.y * this.z);
        float f2 = (this.z * pVector.x) - (pVector.z * this.x);
        float f3 = (this.x * pVector.y) - (pVector.x * this.y);
        if (pVector2 == null) {
            return new PVector(f, f2, f3);
        }
        pVector2.set(f, f2, f3);
        return pVector2;
    }

    public float dist(PVector pVector) {
        float f = this.x - pVector.x;
        float f2 = this.y - pVector.y;
        float f3 = this.z - pVector.z;
        return (float) Math.sqrt((double) ((f * f) + (f2 * f2) + (f3 * f3)));
    }

    public void div(float f) {
        this.x /= f;
        this.y /= f;
        this.z /= f;
    }

    public float dot(float f, float f2, float f3) {
        return (this.x * f) + (this.y * f2) + (this.z * f3);
    }

    public float dot(PVector pVector) {
        return (this.x * pVector.x) + (this.y * pVector.y) + (this.z * pVector.z);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PVector)) {
            return false;
        }
        PVector pVector = (PVector) obj;
        return this.x == pVector.x && this.y == pVector.y && this.z == pVector.z;
    }

    public PVector get() {
        return new PVector(this.x, this.y, this.z);
    }

    public float[] get(float[] fArr) {
        if (fArr == null) {
            return new float[]{this.x, this.y, this.z};
        }
        if (fArr.length >= 2) {
            fArr[0] = this.x;
            fArr[1] = this.y;
        }
        if (fArr.length < 3) {
            return fArr;
        }
        fArr[2] = this.z;
        return fArr;
    }

    public int hashCode() {
        return ((((Float.floatToIntBits(this.x) + 31) * 31) + Float.floatToIntBits(this.y)) * 31) + Float.floatToIntBits(this.z);
    }

    public float heading() {
        return ((float) Math.atan2((double) (-this.y), (double) this.x)) * -1.0f;
    }

    @Deprecated
    public float heading2D() {
        return heading();
    }

    public void lerp(float f, float f2, float f3, float f4) {
        this.x = PApplet.lerp(this.x, f, f4);
        this.y = PApplet.lerp(this.y, f2, f4);
        this.z = PApplet.lerp(this.z, f3, f4);
    }

    public void lerp(PVector pVector, float f) {
        this.x = PApplet.lerp(this.x, pVector.x, f);
        this.y = PApplet.lerp(this.y, pVector.y, f);
        this.z = PApplet.lerp(this.z, pVector.z, f);
    }

    public void limit(float f) {
        if (magSq() > f * f) {
            normalize();
            mult(f);
        }
    }

    public float mag() {
        return (float) Math.sqrt((double) ((this.x * this.x) + (this.y * this.y) + (this.z * this.z)));
    }

    public float magSq() {
        return (this.x * this.x) + (this.y * this.y) + (this.z * this.z);
    }

    public void mult(float f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;
    }

    public PVector normalize(PVector pVector) {
        if (pVector == null) {
            pVector = new PVector();
        }
        float mag = mag();
        if (mag > 0.0f) {
            pVector.set(this.x / mag, this.y / mag, this.z / mag);
        } else {
            pVector.set(this.x, this.y, this.z);
        }
        return pVector;
    }

    public void normalize() {
        float mag = mag();
        if (mag != 0.0f && mag != 1.0f) {
            div(mag);
        }
    }

    public void rotate(float f) {
        float f2 = this.x;
        this.x = (this.x * PApplet.cos(f)) - (this.y * PApplet.sin(f));
        this.y = (f2 * PApplet.sin(f)) + (this.y * PApplet.cos(f));
    }

    public void set(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public void set(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.z = f3;
    }

    public void set(PVector pVector) {
        this.x = pVector.x;
        this.y = pVector.y;
        this.z = pVector.z;
    }

    public void set(float[] fArr) {
        if (fArr.length >= 2) {
            this.x = fArr[0];
            this.y = fArr[1];
        }
        if (fArr.length >= 3) {
            this.z = fArr[2];
        }
    }

    public PVector setMag(PVector pVector, float f) {
        PVector normalize = normalize(pVector);
        normalize.mult(f);
        return normalize;
    }

    public void setMag(float f) {
        normalize();
        mult(f);
    }

    public void sub(float f, float f2, float f3) {
        this.x -= f;
        this.y -= f2;
        this.z -= f3;
    }

    public void sub(PVector pVector) {
        this.x -= pVector.x;
        this.y -= pVector.y;
        this.z -= pVector.z;
    }

    public String toString() {
        return "[ " + this.x + ", " + this.y + ", " + this.z + " ]";
    }
}
