package ketai.net;

import oscP5.OscMessage;

public class KetaiOSCMessage extends OscMessage {
    public KetaiOSCMessage(byte[] _data) {
        super("");
        parseMessage(_data);
    }

    public boolean isValid() {
        return this.isValid;
    }
}
