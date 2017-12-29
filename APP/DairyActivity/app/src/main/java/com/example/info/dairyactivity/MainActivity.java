package com.example.info.dairyactivity;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.example.info.dairyactivity.adapter.ListViewAdapter;
import com.example.info.mylibrary.swipe.SwipeLayout;
import com.example.info.mylibrary.swipe.util.Attributes;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db=null;
    private final static int LISTPAGE_QAQQ=0,NEWLISTPAGE_QAQQ=1,UPDATE_TYPE=0,ADD_TYPE=1,DELETE_TYPE=2;
    private String SQLiteDB_Path="student_project.db";
    DairyDbTable DairyDb;
    ListView listView01;
    Button btnAdd;
    private ListViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.v("UpdateAdapter_Note","1");
        initView();
        Log.v("UpdateAdapter_Note","2");
        //其餘動作
        OpOrCrDb();
        DairyDb=new DairyDbTable(SQLiteDB_Path,db);
        DairyDb.OpenOrCreateTb();
        DairyDb.deleteAllRow();
        DairyDb.AddDairyData();


        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("ListView");
        }

        UpdateAdapter_Note();
    }

    private void initView() {
        //變數指定物件
        listView01=(ListView)findViewById(R.id.listv);
        btnAdd=(Button)findViewById(R.id.Add);
        //監控事件
        btnAdd.setOnClickListener(btnAddClick);
    }
    public void AddDairyData(){
        String  date[]={"2017-11-22","2017-11-08","2017-10-18","2017-11-22","2017-12-08","2017-11-01","2017-10-10","2017-11-07","2017-10-27","2017-10-08","2017-11-27","2017-09-02","2017-11-13","2017-10-03","2017-11-19","2017-09-08","2017-10-19","2017-12-06","2017-09-10"
        };
        String content[]={"今天天氣真好","麻麻說我壞壞QAQ","安安是笨蛋","菁菁這個磨人的小妖精","呵呵","涵涵涵涵涵涵涵涵","\\專題/\\專題/\\專題/\\專題/\\專題/\\專題/","加油加油","今天的紀錄是","喔喔喔喔喔倒數200天了","今天學壞了","老師說我交到壞朋友了","12345上山打老虎","啊啊啊啊啊黑賴好帥","YOOOOO","我推薦的BL小說","是","恩呵呵呵呵呵呵呵","19篇日記完成"};
        for(int i=0;i< date.length&&i<content.length;i++){
            DairyDb.insertDairyData( date[i],content[i]);
        }
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_OK) {
            Bundle extra= data.getExtras();
            Integer RETURN_TYPE ;
            String Changed_date ;
            String Changed_content;

            if (!extra.isEmpty()) {
                switch (requestCode) {
                    case LISTPAGE_QAQQ:
                        int Selected_id = extra.getInt("SELECTED_ID");
                        RETURN_TYPE = extra.getInt("TYPE");
                        if(RETURN_TYPE==DELETE_TYPE){
                            DairyDb.deleteDairyData(Selected_id);
                            UpdateAdapter_Note();
                            return;
                        }
                        Changed_date = extra.getString("CHANGED_DATE");
                        Changed_content = extra.getString("CHANGED_CONTENT");

                        DairyDb.updateDairyData(Selected_id,Changed_date,Changed_content);

                        break;
                    case NEWLISTPAGE_QAQQ:
                        RETURN_TYPE = extra.getInt("TYPE");
                        if(RETURN_TYPE==DELETE_TYPE){
                            return;
                        }
                        Changed_date = extra.getString("CHANGED_DATE");
                        Changed_content = extra.getString("CHANGED_CONTENT");

                        DairyDb.insertDairyData(Changed_date,Changed_content);
                        break;
                }
                UpdateAdapter_Note();
            }
        }
    }
    @Override
    public void onDestroy(){
        db.close();
    }


    //打開或新增資料庫
    private void OpOrCrDb(){
        try{
            db=openOrCreateDatabase(SQLiteDB_Path,MODE_PRIVATE,null);
            Log.v("資料庫","資料庫載入成功");
        }catch (Exception ex){
            Log.e("#001","資料庫載入錯誤");
        }
    }

    //打開或新增資料表
    private void OpOrCrTb(String cmd){
        try{
            db.execSQL(cmd);
            Log.v("資料表","資料表建立或開啟成功");
        }catch (Exception ex){
            Log.e("#002","資料表建立或開啟錯誤");
        }
    }


    private Button.OnClickListener btnAddClick=new Button.OnClickListener(){

        int n=0;
        @Override
        public void onClick(View v) {
            Intent intent  = new Intent(MainActivity.this,DairyPageActivity.class);
            intent.putExtra("TYPE",ADD_TYPE);
            startActivityForResult(intent,NEWLISTPAGE_QAQQ);
        }

    };

    Cursor cursor;
    public void UpdateAdapter_Note(){
        try{
            Cursor Diary_cursor=DairyDb.getCursor();
            mAdapter = new ListViewAdapter(this,Diary_cursor);
            listView01.setAdapter(mAdapter);
            mAdapter.setMode(Attributes.Mode.Single);
            listView01.setOnItemClickListener(List_listener);
            //listView01.setOnItemLongClickListener(List_Long_Listener);
            Log.v("UpdateAdapter_Note",String.format("UpdateAdapter_Note() 更新成功"));

        }catch (Exception e){
            Log.e("#004","清單更新失敗");
        }

    }
    private ListView.OnItemClickListener List_listener=new ListView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
            cursor.moveToPosition(position);
            int Selected_ID=cursor.getInt(0);
            Intent intent  = new Intent(MainActivity.this,DairyPageActivity.class);
            intent.putExtra("TYPE",UPDATE_TYPE);
            intent.putExtra("SELECTED_ID",Selected_ID);
            intent.putExtra("SELECTED_DATE",cursor.getString(1));
            intent.putExtra("SELECTED_CONTENT",cursor.getString(2));
            startActivityForResult(intent,LISTPAGE_QAQQ);*/

            ((SwipeLayout)(listView01.getChildAt(position - listView01.getFirstVisiblePosition()))).open(true);
        }
    };

    private ListView.OnItemLongClickListener List_Long_Listener = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            int Click_ID=cursor.getInt(0);
            DairyDb.deleteDairyData(Click_ID);
            return false;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
