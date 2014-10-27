package hk.com.alex.frag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hk.com.alex.adapter.BluetoothGroupListAdapter;
import hk.com.alex.bluetooth.R;
import hk.com.alex.entity.Bluetooth;
import hk.com.alex.entity.Group;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothFrag extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.device_bluetooth, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initialize();
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		
		// 检测蓝牙是否打开
		if(!btAdapter.isEnabled()) {
			
			if(btSwitch != null) {
				btSwitch.setChecked(false);
			}
			
			Intent openBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(openBluetooth, REQUEST_ENABLE_BLUETOOTH);
			
		} else {
			
			if(btSwitch != null) {
				btSwitch.setChecked(true);
			}
		}
		
		super.onResume();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		inflater.inflate(R.menu.bluetooth_actions, menu);
		
		// 控制蓝牙的开启与关闭
		btSwitch = (CompoundButton) ((ActionBarActivity) act).getSupportActionBar().getCustomView().findViewById(R.id.deviceSwitch);
		btSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked) {
					
					// 打开蓝牙
					btAdapter.enable();
				} else {
					
					// 关闭蓝牙
					btAdapter.disable();
				}
			}
		});
		
		btSwitch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 开关按钮不可用
				btSwitch.setEnabled(false);
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.bluetoothSearch:
			startDiscovery();
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BLUETOOTH){
			if(resultCode == Activity.RESULT_OK) {
				
				if(!btSwitch.isChecked()) {
					btSwitch.setChecked(true);
				}
				
			} else {
				
				if(btSwitch.isChecked()) {
					btSwitch.setChecked(false);
				}
				
				// 退出App
				act.exit();
			}
		}
	}
	
	@Override
	public void onDestroyView() {
		act.unregisterReceiver(statusReceiver);
		super.onDestroyView();
	}
	
	private void enableDiscoverable() {
	}
	
	private void startDiscovery() {
		btListAdapter.setDiscoveryStatus(0);
		btAdapter.startDiscovery();
	}
	
	private void initialize() {
		
		// 将蓝牙设备分组存储
		if(groups == null) {
			groups = new ArrayList<Group>();
		}
		
		// 取得本机蓝牙
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		
		// 检测设备是否支持蓝牙
		if(btAdapter != null) {
			
			// 我的设备
			Group myDeviceGroup = new Group(act.getString(R.string.group_my_devices));
			groups.add(myDeviceGroup);
		} else {
			
			// 这里要做详细的处理(不支持蓝牙)
			Toast.makeText(act, getString(R.string.bluetooth_is_not_supported), Toast.LENGTH_SHORT).show();
			return ;
		}
		
		// 配对设备
		Group pairedDeviceGroup = new Group(act.getString(R.string.group_paired_devices));
		
		// 读取已经配对的蓝牙设备
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		if(pairedDevices.size() > 0) {
			
			for(BluetoothDevice bt : pairedDevices) {
				Bluetooth bluetooth = new Bluetooth(bt);
				pairedDeviceGroup.add(bluetooth);
			}
		}
		groups.add(pairedDeviceGroup);
		
		Group availableDeviceGroup = new Group(act.getString(R.string.group_available_devices));
		groups.add(availableDeviceGroup);
		
		btStatus = (TextView) act.findViewById(R.id.deviceStatus);
		ExpandableListView devicesList = (ExpandableListView) act.findViewById(R.id.deviceList);
		btListAdapter = new BluetoothGroupListAdapter(act);
		btListAdapter.setBtAdapter(btAdapter);
		btListAdapter.setGroups(groups);
		btListAdapter.setDiscoveryStatus(-1);
		devicesList.setAdapter(btListAdapter);
		
		statusReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				int state = intent.getExtras().getInt(BluetoothAdapter.EXTRA_STATE);
				
				// 监听蓝牙状态变化
				if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
					
					switch (state) {
						case BluetoothAdapter.STATE_OFF:
							btSwitch.setEnabled(true);
							btStatus.setVisibility(View.VISIBLE);
							btStatus.setText(R.string.bluetooth_is_off);
//							hideDevices();
							break;
						case BluetoothAdapter.STATE_TURNING_ON:
							
							if(!btSwitch.isChecked()) {
								btSwitch.setChecked(true);
								btSwitch.setEnabled(false);
							}
							
							btStatus.setText(R.string.bluetooth_is_turning_on);
							break;
						case BluetoothAdapter.STATE_ON:
							btSwitch.setEnabled(true);
							btStatus.setVisibility(View.GONE);
//							showDevices();
							break;
						case BluetoothAdapter.STATE_TURNING_OFF:
							
							if(btSwitch.isChecked()) {
								btSwitch.setChecked(false);
								btSwitch.setEnabled(false);
							}
							
							btStatus.setText(R.string.bluetooth_is_turning_off);
							break;
	
						default:
							break;
					}
				}
			}
		};
		
		IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		act.registerReceiver(statusReceiver, filter1);
		
		btReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				
				if(BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					Bluetooth bt = new Bluetooth(btDevice);
					for(Group g : groups) {
						if(act.getString(R.string.group_available_devices).equals(g.getName())) {
							g.add(bt);
							btListAdapter.notifyDataSetChanged();
							break;
						}
					}
				}
			}
		};
		
		IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		act.registerReceiver(btReceiver, filter2);
	}
	
	private BluetoothAdapter btAdapter;
	private TextView btStatus;
	private CompoundButton btSwitch;
	private List<Group> groups;
	private BroadcastReceiver statusReceiver;
	private BroadcastReceiver btReceiver;
	private BluetoothGroupListAdapter btListAdapter;
	private static final int REQUEST_ENABLE_BLUETOOTH = 0x0001;
}