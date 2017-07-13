package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Handler;
import android.util.Log;

public class HttpUpdate extends Thread {

	private String urlString;
	private Handler handler = new Handler();

	public HttpUpdate(Handler handler, String uRlString) {
		this.handler = handler;
		this.urlString = uRlString;
	}

	@Override
	public void run() {

		try {
			URL httpUrl = new URL(urlString);
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

			final String result = sb.toString();
			handler.post(new Runnable() {

				@Override
				public void run() {
					Log.e("info", "result=" + result);
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
