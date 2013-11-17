package com.soboapps.todos.fakedatabase;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FakeTodoTable {

  // Database table
  public static final String TABLE_FAKETODO = "faketodo";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_CATEGORY = "category";
  public static final String COLUMN_SUMMARY = "summary";
  public static final String COLUMN_DESCRIPTION = "description";

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table " 
      + TABLE_FAKETODO
      + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_CATEGORY + " text not null, " 
      + COLUMN_SUMMARY + " text not null," 
      + COLUMN_DESCRIPTION
      + " text not null" 
      + ");";

  private static final String INSERT_FIRST = "insert into " 
      + TABLE_FAKETODO + "(_ID,CATEGORY,SUMMARY,DESCRIPTION)"+
      " values ('1','Reminder','Hidden ToDo','This is the Hidden ToDo List. This area is a" +
      " fuctioning Hidden ToDo list area, but actually hides the real purpose of this application')";
  
  private static final String INSERT_INSTRUCTIONS = "insert into " 
      + TABLE_FAKETODO + "(_ID,CATEGORY,SUMMARY,DESCRIPTION)"+
      " values ('2','Reminder','Access Hidden Gallery','Press and Hold the ToDo Icon" +
      " on the lower right-hand side of the Screen, Enter the Gallery Password.')";
  
  private static final String INSERT_DELETEITEMS = "insert into " 
	  + TABLE_FAKETODO + "(_ID,CATEGORY,SUMMARY,DESCRIPTION)"+
	  " values ('3','Reminder','DELETE THESE ITEMS','After you have read all of the above items" +
	  " delete all of them. You do not want to leave any of this around. Just Press and Hold each" + 
	  " list item and press Delete')";

  public static void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
    database.execSQL(INSERT_FIRST);
    database.execSQL(INSERT_INSTRUCTIONS);
    database.execSQL(INSERT_DELETEITEMS);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(FakeTodoTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + TABLE_FAKETODO);
    onCreate(database);
  }
} 