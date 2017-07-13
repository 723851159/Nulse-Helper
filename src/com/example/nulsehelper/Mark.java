package com.example.nulsehelper;

import cn.jpush.android.api.JPushInterface;

import com.example.qr_codescan.PatientInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class Mark extends Activity {

	private Button bttnReturn;
	private Button bttnMark;
	private CheckBox label1;
	private CheckBox label2;
	private CheckBox label3;
	private CheckBox label4;
	private CheckBox label5;
	private CheckBox label6;
	private CheckBox label7;
	private String infusion_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mark);

		SysApplication.getInstance().addActivity(this);

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		Bundle data = getIntent().getExtras();
		infusion_id = data.getString("infusion_id");

		initView();

		initListener();

	}

	private void getCheckedIds() {
		if (label1.isChecked()) {
			PatientInfo.CheckedIds[0] = 1;
		}
		if (label2.isChecked()) {
			PatientInfo.CheckedIds[1] = 1;
		}
		if (label3.isChecked()) {
			PatientInfo.CheckedIds[2] = 1;
		}
		if (label4.isChecked()) {
			PatientInfo.CheckedIds[3] = 1;
		}
		if (label5.isChecked()) {
			PatientInfo.CheckedIds[4] = 1;
		}
		if (label6.isChecked()) {
			PatientInfo.CheckedIds[5] = 1;
		}
		if (label7.isChecked()) {
			PatientInfo.CheckedIds[6] = 1;
		}

	}

	private void initListener() {

		bttnReturn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < PatientInfo.CheckedIds.length; i++) {
					PatientInfo.CheckedIds[i] = 0;
				}

				Intent intent = new Intent(Mark.this, PatientInfo.class);
				intent.putExtra("infusion_id", infusion_id);
				intent.putExtra("isMark", false);
				startActivity(intent);

				Mark.this.finish();
			}
		});

		bttnMark.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getCheckedIds();
				Intent intent = new Intent(Mark.this, PatientInfo.class);
				intent.putExtra("infusion_id", infusion_id);
				intent.putExtra("isMark", true);
				startActivity(intent);
				Mark.this.finish();
			}
		});
	}

	private void initView() {

		bttnReturn = (Button) findViewById(R.id.bttnReturn);
		bttnMark = (Button) findViewById(R.id.bttnMark);
		label1 = (CheckBox) findViewById(R.id.label1);
		label2 = (CheckBox) findViewById(R.id.label2);
		label3 = (CheckBox) findViewById(R.id.label3);
		label4 = (CheckBox) findViewById(R.id.label4);
		label5 = (CheckBox) findViewById(R.id.label5);
		label6 = (CheckBox) findViewById(R.id.label6);
		label7 = (CheckBox) findViewById(R.id.label7);
	}

}
