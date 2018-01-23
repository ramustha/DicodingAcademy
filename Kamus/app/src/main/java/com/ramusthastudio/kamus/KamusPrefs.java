package com.ramusthastudio.kamus;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class KamusPrefs {
  private static SharedPreferences sPreferences;

  private KamusPrefs() {}

  public static void init(Context aContext) {
    if (sPreferences == null) {
      sPreferences = PreferenceManager.getDefaultSharedPreferences(aContext);
    }
  }

  public static void setFirstRun(Context aContext, boolean aValue) {
    if (sPreferences == null) {
      throw new NullPointerException("Call KamusPrefs.init() first");
    }

    SharedPreferences.Editor editor = sPreferences.edit();
    editor.putBoolean(aContext.getString(R.string.app_first_run), aValue);
    editor.commit();
  }

  public static boolean getFirstRun(Context aContext) {
    if (sPreferences == null) {
      throw new NullPointerException("Call KamusPrefs.init() first");
    }

    return sPreferences.getBoolean(aContext.getString(R.string.app_first_run), true);
  }
}
