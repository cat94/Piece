package nju.com.piece.activity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import nju.com.piece.R;

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
    private BarChart myBar;

    private Button stopButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_statistic);
        icon=(ImageView)findViewById(R.id.item_icon);
        item_name=(TextView)findViewById(R.id.item_stat_name);
        start_date=(TextView)findViewById(R.id.item_stat_date_from);
        totalHours=(TextView)findViewById(R.id.item_stat_total_hour);

        totalDays=(TextView)findViewById(R.id.item_stat_days);
        averageWeek=(TextView)findViewById(R.id.item_stat_per_week);
        lastWeek=(TextView)findViewById(R.id.item_stat_last_week);
        toTarget=(TextView)findViewById(R.id.item_stat_to_target);

        plan_per_week=(TextView)findViewById(R.id.item_stat_plan_per_week);

        tabHost=(TabHost)findViewById(R.id.tabhost);
        myBar=(BarChart)findViewById(R.id.bar_charts);

        stopButton=(Button)findViewById(R.id.item_stat_stopitem);


        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        String itemName= bundle.getString("itemName");


        //每日，每周，每月
        LocalActivityManager localActivityManager=new LocalActivityManager(this,true);
        localActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(localActivityManager);
        tabHost.addTab(tabHost.newTabSpec("daily_chart").setIndicator("每日").setContent(this));
        tabHost.addTab(tabHost.newTabSpec("weekly_chart").setIndicator("每周").setContent(this));
        tabHost.addTab(tabHost.newTabSpec("monthly_chart").setIndicator("每月").setContent(this));

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
}
