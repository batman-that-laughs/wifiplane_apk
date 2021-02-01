package processing.opengl.tess;

import processing.core.PConstants;
import processing.opengl.tess.PriorityQ;

class PriorityQHeap extends PriorityQ {
    static final /* synthetic */ boolean $assertionsDisabled = (!PriorityQHeap.class.desiredAssertionStatus());
    int freeList;
    PriorityQ.PQhandleElem[] handles;
    boolean initialized;
    PriorityQ.Leq leq;
    int max = 32;
    PriorityQ.PQnode[] nodes = new PriorityQ.PQnode[33];
    int size = 0;

    public PriorityQHeap(PriorityQ.Leq leq2) {
        for (int i = 0; i < this.nodes.length; i++) {
            this.nodes[i] = new PriorityQ.PQnode();
        }
        this.handles = new PriorityQ.PQhandleElem[33];
        for (int i2 = 0; i2 < this.handles.length; i2++) {
            this.handles[i2] = new PriorityQ.PQhandleElem();
        }
        this.initialized = false;
        this.freeList = 0;
        this.leq = leq2;
        this.nodes[1].handle = 1;
        this.handles[1].key = null;
    }

    /* access modifiers changed from: package-private */
    public void FloatDown(int i) {
        PriorityQ.PQnode[] pQnodeArr = this.nodes;
        PriorityQ.PQhandleElem[] pQhandleElemArr = this.handles;
        int i2 = pQnodeArr[i].handle;
        while (true) {
            int i3 = i << 1;
            if (i3 < this.size && LEQ(this.leq, pQhandleElemArr[pQnodeArr[i3 + 1].handle].key, pQhandleElemArr[pQnodeArr[i3].handle].key)) {
                i3++;
            }
            if ($assertionsDisabled || i3 <= this.max) {
                int i4 = pQnodeArr[i3].handle;
                if (i3 > this.size || LEQ(this.leq, pQhandleElemArr[i2].key, pQhandleElemArr[i4].key)) {
                    pQnodeArr[i].handle = i2;
                    pQhandleElemArr[i2].node = i;
                } else {
                    pQnodeArr[i].handle = i4;
                    pQhandleElemArr[i4].node = i;
                    i = i3;
                }
            } else {
                throw new AssertionError();
            }
        }
        pQnodeArr[i].handle = i2;
        pQhandleElemArr[i2].node = i;
    }

    /* access modifiers changed from: package-private */
    public void FloatUp(int i) {
        PriorityQ.PQnode[] pQnodeArr = this.nodes;
        PriorityQ.PQhandleElem[] pQhandleElemArr = this.handles;
        int i2 = pQnodeArr[i].handle;
        while (true) {
            int i3 = i >> 1;
            int i4 = pQnodeArr[i3].handle;
            if (i3 == 0 || LEQ(this.leq, pQhandleElemArr[i4].key, pQhandleElemArr[i2].key)) {
                pQnodeArr[i].handle = i2;
                pQhandleElemArr[i2].node = i;
            } else {
                pQnodeArr[i].handle = i4;
                pQhandleElemArr[i4].node = i;
                i = i3;
            }
        }
        pQnodeArr[i].handle = i2;
        pQhandleElemArr[i2].node = i;
    }

    /* access modifiers changed from: package-private */
    public void pqDelete(int i) {
        PriorityQ.PQnode[] pQnodeArr = this.nodes;
        PriorityQ.PQhandleElem[] pQhandleElemArr = this.handles;
        if ($assertionsDisabled || (i >= 1 && i <= this.max && pQhandleElemArr[i].key != null)) {
            int i2 = pQhandleElemArr[i].node;
            pQnodeArr[i2].handle = pQnodeArr[this.size].handle;
            pQhandleElemArr[pQnodeArr[i2].handle].node = i2;
            int i3 = this.size - 1;
            this.size = i3;
            if (i2 <= i3) {
                if (i2 <= 1 || LEQ(this.leq, pQhandleElemArr[pQnodeArr[i2 >> 1].handle].key, pQhandleElemArr[pQnodeArr[i2].handle].key)) {
                    FloatDown(i2);
                } else {
                    FloatUp(i2);
                }
            }
            pQhandleElemArr[i].key = null;
            pQhandleElemArr[i].node = this.freeList;
            this.freeList = i;
            return;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: package-private */
    public void pqDeletePriorityQ() {
        this.handles = null;
        this.nodes = null;
    }

    /* access modifiers changed from: package-private */
    public Object pqExtractMin() {
        PriorityQ.PQnode[] pQnodeArr = this.nodes;
        PriorityQ.PQhandleElem[] pQhandleElemArr = this.handles;
        int i = pQnodeArr[1].handle;
        Object obj = pQhandleElemArr[i].key;
        if (this.size > 0) {
            pQnodeArr[1].handle = pQnodeArr[this.size].handle;
            pQhandleElemArr[pQnodeArr[1].handle].node = 1;
            pQhandleElemArr[i].key = null;
            pQhandleElemArr[i].node = this.freeList;
            this.freeList = i;
            int i2 = this.size - 1;
            this.size = i2;
            if (i2 > 0) {
                FloatDown(1);
            }
        }
        return obj;
    }

    /* access modifiers changed from: package-private */
    public boolean pqInit() {
        for (int i = this.size; i >= 1; i--) {
            FloatDown(i);
        }
        this.initialized = true;
        return true;
    }

    /* access modifiers changed from: package-private */
    public int pqInsert(Object obj) {
        int i;
        int i2 = this.size + 1;
        this.size = i2;
        if (i2 * 2 > this.max) {
            PriorityQ.PQnode[] pQnodeArr = this.nodes;
            PriorityQ.PQhandleElem[] pQhandleElemArr = this.handles;
            this.max <<= 1;
            PriorityQ.PQnode[] pQnodeArr2 = new PriorityQ.PQnode[(this.max + 1)];
            System.arraycopy(this.nodes, 0, pQnodeArr2, 0, this.nodes.length);
            for (int length = this.nodes.length; length < pQnodeArr2.length; length++) {
                pQnodeArr2[length] = new PriorityQ.PQnode();
            }
            this.nodes = pQnodeArr2;
            if (this.nodes == null) {
                this.nodes = pQnodeArr;
                return PConstants.MAX_INT;
            }
            PriorityQ.PQhandleElem[] pQhandleElemArr2 = new PriorityQ.PQhandleElem[(this.max + 1)];
            System.arraycopy(this.handles, 0, pQhandleElemArr2, 0, this.handles.length);
            for (int length2 = this.handles.length; length2 < pQhandleElemArr2.length; length2++) {
                pQhandleElemArr2[length2] = new PriorityQ.PQhandleElem();
            }
            this.handles = pQhandleElemArr2;
            if (this.handles == null) {
                this.handles = pQhandleElemArr;
                return PConstants.MAX_INT;
            }
        }
        if (this.freeList == 0) {
            i = i2;
        } else {
            i = this.freeList;
            this.freeList = this.handles[i].node;
        }
        this.nodes[i2].handle = i;
        this.handles[i].node = i2;
        this.handles[i].key = obj;
        if (this.initialized) {
            FloatUp(i2);
        }
        if ($assertionsDisabled || i != Integer.MAX_VALUE) {
            return i;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: package-private */
    public boolean pqIsEmpty() {
        return this.size == 0;
    }

    /* access modifiers changed from: package-private */
    public Object pqMinimum() {
        return this.handles[this.nodes[1].handle].key;
    }
}
