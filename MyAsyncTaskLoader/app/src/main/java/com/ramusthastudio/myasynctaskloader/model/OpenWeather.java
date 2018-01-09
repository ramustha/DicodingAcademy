package com.ramusthastudio.myasynctaskloader.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OpenWeather {
  @SerializedName("coord")
  private Coord coord;
  @SerializedName("weather")
  private List<Weather> weathers;
  @SerializedName("base")
  private String base;
  @SerializedName("main")
  private Main main;
  @SerializedName("wind")
  private Wind wind;
  @SerializedName("clouds")
  private Clouds clouds;
  @SerializedName("dt")
  private long dt;
  @SerializedName("sys")
  private Sys sys;
  @SerializedName("id")
  private long id;
  @SerializedName("name")
  private String name;
  @SerializedName("cod")
  private int cod;

  public Coord getCoord() { return coord; }
  public List<Weather> getWeather() { return weathers; }
  public String getBase() { return base; }
  public Main getMain() { return main; }
  public Wind getWind() { return wind; }
  public Clouds getClouds() { return clouds; }
  public long getDt() { return dt; }
  public Sys getSys() { return sys; }
  public long getId() { return id; }
  public String getName() { return name; }
  public int getCod() { return cod; }

  @Override public String toString() {
    return "OpenWeather{" +
        "coord=" + coord +
        ", weathers=" + weathers +
        ", base='" + base + '\'' +
        ", main=" + main +
        ", wind=" + wind +
        ", clouds=" + clouds +
        ", dt=" + dt +
        ", sys=" + sys +
        ", id=" + id +
        ", name='" + name + '\'' +
        ", cod=" + cod +
        '}';
  }
}
