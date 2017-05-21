package com.android.parteek.dugo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.io.InputStream;

public class FacebookLogin extends AppCompatActivity {
    TextView textView;
    String name;
    String image;
    AppCompatButton button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_facebook_login);
        textView=(TextView)findViewById(R.id.firstname);
        button1=(AppCompatButton)findViewById(R.id.fblogout);
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        image=intent.getStringExtra("picture");
        textView.setText(name);
        new FacebookLogin.DownloadImage((ImageView)findViewById(R.id.imagefb)).execute(image);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent i=new Intent(FacebookLogin.this,Login.class);
                startActivity(i);
                finish();
            }
        });

    }
    public class DownloadImage extends AsyncTask<String,Void,Bitmap>{
        ImageView imageView;
        public DownloadImage(ImageView imageView){
            this.imageView=imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String userDisplay=params[0];
            Bitmap bit=null;
            try {
                InputStream in=new java.net.URL(userDisplay).openStream();
                bit= BitmapFactory.decodeStream(in);
            }catch (Exception e){
                e.printStackTrace();
            }
            return bit;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
