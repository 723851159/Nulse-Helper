package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.example.nulsehelper.Infusion;
import com.example.nulsehelper.ParseJson;

public class HttpPatient extends Thread {

	String url;
	Handler handler = new Handler();

	TextView tvName;
	TextView tvLocation;
	TextView tvGender;
	TextView tvAge;
	TextView tvThisMedical;
	TextView tvNextMedical;
	TextView tvMarkResult;

	String markresultString;

	public HttpPatient(String url, Handler handler, TextView tvName,
			TextView tvLocation, TextView tvGender, TextView tvAge,
			TextView tvThisMedical, TextView tvNextMedical,
			TextView tvMarkResult, String markresultString) {
		this.url = url;
		this.handler = handler;
		this.tvName = tvName;
		this.tvLocation = tvLocation;
		this.tvGender = tvGender;
		this.tvAge = tvAge;
		this.tvThisMedical = tvThisMedical;
		this.tvNextMedical = tvNextMedical;
		this.tvMarkResult = tvMarkResult;
		this.markresultString = markresultString;
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

					/*
					 * Infusion infusion = new Infusion(); infusion =
					 * ParseJson.ParseInfusion(InfusionJson);
					 */

					Log.e("info", InfusionJson);
					/* Log.e("info", "name=" + infusion.getName().toString()); */

					/* tvName.setText(infusion.getName()); */

					try {
						JSONObject infusion = new JSONObject(InfusionJson);
						Infusion infusiondata = new Infusion();
						ParseJson.ParseInfusion(InfusionJson, infusiondata);

						infusiondata.setName(infusion.getString("name"));
						tvName.setText(infusiondata.getName());

						Log.e("info:name", infusiondata.getName());

						/* tvName.setText(infusion.getString("name")); */
						tvLocation.setText(infusion.getString("location"));
						tvGender.setText(infusion.getString("gender"));
						tvAge.setText(infusion.getString("age"));
						tvThisMedical.setText(infusion.getString("current"));
						tvNextMedical.setText(infusion.getString("next"));
						if (markresultString.equals("")) {
							tvMarkResult.setText(infusion.getString("mark"));
						} else {
							tvMarkResult.setText(markresultString);
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
}
