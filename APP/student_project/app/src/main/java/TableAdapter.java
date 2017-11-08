import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.TextView;

import com.ct.daan.student_project.R;

/**
 * Created by CN on 2017/9/20.
 */

public class TableAdapter extends BaseAdapter {
    private LayoutInflater myInflater;

    public TableAdapter(Context c) {
        myInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = myInflater.inflate(R.layout.timetablelayout, null);

        TextView tv = (TextView) convertView.findViewById(R.id.subjectTxt);
        GridLayout ll = (GridLayout) convertView.findViewById(R.id.ttlayout);

        tv.setText("國文");
        ll.setBackgroundColor(Color.parseColor("#AAAAAA"));

        return convertView;
    }
}
