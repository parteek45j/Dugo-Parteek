package com.android.parteek.dugo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {
TextView t;
    EditText textemail,textpass;
    Button loginBtn;
    RequestQueue requestQueue;
    ProgressDialog pd;
    UserBean userBean;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    void views(){
        t=(TextView)findViewById(R.id.link_signup);
        t.setOnClickListener(this);
        textemail=(EditText)findViewById(R.id.input_email);
        textpass=(EditText)findViewById(R.id.input_password);
        loginBtn=(Button)findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
        requestQueue= Volley.newRequestQueue(this);
        pd=new ProgressDialog(this);
        pd.setMessage("Signing in.....");
        pd.setCancelable(false);
        userBean=new UserBean();
        preferences=getSharedPreferences(Util.pref_name1,MODE_PRIVATE);
        editor=preferences.edit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        views();
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.link_signup) {
            Intent i = new Intent(this, Register.class);
            startActivity(i);
            finish();
        }else if(id==R.id.btn_login){
            userBean.setPhone(textemail.getText().toString().trim());
            userBean.setPassword(textpass.getText().toString().trim());
            if(!userBean.getPhone().equals("123456")&&!userBean.getPassword().equals("admin")) {
                login();
            }else{
                Intent intent=new Intent(Login.this,Adminstrator.class);
                startActivity(intent);
                finish();
            }
        }
    }
    void login(){
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Util.login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object=new JSONObject(response);
                    String mess=object.getString("message");
                    JSONArray jsonArray=object.getJSONArray("students");
                    int id=0;
                    String n="",p="",g="",c="",pa="",da="",ti="";
                    for(int y=0;y<jsonArray.length();y++){
                        JSONObject jsonObject=jsonArray.getJSONObject(y);
                        id=jsonObject.getInt("ID");
                        userBean.setId(id);
                    }
                    if(mess.contains("Login Sucessful")) {
                        editor.putString(Util.key_phone, userBean.getPhone());
                        editor.putInt(Util.key_id,userBean.getId());
                        editor.commit();
                        Intent i = new Intent(Login.this, Home.class);
                        startActivity(i);
                        finish();
                        pd.dismiss();
                        Toast.makeText(Login.this, mess+userBean.getId(), Toast.LENGTH_SHORT).show();
                    }else{
                        pd.dismiss();
                        Toast.makeText(Login.this, mess, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    pd.dismiss();
                    Toast.makeText(Login.this, "Exception", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Login.this, "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("phone",userBean.getPhone());
                map.put("password",userBean.getPassword());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}
