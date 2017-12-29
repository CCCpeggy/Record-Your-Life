package com.example.info.dairyactivity.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.info.dairyactivity.R;
import com.example.info.mylibrary.swipe.SimpleSwipeListener;
import com.example.info.mylibrary.swipe.SwipeLayout;
import com.example.info.mylibrary.swipe.adapters.BaseSwipeAdapter;

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private Cursor cursor;

    public ListViewAdapter(Context mContext,Cursor cursor) {
        this.mContext = mContext;
        this.cursor =cursor;
        cursor.moveToFirst();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
            }
        });
        v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click Edit", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
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
