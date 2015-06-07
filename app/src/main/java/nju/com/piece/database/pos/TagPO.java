package nju.com.piece.database.pos;

import java.util.Date;

import nju.com.piece.database.TagType;

/**
 * Created by shen on 15/6/5.
 */
public class TagPO {
    private String tagName;
//    标签类型(工作/放松)
    private TagType type;
//    目标时间，没有则为空
    private int targetMinute=0;
//    图标的resource
    private int resource;
//    当前累积的时间，构造函数不需要处理此变量
    private int currentMinute =0;
//    开始时间，构造函数不需要处理此变量，数据库自动处理
    private Date start_date;
//    结束时间，若没有则使用不含end date的构造函数
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
