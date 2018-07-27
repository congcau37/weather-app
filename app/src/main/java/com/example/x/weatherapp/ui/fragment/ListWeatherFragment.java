package com.example.x.weatherapp.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.x.weatherapp.MyApplication;
import com.example.x.weatherapp.R;
import com.example.x.weatherapp.config.common.Constants;
import com.example.x.weatherapp.model.WeatherModel;
import com.example.x.weatherapp.ui.adapter.WeatherAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListWeatherFragment extends Fragment {
    private String city;
    private ProgressDialog progressDialog;
    private ArrayList<WeatherModel> mData;
    private WeatherAdapter mAdapter;

    public ListWeatherFragment() {
        // Required empty public constructor
    }

    public static ListWeatherFragment newInstance(String city) {
        ListWeatherFragment listWeatherFragment = new ListWeatherFragment();
        Bundle bd = new Bundle();
        bd.putString("city", city);
        listWeatherFragment.setArguments(bd);
        return listWeatherFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bd = getArguments();
        if (bd != null) {
            city = bd.getString("city", "");
            Log.d(Constants.TAG, city);
        }
        initControls(view);
    }

    private void initControls(View view) {
        RecyclerView rvWeathers = view.findViewById(R.id.rvWeathers);
        mData = new ArrayList<>();
        mAdapter = new WeatherAdapter(getActivity(), mData);
        rvWeathers.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvWeathers.setAdapter(mAdapter);
        getListWeather();
    }

    private void getListWeather() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url = "http://api.openweathermap.org/data/2.5/find?q=" + city + "&units=metric&appid=" + Constants.APP_ID + "&cnt=7";
        Log.d(Constants.TAG, url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(Constants.TAG, response.toString());
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        try {
                            JSONArray list = response.getJSONArray("list");
                            for (int i = 0; i < list.length(); i++) {
                                mData.add(createModelFromJSONObject(list.getJSONObject(i)));
                                Log.d(Constants.TAG, list.getJSONObject(i).toString());
                            }
                            mAdapter.notifyDataSetChanged();
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

    private WeatherModel createModelFromJSONObject(JSONObject jsonObject) {
        String day = "", description = "", image = "";
        int maxTemp = 0, minTemp = 0;
        try {
            day = jsonObject.getString("dt");
            Long dayl = Long.valueOf(day);
            Date date = new Date(dayl * 1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");
            day = simpleDateFormat.format(date);
            description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            image = Constants.LINK_IMAGE + jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon") + ".png";
            maxTemp = jsonObject.getJSONObject("main").getInt("temp_max");
            minTemp = jsonObject.getJSONObject("main").getInt("temp_min");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, day + " " + description + " " + image + " " + maxTemp + " " + minTemp);
        return new WeatherModel(description, image, day, String.valueOf(maxTemp), String.valueOf(minTemp));
    }


}
