package com.example.musicchart;


import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.LinkedList;
import java.util.List;

public class List1 extends ListActivity implements Runnable, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
    private final String TAG = "List1";

    Handler handler;
    String keyword = "";
    private String updateDate = "";
    private String str1 = "";
    private String str2 = "";
    private String url= "";
    String todayStr="";
    String monStr="";
    private List retList1;
    List<MusicItem> rateList = new ArrayList<MusicItem>();
    DBManager manager = new DBManager(this);
    private List<HashMap<String, String>> listItems; //存放文字、图片信息
    private SimpleAdapter listItemAdapter; //适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();


        Intent intent = getIntent();
        keyword = intent.getStringExtra("r_key");

        this.setListAdapter(listItemAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        updateDate = sharedPreferences.getString("update_date", "");

        //获取当前系统时间
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        todayStr = sdf.format(today);

        Log.i(TAG, "onCreate:sp updateDate=" + updateDate);
        Log.i(TAG, "onCreate:sp todayStr=" + todayStr);

        //判断时间
        if (todayStr.equals(updateDate)) {
            Toast.makeText(List1.this,"今天是周一，已更新",Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "onCreate: 不需要更新");
            Toast.makeText(List1.this,"今天不是周一，不需要更新",Toast.LENGTH_SHORT).show();
        }

        Thread t = new Thread(this);
        t.start();


        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 3) {

                    listItems = (List<HashMap<String, String>>) msg.obj;
                    listItemAdapter = new SimpleAdapter(List1.this, listItems,// listItems数据源
                            R.layout.list1,// ListItem的XML 布局实现
                            new String[]{"ItemTitle", "ItemDetail"},
                            new int[]{R.id.itemTitle, R.id.itemDetail}
                    );

                    //保存更新的日期
                    Date today = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    monStr = sdf.format(getThisWeekMonday(today));

                    SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("update_date",monStr);
                    editor.apply();

                    setListAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            };
        };
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }

    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    private void initListView () {
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 30; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "song" + i);//标题文字
            map.put("ItemDetail", "time" + i); //详情描述
            listItems.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems,// listItems数据源
                R.layout.list1,// ListItem的XML 布局实现
                new String[]{"ItemTitle", "ItemDetail"},
                new int[]{R.id.itemTitle, R.id.itemDetail}
        );
    }


    public void run () {
        List<HashMap<String, String>> retList = new ArrayList<HashMap<String, String>>();
        retList1 = new LinkedList();
            Document doc = null;
            try {
                Thread.sleep(100);
                doc = Jsoup.connect("https://www.kugou.com/yy/rank/home/1-4681.html?from=rank").get();
                //Log.i(TAG, "title:" + doc.title());
                Elements uls = doc.getElementsByTag("ul");
                Element ul9 = uls.get(8);
                Elements lis = ul9.getElementsByTag("li");
                Elements as = ul9.getElementsByTag("a");
                Elements spans = ul9.getElementsByTag("span");

                for(int n = 0;n<20;n++){
                    Element a = as.get(n*4);
                    str1 = a.text();
                    //Log.i(TAG, "str1:" + str1);

                    Element span = spans.get(4+n*5);
                    str2 = span.text();
                    //Log.i(TAG, "str2:" + str2);

                    Element li = lis.get(n);
                    url = li.select("a").attr("abs:href");
                    Log.i(TAG, "url:" + url);
                    //rateList.add(new MusicItem(str1,url));

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("ItemTitle", str1);
                    map.put("ItemDetail",str2);
                    retList.add(map);
                    retList1.add(url);
                }
                //Log.i(TAG, "rateList value:" + rateList);
                //Log.i(TAG, "retList1 value:" + retList1);
               /* DBManager manager = new DBManager(this);
                manager.deleteAll();
                manager.addAll(rateList);*/


            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        Message msg = handler.obtainMessage(3);
        msg.obj = retList;
        handler.sendMessage(msg);
    }

    private String inputStream2String (InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    @Override
    public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
        //Log.i(TAG,"onItemClick:position="+position);

            //Log.i(TAG,"onItemClick:key="+retList1.get(position));
            //Log.i(TAG,"onItemClick:retList1.size()="+retList1.size());
                String url = (String)retList1.get(position);
                //Log.i(TAG,"onItemClick:url="+url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
           }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder. setTitle( "提示"). setMessage("是否将这首歌加入播放列表").setPositiveButton("是" ,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: 对话框事件处理");

                String ur = (String)retList1.get(position);
                HashMap<String,String> map = (HashMap<String, String>) getListView().getItemAtPosition(position);
                String titleStr = map.get("ItemTitle");
                rateList.add(new MusicItem(titleStr,ur));
                //manager.deleteAll();
                manager.addAll(rateList);
                //listItems.remove(position);

                Toast.makeText(List1.this,"已添加到播放列表",Toast.LENGTH_SHORT).show();
                listItemAdapter.notifyDataSetChanged();
            }
        })
                .setNegativeButton("否" , null);
        builder.create().show();
        Log.i(TAG,"onItemLongClick: size=" + listItems .size());
        return true;
    }
}

