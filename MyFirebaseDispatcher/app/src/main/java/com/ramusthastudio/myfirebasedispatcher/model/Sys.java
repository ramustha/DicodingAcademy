
package com.ramusthastudio.myfirebasedispatcher.model;

import com.google.gson.annotations.SerializedName;

public class Sys {
  @SerializedName("message")
  private double message;
  @SerializedName("country")
  private String country;
  @SerializedName("sunrise")
  private int sunrise;
  @SerializedName("sunset")
  private int sunset;

  public double getMessage() { return message; }
  public String getCountry() { return country; }
  public int getSunrise() { return sunrise; }
  public int getSunset() { return sunset; }

  @Override public String toString() {
    return "Sys{" +
        "message=" + message +
        ", country='" + country + '\'' +
        ", sunrise=" + sunrise +
        ", sunset=" + sunset +
        '}';
  }
}
