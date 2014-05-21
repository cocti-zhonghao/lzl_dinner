package com.lzlstudio.lzl_dinner.util;

import java.util.Date;

public class DateTimeHelper {
	public static Date NowDate()
	{
		return new Date(System.currentTimeMillis());
	}
	
	public static long NowMilliseconds ()
	{
		return System.currentTimeMillis();
	}
	
	public static int NowSeconds()
	{
		return (int) (System.currentTimeMillis()/1000);
	}
}
