package com.ramusthastudio.myflexiblefragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class OptionDialogFragment extends DialogFragment implements View.OnClickListener {
  private Button fChooseBtn;
  private Button fCloseBtn;
  private RadioGroup fOptionsRg;
  private RadioButton fSafRb;
  private RadioButton fMouRb;
  private RadioButton fLvgRb;
  private RadioButton fMoyesRb;
  private OnOptionDialogListener onOptionDialogListener;
  public OptionDialogFragment() {
    // Required empty public constructor
  }
  public OnOptionDialogListener getOnOptionDialogListener() { return onOptionDialogListener; }
  public void setOnOptionDialogListener(OnOptionDialogListener onOptionDialogListener) {
    this.onOptionDialogListener = onOptionDialogListener;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_option_dialog, container, false);
    fChooseBtn = view.findViewById(R.id.btn_choose);
    fCloseBtn = view.findViewById(R.id.btn_close);
    fSafRb = view.findViewById(R.id.rb_saf);
    fLvgRb = view.findViewById(R.id.rb_lvg);
    fMouRb = view.findViewById(R.id.rb_mou);
    fMoyesRb = view.findViewById(R.id.rb_moyes);

    fChooseBtn.setOnClickListener(this);
    fCloseBtn.setOnClickListener(this);
    fOptionsRg = view.findViewById(R.id.rg_options);
    return view;
  }
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_close:
        getDialog().cancel();
        break;
      case R.id.btn_choose:
        int checkedRadioButtonId = fOptionsRg.getCheckedRadioButtonId();
        if (checkedRadioButtonId != -1) {
          String coach = null;
          switch (checkedRadioButtonId) {
            case R.id.rb_saf:
              coach = fSafRb.getText().toString().trim();
              break;
            case R.id.rb_mou:
              coach = fMouRb.getText().toString().trim();
              break;
            case R.id.rb_lvg:
              coach = fLvgRb.getText().toString().trim();
              break;
            case R.id.rb_moyes:
              coach = fMoyesRb.getText().toString().trim();
              break;
          }
          getOnOptionDialogListener().onOptionChoosen(coach);
          getDialog().cancel();
        }
        break;
    }
  }

  public interface OnOptionDialogListener {
    void onOptionChoosen(String text);
  }
}