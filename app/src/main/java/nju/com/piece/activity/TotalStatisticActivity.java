package nju.com.piece.activity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import nju.com.piece.R;
import nju.com.piece.adapter.ItemListAdapter;
import nju.com.piece.adapter.Utility;
import nju.com.piece.adapter.adapterEntity.StatisticItem;
import nju.com.piece.database.DBFacade;


public class TotalStatisticActivity extends FragmentActivity implements TabHost.TabContentFactory{

    private TabHost tabHost;
    private BarChart myBar;
    private LineChart healthLine;
    private ListView itemList;
    private PieChart itemPie;
    private TextView totalRecord;
    private TextView totalTime;
    private TextView averageWeek;
    private TextView lastWeek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_statistic);
        tabHost=(TabHost)findViewById(R.id.tabhost);
        myBar=(BarChart)findViewById(R.id.bar_charts);
        totalRecord=(TextView)findViewById(R.id.total_record);
        totalTime=(TextView)findViewById(R.id.total_time);
        averageWeek=(TextView)findViewById(R.id.average_week);
        lastWeek=(TextView)findViewById(R.id.last_week);
        healthLine=(LineChart)findViewById(R.id.health_diligence);
        itemPie=(PieChart)findViewById(R.id.percent_pie);
        itemList=(ListView)findViewById(R.id.item_list);

        //总体使用数值
        totalRecord.setText("1234");
        totalTime.setText("1234h");
        averageWeek.setText("29.7h");
        lastWeek.setText("23.3h");

        //健康-努力曲线
        ArrayList<Entry> healthEntries=new ArrayList<Entry>();
        ArrayList<Entry> diligenceEntries=new ArrayList<Entry>();
        Random rand = new Random();
        for (int i=0;i<7;++i){
            healthEntries.add(new Entry(rand.nextInt(200),i));
        }

        for (int i=0;i<7;++i){
            diligenceEntries.add(new Entry(rand.nextInt(200),i));
        }

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
        ArrayList<String> xvals = new ArrayList<String>(){{
            add("周一");
            add("周二");
            add("周三");
            add("周四");
            add("周五");
            add("周六");
            add("周日");
        }
        };
        LineData lineData = new LineData(xvals,dataSets);
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
        LocalActivityManager localActivityManager=new LocalActivityManager(this,true);
        localActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(localActivityManager);
        tabHost.addTab(tabHost.newTabSpec("daily_chart").setIndicator("每日").setContent(this));
        tabHost.addTab(tabHost.newTabSpec("weekly_chart").setIndicator("每周").setContent(this));
        tabHost.addTab(tabHost.newTabSpec("monthly_chart").setIndicator("每月").setContent(this));


        //获取分项统计数据
        final List<StatisticItem> list=new ArrayList<StatisticItem>();
        for (int i=0;i<10;i++){
            StatisticItem statisticItem=new StatisticItem();
            statisticItem.setItemName("ss");
            statisticItem.setPercentage("10%");
            statisticItem.setResourceID(R.drawable.busy);
            list.add(statisticItem);
        }

        //分项统计饼图
          ArrayList<Entry> arrayList=new ArrayList<Entry>();

        for (int i=0;i<10;++i){
            arrayList.add(new Entry(rand.nextInt(200),i));
        }

        PieDataSet pieDataSet=new PieDataSet(arrayList,"单项统计");

        ArrayList<String> pie_xvals = new ArrayList<String>(){{
            add("代码");
            add("不带");
            add("带份");
            add("宫保鸡丁");
            add("鱼香肉丝");
            add("水煮肉片");
            add("再");
            add("带份");
            add("米饭");
            add("和雪碧");
        }
        };
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
        pieData.setValueTextSize(30);
        itemPie.setUsePercentValues(true);

        itemPie.setCenterText("分项比例");
        itemPie.setCenterTextColor(getResources().getColor(R.color.stat_color_bright_14));
        itemPie.setDrawCenterText(true);
        itemPie.setDrawSliceText(true);
        itemPie.setData(pieData);
        itemPie.setDescription("");

        Legend pie_legend=itemPie.getLegend();

        pie_legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        pie_legend.setTextSize(30);
        itemPie.invalidate();
        //分项百分比

        itemList.setAdapter(new ItemListAdapter(TotalStatisticActivity.this,R.layout.statistic_item,list));
        Utility.setListViewHeightBasedOnChildren(itemList);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到分项统计
                Intent intent=new Intent(TotalStatisticActivity.this,ItemStatisticActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("itemName",list.get(position).getItemName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_total_statistic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_set) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View createTabContent(String tag) {
        BarChart barChart=myBar;
        ArrayList<BarEntry> daily=new ArrayList<BarEntry>();
        Random rand = new Random();
        for (int i=0;i<7;++i){
            daily.add(new BarEntry(rand.nextInt(200),i));
        }

        BarDataSet dailySet=new BarDataSet(daily,"asd");
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        ArrayList<String> xvals=null;
        BarData barData=null;
        switch (tag){
            case "daily_chart":
                 dailySet=new BarDataSet(daily,"每日指数");

                dataSets.add(dailySet);
                xvals = new ArrayList<String>(){{
                    add("周一");
                    add("周二");
                    add("周三");
                    add("周四");
                    add("周五");
                    add("周六");
                    add("周日");
                }
                };

                barData=new BarData(xvals,dataSets);
                int colors[]={getResources().getColor(R.color.stat_color_bright_1),getResources().getColor(R.color.stat_color_bright_2),getResources().getColor(R.color.stat_color_bright_3),
                        getResources().getColor(R.color.stat_color_bright_4),getResources().getColor(R.color.stat_color_bright_5),getResources().getColor(R.color.stat_color_bright_6),getResources().getColor(R.color.stat_color_bright_7)};
                dailySet.setColors(colors);
                break;
            case "weekly_chart":
                TextView textView=new TextView(this);
                textView.setText("gotohell");
                return textView;
            case "monthly_chart":
                TextView textView2=new TextView(this);
                textView2.setText("gotohell");
                return textView2;
            default:
                break;
        }



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

    }



    private class GetDataAsyncTask extends AsyncTask<String,Integer,HashMap<String,Object>>{
        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            DBFacade dbFacade=new DBFacade(TotalStatisticActivity.this);
            dbFacade.getAllTags();
            dbFacade.getAllPeriods();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> stringObjectHashMap) {
            super.onPostExecute(stringObjectHashMap);
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
