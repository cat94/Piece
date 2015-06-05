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
        timeline = new Timeline(this, timelineView);
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
                        timeline.stopItem(chronometer.getText().toString());
                        break;
                    case TaskActivity.COUNTDOWN:
                        countDownTimer.cancel();
                        timeline.stopItem(FormatSecond(countDownSec));
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
        startActivityForResult(intent, STARTCODE); // 定义requestcode
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 处理开始任务后的返回值
        if (requestCode == STARTCODE) {
//            int minutes;
//            int taskId;

            // TODO 传回一个TimelineItem
            state = resultCode;
            final TimelineItem item;

            switch (resultCode) {
                case TaskActivity.COUNTDOWN:
                    // 从中取出设定的分钟数
//                    minutes = data.getIntExtra("minutes", 0);
//                    taskId = data.getIntExtra("taskId", 0);
//                    this.taskId = taskId;
                    changeAddToStop();
                    item = new RelaxTimelineItem(TimelineItem.RELAX_TYPE,"relax",R.drawable.play_icon,50);
                    timeline.addItem(item);
                    countDownTimer = new CountDownTimer(item.getSecond() * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            chronometer.setText(FormatSecond((int) millisUntilFinished / 1000));
                            AddSec();
                        }

                        @Override
                        public void onFinish() {
                            timeline.stopItem(FormatSecond(item.getSecond()));
                            changeStopToAdd();
                        }
                    }.start();

                    break;
                case TaskActivity.ADD:
//                    // 从中取出设定的分钟数
//                    minutes = data.getIntExtra("minutes", 0);
//                    taskId = data.getIntExtra("taskId", 0);
                    item = new WorkTimelineItem(TimelineItem.WORK_TYPE,"work",R.drawable.work_icon,50);
                    timeline.addItem(item);
                    timeline.stopItem(FormatSecond(item.getSecond()));
                    break;
                case TaskActivity.TIMING:
//                    // 从中取出设定的分钟数
//                    taskId = data.getIntExtra("taskId", 0);
//                    this.taskId = taskId;
//                    Toast.makeText(MainActivity.this, "为任务" + taskId + "开始正计时:",
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
        // 改变开始按钮为停止按钮
        stopItemBtn.setVisibility(View.VISIBLE);
        addItemBtn.setVisibility(View.GONE);
    }

    private void changeStopToAdd() {
        chronometer.setVisibility(View.INVISIBLE);
        addItemBtn.setVisibility(View.VISIBLE);
        stopItemBtn.setVisibility(View.GONE);
    }

    public static String FormatSecond(int second) {
        int h = second / 3600;
        int mod = second % 3600;
        int m = mod / 60;
        int s = mod % 60;
        return h + "时" + m + "分" + s + "秒";
    }

    public static void AddSec() {
        countDownSec++;
    }
}
