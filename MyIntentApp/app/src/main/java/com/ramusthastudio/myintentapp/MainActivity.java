package com.ramusthastudio.myintentapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
  protected static String EXTRA_AGE = "extra_age";
  protected static String EXTRA_NAME = "extra_name";
  protected static String EXTRA_PERSON = "extra_person";
  private Button fChangeActivityBtn;
  private Button fChangeActivityWithDataBtn;
  private Button fChangeActivityWithOjectBtn;
  private Button fDialNumberBtn;
  private Button fChangeActivityWithResultBtn;
  private TextView fActivityResultTv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fChangeActivityBtn = findViewById(R.id.changeActivity);
    fChangeActivityWithDataBtn = findViewById(R.id.changeActivityWithData);
    fChangeActivityWithOjectBtn = findViewById(R.id.changeActivityWithObject);
    fDialNumberBtn = findViewById(R.id.dialNumber);
    fChangeActivityWithResultBtn = findViewById(R.id.changeActivityWithResult);
    fActivityResultTv = findViewById(R.id.activityResult);

    fChangeActivityBtn.setOnClickListener(fButtonListener);
    fChangeActivityWithDataBtn.setOnClickListener(fButtonListener);
    fChangeActivityWithOjectBtn.setOnClickListener(fButtonListener);
    fDialNumberBtn.setOnClickListener(fButtonListener);
    fChangeActivityWithResultBtn.setOnClickListener(fButtonListener);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == MoveActivity.RESULT_CODE) {
      if (resultCode == RESULT_OK) {
        int selectedValue = data.getIntExtra(MoveActivity.EXTRA_SELECTED_VALUE, 0);
        fActivityResultTv.setText(String.format("Hasil : %s", selectedValue));
      }
    }
  }

  private final View.OnClickListener fButtonListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      final Intent intent = new Intent(MainActivity.this, MoveActivity.class);

      switch (v.getId()) {
        case R.id.changeActivity:
          startActivity(intent);
          break;
        case R.id.changeActivityWithData:
          intent.putExtra(EXTRA_NAME, "Ramustha");
          intent.putExtra(EXTRA_AGE, 26);
          startActivity(intent);
          break;
        case R.id.changeActivityWithObject:
          intent.putExtra(EXTRA_PERSON,
              new Person()
                  .setName("Ramustha")
                  .setAge(26)
                  .setEmail("ramusthastudio@gmail.com")
                  .setCity("Bandung")
          );
          startActivity(intent);
          break;
        case R.id.dialNumber:
          String phoneNumber = "089620785945";
          startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
          break;
        case R.id.changeActivityWithResult:
          startActivityForResult(intent, MoveActivity.RESULT_CODE);
          break;
        default:
          break;
      }
    }
  };
}
