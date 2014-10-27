package hk.com.alex.act;

import hk.com.alex.bluetooth.R;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

public class DeviceManager extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_manager);
		
		initialize();
	}
	
	private void initialize() {
		
		// 设置ActionBar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setCustomView(R.layout.action_device_switch);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
	}
}