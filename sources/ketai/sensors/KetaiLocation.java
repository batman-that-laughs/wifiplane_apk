package ketai.sensors;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import processing.core.PApplet;

public class KetaiLocation implements LocationListener {
    static final String SERVICE_DESCRIPTION = "Android Location.";
    private Object callbackdelegate;
    private Location location;
    /* access modifiers changed from: private */
    public LocationManager locationManager = null;
    private SensorQueue locationQueue;
    KetaiLocation me;
    /* access modifiers changed from: private */
    public float minDistance = 1.0f;
    /* access modifiers changed from: private */
    public long minTime = 10000;
    private Method onLocationEventMethod1arg;
    private Method onLocationEventMethod2arg;
    private Method onLocationEventMethod3arg;
    private Method onLocationEventMethod4arg;
    private PApplet parent;
    /* access modifiers changed from: private */
    public String provider = "none";

    public KetaiLocation(PApplet pParent) {
        this.parent = pParent;
        this.me = this;
        this.locationManager = (LocationManager) this.parent.getSurface().getContext().getSystemService("location");
        this.locationQueue = new SensorQueue();
        PApplet.println("KetaiLocationManager instantiated:" + this.locationManager.toString());
        findObjectIntentions(this.parent);
        this.parent.requestPermission("android.permission.ACCESS_FINE_LOCATION", "onPermissionResult", this);
        this.parent.registerMethod("dispose", this);
        this.parent.registerMethod("post", this);
    }

    public void onLocationChanged(Location _location) {
        PApplet.println("LocationChanged:" + _location.toString());
        this.locationQueue.add(new Location((Location) _location));
        if (this.onLocationEventMethod1arg != null) {
            try {
                this.onLocationEventMethod1arg.invoke(this.callbackdelegate, new Object[]{_location});
            } catch (Exception e) {
                PApplet.println("Disabling onLocationEvent() because of an error:" + e.getMessage());
                e.printStackTrace();
                this.onLocationEventMethod1arg = null;
            }
        }
    }

    public void post() {
        dequeueLocations();
    }

    private void dequeueLocations() {
        while (this.locationQueue.available()) {
            handleLocationEvent((Location) this.locationQueue.remove());
        }
    }

    private void handleLocationEvent(Location _location) {
        this.location = _location;
        if (this.onLocationEventMethod2arg != null) {
            try {
                this.onLocationEventMethod2arg.invoke(this.callbackdelegate, new Object[]{Double.valueOf(this.location.getLatitude()), Double.valueOf(this.location.getLongitude())});
                return;
            } catch (Exception e) {
                PApplet.println("Disabling onLocationEvent() because of an error:" + e.getMessage());
                e.printStackTrace();
                this.onLocationEventMethod2arg = null;
            }
        }
        if (this.onLocationEventMethod3arg != null) {
            try {
                this.onLocationEventMethod3arg.invoke(this.callbackdelegate, new Object[]{Double.valueOf(this.location.getLatitude()), Double.valueOf(this.location.getLongitude()), Double.valueOf(this.location.getAltitude())});
                return;
            } catch (Exception e2) {
                PApplet.println("Disabling onLocationEvent() because of an error:" + e2.getMessage());
                e2.printStackTrace();
                this.onLocationEventMethod3arg = null;
            }
        }
        if (this.onLocationEventMethod4arg != null) {
            try {
                this.onLocationEventMethod4arg.invoke(this.callbackdelegate, new Object[]{Double.valueOf(this.location.getLatitude()), Double.valueOf(this.location.getLongitude()), Double.valueOf(this.location.getAltitude()), Float.valueOf(this.location.getAccuracy())});
            } catch (Exception e3) {
                PApplet.println("Disabling onLocationEvent() because of an error:" + e3.getMessage());
                e3.printStackTrace();
                this.onLocationEventMethod4arg = null;
            }
        }
    }

    public Location getLocation() {
        return this.location;
    }

    public boolean isStarted() {
        return this.onLocationEventMethod4arg != null;
    }

    public void onPermissionResult(boolean granted) {
        if (granted) {
            start();
            return;
        }
        PApplet.println("User did not grant location permission.  Location is disabled.");
        this.provider = "none";
    }

    public void start() {
        Location l;
        PApplet.println("KetaiLocationManager: start()...");
        List<String> foo = this.locationManager.getAllProviders();
        PApplet.println("KetaiLocationManager All Provider(s) list: ");
        for (String s : foo) {
            PApplet.println("\t" + s);
        }
        if (!determineProvider()) {
            PApplet.println("Error obtaining location provider.  Check your location settings.");
            this.provider = "none";
        }
        if (this.location == null) {
            List<String> foo2 = this.locationManager.getProviders(true);
            PApplet.println("KetaiLocationManager Enabled Provider(s) list: ");
            for (String s2 : foo2) {
                if (this.location == null && (l = this.locationManager.getLastKnownLocation(s2)) != null) {
                    this.location = new Location(l);
                    PApplet.println("\t" + s2 + " - lastLocation for provider:" + this.location.toString());
                }
            }
            if (this.location == null) {
                this.location = new Location("default");
            }
        }
        onLocationChanged(this.location);
    }

    public void stop() {
        PApplet.println("KetaiLocationManager: Stop()....");
        this.locationManager.removeUpdates(this);
    }

    public void dispose() {
        stop();
    }

    public void onProviderDisabled(String arg0) {
        PApplet.println("LocationManager onProviderDisabled: " + arg0);
        determineProvider();
    }

    public void onProviderEnabled(String arg0) {
        PApplet.println("LocationManager onProviderEnabled: " + arg0);
        determineProvider();
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        PApplet.println("LocationManager onStatusChanged: " + arg0 + ":" + arg1 + ":" + arg2.toString());
        determineProvider();
    }

    public String getProvider() {
        return this.provider;
    }

    public Collection<? extends String> list() {
        Vector<String> list = new Vector<>();
        list.add("Location");
        return list;
    }

    public void setUpdateRate(int millis, int meters) {
        this.minTime = (long) millis;
        this.minDistance = (float) meters;
        determineProvider();
    }

    private boolean determineProvider() {
        if (this.locationManager.isProviderEnabled("gps")) {
            this.provider = "gps";
        } else {
            this.provider = this.locationManager.getBestProvider(new Criteria(), true);
        }
        if (this.provider == null) {
            return false;
        }
        PApplet.println("Requesting location updates from: " + this.provider);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                KetaiLocation.this.locationManager.requestLocationUpdates(KetaiLocation.this.provider, KetaiLocation.this.minTime, KetaiLocation.this.minDistance, KetaiLocation.this.me);
            }
        });
        return true;
    }

    private void findObjectIntentions(Object o) {
        this.callbackdelegate = o;
        try {
            this.onLocationEventMethod1arg = this.callbackdelegate.getClass().getMethod("onLocationEvent", new Class[]{Location.class});
            PApplet.println("Found Advanced onLocationEventMethod(Location)...");
        } catch (NoSuchMethodException e) {
        }
        try {
            this.onLocationEventMethod2arg = this.callbackdelegate.getClass().getMethod("onLocationEvent", new Class[]{Double.TYPE, Double.TYPE});
            PApplet.println("Found Advanced onLocationEventMethod(long, lat)...");
        } catch (NoSuchMethodException e2) {
        }
        try {
            this.onLocationEventMethod3arg = this.callbackdelegate.getClass().getMethod("onLocationEvent", new Class[]{Double.TYPE, Double.TYPE, Double.TYPE});
            PApplet.println("Found basic onLocationEventMethod(long,lat,alt)...");
        } catch (NoSuchMethodException e3) {
        }
        try {
            this.onLocationEventMethod4arg = this.callbackdelegate.getClass().getMethod("onLocationEvent", new Class[]{Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE});
            PApplet.println("Found basic onLocationEventMethod(long,lat,alt, acc)...");
        } catch (NoSuchMethodException e4) {
        }
    }

    public void onLocationChanged(Location arg0) {
        onLocationChanged(new Location(arg0));
    }

    public void register(Object delegate) {
        boolean running = isStarted();
        if (running) {
            stop();
        }
        findObjectIntentions(delegate);
        if (running) {
            start();
        }
    }
}
