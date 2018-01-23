package com.ramusthastudio.kamus.repository;

import android.provider.BaseColumns;

final class KamusDatabaseContract {
  static final String DATABASE_NAME = "kamusdb";
  static final String TABLE_INDONESIA_ENGLISH = "table_kamus_indonesia_english";
  static final String TABLE_ENGLISH_INDONESIA = "table_kamus_english_indonesia";

  private KamusDatabaseContract() {}

  static final class KamusColumn implements BaseColumns {
    static final String WORD = "word";
    static final String DESCRIPTION = "description";
  }
}
