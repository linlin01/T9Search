package linlin.com.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import linlin.com.tool.qclCopy.BlurBehind;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BlurBehind.getInstance()//在你需要添加模糊或者透明的背景中只需要设置这几行简单的代码就可以了
                .withAlpha(0)
                .withFilterColor(Color.parseColor("#55FF0000"))
                .setBackground(this);
    }
}
