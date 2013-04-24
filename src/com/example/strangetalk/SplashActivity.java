package com.example.strangetalk;

import java.util.Timer;
import java.util.TimerTask;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashActivity extends Activity {
	
	private long splashDelay = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				finish();
				Intent mainIntent = new Intent().setClass(SplashActivity.this, MainActivity.class);
				startActivity(mainIntent);
			}
			
		};
		
		Timer timer = new Timer();
		
		Typewriter writer = new Typewriter(this);
        setContentView(writer);

        //Add a character every 150ms
        writer.setCharacterDelay(250);
        writer.animateText("Place Yourself !!!!");
		
		timer.schedule(task, splashDelay);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}

