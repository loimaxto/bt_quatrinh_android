package com.example.bt_quatrinh_android.Bai1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bt_quatrinh_android.R;

import java.util.ArrayList;

public class DoiTuongAdapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<DoiTuong> list;
    public DoiTuongAdapter(Context context, int layout, ArrayList<DoiTuong> list)
    {
        this.context=context;
        this.layout=layout;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout,null);

        TextView txtTen = (TextView) view.findViewById(R.id.ten);
        txtTen.setText(list.get(i).hovaten);

        TextView txtSdt = (TextView) view.findViewById(R.id.sdt);
        txtSdt.setText(list.get(i).sdt);
        return view;
    }
}
