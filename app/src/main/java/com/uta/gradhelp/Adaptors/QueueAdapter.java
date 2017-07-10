package com.uta.gradhelp.Adaptors;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uta.gradhelp.Application.GradHelp;
import com.uta.gradhelp.Application.QueueModel;
import com.uta.gradhelp.R;

import java.util.ArrayList;

public class QueueAdapter extends BaseAdapter {
    Context context;
    ArrayList<QueueModel> queueResponseArrayList;
    ArrayList<String> content;
    private LayoutInflater inflater;

    public QueueAdapter(Context context, ArrayList<QueueModel> advisorQueueModel) {
        this.queueResponseArrayList = advisorQueueModel;
        this.context = context;
    }

    @Override
    public int getCount() {
        return queueResponseArrayList.size();
    }

    @Override
    public QueueModel getItem(int i) {
        return queueResponseArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.advisor_queue_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.underlay = (LinearLayout) convertView.findViewById(R.id.underlay);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (getItem(position).getStud_netid().equalsIgnoreCase(GradHelp.getInstance().getLoginResponse().getNet_id())) {
            viewHolder.title.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            String s = "" + getItem(position).getStud_mavid();
            s = s.substring(6, s.length());
            viewHolder.underlay.setBackgroundColor(Color.parseColor("#88ff0000"));
            viewHolder.title.setText("******" + s);
        } else {
            viewHolder.title.setTextColor(Color.BLACK);
            String s = "" + getItem(position).getStud_mavid();
            s = s.substring(6, s.length());
            viewHolder.underlay.setBackgroundColor(Color.parseColor("#ffffff"));
            viewHolder.title.setText("******" + s);
        }


        return convertView;
    }

    static class ViewHolder {
        TextView title;
        LinearLayout underlay;
    }
}
