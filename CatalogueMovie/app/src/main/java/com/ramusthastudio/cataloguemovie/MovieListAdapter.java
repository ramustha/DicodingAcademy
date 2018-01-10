package com.ramusthastudio.cataloguemovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ramusthastudio.cataloguemovie.model.Result;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListHolder> {
  private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("E, dd-MM-yyyy", Locale.getDefault());
  private final Context fContext;
  private List<Result> fMovieList;
  private AdapterListener fClickListener;

  public MovieListAdapter(Context aContext) {
    fContext = aContext;
  }

  @Override public int getItemCount() { return fMovieList == null ? 0 : fMovieList.size(); }

  @Override
  public MovieListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(fContext).inflate(R.layout.movie_item, parent, false);
    return new MovieListHolder(view);
  }

  @Override
  public void onBindViewHolder(MovieListHolder holder, int position) {
    Result movie = fMovieList.get(position);
    holder.bind(movie);
  }

  public void setOnClickListener(AdapterListener aClickListener) {
    fClickListener = aClickListener;
  }

  public MovieListAdapter setMovieList(List<Result> aMovieList) {
    if (fMovieList != null) {
      addMovieList(aMovieList);
    } else {
      fMovieList = aMovieList;
      notifyDataSetChanged();
    }
    return this;
  }

  public MovieListAdapter addMovieList(List<Result> aMovieList) {
    if (fMovieList != null) {
      fMovieList.addAll(aMovieList);
      notifyItemRangeChanged(fMovieList.size() - aMovieList.size(), fMovieList.size() - 1);
    }
    return this;
  }

  public MovieListAdapter clear() {
    if (fMovieList != null) {
      fMovieList.clear();
      fMovieList = null;
      notifyDataSetChanged();
    }
    return this;
  }

  interface AdapterListener {
    void onClick(Result aResult);
  }

  class MovieListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ImageView itemImageView;
    private final TextView itemTitleView;
    private final TextView itemDateView;
    private final TextView itemRatingView;

    public MovieListHolder(View itemView) {
      super(itemView);

      itemImageView = itemView.findViewById(R.id.itemImage);
      itemTitleView = itemView.findViewById(R.id.itemTitle);
      itemDateView = itemView.findViewById(R.id.itemDate);
      itemRatingView = itemView.findViewById(R.id.itemRating);

      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      fClickListener.onClick(fMovieList.get(getLayoutPosition()));
    }

    private void bind(Result aMovie) {
      Glide
          .with(fContext)
          .load(BuildConfig.IMAGE_URL + "/w342" + aMovie.getPosterPath())
          .into(itemImageView)
          .clearOnDetach();

      itemTitleView.setText(aMovie.getTitle());

      if (aMovie.getReleaseDate() != null) {
        itemDateView.setText(sDateFormat.format(aMovie.getReleaseDate()));
      } else {
        itemDateView.setText("Unknown");
      }
      // itemDateView.setText(aMovie.getReleaseDate());
      itemRatingView.setText(String.valueOf(aMovie.getVoteAverage()));
    }
  }
}
