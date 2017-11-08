package com.example.info.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class SQLActivity extends AppCompatActivity {

    private SQLiteDatabase db=null;
    private final static String CREATE_TABLE="CREATE TABLE if not exists table02(_id INTEGER PRIMARY KEY," +
            "num INTEGER," +
            " data TEXT)";

    ListView listView01;
    Button btnAdd,btnDelete,btnUpdate;
    String str,itemdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql);

        //變數指定物件
        listView01=(ListView)findViewById(R.id.listv);
        btnAdd=(Button)findViewById(R.id.Add);
        btnDelete=(Button)findViewById(R.id.Delete);
        btnUpdate=(Button)findViewById(R.id.update);

        //監控事件
        btnAdd.setOnClickListener(btnAddClick);
        btnDelete.setOnClickListener(btnDeleteClick);
        btnUpdate.setOnClickListener(btnUpdateClick);

        //其餘動作
        OpOrCrDb();
        OpOrCrTb(CREATE_TABLE);
        UpdateAdapter();
    }


    //打開或新增資料庫
    private void OpOrCrDb(){
        try{
            db=openOrCreateDatabase("student_project.db",MODE_PRIVATE,null);
            Toast.makeText(SQLActivity.this, "資料庫載入成功",android.widget.Toast.LENGTH_LONG).show();
        }catch (Exception ex){
            Toast.makeText(SQLActivity.this, "#001：資料庫載入錯誤",android.widget.Toast.LENGTH_LONG).show();
        }
    }

    //打開或新增資料表
    private void OpOrCrTb(String cmd){
        try{
            db.execSQL(cmd);
            Toast.makeText(SQLActivity.this, "資料表建立成功",android.widget.Toast.LENGTH_LONG).show();
        }catch (Exception ex){
            Toast.makeText(SQLActivity.this, "#002：資料表新增錯誤",android.widget.Toast.LENGTH_LONG).show();
        }
    }


    private Button.OnClickListener btnDeleteClick=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

        }
    };

    private Button.OnClickListener btnUpdateClick=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

        }
    };

    private Button.OnClickListener btnAddClick=new Button.OnClickListener(){

        int n=0;
        @Override
        public void onClick(View v) {
            ContentValues row=new ContentValues();
            row.put("num",++n);
            row.put("data","項目一");
            db.insert("table01",null,row);
        }
    };

    public void UpdateAdapter (){
        Cursor cursor;
        try{
            cursor=db.rawQuery("SELECT * FROM table02",null);
            if(cursor !=  null && cursor.getCount()>0){
                SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,cursor,new String[]{"num","data"},new int[]{android.R.id.text1,android.R.id.text2},0);
                listView01.setAdapter(adapter);
                Toast.makeText(SQLActivity.this, "清單更新成功",android.widget.Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(SQLActivity.this, "#004：清單更新失敗",android.widget.Toast.LENGTH_LONG).show();
        }

    }
}
