package com.example.info.scheduleapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScheduleActivity extends AppCompatActivity {


    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    int Week_id;
    TableLayout layout;
    A_Day_Table Table;


    private static final int MARGIN=5,PADDING_TOPBOTTOM=50,PADDING_LEFTRIGHT=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
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
        initDateBase();
        String Class[][][]=Table.TablesClassInDay();
        String Tables[]=Table.getTableName();
        if(Class!=null)
            viewTable(Class[0][0].length,Class[0],Tables[0]);

    }

    private void addnode(String Time_String,String Name_String){
        addnode( Time_String,Name_String,255,255,255);
    }

    private void addchildnode(String Time_String,String Name_String){
        addchildnode( Time_String,Name_String,255,255,255);
    }

    private void addnode(String Time_String,String Name_String,int colorR,int colorG,int colorB){
        LinearLayout slayout;
        slayout=(LinearLayout)findViewById(R.id.SchLayout);
        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout nodeLayout=(LinearLayout) inflater.inflate(R.layout.node,null,true);
        nodeLayout.setBackgroundColor(Color.argb(255, colorR,colorG,colorB));
        TextView time = (TextView) nodeLayout.findViewById(R.id.time_tv);
        time.setText(Time_String);
        TextView name = (TextView) nodeLayout.findViewById(R.id.name_tv);
        name.setText(Name_String);
        slayout.addView(nodeLayout);
    }

    private void addchildnode(String Time_String,String Name_String,int colorR,int colorG,int colorB){
        LinearLayout slayout;
        slayout=(LinearLayout)findViewById(R.id.SchLayout);
        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout nodeLayout=(LinearLayout) inflater.inflate(R.layout.nodechild,null,true);
        nodeLayout.setBackgroundColor(Color.argb(255, colorR,colorG,colorB));
        TextView time = (TextView) nodeLayout.findViewById(R.id.time_tv);
        time.setText(Time_String);
        TextView name = (TextView) nodeLayout.findViewById(R.id.name_tv);
        name.setText(Name_String);
        slayout.addView(nodeLayout);
    }

    private void initDateBase(){
        OpOrCrDb();
        Table=new A_Day_Table(SQLiteDB_Path,db,"2017-11-22");
        //Cursor Week_cursor=Table.getWeek_cursor();
        Table.outputAllWeekIds();
        
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

    public void viewTable(int row, String[][] subject,String name){
        addnode(subject[1][0],name,230,200,80);
        for(int i=0;i<row;i++) {
            addchildnode(subject[1][i],subject[0][i],230,200,80);
        }

    }


    public Button AddButton(String text, int id, boolean color){
        Button btn=new Button(this);
        btn.setText(text);
        btn.setGravity(17);
        btn.setId(id);
        if(color)
            btn.setBackgroundColor(Color.parseColor("#CCCCCC"));
        else
            btn.setBackgroundColor(Color.parseColor("#EEEEEE"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
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
}
