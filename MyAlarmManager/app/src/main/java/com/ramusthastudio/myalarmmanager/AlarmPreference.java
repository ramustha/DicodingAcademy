package com.ramusthastudio.myalarmmanager;

import android.content.Context;
import android.content.SharedPreferences;

public final class AlarmPreference {
  private static final String PREF_NAME = "AlarmPreference";
  private static final String KEY_ONE_TIME_DATE = "oneTimDate";
  private static final String KEY_ONE_TIME_TIME = "oneTimeTime";
  private static final String KEY_ONE_TIME_MESSAGE = "oneTimeMessage";
  private static final String KEY_REPEATING_TIME = "repeatingTime";
  private static final String KEY_REPEATING_MESSAGE = "repeatingMessage";
  private static SharedPreferences sSharedPreferences;

  private AlarmPreference() {}

  public static void create(Context context) {
    sSharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
  }
  public static long getOneTimeDate() { return sSharedPreferences.getLong(KEY_ONE_TIME_DATE, 0); }
  public static long getOneTimeTime() { return sSharedPreferences.getLong(KEY_ONE_TIME_TIME, 0); }
  public static long getRepeatingTime() { return sSharedPreferences.getLong(KEY_REPEATING_TIME, 0); }
  public static String getOneTimeMessage() { return sSharedPreferences.getString(KEY_ONE_TIME_MESSAGE, null); }
  public static String getRepeatingMessage() { return sSharedPreferences.getString(KEY_REPEATING_MESSAGE, null); }

  public static void setOneTimeDate(long date) {
    SharedPreferences.Editor editor = sSharedPreferences.edit();
    editor.putLong(KEY_ONE_TIME_DATE, date);
    editor.commit();
  }
  public static void setOneTimeTime(long time) {
    SharedPreferences.Editor editor = sSharedPreferences.edit();
    editor.putLong(KEY_ONE_TIME_TIME, time);
    editor.commit();
  }
  public static void setOneTimeMessage(String message) {
    SharedPreferences.Editor editor = sSharedPreferences.edit();
    editor.putString(KEY_ONE_TIME_MESSAGE, message);
    editor.commit();
  }
  public static void setRepeatingTime(long time) {
    SharedPreferences.Editor editor = sSharedPreferences.edit();
    editor.putLong(KEY_REPEATING_TIME, time);
    editor.commit();
  }
  public static void setRepeatingMessage(String message) {
    SharedPreferences.Editor editor = sSharedPreferences.edit();
    editor.putString(KEY_REPEATING_MESSAGE, message);
    editor.commit();
  }
  public static void clear() {
    SharedPreferences.Editor editor = sSharedPreferences.edit();
    editor.clear();
    editor.commit();
  }
}
