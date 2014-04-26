package com.lzlstudio.lzl_dinner;

import android.R.integer;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class HomeContentFragment extends Fragment implements OnClickListener{

	@Override
	public void  onCreate (Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View  onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_home_content, container, false);
		//今日特价
		RelativeLayout itemSpecial = (RelativeLayout) (v.findViewById(R.id.home_content_item_specials));
		itemSpecial.setBackgroundColor(0xff0080ff);//颜色4个字节从高到低对应 不透明度， 红， 绿， 蓝， 四个分量
		ImageView imageSpecial = (ImageView) itemSpecial.findViewById(R.id.home_content_item2_image);
		imageSpecial.setImageResource(R.drawable.home_specials);
		TextView textSpecial = (TextView) itemSpecial.findViewById(R.id.home_content_item2_text);
		textSpecial.setText("今日特价");
		//点菜
		RelativeLayout itemOrder = (RelativeLayout) (v.findViewById(R.id.home_content_item_order));
		ImageView imageOrder = (ImageView) itemOrder.findViewById(R.id.home_content_item1_image);
		imageOrder.setImageResource(R.drawable.home_order);
		RelativeLayout.LayoutParams lp = (LayoutParams) imageOrder.getLayoutParams();;
		lp.leftMargin = 112;
		imageOrder.setLayoutParams(lp);		
		TextView textOrder = (TextView) itemOrder.findViewById(R.id.home_content_item1_text);		
		textOrder.setText("点菜");
		itemOrder.setOnClickListener(this);
		//
		lp = (LayoutParams) textOrder.getLayoutParams();
		lp.rightMargin = 112;
		textOrder.setLayoutParams(lp);		
		//开台
		RelativeLayout itemOpenTable = (RelativeLayout) (v.findViewById(R.id.home_content_item_opentable));
		ImageView imageOpenTable = (ImageView) itemOpenTable.findViewById(R.id.home_content_item2_image);
		imageOpenTable.setImageResource(R.drawable.home_open);
		TextView textOpenTable = (TextView) itemOpenTable.findViewById(R.id.home_content_item2_text);
		textOpenTable.setText("开台");
		//结账
		RelativeLayout itemPay = (RelativeLayout) (v.findViewById(R.id.home_content_item_pay));
		itemPay.setBackgroundColor(0xff0080ff);
		ImageView imagePay = (ImageView) itemPay.findViewById(R.id.home_content_item2_image);
		imagePay.setImageResource(R.drawable.home_pay);
		TextView textPay = (TextView) itemPay.findViewById(R.id.home_content_item2_text);
		textPay.setText("结账");
		//台况
		RelativeLayout itemTabeInfo = (RelativeLayout) (v.findViewById(R.id.home_content_item_tableinfo));
		ImageView imageTableInfo = (ImageView) itemTabeInfo.findViewById(R.id.home_content_item1_image);
		imageTableInfo.setImageResource(R.drawable.home_table_info);
		lp = (LayoutParams) imageTableInfo.getLayoutParams();;
		lp.leftMargin = 72;
		imageTableInfo.setLayoutParams(lp);		
		TextView textTableInfo = (TextView) itemTabeInfo.findViewById(R.id.home_content_item1_text);		
		textTableInfo.setText("台况");
		lp = (LayoutParams) textTableInfo.getLayoutParams();
		lp.rightMargin = 72;
		textTableInfo.setLayoutParams(lp);
		//沽清菜品
		RelativeLayout itemOutOfStock = (RelativeLayout) (v.findViewById(R.id.home_content_item_outofstock));
		ImageView imageOutOfStock = (ImageView) itemOutOfStock.findViewById(R.id.home_content_item2_image);
		imageOutOfStock.setImageResource(R.drawable.home_out_of_stock);
		TextView textOutOfStock = (TextView) itemOutOfStock.findViewById(R.id.home_content_item2_text);
		textOutOfStock.setText("沽清菜品");
		//设置
		RelativeLayout itemSetting = (RelativeLayout) (v.findViewById(R.id.home_content_item_setting));
		itemSetting.setBackgroundColor(0xff0080ff);
		ImageView imageSetting = (ImageView) itemSetting.findViewById(R.id.home_content_item2_image);
		imageSetting.setImageResource(R.drawable.home_setting);
		TextView textSetting = (TextView) itemSetting.findViewById(R.id.home_content_item2_text);
		textSetting.setText("设置");
		//
		itemSetting.setOnClickListener(this);
		return v;
	}
	//
	private void createItemView(int itemId, int backGroundColor, int imageViewId, int imageId, int textViewId, String text)
	{
		
	}
	@Override
	public void  onPause () 
	{
		super.onPause();
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.home_content_item_setting:
		{
			//弹出登录对话框
			// Create the new Dialog.
			Dialog dialog = new Dialog(getActivity(), R.style.NoTitleDialog);

			//Dialog dialog = new Dialog(getActivity());
			// 隐藏 title.
			//dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			// Inflate the layout.
			dialog.setContentView(R.layout.login_dialog);
			dialog.show();
		}
			break;
		case R.id.home_content_item_order:
		{
			Intent intent = new Intent(getActivity(), OrderActivity.class);
			startActivity(intent);
		}
		break;

		default:
			break;
		}
	}
}
