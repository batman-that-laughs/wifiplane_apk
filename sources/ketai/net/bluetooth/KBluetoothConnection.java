package ketai.net.bluetooth;

import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import processing.core.PApplet;

public class KBluetoothConnection extends Thread {
    private String address = "";
    private KetaiBluetooth btm;
    private boolean isConnected = false;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private BluetoothSocket mmSocket;

    public KBluetoothConnection(KetaiBluetooth _btm, BluetoothSocket socket) {
        PApplet.println("create Connection thread to " + socket.getRemoteDevice().getName());
        this.btm = _btm;
        this.mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        this.address = socket.getRemoteDevice().getAddress();
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            PApplet.println("temp sockets not created: " + e.getMessage());
        }
        this.mmInStream = tmpIn;
        this.mmOutStream = tmpOut;
        this.isConnected = true;
    }

    public String getAddress() {
        return this.address;
    }

    public String getDeviceName() {
        if (this.mmSocket == null) {
            return "";
        }
        return this.mmSocket.getRemoteDevice().getName();
    }

    public void disconnect() {
        PApplet.println("Disconnecting device : " + this.address);
        if (this.mmInStream != null) {
            try {
                PApplet.println("Closing input stream for " + this.address);
                this.mmInStream.close();
            } catch (IOException e) {
                PApplet.println("Error closing input stream for " + this.address + " : " + e.getMessage());
            }
            this.mmInStream = null;
        }
        if (this.mmOutStream != null) {
            try {
                PApplet.println("Closing output stream for " + this.address);
                this.mmOutStream.close();
            } catch (IOException e2) {
                PApplet.println("Error closing output stream for " + this.address + " : " + e2.getMessage());
            }
            this.mmOutStream = null;
        }
        if (this.mmSocket != null) {
            try {
                PApplet.println("Closing socket for " + this.address);
                this.mmSocket.close();
            } catch (IOException e3) {
                PApplet.println("Error closing Socket for " + this.address + " : " + e3.getMessage());
            }
            this.mmSocket = null;
        }
    }

    public void run() {
        PApplet.println("BEGIN mConnectedThread to " + this.address);
        byte[] buffer = new byte[1024];
        while (true) {
            try {
                byte[] data = Arrays.copyOfRange(buffer, 0, this.mmInStream.read(buffer));
                if (this.btm.onBluetoothDataEventMethod != null) {
                    try {
                        this.btm.onBluetoothDataEventMethod.invoke(this.btm.parent, new Object[]{this.address, data});
                    } catch (IllegalAccessException e) {
                        PApplet.println("Error in reading connection data.:" + e.getMessage());
                    } catch (InvocationTargetException e2) {
                        PApplet.println("Error in reading connection data.:" + e2.getMessage());
                    }
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e3) {
                }
            } catch (IOException e4) {
                this.btm.removeConnection(this);
                PApplet.println(String.valueOf(getAddress()) + " disconnected" + e4.getMessage());
                this.isConnected = false;
                return;
            }
        }
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public void write(byte[] buffer) {
        try {
            this.mmOutStream.write(buffer);
        } catch (IOException e) {
            PApplet.println(String.valueOf(getAddress()) + ": Exception during write" + e.getMessage());
            this.btm.removeConnection(this);
        }
    }

    public void cancel() {
        try {
            this.mmSocket.close();
        } catch (IOException e) {
            PApplet.println("close() of connect socket failed" + e.getMessage());
        }
    }
}
