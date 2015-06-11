package nju.com.piece.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Button;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import nju.com.piece.TagActivity;
import nju.com.piece.R;
import nju.com.piece.TotalStatisticActivity;

/**
 * @author Hyman
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener{
    Button timelineButton,setButton,newTagButton;
    Button totalButton,loginButton;

    /**
     * ��ȡ��ǰ��Ļ���ܶ�
     */
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         timelineButton=(Button)findViewById(R.id.timelinebutton);
        timelineButton.setOnClickListener(this);
         setButton=(Button)findViewById(R.id.setbutton);
        setButton.setOnClickListener(this);


        newTagButton=(Button)findViewById(R.id.newTagButton);
        newTagButton.setOnClickListener(this);

        totalButton = (Button)findViewById(R.id.totalButton);
        totalButton.setOnClickListener(this);

        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        setOverflowShowingAlways();
        dm = getResources().getDisplayMetrics();

    }



    @Override
    //����menu_main.xml�ļ�
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    //��������overflow���е�Action��ť��ͼ����ʾ����
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

    //���ε�����Menu��
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
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.timelinebutton:
                intent = new Intent(MainActivity.this, TimeLineActivity.class);
                startActivity(intent);
                break;
            case R.id.setbutton:
                intent = new Intent(MainActivity.this, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.newTagButton:
                intent = new Intent(MainActivity.this, TagActivity.class);
                startActivity(intent);
                break;
            case R.id.totalButton:
                intent = new Intent(MainActivity.this, TotalStatisticActivity.class);
                startActivity(intent);
                break;
            case R.id.loginButton:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }


    }
}
