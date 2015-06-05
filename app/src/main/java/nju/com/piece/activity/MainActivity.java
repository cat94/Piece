package nju.com.piece.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import nju.com.piece.R;

/**
 * ���ڲ��Ե������档Ҫ���ĸ�ֻҪ��activity_main�������Ӧ��ť���ڴ����������Ӧ��Ӧʱ�伴�ɡ�
 * @author Hyman
 */
public class MainActivity extends Activity implements View.OnClickListener{
    Button timelineButton,setButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         timelineButton=(Button)findViewById(R.id.timelinebutton);
        timelineButton.setOnClickListener(this);
         setButton=(Button)findViewById(R.id.setbutton);
        setButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.timelinebutton:
              intent = new Intent(MainActivity.this, TimeLineActivity.class);
                startActivity(intent);
            case R.id.setbutton:
              intent = new Intent(MainActivity.this, SetActivity.class);
                startActivity(intent);
        }


    }
}
