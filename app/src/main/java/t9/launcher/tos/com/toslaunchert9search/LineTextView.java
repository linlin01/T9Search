package t9.launcher.tos.com.toslaunchert9search;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 带下划线的textView
 */
public class LineTextView extends TextView {
//    Paint mPaint=new Paint();
    public LineTextView(Context context) {
        super(context);
        init();
    }

    public LineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
//        mPaint.setStyle(Paint.Style.STROKE);//设置非填充
//        mPaint.setStrokeWidth(1);//笔宽5像素
//        mPaint.setColor(Color.BLACK);//设置为红笔
//        mPaint.setAntiAlias(true);//锯齿不显示
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(TextUtils.isEmpty(getText())){
            super.onDraw(canvas);
            return;
        }
        canvas.save();
        super.onDraw(canvas);
        canvas.drawLine(0,getMeasuredHeight(),getMeasuredWidth(),getMeasuredHeight(),getPaint());
        canvas.restore();
    }
}
