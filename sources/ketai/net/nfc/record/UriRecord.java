package ketai.net.nfc.record;

import android.net.Uri;
import android.nfc.NdefRecord;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;

public class UriRecord implements ParsedNdefRecord {
    public static final String RECORD_TYPE = "UriRecord";
    private static final HashMap<Byte, String> URI_PREFIX_MAP = new HashMap<Byte, String>() {
        {
            put(new Byte((byte) 0), "");
            put(new Byte((byte) 1), "http://www.");
            put(new Byte((byte) 2), "https://www.");
            put(new Byte((byte) 3), "http://");
            put(new Byte((byte) 4), "https://");
            put(new Byte((byte) 5), "tel:");
            put(new Byte((byte) 6), "mailto:");
            put(new Byte((byte) 7), "ftp://anonymous:anonymous@");
            put(new Byte((byte) 8), "ftp://ftp.");
            put(new Byte((byte) 9), "ftps://");
            put(new Byte((byte) 10), "sftp://");
            put(new Byte((byte) 11), "smb://");
            put(new Byte((byte) 12), "nfs://");
            put(new Byte((byte) 13), "ftp://");
            put(new Byte((byte) 14), "dav://");
            put(new Byte((byte) 15), "news:");
            put(new Byte((byte) 16), "telnet://");
            put(new Byte((byte) 17), "imap:");
            put(new Byte((byte) 18), "rtsp://");
            put(new Byte((byte) 19), "urn:");
            put(new Byte((byte) 20), "pop:");
            put(new Byte((byte) 21), "sip:");
            put(new Byte((byte) 22), "sips:");
            put(new Byte((byte) 23), "tftp:");
            put(new Byte((byte) 24), "btspp://");
            put(new Byte((byte) 25), "btl2cap://");
            put(new Byte((byte) 26), "btgoep://");
            put(new Byte((byte) 27), "tcpobex://");
            put(new Byte((byte) 28), "irdaobex://");
            put(new Byte((byte) 29), "file://");
            put(new Byte((byte) 30), "urn:epc:id:");
            put(new Byte((byte) 31), "urn:epc:tag:");
            put(new Byte((byte) 32), "urn:epc:pat:");
            put(new Byte((byte) 33), "urn:epc:raw:");
            put(new Byte((byte) 34), "urn:epc:");
            put(new Byte((byte) 35), "urn:nfc:");
        }
    };
    private final Uri mUri;

    private UriRecord(Uri uri) {
        if (uri != null) {
            this.mUri = uri;
        } else {
            this.mUri = Uri.EMPTY;
        }
    }

    public Uri getUri() {
        return this.mUri;
    }

    public static UriRecord parse(NdefRecord record) {
        short tnf = record.getTnf();
        if (tnf == 1) {
            return parseWellKnown(record);
        }
        if (tnf == 3) {
            return parseAbsolute(record);
        }
        throw new IllegalArgumentException("Unknown TNF " + tnf);
    }

    private static UriRecord parseAbsolute(NdefRecord record) {
        return new UriRecord(Uri.parse(new String(record.getPayload(), Charset.forName("UTF-8"))));
    }

    private static UriRecord parseWellKnown(NdefRecord record) {
        if (!Arrays.equals(record.getType(), NdefRecord.RTD_URI)) {
            return new UriRecord(Uri.EMPTY);
        }
        byte[] payload = record.getPayload();
        String prefix = URI_PREFIX_MAP.get(Byte.valueOf(payload[0]));
        byte[] fullUri = Arrays.copyOf(prefix.getBytes(), prefix.getBytes(Charset.forName("UTF-8")).length + payload.length);
        int k = 0;
        for (int i = prefix.getBytes(Charset.forName("UTF-8")).length; i < prefix.getBytes(Charset.forName("UTF-8")).length + payload.length && k < payload.length; i++) {
            fullUri[i] = payload[k];
            k++;
        }
        return new UriRecord(Uri.parse(new String(fullUri, Charset.forName("UTF-8"))));
    }

    public static boolean isUri(NdefRecord record) {
        try {
            parse(record);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String getTag() {
        return this.mUri.toString();
    }
}
