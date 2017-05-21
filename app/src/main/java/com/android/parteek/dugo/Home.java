package com.android.parteek.dugo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    TextView tname,temail,tHname,tHphone;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    int id;
    ProgressDialog pd;
    RequestQueue requestQueue;
    String wifi,phone,name;
    void views(){

        //temail.setText(email);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tname=(TextView)findViewById(R.id.textView2);
        temail=(TextView)findViewById(R.id.textEmail);
        temail.setOnClickListener(this);
        preferences=getSharedPreferences(Util.pref_name1,MODE_PRIVATE);
        editor=preferences.edit();
        name=preferences.getString(Util.key_name,"");
        phone=preferences.getString(Util.key_phone,"");
        id=preferences.getInt(Util.key_id,0);
        tname.setText(phone);

        pd=new ProgressDialog(this);
        pd.setMessage("Logging Out....");
        pd.setCancelable(false);
        requestQueue= Volley.newRequestQueue(this);

        View nv=navigationView.getHeaderView(0);
        tHphone=(TextView)nv.findViewById(R.id.textEmail12);
        tHname=(TextView)nv.findViewById(R.id.textName12);
        Toast.makeText(this, ""+name+phone, Toast.LENGTH_SHORT).show();
        tHname.setText(name);
        tHphone.setText(phone);

        views();
    }
    boolean idConnected(){
        connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null&&networkInfo.isConnected());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment frag= new HomeFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragme,frag);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_camera) {

            NotificationFrag frag=new NotificationFrag();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragme,frag);
            fragmentTransaction.commit();
//            Intent i=new Intent(this,Notification.class);
//            startActivity(i);

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            DonorDetailsFragment frag= new DonorDetailsFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragme,frag);
            fragmentTransaction.commit();
//            Intent i=new Intent(this,Donor_details.class);
//            startActivity(i);

        } else if (id == R.id.nav_slideshow) {

//
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder ad=new AlertDialog.Builder(Home.this);
            ad.setTitle("Do You Wish to Logout");
            ad.setCancelable(false);
            ad.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            });
            ad.setPositiveButton("No",null);
            ad.create().show();

        }
        //AIzaSyBMT95IA9pMtC-iMlRbswKldAN5LFfy_6A

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void logout(){
//        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Util.logout, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    int succ=object.getInt("success");
                    String mess=object.getString("message");
                    if(succ>0){
                        Toast.makeText(Home.this, ""+mess, Toast.LENGTH_SHORT).show();
                        editor.clear();
                        editor.commit();
                        Intent i = new Intent(Home.this, Login.class);
                        startActivity(i);
                        finishAffinity();
                    }else{
                        Toast.makeText(Home.this, ""+mess, Toast.LENGTH_SHORT).show();
                    }
                    pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Home.this, "Some Exception", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
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

    @Override
    public void onClick(View v) {
     //   temail.setText(wifi);
    }
}
