package com.ramusthastudio.mysoundpool;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private Button fSoundBtn;
  private Button fMediaPlayBtn;
  private Button fMediaStopBtn;
  private SoundPool sp;
  private int wav;
  private boolean spLoaded = false;
  private Intent it;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fSoundBtn = findViewById(R.id.btn_soundpool);
    fMediaPlayBtn = findViewById(R.id.btn_mediaplayer);
    fMediaStopBtn = findViewById(R.id.btn_mediaplayer_stop);

    fSoundBtn.setOnClickListener(this);
    fMediaPlayBtn.setOnClickListener(this);
    fMediaStopBtn.setOnClickListener(this);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      sp = new SoundPool.Builder()
          .setMaxStreams(10)
          .build();
    } else {
      sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
    }

    sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
      @Override
      public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        spLoaded = true;
      }
    });

    wav = sp.load(this, R.raw.sound, 1);

    it = new Intent(this, MediaService.class);
    it.setAction(MediaService.ACTION_CREATE);
    it.setPackage(MediaService.ACTION_PACKAGE);
    startService(it);
  }

  @Override
  public void onClick(final View v) {
    switch (v.getId()) {
      case R.id.btn_soundpool:
        if (spLoaded) {
          sp.play(wav, 1, 1, 0, 0, 1);
        }
        break;
      case R.id.btn_mediaplayer:
        it.setAction(MediaService.ACTION_PLAY);
        it.setPackage(MediaService.ACTION_PACKAGE);
        startService(it);
        break;
      case R.id.btn_mediaplayer_stop:
        it.setAction(MediaService.ACTION_STOP);
        it.setPackage(MediaService.ACTION_PACKAGE);
        startService(it);
        break;
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    stopService(it);
  }
}
