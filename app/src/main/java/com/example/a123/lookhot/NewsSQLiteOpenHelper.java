package com.example.a123.lookhot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsSQLiteOpenHelper extends SQLiteOpenHelper {
    public NewsSQLiteOpenHelper(Context context, String name,
                                SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void  onCreate(SQLiteDatabase db){
        String news_Table = "CREATE TABLE hotlook_table(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"news_title varchar,"
                + "publish_date integer,"
                + "comment_count integer,"
                +"news_type varchar,"
                +"publish_house varchar,"
                +"news_content varchar)";
        String comment_Table = "CREATE TABLE comment_table(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"news_id integer,"
                + "comment_date integer,"
                +"comment_content varchar)";
        db.execSQL(comment_Table);
        db.execSQL(news_Table);
        System.out.println("创建数据库表成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
    }
}
