package com.ramusthastudio.myasynctaskloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ramusthastudio.myasynctaskloader.model.OpenWeather;
import com.ramusthastudio.myasynctaskloader.model.Result;
import com.ramusthastudio.myasynctaskloader.model.Weather;
import java.util.List;

public class WeatherAdapter extends BaseAdapter {
  private final LayoutInflater fLayoutInflater;
  private Result fOpenWeathersData;

  public WeatherAdapter(Context aContext) {
    fLayoutInflater = (LayoutInflater) aContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override public long getItemId(int position) { return position; }
  @Override public int getCount() { return fOpenWeathersData == null ? 0 : fOpenWeathersData.getOpenWeatherList().size(); }
  @Override public OpenWeather getItem(int position) { return fOpenWeathersData.getOpenWeatherList().get(position); }

  public WeatherAdapter setOpenWeathersData(Result aOpenWeathersData) {
    fOpenWeathersData = aOpenWeathersData;
    notifyDataSetChanged();
    return this;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      holder = new ViewHolder();
      convertView = fLayoutInflater.inflate(R.layout.weather_items, null);
      holder.fCityTv = convertView.findViewById(R.id.textKota);
      holder.fTempTv = convertView.findViewById(R.id.textTemp);
      holder.fDescTv = convertView.findViewById(R.id.textDesc);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    OpenWeather openWeather = getItem(position);
    toView(openWeather, holder);
    return convertView;
  }

  static void toView(OpenWeather aOpenWeather, ViewHolder aHolder) {
    List<Weather> weather = aOpenWeather.getWeather();
    aHolder.fCityTv.setText(aOpenWeather.getName());
    aHolder.fTempTv.setText(String.valueOf(aOpenWeather.getMain().getTemp()));

    StringBuilder b = new StringBuilder();
    for (Weather w : weather) {
      b.append(w.getMain()).append(", ").append(w.getDescription());
    }
    aHolder.fDescTv.setText(b.toString());
  }

  private static class ViewHolder {
    TextView fCityTv;
    TextView fTempTv;
    TextView fDescTv;
  }
}
