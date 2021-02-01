package processing.opengl.tess;

import processing.core.PConstants;
import processing.opengl.tess.PriorityQ;

class PriorityQSort extends PriorityQ {
    static final /* synthetic */ boolean $assertionsDisabled = (!PriorityQSort.class.desiredAssertionStatus());
    PriorityQHeap heap;
    boolean initialized = false;
    Object[] keys = new Object[32];
    PriorityQ.Leq leq;
    int max = 32;
    int[] order;
    int size = 0;

    private static class Stack {
        int p;
        int r;

        private Stack() {
        }
    }

    public PriorityQSort(PriorityQ.Leq leq2) {
        this.heap = new PriorityQHeap(leq2);
        this.leq = leq2;
    }

    private static boolean GT(PriorityQ.Leq leq2, Object obj, Object obj2) {
        return !PriorityQ.LEQ(leq2, obj, obj2);
    }

    private static boolean LT(PriorityQ.Leq leq2, Object obj, Object obj2) {
        return !PriorityQ.LEQ(leq2, obj2, obj);
    }

    private static void Swap(int[] iArr, int i, int i2) {
        int i3 = iArr[i];
        iArr[i] = iArr[i2];
        iArr[i2] = i3;
    }

    /* access modifiers changed from: package-private */
    public void pqDelete(int i) {
        if (i >= 0) {
            this.heap.pqDelete(i);
            return;
        }
        int i2 = -(i + 1);
        if ($assertionsDisabled || (i2 < this.max && this.keys[i2] != null)) {
            this.keys[i2] = null;
            while (this.size > 0 && this.keys[this.order[this.size - 1]] == null) {
                this.size--;
            }
            return;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: package-private */
    public void pqDeletePriorityQ() {
        if (this.heap != null) {
            this.heap.pqDeletePriorityQ();
        }
        this.order = null;
        this.keys = null;
    }

    /* access modifiers changed from: package-private */
    public Object pqExtractMin() {
        if (this.size == 0) {
            return this.heap.pqExtractMin();
        }
        Object obj = this.keys[this.order[this.size - 1]];
        if (!this.heap.pqIsEmpty()) {
            if (LEQ(this.leq, this.heap.pqMinimum(), obj)) {
                return this.heap.pqExtractMin();
            }
        }
        do {
            this.size--;
            if (this.size <= 0 || this.keys[this.order[this.size - 1]] != null) {
                return obj;
            }
            this.size--;
            return obj;
        } while (this.keys[this.order[this.size - 1]] != null);
        return obj;
    }

    /* access modifiers changed from: package-private */
    public boolean pqInit() {
        Stack[] stackArr = new Stack[50];
        for (int i = 0; i < stackArr.length; i++) {
            stackArr[i] = new Stack();
        }
        int i2 = 2016473283;
        this.order = new int[(this.size + 1)];
        int i3 = this.size - 1;
        int i4 = 0;
        for (int i5 = 0; i5 <= i3; i5++) {
            this.order[i5] = i4;
            i4++;
        }
        stackArr[0].p = 0;
        stackArr[0].r = i3;
        int i6 = 1;
        while (true) {
            int i7 = i6 - 1;
            if (i7 >= 0) {
                int i8 = stackArr[i7].p;
                int i9 = stackArr[i7].r;
                int i10 = i8;
                int i11 = i2;
                i6 = i7;
                while (i9 > i10 + 10) {
                    i11 = Math.abs((1539415821 * i11) + 1);
                    int i12 = (i11 % ((i9 - i10) + 1)) + i10;
                    int i13 = this.order[i12];
                    this.order[i12] = this.order[i10];
                    this.order[i10] = i13;
                    int i14 = i10 - 1;
                    int i15 = i9 + 1;
                    while (true) {
                        i14++;
                        if (!GT(this.leq, this.keys[this.order[i14]], this.keys[i13])) {
                            do {
                                i15--;
                            } while (LT(this.leq, this.keys[this.order[i15]], this.keys[i13]));
                            Swap(this.order, i14, i15);
                            if (i14 >= i15) {
                                break;
                            }
                        }
                    }
                    Swap(this.order, i14, i15);
                    if (i14 - i10 < i9 - i15) {
                        stackArr[i6].p = i15 + 1;
                        stackArr[i6].r = i9;
                        i6++;
                        i9 = i14 - 1;
                    } else {
                        stackArr[i6].p = i10;
                        stackArr[i6].r = i14 - 1;
                        i6++;
                        i10 = i15 + 1;
                    }
                }
                for (int i16 = i10 + 1; i16 <= i9; i16++) {
                    int i17 = this.order[i16];
                    int i18 = i16;
                    while (i18 > i10 && LT(this.leq, this.keys[this.order[i18 - 1]], this.keys[i17])) {
                        this.order[i18] = this.order[i18 - 1];
                        i18--;
                    }
                    this.order[i18] = i17;
                }
                i2 = i11;
            } else {
                this.max = this.size;
                this.initialized = true;
                this.heap.pqInit();
                return true;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int pqInsert(Object obj) {
        if (this.initialized) {
            return this.heap.pqInsert(obj);
        }
        int i = this.size;
        int i2 = this.size + 1;
        this.size = i2;
        if (i2 >= this.max) {
            Object[] objArr = this.keys;
            this.max <<= 1;
            Object[] objArr2 = new Object[this.max];
            System.arraycopy(this.keys, 0, objArr2, 0, this.keys.length);
            this.keys = objArr2;
            if (this.keys == null) {
                this.keys = objArr;
                return PConstants.MAX_INT;
            }
        }
        if ($assertionsDisabled || i != Integer.MAX_VALUE) {
            this.keys[i] = obj;
            return -(i + 1);
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: package-private */
    public boolean pqIsEmpty() {
        return this.size == 0 && this.heap.pqIsEmpty();
    }

    /* access modifiers changed from: package-private */
    public Object pqMinimum() {
        if (this.size == 0) {
            return this.heap.pqMinimum();
        }
        Object obj = this.keys[this.order[this.size - 1]];
        if (!this.heap.pqIsEmpty()) {
            Object pqMinimum = this.heap.pqMinimum();
            if (PriorityQ.LEQ(this.leq, pqMinimum, obj)) {
                return pqMinimum;
            }
        }
        return obj;
    }
}
