package nju.com.piece.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import java.lang.reflect.Field;

/**
 * all the activities except mainactivity that use actionbar should extend this
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
        actionBar.setDisplayHomeAsUpEnabled(true); // �������Ͻ�ͼ����Ҳ��Ƿ��������С��ͷ, true
        // ��С��ͷ������ͼ����Ե��
        actionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// �������ͼ���¼�
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}