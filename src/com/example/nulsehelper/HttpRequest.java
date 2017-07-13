package com.example.nulsehelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class HttpRequest {

	/*
	 * 判断护士登陆密码是否正确
	 */
	public static String ifNurseLoginRight(final String login_id,
			final String password) {
		return HttpRequest.sendGet(
				"http://1.syxt.applinzi.com/nurse-part/nurseLogin.php", "id="
						+ login_id + "&pw=" + password);
	}

	/*
	 * 获得infusion_id
	 */
	public static String GetInfusion_id(final String barcode) {
		return HttpRequest.sendGet(
				"http://1.syxt.applinzi.com/nurse-part/commitP.php",
				"patient_barcode=" + barcode);
	}

	/*
	 * 修改密码
	 */
	public static String UpdatePw(final String id, final String odlpw,
			final String newpw) {
		return HttpRequest.sendGet(
				"http://1.syxt.applinzi.com/nurse-part/pwChange.php", "id=" + id
						+ "&pw=" + odlpw + "&npw=" + newpw);
	}

	/*
	 * 获取接收到的呼叫的病人信息
	 */
	public static String GetPatientInfoJson(final String infusion_id) {

		return HttpRequest.sendGet(
				"http://1.syxt.applinzi.com/nurse-part/patientInfo.php",
				"infusion_id=" + infusion_id);
	}

	/*
	 * 获取呼叫状态
	 */
	public static String GetIfCalling(final String infusion_id) {

		return HttpRequest.sendGet(
				"http://1.syxt.applinzi.com/nurse-part/called.php",
				"infusion_id=" + infusion_id);

	}

	/*
	 * 获取处理状态
	 */
	public static String GetIfAnswered(String infusion_id, String nurse_id) {

		return HttpRequest.sendGet(
				"http://1.syxt.applinzi.com/nurse-part/answer.php",
				"infusion_id=" + infusion_id + "&nurse_id=" + nurse_id);
	}

	/*
	 * 获取所有在等的输液信息
	 */
	public static String GetAllInfusions(String seconds) {
		// TODO 自动生成的方法存根
		return HttpRequest.sendGet(
				"http://1.syxt.applinzi.com/nurse-part/monitor.php", "ctime="
						+ seconds);
	}

	/*
	 * 获取呼叫病人的ids when status=1
	 */
	public static String GetScanResult(final String infusion_id) {
		// TODO 自动生成的方法存根
		return HttpRequest.sendGet(
				"http://1.syxt.applinzi.com/nurse-part/patientInfo.php",
				"infusion_id=" + infusion_id);
	}

	/*
	 * 获取
	 */
	public static String GetNurseScore(String nurse_id) {

		return HttpRequest.sendGet(
				"http://1.syxt.applinzi.com/nurse-part/nurseScore.php",
				"nurse_id=" + nurse_id);

	}

	/*
	 * 获取病人身份信息是否正确 （根据barcode）
	 */
	public static String GetInfusionSum() {
		String ifPatientRight = "{ \"sum\": 2}";

		return HttpRequest.sendGet(
				"http://1.syxt.applinzi.com/nurse-part/waiting.php", "null");
	}

	/*
	 * 获取呼叫病人的所有注射药品的配方（按顺序）
	 */
	public static String[] GetMedicalNames(int patient_id) {
		// TODO 自动生成的方法存根
		String[] MedicalNames = new String[] { "无药品", "无药品",
				"500ml生理盐水 + 25g葡萄糖注射液", "500ml生理盐水 + 氟氯西林",
				"500ml生理盐水 +注射用拉氧头孢（噻吗灵）" };
		return MedicalNames;
	}

	/*
	 * 获取呼叫病人的当前注射药品的编号
	 */
	public static int GetMedicalCurrent(int patient_id) {
		// TODO 自动生成的方法存根
		int MedicalCurrent = 1;
		return MedicalCurrent;
	}

	/*
	 * 获取呼叫病人的当前注射药品是否正确
	 */
	public static String ifMedicalCurrentRight(int patient_id, String bar_code) {
		// TODO 自动生成的方法存根
		String ifMedicalCurrentRight = "true";
		return ifMedicalCurrentRight;
	}

	/*
	 * 更新呼叫病人的当前注射药品的编号
	 */
	public static void UpdateMedicalCurrent(int patient_id, int medical_current) {
		// TODO 自动生成的方法存根
	}

	/*
	 * 更新呼叫病人的当前注射药品的开始时间，结束时间
	 */
	public static void UpdateMedicalTime(int patient_id, int medical_current,
			String CurrentTime, String IdleTime) {
		// TODO 自动生成的方法存根
	}

	/*
	 * 更新呼叫病人的当前注射状态
	 */
	public static void UpdatePatientStatus(int patient_id) {
		// TODO 自动生成的方法存根
		int PatientStatus = 1;
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = null;
			if (param.equals("null")) {
				urlNameString = url;
			} else {

				urlNameString = url + "?" + param;
			}

			Log.e("infourlNameString", urlNameString);
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		Log.e("result=", result);
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
