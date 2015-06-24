package nju.com.piece.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
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
import nju.com.piece.database.PreferenceHelper;
import nju.com.piece.database.TagType;
import nju.com.piece.database.pos.PeriodPO;
import nju.com.piece.database.pos.TagPO;

public class TimeLineActivity extends Fragment {

    public static final String ALL_WORK_TIME = "all_work_time";
    public static final String ALL_RELAX_TIME = "all_relax_time";

    public static List<PeriodPO> periodPOs = null;
    public static ArrayAdapter adapter = null;
    public static List<TimelineItem> items = null;
    private TimelineItem nowItem = null;
    private TagType nowType = null;
    private TagPO nowPO = null;
    public static Map<String, Integer> AllTime = new HashMap<String, Integer>();

    private DBFacade dbFacade = null;

    private ImageView addItemBtn = null;
    private ImageView stopItemBtn = null;
    private ListView timelineView = null;
    private static TextView allWorkTimeView = null;
    private static TextView allRelaxTimeView = null;

    private static int timerState = 0;
    private Chronometer chronometer = null;
    private static CountDownTimer countDownTimer;
    private static int countDownSec = 0;

    private View mMainView;

    private static final int STARTCODE = 1;

    private ImageView addTagBtn = null;
    private ImageView settingBtn = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mMainView = inflater.inflate(R.layout.activity_timeline, (ViewGroup)getActivity().findViewById(R.id.viewpager), false);

        initTimeline();
        initAddItemBtn();
        initStopItemBtn();
        initChronometer();
        updateAllTime();
        initAddTagBtn();
        initSettingBtn();
//        DBFacade dbFacade = new DBFacade(this);
//        TagPO tag1 = new TagPO("relax", TagType.relax,R.drawable.tag_icon_01);
//        dbFacade.addTag(tag1);
//        TagPO tag2 = new TagPO("work", TagType.work,R.drawable.tag_icon_04);
//        dbFacade.addTag(tag2);

    }

    private void initAddTagBtn() {
        addTagBtn = (ImageView) mMainView.findViewById(R.id.add_btn);
        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), TagActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initSettingBtn() {
        settingBtn = (ImageView) mMainView.findViewById(R.id.setting_btn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SetActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup p = (ViewGroup) mMainView.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }
        return mMainView;
    }



    private void initTimeline() {
        timelineView = (ListView) mMainView.findViewById(R.id.timeline);
        allRelaxTimeView = (TextView)  mMainView.findViewById(R.id.all_relax_time);
        allWorkTimeView = (TextView) mMainView. findViewById(R.id.all_work_time);
//        timeline = new Timeline(this, timelineView, allRelaxTimeView, allWorkTimeView);
        this.dbFacade = new DBFacade(getActivity());
        initDataList();
        initSimAdapter();
    }

    public static void updateAllTime() {
        allRelaxTimeView.setText(FormatSecond(AllTime.get(ALL_RELAX_TIME)));
        allWorkTimeView.setText(FormatSecond(AllTime.get(ALL_WORK_TIME)));
    }

    private void initSimAdapter() {
        adapter = new TimelineAdapter(getActivity(), R.layout.timeline_item, items);
        timelineView.setAdapter(adapter);
    }

    private void initDataList() {
        AllTime.put(ALL_RELAX_TIME,0);
        AllTime.put(ALL_WORK_TIME, 0);
        items = new ArrayList<TimelineItem>();
        TimelineItem item;
        TagPO tag;
        TagType type;
        periodPOs = dbFacade.getPeriodsByDate(new Date());
        for (PeriodPO po : periodPOs) {
            tag = dbFacade.getTag(po.getTag());
            type = tag.getType();
            item = new TimelineItem(type.toString(), tag.getTagName(), po.getLength(), tag.getResource());
            items.add(item);
            AllTime.put(type.getAllTimeField(), AllTime.get(type.getAllTimeField()) + po.getLength());
        }
    }

    private void initAddItemBtn() {
        addItemBtn = (ImageView)  mMainView.findViewById(R.id.addItem_btn);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddItemPage();
            }
        });
    }

    private void initStopItemBtn() {
        stopItemBtn = (ImageView)  mMainView.findViewById(R.id.stopItem_btn);
        stopItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (timerState) {
                    case TaskActivity.TIMING:
                        chronometer.stop();
                        stopItem((int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000));
                        break;
                    case TaskActivity.COUNTDOWN:
                        countDownTimer.cancel();
                        stopItem(countDownSec);
                        countDownSec = 0;
                        break;
                }
                changeStopToAdd();
            }
        });
    }

    private void initChronometer() {
        chronometer = (Chronometer)  mMainView.findViewById(R.id.chronometer);
    }


    public void toAddItemPage() {
        Intent intent = new Intent(getActivity(), TaskActivity.class);
        startActivityForResult(intent, STARTCODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STARTCODE) {
            timerState = resultCode;

            final int length;
            TagPO tag;

            switch (resultCode) {
                case TaskActivity.COUNTDOWN:
                    length = data.getIntExtra("length", 0) * 60;
                    tag = (TagPO) data.getSerializableExtra("tag");
                    changeAddToStop();
                    addItem(tag);
                    countDownTimer = new CountDownTimer(length * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            chronometer.setText(FormatSecond((int) millisUntilFinished / 1000));
                            AddSec();
                        }

                        @Override
                        public void onFinish() {
                            stopItem(length);
                            changeStopToAdd();
                        }
                    }.start();

                    break;
                case TaskActivity.ADD:
                    length = data.getIntExtra("length", 0) * 60;
                    tag = (TagPO) data.getSerializableExtra("tag");
                    addItem(tag);
                    stopItem(length);
                    break;
                case TaskActivity.TIMING:
                    tag = (TagPO) data.getSerializableExtra("tag");
                    changeAddToStop();
                    addItem(tag);
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    break;
                default:
                    break;
            } // end switch
        }// end if
    }

    private void changeAddToStop() {
        chronometer.setVisibility(View.VISIBLE);
        stopItemBtn.setVisibility(View.VISIBLE);
        addItemBtn.setVisibility(View.GONE);
    }

    private void changeStopToAdd() {
        chronometer.setVisibility(View.INVISIBLE);
        addItemBtn.setVisibility(View.VISIBLE);
        stopItemBtn.setVisibility(View.GONE);
    }

    public static void AddSec() {
        countDownSec++;
    }

    public void addItem(TagPO po) {
        nowPO = po;
        nowType = po.getType();
        nowItem = new TimelineItem(nowType.toString(), po.getTagName(), po.getResource());
        items.add(0, nowItem);
        adapter.notifyDataSetChanged();
    }

    public void stopItem(int time) {
        AllTime.put(nowType.getAllTimeField(), AllTime.get(nowType.getAllTimeField()) + time);
        updateAllTime();
        nowItem.setLength(time);
        adapter.notifyDataSetChanged();
        PeriodPO newPO = new PeriodPO(nowPO.getTagName(), time);
        periodPOs.add(0, newPO);
        dbFacade.addPeriod(newPO);
        nowPO = null;
        nowItem = null;
        nowType = null;
    }

    public static String FormatSecond(int second) {
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
