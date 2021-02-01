package processing.test.wifiplane;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import java.util.ArrayList;
import processing.core.PApplet;

public class MainActivity extends Activity {
    private static final String MAIN_FRAGMENT_TAG = "main_fragment";
    private static final int REQUEST_PERMISSIONS = 1;
    PApplet fragment;
    int viewId = 4096;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        requestWindowFeature(1);
        window.setFlags(256, 256);
        window.setFlags(1024, 1024);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(this.viewId);
        setContentView(frame, new ViewGroup.LayoutParams(-1, -1));
        if (savedInstanceState == null) {
            this.fragment = new wifiplane();
            getFragmentManager().beginTransaction().add(frame.getId(), this.fragment, MAIN_FRAGMENT_TAG).commit();
            return;
        }
        this.fragment = (PApplet) getFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
    }

    public void onBackPressed() {
        this.fragment.onBackPressed();
        super.onBackPressed();
    }

    public void onStart() {
        super.onStart();
        ArrayList<String> needed = new ArrayList<>();
        if (!needed.isEmpty()) {
            ActivityCompat.requestPermissions(this, (String[]) needed.toArray(new String[needed.size()]), 1);
        } else if (0 != 0) {
            this.fragment.onPermissionsGranted();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0) {
            for (int i : grantResults) {
                if (i != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("The app cannot run without these permissions, will quit now.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    });
                    builder.create().show();
                }
            }
            this.fragment.onPermissionsGranted();
        }
    }
}
