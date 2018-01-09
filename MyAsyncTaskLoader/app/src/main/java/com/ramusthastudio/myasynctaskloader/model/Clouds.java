
package com.ramusthastudio.myasynctaskloader.model;

import com.google.gson.annotations.SerializedName;

public class Clouds {
  @SerializedName("all")
  private int all;

  public int getAll() { return all; }

  @Override public String toString() {
    return "Clouds{" +
        "all=" + all +
        '}';
  }
}
