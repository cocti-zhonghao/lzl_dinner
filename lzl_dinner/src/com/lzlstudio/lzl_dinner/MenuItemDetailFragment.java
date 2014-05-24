package com.lzlstudio.lzl_dinner;


import com.lzlstudio.lzl_dinner.datadefine.MenuData;
import com.lzlstudio.lzl_dinner.datadefine.MenuData.MenuItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuItemDetailFragment extends DialogFragment implements OnClickListener {
	public interface OrderListener
	{
		public void onOrder(MenuData.MenuItem item);
	}
	//
	MenuData.MenuItem m_item;
	OrderListener m_order_listener;
	static public MenuItemDetailFragment newInstance(MenuData.MenuItem item)
	{
		MenuItemDetailFragment fragment = new MenuItemDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("item", item);
		fragment.setArguments(bundle);
		return fragment;
	}
	//
	@Override
	public void  onCreate (Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(null != bundle)
		{
			m_item = (MenuItem) bundle.getSerializable("item");
		}
		//
		setStyle(STYLE_NO_TITLE, 0);		
	}
	@Override
	public void onStart()
	{
		super.onStart();
		if(getDialog() == null) return;
		getDialog().getWindow().setLayout((int) getResources().getDimension(R.dimen.menu_item_detail_window_width), LayoutParams.MATCH_PARENT);	//@zh 设置DialogFragment的尺寸	
	}
	@Override
	public View  onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.menu_content_item_detail, container, false);
		//
		TextView tv = (TextView) v.findViewById(R.id.menu_content_item_detail_title); tv.setText(m_item.title);
		tv = (TextView) v.findViewById(R.id.menu_content_item_detail_title2); tv.setText(m_item.title);
		ImageView iv = (ImageView) v.findViewById(R.id.menu_content_item_detail_img); ImageLoader.getInstance().displayImage(m_item.img, iv);
		tv = (TextView) v.findViewById(R.id.menu_content_item_detail_price); tv.setText(m_item.price+"元/例");
		tv = (TextView) v.findViewById(R.id.menu_content_item_detail_ori_price);
		if(m_item.original_price > 0)
		{
			tv.setText("原价："+m_item.original_price);
			int flag = tv.getPaint().getFlags();
			tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|flag);				
		}
		else
		{
			tv.setVisibility(View.GONE);
		}
		//
		tv = (TextView) v.findViewById(R.id.menu_content_item_detail_des); tv.setText(m_item.des); tv.setMovementMethod(ScrollingMovementMethod.getInstance());
		Button bt = (Button) v.findViewById(R.id.menu_content_item_detail_order_btn);
		bt.setOnClickListener(this);
		return v;
	}
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try {
			m_order_listener = (OrderListener)activity;
		} catch (Exception e) {
		}
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.menu_content_item_detail_order_btn)
		{
			m_order_listener.onOrder(m_item);
			dismiss();
		}
	}
}
