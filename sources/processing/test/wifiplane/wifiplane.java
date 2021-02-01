package processing.test.wifiplane;

import android.support.v4.media.TransportMediator;
import hypermedia.net.UDP;
import ketai.sensors.KetaiSensor;
import ketai.ui.KetaiVibrate;
import processing.core.PApplet;

public class wifiplane extends PApplet {
    int DC_UPDATE = 1;
    byte P_ID = 1;
    float accelerometerX;
    float accelerometerY;
    float accelerometerZ;
    int app_start = 1;
    int dc_count = 0;
    float diff_power = 2.2f;
    int exprt_flag = 0;
    int gas = 0;
    int l_speed = 0;
    int localPort = 2390;
    int lock = 0;
    int offsetl = 0;
    int offsetr = 0;
    int r_speed = 0;
    String remotIp = "192.168.43.255";
    int remotPort = 6000;
    int rssi = 0;
    int rst_count = 0;
    KetaiSensor sensor;
    UDP udp;
    int vcc = 0;
    int vib_count = 0;
    KetaiVibrate vibe;

    public void setup() {
        orientation(1);
        this.udp = new UDP(this, this.localPort);
        this.udp.listen(true);
        this.sensor = new KetaiSensor(this);
        this.vibe = new KetaiVibrate(this);
        this.sensor.start();
    }

    public void draw() {
        background(125.0f, 255.0f, 200.0f);
        fill(255);
        stroke(163);
        rect(0.0f, 0.0f, (float) (this.width / 4), (float) (this.height / 4));
        rect((float) ((this.width * 3) / 4), 0.0f, (float) (this.width / 4), (float) (this.height / 4));
        rect(0.0f, (float) (this.height / 4), (float) (this.width / 4), (float) (this.height / 4));
        rect((float) ((this.width * 3) / 4), (float) (this.height / 4), (float) (this.width / 4), (float) (this.height / 4));
        rect(0.0f, (float) ((this.height * 7) / 8), (float) this.width, (float) (this.height / 8));
        fill(color(255, 100, 60));
        rect((float) (this.width / 4), 0.0f, (float) (this.width / 2), (float) ((this.height * 7) / 8));
        fill(color(100, 150, 255));
        rect((float) (this.width / 4), 0.0f, (float) (this.width / 2), (float) (((this.height * 7) / 8) - (((this.gas * 7) * this.height) / 1016)));
        textSize((float) (this.height / 12));
        textAlign(3, 3);
        fill(color(50, 100, 255));
        text("+", (float) (this.width / 8), (float) ((this.height / 8) - 10));
        text("-", (float) (this.width / 8), (float) (((this.height * 3) / 8) - 10));
        text("+", (float) (((this.width * 3) / 4) + (this.width / 8)), (float) ((this.height / 8) - 10));
        text("-", (float) (((this.width * 3) / 4) + (this.width / 8)), (float) (((this.height * 3) / 8) - 10));
        fill(0);
        text((this.gas * 100) / TransportMediator.KEYCODE_MEDIA_PAUSE, (float) (this.width / 2), (float) (this.height / 2));
        text(this.offsetl, (float) (this.width / 8), (float) ((this.height / 4) - 10));
        text(this.offsetr, (float) (((this.width * 3) / 4) + (this.width / 8)), (float) ((this.height / 4) - 10));
        if (this.exprt_flag == 0) {
            text("BG", (float) (this.width / 8), (float) ((this.height / 2) + (this.height / 6)));
        } else if (this.exprt_flag == 1) {
            text("EX", (float) (this.width / 8), (float) ((this.height / 2) + (this.height / 6)));
        }
        if (this.lock == 0) {
            text("LOCKED", (float) (this.width / 2), (float) (((this.height * 7) / 8) + (this.height / 16)));
        } else if (this.lock == 1) {
            text("ACTIVATED", (float) (this.width / 2), (float) (((this.height * 7) / 8) + (this.height / 16)));
        }
        textSize((float) (this.height / 14));
        fill(255);
        if (this.rssi == 0) {
            text("-" + Character.toString(8734) + "dBm", (float) (this.width / 2), (float) ((this.height * 3) / 4));
        } else {
            text("-" + this.rssi + "dBm", (float) (this.width / 2), (float) ((this.height * 3) / 4));
        }
        text(String.valueOf(this.vcc / 10) + "." + (this.vcc % 10) + "V", (float) (this.width / 2), (float) (((this.height * 3) / 4) + (this.height / 12)));
        fill(0);
        textSize((float) (this.height / 30));
        textAlign(3, 3);
        text("Instructables", (float) (this.width / 2), (float) (this.height / 20));
        text("WiFi Plane App", (float) (this.width / 2), (float) ((this.height * 2) / 20));
        text("By Ravi Butani", (float) (this.width / 2), (float) ((this.height * 3) / 20));
        textSize((float) (this.height / 12));
        delay(1);
        this.dc_count++;
        if (this.dc_count >= this.DC_UPDATE) {
            this.rst_count++;
            if (this.rst_count >= 200) {
                this.vcc = 0;
                this.rssi = 0;
            }
            this.dc_count = 0;
            if (this.accelerometerX > 1.5f) {
                this.accelerometerX -= 1.5f;
            } else if (this.accelerometerX < -1.5f) {
                this.accelerometerX += 1.5f;
            } else {
                this.accelerometerX = 0.0f;
            }
            this.l_speed = (int) (((float) this.gas) + ((float) this.offsetl) + (this.accelerometerX * this.diff_power));
            this.r_speed = (int) ((((float) this.gas) + ((float) this.offsetr)) - (this.accelerometerX * this.diff_power));
            if (this.l_speed >= 127) {
                this.l_speed = TransportMediator.KEYCODE_MEDIA_PAUSE;
            } else if (this.l_speed <= 1) {
                this.l_speed = 1;
            }
            if (this.r_speed >= 127) {
                this.r_speed = TransportMediator.KEYCODE_MEDIA_PAUSE;
            } else if (this.r_speed <= 1) {
                this.r_speed = 1;
            }
            byte[] message = {49, 50, 51};
            message[0] = this.P_ID;
            if (this.lock == 1) {
                message[1] = (byte) this.l_speed;
                message[2] = (byte) this.r_speed;
                this.vib_count++;
                if (this.vcc < 35 && this.vib_count < 5) {
                    this.vibe.vibrate(1000);
                }
                if (this.vib_count >= 40) {
                    this.vib_count = 0;
                }
            } else if (this.lock == 0) {
                message[1] = 1;
                message[2] = 1;
            }
            println(message[1]);
            println(message[2]);
            this.udp.send(new String(message), this.remotIp, this.remotPort);
            println("msgsend");
        }
    }

    public void onAccelerometerEvent(float x, float y, float z) {
        this.accelerometerX = x;
        this.accelerometerY = y;
        this.accelerometerZ = z;
    }

    public void mouseDragged() {
        if (this.mouseY < (this.height * 7) / 8 && this.mouseX > this.width / 4 && this.mouseX < (this.width * 3) / 4 && this.lock == 1) {
            this.gas = 127 - ((int) ((((float) this.mouseY) / ((float) ((this.height * 7) / 8))) * 127.0f));
        }
    }

    public void mousePressed() {
        if (this.mouseX < this.width / 4 && this.mouseY < this.height / 4) {
            this.offsetl++;
        } else if (this.mouseX < this.width / 4 && this.mouseY < this.height / 2) {
            this.offsetl--;
        } else if (this.mouseX > (this.width * 3) / 4 && this.mouseY < this.height / 4) {
            this.offsetr++;
        } else if (this.mouseX > (this.width * 3) / 4 && this.mouseY < this.height / 2) {
            this.offsetr--;
        } else if (this.mouseX >= this.width / 4 || this.mouseY >= (this.height * 3) / 4) {
            if (this.mouseY > (this.height * 7) / 8) {
                this.gas = 0;
                if (this.lock == 0) {
                    this.lock = 1;
                } else {
                    this.lock = 0;
                }
            }
        } else if (this.exprt_flag == 0) {
            this.exprt_flag = 1;
            this.diff_power = 3.9f;
        } else {
            this.exprt_flag = 0;
            this.diff_power = 2.2f;
        }
    }

    public void receive(byte[] data, String ip, int port) {
        this.rst_count = 0;
        this.rssi = data[1];
        this.vcc = data[2] + 3;
    }

    public void settings() {
        size(this.displayWidth, this.displayHeight);
    }

    public static void main(String[] passedArgs) {
        String[] appletArgs = {PApplet.ARGS_PRESENT, "--window-color=#666666", "--stop-color=#cccccc", "wifiplane"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
