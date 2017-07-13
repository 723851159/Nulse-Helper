package com.example.qr_codescan;

import http.HttpAfterScan;
import http.HttpPatient;
import http.HttpUpdate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.security.auth.PrivateCredentialPermission;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.example.nulsehelper.HttpRequest;
import com.example.nulsehelper.Infusion;
import com.example.nulsehelper.MainMenu;
import com.example.nulsehelper.Mark;
import com.example.nulsehelper.ParseJson;
import com.example.nulsehelper.PatientCalling;
import com.example.nulsehelper.R;
import com.example.nulsehelper.SysApplication;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PatientInfo extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 1;
	/**
	 * 显示扫描结果
	 */
	private TextView tvName;
	private TextView tvLocation;
	private TextView tvGender;
	private TextView tvAge;

	private TextView tvThisMedical;
	private TextView tvNextMedical;

	private EditText etIdleTime;
	private TextView txMinutes;

	private Button bttnComplete;
	private Button ScanButton;
	private Button bttnMark;
	private TextView tvMarkResult;

	private Button bttnReturn;

	private String infusion_id;

	public static int[] CheckedIds = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0 };

	private String[] marks = new String[] { "需做皮试", "药物过敏", "心肺疾病", "特殊病史",
			"穿刺困难", "情绪不定", "病情严重", "态度恶劣", "特殊照顾" };
	Handler handler = new Handler();

	private String markresultString = "";
	private String getmarkString = "";

	private boolean ifLastMedical = false;

	private boolean ifMark;

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patient_info);
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		SysApplication.getInstance().addActivity(this);

		Bundle data = getIntent().getExtras();
		infusion_id = data.getString("infusion_id");
		ifMark = data.getBoolean("isMark");

		sharedPreferences = getSharedPreferences("DoingInfusion", MODE_PRIVATE);
		editor = sharedPreferences.edit();
		editor.putBoolean("IfNotDone", true);
		editor.putString("infusion_id", infusion_id);
		editor.commit();

		Log.e("ifnotdone=",
				"" + sharedPreferences.getBoolean("IfNotDone", false));

		initView();

		for (int i = 0; i < CheckedIds.length; i++) {
			if (CheckedIds[i] == 1) {
				if (markresultString.equals("")) {
					markresultString = markresultString + marks[i];
				}

				else {
					markresultString = markresultString + "," + marks[i];
				}

			}
		}

		new Thread() {
			public void run() {
				final String jsonString = HttpRequest
						.GetPatientInfoJson(infusion_id);

				PatientInfo.this.runOnUiThread(new Runnable() {

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
							getmarkString = mark;

							String age = infusion.getString("age");
							String medicalBarcode = infusion
									.getString("medicalBarcode");

							if (medicalBarcode.equals("NULL")) {

								ifLastMedical = true;
							} else {
								ifLastMedical = false;
							}

							tvName.setText(name);
							tvLocation.setText(location);
							tvGender.setText(gender);
							tvAge.setText(age);
							tvThisMedical.setText(currentmedical);
							tvNextMedical.setText(next);

							if (ifMark) {
								tvMarkResult.setText(markresultString);

							} else {
								tvMarkResult.setText(mark);
							}
							// if (markresultString.equals("")) {
							// tvMarkResult.setText(mark);
							// } else {
							// tvMarkResult.setText(markresultString);
							// }
						} catch (JSONException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}

					}
				});

			};

		}.start();

		ScanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PatientInfo.this, MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});

		bttnComplete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				bttnComplete.setEnabled(false);
				// bttnMark.setEnabled(false);
				bttnReturn.setEnabled(false);
				if (ifLastMedical) {

					ShowToast("处理完成！", PatientInfo.this);
					sharedPreferences = getSharedPreferences("DoingInfusion",
							MODE_PRIVATE);
					editor = sharedPreferences.edit();
					editor.clear();
					int Done = sharedPreferences.getInt("Done", 73);

					sharedPreferences = getSharedPreferences("DoingInfusion",
							MODE_PRIVATE);
					editor = sharedPreferences.edit();
					editor.putBoolean("IfNotDone", false);
					editor.putInt("Done", Done + 1);

					editor.commit();

					try {
						String markString;
						if (ifMark) {
							markString = markresultString;
						} else {
							markString = getmarkString;
						}
						String urlString2 = "http://1.syxt.applinzi.com/nurse-part/commit.php?infusion_id="
								+ infusion_id
								+ "&mark="
								+ URLEncoder.encode(markString, "utf-8")
								+ "&time=999";
						new HttpUpdate(handler, urlString2).start();

						Intent intent = new Intent(PatientInfo.this,
								MainMenu.class);
						startActivity(intent);
						PatientInfo.this.finish();
					} catch (UnsupportedEncodingException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				} else {

					if (!(etIdleTime.isShown())) {

						ShowToast("请先扫描输液袋条形码", PatientInfo.this);

					} else if (etIdleTime.getText().toString().equals("")) // 分钟数)
					{
						ShowToast("请先输入预估时间", PatientInfo.this);

					} else {

						sharedPreferences = getSharedPreferences(
								"DoingInfusion", MODE_PRIVATE);
						editor = sharedPreferences.edit();
						editor.clear();
						int Done = sharedPreferences.getInt("Done", 73);

						sharedPreferences = getSharedPreferences(
								"DoingInfusion", MODE_PRIVATE);
						editor = sharedPreferences.edit();
						editor.putBoolean("IfNotDone", false);

						editor.putInt("Done", Done + 1);
						editor.commit();

						ShowToast("处理完成！", PatientInfo.this);

						String idleTimeString = etIdleTime.getText().toString();

						// 更新数据 包括 传递 start_time, end_time,medical_current自动加1？
						String urlString2;
						try {
							String markString;

							if (markresultString.equals("")) {
								markString = tvMarkResult.getText().toString();

							} else {
								markString = markresultString;
							}

							urlString2 = "http://1.syxt.applinzi.com/nurse-part/commit.php?infusion_id="
									+ infusion_id
									+ "&mark="
									+ URLEncoder.encode(markString, "utf-8")
									+ "&time=" + idleTimeString;
							new HttpUpdate(handler, urlString2).start();

							Intent intent = new Intent(PatientInfo.this,
									MainMenu.class);
							startActivity(intent);
							PatientInfo.this.finish();

						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		bttnReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						PatientInfo.this);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setMessage("尚未处理完，确认退出吗?");
				builder.setTitle("提示");
				builder.setPositiveButton("确认",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								Intent intent = new Intent();
								intent.setClass(PatientInfo.this,
										MainMenu.class);
								startActivity(intent);
								PatientInfo.this.finish();
							}
						});
				builder.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();

			}
		});

		bttnMark.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				for (int i = 0; i < CheckedIds.length; i++) {
					CheckedIds[i] = 0;
				}
				Intent intent = new Intent();
				intent.setClass(PatientInfo.this, Mark.class);
				intent.putExtra("infusion_id", infusion_id);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					PatientInfo.this);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setMessage("尚未处理完，确认退出吗?");
			builder.setTitle("提示");
			builder.setPositiveButton("确认",
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Intent intent = new Intent();
							intent.setClass(PatientInfo.this, MainMenu.class);
							/*
							 * if (MainMenu.bttnRemind.isShown()) {
							 * MainMenu.bttnRemind
							 * .setVisibility(View.INVISIBLE); } else {
							 * MainMenu.bttnRemind.setVisibility(View.VISIBLE);
							 * 
							 * }
							 */

							startActivity(intent);
							PatientInfo.this.finish();
						}
					});
			builder.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
		return true;
	}

	private void initView() {
		tvName = (TextView) findViewById(R.id.tvName);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvGender = (TextView) findViewById(R.id.tvGender);
		tvAge = (TextView) findViewById(R.id.tvAge);

		tvThisMedical = (TextView) findViewById(R.id.tvThisMedical);
		tvNextMedical = (TextView) findViewById(R.id.tvNextMedical);

		etIdleTime = (EditText) findViewById(R.id.etIdleTime);
		txMinutes = (TextView) findViewById(R.id.txMinutes);

		bttnComplete = (Button) findViewById(R.id.bttnComplete);
		ScanButton = (Button) findViewById(R.id.button1);

		bttnReturn = (Button) findViewById(R.id.bttnReturn);
		bttnMark = (Button) findViewById(R.id.bttnMark);

		tvMarkResult = (TextView) findViewById(R.id.tvMarkResult);

	}

	private static void ShowToast(Object msg, Context context) {
		Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
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

				/*
				 * String urlString =
				 * "http://1.syxt.sinaapp.com/nurse-part/patientInfo.php?infusion_id=1"
				 * ; new HttpAfterScan(urlString, handler,
				 * bundle.getString("result"), PatientInfo.this, etIdleTime,
				 * txMinutes).start();
				 */

				new Thread() {

					public void run() {

						final String result = HttpRequest
								.GetScanResult(infusion_id);

						Log.e("result=", result);
						PatientInfo.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {

								try {
									JSONObject infusion = new JSONObject(result);

									Log.e("medicalBarcode=",
											bundle.getString("result"));

									if (bundle
											.getString("result")
											.equals(infusion
													.getString("medicalBarcode"))) {

										int Current = Integer.parseInt(infusion
												.getString("currentID")) + 1;
										String Text = "此药品为"
												+ infusion.getString("name")
												+ "的第" + Current
												+ "袋药品，药品核对成功！请输入预估时间后点击完成!";
										ShowToast(Text, PatientInfo.this);

										etIdleTime.setVisibility(View.VISIBLE);
										txMinutes.setVisibility(View.VISIBLE);

									} else {
										ShowToast("药品识别错误，请确保药品为下一袋！",
												PatientInfo.this);
									}
								} catch (JSONException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}

							}
						});

					};

				}.start();

				// 显示
			}
			break;
		}
	}
}
