package ketai.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import processing.core.PApplet;

public class KetaiNet {
    public static String getIP() {
        String thing = "0.0.0.0";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = en.nextElement().getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && thing == "0.0.0.0" && inetAddress.getHostAddress().matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b")) {
                        thing = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            PApplet.println("SocketException:" + ex.toString());
        } catch (NullPointerException e) {
            PApplet.println("Failed to get any network interfaces...");
        }
        return thing;
    }
}
