package com.example.nulsehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.qr_codescan.PatientInfo;

public class PatientInfo_beiyong extends Activity {

	private Button bttnQiangMain;
	private TextView tvDrug1;
	private TextView tvDrug2;
	private TextView tvDrug3;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patient_info_beiyong);

		InitView();

		tvDrug1.setText("500ml生理盐水 + 50g葡萄糖注射液");
		tvDrug2.setText("500ml生理盐水 + 25g葡萄糖注射液");
		tvDrug3.setText("500ml生理盐水 + 阿霉素");

		bttnQiangMain.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(PatientInfo_beiyong.this, PatientInfo.class);
				startActivity(intent);
			}
		});
	}

	private void InitView() {
		// TODO 自动生成的方法存根

		bttnQiangMain = (Button) findViewById(R.id.bttnQiangMain);
		tvDrug1 = (TextView) findViewById(R.id.tvDrug1);
		tvDrug2 = (TextView) findViewById(R.id.tvDrug2);
		tvDrug3 = (TextView) findViewById(R.id.tvDrug3);

	}

}
