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
 * �Զ���Topbar��
 */
public class Topbar extends RelativeLayout {

    //�ؼ�
    private Button leftButton,rightButton;
    private TextView tvTitle;

    //��ť������
    private  int leftTextColor;
    private Drawable leftBackground;
    private String leftText;

    //�Ұ�ť������
    private  int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    //���������
    private float titleTextSize;
    private int titleTextColor;
    private String titleText;

    //Ҫ�ѿؼ����뵽layout����Ҫ������
    private LayoutParams leftParams, rirhtParams, titleParams;

    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);

        //���Զ����attrs.xml��ȡ������ֵ
        leftTextColor = ta.getColor(R.styleable.TopBar_leftTextColor, Color.BLACK);
        leftBackground = ta.getDrawable(R.styleable.TopBar_leftBackground);
        leftText = ta.getString(R.styleable.TopBar_leftText);
        rightTextColor = ta.getColor(R.styleable.TopBar_rightTextColor, Color.BLACK);
        rightBackground = ta.getDrawable(R.styleable.TopBar_rightBackground);
        rightText = ta.getString(R.styleable.TopBar_rightText);
        titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor, Color.BLACK);
        titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize, 12);
        titleText = ta.getString(R.styleable.TopBar_titleText);

        ta.recycle();//�����˷���Դ��Ҳ���⻺������Ĵ���

        //�����ؼ�
        leftButton = new Button(context);
        rightButton = new Button(context);
        tvTitle = new TextView(context);

        //Ϊ�ؼ���ֵ
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

        //���ñ�����ɫ
        setBackgroundColor(0xfff59563);

        //���ò��� - ��
        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        leftParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        addView(leftButton, leftParams);

        //���ò��� - ��
        rirhtParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rirhtParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        rirhtParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        addView(rightButton, rirhtParams);//����ť��ӽ�������

        //���ò��� - ����
        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(tvTitle, titleParams);//����ť��ӽ�������
    }
}
