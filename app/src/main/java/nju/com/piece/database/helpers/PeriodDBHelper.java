package nju.com.piece.database.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    protected static String DATABASE_NAME;

    public static PeriodDBHelper instance(Context context){
        DATABASE_NAME = "period_"+currentUser+".db";
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
        cv.put(COL_DATE, DateTool.Date2Millis(po.getDate()));

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, "_id", cv);
        db.close();
    }

    public void updatePeriod(Date periodDate, int newLength){
        ContentValues cv = new ContentValues();
        cv.put(COL_LEN, newLength);

        String where = COL_DATE + " = ?";
        String[] whereArgs = new String[]{DateTool.Date2Millis(periodDate)+""};

        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, cv, where, whereArgs);

        db.close();
    }

    public void delPeriod(Date periodDate){
        String where = COL_DATE + " = ?";
        String[] whereArgs = new String[]{DateTool.Date2Millis(periodDate)+""};

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, where, whereArgs);
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
        String order = COL_DATE+" DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        ArrayList<PeriodPO> results = new ArrayList<PeriodPO>();

        while(cursor.moveToNext()){
            results.add(getPeriodPOByCursor(db,cursor));
        }

        cursor.close();
        db.close();

        return results;
    }

    private PeriodPO getPeriodPOByCursor(SQLiteDatabase db,Cursor cursor){
        int index_tag = cursor.getColumnIndex(COL_TAG);
        int index_len = cursor.getColumnIndex(COL_LEN);
        int index_date = cursor.getColumnIndex(COL_DATE);

        String tag = cursor.getString(index_tag);
        int len = cursor.getInt(index_len);
        long date_millis = cursor.getLong(index_date);

        Date date = DateTool.Millis2Date(date_millis);

        PeriodPO po = new PeriodPO(tag,len,date);

        return po;
    }

    public ArrayList<PeriodPO> getAllPeriods(){
        String[] result_cols = new String[]{COL_TAG,COL_LEN,COL_DATE};
        String where = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;
        String order = COL_DATE+" DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        ArrayList<PeriodPO> results = new ArrayList<PeriodPO>();

        while(cursor.moveToNext()){
            results.add(getPeriodPOByCursor(db,cursor));
        }

        cursor.close();
        db.close();

        return results;
    }


    public ArrayList<PeriodPO> getLastWeekPeriod(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        calendar.add(Calendar.DATE, -1 * calendar.get(Calendar.DAY_OF_WEEK) + 2);
        Date pre_date = calendar.getTime();
        calendar.add(Calendar.DATE, -1 * calendar.get(Calendar.DAY_OF_WEEK) + 8);
        Date after_date = calendar.getTime();

        int pre_year = DateTool.getYear(pre_date);
        int pre_month = DateTool.getMonth(pre_date);
        int pre_day = DateTool.getDay(pre_date);

        int after_year = DateTool.getYear(after_date);
        int after_month = DateTool.getMonth(after_date);
        int after_day = DateTool.getDay(after_date);

        long pre_time, after_time;

        pre_time = DateTool.getMills(pre_year, pre_month, pre_day, 0, 0, 0);

        after_time =DateTool.getMills(after_year, after_month, after_day, 0, 0, 0);

        String[] result_cols = new String[]{COL_TAG,COL_LEN,COL_DATE};
        String where = COL_DATE + " BETWEEN ? AND ?";
        String[] whereArgs = new String[]{pre_time+"",after_time+""};
        String groupBy = null;
        String having = null;
        String order = COL_DATE+" DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        ArrayList<PeriodPO> results = new ArrayList<PeriodPO>();

        while(cursor.moveToNext()){
            results.add(getPeriodPOByCursor(db,cursor));
        }
        cursor.close();
        db.close();
        return  results;
    }


    public ArrayList<PeriodPO> getLastMonthPeriod(){
        //获取前月的第一天
        Calendar   cal_1=Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, -1);
        Date pre_date = cal_1.getTime();
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DATE,0);//设置为1号,当前日期既为本月第一天
        Date after_date = cale.getTime();

        int pre_year = DateTool.getYear(pre_date);
        int pre_month = DateTool.getMonth(pre_date);
        int pre_day = DateTool.getDay(pre_date);

        int after_year = DateTool.getYear(after_date);
        int after_month = DateTool.getMonth(after_date);
        int after_day = DateTool.getDay(after_date);

        long pre_time, after_time;

        pre_time = DateTool.getMills(pre_year, pre_month, pre_day, 0, 0, 0);

        after_time =DateTool.getMills(after_year, after_month, after_day, 0, 0, 0);

        String[] result_cols = new String[]{COL_TAG,COL_LEN,COL_DATE};
        String where = COL_DATE + " BETWEEN ? AND ?";
        String[] whereArgs = new String[]{pre_time+"",after_time+""};
        String groupBy = null;
        String having = null;
        String order = COL_DATE+" DESC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        ArrayList<PeriodPO> results = new ArrayList<PeriodPO>();

        while(cursor.moveToNext()){
            results.add(getPeriodPOByCursor(db,cursor));
        }
        cursor.close();
        db.close();
        return  results;
    }

   public ArrayList<PeriodPO> getLastSeasonPeroids(){
       //获取前月的第一天
       Calendar   cal_1=Calendar.getInstance();//获取当前日期
       cal_1.add(Calendar.MONTH, -4);
       cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
       Date pre_date = cal_1.getTime();
       Calendar cale = Calendar.getInstance();
       cale.add(Calendar.MONTH, 1);
       cale.set(Calendar.DATE,0);//设置为1号,当前日期既为本月第一天
       Date after_date = cale.getTime();

       int pre_year = DateTool.getYear(pre_date);
       int pre_month = DateTool.getMonth(pre_date);
       int pre_day = DateTool.getDay(pre_date);

       int after_year = DateTool.getYear(after_date);
       int after_month = DateTool.getMonth(after_date);
       int after_day = DateTool.getDay(after_date);

       long pre_time, after_time;

       pre_time = DateTool.getMills(pre_year, pre_month, pre_day, 0, 0, 0);

       after_time =DateTool.getMills(after_year, after_month, after_day, 0, 0, 0);

       String[] result_cols = new String[]{COL_TAG,COL_LEN,COL_DATE};
       String where = COL_DATE + " BETWEEN ? AND ?";
       String[] whereArgs = new String[]{pre_time+"",after_time+""};
       String groupBy = null;
       String having = null;
       String order = COL_DATE+" DESC";
       SQLiteDatabase db = getReadableDatabase();
       Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

       ArrayList<PeriodPO> results = new ArrayList<PeriodPO>();

       while(cursor.moveToNext()){
           results.add(getPeriodPOByCursor(db,cursor));
       }
       cursor.close();
       db.close();
       return  results;
   }

    public ArrayList<PeriodPO> getLastSevenDay(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        Date pre_date = calendar.getTime();

        calendar.add(Calendar.DATE, 8);
        Date after_date = calendar.getTime();

        int pre_year = DateTool.getYear(pre_date);
        int pre_month = DateTool.getMonth(pre_date);
        int pre_day = DateTool.getDay(pre_date);

        int after_year = DateTool.getYear(after_date);
        int after_month = DateTool.getMonth(after_date);
        int after_day = DateTool.getDay(after_date);

        long pre_time, after_time;

        pre_time = DateTool.getMills(pre_year, pre_month, pre_day, 0, 0, 0);

        after_time =DateTool.getMills(after_year, after_month, after_day, 0, 0, 0);

        String[] result_cols = new String[]{COL_TAG,COL_LEN,COL_DATE};
        String where = COL_DATE + " BETWEEN ? AND ?";
        String[] whereArgs = new String[]{pre_time+"",after_time+""};
        String groupBy = null;
        String having = null;
        String order = COL_DATE+" DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        ArrayList<PeriodPO> results = new ArrayList<PeriodPO>();

        while(cursor.moveToNext()){
            results.add(getPeriodPOByCursor(db,cursor));
        }
        cursor.close();
        db.close();
        return  results;
    }

    public ArrayList<PeriodPO> getAllPeriods(String tagName){
        String[] result_cols = new String[]{COL_TAG,COL_LEN,COL_DATE};
        String where = COL_TAG+" = ? ";
        String[] whereArgs = {tagName};
        String groupBy = null;
        String having = null;
        String order = COL_DATE+" DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        ArrayList<PeriodPO> results = new ArrayList<PeriodPO>();

        while(cursor.moveToNext()){
            results.add(getPeriodPOByCursor(db,cursor));
        }

        cursor.close();
        db.close();

        return results;
    }


    public ArrayList<PeriodPO> getLastWeekPeriod(String tagName){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        calendar.add(Calendar.DATE, -1 * calendar.get(Calendar.DAY_OF_WEEK) + 2);
        Date pre_date = calendar.getTime();
        calendar.add(Calendar.DATE, -1 * calendar.get(Calendar.DAY_OF_WEEK) + 8);
        Date after_date = calendar.getTime();

        int pre_year = DateTool.getYear(pre_date);
        int pre_month = DateTool.getMonth(pre_date);
        int pre_day = DateTool.getDay(pre_date);

        int after_year = DateTool.getYear(after_date);
        int after_month = DateTool.getMonth(after_date);
        int after_day = DateTool.getDay(after_date);

        long pre_time, after_time;

        pre_time = DateTool.getMills(pre_year, pre_month, pre_day, 0, 0, 0);

        after_time =DateTool.getMills(after_year, after_month, after_day, 0, 0, 0);

        String[] result_cols = new String[]{COL_TAG,COL_LEN,COL_DATE};
        String where = COL_DATE + " BETWEEN ? AND ? AND "+COL_TAG+" = ? ";
        String[] whereArgs = new String[]{pre_time+"",after_time+"",tagName};
        String groupBy = null;
        String having = null;
        String order = COL_DATE+" DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        ArrayList<PeriodPO> results = new ArrayList<PeriodPO>();

        while(cursor.moveToNext()){
            results.add(getPeriodPOByCursor(db,cursor));
        }
        cursor.close();
        db.close();
        return  results;
    }


    public ArrayList<PeriodPO> getLastMonthPeriod(String tagName){
        //获取前月的第一天
        Calendar   cal_1=Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        Date pre_date = cal_1.getTime();
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DATE,0);//设置为1号,当前日期既为本月第一天
        Date after_date = cale.getTime();

        int pre_year = DateTool.getYear(pre_date);
        int pre_month = DateTool.getMonth(pre_date);
        int pre_day = DateTool.getDay(pre_date);

        int after_year = DateTool.getYear(after_date);
        int after_month = DateTool.getMonth(after_date);
        int after_day = DateTool.getDay(after_date);

        long pre_time, after_time;

        pre_time = DateTool.getMills(pre_year, pre_month, pre_day, 0, 0, 0);

        after_time =DateTool.getMills(after_year, after_month, after_day, 0, 0, 0);

        String[] result_cols = new String[]{COL_TAG,COL_LEN,COL_DATE};
        String where = COL_DATE + " BETWEEN ? AND ? AND "+COL_TAG+" = ? ";
        String[] whereArgs = new String[]{pre_time+"",after_time+"",tagName};
        String groupBy = null;
        String having = null;
        String order = COL_DATE+" DESC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        ArrayList<PeriodPO> results = new ArrayList<PeriodPO>();

        while(cursor.moveToNext()){
            results.add(getPeriodPOByCursor(db,cursor));
        }
        cursor.close();
        db.close();
        return  results;
    }

    public ArrayList<PeriodPO> getLastSeasonPeroids(String tagName){
        //获取前月的第一天
        Calendar   cal_1=Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, -4);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        Date pre_date = cal_1.getTime();
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DATE,0);//设置为1号,当前日期既为本月第一天
        Date after_date = cale.getTime();

        int pre_year = DateTool.getYear(pre_date);
        int pre_month = DateTool.getMonth(pre_date);
        int pre_day = DateTool.getDay(pre_date);

        int after_year = DateTool.getYear(after_date);
        int after_month = DateTool.getMonth(after_date);
        int after_day = DateTool.getDay(after_date);

        long pre_time, after_time;

        pre_time = DateTool.getMills(pre_year, pre_month, pre_day, 0, 0, 0);

        after_time =DateTool.getMills(after_year, after_month, after_day, 0, 0, 0);

        String[] result_cols = new String[]{COL_TAG,COL_LEN,COL_DATE};
        String where = COL_DATE + " BETWEEN ? AND ? AND "+COL_TAG+" = ? ";
        String[] whereArgs = new String[]{pre_time+"",after_time+"",tagName};
        String groupBy = null;
        String having = null;
        String order = COL_DATE+" DESC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        ArrayList<PeriodPO> results = new ArrayList<PeriodPO>();

        while(cursor.moveToNext()){
            results.add(getPeriodPOByCursor(db,cursor));
        }
        cursor.close();
        db.close();
        return  results;
    }

    public ArrayList<PeriodPO> getLastSevenDay(String tagName){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        Date pre_date = calendar.getTime();

        calendar.add(Calendar.DATE, 8);
        Date after_date = calendar.getTime();

        int pre_year = DateTool.getYear(pre_date);
        int pre_month = DateTool.getMonth(pre_date);
        int pre_day = DateTool.getDay(pre_date);

        int after_year = DateTool.getYear(after_date);
        int after_month = DateTool.getMonth(after_date);
        int after_day = DateTool.getDay(after_date);

        long pre_time, after_time;

        pre_time = DateTool.getMills(pre_year, pre_month, pre_day, 0, 0, 0);

        after_time =DateTool.getMills(after_year, after_month, after_day, 0, 0, 0);

        String[] result_cols = new String[]{COL_TAG,COL_LEN,COL_DATE};
        String where = COL_DATE + " BETWEEN ? AND ? AND "+COL_TAG+" = ? ";
        String[] whereArgs = new String[]{pre_time+"",after_time+"",tagName};
        String groupBy = null;
        String having = null;
        String order = COL_DATE+" DESC";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        ArrayList<PeriodPO> results = new ArrayList<PeriodPO>();

        while(cursor.moveToNext()){
            results.add(getPeriodPOByCursor(db,cursor));
        }
        cursor.close();
        db.close();
        return  results;
    }

    protected void updateTagName(String oldName,String newName){
        ContentValues cv = new ContentValues();
        cv.put(COL_TAG, oldName);

        String where = COL_TAG + " = ?";
        String[] whereArgs = new String[]{newName};

        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, cv, where, whereArgs);
        db.close();
    }

    protected void deletePeriodByTag(String tagName){
        String where = COL_TAG + " = ?";
        String[] whereArgs = new String[]{tagName};

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, where, whereArgs);
        db.close();
    }
}
