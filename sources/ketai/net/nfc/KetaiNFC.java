package ketai.net.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.os.Parcelable;
import android.util.Log;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import ketai.net.nfc.record.ParsedNdefRecord;
import processing.core.PApplet;

public class KetaiNFC implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {
    private NfcAdapter mAdapter;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private NdefMessage messageToBeam;
    /* access modifiers changed from: private */
    public NdefMessage messageToWrite;
    Ndef ndefTag;
    private Method onNFCEventMethod_String;
    private Method onNFCEventMethod_URI;
    private Method onNFCEventMethod_bArray;
    /* access modifiers changed from: private */
    public Method onNFCWriteMethod;
    private PendingIntent p;
    /* access modifiers changed from: private */
    public PApplet parent;
    NdefFormatable tag;

    public KetaiNFC(PApplet pParent) {
        Log.d("KetaiNFC", "KetaiNFC instantiated...");
        this.parent = pParent;
        findParentIntentions();
        initAdapter();
        IntentFilter ndef = new IntentFilter("android.nfc.action.NDEF_DISCOVERED");
        IntentFilter tech = new IntentFilter("android.nfc.action.TECH_DISCOVERED");
        IntentFilter tag2 = new IntentFilter("android.nfc.action.TAG_DISCOVERED");
        try {
            ndef.addDataType("*/*");
            tag2.addDataType("*/*");
            tech.addDataType("*/*");
            this.mFilters = new IntentFilter[]{ndef, tag2, tech};
            this.mTechLists = new String[][]{new String[]{NfcA.class.getName()}, new String[]{MifareUltralight.class.getName()}, new String[]{NfcF.class.getName()}, new String[]{NdefFormatable.class.getName()}};
            this.parent.registerMethod("resume", this);
            this.parent.registerMethod("pause", this);
            this.parent.registerMethod("onNewIntent", this);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
    }

    public void resume() {
        Log.i("KetaiNFC", "resuming...");
        PApplet.println("KetaiNFC: resuming...");
        if (this.mAdapter != null) {
            this.mAdapter.enableForegroundDispatch(this.parent.getActivity(), this.p, this.mFilters, this.mTechLists);
            return;
        }
        PApplet.println("mAdapter was null in onResume()");
        this.mAdapter = NfcAdapter.getDefaultAdapter(this.parent.getActivity());
        if (this.mAdapter != null) {
            this.mAdapter.enableForegroundDispatch(this.parent.getActivity(), this.p, this.mFilters, this.mTechLists);
        }
    }

    public void pause() {
        Log.i("KetaiNFC", "pausing...");
        PApplet.println("KetaiNFC Pausing...");
        if (this.mAdapter != null) {
            this.mAdapter.disableForegroundDispatch(this.parent.getActivity());
        }
    }

    public void onNewIntent(Intent intent) {
        PApplet.println("----------------------> KetaiNFC.onNewIntent()");
        handleIntent(intent);
    }

    public void handleIntent(Intent intent) {
        Tag tag2;
        NdefMessage[] msgs;
        if (this.mAdapter != null) {
            Log.i("KetaiNFC", "processing intent...");
            PApplet.println("KetaiNFC: Processing intent: " + intent.getAction());
            String action = intent.getAction();
            String thingToReturn = "";
            if ("android.nfc.action.TAG_DISCOVERED".equals(action) || "android.nfc.action.NDEF_DISCOVERED".equals(action) || "android.nfc.action.TECH_DISCOVERED".equals(action)) {
                Parcelable[] rawMsgs = intent.getParcelableArrayExtra("android.nfc.extra.NDEF_MESSAGES");
                Parcelable pTag = intent.getParcelableExtra("android.nfc.extra.TAG");
                if (pTag == null || pTag.getClass() != Tag.class) {
                    tag2 = null;
                } else {
                    tag2 = (Tag) pTag;
                    PApplet.println("Found Tag object: " + tag2.toString());
                    String foo = "";
                    String[] techList = tag2.getTechList();
                    for (int i = 0; i < techList.length; i++) {
                        foo = String.valueOf(foo) + techList[i] + "\n";
                    }
                    PApplet.println("Supported Tag tech: " + foo + "\n");
                }
                if (rawMsgs != null) {
                    msgs = new NdefMessage[rawMsgs.length];
                    for (int i2 = 0; i2 < rawMsgs.length; i2++) {
                        msgs[i2] = (NdefMessage) rawMsgs[i2];
                    }
                } else {
                    byte[] empty = new byte[0];
                    msgs = new NdefMessage[]{new NdefMessage(new NdefRecord[]{new NdefRecord(5, empty, empty, empty)})};
                }
                if (tag2 != null && this.messageToWrite != null) {
                    PApplet.println("Attempting to write to Tag");
                    writeNFCString(tag2);
                } else if (msgs != null && msgs.length != 0) {
                    PApplet.println("got nfc message:" + new String(msgs[0].toByteArray()));
                    List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
                    for (int i3 = 0; i3 < records.size(); i3++) {
                        thingToReturn = String.valueOf(thingToReturn) + records.get(i3).getTag();
                    }
                    if (this.onNFCEventMethod_String != null) {
                        try {
                            this.onNFCEventMethod_String.invoke(this.parent, new Object[]{thingToReturn});
                        } catch (Exception e) {
                            PApplet.println("Disabling onNFCEvent() because of an error:" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        byte[] textBytes = text.getBytes(encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16"));
        char status = (char) (langBytes.length + (encodeInUtf8 ? 0 : 128));
        byte[] data = new byte[(langBytes.length + 1 + textBytes.length)];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, langBytes.length + 1, textBytes.length);
        return new NdefRecord(1, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    public void write(URI _url) {
        this.messageToWrite = new NdefMessage(new NdefRecord[]{new NdefRecord(3, _url.toString().getBytes(Charset.forName("UTF-8")), new byte[0], new byte[0])});
    }

    public void write(String textToWrite) {
        byte[] langBytes = Locale.US.getLanguage().getBytes(Charset.forName("UTF-8"));
        byte[] textBytes = textToWrite.getBytes(Charset.forName("UTF-8"));
        char status = (char) (langBytes.length + 0);
        byte[] data = new byte[(langBytes.length + 1 + textBytes.length)];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, langBytes.length + 1, textBytes.length);
        this.messageToWrite = new NdefMessage(new NdefRecord[]{new NdefRecord(1, NdefRecord.RTD_TEXT, new byte[0], data)});
    }

    public void beam(String textToWrite) {
        byte[] langBytes = Locale.US.getLanguage().getBytes(Charset.forName("UTF-8"));
        byte[] textBytes = textToWrite.getBytes(Charset.forName("UTF-8"));
        char status = (char) (langBytes.length + 0);
        byte[] data = new byte[(langBytes.length + 1 + textBytes.length)];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, langBytes.length + 1, textBytes.length);
        this.messageToBeam = new NdefMessage(new NdefRecord[]{new NdefRecord(1, NdefRecord.RTD_TEXT, new byte[0], data)});
    }

    public void write(byte[] _data) {
        PApplet.println("NFC tag byte writing not yet implemented...");
    }

    public void cancelWrite() {
        this.messageToWrite = null;
    }

    private void writeNFCString(Tag t) {
        this.tag = NdefFormatable.get(t);
        this.ndefTag = null;
        if (this.tag == null) {
            PApplet.println("Tag does not support writing (via NdefFormattable). Trying NDEF write...");
            this.ndefTag = Ndef.get(t);
            if (this.ndefTag == null) {
                return;
            }
            if (this.ndefTag.isWritable()) {
                PApplet.println("KetaiNFC: Tag is NDEF writable.");
                Log.i("KetaiNFC", "NDEFTag is writable");
            } else {
                PApplet.println("KetaiNFC: Tag is NOT writable");
                Log.i("KetaiNFC", "Tag is NOT writable");
                if (this.onNFCWriteMethod != null) {
                    try {
                        this.onNFCWriteMethod.invoke(this.parent, new Object[]{false, "Tag is NOT writable"});
                        return;
                    } catch (Exception e) {
                        PApplet.println(" onNFCWriteEvent()  error:" + e.getMessage());
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
        if (this.tag != null && this.messageToWrite != null) {
            this.parent.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        KetaiNFC.this.tag.connect();
                        PApplet.println((Object) KetaiNFC.this.messageToWrite.toByteArray());
                        if (KetaiNFC.this.tag.isConnected()) {
                            KetaiNFC.this.tag.format(KetaiNFC.this.messageToWrite);
                            KetaiNFC.this.messageToWrite = null;
                        } else {
                            PApplet.println("Failed to connect to tag.");
                        }
                        KetaiNFC.this.tag.close();
                    } catch (FormatException fx) {
                        fx.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                        String errorMessage = e.getMessage();
                        PApplet.println("Failed to write to tag.  Error: " + errorMessage);
                        if (KetaiNFC.this.onNFCWriteMethod != null) {
                            try {
                                KetaiNFC.this.onNFCWriteMethod.invoke(KetaiNFC.this.parent, new Object[]{false, errorMessage});
                            } catch (Exception ex) {
                                PApplet.println("Failed to write nfc tag because of an error:" + ex.getMessage());
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            });
        } else if (this.ndefTag != null) {
            this.parent.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        PApplet.println((Object) KetaiNFC.this.messageToWrite.toByteArray());
                        KetaiNFC.this.ndefTag.connect();
                        if (KetaiNFC.this.ndefTag.isConnected()) {
                            KetaiNFC.this.ndefTag.writeNdefMessage(KetaiNFC.this.messageToWrite);
                            KetaiNFC.this.messageToWrite = null;
                        } else {
                            PApplet.println("Failed to connect to Tag for an NDef write.");
                        }
                        KetaiNFC.this.ndefTag.close();
                        if (KetaiNFC.this.onNFCWriteMethod != null) {
                            try {
                                KetaiNFC.this.onNFCWriteMethod.invoke(KetaiNFC.this.parent, new Object[]{true, ""});
                            } catch (Exception e) {
                                PApplet.println("Failed to notify sketch of an NFC write Tag operation: " + e.getMessage());
                            }
                        }
                    } catch (FormatException fx) {
                        fx.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        String errorMessage = e2.getMessage();
                        PApplet.println("Failed to write to tag.  Error: " + errorMessage);
                        if (KetaiNFC.this.onNFCWriteMethod != null) {
                            try {
                                KetaiNFC.this.onNFCWriteMethod.invoke(KetaiNFC.this.parent, new Object[]{false, errorMessage});
                            } catch (Exception ex) {
                                PApplet.println("Failed to write nfc tag & subsequently notify sketch, error:" + ex.getMessage());
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }

    private void initAdapter() {
        this.mAdapter = NfcAdapter.getDefaultAdapter(this.parent.getContext());
        if (this.mAdapter == null) {
            Log.i("KetaiNFC", "Failed to get NFC adapter...");
        } else {
            this.mAdapter.setNdefPushMessageCallback(this, this.parent.getActivity(), new Activity[0]);
        }
        this.p = PendingIntent.getActivity(this.parent.getActivity(), 0, new Intent(this.parent.getActivity(), this.parent.getClass()).addFlags(536870912), 0);
    }

    private void findParentIntentions() {
        try {
            this.onNFCEventMethod_String = this.parent.getClass().getMethod("onNFCEvent", new Class[]{String.class});
            Log.d("KetaiNFC", "Found onNFCEvent(String) callback...");
        } catch (NoSuchMethodException e) {
        }
        try {
            this.onNFCWriteMethod = this.parent.getClass().getMethod("onNFCWrite", new Class[]{Boolean.TYPE, String.class});
            Log.d("KetaiNFC", "Found onNFCWrite callback...");
        } catch (NoSuchMethodException e2) {
        }
        try {
            this.onNFCEventMethod_URI = this.parent.getClass().getMethod("onNFCEvent", new Class[]{URI.class});
            Log.d("KetaiNFC", "Found onNFCEvent(URI) callback...");
        } catch (NoSuchMethodException e3) {
        }
        try {
            this.onNFCEventMethod_bArray = this.parent.getClass().getMethod("onNFCEvent", new Class[]{byte[].class});
            Log.d("KetaiNFC", "Found onNFCEvent(byte[]) callback...");
        } catch (NoSuchMethodException e4) {
        }
    }

    public void onNdefPushComplete(NfcEvent arg0) {
        PApplet.println("Completed a beam! clearing out pending message.");
        this.messageToBeam = null;
    }

    public NdefMessage createNdefMessage(NfcEvent arg0) {
        if (this.messageToBeam == null) {
            beam("");
        }
        PApplet.println("createNdefMessage callback called for beam, returning: " + this.messageToBeam.toString());
        return this.messageToBeam;
    }
}
