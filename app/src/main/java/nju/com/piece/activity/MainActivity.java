package nju.com.piece.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Button;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import nju.com.piece.R;
import nju.com.piece.database.DBFacade;

/**
 * @author Hyman
 */
public class MainActivity extends ActionBarActivity{


    private ViewPager m_vp;
    private TimeLineActivity mfragment1;
    private  TotalStatisticActivity mfragment2;
    //页面列表
    private ArrayList<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if logged
        DBFacade dbFacade=new DBFacade(this);
        if(dbFacade.getAccount()==null){
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }


        m_vp = (ViewPager)findViewById(R.id.viewpager);


        mfragment2 = new TotalStatisticActivity();
        mfragment1 = new TimeLineActivity();

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(mfragment1);
        fragmentList.add(mfragment2);

        m_vp.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));

        setOverflowShowingAlways();
    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }


    @Override
    //load menu_main.xml
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    //overflow
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    //show the menu all time,case some phones has physical menu button,so maybe not show menu on screen
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_plus:
                intent=new Intent(this, TagActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.action_set:
                intent=new Intent(this, SetActivity.class);
                startActivity(intent);
                finish();
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }


}
