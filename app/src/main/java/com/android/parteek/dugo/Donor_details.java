package com.android.parteek.dugo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Donor_details extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
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
    void views(){
        textView=(TextView)findViewById(R.id.detials);
        listView=(ListView)findViewById(R.id.donorlist);
        userBean=new UserBean();
        arrayList=new ArrayList<>();
        pd=new ProgressDialog(this);
        pd.setMessage("Loading Donor's Details");
        pd.setCancelable(false);
        requestQueue=Volley.newRequestQueue(this);
        preferences=getSharedPreferences(Util.pref_name,MODE_PRIVATE);
        editor=preferences.edit();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_details);
        views();
        id=preferences.getInt(Util.key_donor,0);
        boolean isReg=preferences.contains(Util.key_donor);
        //Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
        if(isReg) {
            reteriveDonor();
        }else{
            textView.setText("No Donor Found Yet");
        }
    }
    void reteriveDonor(){
        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Util.reterive, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray jsonArray=object.getJSONArray("students");
                    String n="",p="",ge="",ci="";
                    for(int j=0;j<jsonArray.length();j++){
                        JSONObject jsonObject=jsonArray.getJSONObject(j);
                        n=jsonObject.getString("name");
                        p=jsonObject.getString("phone");
                        ge=jsonObject.getString("gender");
                        ci=jsonObject.getString("city");
                        arrayList.add(new UserBean(0,n,p,ge,ci,"Not Available","Not Available","Not Available"));
                    }
                    userAdapter=new UserAdapter(Donor_details.this,R.layout.demo,arrayList);
                    listView.setAdapter(userAdapter);
                    listView.setOnItemLongClickListener(Donor_details.this);
                    Toast.makeText(Donor_details.this, "Succuessfully Retrieved", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                    pd.dismiss();
                    Toast.makeText(Donor_details.this, "Some Exception", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Donor_details.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("donor", String.valueOf(id));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        userBean=arrayList.get(position);
        AlertDialog.Builder ab=new AlertDialog.Builder(this);
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
                        Toast.makeText(Donor_details.this, "Deleted", Toast.LENGTH_SHORT).show();
                       // deleteUser();
                        break;

                }
            }
        });
        ab.create().show();
        return false;
     //   return false;
    }
    void showUser(){
        AlertDialog.Builder ab=new AlertDialog.Builder(this);
        ab.setCancelable(false);
        ab.setTitle("Detials of "+userBean.getName());
        ab.setMessage(userBean.toString());
        ab.setPositiveButton("Ok",null);
        ab.create().show();
    }
    void deleteUser(){

    }
}
