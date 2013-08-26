package com.example.trex.listingexpense;

/*
 * 
 * A class to build objects, to hold values of a expense row's views listed in ListView of expenses listing  
 * 
 * */

public class ExpenseObject {

	int eId ;
	String eName;
	int cId ;
	long eTimeStamp ;
	float eAmount ;
	int eSflag ;
	/*
	 *
	 * Constructor
	 * 
	 * */	
	public ExpenseObject(int id,String name, int cid,float amount,long timeStamp, int flag)
	{
		eId = id ;
		eName = name ;
		cId = cid ;
		eAmount = amount ;
		eTimeStamp = timeStamp ;
		eSflag = flag ;
 	}
	
	/*
	 * 
	 * Get Id of expense
	 * 
	 * */
	
	public int getId()
	{
		return eId ;
	}
	

	/*
	 * 
	 * Get content of expense
	 * 
	 * */
	
	public String getName()
	{
		return eName ;
	}
	

	/*
	 * 
	 * Get category Id of expense
	 * 
	 * */
	public int getCid()
	{
		return cId ;
	}
	

	/*
	 * 
	 * Get Amount of expense
	 * 
	 * */
	
	public float getAmount()
	{
		return eAmount ;
	}
	

	/*
	 * 
	 * Get timeStamp of expense
	 * 
	 * */
	
	public long getTimeStamp()
	{
		return eTimeStamp ;
	}
	

	/*
	 * 
	 * Get flag of expense
	 * 
	 * */
	
	public int getSettledFlag()
	{
		return eSflag ;
	}
}
