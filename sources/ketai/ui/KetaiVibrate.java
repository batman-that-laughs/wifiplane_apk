package ketai.ui;

import android.os.Vibrator;
import processing.core.PApplet;

public class KetaiVibrate {
    private PApplet parent;
    private Vibrator vibe = ((Vibrator) this.parent.getActivity().getSystemService("vibrator"));

    public KetaiVibrate(PApplet _parent) {
        this.parent = _parent;
    }

    public boolean hasVibrator() {
        return this.vibe.hasVibrator();
    }

    public void vibrate() {
        long[] pattern = new long[2];
        pattern[1] = Long.MAX_VALUE;
        this.vibe.vibrate(pattern, 0);
    }

    public void vibrate(long _duration) {
        this.vibe.vibrate(_duration);
    }

    public void vibrate(long[] pattern, int repeat) {
        this.vibe.vibrate(pattern, repeat);
    }

    public void stop() {
        this.vibe.cancel();
    }
}
