package hk.com.alex.frag;

import hk.com.alex.act.BaseActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 允许使用ActionBar
		setHasOptionsMenu(true);
		
		act = (BaseActivity) getActivity();
	}
	
	protected BaseActivity act;
}