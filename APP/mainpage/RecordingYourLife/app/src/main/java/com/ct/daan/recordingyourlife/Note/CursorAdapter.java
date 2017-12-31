package com.ct.daan.recordingyourlife.Note;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.ct.daan.recordingyourlife.DbTable.Note_Reminder_DbTable;
import com.ct.daan.recordingyourlife.DbTable.ReminderDbTable;
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
    Note_Reminder_DbTable NoteReminderDb;

    public CursorAdapter(Context context, int layout, Cursor c, Note_Reminder_DbTable NoteReminderDb) {
        super(context, layout, c, new String[]{},new int[]{});
        this.layout = layout;
        this.mContext=context;
        this.inflater=LayoutInflater.from(context);
        this.cr=c;
        this.NoteReminderDb=NoteReminderDb;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.note_listview_item_layout, parent, false);
            convertView.setBackgroundResource(R.drawable.default_selector);
            holder. Tilte = convertView.findViewById(R.id.text1);
            holder.  Content = convertView.findViewById(R.id.text2);
            holder.  Layout = convertView.findViewById(R.id.layout);
            holder.  imageView = convertView.findViewById(R.id.image1);

            int Tilte_index = cr.getColumnIndexOrThrow("便條標題");
            int Content_index = cr.getColumnIndexOrThrow("便條內容");
            cr.moveToPosition(position);

            holder.Tilte.setText(cr.getString(Tilte_index));
            String Content=cr.getString(Content_index);
            Content=Content.replace("\r\n","、");
            int length=35;
            if(Content.length()>(length+3))Content=Content.substring(0,length-1)+"（省略）";

            holder.Content.setText(Content);

            boolean b=NoteReminderDb.HasReminderByNoteID(cr.getInt(0));
            if(b) holder.imageView.setBackgroundResource(R.drawable.icon_clock);
            else holder.imageView.setBackground(null);
        }
        return convertView;
    }


    class  ViewHolder{
        TextView Tilte;
        TextView Content;
        LinearLayout Layout;
        ImageView imageView;
    }

}
