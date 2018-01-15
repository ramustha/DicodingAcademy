package com.ramusthastudio.myrecyclerview;

import android.view.View;

public class CustomOnItemClickListener implements View.OnClickListener {
  private final int position;
  private final OnItemClickCallback onItemClickCallback;

  public interface OnItemClickCallback {
    void onItemClicked(View view, int position);
  }

  public CustomOnItemClickListener(int position, OnItemClickCallback onItemClickCallback) {
    this.position = position;
    this.onItemClickCallback = onItemClickCallback;
  }

  @Override
  public void onClick(View view) {
    onItemClickCallback.onItemClicked(view, position);
  }
}
