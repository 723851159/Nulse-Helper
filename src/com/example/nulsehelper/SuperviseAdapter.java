package com.example.nulsehelper;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class SuperviseAdapter extends BaseAdapter {

	LayoutInflater inflater;
	Context context;
	List<Infusion> supervises;
	SClickListener sClickListener;

	public SuperviseAdapter(Context context, List<Infusion> supervises) {
		this.inflater = LayoutInflater.from(context);
		this.supervises = supervises;

	}

	@Override
	public int getCount() {
		Infusion infusion = supervises.get(0);
		if (infusion.getName().equals("error")) {

			return 0;

		} else {
			return supervises.size();
		}

	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		Infusion infusion = supervises.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.patient_supervise_listview,
					null);

			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
			holder.txtLocation = (TextView) convertView
					.findViewById(R.id.txtLocation);
			holder.bttnCha = (ImageButton) convertView
					.findViewById(R.id.bttnCha);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		long current_time = System.currentTimeMillis();

		long end_time = Long.parseLong(infusion.getEndtime()) * 1000;

		long time = (end_time - current_time) / 60000;

		Log.e("currenttime=", "" + current_time);

		Log.e("endtime=", "" + end_time);

		if ((end_time) <= 60000) {
			holder.txtTime.setText((end_time - current_time) / 1000 + "秒钟");
		} else {
			holder.txtTime.setText("" + time + "分钟");
		}

		holder.txtName.setText(infusion.getName());
		holder.txtLocation.setText(infusion.getLocation());
		holder.bttnCha.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				sClickListener.onChaClick(position, holder.bttnCha);

			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView txtName;
		TextView txtLocation;
		TextView txtTime;
		ImageButton bttnCha;
	}

	public void setInterface(SClickListener sClickListener) {
		this.sClickListener = sClickListener;
	}

	public interface SClickListener {
		public void onChaClick(int position, ImageButton bttnCha);

	}

}
