package nju.com.piece.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nju.com.piece.R;
import nju.com.piece.adapter.adapterEntity.TimelineItem;
import nju.com.piece.entity.Timeline;

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
        final ImageView delBtn = (ImageView) view.findViewById(R.id.del_btn);
        final ImageView editBtn = (ImageView) view.findViewById(R.id.edit_btn);
        final LinearLayout relaxLayout = (LinearLayout) view.findViewById(R.id.relax_layout);
        final LinearLayout workLayout = (LinearLayout) view.findViewById(R.id.work_layout);
        TextView relax = (TextView) view.findViewById(R.id.relax);
        TextView relaxTime = (TextView) view.findViewById(R.id.relax_time);
        TextView work = (TextView) view.findViewById(R.id.work);
        TextView workTime = (TextView) view.findViewById(R.id.work_time);
        TimelineItem timelineItem = list.get(position);
        if (timelineItem.getType().equals(Timeline.RELAX_TYPE)) {
            relax.setText(timelineItem.getName());
            relaxTime.setText(Timeline.FormatSecond(timelineItem.getLength()));
        } else if (timelineItem.getType().equals(Timeline.WORK_TYPE)) {
            work.setText(timelineItem.getName());
            workTime.setText(Timeline.FormatSecond(timelineItem.getLength()));
        }
        icon.setImageResource(timelineItem.getIcon());
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet leftAnimationSet = new AnimationSet(true);
                TranslateAnimation leftAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 3f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f);
                leftAnimation.setDuration(500);
                leftAnimationSet.addAnimation(leftAnimation);

                AnimationSet rightAnimationSet = new AnimationSet(true);
                TranslateAnimation rightAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, -3f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f);
                rightAnimation.setDuration(500);
                rightAnimationSet.addAnimation(rightAnimation);

                relaxLayout.setVisibility(View.INVISIBLE);
                workLayout.setVisibility(View.INVISIBLE);

                delBtn.setVisibility(View.VISIBLE);
                delBtn.startAnimation(leftAnimationSet);

                editBtn.setVisibility(View.VISIBLE);
                editBtn.startAnimation(rightAnimationSet);
            }
        });
        return view;
    }

}
