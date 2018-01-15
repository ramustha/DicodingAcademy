package com.ramusthastudio.myrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class GridPresidentAdapter extends RecyclerView.Adapter<GridPresidentAdapter.GridViewHolder> {
  private final Context context;
  private ArrayList<President> listPresident;

  GridPresidentAdapter(Context context) {
    this.context = context;
  }

  @Override
  public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_president, parent, false);
    return new GridViewHolder(view);
  }

  @Override
  public void onBindViewHolder(GridViewHolder holder, int position) {
    Glide.with(context)
        .load(getPresident(position).getPhoto())
        .override(350, 550)
        .into(holder.imgPhoto);
  }

  @Override public int getItemCount() { return listPresident != null ? listPresident.size() : 0; }
  private President getPresident(int aPos) { return listPresident.get(aPos); }

  void setListPresident(ArrayList<President> listPresident) {
    this.listPresident = listPresident;
  }

  class GridViewHolder extends RecyclerView.ViewHolder {
    ImageView imgPhoto;
    GridViewHolder(View itemView) {
      super(itemView);
      imgPhoto = itemView.findViewById(R.id.img_item_photo);
    }
  }
}
