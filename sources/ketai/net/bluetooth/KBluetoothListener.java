package ketai.net.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import java.io.IOException;
import processing.core.PApplet;

public class KBluetoothListener extends Thread {
    private KetaiBluetooth btManager;
    private boolean go = true;
    private BluetoothAdapter mAdapter;
    private String mSocketType;
    private final BluetoothServerSocket mmServerSocket;

    public KBluetoothListener(KetaiBluetooth btm, boolean secure) {
        BluetoothServerSocket tmp = null;
        this.mSocketType = secure ? "Secure" : "Insecure";
        this.btManager = btm;
        this.mAdapter = this.btManager.getBluetoothAdapater();
        if (secure) {
            try {
                tmp = this.mAdapter.listenUsingRfcommWithServiceRecord(this.btManager.NAME_SECURE, this.btManager.MY_UUID_SECURE);
            } catch (IOException e) {
                PApplet.println("Socket Type: " + this.mSocketType + "listen() failed" + e);
            }
        } else {
            tmp = this.mAdapter.listenUsingInsecureRfcommWithServiceRecord(this.btManager.NAME_INSECURE, this.btManager.MY_UUID_INSECURE);
        }
        this.mmServerSocket = tmp;
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 124 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r4 = this;
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "Socket Type: "
            r2.<init>(r3)
            java.lang.String r3 = r4.mSocketType
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = "BEGIN mAcceptThread"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r4)
            java.lang.String r2 = r2.toString()
            processing.core.PApplet.println((java.lang.String) r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "AcceptThread"
            r2.<init>(r3)
            java.lang.String r3 = r4.mSocketType
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            processing.core.PApplet.println((java.lang.String) r2)
            r1 = 0
            android.bluetooth.BluetoothServerSocket r2 = r4.mmServerSocket
            if (r2 != 0) goto L_0x0066
            java.lang.String r2 = "Failed to get socket for server! bye."
            processing.core.PApplet.println((java.lang.String) r2)
        L_0x003c:
            return
        L_0x003d:
            android.bluetooth.BluetoothServerSocket r2 = r4.mmServerSocket     // Catch:{ IOException -> 0x0082 }
            android.bluetooth.BluetoothSocket r1 = r2.accept()     // Catch:{ IOException -> 0x0082 }
            if (r1 == 0) goto L_0x0066
            monitor-enter(r4)     // Catch:{ IOException -> 0x0082 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x007f }
            java.lang.String r3 = "Incoming connection from: "
            r2.<init>(r3)     // Catch:{ all -> 0x007f }
            android.bluetooth.BluetoothDevice r3 = r1.getRemoteDevice()     // Catch:{ all -> 0x007f }
            java.lang.String r3 = r3.getName()     // Catch:{ all -> 0x007f }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x007f }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x007f }
            processing.core.PApplet.println((java.lang.String) r2)     // Catch:{ all -> 0x007f }
            ketai.net.bluetooth.KetaiBluetooth r2 = r4.btManager     // Catch:{ all -> 0x007f }
            r2.connectDevice((android.bluetooth.BluetoothSocket) r1)     // Catch:{ all -> 0x007f }
            monitor-exit(r4)     // Catch:{ all -> 0x007f }
        L_0x0066:
            boolean r2 = r4.go
            if (r2 != 0) goto L_0x003d
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "END mAcceptThread, socket Type: "
            r2.<init>(r3)
            java.lang.String r3 = r4.mSocketType
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            processing.core.PApplet.println((java.lang.String) r2)
            goto L_0x003c
        L_0x007f:
            r2 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x007f }
            throw r2     // Catch:{ IOException -> 0x0082 }
        L_0x0082:
            r0 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "Socket Type: "
            r2.<init>(r3)
            java.lang.String r3 = r4.mSocketType
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = "accept() failed"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = r0.getMessage()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            processing.core.PApplet.println((java.lang.String) r2)
            goto L_0x0066
        */
        throw new UnsupportedOperationException("Method not decompiled: ketai.net.bluetooth.KBluetoothListener.run():void");
    }

    public void cancel() {
        PApplet.println("Socket Type" + this.mSocketType + "cancel " + this);
        this.go = false;
        try {
            if (this.mmServerSocket != null) {
                this.mmServerSocket.close();
            }
        } catch (IOException e) {
            PApplet.println("Socket Type" + this.mSocketType + "close() of server failed" + e.getMessage());
        }
    }
}
