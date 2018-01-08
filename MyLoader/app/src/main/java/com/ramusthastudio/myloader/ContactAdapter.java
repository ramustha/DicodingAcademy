package com.ramusthastudio.myloader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends CursorAdapter {

  public ContactAdapter(Context context, Cursor c, boolean autoRequery) {
    super(context, c, autoRequery);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(R.layout.item_row_contact, parent, false);
  }
  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    if (cursor != null) {
      TextView nameTv = view.findViewById(R.id.tv_item_name);
      CircleImageView userIv = view.findViewById(R.id.img_item_user);

      userIv.setImageResource(R.drawable.ic_person_pin);

      nameTv.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
      if (cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI)) != null) {
        userIv.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI))));
      } else {
        userIv.setImageResource(R.drawable.ic_person_pin);
      }
    }
  }
}
