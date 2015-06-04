package nju.com.piece;

import android.app.LocalActivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.github.mikephil.charting.charts.BarChart;


public class TotalStatisticActivity extends ActionBarActivity {
    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_statistic);
        tabHost=(TabHost)findViewById(R.id.tabhost);
        LocalActivityManager localActivityManager=new LocalActivityManager(TotalStatisticActivity.this,true);
        localActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(localActivityManager);
        tabHost.addTab(tabHost.newTabSpec("daily_chart").setIndicator("每日").setContent(R.id.bar_charts));
        tabHost.addTab(tabHost.newTabSpec("weekly_chart").setIndicator("每周").setContent(R.id.bar_charts));
        tabHost.addTab(tabHost.newTabSpec("monthly_chart").setIndicator("每月").setContent(R.id.bar_charts));
        BarChart dailyBar=(BarChart)findViewById(R.id.bar_charts);
        BarChart weeklyBar=(BarChart)findViewById(R.id.bar_charts);
        BarChart monthlyBar=(BarChart)findViewById(R.id.bar_charts);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
