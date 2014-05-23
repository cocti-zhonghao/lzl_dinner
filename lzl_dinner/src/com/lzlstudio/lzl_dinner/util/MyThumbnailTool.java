package com.lzlstudio.lzl_dinner.util;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import com.nostra13.universalimageloader.core.process.BitmapProcessor;

public class MyThumbnailTool implements BitmapProcessor {
	int mWidth;
	int mHeight;
	public MyThumbnailTool(int width, int height)
	{
		mWidth = width;
		mHeight = height;
	}
	@Override
	public Bitmap process(Bitmap bitmap) {
		//先按比例裁剪
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		if(w <= mWidth && h <= mHeight) return bitmap; //如果实际尺寸小于等于缩略图尺寸，返回原图
		//
		Bitmap afterClip = null;
		int x =0;
		int y = 0;
		int w2 = 0;
		int h2 = 0;
		//横图
		if(w > h)			
		{			
			w2 = (int) (h*((float)mWidth/(float)mHeight)); if(w2>w) w2 = w;
			h2 = h;
			//
			x = (w-w2)/2;
			y = 0;
		}
		//竖图
		else if(h > w) 	 
		{
			h2 = (int) (w*((float)mHeight/(float)mWidth)); if(h2 > h) h2=h;
			w2 = w;
			//
			x = 0;
			y = (h-h2)/2;
		}
		//方形图
		else
		{
			if(mWidth > mHeight)
			{
				h2 = (int) (h*((float)mHeight/(float)mWidth));
				w2 = w;
				//
				x = 0;
				y = (h-h2)/2;
			}
			else if(mHeight < mWidth )
			{
				w2 = (int) (w*((float)mWidth/(float)mHeight));
				h2 = h;
				//
				x= (w-w2)/2;
				y = 0;
			}
		}
		//
		afterClip = Bitmap.createBitmap(bitmap, x, y, w2, h2);
		//然后缩略图
		return ThumbnailUtils.extractThumbnail(afterClip, mWidth, mHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
	}

}
