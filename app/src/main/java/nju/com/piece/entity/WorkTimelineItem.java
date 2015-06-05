package nju.com.piece.entity;

/**
 * Created by hyl on 15/6/4.
 */
public class WorkTimelineItem extends TimelineItem{

    public WorkTimelineItem(String type, String label, int iconId, int second) {
        super(type, label, iconId, second);
    }

    public String getTimeField() {
        return TimelineItem.WORK_TIME;
    }
}
