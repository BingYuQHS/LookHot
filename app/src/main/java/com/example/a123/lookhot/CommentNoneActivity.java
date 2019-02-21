package com.example.a123.lookhot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by qhs on 2018/6/5 0005.
 */
public class CommentNoneActivity extends Activity {
    private int news_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得news_id
        if (intent != null) {
            //String value = intent.getStringExtra("key");
            //news_id = Integer.parseInt(value);
            setContentView(R.layout.comment_none);
        }
    }
}
