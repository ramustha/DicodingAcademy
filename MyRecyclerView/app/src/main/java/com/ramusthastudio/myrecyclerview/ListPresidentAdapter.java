package com.ramusthastudio.myrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class ListPresidentAdapter extends RecyclerView.Adapter<ListPresidentAdapter.CategoryViewHolder> {
  private final Context context;
  private ArrayList<President> listPresident;

  public ListPresidentAdapter(Context context) {
    this.context = context;
  }

  @Override
  public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_president, parent, false);
    return new CategoryViewHolder(itemRow);
  }

  @Override
  public void onBindViewHolder(CategoryViewHolder holder, int position) {
    holder.nameTv.setText(getPresident(position).getName());
    holder.remarksTv.setText(getPresident(position).getRemarks());

    Glide.with(context)
        .load(getPresident(position).getPhoto())
        .override(55, 55)
        .crossFade()
        .into(holder.photoImage);
  }

  @Override public int getItemCount() { return listPresident != null ? listPresident.size() : 0; }
  President getPresident(int aPos) { return listPresident.get(aPos); }

  void setListPresident(ArrayList<President> listPresident) {
    this.listPresident = listPresident;
  }

  class CategoryViewHolder extends RecyclerView.ViewHolder {
    TextView nameTv;
    TextView remarksTv;
    ImageView photoImage;

    CategoryViewHolder(View itemView) {
      super(itemView);
      nameTv = itemView.findViewById(R.id.tv_item_name);
      remarksTv = itemView.findViewById(R.id.tv_item_remarks);
      photoImage = itemView.findViewById(R.id.img_item_photo);
    }
  }
}

