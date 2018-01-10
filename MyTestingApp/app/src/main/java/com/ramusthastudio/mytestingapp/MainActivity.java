package com.ramusthastudio.mytestingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MainView {
  private EditText fWidthEdt;
  private EditText fHeightEdt;
  private EditText fLengthEdt;
  private Button fCalculateBtn;
  private TextView fResultTv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fWidthEdt = findViewById(R.id.edt_width);
    fHeightEdt = findViewById(R.id.edt_height);
    fLengthEdt = findViewById(R.id.edt_length);
    fCalculateBtn = findViewById(R.id.btn_calculate);
    fResultTv = findViewById(R.id.tv_result);

    final MainPresenter presenter = new MainPresenter(this);
    fCalculateBtn.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        String width = fWidthEdt.getText().toString().trim();
        String height = fHeightEdt.getText().toString().trim();
        String length = fLengthEdt.getText().toString().trim();

        boolean isEmptyFields = false;

        if (TextUtils.isEmpty(length)) {
          isEmptyFields = true;
          fLengthEdt.setError("Field ini tidak boleh kosong");
        }

        if (TextUtils.isEmpty(width)) {
          isEmptyFields = true;
          fWidthEdt.setError("Field ini tidak boleh kosong");
        }

        if (TextUtils.isEmpty(height)) {
          isEmptyFields = true;
          fHeightEdt.setError("Field ini tidak boleh kosong");
        }

        if (!isEmptyFields) {
          double l = Double.parseDouble(length);
          double w = Double.parseDouble(width);
          double h = Double.parseDouble(height);

          presenter.hitungVolume(l, w, h);
        }
      }
    });
  }

  @Override
  public void showVolume(MainModel aModel) {
    fResultTv.setText(String.valueOf(aModel.getVolume()));
  }
}
