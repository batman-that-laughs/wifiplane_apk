package processing.data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import processing.core.PApplet;

public class StringList implements Iterable<String> {
    int count;
    String[] data;

    public StringList() {
        this(10);
    }

    public StringList(int i) {
        this.data = new String[i];
    }

    public StringList(Iterable<String> iterable) {
        this(10);
        for (String append : iterable) {
            append(append);
        }
    }

    public StringList(Object... objArr) {
        this.count = objArr.length;
        this.data = new String[this.count];
        int i = 0;
        for (Object obj : objArr) {
            if (obj != null) {
                this.data[i] = obj.toString();
            }
            i++;
        }
    }

    public StringList(String[] strArr) {
        this.count = strArr.length;
        this.data = new String[this.count];
        System.arraycopy(strArr, 0, this.data, 0, this.count);
    }

    private void crop() {
        if (this.count != this.data.length) {
            this.data = PApplet.subset(this.data, 0, this.count);
        }
    }

    private void sortImpl(final boolean z) {
        new Sort() {
            public float compare(int i, int i2) {
                float compareToIgnoreCase = (float) StringList.this.data[i].compareToIgnoreCase(StringList.this.data[i2]);
                return z ? -compareToIgnoreCase : compareToIgnoreCase;
            }

            public int size() {
                return StringList.this.count;
            }

            public void swap(int i, int i2) {
                String str = StringList.this.data[i];
                StringList.this.data[i] = StringList.this.data[i2];
                StringList.this.data[i2] = str;
            }
        }.run();
    }

    public void append(String str) {
        if (this.count == this.data.length) {
            this.data = PApplet.expand(this.data);
        }
        String[] strArr = this.data;
        int i = this.count;
        this.count = i + 1;
        strArr[i] = str;
    }

    public void append(StringList stringList) {
        for (String append : stringList.values()) {
            append(append);
        }
    }

    public void append(String[] strArr) {
        for (String append : strArr) {
            append(append);
        }
    }

    public void appendUnique(String str) {
        if (!hasValue(str)) {
            append(str);
        }
    }

    public String[] array() {
        return array((String[]) null);
    }

    public String[] array(String[] strArr) {
        if (strArr == null || strArr.length != this.count) {
            strArr = new String[this.count];
        }
        System.arraycopy(this.data, 0, strArr, 0, this.count);
        return strArr;
    }

    public void clear() {
        this.count = 0;
    }

    public StringList copy() {
        StringList stringList = new StringList(this.data);
        stringList.count = this.count;
        return stringList;
    }

    public String get(int i) {
        if (i < this.count) {
            return this.data[i];
        }
        throw new ArrayIndexOutOfBoundsException(i);
    }

    public IntDict getOrder() {
        IntDict intDict = new IntDict();
        for (int i = 0; i < this.count; i++) {
            intDict.set(this.data[i], i);
        }
        return intDict;
    }

    public StringList getSubset(int i) {
        return getSubset(i, this.count - i);
    }

    public StringList getSubset(int i, int i2) {
        String[] strArr = new String[i2];
        System.arraycopy(this.data, i, strArr, 0, i2);
        return new StringList(strArr);
    }

    public IntDict getTally() {
        IntDict intDict = new IntDict();
        for (int i = 0; i < this.count; i++) {
            intDict.increment(this.data[i]);
        }
        return intDict;
    }

    public String[] getUnique() {
        return getTally().keyArray();
    }

    public boolean hasValue(String str) {
        if (str == null) {
            for (int i = 0; i < this.count; i++) {
                if (this.data[i] == null) {
                    return true;
                }
            }
            return false;
        }
        for (int i2 = 0; i2 < this.count; i2++) {
            if (str.equals(this.data[i2])) {
                return true;
            }
        }
        return false;
    }

    public int index(String str) {
        int i = 0;
        if (str == null) {
            while (i < this.count) {
                if (this.data[i] == null) {
                    return i;
                }
                i++;
            }
        } else {
            while (i < this.count) {
                if (str.equals(this.data[i])) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    public void insert(int i, String str) {
        insert(i, new String[]{str});
    }

    public void insert(int i, StringList stringList) {
        insert(i, stringList.values());
    }

    public void insert(int i, String[] strArr) {
        if (i < 0) {
            throw new IllegalArgumentException("insert() index cannot be negative: it was " + i);
        } else if (i >= this.data.length) {
            throw new IllegalArgumentException("insert() index " + i + " is past the end of this list");
        } else {
            String[] strArr2 = new String[(this.count + strArr.length)];
            System.arraycopy(this.data, 0, strArr2, 0, Math.min(this.count, i));
            System.arraycopy(strArr, 0, strArr2, i, strArr.length);
            System.arraycopy(this.data, i, strArr2, strArr.length + i, this.count - i);
            this.count += strArr.length;
            this.data = strArr2;
        }
    }

    public Iterator<String> iterator() {
        return new Iterator<String>() {
            int index = -1;

            public boolean hasNext() {
                return this.index + 1 < StringList.this.count;
            }

            public String next() {
                String[] strArr = StringList.this.data;
                int i = this.index + 1;
                this.index = i;
                return strArr[i];
            }

            public void remove() {
                StringList.this.remove(this.index);
            }
        };
    }

    public String join(String str) {
        if (this.count == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.data[0]);
        for (int i = 1; i < this.count; i++) {
            sb.append(str);
            sb.append(this.data[i]);
        }
        return sb.toString();
    }

    public void lower() {
        for (int i = 0; i < this.count; i++) {
            if (this.data[i] != null) {
                this.data[i] = this.data[i].toLowerCase();
            }
        }
    }

    public String pop() {
        if (this.count == 0) {
            throw new RuntimeException("Can't call pop() on an empty list");
        }
        String str = get(this.count - 1);
        String[] strArr = this.data;
        int i = this.count - 1;
        this.count = i;
        strArr[i] = null;
        return str;
    }

    public void print() {
        for (int i = 0; i < size(); i++) {
            System.out.format("[%d] %s%n", new Object[]{Integer.valueOf(i), this.data[i]});
        }
    }

    public void push(String str) {
        append(str);
    }

    public String remove(int i) {
        if (i < 0 || i >= this.count) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        String str = this.data[i];
        while (i < this.count - 1) {
            this.data[i] = this.data[i + 1];
            i++;
        }
        this.count--;
        return str;
    }

    public int removeValue(String str) {
        if (str == null) {
            for (int i = 0; i < this.count; i++) {
                if (this.data[i] == null) {
                    remove(i);
                    return i;
                }
            }
        } else {
            int index = index(str);
            if (index != -1) {
                remove(index);
                return index;
            }
        }
        return -1;
    }

    public int removeValues(String str) {
        int i;
        int i2 = 0;
        if (str == null) {
            i = 0;
            while (i2 < this.count) {
                if (this.data[i2] != null) {
                    this.data[i] = this.data[i2];
                    i++;
                }
                i2++;
            }
        } else {
            i = 0;
            while (i2 < this.count) {
                if (!str.equals(this.data[i2])) {
                    this.data[i] = this.data[i2];
                    i++;
                }
                i2++;
            }
        }
        int i3 = this.count - i;
        this.count = i;
        return i3;
    }

    public int replaceValue(String str, String str2) {
        int i = 0;
        if (str == null) {
            while (i < this.count) {
                if (this.data[i] == null) {
                    this.data[i] = str2;
                    return i;
                }
                i++;
            }
        } else {
            while (i < this.count) {
                if (str.equals(this.data[i])) {
                    this.data[i] = str2;
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    public int replaceValues(String str, String str2) {
        int i;
        int i2 = 0;
        if (str == null) {
            i = 0;
            while (i2 < this.count) {
                if (this.data[i2] == null) {
                    this.data[i2] = str2;
                    i++;
                }
                i2++;
            }
        } else {
            i = 0;
            while (i2 < this.count) {
                if (str.equals(this.data[i2])) {
                    this.data[i2] = str2;
                    i++;
                }
                i2++;
            }
        }
        return i;
    }

    public void resize(int i) {
        if (i > this.data.length) {
            String[] strArr = new String[i];
            System.arraycopy(this.data, 0, strArr, 0, this.count);
            this.data = strArr;
        } else if (i > this.count) {
            Arrays.fill(this.data, this.count, i, 0);
        }
        this.count = i;
    }

    public void reverse() {
        int i = this.count - 1;
        for (int i2 = 0; i2 < this.count / 2; i2++) {
            String str = this.data[i2];
            this.data[i2] = this.data[i];
            this.data[i] = str;
            i--;
        }
    }

    public void set(int i, String str) {
        if (i >= this.count) {
            this.data = PApplet.expand(this.data, i + 1);
            for (int i2 = this.count; i2 < i; i2++) {
                this.data[i2] = null;
            }
            this.count = i + 1;
        }
        this.data[i] = str;
    }

    public void shuffle() {
        Random random = new Random();
        int i = this.count;
        while (i > 1) {
            int nextInt = random.nextInt(i);
            i--;
            String str = this.data[i];
            this.data[i] = this.data[nextInt];
            this.data[nextInt] = str;
        }
    }

    public void shuffle(PApplet pApplet) {
        int i = this.count;
        while (i > 1) {
            int random = (int) pApplet.random((float) i);
            i--;
            String str = this.data[i];
            this.data[i] = this.data[random];
            this.data[random] = str;
        }
    }

    public int size() {
        return this.count;
    }

    public void sort() {
        sortImpl(false);
    }

    public void sortReverse() {
        sortImpl(true);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName() + " size=" + size() + " [ ");
        for (int i = 0; i < size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(i + ": \"" + this.data[i] + "\"");
        }
        sb.append(" ]");
        return sb.toString();
    }

    public void upper() {
        for (int i = 0; i < this.count; i++) {
            if (this.data[i] != null) {
                this.data[i] = this.data[i].toUpperCase();
            }
        }
    }

    public String[] values() {
        crop();
        return this.data;
    }
}
