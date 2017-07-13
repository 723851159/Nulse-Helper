package com.example.nulsehelper;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

public class MyReceiver extends BroadcastReceiver {

	public final static String TAG = "JPushTest";

	private static String infusion_idString;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction());

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			System.out.println("�յ����Զ�����Ϣ����Ϣ�����ǣ�"
					+ bundle.getString(JPushInterface.EXTRA_MESSAGE));

			try {
				JSONObject infusionObject = new JSONObject(
						bundle.getString(JPushInterface.EXTRA_MESSAGE));
				String type = infusionObject.getString("type");
				String name = infusionObject.getString("name");
				String location = infusionObject.getString("location");
				String infusion_id = infusionObject.getString("infusion_id");
				String time = infusionObject.getString("time");

				infusion_idString = infusion_id;

				Log.e("info infusion_idString", "===" + infusion_idString);

				if (type.equals("1")) {
					PatientCalling.names.add(name);
					PatientCalling.locations.add(location);
					PatientCalling.times.add(time);
					PatientCalling.infusion_ids.add(infusion_id);
				} else if (type.equals("0")) {

					PatientCalling.names.remove(name);
					Log.e("rec", "remove name success! name.size="
							+ PatientCalling.names.size());
					PatientCalling.locations.remove(location);

					Log.e("rec", "remove location success! name.size="
							+ PatientCalling.locations.size());
					/*
					 * PatientCalling.times .remove(PatientCalling.times.size()
					 * - 1);
					 */
					PatientCalling.infusion_ids.remove(infusion_id);
					Log.e("rec", "remove infusion_id success! name.size="
							+ PatientCalling.infusion_ids.size());

					JPushInterface.clearAllNotifications(context);

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			// �Զ�����Ϣ����չʾ��֪ͨ������ȫҪ������д����ȥ����

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			System.out.println("�յ���֪ͨ");

			new Thread() {

				public void run() {
					final String result = HttpRequest
							.GetIfCalling(infusion_idString);
					Log.e("info result=", result);
				};

			}.start();

			// �����������Щͳ�ƣ�������Щ��������
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			System.out.println("�û��������֪ͨ");
			// ����������Լ�д����ȥ�����û���������Ϊ
			Intent i = new Intent(context, PatientCalling.class); // �Զ���򿪵Ľ���
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else {
			Log.d(TAG, "Unhandled intent - " + intent.getAction());
		}
	}
}
