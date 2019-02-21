package com.example.a123.lookhot;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by qhs on 2018/6/3 0003.
 */

public class CommentContentProvider extends ContentProvider {
    private NewsSQLiteOpenHelper dbHelper=null;

    private static final UriMatcher MATCHER1 = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int COMMENT_TABLE = 1;

    //uri初始化
    static {
        MATCHER1.addURI("com.example.a123.lookhot.comments","comment_table",COMMENT_TABLE);
    }

    //定位哪张表
    public String getType(Uri uri){
        switch (MATCHER1.match(uri)){
            case COMMENT_TABLE:
                return "vnd.android.cursor.dir/comment_table";
            default:
                throw new IllegalArgumentException("UnKnown Uri: "+ uri.toString());
        }

    }

    //创建表
    public boolean onCreate(){
        Log.d("Test", "CommentContentProvider-onCreate()");
        dbHelper = new NewsSQLiteOpenHelper(this.getContext(),
                "NewsSQLiteOpenHelper.db",null,1);
        return false;
    }

    //向表中插入数据
    public Uri insert(Uri uri, ContentValues values){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri insertUri;

        switch (MATCHER1.match(uri)){
            case COMMENT_TABLE:
                long rowId = db.insert("comment_table","", values);
                insertUri = ContentUris.withAppendedId(uri,rowId);
                break;
            default:
                throw new IllegalArgumentException("UnKnown Uri: "+ uri.toString());
        }
        return insertUri;
    }

    /**
     * 条件查询
     * @param uri
     * @param condition
     * @param orderBy
     * @param startIndex
     * @param limit
     * @return
     */
    public Cursor conditionQuery(Uri uri, String condition[],String orderBy, Integer startIndex, Integer limit){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor;
        String limitString = null;
        if (startIndex != null && limit != null) {
            limitString = startIndex + " , " + limit;
        }

        switch (MATCHER1.match(uri)){
            case COMMENT_TABLE:
                cursor =  db.query("comment_table", null,
                        "news_id=?", condition, null, null,
                        orderBy, limitString);
                break;
            default:
                throw new IllegalArgumentException("UnKnown Uri: "+ uri.toString());
        }
        return cursor;
    }

    /**
     * 查询所有评论
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                            String[] selectionArgs, String sortOrder){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor;

            switch (MATCHER1.match(uri)){
                case COMMENT_TABLE:
                    cursor =  db.query("comment_table", projection, selection,
                            selectionArgs,null,null,sortOrder);

                    break;
                default:
                    throw new IllegalArgumentException("UnKnown Uri: "+ uri.toString());
            }
            return cursor;
        }

    /**
     * 条件查询（rawQuery）
     * @param sql
     * @param selectionArgs
     * @return
     */
    public Cursor rawQuery(Uri uri,String sql, String[] selectionArgs){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        Cursor cursor;
        switch (MATCHER1.match(uri)){
            case COMMENT_TABLE:
                //query(TABLE_NAME_PERSON,null,VALUE_ID+"=?",new String[]{"1"},null,null,null);
                sql = "select * from comment_table where new_id  = "+Integer.parseInt(selectionArgs[0]);
                cursor = db.rawQuery(sql,null);
                break;
            default:
                throw new IllegalArgumentException("UnKnown Uri: "+ uri.toString());
        }
        return cursor;
    }

    //更新
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        switch (MATCHER1.match(uri)){
            case COMMENT_TABLE:
                count = db.update("comment_table",values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("UnKnown Uri: "+ uri.toString());
        }
        return count;
    }

    //删除
    public int delete(Uri uri, String selection, String[] selectionArgs){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;

        switch (MATCHER1.match(uri)){
            case COMMENT_TABLE:
                count = db.delete("comment_table", selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("UnKnown Uri: "+ uri.toString());
        }
        return count;
    }
}
/**
 * 问题：
 * SQLiteDatabase和SQLiteOpenHelper
 * SQLiteDatabase支持在sdcard上创建数据库，底层操作。但是
 * 1. 其不支持创建数据库
 * 2. 其不支持版本更新 或者说其不知道如何做 因为具体数据的差异
 * SQLiteOpenHelper实现定制自己的操作方法，但是是默认写入内存中，如果想要获取这部分数据导出，可能需要root手机。
 */
