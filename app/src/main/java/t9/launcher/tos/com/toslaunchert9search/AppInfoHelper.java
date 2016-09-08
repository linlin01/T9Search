package t9.launcher.tos.com.toslaunchert9search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.text.TextUtils;
import android.util.Log;

import com.pinyinsearch.model.PinyinSearchUnit;
import com.pinyinsearch.util.PinyinUtil;
import com.pinyinsearch.util.QwertyUtil;
import com.pinyinsearch.util.T9Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import t9.launcher.tos.com.toslaunchert9search.AppInfo.SearchByType;


public class AppInfoHelper {
	private static final String TAG="zhao11t9";
	private static Character THE_LAST_ALPHABET='z';
	private Context mContext;
	private static AppInfoHelper mInstance;

	private List<AppInfo> mBaseAllAppInfos;

    //搜索结果列表
	private List<AppInfo> mT9SearchAppInfos;

    //这个变量是记录第一次搜索为空的数字串而且以后保持不变只到删除key串
	private StringBuffer mFirstNoT9SearchResultInput=null;
	
	private AsyncTask<Object, Object, List<AppInfo>> mLoadAppInfoTask=null;
	private boolean mAppInfoChanged=true;

	public interface OnAppInfoLoad{
		void onAppInfoLoadSuccess();
		void onAppInfoLoadFailed();
	}
	
	public static AppInfoHelper getInstance(){
		if(null==mInstance){
			mInstance=new AppInfoHelper();
		}
		
		return mInstance;
	} 
	
	private AppInfoHelper(){
		initAppInfoHelper();
		
		return;
	}
	
	private void initAppInfoHelper(){
		mContext=AppSearchApplication.getContext();
		
		clearAppInfoData();
		
		return;
	}


	public void setBaseAllAppInfos(List<AppInfo> baseAllAppInfos) {
		mBaseAllAppInfos = baseAllAppInfos;
	}

	public List<AppInfo> getT9SearchAppInfos() {
		return mT9SearchAppInfos;
	}

	public boolean isAppInfoChanged() {
		return mAppInfoChanged;
	}

	public void setAppInfoChanged(boolean appInfoChanged) {
		mAppInfoChanged = appInfoChanged;
	}
	
	public boolean startLoadAppInfo(){
		if(true==isLoading()){
			return false;
		}
		
		if(false==isAppInfoChanged()){
			return false;
		}
		
		clearAppInfoData();
		mLoadAppInfoTask=new AsyncTask<Object, Object, List<AppInfo>>(){

			@Override
			protected List<AppInfo> doInBackground(Object... params) {
				// TODO Auto-generated method stub
				return loadAppInfo(mContext);
			}

			@Override
			protected void onPostExecute(List<AppInfo> result) {
				parseAppInfo(result);
				super.onPostExecute(result);
				//setAppInfoChanged(false);
				mLoadAppInfoTask=null;
			}
			
		}.execute();
		setAppInfoChanged(false);
		return true;
		
	}
	
	@SuppressLint("DefaultLocale")
	public List<AppInfo> loadAppInfo(Context context){
		List<AppInfo> appInfos=new ArrayList<AppInfo>();
		List<AppInfo> kanjiStartAppInfos = new ArrayList<AppInfo>();
		List<AppInfo> nonKanjiStartAppInfos = new ArrayList<AppInfo>();
		do{
			if(null==context){
				break;
			}
			
			PackageManager pm=context.getPackageManager();
			
			long startLoadTime= System.currentTimeMillis();
			int flags = PackageManager.GET_UNINSTALLED_PACKAGES;
		
		
			List<PackageInfo> packageInfos=pm.getInstalledPackages(flags);
			Log.i(TAG, packageInfos.size()+"");
			for(PackageInfo pi:packageInfos){
				boolean canLaunchTheMainActivity=AppUtil.appCanLaunchTheMainActivity(mContext, pi.packageName);
				if(true==canLaunchTheMainActivity){
					AppInfo appInfo=getAppInfo(pm, pi);
					if(TextUtils.isEmpty(appInfo.getLabel())){
						continue;
					}
					
					appInfo.getLabelPinyinSearchUnit().setBaseData(appInfo.getLabel());
					PinyinUtil.parse(appInfo.getLabelPinyinSearchUnit());
					String sortKey= PinyinUtil.getSortKey(appInfo.getLabelPinyinSearchUnit()).toUpperCase();
					appInfo.setSortKey(praseSortKey(sortKey));
					boolean isKanji= PinyinUtil.isKanji(appInfo.getLabel().charAt(0));
					if(true==isKanji){
						kanjiStartAppInfos.add(appInfo);
					}else{
						nonKanjiStartAppInfos.add(appInfo);
					}

				}
			}
			long endLoadTime= System.currentTimeMillis();
			Log.i(TAG, "endLoadTime-startLoadTime["+(endLoadTime-startLoadTime)+"]");
			//Toast.makeText(mContext, "endLoadTime-startLoadTime["+(endLoadTime-startLoadTime)+"]", Toast.LENGTH_LONG).show();
			break;
		}while(false);
		
		long sortStartTime= System.currentTimeMillis();
		
		Collections.sort(kanjiStartAppInfos, AppInfo.mAscComparator);
		Collections.sort(nonKanjiStartAppInfos, AppInfo.mAscComparator);
		
		//appInfos.addAll(nonKanjiStartAppInfos);
		appInfos.addAll(kanjiStartAppInfos);
	
		/*Start: merge nonKanjiStartAppInfos and kanjiStartAppInfos*/
		int lastIndex=0;
		boolean shouldBeAdd=false;
		for(int i=0; i<nonKanjiStartAppInfos.size(); i++){
			String nonKanfirstLetter= PinyinUtil.getFirstLetter(nonKanjiStartAppInfos.get(i).getLabelPinyinSearchUnit());
			//Log.i(TAG, "nonKanfirstLetter=["+nonKanfirstLetter+"]["+nonKanjiStartAppInfos.get(i).getLabel()+"]["+Integer.valueOf(nonKanjiStartAppInfos.get(i).getLabel().charAt(0))+"]");
			int j=0;
			for(j=0+lastIndex; j<appInfos.size(); j++){
				String firstLetter= PinyinUtil.getFirstLetter(appInfos.get(j).getLabelPinyinSearchUnit());
				lastIndex++;
				if(nonKanfirstLetter.charAt(0)<firstLetter.charAt(0)||nonKanfirstLetter.charAt(0)>THE_LAST_ALPHABET){
					shouldBeAdd=true;
					break;
				}else{
					shouldBeAdd=false;
				}
			}
			
			if(lastIndex>=appInfos.size()){
				lastIndex++;
				shouldBeAdd=true;
				//Log.i(TAG, "lastIndex="+lastIndex);
			}
			
			if(true==shouldBeAdd){
				appInfos.add(j, nonKanjiStartAppInfos.get(i));
				shouldBeAdd=false;
			}
		}
		/*End: merge nonKanjiStartAppInfos and kanjiStartAppInfos*/

		long sortEndTime= System.currentTimeMillis();
		Log.i(TAG, "sortEndTime-sortStartTime["+(sortEndTime-sortStartTime)+"]");
	
		Log.i(TAG, "appInfos.size()"+ appInfos.size());
		//Toast.makeText(context,"["+ appInfos.get(0).getLabel()+"]["+appInfos.get(0).getPackageName()+"]", Toast.LENGTH_LONG).show();
		return appInfos;
	}

	public void t9Search(String keyword){
		List<AppInfo> baseAppInfos=getBaseAppInfo();
		Log.i(TAG, "baseAppInfos["+baseAppInfos.size()+"]");
		if(null != mT9SearchAppInfos){
			mT9SearchAppInfos.clear();
		}else{
			mT9SearchAppInfos = new ArrayList<AppInfo>();
		}

        //下边这个判断是输入数字为空时候才执行的，keyword为输入数字
		if(TextUtils.isEmpty(keyword)){
			for(AppInfo ai:baseAppInfos){
				ai.setSearchByType(SearchByType.SearchByNull);
				ai.clearMatchKeywords();
				ai.setMatchStartIndex(-1);
				ai.setMatchLength(0);
			}
			
			mT9SearchAppInfos.addAll(baseAppInfos);

			mFirstNoT9SearchResultInput.delete(0, mFirstNoT9SearchResultInput.length());
			Log.i(TAG, "null==search,mFirstNoT9SearchResultInput.length()="
                    + mFirstNoT9SearchResultInput.length()+","+
                    mFirstNoT9SearchResultInput);
			return;
		}

        //mFirstNoT9SearchResultInput 这个变量是记录第一次没有结果的数字串
		//这个判断是说已经有为搜索的数字穿记录了，
		if (mFirstNoT9SearchResultInput.length() > 0) {
            //再继续输入数字的话直接走这个方法来返回空的搜索结果就可以了
			if (keyword.contains(mFirstNoT9SearchResultInput.toString())) {
				Log.i(TAG,
						"no need  to search,null!=search,mFirstNoT9SearchResultInput.length()="
								+ mFirstNoT9SearchResultInput.length() + "["
								+ mFirstNoT9SearchResultInput.toString() + "]"
								+ ";searchlen=" + keyword.length() + "["
								+ keyword + "]");
				return;
			} else {//else 就是说当前输入的keyword被删除了（就是按了退格键）
				Log.i(TAG,
						"delete  mFirstNoT9SearchResultInput, null!=search,mFirstNoT9SearchResultInput.length()="
								+ mFirstNoT9SearchResultInput.length()
								+ "["
								+ mFirstNoT9SearchResultInput.toString()
								+ "]"
								+ ";searchlen="
								+ keyword.length()
								+ "["
								+ keyword + "]");
                //将mFirstNoT9SearchResultInput置为空
				mFirstNoT9SearchResultInput.delete(0,mFirstNoT9SearchResultInput.length());
			}
		}

		mT9SearchAppInfos.clear();
		int baseAppInfosCount=baseAppInfos.size();
		for(int i=0; i<baseAppInfosCount; i++){
			PinyinSearchUnit labelPinyinSearchUnit=baseAppInfos.get(i).getLabelPinyinSearchUnit();
//		    Log.i(TAG,"labelPinyinSearchUnit:"+labelPinyinSearchUnit.getPinyinUnits()+",keyword:"+keyword);
			boolean match= T9Util.match(labelPinyinSearchUnit,keyword);
			
			if (true == match) {// search by LabelPinyinUnits;
				AppInfo appInfo = baseAppInfos.get(i);
				appInfo.setSearchByType(SearchByType.SearchByLabel);
				appInfo.setMatchKeywords(labelPinyinSearchUnit.getMatchKeyword().toString());
				appInfo.setMatchStartIndex(appInfo.getLabel().indexOf(appInfo.getMatchKeywords().toString()));
				appInfo.setMatchLength(appInfo.getMatchKeywords().length());
				mT9SearchAppInfos.add(appInfo);

				continue;
			}
		}
		
		if (mT9SearchAppInfos.size() <= 0) {
			if (mFirstNoT9SearchResultInput.length() <= 0) {
				mFirstNoT9SearchResultInput.append(keyword);
				Log.i(TAG,
						"no search result,null!=search,mFirstNoT9SearchResultInput.length()="
								+ mFirstNoT9SearchResultInput.length() + "["
								+ mFirstNoT9SearchResultInput.toString() + "]"
								+ ";searchlen=" + keyword.length() + "["
								+ keyword + "]");
			} else {

			}
		}else{
			Collections.sort(mT9SearchAppInfos, AppInfo.mSearchComparator);
		}
		return;
	}

	private void clearAppInfoData(){
		
		if(null==mBaseAllAppInfos){
			mBaseAllAppInfos=new ArrayList<AppInfo>();
		}
		mBaseAllAppInfos.clear();

		if(null==mT9SearchAppInfos){
			mT9SearchAppInfos=new ArrayList<AppInfo>();
		}
		mT9SearchAppInfos.clear();

		if(null==mFirstNoT9SearchResultInput){
			mFirstNoT9SearchResultInput=new StringBuffer();
		}else{
			mFirstNoT9SearchResultInput.delete(0, mFirstNoT9SearchResultInput.length());
		}
		
		return;
	}
	
	private AppInfo getAppInfo(PackageManager pm, PackageInfo packageInfo){
		if((null==pm)||(null==packageInfo)){
			return null;
		}
		AppInfo appInfo=new AppInfo();
		appInfo.setIcon(packageInfo.applicationInfo.loadIcon(pm));
		appInfo.setLabel((String)packageInfo.applicationInfo.loadLabel(pm));
		appInfo.setPackageName(packageInfo.packageName);
		return appInfo;
		
	}

	private boolean isLoading(){
		return ((null!=mLoadAppInfoTask)&&(mLoadAppInfoTask.getStatus()== Status.RUNNING));
	}

	private void parseAppInfo(List<AppInfo> appInfos){
		Log.i(TAG, "parseAppInfo");

		Log.i(TAG, "before appInfos.size()"+ appInfos.size());
		mBaseAllAppInfos.clear();
		mBaseAllAppInfos.addAll(appInfos);
		Log.i(TAG, "after appInfos.size()"+ appInfos.size());

		return;
	}
	
	private String praseSortKey(String sortKey) {
		if (null == sortKey || sortKey.length() <= 0) {
			return null;
		}

		if ((sortKey.charAt(0) >= 'a' && sortKey.charAt(0) <= 'z')
				|| (sortKey.charAt(0) >= 'A' && sortKey.charAt(0) <= 'Z')) {
			return sortKey;
		}

		return String.valueOf(/*QuickAlphabeticBar.DEFAULT_INDEX_CHARACTER*/'#')
				+ sortKey;
	}
	
	private List<AppInfo> getBaseAppInfo(){
		return mBaseAllAppInfos;
	}
}
