package ketai.sensors;

import processing.core.PApplet;

public class SensorQueue {
    private int count;
    private int offset;
    private Object[] queue = new Object[10];

    /* access modifiers changed from: package-private */
    public synchronized void add(Object val) {
        if (this.count == this.queue.length) {
            this.queue = (Object[]) PApplet.expand((Object) this.queue);
        }
        Object[] objArr = this.queue;
        int i = this.count;
        this.count = i + 1;
        objArr[i] = val;
    }

    /* access modifiers changed from: package-private */
    public synchronized Object remove() {
        Object outgoing;
        if (this.offset == this.count) {
            throw new RuntimeException("Sensor queue is empty.");
        }
        Object[] objArr = this.queue;
        int i = this.offset;
        this.offset = i + 1;
        outgoing = objArr[i];
        if (this.offset == this.count) {
            this.offset = 0;
            this.count = 0;
        }
        return outgoing;
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean available() {
        return this.count != 0;
    }
}
