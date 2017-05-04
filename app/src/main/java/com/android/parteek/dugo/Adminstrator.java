package com.android.parteek.dugo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Adminstrator extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    ListView listView;
    UserBean userBean;
    ArrayList<UserBean> arrayList;
    UserAdapter userAdapter;
    RequestQueue requestQueue;
    ProgressDialog pd;
    void views(){
        listView=(ListView)findViewById(R.id.list);
        userBean =new UserBean();
        arrayList=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(this);
        pd=new ProgressDialog(this);
        pd.setMessage("Reteriving......");
        pd.setCancelable(false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminstrator);
        views();
        reterive();
    }
    void reterive(){
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Util.reterive, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray jsonArray=object.getJSONArray("students");
                    int id=0;
                    String n="",p="",e="",pass="",ge="",st="",ci="",da="",ti="";
                    for(int j=0;j<jsonArray.length();j++){
                        JSONObject jsonObject=jsonArray.getJSONObject(j);
                        id=jsonObject.getInt("id");
                        n=jsonObject.getString("name");
                        p=jsonObject.getString("phone");
                        ge=jsonObject.getString("gender");
                        ci=jsonObject.getString("city");
                        pass=jsonObject.getString("password");
                        da=jsonObject.getString("date");
                        ti=jsonObject.getString("time");

                        arrayList.add(new UserBean(id,n,p,ge,ci,pass,da,ti));
                    }
                    userAdapter=new UserAdapter(Adminstrator.this,R.layout.demo,arrayList);
                    Log.e("tag",userBean.toString());
                    listView.setAdapter(userAdapter);
                    listView.setOnItemLongClickListener(Adminstrator.this);
                    pd.dismiss();

                }catch (Exception e){
                    e.printStackTrace();
                    pd.dismiss();
                    Toast.makeText(Adminstrator.this, "Some Exception", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Adminstrator.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        userBean=arrayList.get(position);
        AlertDialog.Builder ab=new AlertDialog.Builder(this);
        ab.setCancelable(true);
        String[] items={"View","Send Notification","Delete"};
        ab.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        showUser();
                        break;
                    case 1:
                            deleteUser();
                        break;

                    case 2:
                        deleteUser();
                        break;
                }
            }
        });
        ab.create().show();
        return false;
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
        Intent intent=new Intent(Adminstrator.this,Notification.class);
        startActivity(intent);
    }
}

