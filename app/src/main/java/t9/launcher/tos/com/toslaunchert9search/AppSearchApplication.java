package t9.launcher.tos.com.toslaunchert9search;

import android.app.Application;
import android.content.Context;

public class AppSearchApplication extends Application {
	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		AppInfoHelper.getInstance().startLoadAppInfo();

	}

	public static Context getContext() {
		return mContext;
	}

}
