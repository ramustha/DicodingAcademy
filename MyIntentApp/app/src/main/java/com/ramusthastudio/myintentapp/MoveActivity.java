package com.ramusthastudio.myintentapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MoveActivity extends AppCompatActivity {
  protected static final int RESULT_CODE = 0;
  protected static final String EXTRA_SELECTED_VALUE = "extra_selected_value";
  private TextView fExtrasTv;
  private RadioGroup fNumberGroup;
  private Button fChooseBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_move);

    fNumberGroup = findViewById(R.id.numberGroup);
    fChooseBtn = findViewById(R.id.chooseBtn);
    fExtrasTv = findViewById(R.id.extras);

    fChooseBtn.setOnClickListener(fListener);

    if (getIntent().getStringExtra(MainActivity.EXTRA_NAME) != null) {
      String extraName = getIntent().getStringExtra(MainActivity.EXTRA_NAME);
      int extraAge = getIntent().getIntExtra(MainActivity.EXTRA_AGE, 0);
      fExtrasTv.setText(String.format("Name : %s, Your Age : %s", extraName, extraAge));
    } else if (getIntent().getParcelableExtra(MainActivity.EXTRA_PERSON) != null) {
      Person extraPerson = getIntent().getParcelableExtra(MainActivity.EXTRA_PERSON);
      fExtrasTv.setText(String.format("Name : %s, \nEmail: %s, \nAge : %s, \nLocation : %s"
          , extraPerson.getName(), extraPerson.getEmail(), extraPerson.getAge(), extraPerson.getCity()));
    }
  }

  private final View.OnClickListener fListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      if (v.getId() == R.id.chooseBtn) {
        if (fNumberGroup.getCheckedRadioButtonId() != 0) {
          int chooser = 0;
          switch (fNumberGroup.getCheckedRadioButtonId()) {
            case R.id.rb50:
              chooser = 50;
              break;
            case R.id.rb100:
              chooser = 100;
              break;
            case R.id.rb150:
              chooser = 150;
              break;
            case R.id.rb200:
              chooser = 200;
              break;
            default:
              chooser = 0;
              break;
          }

          Intent result = new Intent();
          result.putExtra(EXTRA_SELECTED_VALUE, chooser);
          setResult(RESULT_OK, result);
          finish();
        }
      }
    }
  };
}
