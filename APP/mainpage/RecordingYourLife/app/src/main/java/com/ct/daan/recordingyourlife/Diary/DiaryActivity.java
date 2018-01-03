package com.ct.daan.recordingyourlife.Diary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ct.daan.recordingyourlife.Exam.*;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.DbTable.DiaryDbTable;

public class DiaryActivity extends AppCompatActivity {

    private SQLiteDatabase db=null;
    private final static int UPDATE_DIARYPAGE=1546,ADD_DIARYPAGE=781;
    private String SQLiteDB_Path="student_project.db";
    DiaryDbTable DiaryDb;
    ListView listView01;
    FloatingActionButton btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_activity);
        initView();

        //其餘動作
        OpOrCrDb();
        DiaryDb=new DiaryDbTable(SQLiteDB_Path,db);
        DiaryDb.OpenOrCreateTb();

        UpdateAdapter_Note();
    }

    private void initView() {
        //變數指定物件
        listView01=(ListView)findViewById(R.id.listv);
        btnAdd=(FloatingActionButton)findViewById(R.id.fab);
        btnAdd.setImageResource(R.drawable.icon_add);

        //監控事件
        btnAdd.setOnClickListener(btnAddClick);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
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
            CursorAdapter adapter=new CursorAdapter(DiaryActivity.this, R.layout.diary_listview_style ,cursor, new String[]{"日期", "日記內容"}, new int[]{R.id.tvDate, R.id.tvName});
            //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.style_listview, cursor, new String[]{"日期", "日記內容"}, new int[]{R.id.tvDate, R.id.tvName}, 0);
            listView01.setAdapter(adapter);
            listView01.setOnItemClickListener(List_listener);
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

    void setTheme(){
        SharedPreferences prefs = getSharedPreferences("RECORDINGYOURLIFE", 0);
        int theme_index = prefs.getInt("THEME_INDEX" ,0);
        int theme=0;
        switch (theme_index){
            case 1:
                theme=R.style.AppTheme1;
                break;
            case 2:
                theme=R.style.AppTheme2;
                break;
            case 3:
                theme= R.style.AppTheme3;
                break;
            case 4:
                theme=R.style.AppTheme4;
                break;
            case 5:
                theme=R.style.AppTheme5;
                break;
            case 6:
                theme=R.style.AppTheme6;
                break;
            case 7:
                theme=R.style.AppTheme7;
                break;
            case 8:
                theme=R.style.AppTheme8;
                break;
            case 9:
                theme=R.style.AppTheme9;
                break;
            case 10:
                theme=R.style.AppTheme10;
                break;
            case 0:
            default:
                theme=R.style.AppTheme0;
                break;
        }
        setTheme(theme);
    }
}
