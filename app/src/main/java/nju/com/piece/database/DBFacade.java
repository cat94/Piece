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

    public void updatePeriod(PeriodPO po, int newLength){
        periodHelperInstance().updatePeriod(po.getDate(),newLength);

        increCurrentSum(po.getTag(), (-1) * po.getLength());
        increCurrentSum(po.getTag(),newLength);
    }

    public void delPeriod(PeriodPO po){
        periodHelperInstance().delPeriod(po.getDate());

        increCurrentSum(po.getTag(), (-1)*po.getLength());
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
        tagDBHelperInstance().increCurrent(tagName, increment/60);
    }

    public TagPO getTag(String tagName){
        return tagDBHelperInstance().getTag(tagName);
    }

    public void addTag(TagPO po){
        tagDBHelperInstance().addTag(po);
    }

    public void delTag(String tagName){
        tagDBHelperInstance().delTag(tagName);
    }

    public List<TagPO> getAllTags(){
        return tagDBHelperInstance().getAllTags();
    }

    public List<PeriodPO> getAllPeriods(){
        return periodHelperInstance().getAllPeriods();
    }

    public void setAccount(AccountPO po) {
        accountDBHelperInstance().setAccount(po);
    }

    public List<PeriodPO> getLastWeekPeriods(){
        return periodHelperInstance().getLastWeekPeriod();
    }

    public List<PeriodPO> getLastSevenDaysPeriods(){
        return  periodHelperInstance().getLastSevenDay();
    }

    public List<PeriodPO> getLastMonthPeriods(){
        return periodHelperInstance().getLastMonthPeriod();
    }

    public List<PeriodPO> getLastSeasonPeriods(){
        return  periodHelperInstance().getLastSeasonPeroids();
    }

    public List<PeriodPO> getAllPeriods(String tagName){
        return periodHelperInstance().getAllPeriods(tagName);
    }

    public List<PeriodPO> getLastWeekPeriods(String tagName){
        return periodHelperInstance().getLastWeekPeriod(tagName);
    }

    public List<PeriodPO> getLastSevenDaysPeriods(String tagName){
        return  periodHelperInstance().getLastSevenDay(tagName);
    }

    public List<PeriodPO> getLastMonthPeriods(String tagName){
        return periodHelperInstance().getLastMonthPeriod(tagName);
    }

    public List<PeriodPO> getLastSeasonPeriods(String tagName){
        return  periodHelperInstance().getLastSeasonPeroids(tagName);
    }

    public AccountPO getAccount(String username){
        return accountDBHelperInstance().getAccount(username);
    }

    public void updatePassword(String username, String newPswd){
        accountDBHelperInstance().updatePswd(username, newPswd);
    }

    public void updateTag(String oldTag, TagPO newTag){
        tagDBHelperInstance().updateTag(oldTag,newTag);
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
