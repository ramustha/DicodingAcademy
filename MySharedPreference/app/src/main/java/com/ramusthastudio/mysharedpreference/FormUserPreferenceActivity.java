package com.ramusthastudio.mysharedpreference;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FormUserPreferenceActivity extends AppCompatActivity implements View.OnClickListener {
  public static final String EXTRA_TYPE_FORM = "extra_type_form";
  public static final int REQUEST_CODE = 100;
  public static final int TYPE_ADD = 1;
  public static final int TYPE_EDIT = 2;
  private EditText fNameEdt;
  private EditText fEmailEdt;
  private EditText fPhoneEdt;
  private EditText fAgeEdt;
  private RadioGroup fLoveMuRadioGroup;
  private RadioButton fYesRadioBtn;
  private RadioButton fNoRadioBtn;
  private Button fSaveBtn;
  private int formType;
  private static final String FIELD_REQUIRED = "Field tidak boleh kosong";
  private static final String FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik";
  private static final String FIELD_ISNOT_VALID = "Email tidak valid";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_form_user_preference);

    fNameEdt = findViewById(R.id.edt_name);
    fAgeEdt = findViewById(R.id.edt_age);
    fEmailEdt = findViewById(R.id.edt_email);
    fPhoneEdt = findViewById(R.id.edt_phone);
    fLoveMuRadioGroup = findViewById(R.id.rg_love_mu);
    fYesRadioBtn = findViewById(R.id.rb_yes);
    fNoRadioBtn = findViewById(R.id.rb_no);
    fSaveBtn = findViewById(R.id.btn_save);
    fSaveBtn.setOnClickListener(this);
    formType = getIntent().getIntExtra(EXTRA_TYPE_FORM, 0);

    String actionBarTitle;
    String btnTitle;
    if (formType == 1) {
      actionBarTitle = "Tambah Baru";
      btnTitle = "Simpan";
    } else {
      actionBarTitle = "Ubah";
      btnTitle = "Update";
      showPreferenceInForm();
    }
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(actionBarTitle);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    fSaveBtn.setText(btnTitle);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  private void showPreferenceInForm() {
    fNameEdt.setText(UserPreference.getName());
    fEmailEdt.setText(UserPreference.getEmail());
    fAgeEdt.setText(String.valueOf(UserPreference.getAge()));
    fPhoneEdt.setText(UserPreference.getPhoneNumber());
    if (UserPreference.isLoveMU()) {
      fYesRadioBtn.setChecked(true);
    } else {
      fNoRadioBtn.setChecked(false);
    }
  }
  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.btn_save) {
      String name = fNameEdt.getText().toString().trim();
      String email = fEmailEdt.getText().toString().trim();
      String age = fAgeEdt.getText().toString().trim();
      String phoneNo = fPhoneEdt.getText().toString().trim();
      boolean isLoveMU = fLoveMuRadioGroup.getCheckedRadioButtonId() == R.id.rb_yes;
      boolean isEmpty = false;
      if (TextUtils.isEmpty(name)) {
        isEmpty = true;
        fNameEdt.setError(FIELD_REQUIRED);
      }
      if (TextUtils.isEmpty(email)) {
        isEmpty = true;
        fEmailEdt.setError(FIELD_REQUIRED);
      } else if (!isValidEmail(email)) {
        isEmpty = true;
        fEmailEdt.setError(FIELD_ISNOT_VALID);
      }
      if (TextUtils.isEmpty(age)) {
        isEmpty = true;
        fAgeEdt.setError(FIELD_REQUIRED);
      }
      if (TextUtils.isEmpty(phoneNo)) {
        isEmpty = true;
        fPhoneEdt.setError(FIELD_REQUIRED);
      } else if (!TextUtils.isDigitsOnly(phoneNo)) {
        isEmpty = true;
        fPhoneEdt.setError(FIELD_DIGIT_ONLY);
      }
      if (!isEmpty) {
        UserPreference.setName(name);
        UserPreference.setAge(Integer.parseInt(age));
        UserPreference.setEmail(email);
        UserPreference.setPhoneNumber(phoneNo);
        UserPreference.setLoveMU(isLoveMU);
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show();
        finish();
      }
    }
  }
  private static boolean isValidEmail(CharSequence email) {
    return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
  }
}
