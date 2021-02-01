package ketai.net.nfc.record;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import java.util.Arrays;
import java.util.NoSuchElementException;
import ketai.net.nfc.NdefMessageParser;

public class SmartPoster implements ParsedNdefRecord {
    private static final byte[] ACTION_RECORD_TYPE = {97, 99, 116};
    private static final byte[] TYPE_TYPE = {116};
    private final TextRecord mTitleRecord;
    private final UriRecord mUriRecord;

    private SmartPoster(UriRecord uri, TextRecord title, RecommendedAction action, String type) {
        this.mUriRecord = uri;
        this.mTitleRecord = title;
    }

    public UriRecord getUriRecord() {
        return this.mUriRecord;
    }

    public TextRecord getTitle() {
        return this.mTitleRecord;
    }

    public static SmartPoster parse(NdefRecord record) {
        try {
            return parse(new NdefMessage(record.getPayload()).getRecords());
        } catch (FormatException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static SmartPoster parse(NdefRecord[] recordsRaw) {
        try {
            return new SmartPoster((UriRecord) null, (TextRecord) getFirstIfExists(NdefMessageParser.getRecords(recordsRaw), TextRecord.class), parseRecommendedAction(recordsRaw), parseType(recordsRaw));
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean isPoster(NdefRecord record) {
        try {
            parse(record);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static <T> T getFirstIfExists(Iterable<?> iterable, Class<T> cls) {
        return null;
    }

    private enum RecommendedAction {
        UNKNOWN((byte) -1),
        DO_ACTION((byte) 0),
        SAVE_FOR_LATER((byte) 1),
        OPEN_FOR_EDITING((byte) 2);
        
        private final byte mAction;

        private RecommendedAction(byte val) {
            this.mAction = val;
        }

        private byte getByte() {
            return this.mAction;
        }
    }

    private static NdefRecord getByType(byte[] type, NdefRecord[] records) {
        for (NdefRecord record : records) {
            if (Arrays.equals(type, record.getType())) {
                return record;
            }
        }
        return null;
    }

    private static RecommendedAction parseRecommendedAction(NdefRecord[] records) {
        NdefRecord record = getByType(ACTION_RECORD_TYPE, records);
        if (record == null) {
            return RecommendedAction.UNKNOWN;
        }
        byte b = record.getPayload()[0];
        return RecommendedAction.UNKNOWN;
    }

    private static String parseType(NdefRecord[] records) {
        if (getByType(TYPE_TYPE, records) == null) {
        }
        return null;
    }

    public String getTag() {
        return null;
    }
}
