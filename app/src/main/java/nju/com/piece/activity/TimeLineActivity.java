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

import nju.com.piece.R;
import nju.com.piece.entity.RelaxTimelineItem;
import nju.com.piece.entity.Timeline;
import nju.com.piece.entity.TimelineItem;
import nju.com.piece.entity.WorkTimelineItem;

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
        startActivityForResult(intent, STARTCODE); // ����requestcode
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STARTCODE) {
//            int minutes;
//            int taskId;

            // TODO 获取TimelineItem
            state = resultCode;
            final TimelineItem item;

            switch (resultCode) {
                case TaskActivity.COUNTDOWN:
//                    minutes = data.getIntExtra("minutes", 0);
//                    taskId = data.getIntExtra("taskId", 0);
//                    this.taskId = taskId;
                    changeAddToStop();
                    item = new RelaxTimelineItem(TimelineItem.RELAX_TYPE,"relax",R.drawable.play_icon,50);
                    timeline.addItem(item);
                    countDownTimer = new CountDownTimer(item.getSecond() * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            chronometer.setText(Timeline.FormatSecond((int) millisUntilFinished / 1000));
                            AddSec();
                        }

                        @Override
                        public void onFinish() {
                            timeline.stopItem(item.getSecond());
                            changeStopToAdd();
                        }
                    }.start();

                    break;
                case TaskActivity.ADD:
//                    minutes = data.getIntExtra("minutes", 0);
//                    taskId = data.getIntExtra("taskId", 0);
                    item = new WorkTimelineItem(TimelineItem.WORK_TYPE,"work",R.drawable.work_icon,50);
                    timeline.addItem(item);
                    timeline.stopItem(item.getSecond());
                    break;
                case TaskActivity.TIMING:
//                    taskId = data.getIntExtra("taskId", 0);
//                    this.taskId = taskId;
//                    Toast.makeText(MainActivity.this, "Ϊ����" + taskId + "��ʼ���ʱ:",
//                            Toast.LENGTH_SHORT).show();
                    changeAddToStop();
                    item = new WorkTimelineItem(TimelineItem.WORK_TYPE,"work",R.drawable.work_icon,0);
                    timeline.addItem(item);
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
