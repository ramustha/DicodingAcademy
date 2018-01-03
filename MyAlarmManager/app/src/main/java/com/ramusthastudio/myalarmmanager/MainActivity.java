package com.ramusthastudio.myalarmmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private static final SimpleDateFormat DATE_FORMAT_1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
  private static final SimpleDateFormat DATE_FORMAT_2 = new SimpleDateFormat("HH:mm", Locale.getDefault());
  private static final SimpleDateFormat DATE_FORMAT_3 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
  private TextView fOneTimeDateTv;
  private TextView fOneTimeTimeTv;
  private TextView fRepeatingTimeTv;
  private EditText fOneTimeMessageEt;
  private EditText fRepeatingMessageEt;
  private Button fOneTimeDateBtn;
  private Button fOneTimeTimeBtn;
  private Button fOneTimeBtn;
  private Button fRepeatingTimeBtn;
  private Button fRepeatingBtn;
  private Button fCancelRepeatingAlarmBtn;
  private AlarmReceiver fAlarmReceiver;
  private SimpleDateFormat fDateFormat = DATE_FORMAT_1;
  private SimpleDateFormat fTimeFormat = DATE_FORMAT_2;
  private SimpleDateFormat fDateTimeFormat = DATE_FORMAT_3;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    AlarmPreference.create(this);

    fOneTimeDateTv = findViewById(R.id.tv_one_time_alarm_date);
    fOneTimeTimeTv = findViewById(R.id.tv_one_time_alarm_time);
    fOneTimeMessageEt = findViewById(R.id.edt_one_time_alarm_message);
    fOneTimeDateBtn = findViewById(R.id.btn_one_time_alarm_date);
    fOneTimeTimeBtn = findViewById(R.id.btn_one_time_alarm_time);
    fOneTimeBtn = findViewById(R.id.btn_set_one_time_alarm);
    fRepeatingTimeTv = findViewById(R.id.tv_repeating_alarm_time);
    fRepeatingMessageEt = findViewById(R.id.edt_repeating_alarm_message);
    fRepeatingTimeBtn = findViewById(R.id.btn_repeating_time_alarm_time);
    fRepeatingBtn = findViewById(R.id.btn_repeating_time_alarm);
    fCancelRepeatingAlarmBtn = findViewById(R.id.btn_cancel_repeating_alarm);

    fOneTimeDateBtn.setOnClickListener(this);
    fOneTimeTimeBtn.setOnClickListener(this);
    fOneTimeBtn.setOnClickListener(this);
    fRepeatingTimeBtn.setOnClickListener(this);
    fRepeatingBtn.setOnClickListener(this);
    fCancelRepeatingAlarmBtn.setOnClickListener(this);

    fAlarmReceiver = new AlarmReceiver();

    if (AlarmPreference.getOneTimeDate() != 0) {
      setOneTimeText();
    }

    if (AlarmPreference.getRepeatingTime() != 0) {
      setRepeatingTimeText();
    }
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.btn_one_time_alarm_date) {
      datePickerDialog().show();
    } else if (v.getId() == R.id.btn_one_time_alarm_time) {
      timePickerDialog().show();
    } else if (v.getId() == R.id.btn_repeating_time_alarm_time) {
      repeatingTimePickerDialog().show();
    } else if (v.getId() == R.id.btn_set_one_time_alarm) {

      String oneTimeDate = fDateFormat.format(new Date(AlarmPreference.getOneTimeDate()));
      String oneTimeTime = fTimeFormat.format(new Date(AlarmPreference.getOneTimeTime()));
      String oneTimeMessage = fOneTimeMessageEt.getText().toString();
      AlarmPreference.setOneTimeMessage(oneTimeMessage);

      try {
        Date dateTime = fDateTimeFormat.parse(oneTimeDate + " " + oneTimeTime);

        Log.d("Debug", "############# " + dateTime);
        fAlarmReceiver.setOneTimeAlarm(this, dateTime.getTime(), oneTimeMessage);
      } catch (ParseException aE) {
        aE.printStackTrace();
      }
    } else if (v.getId() == R.id.btn_repeating_time_alarm) {

      String repeatTimeMessage = fRepeatingMessageEt.getText().toString();
      AlarmPreference.setRepeatingMessage(repeatTimeMessage);

      fAlarmReceiver.setRepeatingAlarm(this, AlarmPreference.getRepeatingTime(), repeatTimeMessage);

    } else if (v.getId() == R.id.btn_cancel_repeating_alarm) {
      fAlarmReceiver.cancelAlarm(this);
    }
  }

  private DatePickerDialog datePickerDialog() {
    final Calendar currentDate = Calendar.getInstance();
    return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        fOneTimeDateTv.setText(fDateFormat.format(calendar.getTime()));

        AlarmPreference.setOneTimeDate(calendar.getTimeInMillis());
      }
    }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
  }

  private TimePickerDialog timePickerDialog() {
    final Calendar currentDate = Calendar.getInstance();
    return new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
      @Override
      public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        fOneTimeTimeTv.setText(fTimeFormat.format(calendar.getTime()));

        AlarmPreference.setOneTimeTime(calendar.getTimeInMillis());
      }
    }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true);
  }

  private TimePickerDialog repeatingTimePickerDialog() {
    final Calendar currentDate = Calendar.getInstance();
    return new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
      @Override
      public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        fRepeatingTimeTv.setText(fTimeFormat.format(calendar.getTime()));

        AlarmPreference.setRepeatingTime(calendar.getTimeInMillis());
      }
    }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true);
  }

  private void setOneTimeText() {
    fOneTimeDateTv.setText(fDateFormat.format(new Date(AlarmPreference.getOneTimeDate())));
    fOneTimeTimeTv.setText(fTimeFormat.format(new Date(AlarmPreference.getOneTimeTime())));
    fOneTimeMessageEt.setText(AlarmPreference.getOneTimeMessage());
  }

  private void setRepeatingTimeText() {
    fRepeatingTimeTv.setText(fTimeFormat.format(new Date(AlarmPreference.getRepeatingTime())));
    fRepeatingMessageEt.setText(AlarmPreference.getRepeatingMessage());
  }
}
