package nju.com.piece.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import nju.com.piece.view.MyGridView;
import nju.com.piece.R;
import nju.com.piece.adapter.IconImageAdaptor;
import nju.com.piece.adapter.adapterEntity.IconItem;
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

    private Date current_end_date;
    private double current_target;

    private GridView icon_grid;

    private static ArrayList<IconItem> icon_array;


    private Button btn;

    //    default state is work
    private TagType currentType = TagType.work;


    private IconImageAdaptor icon_adaptor;

    private boolean ifVibrate = false;

    private final DBFacade facade = new DBFacade(this);


    private boolean legal;
    private String tagName;
    private int icon_res;

    private void editInit(final String tagName){

        TagPO tagPO = facade.getTag(tagName);

        tag_name_edit.setText(tagName);

        Date endDate = tagPO.getEndDate();
        if (endDate == null)
            DATEPICKER_TAG = "选择截止日期";
        else {
            current_end_date = endDate;
            date_text.setText("截止到"+DateTool.getYear(endDate) + "年" + (DateTool.getMonth(endDate) + 1) + "月" + DateTool.getDay(endDate)+"日");
        }

        int targetTime = tagPO.getTargetMinute();
        if (targetTime == 0)
            TIMEPICKER_TAG = "选择时间";
        else{
            current_target = targetTime/60.0;
            plan_text.setText("每天投入"+String.format("%.1f", current_target)+"小时");
        }

        currentType = tagPO.getType();

        if (currentType == TagType.relax){
            Resources res = getResources();
            Drawable image_effor = resize_drawable(res.getDrawable(R.drawable.naughty));
            int width = (int) getResources().getDimension(R.dimen.task_type_size);
            image_effor.setBounds(0, 0, width, width);
            tag_name_edit.setCompoundDrawables(image_effor, null, null, null); //设置左图标
        }


        int currentIcon = tagPO.getResource();
        IconImageAdaptor.setCurrentIconIndex(currentIcon);

        btn.setText("编辑标签");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int target = (int) (60.0 * current_target);
                    String newTagName = tag_name_edit.getText().toString();
                    getParams();

                    if (legal) {
                        facade.updateTag(tagName, new TagPO(newTagName, currentType, icon_res, target, current_end_date));
                        Intent intent = new Intent(TagActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
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

        int currentIcon = icon_array.get(0).getResource();

        IconImageAdaptor.setCurrentIconIndex(currentIcon);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String tagName = tag_name_edit.getText().toString();
                    getParams();

                    if (legal) {
                        boolean duplicate = (facade.getTag(tagName) != null);
                        if (duplicate) {
                            Toast toast = Toast.makeText(getApplicationContext(), "标签名已存在!", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            int target = (int) (60.0 * current_target);
                            facade.addTag(new TagPO(tagName, currentType, icon_res, target, current_end_date));
                            Intent intent = new Intent(TagActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getParams() throws ParseException {
        legal = true;

        tagName = tag_name_edit.getText().toString();

        if (tagName.trim().equals("") && legal) {
            Toast toast = Toast.makeText(getApplicationContext(),"标签名不能为空",Toast.LENGTH_SHORT);
            toast.show();
            legal = false;
            tag_name_edit.requestFocus();
        }else if (tagName.trim().length()>10 || tagName.trim().length()<2){
            Toast toast = Toast.makeText(getApplicationContext(),"标签长度应在2到10字之间",Toast.LENGTH_SHORT);
            toast.show();
            legal = false;
            tag_name_edit.requestFocus();
        }



        String date_str = (String) date_text.getText();
        if (!date_str.equals("")){
                if (DateTool.currentDate().after(current_end_date)){
                    Toast toast = Toast.makeText(getApplicationContext(),"截止时间应在今日以后",Toast.LENGTH_SHORT);
                    toast.show();
                    legal = false;
                }
        }else {
            Toast toast = Toast.makeText(getApplicationContext(),"截止时间不能为空",Toast.LENGTH_SHORT);
            toast.show();
            legal = false;
        }

        String plan_str = (String) plan_text.getText();
        if (plan_str.equals("")){
            Toast toast = Toast.makeText(getApplicationContext(),"计划时间不能为空",Toast.LENGTH_SHORT);
            toast.show();
            legal = false;
        }



        icon_res = IconImageAdaptor.getSelectedRes();

        if (icon_res == 0 && legal){

            Toast toast = Toast.makeText(getApplicationContext(),"未选择图标",Toast.LENGTH_SHORT);
            toast.show();

            legal = false;
        }

        IconImageAdaptor.clearSelecetedRes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        readIcons();


        date_text = (TextView)findViewById(R.id.date_text);

        plan_text = (TextView)findViewById(R.id.plan_text);


        btn = (Button)findViewById(R.id.tag_btn);
        tag_name_edit = (EditText)findViewById(R.id.name_edit);


        Resources res = getResources();
        Drawable image_effor = resize_drawable(res.getDrawable(R.drawable.effort));
        image_effor.setBounds(0, 0, image_effor.getIntrinsicWidth(), image_effor.getIntrinsicHeight());
        tag_name_edit.setCompoundDrawables(image_effor, null, null, null); //设置左图标


        tag_name_edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() <= tag_name_edit.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width()) {

                if (currentType == TagType.work) {
                    currentType = TagType.relax;
                    Resources res = getResources();
                    Drawable image_effor = resize_drawable(res.getDrawable(R.drawable.naughty));
                    image_effor.setBounds(0, 0, image_effor.getIntrinsicWidth(), image_effor.getIntrinsicHeight());
                    tag_name_edit.setCompoundDrawables(image_effor, null, null, null); //设置左图标
                } else {
                    currentType = TagType.work;
                    Resources res = getResources();
                    Drawable image_effor = resize_drawable(res.getDrawable(R.drawable.effort));
                    image_effor.setBounds(0, 0, image_effor.getIntrinsicWidth(), image_effor.getIntrinsicHeight());
                    tag_name_edit.setCompoundDrawables(image_effor, null, null, null); //设置左图标
                }
                        return true;
                    }
                }
                return false;
            }
        });


        icon_adaptor = new IconImageAdaptor(this,R.layout.icon,icon_array);

        icon_grid = (MyGridView)findViewById(R.id.icon_grid);
        icon_grid.setAdapter(icon_adaptor);


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
                datePickerDialog.setCancelable(true);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        };

        View.OnClickListener plan_listener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(ifVibrate);
                timePickerDialog.setCloseOnSingleTapMinute(isCloseOnSingleTapMinute());
                timePickerDialog.setStartTime(1, 0);
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        };

        View.OnClickListener custom_plan_listener = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO 自动生成的方法存根
                android.app.TimePickerDialog.OnTimeSetListener otsl = new android.app.TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour,
                                          int minute) {
                        current_target = hour+minute/60.0;
                        plan_text.setText("每天投入"+String.format("%.1f", current_target)+"小时");
                    }
                };
                new android.app.TimePickerDialog(TagActivity.this, otsl, 1, 0,
                        true).show();
            }
        };



        date_text.setOnClickListener(date_listener);

        plan_text.setOnClickListener(custom_plan_listener);

        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }
        }
    }


    private Drawable resize_drawable(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 70, 70, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }

    private void readIcons(){
        if (icon_array == null) {

            String pre_name = "icon";

            icon_array = new ArrayList<IconItem>();

            IconImageAdaptor.clearResView();

            for (int i = 1; i < 46; ++i) {
                int res = this.getResources().getIdentifier(pre_name + i , "drawable", this.getPackageName());
                icon_array.add(new IconItem(res));
            }
        }
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
                Intent intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        current_end_date = calendar.getTime();

        date_text.setText("截止到"+year+"年"+(month+1)+"月"+day+"日");
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int min) {
        current_target = hour+min/60.0;
        plan_text.setText("每天投入"+String.format("%.1f", current_target)+"小时");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent=new Intent(TagActivity.this,MainActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}