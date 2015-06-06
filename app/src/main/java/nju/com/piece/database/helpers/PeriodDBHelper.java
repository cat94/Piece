package nju.com.piece.database.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import nju.com.piece.database.tools.DateTool;
import nju.com.piece.database.pos.PeriodPO;

/**
 * Created by shen on 15/6/5.
 */
public class PeriodDBHelper extends DatabaseHelper {

    private static final String KEY_ID = "_id";
    protected static final String TABLE_NAME = "period";
    private static final String COL_TAG = "tag";
    private static final String COL_LEN = "length";
    private static final String COL_DATE = "start_time";

    protected final static String DATABASE_NAME = "myDB.db";

    public static PeriodDBHelper instance(Context context){
        return new PeriodDBHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    protected String DatabaseCreate() {
        return "create table " + TABLE_NAME +
                " ("+KEY_ID+" integer primary key autoincrement, " +
                COL_TAG +" TEXT not null, " + COL_LEN +" INTEGER not null, "+ COL_DATE +" INTEGER not null);";
    }

    protected PeriodDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public void addPeriod(PeriodPO po){
        ContentValues cv = new ContentValues();
        cv.put(COL_TAG,po.getTag());
        cv.put(COL_LEN, po.getLength());
        cv.put(COL_DATE, System.currentTimeMillis());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, "_id", cv);
        db.close();
    }

    public ArrayList<PeriodPO> getPeriodsByDate(Date date){

        int year = DateTool.getYear(date);
        int month = DateTool.getMonth(date);
        int day = DateTool.getDay(date);

        long pre_time, after_time;

        pre_time = DateTool.getMills(year, month, day, 0, 0, 0);

        after_time = DateTool.Date2Millis(DateTool.increDate(date, 1));

        String[] result_cols = new String[]{COL_TAG,COL_LEN,COL_DATE};
        String where = COL_DATE + " BETWEEN ? AND ?";
        String[] whereArgs = new String[]{pre_time+"",after_time+""};
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        int index_tag = cursor.getColumnIndex(COL_TAG);
        int index_len = cursor.getColumnIndex(COL_LEN);
        int index_date = cursor.getColumnIndex(COL_DATE);


        ArrayList<PeriodPO> results = new ArrayList<PeriodPO>();

        while(cursor.moveToNext()){
            String tag = cursor.getString(index_tag);
            int len = cursor.getInt(index_len);
            long date_millis = cursor.getLong(index_date);

            PeriodPO po = new PeriodPO(tag,len);
            po.setDate(date_millis);

            results.add(po);
        }

        db.close();

        return results;
    }


}