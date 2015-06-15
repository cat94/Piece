package nju.com.piece.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;

import nju.com.piece.R;
import nju.com.piece.adapter.adapterEntity.TimelineItem;
import nju.com.piece.database.DBFacade;
import nju.com.piece.database.TagType;
import nju.com.piece.database.pos.PeriodPO;

public class PeriodItemActivity extends Activity {

    private ImageView delBtn = null;
    private ImageView editBtn = null;
    private int index = -1;
    private PeriodPO now = null;
    private TimelineItem item = null;
    private DBFacade dbFacade = null;

    private TimePickerDialog tpd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_item);
        delBtn = (ImageView) findViewById(R.id.del_btn);
        editBtn = (ImageView) findViewById(R.id.edit_btn);
        index = getIntent().getIntExtra("index", -1);
        now = TimeLineActivity.periodPOs.get(index);
        item = TimeLineActivity.items.get(index);

        dbFacade = new DBFacade(this);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbFacade.delPeriod(now);
                TimeLineActivity.items.remove(index);
                TimeLineActivity.periodPOs.remove(index);
                TimeLineActivity.adapter.notifyDataSetChanged();
                if (item.getType().equals(TagType.relax.toString())) {
                    TimeLineActivity.AllTime.put(TimeLineActivity.ALL_RELAX_TIME, TimeLineActivity.AllTime.get(TimeLineActivity.ALL_RELAX_TIME) - item.getLength());
                } else if (item.getType().equals(TagType.work.toString())) {
                    TimeLineActivity.AllTime.put(TimeLineActivity.ALL_WORK_TIME, TimeLineActivity.AllTime.get(TimeLineActivity.ALL_WORK_TIME) - item.getLength());
                }
                TimeLineActivity.updateAllTime();
                finish();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog.OnTimeSetListener otsl = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        int seconds = 3600 * hour + 60 * minute;
                        dbFacade.updatePeriod(now, seconds);
                        TimeLineActivity.adapter.notifyDataSetChanged();
                        if (item.getType().equals(TagType.relax.toString())) {
                            TimeLineActivity.AllTime.put(TimeLineActivity.ALL_RELAX_TIME, TimeLineActivity.AllTime.get(TimeLineActivity.ALL_RELAX_TIME) - item.getLength() + seconds);
                        } else if (item.getType().equals(TagType.work.toString())) {
                            TimeLineActivity.AllTime.put(TimeLineActivity.ALL_WORK_TIME, TimeLineActivity.AllTime.get(TimeLineActivity.ALL_WORK_TIME) - item.getLength() + seconds);
                        }
                        TimeLineActivity.updateAllTime();
                        item.setLength(seconds);
                        finish();
                    }
                };
                tpd = new TimePickerDialog(PeriodItemActivity.this, otsl, 1, 0,
                        true);
                tpd.show();
            }
        });
    }
}
