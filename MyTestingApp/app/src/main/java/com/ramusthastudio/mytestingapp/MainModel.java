package com.ramusthastudio.mytestingapp;

public class MainModel {
  private double volume;

  public MainModel(double aVolume) {
    volume = aVolume;
  }

  public double getVolume() { return volume; }

  public MainModel setVolume(double aVolume) {
    volume = aVolume;
    return this;
  }
}
