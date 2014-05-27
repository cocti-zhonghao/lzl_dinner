package com.lzlstudio.lzl_dinner;

import java.util.ArrayList;

import com.lzlstudio.lzl_dinner.dao.MenuCategoryDao;
import com.lzlstudio.lzl_dinner.dao.ResourceStatusDao;
import com.lzlstudio.lzl_dinner.dao.LzlDBTable.NoRecordException;
import com.lzlstudio.lzl_dinner.dao.ResourceStatusDao.MenuStatus;
import com.lzlstudio.lzl_dinner.datadefine.MenuData;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.integer;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//
public class MenuCategoryListFragment extends ListFragment
{	
	public interface CategorySelectListener
	{
		public void onCategoryItemSelected(int position, long id);
	}
	/////
	private CategorySelectListener categorySelectListener;	

	MenuCategoryListAdapter foodCategoryListAdapter;
	LoadTask loadTask = new LoadTask();
	View mCurrentSelectItemView = null;
	int mCurrentSelectItemPos = -1;

	@Override
	public void onAttach(Activity activity) 
	{ 
		super.onAttach(activity);
		try 
		{
			categorySelectListener=(CategorySelectListener)activity;
		} 
		catch (Exception e) 
		{
			//如果activity没有实现CategorySelectListener这个接口 系统会报错
		}
		//
		foodCategoryListAdapter = new MenuCategoryListAdapter(activity);
	}

	@Override
	public void  onCreate (Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//getListView().setSelector(resID);
		//getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		setListAdapter(foodCategoryListAdapter);
		loadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	public void  onListItemClick(ListView l, View v, int position, long id)  
	{
		if(mCurrentSelectItemPos != position)
		{
			ViewHolder holder;
			ImageView iv;
			TextView tv;
			if(mCurrentSelectItemView != null && mCurrentSelectItemPos >= 0)
			{
				holder = (ViewHolder) mCurrentSelectItemView.getTag();
				//iv = (ImageView) mCurrentSelectItemView.findViewWithTag("show_img");
				iv = holder.img;
				ImageLoader.getInstance().displayImage(((MenuData.MenuCategoryItem)l.getItemAtPosition(mCurrentSelectItemPos)).img, iv);
				//tv = (TextView) mCurrentSelectItemView.findViewWithTag("category_title");
				tv = holder.title; tv.setTextColor(0xff666666);
				mCurrentSelectItemView.setBackgroundResource(0);					
			}
			//
			mCurrentSelectItemView = v;
			mCurrentSelectItemPos = position;
			holder = (ViewHolder) mCurrentSelectItemView.getTag();
			//iv = (ImageView) mCurrentSelectItemView.findViewWithTag("show_img");
			iv = holder.img;
			ImageLoader.getInstance().displayImage(((MenuData.MenuCategoryItem)l.getItemAtPosition(mCurrentSelectItemPos)).selected_img, iv);
			//tv = (TextView) mCurrentSelectItemView.findViewWithTag("category_title");
			tv = holder.title; tv.setTextColor(0xffffffff);
			mCurrentSelectItemView.setBackgroundResource(R.drawable.menu_category_item_background);
		}

		//
		categorySelectListener.onCategoryItemSelected(position, id);
		//setSelection(position);
	}
	/////////////////////////////////////////////////////////
	static class ViewHolder
	{
		LinearLayout ll;
		ImageView img;
		TextView title;
	}

	class MenuCategoryListAdapter extends BaseAdapter
	{
		Context mContext;
		ArrayList<MenuData.MenuCategoryItem> categoryList;
		
		public MenuCategoryListAdapter(Context context, ArrayList<MenuData.MenuCategoryItem> list)
		{
			super();
			mContext = context;
			categoryList = list;
		}
		
		public MenuCategoryListAdapter(Context context)
		{
			super();
			mContext = context;
		}
		
		public void updateData(ArrayList<MenuData.MenuCategoryItem> list)
		{
			categoryList = list;
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			return null==categoryList ? 0 : categoryList.size();
		}

		@Override
		public Object getItem(int position) {
			return categoryList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return categoryList.get(position).id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			MenuData.MenuCategoryItem item = categoryList.get(position);
			if(null == convertView)
			{
				//
				LinearLayout ll = new LinearLayout(mContext);
				ll.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 50));
				ll.setOrientation(LinearLayout.HORIZONTAL);
				//
				//
				ImageView iv = new ImageView(mContext); iv.setTag("show_img");
				ll.addView(iv);
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv.getLayoutParams();
				lp.leftMargin = 20;
				lp.gravity = Gravity.CENTER_VERTICAL;
				iv.setLayoutParams(lp);
				//
				TextView tv = new TextView(mContext); tv.setTag("category_title");
				ll.addView(tv);
				lp = (LinearLayout.LayoutParams) tv.getLayoutParams();
				lp.leftMargin = 18;
				lp.gravity = Gravity.CENTER_VERTICAL;
				lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
				tv.setLayoutParams(lp);
				tv.setTextColor(0xff666666);
				tv.setTextSize(18);
				//
				holder = new ViewHolder();
				holder.ll = ll;
				holder.img = iv;
				holder.title = tv;
				holder.ll.setTag(holder);
			}			
			else 
			{
				holder = (ViewHolder) convertView.getTag();				
			}
			//
			if(mCurrentSelectItemView == null && mCurrentSelectItemPos == position)
			{
				ImageLoader.getInstance().displayImage(item.selected_img, holder.img);
				mCurrentSelectItemView = holder.ll;
				holder.ll.setBackgroundResource(R.drawable.menu_category_item_background);
				holder.title.setTextColor(0xffffffff);//白色
			}
			else
			{
				ImageLoader.getInstance().displayImage(item.img, holder.img);
			}			
			holder.title.setText(item.title);
			return holder.ll;
		}
		//
	}
	//
	static class LoadResult
	{
		int retCode;
		ArrayList<MenuData.MenuCategoryItem> data;
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
							result.data = MenuCategoryDao.getInstance().getMenuCategory();
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
				foodCategoryListAdapter.updateData(result.data);
				//
//				View v  = getListView().getChildAt(0);
//				if(null != v)
//				{
//					onListItemClick(getListView(), v, 0, foodCategoryListAdapter.getItemId(0));
//				}
//				else
//				{
//					categorySelectListener.onCategoryItemSelected(0, result.data.get(0).id);
//				}
				mCurrentSelectItemPos = 0;
				categorySelectListener.onCategoryItemSelected(0, result.data.get(0).id);
			}
		}
		//
		@Override
		protected void onCancelled()
		{
		}
	}
}