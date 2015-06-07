package nju.com.piece.database.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nju.com.piece.database.TagType;
import nju.com.piece.database.tools.DateTool;
import nju.com.piece.database.pos.TagPO;

/**
 * Created by shen on 15/6/5.
 */
public class TagInfoDBHelper extends DatabaseHelper {
    protected static final String TABLE_NAME = "tag_info";
    private static final String KEY_ID = "_id";
    private static final String COL_TAG = "tag";
    private static final String COL_TYPE = "type";
    private static final String COL_RES = "resource_id";
    private static final String COL_TARGET = "target";
    private static final String COL_CURRENT = "current";
    private static final String COL_START_DATE = "start_date";
    private static final String COL_END_DATE = "end_date";

    protected final static String DATABASE_NAME = "tagInfo.db";

    public static TagInfoDBHelper instance(Context context) {
        return new TagInfoDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    protected TagInfoDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    protected String DatabaseCreate() {
        return "create table " + TABLE_NAME +
                " (" + KEY_ID + " integer primary key autoincrement, " +
                COL_TAG + " TEXT not null, " + COL_TYPE + " TEXT not null, " + COL_RES + " INTEGER not null," +
                COL_TARGET + " INTEGER," + COL_CURRENT + " INTEGER not null, " + COL_START_DATE + " INTEGER not null," +
                COL_END_DATE + " INTEGER );";
    }

    /**
     * caution: do not insert 2 same tagName, can use "getTag" method to check if exists
     *
     * @param po
     */
    public void addTag(TagPO po) {
        ContentValues cv = new ContentValues();
        cv.put(COL_TAG, po.getTagName());
        cv.put(COL_TYPE, po.getType().toString());
        cv.put(COL_RES, po.getResource());
        cv.put(COL_TARGET, po.getTargetMinute());
        cv.put(COL_CURRENT, po.getCurrentMinute());
        cv.put(COL_START_DATE, System.currentTimeMillis());
        if (null != po.getEndDate())
            cv.put(COL_END_DATE, DateTool.Date2Millis(po.getEndDate()));

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, "_id", cv);
    }

    /**
     * return null if not exist
     *
     * @param tagName the name of tag
     * @return TagPO
     */
    public TagPO getTag(String tagName) {
        String[] result_cols = new String[]{COL_TAG, COL_TYPE, COL_RES, COL_TARGET, COL_CURRENT, COL_START_DATE, COL_END_DATE};
        String where = COL_TAG + "=?";
        String[] whereArgs = new String[]{tagName};
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        if (cursor.moveToNext()) {
            return getPOByCursor(db, cursor);
        } else {
            db.close();
            return null;
        }
    }

    private TagPO getPOByCursor(SQLiteDatabase db, Cursor cursor) {
        int index_tag = cursor.getColumnIndex(COL_TAG);
        int index_type = cursor.getColumnIndex(COL_TYPE);
        int index_res = cursor.getColumnIndex(COL_RES);
        int index_target = cursor.getColumnIndex(COL_TARGET);
        int index_current = cursor.getColumnIndex(COL_CURRENT);
        int index_start = cursor.getColumnIndex(COL_START_DATE);
        int index_end = cursor.getColumnIndex(COL_END_DATE);


        String tag = cursor.getString(index_tag);
        String typeStr = cursor.getString(index_type);

        TagType type = TagType.valueOf(typeStr);

        int res = cursor.getInt(index_res);
        int target = 0;
        int current = cursor.getInt(index_current);
        Date start = DateTool.Millis2Date(cursor.getLong(index_start));

        TagPO po;

//            if has target
        if (index_target > -1 && index_end > -1) {
            target = cursor.getInt(index_target);
            Date end = DateTool.Millis2Date(cursor.getLong(index_end));
            po = new TagPO(tag, type, res, target, end);
        } else {
            po = new TagPO(tag, type, res);
        }

        po.setStartDate(start);
        po.setCurrentMinute(current);

        db.close();
        return po;
    }

    /**
     * increment the current time
     *
     * @param tagName
     * @param increment
     */
    public void increCurrent(String tagName, int increment) {
        ContentValues cv = new ContentValues();
        cv.put(COL_CURRENT, getTag(tagName).getCurrentMinute() + increment);

        String where = COL_TAG + " = ?";
        String[] whereArgs = new String[]{tagName};

        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, cv, where, whereArgs);
        db.close();
    }

    /**
     * update the target time
     *
     * @param tagName
     * @param newTarget
     */
    public void updateTarget(String tagName, int newTarget) {
        ContentValues cv = new ContentValues();
        cv.put(COL_TARGET, newTarget);

        String where = COL_TAG + " = ?";
        String[] whereArgs = new String[]{tagName};

        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, cv, where, whereArgs);
        db.close();
    }

    /**
     * update the end date
     *
     * @param tagName
     * @param endDate
     */
    public void updateEndDate(String tagName, Date endDate) {
        ContentValues cv = new ContentValues();
        cv.put(COL_END_DATE, DateTool.Date2Millis(endDate));

        String where = COL_TAG + " = ?";
        String[] whereArgs = new String[]{tagName};

        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, cv, where, whereArgs);
        db.close();
    }

    /**
     * get all the tag info
     * @return
     */
    public List<TagPO> getAllTags() {
        String[] result_cols = new String[]{COL_TAG, COL_TYPE, COL_RES, COL_TARGET, COL_CURRENT, COL_START_DATE, COL_END_DATE};
        String where = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        List<TagPO> tags = new ArrayList<TagPO>();

        while (cursor.moveToNext())
            tags.add(getPOByCursor(db, cursor));

        return tags;
    }

}
