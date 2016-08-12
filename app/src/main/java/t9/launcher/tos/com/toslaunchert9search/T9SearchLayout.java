package t9.launcher.tos.com.toslaunchert9search;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
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
        super(context);
    }

    public T9SearchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public T9SearchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

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

        BaseAdapter baseAdapter = (BaseAdapter) t9_search_grid_view.getAdapter();
        if (null != baseAdapter) {
            baseAdapter.notifyDataSetChanged();
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
