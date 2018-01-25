package com.ramusthastudio.mynotesapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.ramusthastudio.mynotesapp.DatabaseContract.CONTENT_URI;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewholder> {
  private Cursor listNotes;
  private final Activity activity;

  public NoteAdapter(Activity activity) {
    this.activity = activity;
  }

  @Override public int getItemCount() { return listNotes == null ? 0 : listNotes.getCount(); }

  private Note getItem(int position) {
    if (!listNotes.moveToPosition(position)) {
      throw new IllegalStateException("Position invalid");
    }
    return new Note(listNotes);
  }

  public void setListNotes(Cursor listNotes) {
    this.listNotes = listNotes;
  }

  @Override
  public NoteViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
    return new NoteViewholder(view);
  }

  @Override
  public void onBindViewHolder(NoteViewholder holder, int position) {
    final Note note = getItem(position);

    holder.tvTitle.setText(note.getTitle());
    holder.tvDate.setText(note.getDate());
    holder.tvDescription.setText(note.getDescription());
    holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
      @Override
      public void onItemClicked(View view, int position) {
        Intent intent = new Intent(activity, FormAddUpdateActivity.class);
        Uri uri = Uri.parse(CONTENT_URI + "/" + note.getId());
        intent.setData(uri);
        activity.startActivityForResult(intent, FormAddUpdateActivity.REQUEST_UPDATE);
      }
    }));
  }

  protected class NoteViewholder extends RecyclerView.ViewHolder {
    protected TextView tvTitle;
    protected TextView tvDescription;
    protected TextView tvDate;
    protected CardView cvNote;

    public NoteViewholder(View itemView) {
      super(itemView);
      tvTitle = itemView.findViewById(R.id.tv_item_title);
      tvDescription = itemView.findViewById(R.id.tv_item_description);
      tvDate = itemView.findViewById(R.id.tv_item_date);
      cvNote = itemView.findViewById(R.id.cv_item_note);
    }
  }
}
