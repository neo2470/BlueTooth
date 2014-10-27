package hk.com.alex.entity;

import java.util.ArrayList;
import java.util.List;

public class Group {

	public Group() {
		this("");
	}

	public Group(String name) {
		this.name = name;
		id = count++;
		devices = new ArrayList<Device>();
	}

	public int getId() {
		return id;
	}

	public int getGroupCount() {
		return count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 该分组是否显示
	 * @return
	 */
	public boolean isShow() {
		return show;
	}
	
	/**
	 * 设置该分组是否显示
	 * @param show
	 */
	public void setShow(boolean show) {
		this.show = show;
	}
	
	public ArrayList<Device> getDevices() {
		return devices;
	}
	
	public void add(Device device) {
		devices.add(device);
		if(devices.size() > 0) {
			show = true;
		}
	}

	private int id;
	private static int count;
	private String name;
	private boolean show;// 是否显示该分组
	private ArrayList<Device> devices;
}