package nju.com.piece;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


public class TagIconView extends ImageView implements View.OnClickListener{

    private static TagIconView currentIcon = null;

    public TagIconView(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public TagIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public TagIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }

//    计算在给定边界条件下占据的高度和宽度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void deselect(){
        setBackgroundResource(0);
    }

    @Override
    public void onClick(View view) {
//        deselect the pre icon
        if (null != currentIcon){
            currentIcon.deselect();
        }
        currentIcon = this;
        currentIcon.setBackgroundResource(R.drawable.selected_icon_bkg);
    }
}
