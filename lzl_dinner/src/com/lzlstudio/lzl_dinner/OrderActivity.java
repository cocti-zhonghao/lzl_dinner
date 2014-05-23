package com.lzlstudio.lzl_dinner;

import com.lzlstudio.lzl_dinner.MenuCategoryListFragment.CategorySelectListener;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

public class OrderActivity extends Activity implements CategorySelectListener {
	
	SparseArray<MenuItemGridFragment> menuDetailFragmentList = new SparseArray<MenuItemGridFragment>();
	int mCurrentSelectedCategoryPos = -1;
	int mCurrentSelectedCategoryId = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		//
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
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
	
	@Override
	public void onCategoryItemSelected(int position, long id) {
		//
		Log.e("OrderActivity", "onCategoryItemSelected: position["+position+"] id["+id+"]");
		if(id == mCurrentSelectedCategoryId) return;
		setCategorySelected(id);
		showCategoryDetail(id);
		//
		mCurrentSelectedCategoryId = (int) id;
		mCurrentSelectedCategoryPos = position;
	}
	//将菜单类别设置为选中状态
	private void setCategorySelected(long category_id)
	{
		
	}
	//显示对应类别的详情
	private void showCategoryDetail(long category_id)
	{
		MenuItemGridFragment newFragment = getMenuItemGridFragment(category_id);
		//MenuItemGridFragment oldFragment = (MenuItemGridFragment) getFragmentManager().findFragmentById(R.id.menu_item_list_container);
		MenuItemGridFragment oldFragment = (MenuItemGridFragment) getFragmentManager().findFragmentByTag("id:"+mCurrentSelectedCategoryId);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if(newFragment.isAdded())
		{
			if(null != oldFragment) 
			{
				ft.hide(oldFragment);
				//
				Log.e("OrderActivity", "hide["+oldFragment.getCategoryId()+"]");
			}
			ft.show(newFragment);
			Log.e("OrderActivity", "show["+newFragment.getCategoryId()+"]");
		}
		else 
		{
			if(null != oldFragment)
			{
				ft.hide(oldFragment);
				Log.e("OrderActivity", "hide["+oldFragment.getCategoryId()+"]");
			}
			//
			ft.add(R.id.menu_item_list_container, newFragment, "id:"+category_id);
			Log.e("OrderActivity", "show["+newFragment.getCategoryId()+"]");
		}
		//
		ft.commit();
	}
	//
	private MenuItemGridFragment getMenuItemGridFragment(long category_id)
	{
		MenuItemGridFragment fragment = menuDetailFragmentList.get((int) category_id);
		if(null == fragment)
		{
			fragment = MenuItemGridFragment.newInstance((int)category_id);
			menuDetailFragmentList.put((int)category_id, fragment);
		}
		//
		return fragment;			
	}	
}
