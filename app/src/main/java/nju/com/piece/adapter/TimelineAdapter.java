package nju.com.piece.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nju.com.piece.activity.PeriodItemActivity;
import nju.com.piece.R;
import nju.com.piece.activity.TimeLineActivity;
import nju.com.piece.adapter.adapterEntity.TimelineItem;
import nju.com.piece.database.TagType;

/**
 * Created by hyl on 15/6/12.
 */
public class TimelineAdapter extends ArrayAdapter<TimelineItem> {

    private List<TimelineItem> list;
    private int resource;
    private Context context;

    public TimelineAdapter(Context context, int resource, List<TimelineItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout view = (LinearLayout) convertView;
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = (LinearLayout) inflater.inflate(resource, parent, false);
        }
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        TextView relax = (TextView) view.findViewById(R.id.relax);
        TextView relaxTime = (TextView) view.findViewById(R.id.relax_time);
        TextView work = (TextView) view.findViewById(R.id.work);
        TextView workTime = (TextView) view.findViewById(R.id.work_time);
        TimelineItem timelineItem = list.get(position);
        if (timelineItem.getType().equals(TagType.relax.toString())) {
            work.setText("");
            workTime.setText("");
            relax.setText(timelineItem.getName());
            if (timelineItem.getLength() > 0) {
                relaxTime.setText(TimeLineActivity.FormatSecond(timelineItem.getLength()));
            } else {
                relaxTime.setText("");
            }
        } else if (timelineItem.getType().equals(TagType.work.toString())) {
            work.setText(timelineItem.getName());
            if (timelineItem.getLength() > 0) {
                workTime.setText(TimeLineActivity.FormatSecond(timelineItem.getLength()));
            } else {
                workTime.setText("");
            }
            relax.setText("");
            relaxTime.setText("");
        }
        icon.setImageResource(timelineItem.getIcon());
        final int index = position;
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PeriodItemActivity.class);
                intent.putExtra("index", index);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
