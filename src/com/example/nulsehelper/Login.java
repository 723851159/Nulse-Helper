package com.example.nulsehelper;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private EditText etZhanghao;
	private EditText etMima;
	private Button bttnLogin;
	private CheckBox cbRemMima;
	private CheckBox cbIfAuto;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	private String ifQuit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Bundle data = getIntent().getExtras();
		ifQuit = data.getString("ifQuit");

		etZhanghao = (EditText) findViewById(R.id.etZhanghao);
		etMima = (EditText) findViewById(R.id.etMima);
		bttnLogin = (Button) findViewById(R.id.bttnLogin);
		cbRemMima = (CheckBox) findViewById(R.id.cbRemMima);
		cbIfAuto = (CheckBox) findViewById(R.id.cbIfAuto);

		cbIfAuto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (cbIfAuto.isChecked()) {
					cbRemMima.setChecked(true);
				}

			}
		});

		preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
		editor = preferences.edit();
		editor.clear();
		boolean IfRemMima = preferences.getBoolean("IfRemMima", false);
		boolean IfAuto = preferences.getBoolean("IfAuto", false);

		if (IfRemMima) {
			etZhanghao.setText(preferences.getString("account", "fault"));
			etMima.setText(preferences.getString("pw", "fault"));
			cbRemMima.setChecked(true);
		}

		bttnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String account = etZhanghao.getText().toString();
				final String pw = etMima.getText().toString();
				if (account == null || account.length() <= 0)
					ShowToast("请输入用户名！", Login.this);
				else if (pw == null || pw.length() <= 0)
					ShowToast("请输入密码！", Login.this);
				else {
					bttnLogin.setEnabled(false);
					new Thread() {
						public void run() {
							final String result = HttpRequest
									.ifNurseLoginRight(account, pw);
							if (!(result.equals("false")))
								Login.this.runOnUiThread(new Runnable() {
									public void run() {
										ShowToast("登陆成功！欢迎使用微点云-护士端！",
												Login.this);
										if (cbRemMima.isChecked() == true) {
											preferences = getSharedPreferences(
													"UserInfo", MODE_PRIVATE);
											editor = preferences.edit();
											editor.putString("account",
													etZhanghao.getText()
															.toString());
											editor.putString("pw", etMima
													.getText().toString());
											editor.putBoolean("IfRemMima", true);

											try {
												JSONObject infusionObject = new JSONObject(
														result);

												editor.putString(
														"name",
														infusionObject
																.getString("name"));
												editor.putString(
														"nurse_id",
														infusionObject
																.getString("id"));
												editor.commit();
											} catch (JSONException e) {
												// TODO 自动生成的 catch 块
												e.printStackTrace();
											}

										} else {
											preferences = getSharedPreferences(
													"UserInfo", MODE_PRIVATE);
											editor = preferences.edit();
											editor.putString("account", "");
											editor.putString("pw", "");
											editor.commit();
										}

										if (cbIfAuto.isChecked() == true) {
											preferences = getSharedPreferences(
													"UserInfo", MODE_PRIVATE);
											editor = preferences.edit();
											editor.putBoolean("IfAuto", true);
											editor.commit();
										} else {

											preferences = getSharedPreferences(
													"UserInfo", MODE_PRIVATE);
											editor = preferences.edit();
											editor.putBoolean("IfAuto", false);
											editor.commit();
										}

										Intent intent = new Intent(Login.this,
												MainMenu.class);
										startActivity(intent);
										Login.this.finish();
									}
								});
							else if (result.equals("false"))
								Login.this.runOnUiThread(new Runnable() {
									public void run() {
										bttnLogin.setEnabled(true);
										ShowToast("用户不存在或者密码错误！", Login.this);
									}
								});
							else
								Login.this.runOnUiThread(new Runnable() {
									public void run() {
										bttnLogin.setEnabled(true);
										ShowToast("登录失败，请重试", Login.this);
									}
								});
						}
					}.start();
				}
			}
		});

		if (IfAuto) {
			etZhanghao.setText(preferences.getString("account", "fault"));
			etMima.setText(preferences.getString("pw", "fault"));
			cbRemMima.setChecked(true);
			cbIfAuto.setChecked(true);

			if (!(ifQuit.equals("true"))) {
				bttnLogin.performClick();
			}

		}
	}

	private static void ShowToast(Object msg, Context context) {
		Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
	}

}
