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

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public static final SimpleDateFormat sDateFormat = new SimpleDateFormat("E, dd-MM-yyyy", Locale.getDefault());
  private static final int EMPTY_ITEM = 0;
  private static final int MOVIE_ITEM = 1;
  private final Context fContext;
  private List<Result> fMovieList;
  private AdapterListener fClickListener;

  public MovieListAdapter(Context aContext) {
    fContext = aContext;
  }

  @Override public int getItemCount() { return fMovieList == null ? 0 : fMovieList.size(); }

  @Override
  public int getItemViewType(final int position) {
    if (fMovieList == null) {
      return EMPTY_ITEM;
    }
    return fMovieList.get(position) != null ? MOVIE_ITEM : EMPTY_ITEM;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == EMPTY_ITEM) {
      View view = LayoutInflater.from(fContext).inflate(R.layout.movie_empty_item, parent, false);
      return new EmptyListholder(view);
    }
    View view = LayoutInflater.from(fContext).inflate(R.layout.movie_list_item, parent, false);
    return new MovieListHolder(view);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    final int viewType = getItemViewType(position);
    if (viewType == EMPTY_ITEM) {
      EmptyListholder mh = (EmptyListholder) holder;
    } else if (viewType == MOVIE_ITEM) {
      MovieListHolder mh = (MovieListHolder) holder;
      Result movie = fMovieList.get(position);
      mh.bind(position, movie);
    }
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
      notifyItemRangeInserted(fMovieList.size() - aMovieList.size(), fMovieList.size() - 1);
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
    void onClickShare(Result aResult);
  }

  class EmptyListholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public EmptyListholder(final View itemView) {
      super(itemView);

    }

    @Override
    public void onClick(final View v) {

    }
  }

  class MovieListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ImageView itemImageView;
    private final TextView itemTitleView;
    private final TextView itemDateView;
    private final TextView itemRatingView;
    private final ImageView itemShareBtn;

    public MovieListHolder(View itemView) {
      super(itemView);

      itemImageView = itemView.findViewById(R.id.itemImage);
      itemTitleView = itemView.findViewById(R.id.itemTitle);
      itemDateView = itemView.findViewById(R.id.itemDate);
      itemRatingView = itemView.findViewById(R.id.itemRating);
      itemShareBtn = itemView.findViewById(R.id.itemShare);

      itemShareBtn.setOnClickListener(this);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.itemShare:
          fClickListener.onClickShare(fMovieList.get(getLayoutPosition()));
          break;
        default:
          fClickListener.onClick(fMovieList.get(getLayoutPosition()));
          break;
      }
    }

    private void bind(final int aPosition, Result aMovie) {
      Glide
          .with(fContext)
          .load(BuildConfig.IMAGE_URL + "/w342" + aMovie.getPosterPath())
          .into(itemImageView);

      itemTitleView.setText(aMovie.getTitle());

      if (aMovie.getReleaseDate() != null) {
        itemDateView.setText(sDateFormat.format(aMovie.getReleaseDate()));
      } else {
        itemDateView.setText(fContext.getString(R.string.unknown_string));
      }
      // itemDateView.setText(aMovie.getReleaseDate());
      itemRatingView.setText(String.valueOf(aMovie.getVoteAverage()));
    }
  }
}
