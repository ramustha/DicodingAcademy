
package com.ramusthastudio.myasynctaskloader.model;

import com.google.gson.annotations.SerializedName;

public class Main {
  @SerializedName("temp")
  private double temp;
  @SerializedName("pressure")
  private double pressure;
  @SerializedName("humidity")
  private int humidity;
  @SerializedName("temp_min")
  private double tempMin;
  @SerializedName("temp_max")
  private double tempMax;
  @SerializedName("sea_level")
  private double seaLevel;
  @SerializedName("grnd_level")
  private double grndLevel;

  public double getTemp() { return temp; }
  public double getPressure() { return pressure; }
  public int getHumidity() { return humidity; }
  public double getTempMin() { return tempMin; }
  public double getTempMax() { return tempMax; }
  public double getSeaLevel() { return seaLevel; }
  public double getGrndLevel() { return grndLevel; }

  @Override public String toString() {
    return "Main{" +
        "temp=" + temp +
        ", pressure=" + pressure +
        ", humidity=" + humidity +
        ", tempMin=" + tempMin +
        ", tempMax=" + tempMax +
        ", seaLevel=" + seaLevel +
        ", grndLevel=" + grndLevel +
        '}';
  }
}
