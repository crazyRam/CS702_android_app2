package com.example.team2supersecure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mainmenu_activity);
	}
	
	public void gotoRecord(View v){
		Intent intent = new Intent(this, Record.class);
		startActivity(intent);
	}

	public void gotoRecordList(View v){
		Intent intent = new Intent(this, RecordList.class);
		startActivity(intent);
	}
	            
	public void gotoReceivedList(View v){
		Intent intent = new Intent(this, ReceivedList.class);
		startActivity(intent);
	}
	
}
