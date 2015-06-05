package nju.com.piece.logic.tasktiming;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
/**
 * 用于计时的后台服务类。
 * @author Hyman
 */
public class TaskService extends Service {

	private static final String TAG = "TaskService";
	private int timingseconds; // 已计时的秒数
	private boolean timingFlag = true; // 用于判断是否要继续
	private int taskId = -1;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i(TAG, "BindService--onCreate()");
		super.onCreate();
	}

	public class MyBinder extends Binder {
		public TaskService getService() {
			return TaskService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "BindService--onBind()");
		return new MyBinder();
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		// TODO Auto-generated method stub
		Log.i(TAG, "BindService--unbindService()");
		super.unbindService(conn);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "BindService--onDestroy()");
		super.onDestroy();
	}

	public boolean timing(int taskId) {
		if(this.taskId!=-1){   //说明有任务正在执行
			Log.i(TAG, "正计时失败，当前的taskId:"+taskId);
			return false;
		}
		Log.i(TAG, "调用正计时方法");
		this.taskId = taskId;
		timingFlag=true;
		timingseconds = 0; // 初始化为0，重新计时
		TimingTask task = new TimingTask();
		task.execute();
		return true;
	}

	public boolean countDown(int taskId, int minutes) {
		if(this.taskId!=-1){  //说明有任务正在执行
			Log.i(TAG, "倒计时失败，当前的taskId:"+taskId);
			return false;
		}
		this.taskId = taskId;
		timingFlag=true;
		int countdownseconds = minutes * 60;
		timingseconds = 0; // 初始化为0，重新计时
		Log.i(TAG, "开始倒计时，秒数：" + countdownseconds);
		this.taskId = taskId;
		CountDownTask task = new CountDownTask();
		task.execute(countdownseconds);
		return true;
	}

	public int stop(int taskId) {
		Log.i(TAG, "调用停止计时方法");
		timingFlag = false;
		return timingseconds; // 如果计时时间超过60s，返回计时成功，否则返回计时失败
	}

	// 正计时的异步任务类
	class TimingTask extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... arg0) {
			// TODO 自动生成的方法存根
			try {
				while (timingFlag) {
					timingseconds++;
					Thread.sleep(1000);
					if (timingseconds % 10 == 0) {
						Log.i(TAG, "正在正计时，秒数：" + timingseconds);
					}
				}
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			Log.i(TAG, "结束正计时,秒数:" + timingseconds);
			// 这里做一些结束计时之后的事情
			do_after_timing();
		}
	}

	// 倒计时的异步任务类
	class CountDownTask extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... arg0) {
			// TODO 自动生成的方法存根
			int countdownseconds = arg0[0];
			try {
				while (timingFlag && timingseconds < countdownseconds) {
					timingseconds++;
					Thread.sleep(1000);
					if (timingseconds % 10 == 0) {
						Log.i(TAG, "正在倒计时，秒数：" +(countdownseconds-timingseconds));
					}
				}
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO 自动生成的方法存根
			super.onPostExecute(result);
			Log.i(TAG, "结束倒计时,秒数:" + timingseconds);
			// 这里做一些结束计时之后的事情
			do_after_timing();
		}
	}

	// 结束计时之后的操作
	private void do_after_timing() {
		Log.i(TAG, "添加记录:任务" + taskId + "计时" 
					+ timingseconds);
		timingseconds = 0;
		taskId = -1;
		timingFlag = true;
		
	}

}
