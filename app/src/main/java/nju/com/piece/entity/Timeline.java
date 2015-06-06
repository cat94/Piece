package nju.com.piece.entity;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nju.com.piece.R;

/**
 * Created by hyl on 15/6/4.
 */
public class Timeline {

    public static final String ALL_WORK_TIME = "all_work_time";
    public static final String ALL_RELAX_TIME = "all_relax_time";

    private Context context = null;
    private ListView timeline = null;
    private SimpleAdapter simAdapter = null;
    private List<Map<String, Object>> dataList = null;
    private Map<String, Object> nowMap = null;
    private TimelineItem nowItem = null;
    private TextView allWorkTimeView = null;
    private TextView allRelaxTimeView = null;

    private static Map<String, Integer> AllTime = new HashMap<String, Integer>();

    public Timeline(Context context, ListView timeline, TextView allRelaximeView, TextView allWorkTimeView) {
        this.timeline = timeline;
        this.context = context;
        this.allWorkTimeView = allWorkTimeView;
        this.allRelaxTimeView = allRelaximeView;
        initDataList();
        initSimAdapter();
        updateAllTime();
    }

    private void updateAllTime() {
        allRelaxTimeView.setText(FormatSecond(AllTime.get(ALL_RELAX_TIME)));
        allWorkTimeView.setText(FormatSecond(AllTime.get(ALL_WORK_TIME)));
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
        AllTime.put(ALL_RELAX_TIME,0);
        AllTime.put(ALL_WORK_TIME,0);
        dataList = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < 3; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(TimelineItem.ICON, TimelineItem.RELAX_ICON);
            map.put(TimelineItem.RELAX_TYPE, "hanyilu" + i);
            dataList.add(map);
            AllTime.put(ALL_RELAX_TIME,AllTime.get(ALL_RELAX_TIME)+600);
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

    public void stopItem(int time) {
        AllTime.put(nowItem.getAllTimeField(), AllTime.get(nowItem.getAllTimeField())+time);
        updateAllTime();
        // TODO 添加进数据库
        nowMap.put(nowItem.getTimeField(), FormatSecond(time));
        simAdapter.notifyDataSetChanged();
        nowMap = null;
        nowItem = null;
    }

    public static String FormatSecond(int second) {
        Log.i("Main", second+";");
        int h = second / 3600;
        int mod = second % 3600;
        int m = mod / 60;
        int s = mod % 60;
        String result = "";
        result += h > 9 ? h + ":" : "0" + h + ":";
        result += m > 9 ? m + ":" : "0" + m + ":";
        result += s > 9 ? s + "" : "0" + s;
        return result;
    }
}
