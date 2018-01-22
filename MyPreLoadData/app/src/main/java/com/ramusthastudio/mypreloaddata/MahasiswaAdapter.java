package com.ramusthastudio.mypreloaddata;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaHolder> {
  private List<MahasiswaModel> mData = new ArrayList<>();
  private final Context context;
  private final LayoutInflater mInflater;

  public MahasiswaAdapter(Context context) {
    this.context = context;
    mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public MahasiswaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mahasiswa_row, parent, false);
    return new MahasiswaHolder(view);
  }

  public void addItem(List<MahasiswaModel> mData) {
    this.mData = mData;
    notifyDataSetChanged();
  }

  @Override
  public void onBindViewHolder(MahasiswaHolder holder, int position) {
    holder.textViewNim.setText(mData.get(position).getNim());
    holder.textViewNama.setText(mData.get(position).getName());
  }

  @Override
  public int getItemViewType(int position) {
    return 0;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  protected static class MahasiswaHolder extends RecyclerView.ViewHolder {
    protected TextView textViewNim;
    protected TextView textViewNama;
    public MahasiswaHolder(View itemView) {
      super(itemView);
      textViewNim = itemView.findViewById(R.id.txt_nim);
      textViewNama = itemView.findViewById(R.id.txt_nama);
    }
  }
}
