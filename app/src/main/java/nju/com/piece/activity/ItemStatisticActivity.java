package nju.com.piece.activity;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import nju.com.piece.R;
import nju.com.piece.database.DBFacade;
import nju.com.piece.database.TagType;
import nju.com.piece.database.pos.PeriodPO;
import nju.com.piece.database.pos.TagPO;
import nju.com.piece.database.tools.DateTool;

public class ItemStatisticActivity extends FragmentActivity implements TabHost.TabContentFactory {

    private ImageView icon;
    private TextView item_name;
    private TextView start_date;
    private TextView totalHours;

    private TextView totalDays;
    private TextView averageWeek;
    private TextView lastWeek;
    private TextView toTarget;


    private TextView plan_per_week;
    private TabHost tabHost;
    private BarChart dailyBar;
    private BarChart weeklyBar;
    private BarChart monthlyBar;

    private String itemName;

    private int iconResource=R.drawable.default_icon;
    private String start_date_text;
    private String totalHours_text;
    private String totalDays_text;
    private String averageWeek_text;
    private String lastWeek_text;
    private String toTarget_text;
    private String plan_per_week_text;
    private ArrayList<BarEntry> daily=new ArrayList<BarEntry>();
    private ArrayList<BarEntry> weekly=new ArrayList<BarEntry>();
    private ArrayList<BarEntry> monthly=new ArrayList<BarEntry>();
    private ArrayList<String> bar_daily_xvals=new ArrayList<String>();
    private ArrayList<String> bar_weekly_xvals=new ArrayList<String>();
    private ArrayList<String> bar_monthly_xvals=new ArrayList<String>();
    private Button editItem;
    private Button deleteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_statistic);

        icon=(ImageView)findViewById(R.id.item_stat_icon);
        item_name=(TextView)findViewById(R.id.item_stat_name);
        start_date=(TextView)findViewById(R.id.item_stat_date_from);
        totalHours=(TextView)findViewById(R.id.item_stat_total_hour);

        totalDays=(TextView)findViewById(R.id.item_stat_days);
        averageWeek=(TextView)findViewById(R.id.item_stat_per_week);
        lastWeek=(TextView)findViewById(R.id.item_stat_last_week);
        toTarget=(TextView)findViewById(R.id.item_stat_to_target);

        plan_per_week=(TextView)findViewById(R.id.item_stat_plan_per_week);

        tabHost=(TabHost)findViewById(R.id.tabhost);
        dailyBar=(BarChart)findViewById(R.id.item_daily_bar);
        weeklyBar=(BarChart)findViewById(R.id.item_weekly_bar);
        monthlyBar=(BarChart)findViewById(R.id.item_monthly_bar);

        editItem=(Button)findViewById(R.id.edit_item_btn);
        deleteItem=(Button)findViewById(R.id.delete_item_btn);


        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        itemName= bundle.getString("itemName");

        getData();

        icon.setImageResource(iconResource);
        item_name.setText(itemName);
        start_date.setText(start_date_text);
        totalHours.setText(totalHours_text);
        totalDays.setText(totalDays_text);
        averageWeek.setText(averageWeek_text);
        lastWeek.setText(lastWeek_text);
        toTarget.setText(toTarget_text);
        plan_per_week.setText(plan_per_week_text);

        //每日，每周，每月
        LocalActivityManager localActivityManager=new LocalActivityManager(this,true);
        localActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(localActivityManager);
        tabHost.addTab(tabHost.newTabSpec("daily_chart").setIndicator("每日").setContent(this));
        tabHost.addTab(tabHost.newTabSpec("weekly_chart").setIndicator("每周").setContent(this));
        tabHost.addTab(tabHost.newTabSpec("monthly_chart").setIndicator("每月").setContent(this));


        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent=new Intent(ItemStatisticActivity.this,TagActivity.class);
             Bundle bundle=new Bundle();
             bundle.putBoolean("ifEdit",true);
             bundle.putString("tagName",itemName);
             intent.putExtras(bundle);
             startActivity(intent);
            }
        });


        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ItemStatisticActivity.this).setTitle("提醒")//设置对话框标题

                        .setMessage("确认删除这个项目？")//设置显示的内容

                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


                            @Override

                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                // TODO Auto-generated method stub
                                DBFacade dbFacade=new DBFacade(ItemStatisticActivity.this);
                                dbFacade.delTag(itemName);
                                Intent intent=new Intent(ItemStatisticActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮



                    @Override

                    public void onClick(DialogInterface dialog, int which) {//响应事件

                        // TODO Auto-generated method stub



                    }

                }).show();//在按键响应事件中显示此对话框



            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_statistic, menu);
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



    public View createTabContent(String tag) {


        int colors[] = {getResources().getColor(R.color.stat_color_bright_1), getResources().getColor(R.color.stat_color_bright_2), getResources().getColor(R.color.stat_color_bright_3),
                getResources().getColor(R.color.stat_color_bright_4), getResources().getColor(R.color.stat_color_bright_5), getResources().getColor(R.color.stat_color_bright_6), getResources().getColor(R.color.stat_color_bright_7)};

        switch (tag) {
            case "daily_chart":
                BarChart barChart = dailyBar;
                ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
                BarDataSet dataSet = new BarDataSet(daily, "每日指数");
                dataSets.add(dataSet);
                BarData barData = new BarData(bar_daily_xvals, dataSets);
                dataSet.setColors(colors);
                barChart.setData(barData);
                XAxis xAxis = barChart.getXAxis();
                xAxis.setLabelsToSkip(0);       //skip no x label
                xAxis.setDrawGridLines(false);  //don't draw grids
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setAxisLineWidth(2);

                YAxis yAxis_left = barChart.getAxisLeft();
                YAxis yAxis_right = barChart.getAxisRight();
                yAxis_left.setAxisLineWidth(2);
                yAxis_right.setAxisLineWidth(2);
                barChart.setGridBackgroundColor(Color.WHITE);
                barChart.setDescription("");
                barChart.invalidate();
                return barChart;
            case "weekly_chart":
                BarChart weeklyBarChart = weeklyBar;
                ArrayList<BarDataSet> weeklyDataSets = new ArrayList<BarDataSet>();
                BarDataSet weeklyDataSet = new BarDataSet(weekly, "每周指数");
                weeklyDataSets.add(weeklyDataSet);
                BarData weeklyBarData = new BarData(bar_weekly_xvals, weeklyDataSets);
                weeklyDataSet.setColors(colors);
                weeklyBarChart.setData(weeklyBarData);
                XAxis weeklyAxis = weeklyBarChart.getXAxis();
                weeklyAxis.setLabelsToSkip(0);       //skip no x label
                weeklyAxis.setDrawGridLines(false);  //don't draw grids
                weeklyAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                weeklyAxis.setAxisLineWidth(2);

                YAxis weekly_yAxis_left = weeklyBarChart.getAxisLeft();
                YAxis weekly_yAxis_right = weeklyBarChart.getAxisRight();
                weekly_yAxis_left.setAxisLineWidth(2);
                weekly_yAxis_right.setAxisLineWidth(2);
                weeklyBarChart.setGridBackgroundColor(Color.WHITE);
                weeklyBarChart.setDescription("");
                weeklyBarChart.invalidate();
                return weeklyBarChart;
            case "monthly_chart":
                BarChart monthlyBarChart = monthlyBar;
                ArrayList<BarDataSet> monthlyDataSets = new ArrayList<BarDataSet>();
                BarDataSet monthlyDataSet = new BarDataSet(monthly, "每月指数");
                monthlyDataSets.add(monthlyDataSet);
                BarData monthlyBarData = new BarData(bar_monthly_xvals, monthlyDataSets);
                monthlyDataSet.setColors(colors);
                monthlyBarChart.setData(monthlyBarData);
                XAxis monthlyAxis = monthlyBarChart.getXAxis();
                monthlyAxis.setLabelsToSkip(0);       //skip no x label
                monthlyAxis.setDrawGridLines(false);  //don't draw grids
                monthlyAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                monthlyAxis.setAxisLineWidth(2);

                YAxis monthly_yAxis_left = monthlyBarChart.getAxisLeft();
                YAxis monthly_yAxis_right = monthlyBarChart.getAxisRight();
                monthly_yAxis_left.setAxisLineWidth(2);
                monthly_yAxis_right.setAxisLineWidth(2);
                monthlyBarChart.setGridBackgroundColor(Color.WHITE);
                monthlyBarChart.setDescription("");
                monthlyBarChart.invalidate();
                return monthlyBarChart;
            default:
                return null;
        }
    }


    private void getData(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        DBFacade dbFacade=new DBFacade(ItemStatisticActivity.this);
        DecimalFormat decimalFormat=new DecimalFormat(".#");
        TagPO tagPO=dbFacade.getTag(itemName);
        iconResource=tagPO.getResource();

        start_date_text=sf.format(tagPO.getStartDate());

        List<PeriodPO> allPeriods=dbFacade.getAllPeriods(itemName);
        int totalSeconds=0;
        for (PeriodPO periodPO:allPeriods){
            totalSeconds+=periodPO.getLength();
        }

        totalHours_text=totalSeconds/3600+"";
        totalDays_text=totalSeconds/3600/24+"days";

        if (allPeriods!=null&&allPeriods.size()>0){
            Date startDate=allPeriods.get(0).getDate();
            Date today=new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(today);
            long time2 = cal.getTimeInMillis();
            int between_days=Integer.parseInt(String.valueOf((time2-time1)/(1000*3600*24)));
            averageWeek_text=Double.parseDouble(decimalFormat.format((double)totalSeconds/3600/(between_days+1)))+"h";
        }else{
            averageWeek_text=0+"h";
        }


        Calendar calendar = Calendar.getInstance();
        ArrayList<String> lastweekDays=new ArrayList<String>();
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        HashMap<String,Integer> dailySeconds=new HashMap<String,Integer>();
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DATE,1);
            dailySeconds.put(sf.format(calendar.getTime()), 0);
            lastweekDays.add(sf.format(calendar.getTime()));
            bar_daily_xvals.add(sf.format(calendar.getTime()).split("-")[2]+"日");
        }

        List<PeriodPO> lastWeekPeriods=dbFacade.getLastSevenDaysPeriods(tagPO.getTagName());

        for(PeriodPO periodPO:lastWeekPeriods){
            int seconds=dailySeconds.get(sf.format(periodPO.getDate()))+periodPO.getLength();
            dailySeconds.put(sf.format(periodPO.getDate()),seconds);
        }

        int lastWeekSeconds=0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(tagPO.getStartDate());
        long time1 = cal.getTimeInMillis();
        cal.setTime(tagPO.getEndDate());
        long time2 = cal.getTimeInMillis();
        int between_days=Integer.parseInt(String.valueOf((time2-time1)/(1000*3600*24)));
        for (int i=0;i<lastweekDays.size();i++){
            daily.add(new BarEntry(dailySeconds.get(lastweekDays.get(i))/3600,i));
            lastWeekSeconds+=dailySeconds.get(lastweekDays.get(i));
        }
        lastWeek_text=lastWeekSeconds/3600+"h";
        if ((tagPO.getTargetMinute()/60*(between_days+1)-tagPO.getCurrentMinute()/60)<0){
            toTarget_text="已完成";
        }else{
            toTarget_text=(tagPO.getTargetMinute()/60*(between_days+1)-tagPO.getCurrentMinute()/60)+"h";
        }
        plan_per_week_text="计划每周投入"+Double.parseDouble(decimalFormat.format(tagPO.getTargetMinute()/60*(between_days+1)/7.0))+"h";


        List<PeriodPO> lastMonthPeriods=dbFacade.getLastMonthPeriods(itemName);
        Calendar cal_1=Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        Date firstDay = cal_1.getTime();

        for (int i=0;i<4;i++){
            Calendar cale = Calendar.getInstance();
            cale.set(DateTool.getYear(firstDay), DateTool.getMonth(firstDay), DateTool.getDay(firstDay));
            cale.add(Calendar.WEEK_OF_MONTH,1);
            cale.add(Calendar.DATE, -1 * cale.get(Calendar.DAY_OF_WEEK) + 8);
            Date after_date = cale.getTime();
            int second_in_week=0;
            for (PeriodPO periodPO:lastMonthPeriods){
                if (periodPO.getDate().after(firstDay)&&periodPO.getDate().before(after_date)){
                    second_in_week+=periodPO.getLength();
                }
            }
            bar_weekly_xvals.add(sf.format(firstDay).split("-")[1]+"-"+sf.format(firstDay).split("-")[2]);
            BarEntry barEntry=new BarEntry(second_in_week/3600,i);
            weekly.add(barEntry);
            firstDay=after_date;
        }

        List<PeriodPO> lastSeasonPeriods=dbFacade.getLastSeasonPeriods(itemName);
        Calendar   cal_2=Calendar.getInstance();//获取当前日期
        cal_2.add(Calendar.MONTH, -4);
        cal_2.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        Date firstOfMonth = cal_2.getTime();
        for (int i=0;i<5;i++){
            Calendar cale = Calendar.getInstance();
            cale.set(DateTool.getYear(firstOfMonth),DateTool.getMonth(firstOfMonth),DateTool.getDay(firstOfMonth));
            cale.add(Calendar.DAY_OF_MONTH,cale.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date after_date = cale.getTime();

            int second_in_month=0;
            for (PeriodPO periodPO:lastSeasonPeriods){
                if (periodPO.getDate().after(firstOfMonth)&&periodPO.getDate().before(after_date)){
                    second_in_month+=periodPO.getLength();
                }
            }
            bar_monthly_xvals.add(sf.format(firstOfMonth).split("-")[1]+"-"+sf.format(firstOfMonth).split("-")[2]);
            BarEntry barEntry=new BarEntry(second_in_month/3600,i);
            monthly.add(barEntry);

            firstOfMonth=after_date;
        }
    }
}
