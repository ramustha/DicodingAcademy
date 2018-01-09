
package com.ramusthastudio.myasynctaskloader.model;

import com.google.gson.annotations.SerializedName;

public class Wind {
  @SerializedName("speed")
  private double speed;
  @SerializedName("deg")
  private double deg;

  public double getSpeed() { return speed; }
  public double getDeg() { return deg; }

  @Override public String toString() {
    return "Wind{" +
        "speed=" + speed +
        ", deg=" + deg +
        '}';
  }
}
