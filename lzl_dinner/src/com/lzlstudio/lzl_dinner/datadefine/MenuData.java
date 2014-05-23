package com.lzlstudio.lzl_dinner.datadefine;

public class MenuData {

	//////
	public static class MenuCategoryItem
	{
		public int id;
		public String title = "";
		public String title_en = "";
		public String img = "";
		public String selected_img = "";
		public String key = "";
		public String des = "";
	}

	public static class MenuItem
	{
		public int id;
		public int category_id;
		public String title;
		public String title_en="";
		public String img;
		public String key;
		public double original_price;
		public double special_price;
		public double price;
		public String short_cut="";//拼音简码
		public String des="";
	}

}
