package com.android.parteek.dugo;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suraj on 4/25/2017.
 */

public class UserAdapter extends ArrayAdapter<UserBean> {
    Context context;
    int resource;
    ArrayList<UserBean> arrayList;
    public UserAdapter(Context context, int resource, ArrayList<UserBean> arrayList) {
        super(context, resource, arrayList);
        this.context=context;
        this.resource=resource;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=null;
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        view=layoutInflater.inflate(resource,parent,false);

        TextView t1=(TextView)view.findViewById(R.id.textViewid1);
        TextView t2=(TextView)view.findViewById(R.id.textViewname);

        UserBean userBean=arrayList.get(position);
        t1.setText(userBean.getName());
        t2.setText(userBean.getCity());
        return view;
    }
}
