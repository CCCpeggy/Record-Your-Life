package com.ct.daan.recordingyourlife.Exam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ct.daan.mylibrary.SwipeLayout;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.DbTable.ExamDbTable;


public class ExamActivity extends AppCompatActivity {

    private SQLiteDatabase db=null;
    private final static int UPDATEPAGE_EXAMPAGE=544,ADD_EXAMPAGE=1284;
    private final static String SQLiteDB_Path="student_project.db";
    ExamDbTable ExamDb;
    ListView listView01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_activity);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(FloatingButton);
        fab.setImageResource(R.drawable.icon_add);

        initView();

        //其餘動作
        OpOrCrDb();
        ExamDb = new ExamDbTable(SQLiteDB_Path,db);
        ExamDb.OpenOrCreateTb();
        UpdateAdapter_Exam();
    }

    private void initView(){
        //變數指定物件
        listView01=(ListView)findViewById(R.id.listv);
    }

    private View.OnClickListener FloatingButton= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent  = new Intent(ExamActivity.this,AddExamPageActivity.class);
            startActivityForResult(intent,ADD_EXAMPAGE);
        }
    };

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        UpdateAdapter_Exam();
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

    Cursor cursor;
        public void UpdateAdapter_Exam(){
        try{
            cursor=ExamDb.getCursor();
            //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"考試日期", "考試名稱"}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
            //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.exam_listview_item_layout, cursor, new String[]{"考試日期", "考試名稱","考試成績"}, new int[]{R.id.text1,R.id.text2,R.id.text3}, 0);
            //CursorAdapter adapter=new CursorAdapter(this, R.layout.exam_listview_item_layout, cursor, new String[]{"考試日期", "考試名稱","考試成績"}, new int[]{R.id.text1,R.id.text2,R.id.text3});
            ListViewAdapter adapter=new ListViewAdapter(this, cursor,ExamDb, listView01, List_listener);
            listView01.setAdapter(adapter);
            listView01.setOnItemClickListener(List_listener);
            Log.v("UpdateAdapter_Exam", String.format("UpdateAdapter_Exam() 更新成功"));

        }catch (Exception e){
            Log.e("#004","清單更新失敗");
        }

        }
    private ListView.OnItemClickListener List_listener=new ListView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ((SwipeLayout)(listView01.getChildAt(position - listView01.getFirstVisiblePosition()))).open(true);
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
