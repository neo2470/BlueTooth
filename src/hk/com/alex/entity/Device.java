package hk.com.alex.entity;

/**
 * 设备信息
 * @author Alex
 * @time 2014/10/12
 *
 */
public class Device {
	
	public Device() {
		this(true);
	}
	
	public Device(boolean isRemote) {
		markName = "";
		this.isRemote = isRemote;
	}
	
	/**
	 * 取得设备的备注名称
	 * @return 返回设备的备注名称，如果没有设置返回“”
	 */
	public String getMarkName() {
		return markName;
	}

	/**
	 * 设置设备的备注名称
	 * @param markName 备注名称
	 */
	public void setMarkName(String markName) {
		this.markName = markName;
	}

	/**
	 * 是否是远程设备
	 * @return 是，返回true; 否，返回false; 系统默认为true
	 */
	public boolean isRemote() {
		return isRemote;
	}

	/**
	 * 设置是否是远程设备
	 * @param isRemote true，是; false，否; 系统默认为true
	 */
	public void setRemote(boolean isRemote) {
		this.isRemote = isRemote;
	}

	protected String markName;// 备注名称
	protected boolean isRemote;// 是否是远程设备
}