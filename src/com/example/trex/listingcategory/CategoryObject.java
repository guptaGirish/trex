package com.example.trex.listingcategory;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryObject implements Parcelable {
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	

}
