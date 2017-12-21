package com.ramusthastudio.barvolume;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
  private static final String RESULT_STATE = "result_state";
  private EditText fLengthTv;
  private EditText fWidthTv;
  private EditText fHeightTv;
  private Button fCalculateBtn;
  private TextView fResultTv;

  @Override
  protected void onCreate(Bundle aBundle) {
    super.onCreate(aBundle);
    setContentView(R.layout.activity_main);

    fLengthTv = findViewById(R.id.lengthTextView);
    fWidthTv = findViewById(R.id.widthTextView);
    fHeightTv = findViewById(R.id.heightTextView);
    fCalculateBtn = findViewById(R.id.calculateButton);
    fResultTv = findViewById(R.id.resultTextView);

    fCalculateBtn.setOnClickListener(calculateListener);

    if (aBundle != null) {
      fResultTv.setText(aBundle.getString(RESULT_STATE));
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle aBundle) {
    aBundle.putString(RESULT_STATE, fResultTv.getText().toString());
    super.onSaveInstanceState(aBundle);
  }

  private final View.OnClickListener calculateListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      if (v.getId() == R.id.calculateButton) {
        String length = fLengthTv.getText().toString().trim();
        String width = fWidthTv.getText().toString().trim();
        String height = fHeightTv.getText().toString().trim();
        boolean isEmptyFields = false;
        if (TextUtils.isEmpty(length)) {
          isEmptyFields = true;
          fLengthTv.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(width)) {
          isEmptyFields = true;
          fWidthTv.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(height)) {
          isEmptyFields = true;
          fHeightTv.setError("Field ini tidak boleh kosong");
        }
        if (!isEmptyFields) {
          double l = Double.parseDouble(length);
          double w = Double.parseDouble(width);
          double h = Double.parseDouble(height);
          double volume = l * w * h;
          fResultTv.setText(String.valueOf(volume));
        }
      }
    }
  };
}
