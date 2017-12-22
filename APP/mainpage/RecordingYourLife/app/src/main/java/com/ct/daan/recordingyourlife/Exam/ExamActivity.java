package com.ct.daan.recordingyourlife.Exam;

import android.content.Intent;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_activity);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(FloatingButton);

        initView();

        //其餘動作
        OpOrCrDb();
        ExamDb = new ExamDbTable(SQLiteDB_Path,db);
        ExamDb.OpenOrCreateTb();
        //ExamDb.deleteAllRow();
        //ExamDb.AddExamData();
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
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"考試日期", "考試名稱"}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
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
            cursor.moveToPosition(position);
            int Selected_ID=cursor.getInt(0);
            Intent intent  = new Intent(ExamActivity.this,ExamPageActivity.class);
            intent.putExtra("TYPE",UPDATEPAGE_EXAMPAGE);
            intent.putExtra("SELECTED_ID",Selected_ID);
            intent.putExtra("SELECTED_CLASS",cursor.getInt(1));
            intent.putExtra("SELECTED_SUBJECT",cursor.getInt(2));
            intent.putExtra("SELECTED_DATE",cursor.getString(3));
            intent.putExtra("SELECTED_NAME",cursor.getString(4));
            intent.putExtra("SELECTED_CONTENT",cursor.getString(5));
            intent.putExtra("SELECTED_SCORE",cursor.getInt(6));
            startActivityForResult(intent,UPDATEPAGE_EXAMPAGE);
        }
    };
}
