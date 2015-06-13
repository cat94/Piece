package nju.com.piece;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nju.com.piece.activity.MainActivity;
import nju.com.piece.adapter.IconImageAdaptor;
import nju.com.piece.database.DBFacade;
import nju.com.piece.database.TagType;
import nju.com.piece.database.pos.TagPO;
import nju.com.piece.database.tools.DateTool;


public class TagActivity extends FragmentActivity implements OnDateSetListener,OnTimeSetListener{

    private String DATEPICKER_TAG;
    private String TIMEPICKER_TAG;

    private TextView date_text;
    private TextView plan_text;
    private EditText tag_name_edit;

    private ImageView type_icon;
    private ImageView date_icon;
    private ImageView plan_icon;

    private GridView icon_grid;

    private Button addBtn;

//    default state is work
    private TagType currentType = TagType.work;


    private IconImageAdaptor icon_adaptor;

    private boolean ifVibrate = false;

    private final DBFacade facade = new DBFacade(this);

    private void editInit(String tagName){

        TagPO tagPO = facade.getTag(tagName);

        Date endDate = tagPO.getEndDate();
        if (endDate == null)
            DATEPICKER_TAG = "选择截止日期";
        else
            date_text.setText(DateTool.getYear(endDate)+"/"+(DateTool.getMonth(endDate)+1)+"/"+DateTool.getDay(endDate));

        int targetTime = tagPO.getTargetMinute();
        if (targetTime == 0)
            TIMEPICKER_TAG = "选择时间";
        else{
            double time = targetTime%60;
            plan_text.setText(time+"");
        }

        IconsArray.currentIcon = tagPO.getResource();

        addBtn.setText("编辑标签");
    }

    private void notEditInit(){
        DATEPICKER_TAG = "选择截止日期";
        TIMEPICKER_TAG = "选择时间";
        addBtn.setText("添加标签");

        IconsArray.currentIcon = IconsArray.getIconArray().get(0).getResource();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);


        date_text = (TextView)findViewById(R.id.date_text);
        date_icon = (ImageView)findViewById(R.id.date_image);

        plan_text = (TextView)findViewById(R.id.plan_text);
        plan_icon = (ImageView)findViewById(R.id.plan_image);

        type_icon = (ImageView)findViewById(R.id.type_image);

        addBtn = (Button)findViewById(R.id.add_tag_btn);
        tag_name_edit = (EditText)findViewById(R.id.name_edit);

        if (savedInstanceState!=null) {
            Boolean ifEdit = savedInstanceState.getBoolean("ifEdit");
            if (ifEdit != null && ifEdit.booleanValue() == true) {
                String tagName = savedInstanceState.getString("tagName");
                editInit(tagName);
            } else {
                notEditInit();
            }
        }

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), ifVibrate);
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), true, ifVibrate);

        View.OnClickListener date_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(ifVibrate);
                datePickerDialog.setYearRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        };

        View.OnClickListener plan_listener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(ifVibrate);
                timePickerDialog.setCloseOnSingleTapMinute(isCloseOnSingleTapMinute());
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        };



        date_text.setOnClickListener(date_listener);
        date_icon.setOnClickListener(date_listener);

        plan_text.setOnClickListener(plan_listener);
        plan_icon.setOnClickListener(plan_listener);


        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }
        }


        type_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView view = (ImageView) v;

                if (currentType == TagType.work) {
                    currentType = TagType.relax;
                    view.setImageResource(R.drawable.relax);
                } else {
                    currentType = TagType.work;
                    view.setImageResource(R.drawable.busy);
                }

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    Date end_date = DateTool.increDate(formatter.parse((String) date_text.getText()), 1);
                    int target = (int)(60.0*Double.valueOf((String)plan_text.getText()));
                    String tagName = tag_name_edit.getText().toString();

                    int res = IconImageAdaptor.getSelectedRes();
                    IconImageAdaptor.clearSelecetedRes();

                    facade.addTag(new TagPO(tagName,currentType,res,target,end_date));
                    Intent intent = new Intent(TagActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        icon_adaptor = new IconImageAdaptor(this,R.layout.icon,IconsArray.getIconArray());

        icon_grid = (MyGridView)findViewById(R.id.icon_grid);
        icon_grid.setAdapter(icon_adaptor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_tag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean isCloseOnSingleTapDay() {
        return true;
    }

    private boolean isCloseOnSingleTapMinute() {
        return true;
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        date_text.setText(year+"/"+(month+1)+"/"+day);
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int min) {
        double text_hour = hour+min/60.0;
        plan_text.setText(String.format("%.1f", text_hour));
    }
}
