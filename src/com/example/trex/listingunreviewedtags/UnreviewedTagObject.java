package com.example.trex.listingunreviewedtags;

import android.support.v4.os.ParcelableCompat;

public class UnreviewedTagObject  {
	
	private String TAG = "UnreviewedTagObject";
	
	private int id  ;
	private String tag ;
	private long timeStamp ;
	
	
	public UnreviewedTagObject(int id, String tag, long timeStamp)
	{
		
		this.id = id ;
		this.tag = tag ;
		this.timeStamp = timeStamp ;
	}
	
	public int getId()
	{
		return id ;
	}

	public String getTag()
	{
		return tag ;
	}
	public long getTimeStamp()
	{
		return timeStamp ;
	}
}
