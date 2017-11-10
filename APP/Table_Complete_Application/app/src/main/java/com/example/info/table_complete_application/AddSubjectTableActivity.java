package com.example.info.table_complete_application;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

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
    private static final int MARGIN=5,PADDING_TOPBOTTOM=50,PADDING_LEFTRIGHT=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjectselecting);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(FloatingButton);
*/
        Complete_btn=(Button)findViewById(R.id.Complete_btn);
        Complete_btn.setOnClickListener(Complete_btn_Listener);

        OpOrCrDb();
        ClassDb=new ClassDbTable(SQLiteDB_Path,db);
        WeekDb=new WeekDbTable(SQLiteDB_Path,db);
        ClassDb.deleteAllRow();
        ClassDb.AddClassData();
        WeekDb.deleteAllRow();
        WeekDb.AddWeekData();

        getIntentData();
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

        }
    };

    public void viewTable(int row,int col){

        //TableLayout

        layout=(TableLayout)findViewById(R.id.Ly);
        layout.removeAllViewsInLayout();
        layout.setGravity(1);

        for(int i=0;i<row;i++) {
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
       /* if(color)
            btn.setBackgroundColor(Color.parseColor("#CCCCCC"));
        else
            btn.setBackgroundColor(Color.parseColor("#EEEEEE"));*/
        btn.setPadding(PADDING_LEFTRIGHT,PADDING_TOPBOTTOM,PADDING_LEFTRIGHT,PADDING_TOPBOTTOM);
        btn.setOnClickListener(Table_Class_Listener);
        return btn;
    }

    private Button.OnClickListener Table_Class_Listener=new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn=(Button)v;
            int row=btn.getId()/10;
            int col=btn.getId()%10;
            Log.v("點了Table",String.format("row=%d,col=%d",row,col));
        }
    };

    private Button.OnClickListener Complete_btn_Listener=new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            
            finish();
        }
    };

}
