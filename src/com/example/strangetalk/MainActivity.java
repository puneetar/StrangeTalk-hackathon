package com.example.strangetalk;



import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Random;

import android.telephony.TelephonyManager;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements LocationListener {
	static  LatLng MYLOCATION =null;

	private GoogleMap mMap;
	private LocationManager locationManager;
	private String provider;
	public static Marker myMarker;
	private static String TAG="HTC";
	private Location location;
	private Criteria criteria;
	public TelephonyManager tel;
	public long myID; 
	private String myMessage;

	public static boolean setDrag=false;
	public static double setDragLat,setDragLong;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tel=(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		String portStr=tel.getLine1Number();
		try {
			myID = Long.parseLong(portStr);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			myID = 111111111; 
		}

		myMessage="...";


		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		MarkerClick clickListen=new MarkerClick();
		MarkerDrag dragListen=new MarkerDrag();
		mMap.setOnMarkerClickListener(clickListen);
		mMap.setOnMarkerDragListener(dragListen);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		//locationManager.requestLocationUpdates(provider, 10, 0.5f, this);'
		
//provider="gps";
location = locationManager.getLastKnownLocation(provider);
		onLocationChanged(location);

		myMarker = mMap.addMarker(new MarkerOptions().position(MYLOCATION));
		myMarker.setDraggable(true);



		//		.title("Puneetshbasncbkasjcabsjfanscaklskfajskcn aslkjhajhsfnlamsc aajkfhcakjsc naslknclkasfas"));
		//System.out.println(myMarker);
		//    boolean b= mark.onMarkerClick(hamburg);
		//    boolean b1= mark.onMarkerClick(kiel);
		// Move the camera instantly to hamburg with a zoom of 15.
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MYLOCATION, 15));

		// Zoom in, animating the camera.
		mMap.animateCamera(CameraUpdateFactory.zoomTo(18.75f), 2000, null);

		final EditText editText = (EditText) findViewById(R.id.editText1);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				myMessage=editText.getText().toString();
				if(!myMessage.equals(""))
				{
					new webHandle(mMap, myID, MYLOCATION.latitude, MYLOCATION.longitude, myMessage).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
					onLocationChanged(location);
					myMarker.showInfoWindow();
				}
				editText.setText("");

				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			}
		});
		
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {

				while(true){
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						
					}	
					publishProgress();
					
				}
				//return null;
			}
			
			@Override
			protected void onProgressUpdate(Void... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
				double lat = (double) (location.getLatitude());
				double lng = (double) (location.getLongitude());
				
				if(!setDrag)
					MYLOCATION = new LatLng(lat, lng);
				else
					MYLOCATION=new LatLng(setDragLat,setDragLong);
				// Log.v(TAG, ""+hamburg.toString());
				if(myMarker!=null){
					
					myMarker.setPosition(MYLOCATION);
					myMarker.setTitle(myMessage);
				}
				new webHandle(mMap, myID, MYLOCATION.latitude, MYLOCATION.longitude, myMessage).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
				
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	public void onLocationChanged(Location location) {
		double lat = (double) (location.getLatitude());
		double lng = (double) (location.getLongitude());
		if(!setDrag)
			MYLOCATION = new LatLng(lat, lng);
		else
			MYLOCATION=new LatLng(setDragLat,setDragLong);
		// Log.v(TAG, ""+hamburg.toString());
		if(myMarker!=null){
			myMarker.setPosition(MYLOCATION);
			myMarker.setTitle(myMessage);
			myMarker.setVisible(true);
			
		}
		mMap.moveCamera(CameraUpdateFactory.newLatLng(MYLOCATION));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		System.out.println("yes on BACK pressed*******************************************");
		new webHandle(mMap, myID,MYLOCATION.latitude, MYLOCATION.longitude, "1Exit1").executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
	  //  System.exit(0);
		return;
	}

	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

} 
