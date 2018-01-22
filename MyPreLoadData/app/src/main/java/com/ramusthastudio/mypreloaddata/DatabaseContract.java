package com.ramusthastudio.mypreloaddata;

import android.provider.BaseColumns;

final class DatabaseContract {
  static final String TABLE_NAME = "table_mahasiswa";

  private DatabaseContract() {}

  static final class MahasiswaColumns implements BaseColumns {
    // Mahasiswa nama
    static final String NAMA = "nama";
    // Mahasiswa nim
    static final String NIM = "nim";

  }
}
