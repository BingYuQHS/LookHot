package com.example.a123.lookhot;
import com.example.a123.lookhot.CommentListView.IReflashListener;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qhs on 2018/6/3 0003.
 */

public class CommentListActivity extends Activity implements IReflashListener{

    CommentListViewAdapter adapter; //ListView对应的适配器
    CommentListView listview;       //自定义ListView

    ArrayList<CommentOfNews> comments_list = null;  //数据源：要展示的评论
    ArrayList<CommentOfNews> comments = null;
    ArrayList<CommentOfNews> fresh_data = null;     //数据源：用于存放刷新数据的容器
    List<ArrayList<CommentOfNews>> arrFreshData = null;
    int times,time=0;
    private int news_id;                            //接收上个Activity传过来的参数新闻id



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_main);

        Intent intent = getIntent();                           //取得从上一个Activity当中传递过来的Intent对象
        if (intent != null) {
            String value = intent.getStringExtra("key");//从Intent当中根据key取得news_id
            news_id = Integer.parseInt(value);

            //初始化数据1：接收上个activity传过来的评论数据
            comments = (ArrayList<CommentOfNews>) getIntent().getSerializableExtra("comments");
            //setData(news_id);//初始化测试数据
            //comments_list = getComments(news_id);//初始化数据2

            //展示数据到列表
            showList1(comments);

//            if(null != comments_list){
//                //展示数据到列表
//                showList1(comments_list);
//            }else{
//                //跳转到新页面，提示该新闻暂无评论数据
//                Intent intent2 = new Intent(CommentListActivity.this,
//                        CommentNoneActivity.class);
//                intent.putExtra("key",news_id);
//                startActivity(intent2);
//            }
        }
    }

    /**
     * 重写方法：数据刷新
     */
    @Override
    public void onReflash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //获取最新数据
                //setReflashData();

                //将要刷新的数据分组
                arrFreshData = getFiveNewData(comments);
                if(comments.size()>10 && times>0){
                    if (time < times){
                        fresh_data = arrFreshData.get(time);
                        setReflashData2(fresh_data);
                        times -= 1;
                        time += 1;
                        //通知界面显示
                        showList2(comments_list);
                    }
                }else{
                    showList2(comments);
                }

                //通知listview 刷新数据完毕；
                listview.reflashComplete();
            }
        }, 2000);
    }
    /**
     * 展示未刷新时的回帖列表
     * @param commentslist
     */
    private void showList1(ArrayList<CommentOfNews> commentslist) {
        //调整评论楼层，初始查询最多显示10条数据
        List<CommentOfNews> show_list = new ArrayList<CommentOfNews>();
        List<CommentOfNews> rest = new ArrayList<CommentOfNews>();
        ArrayList<CommentOfNews> rest2 = new ArrayList<>();
        int count = commentslist.size();
        if(count > 10){
            //截取ArrayList为List
            //list.subList(0, 2)。不是0、1、2三个，子集只是索引0和1的值
            show_list =  commentslist.subList(count-10,count);

            rest = commentslist.subList(0,count-10);
            for (int j=0; j<rest.size(); j++){
                rest2.add(rest.get(j));
            }
            comments_list = rest2;

            count = 10;
        }else{
            show_list = commentslist;
        }

        //放了10条数据的数据源(10楼~1楼)
        ArrayList<CommentOfNews> show_list2 = new ArrayList<>();
        for(int i=0; i< count; i++){
            show_list.get(i).setId(count-i);
            show_list2.add(show_list.get(i));
        }
        comments_list = show_list2;//初始化comments_list
        if (adapter == null) {
            listview = (CommentListView) findViewById(R.id.listview);
            listview.setInterface(this);
            adapter = new CommentListViewAdapter(this, show_list2);
            listview.setAdapter(adapter);
        } else {
            adapter.onDateChange(show_list2);
        }
    }

    /**
     * 展示刷新后的回帖列表
     * @param comments_list
     */
    private void showList2(ArrayList<CommentOfNews> comments_list) {
        if (adapter == null) {
            listview = (CommentListView) findViewById(R.id.listview);
            listview.setInterface(this);
            adapter = new CommentListViewAdapter(this, comments_list);
            listview.setAdapter(adapter);
        } else {
            adapter.onDateChange(comments_list);
        }
    }


    /**
     * 初始化评论列表
     * 根据新闻id查找评论数据
     * @param nid
     * @return
     */
    private ArrayList<CommentOfNews> getComments(int nid){
        ArrayList<CommentOfNews> comments = new ArrayList<>();

        ContentResolver cr = CommentListActivity.this.getContentResolver();
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

    //测试初始化数据
    private void setData(int news_ID) {
        comments_list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CommentOfNews entity = new CommentOfNews();
            entity.setId(1);
            entity.setPub_date("回帖时间是：2018.6.3 20:55:20");
            entity.setContent("回帖:楼主太赞！");
            comments_list.add(entity);
        }
    }

    //测试刷新数据
    private void setReflashData() {
        int count = comments_list.size();
        for (int i = count+1; i < count+3; i++) {
            CommentOfNews entity = new CommentOfNews();
            entity.setId(i);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String time = df.format(new Date());// new Date()为获取当前系统时间
            entity.setPub_date(time);
            entity.setContent("楼主太赞！");
            comments_list.add(0,entity);
        }
    }

    /**
     *将要刷新的数据分组
     * @param comments_list
     */
    private List<ArrayList<CommentOfNews>> getFiveNewData(ArrayList<CommentOfNews> comments_list) {

        List<ArrayList<CommentOfNews>> arr = new ArrayList<>();
        int count = comments_list.size();
        //存放截取的ListArray的数据
        List<CommentOfNews> show_list = new ArrayList<CommentOfNews>();

        //times = (count - 10)%2 == 0 ? (count-10)/2 : (count-10)/2+1;
        times = (count-10)/2;
        for (int i=0 ;i<times; i++) {
            show_list = comments_list.subList(count - 10 - (i + 1) * 2, count - 10 - i * 2);
            ArrayList<CommentOfNews> show_list2 = new ArrayList<>();
            show_list2.add(show_list.get(0));
            show_list2.add(show_list.get(1));
            arr.add(show_list2);
        }
        return arr;
    }

    //真实使用的刷新数据方法
    private void setReflashData2(ArrayList<CommentOfNews> fresh) {
        comments_list.add(0,fresh.get(1));
        comments_list.add(0,fresh.get(0));
    }

}
