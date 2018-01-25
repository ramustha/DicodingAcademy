package com.ramusthastudio.dicodingnotesapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.ramusthastudio.dicodingnotesapp.DatabaseContract.NoteColumns.DATE;
import static com.ramusthastudio.dicodingnotesapp.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.ramusthastudio.dicodingnotesapp.DatabaseContract.NoteColumns.TITLE;
import static com.ramusthastudio.dicodingnotesapp.DatabaseContract.getColumnString;

public class DicodingNotesAdapter extends CursorAdapter {

  public DicodingNotesAdapter(Context context, Cursor c, boolean autoRequery) {
    super(context, c, autoRequery);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_note, viewGroup, false);
    return view;
  }

  @Override
  public Cursor getCursor() {
    return super.getCursor();
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    if (cursor != null) {
      TextView tvTitle = view.findViewById(R.id.tv_item_title);
      TextView tvDate = view.findViewById(R.id.tv_item_date);
      TextView tvDescription = view.findViewById(R.id.tv_item_description);

      tvTitle.setText(getColumnString(cursor, TITLE));
      tvDescription.setText(getColumnString(cursor, DESCRIPTION));
      tvDate.setText(getColumnString(cursor, DATE));
    }
  }
}
