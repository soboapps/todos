package com.soboapps.todos.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

import com.soboapps.todos.R;

public class QuickEscape extends Activity {
	
	String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    String farPath = ExternalStorageDirectoryPath + "/DCIM/ToDo/.nomedia/far";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);
		
        Toast customToast = new Toast(getBaseContext());
    	customToast = Toast.makeText(getBaseContext(), "Press Home to Exit", Toast.LENGTH_SHORT);
    	customToast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
    	customToast.show();
		
		Intent intent = new Intent();
	 	intent.setAction(Intent.ACTION_VIEW);
	 	intent.setDataAndType(Uri.parse("file://" + farPath + "/" + "/farside.png"), "image/*");
	 	startActivity(intent);
	}	
	
	@Override
	public void onBackPressed() {
	    Intent intent = new Intent(this, QuickEscape.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    intent.putExtra("Exit me", true);
	    startActivity(intent);
	    finish();
	}
	
}
