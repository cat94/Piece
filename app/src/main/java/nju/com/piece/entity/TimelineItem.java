//package nju.com.piece.entity;
//
//
//import nju.com.piece.R;
//
///**
// * Created by hyl on 15/6/4.
// */
//public abstract class TimelineItem {
//
//    public static final int DEFAULT_ICON = R.drawable.default_icon;
//    public static final int WORK_ICON = R.drawable.work_icon;
//    public static final int RELAX_ICON = R.drawable.play_icon;
//
//    private String type;
//    private String label;
//    private int iconId;
//    private int second;
//
//    public TimelineItem(String type, String label, int iconId, int second) {
//        this.type = type;
//        this.label = label;
//        this.iconId = iconId;
//        this.second = second;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public String getLabel() {
//        return label;
//    }
//
//    public int getIconId() {
//        return iconId;
//    }
//
//    public int getSecond() {
//        return second;
//    }
//
//    public abstract String getTimeField();
//
//    public abstract String getAllTimeField();
//}
