package com.lzlstudio.lzl_dinner;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

//
public class MenuCategoryListFragment extends ListFragment
{
	private CategorySelectListener categorySelectListener;
	public interface CategorySelectListener
	{
		public void onCategoryItemSelected(int position, long id);
	}
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
	}
//	//	@Override
//	public View  onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
//	{
//		View v = super.onCreateView(inflater, container, savedInstanceState);
//		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//		return v;
//	}
	@Override
	public void  onListItemClick(ListView l, View v, int position, long id)  
	{
		categorySelectListener.onCategoryItemSelected(position, id);
		//setSelection(position);
	}
}