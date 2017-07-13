package com.example.nulsehelper;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.qr_codescan.PatientInfo;

import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MySpace extends Activity {

	private Button bttnUpdatepw;
	private Button bttnQuit;

	private Button bttnReturn;
	private String name;
	private String account;

	private TextView tvName;
	private TextView tvScore;
	private TextView tvRange;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myspace);

		SysApplication.getInstance().addActivity(this);

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		initView();
		initListener();

		preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
		editor = preferences.edit();
		editor.clear();

		name = preferences.getString("name", "护士");
		account = preferences.getString("account", "701");

		tvName.setText(name);

		new Thread() {

			public void run() {

				final String result = HttpRequest.GetNurseScore(account);

				MySpace.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							JSONObject infusionObject = new JSONObject(result);

							tvScore.setText(infusionObject.getString("score"));
							tvRange.setText(infusionObject.getString("sort"));

						} catch (JSONException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}

					}
				});

			};

		}.start();
	}

	private void initListener() {

		bttnQuit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MySpace.this, Login.class);
				intent.putExtra("ifQuit", "true");
				startActivity(intent);

			}
		});

		bttnUpdatepw.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MySpace.this, UpdatePw.class);
				startActivity(intent);
				MySpace.this.finish();
			}
		});

		bttnReturn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MySpace.this, MainMenu.class);
				startActivity(intent);
				MySpace.this.finish();

			}
		});
	}

	private void initView() {

		bttnUpdatepw = (Button) findViewById(R.id.bttnUpdatepw);
		bttnQuit = (Button) findViewById(R.id.bttnQuit);
		bttnReturn = (Button) findViewById(R.id.bttnReturn);
		tvName = (TextView) findViewById(R.id.tvName);
		tvScore = (TextView) findViewById(R.id.tvScore);
		tvRange = (TextView) findViewById(R.id.tvRange);

	}

}
