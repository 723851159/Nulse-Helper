package com.example.nulsehelper;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.example.nulsehelper.InfusionAdapter.PClickListener;
import com.example.nulsehelper.ReFlashListView.IReflashListener;
import com.example.qr_codescan.MipcaActivityCapture;
import com.example.qr_codescan.PatientInfo;

public class PatientCalling extends Activity implements PClickListener,
		IReflashListener {

	private final static int SCANNIN_GREQUEST_CODE = 1;

	private Button bttnReturn;

	public static List<String> names = new ArrayList<String>();
	public static List<String> locations = new ArrayList<String>();
	public static List<String> times = new ArrayList<String>();
	public static List<String> infusion_ids = new ArrayList<String>();

	private String infuision_idString = null;

	private boolean ifnull;

	InfusionAdapter adapter;
	ReFlashListView lvMainInfo;

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patient_calling);
		SysApplication.getInstance().addActivity(this);
		initView();

		onReflash();

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

	}

	private void initView() {

		adapter = new InfusionAdapter(PatientCalling.this, names, locations,
				times, infusion_ids);

		adapter.setInterface(this);

		lvMainInfo = (ReFlashListView) findViewById(R.id.lvMainInfo);
		lvMainInfo.setInterface(this);

		lvMainInfo.setAdapter(adapter);

		bttnReturn = (Button) findViewById(R.id.bttnReturn);

		bttnReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PatientCalling.this, MainMenu.class);
				startActivity(intent);
				PatientCalling.this.finish();
			}
		});
	}

	@Override
	public void onQiangClick(final int position, ImageButton bttnQiang) {

		bttnQiang.setEnabled(false);
		new Thread() {
			public void run() {

				sharedPreferences = getSharedPreferences("UserInfo",
						MODE_PRIVATE);
				editor = sharedPreferences.edit();
				editor.clear();
				String nurse_id = sharedPreferences.getString("nurse_id", "1");

				if (position >= infusion_ids.size()) {
					ifnull = true;
				} else {
					ifnull = false;
					infuision_idString = infusion_ids.get(infusion_ids.size()
							- 1 - position);
				}

				Log.e("infusionid==========", infuision_idString);
				Log.e("nurese_id==========", nurse_id);

				final String result = HttpRequest.GetIfAnswered(
						infuision_idString, nurse_id);

				Log.e("result=====", result);
				PatientCalling.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {

						if (result.equals("answerd") || names.size() == 0) {

							ShowToast("该呼叫已经被处理！", PatientCalling.this);
							onReflash();

						} else /* if (result.equals("true")) */ {

							ShowToast("接收呼叫成功!", PatientCalling.this);

							sharedPreferences = getSharedPreferences(
									"DoingInfusion", MODE_PRIVATE);
							editor = sharedPreferences.edit();
							editor.putString("infusion_id", infuision_idString);
							editor.putBoolean("IfDone", false);

							editor.commit();

							Intent intent = new Intent();
							intent.setClass(PatientCalling.this,
									PatientInfo.class);
							intent.putExtra("infusion_id", infuision_idString);
							startActivity(intent);
							PatientCalling.this.finish();

						}

					}
				});

			};
		}.start();

	}

	@Override
	public void onReflash() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				initView();

				lvMainInfo.reflashComplete();
			}
		}, 1000);

	}

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == RESULT_OK) {
				final Bundle bundle = data.getExtras();
				// 显示扫描到的内容

				new Thread() {

					public void run() {
						final String infusion_idString = HttpRequest
								.GetInfusion_id(bundle.getString("result"));
						PatientCalling.this.runOnUiThread(new Runnable() {

							public void run() {

								Intent intent = new Intent(PatientCalling.this,
										PatientInfo.class);
								intent.putExtra("infusion_id",
										infusion_idString);
								startActivity(intent);
								PatientCalling.this.finish();
							}
						});

					};
				}.start();

				// 显示
			}
			break;
		}
	}

	private static void ShowToast(Object msg, Context context) {
		Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
	}

}
