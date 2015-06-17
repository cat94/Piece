package nju.com.piece.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.IllegalFormatException;

import nju.com.piece.IconsArray;
import nju.com.piece.MyGridView;
import nju.com.piece.R;
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

    private GridView icon_grid;

    private Button btn;

    //    default state is work
    private TagType currentType = TagType.work;


    private IconImageAdaptor icon_adaptor;

    private boolean ifVibrate = false;

    private final DBFacade facade = new DBFacade(this);


    private Date end_date;
    private int target;
    private boolean legal;
    private String tagName;
    private int icon_res;

    private void editInit(final String tagName){

        TagPO tagPO = facade.getTag(tagName);

        tag_name_edit.setText(tagName);

        Date endDate = tagPO.getEndDate();
        if (endDate == null)
            DATEPICKER_TAG = "选择截止日期";
        else
            date_text.setText(DateTool.getYear(endDate)+"/"+(DateTool.getMonth(endDate)+1)+"/"+DateTool.getDay(endDate));

        int targetTime = tagPO.getTargetMinute();
        if (targetTime == 0)
            TIMEPICKER_TAG = "选择时间";
        else{
            double time = targetTime/60.0;
            plan_text.setText(time+"");
        }

        IconsArray.currentIcon = tagPO.getResource();

        btn.setText("编辑标签");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getParams();

                    if (legal) {
                        facade.updateTag(tagName, new TagPO(tagName, currentType, icon_res, target, end_date));
                        Intent intent = new Intent(TagActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void notEditInit(){
        DATEPICKER_TAG = "选择截止日期";
        TIMEPICKER_TAG = "选择时间";
        btn.setText("添加标签");

        IconsArray.currentIcon = IconsArray.getIconArray().get(0).getResource();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getParams();

                    if (legal) {
                        facade.addTag(new TagPO(tagName, currentType, icon_res, target, end_date));
                        Intent intent = new Intent(TagActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getParams() throws ParseException {
        legal = true;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        end_date = null;
        String date_str = (String) date_text.getText();
        if (!date_str.equals("")){
            try{
                end_date = formatter.parse(date_str);
            }catch (IllegalFormatException ex){
                end_date = null;
            }
        }

        String plan_str = (String) plan_text.getText();
        target = 0;
        if (!plan_str.equals(""))
            target = (int) (60.0 * Double.valueOf((String) plan_text.getText()));

        tagName = tag_name_edit.getText().toString();

        if (tagName.trim().equals("")) {
            new AlertDialog.Builder(TagActivity.this)
                    .setTitle("标签不能为空")
                    .setMessage("请输入标签名")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    }).show();
            tag_name_edit.requestFocus();
            legal = false;
        }


        icon_res = IconImageAdaptor.getSelectedRes();

        if (icon_res == 0){
            new AlertDialog.Builder(TagActivity.this)
                    .setTitle("未选择图标")
                    .setMessage("请选择图标")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    }).show();

            legal = false;
        }

        IconImageAdaptor.clearSelecetedRes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);


        //about actionbar
        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

        date_text = (TextView)findViewById(R.id.date_text);
//        date_icon = (ImageView)findViewById(R.id.date_image);

        plan_text = (TextView)findViewById(R.id.plan_text);
//        plan_icon = (ImageView)findViewById(R.id.plan_image);

        type_icon = (ImageView)findViewById(R.id.type_image);

        btn = (Button)findViewById(R.id.tag_btn);
        tag_name_edit = (EditText)findViewById(R.id.name_edit);


        Bundle bundle=this.getIntent().getExtras();

        if (bundle!=null) {
            Boolean ifEdit = bundle.getBoolean("ifEdit");
            if (ifEdit != null && ifEdit.booleanValue() == true) {
                String tagName = bundle.getString("tagName");
                editInit(tagName);
            } else {
                notEditInit();
            }
        }else{
            notEditInit();
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
                timePickerDialog.setStartTime(1,0);
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        };



        date_text.setOnClickListener(date_listener);

        plan_text.setOnClickListener(plan_listener);


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
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
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
