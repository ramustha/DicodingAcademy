package com.ramusthastudio.myflexiblefragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

  public HomeFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    Button btnCategory = view.findViewById(R.id.btn_category);
    btnCategory.setOnClickListener(this);
    return view;
  }
  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.btn_category) {
      CategoryFragment fragment = new CategoryFragment();

      if (getActivity() instanceof MainActivity) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragment(fragment, CategoryFragment.class.getSimpleName());
      }
    }
  }
}
