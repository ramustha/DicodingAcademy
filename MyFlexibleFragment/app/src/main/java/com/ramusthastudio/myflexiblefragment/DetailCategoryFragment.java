package com.ramusthastudio.myflexiblefragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailCategoryFragment extends Fragment implements View.OnClickListener {
  public static final String EXTRA_NAME = "extra_name";
  private TextView fCategoryNameTv;
  private TextView fCategoryDescriptionTv;
  private Button fProfileBtn;
  private Button fShowDialogBtn;
  private String fDescription;

  public DetailCategoryFragment() {
    // Required empty public constructor
  }

  public String getDescription() { return fDescription; }
  public void setDescription(String description) {
    this.fDescription = description;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_detail_category, container, false);
    fCategoryNameTv = view.findViewById(R.id.tv_category_name);
    fCategoryDescriptionTv = view.findViewById(R.id.tv_category_description);
    fProfileBtn = view.findViewById(R.id.btn_profile);
    fShowDialogBtn = view.findViewById(R.id.btn_show_dialog);

    fProfileBtn.setOnClickListener(this);
    fShowDialogBtn.setOnClickListener(this);
    return view;
  }
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    String categoryName = getArguments().getString(EXTRA_NAME);
    fCategoryNameTv.setText(categoryName);
    fCategoryDescriptionTv.setText(getDescription());
  }
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_profile:
        if (getActivity() instanceof MainActivity) {
          MainActivity mainActivity = (MainActivity) getActivity();
          mainActivity.replaceFragment(new HomeFragment(), HomeFragment.class.getSimpleName());
        }

        break;
      case R.id.btn_show_dialog:
        OptionDialogFragment fragment = new OptionDialogFragment();
        fragment.setOnOptionDialogListener(new OptionDialogFragment.OnOptionDialogListener() {
          @Override
          public void onOptionChoosen(String text) {
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
          }
        });
        FragmentManager mFragmentManager = getChildFragmentManager();
        fragment.show(mFragmentManager, OptionDialogFragment.class.getSimpleName());
        break;
    }
  }
}
