package com.soboapps.todos.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TodoTable {

  // Database table
  public static final String TABLE_TODO = "todo";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_CATEGORY = "category";
  public static final String COLUMN_SUMMARY = "summary";
  public static final String COLUMN_DESCRIPTION = "description";

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table " 
      + TABLE_TODO
      + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_CATEGORY + " text not null, " 
      + COLUMN_SUMMARY + " text not null," 
      + COLUMN_DESCRIPTION
      + " text not null" 
      + ");";
  
  private static final String INSERT_FIRST = "insert into " 
      + TABLE_TODO + "(_ID,CATEGORY,SUMMARY,DESCRIPTION)"+
      " values ('1','Reminder','Main ToDo','This is the Main ToDo List. To Delete an Item in the list," +
      " Press and Hold the item and press \"Delete Item?\".  To Insert an item, press \"+\" in the" +
      " upper right-hand side of the Main Screen.')";
  
  private static final String INSERT_INSTRUCTIONS = "insert into " 
      + TABLE_TODO + "(_ID,CATEGORY,SUMMARY,DESCRIPTION)"+
      " values ('2','Reminder','Access Hidden Gallery','Press and Hold the ToDo icon" +
      " on the lower right-hand side of the Main Screen. You will be prompted to set a Gallery" + 
      " and Hidden Todo List PIN the first time')";
  
  private static final String INSERT_FAKE = "insert into " 
      + TABLE_TODO + "(_ID,CATEGORY,SUMMARY,DESCRIPTION)"+
      " values ('3','Reminder','How to Change your PINs','Press and Hold the ToDo icon on the lower right-hand side," +
      " Login into the Gallery.  Press the Menu button, choose either, \"Change Hidden ToDo PIN\" or \"Change Gallery PIN\" ')";
  
  private static final String INSERT_DELETEITEMS = "insert into " 
	  + TABLE_TODO + "(_ID,CATEGORY,SUMMARY,DESCRIPTION)"+
	  " values ('4','Reminder','DELETE THESE ITEMS','After you have read all of the above items" +
	  " delete all of them. You do not want to leave any of this around. Just Press and Hold each" + 
	  " list item and press Delete')";
 
  
  public static void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
    database.execSQL(INSERT_FIRST);
    database.execSQL(INSERT_INSTRUCTIONS);
    database.execSQL(INSERT_FAKE);
    database.execSQL(INSERT_DELETEITEMS);
    
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(TodoTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
    onCreate(database);
  }
} 