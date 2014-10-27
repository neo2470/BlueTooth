package hk.com.alex.adapter;


import hk.com.alex.bluetooth.R;
import hk.com.alex.entity.Bluetooth;
import hk.com.alex.entity.Group;

import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BluetoothGroupListAdapter extends BaseExpandableListAdapter {
	
	public BluetoothGroupListAdapter(Context context) {
		this.context = context;
		discoveryStatus = -1;
	}
	
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	public void setBtAdapter(BluetoothAdapter btAdapter) {
		this.btAdapter = btAdapter;
	}
	
	public void setDiscoveryStatus(int discoveryStatus) {
		this.discoveryStatus = discoveryStatus;
		notifyDataSetChanged();
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		
		int childCount = groups.get(groupPosition).getDevices().size();
		
		if(context.getString(R.string.group_my_devices).equals(groups.get(groupPosition).getName())) {
			childCount = 1;
		}
		
		return childCount;
	}
	
	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		View view = null;
		
		if(convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.list_bluetooth_group_item, parent, false);
		} else {
			view = convertView;
		}
		
		view.setVisibility(View.VISIBLE);
		TextView groupName = (TextView) view.findViewById(R.id.groupName);
		
		// 分组名称
		Group group = groups.get(groupPosition);
		groupName.setText(group.getName());
		
		// 设置分组是否显示
//		if(group.isShow()) {
//			view.setVisibility(View.VISIBLE);
//		} else {
//			view.setVisibility(View.GONE);
//		}
		
		// 此处尚不完善
		discoveryDevices(view, group);
		
		return view;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		View view = null;
		
		if(convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.list_bluetooth_item, parent, false);
			
		} else {
			view = convertView;
		}
		
		TextView deviceName = (TextView) view.findViewById(R.id.deviceName);
		TextView deviceInfo = (TextView) view.findViewById(R.id.deviceInfo);
		
		Group group = groups.get(groupPosition);
		
		// 我的设备
		if(context.getString(R.string.group_my_devices).equals(group.getName())) {
			deviceName.setText(btAdapter.getName());
			deviceInfo.setText(btAdapter.getAddress());
		}
		
		if(group.isShow()) {
			Bluetooth bluetooth = (Bluetooth) group.getDevices().get(childPosition);
			BluetoothDevice btDevice = bluetooth.getDevice();
			deviceName.setText(btDevice.getName());
			deviceInfo.setText(btDevice.getAddress());
		}
		
		return view;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	private void discoveryDevices(View view, Group group) {
		
		// 只对可用设备进行处理
		if(!context.getString(R.string.group_available_devices).equals(group.getName())) {
			return ;
		}
		
		ProgressBar progress = (ProgressBar) view.findViewById(R.id.groupProgress);
		switch (discoveryStatus) {
			case -1:// 默认状态
				view.setVisibility(View.GONE);
				break;
			case 0:// 开始搜索蓝牙
				view.setVisibility(View.VISIBLE);
				progress.setVisibility(View.VISIBLE);
				break;
			case 1:// 搜索蓝牙完毕
				view.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				break;
		}
	}
	
	private int discoveryStatus;
	private Context context;
	private BluetoothAdapter btAdapter;
	private List<Group> groups;
}