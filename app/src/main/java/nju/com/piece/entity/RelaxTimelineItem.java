package nju.com.piece.entity;

import java.sql.Time;

/**
 * Created by hyl on 15/6/4.
 */
public class RelaxTimelineItem extends TimelineItem {

    public RelaxTimelineItem(String type, String label, int iconId, int second) {
        super(type, label, iconId, second);
    }

    @Override
    public String getTimeField() {
        return TimelineItem.RELAX_TIME;
    }

    @Override
    public String getAllTimeField() {
        return Timeline.ALL_RELAX_TIME;
    }
}
