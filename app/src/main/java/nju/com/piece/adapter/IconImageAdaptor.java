package nju.com.piece.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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

    public static List<IconViewPair> iconViewPairs = new ArrayList<IconViewPair>();

    public static class IconViewPair {
        private int res;
        private LinearLayout view;
        public IconViewPair(int res, LinearLayout view){
            this.res = res;
            this.view = view;
        }

        public int getRes() {
            return res;
        }

        public LinearLayout getView() {
            return view;
        }
    }

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

        TagIconView icon = (TagIconView)view.findViewById(R.id.icon_img);

        IconItem currentIconItem = images.get(position);

        int res = currentIconItem.getResource();
        icon.setImageResource(res);
        icon.setTag(res);

        view.setClickable(true);
        view.setOnClickListener(new IconClickListener());


        if (findResView(res) == null)
            iconViewPairs.add(new IconViewPair(res, view));

        if (images.size()-1 == position){
            setCurrentIconImage();
        }

        return view;
    }


    private static int current_icon=-1;

    public static void setCurrentIconIndex(int icon_res){
        current_icon = icon_res;
    }

    private void setCurrentIconImage(){
        if (current_icon != -1) {
            LinearLayout view = findResView(current_icon);
            view.performClick();
            current_icon = -1;
            clearResView();
        }
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

    public static void clearResView(){
        iconViewPairs = new ArrayList<IconViewPair>();
//        iconViewPairs.clear();
    }

    public static LinearLayout findResView(int res){
        for (IconViewPair iconView: iconViewPairs){
            if (res == iconView.getRes())
                return iconView.getView();
        }
        return null;
    }
}
