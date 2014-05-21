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

	private class ItemParameter
	{
		public int layoutId;
		public int backgroundColor;
		public int imgViewId;
		public int imgDrawableId;
		public int imgViewLeftMargin;
		public int imgViewRightMargin;
		public int textViewId;
		public String text;
		public int textViewLeftMargin;
		public int textViewRightMargin;
	}
	private static final int PARAMETER_BIT_LAYOUT_ID = 0x01;
	private static final int PARAMETER_BIT_BACKGROUND_COLOR = 0x02;
	private static final int PARAMETER_BIT_IMG_VIEW_ID = 0x04;
	private static final int PARAMETER_BIT_IMG_DRAWABLE_ID = 0x08;
	private static final int PARAMETER_BIT_IMG_VIEW_LEFT_MARGIN = 0x0100;
	private static final int PARAMETER_BIT_IMG_VIEW_RIGHT_MARGIN = 0x0200;
	private static final int PARAMETER_BIT_TEXT_VIEW_ID = 0x0400;
	private static final int PARAMETER_BIT_TEXT = 0x0800;
	private static final int PARAMETER_BIT_TEXT_VIEW_LEFT_MARGIN = 0x010000;
	private static final int PARAMETER_BIT_TEXT_VIEW_RIGHT_MARGIN = 0x020000;
	//
	private void createItemView(int paramBitMask, ItemParameter param, View v)
	{
		RelativeLayout vg = (RelativeLayout) v.findViewById(param.layoutId);
		if((paramBitMask&PARAMETER_BIT_BACKGROUND_COLOR) > 0) vg.setBackgroundColor(param.backgroundColor);
		ImageView imgView = (ImageView) vg.findViewById(param.imgViewId);
		imgView.setImageResource(param.imgDrawableId);
		if((paramBitMask&PARAMETER_BIT_IMG_VIEW_LEFT_MARGIN) > 0)
		{
			RelativeLayout.LayoutParams lp = (LayoutParams) imgView.getLayoutParams();
			lp.leftMargin = param.imgViewLeftMargin;
			imgView.setLayoutParams(lp);		
		}
		TextView textView = (TextView) vg.findViewById(param.textViewId);
		textView.setText(param.text);
		if((paramBitMask&PARAMETER_BIT_TEXT_VIEW_RIGHT_MARGIN) > 0)
		{
			RelativeLayout.LayoutParams lp = (LayoutParams) textView.getLayoutParams();
			lp.rightMargin = param.textViewRightMargin;
			textView.setLayoutParams(lp);		
		}
		//
		vg.setOnClickListener(this);
	}

	@Override
	public void  onCreate (Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View  onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_home_content, container, false);
		//
		ItemParameter param = new ItemParameter();
		int paramBitMask = 0;
		//餐厅介绍
		RelativeLayout vg = (RelativeLayout) v.findViewById(R.id.home_content_item_introduce);
		vg.setOnClickListener(this);
		//今日特价
		param.layoutId = R.id.home_content_item_specials;
		param.backgroundColor = 0xff0080ff;
		param.imgViewId = R.id.home_content_item2_image;
		param.imgDrawableId = R.drawable.home_specials;
		param.textViewId = R.id.home_content_item2_text;
		param.text = "今日特价";
		paramBitMask = PARAMETER_BIT_BACKGROUND_COLOR;
		createItemView(paramBitMask, param, v);
		
//		RelativeLayout itemSpecial = (RelativeLayout) (v.findViewById(R.id.home_content_item_specials));
//		itemSpecial.setBackgroundColor(0xff0080ff);//颜色4个字节从高到低对应 不透明度， 红， 绿， 蓝， 四个分量
//		ImageView imageSpecial = (ImageView) itemSpecial.findViewById(R.id.home_content_item2_image);
//		imageSpecial.setImageResource(R.drawable.home_specials);
//		TextView textSpecial = (TextView) itemSpecial.findViewById(R.id.home_content_item2_text);
//		textSpecial.setText("今日特价");
		//优惠活动
		vg = (RelativeLayout) v.findViewById(R.id.home_content_item_discount);
		vg.setOnClickListener(this);
		//点菜
		paramBitMask = 0;
		param.layoutId = R.id.home_content_item_order;
		param.imgViewId = R.id.home_content_item1_image;
		param.imgDrawableId = R.drawable.home_order;
		param.imgViewLeftMargin = 112;
		param.textViewId = R.id.home_content_item1_text;
		param.text = "点菜";
		param.textViewRightMargin = 112;
		paramBitMask = PARAMETER_BIT_IMG_VIEW_LEFT_MARGIN|PARAMETER_BIT_TEXT_VIEW_RIGHT_MARGIN;
		createItemView(paramBitMask, param, v);

//		RelativeLayout itemOrder = (RelativeLayout) (v.findViewById(R.id.home_content_item_order));
//		ImageView imageOrder = (ImageView) itemOrder.findViewById(R.id.home_content_item1_image);
//		imageOrder.setImageResource(R.drawable.home_order);
//		RelativeLayout.LayoutParams lp = (LayoutParams) imageOrder.getLayoutParams();;
//		lp.leftMargin = 112;
//		imageOrder.setLayoutParams(lp);		
//		TextView textOrder = (TextView) itemOrder.findViewById(R.id.home_content_item1_text);		
//		textOrder.setText("点菜");
//		itemOrder.setOnClickListener(this);
//		//
//		lp = (LayoutParams) textOrder.getLayoutParams();
//		lp.rightMargin = 112;
//		textOrder.setLayoutParams(lp);		
		//开台
		paramBitMask = 0;
		param.layoutId = R.id.home_content_item_opentable;
		param.imgViewId = R.id.home_content_item2_image;
		param.imgDrawableId = R.drawable.home_open;
		param.textViewId = R.id.home_content_item2_text;
		param.text = "开台";
		createItemView(paramBitMask, param, v);

//		RelativeLayout itemOpenTable = (RelativeLayout) (v.findViewById(R.id.home_content_item_opentable));
//		ImageView imageOpenTable = (ImageView) itemOpenTable.findViewById(R.id.home_content_item2_image);
//		imageOpenTable.setImageResource(R.drawable.home_open);
//		TextView textOpenTable = (TextView) itemOpenTable.findViewById(R.id.home_content_item2_text);
//		textOpenTable.setText("开台");
		//结账
		paramBitMask = 0;
		param.layoutId = R.id.home_content_item_pay;
		param.backgroundColor = 0xff0080ff;
		param.imgViewId = R.id.home_content_item2_image;
		param.imgDrawableId = R.drawable.home_pay;
		param.textViewId = R.id.home_content_item2_text;
		param.text = "结账";
		paramBitMask = PARAMETER_BIT_BACKGROUND_COLOR;
		createItemView(paramBitMask, param, v);

//		RelativeLayout itemPay = (RelativeLayout) (v.findViewById(R.id.home_content_item_pay));
//		itemPay.setBackgroundColor(0xff0080ff);
//		ImageView imagePay = (ImageView) itemPay.findViewById(R.id.home_content_item2_image);
//		imagePay.setImageResource(R.drawable.home_pay);
//		TextView textPay = (TextView) itemPay.findViewById(R.id.home_content_item2_text);
//		textPay.setText("结账");
		//台况
		paramBitMask = 0;
		param.layoutId = R.id.home_content_item_tableinfo;
		param.imgViewId = R.id.home_content_item1_image;
		param.imgDrawableId = R.drawable.home_table_info;
		param.imgViewLeftMargin = 72;
		param.textViewId = R.id.home_content_item1_text;
		param.text = "台况";
		param.textViewRightMargin = 72;
		paramBitMask = PARAMETER_BIT_IMG_VIEW_LEFT_MARGIN|PARAMETER_BIT_TEXT_VIEW_RIGHT_MARGIN;
		createItemView(paramBitMask, param, v);

//		RelativeLayout itemTabeInfo = (RelativeLayout) (v.findViewById(R.id.home_content_item_tableinfo));
//		ImageView imageTableInfo = (ImageView) itemTabeInfo.findViewById(R.id.home_content_item1_image);
//		imageTableInfo.setImageResource(R.drawable.home_table_info);
//		lp = (LayoutParams) imageTableInfo.getLayoutParams();;
//		lp.leftMargin = 72;
//		imageTableInfo.setLayoutParams(lp);		
//		TextView textTableInfo = (TextView) itemTabeInfo.findViewById(R.id.home_content_item1_text);		
//		textTableInfo.setText("台况");
//		lp = (LayoutParams) textTableInfo.getLayoutParams();
//		lp.rightMargin = 72;
//		textTableInfo.setLayoutParams(lp);
		//沽清菜品
		paramBitMask = 0;
		param.layoutId = R.id.home_content_item_outofstock;
		param.imgViewId = R.id.home_content_item2_image;
		param.imgDrawableId = R.drawable.home_out_of_stock;
		param.textViewId = R.id.home_content_item2_text;
		param.text = "沽清菜品";
		createItemView(paramBitMask, param, v);

//		RelativeLayout itemOutOfStock = (RelativeLayout) (v.findViewById(R.id.home_content_item_outofstock));
//		ImageView imageOutOfStock = (ImageView) itemOutOfStock.findViewById(R.id.home_content_item2_image);
//		imageOutOfStock.setImageResource(R.drawable.home_out_of_stock);
//		TextView textOutOfStock = (TextView) itemOutOfStock.findViewById(R.id.home_content_item2_text);
//		textOutOfStock.setText("沽清菜品");
		//设置
		paramBitMask = 0;
		param.layoutId = R.id.home_content_item_setting;
		param.backgroundColor = 0xff0080ff;
		param.imgViewId = R.id.home_content_item2_image;
		param.imgDrawableId = R.drawable.home_setting;
		param.textViewId = R.id.home_content_item2_text;
		param.text = "设置";
		paramBitMask = PARAMETER_BIT_BACKGROUND_COLOR;
		createItemView(paramBitMask, param, v);

//		RelativeLayout itemSetting = (RelativeLayout) (v.findViewById(R.id.home_content_item_setting));
//		itemSetting.setBackgroundColor(0xff0080ff);
//		ImageView imageSetting = (ImageView) itemSetting.findViewById(R.id.home_content_item2_image);
//		imageSetting.setImageResource(R.drawable.home_setting);
//		TextView textSetting = (TextView) itemSetting.findViewById(R.id.home_content_item2_text);
//		textSetting.setText("设置");
//		//
//		itemSetting.setOnClickListener(this);
		return v;
	}
	//
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
