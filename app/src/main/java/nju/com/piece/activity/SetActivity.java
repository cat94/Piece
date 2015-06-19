package nju.com.piece.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import nju.com.piece.R;
import nju.com.piece.database.DBFacade;
import nju.com.piece.logic.sync.SyncRecords;
import nju.com.piece.logic.update.UpdateInfo;
import nju.com.piece.logic.update.UpdateInfoService;

/**
 * @author Hyman
 */
public class SetActivity extends BaseActionBarActivity implements OnClickListener {
    private UpdateInfo info;
    private ProgressDialog progressDialog;
    UpdateInfoService updateInfoService;
    private static String TAG = "SetActivity";
    static Context context;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        context = SetActivity.this;
        progressBar = (ProgressBar) findViewById(R.id.set_progressBar);
        LinearLayout resetpsw = (LinearLayout) findViewById(R.id.resetpsw);
        LinearLayout update = (LinearLayout) findViewById(R.id.update);
        LinearLayout task = (LinearLayout) findViewById(R.id.task);
        LinearLayout logout = (LinearLayout) findViewById(R.id.logout);
        resetpsw.setOnClickListener(this);
        task.setOnClickListener(this);
        update.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.resetpsw:
                Toast.makeText(context, "这应该打开修改密码", Toast.LENGTH_SHORT).show();
                break;
            case R.id.task:
                SyncRecords syncRecords = new SyncRecords(context, progressBar);
                syncRecords.sync();
                Toast.makeText(context, "正在同步任务", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                DBFacade dbFacade=new DBFacade(this);
                dbFacade.clearAccount();
                intent=new Intent(SetActivity.this,LoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.update:
                checkUpdate();
                break;

        }
    }


    public void checkUpdate() {
        Toast.makeText(SetActivity.this, "正在检查更新", Toast.LENGTH_SHORT).show();
        new Thread() {
            public void run() {
                try {
                    updateInfoService = new UpdateInfoService(SetActivity.this);
                    info = updateInfoService.getUpDateInfo();
                    Log.i(TAG, "收到更新信息:" + info.getUrl());
                    handler1.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            if (updateInfoService.isNeedUpdate()) {
                showUpdateDialog();
            }
        }

        ;
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
        progressDialog = new ProgressDialog(SetActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("请稍候");
        progressDialog.setMessage("正在下载...");
        progressDialog.setProgress(0);
        progressDialog.show();
        updateInfoService.downLoadFile(url, progressDialog, handler1);
    }

}