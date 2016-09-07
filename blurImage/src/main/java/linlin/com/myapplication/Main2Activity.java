package linlin.com.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import linlin.com.tool.qclCopy.BlurBehind;

public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BlurBehind.getInstance()//在你需要添加模糊或者透明的背景中只需要设置这几行简单的代码就可以了
                .withAlpha(50)
                .withFilterColor(Color.parseColor("#EE000000"))
                .setBackground(this);
    }
}
