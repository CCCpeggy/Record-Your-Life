package com.example.info.note;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
//修改內容

public class NoteActivity extends AppCompatActivity {

    private SQLiteDatabase db=null;
    private final static int LISTPAGE_QAQQ=0,NEWLISTPAGE_QAQQ=1,UPDATE_TYPE=0,ADD_TYPE=1,DELETE_TYPE=2;
    private String SQLiteDB_Path="student_project.db";
    private String SQLiteTable_Name="便條15";
    private final static String CREATE_NOTE_TABLE="CREATE TABLE if not exists '便條15'(" +
            "_id INTEGER  PRIMARY KEY NOT NULL," +
            "'便條標題' TEXT NOT NULL," +
            "'便條內容' TEXT)";
    ListView listView01;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initView();

        //其餘動作
        OpOrCrDb();
        OpOrCrTb(CREATE_NOTE_TABLE);

        //AddNoteData();

        UpdateAdapter_Note(false);
    }

    private void initView() {
        //變數指定物件
        listView01=(ListView)findViewById(R.id.listv);
        btnAdd=(Button)findViewById(R.id.Add);

        //監控事件
        btnAdd.setOnClickListener(btnAddClick);
    }
    public void AddNoteData(){
        String title[]={"帶","智障","怎樣","??","最棒了","=.=","喔","幼稚園","你好","好喔","喔","模考行程","動漫名稱","想買的書","成績公布","下載","今天吃的","帶","帶"
        };
        String content[]={"帶班費","哇哈哈哈哈","那群人","HOWHOW","帶泳衣","考試","就是這樣","呵呵","安安","專題發表","嘿嘿嘿","第一次10月19~20日\r\n第二次12月19~20日\r\n第三次\r\n2月26~27日\r\n第四次\r\n不參加第五次4月9~10日","kingofthehill","BL進化論","成績公布","下載","老地方","班費200","腦袋"};
        for(int i=0;i<title.length&&i<content.length;i++){
            insertNoteData(title[i],content[i]);
        }
    }

    public void insertNoteData(String title,String content){
        ContentValues row = new ContentValues();
        row.put("便條標題", title);
        row.put("便條內容", content);
        db.insert(SQLiteTable_Name, null, row);
        Log.v("新增資料列", String.format("在%s新增一筆資料：%s=%s,%s=%s", SQLiteTable_Name, "便條標題", title, "便條內容",content));
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==RESULT_OK) {
            Bundle extra= data.getExtras();
            Integer RETURN_TYPE ;
            String Changed_title ;
            String Changed_content;

            if (!extra.isEmpty()) {
                switch (requestCode) {
                    case LISTPAGE_QAQQ:
                        int Selected_id = extra.getInt("SELECTED_ID");
                        RETURN_TYPE = extra.getInt("TYPE");
                        if(RETURN_TYPE==DELETE_TYPE){
                            Delete_Row(Selected_id);
                            UpdateAdapter_Note(false);
                            return;
                        }
                        Changed_title = extra.getString("CHANGED_TITLE");
                        Changed_content = extra.getString("CHANGED_CONTENT");

                        try {
                            ContentValues row = new ContentValues();
                            row.put("便條標題", Changed_title);
                            row.put("便條內容", Changed_content);
                            db.update(SQLiteTable_Name, row, "_id=" + Selected_id, null);
                            Log.v("更新更新列", String.format("在%s更新一筆資料：%s,%s,%s,%s", SQLiteTable_Name, "便條標題", Changed_title, "便條內容", Changed_content));
                        } catch (Exception e) {
                            Log.e("#005", "資料列更新失敗");
                        }
                        break;
                    case NEWLISTPAGE_QAQQ:
                        RETURN_TYPE = extra.getInt("TYPE");
                        if(RETURN_TYPE==DELETE_TYPE){
                            return;
                        }
                        Changed_title = extra.getString("CHANGED_TITLE");
                        Changed_content = extra.getString("CHANGED_CONTENT");

                        insertNoteData(Changed_title,Changed_content);

                        break;
                }
                UpdateAdapter_Note(false);
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

    private void Delete_Row(int Delete_id){
        try{
            db.delete(SQLiteTable_Name,"_id="+Delete_id,null);
            Log.v("刪除資料列", String.format("在%s刪除一筆資料：%s=%d", SQLiteTable_Name, "_id", Delete_id));
        }catch (Exception ex){
            Log.e("#007","刪除資料列錯誤");
        }
    }
    private Button.OnClickListener btnUpdateClick=new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

        }
    };

    private Button.OnClickListener btnAddClick=new Button.OnClickListener(){

        int n=0;
        @Override
        public void onClick(View v) {
            Intent intent  = new Intent(NoteActivity.this,NotePageActivity.class);
            intent.putExtra("TYPE",ADD_TYPE);
            startActivityForResult(intent,NEWLISTPAGE_QAQQ);
        }

    };
    Cursor cursor;
    public void UpdateAdapter_Note(boolean haschecked){
        try{
            cursor=db.rawQuery(String.format("SELECT _id,便條標題,便條內容  FROM '%s'",SQLiteTable_Name),null);
            if(cursor !=  null && cursor.getCount()>0){
                if(haschecked) {
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_view_checkbox, cursor, new String[]{"便條標題", "便條內容"}, new int[]{R.id.text1,R.id.text2}, 0);
                    listView01.setAdapter(adapter);
                    listView01.setOnItemClickListener(null);
                }
                else {
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"便條標題", "便條內容"}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
                    listView01.setAdapter(adapter);
                    listView01.setOnItemClickListener(List_listener);
                    //listView01.setOnItemLongClickListener(List_Long_Listener);
                }
                Log.v("UpdateAdapter_Note",String.format("UpdateAdapter_Note() 更新成功"));
            }
        }catch (Exception e){
            Log.e("#004","清單更新失敗");
        }

    }

    private ListView.OnItemClickListener List_listener=new ListView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            int Selected_ID=cursor.getInt(0);
            Intent intent  = new Intent(NoteActivity.this,NotePageActivity.class);
            intent.putExtra("TYPE",UPDATE_TYPE);
            intent.putExtra("SELECTED_ID",Selected_ID);
            intent.putExtra("SELECTED_TITLE",cursor.getString(1));
            intent.putExtra("SELECTED_CONTENT",cursor.getString(2));
            startActivityForResult(intent,LISTPAGE_QAQQ);
        }
    };

    private ListView.OnItemLongClickListener List_Long_Listener = new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            int Click_ID=cursor.getInt(0);
            try{
                db.delete(SQLiteTable_Name, "_id="+Click_ID,null);
                UpdateAdapter_Note(false);
                Log.v("刪除資料列",String.format("在%s刪除一筆資料：%s=%d",SQLiteTable_Name,"ID",Click_ID));
            }catch (Exception e){
                Log.e("#006","資料列刪除失敗");
            }
            return false;
        }
    };

}
