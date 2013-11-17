package com.soboapps.todos.fakecontentprovider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import com.soboapps.todos.fakedatabase.FakeTodoDatabaseHelper;
import com.soboapps.todos.fakedatabase.FakeTodoTable;

public class MyFakeTodoContentProvider extends ContentProvider {

  // database
  private FakeTodoDatabaseHelper database;

  // Used for the UriMacher
  private static final int FAKETODOS = 10;
  private static final int FAKETODO_ID = 20;

  private static final String AUTHORITY = "com.soboapps.todos.fakecontentprovider";

  private static final String BASE_PATH = "faketodos";
  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
      + "/" + BASE_PATH);

  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
      + "/faketodos";
  public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
      + "/faketodo";

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  static {
    sURIMatcher.addURI(AUTHORITY, BASE_PATH, FAKETODOS);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FAKETODO_ID);
  }

  @Override
  public boolean onCreate() { 
    database = new FakeTodoDatabaseHelper(getContext());
    return false;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    // Uisng SQLiteQueryBuilder instead of query() method
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    // Check if the caller has requested a column which does not exists
    checkColumns(projection);

    // Set the table
    queryBuilder.setTables(FakeTodoTable.TABLE_FAKETODO);

    int uriType = sURIMatcher.match(uri);
    switch (uriType) {
    case FAKETODOS:
      break;
    case FAKETODO_ID:
      // Adding the ID to the original query
      queryBuilder.appendWhere(FakeTodoTable.COLUMN_ID + "="
          + uri.getLastPathSegment());
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    SQLiteDatabase db = database.getWritableDatabase();
    Cursor cursor = queryBuilder.query(db, projection, selection,
        selectionArgs, null, null, sortOrder);
    // Make sure that potential listeners are getting notified
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    long id = 0;
    switch (uriType) {
    case FAKETODOS:
      id = sqlDB.insert(FakeTodoTable.TABLE_FAKETODO, null, values);
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return Uri.parse(BASE_PATH + "/" + id);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    switch (uriType) {
    case FAKETODOS:
      rowsDeleted = sqlDB.delete(FakeTodoTable.TABLE_FAKETODO, selection,
          selectionArgs);
      break;
    case FAKETODO_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsDeleted = sqlDB.delete(FakeTodoTable.TABLE_FAKETODO,
            FakeTodoTable.COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsDeleted = sqlDB.delete(FakeTodoTable.TABLE_FAKETODO,
            FakeTodoTable.COLUMN_ID + "=" + id 
            + " and " + selection,
            selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {

    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsUpdated = 0;
    switch (uriType) {
    case FAKETODOS:
      rowsUpdated = sqlDB.update(FakeTodoTable.TABLE_FAKETODO, 
          values, 
          selection,
          selectionArgs);
      break;
    case FAKETODO_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsUpdated = sqlDB.update(FakeTodoTable.TABLE_FAKETODO, 
            values,
            FakeTodoTable.COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsUpdated = sqlDB.update(FakeTodoTable.TABLE_FAKETODO, 
            values,
            FakeTodoTable.COLUMN_ID + "=" + id 
            + " and " 
            + selection,
            selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsUpdated;
  }

  private void checkColumns(String[] projection) {
    String[] available = { FakeTodoTable.COLUMN_CATEGORY,
        FakeTodoTable.COLUMN_SUMMARY, FakeTodoTable.COLUMN_DESCRIPTION,
        FakeTodoTable.COLUMN_ID };
    if (projection != null) {
      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
      // Check if all columns which are requested are available
      if (!availableColumns.containsAll(requestedColumns)) {
        throw new IllegalArgumentException("Unknown columns in projection");
      }
    }
  }
}