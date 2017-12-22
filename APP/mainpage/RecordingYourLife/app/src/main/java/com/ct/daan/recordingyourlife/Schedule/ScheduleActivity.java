package com.ct.daan.recordingyourlife.Schedule;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.Class.Table.Class;
import com.ct.daan.recordingyourlife.Class.Schedule.ScheduleClass;
import com.ct.daan.recordingyourlife.Class.Table.Table;
import com.ct.daan.recordingyourlife.Class.Table.TablesClass;
import com.ct.daan.recordingyourlife.Class.Schedule.item;
import com.ct.daan.recordingyourlife.R;
import com.ct.daan.recordingyourlife.DbTable.A_Day_Table;
import com.ct.daan.recordingyourlife.DbTable.ScheduleDbTable;

//行程畫面

public class ScheduleActivity extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int NEWSCHEDULE = 564;
    private String mParam1;
    private String mParam2;

    CalendarFunction calFunction;
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    int Week_id;
    TableLayout layout;
    A_Day_Table TableClass;
    ScheduleDbTable ScheduleDb;
    TablesClass tablesClass;
    ScheduleClass Schedules;
    String Now_date;
    TextView date_tv;
    Button Next_btn,Pre_btn;
    FloatingActionButton btnAdd;
    Context context;
    View v;
    private static final int PADDING_TOPBOTTOM=50,PADDING_LEFTRIGHT=0;
    private static final int TABLEICON=4651,CALENDARICON=1468;

    private OnFragmentInteractionListener mListener;

    public ScheduleActivity() {
    }


    public static ScheduleActivity newInstance(String param1, String param2) {
        ScheduleActivity fragment = new ScheduleActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    TableRow.OnClickListener TableClick= new TableRow.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn=(Button) v.findViewById(R.id.bt2);
            if(btn.getText().toString().equals(""))return;
            int index=Integer.parseInt( btn.getText().toString());
            tablesClass.Tables[index].isOpen=! tablesClass.Tables[index].isOpen;
            updateView();
        }
    };

    Button.OnClickListener onClickListener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_next:
                    ChangeDate(calFunction.getTheDayAfter(Now_date));
                    break;
                case R.id.btn_previous:
                    ChangeDate(calFunction.getTheDayBefore(Now_date));
                    break;
            }
        }
    };

    private void ChangeDate(String s) {
        Now_date=s;
        date_tv.setText(s);
        Schedules=null;
        Log.v("123","1");
        Schedules= ScheduleDb.getScheduleClass(Now_date);
        TableClass.setDays(Now_date);
        tablesClass=null;
        Log.v("123","2");
        tablesClass=TableClass.getTablesClass();
        Log.v("123","3");
        updateView();
    }

    public void updateView(){
        LinearLayout slayout;
        Log.v("123","4");
        slayout=(LinearLayout)v.findViewById(R.id.SchLayout);
        Log.v("123","5");
        slayout.removeAllViewsInLayout();
        Log.v("123","6");
        viewScheduleAndTables(tablesClass,Schedules);
    }

    private void viewScheduleAndTables(TablesClass Tables,ScheduleClass Schedule){
        //i is Table's positioin ,j is Schedul's position
        int i,j;

        Log.v("123","4");
        for(i=0,j=0;i<Tables.Tables.length&&j<Schedule.items.length;){
            Table table=Tables.Tables[i];
            item Schedule_item=Schedule.items[j];

            String table_time=table.classes[0].start_time;
            String schedule_time=Schedule_item.Time;
            if( calFunction.CompareTime(table_time,schedule_time)){
                j=viewScheduleAndTable(i,table,Schedule,j);
                i++;
            }
            else{
                addnode(schedule_time,Schedule_item.Name,CALENDARICON);
                j++;
            }
        }
        for(;i<Tables.Tables.length;i++){
            Table table=Tables.Tables[i];
            viewScheduleAndTable(i,table,Schedule,j);
        }
        for(;j<Schedule.items.length;j++) {
            item Schedule_item=Schedule.items[j];
            String schedule_time=Schedule_item.Time;
            addnode(schedule_time,Schedule_item.Name,CALENDARICON);
        }

        Log.v("123","5");
    }


    private FloatingActionButton.OnClickListener btnAddClick=new FloatingActionButton.OnClickListener(){
        int n=0;
        @Override
        public void onClick(View v) {
            Intent intent  = new Intent(context,NewSchedulePageActivity.class);
            startActivityForResult(intent,NEWSCHEDULE);
        }
    };

    private int viewScheduleAndTable(int table_index, Table table, ScheduleClass Schedule, int Schedule_position){
        if(!table.isOpen){
            addnode(table.classes[0].start_time, table.Name,table_index+"",table.colorR,table.colorG,table.colorB,TABLEICON);
            return Schedule_position;
        }
        addnode(table.classes[0].start_time, table.Name,table_index+"",table.colorR,table.colorG,table.colorB,TABLEICON);
        int i;
        for(i=0;i<table.classes.length && Schedule_position<Schedule.items.length;){
            Class Oneclass=table.classes[i];
            item Schedule_item=Schedule.items[Schedule_position];
            String table_time=Oneclass.start_time;
            String schedule_time=Schedule_item.Time;
            if( calFunction.CompareTime(table_time,schedule_time)){
                addchildnode(table_time,Oneclass.Subject,table.colorR,table.colorG,table.colorB);
                i++;
            }
            else{
                addnode(schedule_time,Schedule_item.Name,CALENDARICON);
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

    private void addnode(String Time_String,String Name_String,int image){
        addnode( Time_String,Name_String,"",255,255,255,image);
    }

    private void addchildnode(String Time_String,String Name_String,Context context){
        addchildnode( Time_String,Name_String,255,255,255);
    }

    private void addnode(String Time_String,String Name_String,String Table_index,int colorR,int colorG,int colorB,int image){
        Log.v("addnode",String.format("Time_String=%s,Name_String=%s, colorR=%d,colorG=%d,colorB=%d",Time_String,Name_String,colorR,colorG,colorB));
        TableLayout slayout;
        slayout=(TableLayout)v.findViewById(R.id.SchLayout);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow nodeLayout=(TableRow) inflater.inflate(R.layout.schedule_node,null,true);
        nodeLayout.setBackgroundColor(Color.argb(255, colorR,colorG,colorB));
        TextView time = (TextView) nodeLayout.findViewById(R.id.time_tv);
        time.setText(Time_String);
        TextView name = (TextView) nodeLayout.findViewById(R.id.name_tv);
        name.setText(Name_String);
        nodeLayout.setOnClickListener(TableClick);
        Button btn2= (Button) nodeLayout.findViewById(R.id.bt2);
        btn2.setText(Table_index);
        ImageButton btn= (ImageButton) nodeLayout.findViewById(R.id.bt);
        GetFirstFruit(btn,image);
        name.setText(Name_String);
        slayout.addView(nodeLayout);
    }
    private void addnode(Spanned span,String Time_String,String Name_String,String Table_index,int image){
        addnode(Time_String, Name_String, Table_index, 255,255,255,image);
    }

    private void addnode(String Time_String,String Name_String,int colorR,int colorG,int colorB,int image){
        addnode(Time_String,Name_String,"",colorR,colorG,colorB,image);
    }



    private void addchildnode(String Time_String,String Name_String,int colorR,int colorG,int colorB){
        Log.v("addnode",String.format("Time_String=%s,Name_String=%s, colorR=%d,colorG=%d,colorB=%d",Time_String,Name_String,colorR,colorG,colorB));
        LinearLayout slayout;
        slayout=(LinearLayout)v.findViewById(R.id.SchLayout);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow nodeLayout=(TableRow) inflater.inflate(R.layout.schedule_nodechild,null,true);
        nodeLayout.setBackgroundColor(Color.argb(255, colorR,colorG,colorB));
        TextView time = (TextView) nodeLayout.findViewById(R.id.time_tv);
        time.setText(Time_String);
        TextView name = (TextView) nodeLayout.findViewById(R.id.name_tv);
        name.setText(Name_String);
        slayout.addView(nodeLayout);
    }

    private void GetFirstFruit(ImageButton btn,int i){
        String uri;
        switch (i){
            case TABLEICON:
                uri="icon_table";
                break;
            case CALENDARICON:
                uri="icon_calendar";
                break;
            default:
                return;
        }
        uri = "@drawable/" + uri; //圖片路徑和名稱

        int imageResource = getResources().getIdentifier(uri, null, context.getPackageName());

        btn.setImageResource(imageResource);

    }

    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        ChangeDate(Now_date);
    }

    private void initDateBase(){
        OpOrCrDb(getContext());

        TableClass=new A_Day_Table(SQLiteDB_Path,db,Now_date);
        //Cursor Week_cursor=Table.getWeek_cursor();
        TableClass.outputAllWeekIds();
        ScheduleDb=new ScheduleDbTable(SQLiteDB_Path,db);
        ScheduleDb.OpenOrCreateTb();

        //ScheduleDb.deleteAllRow();
        //ScheduleDb.AddScheduleData();

        btnAdd.setOnClickListener(btnAddClick);
    }

    //打開或新增資料庫
    private void OpOrCrDb(Context context){
        try{
            db=context.openOrCreateDatabase(SQLiteDB_Path,context.MODE_PRIVATE,null);
            Log.v("資料庫","資料庫載入成功");
        }catch (Exception ex){
            Log.e("#001","資料庫載入錯誤");
        }
    }


    public Button AddButton(String text, int id, boolean color){
        Button btn=new Button(getContext());
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
    //date2>date1 is true



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.schedule_activity, container, false);

        calFunction=new CalendarFunction();
        context=getContext();
        Next_btn=(Button)v.findViewById(R.id.btn_next);
        Pre_btn=(Button)v.findViewById(R.id.btn_previous);
        btnAdd=(FloatingActionButton)v.findViewById(R.id.fab);
        Next_btn.setOnClickListener(onClickListener);
        Pre_btn.setOnClickListener(onClickListener);
        Now_date= calFunction.getTodayDate();
        date_tv=(TextView)v.findViewById(R.id.date_tv);
        date_tv.setText(Now_date);
        btnAdd.setImageResource(R.drawable.icon_add);


        Html.ImageGetter imgGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable = null;
                drawable = ScheduleActivity.this.getResources().getDrawable(Integer.parseInt(source));
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight());
                return drawable;
            }
        };

        initDateBase();

        ChangeDate(Now_date);
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    Button.OnClickListener AddSchedule_Listener=new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            String Name="";
            String Date="";
            String Time="";
            ScheduleDb.insertScheduleData(Name,Date,Time);
        }
    };
}
