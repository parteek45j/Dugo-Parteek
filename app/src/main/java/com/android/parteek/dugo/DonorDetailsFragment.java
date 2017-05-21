package com.android.parteek.dugo;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DonorDetailsFragment extends Fragment {
    int donorId;
    TextView textView;
    ListView listView;
    UserBean userBean;
    ArrayList<UserBean> arrayList;
    UserAdapter userAdapter;
    RequestQueue requestQueue;
    ProgressDialog pd;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int id;


    public DonorDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_donor_details, container, false);
        textView=(TextView)view.findViewById(R.id.detials);
        listView=(ListView)view.findViewById(R.id.donorlist);
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Loading Donor's Details");
        pd.setCancelable(false);
        userBean=new UserBean();
        arrayList=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(getActivity());
        preferences=getActivity().getSharedPreferences(Util.pref_name,MODE_PRIVATE);
        editor=preferences.edit();
        id=preferences.getInt(Util.key_request,0);
        Log.e("idddddd", String.valueOf(id));
        boolean isReg=preferences.contains(Util.key_request);
        //Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
        if(isReg) {
           // Toast.makeText(getActivity(), ""+id, Toast.LENGTH_SHORT).show();
            reteriveDonor();
        }else{
            textView.setText("No Donor Found Yet");
        }
        // Inflate the layout for this fragment
        return view;
    }

    void reteriveDonor(){
        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Util.reterive, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray jsonArray=object.getJSONArray("students");
                    int see=0;String n="",p="",ge="",ci="",da="";
                    for(int j=0;j<jsonArray.length();j++){
                        JSONObject jsonObject=jsonArray.getJSONObject(j);
                        // see=jsonObject.getInt("SeekerId");
                        //  da=jsonObject.getString("Date");
                        n=jsonObject.getString("name");
                        p=jsonObject.getString("phone");
                        // ge=jsonObject.getString("gender");
                        //ci=jsonObject.getString("city");
                        arrayList.add(new UserBean(0,n,p,ge,ci,"","","Not Available","Not Available","Not Available"));
                    }
                    userAdapter=new UserAdapter(getActivity(),R.layout.demo,arrayList);
                    listView.setAdapter(userAdapter);
//                    listView.setOnItemLongClickListener(this);
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                            userBean=arrayList.get(position);
                            AlertDialog.Builder ab=new AlertDialog.Builder(getActivity());
                            ab.setCancelable(true);
                            String[] items={"View","Delete"};
                            ab.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0:
                                            showUser();
                                            break;
                                        case 1:
                                            editor.clear();
                                            editor.commit();
                                            arrayList.remove(position);
                                            textView.setText("No Donor Found Yet");
                                            userAdapter.notifyDataSetChanged();
                                            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                            // deleteUser();
                                            break;

                                    }
                                }
                            });
                            ab.create().show();

                            return false;
                        }
                    });
                    Toast.makeText(getActivity(), "Succuessfully Retrieved", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Some Exception", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("requestID", String.valueOf(id));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

  /*  @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        userBean=arrayList.get(position);
        AlertDialog.Builder ab=new AlertDialog.Builder(getActivity());
        ab.setCancelable(true);
        String[] items={"View","Delete"};
        ab.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        showUser();
                        break;
                    case 1:
                        editor.clear();
                        editor.commit();
                        arrayList.remove(position);
                        textView.setText("No Donor Found Yet");
                        userAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                        // deleteUser();
                        break;

                }
            }
        });
        ab.create().show();
        return false;
        //   return false;
    }*/
    void showUser(){
        AlertDialog.Builder ab=new AlertDialog.Builder(getActivity());
        ab.setCancelable(false);
        ab.setTitle("Detials of "+userBean.getName());
        ab.setMessage(userBean.toString());
        ab.setPositiveButton("Ok",null);
        ab.create().show();
    }

}
