package hypermedia.net;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import processing.core.PApplet;

public class UDP implements Runnable {
    public static final int BUFFER_SIZE = 65507;
    InetAddress group;
    String header;
    boolean listen;
    boolean log;
    MulticastSocket mcSocket;
    Object owner;
    String receiveHandler;
    int size;
    Thread thread;
    int timeout;
    String timeoutHandler;
    DatagramSocket ucSocket;

    public UDP(Object obj) {
        this(obj, 0);
    }

    public UDP(Object obj, int i) {
        this(obj, i, (String) null);
    }

    public UDP(Object obj, int i, String str) {
        this.ucSocket = null;
        this.mcSocket = null;
        this.log = false;
        this.listen = false;
        this.timeout = 0;
        this.size = BUFFER_SIZE;
        this.group = null;
        this.thread = null;
        this.owner = null;
        this.receiveHandler = "receive";
        this.timeoutHandler = "timeout";
        this.header = "";
        this.owner = obj;
        try {
            if (obj instanceof PApplet) {
                ((PApplet) obj).registerMethod("dispose", this);
            }
        } catch (NoClassDefFoundError e) {
        }
        try {
            InetAddress byName = InetAddress.getByName(str);
            InetAddress inetAddress = str == null ? null : byName;
            if (!byName.isMulticastAddress()) {
                this.ucSocket = new DatagramSocket(i, inetAddress);
                log(new StringBuffer().append("bound socket to host:").append(address()).append(", port: ").append(port()).toString());
                return;
            }
            this.mcSocket = new MulticastSocket(i);
            this.mcSocket.joinGroup(byName);
            this.group = byName;
            log(new StringBuffer().append("bound multicast socket to host:").append(address()).append(", port: ").append(port()).append(", group:").append(this.group).toString());
        } catch (IOException e2) {
            error(new StringBuffer().append("opening socket failed!\n\t> address:").append(str).append(", port:").append(i).append(" [group:").append(this.group).append("]").append("\n\t> ").append(e2.getMessage()).toString());
        } catch (IllegalArgumentException e3) {
            error(new StringBuffer().append("opening socket failed!\n\t> bad arguments: ").append(e3.getMessage()).toString());
        } catch (SecurityException e4) {
            error(new StringBuffer().append(isMulticast() ? "could not joined the group" : "warning").append("\n\t> ").append(e4.getMessage()).toString());
        }
    }

    public void dispose() {
        close();
    }

    public void close() {
        String str;
        String str2;
        String str3;
        if (!isClosed()) {
            int port = port();
            String address = address();
            try {
                if (isMulticast()) {
                    if (this.group != null) {
                        this.mcSocket.leaveGroup(this.group);
                        log(new StringBuffer().append("leave group < address:").append(this.group).append(" >").toString());
                    }
                    this.mcSocket.close();
                    this.mcSocket = null;
                } else {
                    this.ucSocket.close();
                    this.ucSocket = null;
                }
            } catch (IOException e) {
                error(new StringBuffer().append("Error while closing the socket!\n\t> ").append(e.getMessage()).toString());
            } catch (SecurityException e2) {
            } finally {
                str = "close socket < port:";
                str2 = ", address:";
                StringBuffer append = new StringBuffer().append(str).append(port).append(str2).append(address);
                str3 = " >\n";
                log(append.append(str3).toString());
            }
        }
    }

    public boolean isClosed() {
        if (isMulticast()) {
            if (this.mcSocket == null) {
                return true;
            }
            return this.mcSocket.isClosed();
        } else if (this.ucSocket != null) {
            return this.ucSocket.isClosed();
        } else {
            return true;
        }
    }

    public int port() {
        if (isClosed()) {
            return -1;
        }
        return isMulticast() ? this.mcSocket.getLocalPort() : this.ucSocket.getLocalPort();
    }

    public String address() {
        if (isClosed()) {
            return null;
        }
        InetAddress localAddress = isMulticast() ? this.mcSocket.getLocalAddress() : this.ucSocket.getLocalAddress();
        return localAddress.isAnyLocalAddress() ? null : localAddress.getHostAddress();
    }

    public boolean send(String str) {
        return send(str.getBytes());
    }

    public boolean send(byte[] bArr) {
        if (isMulticast() && this.group == null) {
            return false;
        }
        return send(bArr, isMulticast() ? this.group.getHostAddress() : address(), port());
    }

    public boolean send(String str, String str2) {
        return send(str.getBytes(), str2);
    }

    public boolean send(byte[] bArr, String str) {
        return send(bArr, str, port());
    }

    public boolean send(String str, String str2, int i) {
        return send(str.getBytes(), str2, i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0055, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0056, code lost:
        r1 = r0;
        r0 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        return false;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x00a3 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:1:0x0002] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean send(byte[] r6, java.lang.String r7, int r8) {
        /*
            r5 = this;
            r3 = 0
            r1 = 0
            java.net.DatagramPacket r2 = new java.net.DatagramPacket     // Catch:{ IOException -> 0x00a6, all -> 0x00a3 }
            int r0 = r6.length     // Catch:{ IOException -> 0x00a6, all -> 0x00a3 }
            java.net.InetAddress r4 = java.net.InetAddress.getByName(r7)     // Catch:{ IOException -> 0x00a6, all -> 0x00a3 }
            r2.<init>(r6, r0, r4, r8)     // Catch:{ IOException -> 0x00a6, all -> 0x00a3 }
            boolean r0 = r5.isMulticast()     // Catch:{ IOException -> 0x0055, all -> 0x00a3 }
            if (r0 == 0) goto L_0x004f
            java.net.MulticastSocket r0 = r5.mcSocket     // Catch:{ IOException -> 0x0055, all -> 0x00a3 }
            r0.send(r2)     // Catch:{ IOException -> 0x0055, all -> 0x00a3 }
        L_0x0017:
            r0 = 1
            java.lang.StringBuffer r1 = new java.lang.StringBuffer     // Catch:{ IOException -> 0x00ab }
            r1.<init>()     // Catch:{ IOException -> 0x00ab }
            java.lang.String r3 = "send packet -> address:"
            java.lang.StringBuffer r1 = r1.append(r3)     // Catch:{ IOException -> 0x00ab }
            java.net.InetAddress r3 = r2.getAddress()     // Catch:{ IOException -> 0x00ab }
            java.lang.StringBuffer r1 = r1.append(r3)     // Catch:{ IOException -> 0x00ab }
            java.lang.String r3 = ", port:"
            java.lang.StringBuffer r1 = r1.append(r3)     // Catch:{ IOException -> 0x00ab }
            int r3 = r2.getPort()     // Catch:{ IOException -> 0x00ab }
            java.lang.StringBuffer r1 = r1.append(r3)     // Catch:{ IOException -> 0x00ab }
            java.lang.String r3 = ", length: "
            java.lang.StringBuffer r1 = r1.append(r3)     // Catch:{ IOException -> 0x00ab }
            int r3 = r2.getLength()     // Catch:{ IOException -> 0x00ab }
            java.lang.StringBuffer r1 = r1.append(r3)     // Catch:{ IOException -> 0x00ab }
            java.lang.String r1 = r1.toString()     // Catch:{ IOException -> 0x00ab }
            r5.log((java.lang.String) r1)     // Catch:{ IOException -> 0x00ab }
        L_0x004e:
            return r0
        L_0x004f:
            java.net.DatagramSocket r0 = r5.ucSocket     // Catch:{ IOException -> 0x0055, all -> 0x00a3 }
            r0.send(r2)     // Catch:{ IOException -> 0x0055, all -> 0x00a3 }
            goto L_0x0017
        L_0x0055:
            r0 = move-exception
            r1 = r0
            r0 = r3
        L_0x0058:
            java.lang.StringBuffer r3 = new java.lang.StringBuffer     // Catch:{ all -> 0x00a1 }
            r3.<init>()     // Catch:{ all -> 0x00a1 }
            java.lang.String r4 = "could not send message!\t\n> port:"
            java.lang.StringBuffer r3 = r3.append(r4)     // Catch:{ all -> 0x00a1 }
            java.lang.StringBuffer r3 = r3.append(r8)     // Catch:{ all -> 0x00a1 }
            java.lang.String r4 = ", ip:"
            java.lang.StringBuffer r3 = r3.append(r4)     // Catch:{ all -> 0x00a1 }
            java.lang.StringBuffer r3 = r3.append(r7)     // Catch:{ all -> 0x00a1 }
            java.lang.String r4 = ", buffer size: "
            java.lang.StringBuffer r3 = r3.append(r4)     // Catch:{ all -> 0x00a1 }
            int r4 = r5.size     // Catch:{ all -> 0x00a1 }
            java.lang.StringBuffer r3 = r3.append(r4)     // Catch:{ all -> 0x00a1 }
            java.lang.String r4 = ", packet length: "
            java.lang.StringBuffer r3 = r3.append(r4)     // Catch:{ all -> 0x00a1 }
            int r2 = r2.getLength()     // Catch:{ all -> 0x00a1 }
            java.lang.StringBuffer r2 = r3.append(r2)     // Catch:{ all -> 0x00a1 }
            java.lang.String r3 = "\t\n> "
            java.lang.StringBuffer r2 = r2.append(r3)     // Catch:{ all -> 0x00a1 }
            java.lang.String r1 = r1.getMessage()     // Catch:{ all -> 0x00a1 }
            java.lang.StringBuffer r1 = r2.append(r1)     // Catch:{ all -> 0x00a1 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00a1 }
            r5.error(r1)     // Catch:{ all -> 0x00a1 }
            goto L_0x004e
        L_0x00a1:
            r1 = move-exception
            goto L_0x004e
        L_0x00a3:
            r0 = move-exception
            r0 = r3
            goto L_0x004e
        L_0x00a6:
            r0 = move-exception
            r2 = r1
            r1 = r0
            r0 = r3
            goto L_0x0058
        L_0x00ab:
            r1 = move-exception
            goto L_0x0058
        */
        throw new UnsupportedOperationException("Method not decompiled: hypermedia.net.UDP.send(byte[], java.lang.String, int):boolean");
    }

    public boolean setBuffer(int i) {
        int i2;
        int i3;
        if (isListening()) {
            return false;
        }
        try {
            if (isMulticast()) {
                this.mcSocket.setSendBufferSize(i > 0 ? i : 65507);
                MulticastSocket multicastSocket = this.mcSocket;
                if (i > 0) {
                    i3 = i;
                } else {
                    i3 = 65507;
                }
                multicastSocket.setReceiveBufferSize(i3);
            } else {
                this.ucSocket.setSendBufferSize(i > 0 ? i : 65507);
                DatagramSocket datagramSocket = this.ucSocket;
                if (i > 0) {
                    i2 = i;
                } else {
                    i2 = 65507;
                }
                datagramSocket.setReceiveBufferSize(i2);
            }
            if (i <= 0) {
                i = 65507;
            }
            this.size = i;
            return true;
        } catch (SocketException e) {
            error(new StringBuffer().append("could not set the buffer!\n> ").append(e.getMessage()).toString());
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    public int getBuffer() {
        return this.size;
    }

    public boolean isListening() {
        return this.listen;
    }

    public void listen(boolean z) {
        this.listen = z;
        this.timeout = 0;
        if (z && this.thread == null && !isClosed()) {
            this.thread = new Thread(this);
            this.thread.start();
        }
        if (!z && this.thread != null) {
            send(new byte[0]);
            this.thread.interrupt();
            this.thread = null;
        }
    }

    public void listen(int i) {
        if (!isClosed()) {
            this.listen = true;
            this.timeout = i;
            if (this.thread != null) {
                send(new byte[0]);
            }
            if (this.thread == null) {
                this.thread = new Thread(this);
                this.thread.start();
            }
        }
    }

    public void listen() {
        try {
            byte[] bArr = new byte[this.size];
            DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
            if (isMulticast()) {
                this.mcSocket.setSoTimeout(this.timeout);
                this.mcSocket.receive(datagramPacket);
            } else {
                this.ucSocket.setSoTimeout(this.timeout);
                this.ucSocket.receive(datagramPacket);
            }
            log(new StringBuffer().append("receive packet <- from ").append(datagramPacket.getAddress()).append(", port:").append(datagramPacket.getPort()).append(", length: ").append(datagramPacket.getLength()).toString());
            if (datagramPacket.getLength() != 0) {
                byte[] bArr2 = new byte[datagramPacket.getLength()];
                System.arraycopy(datagramPacket.getData(), 0, bArr2, 0, bArr2.length);
                try {
                    callReceiveHandler(bArr2);
                } catch (NoSuchMethodException e) {
                    callReceiveHandler(bArr2, datagramPacket.getAddress().getHostAddress(), datagramPacket.getPort());
                }
            }
        } catch (NullPointerException e2) {
            this.listen = false;
            this.thread = null;
        } catch (IOException e3) {
            this.listen = false;
            this.thread = null;
            if (e3 instanceof SocketTimeoutException) {
                callTimeoutHandler();
            } else if (this.ucSocket != null && this.mcSocket != null) {
                error(new StringBuffer().append("listen failed!\n\t> ").append(e3.getMessage()).toString());
            }
        }
    }

    public void run() {
        while (this.listen) {
            listen();
        }
    }

    public void setReceiveHandler(String str) {
        this.receiveHandler = str;
    }

    private void callReceiveHandler(byte[] bArr) throws NoSuchMethodException {
        try {
            Object[] objArr = {bArr};
            this.owner.getClass().getMethod(this.receiveHandler, new Class[]{bArr.getClass()}).invoke(this.owner, objArr);
        } catch (IllegalAccessException e) {
            error(e.getMessage());
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        }
    }

    private void callReceiveHandler(byte[] bArr, String str, int i) {
        try {
            Class[] clsArr = {bArr.getClass(), str.getClass(), Integer.TYPE};
            this.owner.getClass().getMethod(this.receiveHandler, clsArr).invoke(this.owner, new Object[]{bArr, str, new Integer(i)});
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e2) {
            error(e2.getMessage());
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        }
    }

    public void setTimeoutHandler(String str) {
        this.timeoutHandler = str;
    }

    private void callTimeoutHandler() {
        try {
            this.owner.getClass().getDeclaredMethod(this.timeoutHandler, (Class[]) null).invoke(this.owner, (Object[]) null);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e2) {
            error(e2.getMessage());
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        }
    }

    public boolean isMulticast() {
        return this.mcSocket != null;
    }

    public boolean isJoined() {
        return this.group != null;
    }

    public boolean isBroadcast() {
        try {
            if (this.ucSocket == null) {
                return false;
            }
            return this.ucSocket.getBroadcast();
        } catch (SocketException e) {
            error(e.getMessage());
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    public boolean broadcast(boolean z) {
        try {
            if (this.ucSocket == null) {
                return false;
            }
            this.ucSocket.setBroadcast(z);
            return isBroadcast();
        } catch (SocketException e) {
            error(e.getMessage());
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    public void loopback(boolean z) {
        try {
            if (isMulticast()) {
                this.mcSocket.setLoopbackMode(!z);
            }
        } catch (SocketException e) {
            error(new StringBuffer().append("could not set the loopback mode!\n\t>").append(e.getMessage()).toString());
        }
    }

    public boolean isLoopback() {
        try {
            if (!isMulticast() || isClosed() || this.mcSocket.getLoopbackMode()) {
                return false;
            }
            return true;
        } catch (SocketException e) {
            error(new StringBuffer().append("could not get the loopback mode!\n\t> ").append(e.getMessage()).toString());
            return false;
        }
    }

    public boolean setTimeToLive(int i) {
        try {
            if (isMulticast() && !isClosed()) {
                this.mcSocket.setTimeToLive(i);
            }
            return true;
        } catch (IOException e) {
            error(new StringBuffer().append("setting the default \"Time to Live\" value failed!\n\t> ").append(e.getMessage()).toString());
            return false;
        } catch (IllegalArgumentException e2) {
            error("\"Time to Live\" value must be in the range of 0-255");
            return false;
        }
    }

    public int getTimeToLive() {
        try {
            if (isMulticast() && !isClosed()) {
                return this.mcSocket.getTimeToLive();
            }
        } catch (IOException e) {
            error(new StringBuffer().append("could not retrieve the current time-to-live value!\n\t> ").append(e.getMessage()).toString());
        }
        return -1;
    }

    public void log(boolean z) {
        this.log = z;
    }

    private void log(String str) {
        Date date = new Date();
        if (!this.log && this.header.equals("")) {
            this.header = new StringBuffer().append("-- UDP session started at ").append(date).append(" --\n-- ").append(str).append(" --\n").toString();
        }
        if (this.log) {
            System.out.println(new StringBuffer().append(this.header).append("[").append(new SimpleDateFormat("yy-MM-dd HH:mm:ss.S Z").format(date)).append("] ").append(str).toString());
            this.header = "";
        }
    }

    private void error(String str) {
        System.err.println(str);
    }
}
