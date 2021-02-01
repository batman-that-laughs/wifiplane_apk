package processing.opengl.tess;

abstract class PriorityQ {
    public static final int INIT_SIZE = 32;

    public interface Leq {
        boolean leq(Object obj, Object obj2);
    }

    public static class PQhandleElem {
        Object key;
        int node;
    }

    public static class PQnode {
        int handle;
    }

    PriorityQ() {
    }

    public static boolean LEQ(Leq leq, Object obj, Object obj2) {
        return Geom.VertLeq((GLUvertex) obj, (GLUvertex) obj2);
    }

    static PriorityQ pqNewPriorityQ(Leq leq) {
        return new PriorityQSort(leq);
    }

    /* access modifiers changed from: package-private */
    public abstract void pqDelete(int i);

    /* access modifiers changed from: package-private */
    public abstract void pqDeletePriorityQ();

    /* access modifiers changed from: package-private */
    public abstract Object pqExtractMin();

    /* access modifiers changed from: package-private */
    public abstract boolean pqInit();

    /* access modifiers changed from: package-private */
    public abstract int pqInsert(Object obj);

    /* access modifiers changed from: package-private */
    public abstract boolean pqIsEmpty();

    /* access modifiers changed from: package-private */
    public abstract Object pqMinimum();
}
