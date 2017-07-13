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
					ShowToast("���벻��Ϊ��", UpdatePw.this);
				} else if (!(etNewpassword.getText().toString()
						.equals(etNewpassword2.getText().toString()))) {
					etNewpassword.setText("");
					etNewpassword2.setText("");
					etOldpassword.setText("");
					ShowToast("�������������벻һ�£����������룡", UpdatePw.this);
				} else {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							UpdatePw.this);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setMessage("ȷ���޸�������?");
					builder.setTitle("��ʾ");
					builder.setPositiveButton(
							"ȷ��",
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
																		"�޸ĳɹ����´ε�¼��ʹ�������룡",
																		UpdatePw.this);
															} else {
																etNewpassword
																		.setText("");
																etNewpassword2
																		.setText("");
																etOldpassword
																		.setText("");
																ShowToast(
																		"�޸�ʧ�ܣ���ȷ�Ͼ�������ȷ��",
																		UpdatePw.this);

															}

														}
													});

										};

									}.start();

								}
							});
					builder.setNegativeButton(
							"ȡ��",
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
