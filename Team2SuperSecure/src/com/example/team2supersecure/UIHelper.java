package com.example.team2supersecure;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

public class UIHelper {
	File file;
	
	public void changeWorkingDir(Context c, String dirName){
		//this does not work well kind of left it here
		// check iit out later
	      file = new File(dirName).getAbsoluteFile();
	      //change the directory to the records directory for recordings.
	      if (createDirIfNotExists(c, dirName)) {
	    	  
	    	  boolean result = (System.setProperty("user.dir", file.getAbsolutePath()) != null );
		  }
	}
	
	public boolean isExternalStoragePresent(Context c){
		
		boolean extStorageAvailable = false;
		boolean extStorageWritable = false;
		
		String state = Environment.getExternalStorageState();
		
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// we can read and write 
			extStorageAvailable = extStorageWritable = true;
			
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			//we can only read the media
			extStorageAvailable = true;
			extStorageWritable = false;
		}
		
		if (!(extStorageAvailable && extStorageWritable) ) {
			Toast.makeText(c, "SD card is not present", Toast.LENGTH_LONG).show();
		}
	
		return extStorageAvailable && extStorageWritable;
	}
	
	public boolean createDirIfNotExists(Context c, String newDir){
		boolean result = true;
		
		File file = new File( Environment.getExternalStorageDirectory(), newDir );
		
		if ( !file.exists() ) {
			// if file not exists then make it, in case fails give msg
			if ( !file.mkdirs() ) {
				result = false;
				Toast.makeText(c, "Problem creating the folder", Toast.LENGTH_LONG).show();
			}
		}
		
		result = (System.setProperty("user.dir", file.getAbsolutePath()) != null);
	
		
		return result;
	}
	
	
	
	
}
