package nju.com.piece.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import nju.com.piece.R;
import nju.com.piece.view.SlideView;
import nju.com.piece.view.TaskListView;

/**
 * 选择任务进行及时的界面
 * @author Hyman
 */


public class TaskActivity extends Activity implements OnItemClickListener,
		SlideView.OnSlideListener {

	private static final String TAG = "MainActivity";
	private TaskListView mListView;
	SlideAdapter slideAdapter;
	private List<MessageItem> mMessageItems = new ArrayList<MessageItem>();
	private SlideView mLastSlideViewWithStatusOn;
	private TimePickerDialog tpd = null;
//	private StartTaskSer startTaskSer;
	private int minutes;
	
	public static final int ADD = 1;
	public static final int TIMING = 2;
	public static final int COUNTDOWN = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);
		initView();
	}

	private void initView() {
		mListView = (TaskListView) findViewById(R.id.list);
		for (int i = 0; i < 10; i++) {
			MessageItem item = new MessageItem();
			item.taskId = i;
			item.taskname = "任务" + i;
			item.percent = i * 10;
			item.iconRes = R.drawable.default_qq_avatar;
			mMessageItems.add(item);
		}
		slideAdapter = new SlideAdapter();
		mListView.setAdapter(slideAdapter);
		mListView.setOnItemClickListener(this);
//		startTaskSer = TaskImp.getInstance();
	}

	private class SlideAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		SlideAdapter() {
			super();
			mInflater = getLayoutInflater();
		}

		@Override
		public int getCount() {
			return mMessageItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mMessageItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			SlideView slideView = (SlideView) convertView;
			if (slideView == null) {
				View itemView = mInflater.inflate(R.layout.list_item, null);
				slideView = new SlideView(TaskActivity.this);
				slideView.setContentView(itemView);
				holder = new ViewHolder(slideView);
				slideView.setOnSlideListener(TaskActivity.this);
				slideView.setTag(holder);
			} else {
				holder = (ViewHolder) slideView.getTag();
			}
			final MessageItem item = mMessageItems.get(position);
			item.slideView = slideView;
			item.slideView.reset();
			holder.icon.setImageResource(item.iconRes);
			holder.taskname.setText(item.taskname);
			holder.icon_add.setImageResource(item.icon_add);
			holder.progressBar.incrementProgressBy(item.percent);
			holder.icon_add.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根

					TimePickerDialog.OnTimeSetListener otsl = new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hour,
								int minute) {
							// TODO 自动生成的方法存根
							minutes = 60 * hour + minute;
//							if (startTaskSer.addRecord(item.taskId, minutes)) {
								 Intent intent = new Intent();  
					             intent.putExtra("minutes", minutes);   
					             intent.putExtra("taskId", item.taskId);
					             setResult(ADD , intent);     
					             finish();// 结束当前Activity的生命周期  
//							} else {
//								Toast.makeText(TaskActivity.this, "任务添加失败",
//										Toast.LENGTH_SHORT).show();
//							}
						}
					};
					tpd = new TimePickerDialog(TaskActivity.this, otsl, 1, 0,
							true);
					tpd.show();
				}
			});

			holder.countDown.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					TimePickerDialog.OnTimeSetListener otsl = new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hour,
								int minute) {
							// TODO 自动生成的方法存根
							minutes = 60 * hour + minute;
//							if (startTaskSer.countDown(item.taskId, minutes)) {
								 Intent intent = new Intent();  
					             intent.putExtra("minutes", minutes);   
					             intent.putExtra("taskId", item.taskId);
					             setResult(COUNTDOWN , intent);     
					             finish();// 结束当前Activity的生命周期  
//							} else {
//								Toast.makeText(TaskActivity.this, "任务倒计时失败",
//										Toast.LENGTH_SHORT).show();
//							}
						}
					};
					tpd = new TimePickerDialog(TaskActivity.this, otsl, 1, 0,
							true);
					tpd.show();
				}
			});

			holder.timing.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
//					if (startTaskSer.timing(item.taskId)) {
						Intent intent = new Intent();  
			             intent.putExtra("taskId", item.taskId);
			             setResult(TIMING , intent);     
			             finish();// 结束当前Activity的生命周期  
//					} else {
//						Toast.makeText(TaskActivity.this, "任务计时失败",
//								Toast.LENGTH_SHORT).show();
//					}
				}
			});
			return slideView;
		}
	}

	public class MessageItem {
		public int taskId;
		public int iconRes;
		public String taskname;
		public int percent;
		public int icon_add;
		public SlideView slideView;
	}

	private static class ViewHolder {
		public ImageView icon;
		public TextView taskname;
		public ProgressBar progressBar;
		public ImageButton icon_add;
		public ViewGroup holder;
		public ImageButton countDown;
		public ImageButton timing;

		ViewHolder(View view) {
			icon = (ImageView) view.findViewById(R.id.icon);
			taskname = (TextView) view.findViewById(R.id.taskname);
			progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
			icon_add = (ImageButton) view.findViewById(R.id.icon_add);
			holder = (ViewGroup) view.findViewById(R.id.holder);
			countDown = (ImageButton) view.findViewById(R.id.countdown);
			timing = (ImageButton) view.findViewById(R.id.timing);
		}
	}

	// item的点击事件
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.e(TAG, "onItemClick position=" + position);
		SlideView slideView = mMessageItems.get(position).slideView;
		if (slideView.isMoveClick()) {// 如果是滑动中触发的点击事件，不做处理
			return;
		}
		if (slideView.close() && slideView.getScrollX() == 0) {
			if (mLastSlideViewWithStatusOn == null
					|| mLastSlideViewWithStatusOn.getScrollX() == 0) {
				// 此处添加item的点击事件

			}
		}
	}

	@Override
	public void onSlide(View view, int status) {
		if (mLastSlideViewWithStatusOn != null
				&& mLastSlideViewWithStatusOn != view) {
			mLastSlideViewWithStatusOn.shrink();
		}

		if (status == SLIDE_STATUS_ON) {
			mLastSlideViewWithStatusOn = (SlideView) view;
		}
	}
	
}
