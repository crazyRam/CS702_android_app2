package com.example.team2supersecure;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.renderscript.Int2;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

import com.example.team2supersecure.data.*;

public class MainActivity extends ActionBarActivity {

	private static final String FILE_TXT = "authorise.txt";
	 
	//private static boolean fileExist = true;
	private boolean confirmPwd = true; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		// get the current working directory
//		String cwd = System.getProperty("user.dir");
//		cwd += FILE_TXT;
//		setCurrentDirectory(FILE_TXT);
//		
//		// Toast.makeText(getBaseContext(), "working dir="+cwd,
//		// Toast.LENGTH_SHORT).show();
//
//		// check whether the file exists
//
//		File file = new File(cwd);
//		fileExist = file.exists();
		
		//String extDir = Environment.getExternalStorageDirectory().toString();
		// for debug to show SD directory, leave it
		// Toast.makeText(getBaseContext(), extDir, Toast.LENGTH_LONG);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		File file = getFilesDir();
		String path = file.getAbsolutePath();
		
		// for debug reasons used, leave it
		// Toast.makeText(getBaseContext(), path,Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		// this part will work if the user already signed his pwd
		// ............... will be used later
		// if (fileExist) {
			 
			 // file exist and reading whether there is an pwd in it
			String encoded="";
			try {
				encoded = readFromFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			 // if the encoded str is not null then make confirm pwd invisible 
			 confirmPwd = true;
			 if ( encoded != null && !encoded.isEmpty() ) {
				 confirmPwd = false;

				 // confirm text view field is invisible now, because we have the pwd
				 TextView confirm_tv = (TextView) findViewById(R.id.textView2);
				 confirm_tv.setVisibility(0x00000004); // int 4 invisible
				
				 // confirm edit text is invisibles
				 EditText confirm_etxt= (EditText) findViewById(R.id.editText2);
				 confirm_etxt.setVisibility(0x00000004); // int 4 invisible
			}

		// }

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			return rootView;
		}

	}
	
	public void createFile(String str) throws IOException{
		// Create a file and save the pwd
		// Open the file and write it in private mode
		FileOutputStream fos = openFileOutput(FILE_TXT, MODE_PRIVATE);
		fos.write(str.getBytes());
		fos.close();
	}

//	public void saveToFile(String str) {
//
//		try {
//			
//			@SuppressWarnings("deprecation")
//			FileOutputStream fileOS = openFileOutput(FILE_TXT,
//					MODE_WORLD_READABLE);
//
//			OutputStreamWriter outputSW = new OutputStreamWriter(fileOS);
//
//			// write string to file
//			outputSW.write(str);
//			outputSW.flush();
//			outputSW.close();
//
//			// display the saved into file msg
//			Toast.makeText(getBaseContext(), "Password saved",
//					Toast.LENGTH_SHORT).show();
//
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//		}
//	}

	public String readFromFile() throws IOException {
		
		FileInputStream fis = openFileInput(FILE_TXT);
		BufferedInputStream bis = new BufferedInputStream(fis);
		StringBuffer b = new StringBuffer();

		while ( bis.available() != 0) {
			char c = (char) bis.read();
			b.append(c);
		}
		
		bis.close();
		fis.close();

		// Toast.makeText(getBaseContext(), "File has been Read="+b, Toast.LENGTH_SHORT).show();

		return b.toString();
	}
	
	private String makePwd(String str){
		
		//create password token object 
		//PasswordToken pwdToken = new PasswordToken();
		
		// make the pwd encoded and assign to encoded
		String encoded = PasswordToken.makeDigest(str);
		
		//write the pwd to a file
		//saveToFile(encoded);
		
		return encoded;
	}
	
	private boolean pwdOK(String pwd) throws IOException{
		//create password token object 
		//PasswordToken pwdToken = new PasswordToken();
		//String token = PasswordToken.makeDigest(pwd);
		String token = readFromFile();
		
		if (token == null )
			token="";
		
		if (pwd == null )
			pwd="";
		
		if (PasswordToken.validate(pwd, token) )
			return true;
		
		return false;
		
	}
	

	// this event is called when Sign in button clicked
	// checking the pword and calling the next activity with an intent
	public void gotoRecording(View v) throws IOException {
		
		// if fileExist and there is a pwd in it then 
		// get the texts from the editboxes of pwd and confirm
	    EditText pwdEdit = (EditText) findViewById(R.id.editText1);
		EditText confirmEdit = (EditText) findViewById(R.id.editText2); 
		
		String pwd = pwdEdit.getText().toString();
		String confirm = confirmEdit.getText().toString();
				
		// Creating pwd for the first time confirmPwd is true
		if ( (pwd != null && confirmPwd) && ( pwd.equals(confirm)) ) {
			// make sure the password and confirm password are equal
			
			// get pwd encrypted and save it to file
			// saveToFile(pwd);
			String encrypted = makePwd(pwd);
			createFile(encrypted);
			
			// Create a new intent and go to the recording main menu 
			Intent intent = new Intent(this, MainMenu.class);
			startActivity(intent);
			
		} else if ( (pwd != null && confirmPwd) && ( !pwd.equals(confirm))) {
			// pwd and confirm field are not equal
			Toast.makeText(getBaseContext(), "Confirmation the Password", Toast.LENGTH_LONG).show();
			return;
			
		} else if ( pwd != null && pwdOK(pwd)) {
		    // it's not the first time and if the pwdOK true	
			// go to the main menu
			Intent intent = new Intent(this, MainMenu.class);
			startActivity(intent);
		} else if ( !confirmPwd && !pwdOK(pwd) ) {
			// warn user to make a new password or exit
			Toast.makeText(getBaseContext(), "Try a new password", Toast.LENGTH_LONG).show();
		} else {
			// figure out other options
			// warn user to make a new password or exit
			// Toast.makeText(getBaseContext(), "Create a new password", Toast.LENGTH_LONG).show();
			return;
		}
	}
	
}
