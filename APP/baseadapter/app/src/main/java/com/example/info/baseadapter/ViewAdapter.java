package com.example.info.baseadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ViewAdapter extends BaseAdapter {


    private String[][] ElementsData ;   //資料
    private LayoutInflater inflater;    //加載layout


    //優化Listview 避免重新加載
    //這邊宣告你會動到的Item元件
    static class ViewHolder{
        LinearLayout rlBorder;
        TextView Name;
    }

    //初始化
    public ViewAdapter(String[][] data, LayoutInflater inflater){
        this.ElementsData = data;
        this.inflater = inflater;
    }

    //取得數量
    @Override
    public int getCount() {
        return ElementsData.length;
    }
    //取得Item
    @Override
    public Object getItem(int position) {
        return ElementsData[position];
    }
    //此範例沒有特別設計ID所以隨便回傳一個值
    @Override
    public long getItemId(int position) {
        return position;
    }

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
        holder.Name.setText(ElementsData[position][1]);
        return convertView;
    }
}
