package com.lzlstudio.lzl_dinner;

import java.util.ArrayList;

import com.lzlstudio.lzl_dinner.R;
import com.lzlstudio.lzl_dinner.datadefine.MenuData;
import com.lzlstudio.lzl_dinner.datadefine.MenuData.MenuItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class OrderedMenuListFragment extends DialogFragment {
	
	static public OrderedMenuListFragment newInstance(ArrayList<MenuData.MenuItem>  list)
	{
		OrderedMenuListFragment fragment = new OrderedMenuListFragment();
		//
		Bundle bundle = new Bundle();
		bundle.putSerializable("list", list);
		fragment.setArguments(bundle);
		//
		return fragment;
	}
	//
	private ArrayList<MenuData.MenuItem> mDataList;
	private ListView mLv;
	OrderedMenuListAdapter menuItemListAdapter;
	@SuppressWarnings("unchecked")
	@Override
	public void  onCreate (Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(null != bundle)
		{
			mDataList = (ArrayList<MenuItem>) bundle.getSerializable("list");
		}
		//
		setStyle(STYLE_NO_TITLE, 0);		
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		if(getDialog() == null) return;
		//getDialog().getWindow().setLayout((int) getResources().getDimension(R.dimen.ordered_menu_list_window_width), LayoutParams.MATCH_PARENT);	//@zh 设置DialogFragment的尺寸
		//
		Window wd = getDialog().getWindow();
		Resources rs = getResources();
		WindowManager.LayoutParams lp = wd.getAttributes();
		lp.width = (int) rs.getDimension(R.dimen.ordered_menu_list_window_width);
		lp.x = (int) rs.getDimension(R.dimen.ordered_menu_list_window_x);
		wd.setAttributes(lp);
	}

	@Override
	public View  onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.ordered_menu_list, container, false);
		//
		mLv = (ListView) v.findViewById(R.id.ordered_menu_list_order_list);
		//mLv.addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.ordered_menu_list_header, null), null, false);
		mLv.setAdapter(menuItemListAdapter);
		//
		return v;
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		menuItemListAdapter = new OrderedMenuListAdapter(activity);
	}
	//
	static class ViewHolder
	{
		int dataPositon;
		View rootView;
		//
		CheckBox checkBox;
		ImageView img;
		TextView title;
		TextView item_price;
		Button   item_add_bt;
		Button   item_minus_bt;
		TextView item_count;
		TextView total_price;
	}

	class OrderedMenuListAdapter extends BaseAdapter
	{
		Context mContext;
		private LayoutInflater mInflater;
		
		public OrderedMenuListAdapter(Context context)
		{
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}
		
		@Override
		public int getCount() {
			return mDataList.size();
		}

		@Override
		public Object getItem(int position) {
			return mDataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mDataList.get(position).id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MenuData.MenuItem item = mDataList.get(position);
			ViewHolder holder;
			if(null == convertView)
			{
				convertView = mInflater.inflate(R.layout.ordered_menu_list_item, null);
				holder = new ViewHolder();
				holder.rootView = convertView;
				holder.checkBox = (CheckBox) convertView.findViewById(R.id.ordered_menu_list_item_cb);
				holder.img = (ImageView) convertView.findViewById(R.id.ordered_menu_list_item_pic);
				holder.title = (TextView) convertView.findViewById(R.id.ordered_menu_list_item_title);
				holder.item_price = (TextView) convertView.findViewById(R.id.ordered_menu_list_item_price);
				holder.item_add_bt = (Button) convertView.findViewById(R.id.ordered_menu_list_item_plus_bt);
				holder.item_minus_bt = (Button) convertView.findViewById(R.id.ordered_menu_list_item_minus_bt);
				holder.item_count = (TextView) convertView.findViewById(R.id.ordered_menu_list_item_count);
				holder.total_price = (TextView) convertView.findViewById(R.id.ordered_menu_list_item_total_price);
				//
				convertView.setTag(holder);
			}
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
			//
			holder.checkBox.setChecked(true);
			ImageLoader.getInstance().displayImage(item.img, holder.img);
			holder.title.setText(item.title);
			holder.item_price.setText("￥"+item.price);
			holder.item_count.setText(""+1);
			holder.total_price.setText("￥"+item.price*1);
			//
			return convertView;
		}
	}
}
