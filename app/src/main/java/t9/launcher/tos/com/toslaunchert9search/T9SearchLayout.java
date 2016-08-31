package t9.launcher.tos.com.toslaunchert9search;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * t9 搜索器
 */
public class T9SearchLayout extends RelativeLayout implements AppSearchT9View.OnT9TelephoneDialpadView {

    TextView search_result_prompt_text_view,search_emmpy;
    GridView t9_search_grid_view;
    AppSearchT9View mAppSearchT9View;

    private AppInfoAdapter mAppInfoAdapter;
    public T9SearchLayout(Context context) {
        this(context,null);
    }

    public T9SearchLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public T9SearchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * view的onFinishInflate()何时调用的？
     * 当View中所有的子控件均被映射成xml后触发；
     * MyView mv = (MyView)View.inflate (context,R.layout.my_view,null);
     * 当加载完成xml后，就会执行那个方法；
     * 我们一般使用View的流程是在onCreate中使用setContentView来设置要显示Layout文件或直接创建一个View，
     * 在当设置了ContentView之后系统会对这个View进行解析，然后回调当前视图View中的onFinishInflate方法。
     * 只有解析了这个View我们才能在这个View容器中获取到拥有Id的组件，
     * 同样因为系统解析完View之后才会调用onFinishInflate方法，
     * 所以我们自定义组件时可以onFinishInflate方法中获取指定子View的引用。
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        search_result_prompt_text_view= (TextView) findViewById(R.id.search_result_prompt_text_view);
        search_emmpy= (TextView) findViewById(R.id.search_emmpy);
        t9_search_grid_view= (GridView) findViewById(R.id.t9_search_grid_view);
        mAppSearchT9View= (AppSearchT9View) findViewById(R.id.mAppSearchT9View);

        mAppSearchT9View.setTextInput(search_result_prompt_text_view);
        mAppSearchT9View.setOnT9TelephoneDialpadView(this);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                mAppInfoAdapter = new AppInfoAdapter(getContext(),
                        R.layout.app_info_grid_item, AppInfoHelper.getInstance()
                        .getT9SearchAppInfos());

                t9_search_grid_view.setAdapter(mAppInfoAdapter);
            }
        },2000);

    }


    @Override
    public void onAddDialCharacter(String addCharacter) {

    }

    @Override
    public void onDeleteDialCharacter(String deleteCharacter) {

    }

    @Override
    public void onDialInputTextChanged(String curCharacter) {
        updateSearch(curCharacter);
        refreshView();
    }


    @Override
    public void onHideT9TelephoneDialpadView() {

    }

    private void updateSearch(String search) {
        String curCharacter;
        if (null == search) {
            curCharacter = search;
        } else {
            curCharacter = search.trim();
        }

        if (TextUtils.isEmpty(curCharacter)) {
            AppInfoHelper.getInstance().t9Search(null);
        } else {
            AppInfoHelper.getInstance().t9Search(curCharacter);
        }
    }

    public void refreshView() {
        refreshT9SearchGv();
    }

    private void refreshT9SearchGv() {
        if (null == t9_search_grid_view) {
            return;
        }
        //t9_search_grid_view.setAdapter(mAppInfoAdapter); 52行的代码
        //所以这里的getAdapter获得的就是 mAppInfoAdapter 对象
        BaseAdapter baseAdapter = (BaseAdapter) t9_search_grid_view.getAdapter();
        if (null != baseAdapter) {
            baseAdapter.notifyDataSetChanged();//这个语句就是观察这模式的一句（提示这个baseAdapter）改变了
            Log.i("zhao11t9","baseAdapter.getCount():"+baseAdapter.getCount());
            if (baseAdapter.getCount() > 0) {
                ViewUtil.showView(t9_search_grid_view);
                ViewUtil.hideView(search_emmpy);
            } else {
                ViewUtil.hideView(t9_search_grid_view);
                ViewUtil.showView(search_emmpy);
            }
        }
    }

}
