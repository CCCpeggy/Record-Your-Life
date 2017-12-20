package com.example.info.examactivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.info.examactivity.Table.ExamDbTable;


public class ExamActivity extends AppCompatActivity {

    private  SQLiteDatabase db=null;
    private final static int UPDATEPAGE_EXAMPAGE=0,ADD_EXAMPAGE=1,RESULT_DELETE=100;
    private final static String SQLiteDB_Path="student_project.db";
    ExamDbTable ExamDb;
    ListView listView01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(FloatingButton);

        initView();

        //其餘動作
        OpOrCrDb();
        ExamDb = new ExamDbTable(SQLiteDB_Path,db);
        ExamDb.OpenOrCreateTb();
        ExamDb.deleteAllRow();
        ExamDb.AddExamData();
        UpdateAdapter_Exam();
    }

    private void initView(){
        //變數指定物件
        listView01=(ListView)findViewById(R.id.listv);
    }

    private View.OnClickListener FloatingButton= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Intent intent  = new Intent(ExamActivity.this,ExamPageActivity.class);
            intent.putExtra("TYPE",ADD_EXAMPAGE);
            startActivityForResult(intent,ADD_EXAMPAGE);
        }
    };

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_DELETE){
            Bundle extra= data.getExtras();
            if (extra.isEmpty()) return;
            int Selected_id = extra.getInt("SELECTED_ID");
            ExamDb.deleteExamData(Selected_id);
            UpdateAdapter_Exam();
            return;
        }
        if(resultCode==RESULT_OK) {
            Bundle extra= data.getExtras();
            int Changed_class;
            int Changed_subject;
            String Changed_date;
            String Changed_name;
            String Changed_content;
            int Changed_score;

            if (!extra.isEmpty()) {
                switch (requestCode) {
                    case UPDATEPAGE_EXAMPAGE:
                        int Selected_id = extra.getInt("SELECTED_ID");
                        Changed_class= extra.getInt("CHANGED_CLASS");
                        Changed_subject= extra.getInt("CHANGED_SUBJECT");
                        Changed_date = extra.getString("CHANGED_DATE");
                        Changed_name= extra.getString("CHANGED_NAME");
                        Changed_content = extra.getString("CHANGED_CONTENT");
                        Changed_score = extra.getInt("CHANGED_SCORE");
                        ExamDb.updateExamData(Selected_id,Changed_class,Changed_subject,Changed_date,Changed_name,Changed_content,Changed_score);
                        break;
                    case ADD_EXAMPAGE:
                        Changed_class= extra.getInt("CHANGED_CLASS");
                        Changed_subject= extra.getInt("CHANGED_SUBJECT");
                        Changed_date = extra.getString("CHANGED_DATE");
                        Changed_name= extra.getString("CHANGED_NAME");
                        Changed_content = extra.getString("CHANGED_CONTENT");
                        Changed_score = extra.getInt("CHANGED_SCORE");
                        ExamDb.insertExamData(Changed_class,Changed_subject,Changed_date,Changed_name,Changed_content,Changed_score);
                        break;
                }
                UpdateAdapter_Exam();
            }
        }
    }

    public void onDestroy(){
        db.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exam, menu);
        return true;
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

    Cursor cursor;
        public void UpdateAdapter_Exam(){
        try{
            cursor=ExamDb.getCursor();
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"考試日期", "考試名稱"}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
            listView01.setAdapter(adapter);
            listView01.setOnItemClickListener(List_listener);
            Log.v("UpdateAdapter_Exam",String.format("UpdateAdapter_Exam() 更新成功"));

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
    /*private ListView.OnItemLongClickListener List_Long_Listener = new ListView.OnItemLongClickListener() {
    /*private ListView.OnItemLongClickListener List_Long_Listener = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            int Click_ID=cursor.getInt(0);
            ExamDb.deleteExamData(Click_ID);
            return false;
        }
    };*/

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
