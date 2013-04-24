package com.example.strangetalk;

import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.Marker;

public class MarkerDrag implements OnMarkerDragListener {

	@Override
	public void onMarkerDrag(Marker marker) {
		// TODO Auto-generated method stub
	//	System.out.println("*****"+marker.getPosition().latitude+" : "+marker.getPosition().longitude);
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		// TODO Auto-generated method stub
		
		System.out.println("*****"+marker.getPosition().latitude+" : "+marker.getPosition().longitude);
		MainActivity.setDrag=true;
		MainActivity.setDragLat=marker.getPosition().latitude;
		MainActivity.setDragLong=marker.getPosition().longitude;
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		// TODO Auto-generated method stub
	//	System.out.println("*****"+marker.getPosition().latitude+" : "+marker.getPosition().longitude);
	}

}
