package com.android.parteek.dugo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {
TextView t;
    EditText textemail,textpass;
    Button loginBtn;
    TextView textFb;
    LoginButton loginButton;
    RequestQueue requestQueue;
    ProgressDialog pd;
    UserBean userBean;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String wifi;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;

    void views(){
        t=(TextView)findViewById(R.id.link_signup);
        textFb=(TextView)findViewById(R.id.faceb);
        loginButton=(LoginButton)findViewById(R.id.loginButton);
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

        WifiManager wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifi=wifiManager.getConnectionInfo().getMacAddress();
        callbackManager=CallbackManager.Factory.create();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);
        views();
        accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                nextActivity(currentProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile=Profile.getCurrentProfile();
                nextActivity(profile);
                textFb.setText("Login Success\n"+loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {
                textFb.setText("Login Canceled");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
//        ActionBar a=getSupportActionBar();
//        a.hide();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile=Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onStop() {
        super.onStop();
        profileTracker.stopTracking();
        accessTokenTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    void nextActivity(Profile profile){
        if(profile!=null){
            Intent intent=new Intent(this,FacebookLogin.class);
            intent.putExtra("name",profile.getName());
            intent.putExtra("picture",profile.getProfilePictureUri(200,200).toString());
            startActivity(intent);

        }
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
        final String token= FirebaseInstanceId.getInstance().getToken();
        Log.e("token",token);
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
                        n=jsonObject.getString("name");
                        userBean.setId(id);
                        userBean.setName(n);
                    }
                    if(mess.contains("Login Sucessful")) {
                        editor.putString(Util.key_phone, userBean.getPhone());
                        editor.putInt(Util.key_id,userBean.getId());
                        editor.putString(Util.key_mac,wifi);
                        editor.putString(Util.key_name,userBean.getName());
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
                map.put("token",token);
                map.put("wifi",wifi);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}
