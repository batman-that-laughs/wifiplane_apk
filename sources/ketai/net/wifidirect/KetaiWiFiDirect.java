package ketai.net.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import processing.core.PApplet;

public class KetaiWiFiDirect extends BroadcastReceiver implements WifiP2pManager.ChannelListener, WifiP2pManager.ConnectionInfoListener, WifiP2pManager.ActionListener, WifiP2pManager.PeerListListener {
    private WifiP2pManager.Channel channel;
    private final IntentFilter intentFilter = new IntentFilter();
    private String ip = "";
    private boolean isWifiP2pEnabled = false;
    private WifiP2pManager manager;
    PApplet parent;
    private List<WifiP2pDevice> peers = new ArrayList();
    private boolean retryChannel = false;

    public KetaiWiFiDirect(PApplet _parent) {
        this.parent = _parent;
        initIntentFilter();
        this.parent.registerMethod("resume", this);
        this.parent.registerMethod("pause", this);
    }

    private void initIntentFilter() {
        this.intentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
        this.intentFilter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
        this.intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
        this.intentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
        this.manager = (WifiP2pManager) this.parent.getActivity().getSystemService("wifip2p");
        this.channel = this.manager.initialize(this.parent.getActivity(), this.parent.getActivity().getMainLooper(), this);
        this.parent.getActivity().registerReceiver(this, this.intentFilter);
    }

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled2) {
        this.isWifiP2pEnabled = isWifiP2pEnabled2;
    }

    public void resume() {
        this.parent.getActivity().registerReceiver(this, this.intentFilter);
    }

    public void pause() {
        this.parent.getActivity().unregisterReceiver(this);
    }

    private void connectToConfig(WifiP2pConfig config) {
        this.manager.connect(this.channel, config, new WifiP2pManager.ActionListener() {
            public void onSuccess() {
            }

            public void onFailure(int reason) {
                PApplet.println("Connect failed. Retry." + reason);
            }
        });
    }

    public void disconnect() {
        this.manager.removeGroup(this.channel, new WifiP2pManager.ActionListener() {
            public void onFailure(int reasonCode) {
                PApplet.println("Disconnect failed. Reason :" + reasonCode);
            }

            public void onSuccess() {
            }
        });
    }

    public void onChannelDisconnected() {
        if (this.manager == null || this.retryChannel) {
            PApplet.println("Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.");
            return;
        }
        PApplet.println("Channel lost. Trying again");
        this.retryChannel = true;
        this.manager.initialize(this.parent.getActivity(), this.parent.getActivity().getMainLooper(), this);
    }

    public void cancelDisconnect() {
        if (this.manager != null) {
            this.manager.cancelConnect(this.channel, new WifiP2pManager.ActionListener() {
                public void onSuccess() {
                    PApplet.println("Aborting connection");
                }

                public void onFailure(int reasonCode) {
                    PApplet.println("Connect abort request failed. Reason Code: " + reasonCode);
                }
            });
        }
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.net.wifi.p2p.STATE_CHANGED".equals(action)) {
            int state = intent.getIntExtra("wifi_p2p_state", -1);
            if (state == 2) {
                setIsWifiP2pEnabled(true);
            } else {
                setIsWifiP2pEnabled(false);
            }
            PApplet.println("P2P state changed - " + state);
        } else if ("android.net.wifi.p2p.PEERS_CHANGED".equals(action)) {
            if (this.manager != null) {
                this.manager.requestPeers(this.channel, this);
            }
            PApplet.println("P2P peers changed");
        } else if ("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(action)) {
            if (this.manager != null && ((NetworkInfo) intent.getParcelableExtra("networkInfo")).isConnected()) {
                this.manager.requestConnectionInfo(this.channel, this);
            }
        } else if ("android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(action)) {
            PApplet.println("p2p device changed" + ((WifiP2pDevice) intent.getParcelableExtra("wifiP2pDevice")));
        }
    }

    public void getConnectionInfo() {
        this.manager.requestConnectionInfo(this.channel, this);
    }

    public void onConnectionInfoAvailable(WifiP2pInfo arg0) {
        WifiP2pInfo info = arg0;
        if (!arg0.groupFormed) {
            this.ip = "";
            return;
        }
        this.ip = info.groupOwnerAddress.getHostAddress();
        PApplet.println("Connection info available for :" + arg0.toString() + "--" + info.groupOwnerAddress.getHostAddress());
    }

    public String getIPAddress() {
        return this.ip;
    }

    public void discover() {
        if (this.manager != null) {
            this.manager.discoverPeers(this.channel, this);
        }
    }

    public void onFailure(int arg0) {
        switch (arg0) {
            case 0:
                PApplet.println("WifiDirect failed " + arg0);
                return;
            case 1:
                PApplet.println("WifiDirect failed " + arg0);
                return;
            case 2:
                PApplet.println("WifiDirect failed " + arg0);
                return;
            default:
                PApplet.println("WifiDirect failed " + arg0);
                return;
        }
    }

    public void onSuccess() {
        PApplet.println("WifiDirect succeeded ");
    }

    public void onPeersAvailable(WifiP2pDeviceList arg0) {
        Collection<WifiP2pDevice> list = arg0.getDeviceList();
        if (list.size() > 0) {
            this.peers.clear();
            for (WifiP2pDevice add : list) {
                this.peers.add(add);
            }
            PApplet.println("New KetaiWifiDirect peer list received:");
            for (WifiP2pDevice d : this.peers) {
                PApplet.println("\t\t" + d.deviceName + ":" + d.deviceAddress);
            }
        }
    }

    public String getHardwareAddress() {
        return ((WifiManager) this.parent.getActivity().getSystemService("wifi")).getConnectionInfo().getMacAddress();
    }

    public void reset() {
        this.peers.clear();
        this.manager.cancelConnect(this.channel, this);
        this.manager.removeGroup(this.channel, this);
    }

    public ArrayList<String> getPeerNameList() {
        ArrayList<String> names = new ArrayList<>();
        for (WifiP2pDevice d : this.peers) {
            names.add(d.deviceName);
        }
        return names;
    }

    public void connect(String deviceName) {
        WifiP2pDevice device = null;
        for (WifiP2pDevice d : this.peers) {
            if (d.deviceAddress == deviceName || d.deviceName == deviceName) {
                device = d;
            }
        }
        WifiP2pConfig config = new WifiP2pConfig();
        if (device != null) {
            config.deviceAddress = device.deviceAddress;
        } else {
            config.deviceAddress = deviceName;
        }
        this.manager.connect(this.channel, config, new WifiP2pManager.ActionListener() {
            public void onSuccess() {
            }

            public void onFailure(int reason) {
                PApplet.println("Failed to connect to device (" + reason + ")");
            }
        });
    }
}
