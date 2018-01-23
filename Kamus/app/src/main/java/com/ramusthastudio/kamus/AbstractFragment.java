package com.ramusthastudio.kamus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import com.ramusthastudio.kamus.model.Kamus;
import com.ramusthastudio.kamus.repository.RepositoryService;
import java.util.List;

public abstract class AbstractFragment extends Fragment implements View.OnClickListener {
  private AutoCompleteTextView fSearchTextView;
  private Button fSearchButtonView;
  private TextView fWordSeacrhView;
  private TextView fDescSeacrhView;
  private ArrayAdapter<Kamus> fSearchSuggestAdapter;

  public AbstractFragment() {}

  @Override
  public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.kamus_fragment, container, false);

    fSearchTextView = view.findViewById(R.id.search_text_view);
    fSearchButtonView = view.findViewById(R.id.search_text_button);
    fWordSeacrhView = view.findViewById(R.id.word_search);
    fDescSeacrhView = view.findViewById(R.id.desc_search);

    fSearchButtonView.setOnClickListener(this);

    fSearchSuggestAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line);
    fSearchTextView.setAdapter(fSearchSuggestAdapter);

    fSearchTextView.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) { }
      @Override public void afterTextChanged(final Editable s) { }
      @Override
      public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        if (start > 1) {
          fSearchSuggestAdapter.clear();
          fSearchSuggestAdapter.addAll(queryByWordLike(s.toString()));
          fSearchSuggestAdapter.notifyDataSetChanged();
        }
      }
    });

    fSearchTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        final Kamus kamus = fSearchSuggestAdapter.getItem(position);
        if (kamus != null) {
          fWordSeacrhView.setText(kamus.getWord());
          fDescSeacrhView.setText(kamus.getDescription());
        }
      }
    });
    return view;
  }

  @Override
  public void onClick(final View v) {
    switch (v.getId()) {
      case R.id.search_text_button:
        final String searchText = fSearchTextView.getText().toString();
        final List<Kamus> kamusList = queryByWord(searchText);
        if (!kamusList.isEmpty()) {
          final Kamus kamus = kamusList.get(0);
          fWordSeacrhView.setText(kamus.getWord().toUpperCase());
          fDescSeacrhView.setText(kamus.getDescription());
        } else {
          fWordSeacrhView.setText(null);
          fDescSeacrhView.setText(null);
        }
        break;
    }
  }

  abstract List<Kamus> queryByWord(String aQuery);
  abstract List<Kamus> queryByWordLike(String aQuery);
}
