package com.soboapps.todos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class Password extends Activity {
	
public static final String PASSWORD_PREF_KEY ="PasswordSharedPreferences";
public static final String PREFS_PRIVATE = "PREFS_PRIVATE";
public static final String KEY_PRIVATE = "KEY_PRIVATE";
public static final String PREFS_READ = "PREFS_READ";
public static final String KEY_READ = "KEY_READ";
public static final String PREFS_WRITE = "PREFS_WRITE";
public static final String KEY_WRITE = "KEY_WRITE";
public static final String PREFS_READ_WRITE = "PREFS_READ_WRITE";
public static final String KEY_READ_WRITE = "KEY_READ_WRITE";
private String inStr = "";

Button btnZero=null;
Button btnOne=null;
Button btnTwo=null;
Button btnThree=null;
Button btnFour=null;
Button btnFive=null;
Button btnSix=null;
Button btnSeven=null;
Button btnEight=null;
Button btnNine=null;
Button btnSubmit, btnCancel;
ImageButton btnBS;

EditText pass1, password, passwordEditText;
TextView messages, passHint, txtPasswd;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);   
    setContentView(R.layout.password);

    PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    final SharedPreferences settings = this.getSharedPreferences("PasswordSharedPreferences", MODE_PRIVATE);
    final SharedPreferences fake = this.getSharedPreferences("PasswordSharedPreferences", MODE_PRIVATE);
    String password1 = settings.getString("User Password", "");

    btnSubmit = (Button) findViewById(R.id.btnSubmit);
    btnCancel = (Button) findViewById(R.id.btnCancel);
    btnOne = (Button) findViewById(R.id.btnOne);
    btnTwo = (Button) findViewById(R.id.btnTwo);
    btnThree = (Button) findViewById(R.id.btnThree);
    btnFour = (Button) findViewById(R.id.btnFour);
    btnFive = (Button) findViewById(R.id.btnFive);
    btnSix = (Button) findViewById(R.id.btnSix);
    btnSeven = (Button) findViewById(R.id.btnSeven);
    btnEight = (Button) findViewById(R.id.btnEight);
    btnNine = (Button) findViewById(R.id.btnNine);
    btnZero = (Button) findViewById(R.id.btnZero);
    btnOne.setText("1");
    btnTwo.setText("2");
    btnThree.setText("3");
    btnFour.setText("4");
    btnFive.setText("5");
    btnSix.setText("6");
    btnSeven.setText("7");
    btnEight.setText("8");
    btnNine.setText("9");
    btnZero.setText("0");
    
    txtPasswd = (TextView) findViewById(R.id.txtPassword);
    txtPasswd.setText("");

    messages = (TextView) findViewById (R.id.text1);
    pass1 = (EditText) findViewById(R.id.txtPassword);
    passHint = (TextView) findViewById(R.id.textView1);
    passwordEditText = (EditText) findViewById(R.id.password);
    passHint.setText("Enter PIN");
    
    if(password1.isEmpty()) {
    	passHint.setText("Set Gallery PIN");  	
    	EditText txtPassword = (EditText) findViewById(R.id.txtPassword); 
    	txtPassword.setTransformationMethod(null); 
    }   

	final Button buttonSubmit = (Button) findViewById(R.id.btnSubmit);
	buttonSubmit.setOnClickListener(new View.OnClickListener() {
	public void onClick(View v) {
		
	    final String password1 = settings.getString("User Password", "");
	    final String fakepassword = settings.getString("ToDo Password", "");
	    final String p1 = pass1.getText().toString();
		
	    if(password1.isEmpty()) {
	    
	        if (p1.length() < 4) {
                Toast customToast = new Toast(getBaseContext());
	        	customToast = Toast.makeText(getBaseContext(), "PIN must be at least 4 characters", Toast.LENGTH_SHORT);
	        	customToast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
	        	customToast.show();
	        	txtPasswd.setText("");
	            inStr = pass1.getText().toString();		            
	            txtPasswd.setText(inStr);
	        }
	        
	        else {   	
	        	settings.edit().putString("User Password", p1).apply();
                Toast customToast = new Toast(getBaseContext());
	        	customToast = Toast.makeText(getBaseContext(), "PIN has been set!", Toast.LENGTH_SHORT);
	        	customToast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
	        	customToast.show();
	        	//onDestroy();
	        	StartGallery();
	            //finish();
	            
	        }
	    }
			else if(p1.equals(password1)) {		
				StartGallery();
				onDestroy();
		        }
			else if(p1.equals(fakepassword)) {
				StartFakeToDo();
				onDestroy();
		        }
		        else {
		        	Toast customToast = new Toast(getBaseContext());
		        	customToast = Toast.makeText(getBaseContext(), "Incorrect PIN!", Toast.LENGTH_SHORT);
		        	customToast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
		        	customToast.show(); {
		            }
		        	txtPasswd.setText("");
		            inStr = pass1.getText().toString();		            
		            txtPasswd.setText(inStr);
		        }
    }
	});

	 final ImageButton buttonBS = (ImageButton) findViewById(R.id.btnBS);
	    buttonBS.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View v) {
	            inStr = pass1.getText().toString();
	            inStr = inStr.length() > 1 ? inStr.substring(0, inStr.length() - 1): "";
	            txtPasswd.setText(inStr);
	            
	         }
	    });
	
		final Button buttonCancel = (Button) findViewById(R.id.btnCancel);
		buttonCancel.setOnClickListener(new View.OnClickListener() {
		         public void onClick(View v) {
		        	 //startActivity(new Intent("com.soboapps.todos.MAIN"));
		        	 onDestroy();
		         }
		    });	    
	    	    
    BtnListener listener = new BtnListener();
    ((Button) findViewById(R.id.btnZero)).setOnClickListener(listener);
    ((Button) findViewById(R.id.btnOne)).setOnClickListener(listener);
    ((Button) findViewById(R.id.btnTwo)).setOnClickListener(listener);
    ((Button) findViewById(R.id.btnThree)).setOnClickListener(listener);
    ((Button) findViewById(R.id.btnFour)).setOnClickListener(listener);
    ((Button) findViewById(R.id.btnFive)).setOnClickListener(listener);
    ((Button) findViewById(R.id.btnSix)).setOnClickListener(listener);
    ((Button) findViewById(R.id.btnSeven)).setOnClickListener(listener);
    ((Button) findViewById(R.id.btnEight)).setOnClickListener(listener);
    ((Button) findViewById(R.id.btnNine)).setOnClickListener(listener);
}

private class BtnListener implements OnClickListener {
    public void onClick(View view) {
       switch (view.getId()) {
       case R.id.btnZero:
       case R.id.btnOne:
       case R.id.btnTwo:
       case R.id.btnThree:
       case R.id.btnFour:
       case R.id.btnFive:
       case R.id.btnSix:
       case R.id.btnSeven:
       case R.id.btnEight:
       case R.id.btnNine:
          String inDigit = ((Button) view).getText().toString();
             inStr += inDigit;
             txtPasswd.setText(inStr);
          break;
       }
    }
 }

    public void StartGallery(){
		    Intent intent = new Intent(this, GalleryActivity.class);
		    startActivity(intent);
		}
    
    public void StartFakeToDo(){
	    Intent intent = new Intent(this, FakeTodosOverviewActivity.class);
	    startActivity(intent);
	}
    
    @Override
	public void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}