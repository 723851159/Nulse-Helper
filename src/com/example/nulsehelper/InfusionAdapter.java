package com.example.nulsehelper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class InfusionAdapter extends BaseAdapter {

	LayoutInflater inflater;
	Context context;
	PClickListener iClickListener;

	List<String> names = new ArrayList<String>();
	List<String> locations = new ArrayList<String>();
	List<String> times = new ArrayList<String>();
	List<String> infusion_ids = new ArrayList<String>();

	public InfusionAdapter(Context context, List<String> names,
			List<String> locations, List<String> times,
			List<String> infusion_ids) {

		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.names = names;
		this.locations = locations;
		this.times = times;
		this.infusion_ids = infusion_ids;

	}

	/*
	 * public void onDateChange(List<String> names, List<String> locations,
	 * List<String> times, List<String> infusion_ids) { this.names = names;
	 * this.locations = locations; this.times = times; this.infusion_ids =
	 * infusion_ids; this.notifyDataSetChanged(); }
	 */

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		// return PatientNames.length;

		return infusion_ids.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.patient_calling_listview_item, null);

			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
			holder.txtLocation = (TextView) convertView
					.findViewById(R.id.txtLocation);
			holder.bttnQiang = (ImageButton) convertView
					.findViewById(R.id.bttnQiang);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final int size = infusion_ids.size();

		holder.txtName.setText(names.get(size - 1 - position).toString());
		holder.txtLocation.setText(locations.get(size - 1 - position)
				.toString());

		if (names.size() == 0) {

			holder.txtTime.setText("0分钟前");

		} else {
			String end_timeString = times.get(size - 1 - position).toString();

			long called_time = Long.parseLong(end_timeString) * 1000;

			long current_time = System.currentTimeMillis();

			long time = (current_time - called_time) / 60000;

			if ((current_time - called_time) <= 60000) {
				holder.txtTime.setText((current_time - called_time) / 1000
						+ "秒钟前");
			} else {
				holder.txtTime.setText("" + time + "分钟前");
			}

		}

		// holder.txtName.setText(PatientNames[position]);
		// holder.txtLocation.setText(Location[position]);
		// holder.txtTime.setText(Times[position]);
		holder.bttnQiang.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				iClickListener.onQiangClick(position, holder.bttnQiang);
			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView txtName;
		TextView txtLocation;
		TextView txtTime;
		ImageButton bttnQiang;
	}

	public void setInterface(PClickListener iClickListener) {
		this.iClickListener = iClickListener;
	}

	/**
	 * 开始按钮借口 * @author Administrator
	 */
	public interface PClickListener {
		void onQiangClick(int position, ImageButton bttnQiang);

	}

}
