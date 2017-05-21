package com.android.parteek.dugo;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView t1;
    TextView t2;
    int id;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        t1=(TextView) view.findViewById(R.id.textView2);
        t2=(TextView) view.findViewById(R.id.textEmail);
        preferences=getActivity().getSharedPreferences(Util.pref_name1,MODE_PRIVATE);
        editor=preferences.edit();
        //  String name=preferences.getString(Util.key_name,"");
        String phone=preferences.getString(Util.key_phone,"");
        id=preferences.getInt(Util.key_id,0);
        t1.setText(phone);

        // Inflate the layout for this fragment
        return view;
    }

}
