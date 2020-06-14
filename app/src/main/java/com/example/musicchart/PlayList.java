package com.example.musicchart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlayList extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private String TAG = "PlayList";
    List<String> data = new ArrayList<String>();
    ArrayAdapter adapter;
    List relist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        ListView listView = (ListView)findViewById(R.id.mylist);
        DBManager manager = new DBManager(this);
        relist = new LinkedList();
        for(MusicItem item : manager.listAll()){
            data.add(item.getCurName());
//            Log.i(TAG, "item.getCurUrl():" + item.getCurUrl());
            relist.add(item.getCurRate());
        }
        Log.i(TAG, "relist value:" + relist);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.nodata));


        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String url = (String)relist.get(position);
        Log.i(TAG,"onItemClick:url="+url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder. setTitle( "提示"). setMessage("请确认是否从播放列表中删除当前歌曲").setPositiveButton("是" ,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: 对话框事件处理");
                data.remove(position);
                adapter.notifyDataSetChanged();
            }
        })
                .setNegativeButton("否" , null);
        builder.create().show();
        Log.i(TAG,"onItemLongClick: size=" + data .size());

        return true;
    }

    public void empty(View btn) {
        DBManager manager = new DBManager(this);
        manager.deleteAll();
        Intent list = new Intent(this, PlayList.class);
        startActivity(list);
        Toast.makeText(PlayList.this,"已清空播放列表",Toast.LENGTH_SHORT).show();
    }
}
