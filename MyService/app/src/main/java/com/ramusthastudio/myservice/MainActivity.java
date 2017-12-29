package com.ramusthastudio.myservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private Button fStartServiceBtn;
  private Button fStartIntentService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    fStartServiceBtn = findViewById(R.id.btn_start_service);
    fStartIntentService = findViewById(R.id.btn_start_intent_service);

    fStartServiceBtn.setOnClickListener(this);
    fStartIntentService.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_start_service:
        Intent service = new Intent(MainActivity.this, OriginService.class);
        startService(service);
        break;
      case R.id.btn_start_intent_service:
        Intent intentService = new Intent(MainActivity.this, DicodingIntentService.class);
        intentService.putExtra(DicodingIntentService.EXTRA_DURATION, 5000);
        startService(intentService);
        break;
    }
  }
}
