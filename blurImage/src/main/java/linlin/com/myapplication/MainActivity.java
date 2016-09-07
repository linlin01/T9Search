package linlin.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

import linlin.com.tool.qclCopy.BlurBehind;
import linlin.com.tool.qclCopy.OnBlurCompleteListener;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        findViewById(R.id.mtext).setBackground(
//                new BitmapDrawable(//BlurBitmap.blur(this,
//                        BitmapFactory.decodeResource(getResources(),
//                                R.drawable.keyguard_wallpaper)));
        this.getWindow().setBackgroundDrawable(new BitmapDrawable(//BlurBitmap.blur(this,
                BitmapFactory.decodeResource(getResources(),
                        R.drawable.keyguard_wallpaper))
        );
        findViewById(R.id.dummy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BlurBehind.getInstance().execute(MainActivity.this, new OnBlurCompleteListener() {
                    @Override
                    public void onBlurComplete() {
                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //findViewById(R.id.mtext).setBackground(new BitmapDrawable(myShot(this)));

//        BlurBehind.getInstance()//在你需要添加模糊或者透明的背景中只需要设置这几行简单的代码就可以了
//                .withAlpha(50)
//                .withFilterColor(Color.parseColor("#0075c0"))
//                .setBackground(this);
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
