package com.example.musicchart;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private DBhelper dbhelper;
    private String TBNAME;

    public DBManager(Context context) {
        dbhelper = new DBhelper(context);
        TBNAME = DBhelper.TB_NAME ;
    }


    public void add(MusicItem item){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values . put(" curname",item. getCurName());
        values . put("currate",item. getCurRate());
        db. insert(TBNAME,null, values);
        db.close();
    }

    public void addAll(List<MusicItem> list){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        for (MusicItem item : list) {
            ContentValues values = new ContentValues();
            values . put("curname", item. getCurName());
            values . put("currate",item. getCurRate());
            db.insert(TBNAME,null, values);
        }
        db.close();
    }

    public void deleteAll(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db. delete(TBNAME , null,null);
        db.close();
    }

    public List<MusicItem> listAll(){
        List<MusicItem> rateList = null;
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME,null, null, null, null, null, null);
        if(cursor!=null){
            rateList = new ArrayList<MusicItem>();
            while(cursor.moveToNext()){
                MusicItem item = new MusicItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item. setCurName(cursor.getString(cursor.getColumnIndex("CURNAME")));
                item. setCurRate(cursor.getString(cursor.getColumnIndex("CURRATE")));
                rateList.add(item);
            }
            cursor.close( );
        }
        db.close();
        return rateList;
    }

}
