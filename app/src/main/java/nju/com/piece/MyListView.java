package nju.com.piece;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by shen on 15/6/8.
 */
public class MyListView extends ListView {

    boolean expanded = false;

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public class ExpandableListView extends ListView {

        boolean expanded = false;

        public ExpandableListView(Context context, AttributeSet attrs, int defaultStyle) {
            super(context, attrs, defaultStyle);
        }

        public boolean isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // HACK!  TAKE THAT ANDROID!
            if (isExpanded()) {
                // Calculate entire height by providing a very large height hint.
                // View.MEASURED_SIZE_MASK represents the largest height possible.
                int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                        MeasureSpec.AT_MOST);
                super.onMeasure(widthMeasureSpec, expandSpec);

                LayoutParams params = (LayoutParams) getLayoutParams();
                params.height = getMeasuredHeight();
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

}
