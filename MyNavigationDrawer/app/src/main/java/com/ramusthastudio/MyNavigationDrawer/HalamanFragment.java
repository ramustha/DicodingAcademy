package com.ramusthastudio.MyNavigationDrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HalamanFragment extends Fragment {
  static final String TAG = HalamanFragment.class.getSimpleName();
  public static final String EXTRAS = "extras";

  public HalamanFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_halaman, container, false);

    TextView textView = view.findViewById(R.id.tv_fragment);

    String halaman = getArguments().getString(EXTRAS);
    textView.setText(halaman);

    Log.e(TAG, "onCreateView: halaman fragment " + halaman);
    return view;
  }
}
