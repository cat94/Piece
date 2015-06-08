package nju.com.piece.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import nju.com.piece.activity.TaskActivity;
import nju.com.piece.database.pos.TagPO;
import nju.com.piece.view.SlideView;

public class TaskListView extends ListView {

    private static final String TAG = "ListViewCompat";

    private SlideView mFocusedItemView;

    private int position;

    private Map<TagPO, SlideView> slideViewMap = new HashMap<TagPO, SlideView>();
    
    /**
     * 下面是三个默认的构造函数
     */
    public TaskListView(Context context) {
        super(context);
    }

    public TaskListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TaskListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    
    public void shrinkListItem(int position) {
        View item = getChildAt(position);
        if (item != null) {
            try {
                ((SlideView) item).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	int x = (int) event.getX();
    	int y = (int) event.getY();
    	//想知道当前点击了哪一行  
    	position = pointToPosition(x, y);
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            Log.e(TAG, "postion=" + position);
            if (position != INVALID_POSITION) {
                TagPO data = (TagPO) getItemAtPosition(position);
                mFocusedItemView = slideViewMap.get(data);
                Log.e(TAG, "FocusedItemView=" + mFocusedItemView);
            }
        }
        default:
            break;
        }
        
        if (mFocusedItemView != null) {
        	if(position == INVALID_POSITION){
                mFocusedItemView.shrink();
        		return super.onTouchEvent(event);
        	}
            mFocusedItemView.onRequireTouchEvent(event);
        }

        return super.onTouchEvent(event);
    }

    public int getPosition() {
		return position;
	}

    public void setSlideViewMap(Map<TagPO, SlideView> slideViewMap) {
        this.slideViewMap = slideViewMap;
    }
}
