package com.example.x.weatherapp.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.x.weatherapp.MyApplication;
import com.example.x.weatherapp.R;
import com.example.x.weatherapp.config.common.Constants;
import com.example.x.weatherapp.ui.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private EditText txtInputCity;
    private Button btnNextDay, btnSearch;
    private TextView txtCityName, txtCountryName, txtTemparature, txtStatus, txtHumidityDetail, txtCloudDetail, txtWindDetail, txtDateUpdate;
    private ImageView imgWeather;
    private ProgressDialog progressDialog;
    private View view1 = null;
    private String data;
    public static final String TAG = "LIST_WEATHER_FRAGMENT";

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view1 == null)
            view1 = inflater.inflate(R.layout.fragment_home, container, false);
        return view1;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initControls(view);
        initEvents();
    }

    private void initEvents() {
        btnSearch.setOnClickListener(this);
        btnNextDay.setOnClickListener(this);
    }

    private void initControls(View view) {
        txtInputCity = view.findViewById(R.id.txtInputCity);
        btnNextDay = view.findViewById(R.id.btnNextDay);
        btnSearch = view.findViewById(R.id.btnSearch);
        txtCityName = view.findViewById(R.id.txtCityName);
        txtCountryName = view.findViewById(R.id.txtCountryName);
        txtTemparature = view.findViewById(R.id.txtTemparature);
        txtStatus = view.findViewById(R.id.txtStatus);
        txtHumidityDetail = view.findViewById(R.id.txtHumidityDetail);
        txtCloudDetail = view.findViewById(R.id.txtCloudDetail);
        txtWindDetail = view.findViewById(R.id.txtWindDetail);
        txtDateUpdate = view.findViewById(R.id.txtDateUpdate);
        imgWeather = view.findViewById(R.id.imgWeather);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNextDay:
                ((MainActivity) getActivity()).nextFragment(R.id.myLayout, ListWeatherFragment.newInstance(data), TAG);
                break;
            case R.id.btnSearch:
                data = txtInputCity.getText().toString();
                if (data.equals(""))
                    data = "Seoul";
                getCurrentWeather(data);
                txtInputCity.setText("");
                break;
        }
    }

    private void getCurrentWeather(final String data) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=" + Constants.APP_ID;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(Constants.TAG, response.toString());
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        try {
                            JSONArray status = response.getJSONArray("weather");
                            txtStatus.setText(status.getJSONObject(0).getString("description"));
                            txtTemparature.setText(String.valueOf(response.getJSONObject("main").getDouble("temp")));
                            txtCityName.setText(data);
                            txtCountryName.setText(response.getJSONObject("sys").getString("country"));
                            txtHumidityDetail.setText(response.getJSONObject("main").getInt("humidity") + "%");
                            txtWindDetail.setText(response.getJSONObject("wind").getInt("speed") + "m/s");
                            txtCloudDetail.setText(response.getJSONObject("clouds").getInt("all") + "%");
                            Glide.with(getActivity())
                                    .load(Constants.LINK_IMAGE
                                            + response.getJSONArray("weather").getJSONObject(0).getString("icon") + ".png")
                                    .centerCrop()
                                    .into(imgWeather);
                            String day = response.getString("dt");
                            Long dayl = Long.valueOf(day);
                            Date date = new Date(dayl * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");
                            day = simpleDateFormat.format(date);
                            txtDateUpdate.setText(day);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(getActivity(), "Cannot load data", Toast.LENGTH_SHORT).show();
            }
        });
        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(jsonObjReq, Constants.TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().cancelPendingRequests(Constants.TAG);
    }
}
