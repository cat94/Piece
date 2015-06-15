package nju.com.piece.database;


import nju.com.piece.activity.TimeLineActivity;

/**
 * Created by shen on 15/6/6.
 */
public enum TagType {
    work,relax;

    public String getAllTimeField(){
        String field = "";
        switch (this) {
            case work:
                field = TimeLineActivity.ALL_WORK_TIME;
                break;
            case relax:
                field = TimeLineActivity.ALL_RELAX_TIME;
                break;
        }
        return field;
    }
}
