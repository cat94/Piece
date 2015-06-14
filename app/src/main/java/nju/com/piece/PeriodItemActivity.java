package nju.com.piece;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PeriodItemActivity extends Activity {

    private ImageView delBtn = null;
    private ImageView editBtn = null;
    private int now = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_item);
        delBtn = (ImageView) findViewById(R.id.del_btn);
        editBtn = (ImageView) findViewById(R.id.edit_btn);
        now = getIntent().getIntExtra("index", -1);

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}
