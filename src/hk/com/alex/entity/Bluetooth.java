package hk.com.alex.entity;

import android.bluetooth.BluetoothDevice;

public class Bluetooth extends Device {
	
	public Bluetooth(BluetoothDevice device) {
		this(device, true);
	}
	
	public Bluetooth(BluetoothDevice device, boolean isRemote) {
		super(isRemote);
		this.device = device;
	}
	
	public BluetoothDevice getDevice() {
		return device;
	}

	private BluetoothDevice device;
}