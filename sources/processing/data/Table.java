package processing.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import processing.core.PApplet;
import processing.core.PConstants;

public class Table {
    public static final int CATEGORY = 5;
    public static final int DOUBLE = 4;
    public static final int FLOAT = 3;
    public static final int INT = 1;
    public static final int LONG = 2;
    public static final int STRING = 0;
    static final String[] loadExtensions = {"csv", "tsv", "ods", "bin"};
    static final String[] saveExtensions = {"csv", "tsv", "html", "bin"};
    HashMapBlows[] columnCategories;
    HashMap<String, Integer> columnIndices;
    String[] columnTitles;
    int[] columnTypes;
    protected Object[] columns;
    protected int expandIncrement;
    protected int missingCategory;
    protected double missingDouble;
    protected float missingFloat;
    protected int missingInt;
    protected long missingLong;
    protected String missingString;
    protected int rowCount;
    protected RowIterator rowIterator;

    class HashMapBlows {
        HashMap<String, Integer> dataToIndex = new HashMap<>();
        ArrayList<String> indexToData = new ArrayList<>();

        HashMapBlows() {
        }

        HashMapBlows(DataInputStream dataInputStream) throws IOException {
            read(dataInputStream);
        }

        /* access modifiers changed from: private */
        public void writeln(PrintWriter printWriter) throws IOException {
            Iterator<String> it = this.indexToData.iterator();
            while (it.hasNext()) {
                printWriter.println(it.next());
            }
            printWriter.flush();
            printWriter.close();
        }

        /* access modifiers changed from: package-private */
        public int index(String str) {
            Integer num = this.dataToIndex.get(str);
            if (num != null) {
                return num.intValue();
            }
            int size = this.dataToIndex.size();
            this.dataToIndex.put(str, Integer.valueOf(size));
            this.indexToData.add(str);
            return size;
        }

        /* access modifiers changed from: package-private */
        public String key(int i) {
            return this.indexToData.get(i);
        }

        /* access modifiers changed from: package-private */
        public void read(DataInputStream dataInputStream) throws IOException {
            int readInt = dataInputStream.readInt();
            this.dataToIndex = new HashMap<>(readInt);
            for (int i = 0; i < readInt; i++) {
                String readUTF = dataInputStream.readUTF();
                this.dataToIndex.put(readUTF, Integer.valueOf(i));
                this.indexToData.add(readUTF);
            }
        }

        /* access modifiers changed from: package-private */
        public int size() {
            return this.dataToIndex.size();
        }

        /* access modifiers changed from: package-private */
        public void write(DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.writeInt(size());
            Iterator<String> it = this.indexToData.iterator();
            while (it.hasNext()) {
                dataOutputStream.writeUTF(it.next());
            }
        }
    }

    static class RowIndexIterator implements Iterator<TableRow> {
        int index = -1;
        int[] indices;
        RowPointer rp;
        Table table;

        public RowIndexIterator(Table table2, int[] iArr) {
            this.table = table2;
            this.indices = iArr;
            this.rp = new RowPointer(table2, -1);
        }

        public boolean hasNext() {
            return this.index + 1 < this.indices.length;
        }

        public TableRow next() {
            RowPointer rowPointer = this.rp;
            int[] iArr = this.indices;
            int i = this.index + 1;
            this.index = i;
            rowPointer.setRow(iArr[i]);
            return this.rp;
        }

        public void remove() {
            this.table.removeRow(this.indices[this.index]);
        }

        public void reset() {
            this.index = -1;
        }
    }

    static class RowIterator implements Iterator<TableRow> {
        int row = -1;
        RowPointer rp;
        Table table;

        public RowIterator(Table table2) {
            this.table = table2;
            this.rp = new RowPointer(table2, this.row);
        }

        public boolean hasNext() {
            return this.row + 1 < this.table.getRowCount();
        }

        public TableRow next() {
            RowPointer rowPointer = this.rp;
            int i = this.row + 1;
            this.row = i;
            rowPointer.setRow(i);
            return this.rp;
        }

        public void remove() {
            this.table.removeRow(this.row);
        }

        public void reset() {
            this.row = -1;
        }
    }

    static class RowPointer implements TableRow {
        int row;
        Table table;

        public RowPointer(Table table2, int i) {
            this.table = table2;
            this.row = i;
        }

        public int getColumnCount() {
            return this.table.getColumnCount();
        }

        public int getColumnType(int i) {
            return this.table.getColumnType(i);
        }

        public int getColumnType(String str) {
            return this.table.getColumnType(str);
        }

        public int[] getColumnTypes() {
            return this.table.getColumnTypes();
        }

        public double getDouble(int i) {
            return this.table.getDouble(this.row, i);
        }

        public double getDouble(String str) {
            return this.table.getDouble(this.row, str);
        }

        public float getFloat(int i) {
            return this.table.getFloat(this.row, i);
        }

        public float getFloat(String str) {
            return this.table.getFloat(this.row, str);
        }

        public int getInt(int i) {
            return this.table.getInt(this.row, i);
        }

        public int getInt(String str) {
            return this.table.getInt(this.row, str);
        }

        public long getLong(int i) {
            return this.table.getLong(this.row, i);
        }

        public long getLong(String str) {
            return this.table.getLong(this.row, str);
        }

        public String getString(int i) {
            return this.table.getString(this.row, i);
        }

        public String getString(String str) {
            return this.table.getString(this.row, str);
        }

        public void setDouble(int i, double d) {
            this.table.setDouble(this.row, i, d);
        }

        public void setDouble(String str, double d) {
            this.table.setDouble(this.row, str, d);
        }

        public void setFloat(int i, float f) {
            this.table.setFloat(this.row, i, f);
        }

        public void setFloat(String str, float f) {
            this.table.setFloat(this.row, str, f);
        }

        public void setInt(int i, int i2) {
            this.table.setInt(this.row, i, i2);
        }

        public void setInt(String str, int i) {
            this.table.setInt(this.row, str, i);
        }

        public void setLong(int i, long j) {
            this.table.setLong(this.row, i, j);
        }

        public void setLong(String str, long j) {
            this.table.setLong(this.row, str, j);
        }

        public void setRow(int i) {
            this.row = i;
        }

        public void setString(int i, String str) {
            this.table.setString(this.row, i, str);
        }

        public void setString(String str, String str2) {
            this.table.setString(this.row, str, str2);
        }
    }

    public Table() {
        this.missingString = null;
        this.missingInt = 0;
        this.missingLong = 0;
        this.missingFloat = Float.NaN;
        this.missingDouble = Double.NaN;
        this.missingCategory = -1;
        init();
    }

    public Table(File file) throws IOException {
        this(file, (String) null);
    }

    public Table(File file, String str) throws IOException {
        this.missingString = null;
        this.missingInt = 0;
        this.missingLong = 0;
        this.missingFloat = Float.NaN;
        this.missingDouble = Double.NaN;
        this.missingCategory = -1;
        init();
        parse(PApplet.createInput(file), extensionOptions(true, file.getName(), str));
    }

    public Table(InputStream inputStream) throws IOException {
        this(inputStream, (String) null);
    }

    public Table(InputStream inputStream, String str) throws IOException {
        this.missingString = null;
        this.missingInt = 0;
        this.missingLong = 0;
        this.missingFloat = Float.NaN;
        this.missingDouble = Double.NaN;
        this.missingCategory = -1;
        init();
        parse(inputStream, str);
    }

    public Table(Iterable<TableRow> iterable) {
        this.missingString = null;
        this.missingInt = 0;
        this.missingLong = 0;
        this.missingFloat = Float.NaN;
        this.missingDouble = Double.NaN;
        this.missingCategory = -1;
        for (TableRow next : iterable) {
            setColumnTypes(next.getColumnTypes());
            addRow(next);
        }
    }

    public Table(ResultSet resultSet) {
        this.missingString = null;
        this.missingInt = 0;
        this.missingLong = 0;
        this.missingFloat = Float.NaN;
        this.missingDouble = Double.NaN;
        this.missingCategory = -1;
        init();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            setColumnCount(columnCount);
            for (int i = 0; i < columnCount; i++) {
                setColumnTitle(i, metaData.getColumnName(i + 1));
                switch (metaData.getColumnType(i + 1)) {
                    case PConstants.ENABLE_OPTIMIZED_STROKE:
                    case 4:
                    case 5:
                        setColumnType(i, 1);
                        break;
                    case PConstants.ENABLE_DEPTH_MASK:
                        setColumnType(i, 2);
                        break;
                    case 3:
                    case 7:
                    case 8:
                        setColumnType(i, 4);
                        break;
                    case 6:
                        setColumnType(i, 3);
                        break;
                }
            }
            int i2 = 0;
            while (resultSet.next()) {
                for (int i3 = 0; i3 < columnCount; i3++) {
                    switch (this.columnTypes[i3]) {
                        case 0:
                            setString(i2, i3, resultSet.getString(i3 + 1));
                            break;
                        case 1:
                            setInt(i2, i3, resultSet.getInt(i3 + 1));
                            break;
                        case 2:
                            setLong(i2, i3, resultSet.getLong(i3 + 1));
                            break;
                        case 3:
                            setFloat(i2, i3, resultSet.getFloat(i3 + 1));
                            break;
                        case 4:
                            setDouble(i2, i3, resultSet.getDouble(i3 + 1));
                            break;
                        default:
                            throw new IllegalArgumentException("column type " + this.columnTypes[i3] + " not supported.");
                    }
                }
                i2++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extensionOptions(boolean z, String str, String str2) {
        String checkExtension = PApplet.checkExtension(str);
        if (checkExtension == null) {
            return str2;
        }
        for (String equals : z ? loadExtensions : saveExtensions) {
            if (checkExtension.equals(equals)) {
                return str2 == null ? checkExtension : checkExtension + "," + str2;
            }
        }
        return str2;
    }

    protected static int nextComma(char[] cArr, int i) {
        boolean z = false;
        while (i < cArr.length) {
            if (!z && cArr[i] == ',') {
                return i;
            }
            if (cArr[i] == '\"') {
                z = !z;
            }
            i++;
        }
        return cArr.length;
    }

    private void odsAppendNotNull(XML xml, StringBuffer stringBuffer) {
        String content = xml.getContent();
        if (content != null) {
            stringBuffer.append(content);
        }
    }

    private InputStream odsFindContentXML(InputStream inputStream) {
        ZipEntry nextEntry;
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        do {
            try {
                nextEntry = zipInputStream.getNextEntry();
                if (nextEntry == null) {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!nextEntry.getName().equals("content.xml"));
        return zipInputStream;
    }

    private void odsParseSheet(XML xml) {
        XML[] children = xml.getChildren("table:table-row");
        int length = children.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            XML xml2 = children[i];
            int i3 = xml2.getInt("table:number-rows-repeated", 1);
            boolean z = false;
            XML[] children2 = xml2.getChildren();
            int i4 = 0;
            int length2 = children2.length;
            int i5 = 0;
            while (i5 < length2) {
                XML xml3 = children2[i5];
                int i6 = xml3.getInt("table:number-columns-repeated", 1);
                String string = xml3.getString("office:value");
                if (string == null && xml3.getChildCount() != 0) {
                    XML[] children3 = xml3.getChildren("text:p");
                    if (children3.length != 1) {
                        for (XML xml4 : children3) {
                            System.err.println(xml4.toString());
                        }
                        throw new RuntimeException("found more than one text:p element");
                    }
                    XML xml5 = children3[0];
                    string = xml5.getContent();
                    if (string == null) {
                        XML[] children4 = xml5.getChildren();
                        StringBuffer stringBuffer = new StringBuffer();
                        int length3 = children4.length;
                        int i7 = 0;
                        while (true) {
                            int i8 = i7;
                            if (i8 >= length3) {
                                break;
                            }
                            XML xml6 = children4[i8];
                            String name = xml6.getName();
                            if (name == null) {
                                odsAppendNotNull(xml6, stringBuffer);
                            } else if (name.equals("text:s")) {
                                int i9 = xml6.getInt("text:c", 1);
                                for (int i10 = 0; i10 < i9; i10++) {
                                    stringBuffer.append(' ');
                                }
                            } else if (name.equals("text:span")) {
                                odsAppendNotNull(xml6, stringBuffer);
                            } else if (name.equals("text:a")) {
                                stringBuffer.append(xml6.getString("xlink:href"));
                            } else {
                                odsAppendNotNull(xml6, stringBuffer);
                                System.err.println(getClass().getName() + ": don't understand: " + xml6);
                            }
                            i7 = i8 + 1;
                        }
                        string = stringBuffer.toString();
                    }
                }
                boolean z2 = z;
                int i11 = i4;
                for (int i12 = 0; i12 < i6; i12++) {
                    if (string != null) {
                        setString(i2, i11, string);
                    }
                    i11++;
                    if (string != null) {
                        z2 = true;
                    }
                }
                i5++;
                i4 = i11;
                z = z2;
            }
            if (z && i3 > 1) {
                String[] stringRow = getStringRow(i2);
                for (int i13 = 1; i13 < i3; i13++) {
                    addRow((Object[]) stringRow);
                }
            }
            i++;
            i2 += i3;
        }
    }

    protected static String[] splitLineCSV(String str) {
        int i;
        int i2;
        int i3 = 0;
        char[] charArray = str.toCharArray();
        boolean z = false;
        int i4 = 1;
        for (int i5 = 0; i5 < charArray.length; i5++) {
            if (!z && charArray[i5] == ',') {
                i4++;
            } else if (charArray[i5] == '\"') {
                z = !z;
            }
        }
        String[] strArr = new String[i4];
        int i6 = 0;
        while (i3 < charArray.length) {
            int nextComma = nextComma(charArray, i3);
            int i7 = nextComma + 1;
            if (charArray[i3] == '\"' && charArray[nextComma - 1] == '\"') {
                int i8 = i3 + 1;
                i2 = nextComma - 1;
                i = i8;
            } else {
                int i9 = nextComma;
                i = i3;
                i2 = i9;
            }
            int i10 = i;
            int i11 = i;
            while (i11 < i2) {
                if (charArray[i11] == '\"') {
                    i11++;
                }
                if (i11 != i10) {
                    charArray[i10] = charArray[i11];
                }
                i10++;
                i11++;
            }
            strArr[i6] = new String(charArray, i, i10 - i);
            i6++;
            i3 = i7;
        }
        while (i6 < strArr.length) {
            strArr[i6] = "";
            i6++;
        }
        return strArr;
    }

    public void addColumn() {
        addColumn((String) null, 0);
    }

    public void addColumn(String str) {
        addColumn(str, 0);
    }

    public void addColumn(String str, int i) {
        insertColumn(this.columns.length, str, i);
    }

    public TableRow addRow() {
        setRowCount(this.rowCount + 1);
        return new RowPointer(this, this.rowCount - 1);
    }

    public TableRow addRow(TableRow tableRow) {
        int i = this.rowCount;
        ensureBounds(i, tableRow.getColumnCount() - 1);
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            switch (this.columnTypes[i2]) {
                case 0:
                    setString(i, i2, tableRow.getString(i2));
                    break;
                case 1:
                case 5:
                    setInt(i, i2, tableRow.getInt(i2));
                    break;
                case 2:
                    setLong(i, i2, tableRow.getLong(i2));
                    break;
                case 3:
                    setFloat(i, i2, tableRow.getFloat(i2));
                    break;
                case 4:
                    setDouble(i, i2, tableRow.getDouble(i2));
                    break;
                default:
                    throw new RuntimeException("no types");
            }
        }
        return new RowPointer(this, i);
    }

    public TableRow addRow(Object[] objArr) {
        setRow(getRowCount(), objArr);
        return new RowPointer(this, this.rowCount - 1);
    }

    /* access modifiers changed from: protected */
    public void checkBounds(int i, int i2) {
        checkRow(i);
        checkColumn(i2);
    }

    /* access modifiers changed from: protected */
    public void checkColumn(int i) {
        if (i < 0 || i >= this.columns.length) {
            throw new ArrayIndexOutOfBoundsException("Column " + i + " does not exist.");
        }
    }

    public int checkColumnIndex(String str) {
        int columnIndex = getColumnIndex(str, false);
        if (columnIndex != -1) {
            return columnIndex;
        }
        addColumn(str);
        return getColumnCount() - 1;
    }

    /* access modifiers changed from: protected */
    public void checkRow(int i) {
        if (i < 0 || i >= this.rowCount) {
            throw new ArrayIndexOutOfBoundsException("Row " + i + " does not exist.");
        }
    }

    public void clearRows() {
        setRowCount(0);
    }

    /* access modifiers changed from: protected */
    public void convertBasic(BufferedReader bufferedReader, boolean z, File file) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file), 16384));
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(getColumnCount());
        if (this.columnTitles != null) {
            dataOutputStream.writeBoolean(true);
            for (String writeUTF : this.columnTitles) {
                dataOutputStream.writeUTF(writeUTF);
            }
        } else {
            dataOutputStream.writeBoolean(false);
        }
        for (int writeInt : this.columnTypes) {
            dataOutputStream.writeInt(writeInt);
        }
        int i = 0;
        int i2 = -1;
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                break;
            }
            convertRow(dataOutputStream, z ? PApplet.split(readLine, 9) : splitLineCSV(readLine));
            int i3 = i + 1;
            if (i3 % 10000 != 0 || i3 >= this.rowCount) {
                i = i3;
            } else {
                int i4 = (i3 * 100) / this.rowCount;
                if (i4 != i2) {
                    System.out.println(i4 + "%");
                } else {
                    i4 = i2;
                }
                i2 = i4;
                i = i3;
            }
        }
        int i5 = 0;
        for (HashMapBlows hashMapBlows : this.columnCategories) {
            if (hashMapBlows == null) {
                dataOutputStream.writeInt(0);
            } else {
                hashMapBlows.write(dataOutputStream);
                hashMapBlows.writeln(PApplet.createWriter(new File(this.columnTitles[i5] + ".categories")));
            }
            i5++;
        }
        dataOutputStream.flush();
        dataOutputStream.close();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.writeInt(this.rowCount);
        randomAccessFile.close();
    }

    /* access modifiers changed from: protected */
    public void convertRow(DataOutputStream dataOutputStream, String[] strArr) throws IOException {
        if (strArr.length > getColumnCount()) {
            throw new IllegalArgumentException("Row with too many columns: " + PApplet.join(strArr, ","));
        }
        for (int i = 0; i < strArr.length; i++) {
            switch (this.columnTypes[i]) {
                case 0:
                    dataOutputStream.writeUTF(strArr[i]);
                    break;
                case 1:
                    dataOutputStream.writeInt(PApplet.parseInt(strArr[i], this.missingInt));
                    break;
                case 2:
                    try {
                        dataOutputStream.writeLong(Long.parseLong(strArr[i]));
                        break;
                    } catch (NumberFormatException e) {
                        dataOutputStream.writeLong(this.missingLong);
                        break;
                    }
                case 3:
                    dataOutputStream.writeFloat(PApplet.parseFloat(strArr[i], this.missingFloat));
                    break;
                case 4:
                    try {
                        dataOutputStream.writeDouble(Double.parseDouble(strArr[i]));
                        break;
                    } catch (NumberFormatException e2) {
                        dataOutputStream.writeDouble(this.missingDouble);
                        break;
                    }
                case 5:
                    dataOutputStream.writeInt(this.columnCategories[i].index(strArr[i]));
                    break;
            }
        }
        for (int length = strArr.length; length < getColumnCount(); length++) {
            switch (this.columnTypes[length]) {
                case 0:
                    dataOutputStream.writeUTF("");
                    break;
                case 1:
                    dataOutputStream.writeInt(this.missingInt);
                    break;
                case 2:
                    dataOutputStream.writeLong(this.missingLong);
                    break;
                case 3:
                    dataOutputStream.writeFloat(this.missingFloat);
                    break;
                case 4:
                    dataOutputStream.writeDouble(this.missingDouble);
                    break;
                case 5:
                    dataOutputStream.writeInt(this.missingCategory);
                    break;
            }
        }
    }

    /* access modifiers changed from: protected */
    public Table createSubset(int[] iArr) {
        Table table = new Table();
        table.setColumnTitles(this.columnTitles);
        table.columnTypes = this.columnTypes;
        table.setRowCount(iArr.length);
        for (int i = 0; i < iArr.length; i++) {
            int i2 = iArr[i];
            for (int i3 = 0; i3 < this.columns.length; i3++) {
                switch (this.columnTypes[i3]) {
                    case 0:
                        table.setString(i, i3, getString(i2, i3));
                        break;
                    case 1:
                        table.setInt(i, i3, getInt(i2, i3));
                        break;
                    case 2:
                        table.setLong(i, i3, getLong(i2, i3));
                        break;
                    case 3:
                        table.setFloat(i, i3, getFloat(i2, i3));
                        break;
                    case 4:
                        table.setDouble(i, i3, getDouble(i2, i3));
                        break;
                }
            }
        }
        return table;
    }

    /* access modifiers changed from: protected */
    public void ensureBounds(int i, int i2) {
        ensureRow(i);
        ensureColumn(i2);
    }

    /* access modifiers changed from: protected */
    public void ensureColumn(int i) {
        if (i >= this.columns.length) {
            setColumnCount(i + 1);
        }
    }

    /* access modifiers changed from: protected */
    public void ensureRow(int i) {
        if (i >= this.rowCount) {
            setRowCount(i + 1);
        }
    }

    public TableRow findRow(String str, int i) {
        int findRowIndex = findRowIndex(str, i);
        if (findRowIndex == -1) {
            return null;
        }
        return new RowPointer(this, findRowIndex);
    }

    public TableRow findRow(String str, String str2) {
        return findRow(str, getColumnIndex(str2));
    }

    public int findRowIndex(String str, int i) {
        int i2 = 0;
        checkColumn(i);
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            if (str == null) {
                while (i2 < this.rowCount) {
                    if (strArr[i2] == null) {
                        return i2;
                    }
                    i2++;
                }
            } else {
                while (i2 < this.rowCount) {
                    if (strArr[i2] != null && strArr[i2].equals(str)) {
                        return i2;
                    }
                    i2++;
                }
            }
        } else {
            while (i2 < this.rowCount) {
                String string = getString(i2, i);
                if (string == null) {
                    if (str == null) {
                        return i2;
                    }
                } else if (string.equals(str)) {
                    return i2;
                }
                i2++;
            }
        }
        return -1;
    }

    public int findRowIndex(String str, String str2) {
        return findRowIndex(str, getColumnIndex(str2));
    }

    public int[] findRowIndices(String str, int i) {
        int i2;
        int[] iArr = new int[this.rowCount];
        checkColumn(i);
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            if (str == null) {
                i2 = 0;
                for (int i3 = 0; i3 < this.rowCount; i3++) {
                    if (strArr[i3] == null) {
                        iArr[i2] = i3;
                        i2++;
                    }
                }
            } else {
                i2 = 0;
                for (int i4 = 0; i4 < this.rowCount; i4++) {
                    if (strArr[i4] != null && strArr[i4].equals(str)) {
                        iArr[i2] = i4;
                        i2++;
                    }
                }
            }
        } else {
            int i5 = 0;
            for (int i6 = 0; i6 < this.rowCount; i6++) {
                String string = getString(i6, i);
                if (string == null) {
                    if (str == null) {
                        iArr[i5] = i6;
                        i5++;
                    }
                } else if (string.equals(str)) {
                    iArr[i5] = i6;
                    i5++;
                }
            }
            i2 = i5;
        }
        return PApplet.subset(iArr, 0, i2);
    }

    public int[] findRowIndices(String str, String str2) {
        return findRowIndices(str, getColumnIndex(str2));
    }

    public Iterator<TableRow> findRowIterator(String str, int i) {
        return new RowIndexIterator(this, findRowIndices(str, i));
    }

    public Iterator<TableRow> findRowIterator(String str, String str2) {
        return findRowIterator(str, getColumnIndex(str2));
    }

    public Iterable<TableRow> findRows(final String str, final int i) {
        return new Iterable<TableRow>() {
            public Iterator<TableRow> iterator() {
                return Table.this.findRowIterator(str, i);
            }
        };
    }

    public Iterable<TableRow> findRows(String str, String str2) {
        return findRows(str, getColumnIndex(str2));
    }

    public int getColumnCount() {
        return this.columns.length;
    }

    public int getColumnIndex(String str) {
        return getColumnIndex(str, true);
    }

    /* access modifiers changed from: protected */
    public int getColumnIndex(String str, boolean z) {
        if (this.columnTitles != null) {
            if (this.columnIndices == null) {
                this.columnIndices = new HashMap<>();
                for (int i = 0; i < this.columns.length; i++) {
                    this.columnIndices.put(this.columnTitles[i], Integer.valueOf(i));
                }
            }
            Integer num = this.columnIndices.get(str);
            if (num != null) {
                return num.intValue();
            }
            if (!z) {
                return -1;
            }
            throw new IllegalArgumentException("This table has no column named '" + str + "'");
        } else if (!z) {
            return -1;
        } else {
            throw new IllegalArgumentException("This table has no header, so no column titles are set.");
        }
    }

    public String getColumnTitle(int i) {
        if (this.columnTitles == null) {
            return null;
        }
        return this.columnTitles[i];
    }

    public String[] getColumnTitles() {
        return this.columnTitles;
    }

    public int getColumnType(int i) {
        return this.columnTypes[i];
    }

    public int getColumnType(String str) {
        return getColumnType(getColumnIndex(str));
    }

    public int[] getColumnTypes() {
        return this.columnTypes;
    }

    public double getDouble(int i, int i2) {
        checkBounds(i, i2);
        if (this.columnTypes[i2] == 4) {
            return ((double[]) this.columns[i2])[i];
        }
        String string = getString(i, i2);
        if (string == null || string.equals(this.missingString)) {
            return this.missingDouble;
        }
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return this.missingDouble;
        }
    }

    public double getDouble(int i, String str) {
        return getDouble(i, getColumnIndex(str));
    }

    public double[] getDoubleColumn(int i) {
        double[] dArr = new double[this.rowCount];
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            dArr[i2] = getDouble(i2, i);
        }
        return dArr;
    }

    public double[] getDoubleColumn(String str) {
        int columnIndex = getColumnIndex(str);
        if (columnIndex == -1) {
            return null;
        }
        return getDoubleColumn(columnIndex);
    }

    public double[] getDoubleRow(int i) {
        double[] dArr = new double[this.columns.length];
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            dArr[i2] = getDouble(i, i2);
        }
        return dArr;
    }

    public float getFloat(int i, int i2) {
        checkBounds(i, i2);
        if (this.columnTypes[i2] == 3) {
            return ((float[]) this.columns[i2])[i];
        }
        String string = getString(i, i2);
        return (string == null || string.equals(this.missingString)) ? this.missingFloat : PApplet.parseFloat(string, this.missingFloat);
    }

    public float getFloat(int i, String str) {
        return getFloat(i, getColumnIndex(str));
    }

    public float[] getFloatColumn(int i) {
        float[] fArr = new float[this.rowCount];
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            fArr[i2] = getFloat(i2, i);
        }
        return fArr;
    }

    public float[] getFloatColumn(String str) {
        int columnIndex = getColumnIndex(str);
        if (columnIndex == -1) {
            return null;
        }
        return getFloatColumn(columnIndex);
    }

    public FloatDict getFloatDict(int i, int i2) {
        return new FloatDict(getStringColumn(i), getFloatColumn(i2));
    }

    public FloatDict getFloatDict(String str, String str2) {
        return new FloatDict(getStringColumn(str), getFloatColumn(str2));
    }

    public FloatList getFloatList(int i) {
        return new FloatList(getFloatColumn(i));
    }

    public FloatList getFloatList(String str) {
        return new FloatList(getFloatColumn(str));
    }

    public float[] getFloatRow(int i) {
        float[] fArr = new float[this.columns.length];
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            fArr[i2] = getFloat(i, i2);
        }
        return fArr;
    }

    public int getInt(int i, int i2) {
        checkBounds(i, i2);
        if (this.columnTypes[i2] == 1 || this.columnTypes[i2] == 5) {
            return ((int[]) this.columns[i2])[i];
        }
        String string = getString(i, i2);
        return (string == null || string.equals(this.missingString)) ? this.missingInt : PApplet.parseInt(string, this.missingInt);
    }

    public int getInt(int i, String str) {
        return getInt(i, getColumnIndex(str));
    }

    public int[] getIntColumn(int i) {
        int[] iArr = new int[this.rowCount];
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            iArr[i2] = getInt(i2, i);
        }
        return iArr;
    }

    public int[] getIntColumn(String str) {
        int columnIndex = getColumnIndex(str);
        if (columnIndex == -1) {
            return null;
        }
        return getIntColumn(columnIndex);
    }

    public IntDict getIntDict(int i, int i2) {
        return new IntDict(getStringColumn(i), getIntColumn(i2));
    }

    public IntDict getIntDict(String str, String str2) {
        return new IntDict(getStringColumn(str), getIntColumn(str2));
    }

    public IntList getIntList(int i) {
        return new IntList(getIntColumn(i));
    }

    public IntList getIntList(String str) {
        return new IntList(getIntColumn(str));
    }

    public int[] getIntRow(int i) {
        int[] iArr = new int[this.columns.length];
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            iArr[i2] = getInt(i, i2);
        }
        return iArr;
    }

    public long getLong(int i, int i2) {
        checkBounds(i, i2);
        if (this.columnTypes[i2] == 2) {
            return ((long[]) this.columns[i2])[i];
        }
        String string = getString(i, i2);
        if (string == null || string.equals(this.missingString)) {
            return this.missingLong;
        }
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return this.missingLong;
        }
    }

    public long getLong(int i, String str) {
        return getLong(i, getColumnIndex(str));
    }

    public long[] getLongColumn(int i) {
        long[] jArr = new long[this.rowCount];
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            jArr[i2] = getLong(i2, i);
        }
        return jArr;
    }

    public long[] getLongColumn(String str) {
        int columnIndex = getColumnIndex(str);
        if (columnIndex == -1) {
            return null;
        }
        return getLongColumn(columnIndex);
    }

    public long[] getLongRow(int i) {
        long[] jArr = new long[this.columns.length];
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            jArr[i2] = getLong(i, i2);
        }
        return jArr;
    }

    /* access modifiers changed from: protected */
    public float getMaxFloat() {
        boolean z;
        float f;
        float f2 = -3.4028235E38f;
        boolean z2 = false;
        for (int i = 0; i < getRowCount(); i++) {
            int i2 = 0;
            while (i2 < getColumnCount()) {
                float f3 = getFloat(i, i2);
                if (!Float.isNaN(f3)) {
                    if (!z2) {
                        float f4 = f3;
                        z = true;
                        f = f4;
                    } else if (f3 > f2) {
                        float f5 = f3;
                        z = z2;
                        f = f5;
                    }
                    i2++;
                    f2 = f;
                    z2 = z;
                }
                z = z2;
                f = f2;
                i2++;
                f2 = f;
                z2 = z;
            }
        }
        return z2 ? f2 : this.missingFloat;
    }

    public IntDict getOrder(int i) {
        return new StringList(getStringColumn(i)).getOrder();
    }

    public IntDict getOrder(String str) {
        return getOrder(getColumnIndex(str));
    }

    public TableRow getRow(int i) {
        return new RowPointer(this, i);
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public String getString(int i, int i2) {
        checkBounds(i, i2);
        if (this.columnTypes[i2] == 0) {
            return ((String[]) this.columns[i2])[i];
        }
        if (this.columnTypes[i2] != 5) {
            return String.valueOf(Array.get(this.columns[i2], i));
        }
        int i3 = getInt(i, i2);
        return i3 == this.missingCategory ? this.missingString : this.columnCategories[i2].key(i3);
    }

    public String getString(int i, String str) {
        return getString(i, getColumnIndex(str));
    }

    public String[] getStringColumn(int i) {
        String[] strArr = new String[this.rowCount];
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            strArr[i2] = getString(i2, i);
        }
        return strArr;
    }

    public String[] getStringColumn(String str) {
        int columnIndex = getColumnIndex(str);
        if (columnIndex == -1) {
            return null;
        }
        return getStringColumn(columnIndex);
    }

    public StringDict getStringDict(int i, int i2) {
        return new StringDict(getStringColumn(i), getStringColumn(i2));
    }

    public StringDict getStringDict(String str, String str2) {
        return new StringDict(getStringColumn(str), getStringColumn(str2));
    }

    public StringList getStringList(int i) {
        return new StringList(getStringColumn(i));
    }

    public StringList getStringList(String str) {
        return new StringList(getStringColumn(str));
    }

    public String[] getStringRow(int i) {
        String[] strArr = new String[this.columns.length];
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            strArr[i2] = getString(i, i2);
        }
        return strArr;
    }

    public IntDict getTally(int i) {
        return new StringList(getStringColumn(i)).getTally();
    }

    public IntDict getTally(String str) {
        return getTally(getColumnIndex(str));
    }

    public String[] getUnique(int i) {
        return new StringList(getStringColumn(i)).getUnique();
    }

    public String[] getUnique(String str) {
        return getUnique(getColumnIndex(str));
    }

    public boolean hasColumnTitles() {
        return this.columnTitles != null;
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.columns = new Object[0];
        this.columnTypes = new int[0];
        this.columnCategories = new HashMapBlows[0];
    }

    public void insertColumn(int i) {
        insertColumn(i, (String) null, 0);
    }

    public void insertColumn(int i, String str) {
        insertColumn(i, str, 0);
    }

    public void insertColumn(int i, String str, int i2) {
        if (str != null && this.columnTitles == null) {
            this.columnTitles = new String[this.columns.length];
        }
        if (this.columnTitles != null) {
            this.columnTitles = PApplet.splice(this.columnTitles, str, i);
            this.columnIndices = null;
        }
        this.columnTypes = PApplet.splice(this.columnTypes, i2, i);
        HashMapBlows[] hashMapBlowsArr = new HashMapBlows[(this.columns.length + 1)];
        for (int i3 = 0; i3 < i; i3++) {
            hashMapBlowsArr[i3] = this.columnCategories[i3];
        }
        hashMapBlowsArr[i] = new HashMapBlows();
        for (int i4 = i; i4 < this.columns.length; i4++) {
            hashMapBlowsArr[i4 + 1] = this.columnCategories[i4];
        }
        this.columnCategories = hashMapBlowsArr;
        Object[] objArr = new Object[(this.columns.length + 1)];
        System.arraycopy(this.columns, 0, objArr, 0, i);
        System.arraycopy(this.columns, i, objArr, i + 1, this.columns.length - i);
        this.columns = objArr;
        switch (i2) {
            case 0:
                this.columns[i] = new String[this.rowCount];
                return;
            case 1:
                this.columns[i] = new int[this.rowCount];
                return;
            case 2:
                this.columns[i] = new long[this.rowCount];
                return;
            case 3:
                this.columns[i] = new float[this.rowCount];
                return;
            case 4:
                this.columns[i] = new double[this.rowCount];
                return;
            case 5:
                this.columns[i] = new int[this.rowCount];
                return;
            default:
                return;
        }
    }

    public void insertRow(int i, Object[] objArr) {
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            switch (this.columnTypes[i2]) {
                case 0:
                    Object obj = new String[(this.rowCount + 1)];
                    System.arraycopy(this.columns[i2], 0, obj, 0, i);
                    System.arraycopy(this.columns[i2], i, obj, i + 1, this.rowCount - i);
                    this.columns[i2] = obj;
                    break;
                case 1:
                case 5:
                    Object obj2 = new int[(this.rowCount + 1)];
                    System.arraycopy(this.columns[i2], 0, obj2, 0, i);
                    System.arraycopy(this.columns[i2], i, obj2, i + 1, this.rowCount - i);
                    this.columns[i2] = obj2;
                    break;
                case 2:
                    Object obj3 = new long[(this.rowCount + 1)];
                    System.arraycopy(this.columns[i2], 0, obj3, 0, i);
                    System.arraycopy(this.columns[i2], i, obj3, i + 1, this.rowCount - i);
                    this.columns[i2] = obj3;
                    break;
                case 3:
                    Object obj4 = new float[(this.rowCount + 1)];
                    System.arraycopy(this.columns[i2], 0, obj4, 0, i);
                    System.arraycopy(this.columns[i2], i, obj4, i + 1, this.rowCount - i);
                    this.columns[i2] = obj4;
                    break;
                case 4:
                    Object obj5 = new double[(this.rowCount + 1)];
                    System.arraycopy(this.columns[i2], 0, obj5, 0, i);
                    System.arraycopy(this.columns[i2], i, obj5, i + 1, this.rowCount - i);
                    this.columns[i2] = obj5;
                    break;
            }
        }
        setRow(i, objArr);
        this.rowCount++;
    }

    public int lastRowIndex() {
        return getRowCount() - 1;
    }

    /* access modifiers changed from: protected */
    public void loadBinary(InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
        int readInt = dataInputStream.readInt();
        if (readInt != -1878545634) {
            throw new IOException("Not a compatible binary table (magic was " + PApplet.hex(readInt) + ")");
        }
        int readInt2 = dataInputStream.readInt();
        setRowCount(readInt2);
        int readInt3 = dataInputStream.readInt();
        setColumnCount(readInt3);
        if (dataInputStream.readBoolean()) {
            this.columnTitles = new String[getColumnCount()];
            for (int i = 0; i < readInt3; i++) {
                setColumnTitle(i, dataInputStream.readUTF());
            }
        }
        for (int i2 = 0; i2 < readInt3; i2++) {
            int readInt4 = dataInputStream.readInt();
            this.columnTypes[i2] = readInt4;
            switch (readInt4) {
                case 0:
                    this.columns[i2] = new String[readInt2];
                    break;
                case 1:
                    this.columns[i2] = new int[readInt2];
                    break;
                case 2:
                    this.columns[i2] = new long[readInt2];
                    break;
                case 3:
                    this.columns[i2] = new float[readInt2];
                    break;
                case 4:
                    this.columns[i2] = new double[readInt2];
                    break;
                case 5:
                    this.columns[i2] = new int[readInt2];
                    break;
                default:
                    throw new IllegalArgumentException(readInt4 + " is not a valid column type.");
            }
        }
        for (int i3 = 0; i3 < readInt3; i3++) {
            if (this.columnTypes[i3] == 5) {
                this.columnCategories[i3] = new HashMapBlows(dataInputStream);
            }
        }
        if (dataInputStream.readBoolean()) {
            this.missingString = dataInputStream.readUTF();
        } else {
            this.missingString = null;
        }
        this.missingInt = dataInputStream.readInt();
        this.missingLong = dataInputStream.readLong();
        this.missingFloat = dataInputStream.readFloat();
        this.missingDouble = dataInputStream.readDouble();
        this.missingCategory = dataInputStream.readInt();
        for (int i4 = 0; i4 < readInt2; i4++) {
            for (int i5 = 0; i5 < readInt3; i5++) {
                switch (this.columnTypes[i5]) {
                    case 0:
                        setString(i4, i5, dataInputStream.readBoolean() ? dataInputStream.readUTF() : null);
                        break;
                    case 1:
                        setInt(i4, i5, dataInputStream.readInt());
                        break;
                    case 2:
                        setLong(i4, i5, dataInputStream.readLong());
                        break;
                    case 3:
                        setFloat(i4, i5, dataInputStream.readFloat());
                        break;
                    case 4:
                        setDouble(i4, i5, dataInputStream.readDouble());
                        break;
                    case 5:
                        setInt(i4, i5, dataInputStream.readInt());
                        break;
                }
            }
        }
        dataInputStream.close();
    }

    public TableRow matchRow(String str, int i) {
        int matchRowIndex = matchRowIndex(str, i);
        if (matchRowIndex == -1) {
            return null;
        }
        return new RowPointer(this, matchRowIndex);
    }

    public TableRow matchRow(String str, String str2) {
        return matchRow(str, getColumnIndex(str2));
    }

    public int matchRowIndex(String str, int i) {
        int i2 = 0;
        checkColumn(i);
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            while (i2 < this.rowCount) {
                if (strArr[i2] != null && PApplet.match(strArr[i2], str) != null) {
                    return i2;
                }
                i2++;
            }
        } else {
            while (i2 < this.rowCount) {
                String string = getString(i2, i);
                if (string != null && PApplet.match(string, str) != null) {
                    return i2;
                }
                i2++;
            }
        }
        return -1;
    }

    public int matchRowIndex(String str, String str2) {
        return matchRowIndex(str, getColumnIndex(str2));
    }

    public int[] matchRowIndices(String str, int i) {
        int i2;
        int[] iArr = new int[this.rowCount];
        checkColumn(i);
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            i2 = 0;
            for (int i3 = 0; i3 < this.rowCount; i3++) {
                if (!(strArr[i3] == null || PApplet.match(strArr[i3], str) == null)) {
                    iArr[i2] = i3;
                    i2++;
                }
            }
        } else {
            int i4 = 0;
            for (int i5 = 0; i5 < this.rowCount; i5++) {
                String string = getString(i5, i);
                if (!(string == null || PApplet.match(string, str) == null)) {
                    iArr[i4] = i5;
                    i4++;
                }
            }
            i2 = i4;
        }
        return PApplet.subset(iArr, 0, i2);
    }

    public int[] matchRowIndices(String str, String str2) {
        return matchRowIndices(str, getColumnIndex(str2));
    }

    public Iterator<TableRow> matchRowIterator(String str, int i) {
        return new RowIndexIterator(this, matchRowIndices(str, i));
    }

    public Iterator<TableRow> matchRowIterator(String str, String str2) {
        return matchRowIterator(str, getColumnIndex(str2));
    }

    public Iterable<TableRow> matchRows(final String str, final int i) {
        return new Iterable<TableRow>() {
            public Iterator<TableRow> iterator() {
                return Table.this.matchRowIterator(str, i);
            }
        };
    }

    public Iterable<TableRow> matchRows(String str, String str2) {
        return matchRows(str, getColumnIndex(str2));
    }

    /* access modifiers changed from: protected */
    public void odsParse(InputStream inputStream, String str) {
        boolean z = false;
        try {
            for (XML xml : new XML(odsFindContentXML(inputStream)).getChildren("office:body/office:spreadsheet/table:table")) {
                if (str == null || str.equals(xml.getString("table:name"))) {
                    odsParseSheet(xml);
                    z = true;
                    if (str == null) {
                        break;
                    }
                }
            }
            if (z) {
                return;
            }
            if (str == null) {
                throw new RuntimeException("No worksheets found in the ODS file.");
            }
            throw new RuntimeException("No worksheet named " + str + " found in the ODS file.");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (ParserConfigurationException e3) {
            e3.printStackTrace();
        } catch (SAXException e4) {
            e4.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void parse(InputStream inputStream, String str) throws IOException {
        boolean z;
        boolean z2;
        String str2;
        boolean z3;
        String str3 = null;
        if (str != null) {
            z3 = false;
            str2 = null;
            z2 = false;
            z = false;
            for (String str4 : PApplet.splitTokens(str, " ,")) {
                if (str4.equals("tsv")) {
                    str2 = "tsv";
                } else if (str4.equals("csv")) {
                    str2 = "csv";
                } else if (str4.equals("ods")) {
                    str2 = "ods";
                } else if (str4.equals("newlines")) {
                    str2 = "csv";
                    z = true;
                } else if (str4.equals("bin")) {
                    str2 = "bin";
                    z3 = true;
                } else if (str4.equals("header")) {
                    z2 = true;
                } else if (str4.startsWith("worksheet=")) {
                    str3 = str4.substring("worksheet=".length());
                } else if (!str4.startsWith("dictionary=")) {
                    throw new IllegalArgumentException("'" + str4 + "' is not a valid option for loading a Table");
                }
            }
        } else {
            z3 = false;
            str2 = null;
            z2 = false;
            z = false;
        }
        if (str2 == null) {
            throw new IllegalArgumentException("No extension specified for this Table");
        } else if (z3) {
            loadBinary(inputStream);
        } else if (str2.equals("ods")) {
            odsParse(inputStream, str3);
        } else {
            BufferedReader createReader = PApplet.createReader(inputStream);
            if (z) {
                parseAwfulCSV(createReader, z2);
            } else if ("tsv".equals(str2)) {
                parseBasic(createReader, z2, true);
            } else if ("csv".equals(str2)) {
                parseBasic(createReader, z2, false);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void parseAwfulCSV(BufferedReader bufferedReader, boolean z) throws IOException {
        int i = 0;
        boolean z2 = false;
        int i2 = 0;
        char[] cArr = new char[100];
        int i3 = 0;
        while (true) {
            int read = bufferedReader.read();
            if (read == -1) {
                break;
            } else if (z2) {
                if (read == 34) {
                    bufferedReader.mark(1);
                    if (bufferedReader.read() == 34) {
                        char[] expand = i2 == cArr.length ? PApplet.expand(cArr) : cArr;
                        expand[i2] = '\"';
                        i2++;
                        cArr = expand;
                    } else {
                        bufferedReader.reset();
                        z2 = false;
                    }
                } else {
                    char[] expand2 = i2 == cArr.length ? PApplet.expand(cArr) : cArr;
                    expand2[i2] = (char) read;
                    i2++;
                    cArr = expand2;
                }
            } else if (read == 34) {
                z2 = true;
            } else if (read == 13 || read == 10) {
                if (read == 13) {
                    bufferedReader.mark(1);
                    if (bufferedReader.read() != 10) {
                        bufferedReader.reset();
                    }
                }
                setString(i, i3, new String(cArr, 0, i2));
                if (i == 0 && z) {
                    removeTitleRow();
                    z = false;
                }
                i++;
                i2 = 0;
                i3 = 0;
            } else if (read == 44) {
                setString(i, i3, new String(cArr, 0, i2));
                i3++;
                ensureColumn(i3);
                i2 = 0;
            } else {
                char[] expand3 = i2 == cArr.length ? PApplet.expand(cArr) : cArr;
                expand3[i2] = (char) read;
                i2++;
                cArr = expand3;
            }
        }
        if (i2 > 0) {
            setString(i, i3, new String(cArr, 0, i2));
        }
    }

    /* access modifiers changed from: protected */
    public void parseBasic(BufferedReader bufferedReader, boolean z, boolean z2) throws IOException {
        if (this.rowCount == 0) {
            setRowCount(10);
        }
        int i = 0;
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                if (i == getRowCount()) {
                    setRowCount(i << 1);
                }
                if (i != 0 || !z) {
                    setRow(i, z2 ? PApplet.split(readLine, 9) : splitLineCSV(readLine));
                    i++;
                } else {
                    setColumnTitles(z2 ? PApplet.split(readLine, 9) : splitLineCSV(readLine));
                    z = false;
                }
                if (i % 10000 == 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e2) {
                throw new RuntimeException("Error reading table on line " + i, e2);
            }
        }
        if (i != getRowCount()) {
            setRowCount(i);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:104:0x01a8 A[Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002c A[Catch:{ SecurityException -> 0x01b6, NoSuchMethodException -> 0x01b3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00be A[Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x001f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void parseInto(java.lang.Object r18, java.lang.String r19) {
        /*
            r17 = this;
            r3 = 0
            r2 = 0
            r5 = 0
            java.lang.Class r4 = r18.getClass()     // Catch:{ NoSuchFieldException -> 0x006b, SecurityException -> 0x007c }
            r0 = r19
            java.lang.reflect.Field r4 = r4.getDeclaredField(r0)     // Catch:{ NoSuchFieldException -> 0x006b, SecurityException -> 0x007c }
            java.lang.Class r5 = r4.getType()     // Catch:{ NoSuchFieldException -> 0x01c9, SecurityException -> 0x01b9 }
            boolean r6 = r5.isArray()     // Catch:{ NoSuchFieldException -> 0x01c9, SecurityException -> 0x01b9 }
            if (r6 != 0) goto L_0x005e
        L_0x0017:
            r5 = r2
        L_0x0018:
            java.lang.Class r9 = r3.getEnclosingClass()
            r6 = 0
            if (r9 != 0) goto L_0x008d
            r2 = 0
            java.lang.Class[] r2 = new java.lang.Class[r2]     // Catch:{ SecurityException -> 0x0098, NoSuchMethodException -> 0x00a3 }
            java.lang.reflect.Constructor r2 = r3.getDeclaredConstructor(r2)     // Catch:{ SecurityException -> 0x0098, NoSuchMethodException -> 0x00a3 }
        L_0x0026:
            boolean r6 = r2.isAccessible()     // Catch:{ SecurityException -> 0x01b6, NoSuchMethodException -> 0x01b3 }
            if (r6 != 0) goto L_0x0030
            r6 = 1
            r2.setAccessible(r6)     // Catch:{ SecurityException -> 0x01b6, NoSuchMethodException -> 0x01b3 }
        L_0x0030:
            r6 = r2
        L_0x0031:
            java.lang.reflect.Field[] r3 = r3.getDeclaredFields()
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            int r7 = r3.length
            r2 = 0
        L_0x003c:
            if (r2 >= r7) goto L_0x00ae
            r8 = r3[r2]
            java.lang.String r11 = r8.getName()
            r12 = 0
            r0 = r17
            int r11 = r0.getColumnIndex(r11, r12)
            r12 = -1
            if (r11 == r12) goto L_0x005b
            boolean r11 = r8.isAccessible()
            if (r11 != 0) goto L_0x0058
            r11 = 1
            r8.setAccessible(r11)
        L_0x0058:
            r10.add(r8)
        L_0x005b:
            int r2 = r2 + 1
            goto L_0x003c
        L_0x005e:
            java.lang.Class r3 = r5.getComponentType()     // Catch:{ NoSuchFieldException -> 0x01c9, SecurityException -> 0x01b9 }
            int r5 = r17.getRowCount()     // Catch:{ NoSuchFieldException -> 0x01d1, SecurityException -> 0x01c1 }
            java.lang.Object r2 = java.lang.reflect.Array.newInstance(r3, r5)     // Catch:{ NoSuchFieldException -> 0x01d1, SecurityException -> 0x01c1 }
            goto L_0x0017
        L_0x006b:
            r4 = move-exception
            r16 = r4
            r4 = r3
            r3 = r5
            r5 = r16
        L_0x0072:
            r5.printStackTrace()
            r5 = r2
            r16 = r3
            r3 = r4
            r4 = r16
            goto L_0x0018
        L_0x007c:
            r4 = move-exception
            r16 = r4
            r4 = r3
            r3 = r5
            r5 = r16
        L_0x0083:
            r5.printStackTrace()
            r5 = r2
            r16 = r3
            r3 = r4
            r4 = r16
            goto L_0x0018
        L_0x008d:
            r2 = 1
            java.lang.Class[] r2 = new java.lang.Class[r2]     // Catch:{ SecurityException -> 0x0098, NoSuchMethodException -> 0x00a3 }
            r7 = 0
            r2[r7] = r9     // Catch:{ SecurityException -> 0x0098, NoSuchMethodException -> 0x00a3 }
            java.lang.reflect.Constructor r2 = r3.getDeclaredConstructor(r2)     // Catch:{ SecurityException -> 0x0098, NoSuchMethodException -> 0x00a3 }
            goto L_0x0026
        L_0x0098:
            r2 = move-exception
            r16 = r2
            r2 = r6
            r6 = r16
        L_0x009e:
            r6.printStackTrace()
            r6 = r2
            goto L_0x0031
        L_0x00a3:
            r2 = move-exception
            r16 = r2
            r2 = r6
            r6 = r16
        L_0x00a9:
            r6.printStackTrace()
            r6 = r2
            goto L_0x0031
        L_0x00ae:
            r2 = 0
            java.lang.Iterable r3 = r17.rows()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.util.Iterator r11 = r3.iterator()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            r7 = r2
        L_0x00b8:
            boolean r2 = r11.hasNext()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r2 == 0) goto L_0x01a2
            java.lang.Object r2 = r11.next()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            r0 = r2
            processing.data.TableRow r0 = (processing.data.TableRow) r0     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            r3 = r0
            if (r9 != 0) goto L_0x00f9
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.lang.Object r2 = r6.newInstance(r2)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            r8 = r2
        L_0x00d0:
            java.util.Iterator r12 = r10.iterator()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
        L_0x00d4:
            boolean r2 = r12.hasNext()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r2 == 0) goto L_0x019a
            java.lang.Object r2 = r12.next()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.lang.reflect.Field r2 = (java.lang.reflect.Field) r2     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.lang.String r13 = r2.getName()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.lang.Class r14 = r2.getType()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.lang.Class<java.lang.String> r15 = java.lang.String.class
            if (r14 != r15) goto L_0x0105
            java.lang.String r13 = r3.getString((java.lang.String) r13)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            r2.set(r8, r13)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            goto L_0x00d4
        L_0x00f4:
            r2 = move-exception
            r2.printStackTrace()
        L_0x00f8:
            return
        L_0x00f9:
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            r8 = 0
            r2[r8] = r18     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.lang.Object r2 = r6.newInstance(r2)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            r8 = r2
            goto L_0x00d0
        L_0x0105:
            java.lang.Class r14 = r2.getType()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.lang.Class r15 = java.lang.Integer.TYPE     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r14 != r15) goto L_0x011a
            int r13 = r3.getInt((java.lang.String) r13)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            r2.setInt(r8, r13)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            goto L_0x00d4
        L_0x0115:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x00f8
        L_0x011a:
            java.lang.Class r14 = r2.getType()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.lang.Class r15 = java.lang.Long.TYPE     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r14 != r15) goto L_0x012f
            long r14 = r3.getLong((java.lang.String) r13)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            r2.setLong(r8, r14)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            goto L_0x00d4
        L_0x012a:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x00f8
        L_0x012f:
            java.lang.Class r14 = r2.getType()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.lang.Class r15 = java.lang.Float.TYPE     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r14 != r15) goto L_0x0144
            float r13 = r3.getFloat((java.lang.String) r13)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            r2.setFloat(r8, r13)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            goto L_0x00d4
        L_0x013f:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x00f8
        L_0x0144:
            java.lang.Class r14 = r2.getType()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.lang.Class r15 = java.lang.Double.TYPE     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r14 != r15) goto L_0x0154
            double r14 = r3.getDouble((java.lang.String) r13)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            r2.setDouble(r8, r14)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            goto L_0x00d4
        L_0x0154:
            java.lang.Class r14 = r2.getType()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.lang.Class r15 = java.lang.Boolean.TYPE     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r14 != r15) goto L_0x017c
            java.lang.String r13 = r3.getString((java.lang.String) r13)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r13 == 0) goto L_0x00d4
            java.lang.String r14 = r13.toLowerCase()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.lang.String r15 = "true"
            boolean r14 = r14.equals(r15)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r14 != 0) goto L_0x0176
            java.lang.String r14 = "1"
            boolean r13 = r13.equals(r14)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r13 == 0) goto L_0x00d4
        L_0x0176:
            r13 = 1
            r2.setBoolean(r8, r13)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            goto L_0x00d4
        L_0x017c:
            java.lang.Class r14 = r2.getType()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            java.lang.Class r15 = java.lang.Character.TYPE     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r14 != r15) goto L_0x00d4
            java.lang.String r13 = r3.getString((java.lang.String) r13)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r13 == 0) goto L_0x00d4
            int r14 = r13.length()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r14 <= 0) goto L_0x00d4
            r14 = 0
            char r13 = r13.charAt(r14)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            r2.setChar(r8, r13)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            goto L_0x00d4
        L_0x019a:
            int r2 = r7 + 1
            java.lang.reflect.Array.set(r5, r7, r8)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            r7 = r2
            goto L_0x00b8
        L_0x01a2:
            boolean r2 = r4.isAccessible()     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            if (r2 != 0) goto L_0x01ac
            r2 = 1
            r4.setAccessible(r2)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
        L_0x01ac:
            r0 = r18
            r4.set(r0, r5)     // Catch:{ InstantiationException -> 0x00f4, IllegalAccessException -> 0x0115, IllegalArgumentException -> 0x012a, InvocationTargetException -> 0x013f }
            goto L_0x00f8
        L_0x01b3:
            r6 = move-exception
            goto L_0x00a9
        L_0x01b6:
            r6 = move-exception
            goto L_0x009e
        L_0x01b9:
            r5 = move-exception
            r16 = r4
            r4 = r3
            r3 = r16
            goto L_0x0083
        L_0x01c1:
            r5 = move-exception
            r16 = r4
            r4 = r3
            r3 = r16
            goto L_0x0083
        L_0x01c9:
            r5 = move-exception
            r16 = r4
            r4 = r3
            r3 = r16
            goto L_0x0072
        L_0x01d1:
            r5 = move-exception
            r16 = r4
            r4 = r3
            r3 = r16
            goto L_0x0072
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.data.Table.parseInto(java.lang.Object, java.lang.String):void");
    }

    public void removeColumn(int i) {
        int length = this.columns.length - 1;
        Object[] objArr = new Object[length];
        HashMapBlows[] hashMapBlowsArr = new HashMapBlows[length];
        for (int i2 = 0; i2 < i; i2++) {
            objArr[i2] = this.columns[i2];
            hashMapBlowsArr[i2] = this.columnCategories[i2];
        }
        for (int i3 = i; i3 < length; i3++) {
            objArr[i3] = this.columns[i3 + 1];
            hashMapBlowsArr[i3] = this.columnCategories[i3 + 1];
        }
        this.columns = objArr;
        this.columnCategories = hashMapBlowsArr;
        if (this.columnTitles != null) {
            String[] strArr = new String[length];
            for (int i4 = 0; i4 < i; i4++) {
                strArr[i4] = this.columnTitles[i4];
            }
            while (i < length) {
                strArr[i] = this.columnTitles[i + 1];
                i++;
            }
            this.columnTitles = strArr;
            this.columnIndices = null;
        }
    }

    public void removeColumn(String str) {
        removeColumn(getColumnIndex(str));
    }

    public void removeRow(int i) {
        for (int i2 = 0; i2 < this.columns.length; i2++) {
            switch (this.columnTypes[i2]) {
                case 0:
                    Object obj = new String[(this.rowCount - 1)];
                    System.arraycopy(this.columns[i2], 0, obj, 0, i);
                    System.arraycopy(this.columns[i2], i + 1, obj, i, (this.rowCount - i) - 1);
                    this.columns[i2] = obj;
                    break;
                case 1:
                case 5:
                    Object obj2 = new int[(this.rowCount - 1)];
                    System.arraycopy(this.columns[i2], 0, obj2, 0, i);
                    System.arraycopy(this.columns[i2], i + 1, obj2, i, (this.rowCount - i) - 1);
                    this.columns[i2] = obj2;
                    break;
                case 2:
                    Object obj3 = new long[(this.rowCount - 1)];
                    System.arraycopy(this.columns[i2], 0, obj3, 0, i);
                    System.arraycopy(this.columns[i2], i + 1, obj3, i, (this.rowCount - i) - 1);
                    this.columns[i2] = obj3;
                    break;
                case 3:
                    Object obj4 = new float[(this.rowCount - 1)];
                    System.arraycopy(this.columns[i2], 0, obj4, 0, i);
                    System.arraycopy(this.columns[i2], i + 1, obj4, i, (this.rowCount - i) - 1);
                    this.columns[i2] = obj4;
                    break;
                case 4:
                    Object obj5 = new double[(this.rowCount - 1)];
                    System.arraycopy(this.columns[i2], 0, obj5, 0, i);
                    System.arraycopy(this.columns[i2], i + 1, obj5, i, (this.rowCount - i) - 1);
                    this.columns[i2] = obj5;
                    break;
            }
        }
        this.rowCount--;
    }

    @Deprecated
    public String[] removeTitleRow() {
        String[] stringRow = getStringRow(0);
        removeRow(0);
        setColumnTitles(stringRow);
        return stringRow;
    }

    public void removeTokens(String str) {
        for (int i = 0; i < getColumnCount(); i++) {
            removeTokens(str, i);
        }
    }

    public void removeTokens(String str, int i) {
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            String string = getString(i2, i);
            if (string != null) {
                char[] charArray = string.toCharArray();
                int i3 = 0;
                for (int i4 = 0; i4 < charArray.length; i4++) {
                    if (str.indexOf(charArray[i4]) == -1) {
                        if (i3 != i4) {
                            charArray[i3] = charArray[i4];
                        }
                        i3++;
                    }
                }
                if (i3 != charArray.length) {
                    setString(i2, i, new String(charArray, 0, i3));
                }
            }
        }
    }

    public void removeTokens(String str, String str2) {
        removeTokens(str, getColumnIndex(str2));
    }

    public void replace(String str, String str2) {
        for (int i = 0; i < this.columns.length; i++) {
            replace(str, str2, i);
        }
    }

    public void replace(String str, String str2, int i) {
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            for (int i2 = 0; i2 < this.rowCount; i2++) {
                if (strArr[i2].equals(str)) {
                    strArr[i2] = str2;
                }
            }
        }
    }

    public void replace(String str, String str2, String str3) {
        replace(str, str2, getColumnIndex(str3));
    }

    public void replaceAll(String str, String str2) {
        for (int i = 0; i < this.columns.length; i++) {
            replaceAll(str, str2, i);
        }
    }

    public void replaceAll(String str, String str2, int i) {
        checkColumn(i);
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            for (int i2 = 0; i2 < this.rowCount; i2++) {
                if (strArr[i2] != null) {
                    strArr[i2] = strArr[i2].replaceAll(str, str2);
                }
            }
            return;
        }
        throw new IllegalArgumentException("replaceAll() can only be used on String columns");
    }

    public void replaceAll(String str, String str2, String str3) {
        replaceAll(str, str2, getColumnIndex(str3));
    }

    public Iterable<TableRow> rows() {
        return new Iterable<TableRow>() {
            public Iterator<TableRow> iterator() {
                if (Table.this.rowIterator == null) {
                    Table.this.rowIterator = new RowIterator(Table.this);
                } else {
                    Table.this.rowIterator.reset();
                }
                return Table.this.rowIterator;
            }
        };
    }

    public Iterable<TableRow> rows(final int[] iArr) {
        return new Iterable<TableRow>() {
            public Iterator<TableRow> iterator() {
                return new RowIndexIterator(Table.this, iArr);
            }
        };
    }

    public boolean save(File file, String str) throws IOException {
        return save(PApplet.createOutput(file), extensionOptions(false, file.getName(), str));
    }

    public boolean save(OutputStream outputStream, String str) {
        boolean z;
        PrintWriter createWriter = PApplet.createWriter(outputStream);
        if (str == null) {
            throw new IllegalArgumentException("No extension specified for saving this Table");
        }
        String[] splitTokens = PApplet.splitTokens(str, ", ");
        String str2 = splitTokens[splitTokens.length - 1];
        String[] strArr = saveExtensions;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            } else if (str2.equals(strArr[i])) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (!z) {
            throw new IllegalArgumentException("'" + str2 + "' not available for Table");
        }
        if (str2.equals("csv")) {
            writeCSV(createWriter);
        } else if (str2.equals("tsv")) {
            writeTSV(createWriter);
        } else if (str2.equals("html")) {
            writeHTML(createWriter);
        } else if (str2.equals("bin")) {
            try {
                saveBinary(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        createWriter.flush();
        createWriter.close();
        return true;
    }

    /* access modifiers changed from: protected */
    public void saveBinary(OutputStream outputStream) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(outputStream));
        dataOutputStream.writeInt(-1878545634);
        dataOutputStream.writeInt(getRowCount());
        dataOutputStream.writeInt(getColumnCount());
        if (this.columnTitles != null) {
            dataOutputStream.writeBoolean(true);
            for (String writeUTF : this.columnTitles) {
                dataOutputStream.writeUTF(writeUTF);
            }
        } else {
            dataOutputStream.writeBoolean(false);
        }
        for (int i = 0; i < getColumnCount(); i++) {
            dataOutputStream.writeInt(this.columnTypes[i]);
        }
        for (int i2 = 0; i2 < getColumnCount(); i2++) {
            if (this.columnTypes[i2] == 5) {
                this.columnCategories[i2].write(dataOutputStream);
            }
        }
        if (this.missingString == null) {
            dataOutputStream.writeBoolean(false);
        } else {
            dataOutputStream.writeBoolean(true);
            dataOutputStream.writeUTF(this.missingString);
        }
        dataOutputStream.writeInt(this.missingInt);
        dataOutputStream.writeLong(this.missingLong);
        dataOutputStream.writeFloat(this.missingFloat);
        dataOutputStream.writeDouble(this.missingDouble);
        dataOutputStream.writeInt(this.missingCategory);
        for (TableRow next : rows()) {
            for (int i3 = 0; i3 < getColumnCount(); i3++) {
                switch (this.columnTypes[i3]) {
                    case 0:
                        String string = next.getString(i3);
                        if (string != null) {
                            dataOutputStream.writeBoolean(true);
                            dataOutputStream.writeUTF(string);
                            break;
                        } else {
                            dataOutputStream.writeBoolean(false);
                            break;
                        }
                    case 1:
                        dataOutputStream.writeInt(next.getInt(i3));
                        break;
                    case 2:
                        dataOutputStream.writeLong(next.getLong(i3));
                        break;
                    case 3:
                        dataOutputStream.writeFloat(next.getFloat(i3));
                        break;
                    case 4:
                        dataOutputStream.writeDouble(next.getDouble(i3));
                        break;
                    case 5:
                        dataOutputStream.writeInt(this.columnCategories[i3].index(next.getString(i3)));
                        break;
                }
            }
        }
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    public void setColumnCount(int i) {
        int length = this.columns.length;
        if (length != i) {
            this.columns = (Object[]) PApplet.expand((Object) this.columns, i);
            for (int i2 = length; i2 < i; i2++) {
                this.columns[i2] = new String[this.rowCount];
            }
            if (this.columnTitles != null) {
                this.columnTitles = PApplet.expand(this.columnTitles, i);
            }
            this.columnTypes = PApplet.expand(this.columnTypes, i);
            this.columnCategories = (HashMapBlows[]) PApplet.expand((Object) this.columnCategories, i);
        }
    }

    public void setColumnTitle(int i, String str) {
        ensureColumn(i);
        if (this.columnTitles == null) {
            this.columnTitles = new String[getColumnCount()];
        }
        this.columnTitles[i] = str;
        this.columnIndices = null;
    }

    public void setColumnTitles(String[] strArr) {
        if (strArr != null) {
            ensureColumn(strArr.length - 1);
        }
        this.columnTitles = strArr;
        this.columnIndices = null;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v9, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v12, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v15, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v19, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setColumnType(int r5, int r6) {
        /*
            r4 = this;
            r0 = 0
            switch(r6) {
                case 0: goto L_0x0088;
                case 1: goto L_0x000c;
                case 2: goto L_0x002c;
                case 3: goto L_0x004c;
                case 4: goto L_0x0068;
                case 5: goto L_0x00a4;
                default: goto L_0x0004;
            }
        L_0x0004:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "That's not a valid column type."
            r0.<init>(r1)
            throw r0
        L_0x000c:
            int r1 = r4.rowCount
            int[] r1 = new int[r1]
        L_0x0010:
            int r2 = r4.rowCount
            if (r0 >= r2) goto L_0x0023
            java.lang.String r2 = r4.getString((int) r0, (int) r5)
            int r3 = r4.missingInt
            int r2 = processing.core.PApplet.parseInt((java.lang.String) r2, (int) r3)
            r1[r0] = r2
            int r0 = r0 + 1
            goto L_0x0010
        L_0x0023:
            java.lang.Object[] r0 = r4.columns
            r0[r5] = r1
        L_0x0027:
            int[] r0 = r4.columnTypes
            r0[r5] = r6
            return
        L_0x002c:
            int r1 = r4.rowCount
            long[] r1 = new long[r1]
        L_0x0030:
            int r2 = r4.rowCount
            if (r0 >= r2) goto L_0x0047
            java.lang.String r2 = r4.getString((int) r0, (int) r5)
            long r2 = java.lang.Long.parseLong(r2)     // Catch:{ NumberFormatException -> 0x0041 }
            r1[r0] = r2     // Catch:{ NumberFormatException -> 0x0041 }
        L_0x003e:
            int r0 = r0 + 1
            goto L_0x0030
        L_0x0041:
            r2 = move-exception
            long r2 = r4.missingLong
            r1[r0] = r2
            goto L_0x003e
        L_0x0047:
            java.lang.Object[] r0 = r4.columns
            r0[r5] = r1
            goto L_0x0027
        L_0x004c:
            int r1 = r4.rowCount
            float[] r1 = new float[r1]
        L_0x0050:
            int r2 = r4.rowCount
            if (r0 >= r2) goto L_0x0063
            java.lang.String r2 = r4.getString((int) r0, (int) r5)
            float r3 = r4.missingFloat
            float r2 = processing.core.PApplet.parseFloat((java.lang.String) r2, (float) r3)
            r1[r0] = r2
            int r0 = r0 + 1
            goto L_0x0050
        L_0x0063:
            java.lang.Object[] r0 = r4.columns
            r0[r5] = r1
            goto L_0x0027
        L_0x0068:
            int r1 = r4.rowCount
            double[] r1 = new double[r1]
        L_0x006c:
            int r2 = r4.rowCount
            if (r0 >= r2) goto L_0x0083
            java.lang.String r2 = r4.getString((int) r0, (int) r5)
            double r2 = java.lang.Double.parseDouble(r2)     // Catch:{ NumberFormatException -> 0x007d }
            r1[r0] = r2     // Catch:{ NumberFormatException -> 0x007d }
        L_0x007a:
            int r0 = r0 + 1
            goto L_0x006c
        L_0x007d:
            r2 = move-exception
            double r2 = r4.missingDouble
            r1[r0] = r2
            goto L_0x007a
        L_0x0083:
            java.lang.Object[] r0 = r4.columns
            r0[r5] = r1
            goto L_0x0027
        L_0x0088:
            int[] r1 = r4.columnTypes
            r1 = r1[r5]
            if (r1 == 0) goto L_0x0027
            int r1 = r4.rowCount
            java.lang.String[] r1 = new java.lang.String[r1]
        L_0x0092:
            int r2 = r4.rowCount
            if (r0 >= r2) goto L_0x009f
            java.lang.String r2 = r4.getString((int) r0, (int) r5)
            r1[r0] = r2
            int r0 = r0 + 1
            goto L_0x0092
        L_0x009f:
            java.lang.Object[] r0 = r4.columns
            r0[r5] = r1
            goto L_0x0027
        L_0x00a4:
            int r1 = r4.rowCount
            int[] r1 = new int[r1]
            processing.data.Table$HashMapBlows r2 = new processing.data.Table$HashMapBlows
            r2.<init>()
        L_0x00ad:
            int r3 = r4.rowCount
            if (r0 >= r3) goto L_0x00be
            java.lang.String r3 = r4.getString((int) r0, (int) r5)
            int r3 = r2.index(r3)
            r1[r0] = r3
            int r0 = r0 + 1
            goto L_0x00ad
        L_0x00be:
            processing.data.Table$HashMapBlows[] r0 = r4.columnCategories
            r0[r5] = r2
            java.lang.Object[] r0 = r4.columns
            r0[r5] = r1
            goto L_0x0027
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.data.Table.setColumnType(int, int):void");
    }

    public void setColumnType(int i, String str) {
        int i2;
        if (str.equals("String")) {
            i2 = 0;
        } else if (str.equals("int")) {
            i2 = 1;
        } else if (str.equals("long")) {
            i2 = 2;
        } else if (str.equals("float")) {
            i2 = 3;
        } else if (str.equals("double")) {
            i2 = 4;
        } else if (str.equals("category")) {
            i2 = 5;
        } else {
            throw new IllegalArgumentException("'" + str + "' is not a valid column type.");
        }
        setColumnType(i, i2);
    }

    public void setColumnType(String str, int i) {
        setColumnType(checkColumnIndex(str), i);
    }

    public void setColumnType(String str, String str2) {
        setColumnType(checkColumnIndex(str), str2);
    }

    public void setColumnTypes(Table table) {
        int i;
        int i2;
        final int i3 = 0;
        if (table.hasColumnTitles()) {
            i2 = table.getColumnIndex("title", true);
            i = table.getColumnIndex("type", true);
        } else {
            i = 1;
            i2 = 0;
        }
        setColumnTitles(table.getStringColumn(i2));
        final String[] stringColumn = table.getStringColumn(i);
        if (table.getColumnCount() <= 1) {
            return;
        }
        if (getRowCount() > 1000) {
            ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2);
            while (i3 < table.getRowCount()) {
                newFixedThreadPool.execute(new Runnable() {
                    public void run() {
                        Table.this.setColumnType(i3, stringColumn[i3]);
                    }
                });
                i3++;
            }
            newFixedThreadPool.shutdown();
            while (!newFixedThreadPool.isTerminated()) {
                Thread.yield();
            }
            return;
        }
        while (i3 < table.getRowCount()) {
            setColumnType(i3, stringColumn[i3]);
            i3++;
        }
    }

    public void setColumnTypes(int[] iArr) {
        for (int i = 0; i < iArr.length; i++) {
            setColumnType(i, iArr[i]);
        }
    }

    public void setDouble(int i, int i2, double d) {
        if (this.columnTypes[i2] == 0) {
            setString(i, i2, String.valueOf(d));
            return;
        }
        ensureBounds(i, i2);
        if (this.columnTypes[i2] != 4) {
            throw new IllegalArgumentException("Column " + i2 + " is not a 'double' column.");
        }
        ((double[]) this.columns[i2])[i] = d;
    }

    public void setDouble(int i, String str, double d) {
        setDouble(i, getColumnIndex(str), d);
    }

    public void setFloat(int i, int i2, float f) {
        if (this.columnTypes[i2] == 0) {
            setString(i, i2, String.valueOf(f));
            return;
        }
        ensureBounds(i, i2);
        if (this.columnTypes[i2] != 3) {
            throw new IllegalArgumentException("Column " + i2 + " is not a float column.");
        }
        ((float[]) this.columns[i2])[i] = f;
    }

    public void setFloat(int i, String str, float f) {
        setFloat(i, getColumnIndex(str), f);
    }

    public void setInt(int i, int i2, int i3) {
        if (this.columnTypes[i2] == 0) {
            setString(i, i2, String.valueOf(i3));
            return;
        }
        ensureBounds(i, i2);
        if (this.columnTypes[i2] == 1 || this.columnTypes[i2] == 5) {
            ((int[]) this.columns[i2])[i] = i3;
            return;
        }
        throw new IllegalArgumentException("Column " + i2 + " is not an int column.");
    }

    public void setInt(int i, String str, int i2) {
        setInt(i, getColumnIndex(str), i2);
    }

    public void setLong(int i, int i2, long j) {
        if (this.columnTypes[i2] == 0) {
            setString(i, i2, String.valueOf(j));
            return;
        }
        ensureBounds(i, i2);
        if (this.columnTypes[i2] != 2) {
            throw new IllegalArgumentException("Column " + i2 + " is not a 'long' column.");
        }
        ((long[]) this.columns[i2])[i] = j;
    }

    public void setLong(int i, String str, long j) {
        setLong(i, getColumnIndex(str), j);
    }

    public void setMissingDouble(double d) {
        this.missingDouble = d;
    }

    public void setMissingFloat(float f) {
        this.missingFloat = f;
    }

    public void setMissingInt(int i) {
        this.missingInt = i;
    }

    public void setMissingLong(long j) {
        this.missingLong = j;
    }

    public void setMissingString(String str) {
        this.missingString = str;
    }

    public void setRow(int i, Object[] objArr) {
        ensureBounds(i, objArr.length - 1);
        for (int i2 = 0; i2 < objArr.length; i2++) {
            setRowCol(i, i2, objArr[i2]);
        }
    }

    /* access modifiers changed from: protected */
    public void setRowCol(int i, int i2, Object obj) {
        switch (this.columnTypes[i2]) {
            case 0:
                String[] strArr = (String[]) this.columns[i2];
                if (obj == null) {
                    strArr[i] = null;
                    return;
                } else {
                    strArr[i] = String.valueOf(obj);
                    return;
                }
            case 1:
                int[] iArr = (int[]) this.columns[i2];
                if (obj == null) {
                    iArr[i] = this.missingInt;
                    return;
                } else if (obj instanceof Integer) {
                    iArr[i] = ((Integer) obj).intValue();
                    return;
                } else {
                    iArr[i] = PApplet.parseInt(String.valueOf(obj), this.missingInt);
                    return;
                }
            case 2:
                long[] jArr = (long[]) this.columns[i2];
                if (obj == null) {
                    jArr[i] = this.missingLong;
                    return;
                } else if (obj instanceof Long) {
                    jArr[i] = ((Long) obj).longValue();
                    return;
                } else {
                    try {
                        jArr[i] = Long.parseLong(String.valueOf(obj));
                        return;
                    } catch (NumberFormatException e) {
                        jArr[i] = this.missingLong;
                        return;
                    }
                }
            case 3:
                float[] fArr = (float[]) this.columns[i2];
                if (obj == null) {
                    fArr[i] = this.missingFloat;
                    return;
                } else if (obj instanceof Float) {
                    fArr[i] = ((Float) obj).floatValue();
                    return;
                } else {
                    fArr[i] = PApplet.parseFloat(String.valueOf(obj), this.missingFloat);
                    return;
                }
            case 4:
                double[] dArr = (double[]) this.columns[i2];
                if (obj == null) {
                    dArr[i] = this.missingDouble;
                    return;
                } else if (obj instanceof Double) {
                    dArr[i] = ((Double) obj).doubleValue();
                    return;
                } else {
                    try {
                        dArr[i] = Double.parseDouble(String.valueOf(obj));
                        return;
                    } catch (NumberFormatException e2) {
                        dArr[i] = this.missingDouble;
                        return;
                    }
                }
            case 5:
                int[] iArr2 = (int[]) this.columns[i2];
                if (obj == null) {
                    iArr2[i] = this.missingCategory;
                    return;
                } else {
                    iArr2[i] = this.columnCategories[i2].index(String.valueOf(obj));
                    return;
                }
            default:
                throw new IllegalArgumentException("That's not a valid column type.");
        }
    }

    public void setRowCount(int i) {
        if (i != this.rowCount) {
            if (i > 1000000) {
                System.out.print("Note: setting maximum row count to " + PApplet.nfc(i));
            }
            long currentTimeMillis = System.currentTimeMillis();
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < this.columns.length) {
                    switch (this.columnTypes[i3]) {
                        case 0:
                            this.columns[i3] = PApplet.expand((String[]) this.columns[i3], i);
                            break;
                        case 1:
                            this.columns[i3] = PApplet.expand((int[]) this.columns[i3], i);
                            break;
                        case 2:
                            this.columns[i3] = PApplet.expand((Object) (long[]) this.columns[i3], i);
                            break;
                        case 3:
                            this.columns[i3] = PApplet.expand((float[]) this.columns[i3], i);
                            break;
                        case 4:
                            this.columns[i3] = PApplet.expand((Object) (double[]) this.columns[i3], i);
                            break;
                        case 5:
                            this.columns[i3] = PApplet.expand((int[]) this.columns[i3], i);
                            break;
                    }
                    if (i > 1000000) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    i2 = i3 + 1;
                } else if (i > 1000000) {
                    System.out.println(" (resize took " + PApplet.nfc((int) (System.currentTimeMillis() - currentTimeMillis)) + " ms)");
                }
            }
        }
        this.rowCount = i;
    }

    public void setString(int i, int i2, String str) {
        ensureBounds(i, i2);
        if (this.columnTypes[i2] != 0) {
            throw new IllegalArgumentException("Column " + i2 + " is not a String column.");
        }
        ((String[]) this.columns[i2])[i] = str;
    }

    public void setString(int i, String str, String str2) {
        setString(i, checkColumnIndex(str), str2);
    }

    public void setTableType(String str) {
        for (int i = 0; i < getColumnCount(); i++) {
            setColumnType(i, str);
        }
    }

    public void sort(int i) {
        sort(i, false);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v9, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v14, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v19, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v24, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v29, resolved type: java.lang.Object[]} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sort(final int r9, final boolean r10) {
        /*
            r8 = this;
            r2 = 0
            int r0 = r8.getRowCount()
            processing.data.IntList r0 = processing.data.IntList.fromRange(r0)
            int[] r4 = r0.array()
            processing.data.Table$6 r0 = new processing.data.Table$6
            r0.<init>(r10, r4, r9)
            r0.run()
            r1 = r2
        L_0x0016:
            int r0 = r8.getColumnCount()
            if (r1 >= r0) goto L_0x00ce
            int r0 = r8.getColumnType((int) r1)
            switch(r0) {
                case 0: goto L_0x00ac;
                case 1: goto L_0x0027;
                case 2: goto L_0x0048;
                case 3: goto L_0x0069;
                case 4: goto L_0x008a;
                case 5: goto L_0x0027;
                default: goto L_0x0023;
            }
        L_0x0023:
            int r0 = r1 + 1
            r1 = r0
            goto L_0x0016
        L_0x0027:
            java.lang.Object[] r0 = r8.columns
            r0 = r0[r1]
            int[] r0 = (int[]) r0
            int[] r0 = (int[]) r0
            int r3 = r8.rowCount
            int[] r5 = new int[r3]
            r3 = r2
        L_0x0034:
            int r6 = r8.getRowCount()
            if (r3 >= r6) goto L_0x0043
            r6 = r4[r3]
            r6 = r0[r6]
            r5[r3] = r6
            int r3 = r3 + 1
            goto L_0x0034
        L_0x0043:
            java.lang.Object[] r0 = r8.columns
            r0[r1] = r5
            goto L_0x0023
        L_0x0048:
            java.lang.Object[] r0 = r8.columns
            r0 = r0[r1]
            long[] r0 = (long[]) r0
            long[] r0 = (long[]) r0
            int r3 = r8.rowCount
            long[] r5 = new long[r3]
            r3 = r2
        L_0x0055:
            int r6 = r8.getRowCount()
            if (r3 >= r6) goto L_0x0064
            r6 = r4[r3]
            r6 = r0[r6]
            r5[r3] = r6
            int r3 = r3 + 1
            goto L_0x0055
        L_0x0064:
            java.lang.Object[] r0 = r8.columns
            r0[r1] = r5
            goto L_0x0023
        L_0x0069:
            java.lang.Object[] r0 = r8.columns
            r0 = r0[r1]
            float[] r0 = (float[]) r0
            float[] r0 = (float[]) r0
            int r3 = r8.rowCount
            float[] r5 = new float[r3]
            r3 = r2
        L_0x0076:
            int r6 = r8.getRowCount()
            if (r3 >= r6) goto L_0x0085
            r6 = r4[r3]
            r6 = r0[r6]
            r5[r3] = r6
            int r3 = r3 + 1
            goto L_0x0076
        L_0x0085:
            java.lang.Object[] r0 = r8.columns
            r0[r1] = r5
            goto L_0x0023
        L_0x008a:
            java.lang.Object[] r0 = r8.columns
            r0 = r0[r1]
            double[] r0 = (double[]) r0
            double[] r0 = (double[]) r0
            int r3 = r8.rowCount
            double[] r5 = new double[r3]
            r3 = r2
        L_0x0097:
            int r6 = r8.getRowCount()
            if (r3 >= r6) goto L_0x00a6
            r6 = r4[r3]
            r6 = r0[r6]
            r5[r3] = r6
            int r3 = r3 + 1
            goto L_0x0097
        L_0x00a6:
            java.lang.Object[] r0 = r8.columns
            r0[r1] = r5
            goto L_0x0023
        L_0x00ac:
            java.lang.Object[] r0 = r8.columns
            r0 = r0[r1]
            java.lang.String[] r0 = (java.lang.String[]) r0
            java.lang.String[] r0 = (java.lang.String[]) r0
            int r3 = r8.rowCount
            java.lang.String[] r5 = new java.lang.String[r3]
            r3 = r2
        L_0x00b9:
            int r6 = r8.getRowCount()
            if (r3 >= r6) goto L_0x00c8
            r6 = r4[r3]
            r6 = r0[r6]
            r5[r3] = r6
            int r3 = r3 + 1
            goto L_0x00b9
        L_0x00c8:
            java.lang.Object[] r0 = r8.columns
            r0[r1] = r5
            goto L_0x0023
        L_0x00ce:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.data.Table.sort(int, boolean):void");
    }

    public void sort(String str) {
        sort(getColumnIndex(str), false);
    }

    public void sortReverse(int i) {
        sort(i, true);
    }

    public void sortReverse(String str) {
        sort(getColumnIndex(str), true);
    }

    public void trim() {
        for (int i = 0; i < getColumnCount(); i++) {
            trim(i);
        }
    }

    public void trim(int i) {
        if (this.columnTypes[i] == 0) {
            String[] strArr = (String[]) this.columns[i];
            for (int i2 = 0; i2 < this.rowCount; i2++) {
                if (strArr[i2] != null) {
                    strArr[i2] = PApplet.trim(strArr[i2]);
                }
            }
        }
    }

    public void trim(String str) {
        trim(getColumnIndex(str));
    }

    public Table typedParse(InputStream inputStream, String str) throws IOException {
        Table table = new Table();
        table.setColumnTypes(this);
        table.parse(inputStream, str);
        return table;
    }

    /* access modifiers changed from: protected */
    public void writeCSV(PrintWriter printWriter) {
        if (this.columnTitles != null) {
            for (int i = 0; i < this.columns.length; i++) {
                if (i != 0) {
                    printWriter.print(',');
                }
                if (this.columnTitles[i] != null) {
                    writeEntryCSV(printWriter, this.columnTitles[i]);
                }
            }
            printWriter.println();
        }
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            for (int i3 = 0; i3 < getColumnCount(); i3++) {
                if (i3 != 0) {
                    printWriter.print(',');
                }
                String string = getString(i2, i3);
                if (string != null) {
                    writeEntryCSV(printWriter, string);
                }
            }
            printWriter.println();
        }
        printWriter.flush();
    }

    /* access modifiers changed from: protected */
    public void writeEntryCSV(PrintWriter printWriter, String str) {
        if (str == null) {
            return;
        }
        if (str.indexOf(34) != -1) {
            char[] charArray = str.toCharArray();
            printWriter.print('\"');
            for (int i = 0; i < charArray.length; i++) {
                if (charArray[i] == '\"') {
                    printWriter.print("\"\"");
                } else {
                    printWriter.print(charArray[i]);
                }
            }
            printWriter.print('\"');
        } else if (str.indexOf(44) != -1 || str.indexOf(10) != -1 || str.indexOf(13) != -1) {
            printWriter.print('\"');
            printWriter.print(str);
            printWriter.print('\"');
        } else if (str.length() <= 0 || !(str.charAt(0) == ' ' || str.charAt(str.length() - 1) == ' ')) {
            printWriter.print(str);
        } else {
            printWriter.print('\"');
            printWriter.print(str);
            printWriter.print('\"');
        }
    }

    /* access modifiers changed from: protected */
    public void writeEntryHTML(PrintWriter printWriter, String str) {
        for (char c : str.toCharArray()) {
            if (c == '<') {
                printWriter.print("&lt;");
            } else if (c == '>') {
                printWriter.print("&gt;");
            } else if (c == '&') {
                printWriter.print("&amp;");
            } else if (c == '\'') {
                printWriter.print("&apos;");
            } else if (c == '\"') {
                printWriter.print("&quot;");
            } else {
                printWriter.print(c);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void writeHTML(PrintWriter printWriter) {
        printWriter.println("<html>");
        printWriter.println("<head>");
        printWriter.println("  <meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\" />");
        printWriter.println("</head>");
        printWriter.println("<body>");
        printWriter.println("  <table>");
        for (int i = 0; i < getRowCount(); i++) {
            printWriter.println("    <tr>");
            for (int i2 = 0; i2 < getColumnCount(); i2++) {
                String string = getString(i, i2);
                printWriter.print("      <td>");
                writeEntryHTML(printWriter, string);
                printWriter.println("      </td>");
            }
            printWriter.println("    </tr>");
        }
        printWriter.println("  </table>");
        printWriter.println("</body>");
        printWriter.println("</hmtl>");
        printWriter.flush();
    }

    /* access modifiers changed from: protected */
    public void writeTSV(PrintWriter printWriter) {
        if (this.columnTitles != null) {
            for (int i = 0; i < this.columns.length; i++) {
                if (i != 0) {
                    printWriter.print(9);
                }
                if (this.columnTitles[i] != null) {
                    printWriter.print(this.columnTitles[i]);
                }
            }
            printWriter.println();
        }
        for (int i2 = 0; i2 < this.rowCount; i2++) {
            for (int i3 = 0; i3 < getColumnCount(); i3++) {
                if (i3 != 0) {
                    printWriter.print(9);
                }
                String string = getString(i2, i3);
                if (string != null) {
                    printWriter.print(string);
                }
            }
            printWriter.println();
        }
        printWriter.flush();
    }
}
