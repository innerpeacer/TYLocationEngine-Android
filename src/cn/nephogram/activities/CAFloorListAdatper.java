package cn.nephogram.activities;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.nephogram.ble.R;
import cn.nephogram.mapsdk.data.NPMapInfo;

public class CAFloorListAdatper extends BaseAdapter {
	private Context mContext;
	private List<NPMapInfo> mData;

	public CAFloorListAdatper(Context context) {
		this.mContext = context;
	}

	public void setData(List<NPMapInfo> mData) {
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
