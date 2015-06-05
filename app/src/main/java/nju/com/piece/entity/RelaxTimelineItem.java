package nju.com.piece.entity;

/**
 * Created by hyl on 15/6/4.
 */
public class RelaxTimelineItem extends TimelineItem {

    public RelaxTimelineItem(String type, String label, int iconId, int second) {
        super(type, label, iconId, second);
    }

    public String getTimeField() {
        return TimelineItem.RELAX_TIME;
    }
}
