package nju.com.piece.database.tools;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by shen on 15/6/5.
 */
public class DateTool {
    private static Calendar calendar = Calendar.getInstance();

    public static long Date2Millis(Date date){
        calendar.clear();

        calendar.setTime(date);

        return calendar.getTimeInMillis();
    }

    public static Date Millis2Date(long millis){
        calendar.clear();

        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    public static Date currentDate(){
        calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static Date increDate(Date date,int day_num){
        calendar.clear();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,day_num);
        return calendar.getTime();
    }

    /**
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return the millis
     */
    public static long getMills(int year, int month, int day, int hour, int minute,int second){
        calendar.clear();
        calendar.set(year, month, day, hour, minute,second);
        return calendar.getTimeInMillis();
    }

    public static int getDay(Date date){
        calendar.clear();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(Date date){
        calendar.clear();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int getYear(Date date){
        calendar.clear();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
}
