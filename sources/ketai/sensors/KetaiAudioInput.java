package ketai.sensors;

import android.media.AudioRecord;
import android.util.Log;
import java.lang.reflect.Method;
import processing.core.PApplet;

public class KetaiAudioInput implements Runnable {
    private String LOG_TAG = "KetaiAudioInput";
    private AudioRecord audioRecorder = null;
    private int bufferSize;
    private Method callbackMethod;
    public Object callbackdelegate;
    private boolean isRecording;
    private int samplesPerSec = 16000;
    private Thread thread = null;

    public KetaiAudioInput(Object consumer) {
        register(consumer);
    }

    public void start() {
        this.bufferSize = AudioRecord.getMinBufferSize(this.samplesPerSec, 16, 2);
        if (this.bufferSize == -2 || this.bufferSize == -1) {
            Log.e(this.LOG_TAG, "Unable to get minimum buffer size");
            return;
        }
        PApplet.println("Buffer size: " + this.bufferSize);
        this.audioRecorder = new AudioRecord(0, this.samplesPerSec, 16, 2, this.bufferSize * 10);
        if (this.audioRecorder == null || this.audioRecorder.getState() != 1) {
            Log.e(this.LOG_TAG, "Unable to create AudioRecord instance");
            PApplet.println("Unable to create AudioRecord instance");
            return;
        }
        Log.i(this.LOG_TAG, "Audio Recorder created");
        PApplet.println("Audio Recorder created");
        this.audioRecorder.startRecording();
        this.isRecording = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void stop() {
        this.isRecording = false;
        if (this.audioRecorder != null) {
            if (this.audioRecorder.getRecordingState() == 3) {
                this.audioRecorder.stop();
            }
            if (this.audioRecorder.getState() == 1) {
                this.audioRecorder.release();
            }
        }
    }

    public void dispose() {
        stop();
    }

    public boolean isActive() {
        return this.audioRecorder != null && this.audioRecorder.getRecordingState() == 3;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r7 = this;
            r6 = 0
            r2 = -19
            android.os.Process.setThreadPriority(r2)
        L_0x0006:
            boolean r2 = r7.isRecording
            if (r2 == 0) goto L_0x0013
            android.media.AudioRecord r2 = r7.audioRecorder
            int r2 = r2.getRecordingState()
            r3 = 3
            if (r2 == r3) goto L_0x0014
        L_0x0013:
            return
        L_0x0014:
            int r2 = r7.bufferSize
            short[] r0 = new short[r2]
            android.media.AudioRecord r2 = r7.audioRecorder
            int r3 = r0.length
            r2.read(r0, r6, r3)
            java.lang.reflect.Method r2 = r7.callbackMethod     // Catch:{ Exception -> 0x002c }
            java.lang.Object r3 = r7.callbackdelegate     // Catch:{ Exception -> 0x002c }
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x002c }
            r5 = 0
            r4[r5] = r0     // Catch:{ Exception -> 0x002c }
            r2.invoke(r3, r4)     // Catch:{ Exception -> 0x002c }
            goto L_0x0006
        L_0x002c:
            r1 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "OOps... onAudioEvent() because of an error:"
            r2.<init>(r3)
            java.lang.String r3 = r1.getMessage()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            processing.core.PApplet.println((java.lang.String) r2)
            r1.printStackTrace()
            goto L_0x0006
        */
        throw new UnsupportedOperationException("Method not decompiled: ketai.sensors.KetaiAudioInput.run():void");
    }

    public void register(Object o) {
        this.callbackdelegate = o;
        if (this.callbackdelegate instanceof PApplet) {
            PApplet parent = (PApplet) this.callbackdelegate;
            parent.requestPermission("android.permission.RECORD_AUDIO", "onPermissionResult", this);
            parent.registerMethod("dispose", this);
        }
        try {
            this.callbackMethod = o.getClass().getMethod("onAudioEvent", new Class[]{short[].class});
            PApplet.println("Found onAudioEvent callback method...");
        } catch (NoSuchMethodException e) {
            PApplet.println("Failed to find onAudioEvent callback method...");
        }
    }

    public void onPermissionResult(boolean granted) {
        if (!granted) {
            PApplet.println("User did not allow to record audio.  Audio recording is disabled.");
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        System.out.println("AudioCapturer finalizer");
        if (this.audioRecorder != null && this.audioRecorder.getState() == 1) {
            this.audioRecorder.stop();
            this.audioRecorder.release();
        }
        this.audioRecorder = null;
        this.thread = null;
    }
}
