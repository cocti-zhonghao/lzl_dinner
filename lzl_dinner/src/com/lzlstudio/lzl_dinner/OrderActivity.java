package com.lzlstudio.lzl_dinner;

import java.util.ArrayList;

import com.lzlstudio.lzl_dinner.MenuCategoryListFragment.CategorySelectListener;
import com.lzlstudio.lzl_dinner.dao.LzlDBTable.NoRecordException;
import com.lzlstudio.lzl_dinner.dao.MenuCategoryDao;
import com.lzlstudio.lzl_dinner.dao.ResourceStatusDao;
import com.lzlstudio.lzl_dinner.dao.ResourceStatusDao.MenuStatus;
import com.lzlstudio.lzl_dinner.datadefine.MenuData;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class OrderActivity extends Activity implements CategorySelectListener {
	
	ListFragment foodCategoryList;
	MenuCategoryListAdapter foodCategoryListAdapter = new MenuCategoryListAdapter(this);
	LoadTask loadTask = new LoadTask();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		//
		foodCategoryList = (MenuCategoryListFragment) getFragmentManager().findFragmentById(R.id.order_food_category_list_fragment);
		foodCategoryList.setListAdapter(foodCategoryListAdapter);
		//foodCategoryList.getListView().setSelector(R.drawable.menu_category_item_background);
		//foodCategoryList.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		//
		loadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//		//TODO
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.menu_item_list_container, new MenuItemGridFragment());
		ft.commit();
		//
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		loadTask.cancel(true);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	//
	static class ViewHolder
	{
		LinearLayout ll;
		ImageView img;
		TextView title;
	}

	static class MenuCategoryListAdapter extends BaseAdapter
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
				ll.setBackgroundResource(R.drawable.menu_category_item_background);
				//
				ImageView iv = new ImageView(mContext);
				ll.addView(iv);
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv.getLayoutParams();
				lp.leftMargin = 20;
				lp.gravity = Gravity.CENTER_VERTICAL;
				iv.setLayoutParams(lp);
				//
				TextView tv = new TextView(mContext);
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
			ImageLoader.getInstance().displayImage(item.img, holder.img);
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
			}
		}
		//
		@Override
		protected void onCancelled()
		{
		}
	}
	//
	@Override
	public void onCategoryItemSelected(int position, long id) {
		// TODO Auto-generated method stub
		
	}
}
