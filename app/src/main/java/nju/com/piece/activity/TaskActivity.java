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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nju.com.piece.R;
import nju.com.piece.database.DBFacade;
import nju.com.piece.database.pos.PeriodPO;
import nju.com.piece.database.pos.TagPO;
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
//	private List<MessageItem> mMessageItems = new ArrayList<MessageItem>();
	private List<TagPO> tagPOs = new ArrayList<TagPO>();
	private Map<TagPO, SlideView> slideViewMap = new HashMap<TagPO, SlideView>();
	private SlideView mLastSlideViewWithStatusOn;
	private TimePickerDialog tpd = null;
//	private StartTaskSer startTaskSer;
	private int minutes;
	
	public static final int ADD = 1;
	public static final int TIMING = 2;
	public static final int COUNTDOWN = 3;

	private DBFacade dbFacade = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);
		this.dbFacade = new DBFacade(this);
		initView();
	}

	private void initView() {
		mListView = (TaskListView) findViewById(R.id.list);
		tagPOs = dbFacade.getAllTags();
		slideAdapter = new SlideAdapter();
		mListView.setAdapter(slideAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setSlideViewMap(slideViewMap);
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
			return tagPOs.size();
		}

		@Override
		public Object getItem(int position) {
			return tagPOs.get(position);
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
			final TagPO item = tagPOs.get(position);
			slideViewMap.put(item, slideView);
			slideView.reset();
			holder.icon.setImageResource(item.getResource());
			holder.taskname.setText(item.getTagName());
			// TODO 这个图片对吗。。为什么每个都要加一遍。。
			holder.icon_add.setImageResource(R.drawable.actionbar_add_icon);
			holder.progressBar.incrementProgressBy(item.getCurrentMinute()/item.getTargetMinute());
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
					             intent.putExtra("length", minutes);
					             intent.putExtra("tag", item);
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
					             intent.putExtra("length", minutes);
					             intent.putExtra("tag", item);
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
			             intent.putExtra("tag", item);
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
//
//	public class MessageItem {
//		public int taskId;
//		public int iconRes;
//		public String taskname;
//		public int percent;
//		public int icon_add;
//		public SlideView slideView;
//	}

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
		SlideView slideView = slideViewMap.get(tagPOs.get(position));
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
