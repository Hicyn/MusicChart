package com.example.musicchart;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private final String TAG = "Main";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView)findViewById(R.id.mylist);
        String data[] = {"美国BillBoard榜","韩国Melon音乐榜","酷狗飙升榜","日本公信榜"};


        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position ==0){
            Intent intent = new Intent(this,List1.class);
            startActivity(intent);

           /* //测试数据库
            MusicItem item1 = new MusicItem("aaa","123");
            DBManager manager = new DBManager(this);
            manager.add(item1);
            manager.add(new MusicItem("bbbb","23"));
            Log.i(TAG,"onOptionsItemSelected:写入数据完毕");

            //查询所有数据
            List<MusicItem> testList = manager.listAll();
            for(MusicItem i : testList){
                Log.i(TAG,"onOptionsItemSelected:取出数据[id="+ i.getId()+"]Name="+i.getCurName()+"Rate="+i.getCurRate());
            }*/

        }else if(position == 1){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.kugou.com/yy/rank/home/1-38623.html?from=rank"));
            startActivity(intent);
        }else if(position == 2){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.kugou.com/yy/rank/home/1-6666.html?from=rank"));
            startActivity(intent);
        }else if(position == 3) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.kugou.com/yy/rank/home/1-4673.html?from=rank"));
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.playlist){
            Intent list = new Intent(this, PlayList.class);
            startActivity(list);
        }
        return super.onOptionsItemSelected(item);
    }
}
