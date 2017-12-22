package com.ct.daan.recordingyourlife.Note;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class SimpleCheckedCursorAdapter extends BaseAdapter {

    private boolean[] isSelected;
    private Context context = null;
    private LayoutInflater inflater = null;
    private Cursor cursor = null;
    private int resource = 0;
    private String keyString[] = null;
    private String itemString = null;
    private int idValue[] = null;// id
    private int checkboxID = 0;


    public class ViewHolder {
        public CheckBox cb = null;
    }


    public SimpleCheckedCursorAdapter(Context context, Cursor cursor,
                                      int resource, String[] from, int[] to, int checkboxID) {
        this.context = context;
        this.cursor = cursor;
        this.resource = resource;
        keyString = new String[from.length];
        idValue = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, idValue, 0, to.length);
        this.checkboxID = checkboxID;
        inflater = LayoutInflater.from(context);
        init();
    }


    public void init() {
        cursor.moveToFirst();
        isSelected = new boolean[getCount()];
        Arrays.fill(isSelected, Boolean.FALSE);
    }



    public void toggle(View v, int pos){
        ViewHolder holder = (ViewHolder) v.getTag();
        holder.cb.toggle();
        isSelected[pos] = holder.cb.isChecked();
    }



    public long[] getCheckedItemIDs(){
        List<Long> CheckedIDs = new ArrayList<Long>();
        for(int i =0;i<isSelected.length;i++){
            if(isSelected[i]){
                cursor.moveToPosition(i);
                CheckedIDs.add(cursor.getLong(cursor.getColumnIndex("_id")));
            }
        }
        long[] IDs = new long[CheckedIDs.size()];
        for(int i=0;i<CheckedIDs.size();i++){
            IDs[i]=CheckedIDs.get(i);
        }

        return IDs;
    }


    @Override
    public int getCount() {
        cursor.moveToFirst();
        return cursor.getCount();
    }

    @Override
    public Object getItem(int pos) {
        return null;
    }

    @Override
    public long getItemId(int pos) {
        return cursor.getLong(cursor.getColumnIndex("_id"));
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        cursor.moveToPosition(position);
        ViewHolder holder = null;
        if (holder == null) {
            holder = new ViewHolder();
            if (view == null) {
                view = inflater.inflate(resource, null);
            }

            for(int i=0;i<idValue.length;i++){
                View v = view.findViewById(idValue[i]);
                itemString =  cursor.getString(cursor.getColumnIndex(keyString[i]));
                if(v instanceof TextView){
                    TextView tv = (TextView) v;
                    tv.setText(itemString);
                }else if(v instanceof EditText){
                    EditText et = (EditText) v;
                    et.setText(itemString);
                }else if(v instanceof Button){
                    Button btn = (Button) v;
                    btn.setText(itemString);
                }else if (v instanceof CheckBox){
                    CheckBox cb =(CheckBox) v;
                    if(itemString.contentEquals("1")||itemString.contentEquals("0")){
                        cb.setChecked(itemString.contains("1"));
                    }else{
                        cb.setText("itemString[i]");
                    }

                }

            }

            holder.cb = (CheckBox) view.findViewById(checkboxID);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.cb.setChecked(isSelected[position]);
        return view;
    }
}

