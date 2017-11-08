package com.ct.daan.student_project;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TableLayout layout;
    public String[] WeekName={"一","二","三","四","五","六","日"};
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String[][] subject=new String[][]{
                {"國文","國文","國文","國文","國文","國文","國文"},
                {"國文","國文","國文","國文","國文","國文","國文"},
                {"國文","國文","國文","國文","國文","國文","國文"},
                {"國文","國文","國文","國文","國文","國文","國文"},
                {"國文","國文","國文","國文","國文","國文","國文"},
                {"國文","國文","國文","國文","國文","國文","國文"},
                {"國文","國文","國文","國文","國文","國文","國文"},
                {"國文","國文","國文","國文","國文","國文","國文"},
                {"國文","國文","國文","國文","國文","國文","國文"},
                {"國文","國文","國文","國文","國文","國文","國文"}};

        //viewTable(subject,"課表1");
        addTable(10,7,"課表1",7);
    }
    public void viewTable(String[][] subject,String Table_name) {
        viewTable(subject.length,subject[0].length,subject,Table_name);
    }
    public void viewTable(int row,int col, String[][] subject,String Table_name){
        TextView name=(TextView)findViewById(R.id.Table_name);
        name.setText(Table_name);
        //TableLayout
        int MARGIN=5,PADDING_TOPBOTTOM=50,PADDING_LEFTRIGHT=20;
        layout=(TableLayout)findViewById(R.id.Ly);
        layout.setGravity(1);

        for(int i=0;i<row;i++) {
            TableRow tr=new TableRow(this);
            tr.setGravity(16);
            if(i!=0) layout.setColumnStretchable(i,true);
            //tr.setPadding(0,0,0,3);
            //tr.setBackgroundColor(Color.parseColor("#AAAAAA"));
            layout.addView(tr,new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT));
            for (int j=0;j<=col;j++){
                TextView tw=new TextView(this);
                if(j==0 && i==0)tw.setText("");
                else if(j==0) tw.setText(i+"");
                else tw.setText(subject[i][j-1]);
                tw.setGravity(17);
                tw.setBackgroundColor(Color.parseColor("#BBBBBB"));
                tw.setId(row*10+col);
                tw.setPadding(PADDING_LEFTRIGHT,PADDING_TOPBOTTOM,PADDING_LEFTRIGHT,PADDING_TOPBOTTOM);
                LinearLayout tw_ly=new LinearLayout(this);
                tw_ly.setPadding(MARGIN,0,MARGIN,0);
                tw_ly.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tr.addView(tw_ly);
                tw_ly.addView(tw,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                //tw.setOnClickListener(subjectBtn_click);
                tw.setOnTouchListener(subjectBtn_touch);
            }
        }

    }

    public void addTable(int row,int col,String Table_name,int start_day) {
        String[][] new_subject=new String[row+1][col];
        for (int j = 0; j < col; j++) {
            new_subject[0][j]=WeekName[(j+start_day-1)%7];
        }
        for(int i=0;i<row;i++) {
            for (int j = 0; j < col; j++) {
                new_subject[i+1][j]="+";
            }
        }
        viewTable(new_subject,Table_name);
    }

    public TextView.OnTouchListener subjectBtn_touch=new TextView.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TextView tw=(TextView)v;
            boolean click=false;
            if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
            {
                tw.setBackgroundColor(Color.parseColor("#AAAAAA"));
                click=true;
            }
            else if(event.getAction() == MotionEvent.ACTION_UP||click){
                tw.setBackgroundColor(Color.parseColor("#BBBBBB"));
                Toast.makeText(MainActivity.this, "11", Toast.LENGTH_SHORT).show();
            }
            else {
                tw.setBackgroundColor(Color.parseColor("#BBBBBB"));
                click=false;
            }
            return true;

        }

    };


    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
