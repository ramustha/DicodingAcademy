package com.ramusthastudio.myintentapp;

import android.os.Parcel;
import android.os.Parcelable;

final class Person implements Parcelable {
  private String fName;
  private int fAge;
  private String fEmail;
  private String fCity;

  protected Person() {}

  protected Person(Parcel in) {
    this.fName = in.readString();
    this.fAge = in.readInt();
    this.fEmail = in.readString();
    this.fCity = in.readString();
  }

  public String getName() { return fName; }
  public int getAge() { return fAge; }
  public String getEmail() { return fEmail; }
  public String getCity() { return fCity; }

  Person setName(String aName) {
    fName = aName;
    return this;
  }
  Person setAge(int aAge) {
    fAge = aAge;
    return this;
  }
  Person setEmail(String aEmail) {
    fEmail = aEmail;
    return this;
  }
  Person setCity(String aCity) {
    fCity = aCity;
    return this;
  }

  @Override public int describeContents() { return 0; }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.fName);
    dest.writeInt(this.fAge);
    dest.writeString(this.fEmail);
    dest.writeString(this.fCity);
  }

  public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
    @Override public Person createFromParcel(Parcel source) {return new Person(source);}
    @Override public Person[] newArray(int size) {return new Person[size];}
  };
}
