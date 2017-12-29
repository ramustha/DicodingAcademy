package com.ramusthastudio.smslistenerapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SmsReceiverActivity extends AppCompatActivity implements View.OnClickListener {
  private TextView fSmsFromTv;
  private TextView fSmsMessageTv;
  private Button fCloseTv;
  public static final String EXTRA_SMS_NO = "extra_sms_no";
  public static final String EXTRA_SMS_MESSAGE = "extra_sms_message";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sms_receiver);
    fSmsFromTv = findViewById(R.id.tv_no);
    fSmsMessageTv = findViewById(R.id.tv_message);
    fCloseTv = findViewById(R.id.btn_close);

    fCloseTv.setOnClickListener(this);

    String senderNo = getIntent().getStringExtra(EXTRA_SMS_NO);
    String senderMessage = getIntent().getStringExtra(EXTRA_SMS_MESSAGE);
    fSmsFromTv.setText(String.format("from : %s", senderNo));
    fSmsMessageTv.setText(senderMessage);
  }
  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.btn_close) {
      finish();
    }
  }
}
