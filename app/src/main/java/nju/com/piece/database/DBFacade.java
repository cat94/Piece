package nju.com.piece.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nju.com.piece.database.helpers.AccountDBHelper;
import nju.com.piece.database.helpers.PeriodDBHelper;
import nju.com.piece.database.helpers.TagInfoDBHelper;
import nju.com.piece.database.pos.AccountPO;
import nju.com.piece.database.pos.PeriodPO;
import nju.com.piece.database.pos.TagPO;

/**
 * Created by shen on 15/6/5.
 */
public class DBFacade {
    Context context;

    private PeriodDBHelper periodDBHelper;
    private TagInfoDBHelper tagInfoDBHelper;
    private AccountDBHelper accountDBHelper;

    public DBFacade(Context context){
        this.context = context;
    }

    public void addPeriod(PeriodPO po){
        periodHelperInstance().addPeriod(po);

        increCurrentSum(po.getTag(), po.getLength());
    }


    public ArrayList<PeriodPO> getPeriodsByDate(Date date){
        return periodHelperInstance().getPeriodsByDate(date);
    }

    public void updateEndDate(String tagName, Date endDate){
        tagDBHelperInstance().updateEndDate(tagName, endDate);
    }

    public void updateTarget(String tagName, int newTarget){
        tagDBHelperInstance().updateTarget(tagName, newTarget);
    }

    private void increCurrentSum(String tagName, int increment){
        tagDBHelperInstance().increCurrent(tagName, increment);
    }

    public TagPO getTag(String tagName){
        return tagDBHelperInstance().getTag(tagName);
    }

    public void addTag(TagPO po){
        tagDBHelperInstance().addTag(po);
    }

    public List<TagPO> getAllTags(){
        return tagDBHelperInstance().getAllTags();
    }

    public List<PeriodPO> getAllPeriods(){
        return periodHelperInstance().getAllPeriods();
    }


    public void addAccount(AccountPO po){
        accountDBHelperInstance().addAccount(po);
    }
    public void delAccount(String username){
        accountDBHelperInstance().delAccount(username);
    }
    public AccountPO getAccount(String username){
        return accountDBHelperInstance().getAccount(username);
    }
    public void updatePassword(String username, String newPswd){
        accountDBHelperInstance().updatePswd(username, newPswd);
    }

    public void updateTagName(String oldTag, String newTag){
        tagDBHelperInstance().updateTagName(oldTag,newTag);
    }

    private PeriodDBHelper periodHelperInstance(){
        if (periodDBHelper == null)
            periodDBHelper = PeriodDBHelper.instance(context);
        return periodDBHelper;
    }

    private TagInfoDBHelper tagDBHelperInstance(){
        if (tagInfoDBHelper == null)
            tagInfoDBHelper = TagInfoDBHelper.instance(context);
        return tagInfoDBHelper;
    }

    private AccountDBHelper accountDBHelperInstance(){
        if (accountDBHelper == null)
            accountDBHelper = AccountDBHelper.instance(context);
        return accountDBHelper;
    }


}
