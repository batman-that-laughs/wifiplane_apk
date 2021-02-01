package ketai.sensors;

import android.hardware.SensorEvent;
import processing.core.PApplet;

public class SensorData {
    public int accuracy;
    public int sensorType;
    public long timestamp;
    public float[] values;

    SensorData(SensorEvent event) {
        this.sensorType = event.sensor.getType();
        this.accuracy = event.accuracy;
        this.timestamp = event.timestamp;
        this.values = new float[event.values.length];
        PApplet.arrayCopy(event.values, this.values);
    }
}
