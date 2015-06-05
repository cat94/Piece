package nju.com.piece.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nju.com.piece.R;

/**
 * Created by Hyman on 2015/6/5.
 * 自定义Topbar。
 */
public class Topbar extends RelativeLayout {

    //控件
    private Button leftButton,rightButton;
    private TextView tvTitle;

    //左按钮的属性
    private  int leftTextColor;
    private Drawable leftBackground;
    private String leftText;

    //右按钮的属性
    private  int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    //标题的属性
    private float titleTextSize;
    private int titleTextColor;
    private String titleText;

    //要把控件加入到layout所需要的属性
    private LayoutParams leftParams, rirhtParams, titleParams;

    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);

        //从自定义的attrs.xml中取出属性值
        leftTextColor = ta.getColor(R.styleable.TopBar_leftTextColor, Color.BLACK);
        leftBackground = ta.getDrawable(R.styleable.TopBar_leftBackground);
        leftText = ta.getString(R.styleable.TopBar_leftText);
        rightTextColor = ta.getColor(R.styleable.TopBar_rightTextColor, Color.BLACK);
        rightBackground = ta.getDrawable(R.styleable.TopBar_rightBackground);
        rightText = ta.getString(R.styleable.TopBar_rightText);
        titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor, Color.BLACK);
        titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize, 12);
        titleText = ta.getString(R.styleable.TopBar_titleText);

        ta.recycle();//避免浪费资源，也避免缓存产生的错误

        //创建控件
        leftButton = new Button(context);
        rightButton = new Button(context);
        tvTitle = new TextView(context);

        //为控件赋值
        leftButton.setText(leftText);
        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackground);
        rightButton.setText(rightText);
        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackground);
        tvTitle.setText(titleText);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setGravity(Gravity.CENTER);

        //设置背景颜色
        setBackgroundColor(0xfff59563);

        //设置布局 - 左
        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        leftParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        addView(leftButton, leftParams);

        //设置布局 - 右
        rirhtParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rirhtParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        rirhtParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        addView(rightButton, rirhtParams);//将按钮添加进布局中

        //设置布局 - 标题
        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(tvTitle, titleParams);//将按钮添加进布局中
    }
}
