package com.soboapps.todos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.soboapps.todos.adapter.FullScreenImageAdapter;
import com.soboapps.todos.helper.QuickEscape;
import com.soboapps.todos.helper.Utils;

public class FullScreenViewActivity extends Activity{
	
 	static int random = (int)Math.ceil(Math.random()*100000000);
    private static String fname = Integer.toString(random);
	
	public static String selectedImagePath;
	public static Intent selectedImagePathIntent;
    public static String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String restorePath = ExternalStorageDirectoryPath + "/Download/";

	private Utils utils;
	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;
	private ShareActionProvider mShareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.unlock);

		viewPager = (ViewPager) findViewById(R.id.pager);

		utils = new Utils(getApplicationContext());

		Intent i = getIntent();
		int position = i.getIntExtra("position", 0);

		adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, utils.getFilePaths());

		viewPager.setAdapter(adapter);

		// displaying selected image first
		viewPager.setCurrentItem(position);
		
		viewPager.getCurrentItem();
		
		selectedImagePath = adapter.mImagePaths.get(viewPager.getCurrentItem());
    	String file = selectedImagePath;
    	File sFile = new File (file);

	}
	
		// Listen for Volume Up or Down - 1
	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
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
	
		//Image Switcher
	public void imageSwitcher(){
		Intent intent=new Intent(getApplicationContext(),QuickEscape.class);
		startActivity(intent);
        FullScreenViewActivity.this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        getParent().finish();
	}

	// Menus
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		//MenuInflater prefs = getMenuInflater();
		//prefs.inflate(R.menu.prefs_menu, menu);
	    getMenuInflater().inflate(R.menu.menu_item_select, menu);
	    
	    // Locate MenuItem with ShareActionProvider
	    //MenuItem item = (MenuItem) menu.findItem(R.id.menu_item_share);
	
	    // Fetch and store ShareActionProvider
	    //mShareActionProvider = (ShareActionProvider) item.getActionProvider();

	    
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		//case R.id.menu_item_share:
			//Intent sFile = (selectedImagePathIntent);
			//setShareIntent(selectedImagePathIntent);
			//return true;
		case R.id.menu_item_discard:
			deleteFile();
			return true;
		case R.id.menu_item_restore:
			try {
				
				restoreFile(selectedImagePath, restorePath + fname + ".jpg");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;	
		}
		return false;

	}
	
	
    public void restoreFile(String selectedImagePath, final String restorePath) throws IOException {
    	final String selectedImagePath1 = adapter.mImagePaths.get(viewPager.getCurrentItem());
		
		new AlertDialog.Builder(this)
		.setIcon(R.drawable.undo)
		.setTitle(R.string.restoreConfirmationTitle)
		.setMessage(R.string.restoreConfirmationString)
		.setPositiveButton(R.string.restoreConfirmationStringYes, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				String restorePath1 = restorePath;	    	
				InputStream in = null;
				try {
					in = new FileInputStream(selectedImagePath1);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				OutputStream out = null;
				try {
					out = new FileOutputStream(restorePath1);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	  
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				try {
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast customToast = new Toast(getBaseContext());
				customToast = Toast.makeText(getBaseContext(), "Image Transferred " + selectedImagePath1 + " To " + restorePath1, Toast.LENGTH_LONG);
				customToast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
				customToast.show();	    	
				// request scan    
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" +  Environment.getExternalStorageDirectory())));
				String fileDelete = selectedImagePath1;
				File fdelete = new File(fileDelete);
				boolean deleted = fdelete.delete();
					if(deleted == false){
						Toast customToast1 = new Toast(getBaseContext());
						customToast1 = Toast.makeText(getBaseContext(), "Image Deleted", Toast.LENGTH_LONG);
						customToast1.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
						customToast1.show();
					}else{
						Toast customToast1 = new Toast(getBaseContext());
						customToast1 = Toast.makeText(getBaseContext(), "Image Restored", Toast.LENGTH_LONG);
						customToast1.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
						customToast1.show();
						viewPager.setAdapter(adapter);			    	
						refresh();
						
						// request scan    
						Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
						scanIntent.setData(Uri.fromFile(fdelete));
						sendBroadcast(scanIntent);
						refresh();
				}
			}		
		})
		.setNegativeButton(R.string.deleteConfirmationStringNo, null)
		.show();
	}
    
		private void deleteFile(){
		selectedImagePath = adapter.mImagePaths.get(viewPager.getCurrentItem());
	
		new AlertDialog.Builder(this)
		.setIcon(R.drawable.discard_button)
        .setTitle(R.string.deleteConfirmationTitle)
        .setMessage(R.string.deleteConfirmationString)
        .setPositiveButton(R.string.deleteConfirmationStringYes, new DialogInterface.OnClickListener() {        	
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	String fileDelete = selectedImagePath;
				File fdelete = new File(fileDelete);
				boolean deleted = fdelete.delete();
					if(deleted == false){
				        Toast customToast = new Toast(getBaseContext());
				    	customToast = Toast.makeText(getBaseContext(), "Image NOT Removed", Toast.LENGTH_LONG);
				    	customToast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
				    	customToast.show();
					}else{
				        Toast customToast = new Toast(getBaseContext());
				    	customToast = Toast.makeText(getBaseContext(), "Image Removed", Toast.LENGTH_LONG);
				    	customToast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
				    	customToast.show();
				    	viewPager.setAdapter(adapter);						
						// request scan    
				    	Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				    	scanIntent.setData(Uri.fromFile(fdelete));
				    	sendBroadcast(scanIntent);
				    	refresh();
					}
				}    

        })
        .setNegativeButton(R.string.deleteConfirmationStringNo, null)
        .show();
		}

		// Call to update the share intent
		private void setShareIntent(Intent shareIntent) {
		    if (mShareActionProvider != null) {
		        mShareActionProvider.setShareIntent(shareIntent);
		    }
		}
	    
	    
	    public void refresh() {
	        adapter.notifyDataSetChanged();
	        Intent intent=new Intent(getApplicationContext(),GalleryActivity.class);
	        startActivity(intent);
	    }
	    
}
