package com.ramusthastudio.favoritemovie;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ramusthastudio.favoritemovie.provider.DatabaseContract;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.provider.BaseColumns._ID;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.CONTENT_URI;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.BACKDROP;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.GENRE;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.OVERVIEW;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.POPULARITY;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.POSTER;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.RATING;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.TITLE;

public class MovieListAdapter extends CursorAdapter implements View.OnClickListener {
  public static final SimpleDateFormat sDateFormat = new SimpleDateFormat("E, dd-MM-yyyy", Locale.getDefault());
  private final AdapterListener fClickListener;

  public MovieListAdapter(final Context context, final Cursor aCursor, AdapterListener aClickListener) {
    super(context, aCursor, true);
    fClickListener = aClickListener;
  }

  @Override
  public View newView(final Context context, final Cursor cursor, final ViewGroup viewGroup) {
    final View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, viewGroup, false);
    view.findViewById(R.id.itemDelete).setOnClickListener(this);
    view.setOnClickListener(this);
    return view;
  }

  @Override
  public Cursor getCursor() {
    return super.getCursor();
  }

  @Override
  public void bindView(final View aView, final Context aContext, final Cursor aCursor) {
    if (aCursor != null) {
      ImageView itemImageView = aView.findViewById(R.id.itemImage);
      TextView itemTitleView = aView.findViewById(R.id.itemTitle);
      TextView itemDateView = aView.findViewById(R.id.itemDate);
      TextView itemRatingView = aView.findViewById(R.id.itemRating);
      ImageView itemShareBtn = aView.findViewById(R.id.itemDelete);
      aView.setTag(aCursor.getPosition());
      itemShareBtn.setTag(aCursor.getPosition());

      final int dbId = DatabaseContract.getColumnInt(aCursor, _ID);
      final String title = DatabaseContract.getColumnString(aCursor, TITLE);
      final String poster = DatabaseContract.getColumnString(aCursor, POSTER);
      final String backdrop = DatabaseContract.getColumnString(aCursor, BACKDROP);
      final long releaseDate = DatabaseContract.getColumnLong(aCursor, RELEASE_DATE);
      final String genre = DatabaseContract.getColumnString(aCursor, GENRE);
      final double rating = DatabaseContract.getColumnDouble(aCursor, RATING);
      final String overview = DatabaseContract.getColumnString(aCursor, OVERVIEW);
      final float popularity = DatabaseContract.getColumnFloat(aCursor, POPULARITY);

      final String[] splited = genre.split(" ");
      List<Integer> b = new ArrayList<>();
      for (final String s : splited) {
        b.add(Integer.valueOf(s));
      }

      if (backdrop != null) {
        Glide
            .with(aContext)
            .load(BuildConfig.IMAGE_URL + "/w500" + backdrop)
            .into(itemImageView);
      }

      itemTitleView.setText(title);

      if (releaseDate != 0) {
        itemDateView.setText(sDateFormat.format(releaseDate));
      } else {
        itemDateView.setText(aContext.getString(R.string.unknown_string));
      }
      itemRatingView.setText(String.valueOf(rating));

    }
  }

  @Override
  public void onClick(final View v) {
    final Cursor cursor = getCursor();
    if (cursor.moveToPosition((int) v.getTag())) {
      Uri uri = Uri.parse(CONTENT_URI + "/" + DatabaseContract.getColumnInt(cursor, _ID));
      switch (v.getId()) {
        case R.id.itemDelete:
          fClickListener.onClickDelete(uri);
          break;
        default:
          fClickListener.onClick(uri);
          break;
      }
    }
  }

  interface AdapterListener {
    void onClick(Uri aUri);
    void onClickDelete(Uri aUri);
  }
}
