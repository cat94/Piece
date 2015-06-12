package nju.com.piece.entity;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nju.com.piece.R;
import nju.com.piece.adapter.TimelineAdapter;
import nju.com.piece.adapter.adapterEntity.TimelineItem;
import nju.com.piece.database.DBFacade;
import nju.com.piece.database.TagType;
import nju.com.piece.database.pos.PeriodPO;
import nju.com.piece.database.pos.TagPO;

/**
 * Created by hyl on 15/6/4.
 */
public class Timeline {

    public static final String WORK_TYPE = "work";
    public static final String RELAX_TYPE = "relax";
    public static final String ICON = "icon";
    public static final String WORK_TIME = "work_time";
    public static final String RELAX_TIME = "relax_time";
    public static final String ALL_WORK_TIME = "all_work_time";
    public static final String ALL_RELAX_TIME = "all_relax_time";

    private Context context = null;
    private ListView timeline = null;
//    private SimpleAdapter simAdapter = null;
//    private List<Map<String, Object>> dataList = null;
    private ArrayAdapter adapter = null;
    private List<TimelineItem> items = null;
//    private Map<String, Object> nowMap = null;
    private TimelineItem nowItem = null;
    private TagType nowType = null;
    private TagPO nowPO = null;
    private TextView allWorkTimeView = null;
    private TextView allRelaxTimeView = null;

    private static Map<String, Integer> AllTime = new HashMap<String, Integer>();

    private DBFacade dbFacade = null;

    public Timeline(Context context, ListView timeline, TextView allRelaxTimeView, TextView allWorkTimeView) {
        this.timeline = timeline;
        this.context = context;
        this.allWorkTimeView = allWorkTimeView;
        this.allRelaxTimeView = allRelaxTimeView;
        this.dbFacade = new DBFacade(context);
        initDataList();
        initSimAdapter();
        updateAllTime();
    }

    private void updateAllTime() {
        allRelaxTimeView.setText(FormatSecond(AllTime.get(ALL_RELAX_TIME)));
        allWorkTimeView.setText(FormatSecond(AllTime.get(ALL_WORK_TIME)));
    }

    private void initSimAdapter() {
//        simAdapter = new SimpleAdapter(context, dataList,
//                R.layout.timeline_item,
//                new String[]{Timeline.RELAX_TYPE, Timeline.RELAX_TIME,
//                    Timeline.ICON, Timeline.WORK_TYPE, Timeline.WORK_TIME},
//                new int[]{R.id.relax,R.id.relax_time, R.id.icon, R.id.work, R.id.work_time});
//        timeline.setAdapter(simAdapter);
        adapter = new TimelineAdapter(context, R.layout.timeline_item, items);
        timeline.setAdapter(adapter);
    }

    private void initDataList() {
        AllTime.put(ALL_RELAX_TIME,0);
        AllTime.put(ALL_WORK_TIME, 0);
//        dataList = new ArrayList<Map<String, Object>>();
//        Map<String, Object> map;
//        ArrayList<PeriodPO> pos = dbFacade.getPeriodsByDate(new Date());
//        for (PeriodPO po : pos) {
//            TagPO tag = dbFacade.getTag(po.getTag());
//            TagType type = tag.getType();
//            map = new HashMap<String, Object>();
//            map.put(Timeline.ICON, tag.getResource());
//            map.put(type.toString(), tag.getTagName());
//            map.put(type.getTimeField(), FormatSecond(po.getLength()));
//            dataList.add(map);
//            AllTime.put(type.getAllTimeField(),AllTime.get(type.getAllTimeField())+po.getLength());
//        }
        items = new ArrayList<TimelineItem>();
        TimelineItem item;
        TagPO tag;
        TagType type;
        ArrayList<PeriodPO> pos = dbFacade.getPeriodsByDate(new Date());
        for (PeriodPO po : pos) {
            tag = dbFacade.getTag(po.getTag());
            type = tag.getType();
            item = new TimelineItem(type.toString(), tag.getTagName(), po.getLength(), tag.getResource());
            items.add(item);
            AllTime.put(type.getAllTimeField(), AllTime.get(type.getAllTimeField()) + po.getLength());
        }
    }

    public void addItem(TagPO po) {
        nowPO = po;
        nowType = po.getType();
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put(nowType.toString(), po.getTagName());
//        map.put(Timeline.ICON, po.getResource());
//        dataList.add(0, map);
//        this.nowMap = map;
//        simAdapter.notifyDataSetChanged();
        nowItem = new TimelineItem(nowType.toString(), po.getTagName(), po.getResource());
        items.add(nowItem);
        adapter.notifyDataSetChanged();
    }

    public void stopItem(int time) {
        AllTime.put(nowType.getAllTimeField(), AllTime.get(nowType.getAllTimeField()) + time);
        updateAllTime();
//        nowMap.put(nowType.getTimeField(), FormatSecond(time));
//        simAdapter.notifyDataSetChanged();
        nowItem.setLength(time);
        PeriodPO newPO = new PeriodPO(nowPO.getTagName(), time);
        dbFacade.addPeriod(newPO);
        nowPO = null;
//        nowMap = null;
        nowItem = null;
        nowType = null;
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
