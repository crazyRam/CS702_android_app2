package com.example.team2supersecure;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class RecordList extends Activity {
	ArrayList<String> files = new ArrayList<String>();
	private ListView lview;
	private String path="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_listview);

		//get SD card path 
		path = Environment.getExternalStorageDirectory().toString() + "/records";

		getFileList();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.record_list_item, files);

		lview = (ListView) findViewById(R.id.listview1);
		lview.setAdapter(adapter);

		registerForContextMenu(lview);
		

	}

	public void getFileList() {
		File f = new File(path);

		File flist[] = f.listFiles();

		for (int i = 0; i < flist.length; i++) {
			//Toast.makeText(this, flist[i].getName(), Toast.LENGTH_SHORT).show();
			files.add(flist[i].getName());
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.afiles_options_menu, menu);

	}

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
		
		//we don't deal with the other choices 
		//we are interested only in  ***BLUETOOTH****
		
		if (item.getTitle().equals("Bluetooth")) {
			
			BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
			if (btAdapter == null) {
				Log.d("Bluetooth", "Bluetooth adapter is null");
				//return false; 
			} 
			
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND);
			intent.setType("text/plain");
			// we need to get the file id when the listview selected
			                                                 // add it here
			
			if ( !AdapterContextMenuInfo.class.isInstance(item.getMenuInfo())) {
				//return false;
				//To do 
			}
			AdapterView.AdapterContextMenuInfo cmi = 
					(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			ListView list = (ListView) findViewById(R.id.listview1);
			Object obj = list.getItemAtPosition(cmi.position).toString();
			Log.d("ListView", obj.toString());
			
			String fileToTransfer = obj.toString();
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(getFilesDir()));
			
		}
		

		return super.onContextItemSelected(item);
	}
	
}
