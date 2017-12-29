package com.ct.daan.recordingyourlife.Table;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.ct.daan.recordingyourlife.DbTable.All_Table;
import com.ct.daan.recordingyourlife.R;


public class TableActivity extends AppCompatActivity {

    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    int Table_id=1;
    TableLayout layout;
    Spinner name;
    All_Table Table;
    final int ADDTABLEPAGE=5189,SETTINGTABLEPAGE=5947;
    private static final int MARGIN=5,PADDING_TOPBOTTOM=50,PADDING_LEFTRIGHT=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_activity);

        name=(Spinner)findViewById(R.id.Table_name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.icon_add);
        fab.setOnClickListener(FloatingButton);

        OpOrCrDb();
        Table=new All_Table(SQLiteDB_Path,db,1);

        updateSpinner();

        Table_id=Table.getMain_id();
        setSpinnerByValue(name,Table_id,Table.getAllTableCursors(),0);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        updateSpinner();
    }


    public void updateSpinner(){
        Cursor cursor=Table.getAllTableCursors();
        Log.v("updateSpinner","updateSpinner()執行");
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"課表名稱"}, new int[]{android.R.id.text1}, 0);
        name.setAdapter(adapter);
        name.setOnItemSelectedListener(List_listener);
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

    public void setSpinnerByValue(Spinner spinner, int value, Cursor cursor, int Col){
        if(cursor==null|| cursor.getCount()<=0){
            viewTableNull();
            return;
        }
        cursor.moveToFirst();
        do{
            Log.v("比對資料", String.format("%s=%s,%s=%s,%s=%s",cursor.getColumnName(0),cursor.getInt(0),cursor.getColumnName(1),cursor.getString(1),"value",value));
            if (value==cursor.getInt(Col)) {
                spinner.setSelection(cursor.getPosition(), true);
                break;
            }
        }while(cursor.moveToNext());
        cursor.moveToFirst();
    }

    private Spinner.OnItemSelectedListener List_listener= new Spinner.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor=(Cursor)parent.getSelectedItem();
            String TableName=cursor.getString(1);
            Log.v("選取課表名",TableName);
            Table=new All_Table(SQLiteDB_Path,db);
            Table_id= Table.getTable_id(TableName);
            Table.setTable(Table_id);
            if(TableName.isEmpty()) {
                viewTableNull();
                return;
            }
            String[][] Class=Table.ClassInTable();
            viewTable(Table.getClassWeekCount()+1,Table.getDayCount()+1,Class);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            viewTableNull();
        }

    };


    public void viewTable(String[][] subject) {

        viewTable(subject.length,subject[0].length,subject);
    }

    public void viewTableNull() {
        layout = (TableLayout) findViewById(R.id.Ly);
        layout.removeAllViewsInLayout();
    }

    public void viewTable(int row,int col, String[][] subject){

        //TableLayout

        layout=(TableLayout)findViewById(R.id.Ly);
        layout.removeAllViewsInLayout();
        layout.setGravity(1);

        for(int i=0;i<row;i++) {
            TableRow tr=new TableRow(this);
            tr.setGravity(16);
            layout.setColumnShrinkable(i,true);
            //tr.setPadding(0,0,0,3);
            //tr.setBackgroundColor(Color.parseColor("#AAAAAA"));
            layout.addView(tr,new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            for (int j=0;j<col;j++){
                Log.v("subject",i+""+j);
                LinearLayout tw_ly=new LinearLayout(this);
                tw_ly.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tw_ly.setPadding(MARGIN,0,MARGIN,0);
                if(i==0)tw_ly.setPadding(MARGIN,5,MARGIN,5);
                tr.addView(tw_ly);
                tw_ly.addView(AddButton(subject[i][j],i*10+j,i==0,i%2==0),new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }

    }

    public Button AddButton(String text, int id, boolean isTiltle, boolean color){
        Button btn=new Button(this);
        btn.setText(text);
        btn.setGravity(17);
        btn.setLines(3);
        btn.setId(id);
        if(color)
            btn.setBackgroundColor(Color.parseColor("#EFEFEF"));
        else
            btn.setBackgroundColor(Color.parseColor("#E5E5E5"));
        if(isTiltle){
            btn.setBackgroundColor(Color.parseColor("#CCCCCC"));
            btn.setSingleLine();
        }
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
            Log.v("點了Table", String.format("row=%d,col=%d",row,col));
        }
    };
    private View.OnClickListener FloatingButton= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(TableActivity.this,AddTableSettingsActivity.class);
            startActivityForResult(intent,ADDTABLEPAGE);
        }
    };

    //增加動作按鈕到工具列
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_actions, menu);
        return true;
    }

    //動作按鈕回應
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                Log.v("刪除課表",Table_id+"");
                new AlertDialog.Builder(TableActivity.this)
                        .setMessage(R.string.delete_content)
                        .setTitle(R.string.delete_tilte)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Table.deleteAllTable();
                                Table=new All_Table(SQLiteDB_Path,db);

                                updateSpinner();
                                Table_id=Table.getMain_id();
                                setSpinnerByValue(name,Table_id,Table.getAllTableCursors(),0);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void setTheme(){
        SharedPreferences prefs = getSharedPreferences("RECORDINGYOURLIFE", 0);
        int theme_index = prefs.getInt("THEME_INDEX" ,0);
        int theme=0;
        switch (theme_index){
            case 1:
                theme=R.style.AppTheme_brown;
                break;
            case 2:
                theme=R.style.AppTheme_orange;
                break;
            case 3:
                theme= R.style.AppTheme_purple;
                break;
            case 4:
                theme=R.style.AppTheme_red;
                break;
            case 5:
                theme=R.style.AppTheme_white;
                break;
            case 0:
            default:
                theme=R.style.AppTheme;
                break;
        }
        setTheme(theme);
    }
}
