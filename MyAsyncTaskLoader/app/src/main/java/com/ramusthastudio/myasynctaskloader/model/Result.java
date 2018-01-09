package com.ramusthastudio.myasynctaskloader.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Result {
  @SerializedName("cnt")
  private int ctn;
  @SerializedName("list")
  private List<OpenWeather> openWeatherList;

  public int getCtn() { return ctn; }
  public List<OpenWeather> getOpenWeatherList() { return openWeatherList; }

  @Override public String toString() {
    return "Result{" +
        "ctn=" + ctn +
        ", openWeatherList=" + openWeatherList +
        '}';
  }
}
