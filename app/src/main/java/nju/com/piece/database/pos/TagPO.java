package nju.com.piece.database.pos;

import java.util.Date;

import nju.com.piece.database.TagType;

/**
 * Created by shen on 15/6/5.
 */
public class TagPO {
    private String tagName;
    private TagType type;
    private int targetMinute=0;
    private int resource;
    private int currentMinute =0;
    private Date start_date;
    private Date end_date;

    /**
     * tag with no end date
     * @param tagName
     * @param type
     * @param resource
     */
    public TagPO(String tagName, TagType type, int resource) {
        this.tagName = tagName;
        this.type = type;
        this.resource = resource;
    }

    /**
     * tag with end date
     * @param tagName
     * @param type
     * @param targetMinute
     * @param resource
     * @param endDate
     */
    public TagPO(String tagName, TagType type, int resource,int targetMinute,Date endDate) {
        this.tagName = tagName;
        this.type = type;
        this.targetMinute = targetMinute;
        this.resource = resource;
        this.end_date = endDate;
    }

    public void setStartDate(Date start_date){
        this.start_date = start_date;
    }

    public Date getStartDate() {
        return start_date;
    }

    public Date getEndDate() {
        return end_date;
    }

    public void setCurrentMinute(int current_minute) {
        this.currentMinute = current_minute;
    }

    public String getTagName() {
        return tagName;
    }

    public TagType getType() {
        return type;
    }

    public int getTargetMinute() {
        return targetMinute;
    }

    public int getResource() {
        return resource;
    }

    public int getCurrentMinute() {
        return currentMinute;
    }
}
