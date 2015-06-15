package nju.com.piece.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import nju.com.piece.R;

public class MyInfo extends ActionBarActivity {
    private ImageView portrait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        portrait=(ImageView)findViewById(R.id.portrait);
        //获得imageview中设置的图片
        BitmapDrawable drawable = (BitmapDrawable) portrait.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        //获得图片的宽，并创建结果bitmap
        int width = bmp.getWidth();
        Bitmap resultBmp = Bitmap.createBitmap(width, width,
                Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(resultBmp);
        //画圆
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 选择交集去上层图片
        canvas.drawBitmap(bmp, 0, 0, paint);
        portrait.setImageBitmap(resultBmp);
        bmp.recycle();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_info, menu);
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
