package com.ramusthastudio.myreadwritefile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private Button fNewBtn;
  private Button fOpenBtn;
  private Button fSaveBtn;
  private EditText fEditTextEdt;
  private EditText ffEditTitleEdt;
  private File path;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fNewBtn = findViewById(R.id.button_new);
    fOpenBtn = findViewById(R.id.button_open);
    fSaveBtn = findViewById(R.id.button_save);
    fEditTextEdt = findViewById(R.id.editFile);
    ffEditTitleEdt = findViewById(R.id.editTitle);

    fNewBtn.setOnClickListener(this);
    fOpenBtn.setOnClickListener(this);
    fSaveBtn.setOnClickListener(this);
    path = getFilesDir();
  }

  @Override
  public void onClick(final View v) {
    switch (v.getId()) {
      case R.id.button_new:
        newFile();
        break;
      case R.id.button_open:
        openFile();
        break;
      case R.id.button_save:
        saveFile();
        break;
    }
  }

  public void newFile() {
    ffEditTitleEdt.setText("");
    fEditTextEdt.setText("");

    Toast.makeText(this, "Clearing file", Toast.LENGTH_SHORT).show();
  }

  public void openFile() {
    showList();
  }

  public void saveFile() {
    if (ffEditTitleEdt.getText().toString().isEmpty()) {

      Toast.makeText(this, "Title harus diisi terlebih dahulu", Toast.LENGTH_SHORT).show();
    } else {
      String title = ffEditTitleEdt.getText().toString();
      String text = fEditTextEdt.getText().toString();
      FileHelper.writeToFile(title, text, this);
      Toast.makeText(this, "Saving " + ffEditTitleEdt.getText().toString() + " file", Toast.LENGTH_SHORT).show();
    }
  }
  private void showList() {
    final ArrayList<String> arrayList = new ArrayList<>();
    arrayList.addAll(Arrays.asList(path.list()));
    final CharSequence[] items = arrayList.toArray(new CharSequence[arrayList.size()]);

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Pilih file yang diinginkan");
    builder.setItems(items, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int item) {
        // Do something with the selection
        loadData(items[item].toString());
      }
    });
    AlertDialog alert = builder.create();
    alert.show();
  }

  private void loadData(String title) {
    String text = FileHelper.readFromFile(this, title);
    ffEditTitleEdt.setText(title);
    fEditTextEdt.setText(text);
    Toast.makeText(this, "Loading " + title + " data", Toast.LENGTH_SHORT).show();
  }
}
