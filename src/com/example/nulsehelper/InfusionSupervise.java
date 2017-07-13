package com.example.nulsehelper;

import java.nio.channels.ScatteringByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.example.nulsehelper.ReFlashListView.IReflashListener;
import com.example.nulsehelper.SuperviseAdapter.SClickListener;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class InfusionSupervise extends Activity implements IReflashListener {

	ReFlashListView lvSupervise;

	SuperviseAdapter adapter;

	SClickListener sClickListener = new SClickListener() {

		@Override
		public void onChaClick(int position, ImageButton bttnCha) {

			bttnCha.setEnabled(false);

			Intent intent = new Intent(InfusionSupervise.this,
					InfusionSuperviseDetail.class);
			intent.putExtra("infusion_id", supervises.get(position)
					.getInfusion_id());
			intent.putExtra("end_time", supervises.get(position).getEndtime());

			startActivity(intent);
			InfusionSupervise.this.finish();

		}
	};
	IReflashListener iReflashListener;

	private List<Infusion> supervises = new ArrayList<Infusion>();

	private Button bttnReturn;

	private String infusion_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.patient_supervise);

		SysApplication.getInstance().addActivity(this);

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		/*
		 * try { String jsonString =
		 * "{\"infusion_id\":\"1\",\"location\":\"1\",\"name\":\"\u674e\u534e\"}"
		 * ;
		 * 
		 * String jsonnewString =
		 * "{\"infusion\":[ {\"infusion_id\": \"1\",\"name\": \"李华\",\"location\": \"4\",\"end_time\": \"1439705444\"},{\"infusion_id\": \"2\",\"name\": \"李华\",\"location\": \"5\",\"end_time\": \"1439705144\"}]}"
		 * ; JSONObject infusionObject = new JSONObject(jsonString); String name
		 * = infusionObject.getString("name"); String location =
		 * infusionObject.getString("location"); String infusion_id =
		 * infusionObject.getString("infusion_id");
		 * 
		 * Infusion infusion = new Infusion(); infusion.setName(name);
		 * infusion.setInfusion_id(infusion_id); infusion.setLocation(location);
		 * infusion.setTime("5");
		 * 
		 * supervises.add(infusion); } catch (JSONException e) { // TODO
		 * 自动生成的catch 块 e.printStackTrace(); }
		 */

		initview();

		setdata();

	}

	private void setdata() {
		new Thread() {

			public void run() {

				long currentTime = System.currentTimeMillis();

				final String jsonnewString = "{\"infusion\":[ {\"infusion_id\": \"1\",\"name\": \"李华\",\"location\": \"4\",\"end_time\": \"1439820542000\"},{\"infusion_id\": \"2\",\"name\": \"王秀梅\",\"location\": \"5\",\"end_time\": \"1439820442000\"}]}";

				long current_time = System.currentTimeMillis() / 1000;

				final String result = HttpRequest.GetAllInfusions(""
						+ current_time);

				InfusionSupervise.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						supervises = ParseJson.GetInfusions(result);

						adapter = new SuperviseAdapter(InfusionSupervise.this,
								supervises);

						adapter.setInterface(sClickListener);

						lvSupervise.setAdapter(adapter);

					}
				});

			};

		}.start();
	}

	private void initview() {
		// TODO 自动生成的方法存根

		bttnReturn = (Button) findViewById(R.id.bttnReturn);

		lvSupervise = (ReFlashListView) findViewById(R.id.lvSupervise);

		lvSupervise.setInterface(this);

		bttnReturn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(InfusionSupervise.this,
						MainMenu.class);
				startActivity(intent);
				InfusionSupervise.this.finish();
			}
		});
	}

	@Override
	public void onReflash() {

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				setdata();

				lvSupervise.reflashComplete();
			}
		}, 1000);

	}

}
