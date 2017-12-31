package com.ct.daan.recordingyourlife.Event;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ct.daan.mylibrary.SwipeLayout;
import com.ct.daan.mylibrary.adapters.BaseSwipeAdapter;
import com.ct.daan.recordingyourlife.Class.CalendarFunction;
import com.ct.daan.recordingyourlife.DbTable.EventDbTable;
import com.ct.daan.recordingyourlife.Note.NotePageActivity;
import com.ct.daan.recordingyourlife.R;

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private Cursor cursor;
    private EventDbTable EventDb;
    private ListView listView;
    private ListView.OnItemClickListener List_listener;
    String date;

    public ListViewAdapter(Context mContext, Cursor cursor, EventDbTable EventDb, ListView listView, ListView.OnItemClickListener List_listener, String date) {
        this.mContext = mContext;
        this.cursor =cursor;
        cursor.moveToFirst();
        this.EventDb=EventDb;
        this.listView=listView;
        this.List_listener=List_listener;
        this.date=date;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.event_listview_item, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToPosition(position);
                final int EventId=cursor.getInt(0);
                new AlertDialog.Builder(mContext)
                        .setMessage(R.string.delete_content)
                        .setTitle(R.string.delete_tilte)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventDb.deleteEventData(EventId);
                                UpdateAdapter_Note();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

            }
        });
        v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToPosition(position);
                int EventId=cursor.getInt(0);
                Intent intent=new Intent(mContext, ChangEventActivity.class);
                intent.putExtra("SELECTED_ID",EventId);
                mContext.startActivity(intent);
                UpdateAdapter_Note();
            }
        });
        return v;
    }

    public void UpdateAdapter_Note(){
        try{
            CalendarFunction calendarFunction=new CalendarFunction();
            Calendar calendar=calendarFunction.DateTextToCalendarType(date);
            Cursor Event_Cursor=EventDb.getCursorByDay(date);
            ListViewAdapter adapter=new ListViewAdapter(mContext,Event_Cursor,EventDb,listView,List_listener,date);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(List_listener);

        }catch (Exception e){
            Log.e("#004","清單更新失敗");
        }

    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView t = (TextView)convertView.findViewById(R.id.position);
        cursor.moveToPosition(position);
        t.setText(cursor.getString(1)+"");

        Log.v("Event"+position,cursor.getInt(0)+"");
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
