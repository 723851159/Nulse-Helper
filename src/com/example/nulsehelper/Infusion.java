package com.example.nulsehelper;

import android.util.Log;

public class Infusion {

	private String name;
	private String location;

	private String time;
	private String infusion_id;

	private String gender;
	private String current;

	private String currentID;
	private String next;

	private String medicalBarcode;
	private String mark;

	private String age;
	private String endtime;

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public Infusion() {

	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		Log.e("info", "success!name");
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getInfusion_id() {
		return infusion_id;
	}

	public void setInfusion_id(String infusion_id) {
		this.infusion_id = infusion_id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getCurrentID() {
		return currentID;
	}

	public void setCurrentID(String currentID) {
		this.currentID = currentID;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getMedicalBarcode() {
		return medicalBarcode;
	}

	public void setMedicalBarcode(String medicalBarcode) {
		this.medicalBarcode = medicalBarcode;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

}
