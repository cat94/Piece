package nju.com.piece.activity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.DefaultValueFormatter;
import com.github.mikephil.charting.utils.FillFormatter;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import nju.com.piece.R;
import nju.com.piece.adapter.ItemListAdapter;
import nju.com.piece.adapter.Utility;
import nju.com.piece.adapter.adapterEntity.StatisticItem;
import nju.com.piece.database.DBFacade;
import nju.com.piece.database.TagType;
import nju.com.piece.database.pos.PeriodPO;
import nju.com.piece.database.pos.TagPO;
import nju.com.piece.database.tools.DateTool;


public class TotalStatisticActivity extends Fragment implements TabHost.TabContentFactory{

    private TabHost tabHost;
    private BarChart dailyBar;
    private BarChart weeklyBar;
    private BarChart monthlyBar;
    private LineChart healthLine;
    private ListView itemList;
    private PieChart itemPie;
    private TextView totalRecord;
    private TextView totalTime;
    private TextView averageWeek;
    private TextView lastWeek;
    private View mMainView;
    private List<TagPO> allTags;
    private List<PeriodPO> allPeriods;
    private int totalHour;
    private double hoursPerWeek;
    private double hoursPerLastWeek;

    private  ArrayList<Entry> healthEntries=new ArrayList<Entry>();
    private ArrayList<Entry> diligenceEntries=new ArrayList<Entry>();
    private final List<StatisticItem> list=new ArrayList<StatisticItem>();
    private ArrayList<Entry> arrayList=new ArrayList<Entry>();
    private   ArrayList<String> pie_xvals=new ArrayList<String>();
    private ArrayList<BarEntry> daily=new ArrayList<BarEntry>();
    private ArrayList<BarEntry> weekly=new ArrayList<BarEntry>();
    private ArrayList<BarEntry> monthly=new ArrayList<BarEntry>();
    private ArrayList<String> line_xvals=new ArrayList<String>();
    private ArrayList<String> bar_daily_xvals=new ArrayList<String>();
    private ArrayList<String> bar_weekly_xvals=new ArrayList<String>();
    private ArrayList<String> bar_monthly_xvals=new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mMainView = inflater.inflate(R.layout.activity_total_statistic, (ViewGroup)getActivity().findViewById(R.id.viewpager), false);

        tabHost=(TabHost) mMainView.findViewById(R.id.tabhost);
        dailyBar=(BarChart) mMainView.findViewById(R.id.daily_charts);
        weeklyBar=(BarChart) mMainView.findViewById(R.id.weekly_charts);
        monthlyBar=(BarChart) mMainView.findViewById(R.id.monthly_charts);
        totalRecord=(TextView) mMainView.findViewById(R.id.total_record);
        totalTime=(TextView) mMainView.findViewById(R.id.total_time);
        averageWeek=(TextView) mMainView.findViewById(R.id.average_week);
        lastWeek=(TextView) mMainView.findViewById(R.id.last_week);
        healthLine=(LineChart) mMainView.findViewById(R.id.health_diligence);
        itemPie=(PieChart) mMainView.findViewById(R.id.percent_pie);
        itemList=(ListView) mMainView.findViewById(R.id.item_list);

        //加载数据
        getData();

        //总体使用数值
        totalRecord.setText(allPeriods.size()+"");
        totalTime.setText(totalHour+"h");
        averageWeek.setText(hoursPerWeek+"h");
        lastWeek.setText(hoursPerLastWeek+"h");

        //健康-努力曲线




        LineDataSet healthDataSet=new LineDataSet(healthEntries,"健康指数");
        LineDataSet diligenceDataSet=new LineDataSet(diligenceEntries,"努力指数");
        healthDataSet.setColor(Color.parseColor("#CCFF66"));
        healthDataSet.setCircleColor(Color.parseColor("#CCFF66"));
        healthDataSet.setCircleColorHole(Color.parseColor("#CCFF66"));
        healthDataSet.setValueTextSize(0);
        healthDataSet.setLineWidth(2);

        diligenceDataSet.setColor(Color.parseColor("#0099FF"));
        diligenceDataSet.setCircleColor(Color.parseColor("#0099FF"));
        diligenceDataSet.setCircleColorHole(Color.parseColor("#0099FF"));
        diligenceDataSet.setValueTextSize(0);
        diligenceDataSet.setLineWidth(2);


        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(healthDataSet);
        dataSets.add(diligenceDataSet);
        LineData lineData = new LineData(line_xvals,dataSets);
        healthLine.setData(lineData);
        XAxis xAxis = healthLine.getXAxis();
        xAxis.setLabelsToSkip(0);       //skip no x label
        xAxis.setDrawGridLines(false);  //don't draw grids
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineWidth(2);

        YAxis yAxis_left=healthLine.getAxisLeft();
        YAxis yAxis_right=healthLine.getAxisRight();
        yAxis_left.setAxisLineWidth(2);
        yAxis_right.setAxisLineWidth(2);
        //xAxis.setTextSize(7);



        healthLine.setDescription("");
        healthLine.setGridBackgroundColor(Color.WHITE);
        //healthLine.setBackgroundColor(ColorTemplate.PASTEL_COLORS[0]);
        healthLine.setDrawGridBackground(true);

        healthLine.invalidate();



        //每日，每周，每月
        LocalActivityManager localActivityManager=new LocalActivityManager(getActivity(),true);
        localActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(localActivityManager);
        tabHost.addTab(tabHost.newTabSpec("daily_chart").setIndicator("每日").setContent(this));
        tabHost.addTab(tabHost.newTabSpec("weekly_chart").setIndicator("每周").setContent(this));
        tabHost.addTab(tabHost.newTabSpec("monthly_chart").setIndicator("每月").setContent(this));






        //分项统计饼图


        PieDataSet pieDataSet=new PieDataSet(arrayList,"单项统计");




        int colors[]={getResources().getColor(R.color.stat_color_bright_1),
                getResources().getColor(R.color.stat_color_bright_2),
                getResources().getColor(R.color.stat_color_bright_3),
                getResources().getColor(R.color.stat_color_bright_4),
                getResources().getColor(R.color.stat_color_bright_5),
                getResources().getColor(R.color.stat_color_bright_6),
                getResources().getColor(R.color.stat_color_bright_7),
                getResources().getColor(R.color.stat_color_bright_8),
                getResources().getColor(R.color.stat_color_bright_9),
                getResources().getColor(R.color.stat_color_bright_10),
                getResources().getColor(R.color.stat_color_bright_11),
                getResources().getColor(R.color.stat_color_bright_12),
                getResources().getColor(R.color.stat_color_bright_13),
        };
        pieDataSet.setColors(colors);
        pieDataSet.setSelectionShift(15);

        pieDataSet.setValueFormatter(new PercentFormatter());

        PieData pieData=new PieData(pie_xvals,pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextSize(9);
        itemPie.setUsePercentValues(true);

        itemPie.setCenterText("分项比例");
        itemPie.setCenterTextColor(getResources().getColor(R.color.stat_color_bright_14));
        itemPie.setDrawCenterText(true);
        itemPie.setDrawSliceText(true);
        itemPie.setData(pieData);
        itemPie.setDescription("");

        Legend pie_legend=itemPie.getLegend();

        pie_legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        pie_legend.setTextSize(5);
        itemPie.invalidate();
        //分项百分比

        itemList.setAdapter(new ItemListAdapter(getActivity(),R.layout.statistic_item,list));
        Utility.setListViewHeightBasedOnChildren(itemList);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到分项统计
                Intent intent=new Intent(getActivity(),ItemStatisticActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("itemName",list.get(position).getItemName());
                intent.putExtras(bundle);
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

    @Override
    public View createTabContent(String tag) {



        int colors[]={getResources().getColor(R.color.stat_color_bright_1),getResources().getColor(R.color.stat_color_bright_2),getResources().getColor(R.color.stat_color_bright_3),
                getResources().getColor(R.color.stat_color_bright_4),getResources().getColor(R.color.stat_color_bright_5),getResources().getColor(R.color.stat_color_bright_6),getResources().getColor(R.color.stat_color_bright_7)};

        switch (tag){
            case "daily_chart":
                BarChart   barChart=dailyBar;
                ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
                BarDataSet dataSet=new BarDataSet(daily,"每日指数");
                dataSets.add(dataSet);
                BarData   barData=new BarData(bar_daily_xvals,dataSets);
                dataSet.setColors(colors);
                barChart.setData(barData);
                XAxis xAxis = barChart.getXAxis();
                xAxis.setLabelsToSkip(0);       //skip no x label
                xAxis.setDrawGridLines(false);  //don't draw grids
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setAxisLineWidth(2);

                YAxis yAxis_left=barChart.getAxisLeft();
                YAxis yAxis_right=barChart.getAxisRight();
                yAxis_left.setAxisLineWidth(2);
                yAxis_right.setAxisLineWidth(2);
                barChart.setGridBackgroundColor(Color.WHITE);
                barChart.setDescription("");
                barChart.invalidate();
                return  barChart;
            case "weekly_chart":
                BarChart  weeklyBarChart=weeklyBar;
                ArrayList<BarDataSet> weeklyDataSets = new ArrayList<BarDataSet>();
                BarDataSet weeklyDataSet=new BarDataSet(weekly,"每周指数");
                weeklyDataSets.add(weeklyDataSet);
                BarData  weeklyBarData=new BarData(bar_weekly_xvals,weeklyDataSets);
                weeklyDataSet.setColors(colors);
                weeklyBarChart.setData(weeklyBarData);
                XAxis weeklyAxis = weeklyBarChart.getXAxis();
                weeklyAxis.setLabelsToSkip(0);       //skip no x label
                weeklyAxis.setDrawGridLines(false);  //don't draw grids
                weeklyAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                weeklyAxis.setAxisLineWidth(2);

                YAxis weekly_yAxis_left=weeklyBarChart.getAxisLeft();
                YAxis weekly_yAxis_right=weeklyBarChart.getAxisRight();
                weekly_yAxis_left.setAxisLineWidth(2);
                weekly_yAxis_right.setAxisLineWidth(2);
                weeklyBarChart.setGridBackgroundColor(Color.WHITE);
                weeklyBarChart.setDescription("");
                weeklyBarChart.invalidate();
                return  weeklyBarChart;
            case "monthly_chart":
                BarChart  monthlyBarChart=monthlyBar;
                ArrayList<BarDataSet> monthlyDataSets = new ArrayList<BarDataSet>();
                BarDataSet monthlyDataSet=new BarDataSet(monthly,"每月指数");
                monthlyDataSets.add(monthlyDataSet);
                BarData  monthlyBarData=new BarData(bar_monthly_xvals,monthlyDataSets);
                monthlyDataSet.setColors(colors);
                monthlyBarChart.setData(monthlyBarData);
                XAxis monthlyAxis = monthlyBarChart.getXAxis();
                monthlyAxis.setLabelsToSkip(0);       //skip no x label
                monthlyAxis.setDrawGridLines(false);  //don't draw grids
                monthlyAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                monthlyAxis.setAxisLineWidth(2);

                YAxis monthly_yAxis_left=monthlyBarChart.getAxisLeft();
                YAxis monthly_yAxis_right=monthlyBarChart.getAxisRight();
                monthly_yAxis_left.setAxisLineWidth(2);
                monthly_yAxis_right.setAxisLineWidth(2);
                monthlyBarChart.setGridBackgroundColor(Color.WHITE);
                monthlyBarChart.setDescription("");
                monthlyBarChart.invalidate();
                return  monthlyBarChart;
            default:
               return  null;
        }






    }



    private void getData(){
        DBFacade dbFacade=new DBFacade(getActivity());
        allPeriods=dbFacade.getAllPeriods();
        allTags=dbFacade.getAllTags();
        int totalSeconds=0;

        int lastWeekSeconds=0;

        HashMap<String,Integer> tagOccupation=new HashMap<String,Integer>();
        for(TagPO tagPO:allTags){
            tagOccupation.put(tagPO.getTagName(),0);
            pie_xvals.add(tagPO.getTagName());
        }
       for (PeriodPO periodPO:allPeriods){
           totalSeconds+=periodPO.getLength();
          int tagIndex=tagOccupation.get(periodPO.getTag());
          tagIndex+=periodPO.getLength();
           tagOccupation.put(periodPO.getTag(),tagIndex);
       }

        List<PeriodPO> lastSevenDaysPeriods=dbFacade.getLastSevenDaysPeriods();
        for (PeriodPO periodPO:lastSevenDaysPeriods){
            lastWeekSeconds+=periodPO.getLength();
        }

        totalHour=totalSeconds/3600;
        hoursPerLastWeek=((double)lastWeekSeconds/3600)/7;



        if (allPeriods!=null&&allPeriods.size()>0){
           Date startDate=allPeriods.get(0).getDate();
            Date today=new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(today);
            long time2 = cal.getTimeInMillis();
            int between_days=Integer.parseInt(String.valueOf((time2-time1)/(1000*3600*24)));
            hoursPerWeek=((double)totalHour)/(between_days+1);
        }


        HashMap<String,Integer> weeklyHealth=new HashMap<String,Integer>();
        HashMap<String,Integer> weeklyWork=new HashMap<String,Integer>();
        HashMap<String,Integer> dailySeconds=new HashMap<String,Integer>();
        //上周所有日期
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        ArrayList<String> lastweekDays=new ArrayList<String>();
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DATE, -1 * calendar.get(Calendar.DAY_OF_WEEK) + 2 + i);
            line_xvals.add(sf.format(calendar.getTime()).split("-")[2]+"日");
            weeklyHealth.put(sf.format(calendar.getTime()), 0);
            weeklyWork.put(sf.format(calendar.getTime()), 0);
            dailySeconds.put(sf.format(calendar.getTime()), 0);
            lastweekDays.add(sf.format(calendar.getTime()));
            bar_daily_xvals.add(sf.format(calendar.getTime()).split("-")[2]+"日");
        }

        List<PeriodPO> lastWeekPeriods=dbFacade.getLastWeekPeriods();

        for(PeriodPO periodPO:lastWeekPeriods){
            if (periodPO.getTag().equals(TagType.relax)){
                int health=weeklyHealth.get(sf.format(periodPO.getDate()))+periodPO.getLength();
                weeklyHealth.put(sf.format(periodPO.getDate()), health);
            }else{
                int work=weeklyWork.get(sf.format(periodPO.getDate()))+periodPO.getLength();
                weeklyWork.put(sf.format(periodPO.getDate()),work);
            }
            int seconds=dailySeconds.get(sf.format(periodPO.getDate()))+periodPO.getLength();
            dailySeconds.put(sf.format(periodPO.getDate()),seconds);
        }


        for (int i=0;i<lastweekDays.size();i++){
            healthEntries.add(new Entry(weeklyHealth.get(lastweekDays.get(i)),i));
            diligenceEntries.add(new Entry(weeklyWork.get(lastweekDays.get(i)),i));
            daily.add(new BarEntry(dailySeconds.get(lastweekDays.get(i)),i));
        }


        List<PeriodPO> lastMonthPeriods=dbFacade.getLastMonthPeriods();
        Calendar   cal_1=Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        Date firstDay = cal_1.getTime();

        for (int i=0;i<3;i++){
            Calendar cale = Calendar.getInstance();
            cale.set(DateTool.getYear(firstDay), DateTool.getMonth(firstDay), DateTool.getDay(firstDay));
            cale.add(Calendar.WEEK_OF_MONTH,i);
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

        List<PeriodPO> lastSeasonPeriods=dbFacade.getLastSeasonPeriods();
        Calendar   cal_2=Calendar.getInstance();//获取当前日期
        cal_2.add(Calendar.MONTH, -4);
        cal_2.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        Date firstOfMonth = cal_2.getTime();
        for (int i=0;i<4;i++){
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



        PercentFormatter percentFormatter=new PercentFormatter();

        for (TagPO tagPO:allTags){
            StatisticItem statisticItem=new StatisticItem();
            statisticItem.setItemName(tagPO.getTagName());
            statisticItem.setPercentage(percentFormatter.getFormattedValue((float)tagOccupation.get(tagPO.getTagName())/totalSeconds*100));
            statisticItem.setResourceID(tagPO.getResource());
            list.add(statisticItem);
            arrayList.add(new Entry(tagOccupation.get(tagPO.getTagName()),allTags.indexOf(tagPO)));
        }


    }

     private class PercentFormatter implements ValueFormatter{
         private DecimalFormat mFormat=new DecimalFormat("###,###,##0.0");


         @Override
         public String getFormattedValue(float value) {
             return mFormat.format(value) + "%";
         }
     }
}
