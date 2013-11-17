package com.soboapps.todos.fakedatabase;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FakeUserPrefsTable {

  // Database table
  public static final String TABLE_FAKEUSERPREFS = "fakeuserprefs";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_PASS = "pass";
  
  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table " 
      + TABLE_FAKEUSERPREFS
      + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_PASS 
      + " text not null" 
      + ");";
  
  public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(FakeTodoTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + TABLE_FAKEUSERPREFS);
    onCreate(database);
  }
} 