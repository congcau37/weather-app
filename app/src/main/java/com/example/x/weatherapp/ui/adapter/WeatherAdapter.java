package com.example.x.weatherapp.ui.adapter;

/* Created by X on 12/9/2017.
* */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.x.weatherapp.R;
import com.example.x.weatherapp.model.WeatherModel;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<WeatherModel> mData;

    public WeatherAdapter(Context mContext, ArrayList<WeatherModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.custom_item_weather, parent, false));
    }

    @Override
    public void onBindViewHolder(WeatherAdapter.ViewHolder holder, int position) {
        holder.txtDateContent.setText(mData.get(position).getDate());
        holder.txtStatusContent.setText(mData.get(position).getDescription());
        holder.txtMaxTemp.setText(mData.get(position).getMaxTemp());
        holder.txtMinTemp.setText(mData.get(position).getMinTemp());
        Glide.with(mContext)
                .load(mData.get(position).getImage())
                .centerCrop()
                .into(holder.imgWeather2);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDateContent, txtStatusContent, txtMaxTemp, txtMinTemp;
        ImageView imgWeather2;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDateContent = itemView.findViewById(R.id.txtDateContent);
            txtStatusContent = itemView.findViewById(R.id.txtStatusContent);
            txtMaxTemp = itemView.findViewById(R.id.txtMaxTemp);
            txtMinTemp = itemView.findViewById(R.id.txtMinTemp);
            imgWeather2 = itemView.findViewById(R.id.imgWeather2);
        }
    }
}
