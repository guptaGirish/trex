package com.example.trex.listingcategory;

public class CategoryObject {
	int id ;
	String cname ;
	public CategoryObject(int id, String cname) {
		// TODO Auto-generated constructor stub
		this.id =id ;
		this.cname = cname ;
	}
	
	public int getCategoryId()
	{
		return id ;
	}
	public String getCategoryName()
	{
		return cname;
	}
	

}
