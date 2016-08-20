package t9.launcher.tos.com.toslaunchert9search;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * T9拨号盘 3*4 T9拨号盘
 */
public class AppSearchT9View extends ViewGroup implements View.OnClickListener {
    private int col=3;
    private int row=4;

    private TextView mT9InputEt;
    private ImageView mDialDeleteBtn;
    public void setTextInput(TextView mT9InputEt){
        this.mT9InputEt=mT9InputEt;

        mT9InputEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("zhao11t9","afterTextChanged");
                if (null != mOnT9TelephoneDialpadView) {
                    String inputStr=s.toString();
                    mOnT9TelephoneDialpadView.onDialInputTextChanged(inputStr);
                }
            }
        });
    }
    private OnT9TelephoneDialpadView mOnT9TelephoneDialpadView = null;
    public OnT9TelephoneDialpadView getOnT9TelephoneDialpadView() {
        return mOnT9TelephoneDialpadView;
    }

    public void setOnT9TelephoneDialpadView(
            OnT9TelephoneDialpadView onT9TelephoneDialpadView) {
        mOnT9TelephoneDialpadView = onT9TelephoneDialpadView;
    }
    public AppSearchT9View(Context context) {
        super(context);
        init(context,null);
    }

    public AppSearchT9View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }


    public  void init(Context context,AttributeSet attrs){
        setPadding(25,25,25,25);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        for (int i = 0; i < getChildCount(); i++) {
            View v =getChildAt(i);
            if(v.getId()==R.id.dial_delete_btn){
                mDialDeleteBtn = (ImageView) v;
                mDialDeleteBtn.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        deleteAllDialCharacter();
                        return true;
                    }
                });
            }
            v.setOnClickListener(this);
        }


    }

    /**
     * Interface definition for a callback to be invoked when a
     * T9TelephoneDialpadView is operated.
     */
    public interface OnT9TelephoneDialpadView {
        void onAddDialCharacter(String addCharacter);

        void onDeleteDialCharacter(String deleteCharacter);

        void onDialInputTextChanged(String curCharacter);

        void onHideT9TelephoneDialpadView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //measure每个图标的大小
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        int paddingLeft=getPaddingLeft();
        int paddingRight=getPaddingRight();
        int padingTop=getPaddingTop();
        int padingBottom=getPaddingBottom();
        int gridWidth=(width-paddingLeft-paddingRight)/col;
        int gridHeight=(height-padingTop-padingBottom)/row;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v != null) {
                int childWidthSpec = View.MeasureSpec.makeMeasureSpec(gridWidth, widthMode);
                int childHeightSpec = View.MeasureSpec.makeMeasureSpec(gridHeight, heightMode);
                v.measure(childWidthSpec, childHeightSpec);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // layout child
        int paddingLeft=getPaddingLeft();
        int paddingRight=getPaddingRight();
        int padingTop=getPaddingTop();
        int padingBottom=getPaddingBottom();
        int releaseWidth=(getMeasuredWidth()-paddingLeft-paddingRight)/col;
        int releaseHeiht=(getMeasuredHeight()-padingTop-padingBottom)/row;
        int iconOffsetX=paddingLeft;
        int iconOffsetY=padingTop;
        int left;
        int top;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            //行
            int currentRow=i/col;
            //列  row=1 col=4
            int currentCol=i%col;
            left=currentCol*releaseWidth+iconOffsetX;
            top=currentRow*releaseHeiht+iconOffsetY;
            if (v != null) {
                v.layout(left, top, left+releaseWidth, top+releaseHeiht);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.dial_delete_btn:
                deleteSingleDialCharacter();
                break;
            case R.id.dialNum0:
            case R.id.dialNum1:
            case R.id.dialNum2:
            case R.id.dialNum3:
            case R.id.dialNum4:
            case R.id.dialNum5:
            case R.id.dialNum6:
            case R.id.dialNum7:
            case R.id.dialNum8:
            case R.id.dialNum9:
                addSingleDialCharacter(v.getTag().toString());
                break;

            default:
                break;
        }

    }
    public void deleteAllDialCharacter() {
        String curInputStr = mT9InputEt.getText().toString();
        if (curInputStr.length() > 0) {
            String deleteCharacter = curInputStr.substring(0,
                    curInputStr.length());
            if (null != mOnT9TelephoneDialpadView) {
                mOnT9TelephoneDialpadView
                        .onDeleteDialCharacter(deleteCharacter);
            }
            mT9InputEt.setText("");
            Log.i("zhao11t9", "222222222");
//            ViewUtil.hideView(mDialDeleteBtn);
        }
    }
    public void deleteSingleDialCharacter() {
        String curInputStr = mT9InputEt.getText().toString();
        if (curInputStr.length() > 0) {
            String deleteCharacter = curInputStr.substring(
                    curInputStr.length() - 1, curInputStr.length());
            if (null != mOnT9TelephoneDialpadView) {
                mOnT9TelephoneDialpadView
                        .onDeleteDialCharacter(deleteCharacter);
            }

            String newCurInputStr=curInputStr.substring(0,curInputStr.length() - 1);
            mT9InputEt.setText(newCurInputStr);
            //mT9InputEt.setSelection(newCurInputStr.length());
//            if(TextUtils.isEmpty(newCurInputStr)){
//                ViewUtil.hideView(mDialDeleteBtn);
//            }else{
//                ViewUtil.showView(mDialDeleteBtn);
//            }


        }
    }

    private void addSingleDialCharacter(String addCharacter) {
        Log.i("zhao11t9","333333333333");
        String preInputStr = mT9InputEt.getText().toString();
        if (!TextUtils.isEmpty(addCharacter)) {
            mT9InputEt.setText(preInputStr + addCharacter);
        //    mT9InputEt.setSelection(mT9InputEt.getText().length());
            if (null != mOnT9TelephoneDialpadView) {
                mOnT9TelephoneDialpadView.onAddDialCharacter(addCharacter);
            }
         //   ViewUtil.showView(mDialDeleteBtn);
        }

    }

}
