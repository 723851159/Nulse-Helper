package com.example.nulsehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomePage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {

				Intent mainIntent = new Intent(WelcomePage.this, Login.class);
				mainIntent.putExtra("ifQuit", "false");
				startActivity(mainIntent);

				WelcomePage.this.finish();
			}
		}, 1000);
	}
}
