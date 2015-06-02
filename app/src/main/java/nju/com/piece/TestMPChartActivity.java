package nju.com.piece;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Random;


public class TestMPChartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mpchart);

        LineChart chart = (LineChart) findViewById(R.id.chart);

        ArrayList<Entry> vals = new ArrayList<Entry>();

        Random rand = new Random();
        for (int i=0;i<7;++i){
            vals.add(new Entry(rand.nextInt(200),i));
        }

        LineDataSet daily_sum = new LineDataSet(vals,"日累积时间");

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(daily_sum);
        ArrayList<String> xvals = new ArrayList<String>(){{
            add("Monday");
            add("Tuesday");
            add("Wednesday");
            add("Thursday");
            add("Friday");
            add("Saturday");
            add("Sunday");
        }
        };

        LineData lineData = new LineData(xvals,dataSets);
        chart.setData(lineData);

//        specify the xAxis style
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelsToSkip(0);       //skip no x label
        xAxis.setDrawGridLines(false);  //don't draw grids
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.invalidate();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_mpchart, menu);
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
