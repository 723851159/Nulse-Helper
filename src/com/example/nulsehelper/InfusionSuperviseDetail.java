package com.example.nulsehelper;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.example.qr_codescan.PatientInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InfusionSuperviseDetail extends Activity {

	private Button bttnReturn;
	private TextView tvName;
	private TextView tvLocation;
	private TextView tvGender;
	private TextView tvAge;
	private TextView tvThisMedical;
	private TextView tvNextMedical;
	private TextView tvMarkResult;
	private TextView txtEndtime;
	private String infusion_id;
	private String endtime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.patient_supervise_detail);

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		initView();

		Bundle data = getIntent().getExtras();
		infusion_id = data.getString("infusion_id");
		endtime = data.getString("end_time");

		long current_time = System.currentTimeMillis() / 1000;

		long end_time = Long.parseLong(endtime);

		long time = (end_time - current_time) / 60;

		txtEndtime.setText("" + time);

		bttnReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(InfusionSuperviseDetail.this,
						InfusionSupervise.class);
				intent.putExtra("infusion_id", infusion_id);
				startActivity(intent);
				InfusionSuperviseDetail.this.finish();
			}
		});

		new Thread() {
			public void run() {
				final String jsonString = HttpRequest
						.GetPatientInfoJson(infusion_id);

				InfusionSuperviseDetail.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {

						try {
							JSONObject infusion = new JSONObject(jsonString);

							String name = infusion.getString("name");
							String location = infusion.getString("location");
							String gender = infusion.getString("gender");
							String currentmedical = infusion
									.getString("current");
							String next = infusion.getString("next");
							String mark = infusion.getString("mark");
							String age = infusion.getString("age");
							String medicalBarcode = infusion
									.getString("medicalBarcode");

							tvName.setText(name);
							tvLocation.setText(location);
							tvGender.setText(gender);
							tvAge.setText(age);
							tvThisMedical.setText(currentmedical);
							tvNextMedical.setText(next);
							tvMarkResult.setText(mark);
							if (medicalBarcode.equals("NULL")) {

								tvNextMedical.setText("无");
							} else {
								tvNextMedical.setText(next);
							}

						} catch (JSONException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}

					}
				});

			};

		}.start();

	}

	private void initView() {
		tvName = (TextView) findViewById(R.id.tvName);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvGender = (TextView) findViewById(R.id.tvGender);
		tvAge = (TextView) findViewById(R.id.tvAge);

		tvThisMedical = (TextView) findViewById(R.id.tvThisMedical);
		tvNextMedical = (TextView) findViewById(R.id.tvNextMedical);
		bttnReturn = (Button) findViewById(R.id.bttnReturn);
		tvMarkResult = (TextView) findViewById(R.id.tvMarkResult);
		txtEndtime = (TextView) findViewById(R.id.txtEndtime);

	}

}
