package nju.com.piece.database.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shen on 15/6/4.
 *
 * usage: XXXDBHelper.instance(this) ———— "this" is the context
 *
 */
public abstract class DatabaseHelper extends SQLiteOpenHelper {
    protected static final int DATABASE_VERSION = 1;

    protected static String currentUser;

    protected String DATABASE_CREATE = DatabaseCreate();

    protected abstract String DatabaseCreate();

    public static void setCurrentUser(String user){
        currentUser = user;
    }

    protected DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

//    when update the database structure, should change the old data here
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Log.w("TaskDBAdapter", "Upgrading from version "+ oldVersion +" to "+newVersion+", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_CREATE);

        onCreate(db);
    }
}
