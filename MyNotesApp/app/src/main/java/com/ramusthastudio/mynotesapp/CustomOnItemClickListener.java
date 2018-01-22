package com.ramusthastudio.mynotesapp;

import android.view.View;
import java.lang.ref.WeakReference;

public final class CustomOnItemClickListener implements View.OnClickListener {
  private final int position;
  private final WeakReference<OnItemClickCallback> weakReference;

  public CustomOnItemClickListener(int position, OnItemClickCallback onItemClickCallback) {
    this.position = position;
    weakReference = new WeakReference<>(onItemClickCallback);
  }

  @Override
  public void onClick(View view) {
    final OnItemClickCallback ref = weakReference.get();
    ref.onItemClicked(view, position);
  }

  public interface OnItemClickCallback {
    void onItemClicked(View view, int position);
  }
}
