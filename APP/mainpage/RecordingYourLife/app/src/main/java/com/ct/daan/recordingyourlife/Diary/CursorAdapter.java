package com.ct.daan.recordingyourlife.Diary;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
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
        TextView Tilte=view.findViewById(android.R.id.text1);
        TextView Content=view.findViewById(android.R.id.text2);

        int Date_col =cursor.getColumnIndexOrThrow("日期");
        int Content_col =cursor.getColumnIndexOrThrow("日記內容");

        Tilte.setText(cr.getString(Date_col));
        Content.setText(cr.getString(Content_col));


    }

}
