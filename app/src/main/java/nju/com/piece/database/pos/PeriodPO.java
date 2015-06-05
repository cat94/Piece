package nju.com.piece.database.pos;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by shen on 15/6/5.
 *
 * the date param can be overlook when store: the db helper will save the current time
 *
 */
public class PeriodPO {
    private String tag;
    private int length;

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
