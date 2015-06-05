package nju.com.piece.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import nju.com.piece.database.helpers.PeriodDBHelper;
import nju.com.piece.database.helpers.TagInfoDBHelper;
import nju.com.piece.database.pos.PeriodPO;
import nju.com.piece.database.pos.TagPO;

/**
 * Created by shen on 15/6/5.
 */
public class DBFacade {
    Context context;

    private PeriodDBHelper periodDBHelper;
    private TagInfoDBHelper tagInfoDBHelper;

    public DBFacade(Context context){
        this.context = context;
    }

    public void addPeriod(PeriodPO po){
        if (periodDBHelper == null)
            periodDBHelper = PeriodDBHelper.instance(context);

        periodDBHelper.addPeriod(po);

        increCurrentSum(po.getTag(), po.getLength());
    }


    public ArrayList<PeriodPO> getPeriodsByDate(Date date){
        if (periodDBHelper == null)
            periodDBHelper = PeriodDBHelper.instance(context);

        return periodDBHelper.getPeriodsByDate(date);
    }

    public void updateEndDate(String tagName, Date endDate){
        if (tagInfoDBHelper == null)
            tagInfoDBHelper = TagInfoDBHelper.instance(context);

        tagInfoDBHelper.updateEndDate(tagName, endDate);
    }

    public void updateTarget(String tagName, int newTarget){
        if (tagInfoDBHelper == null)
            tagInfoDBHelper = TagInfoDBHelper.instance(context);

        tagInfoDBHelper.updateTarget(tagName, newTarget);
    }

    private void increCurrentSum(String tagName, int increment){
        if (tagInfoDBHelper == null)
            tagInfoDBHelper = TagInfoDBHelper.instance(context);

        tagInfoDBHelper.increCurrent(tagName, increment);
    }

    public TagPO getTag(String tagName){
        if (tagInfoDBHelper == null)
            tagInfoDBHelper = TagInfoDBHelper.instance(context);

        return tagInfoDBHelper.getTag(tagName);
    }

    public void addTag(TagPO po){
        if (tagInfoDBHelper == null)
            tagInfoDBHelper = TagInfoDBHelper.instance(context);

        tagInfoDBHelper.addTag(po);
    }
}
