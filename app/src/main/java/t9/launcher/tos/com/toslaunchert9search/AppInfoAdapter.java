package t9.launcher.tos.com.toslaunchert9search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppInfoAdapter extends ArrayAdapter<AppInfo> {
	private Context mContext;
	private int mTextViewResourceId;

	//private List<AppInfo> mAppInfos;

	public AppInfoAdapter(Context context, int textViewResourceId,
						  List<AppInfo> appInfos) {
        //这个父类的构造方法里传入appInfos就可以在AppInfo appInfo = getItem(position);
        //不用重写getItem方法。不知道是不是ArrayAdapter已经复写了getitem方法。
		super(context, textViewResourceId, appInfos);
		mContext = context;
		mTextViewResourceId = textViewResourceId;
		//mAppInfos = appInfos;

	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder viewHolder;
		AppInfo appInfo = getItem(position);
		if (null == convertView) {
			view = LayoutInflater.from(mContext).inflate(mTextViewResourceId,
					null);
			viewHolder = new ViewHolder();
			viewHolder.mIconIv = (ImageView) view
					.findViewById(R.id.icon_image_view);
			viewHolder.mLabelTv = (TextView) view
					.findViewById(R.id.label_text_view);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.mIconIv.setBackground(appInfo.getIcon());
		switch (appInfo.getSearchByType()) {
		case SearchByLabel:
			ViewUtil.showTextHighlight(viewHolder.mLabelTv, appInfo.getLabel(),
					appInfo.getMatchKeywords().toString());

			break;
		case SearchByNull:
			ViewUtil.showTextNormal(viewHolder.mLabelTv, appInfo.getLabel());
			break;
		default:
			break;
		}

		return view;
	}

	private class ViewHolder {
		ImageView mIconIv;
		TextView mLabelTv;
	}

}
