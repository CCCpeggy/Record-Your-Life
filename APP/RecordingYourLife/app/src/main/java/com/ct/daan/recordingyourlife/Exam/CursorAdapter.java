package com.ct.daan.recordingyourlife.Exam;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.MediaStore;
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

    public CursorAdapter(Context context,int layout,Cursor c,String[] from,int[] to) {
        super(context, layout, c, from, to);
        this.layout = layout;
        this.mContext=context;
        this.inflater=LayoutInflater.from(context);
        this.cr=c;
    }

    @Override
    public void bindView(View view,Context context,Cursor cursor){
        super.bindView(view, context, cursor);
        TextView Tilte=view.findViewById(R.id.text1);
        TextView Content=view.findViewById(R.id.text2);
        TextView Score=view.findViewById(R.id.text3);

        int Tilte_index =cursor.getColumnIndexOrThrow("考試日期");
        int Content_index =cursor.getColumnIndexOrThrow("考試名稱");
        int Score_index =cursor.getColumnIndexOrThrow("考試成績");

        Tilte.setText(cr.getString(Tilte_index));
        Content.setText(cr.getString(Content_index));
        int score=cr.getInt(Score_index);

        if(score<60)
            Score.setTextColor(Color.RED);
        else
            Score.setTextColor(Color.BLACK);

        if(score==-100)
            Score.setText("");
        else
            Score.setText(score+"");

    }

}
