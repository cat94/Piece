package nju.com.piece.database.pos;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import nju.com.piece.database.tools.DateTool;

/**
 * Created by shen on 15/6/5.
 *
 * the date param can be overlook when store: the db helper will save the current time
 *
 */
public class PeriodPO {
//    标签名
    private String tag;
//    时间段长度，以分钟计算
    private int length;

//    发生时间，不需要关心，数据库自动处理
    private Date date;

    public PeriodPO(String tag, int length) {
        this.tag = tag;
        this.length = length;
        this.date = DateTool.currentDate();
    }

    public PeriodPO(String tag, int length, Date date) {
        this.tag = tag;
        this.length = length;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public String getTag() {
        return tag;
    }

    public int getLength() {
        return length;
    }

}
