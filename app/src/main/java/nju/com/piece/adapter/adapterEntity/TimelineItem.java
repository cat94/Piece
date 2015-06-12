package nju.com.piece.adapter.adapterEntity;

/**
 * Created by hyl on 15/6/12.
 */
public class TimelineItem {

    private String type = "";
    private String name = "";
    private int length = 0;
    private int icon = 0;

    public TimelineItem(String type, String name, int icon) {
        this.type = type;
        this.name = name;
        this.icon = icon;
    }

    public TimelineItem(String type, String name, int length, int icon) {
        this.type = type;
        this.name = name;
        this.length = length;
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
