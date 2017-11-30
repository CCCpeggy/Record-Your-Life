package com.example.info.scheduleapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
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
import android.widget.Toast;

import java.sql.Struct;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {


    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    int Week_id;
    TableLayout layout;
    A_Day_Table TableClass;
    ScheduleDbTable ScheduleDb;
    TablesClass tablesClass;
    ScheduleClass Schedules;

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

        Schedules=ScheduleDb.getScheduleClass("2017-11-22");
        for(item array: Schedules.items){
            Log.v("Schedule","[Name]"+array.Name+"[Time]"+array.Time);
        }

        tablesClass=TableClass.getTablesClass();
        updateView();
    }

    LinearLayout.OnClickListener TableClick= new LinearLayout.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn=(Button) v.findViewById(R.id.bt2);
            if(btn.getText().toString().equals(""))return;
            int index=Integer.parseInt( btn.getText().toString());
            tablesClass.Tables[index].isOpen=! tablesClass.Tables[index].isOpen;
            updateView();
        }
    };

    public void updateView(){
        LinearLayout slayout;
        slayout=(LinearLayout)findViewById(R.id.SchLayout);
        slayout.removeAllViewsInLayout();
        viewScheduleAndTables(tablesClass,Schedules);
    }
    public void viewTable(int row, Table table) {
        addnode(table.classes[0].start_time, table.Name,table.colorR,table.colorG,table.colorB);
        for (int i = 0; i < row; i++) {
            addchildnode(table.classes[i].start_time, table.classes[i].Subject,table.colorR,table.colorG,table.colorB);
        }
    }

    private void viewScheduleAndTables(TablesClass Tables,ScheduleClass Schedule){
        //i is Table's positioin ,j is Schedul's position
        int i,j;
        for(i=0,j=0;i<Tables.Tables.length&&j<Schedule.items.length;){
            Table table=Tables.Tables[i];
            item Schedule_item=Schedule.items[j];

            String table_time=table.classes[0].start_time;
            String schedule_time=Schedule_item.Time;
            if( compareTime(table_time,schedule_time)){
                j=viewScheduleAndTable(i,table,Schedule,j);
                i++;
            }
            else{
                addnode(schedule_time,Schedule_item.Name);
                j++;
            }
        }
        for(;i<Tables.Tables.length;i++){
            viewTable(Tables.Tables[i].classes.length, Tables.Tables[i]);
        }
        for(;j<Schedule.items.length;j++) {
            item Schedule_item=Schedule.items[j];
            String schedule_time=Schedule_item.Time;
            addnode(schedule_time,Schedule_item.Name);
        }
    }

    private int viewScheduleAndTable(int table_index,Table table,ScheduleClass Schedule,int Schedule_position){
        addnode(table.classes[0].start_time, table.Name,table_index+"",table.colorR,table.colorG,table.colorB);
        if(!table.isOpen)return Schedule_position;
        int i;
        for(i=0;i<table.classes.length && Schedule_position<Schedule.items.length;){
            Class Oneclass=table.classes[i];
            item Schedule_item=Schedule.items[Schedule_position];
            String table_time=Oneclass.start_time;
            String schedule_time=Schedule_item.Time;
            if( compareTime(table_time,schedule_time)){
                addchildnode(table_time,Oneclass.Subject,table.colorR,table.colorG,table.colorB);
                i++;
            }
            else{
                addnode(schedule_time,Schedule_item.Name,table.colorR,table.colorG,table.colorB);
                Schedule_position++;
            }
        }
        for(;i<table.classes.length ;i++){
            Class Oneclass=table.classes[i];
            String table_time=Oneclass.start_time;
            addchildnode(table_time,Oneclass.Subject,table.colorR,table.colorG,table.colorB);
        }
        return Schedule_position;
    }

    private void addnode(String Time_String,String Name_String){
        addnode( Time_String,Name_String,"",255,255,255);
    }

    private void addchildnode(String Time_String,String Name_String){
        addchildnode( Time_String,Name_String,255,255,255);
    }

    private void addnode(String Time_String,String Name_String,String Table_index,int colorR,int colorG,int colorB){
        Log.v("addnode",String.format("Time_String=%s,Name_String=%s, colorR=%d,colorG=%d,colorB=%d",Time_String,Name_String,colorR,colorG,colorB));
        LinearLayout slayout;
        slayout=(LinearLayout)findViewById(R.id.SchLayout);
        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout nodeLayout=(LinearLayout) inflater.inflate(R.layout.node,null,true);
        nodeLayout.setBackgroundColor(Color.argb(255, colorR,colorG,colorB));
        TextView time = (TextView) nodeLayout.findViewById(R.id.time_tv);
        time.setText(Time_String);
        TextView name = (TextView) nodeLayout.findViewById(R.id.name_tv);
        name.setText(Name_String);
        nodeLayout.setOnClickListener(TableClick);
        /*Button btn= (Button) nodeLayout.findViewById(R.id.bt);
        btn.setOnClickListener(TableClick);*/
        Button btn2= (Button) nodeLayout.findViewById(R.id.bt2);
        btn2.setText(Table_index);
        name.setText(Name_String);
        slayout.addView(nodeLayout);
    }

    private void addnode(String Time_String,String Name_String,int colorR,int colorG,int colorB){
        addnode( Time_String,Name_String,"",colorR,colorG,colorB);
    }


    private void addchildnode(String Time_String,String Name_String,int colorR,int colorG,int colorB){
        Log.v("addnode",String.format("Time_String=%s,Name_String=%s, colorR=%d,colorG=%d,colorB=%d",Time_String,Name_String,colorR,colorG,colorB));
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
        TableClass=new A_Day_Table(SQLiteDB_Path,db,"2017-11-22");
        //Cursor Week_cursor=Table.getWeek_cursor();
        TableClass.outputAllWeekIds();

        ScheduleDb=new ScheduleDbTable(SQLiteDB_Path,db);
        ScheduleDb.OpenOrCreateTb();
        ScheduleDb.deleteAllRow();
        ScheduleDb.AddScheduleData();
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

    //date2>date1 is true
    private boolean compareTime(String time1,String time2){
        Calendar cal=StringtoCalendarByTime(time1);
        Calendar cal2=StringtoCalendarByTime(time2);
        if (cal.equals("")||cal2.equals(""))return false;
        Log.v("傳入日期",String.format("HOUR=%d/%d,MINUTE=%d/%d",cal.get(Calendar.HOUR_OF_DAY),cal2.get(Calendar.HOUR_OF_DAY)
                ,cal.get(Calendar.MINUTE),cal2.get(Calendar.MINUTE)));
        if(cal.get(Calendar.HOUR_OF_DAY)>cal2.get(Calendar.HOUR_OF_DAY))return false;
        if(cal.get(Calendar.HOUR_OF_DAY)<cal2.get(Calendar.HOUR_OF_DAY))return true;
        return !(cal.get(Calendar.MINUTE)>cal2.get(Calendar.MINUTE));
    }

    private Calendar StringtoCalendarByTime(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.TAIWAN);
        Calendar Calendar= android.icu.util.Calendar.getInstance();
        try{
            Calendar.setTime(sdf.parse(time));
        }catch (Exception e){
            Log.v("時間格式不符合",time);
        }
        return Calendar;
    }

}
