package com.ty.bleproject.activities;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ty.bleproject.R;
import com.ty.mapsdk.TYMapInfo;

public class FloorListAdatper extends BaseAdapter {
	private Context mContext;
	private List<TYMapInfo> mData;

	public FloorListAdatper(Context context) {
		this.mContext = context;
	}

	public void setData(List<TYMapInfo> mData) {
		this.mData = mData;
	}

	public int getCount() {
		return mData.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(mContext);
		convertView = inflater.inflate(R.layout.list_item_floor, null);

		TextView tv = (TextView) (convertView
				.findViewById(R.id.list_item_floor));
		tv.setText(mData.get(position).getFloorName());
		tv.setTextSize(20);
		return convertView;
	}

}
