package com.sqdr.logreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LogAdapter extends BaseAdapter {

    private  ArrayList<String> mLogs;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public LogAdapter(Context context, SharedPreferences sharedPreferences,  ArrayList<String> text) {
        mContext = context;
        mLogs = text;
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public int getCount() {
        return this.mLogs.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mLogs.get(position);
    }

    @Override
    public boolean isEnabled(int position) {
        return position < getCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        public TextView level;
        public TextView timestamp;
        public TextView location;
        public TextView title;
        public TextView message;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ViewHolder holder;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.log_layout, null);
            holder = new ViewHolder();
            holder.level = (TextView) v.findViewById(R.id.level);
            holder.timestamp = (TextView) v.findViewById(R.id.timestamp);
            holder.location = (TextView) v.findViewById(R.id.location);
            holder.title = (TextView) v.findViewById(R.id.title);
            holder.message = (TextView) v.findViewById(R.id.message);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        // check preferences
        boolean show_title = mSharedPreferences.getBoolean("pref_show_title", true);
        boolean show_timestamp = mSharedPreferences.getBoolean("pref_show_timestamp", true);
        boolean show_flight_log = mSharedPreferences.getBoolean("pref_show_flight_log", true);
        boolean show_location = mSharedPreferences.getBoolean("pref_show_type", true);

        if(!show_title) holder.title.setVisibility(View.GONE);
        if(!show_timestamp) holder.timestamp.setVisibility(View.GONE);
        if(!show_location) holder.location.setVisibility(View.GONE);

        String line = (String) getItem(position);
        String[] elements = line.split(";");

        try{

            if(!show_flight_log && elements[0].equals("F")) {
                holder.level.setVisibility(View.GONE);
                holder.timestamp.setVisibility(View.GONE);
                holder.title.setVisibility(View.GONE);
                holder.location.setVisibility(View.GONE);
                holder.message.setVisibility(View.GONE);
                return v;
            }

            holder.level.setText(elements[0]);
            holder.timestamp.setText(elements[1]);
            holder.title.setText(elements[3]);
            holder.location.setText(elements[2]);
            if( elements.length > 4 ) {
                holder.message.setText(elements[4]);
            }else{
                holder.message.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){
            holder.level.setVisibility(View.GONE);
            holder.timestamp.setVisibility(View.GONE);
            holder.title.setVisibility(View.GONE);
            holder.location.setVisibility(View.GONE);
            holder.message.setText(line);
        }


//        holder.info.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(SqdrApplication.getAppContext(), "No available info", Toast.LENGTH_SHORT).show();
//            }
//        });

        return v;
    }
}
