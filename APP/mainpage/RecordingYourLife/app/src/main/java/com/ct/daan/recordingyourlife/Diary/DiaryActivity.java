package com.ct.daan.recordingyourlife.Diary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ct.daan.recordingyourlife.Event.AddEventActivity;
import com.ct.daan.recordingyourlife.Event.EventActivity;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.Table.DiaryDbTable;

public class DiaryActivity extends AppCompatActivity {

    private SQLiteDatabase db=null;
    private final static int UPDATE_DIARYPAGE=1546,ADD_DIARYPAGE=781,DELETE_TYPE=2;
    private String SQLiteDB_Path="student_project.db";
    DiaryDbTable DiaryDb;
    ListView listView01;
    FloatingActionButton btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_activity);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initView();

        //其餘動作
        OpOrCrDb();
        DiaryDb=new DiaryDbTable(SQLiteDB_Path,db);
        DiaryDb.OpenOrCreateTb();
        DiaryDb.deleteAllRow();
        DiaryDb.AddDiaryData();

        UpdateAdapter_Note();
    }

    private void initView() {
        //變數指定物件
        listView01=(ListView)findViewById(R.id.listv);
        btnAdd=(FloatingActionButton)findViewById(R.id.fab);

        //監控事件
        btnAdd.setOnClickListener(btnAddClick);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case UPDATE_DIARYPAGE:
                    Bundle extra= data.getExtras();
                    if (!extra.isEmpty()) {
                        String Changed_date;
                        String Changed_content;
                        int Selected_id = extra.getInt("SELECTED_ID");
                        Changed_date = extra.getString("CHANGED_DATE");
                        Changed_content = extra.getString("CHANGED_CONTENT");
                        DiaryDb.updateDiaryData(Selected_id, Changed_date, Changed_content);
                    }
                    break;
            }
        }
        UpdateAdapter_Note();
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


    private FloatingActionButton.OnClickListener btnAddClick=new FloatingActionButton.OnClickListener(){

        int n=0;
        @Override
        public void onClick(View v) {
            Intent intent  = new Intent(DiaryActivity.this,AddDiaryPageActivity.class);
            startActivityForResult(intent,ADD_DIARYPAGE);
        }

    };

    Cursor cursor;
    public void UpdateAdapter_Note(){
        try{
            cursor=DiaryDb.getCursor();
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"日期", "日記內容"}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
            listView01.setAdapter(adapter);
            listView01.setOnItemClickListener(List_listener);
            //listView01.setOnItemLongClickListener(List_Long_Listener);
            Log.v("UpdateAdapter_Note", String.format("UpdateAdapter_Note() 更新成功"));

        }catch (Exception e){
            Log.e("#004","清單更新失敗");
        }

    }
    private ListView.OnItemClickListener List_listener=new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            int Selected_ID=cursor.getInt(0);
            Intent intent  = new Intent(DiaryActivity.this,DiaryPageActivity.class);
            intent.putExtra("SELECTED_ID",Selected_ID);
            intent.putExtra("SELECTED_DATE",cursor.getString(1));
            intent.putExtra("SELECTED_CONTENT",cursor.getString(2));
            startActivityForResult(intent,UPDATE_DIARYPAGE);
        }
    };

    private ListView.OnItemLongClickListener List_Long_Listener = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            int Click_ID=cursor.getInt(0);
            DiaryDb.deleteDiaryData(Click_ID);
            return false;
        }
    };
}
