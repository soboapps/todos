package com.soboapps.todos;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.soboapps.todos.fakedatabase.FakeTodoTable;
import com.soboapps.todos.fakecontentprovider.MyFakeTodoContentProvider;

/*
 * TodoDetailActivity allows to enter a new todo item 
 * or to change an existing
 */
public class FakeTodoDetailActivity extends Activity {
  private Spinner mCategory;
  private EditText mTitleText;
  private EditText mBodyText;

  private Uri faketodoUri;

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.faketodo_edit);

    mCategory = (Spinner) findViewById(R.id.category);
    mTitleText = (EditText) findViewById(R.id.todo_edit_summary);
    mBodyText = (EditText) findViewById(R.id.todo_edit_description);
    Button confirmButton = (Button) findViewById(R.id.todo_edit_button);

    Bundle extras = getIntent().getExtras();

    // Check from the saved Instance
    faketodoUri = (bundle == null) ? null : (Uri) bundle
        .getParcelable(MyFakeTodoContentProvider.CONTENT_ITEM_TYPE);

    // Or passed from the other activity
    if (extras != null) {
      faketodoUri = extras
          .getParcelable(MyFakeTodoContentProvider.CONTENT_ITEM_TYPE);

      fillData(faketodoUri);
    }

    confirmButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        if (TextUtils.isEmpty(mTitleText.getText().toString())) {
          makeToast();
        } else {
          setResult(RESULT_OK);
          finish();
        }
      }

    });
  }

  private void fillData(Uri uri) {
    String[] projection = { FakeTodoTable.COLUMN_SUMMARY,
        FakeTodoTable.COLUMN_DESCRIPTION, FakeTodoTable.COLUMN_CATEGORY };
    Cursor cursor = getContentResolver().query(uri, projection, null, null,
        null);
    if (cursor != null) {
      cursor.moveToFirst();
      String category = cursor.getString(cursor
          .getColumnIndexOrThrow(FakeTodoTable.COLUMN_CATEGORY));

      for (int i = 0; i < mCategory.getCount(); i++) {

        String s = (String) mCategory.getItemAtPosition(i);
        if (s.equalsIgnoreCase(category)) {
          mCategory.setSelection(i);
        }
      }

      mTitleText.setText(cursor.getString(cursor
          .getColumnIndexOrThrow(FakeTodoTable.COLUMN_SUMMARY)));
      mBodyText.setText(cursor.getString(cursor
          .getColumnIndexOrThrow(FakeTodoTable.COLUMN_DESCRIPTION)));

      // Always close the cursor
      cursor.close();
    }
  }

  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    saveState();
    outState.putParcelable(MyFakeTodoContentProvider.CONTENT_ITEM_TYPE, faketodoUri);
  }

  @Override
  protected void onPause() {
    super.onPause();
    saveState();
  }

  private void saveState() {
    String category = (String) mCategory.getSelectedItem();
    String summary = mTitleText.getText().toString();
    String description = mBodyText.getText().toString();

    // Only save if either summary or description
    // is available

    if (description.length() == 0 && summary.length() == 0) {
      return;
    }

    ContentValues values = new ContentValues();
    values.put(FakeTodoTable.COLUMN_CATEGORY, category);
    values.put(FakeTodoTable.COLUMN_SUMMARY, summary);
    values.put(FakeTodoTable.COLUMN_DESCRIPTION, description);

    if (faketodoUri == null) {
      // New todo
      faketodoUri = getContentResolver().insert(MyFakeTodoContentProvider.CONTENT_URI, values);
    } else {
      // Update todo
      getContentResolver().update(faketodoUri, values, null, null);
    }
  }

  private void makeToast() {
    Toast.makeText(FakeTodoDetailActivity.this, "Please enter a Subject",
        Toast.LENGTH_LONG).show();
  }
} 