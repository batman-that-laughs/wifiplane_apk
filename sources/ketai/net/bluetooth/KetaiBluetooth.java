package ketai.net.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import processing.core.PApplet;

public class KetaiBluetooth {
    static final int BLUETOOTH_ENABLE_REQUEST = 1;
    protected UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    protected UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    protected String NAME_INSECURE = "BluetoothInsecure";
    protected String NAME_SECURE = "BluetoothSecure";
    protected BluetoothAdapter bluetoothAdapter;
    private KBluetoothListener btListener;
    private HashMap<String, KBluetoothConnection> currentConnections;
    /* access modifiers changed from: private */
    public HashMap<String, String> discoveredDevices;
    private boolean isStarted = false;
    /* access modifiers changed from: private */
    public ConnectThread mConnectThread;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device;
            if ("android.bluetooth.device.action.FOUND".equals(intent.getAction()) && (device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")) != null) {
                KetaiBluetooth.this.discoveredDevices.put(device.getName(), device.getAddress());
                PApplet.println("New Device Discovered: " + device.getName());
            }
        }
    };
    protected Method onBluetoothDataEventMethod;
    private HashMap<String, String> pairedDevices;
    protected PApplet parent;

    public KetaiBluetooth(PApplet _parent) {
        this.parent = _parent;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.bluetoothAdapter == null) {
            PApplet.println("No Bluetooth Support.");
            return;
        }
        if (!this.bluetoothAdapter.isEnabled()) {
            this.parent.getActivity().startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        }
        this.pairedDevices = new HashMap<>();
        this.discoveredDevices = new HashMap<>();
        this.currentConnections = new HashMap<>();
        findParentIntention();
        this.parent.registerMethod("dispose", this);
    }

    public void setSLIPMode(boolean _flag) {
    }

    public boolean isStarted() {
        return this.isStarted;
    }

    public BluetoothAdapter getBluetoothAdapater() {
        return this.bluetoothAdapter;
    }

    public boolean isDiscovering() {
        return this.bluetoothAdapter.isDiscovering();
    }

    public String toString() {
        String info = "KBluetoothManager dump:\n--------------------\nPairedDevices:\n";
        for (String key : this.pairedDevices.keySet()) {
            info = String.valueOf(info) + key + "->" + this.pairedDevices.get(key) + "\n";
        }
        String info2 = String.valueOf(info) + "\n\nDiscovered Devices\n";
        for (String key2 : this.discoveredDevices.keySet()) {
            info2 = String.valueOf(info2) + key2 + "->" + this.discoveredDevices.get(key2) + "\n";
        }
        String info3 = String.valueOf(info2) + "\n\nCurrent Connections\n";
        for (String key3 : this.currentConnections.keySet()) {
            info3 = String.valueOf(info3) + key3 + "->" + this.currentConnections.get(key3) + "\n";
        }
        return String.valueOf(info3) + "\n-------------------------------\n";
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    PApplet.println("BT made available.");
                    return;
                } else {
                    PApplet.println("BT was not made available.");
                    return;
                }
            default:
                return;
        }
    }

    public boolean isDiscoverable() {
        return this.bluetoothAdapter.getScanMode() == 23;
    }

    public boolean start() {
        if (this.btListener != null) {
            stop();
            this.isStarted = false;
        }
        this.btListener = new KBluetoothListener(this, true);
        this.btListener.start();
        this.isStarted = true;
        findParentIntention();
        this.parent.getActivity().registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.device.action.FOUND"));
        this.parent.getActivity().registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
        return this.isStarted;
    }

    public String getAddress() {
        if (this.bluetoothAdapter != null) {
            return this.bluetoothAdapter.getAddress();
        }
        return "";
    }

    public ArrayList<String> getDiscoveredDeviceNames() {
        ArrayList<String> devices = new ArrayList<>();
        for (String key : this.discoveredDevices.keySet()) {
            if (key != null) {
                devices.add(key);
            }
        }
        return devices;
    }

    public ArrayList<String> getPairedDeviceNames() {
        ArrayList<String> devices = new ArrayList<>();
        this.pairedDevices.clear();
        Set<BluetoothDevice> bondedDevices = this.bluetoothAdapter.getBondedDevices();
        if (bondedDevices.size() > 0) {
            for (BluetoothDevice device : bondedDevices) {
                this.pairedDevices.put(device.getName(), device.getAddress());
                devices.add(device.getName());
            }
        }
        return devices;
    }

    public ArrayList<String> getConnectedDeviceNames() {
        ArrayList<String> devices = new ArrayList<>();
        Set<String> connectedDevices = this.currentConnections.keySet();
        if (connectedDevices.size() > 0) {
            for (String device : connectedDevices) {
                devices.add(this.currentConnections.get(device).getDeviceName());
            }
        }
        return devices;
    }

    public ArrayList<String> getConnectedDeviceLabel() {
        ArrayList<String> devices = new ArrayList<>();
        Set<String> connectedDevices = this.currentConnections.keySet();
        if (connectedDevices.size() > 0) {
            for (String device : connectedDevices) {
                devices.add(String.valueOf(this.currentConnections.get(device).getDeviceName()) + "(" + device + ")");
            }
        }
        return devices;
    }

    public ArrayList<String> getConnectedDeviceAddresses() {
        ArrayList<String> devices = new ArrayList<>();
        Set<String> connectedDevices = this.currentConnections.keySet();
        if (connectedDevices.size() > 0) {
            for (String device : connectedDevices) {
                devices.add(this.currentConnections.get(device).getAddress());
            }
        }
        return devices;
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x0042  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean disconnectDevice(java.lang.String r7) {
        /*
            r6 = this;
            java.util.HashMap<java.lang.String, ketai.net.bluetooth.KBluetoothConnection> r3 = r6.currentConnections
            java.util.Set r1 = r3.keySet()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Disconnecting device: "
            r3.<init>(r4)
            java.lang.StringBuilder r3 = r3.append(r7)
            java.lang.String r3 = r3.toString()
            processing.core.PApplet.println((java.lang.String) r3)
            int r3 = r1.size()
            if (r3 <= 0) goto L_0x0040
            java.util.Iterator r3 = r1.iterator()
        L_0x0022:
            boolean r4 = r3.hasNext()
            if (r4 != 0) goto L_0x0042
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Did not find device ("
            r3.<init>(r4)
            java.lang.StringBuilder r3 = r3.append(r7)
            java.lang.String r4 = ")in connected list."
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            processing.core.PApplet.println((java.lang.String) r3)
        L_0x0040:
            r3 = 0
        L_0x0041:
            return r3
        L_0x0042:
            java.lang.Object r2 = r3.next()
            java.lang.String r2 = (java.lang.String) r2
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "Comparing "
            r4.<init>(r5)
            java.lang.StringBuilder r4 = r4.append(r2)
            java.lang.String r5 = " to target "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r7)
            java.lang.String r4 = r4.toString()
            processing.core.PApplet.println((java.lang.String) r4)
            java.util.HashMap<java.lang.String, ketai.net.bluetooth.KBluetoothConnection> r4 = r6.currentConnections
            java.lang.Object r0 = r4.get(r2)
            ketai.net.bluetooth.KBluetoothConnection r0 = (ketai.net.bluetooth.KBluetoothConnection) r0
            boolean r4 = r7.equals(r2)
            if (r4 != 0) goto L_0x007c
            java.lang.String r4 = r0.getAddress()
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L_0x0022
        L_0x007c:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Disconnecting device ("
            r3.<init>(r4)
            java.lang.StringBuilder r3 = r3.append(r7)
            java.lang.String r4 = ")."
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            processing.core.PApplet.println((java.lang.String) r3)
            r0.disconnect()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Removing current connection for "
            r3.<init>(r4)
            java.lang.String r4 = r0.getDeviceName()
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            processing.core.PApplet.println((java.lang.String) r3)
            java.util.HashMap<java.lang.String, ketai.net.bluetooth.KBluetoothConnection> r3 = r6.currentConnections
            r3.remove(r0)
            r1.remove(r2)
            r3 = 1
            goto L_0x0041
        */
        throw new UnsupportedOperationException("Method not decompiled: ketai.net.bluetooth.KetaiBluetooth.disconnectDevice(java.lang.String):boolean");
    }

    public boolean connectToDeviceByName(String _name) {
        String address = "";
        if (this.pairedDevices.containsKey(_name)) {
            address = this.pairedDevices.get(_name);
        } else if (this.discoveredDevices.containsKey(_name)) {
            address = this.discoveredDevices.get(_name);
        }
        if (address.length() <= 0 || !this.currentConnections.containsKey(address)) {
            return connectDevice(address);
        }
        return true;
    }

    public boolean connectDevice(String _hwAddress) {
        if (!BluetoothAdapter.checkBluetoothAddress(_hwAddress)) {
            PApplet.println("Bad bluetooth hardware address! : " + _hwAddress);
        } else {
            BluetoothDevice device = this.bluetoothAdapter.getRemoteDevice(_hwAddress);
            if (this.mConnectThread == null) {
                this.mConnectThread = new ConnectThread(device, true);
                this.mConnectThread.start();
            } else if (this.mConnectThread.mmDevice.getAddress() != _hwAddress) {
                this.mConnectThread.cancel();
                this.mConnectThread = new ConnectThread(device, true);
                this.mConnectThread.start();
            }
        }
        return false;
    }

    public boolean connectDeviceUsingSLIP(String _hwAddress) {
        return false;
    }

    public boolean connectDevice(BluetoothSocket _socket) {
        KBluetoothConnection tmp = new KBluetoothConnection(this, _socket);
        if (tmp.isConnected()) {
            tmp.start();
            if (tmp != null && !this.currentConnections.containsKey(_socket.getRemoteDevice().getAddress())) {
                this.currentConnections.put(_socket.getRemoteDevice().getAddress(), tmp);
            }
            if (this.mConnectThread != null) {
                this.mConnectThread.cancel();
                this.mConnectThread = null;
            }
            return true;
        }
        PApplet.println("Error trying to connect to " + _socket.getRemoteDevice().getName() + " (" + _socket.getRemoteDevice().getAddress() + ")");
        this.mConnectThread = null;
        return false;
    }

    public void discoverDevices() {
        this.discoveredDevices.clear();
        this.bluetoothAdapter.cancelDiscovery();
        if (this.bluetoothAdapter.startDiscovery()) {
            PApplet.println("Starting bt discovery.");
        } else {
            PApplet.println("BT discovery failed to start.");
        }
    }

    public String lookupAddressByName(String _name) {
        if (this.pairedDevices.containsKey(_name)) {
            return this.pairedDevices.get(_name);
        }
        if (this.discoveredDevices.containsKey(_name)) {
            return this.discoveredDevices.get(_name);
        }
        return "";
    }

    public void writeToDeviceName(String _name, byte[] data) {
        String address = lookupAddressByName(_name);
        if (address.length() > 0) {
            write(address, data);
        } else {
            PApplet.println("Error writing to " + _name + ".  HW Address was not found.");
        }
    }

    public void write(String _deviceAddress, byte[] data) {
        this.bluetoothAdapter.cancelDiscovery();
        if ((this.currentConnections.containsKey(_deviceAddress) || connectDevice(_deviceAddress)) && this.currentConnections.containsKey(_deviceAddress)) {
            this.currentConnections.get(_deviceAddress).write(data);
        }
    }

    public void broadcast(byte[] data) {
        for (Map.Entry<String, KBluetoothConnection> device : this.currentConnections.entrySet()) {
            device.getValue().write(data);
        }
    }

    /* access modifiers changed from: protected */
    public void removeConnection(KBluetoothConnection c) {
        PApplet.println("KBTM removing connection for " + c.getAddress());
        if (this.currentConnections.containsKey(c.getAddress())) {
            c.cancel();
            this.currentConnections.remove(c.getAddress());
        }
    }

    public void makeDiscoverable() {
        if (this.bluetoothAdapter.getScanMode() != 23) {
            Intent discoverableIntent = new Intent("android.bluetooth.adapter.action.REQUEST_DISCOVERABLE");
            discoverableIntent.putExtra("android.bluetooth.adapter.extra.DISCOVERABLE_DURATION", 300);
            this.parent.getActivity().startActivity(discoverableIntent);
        }
    }

    private void findParentIntention() {
        try {
            this.onBluetoothDataEventMethod = this.parent.getClass().getMethod("onBluetoothDataEvent", new Class[]{String.class, byte[].class});
            PApplet.println("Found onBluetoothDataEvent method.");
        } catch (NoSuchMethodException e) {
            PApplet.println("Did not find onBluetoothDataEvent callback method.");
        }
    }

    public void stop() {
        if (this.btListener != null) {
            this.btListener.cancel();
        }
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
        }
        for (String key : this.currentConnections.keySet()) {
            this.currentConnections.get(key).cancel();
        }
        this.currentConnections.clear();
        this.btListener = null;
        this.mConnectThread = null;
    }

    public void dispose() {
        stop();
    }

    private class ConnectThread extends Thread {
        private String mSocketType;
        protected final BluetoothDevice mmDevice;
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            this.mmDevice = device;
            BluetoothSocket tmp = null;
            this.mSocketType = secure ? "Secure" : "Insecure";
            if (secure) {
                try {
                    tmp = device.createRfcommSocketToServiceRecord(KetaiBluetooth.this.MY_UUID_SECURE);
                } catch (IOException e) {
                    PApplet.println("Socket Type: " + this.mSocketType + "create() failed" + e);
                }
            } else {
                tmp = device.createInsecureRfcommSocketToServiceRecord(KetaiBluetooth.this.MY_UUID_INSECURE);
            }
            this.mmSocket = tmp;
        }

        public void run() {
            while (this.mmSocket == null) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            PApplet.println("BEGIN mConnectThread SocketType:" + this.mSocketType + ":" + this.mmSocket.getRemoteDevice().getName());
            KetaiBluetooth.this.bluetoothAdapter.cancelDiscovery();
            try {
                if (this.mmSocket != null) {
                    this.mmSocket.connect();
                }
                PApplet.println("KBTConnect thread connected!");
                KetaiBluetooth.this.connectDevice(this.mmSocket);
            } catch (IOException e) {
                try {
                    this.mmSocket.close();
                } catch (IOException e2) {
                    PApplet.println("unable to close() " + this.mSocketType + " socket during connection failure" + e2);
                }
                KetaiBluetooth.this.mConnectThread = null;
            }
        }

        public void cancel() {
        }
    }
}
