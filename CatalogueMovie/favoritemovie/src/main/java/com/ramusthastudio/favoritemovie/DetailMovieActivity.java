package com.ramusthastudio.favoritemovie;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ramusthastudio.favoritemovie.provider.DatabaseContract;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.BACKDROP;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.GENRE;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.OVERVIEW;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.POPULARITY;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.POSTER;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.RATING;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.ramusthastudio.favoritemovie.provider.DatabaseContract.MovieColumns.TITLE;

public class DetailMovieActivity extends AppCompatActivity {
  private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("E, dd-MM-yyyy", Locale.getDefault());
  private ImageView fItemImageView;
  private TextView fItemTitleView;
  private TextView fItemDateView;
  private TextView fItemGenreView;
  private TextView fItemRatingView;
  private TextView fItemDescView;
  private TextView fItemPopularityView;
  private AppBarLayout fAppBarView;
  private CollapsingToolbarLayout fCollapsingToolbarView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_movie);

    setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    fItemImageView = findViewById(R.id.detailImage);
    fItemTitleView = findViewById(R.id.detailTitle);
    fItemDateView = findViewById(R.id.detailDate);
    fItemGenreView = findViewById(R.id.detailGenre);
    fItemRatingView = findViewById(R.id.detailRating);
    fItemDescView = findViewById(R.id.detailDesc);
    fItemPopularityView = findViewById(R.id.detailPopularity);
    fAppBarView = findViewById(R.id.detailAppBar);
    fCollapsingToolbarView = findViewById(R.id.detailImageCollapsingToolbar);
    fCollapsingToolbarView.setExpandedTitleColor(Color.parseColor("#00FFFFFF"));

    final Uri uri = getIntent().getData();
    if (uri != null) {
      final Cursor cursor = getContentResolver()
          .query(uri, null, null, null, null);

      if (cursor != null && cursor.moveToFirst()) {
        final String title = DatabaseContract.getColumnString(cursor, TITLE);
        final String poster = DatabaseContract.getColumnString(cursor, POSTER);
        final String backdrop = DatabaseContract.getColumnString(cursor, BACKDROP);
        final long releaseDate = DatabaseContract.getColumnLong(cursor, RELEASE_DATE);
        final String genre = DatabaseContract.getColumnString(cursor, GENRE);
        final double rating = DatabaseContract.getColumnDouble(cursor, RATING);
        final String overview = DatabaseContract.getColumnString(cursor, OVERVIEW);
        final float popularity = DatabaseContract.getColumnFloat(cursor, POPULARITY);

        if (backdrop != null) {
          Glide
              .with(this)
              .load(BuildConfig.IMAGE_URL + "/w500" + backdrop)
              .into(fItemImageView)
              .clearOnDetach();
        }

        fCollapsingToolbarView.setTitle(title);
        fItemTitleView.setText(title);

        if (releaseDate != 0) {
          fItemDateView.setText(sDateFormat.format(releaseDate));
        } else {
          fItemDateView.setText("Unknown");
        }
        fItemPopularityView.setText(String.valueOf(popularity));

        StringBuilder b = new StringBuilder();
        final String[] genreIds = genre.split(" ");
        for (String genreId : genreIds) {
          b.append(Genre.displayName(Integer.parseInt(genreId))).append(" ");
        }
        fItemGenreView.setText(b.toString());
        fItemRatingView.setText(String.valueOf(rating));
        fItemDescView.setText(String.valueOf(overview));
      }

      if (cursor != null) {
        cursor.close();
      }
    }
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case android.R.id.home:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
