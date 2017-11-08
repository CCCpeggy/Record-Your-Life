package com.example.info.tableapplication;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableActivity extends AppCompatActivity {


    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    int Table_id=1;
    TableLayout layout;
    private static final int MARGIN=5,PADDING_TOPBOTTOM=50,PADDING_LEFTRIGHT=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(FloatingButton);
        OpOrCrDb();
        All_Table Table=new All_Table(SQLiteDB_Path,db,Table_id);
        //All_Table Table=new All_Table(SQLiteDB_Path,db,"測試",2,0,"2017-11-06","2017-12-30",0,new String [][]{{"國文","國文"},{"國文","國文"}},new String []{"08:00","09:00"},new String []{"09:00","10:00"});
        String[][] Class=Table.ClassInTable();
        viewTable(Table.getClassWeekCount(),Table.getDayCount(),Class,Table.getTableName());
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
    public void viewTable(String[][] subject,String Table_name) {

        viewTable(subject.length,subject[0].length,subject,Table_name);
    }
    public void viewTable(int row,int col, String[][] subject,String Table_name){
        TextView name=(TextView)findViewById(R.id.Table_name);
        name.setText(Table_name);
        //TableLayout

        layout=(TableLayout)findViewById(R.id.Ly);
        layout.setGravity(1);

        for(int i=0;i<row;i++) {
            TableRow tr=new TableRow(this);
            tr.setGravity(16);
            layout.setColumnShrinkable(i,true);
            //tr.setPadding(0,0,0,3);
            //tr.setBackgroundColor(Color.parseColor("#AAAAAA"));
            layout.addView(tr,new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT));
            for (int j=0;j<col;j++){
                Log.v("subject",i+""+j);
                LinearLayout tw_ly=new LinearLayout(this);
                tw_ly.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tw_ly.setPadding(MARGIN,0,MARGIN,0);
                tr.addView(tw_ly);
                tw_ly.addView(AddButton(subject[i][j],i*10+j,i%2==0),new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }

    }

    public Button AddButton(String text,int id,boolean color){
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_table, menu);
        return true;
    }

    private View.OnClickListener FloatingButton= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog alertDialog=new AlertDialog.Builder(TableActivity.this)
                    .setTitle("1230")
                    .setMessage("46")
                    .setPositiveButton("確定",null)
                    .show();
        }
    };

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
