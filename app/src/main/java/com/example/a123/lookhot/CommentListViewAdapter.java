package com.example.a123.lookhot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by qhs on 2018/6/3 0003.
 */

public class CommentListViewAdapter extends BaseAdapter {

    private Context context;                     //当前界面上下文
    private ArrayList<CommentOfNews> comments_list;//数据源
    private LayoutInflater inflater;

    public CommentListViewAdapter(Context context, ArrayList<CommentOfNews> comments_list) {
        this.context = context;
        this.comments_list = comments_list;
        this.inflater = LayoutInflater.from(context);
    }

    public void onDateChange(ArrayList<CommentOfNews> comments_list) {
        this.comments_list = comments_list;
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return comments_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 要对ListView的列表显示项目进行自定义,就要重写适配器类的onView方法
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentOfNews entity = comments_list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.comment_item, null);
            holder.floor = (TextView) convertView
                    .findViewById(R.id.item3_floor);
            holder.content = (TextView) convertView
                    .findViewById(R.id.item3_info);
            holder.time = (TextView) convertView
                    .findViewById(R.id.item3_time);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.floor.setText("#"+String.valueOf(entity.getId())+"楼");
        holder.content.setText("回帖："+entity.getContent() == null? "":entity.getContent());
        holder.time.setText("回帖时间："+entity.getPub_date());
        return convertView;
    }

    class ViewHolder {
        TextView floor;
        TextView content;
        TextView time;
    }
}
