package nju.com.piece.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nju.com.piece.R;
import nju.com.piece.adapter.adapterEntity.StatisticItem;

/**
 * Created by Jianwei on 2015/6/6.
 */
public class ItemListAdapter extends ArrayAdapter<StatisticItem>{
    private List<StatisticItem> list;
    private int resource;
    private Context context;

    public ItemListAdapter(Context context, int resource, List<StatisticItem> objects) {
               super(context, resource, objects);
            this.context=context;
        this.resource=resource;
        list=objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout view = (LinearLayout)convertView;

        if (view == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = (LinearLayout)inflater.inflate(resource, parent, false);
        }
        ImageView icon=(ImageView)view.findViewById(R.id.stat_item_icon);
        TextView itemName=(TextView)view.findViewById(R.id.item_name);
        TextView itemPercent=(TextView)view.findViewById(R.id.item_percent);
        TextView color=(TextView)view.findViewById(R.id.stat_item_color);
        StatisticItem statisticItem=list.get(position);
        icon.setImageResource(statisticItem.getResourceID());
        itemName.setText(statisticItem.getItemName());
        itemPercent.setText(statisticItem.getPercentage());
        color.setBackgroundColor(statisticItem.getColorID());

        return view;
    }
}
