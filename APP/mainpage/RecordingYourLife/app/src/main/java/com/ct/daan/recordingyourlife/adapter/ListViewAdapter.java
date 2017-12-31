package com.ct.daan.recordingyourlife.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ct.daan.mylibrary.SwipeLayout;
import com.ct.daan.mylibrary.adapters.BaseSwipeAdapter;
import com.ct.daan.mylibrary.util.Attributes;
import com.ct.daan.recordingyourlife.DbTable.DiaryDbTable;
import com.ct.daan.recordingyourlife.Diary.DiaryPageActivity;
import com.ct.daan.recordingyourlife.R;

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private Cursor cursor;
    private DiaryDbTable DairyDb;
    private final static int UPDATE_TYPE=0,ADD_TYPE=1,DELETE_TYPE=2;
    private SQLiteDatabase db=null;
    private String SQLiteDB_Path="student_project.db";
    private ListView listView;
    private ListView.OnItemClickListener List_listener;

    public ListViewAdapter(Context mContext, Cursor cursor, DiaryDbTable DairyDb,ListView listView, ListView.OnItemClickListener List_listener) {
        this.mContext = mContext;
        this.cursor =cursor;
        cursor.moveToFirst();
        //this.DairyDb=new DairyDbTable(SQLiteDB_Path,db);
        //DairyDb.OpenOrCreateTb();
        this.DairyDb=DairyDb;
        this.listView=listView;
        this.List_listener=List_listener;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToPosition(position);
                int DairyId=cursor.getInt(0);
                DairyDb.deleteDiaryData(DairyId);
                Toast.makeText(mContext, "click delete "+DairyId, Toast.LENGTH_SHORT).show();
                UpdateAdapter_Note();
            }
        });
        v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToPosition(position);
                int DairyId=cursor.getInt(0);

                Intent intent=new Intent(mContext, DiaryPageActivity.class);
                intent.putExtra("TYPE",UPDATE_TYPE);
                intent.putExtra("SELECTED_ID",DairyId);
                intent.putExtra("SELECTED_DATE",cursor.getString(1));
                intent.putExtra("SELECTED_CONTENT",cursor.getString(2));
                mContext.startActivity(intent);
                Toast.makeText(mContext, "click Edit", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    public void UpdateAdapter_Note(){
        try{
            Cursor Diary_cursor=DairyDb.getCursor();
            ListViewAdapter mAdapter = new ListViewAdapter(mContext,Diary_cursor,DairyDb,listView,List_listener);
            listView.setAdapter(mAdapter);
            mAdapter.setMode(Attributes.Mode.Single);
            listView.setOnItemClickListener(List_listener);
            Log.v("UpdateAdapter_Note",String.format("UpdateAdapter_Note() 更新成功"));

        }catch (Exception e){
            Log.e("#004","清單更新失敗");
        }

    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView t = (TextView)convertView.findViewById(R.id.position);
        TextView content = (TextView)convertView.findViewById(R.id.text_data);
        cursor.moveToPosition(position);
        t.setText(cursor.getString(1)+"");
        content.setText(cursor.getString(2)+"");

        Log.v("Diary"+position,cursor.getInt(0)+"");
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
