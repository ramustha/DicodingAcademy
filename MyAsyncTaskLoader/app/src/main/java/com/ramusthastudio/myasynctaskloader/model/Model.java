
package com.ramusthastudio.myasynctaskloader.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Model {
  @SerializedName("coord")
  private Coord coord;
  @SerializedName("weather")
  private List<Weather> weather = null;
  @SerializedName("base")
  private String base;
  @SerializedName("main")
  private Main main;
  @SerializedName("wind")
  private Wind wind;
  @SerializedName("clouds")
  private Clouds clouds;
  @SerializedName("dt")
  private int dt;
  @SerializedName("sys")
  private Sys sys;
  @SerializedName("id")
  private int id;
  @SerializedName("name")
  private String name;
  @SerializedName("cod")
  private int cod;

  public Coord getCoord() { return coord; }
  public List<Weather> getWeather() { return weather; }
  public String getBase() { return base; }
  public Main getMain() { return main; }
  public Wind getWind() { return wind; }
  public Clouds getClouds() { return clouds; }
  public int getDt() { return dt; }
  public Sys getSys() { return sys; }
  public int getId() { return id; }
  public String getName() { return name; }
  public int getCod() { return cod; }

  @Override public String toString() {
    return "Model{" +
        "coord=" + coord +
        ", weather=" + weather +
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
