package com.android.parteek.dugo;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Notification extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Spinner spOpt;
    Spinner spCity;
    TextView text;
    AppCompatButton button;
    ArrayAdapter<String> adapter,adapter1;
    UserBean userBean;
    String option;
    RequestQueue requestQueue;
    ProgressDialog pd;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int id;
    String date_time;

    void views(){
        userBean=new UserBean();
        spOpt=(Spinner)findViewById(R.id.spinneropt);
        spCity=(Spinner)findViewById(R.id.spinnercity1);
        text =(TextView)findViewById(R.id.textAddress);
        button=(AppCompatButton)findViewById(R.id.btn);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Send this individual");
        adapter.add("Send by city");
        spOpt.setAdapter(adapter);
        spOpt.setOnItemSelectedListener(this);
        button.setOnClickListener(this);
        requestQueue= Volley.newRequestQueue(this);
        pd=new ProgressDialog(this);
        pd.setMessage("Sending Notification.....");
        pd.setCancelable(false);
        preferences=getSharedPreferences(Util.pref_name1,MODE_PRIVATE);
        editor=preferences.edit();
        id=preferences.getInt(Util.key_id,0);
        date_time= DateFormat.getDateTimeInstance().format(new Date());



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        views();
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder p=new PlacePicker.IntentBuilder();
                try {
                    Intent intent=p.build(Notification.this);
                    startActivityForResult(intent,1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                Place place=PlacePicker.getPlace(data,this);
                String address=String.format("%s",place.getName()+"\n"+place.getAddress());
                text.setText(address);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        option=adapter.getItem(position);
        if(option.contentEquals("Send this individual")){
            text.setEnabled(true);
            text.setFocusable(true);
            spCity.setEnabled(false);
            spCity.setFocusable(false);

        }else if(option.contentEquals("Send by city")){
            spCity.setEnabled(true);
            spCity.setFocusable(true);
            adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
            adapter1.add("Ludhiana");
            adapter1.add("Amritsar");
            adapter1.add("Jalandhar");
            spCity.setAdapter(adapter1);
            spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    userBean.setCity(adapter1.getItem(position));
                    Toast.makeText(Notification.this, ""+userBean.getCity(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        //putRequest();
            sendNoti();
        finish();
    }


    void sendNoti(){
//        pd.show();
        StringRequest request=new StringRequest(Request.Method.POST, Util.sendNotif, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    int succ=object.getInt("success");
                    int idd=object.getInt("UserId");
                    String message=object.getString("message");
                    if(succ>0){
                        Toast.makeText(Notification.this, ""+message , Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Notification.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                  pd.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(Notification.this, "Some Exception", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Notification.this, "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("city",userBean.getCity());
                map.put("id", String.valueOf(id));
                map.put("date_time",date_time);
                return map;
            }
        };
        requestQueue.add(request);
    }
    void putRequest(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Util.insertId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    int succ=object.getInt("success");
                    String mess=object.getString("message");
                    if(succ>0){
                        Toast.makeText(Notification.this, ""+mess, Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(Notification.this, ""+mess, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Notification.this, "Some Exception", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Notification.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("id", String.valueOf(id));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
