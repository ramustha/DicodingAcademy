package com.ramusthastudio.myloader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
  private static final String TAG = "ContactApp";
  private static final int CONTACT_LOAD_ID = 110;
  private static final int CONTACT_PHONE_ID = 120;
  private ListView fContactLv;
  private ProgressBar fProgressBar;
  private ContactAdapter fContactAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    fContactLv = findViewById(R.id.lv_contact);
    fProgressBar = findViewById(R.id.progress_bar);

    fContactLv.setVisibility(View.INVISIBLE);
    fProgressBar.setVisibility(View.VISIBLE);
    fContactAdapter = new ContactAdapter(MainActivity.this, null, true);
    fContactLv.setAdapter(fContactAdapter);
    fContactLv.setOnItemClickListener(this);
    getSupportLoaderManager().initLoader(CONTACT_LOAD_ID, null, this);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    CursorLoader mCursorLoader = null;
    if (id == CONTACT_LOAD_ID) {
      String[] projectionFields = new String[] {
          ContactsContract.Contacts._ID,
          ContactsContract.Contacts.DISPLAY_NAME,
          ContactsContract.Contacts.PHOTO_URI
      };
      mCursorLoader = new CursorLoader(MainActivity.this,
          ContactsContract.Contacts.CONTENT_URI,
          projectionFields,
          ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1",
          null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
    }
    if (id == CONTACT_PHONE_ID) {
      String[] phoneProjectionFields = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER};
      mCursorLoader = new CursorLoader(MainActivity.this,
          ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
          phoneProjectionFields,
          ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
              ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
              ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE + " AND " +
              ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + "=1",
          new String[] {args.getString("id")},
          null);
    }
    return mCursorLoader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    Log.d(TAG, "LoadFinished");
    if (loader.getId() == CONTACT_LOAD_ID) {
      if (data.getCount() > 0) {
        fContactLv.setVisibility(View.VISIBLE);
        fProgressBar.setVisibility(View.GONE);
        fContactAdapter.swapCursor(data);
      }
    }
    if (loader.getId() == CONTACT_PHONE_ID) {
      String contactNumber = null;
      if (data.moveToFirst()) {
        contactNumber = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
      }
      Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactNumber));
      startActivity(dialIntent);
    }
  }
  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    if (loader.getId() == CONTACT_LOAD_ID) {
      fContactLv.setVisibility(View.VISIBLE);
      fProgressBar.setVisibility(View.GONE);
      fContactAdapter.swapCursor(null);
      Log.d(TAG, "LoaderReset");
    }
  }
  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

  }
}
