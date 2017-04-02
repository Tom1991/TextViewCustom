package customtextview.tomcode.cc.textviewcustom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Administrator on 2017/3/21.
 */

public class CustomTextView extends View {

    private String mText;
    private int mColor;
    private float mTextSize;
    private Paint mPaint;
    private Rect mRect;

    public CustomTextView(Context context) {
        //调用2个参数的构造方法
        this(context,null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        //调用三个参数的构造方法，在Xml文件中使用这个
        this(context, attrs,0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //在这里取出attrs中定义的值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomView);

        mText = ta.getString(R.styleable.CustomView_Text);
        mColor = ta.getColor(R.styleable.CustomView_TextColor, 0);
        mTextSize = ta.getDimension(R.styleable.CustomView_TextSize, 20);
        //回收
        ta.recycle();

        //给自定义的View设置一个点击事件
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mText=randomText();
                postInvalidate();
            }
        });

        //主要是为了获取文本的尺寸
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        //存放尺寸的盒子
        mRect = new Rect();
        mPaint.getTextBounds(mText,0,mText.length(),mRect);
    }

    /**
     * 自定义View一般需要重写的2个方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 如果不自己测量，在设置属性为wrap_content时就会出现铺满父控件的现象，失控了
         *
         EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
         AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
         UNSPECIFIED：表示子布局想要多大就多大，很少使用
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode==MeasureSpec.EXACTLY){
            width=widthSize;
        }else {
            //测量出字体的宽度
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText,0,mText.length(),mRect);
            int textWidth = mRect.width();
            //获取两侧的内边距
            width=getPaddingLeft()+textWidth+getPaddingRight();
        }

        if (heightMode==MeasureSpec.EXACTLY){
            height=heightSize;
        }else {
            //测量出字体的高度
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText,0,mText.length(),mRect);
            int textHeight = mRect.height();
            //获取上下的内边距
            height=getPaddingTop()+textHeight+getPaddingBottom();
        }
        //使用测量好的宽高
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //先画最下面的一层
        mPaint.setColor(Color.GRAY);
        /**
         * 左边距，上边距，右边距和下边距，画笔
         * 右边距就是宽，调用系统的测量方法
         * 下边距就是高，调用系统侧测量方法
         */
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

        //画上面的文字
        mPaint.setColor(mColor);
        /**
         * getWidth和getHeight获取的而是自定义的这个View的宽度和高度
         * mRect.width()和mRect.height()获取的是文本的宽和高
         */
        canvas.drawText(mText,getWidth()/2-mRect.width()/2,getHeight()/2+mRect.height()/2,mPaint);
    }

    public String randomText(){
        {
            Random random = new Random();
            Set<Integer> set = new HashSet<Integer>();
            while (set.size() < 4)
            {
                int randomInt = random.nextInt(10);
                set.add(randomInt);
            }
            StringBuffer sb = new StringBuffer();
            for (Integer i : set)
            {
                sb.append("" + i);
            }
            return sb.toString();
        }
    };
}
