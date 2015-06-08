package nju.com.piece.database.pos;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shen on 15/6/5.
 *
 * the date param can be overlook when store: the db helper will save the current time
 *
 */
public class PeriodPO {
//    标签名
    private String tag;
//    时间段长度
    private int length;

//    发生时间，不需要关心，数据库自动处理
    private Date date;

    public PeriodPO(String tag, int length) {
        this.tag = tag;
        this.length = length;
    }

    public void setDate(long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        date = calendar.getTime();
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
