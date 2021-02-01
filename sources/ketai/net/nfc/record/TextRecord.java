package ketai.net.nfc.record;

import android.nfc.NdefRecord;
import java.io.UnsupportedEncodingException;
import processing.core.PApplet;

public class TextRecord implements ParsedNdefRecord {
    private final String mLanguageCode;
    private final String mText;

    private TextRecord(String languageCode, String text) {
        this.mLanguageCode = languageCode;
        this.mText = text;
    }

    public String getText() {
        return this.mText;
    }

    public String getLanguageCode() {
        return this.mLanguageCode;
    }

    public static TextRecord parse(NdefRecord record) {
        String textEncoding;
        try {
            byte[] payload = record.getPayload();
            PApplet.println("TextRecord parsed and NdefRecord with a payload of " + payload.length + " bytes.");
            if ((payload[0] & 128) == 0) {
                textEncoding = "UTF-8";
            } else {
                textEncoding = "UTF-16";
            }
            int languageCodeLength = payload[0] & 63;
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            String text = new String(payload, languageCodeLength + 1, (payload.length - languageCodeLength) - 1, textEncoding);
            PApplet.println("TextRecord parsing: " + payload);
            PApplet.println("\t parsed text:" + text);
            return new TextRecord(languageCode, text);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        } catch (Exception x) {
            throw new IllegalArgumentException("Error parsing as a TextRecord: " + x.getMessage());
        }
    }

    public static boolean isText(NdefRecord record) {
        try {
            parse(record);
            PApplet.println("TextRecord.isText is true!");
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String getTag() {
        return getText();
    }
}
