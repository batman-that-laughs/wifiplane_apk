package ketai.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;
import processing.core.PApplet;

public class KetaiSensor implements SensorEventListener {
    static final String SERVICE_DESCRIPTION = "Android Sensors.";
    float[] accelerometerData;
    private boolean accelerometerSensorEnabled;
    private boolean ambientTemperatureSensorEnabled;
    public Object callbackdelegate;
    private long delayInterval;
    private SensorQueue eventQueue;
    private boolean gameRotationSensorEnabled;
    private boolean geomagneticRotationVectorSensorEnabled;
    private boolean gravitySensorEnabled;
    private boolean gyroscopeSensorEnabled;
    private boolean heartRateSensorEnabled;
    private float[] inclinationMat;
    private boolean isRegistered = false;
    private boolean lightSensorEnabled;
    private boolean linearAccelerationSensorEnabled;
    private boolean magneticFieldSensorEnabled;
    float[] magnetometerData;
    private Method onAccelerometerEventMethod;
    private Method onAccelerometerEventMethodSimple;
    private Method onAmbientTemperatureEventMethod;
    private Method onGameRotationEventMethod;
    private Method onGeomagneticRotationVectorEventMethod;
    private Method onGravitySensorEventMethod;
    private Method onGravitySensorEventMethodSimple;
    private Method onGyroscopeSensorEventMethod;
    private Method onGyroscopeSensorEventMethodSimple;
    private Method onHeartRateEventMethod;
    private Method onLightSensorEventMethod;
    private Method onLightSensorEventMethodSimple;
    private Method onLinearAccelerationSensorEventMethod;
    private Method onLinearAccelerationSensorEventMethodSimple;
    private Method onMagneticFieldSensorEventMethod;
    private Method onMagneticFieldSensorEventMethodSimple;
    private Method onOrientationSensorEventMethod;
    private Method onOrientationSensorEventMethodSimple;
    private Method onPressureSensorEventMethod;
    private Method onPressureSensorEventMethodSimple;
    private Method onProximitySensorEventMethod;
    private Method onProximitySensorEventMethodSimple;
    private Method onRelativeHumidityEventMethod;
    private Method onRotationVectorSensorEventMethod;
    private Method onRotationVectorSensorEventMethodSimple;
    private Method onSensorEventMethod;
    private Method onSignificantMotionEventMethod;
    private Method onStepCounterEventMethod;
    private Method onStepDetectorEventMethod;
    private Method onTemperatureSensorEventMethod;
    private Method onTemperatureSensorEventMethodSimple;
    private boolean orientationSensorEnabled;
    private float[] orientationVec;
    private PApplet parent;
    private boolean pressureSensorEnabled;
    private boolean proximitySensorEnabled;
    private boolean relativeHumiditySensorEnabled;
    private float[] rotationMat;
    private boolean rotationVectorSensorEnabled;
    private int samplingRate = 2;
    private SensorManager sensorManager = null;
    private boolean significantMotionSensorEnabled;
    private boolean stepCounterSensorEnabled;
    private boolean stepDetectorSensorEnabled;
    private boolean temperatureSensorEnabled;
    private long timeOfLastUpdate;
    private boolean useSimulator;
    private float[] zeroes = {0.0f, 0.0f, 0.0f};

    public KetaiSensor(PApplet pParent) {
        this.parent = pParent;
        findParentIntentions();
        this.useSimulator = false;
        this.sensorManager = (SensorManager) this.parent.getContext().getSystemService("sensor");
        this.eventQueue = new SensorQueue();
        this.timeOfLastUpdate = 0;
        this.delayInterval = 0;
        this.parent.registerMethod("dispose", this);
        this.parent.registerMethod("post", this);
    }

    public static boolean remapCoordinateSystem(float[] inR, int X, int Y, float[] outR) {
        return SensorManager.remapCoordinateSystem(inR, X, Y, outR);
    }

    public void useSimulator(boolean flag) {
        this.useSimulator = flag;
    }

    public boolean usingSimulator() {
        return this.useSimulator;
    }

    public void setDelayInterval(long pDelayInterval) {
        this.delayInterval = pDelayInterval;
    }

    public void setSamplingRate(int pSamplingInterval) {
        this.samplingRate = pSamplingInterval;
    }

    public void enableAccelerometer() {
        this.accelerometerSensorEnabled = true;
    }

    public void enableRotationVector() {
        this.rotationVectorSensorEnabled = true;
    }

    public void enableLinearAcceleration() {
        this.linearAccelerationSensorEnabled = true;
    }

    public void disableAccelerometer() {
        this.accelerometerSensorEnabled = true;
    }

    public void enableMagenticField() {
        this.magneticFieldSensorEnabled = true;
    }

    public void disableMagneticField() {
        this.magneticFieldSensorEnabled = true;
    }

    public void enableOrientation() {
        this.orientationSensorEnabled = true;
    }

    public void disableOrientation() {
        this.orientationSensorEnabled = false;
    }

    public void enableProximity() {
        this.proximitySensorEnabled = true;
    }

    public void disableProximity() {
        this.proximitySensorEnabled = false;
    }

    public void disablelinearAcceleration() {
        this.linearAccelerationSensorEnabled = false;
    }

    public void disableRotationVector() {
        this.rotationVectorSensorEnabled = false;
    }

    public void enableLight() {
        this.lightSensorEnabled = true;
    }

    public void disableLight() {
        this.lightSensorEnabled = true;
    }

    public void enablePressure() {
        this.pressureSensorEnabled = true;
    }

    public void disablePressure() {
        this.pressureSensorEnabled = true;
    }

    public void enableTemperature() {
        this.temperatureSensorEnabled = true;
    }

    public void disableTemperature() {
        this.temperatureSensorEnabled = false;
    }

    public void enableGyroscope() {
        this.gyroscopeSensorEnabled = true;
    }

    public void disableGyroscope() {
        this.gyroscopeSensorEnabled = false;
    }

    public void disableAmibentTemperature() {
        this.ambientTemperatureSensorEnabled = false;
    }

    public void disableRelativeHumiditySensor() {
        this.relativeHumiditySensorEnabled = false;
    }

    public void enableAmibentTemperature() {
        this.ambientTemperatureSensorEnabled = true;
    }

    public void enableRelativeHumiditySensor() {
        this.relativeHumiditySensorEnabled = true;
    }

    public void enableStepDetectorSensor() {
        this.stepDetectorSensorEnabled = true;
    }

    public void disableStepDetectorSensor() {
        this.stepDetectorSensorEnabled = false;
    }

    public void enableStepCounterSensor() {
        this.stepCounterSensorEnabled = true;
    }

    public void disableStepCounterSensor() {
        this.stepCounterSensorEnabled = false;
    }

    public void enableSignificantMotionSensor() {
        this.significantMotionSensorEnabled = true;
    }

    public void disableSignificantMotionSensor() {
        this.significantMotionSensorEnabled = false;
    }

    public void enableHeartRateSensor() {
        this.heartRateSensorEnabled = true;
    }

    public void disableHeartRateSensor() {
        this.heartRateSensorEnabled = false;
    }

    public void enableGeomagneticRotationVectorSensor() {
        this.geomagneticRotationVectorSensorEnabled = true;
    }

    public void disableGeomagneticRotationVectorSensor() {
        this.geomagneticRotationVectorSensorEnabled = false;
    }

    public void enableGameRotationSensor() {
        this.gameRotationSensorEnabled = true;
    }

    public void disableGameRotationSensor() {
        this.gameRotationSensorEnabled = false;
    }

    public void enableAllSensors() {
        this.stepDetectorSensorEnabled = true;
        this.stepCounterSensorEnabled = true;
        this.significantMotionSensorEnabled = true;
        this.heartRateSensorEnabled = true;
        this.geomagneticRotationVectorSensorEnabled = true;
        this.gameRotationSensorEnabled = true;
        this.relativeHumiditySensorEnabled = true;
        this.ambientTemperatureSensorEnabled = true;
        this.rotationVectorSensorEnabled = true;
        this.linearAccelerationSensorEnabled = true;
        this.gyroscopeSensorEnabled = true;
        this.temperatureSensorEnabled = true;
        this.pressureSensorEnabled = true;
        this.lightSensorEnabled = true;
        this.proximitySensorEnabled = true;
        this.orientationSensorEnabled = true;
        this.magneticFieldSensorEnabled = true;
        this.accelerometerSensorEnabled = true;
    }

    public boolean isAccelerometerAvailable() {
        return isSensorSupported(1);
    }

    public boolean isLinearAccelerationAvailable() {
        return isSensorSupported(10);
    }

    public boolean isRotationVectorAvailable() {
        return isSensorSupported(11);
    }

    public boolean isMagenticFieldAvailable() {
        return isSensorSupported(2);
    }

    public boolean isOrientationAvailable() {
        return isSensorSupported(3);
    }

    public boolean isProximityAvailable() {
        return isSensorSupported(8);
    }

    public boolean isLightAvailable() {
        return isSensorSupported(5);
    }

    public boolean isPressureAvailable() {
        return isSensorSupported(6);
    }

    public boolean isTemperatureAvailable() {
        return isSensorSupported(7);
    }

    public boolean isGyroscopeAvailable() {
        return isSensorSupported(4);
    }

    public boolean isAmbientTemperatureAvailable() {
        return isSensorSupported(13);
    }

    public boolean isRelativeHumidityAvailable() {
        return isSensorSupported(12);
    }

    public boolean isStepDetectorAvailable() {
        return isSensorSupported(18);
    }

    public boolean isStepCounterAvailable() {
        return isSensorSupported(19);
    }

    public boolean isSignificantMotionAvailable() {
        return isSensorSupported(17);
    }

    public boolean isGeomagneticRotationVectorAvailable() {
        return isSensorSupported(20);
    }

    public boolean isGameRotationAvailable() {
        return isSensorSupported(15);
    }

    public Collection<? extends String> list() {
        Vector<String> list = new Vector<>();
        for (Sensor s : this.sensorManager.getSensorList(-1)) {
            list.add(s.getName());
            PApplet.println("\tKetaiSensor sensor: " + s.getName() + ":" + s.getType());
        }
        return list;
    }

    public boolean isStarted() {
        return this.isRegistered;
    }

    public void start() {
        Sensor s;
        Sensor s2;
        Sensor s3;
        Sensor s4;
        Sensor s5;
        Sensor s6;
        Sensor s7;
        Sensor s8;
        Sensor s9;
        Sensor s10;
        Sensor s11;
        Sensor s12;
        Sensor s13;
        Sensor s14;
        Sensor s15;
        Sensor s16;
        Sensor s17;
        Sensor s18;
        PApplet.println("KetaiSensor: start()...");
        if (this.accelerometerSensorEnabled && (s18 = this.sensorManager.getDefaultSensor(1)) != null) {
            this.sensorManager.registerListener(this, s18, this.samplingRate);
        }
        if (this.magneticFieldSensorEnabled && (s17 = this.sensorManager.getDefaultSensor(2)) != null) {
            this.sensorManager.registerListener(this, s17, this.samplingRate);
        }
        if (this.pressureSensorEnabled && (s16 = this.sensorManager.getDefaultSensor(6)) != null) {
            this.sensorManager.registerListener(this, s16, this.samplingRate);
        }
        if (this.orientationSensorEnabled && (s15 = this.sensorManager.getDefaultSensor(3)) != null) {
            this.sensorManager.registerListener(this, s15, this.samplingRate);
        }
        if (this.proximitySensorEnabled && (s14 = this.sensorManager.getDefaultSensor(8)) != null) {
            this.sensorManager.registerListener(this, s14, this.samplingRate);
        }
        if (this.temperatureSensorEnabled && (s13 = this.sensorManager.getDefaultSensor(7)) != null) {
            this.sensorManager.registerListener(this, s13, this.samplingRate);
        }
        if (this.gyroscopeSensorEnabled && (s12 = this.sensorManager.getDefaultSensor(4)) != null) {
            this.sensorManager.registerListener(this, s12, this.samplingRate);
        }
        if (this.rotationVectorSensorEnabled && (s11 = this.sensorManager.getDefaultSensor(11)) != null) {
            this.sensorManager.registerListener(this, s11, this.samplingRate);
        }
        if (this.linearAccelerationSensorEnabled && (s10 = this.sensorManager.getDefaultSensor(10)) != null) {
            this.sensorManager.registerListener(this, s10, this.samplingRate);
        }
        if (this.lightSensorEnabled && (s9 = this.sensorManager.getDefaultSensor(5)) != null) {
            this.sensorManager.registerListener(this, s9, this.samplingRate);
        }
        if (this.gravitySensorEnabled && (s8 = this.sensorManager.getDefaultSensor(9)) != null) {
            this.sensorManager.registerListener(this, s8, this.samplingRate);
        }
        if (this.ambientTemperatureSensorEnabled && (s7 = this.sensorManager.getDefaultSensor(13)) != null) {
            this.sensorManager.registerListener(this, s7, this.samplingRate);
        }
        if (this.relativeHumiditySensorEnabled && (s6 = this.sensorManager.getDefaultSensor(12)) != null) {
            this.sensorManager.registerListener(this, s6, this.samplingRate);
        }
        if (this.stepDetectorSensorEnabled && (s5 = this.sensorManager.getDefaultSensor(18)) != null) {
            this.sensorManager.registerListener(this, s5, this.samplingRate);
        }
        if (this.stepCounterSensorEnabled && (s4 = this.sensorManager.getDefaultSensor(19)) != null) {
            this.sensorManager.registerListener(this, s4, this.samplingRate);
        }
        if (this.significantMotionSensorEnabled && (s3 = this.sensorManager.getDefaultSensor(17)) != null) {
            this.sensorManager.registerListener(this, s3, this.samplingRate);
        }
        if (this.geomagneticRotationVectorSensorEnabled && (s2 = this.sensorManager.getDefaultSensor(20)) != null) {
            this.sensorManager.registerListener(this, s2, this.samplingRate);
        }
        if (this.gameRotationSensorEnabled && (s = this.sensorManager.getDefaultSensor(15)) != null) {
            this.sensorManager.registerListener(this, s, this.samplingRate);
        }
        this.isRegistered = true;
    }

    public void stop() {
        PApplet.println("KetaiSensor: Stop()....");
        this.sensorManager.unregisterListener(this);
        this.isRegistered = false;
    }

    public void dispose() {
        stop();
    }

    public void onSensorChanged(SensorEvent arg0) {
        long now = new Date().getTime();
        if (now >= this.timeOfLastUpdate + this.delayInterval) {
            if (this.onSensorEventMethod != null) {
                try {
                    this.onSensorEventMethod.invoke(this.callbackdelegate, new Object[]{arg0});
                    return;
                } catch (Exception e) {
                    PApplet.println("Disabling onSensorEvent() because of an error:" + e.getMessage());
                    e.printStackTrace();
                    this.onSensorEventMethod = null;
                }
            }
            this.eventQueue.add(new SensorData(arg0));
            this.timeOfLastUpdate = now;
        }
    }

    public void post() {
        dequeueEvents();
    }

    private void dequeueEvents() {
        while (this.eventQueue.available()) {
            handleSensorEvent((SensorData) this.eventQueue.remove());
        }
    }

    private void handleSensorEvent(SensorData arg0) {
        if (arg0.sensorType == 1 && this.accelerometerSensorEnabled) {
            this.accelerometerData = (float[]) arg0.values.clone();
            if (this.onAccelerometerEventMethod != null) {
                try {
                    this.onAccelerometerEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2]), Long.valueOf(arg0.timestamp), Integer.valueOf(arg0.accuracy)});
                    return;
                } catch (Exception e) {
                    PApplet.println("Error onAccelerometerEvent():" + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (this.onAccelerometerEventMethodSimple != null) {
                try {
                    this.onAccelerometerEventMethodSimple.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2])});
                    return;
                } catch (Exception e2) {
                    PApplet.println("Error onAccelerometerEvent() [simple]:" + e2.getMessage());
                    e2.printStackTrace();
                }
            }
        }
        if (arg0.sensorType == 9 && this.gravitySensorEnabled) {
            if (this.onGravitySensorEventMethod != null) {
                try {
                    this.onGravitySensorEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2]), Long.valueOf(arg0.timestamp), Integer.valueOf(arg0.accuracy)});
                    return;
                } catch (Exception e3) {
                    PApplet.println("Error onGravityEvent():" + e3.getMessage());
                    e3.printStackTrace();
                }
            }
            if (this.onGravitySensorEventMethodSimple != null) {
                try {
                    this.onGravitySensorEventMethodSimple.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2])});
                    return;
                } catch (Exception e4) {
                    PApplet.println("Error onGravityEvent()[simple]:" + e4.getMessage());
                    e4.printStackTrace();
                }
            }
        }
        if (arg0.sensorType == 3 && this.orientationSensorEnabled) {
            if (this.onOrientationSensorEventMethod != null) {
                try {
                    this.onOrientationSensorEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2]), Long.valueOf(arg0.timestamp), Integer.valueOf(arg0.accuracy)});
                    return;
                } catch (Exception e5) {
                    PApplet.println("Error onOrientationEvent():" + e5.getMessage());
                    e5.printStackTrace();
                }
            }
            if (this.onOrientationSensorEventMethodSimple != null) {
                try {
                    this.onOrientationSensorEventMethodSimple.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2])});
                    return;
                } catch (Exception e6) {
                    PApplet.println("Error onOrientationEvent()[simple] :" + e6.getMessage());
                    e6.printStackTrace();
                }
            }
        }
        if (arg0.sensorType == 2 && this.magneticFieldSensorEnabled) {
            this.magnetometerData = (float[]) arg0.values.clone();
            if (this.onMagneticFieldSensorEventMethod != null) {
                try {
                    this.onMagneticFieldSensorEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2]), Long.valueOf(arg0.timestamp), Integer.valueOf(arg0.accuracy)});
                    return;
                } catch (Exception e7) {
                    PApplet.println("Error onMagneticFieldEvent():" + e7.getMessage());
                    e7.printStackTrace();
                }
            }
            if (this.onMagneticFieldSensorEventMethodSimple != null) {
                try {
                    this.onMagneticFieldSensorEventMethodSimple.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2])});
                    return;
                } catch (Exception e8) {
                    PApplet.println("Error onMagneticFieldEvent()[simple]:" + e8.getMessage());
                    e8.printStackTrace();
                }
            }
        }
        if (arg0.sensorType == 4 && this.gyroscopeSensorEnabled) {
            if (this.onGyroscopeSensorEventMethod != null) {
                try {
                    this.onGyroscopeSensorEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2]), Long.valueOf(arg0.timestamp), Integer.valueOf(arg0.accuracy)});
                    return;
                } catch (Exception e9) {
                    PApplet.println("Error onGyroscopeEvent():" + e9.getMessage());
                    e9.printStackTrace();
                }
            }
            if (this.onGyroscopeSensorEventMethodSimple != null) {
                try {
                    this.onGyroscopeSensorEventMethodSimple.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2])});
                    return;
                } catch (Exception e10) {
                    PApplet.println("Error onGyroscopeEvent()[simple]:" + e10.getMessage());
                    e10.printStackTrace();
                }
            }
        }
        if (arg0.sensorType == 5 && this.lightSensorEnabled) {
            if (this.onLightSensorEventMethod != null) {
                try {
                    this.onLightSensorEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Long.valueOf(arg0.timestamp), Integer.valueOf(arg0.accuracy)});
                    return;
                } catch (Exception e11) {
                    PApplet.println("Error onLightEvent():" + e11.getMessage());
                    e11.printStackTrace();
                }
            }
            if (this.onLightSensorEventMethodSimple != null) {
                try {
                    this.onLightSensorEventMethodSimple.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0])});
                    return;
                } catch (Exception e12) {
                    PApplet.println("Error onLightEvent()[simple]r:" + e12.getMessage());
                    e12.printStackTrace();
                }
            }
        }
        if (arg0.sensorType == 8 && this.proximitySensorEnabled) {
            if (this.onProximitySensorEventMethod != null) {
                try {
                    this.onProximitySensorEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Long.valueOf(arg0.timestamp), Integer.valueOf(arg0.accuracy)});
                    return;
                } catch (Exception e13) {
                    PApplet.println("Error onProximityEvent():" + e13.getMessage());
                    e13.printStackTrace();
                }
            }
            if (this.onProximitySensorEventMethodSimple != null) {
                try {
                    this.onProximitySensorEventMethodSimple.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0])});
                    return;
                } catch (Exception e14) {
                    PApplet.println("Error onProximityEvent()[simple]:" + e14.getMessage());
                    e14.printStackTrace();
                }
            }
        }
        if (arg0.sensorType == 6 && this.pressureSensorEnabled) {
            if (this.onPressureSensorEventMethod != null) {
                try {
                    this.onPressureSensorEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Long.valueOf(arg0.timestamp), Integer.valueOf(arg0.accuracy)});
                    return;
                } catch (Exception e15) {
                    PApplet.println("Error onPressureEvent()r:" + e15.getMessage());
                    e15.printStackTrace();
                }
            }
            if (this.onPressureSensorEventMethodSimple != null) {
                try {
                    this.onPressureSensorEventMethodSimple.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0])});
                    return;
                } catch (Exception e16) {
                    PApplet.println("Error onPressureEvent()[simple]:" + e16.getMessage());
                    e16.printStackTrace();
                }
            }
        }
        if (arg0.sensorType == 7 && this.temperatureSensorEnabled) {
            if (this.onTemperatureSensorEventMethod != null) {
                try {
                    this.onTemperatureSensorEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Long.valueOf(arg0.timestamp), Integer.valueOf(arg0.accuracy)});
                    return;
                } catch (Exception e17) {
                    PApplet.println("Error onTemperatureEvent():" + e17.getMessage());
                    e17.printStackTrace();
                }
            }
            if (this.onTemperatureSensorEventMethodSimple != null) {
                try {
                    this.onTemperatureSensorEventMethodSimple.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0])});
                    return;
                } catch (Exception e18) {
                    PApplet.println("Error onTemperatureEvent()[simple]:" + e18.getMessage());
                    e18.printStackTrace();
                }
            }
        }
        if (arg0.sensorType == 10 && this.linearAccelerationSensorEnabled) {
            if (this.onLinearAccelerationSensorEventMethod != null) {
                try {
                    this.onLinearAccelerationSensorEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2]), Long.valueOf(arg0.timestamp), Integer.valueOf(arg0.accuracy)});
                    return;
                } catch (Exception e19) {
                    PApplet.println("Error onLinearAccelerationEvent():" + e19.getMessage());
                    e19.printStackTrace();
                }
            }
            if (this.onLinearAccelerationSensorEventMethodSimple != null) {
                try {
                    this.onLinearAccelerationSensorEventMethodSimple.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2])});
                    return;
                } catch (Exception e20) {
                    PApplet.println("Error onLinearAccelerationEvent()[simple]:" + e20.getMessage());
                    e20.printStackTrace();
                }
            }
        }
        if (arg0.sensorType == 11 && this.rotationVectorSensorEnabled) {
            if (this.onRotationVectorSensorEventMethod != null) {
                try {
                    this.onRotationVectorSensorEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2]), Long.valueOf(arg0.timestamp), Integer.valueOf(arg0.accuracy)});
                    return;
                } catch (Exception e21) {
                    PApplet.println("Error onRotationVectorEvent():" + e21.getMessage());
                    e21.printStackTrace();
                }
            }
            if (this.onRotationVectorSensorEventMethodSimple != null) {
                try {
                    this.onRotationVectorSensorEventMethodSimple.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2])});
                    return;
                } catch (Exception e22) {
                    PApplet.println("Error onRotationVectorEvent()[simple]:" + e22.getMessage());
                    e22.printStackTrace();
                }
            }
        }
        if (arg0.sensorType == 13 && this.ambientTemperatureSensorEnabled && this.onAmbientTemperatureEventMethod != null) {
            try {
                this.onAmbientTemperatureEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0])});
                return;
            } catch (Exception e23) {
                PApplet.println("Error onAmbientTemperatureEvent():" + e23.getMessage());
                e23.printStackTrace();
            }
        }
        if (arg0.sensorType == 12 && this.relativeHumiditySensorEnabled && this.onRelativeHumidityEventMethod != null) {
            try {
                this.onRelativeHumidityEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0])});
                return;
            } catch (Exception e24) {
                PApplet.println("Error onRelativeHumidityEventMethod():" + e24.getMessage());
                e24.printStackTrace();
            }
        }
        if (arg0.sensorType == 18 && this.stepDetectorSensorEnabled && this.onStepDetectorEventMethod != null) {
            try {
                this.onStepDetectorEventMethod.invoke(this.callbackdelegate, new Object[0]);
                return;
            } catch (Exception e25) {
                PApplet.println("Error onStepDetectorEventMethod():" + e25.getMessage());
                e25.printStackTrace();
            }
        }
        if (arg0.sensorType == 19 && this.stepCounterSensorEnabled && this.onStepCounterEventMethod != null) {
            try {
                this.onStepCounterEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0])});
                return;
            } catch (Exception e26) {
                PApplet.println("Error onStepCounterEventMethod():" + e26.getMessage());
                e26.printStackTrace();
            }
        }
        if (arg0.sensorType == 17 && this.significantMotionSensorEnabled && this.onSignificantMotionEventMethod != null) {
            try {
                PApplet.println("significant motion data: ");
                PApplet.println((Object) arg0);
                this.onSignificantMotionEventMethod.invoke(this.callbackdelegate, new Object[0]);
                return;
            } catch (Exception e27) {
                PApplet.println("Error onSignificantMotionEventMethod():" + e27.getMessage());
                e27.printStackTrace();
            }
        }
        if (arg0.sensorType == 20 && this.geomagneticRotationVectorSensorEnabled && this.onGeomagneticRotationVectorEventMethod != null) {
            try {
                this.onGeomagneticRotationVectorEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2])});
                return;
            } catch (Exception e28) {
                PApplet.println("Error onGeomagneticRotationVectorEventMethod():" + e28.getMessage());
                e28.printStackTrace();
            }
        }
        if (arg0.sensorType == 15 && this.gameRotationSensorEnabled && this.onGameRotationEventMethod != null) {
            try {
                this.onGameRotationEventMethod.invoke(this.callbackdelegate, new Object[]{Float.valueOf(arg0.values[0]), Float.valueOf(arg0.values[1]), Float.valueOf(arg0.values[2])});
            } catch (Exception e29) {
                PApplet.println("Error onGameRotationEventMethod():" + e29.getMessage());
                e29.printStackTrace();
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private boolean isSensorSupported(int type) {
        for (Sensor s : this.sensorManager.getSensorList(-1)) {
            if (type == s.getType()) {
                return true;
            }
        }
        return false;
    }

    private void findParentIntentions() {
        findObjectIntentions(this.parent);
    }

    private void findObjectIntentions(Object o) {
        this.callbackdelegate = o;
        try {
            this.onSensorEventMethod = o.getClass().getMethod("onSensorEvent", new Class[]{SensorEvent.class});
        } catch (NoSuchMethodException e) {
        }
        try {
            this.onAccelerometerEventMethod = o.getClass().getMethod("onAccelerometerEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE});
            this.accelerometerSensorEnabled = true;
            PApplet.println("Found onAccelerometerEvent\tMethod...in " + o.getClass());
        } catch (NoSuchMethodException e2) {
        }
        try {
            this.onAccelerometerEventMethodSimple = o.getClass().getMethod("onAccelerometerEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE});
            this.accelerometerSensorEnabled = true;
            PApplet.println("Found onAccelerometerEventMethod(simple)...");
        } catch (NoSuchMethodException e3) {
        }
        try {
            this.onOrientationSensorEventMethod = o.getClass().getMethod("onOrientationEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE});
            this.orientationSensorEnabled = true;
            PApplet.println("Found onOrientationEventMethod...");
        } catch (NoSuchMethodException e4) {
        }
        try {
            this.onOrientationSensorEventMethodSimple = o.getClass().getMethod("onOrientationEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE});
            this.orientationSensorEnabled = true;
            PApplet.println("Found onOrientationEventMethod(simple)...");
        } catch (NoSuchMethodException e5) {
        }
        try {
            this.onMagneticFieldSensorEventMethod = o.getClass().getMethod("onMagneticFieldEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE});
            this.magneticFieldSensorEnabled = true;
            PApplet.println("Found onMagneticFieldEventMethod...");
        } catch (NoSuchMethodException e6) {
        }
        try {
            this.onMagneticFieldSensorEventMethodSimple = o.getClass().getMethod("onMagneticFieldEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE});
            this.magneticFieldSensorEnabled = true;
            PApplet.println("Found onMagneticFieldEventMethod(simple)...");
        } catch (NoSuchMethodException e7) {
        }
        try {
            this.onGyroscopeSensorEventMethod = o.getClass().getMethod("onGyroscopeEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE});
            this.gyroscopeSensorEnabled = true;
            PApplet.println("Found onGyroscopeEventMethod...");
        } catch (NoSuchMethodException e8) {
        }
        try {
            this.onGyroscopeSensorEventMethodSimple = o.getClass().getMethod("onGyroscopeEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE});
            this.gyroscopeSensorEnabled = true;
            PApplet.println("Found onGyroscopeEventMethod(simple)...");
        } catch (NoSuchMethodException e9) {
        }
        try {
            this.onGravitySensorEventMethod = o.getClass().getMethod("onGravityEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE});
            this.gravitySensorEnabled = true;
            PApplet.println("Found onGravityEvenMethod...");
        } catch (NoSuchMethodException e10) {
        }
        try {
            this.onGravitySensorEventMethodSimple = o.getClass().getMethod("onGravityEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE});
            this.gravitySensorEnabled = true;
            PApplet.println("Found onGravityEvenMethod(simple)...");
        } catch (NoSuchMethodException e11) {
        }
        try {
            this.onProximitySensorEventMethod = o.getClass().getMethod("onProximityEvent", new Class[]{Float.TYPE, Long.TYPE, Integer.TYPE});
            this.proximitySensorEnabled = true;
            PApplet.println("Found onLightEventMethod...");
        } catch (NoSuchMethodException e12) {
        }
        try {
            this.onProximitySensorEventMethodSimple = o.getClass().getMethod("onProximityEvent", new Class[]{Float.TYPE});
            this.proximitySensorEnabled = true;
            PApplet.println("Found onProximityEventMethod(simple)...");
        } catch (NoSuchMethodException e13) {
        }
        try {
            this.onLightSensorEventMethod = o.getClass().getMethod("onLightEvent", new Class[]{Float.TYPE, Long.TYPE, Integer.TYPE});
            this.lightSensorEnabled = true;
            PApplet.println("Found onLightEventMethod...");
        } catch (NoSuchMethodException e14) {
        }
        try {
            this.onLightSensorEventMethodSimple = o.getClass().getMethod("onLightEvent", new Class[]{Float.TYPE});
            this.lightSensorEnabled = true;
            PApplet.println("Found onLightEventMethod(simple)...");
        } catch (NoSuchMethodException e15) {
        }
        try {
            this.onPressureSensorEventMethod = o.getClass().getMethod("onPressureEvent", new Class[]{Float.TYPE, Long.TYPE, Integer.TYPE});
            this.pressureSensorEnabled = true;
            PApplet.println("Found onPressureEventMethod...");
        } catch (NoSuchMethodException e16) {
        }
        try {
            this.onPressureSensorEventMethodSimple = o.getClass().getMethod("onPressureEvent", new Class[]{Float.TYPE});
            this.pressureSensorEnabled = true;
            PApplet.println("Found onPressureEventMethod(simple)...");
        } catch (NoSuchMethodException e17) {
        }
        try {
            this.onTemperatureSensorEventMethod = o.getClass().getMethod("onTemperatureEvent", new Class[]{Float.TYPE, Long.TYPE, Integer.TYPE});
            this.temperatureSensorEnabled = true;
            PApplet.println("Found onTemperatureEventMethod...");
        } catch (NoSuchMethodException e18) {
        }
        try {
            this.onTemperatureSensorEventMethodSimple = o.getClass().getMethod("onTemperatureEvent", new Class[]{Float.TYPE});
            this.temperatureSensorEnabled = true;
            PApplet.println("Found onTemperatureEventMethod(simple)...");
        } catch (NoSuchMethodException e19) {
        }
        try {
            this.onLinearAccelerationSensorEventMethod = o.getClass().getMethod("onLinearAccelerationEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE});
            this.linearAccelerationSensorEnabled = true;
            PApplet.println("Found onLinearAccelerationEventMethod...");
        } catch (NoSuchMethodException e20) {
        }
        try {
            this.onLinearAccelerationSensorEventMethodSimple = o.getClass().getMethod("onLinearAccelerationEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE});
            this.linearAccelerationSensorEnabled = true;
            PApplet.println("Found onLinearAccelerationEventMethod(simple)...");
        } catch (NoSuchMethodException e21) {
        }
        try {
            this.onRotationVectorSensorEventMethod = o.getClass().getMethod("onRotationVectorEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE, Long.TYPE, Integer.TYPE});
            this.rotationVectorSensorEnabled = true;
            PApplet.println("Found onRotationVectorEvenMethod...");
        } catch (NoSuchMethodException e22) {
        }
        try {
            this.onRotationVectorSensorEventMethodSimple = o.getClass().getMethod("onRotationVectorEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE});
            this.rotationVectorSensorEnabled = true;
            PApplet.println("Found onRotationVectorEventMethod(simple)...");
        } catch (NoSuchMethodException e23) {
        }
        try {
            this.onAmbientTemperatureEventMethod = o.getClass().getMethod("onAmibentTemperatureEvent", new Class[]{Float.TYPE});
            this.ambientTemperatureSensorEnabled = true;
            PApplet.println("Found onAmbientTemperatureEvent callback...");
        } catch (NoSuchMethodException e24) {
        }
        try {
            this.onRelativeHumidityEventMethod = o.getClass().getMethod("onRelativeHumidityEvent", new Class[]{Float.TYPE});
            this.relativeHumiditySensorEnabled = true;
            PApplet.println("Found onRelativeHumidityEventMethod...");
        } catch (NoSuchMethodException e25) {
        }
        try {
            this.onStepDetectorEventMethod = o.getClass().getMethod("onStepDetectorEvent", new Class[0]);
            this.stepDetectorSensorEnabled = true;
            PApplet.println("Found onStepDetectorEvent...");
        } catch (NoSuchMethodException e26) {
        }
        try {
            this.onStepCounterEventMethod = o.getClass().getMethod("onStepCounterEvent", new Class[]{Float.TYPE});
            this.stepCounterSensorEnabled = true;
            PApplet.println("Found onStepCounterEvent...");
        } catch (NoSuchMethodException e27) {
        }
        try {
            this.onSignificantMotionEventMethod = o.getClass().getMethod("onSignificantMotionEvent", new Class[0]);
            this.significantMotionSensorEnabled = true;
            PApplet.println("Found onSignificantMotionEvent...");
        } catch (NoSuchMethodException e28) {
        }
        try {
            this.onHeartRateEventMethod = o.getClass().getMethod("onHeartRateEvent", new Class[]{Float.TYPE});
            this.heartRateSensorEnabled = true;
            PApplet.println("Found onHeartRateEvent...");
        } catch (NoSuchMethodException e29) {
        }
        try {
            this.onGeomagneticRotationVectorEventMethod = o.getClass().getMethod("onGeomagneticRotationVectorEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE});
            this.geomagneticRotationVectorSensorEnabled = true;
            PApplet.println("Found onGeomagneticRotationVectorEvent...");
        } catch (NoSuchMethodException e30) {
        }
        try {
            this.onGameRotationEventMethod = o.getClass().getMethod("onGameRotationEvent", new Class[]{Float.TYPE, Float.TYPE, Float.TYPE});
            this.gameRotationSensorEnabled = true;
            PApplet.println("Found onGameRotationEvent...");
        } catch (NoSuchMethodException e31) {
        }
    }

    public void startService() {
        start();
    }

    public int getStatus() {
        return 0;
    }

    public void stopService() {
        stop();
    }

    public String getServiceDescription() {
        return SERVICE_DESCRIPTION;
    }

    public static void getRotationMatrixFromVector(float[] R, float[] rotationVector) {
        SensorManager.getRotationMatrixFromVector(R, rotationVector);
    }

    public void getQuaternionFromVector(float[] Q, float[] rv) {
        SensorManager.getQuaternionFromVector(Q, rv);
    }

    public float[] getOrientation() {
        if (!isStarted() || !this.accelerometerSensorEnabled || !this.magneticFieldSensorEnabled) {
            PApplet.println("Cannot compute orientation until sensor service is started and accelerometer and magnetometer must also be enabled.");
            return (float[]) this.zeroes.clone();
        } else if (this.accelerometerData == null || this.magnetometerData == null) {
            return (float[]) this.zeroes.clone();
        } else {
            if (this.rotationMat == null) {
                this.rotationMat = new float[16];
            }
            if (this.inclinationMat == null) {
                this.inclinationMat = new float[9];
            }
            if (this.orientationVec == null) {
                this.orientationVec = new float[3];
            }
            if (!SensorManager.getRotationMatrix(this.rotationMat, this.inclinationMat, this.accelerometerData, this.magnetometerData)) {
                return (float[]) this.zeroes.clone();
            }
            SensorManager.getOrientation(this.rotationMat, this.orientationVec);
            return (float[]) this.orientationVec.clone();
        }
    }

    public float[] getOrientation(float[] v) {
        if (v == null) {
            v = new float[3];
        }
        if (!isStarted() || !this.accelerometerSensorEnabled || !this.magneticFieldSensorEnabled) {
            PApplet.println("Cannot compute orientation until sensor service is started and accelerometer and magnetometer must also be enabled.");
            PApplet.arrayCopy(this.zeroes, v);
        }
        if (this.accelerometerData == null || this.magnetometerData == null) {
            PApplet.arrayCopy(this.zeroes, v);
        } else {
            if (this.rotationMat == null) {
                this.rotationMat = new float[16];
            }
            if (this.inclinationMat == null) {
                this.inclinationMat = new float[9];
            }
            if (SensorManager.getRotationMatrix(this.rotationMat, this.inclinationMat, this.accelerometerData, this.magnetometerData)) {
                SensorManager.getOrientation(this.rotationMat, v);
            } else {
                PApplet.arrayCopy(this.zeroes, v);
            }
        }
        return v;
    }

    public void register(Object delegate) {
        PApplet.println("KetaiSensor delegating Events to class: " + delegate.getClass());
        findObjectIntentions(delegate);
    }
}
