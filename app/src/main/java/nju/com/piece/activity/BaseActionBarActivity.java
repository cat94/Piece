package nju.com.piece.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import java.lang.reflect.Field;

/**
 * ActionBar��activity������ͳһ����������
 * ���ж�������actionbar���඼�̳д��ࡣ
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