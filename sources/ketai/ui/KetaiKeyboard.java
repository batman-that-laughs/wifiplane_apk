package ketai.ui;

import android.view.inputmethod.InputMethodManager;
import processing.core.PApplet;

public class KetaiKeyboard {
    public static void toggle(PApplet parent) {
        ((InputMethodManager) parent.getActivity().getSystemService("input_method")).toggleSoftInput(0, 0);
    }

    public static void show(PApplet parent) {
        ((InputMethodManager) parent.getActivity().getSystemService("input_method")).showSoftInput(parent.getActivity().getCurrentFocus(), 0);
    }

    public static void hide(PApplet parent) {
        ((InputMethodManager) parent.getActivity().getSystemService("input_method")).hideSoftInputFromWindow(parent.getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}
