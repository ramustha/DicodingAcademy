package com.ramusthastudio.mysharedpreference;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private TextView tvName;
  private TextView tvAge;
  private TextView tvPhoneNo;
  private TextView tvEmail;
  private TextView tvIsLoveMU;
  private Button btnSave;
  private boolean isPreferenceEmpty = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    UserPreference.init(this);

    tvName = findViewById(R.id.tv_name);
    tvAge = findViewById(R.id.tv_age);
    tvPhoneNo = findViewById(R.id.tv_phone);
    tvEmail = findViewById(R.id.tv_email);
    tvIsLoveMU = findViewById(R.id.tv_is_love_mu);
    btnSave = findViewById(R.id.btn_save);
    btnSave.setOnClickListener(this);
    getSupportActionBar().setTitle("My User Preference");
    showExistingPreference();
  }
  private void showExistingPreference() {
    if (!TextUtils.isEmpty(UserPreference.getName())) {
      tvName.setText(UserPreference.getName());
      tvAge.setText(String.valueOf(UserPreference.getAge()));
      tvIsLoveMU.setText(UserPreference.isLoveMU() ? "Ya" : "Tidak");
      tvEmail.setText(UserPreference.getEmail());
      tvPhoneNo.setText(UserPreference.getPhoneNumber());
      btnSave.setText("Ubah");
    } else {
      final String TEXT_EMPTY = "Tidak Ada";
      tvName.setText(TEXT_EMPTY);
      tvAge.setText(TEXT_EMPTY);
      tvIsLoveMU.setText(TEXT_EMPTY);
      tvEmail.setText(TEXT_EMPTY);
      tvPhoneNo.setText(TEXT_EMPTY);
      btnSave.setText("Simpan");
      isPreferenceEmpty = true;
    }
  }
  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.btn_save) {
      Intent intent = new Intent(MainActivity.this, FormUserPreferenceActivity.class);
      if (isPreferenceEmpty) {
        intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_ADD);
      } else {
        intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_EDIT);
      }
      startActivityForResult(intent, FormUserPreferenceActivity.REQUEST_CODE);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == FormUserPreferenceActivity.REQUEST_CODE) {
      showExistingPreference();
    }
  }
}
