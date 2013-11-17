package com.soboapps.todos.fakedatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FakeUserPrefsDatabaseHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "fakeuserprefs.db";
  private static final int DATABASE_VERSION = 1;

  public FakeUserPrefsDatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  // Method is called during creation of the database
  @Override
  public void onCreate(SQLiteDatabase database) {
    FakeUserPrefsTable.onCreate(database);
  }

  // Method is called during an upgrade of the database,
  // e.g. if you increase the database version
  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
	  FakeUserPrefsTable.onUpgrade(database, oldVersion, newVersion);
  }
}