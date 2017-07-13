package com.example.nulsehelper;

import org.json.JSONException;
import org.json.JSONObject;

import http.HttpAfterScan;

import cn.jpush.android.api.JPushInterface;

import com.example.qr_codescan.MipcaActivityCapture;
import com.example.qr_codescan.PatientInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends Activity {

	private Button bttnScan;
	private Button bttnCall;
	private Button bttnSpace;
	private Button bttnSupervise;

	public static Button bttnRemind;

	private TextView tvDone;
	private int Done;
	private TextView tvDoing;
	private TextView tvName;
	private String name;

	private boolean IfNotDone;

	private String infusion_id;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	private final static int SCANNIN_GREQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		SysApplication.getInstance().addActivity(this);

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
		editor = preferences.edit();
		editor.clear();

		name = preferences.getString("name", "��ʿ");

		preferences = getSharedPreferences("DoingInfusion", MODE_PRIVATE);
		editor = preferences.edit();
		Done = preferences.getInt("Done", 73);
		IfNotDone = preferences.getBoolean("IfNotDone", false);
		infusion_id = preferences.getString("infusion_id", "-1");

		initView();
		if (IfNotDone) {
			ShowToast("���к�����δ���,��������!", MainMenu.this);
			bttnRemind.setVisibility(View.VISIBLE);
			bttnRemind.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					bttnCall.setEnabled(false);
					bttnScan.setEnabled(false);
					bttnSpace.setEnabled(false);
					bttnSupervise.setEnabled(false);
					bttnRemind.setEnabled(false);

					Intent intent = new Intent(MainMenu.this, PatientInfo.class);
					intent.putExtra("infusion_id", infusion_id);
					startActivity(intent);
				}
			});
		}
		initlistener();

		tvName.setText(name);
		tvDone.setText("" + Done);

		new Thread() {

			public void run() {

				final String result = HttpRequest.GetInfusionSum();

				MainMenu.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {

						tvDoing.setText(result);

					}
				});

			};

		}.start();
	}

	private void initlistener() {
		bttnCall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (IfNotDone) {
					Intent intent = new Intent(MainMenu.this, PatientInfo.class);
					intent.putExtra("infusion_id", infusion_id);
					startActivity(intent);
				} else {

					Intent intent = new Intent(MainMenu.this,
							PatientCalling.class);
					startActivity(intent);

				}
			}
		});
		bttnScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainMenu.this, MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});

		bttnSpace.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainMenu.this, MySpace.class);
				startActivity(intent);
			}
		});

		bttnSupervise.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainMenu.this,
						InfusionSupervise.class);
				startActivity(intent);
			}
		});
	}

	private void initView() {

		bttnScan = (Button) findViewById(R.id.bttnScan);
		bttnCall = (Button) findViewById(R.id.bttnCall);
		bttnSpace = (Button) findViewById(R.id.bttnSpace);
		bttnSupervise = (Button) findViewById(R.id.bttnSuperVise);
		tvName = (TextView) findViewById(R.id.tvName);

		tvDone = (TextView) findViewById(R.id.tvDone);
		tvDoing = (TextView) findViewById(R.id.tvDoing);

		bttnRemind = (Button) findViewById(R.id.bttnRemind);

	}

	/*
	 * �������ؼ�
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setMessage("ȷ��Ҫ�˳�΢���ơ���ʿ����?");
			builder.setTitle("��ʾ");
			builder.setPositiveButton("ȷ��",
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

							SysApplication.getInstance().exit();
							// android.os.Process.killProcess(android.os.Process
							// .myPid());
						}
					});
			builder.setNegativeButton("ȡ��",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
		return true;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == RESULT_OK) {
				final Bundle bundle = data.getExtras();
				// ��ʾɨ�赽������

				new Thread() {

					public void run() {
						final String result = HttpRequest.GetInfusion_id(bundle
								.getString("result"));
						MainMenu.this.runOnUiThread(new Runnable() {

							public void run() {

								if (result.equals("null")) {
									ShowToast("�޷���ȡ������ݣ���ȷ����ȷ�����ԣ�",
											MainMenu.this);
								} else if (result.equals("false")) {
									ShowToast("ʶ�������ȷ����ȷ�����ԣ�", MainMenu.this);

								} else {
									ShowToast("��ȡ�����Ϣ�ɹ�������������", MainMenu.this);
									Intent intent = new Intent(MainMenu.this,
											PatientInfo.class);
									intent.putExtra("infusion_id", result);
									startActivity(intent);
									MainMenu.this.finish();
								}

							}
						});

					};
				}.start();

			}
			break;
		}
	}

	private static void ShowToast(Object msg, Context context) {
		Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
	}
}
