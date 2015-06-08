package nju.com.piece.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

import nju.com.piece.R;
import nju.com.piece.database.DBFacade;
import nju.com.piece.database.TagType;
import nju.com.piece.database.pos.PeriodPO;
import nju.com.piece.database.pos.TagPO;
import nju.com.piece.entity.Timeline;

public class TimeLineActivity extends Activity {

    private Timeline timeline = null;

    private ImageView addItemBtn = null;
    private ImageView stopItemBtn = null;
    private ListView timelineView = null;
    private TextView allWorkTimeView = null;
    private TextView allRelaxTimeView = null;

    private static int state = 0;
    private Chronometer chronometer = null;
    private static CountDownTimer countDownTimer;
    private static int countDownSec = 0;

    private static final int STARTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        initTimeline();
        initAddItemBtn();
        initStopItemBtn();
        initChronometer();

        DBFacade dbFacade = new DBFacade(this);
        TagPO tag1 = new TagPO("relax", TagType.relax,R.drawable.tag_icon_01);
        dbFacade.addTag(tag1);
        TagPO tag2 = new TagPO("work", TagType.work,R.drawable.tag_icon_04);
        dbFacade.addTag(tag2);
    }

    private void initTimeline() {
        timelineView = (ListView) findViewById(R.id.timeline);
        allRelaxTimeView = (TextView) findViewById(R.id.all_relax_time);
        allWorkTimeView = (TextView) findViewById(R.id.all_work_time);
        timeline = new Timeline(this, timelineView, allRelaxTimeView, allWorkTimeView);
    }

    private void initAddItemBtn() {
        addItemBtn = (ImageView) findViewById(R.id.addItem_btn);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddItemPage();
            }
        });
    }

    private void initStopItemBtn() {
        stopItemBtn = (ImageView) findViewById(R.id.stopItem_btn);
        stopItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (state) {
                    case TaskActivity.TIMING:
                        chronometer.stop();
                        timeline.stopItem((int)((SystemClock.elapsedRealtime()- chronometer.getBase())/1000));
                        break;
                    case TaskActivity.COUNTDOWN:
                        countDownTimer.cancel();
                        timeline.stopItem(countDownSec);
                        countDownSec = 0;
                        break;
                }
                changeStopToAdd();
            }
        });
    }

    private void initChronometer() {
        chronometer = (Chronometer) findViewById(R.id.chronometer);
    }


    public void toAddItemPage() {
        Intent intent = new Intent(TimeLineActivity.this, TaskActivity.class);
        startActivityForResult(intent, STARTCODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STARTCODE) {
            state = resultCode;

            final int length;
            TagPO tag;

            switch (resultCode) {
                case TaskActivity.COUNTDOWN:
                    length = data.getIntExtra("length", 0) * 60;
                    tag = (TagPO) data.getSerializableExtra("tag");
                    changeAddToStop();
                    timeline.addItem(tag);
                    countDownTimer = new CountDownTimer(length * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            chronometer.setText(Timeline.FormatSecond((int) millisUntilFinished / 1000));
                            AddSec();
                        }

                        @Override
                        public void onFinish() {
                            timeline.stopItem(length);
                            changeStopToAdd();
                        }
                    }.start();

                    break;
                case TaskActivity.ADD:
                    length = data.getIntExtra("length", 0) * 60;
                    tag = (TagPO) data.getSerializableExtra("tag");
                    timeline.addItem(tag);
                    timeline.stopItem(length);
                    break;
                case TaskActivity.TIMING:
                    tag = (TagPO) data.getSerializableExtra("tag");
                    changeAddToStop();
                    timeline.addItem(tag);
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
}
