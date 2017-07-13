package com.example.nulsehelper;

import cn.jpush.android.api.JPushInterface;
import http.HttpUpdate;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdatePw extends Activity {

	private EditText etOldpassword;
	private EditText etNewpassword;
	private EditText etNewpassword2;
	private Button bttnReturn;
	private Button bttnUpdate;
	Handler handler = new Handler();

	private String student_numberstring;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatepw);

		SysApplication.getInstance().addActivity(this);
		initView();

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		bttnReturn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UpdatePw.this, MySpace.class);
				startActivity(intent);
				UpdatePw.this.finish();
			}
		});

		bttnUpdate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (etNewpassword.getText().toString().equals("")
						|| etNewpassword2.getText().toString().equals("")
						|| etOldpassword.getText().toString().equals("")) {
					etOldpassword.setText("");
					etNewpassword.setText("");
					etNewpassword2.setText("");
					ShowToast("密码不能为空", UpdatePw.this);
				} else if (!(etNewpassword.getText().toString()
						.equals(etNewpassword2.getText().toString()))) {
					etNewpassword.setText("");
					etNewpassword2.setText("");
					etOldpassword.setText("");
					ShowToast("新密码两次输入不一致，请重新输入！", UpdatePw.this);
				} else {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							UpdatePw.this);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setMessage("确定修改密码吗?");
					builder.setTitle("提示");
					builder.setPositiveButton(
							"确认",
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									new Thread() {
										public void run() {

											final String result = HttpRequest
													.UpdatePw(
															"701",
															etOldpassword
																	.getText()
																	.toString(),
															etNewpassword
																	.getText()
																	.toString());

											UpdatePw.this
													.runOnUiThread(new Runnable() {

														@Override
														public void run() {
															if (result
																	.equals("true")) {
																Intent intent = new Intent(
																		UpdatePw.this,
																		MySpace.class);
																startActivity(intent);
																UpdatePw.this
																		.finish();
																ShowToast(
																		"修改成功，下次登录请使用新密码！",
																		UpdatePw.this);
															} else {
																etNewpassword
																		.setText("");
																etNewpassword2
																		.setText("");
																etOldpassword
																		.setText("");
																ShowToast(
																		"修改失败！请确认旧密码正确！",
																		UpdatePw.this);

															}

														}
													});

										};

									}.start();

								}
							});
					builder.setNegativeButton(
							"取消",
							new android.content.DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.create().show();
				}
			}
		});

	}

	private static void ShowToast(Object msg, Context context) {
		Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
	}

	private void initView() {
		etOldpassword = (EditText) findViewById(R.id.etOldPassword);
		etNewpassword = (EditText) findViewById(R.id.etNewPassword);
		etNewpassword2 = (EditText) findViewById(R.id.etNewPassword2);
		bttnReturn = (Button) findViewById(R.id.bttnReturn);
		bttnUpdate = (Button) findViewById(R.id.bttnUpdate);
	}

}
