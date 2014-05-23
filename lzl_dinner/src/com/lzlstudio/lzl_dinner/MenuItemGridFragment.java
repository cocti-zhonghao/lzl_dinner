package com.lzlstudio.lzl_dinner;

import java.util.ArrayList;

import com.lzlstudio.lzl_dinner.dao.MenuDetailDao;
import com.lzlstudio.lzl_dinner.dao.ResourceStatusDao;
import com.lzlstudio.lzl_dinner.dao.LzlDBTable.NoRecordException;
import com.lzlstudio.lzl_dinner.dao.ResourceStatusDao.MenuStatus;
import com.lzlstudio.lzl_dinner.datadefine.MenuData;
import com.lzlstudio.lzl_dinner.util.MyThumbnailTool;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuItemGridFragment extends Fragment {
	GridView gv;
	MenuItemListAdapter menuItemListAdapter;
	//ArrayList<MenuData.MenuItem> menuItemList;//@zh
	LoadTask loadTask = new LoadTask();
	int m_category_id;	
	
	static public MenuItemGridFragment newInstance(int category_id)
	{
		MenuItemGridFragment fragment = new MenuItemGridFragment();
		fragment.setCategoryId(category_id);
		//
		Bundle bundle = new Bundle();
		bundle.putInt("category_id", category_id);
		fragment.setArguments(bundle);
		//
		return fragment;
	}
	
	public void setCategoryId(int category_id)
	{
		m_category_id = category_id;
	}
	
	public int getCategoryId()
	{
		return m_category_id;
	}
	
	@Override
	public void  onCreate (Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(null != bundle)
		{
			m_category_id = bundle.getInt("category_id");
		}
		loadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	@Override
	public View  onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.common_grid_layout, container, false);
		v.setBackgroundColor(0xffe4e3e4);
		//
		gv = (GridView) v.findViewById(R.id.common_grid_content);
		gv.setNumColumns(3);
		gv.setColumnWidth(257);
		
		gv.setPadding(16, 0, 0, 0);
		gv.setHorizontalSpacing(10);
		gv.setVerticalSpacing(10);
		gv.setAdapter(menuItemListAdapter);		
		
		//
		return v;
	}
	//called before onCreate
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		menuItemListAdapter = new MenuItemListAdapter(activity);
	}
	//
	static class ViewHolder
	{
		View rootView;
	}

	static class MenuItemListAdapter extends BaseAdapter
	{
		Context mContext;
		ArrayList<MenuData.MenuItem> menuItemList;
		private LayoutInflater mInflater;
		DisplayImageOptions options = new DisplayImageOptions.Builder().postProcessor(new MyThumbnailTool(257, 286)).build();//@zh TODO db->pixel
		
		public MenuItemListAdapter(Context context, ArrayList<MenuData.MenuItem> list)
		{
			super();
			mContext = context;
			menuItemList = list;
			mInflater = LayoutInflater.from(mContext);
		}
		
		public MenuItemListAdapter(Context context)
		{
			super();
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}
		
		public MenuItemListAdapter()
		{
			super();
		}
		
		public void setContext(Context context) 
		{
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}
		
		public void updateData(ArrayList<MenuData.MenuItem> list)
		{
			menuItemList = list;
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			return null==menuItemList ? 0 : menuItemList.size();
		}

		@Override
		public Object getItem(int position) {
			return menuItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return menuItemList.get(position).id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			MenuData.MenuItem item = menuItemList.get(position);
			if(null == convertView)
			{
				//
				holder = new ViewHolder();
				holder.rootView = mInflater.inflate(R.layout.menu_content_item, null);
				holder.rootView.setTag(holder);
				//
				Resources r = mContext.getResources(); 				
				holder.rootView.setLayoutParams(new GridView.LayoutParams((int)r.getDimension(R.dimen.menu_item_width),(int)r.getDimension(R.dimen.menu_item_height)));
			}			
			else 
			{
				holder = (ViewHolder) convertView.getTag();				
			}
			//
			ImageView iv = (ImageView) holder.rootView.findViewById(R.id.menu_content_item_detail_img);
			ImageLoader.getInstance().displayImage(item.img, iv, options);
			TextView name = (TextView) holder.rootView.findViewById(R.id.menu_content_item_name);
			name.setText(item.title);
			TextView oPrice = (TextView) holder.rootView.findViewById(R.id.menu_content_item_show_price);
			if(item.original_price > 0)
			{
				oPrice.setText("原价：￥ "+item.original_price);
				oPrice.setTextColor(0xffaeaeae);
				int flag = oPrice.getPaint().getFlags();				
				oPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|flag);
			}
			else if(item.special_price > 0) 
			{
				oPrice.setText("特价：￥ "+item.special_price);
				oPrice.setTextColor(0xff63c93f);
				int flag = oPrice.getPaint().getFlags();
				name.getPaint().setFlags((~Paint.STRIKE_THRU_TEXT_FLAG)&flag);				
			}
			
			//
			return holder.rootView;
		}
		//
	}
	//
	static class LoadResult
	{
		int retCode;
		ArrayList<MenuData.MenuItem> data;
	}
	class LoadTask extends  AsyncTask<Void, Void, LoadResult>
	{

		@Override
		protected LoadResult doInBackground(Void... params) {
			LoadResult result = null;
			while(!isCancelled())
			{
				boolean retry = false;
				try {
					MenuStatus ms = ResourceStatusDao.getInstance().getMenuStatus();
					if(ms.status < ResourceStatusDao.STATE_OK) retry = true;
					else 
					{
						result = new LoadResult();
						result.retCode = ms.status;
						if(result.retCode == ResourceStatusDao.STATE_OK)
						{
							result.data = MenuDetailDao.getInstance().getMenuItemList(m_category_id);
						}
						break;
					}
				} catch (NoRecordException e) {
					retry = true;
				}				
				//
				if(retry)
				{
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
			//
			return result;
		}
		
		@Override
		protected void onPostExecute(LoadResult result)
		{
			if(result.data != null)
			{
				menuItemListAdapter.updateData(result.data);
			}
		}
		//
		@Override
		protected void onCancelled()
		{
		}
	}
}
