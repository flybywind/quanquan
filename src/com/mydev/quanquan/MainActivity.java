package com.mydev.quanquan;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 提示用户打开gps
		LocationManager loc_manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if(!loc_manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}
//		google_map_init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	public void clickSquare(View v){
		Intent Square = new Intent();
		Square.setClass(this, com.mydev.quanquan.Square.class);
		startActivity(Square);
//		Toast.makeText(this, "进入广场", Toast.LENGTH_SHORT).show();
	}
	public void clickMap(View v){
//		Toast.makeText(this, "进入地图", Toast.LENGTH_SHORT).show();
	}
	public void clickMarker(View v){
//		Toast.makeText(this, "进入地标", Toast.LENGTH_SHORT).show();
	}
	public void clickMyattention(View v){
//		Toast.makeText(this, "进入我的关注", Toast.LENGTH_SHORT).show();
	}
//	void google_map_init()
//	{
//		mFragmentMap = (MapFragment)  getFragmentManager().findFragmentById(R.id.map);
//		mGMap = mFragmentMap.getMap();	
//		mGMap.setMyLocationEnabled(true);			
//		mGMap.getUiSettings().setMyLocationButtonEnabled(true);
//		mGMap.getUiSettings().setRotateGesturesEnabled(true);
//		mGMap.getUiSettings().setCompassEnabled(true);
//	}
//	private GoogleMap mGMap;
//	private MapFragment mFragmentMap;
}
