package processing.opengl.tess;

class Dict {
    Object frame;
    DictNode head;
    DictLeq leq;

    public interface DictLeq {
        boolean leq(Object obj, Object obj2, Object obj3);
    }

    private Dict() {
    }

    static void dictDelete(Dict dict, DictNode dictNode) {
        dictNode.next.prev = dictNode.prev;
        dictNode.prev.next = dictNode.next;
    }

    static void dictDeleteDict(Dict dict) {
        dict.head = null;
        dict.frame = null;
        dict.leq = null;
    }

    static DictNode dictInsert(Dict dict, Object obj) {
        return dictInsertBefore(dict, dict.head, obj);
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START, MTH_ENTER_BLOCK, PHI: r4 
      PHI: (r4v1 processing.opengl.tess.DictNode) = (r4v0 processing.opengl.tess.DictNode), (r4v2 processing.opengl.tess.DictNode) binds: [B:0:0x0000, B:3:0x0010] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static processing.opengl.tess.DictNode dictInsertBefore(processing.opengl.tess.Dict r3, processing.opengl.tess.DictNode r4, java.lang.Object r5) {
        /*
        L_0x0000:
            processing.opengl.tess.DictNode r4 = r4.prev
            java.lang.Object r0 = r4.key
            if (r0 == 0) goto L_0x0012
            processing.opengl.tess.Dict$DictLeq r0 = r3.leq
            java.lang.Object r1 = r3.frame
            java.lang.Object r2 = r4.key
            boolean r0 = r0.leq(r1, r2, r5)
            if (r0 == 0) goto L_0x0000
        L_0x0012:
            processing.opengl.tess.DictNode r0 = new processing.opengl.tess.DictNode
            r0.<init>()
            r0.key = r5
            processing.opengl.tess.DictNode r1 = r4.next
            r0.next = r1
            processing.opengl.tess.DictNode r1 = r4.next
            r1.prev = r0
            r0.prev = r4
            r4.next = r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.opengl.tess.Dict.dictInsertBefore(processing.opengl.tess.Dict, processing.opengl.tess.DictNode, java.lang.Object):processing.opengl.tess.DictNode");
    }

    static Object dictKey(DictNode dictNode) {
        return dictNode.key;
    }

    static DictNode dictMax(Dict dict) {
        return dict.head.prev;
    }

    static DictNode dictMin(Dict dict) {
        return dict.head.next;
    }

    static Dict dictNewDict(Object obj, DictLeq dictLeq) {
        Dict dict = new Dict();
        dict.head = new DictNode();
        dict.head.key = null;
        dict.head.next = dict.head;
        dict.head.prev = dict.head;
        dict.frame = obj;
        dict.leq = dictLeq;
        return dict;
    }

    static DictNode dictPred(DictNode dictNode) {
        return dictNode.prev;
    }

    static DictNode dictSearch(Dict dict, Object obj) {
        DictNode dictNode = dict.head;
        do {
            dictNode = dictNode.next;
            if (dictNode.key == null || dict.leq.leq(dict.frame, obj, dictNode.key)) {
                return dictNode;
            }
            dictNode = dictNode.next;
            break;
        } while (dict.leq.leq(dict.frame, obj, dictNode.key));
        return dictNode;
    }

    static DictNode dictSucc(DictNode dictNode) {
        return dictNode.next;
    }
}
