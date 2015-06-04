package nju.com.piece;

/**
 * Created by shen on 15/6/4.
 */
public class Item {
    private int resource;
    private int selected;

    public Item(int resource, int selected) {
        this.resource = resource;
        this.selected = selected;
    }

    public int getResource() {
        return resource;
    }

    public int getSelected() {
        return selected;
    }
}
