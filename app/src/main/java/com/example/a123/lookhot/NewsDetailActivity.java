package com.example.a123.lookhot;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 123 on 2018/6/3.
 */

public class NewsDetailActivity extends AppCompatActivity {

    TextView title;
    TextView publishHouse;
    TextView publishTime;
    EditText detailContent;
    HotNews hotNews;
    //评论部分
    private EditText add_Comment;       //添加评论的View
    private ImageButton show_Comment;   //查看评论的View
    private TextView comment_count;     //显示评论数的View

    //数据源：要展示的评论
    ArrayList<CommentOfNews> comments_list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newscontent);
        Intent intent=getIntent();
        int scId=intent.getIntExtra("newsId",0);
        System.out.println("activity传过来的id(oncreate):"+scId);
        setNewsDetail(scId);            //给页面设置具体内容

        //设置评论数
        ContentResolver cr = NewsDetailActivity.this.getContentResolver();
        Uri uri = Uri.parse
                ("content://com.example.a123.lookhot.news/hotlook_table");
        Cursor c = cr.query(uri,null,null,null,null);
        c.moveToPosition(scId);//移到指定行
        int num=c.getInt(c.getColumnIndex("comment_count"));
        TextView cnum=findViewById(R.id.tw_count);
        cnum.setText(String.valueOf(num));

        //评论部分
        //获取当前点解的新闻ID
        final String news_id = ""+scId;
        //初始化数据源：获取当前news_id的评论内容
        comments_list = getComments(Integer.parseInt(news_id));

        //添加评论
        add_Comment = this.findViewById(R.id.et_comment);
        add_Comment.setFocusable(false);//让EditText失去焦点，然后获取点击事件
        add_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击编辑框，跳转到添加评论的页面
                addComment(news_id);
            }
        });

        //查询评论
        show_Comment = this.findViewById(R.id.ib_show_comment);
        show_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comments_list = getComments(Integer.parseInt(news_id));
                if(comments_list.size() != 0){
                    //展示数据到列表
                    showComment(news_id);
                }else{
                    //跳转到新页面，提示该新闻暂无评论数据
                    Intent intent = new Intent(NewsDetailActivity.this,
                            CommentNoneActivity.class);
                    //intent.putExtra("key",news_id);
                    startActivity(intent);
                }
            }
        });

        //设置评论总数
        comment_count = this.findViewById(R.id.tw_count);
        comment_count.setText(String.valueOf(comments_list.size()));
    }

    public void setNewsDetail(int scId){

        title=findViewById(R.id.detail_title);
        publishHouse=findViewById(R.id.detail_house);
        publishTime=findViewById(R.id.detail_time);
        detailContent=findViewById(R.id.detail_content);

        ContentResolver cr = NewsDetailActivity.this.getContentResolver();
        Uri uri = Uri.parse
                ("content://com.example.a123.lookhot.news/hotlook_table");
        Cursor c = cr.query(uri,null,null,null,null);
        c.moveToPosition(scId);//移到指定行
        if(!c.isAfterLast()||!c.isBeforeFirst()){//是否指向最后一条数据之后
            System.out.println("isAfterLast");
            int id=c.getInt(c.getColumnIndex("_id"));
            String news_title=c.getString(c.getColumnIndex("news_title"));
            String publish_date=c.getString(c.getColumnIndex("publish_date"));
            String publish_house=c.getString(c.getColumnIndex("publish_house"));
            String news_content=c.getString(c.getColumnIndex("news_content"));
            hotNews=new HotNews(id,news_title,publish_date,publish_house,news_content);

            title.setText(hotNews.getNews_title());
            publishHouse.setText(hotNews.getPublish_house());
            publishTime.setText(hotNews.getPublish_date());
            detailContent.setText(hotNews.getNews_content());
        }
        c.close();
    }
    /**
     * 点击按钮，查看当前新闻的回帖，以列表的方式显示在界面中
     * 使用显式Intent传的参数是将当前点击的新闻ID
     */
    private void showComment(String news_id) {

        if(null != comments_list){
            //展示数据到列表
            //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
            Intent intent = new Intent(NewsDetailActivity.this,
                    CommentListActivity.class);

            //在Intent对象当中添加一个键值对
            intent.putExtra("key",news_id);
            intent.putExtra("comments",(Serializable) comments_list);
            startActivity(intent);
        }else{
            //跳转到新页面，提示该新闻暂无评论数据
            Intent intent = new Intent(NewsDetailActivity.this,
                    CommentNoneActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 点击编辑框，跳转到添加评论的界面中
     * 使用显式Intent传的参数是将当前点击的新闻ID
     */
    private void addComment(String news_id) {
        //新建一个显式意图
        Intent intent = new Intent(NewsDetailActivity.this,
                CommentAddActivity.class);

        //在Intent对象当中添加一个键值对
        intent.putExtra("key",news_id);
        //将查到的数据先传过去，后面数据在此基础上追加
        intent.putExtra("comments",(Serializable) comments_list);

        startActivity(intent);
    }


    /**
     * 根据新闻id查找评论数据
     * @param nid
     * @return
     */
    private ArrayList<CommentOfNews> getComments(int nid){
        ArrayList<CommentOfNews> comments = new ArrayList<>();

        ContentResolver cr = NewsDetailActivity.this.getContentResolver();
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
