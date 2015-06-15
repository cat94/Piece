package nju.com.piece.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import nju.com.piece.R;

/**
 * @author Hyman
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener{

    Button timelineButton,setButton;
    Button totalButton,loginButton;
    Button newTagButton,editTagButton;

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

        editTagButton = (Button)findViewById(R.id.editTagButton);
        editTagButton.setOnClickListener(this);

        totalButton = (Button)findViewById(R.id.totalButton);
        totalButton.setOnClickListener(this);

        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

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
            case R.id.editTagButton:
                Bundle bundle = new Bundle();
                bundle.putBoolean("ifEdit",true);
                bundle.putString("tagName","tag name to edit");

                intent = new Intent(MainActivity.this, TagActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.loginButton:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }


    }
}
