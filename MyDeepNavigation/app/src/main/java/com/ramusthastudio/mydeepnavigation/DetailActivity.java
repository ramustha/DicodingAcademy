package com.ramusthastudio.mydeepnavigation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
  public static final String EXTRA_TITLE = "extra_title";
  public static final String EXTRA_MESSAGE = "extra_message";
  private TextView fTitleTv;
  private TextView fMessageTv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    fTitleTv = findViewById(R.id.tv_title);
    fMessageTv = findViewById(R.id.tv_message);

    String title = getIntent().getStringExtra(EXTRA_TITLE);
    String message = getIntent().getStringExtra(EXTRA_MESSAGE);

    fTitleTv.setText(title);
    fMessageTv.setText(message);
  }
}
