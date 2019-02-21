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
 * Created by 123 on 2018/5/14.
 */

public class NewsContentProvider extends ContentProvider {
    private NewsSQLiteOpenHelper dbHelper=null;
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int hotLOOK_TABLE = 1;

    static {
        MATCHER.addURI("com.example.a123.lookhot.news","hotlook_table",hotLOOK_TABLE);
    }

    public  int delete(Uri uri,String selection,String[] selectionArgs){
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        int count = 0;

        switch (MATCHER.match(uri)){
            case hotLOOK_TABLE:
                count = db.delete("hotlook_table",selection,selectionArgs);
                return count;
            default:
                throw new IllegalArgumentException("Unknown Uri:"+uri.toString());
        }
    }

    public String getType(Uri uri){
        switch (MATCHER.match(uri)){
            case hotLOOK_TABLE:
                return "vnd.android.cursor.dir/hotlook_table";
            default:
                throw  new IllegalArgumentException("Unknown Uri:"+uri.toString());
        }
    }

    public Uri insert(Uri uri, ContentValues values){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri insertUri=null;

        switch (MATCHER.match(uri)){
            case hotLOOK_TABLE:
                long rowId = db.insert("hotlook_table","",values);
                insertUri = ContentUris.withAppendedId(uri,rowId);
                return insertUri;
            default:
                throw  new IllegalArgumentException("Unknown Uri:"+uri.toString());
        }
    }

    public boolean onCreate(){
        Log.d("Test","ContentProvider-onCreate()");
        dbHelper = new NewsSQLiteOpenHelper(this.getContext(),"NewsSQLiteOpenHelper.db",null,1);
        return false;
    }

    public Cursor query(Uri uri,String[] projection,String selection,String[] selectionArgs,String sortOrder){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (MATCHER.match(uri)){
            case hotLOOK_TABLE:
                return db.query("hotlook_table",projection,selection,selectionArgs,null,null,sortOrder);
            default:
                throw  new IllegalArgumentException("Unknown Uri:"+uri.toString());
        }
    }

    public int update(Uri uri,ContentValues values,String selection,String[] selectionArgs){
        SQLiteDatabase db= dbHelper.getWritableDatabase();
        int count=0;
        switch (MATCHER.match(uri)){
            case hotLOOK_TABLE:
                count=db.update("hotlook_table",values,selection,selectionArgs);
                break;
            default:
                throw  new IllegalArgumentException("Unknown Uri:"+uri.toString());
        }
        return count;
    }
}
