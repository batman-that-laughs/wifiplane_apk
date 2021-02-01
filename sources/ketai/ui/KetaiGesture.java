package ketai.ui;

import android.view.GestureDetector;
import android.view.MotionEvent;
import java.lang.reflect.Method;
import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.TouchEvent;

public class KetaiGesture implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    HashMap<Integer, PVector> cursors = new HashMap<>();
    GestureDetector gestures;
    KetaiGesture me;
    Method onDoubleTapMethod;
    Method onFlickMethod;
    Method onLongPressMethod;
    Method onPinchMethod;
    Method onRotateMethod;
    Method onScrollMethod;
    Method onTapMethod;
    PApplet parent;
    HashMap<Integer, PVector> pcursors = new HashMap<>();

    public KetaiGesture(PApplet _parent) {
        this.parent = _parent;
        this.me = this;
        this.parent.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                KetaiGesture.this.gestures = new GestureDetector(KetaiGesture.this.parent.getActivity(), KetaiGesture.this.me);
            }
        });
        this.parent.registerMethod("touchEvent", this);
        findParentIntentions();
    }

    public boolean onDown(MotionEvent arg0) {
        return true;
    }

    public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        if (this.onFlickMethod != null) {
            try {
                PVector v = new PVector(arg2, arg3);
                this.onFlickMethod.invoke(this.parent, new Object[]{Float.valueOf(arg1.getX()), Float.valueOf(arg1.getY()), Float.valueOf(arg0.getX()), Float.valueOf(arg0.getY()), Float.valueOf(v.mag())});
            } catch (Exception e) {
            }
        }
        return true;
    }

    public void onLongPress(MotionEvent arg0) {
        if (this.onLongPressMethod != null) {
            try {
                this.onLongPressMethod.invoke(this.parent, new Object[]{Float.valueOf(arg0.getX()), Float.valueOf(arg0.getY())});
            } catch (Exception e) {
            }
        }
    }

    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        return true;
    }

    public void onShowPress(MotionEvent arg0) {
    }

    public boolean onSingleTapUp(MotionEvent arg0) {
        if (this.onTapMethod != null) {
            try {
                this.onTapMethod.invoke(this.parent, new Object[]{Float.valueOf(arg0.getX()), Float.valueOf(arg0.getY())});
            } catch (Exception e) {
            }
        }
        return true;
    }

    public void touchEvent(TouchEvent e) {
        PApplet.println("motionEvent called inside kgesture");
        if (e.getNative() instanceof MotionEvent) {
            PApplet.println("KGesture got a MotionEvent!");
            surfaceTouchEvent((MotionEvent) e.getNative());
        }
    }

    public boolean surfaceTouchEvent(MotionEvent event) {
        int code = event.getAction() & 255;
        int index = event.getAction() >> 8;
        float x = event.getX(index);
        float y = event.getY(index);
        int id = event.getPointerId(index);
        if (code == 0 || code == 5) {
            this.cursors.put(Integer.valueOf(id), new PVector(x, y));
        } else if (code == 1 || code == 6) {
            if (this.cursors.containsKey(Integer.valueOf(id))) {
                this.cursors.remove(Integer.valueOf(id));
            }
            if (this.pcursors.containsKey(Integer.valueOf(id))) {
                this.pcursors.remove(Integer.valueOf(id));
            }
        } else if (code == 2) {
            int numPointers = event.getPointerCount();
            for (int i = 0; i < numPointers; i++) {
                int id2 = event.getPointerId(i);
                float x2 = event.getX(i);
                float y2 = event.getY(i);
                if (this.cursors.containsKey(Integer.valueOf(id2))) {
                    this.pcursors.put(Integer.valueOf(id2), this.cursors.get(Integer.valueOf(id2)));
                } else {
                    this.pcursors.put(Integer.valueOf(id2), new PVector(x2, y2));
                }
                this.cursors.put(Integer.valueOf(id2), new PVector(x2, y2));
            }
        }
        analyse();
        this.parent.getActivity().onTouchEvent(event);
        return this.gestures.onTouchEvent(event);
    }

    public boolean onSingleTapConfirmed(MotionEvent arg0) {
        return false;
    }

    public boolean onDoubleTap(MotionEvent arg0) {
        if (this.onDoubleTapMethod != null) {
            try {
                this.onDoubleTapMethod.invoke(this.parent, new Object[]{Float.valueOf(arg0.getX()), Float.valueOf(arg0.getY())});
            } catch (Exception e) {
            }
        }
        return true;
    }

    private void findParentIntentions() {
        try {
            this.onTapMethod = this.parent.getClass().getMethod("onTap", new Class[]{Float.TYPE, Float.TYPE});
        } catch (Exception e) {
        }
        try {
            this.onDoubleTapMethod = this.parent.getClass().getMethod("onDoubleTap", new Class[]{Float.TYPE, Float.TYPE});
        } catch (Exception e2) {
        }
        try {
            this.onFlickMethod = this.parent.getClass().getMethod("onFlick", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE});
        } catch (Exception e3) {
        }
        try {
            this.onScrollMethod = this.parent.getClass().getMethod("onScroll", new Class[]{Integer.TYPE, Integer.TYPE});
        } catch (Exception e4) {
        }
        try {
            this.onLongPressMethod = this.parent.getClass().getMethod("onLongPress", new Class[]{Float.TYPE, Float.TYPE});
        } catch (Exception e5) {
        }
        try {
            this.onPinchMethod = this.parent.getClass().getMethod("onPinch", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE});
        } catch (Exception e6) {
        }
        try {
            this.onRotateMethod = this.parent.getClass().getMethod("onRotate", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE});
        } catch (Exception e7) {
        }
    }

    private synchronized void analyse() {
        if (this.cursors.size() > 1 && this.pcursors.size() > 1) {
            PVector c1 = this.cursors.get(0);
            PVector p1 = this.pcursors.get(0);
            PVector c2 = this.cursors.get(1);
            PVector p2 = this.pcursors.get(1);
            if (!(c1 == null || c2 == null || p1 == null || p2 == null)) {
                float midx = (c1.x + c2.x) / 2.0f;
                float midy = (c1.y + c2.y) / 2.0f;
                float dp = PApplet.dist(p1.x, p1.y, p2.x, p2.y);
                float dc = PApplet.dist(c1.x, c1.y, c2.x, c2.y);
                float delta = PApplet.atan2(PVector.sub(c1, c2).y, PVector.sub(c1, c2).x) - PApplet.atan2(PVector.sub(p1, p2).y, PVector.sub(p1, p2).x);
                if (this.onPinchMethod != null) {
                    try {
                        this.onPinchMethod.invoke(this.parent, new Object[]{Float.valueOf(midx), Float.valueOf(midy), Float.valueOf(dc - dp)});
                    } catch (Exception e) {
                    }
                }
                if (this.onRotateMethod != null) {
                    try {
                        this.onRotateMethod.invoke(this.parent, new Object[]{Float.valueOf(midx), Float.valueOf(midy), Float.valueOf(delta)});
                    } catch (Exception e2) {
                    }
                }
            }
        }
    }

    public boolean onDoubleTapEvent(MotionEvent arg0) {
        return false;
    }
}
