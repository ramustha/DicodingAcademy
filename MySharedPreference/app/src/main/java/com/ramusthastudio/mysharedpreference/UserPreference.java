package com.ramusthastudio.mysharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

public final class UserPreference {
  private static final String KEY_NAME = "name";
  private static final String KEY_EMAIL = "email";
  private static final String KEY_LOVE_MU = "love_mu";
  private static final String KEY_PHONE_NUMBER = "phone_number";
  private static final String KEY_AGE = "age";
  private static SharedPreferences sPreferences;

  private UserPreference() {}

  public static void init(Context context) {
    String PREFS_NAME = "UserPref";
    if (sPreferences == null) {
      sPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
  }

  public static String getName() { return sPreferences.getString(KEY_NAME, null); }
  public static String getEmail() { return sPreferences.getString(KEY_EMAIL, null); }
  public static boolean isLoveMU() { return sPreferences.getBoolean(KEY_LOVE_MU, false); }
  public static String getPhoneNumber() { return sPreferences.getString(KEY_PHONE_NUMBER, null); }
  public static int getAge() { return sPreferences.getInt(KEY_AGE, 0); }

  public static void setName(String name) {
    SharedPreferences.Editor editor = sPreferences.edit();
    editor.putString(KEY_NAME, name);
    editor.apply();
  }

  public static void setEmail(String email) {
    SharedPreferences.Editor editor = sPreferences.edit();
    editor.putString(KEY_EMAIL, email);
    editor.apply();
  }

  public static void setLoveMU(boolean status) {
    SharedPreferences.Editor editor = sPreferences.edit();
    editor.putBoolean(KEY_LOVE_MU, status);
    editor.apply();
  }

  public static void setPhoneNumber(String phoneNumber) {
    SharedPreferences.Editor editor = sPreferences.edit();
    editor.putString(KEY_PHONE_NUMBER, phoneNumber);
    editor.apply();
  }

  public static void setAge(int age) {
    SharedPreferences.Editor editor = sPreferences.edit();
    editor.putInt(KEY_AGE, age);
    editor.apply();
  }
}
