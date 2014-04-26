package com.lzlstudio.lzl_dinner;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeTitleBarFragment extends Fragment {
	@Override
	public void  onCreate (Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View  onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_home_title_bar, container, false);
		return v;
	}
	@Override
	public void  onPause () 
	{
		super.onPause();
	}
}
