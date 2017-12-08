package com.example.info.table_complete_application;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.info.table_complete_application.Table.ClassDbTable;
import com.example.info.table_complete_application.Table.ClassWeekDbTable;
import com.example.info.table_complete_application.Table.SubjectDbTable;
import com.example.info.table_complete_application.Table.WeekDbTable;

/**
 * Created by info on 2017/11/10.
 */

public class AddSubjectTableActivity extends AppCompatActivity {
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    int Table_id;
    TableLayout layout;
    int col,row;
    Button Complete_btn;
    WeekDbTable WeekDb;
    ClassDbTable ClassDb;
    ClassWeekDbTable ClassWeekDb;
    SubjectDbTable SubjectDb;
    String TableSuject[][];
    private static final int MARGIN=5,PADDING_TOPBOTTOM=50,PADDING_LEFTRIGHT=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjectselecting);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(FloatingButton);

        Complete_btn=(Button)findViewById(R.id.Complete_btn);
        Complete_btn.setOnClickListener(Complete_btn_Listener);

        OpOrCrDb();
        ClassDb=new ClassDbTable(SQLiteDB_Path,db);
        WeekDb=new WeekDbTable(SQLiteDB_Path,db);
        ClassWeekDb=new ClassWeekDbTable(SQLiteDB_Path,db);
        SubjectDb=new SubjectDbTable(SQLiteDB_Path,db);

        getIntentData();
        TableSuject=new String[row][];
        viewTable(row,col);


    }

    private void OpOrCrDb(){
        try{
            db=openOrCreateDatabase(SQLiteDB_Path,MODE_PRIVATE,null);
            Log.v("資料庫","資料庫載入成功");
        }catch (Exception ex){
            Log.e("#001","資料庫載入錯誤");
        }
    }

    private void getIntentData(){
        Intent intent=getIntent();
        Bundle extra=intent.getExtras();
        row= extra.getInt("ROW");
        col= extra.getInt("COL");
        Table_id= extra.getInt("TABLE_ID");
    }

    private View.OnClickListener FloatingButton= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AddSubjectDialogEvent(AddSubjectTableActivity.this);
        }
    };

    public void viewTable(int row,int col)
    {

        layout=(TableLayout)findViewById(R.id.Ly);
        layout.removeAllViewsInLayout();
        layout.setGravity(1);

        for(int i=0;i<row;i++) {
            TableSuject[i]=new String[col];
            TableRow tr=new TableRow(this);
            tr.setGravity(16);
            layout.setColumnShrinkable(i,true);
            layout.addView(tr,new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT));
            for (int j=0;j<col;j++){
                Log.v("subject",i+""+j);
                LinearLayout tw_ly=new LinearLayout(this);
                tw_ly.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tw_ly.setPadding(MARGIN,0,MARGIN,0);
                tr.addView(tw_ly);
                tw_ly.addView(AddButton("+",i*10+j,i%2==0),new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }

    }
    public Button AddButton(String text, int id, boolean color){
        Button btn=new Button(this);
        btn.setText(text);
        btn.setGravity(17);
        btn.setId(id);
        btn.setPadding(PADDING_LEFTRIGHT,PADDING_TOPBOTTOM,PADDING_LEFTRIGHT,PADDING_TOPBOTTOM);
        btn.setOnClickListener(Table_Class_Listener);
        return btn;
    }
    int tmp_row;
    int tmp_col;
    Button tmp_btn;
    private Button.OnClickListener Table_Class_Listener=new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            tmp_btn=(Button)v;
            tmp_row=tmp_btn.getId()/10;
            tmp_col=tmp_btn.getId()%10;
            Log.v("點了Table",String.format("row=%d,col=%d",tmp_row,tmp_col));
            SelectSubjectDialogEvent(AddSubjectTableActivity.this);
        }
    };

    private Button.OnClickListener Complete_btn_Listener=new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Cursor ClassWeek_cursor= ClassWeekDb.getCursor(Table_id);
            ClassWeek_cursor.moveToFirst();
            Cursor Week_cursor=WeekDb.getCursor("課表ID = "+Table_id);
            int row=0,col;
            do{
                col=0;
                Week_cursor.moveToFirst();
                do {
                    int Week_id=Week_cursor.getInt(0) , ClassWeek_id=ClassWeek_cursor.getInt(0),Subject_id=SubjectDb.getSubjectID(TableSuject[row][col++]);
                    ClassDb.insertClassData(ClassWeek_id,Week_id,Subject_id);
                }while (Week_cursor.moveToNext());
                row++;
            }while (ClassWeek_cursor.moveToNext());
            finish();
        }
    };

    private void AddSubjectDialogEvent(Context context) {
        final View item = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
        new AlertDialog.Builder(context)
                .setTitle(R.string.input_ur_name)
                .setView(item)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText)item.findViewById(R.id.edit_text);
                        EditText editText1 = (EditText)item.findViewById(R.id.edit_text2);

                        String name = editText.getText().toString();
                        String teacher = editText1.getText().toString();

                        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(teacher)){
                            Toast.makeText(getApplicationContext(), R.string.input_ur_name, Toast.LENGTH_SHORT).show();
                        } else {
                            SubjectDb.insertSubjectData(name,teacher);
                            Cursor cursor=SubjectDb.getCursor();
                            cursor.moveToLast();
                            int  NewSubject_id= cursor.getInt(0);
                            Log.v("加入了NewSubject_id",NewSubject_id+"");
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void SelectSubjectDialogEvent(Context context) {
        final View item = LayoutInflater.from(context).inflate(R.layout.listview_layout, null);
        ListView list = (ListView) item.findViewById(R.id.listview);
        UpdateAdapter(list);
        new AlertDialog.Builder(context)
                .setTitle(R.string.input_title)
                .setView(item)
                .setNegativeButton("取消", null)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!tmp_Subject_name.isEmpty()) {
                            tmp_btn.setText(tmp_Subject_name);
                            TableSuject[tmp_row][tmp_col] =tmp_Subject_name;
                        }
                    }
                })
                .show();
    }

    public void UpdateAdapter(ListView listView) {
        try {
            Cursor cursor=SubjectDb.getCursor();
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"科目名稱"}, new int[]{android.R.id.text1}, 0);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(List_listener);
            Log.v("UpdateAdapter", String.format("UpdateAdapter() 更新成功"));

        } catch (Exception e) {
            Log.e("#004", "清單更新失敗");
        }

    }
    String tmp_Subject_name;
    ListView.OnItemClickListener List_listener= new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView Text=(TextView) view.findViewById(android.R.id.text1);
            tmp_Subject_name=Text.getText().toString();
            Log.v("選擇了",tmp_Subject_name);
        }
    };



}
