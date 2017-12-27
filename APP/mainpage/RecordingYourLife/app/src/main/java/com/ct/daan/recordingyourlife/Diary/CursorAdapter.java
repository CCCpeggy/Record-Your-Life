package com.ct.daan.recordingyourlife.Diary;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.ct.daan.recordingyourlife.R;

/**
 * Created by info on 2017/12/23.
 */

public class CursorAdapter extends SimpleCursorAdapter {
    private Context mContext;
    private Context appContext;
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;


    static class ViewHolder{
        LinearLayout rlBorder;
        TextView Name;
    }

    public CursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.layout = layout;
        this.mContext=context;
        this.inflater=LayoutInflater.from(context);
        this.cr=c;
    }

    @Override
    public void bindView(View view,Context context,Cursor cursor){
        super.bindView(view, context, cursor);
        TextView Tilte=view.findViewById(R.id.tvDate);
        TextView Content=view.findViewById(R.id.tvName);

        int Date_col =cursor.getColumnIndexOrThrow("日期");
        int Content_col =cursor.getColumnIndexOrThrow("日記內容");

        view.setBackgroundResource(R.drawable.default_selector);

        Tilte.setText(cr.getString(Date_col));
        Content.setText(cr.getString(Content_col));
    }
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //當ListView被拖拉時會不斷觸發getView，為了避免重複加載必須加上這個判斷
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.style_listview, null);
            holder.Name = (TextView) convertView.findViewById(R.id.tvName);

            holder.rlBorder = (LinearLayout) convertView.findViewById(R.id.llBorder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundResource(R.drawable.default_selector);
        return convertView;
    }*/

}
