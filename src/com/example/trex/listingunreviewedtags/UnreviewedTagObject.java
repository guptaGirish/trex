package com.example.trex.listingunreviewedtags;


/*
 * 
 * A class to build objects, to hold values of a unreviewed expense row's views listed in ListView of unreviewed expenses listing  
 * 
 * */

public class UnreviewedTagObject  {
	
	//private String TAG = "UnreviewedTagObject";	
	private int id  ;
	private String tag ;
	private long timeStamp ;

	/*
	 * 
	 * Constructor
	 * 
	 * */
	
	public UnreviewedTagObject(int id, String tag, long timeStamp)
	{
		
		this.id = id ;
		this.tag = tag ;
		this.timeStamp = timeStamp ;
	}
	
	/*
	 * 
	 * Get Id of tagged expense
	 * 
	 * */
	
	public int getId()
	{
		return id ;
	}

	/*
	 * 
	 * Get Tag of tagged expense
	 * 
	 * */
	
	public String getTag()
	{
		return tag ;
	}
	
	/*
	 * 
	 * Get timeStamp of tagged expense
	 * 
	 * */
	
	public long getTimeStamp()
	{
		return timeStamp ;
	}
}
