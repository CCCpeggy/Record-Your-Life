package com.example.user.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db=null;
    private final static String CREATE_TABLE="CREATE TABLE if not exists table02(_id INTEGER PRIMARY KEY," +
                                                                                     "num INTEGER," +
                                                                                     " data TEXT)";

    ListView listView01;
    Button btnAdd,btnDelete,btnUpdate;
    String str,itemdata;
    int n=1;
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


    //打開或新增資料庫
    private void OpOrCrDb(){
        try{
            db=openOrCreateDatabase("student_project.db",MODE_PRIVATE,null);
            Toast.makeText(MainActivity.this, "資料庫載入成功",android.widget.Toast.LENGTH_LONG).show();
        }catch (Exception ex){
            Toast.makeText(MainActivity.this, "#001：資料庫載入錯誤",android.widget.Toast.LENGTH_LONG).show();
        }
    }

    //打開或新增資料表
    private void OpOrCrTb(String cmd){
        try{
            db.execSQL(cmd);
            Toast.makeText(MainActivity.this, "資料表建立成功",android.widget.Toast.LENGTH_LONG).show();
        }catch (Exception ex){
            Toast.makeText(MainActivity.this, "#002：資料表新增錯誤",android.widget.Toast.LENGTH_LONG).show();
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
            /*try{
                db.execSQL("INSERT INTO table02 (num,data) VALUES ("+ ++n+",'項目一')");
                UpdateAdapter();
            }catch (Exception e){
                Toast.makeText(MainActivity.this, "#003：資料列新增錯誤"+"\r\nINSERT INTO table02 (num,data) VALUES (%d,'項目一')"+n+"",android.widget.Toast.LENGTH_LONG).show();
            }*/
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
                Toast.makeText(MainActivity.this, "清單更新成功",android.widget.Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "#004：清單更新失敗",android.widget.Toast.LENGTH_LONG).show();
        }

    }

}
