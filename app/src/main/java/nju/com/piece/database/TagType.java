package nju.com.piece.database;

import nju.com.piece.entity.Timeline;

/**
 * Created by shen on 15/6/6.
 */
public enum TagType {
    work,relax;

    public String getTimeField(){
        String field = "";
        switch (this) {
            case work:
                field = Timeline.WORK_TIME;
                break;
            case relax:
                field = Timeline.RELAX_TIME;
                break;
        }
        return field;
    }

    public String getAllTimeField(){
        String field = "";
        switch (this) {
            case work:
                field = Timeline.ALL_WORK_TIME;
                break;
            case relax:
                field = Timeline.ALL_RELAX_TIME;
                break;
        }
        return field;
    }
}
