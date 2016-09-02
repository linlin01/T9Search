package linlin.com.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        findViewById(R.id.mtext).setBackground(
//                new BitmapDrawable(BlurBitmap.blur(this,
//                        BitmapFactory.decodeResource(getResources(),
//                                R.drawable.keyguard_wallpaper))));
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.mtext).setBackground(new BitmapDrawable(myShot(this)));
    }

    public Bitmap myShot(Activity activity) {
// 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if(null == bitmap)Log.i("zhao11","66666666666");
// 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();
// 获取屏幕宽和高
        if(null == display) Log.i("zhao11","33333333");
        int widths = display.getWidth();
        int heights = display.getHeight();
        Log.i("zhao11", "widths:" + widths + ",heights:" + heights);
// 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
// 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0,0, 720, 1184);
// 销毁缓存信息
        view.destroyDrawingCache();
        return bmp;
    }
}
