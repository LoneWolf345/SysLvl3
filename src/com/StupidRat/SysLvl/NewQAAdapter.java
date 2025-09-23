package com.StupidRat.SysLvl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewQAAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private String[] data;

        public NewQAAdapter(Context context) {
                mInflater = LayoutInflater.from(context);
                data = new String[0];
        }

        public void setData(String[] data) {
                if (data == null) {
                        this.data = new String[0];
                } else {
                        this.data = data;
                }
        }

        @Override
        public int getCount() {
                return data != null ? data.length : 0;
        }

        @Override
        public Object getItem(int item) {
                return data != null ? data[item] : null;
        }

        @Override
        public long getItemId(int position) {
                return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;

                if (convertView == null) {
                        convertView = mInflater.inflate(R.layout.list, parent, false);

                        holder = new ViewHolder();
                        holder.primaryText = (TextView) convertView.findViewById(R.id.primary_text);
                        holder.secondaryText = (TextView) convertView.findViewById(R.id.secondary_text);

                        convertView.setTag(holder);
                } else {
                        holder = (ViewHolder) convertView.getTag();
                }

                String item = data[position];
                String primary = item;
                String secondary = "";

                if (item != null) {
                        int separatorIndex = item.indexOf('\n');
                        if (separatorIndex >= 0) {
                                primary = item.substring(0, separatorIndex).trim();
                                secondary = item.substring(separatorIndex + 1).trim();
                        }
                }

                holder.primaryText.setText(primary);
                if (secondary.length() > 0) {
                        holder.secondaryText.setVisibility(View.VISIBLE);
                        holder.secondaryText.setText(secondary);
                } else {
                        holder.secondaryText.setVisibility(View.GONE);
                        holder.secondaryText.setText(null);
                }

                return convertView;
        }

        static class ViewHolder {
                TextView primaryText;
                TextView secondaryText;
        }
}
