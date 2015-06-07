package nju.com.piece.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import nju.com.piece.R;
import nju.com.piece.logic.update.UpdateInfo;
import nju.com.piece.logic.update.UpdateInfoService;

/**
 * @author Hyman
 */
public class SetActivity extends BaseActionBarActivity{
    private UpdateInfo info;
    private ProgressDialog progressDialog;
    UpdateInfoService updateInfoService;
    private static String TAG="SetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements  OnClickListener {
        public PlaceholderFragment( ) {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            LinearLayout info=(LinearLayout)rootView.findViewById(R.id.info);
            LinearLayout update=(LinearLayout)rootView.findViewById(R.id.update);
            LinearLayout task=(LinearLayout)rootView.findViewById(R.id.task);
            info.setOnClickListener(this);
            task.setOnClickListener(this);
            update.setOnClickListener(this);
            return rootView;
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.info:
                    Toast.makeText(getActivity(), "这应该打开个人信息", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.task:
                    Toast.makeText(getActivity(), "这应该做的是同步任务", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.update:
                    SetActivity parentActivity = (SetActivity) getActivity();
                    parentActivity.checkUpdate();
                    break;

            }
        }

    }

    public  void checkUpdate(){
        Toast.makeText(SetActivity.this, "正在检查更新", Toast.LENGTH_SHORT).show();
        new Thread() {
            public void run() {
                try {
                    updateInfoService = new UpdateInfoService(SetActivity.this);
                    info = updateInfoService.getUpDateInfo();
                    Log.i(TAG,"收到更新信息:"+info.getUrl());
                    handler1.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            if (updateInfoService.isNeedUpdate()) {
                showUpdateDialog();
            }
        };
    };

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("请更新APP版本" + info.getVersion());
        builder.setMessage(info.getDescription());
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    downFile(info.getUrl());
                } else {
                    Toast.makeText(SetActivity.this, "SD卡未插入",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    void downFile(final String url) {
        progressDialog = new ProgressDialog(SetActivity.this);    //������������ص�ʱ��ʵʱ���½�ȣ�����û��Ѻö�
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("请稍候");
        progressDialog.setMessage("正在下载...");
        progressDialog.setProgress(0);
        progressDialog.show();
        updateInfoService.downLoadFile(url, progressDialog, handler1);
    }

}