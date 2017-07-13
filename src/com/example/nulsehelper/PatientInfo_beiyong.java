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

		tvDrug1.setText("500ml������ˮ + 50g������ע��Һ");
		tvDrug2.setText("500ml������ˮ + 25g������ע��Һ");
		tvDrug3.setText("500ml������ˮ + ��ù��");

		bttnQiangMain.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(PatientInfo_beiyong.this, PatientInfo.class);
				startActivity(intent);
			}
		});
	}

	private void InitView() {
		// TODO �Զ����ɵķ������

		bttnQiangMain = (Button) findViewById(R.id.bttnQiangMain);
		tvDrug1 = (TextView) findViewById(R.id.tvDrug1);
		tvDrug2 = (TextView) findViewById(R.id.tvDrug2);
		tvDrug3 = (TextView) findViewById(R.id.tvDrug3);

	}

}
