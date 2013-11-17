package com.soboapps.todos;


import java.io.File;
import java.util.ArrayList;

import com.soboapps.todos.adapter.GridViewImageAdapter;
import com.soboapps.todos.helper.AppConstant;
import com.soboapps.todos.helper.QuickEscape;
import com.soboapps.todos.helper.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ShareActionProvider;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryActivity extends Activity implements Shaker.Callback {
	
	File sdCardLoc = Environment.getExternalStorageDirectory();
	File intImagesDir = new File(sdCardLoc,"/DCIM/ToDo/.nomedia");
	File farDir = new File(sdCardLoc,"/DCIM/ToDo/.nomedia/far/");

	File[] imageList = intImagesDir.listFiles();

    String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    String targetPath = ExternalStorageDirectoryPath + "/DCIM/ToDo/.nomedia/";
    String farPath = ExternalStorageDirectoryPath + "/DCIM/ToDo/.nomedia/far";
	
	File targetDirector = new File(targetPath);
	
	private Shaker shaker=null;
	
	private ShareActionProvider mShareActionProvider;

	private Utils utils;
	private ArrayList<String> imagePaths = new ArrayList<String>();
	private GridViewImageAdapter adapter;
	private GridView gridView;
	private int columnWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid_view);
		
        PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final SharedPreferences settings = this.getSharedPreferences("PasswordSharedPreferences", MODE_PRIVATE);
        String todopassword = settings.getString("ToDo Password", "");
        
        if(todopassword.isEmpty()) {
        	startActivity(new Intent(GalleryActivity.this,FakePassPrefs.class));
        }
		
		shaker=new Shaker(this, 2.25F, 400, this);

		gridView = (GridView) findViewById(R.id.grid_view);

		utils = new Utils(this);

		// Initializing Grid View
		InitilizeGridLayout();

		// loading all image paths from SD card
		imagePaths = utils.getFilePaths();

		// Gridview adapter
		adapter = new GridViewImageAdapter(GalleryActivity.this, imagePaths, columnWidth);

		// setting grid view adapter
		gridView.setAdapter(adapter);
		
        gridView.setOnItemClickListener(new OnItemClickListener() {
        	int mPosition;
        	
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	this.mPosition = position;
                Intent i = new Intent(getApplicationContext(), FullScreenViewActivity.class);
                //Toast.makeText(GalleryActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                i.putExtra("position", mPosition);
                startActivity(i);
            }
        });
	}

	private void InitilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstant.GRID_PADDING, r.getDisplayMetrics());

		columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

		gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
		gridView.setColumnWidth(columnWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
		gridView.setHorizontalSpacing((int) padding);
		gridView.setVerticalSpacing((int) padding);
		gridView.setFastScrollEnabled(true);
		
	}
	
		// Listen for Volume Up or Down - 1
		@Override
	    public boolean dispatchKeyEvent(KeyEvent event) 
	    {
	        if (event.getAction() == KeyEvent.ACTION_DOWN)
	        {
	            switch (event.getKeyCode()) 
	            {
	                case KeyEvent.KEYCODE_VOLUME_UP:
		       			 imageSwitcher();
		    	    	 super.onDestroy();
		                    return true;
	                case KeyEvent.KEYCODE_VOLUME_DOWN:
		       			 imageSwitcher();
		    	    	 super.onDestroy();
	                    return true;
	            }
	        }

	        return super.dispatchKeyEvent(event);
	    }


	 
	 //On Shake
	public void shakingStarted() {
		imageSwitcher(); 
		super.onDestroy();
		}
	
	
	public void shakingStopped() {
		imageSwitcher();
		super.onDestroy();
		}
	
	
	//Image Switcher
	public void imageSwitcher(){
		Intent intent=new Intent(getApplicationContext(),QuickEscape.class);
		startActivity(intent);
        GalleryActivity.this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(0);
        getParent().finish();
	}
	
	// Menus
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater prefs = getMenuInflater();
		prefs.inflate(R.menu.prefs_menu, menu);
	    getMenuInflater().inflate(R.menu.menu_share, menu);
	
	    // Locate MenuItem with ShareActionProvider
	    //MenuItem item = menu.findItem(R.id.menu_item_share);
	
	    // Fetch and store ShareActionProvider
	    //mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		return true;
	}
	
	// Call to update the share intent
	private void setShareIntent(Intent shareIntent) {
	    //if (mShareActionProvider != null) {
	    //   mShareActionProvider.setShareIntent(shareIntent);
	    //}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.menuPrefs:
			startActivity(new Intent("com.soboapps.todos.PASSPREFS"));
			return true;
		case R.id.menuFake:
			startActivity(new Intent(GalleryActivity.this,FakePassPrefs.class));
			return true;
		case R.id.menuSetQE:
			startActivity(new Intent(GalleryActivity.this,SetQuickEscape.class));
			return true;
		case R.id.menuHelp:
			startActivity(new Intent(GalleryActivity.this,Help.class));
			return true;	
		case R.id.menu_item_new:
			startActivity(new Intent("com.soboapps.todos.GETTODOIMAGE"));
			onDestroy();
			return true;	
		}
		return false;
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    android.os.Process.killProcess(android.os.Process.myPid());
	    shaker.close();
	}

}
