package com.ramusthastudio.kamus;

import com.ramusthastudio.kamus.repository.RepositoryService;

public class Application extends android.app.Application {
  @Override public void onCreate() {
    super.onCreate();
    KamusPrefs.init(this);
    RepositoryService.init(this);
  }
}
