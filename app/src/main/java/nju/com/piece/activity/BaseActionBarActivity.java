package nju.com.piece.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import java.lang.reflect.Field;

/**
 * all the activities except MainActivity that use ActionBar should extends this class
 * 用到ActionBar的activity都继承这个类(除了主界面）
 * @author hyman
 *
 */

public class BaseActionBarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  //the back button  "<" on the top-left
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}