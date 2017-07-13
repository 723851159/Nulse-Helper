package com.example.nulsehelper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ParseJson {

	private static String TAG = "ParseJson";

	public static void ParseInfusion(String InfusionJason, Infusion infusiondata) {

		try {
			JSONObject infusion = new JSONObject(InfusionJason);

			String name = infusion.getString("name");
			String location = infusion.getString("location");
			String time = infusion.getString("time");
			String infusion_id = infusion.getString("infusion_id");
			String gender = infusion.getString("gender");
			String currentmedical = infusion.getString("current");
			String currentID = infusion.getString("currentID");
			String next = infusion.getString("next");
			String medicalBarcode = infusion.getString("medicalBarcode");
			String mark = infusion.getString("mark");

			infusiondata.setName(name);
			Log.e(TAG, "name=" + infusiondata.getName());

			infusiondata.setLocation(location);
			infusiondata.setTime(time);
			infusiondata.setInfusion_id(infusion_id);
			infusiondata.setGender(gender);
			infusiondata.setCurrent(currentmedical);
			infusiondata.setCurrentID(currentID);
			infusiondata.setNext(next);
			infusiondata.setMedicalBarcode(medicalBarcode);
			infusiondata.setMark(mark);

		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	public static List<Infusion> GetInfusions(String InfusionJson) {

		List<Infusion> infusions = new ArrayList<Infusion>();
		try {
			JSONObject object = new JSONObject(InfusionJson);

			JSONArray objects = object.getJSONArray("infusion");

			for (int i = 0; i < objects.length(); i++) {

				JSONObject infusion = objects.getJSONObject(i);

				String name = infusion.getString("name");

				String location = infusion.getString("location");
				String infusion_id = infusion.getString("infusion_id");
				String endtime = infusion.getString("end_time");

				Infusion infusiondata = new Infusion();
				infusiondata.setName(name);
				infusiondata.setLocation(location);
				infusiondata.setEndtime(endtime);
				infusiondata.setInfusion_id(infusion_id);

				infusions.add(infusiondata);
			}

			return infusions;

		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;

	}
}
