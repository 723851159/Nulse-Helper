package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nulsehelper.Infusion;
import com.example.nulsehelper.ParseJson;
import com.example.nulsehelper.PatientCalling;

public class HttpAfterScan extends Thread {

	String url;
	Handler handler = new Handler();
	Context context;
	String Medcialbarcode;

	EditText etIdleTime;
	TextView txMinutes;

	public HttpAfterScan(String url, Handler handler, String barcode,
			Context context, EditText etIdleTime, TextView txMinutes) {
		this.url = url;
		this.handler = handler;
		this.Medcialbarcode = barcode;
		this.context = context;
		this.etIdleTime = etIdleTime;
		this.txMinutes = txMinutes;
	}

	@Override
	public void run() {

		try {
			URL httpUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) httpUrl
					.openConnection();
			connection.setReadTimeout(5000);
			connection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String string;
			final StringBuffer sb = new StringBuffer();
			while ((string = reader.readLine()) != null) {
				sb.append(string);
			}

			final String InfusionJson = sb.toString();
			handler.post(new Runnable() {

				@Override
				public void run() {

					JSONObject infusion;
					try {
						infusion = new JSONObject(InfusionJson);
						if (Medcialbarcode.equals(infusion
								.getString("medicalBarcode"))) {

							int Current = Integer.parseInt(infusion
									.getString("currentID")) + 1;
							String Text = "此药品为" + infusion.getString("name")
									+ "的第" + Current
									+ "袋药品，药品核对成功！请输入预估时间后点击完成!";
							ShowToast(Text, context);

							etIdleTime.setVisibility(View.VISIBLE);
							txMinutes.setVisibility(View.VISIBLE);

						} else {
							ShowToast("药品识别失败！", context);
						}
					} catch (JSONException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}

				}
			});

		} catch (MalformedURLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	private static void ShowToast(Object msg, Context context) {
		Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
	}
}
