
package com.ramusthastudio.mygcmnetworkmanager.model;

import com.google.gson.annotations.SerializedName;

public class Wind {
  @SerializedName("speed")
  private double speed;
  @SerializedName("deg")
  private int deg;

  public double getSpeed() { return speed; }
  public int getDeg() { return deg; }

  @Override public String toString() {
    return "Wind{" +
        "speed=" + speed +
        ", deg=" + deg +
        '}';
  }
}
