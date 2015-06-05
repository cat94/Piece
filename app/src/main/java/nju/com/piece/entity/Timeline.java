package nju.com.piece.entity;

import android.content.Context;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nju.com.piece.R;

/**
 * Created by hyl on 15/6/4.
 */
public class Timeline {

    private Context context = null;
    private ListView timeline = null;
    private SimpleAdapter simAdapter = null;
    private List<Map<String, Object>> dataList = null;
    private Map<String, Object> nowMap = null;
    private TimelineItem nowItem= null;

    public Timeline(Context context, ListView timeline) {
        this.timeline = timeline;
        this.context = context;
        initDataList();
        initSimAdapter();
    }

    private void initSimAdapter() {
        simAdapter = new SimpleAdapter(context, dataList,
                R.layout.timeline_item,
                new String[]{TimelineItem.RELAX_TYPE, TimelineItem.RELAX_TIME,
                    TimelineItem.ICON, TimelineItem.WORK_TYPE, TimelineItem.WORK_TIME},
                new int[]{R.id.relax,R.id.relax_time, R.id.icon, R.id.work, R.id.work_time});
        timeline.setAdapter(simAdapter);
    }

    private List<Map<String, Object>> initDataList() {
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(TimelineItem.ICON, TimelineItem.RELAX_ICON);
            map.put(TimelineItem.RELAX_TYPE, "hanyilu" + i);
            dataList.add(map);
        }
        return dataList;
    }

    public void addItem(TimelineItem item) {
        nowItem = item;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(item.getType(), item.getLabel());
        map.put(TimelineItem.ICON, item.getIconId());
        dataList.add(0, map);
        this.nowMap = map;
        simAdapter.notifyDataSetChanged();
    }

    public void stopItem(String time) {
        nowMap.put(nowItem.getTimeField(), time);
        simAdapter.notifyDataSetChanged();
        nowMap = null;
        nowItem = null;
    }

}
