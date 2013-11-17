package com.soboapps.todos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

	public class FakePassPrefs extends Activity {


		public static final String PASSWORD_PREF_KEY ="PasswordSharedPreferences";

		Button button1, button2;
		
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
		Button btnSubmit, btnDone;
		ImageButton btnBS;

		EditText pass1, password, passwordEditText;
		TextView messages, passHint, txtPasswd;
		
		@Override
		public void onCreate(Bundle icicle) {
		    super.onCreate(icicle);
		    setContentView(R.layout.setgalpass);

		    messages = (TextView) findViewById (R.id.text1);
		    pass1 = (EditText) findViewById(R.id.password);
		
	    btnSubmit = (Button) findViewById(R.id.btnSubmit);
	    btnDone = (Button) findViewById(R.id.btnDone);
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
	    passHint.setText("Set Secure ToDo PIN");
		
	    final SharedPreferences settings = this.getSharedPreferences("PasswordSharedPreferences", MODE_PRIVATE);
		final String password1 = settings.getString("User Password", "");
		
		final Button buttonSubmit = (Button) findViewById(R.id.btnSubmit);
		buttonSubmit.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v){
			    final String p1 = pass1.getText().toString();
		    	
		        if (p1.length() < 4) {
	                Toast customToast = new Toast(getBaseContext());
		        	customToast = Toast.makeText(getBaseContext(), "PIN must be at least 4 characters", Toast.LENGTH_SHORT);
		        	customToast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
		        	customToast.show();
		        	txtPasswd.setText("");
		            inStr = pass1.getText().toString();		            
		            txtPasswd.setText(inStr);
		        }
				else if (p1.equals(password1)) {		
	                Toast customToast = new Toast(getBaseContext());
		        	customToast = Toast.makeText(getBaseContext(), "PIN's Must Be Unique", Toast.LENGTH_SHORT);
		        	customToast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
		        	customToast.show();
		        	txtPasswd.setText("");
		            inStr = pass1.getText().toString();		            
		            txtPasswd.setText(inStr);
			    }
				else {
		        	
		        	settings.edit().putString("ToDo Password", p1).apply();
	                Toast customToast = new Toast(getBaseContext());
		        	customToast = Toast.makeText(getBaseContext(), "PIN has been set!", Toast.LENGTH_SHORT);
		        	customToast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
		        	customToast.show();
		        	//onDestroy();
		        	//finish();
		        	StartGallery();
		        	onDestroy();
		        	//startActivity(new Intent(FakePassPrefs.this,GalleryActivity.class));
		        }
		        }
		    });


		 final ImageButton buttonBS = (ImageButton) findViewById(R.id.btnBS);
		    buttonBS.setOnClickListener(new View.OnClickListener() {
		         public void onClick(View v) {
		            inStr = pass1.getText().toString();
		            inStr = inStr.length() > 1 ? inStr.substring(0, inStr.length() - 1): "";
		            txtPasswd.setText(inStr);
		            onDestroy();
		         }
		    });
		
			final Button buttonDone = (Button) findViewById(R.id.btnDone);
			buttonDone.setOnClickListener(new View.OnClickListener() {
			         public void onClick(View v) {
			        	 onDestroy();
			        	 //startActivity(new Intent(FakePassPrefs.this,GalleryActivity.class));  finish();
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
	    
	    @Override
		public void onDestroy() {
	        super.onDestroy();
	        android.os.Process.killProcess(android.os.Process.myPid());
	    }
	}