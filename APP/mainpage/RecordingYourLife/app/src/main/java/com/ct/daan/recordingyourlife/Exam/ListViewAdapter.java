package com.ct.daan.recordingyourlife.Exam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import com.ct.daan.recordingyourlife.DbTable.ExamDbTable;
import com.ct.daan.recordingyourlife.R;

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private Cursor cursor;
    private ExamDbTable ExamDb;
    private ListView listView;
    private final static int UPDATEPAGE_EXAMPAGE=544;
    private ListView.OnItemClickListener List_listener;

    public ListViewAdapter(Context mContext, Cursor cursor, ExamDbTable ExamDb, ListView listView, ListView.OnItemClickListener List_listener) {
        this.mContext = mContext;
        this.cursor =cursor;
        cursor.moveToFirst();
        this.ExamDb=ExamDb;
        this.listView=listView;
        this.List_listener=List_listener;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.exam_listview_item, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToPosition(position);
                final int ExamId=cursor.getInt(0);
                new AlertDialog.Builder(mContext)
                        .setMessage(R.string.delete_content)
                        .setTitle(R.string.delete_tilte)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ExamDb.deleteExamData(ExamId);
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
                int ExamId=cursor.getInt(0);
                Intent intent=new Intent(mContext, ExamPageActivity.class);
                intent.putExtra("TYPE",UPDATEPAGE_EXAMPAGE);
                intent.putExtra("SELECTED_ID",ExamId);
                intent.putExtra("SELECTED_CLASS",cursor.getInt(1));
                intent.putExtra("SELECTED_SUBJECT",cursor.getInt(2));
                intent.putExtra("SELECTED_DATE",cursor.getString(3));
                intent.putExtra("SELECTED_NAME",cursor.getString(4));
                intent.putExtra("SELECTED_CONTENT",cursor.getString(5));
                intent.putExtra("SELECTED_SCORE",cursor.getInt(6));
                mContext.startActivity(intent);
                UpdateAdapter_Note();
            }
        });
        return v;
    }

    public void UpdateAdapter_Note(){
        try{
            cursor=ExamDb.getCursor();
            //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[]{"考試日期", "考試名稱"}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
            //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.exam_listview_item_layout, cursor, new String[]{"考試日期", "考試名稱","考試成績"}, new int[]{R.id.text1,R.id.text2,R.id.text3}, 0);
            ListViewAdapter adapter=new ListViewAdapter(mContext, cursor,ExamDb, listView, List_listener);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(List_listener);
            Log.v("UpdateAdapter_Exam", String.format("UpdateAdapter_Exam() 更新成功"));

        }catch (Exception e){
            Log.e("#004","清單更新失敗");
        }

    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView Tilte=convertView.findViewById(R.id.text1);
        TextView Content=convertView.findViewById(R.id.text2);
        TextView Score=convertView.findViewById(R.id.text3);

        int Tilte_index =cursor.getColumnIndexOrThrow("考試日期");
        int Content_index =cursor.getColumnIndexOrThrow("考試名稱");
        int Score_index =cursor.getColumnIndexOrThrow("考試成績");

        cursor.moveToPosition(position);

        Tilte.setText(cursor.getString(Tilte_index));
        Content.setText(cursor.getString(Content_index));
        int score=cursor.getInt(Score_index);

        if(score<60)
            Score.setTextColor(Color.RED);
        else
            Score.setTextColor(Color.BLACK);

        if(score==-100)
            Score.setText("");
        else
            Score.setText(score+"");

        Log.v("Exam"+position,cursor.getInt(0)+"");
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
