package processing.data;

import android.support.v4.view.MotionEventCompat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import processing.core.PConstants;

class JSONTokener {
    private long character;
    private boolean eof;
    private long index;
    private long line;
    private char previous;
    private Reader reader;
    private boolean usePrevious;

    public JSONTokener(InputStream inputStream) {
        this((Reader) new InputStreamReader(inputStream));
    }

    public JSONTokener(Reader reader2) {
        this.reader = !reader2.markSupported() ? new BufferedReader(reader2) : reader2;
        this.eof = false;
        this.usePrevious = false;
        this.previous = 0;
        this.index = 0;
        this.character = 1;
        this.line = 1;
    }

    public JSONTokener(String str) {
        this((Reader) new StringReader(str));
    }

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - '7';
        }
        if (c < 'a' || c > 'f') {
            return -1;
        }
        return c - 'W';
    }

    public void back() {
        if (this.usePrevious || this.index <= 0) {
            throw new RuntimeException("Stepping back two steps is not supported");
        }
        this.index--;
        this.character--;
        this.usePrevious = true;
        this.eof = false;
    }

    public boolean end() {
        return this.eof && !this.usePrevious;
    }

    public boolean more() {
        next();
        if (end()) {
            return false;
        }
        back();
        return true;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: char} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public char next() {
        /*
            r9 = this;
            r2 = 0
            r8 = 10
            r0 = 0
            r4 = 1
            boolean r1 = r9.usePrevious
            if (r1 == 0) goto L_0x0029
            r9.usePrevious = r0
            char r0 = r9.previous
        L_0x000f:
            long r6 = r9.index
            long r6 = r6 + r4
            r9.index = r6
            char r1 = r9.previous
            r6 = 13
            if (r1 != r6) goto L_0x003e
            long r6 = r9.line
            long r6 = r6 + r4
            r9.line = r6
            if (r0 != r8) goto L_0x003c
        L_0x0021:
            r9.character = r2
        L_0x0023:
            char r0 = (char) r0
            r9.previous = r0
            char r0 = r9.previous
            return r0
        L_0x0029:
            java.io.Reader r1 = r9.reader     // Catch:{ IOException -> 0x0035 }
            int r1 = r1.read()     // Catch:{ IOException -> 0x0035 }
            if (r1 > 0) goto L_0x004e
            r1 = 1
            r9.eof = r1
            goto L_0x000f
        L_0x0035:
            r0 = move-exception
            java.lang.RuntimeException r1 = new java.lang.RuntimeException
            r1.<init>(r0)
            throw r1
        L_0x003c:
            r2 = r4
            goto L_0x0021
        L_0x003e:
            if (r0 != r8) goto L_0x0048
            long r6 = r9.line
            long r4 = r4 + r6
            r9.line = r4
            r9.character = r2
            goto L_0x0023
        L_0x0048:
            long r2 = r9.character
            long r2 = r2 + r4
            r9.character = r2
            goto L_0x0023
        L_0x004e:
            r0 = r1
            goto L_0x000f
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.data.JSONTokener.next():char");
    }

    public char next(char c) {
        char next = next();
        if (next == c) {
            return next;
        }
        throw new RuntimeException("Expected '" + c + "' and instead saw '" + next + "'");
    }

    public String next(int i) {
        if (i == 0) {
            return "";
        }
        char[] cArr = new char[i];
        for (int i2 = 0; i2 < i; i2++) {
            cArr[i2] = next();
            if (end()) {
                throw new RuntimeException("Substring bounds error");
            }
        }
        return new String(cArr);
    }

    /*  JADX ERROR: StackOverflow in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public char nextClean() {
        /*
            r2 = this;
        L_0x0000:
            char r0 = r2.next()
            if (r0 == 0) goto L_0x000a
            r1 = 32
            if (r0 <= r1) goto L_0x0000
        L_0x000a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.data.JSONTokener.nextClean():char");
    }

    public String nextString(char c) {
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            char next = next();
            switch (next) {
                case 0:
                case 10:
                case 13:
                    throw new RuntimeException("Unterminated string");
                case '\\':
                    char next2 = next();
                    switch (next2) {
                        case '\"':
                        case MotionEventCompat.AXIS_GENERIC_8:
                        case MotionEventCompat.AXIS_GENERIC_16:
                        case '\\':
                            stringBuffer.append(next2);
                            break;
                        case 'b':
                            stringBuffer.append(8);
                            break;
                        case PConstants.BOTTOM:
                            stringBuffer.append(12);
                            break;
                        case 'n':
                            stringBuffer.append(10);
                            break;
                        case 'r':
                            stringBuffer.append(PConstants.RETURN);
                            break;
                        case 't':
                            stringBuffer.append(9);
                            break;
                        case 'u':
                            stringBuffer.append((char) Integer.parseInt(next(4), 16));
                            break;
                        default:
                            throw new RuntimeException("Illegal escape.");
                    }
                default:
                    if (next != c) {
                        stringBuffer.append(next);
                        break;
                    } else {
                        return stringBuffer.toString();
                    }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0017  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String nextTo(char r4) {
        /*
            r3 = this;
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
        L_0x0005:
            char r1 = r3.next()
            if (r1 == r4) goto L_0x0015
            if (r1 == 0) goto L_0x0015
            r2 = 10
            if (r1 == r2) goto L_0x0015
            r2 = 13
            if (r1 != r2) goto L_0x0023
        L_0x0015:
            if (r1 == 0) goto L_0x001a
            r3.back()
        L_0x001a:
            java.lang.String r0 = r0.toString()
            java.lang.String r0 = r0.trim()
            return r0
        L_0x0023:
            r0.append(r1)
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.data.JSONTokener.nextTo(char):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x001b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String nextTo(java.lang.String r4) {
        /*
            r3 = this;
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
        L_0x0005:
            char r1 = r3.next()
            int r2 = r4.indexOf(r1)
            if (r2 >= 0) goto L_0x0019
            if (r1 == 0) goto L_0x0019
            r2 = 10
            if (r1 == r2) goto L_0x0019
            r2 = 13
            if (r1 != r2) goto L_0x0027
        L_0x0019:
            if (r1 == 0) goto L_0x001e
            r3.back()
        L_0x001e:
            java.lang.String r0 = r0.toString()
            java.lang.String r0 = r0.trim()
            return r0
        L_0x0027:
            r0.append(r1)
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.data.JSONTokener.nextTo(java.lang.String):java.lang.String");
    }

    public Object nextValue() {
        char nextClean = nextClean();
        switch (nextClean) {
            case '\"':
            case MotionEventCompat.AXIS_GENERIC_8:
                return nextString(nextClean);
            case '[':
                back();
                return new JSONArray(this);
            case '{':
                back();
                return new JSONObject(this);
            default:
                StringBuffer stringBuffer = new StringBuffer();
                while (nextClean >= ' ' && ",:]}/\\\"[{;=#".indexOf(nextClean) < 0) {
                    stringBuffer.append(nextClean);
                    nextClean = next();
                }
                back();
                String trim = stringBuffer.toString().trim();
                if (!"".equals(trim)) {
                    return JSONObject.stringToValue(trim);
                }
                throw new RuntimeException("Missing value");
        }
    }

    public char skipTo(char c) {
        char next;
        try {
            long j = this.index;
            long j2 = this.character;
            long j3 = this.line;
            this.reader.mark(1000000);
            while (true) {
                next = next();
                if (next != 0) {
                    if (next == c) {
                        back();
                        break;
                    }
                } else {
                    this.reader.reset();
                    this.index = j;
                    this.character = j2;
                    this.line = j3;
                    break;
                }
            }
            return next;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
    }
}
