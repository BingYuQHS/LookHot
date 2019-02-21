package com.example.a123.lookhot;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 123 on 2018/5/31.
 */

public class NewsAdaptor extends BaseAdapter {
    private List<HotNews> mList;
    private LayoutInflater mInflater = null;
    private Context context = null;
//    private int[] imgsrc={R.drawable.entertainment1,R.drawable.economy1,R.drawable.military1,
//            R.drawable.entertainment2,R.drawable.military2,R.drawable.economy2,R.drawable.entertainment3,
//            R.drawable.military3,R.drawable.military4,R.drawable.economy3};
    public NewsAdaptor(Context context, List<HotNews> list){
        mInflater = LayoutInflater.from(context);
        this.mList=list;
        this.context=context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            HotNews hot = mList.get(position);

            ViewHolder holder = null;
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.listview_hotnews, null);
                holder.img = (ImageView)convertView.findViewById(R.id.new_hotPic);
                holder.title = (TextView)convertView.findViewById(R.id.news_titleText);
                holder.comment_num = (TextView)convertView.findViewById(R.id.comment_num);
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }

            int imgshow=position%10;
            System.out.println("适配器中获得的imgshow:"+imgshow);
            switch (imgshow){
                case 0:holder.img.setImageResource(R.drawable.entertainment1);break;
                case 1:holder.img.setImageResource(R.drawable.economy1);break;
                case 2:holder.img.setImageResource(R.drawable.military1);break;
                case 3:holder.img.setImageResource(R.drawable.entertainment2);break;
                case 4:holder.img.setImageResource(R.drawable.military2);break;
                case 5:holder.img.setImageResource(R.drawable.economy2);break;
                case 6:holder.img.setImageResource(R.drawable.entertainment3);break;
                case 7:holder.img.setImageResource(R.drawable.military3);break;
                case 8:holder.img.setImageResource(R.drawable.military4);break;
                case 9:holder.img.setImageResource(R.drawable.economy3);break;
                default:holder.img.setImageResource(R.drawable.entertainment2);break;
            }
//            holder.img.setImageResource(imgsrc[imgshow]);
            holder.title.setText((String)hot.getNews_title());
            holder.comment_num.setText(String.valueOf(hot.getComment_count()));

            return convertView;
    }
    static class ViewHolder
    {
        public ImageView img;
        public TextView title;
        public TextView comment_num;
    }

}
