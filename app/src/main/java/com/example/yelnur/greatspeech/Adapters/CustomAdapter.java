package com.example.yelnur.greatspeech.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yelnur.greatspeech.R;

/**
 * Created by Yelnur on 28.01.2018.
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    String[] filler;
    int filler_qnt1[];
    LayoutInflater layoutInflater;

    public CustomAdapter(Context applicationContext, String[] filler, int[] filler_qnt1) {
        this.context = context;
        this.filler = filler;
        this.filler_qnt1 = filler_qnt1;
        layoutInflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return filler.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.filler_listview, null);
        TextView filler_name = (TextView) convertView.findViewById(R.id.filler_name);
        TextView filler_qnt = (TextView) convertView.findViewById(R.id.filler_qnt);
        filler_name.setText(filler[position]);
        filler_qnt.setText(String.valueOf(filler_qnt1[position]));
        return convertView;
    }
}
