package nju.com.piece.logic.tasktiming;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
/**
 * 实现类，介绍请戳他的接口。
 * @author Hyman
 */
public class TaskImp implements StartTaskSer {

	private static TaskService service;
	private static Context context;
	private static final String TAG = "TaskImp";
    private static TaskImp taskImp;
	
	private TaskImp() {
	}
	
	public  static TaskImp getInstance(){
		return taskImp;
	}
	
	public TaskImp(Context context) {
		this.context = context;
		taskImp=this;
		ini();
	}

	ServiceConnection conn = new ServiceConnection() {
		@Override
		// 当服务跟启动源断开的时候 会自动回调
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
		}

		@Override
		// 当服务跟启动源连接的时候 会自动回调
		public void onServiceConnected(ComponentName name, IBinder binder) {
			// TODO Auto-generated method stub
			Log.i(TAG, "绑定成功");
			service = ((TaskService.MyBinder) binder).getService();
		}
	};

	private void ini() {
		Log.i(TAG, "ini");
		Log.i(TAG, "context" + context);
		Intent taskService = new Intent(context, TaskService.class);
		context.startService(taskService);
		context.bindService(taskService, conn, Service.BIND_AUTO_CREATE);
	}

	@Override
	public boolean addRecord(int taskId, int minutes) {
		// TODO 自动生成的方法存根
		//这个正确与否要先留着，等到具体添加记录的逻辑写完了再补充
		return Math.random() > 0.5;
	}

	@Override
	public boolean timing(int taskId) {
		// TODO 自动生成的方法存根
		return service.timing(taskId);
	}

	@Override
	public boolean countDown(int taskId, int minutes) {
		// TODO 自动生成的方法存根
		return service.countDown(taskId, minutes);
	}

	@Override
	public int stop(int taskId) {
		// TODO 自动生成的方法存根
		return service.stop(taskId);
	}

}
