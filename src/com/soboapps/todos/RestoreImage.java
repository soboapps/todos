package com.soboapps.todos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.Toast;

public class RestoreImage extends Activity {
	
    static int random = (int)Math.ceil(Math.random()*100000000);
    private static String fname = Integer.toString(random);
    
    private static final int SELECT_PICTURE = 1;
    
    private static String selectedImagePath;
    private static String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static String targetPath = ExternalStorageDirectoryPath + "/DCIM/ToDo/";
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        	Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
    }
 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                try {
			     copyFile(selectedImagePath, targetPath + fname + ".jpg");
			    } catch (IOException e) {
			     e.printStackTrace();
			    }
            }
        }
    }
    
    // Copy the file the Hidden Folder 
    public void copyFile(String selectedImagePath2, String string) throws IOException {
        InputStream in = new FileInputStream(selectedImagePath2);
        OutputStream out = new FileOutputStream(string);
  
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        
        Toast customToast = new Toast(getBaseContext());
    	customToast = Toast.makeText(getBaseContext(), "Image Transferred", Toast.LENGTH_LONG);
    	customToast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
    	customToast.show();
    	
    	//Restart the Hidden Gallery
        StartGallery();
        
        //Delete the File from Main Gallery
        deleteFile();
      }
 
    // Restart Hidden Gallery
    private void StartGallery() {
	    Intent intent = new Intent(this, GalleryActivity.class);
	    startActivity(intent);
	}

	public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
	//Delete File from Main Gallery
    private void deleteFile() {
    	File fdelete = new File(selectedImagePath);
        fdelete.delete();
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" +  Environment.getExternalStorageDirectory())));
	}
    
}