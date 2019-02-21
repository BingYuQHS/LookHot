package com.example.a123.lookhot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by qhs on 2018/6/4 0004.
 */

public class CommentAddActivity extends Activity {

    private EditText content;   //编辑评论内容View
    private Button add;         //提交评论按钮
    private static String commentContent = null;
    private int news_id;
    ArrayList<CommentOfNews> comments_list = null; //数据源：要展示的评论

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_add);

        //取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得news_id
        if (intent != null) {
            String value = intent.getStringExtra("key");
            //初始化数据1：接收上个activity传过来的评论数据
            comments_list =  (ArrayList<CommentOfNews>) getIntent().getSerializableExtra("comments");

            news_id = Integer.parseInt(value);
            //提交编辑的数据
            insert(news_id);
            System.out.println("新闻id："+news_id);
        }

    }

    /**
     * 向评论表中插入一条评论记录
     * @param news_id
     */
    private void insert(int news_id){
        //获取编辑的评论内容
        content = (EditText) this.findViewById(R.id.content);
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                commentContent = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //组装CommentOfNews实例对象
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());// new Date()为获取当前系统时间

        commentContent = content.getText().toString();
        final CommentOfNews commentOfNews = new CommentOfNews(0,news_id,time,commentContent);

        //提交内容
        add = this.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commentContent.length() != 0){
                    ContentResolver cr = CommentAddActivity.this.getContentResolver();
                    Uri uri = Uri.parse("content://com.example.a123.lookhot.comments/comment_table");

                    //zzy-add comment num
                    addCommentCount(commentOfNews);

                    ContentValues values = new ContentValues();
                    //由于表中字段_id设置自增，该句不用
                    //values.put("_id",11);

                    values.put("news_id",commentOfNews.getNews_id());
                    values.put("comment_date",commentOfNews.getPub_date());
                    values.put("comment_content",commentContent);

                    Uri result = cr.insert(uri,values);
                    if (ContentUris.parseId(result) > 0){
                        content.setText("");
                        Toast.makeText(CommentAddActivity.this,"评论成功！",
                                Toast.LENGTH_SHORT).show();

                        //追加数据
                        commentOfNews.setContent(commentContent);
                        commentOfNews.setId(comments_list.size()+1);
                        comments_list.add(commentOfNews);

                        //数据添加成功后，跳转到评论列表展示页面,参数是新闻id
                        showComment(String.valueOf(commentOfNews.getNews_id()));
                    }
                }else{
                    Toast.makeText(CommentAddActivity.this,"请先添加评论内容！",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //zzy-add comment num
    public void addCommentCount(CommentOfNews commentOfNews){
        //put news--commentcount---zzy
        ContentResolver cr = CommentAddActivity.this.getContentResolver();
        Uri uri1 = Uri.parse
                ("content://com.example.a123.lookhot.news/hotlook_table");
        ContentValues values1 = new ContentValues();
        int count = comments_list.size()+1;
        values1.put("comment_count",count);
        int newsid=commentOfNews.getNews_id()+1;
        cr.update(uri1,values1,"_id="+newsid,null);
    }

    /**
     * 数据添加成功后，跳转到评论列表展示页面,参数是新闻id
     * 使用显式Intent传的参数是将当前点击的新闻ID
     */
    private void showComment(String news_id) {
        Intent intent = new Intent(CommentAddActivity.this,
                CommentListActivity.class);

        //根据news_id重新查一次数据库
        ArrayList<CommentOfNews> new_comments_list = getComments(Integer.parseInt(news_id));

        //在Intent对象当中添加一个键值对
        intent.putExtra("key",news_id);
        intent.putExtra("comments",(Serializable) new_comments_list);
        startActivity(intent);
    }

    /**
     * 根据新闻id查找评论数据
     * @param nid
     * @return
     */
    private ArrayList<CommentOfNews> getComments(int nid){
        ArrayList<CommentOfNews> comments = new ArrayList<>();

        ContentResolver cr = CommentAddActivity.this.getContentResolver();
        Uri uri = Uri.parse("content://com.example.a123.lookhot.comments/comment_table");
        String condition[] = new String[1];
        condition[0] = String.valueOf(nid);
        String orderBy = "_id desc";//asc|desc

        Cursor c = cr.query(uri, null, "news_id=?",
                condition, orderBy);

        //查询结果
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            int id = Integer.parseInt(c.getString(0));          //回帖序号
            int newsId = Integer.parseInt(c.getString(1));      //新闻序号
            String pub_date=c.getString(c.getColumnIndex("comment_date"));      //回帖时间
            String content = c.getString(c.getColumnIndex("comment_content"));  //回复内容

            CommentOfNews comment = new CommentOfNews(id, newsId, pub_date, content);
            comments.add(comment);
        }
        c.close();
        return comments ;
    }
}
