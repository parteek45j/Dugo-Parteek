package com.android.parteek.dugo;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFrag extends Fragment implements View.OnClickListener {

    Spinner spOpt;
    Spinner spCity;
    TextView text;
    AppCompatButton button;
    ArrayAdapter<String> adapter,adapter1;
    UserBean userBean;
    String option;
    RequestQueue requestQueue;
    ProgressDialog pd;
    SharedPreferences preferences,preferences1;
    SharedPreferences.Editor editor,editor1;
    int id;
    String date_time,wifi;

    void views(){



    }

    public NotificationFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userBean=new UserBean();
        date_time= DateFormat.getDateTimeInstance().format(new Date());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_notification,container,false);
        spOpt=(Spinner)view.findViewById(R.id.spinneropt);
        spCity=(Spinner)view.findViewById(R.id.spinnercity1);
        text =(TextView)view.findViewById(R.id.textAddress);
        button=(AppCompatButton)view.findViewById(R.id.btn);


        adapter=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Ludhiana");
        adapter.add("Jalandhar");
        adapter.add("Amritsar");
        spOpt.setAdapter(adapter);
//        spOpt.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) getActivity());
        spOpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userBean.setCity(adapter.getItem(position));
                //Toast.makeText(getContext(), ""+userBean.getCity(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter1=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_dropdown_item);
        adapter1.add("A+");
        adapter1.add("A-");
        adapter1.add("B+");
        adapter1.add("B-");
        adapter1.add("O+");
        adapter1.add("O-");
        adapter1.add("AB+");
        adapter1.add("AB-");
        spCity.setAdapter(adapter1);
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userBean.setBloodGroup(adapter1.getItem(position));
                //Toast.makeText(getContext(), ""+userBean.getBlooddGroup(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(this);
        requestQueue= Volley.newRequestQueue(getActivity());
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Sending Notification.....");
        pd.setCancelable(false);


        preferences=this.getActivity().getSharedPreferences(Util.pref_name1,MODE_PRIVATE);
        editor=preferences.edit();
        wifi=preferences.getString(Util.key_mac,"");
        id=preferences.getInt(Util.key_id,0);
        preferences1=this.getActivity().getSharedPreferences(Util.pref_name,MODE_PRIVATE);
        editor1=preferences1.edit();

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder p=new PlacePicker.IntentBuilder();
                try {
                    Intent intent=p.build(getActivity());
                    startActivityForResult(intent,1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==getActivity().RESULT_OK){
                Place place=PlacePicker.getPlace(data,getActivity());
                String address=String.format(" %s",place.getName()+"\n"+place.getAddress());
                text.setText(address);
            }
        }
    }


    @Override
    public void onClick(View v) {
        //putRequest();
        sendNoti();
       // finish();
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
                        editor1.putInt(Util.key_request,idd);
                        editor1.commit();
                        Toast.makeText(getActivity(), ""+message , Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
                    }
                    pd.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Some Exception", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("city",userBean.getCity());
                map.put("blood",userBean.getBlooddGroup());
                map.put("id", String.valueOf(id));
                map.put("date_time",date_time);
                map.put("wifi",wifi);
                return map;
            }
        };
        requestQueue.add(request);
    }


}
