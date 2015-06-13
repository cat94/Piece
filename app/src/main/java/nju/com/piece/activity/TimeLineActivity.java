package nju.com.piece.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import nju.com.piece.R;
import nju.com.piece.database.DBFacade;
import nju.com.piece.database.TagType;
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
    private DisplayMetrics dm;

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
        TagPO tag1 = new TagPO("relax", TagType.relax, R.drawable.tag_icon_01);
        dbFacade.addTag(tag1);
        TagPO tag2 = new TagPO("work", TagType.work, R.drawable.tag_icon_04);
        dbFacade.addTag(tag2);
        setOverflowShowingAlways();
        dm = getResources().getDisplayMetrics();

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
                        timeline.stopItem((int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000));
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

    @Override
    //load menu_main.xml
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    //overflow
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    //show the menu all time
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
           Intent intent;
                switch (item.getItemId()) {
                    case R.id.action_plus:
                         intent=new Intent(TimeLineActivity.this, TagActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_set:
                        intent=new Intent(TimeLineActivity.this, SetActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_manage:
                         intent=new Intent(TimeLineActivity.this, TagActivity.class);
                        startActivity(intent);
                        break;
                    default:

                }
         return super.onOptionsItemSelected(item);
            }


}
