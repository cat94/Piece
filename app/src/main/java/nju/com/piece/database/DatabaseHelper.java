package nju.com.piece.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shen on 15/6/4.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myDB.db";
    private static final String DATABASE_TABLE = "my_table";
    private static final int DATABASE_VERSION = 1;

    private static final String KEY_ID = "_id";
    private static final String KEY_COL1 = "col1";
    private static final String KEY_COL2 = "col2";

    private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE +
            " ("+KEY_ID+" integer primary key autoincrement, " +
            KEY_COL1+" text not null, " + KEY_COL2+" float);";


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version "+ oldVersion +" to "+newVersion+", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_CREATE);

        onCreate(db);
    }
}
