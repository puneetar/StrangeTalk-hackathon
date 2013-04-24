package com.example.strangetalk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

public class webHandle extends AsyncTask<Void, String, Void>{

	private static HashMap<Long, Marker> markerID = new HashMap<Long, Marker>();
	long id;
	double latt;
	double longit;
	String msg;
	GoogleMap mMap;
	public webHandle(GoogleMap Map, long id, double lattitude, double longitude, String message)
	{
		this.id = id;
		this.latt = lattitude;
		this.longit = longitude;
		this.msg = message;
		this.mMap = Map;
	}

	@Override
	protected Void doInBackground(Void... params) {
		sendData(id, latt, longit, msg);
		return null;
	}

	public void sendData(long id, double lattitude, double longitude, String message)
	{
		HttpClient httpclient = new DefaultHttpClient();
		//This is the data to send
		String latt = ""+lattitude;
		String longit = "" + longitude;

		//String url = "http://ubhack.co.nf/default.php?" + "id=1234567890" + "&latt=" + latt + "&longit=" 
		// 	+ longit + "&msg=" + message;
		HttpPost httppost = new HttpPost("http://ubhack.co.nf/insertNsend.php");
		try{
			List<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>(4);
			namevaluepairs.add(new BasicNameValuePair("id", "" + id));
			namevaluepairs.add(new BasicNameValuePair("latt", latt));
			namevaluepairs.add(new BasicNameValuePair("longit", longit));
			namevaluepairs.add(new BasicNameValuePair("msg", message));
			httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs));


			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpclient.execute(httppost, responseHandler);
			String result = Html.fromHtml(response).toString();

		
			publishProgress(result);
			if(message.equals("1Exit1"))
				System.exit(0);
			System.out.println("result = "+result);

		}catch(Exception e)
		{
			System.out.println("Web HAndle exception!!!");
			e.printStackTrace();
		}
	}

	@Override
	protected void onProgressUpdate(String... strings ){
		super.onProgressUpdate(strings[0]);
		String result=strings[0];
		//MainActivity.myMarker.showInfoWindow();
		//	TextView textView = (TextView) findViewById(R.id.textView1);
		//textView.append(strings[0] + "\n");
		if(result.equals(""))
			System.out.println("Empty");
		else
		{
			try {
				JSONArray jarr = new JSONArray(result);
				System.out.println("Result = 11111");
				//					MainActivity.globalLat = new ArrayList<Double>();
				//					MainActivity.globalLong = new ArrayList<Double>();
				//					MainActivity.globalMsgs = new ArrayList<String>();
				//System.out.println(jarr.get(0));

				for(int i=0;i<jarr.length();i++)
				{
					JSONObject jobj = jarr.getJSONObject(i);
					long rID = jobj.getLong("ID");
					double lat = jobj.getDouble("lattitude");
					double longt = jobj.getDouble("longitude");
					String msg = jobj.getString("message");
					System.out.println("id = "+rID+"lat = "+lat+" long = "+longt+" msg = "+msg);

					
					if(msg.equals("1Exit1")){
						System.out.println("(****yes I am inn nnnnn *****************************");
						if(markerID.get(new Long(rID))!=null){
							markerID.get(new Long(rID)).remove();
							
							
							markerID.remove(rID);
						}
					
					}
					else if(markerID.containsKey(new Long(rID)))
					{
						Marker mark=markerID.get(new Long(rID));
						mark.setPosition(new LatLng(lat, longt));
						if(!(mark.getTitle().equals(msg)))
						{

							mark.setTitle(msg);
							mark.setVisible(true);
							mark.showInfoWindow();
						}

						markerID.put(new Long(rID), mark);
					}
					else{
						Marker mark=mMap.addMarker(new MarkerOptions().position(new LatLng(lat, longt)).title(msg));
						mark.setVisible(true);
						mark.showInfoWindow();
						markerID.put(new Long(rID), mark);
					}
					//mMap.addMarker(new MarkerOptions().position(obj).snippet(msg));

					//						MainActivity.globalLat.add(jobj.getDouble("lattitude"));
					//						MainActivity.globalLong.add(jobj.getDouble("longitude"));
					//						MainActivity.globalMsgs.add(jobj.getString("message"));
				}


			} catch (Exception e) {
				System.out.println("JSON Exception");
				e.printStackTrace();
			}
		}
		return;
	}

}
