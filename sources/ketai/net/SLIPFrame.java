package ketai.net;

import java.io.ByteArrayOutputStream;

public class SLIPFrame {
    public static byte END = -64;
    public static byte ESC = -37;
    public static byte ESC_END = -36;
    public static byte ESC_ESC = -35;

    public static byte[] createFrame(byte[] _data) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(_data.length * 2);
        for (int i = 0; i < _data.length; i++) {
            if (_data[i] == END) {
                bout.write(ESC);
                bout.write(ESC_END);
            } else if (_data[i] == ESC) {
                bout.write(ESC);
                bout.write(ESC_ESC);
            } else {
                bout.write(_data[i]);
            }
        }
        return bout.toByteArray();
    }

    public static byte[] parseFrame(byte[] data) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(data.length);
        int i = 0;
        while (i < data.length) {
            if (data[i] == ESC && i + 1 < data.length) {
                i++;
                if (data[i] == ESC_END) {
                    bout.write(END);
                } else if (data[i] == ESC_ESC) {
                    bout.write(ESC);
                } else {
                    bout.write(data[i]);
                }
            }
            i++;
        }
        return bout.toByteArray();
    }
}
