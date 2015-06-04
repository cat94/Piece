package nju.com.piece;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shen on 15/6/4.
 */
public class IconImageAdaptor extends ArrayAdapter<Integer> {

    Context context;
    int layoutResourceId;
    List<Integer> images = new ArrayList<Integer>();


    public IconImageAdaptor(Context context, int resource, List<Integer> images) {
        super(context, resource, images);
        this.context = context;
        this.layoutResourceId = resource;
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout view = (LinearLayout)convertView;

        if (view == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = (LinearLayout)inflater.inflate(layoutResourceId, parent, false);
        }

        ImageView icon = (ImageView)view.findViewById(R.id.icon_img);
        icon.setImageResource(images.get(position));

        return view;
    }
}
