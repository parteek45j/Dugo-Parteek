package com.android.parteek.dugo;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class Splash extends AppCompatActivity {
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       // ActionBar a=getActionBar();
        //a.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        preferences=getSharedPreferences(Util.pref_name1,MODE_PRIVATE);
        boolean isReg=preferences.contains(Util.key_phone);
        if(isReg){
                handler.sendEmptyMessageDelayed(101,3000);
        }else{
            handler.sendEmptyMessageDelayed(102,3000);
        }
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==102){
                Intent i=new Intent(Splash.this,Login.class);
                startActivity(i);
                finish();
            }else if(msg.what==101){
                Intent i=new Intent(Splash.this,Home.class);
                startActivity(i);
                finish();
            }
        }
    };
}
