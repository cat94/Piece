package nju.com.piece.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import nju.com.piece.adapter.adapterEntity.IconItem;
import nju.com.piece.R;
import nju.com.piece.TagIconView;

/**
 * Created by shen on 15/6/4.
 */
public class IconImageAdaptor extends ArrayAdapter<IconItem> {

    Context context;
    int layoutResourceId;
    List<IconItem> images = new ArrayList<IconItem>();

    private static int current_selected_res;

    public IconImageAdaptor(Context context, int resource, List<IconItem> images) {
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

//        view.setBackgroundResource(R.drawable.selected_bkg2);
        TagIconView icon = (TagIconView)view.findViewById(R.id.icon_img);

        IconItem currentIconItem = images.get(position);

        int res = currentIconItem.getResource();
        icon.setImageResource(res);
        icon.setTag(res);

        view.setClickable(true);
        view.setOnClickListener(new IconClickListener());

//        if (res == IconsArray.currentIcon) {
//            IconClickListener.click(view);
//        }

        return view;
    }

    public static void clearSelecetedRes(){
        current_selected_res = 0;
    }

    public static int getSelectedRes(){
        return current_selected_res;
    }

    public static class IconClickListener implements View.OnClickListener {
        private static View pre_view = null;

        @Override
        public void onClick(View v) {
            click(v);

        }
        public static void click(View v){
            if (null != pre_view)
                deselect();
            v.setBackgroundResource(R.drawable.selected_bkg);
            pre_view = v;

            current_selected_res = (Integer)v.findViewById(R.id.icon_img).getTag();

        }

        private static void deselect(){
            pre_view.setBackgroundResource(0);
        }
    }
}
