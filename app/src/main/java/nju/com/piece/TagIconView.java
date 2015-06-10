package nju.com.piece;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;


public class TagIconView extends ImageView{

    public TagIconView(Context context) {
        super(context);
    }

    public TagIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

}
