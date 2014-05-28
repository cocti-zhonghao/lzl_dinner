package com.lzlstudio.lzl_dinner;

import com.lzlstudio.lzl_dinner.MenuCategoryListFragment.CategorySelectListener;
import com.lzlstudio.lzl_dinner.datadefine.MenuData;

import android.R.integer;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class OrderActivity extends Activity implements CategorySelectListener,MenuItemDetailFragment.OrderListener{
	
	SparseArray<MenuItemGridFragment> menuDetailFragmentList = new SparseArray<MenuItemGridFragment>();
	int mCurrentSelectedCategoryPos = -1;
	int mCurrentSelectedCategoryId = -1;
	RelativeLayout mOrderedMenuCountContianer;
	ImageView mOrderMenuImg;
	
	void initActionBar()
	{
		mOrderedMenuCountContianer = (RelativeLayout) findViewById(R.id.my_action_bar_menu);
		mOrderMenuImg = (ImageView) findViewById(R.id.my_action_bar_menu_img);
	}
	
	int[] getAddToCartDestXY()
	{
		int [] dest = new int[2];
		mOrderedMenuCountContianer.getLocationInWindow(dest); dest[1] -= 30; dest[0] +=20;
		//mOrderMenuImg.getLocationInWindow(dest);
		//@zh for test
		Log.e("***DEBUG***", "destination: x["+dest[0]+"], y["+dest[1]+"]");
		return dest;
	}
	
	public void startAddToCartAnimation(int[] from)
	{
		final ImageView buyImg = new ImageView(this);
		//final ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
		final ViewGroup rootView = (ViewGroup) ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
		int to[] = getAddToCartDestXY();
		rootView.addView(buyImg);
		//
		int x = from[0];
		int y = from[1];
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(10,10);
		lp.leftMargin = x;
		lp.topMargin = y;
		buyImg.setLayoutParams(lp);
		buyImg.setImageResource(R.drawable.menu_item_cart_num_background);
		////
		// 计算位移
		int endX = to[0] - from[0];	// 动画位移的X坐标
		int endY = to[1] - from[1];		// 动画位移的y坐标
		TranslateAnimation translateAnimationX = new TranslateAnimation(0, endX, 0, 0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		//translateAnimationX.setInterpolator(new AccelerateInterpolator());
		translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(false);

		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
		//translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setInterpolator(new LinearInterpolator());
		translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(false);

		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(false);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.setDuration(2000);// 动画的执行时间
		buyImg.startAnimation(set);
		// 动画监听事件
		set.setAnimationListener(new AnimationListener() {
			// 动画的开始
			@Override
			public void onAnimationStart(Animation animation) {
				buyImg.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			// 动画的结束
			@Override
			public void onAnimationEnd(Animation animation) {
				buyImg.setVisibility(View.GONE);
				//
				//rootView.removeView(buyImg);//NullPointerException
				new Handler().post(new Runnable() {
					public void run() {rootView.removeView(buyImg);}});
				}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		//
		initActionBar();
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

	@Override
	public void onOrder(MenuData.MenuItem item) 
	{	
	}	
}
