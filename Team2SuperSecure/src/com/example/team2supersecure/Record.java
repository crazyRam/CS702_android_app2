package com.example.team2supersecure;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Record extends Activity {

   private static final String FILE_TRACE = "FILE_TRACE";
   private MediaRecorder myRecorder;
   private MediaPlayer myPlayer;
   private String outputFile = null;
   private Button recordBtn;
   private Button stopBtn;
   private Button playBtn;
   
   private TextView tv;
   UIHelper UIHelp = new UIHelper();
   private boolean folderExist = false;
   public File file;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.record_main);
      
      tv = (TextView) findViewById(R.id.textView1);
      
      //checking whether the records folder exists
      folderExist = UIHelp.createDirIfNotExists( getApplicationContext(), "records");
      getFileList();

      myRecorder = new MediaRecorder();
      myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
      myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
      
      recordBtn = (Button)findViewById(R.id.recordBtn);
      recordBtn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//record if external storage is present
			if (UIHelp.isExternalStoragePresent(getBaseContext())) {
				record(v);	
			}
		}
      });
      
      stopBtn = (Button)findViewById(R.id.stopBtn);
      stopBtn.setOnClickListener(new OnClickListener() {
  		
  		@Override
  		public void onClick(View v) {
  			stop(v);
  		}
      });
      
      playBtn = (Button)findViewById(R.id.playBtn);
      playBtn.setOnClickListener(new OnClickListener() {
  		
  		@Override
  		public void onClick(View v) {
				play(v);	
  		}
      });
      
      
   }

   public void record(View view){
	   // change the buttons look 
	   // set them invisible and visible
       recordBtn.setEnabled(false);
       stopBtn.setEnabled(true);
       playBtn.setEnabled(false);
       
	   
	   /**
	    * This two lines of code retrived from 
	    * http://stackoverflow.com/questions/5369682/get-current-time-and-date-on-android
	    * 
	    */
	   SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSSZ");
	   String fileNameAtTime = sdf.format(Calendar.getInstance().getTime());
	   
	   fileNameAtTime ="AudF" + fileNameAtTime + ".3gpp";
	   
/*	   "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
	   "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
	   "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
	   "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
	   "yyMMddHHmmssZ"-------------------- 010704120856-0700
	   "K:mm a, z" ----------------------- 0:08 PM, PDT
	   "h:mm a" -------------------------- 12:08 PM
	   "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01
*/	   
	   // store it to sd card
	   outputFile = Environment.getExternalStorageDirectory().
	    		  getAbsolutePath() + "/records/" + fileNameAtTime;

	   myRecorder.setOutputFile(outputFile);
	   Toast.makeText(getApplicationContext(), outputFile, Toast.LENGTH_LONG).show();
	      
	   try {
          myRecorder.prepare();
          myRecorder.start();
       } catch (IllegalStateException e) {
          // start:it is called before prepare()
    	  // prepare: it is called after start() or before setOutputFormat() 
          e.printStackTrace();
       } catch (IOException e) {
           // prepare() fails
           e.printStackTrace();
        }
	   
       tv.setText("Recording Point: Recording");

       
       Toast.makeText(getApplicationContext(), "Start recording...", 
    		   Toast.LENGTH_SHORT).show();
   }

   public void stop(View view){
	   try {
		  if (myRecorder != null) {

		      myRecorder.stop();
		      myRecorder.release();
		      myRecorder  = null;
		      
		      tv.setText("Recording Point: Stop recording");
		      
		  } else if (myPlayer != null) {
	    	   myPlayer.stop();
	           myPlayer.release();
	           myPlayer = null;

	           tv.setText("Recording Point: Stop playing");
	       }
		   recordBtn.setEnabled(true);
           playBtn.setEnabled(true);
		   stopBtn.setEnabled(false);

	       
	      Toast.makeText(getApplicationContext(), "Stop recording...",
	    		  Toast.LENGTH_SHORT).show();
	   } catch (IllegalStateException e) {
			//  it is called before start()
			e.printStackTrace();
	   } catch (RuntimeException e) {
			// no valid audio/video data has been received
			e.printStackTrace();
	   }
   }
  
   public void play(View view) {
	   try{
		   myPlayer = new MediaPlayer();
		   myPlayer.setDataSource(outputFile);
		   myPlayer.prepare();
		   myPlayer.start();
		   
		   playBtn.setEnabled(false);
		   recordBtn.setEnabled(false);
		   stopBtn.setEnabled(true);

		   
		   tv.setText("Recording Point: Playing");
		   
		   Toast.makeText(getApplicationContext(), "Start play the recording...", 
				   Toast.LENGTH_SHORT).show();
	   } catch (Exception e) {
			e.printStackTrace();
		}
   }

   
   public void getFileList(){
	   String path = Environment.getExternalStorageDirectory().toString() + "/records";
	   Log.d("FilesList", "Path:"+path);
	   File f = new File(path);
	   File file[] = f.listFiles();
	   Log.d("FilesList", "Size " + file.length);
	   for (int i = 0; i < file.length; i++) {
		   Log.d("FilesList", "File name: "+file[i].getName());		   
	   }
   }
}