package com.ramusthastudio.mypreloaddata;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class AppPreference {
  private final SharedPreferences prefs;
  private final Context context;

  public AppPreference(Context context) {
    prefs = PreferenceManager.getDefaultSharedPreferences(context);
    this.context = context;
  }

  public void setFirstRun(Boolean input) {
    SharedPreferences.Editor editor = prefs.edit();
    String key = context.getResources().getString(R.string.app_first_run);
    editor.putBoolean(key, input);
    editor.commit();
  }

  public Boolean getFirstRun() {
    String key = context.getResources().getString(R.string.app_first_run);
    return prefs.getBoolean(key, true);
  }
}
