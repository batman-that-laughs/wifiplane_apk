package ketai.ui;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.lang.reflect.Method;
import java.util.ArrayList;
import processing.core.PApplet;

public class KetaiList extends ListView {
    /* access modifiers changed from: private */
    public ArrayAdapter<String> adapter;
    RelativeLayout layout;
    String name = "KetaiList";
    /* access modifiers changed from: private */
    public PApplet parent;
    /* access modifiers changed from: private */
    public Method parentCallback;
    String selection = "";
    ListView self;
    String title = "";

    public KetaiList(PApplet _parent, ArrayList<String> data) {
        super(_parent.getActivity().getApplicationContext());
        this.parent = _parent;
        this.adapter = new ArrayAdapter<>(this.parent.getActivity(), 17367043, data);
        init();
    }

    public KetaiList(PApplet _parent, String[] data) {
        super(_parent.getActivity().getApplicationContext());
        this.parent = _parent;
        this.adapter = new ArrayAdapter<>(this.parent.getActivity(), 17367043, data);
        init();
    }

    public KetaiList(PApplet _parent, String _title, String[] data) {
        super(_parent.getActivity().getApplicationContext());
        this.parent = _parent;
        this.title = _title;
        this.adapter = new ArrayAdapter<>(this.parent.getActivity(), 17367043, data);
        init();
    }

    public KetaiList(PApplet _parent, String _title, ArrayList<String> data) {
        super(_parent.getActivity().getApplicationContext());
        this.parent = _parent;
        this.title = _title;
        this.adapter = new ArrayAdapter<>(this.parent.getActivity(), 17367043, data);
        init();
    }

    public void refresh() {
        if (this.adapter != null) {
            this.parent.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    KetaiList.this.adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public String getSelection() {
        return this.selection;
    }

    private void init() {
        setBackgroundColor(-3355444);
        setAlpha(1.0f);
        this.self = this;
        this.layout = new RelativeLayout(this.parent.getActivity());
        if (this.title != "") {
            TextView tv = new TextView(this.parent.getActivity());
            tv.setText(this.title);
            setHeaderDividersEnabled(true);
            addHeaderView(tv);
        }
        try {
            this.parentCallback = this.parent.getClass().getMethod("onKetaiListSelection", new Class[]{KetaiList.class});
            PApplet.println("Found onKetaiListSelection...");
        } catch (NoSuchMethodException e) {
        }
        setAdapter(this.adapter);
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                KetaiList.this.selection = ((String) KetaiList.this.adapter.getItem(position)).toString();
                KetaiList.this.layout.removeAllViewsInLayout();
                try {
                    KetaiList.this.parentCallback.invoke(KetaiList.this.parent, new Object[]{KetaiList.this.self});
                } catch (Exception e) {
                }
                KetaiList.this.self.setVisibility(8);
                ((ViewManager) KetaiList.this.self.getParent()).removeView(KetaiList.this.self);
                KetaiList.this.parent.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        KetaiList.this.layout.removeAllViews();
                        KetaiList.this.layout.setVisibility(8);
                    }
                });
            }
        });
        this.parent.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                KetaiList.this.parent.getActivity().addContentView(KetaiList.this.self, new ViewGroup.LayoutParams(-1, -1));
            }
        });
    }
}
