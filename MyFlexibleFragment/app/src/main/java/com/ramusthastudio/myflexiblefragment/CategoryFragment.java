package com.ramusthastudio.myflexiblefragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements View.OnClickListener {
  private Button fDetailCategoryBtn;

  public CategoryFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_category, container, false);
    fDetailCategoryBtn = view.findViewById(R.id.btn_detail_category);
    fDetailCategoryBtn.setOnClickListener(this);
    return view;
  }
  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.btn_detail_category) {
      DetailCategoryFragment fragment = new DetailCategoryFragment();
      Bundle bundle = new Bundle();
      bundle.putString(DetailCategoryFragment.EXTRA_NAME, "Lifestyle");
      String description = "Kategori ini akan berisi produk-produk lifestyle";
      fragment.setArguments(bundle);
      fragment.setDescription(description);

      if (getActivity() instanceof MainActivity) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragment(fragment, DetailCategoryFragment.class.getSimpleName());
      }
    }
  }
}

