package com.example.strangetalk;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;

public class MarkerClick implements OnMarkerClickListener {

	@Override
	public boolean onMarkerClick(Marker marker) {
		//System.out.println("************************************************");
		marker.showInfoWindow();
		
//		for(int i=0;i<10;i++){
//			marker.showInfoWindow();
//			try {
//				Thread.sleep(3000);
//				marker.hideInfoWindow();
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		return false;
	}

	
//	static void doClick(){
//		new Mark
//	}

}
