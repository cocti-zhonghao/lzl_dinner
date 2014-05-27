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

import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuItemGridFragment extends Fragment implements OnItemClickListener {
	GridView gv;
	MenuItemListAdapter menuItemListAdapter;
	//ArrayList<MenuData.MenuItem> menuItemList;//@zh
	LoadTask loadTask = new LoadTask();
	int m_category_id;	
	
	//
	boolean cartClicked = false;
	
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
		
		Resources r = getResources(); 	
		gv.setColumnWidth((int)r.getDimension(R.dimen.menu_item_width));

		
		gv.setPadding(16, 0, 0, 0);
		gv.setHorizontalSpacing(10);
		gv.setVerticalSpacing(10);
		gv.setAdapter(menuItemListAdapter);		
		//
		gv.setOnItemClickListener(this);
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
		int dataPositon;
		View rootView;
		//
		ImageView img;
		TextView title;
		TextView show_price;
		TextView sale_price;
		RelativeLayout cart;
		TextView cart_num;
	}

	class MenuItemListAdapter extends BaseAdapter implements View.OnClickListener
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
				//((ViewGroup)holder.rootView).requestDisallowInterceptTouchEvent(true);//@zh TODO
				//
				holder.img = (ImageView) holder.rootView.findViewById(R.id.menu_content_item_img);
				holder.title = (TextView) holder.rootView.findViewById(R.id.menu_content_item_name);
				holder.show_price = (TextView) holder.rootView.findViewById(R.id.menu_content_item_show_price);
				holder.sale_price = (TextView) holder.rootView.findViewById(R.id.menu_content_item_sale_price);
				holder.cart = (RelativeLayout) holder.rootView.findViewById(R.id.menu_content_item_cart);
				holder.cart_num = (TextView) holder.rootView.findViewById(R.id.menu_content_item_cart_num);	
				//
				holder.cart.setOnClickListener(this);
				//
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
			holder.dataPositon = position;
			ImageLoader.getInstance().displayImage(item.img, holder.img, options);
			holder.title.setText(item.title);
			if(item.original_price > 0)
			{
				holder.show_price.setVisibility(View.VISIBLE);
				holder.show_price.setText("原价：￥ "+item.original_price);
				holder.show_price.setTextColor(0xffaeaeae);
				int flag = holder.show_price.getPaint().getFlags();				
				holder.show_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|flag);
			}
			else if(item.special_price > 0) 
			{
				holder.show_price.setVisibility(View.VISIBLE);
				holder.show_price.setText("会员价：￥ "+item.special_price);
				holder.show_price.setTextColor(0xff63c93f);
				int flag = holder.show_price.getPaint().getFlags();
				holder.show_price.getPaint().setFlags((~Paint.STRIKE_THRU_TEXT_FLAG)&flag);				
			}
			else 
			{
				holder.show_price.setVisibility(View.INVISIBLE);
			}
			//
			holder.sale_price.setText(item.price+"元/例");
			//
			return holder.rootView;
		}
		//



		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.menu_content_item_cart)
			{
				RelativeLayout parent = (RelativeLayout) v.getParent();
				ViewHolder vh = (ViewHolder) parent.getTag();
				onAddToCart(menuItemList.get(vh.dataPositon), v);
			}
		}
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
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		MenuItemDetailFragment detail = MenuItemDetailFragment.newInstance((MenuData.MenuItem)menuItemListAdapter.getItem(position));
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		detail.show(ft, null);
	}
	
	public void onAddToCart(MenuData.MenuItem item, View v)
	{
		TextView tv = (TextView) v.findViewById(R.id.menu_content_item_cart_num);
		tv.setVisibility(View.VISIBLE);
		tv.startAnimation(new CartAnimation());
	}
	
	static class CartAnimation extends Animation
	{
		private int mWidth;
		private int mHeight;
		@Override
		public void initialize(int width, int height, int parentWidth, int parentHeight)
		{
			mWidth = width;
			mHeight = height;
			
			super.initialize(width, height, parentWidth, parentHeight);
			setDuration(100);
			setFillAfter(true);
			setInterpolator(new LinearInterpolator());
		}
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t)
		{
			final Matrix matrix = t.getMatrix();
			matrix.setScale(interpolatedTime, interpolatedTime, mWidth/2, mHeight/2);
		}
	}
}
