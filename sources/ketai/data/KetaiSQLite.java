package ketai.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import processing.core.PApplet;

public class KetaiSQLite {
    private static final int DATABASE_VERSION = 1;
    /* access modifiers changed from: private */
    public String DATABASE_NAME = "data";
    private String DATA_ROOT_DIRECTORY = "_data";
    private Context context;
    private Cursor cursor;
    private SQLiteDatabase db;
    private SQLiteStatement sqlStatement;

    public KetaiSQLite(PApplet p) {
        this.context = p.getActivity().getApplicationContext();
        this.DATABASE_NAME = this.context.getPackageName();
        this.DATA_ROOT_DIRECTORY = this.context.getPackageName();
        PApplet.println("data path" + this.context.getDatabasePath(this.context.getPackageName()).getAbsolutePath());
        this.db = new OpenHelper(this.context).getWritableDatabase();
    }

    public KetaiSQLite(PApplet p, String dbname) {
        this.context = p.getActivity().getApplicationContext();
        this.DATABASE_NAME = dbname;
        this.db = new OpenHelper(this.context, dbname).getWritableDatabase();
    }

    public static boolean load(PApplet _context, String filename, String dbname) {
        try {
            InputStream myInput = _context.getActivity().getApplicationContext().getAssets().open(filename);
            if (myInput == null) {
                return false;
            }
            OutputStream myOutput = new FileOutputStream(_context.getActivity().getApplicationContext().getDatabasePath(dbname).getAbsolutePath());
            byte[] buffer = new byte[4096];
            while (true) {
                int length = myInput.read(buffer);
                if (length <= 0) {
                    myOutput.flush();
                    myOutput.close();
                    myInput.close();
                    return true;
                }
                myOutput.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            PApplet.println("Failed to load SQLite file(not found): " + filename);
            return false;
        } catch (IOException iox) {
            PApplet.println("IO Error in copying SQLite database " + filename + ": " + iox.getMessage());
            return false;
        }
    }

    public String getPath() {
        return this.db.getPath();
    }

    public SQLiteDatabase getDb() {
        return this.db;
    }

    public boolean connect() {
        return this.db.isOpen();
    }

    public void close() {
        if (this.db != null) {
            this.db.close();
        }
    }

    public void dispose() {
        close();
    }

    public boolean execute(String _sql) {
        try {
            this.db.execSQL(_sql);
            return true;
        } catch (SQLiteException x) {
            PApplet.println("Error executing sql statement: " + x.getMessage());
            return false;
        }
    }

    public boolean query(String _query) {
        try {
            this.cursor = this.db.rawQuery(_query, (String[]) null);
            return true;
        } catch (SQLiteException x) {
            PApplet.println("Error executing query: " + x.getMessage());
            return false;
        }
    }

    public boolean next() {
        if (this.cursor == null) {
            return false;
        }
        return this.cursor.moveToNext();
    }

    public double getDouble(int _col) {
        if (_col < 0) {
            return 0.0d;
        }
        return this.cursor.getDouble(_col);
    }

    public double getDouble(String field) {
        return getDouble(this.cursor.getColumnIndex(field));
    }

    public float getFloat(int _col) {
        if (_col < 0) {
            return 0.0f;
        }
        return this.cursor.getFloat(_col);
    }

    public float getFloat(String field) {
        return getFloat(this.cursor.getColumnIndex(field));
    }

    public int getInt(int _col) {
        if (_col < 0) {
            return 0;
        }
        return this.cursor.getInt(_col);
    }

    public int getInt(String field) {
        return getInt(this.cursor.getColumnIndex(field));
    }

    public long getLong(int _col) {
        if (_col < 0) {
            return 0;
        }
        return this.cursor.getLong(_col);
    }

    public long getLong(String field) {
        return getLong(this.cursor.getColumnIndex(field));
    }

    public byte[] getBlob(int _col) {
        if (_col < 0) {
            return null;
        }
        return this.cursor.getBlob(_col);
    }

    public byte[] getBlob(String field) {
        return getBlob(this.cursor.getColumnIndex(field));
    }

    public String getString(int _col) {
        if (_col < 0) {
            return null;
        }
        return this.cursor.getString(_col);
    }

    public String getString(String field) {
        return getString(this.cursor.getColumnIndex(field));
    }

    public String[] getTables() {
        ArrayList<String> tables = new ArrayList<>();
        try {
            Cursor cursor2 = this.db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;", (String[]) null);
            if (cursor2.moveToFirst()) {
                do {
                    if (cursor2.getString(0) != "android_metadata") {
                        tables.add(cursor2.getString(0));
                    }
                } while (cursor2.moveToNext());
            }
        } catch (SQLiteException x) {
            x.printStackTrace();
        }
        String[] strArray = new String[tables.size()];
        tables.toArray(strArray);
        return strArray;
    }

    public String[] getFields(String table) {
        String s = "PRAGMA table_info(" + table + ");";
        ArrayList<String> fields = new ArrayList<>();
        try {
            Cursor cursor2 = this.db.rawQuery(s, (String[]) null);
            if (cursor2.moveToFirst()) {
                do {
                    fields.add(cursor2.getString(1));
                } while (cursor2.moveToNext());
            }
        } catch (SQLiteException x) {
            x.printStackTrace();
        }
        String[] strArray = new String[fields.size()];
        fields.toArray(strArray);
        return strArray;
    }

    public String getFieldMin(String table, String field) {
        this.sqlStatement = this.db.compileStatement("SELECT MIN(" + field + ") FROM " + table);
        String c = this.sqlStatement.simpleQueryForString();
        if (c == null) {
            return "0";
        }
        return c;
    }

    public String getFieldMax(String table, String field) {
        this.sqlStatement = this.db.compileStatement("SELECT MAX(" + field + ") FROM " + table);
        String c = this.sqlStatement.simpleQueryForString();
        if (c == null) {
            return "0";
        }
        return c;
    }

    public long getRecordCount(String table) {
        this.sqlStatement = this.db.compileStatement("SELECT COUNT(*) FROM " + table);
        return this.sqlStatement.simpleQueryForLong();
    }

    public long getDataCount() {
        long count = 0;
        try {
            Cursor cursor2 = this.db.rawQuery("select name from SQLite_Master", (String[]) null);
            if (cursor2.moveToFirst()) {
                do {
                    String tablename = cursor2.getString(0);
                    if (!tablename.equals("android_metadata")) {
                        this.sqlStatement = this.db.compileStatement("SELECT COUNT(*) FROM " + tablename);
                        count += this.sqlStatement.simpleQueryForLong();
                    }
                } while (cursor2.moveToNext());
            }
            if (cursor2 != null && !cursor2.isClosed()) {
                cursor2.close();
            }
        } catch (SQLiteException x) {
            x.printStackTrace();
        }
        return count;
    }

    public boolean tableExists(String _table) {
        Cursor cursor2 = this.db.rawQuery("select name from SQLite_Master", (String[]) null);
        if (cursor2.moveToFirst()) {
            do {
                PApplet.println("DataManager found this table: " + cursor2.getString(0));
                if (cursor2.getString(0).equalsIgnoreCase(_table)) {
                    return true;
                }
            } while (cursor2.moveToNext());
        }
        if (cursor2 == null || cursor2.isClosed()) {
            return false;
        }
        cursor2.close();
        return false;
    }

    public void exportData(String _targetDirectory) throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory(), String.valueOf(this.DATA_ROOT_DIRECTORY) + "/" + String.valueOf(System.currentTimeMillis()));
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                PApplet.println("success making directory: " + dir.getAbsolutePath());
            } else {
                PApplet.println("Failed making directory. Check your sketch permissions or that your device is not connected in disk mode.");
                return;
            }
        }
        int rowCount = 0;
        try {
            Cursor cursor2 = this.db.rawQuery("select name from SQLite_Master", (String[]) null);
            if (!cursor2.moveToFirst() || cursor2.getCount() <= 0) {
                if (cursor2 != null && !cursor2.isClosed()) {
                    cursor2.close();
                }
                deleteAllData();
            }
            String row = "";
            do {
                String tablename = cursor2.getString(0);
                if (!tablename.equals("android_metadata")) {
                    Cursor c = this.db.rawQuery("SELECT * FROM " + tablename, (String[]) null);
                    if (c.moveToFirst()) {
                        do {
                            for (int j = 0; j < c.getColumnCount(); j++) {
                                row = String.valueOf(row) + c.getString(j) + "\t";
                            }
                            row = String.valueOf(row) + "\n";
                            rowCount++;
                            if (rowCount > 100) {
                                if (row.length() > 0) {
                                    writeToFile(row, dir.getAbsolutePath(), tablename);
                                }
                                row = "";
                                rowCount = 0;
                            }
                        } while (c.moveToNext());
                        writeToFile(row, dir.getAbsolutePath(), tablename);
                        row = "";
                        rowCount = 0;
                    }
                }
            } while (cursor2.moveToNext());
            cursor2.close();
            deleteAllData();
        } catch (SQLiteException x) {
            x.printStackTrace();
        }
    }

    public void deleteAllData() {
        try {
            Cursor cursor2 = this.db.rawQuery("select name from SQLite_Master", (String[]) null);
            if (cursor2.moveToFirst()) {
                do {
                    String tablename = cursor2.getString(0);
                    if (!tablename.equals("android_metadata")) {
                        this.db.delete(tablename, (String) null, (String[]) null);
                    }
                } while (cursor2.moveToNext());
            }
            if (cursor2 != null && !cursor2.isClosed()) {
                cursor2.close();
            }
        } catch (SQLiteException x) {
            x.printStackTrace();
        }
    }

    private void writeToFile(String data, String _dir, String exportFileName) {
        try {
            PApplet.print(".");
            FileWriter fw = new FileWriter(String.valueOf(_dir) + "/" + exportFileName + ".csv", true);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(data);
            out.close();
            fw.close();
        } catch (Exception x) {
            PApplet.println("Error exporting data. (" + x.getMessage() + ") Check the sketch permissions or that the device is not connected in disk mode.");
        }
    }

    private class OpenHelper extends SQLiteOpenHelper {
        OpenHelper(Context context) {
            super(context, KetaiSQLite.this.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        }

        OpenHelper(Context context, String dbname) {
            super(context, dbname, (SQLiteDatabase.CursorFactory) null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
