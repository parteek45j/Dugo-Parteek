package com.android.parteek.dugo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    TextView t;
    EditText name,phone,password;
    Spinner city;
    RadioButton male,female;
    Button signup;
    UserBean userBean;
    ArrayList<UserBean> beanArrayList;
    ArrayAdapter<String> cityAdapter;
    RequestQueue requestQueue;
    String date,time;
    ProgressDialog pd;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    void views(){
        userBean=new UserBean();

        beanArrayList=new ArrayList<>();
        name=(EditText)findViewById(R.id.input_name);

        phone=(EditText)findViewById(R.id.input_phone);


        password=(EditText)findViewById(R.id.input_password);


        city=(Spinner) findViewById(R.id.input_city);

        male=(RadioButton) findViewById(R.id.input_male);
        male.setOnCheckedChangeListener(this);

        female=(RadioButton) findViewById(R.id.input_female);
        female.setOnCheckedChangeListener(this);

        signup=(Button)findViewById(R.id.input_signup);

        requestQueue= Volley.newRequestQueue(this);


        cityAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item);
        cityAdapter.add("---Select City---");
        cityAdapter.add("Ludhiana");
        cityAdapter.add("Amritsar");
        cityAdapter.add("Jalandhar");
        city.setAdapter(cityAdapter);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    userBean.setCity(cityAdapter.getItem(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        signup.setOnClickListener(this);

        date= DateFormat.getDateInstance().format(new Date());
        time= DateFormat.getTimeInstance().format(new Date());

        pd=new ProgressDialog(this);
        pd.setMessage("Registering.... ");
        pd.setCancelable(false);

        preferences=getSharedPreferences(Util.pref_name1,MODE_PRIVATE);
        editor=preferences.edit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        views();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
     int id=buttonView.getId();
        if(isChecked) {
            if (id==R.id.input_male) {
                userBean.setGender("Male");
            } else {
                userBean.setGender("Female");
            }
        }
    }

    @Override
    public void onClick(View v) {
        userBean.setName(name.getText().toString().trim());
        userBean.setPhone(phone.getText().toString().trim());
        userBean.setPassword(password.getText().toString().trim());
        userBean.setDate(date);
        userBean.setTime(time);
        Log.e("details",userBean.toString());
        insert();
    }
    void insert(){
        final String token= FirebaseInstanceId.getInstance().getToken();
        Log.e("Token",token.toString());
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Util.insert, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    int success=object.getInt("success");
                    String message=object.getString("message");
                    int userid=object.getInt("UserId");
                    if(success>0){
                        editor.putString(Util.key_name,userBean.getName());
                        editor.putString(Util.key_phone,userBean.getPhone());
                        editor.putInt(Util.key_id,userid);
                        editor.commit();
                        Intent home=new Intent(Register.this,Home.class);
                        startActivity(home);
                        finish();
                        pd.dismiss();
                        Toast.makeText(Register.this, "Response "+message+" id "+userid, Toast.LENGTH_SHORT).show();
                    }else{
                        pd.dismiss();
                        Toast.makeText(Register.this, "Response "+message, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    pd.dismiss();
                    e.printStackTrace();
                    Toast.makeText(Register.this, "Exception", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError error) {
        pd.dismiss();
        Toast.makeText(Register.this, "Volley Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }){
@Override
protected Map<String, String> getParams() throws AuthFailureError {
        Map<String,String> map=new HashMap<>();
        map.put("name",userBean.getName());
        map.put("phone",userBean.getPhone());
        map.put("gender",userBean.getGender());
        map.put("city",userBean.getCity());
        map.put("password",userBean.getPassword());
        map.put("date",userBean.getDate());
        map.put("time",userBean.getTime());
        map.put("token",token);
        return map;
        }
        };
        requestQueue.add(stringRequest);
        }
        }
