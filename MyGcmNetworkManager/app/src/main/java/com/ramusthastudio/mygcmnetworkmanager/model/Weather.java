
package com.ramusthastudio.mygcmnetworkmanager.model;

import com.google.gson.annotations.SerializedName;

public class Weather {
  @SerializedName("id")
  private int id;
  @SerializedName("main")
  private String main;
  @SerializedName("description")
  private String description;
  @SerializedName("icon")
  private String icon;

  public int getId() { return id; }
  public String getMain() { return main; }
  public String getDescription() { return description; }
  public String getIcon() { return icon; }

  @Override public String toString() {
    return "Weather{" +
        "id=" + id +
        ", main='" + main + '\'' +
        ", description='" + description + '\'' +
        ", icon='" + icon + '\'' +
        '}';
  }
}
