package com.soboapps.todos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.soboapps.todos.contentprovider.MyTodoContentProvider;
import com.soboapps.todos.database.TodoTable;


public class TodosOverviewActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	
  private static final int ACTIVITY_CREATE = 0;
  private static final int ACTIVITY_EDIT = 1;
  private static final int DELETE_ID = Menu.FIRST + 1;
  // private Cursor cursor;
  private SimpleCursorAdapter adapter;
  
  private RateMyApp rate;
  
  SharedPreferences setNoti;
  boolean showHelp1;
  
  
  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.todo_list);
    this.getListView().setDividerHeight(2);
    fillData();
    registerForContextMenu(getListView());
    
    setNoti = PreferenceManager.getDefaultSharedPreferences(this );
    //SharedPref tutorial
    showHelp1 = setNoti.getBoolean("help", true);

    if (showHelp1 == true) {
    	showActivityOverlay();
    }
    
    //Initialize the RateMyApp component
    //set the title, days till the user is prompted and the no. of launches till the user is prompted
    rate = new RateMyApp(this, "Secure ToDo List", 7, 2);
    
    //make all text white
    rate.setTextColor(Color.BLACK);
    
    //set a custom message
    rate.setMessage("Do you like the app? Would you kindly rate it?");
    
    //set a custom text size
    rate.setTextSize(16);
    rate.start();
    
    

	ImageButton btnPass = (ImageButton) findViewById(R.id.btnPass);
	btnPass.setOnLongClickListener(new View.OnLongClickListener() {
		
		public boolean onLongClick(View v) {
			// Image file used for the imageSwitcher() Method in GalleryActivity
			Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.farside);
			// Get sdCard location so we can Create Dir and File
			File sdCardLoc = Environment.getExternalStorageDirectory();
			// Get Internal Storage location so we can Create Dir and File
			//File intCardLoc = Environment.getDataDirectory();
			// Create the Hidden Folder
			
			File restoreDir = new File(sdCardLoc,"/DCIM/Download");
			if(!restoreDir.exists()) {
			restoreDir.mkdirs();
			}
			
			File imagesDir = new File(sdCardLoc,"/DCIM/ToDo/.nomedia");
			if(!imagesDir.exists()) {
			imagesDir.mkdirs();
            
			// Create the Folder to hold the image file used for the imageSwitcher() Method
			File farDir = new File(imagesDir,"/far");
			farDir.mkdirs();
			
			// Copy the image file used for the imageSwitcher() Method
			File file = new File(farDir, "farside.png");
			File rfile = new File(farDir, "farside1.png");
			
				//farside
				FileOutputStream outStream = null;
				try {
					outStream = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			    bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			    try {
					outStream.flush();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			    try {
					outStream.close();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			    
			    //fareside1
				FileOutputStream outStream1 = null;
				try {
					outStream1 = new FileOutputStream(rfile);
				} catch (FileNotFoundException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			    bm.compress(Bitmap.CompressFormat.PNG, 100, outStream1);
			    try {
					outStream1.flush();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			    try {
					outStream1.close();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
		    
			}
			startActivity(new Intent("com.soboapps.todos.PASSWORD"));
			onDestroy();
			return true;
			
		}
	});
	
	ImageButton btnNew = (ImageButton) findViewById(R.id.btnNew);
	btnNew.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			createTodo();
		}
	}); 

  }
  
  private void showActivityOverlay() {
	  final Dialog dialog = new Dialog(this,
	  android.R.style.Theme_Translucent_NoTitleBar);

	  dialog.setContentView(R.layout.helpone);

	  LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.llOverlay_activity);
	  layout.setBackgroundColor(Color.TRANSPARENT);
	  	layout.setOnClickListener(new OnClickListener() {

		  @Override
		  public void onClick(View arg0) {
			  dialog.dismiss();
			  SharedPreferences.Editor editor = setNoti.edit();
			  editor.putBoolean("help", false);
			  editor.commit();
		  }
	  });
	  dialog.show();
  }

  // Create the menu based on the XML definition
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.listmenu, menu);
    return true;
  }

  // Reaction to the menu selection
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.btnNew:
      createTodo();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case DELETE_ID:
      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
      Uri uri = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/" + info.id);
      getContentResolver().delete(uri, null, null);
      fillData();
      return true;
    }
    return super.onContextItemSelected(item);
  }

  private void createTodo() {
    Intent i = new Intent(this, TodoDetailActivity.class);
    startActivityForResult(i, ACTIVITY_CREATE);
  }

  // Opens the second activity if an entry is clicked
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    Intent i = new Intent(this, TodoDetailActivity.class);
    Uri todoUri = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/" + id);
    i.putExtra(MyTodoContentProvider.CONTENT_ITEM_TYPE, todoUri);

    // Activity returns an result if called with startActivityForResult
    startActivityForResult(i, ACTIVITY_EDIT);
  }

  // Called with the result of the other activity
  // requestCode was the origin request code send to the activity
  // resultCode is the return code, 0 is everything is ok
  // intend can be used to get data
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
  }
  
  private void fillData() {

    // Fields from the database (projection)
    // Must include the _id column for the adapter to work
    String[] from = new String[] { TodoTable.COLUMN_SUMMARY };
    // Fields on the UI to which we map
    int[] to = new int[] { R.id.label };

    getLoaderManager().initLoader(0, null, this);
    adapter = new SimpleCursorAdapter(this, R.layout.todo_row, null, from, to, 0);

    setListAdapter(adapter);
  }
  
@Override
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.add(0, DELETE_ID, 0, R.string.menu_delete);
  }

  // Creates a new loader after the initLoader () call

  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = { TodoTable.COLUMN_ID, TodoTable.COLUMN_SUMMARY };
    CursorLoader cursorLoader = new CursorLoader(this, MyTodoContentProvider.CONTENT_URI, projection, null, null, null);
    return cursorLoader;
  }


  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    adapter.swapCursor(data);
  }


  public void onLoaderReset(Loader<Cursor> loader) {
    // data is not available anymore, delete reference
    adapter.swapCursor(null);
} 
  @Override
public void onDestroy() {
      super.onDestroy();
      android.os.Process.killProcess(android.os.Process.myPid());
  }
} 