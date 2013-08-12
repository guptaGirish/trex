package com.example.trex.listingexpense;

public class ExpenseObject {

	int eId ;
	String eName;
	int cId ;
	long eTimeStamp ;
	float eAmount ;
	int eSflag ;
	
	public ExpenseObject(int id,String name, int cid,float amount,long timeStamp, int flag)
	{
		eId = id ;
		eName = name ;
		cId = cid ;
		eAmount = amount ;
		eTimeStamp = timeStamp ;
		eSflag = flag ;
 	}
	public int getId()
	{
		return eId ;
	}
	public String getName()
	{
		return eName ;
	}
	public int getCid()
	{
		return cId ;
	}
	public float getAmount()
	{
		return eAmount ;
	}
	public long getTimeStamp()
	{
		return eTimeStamp ;
	}
	public int getSettledFlag()
	{
		return eSflag ;
	}
}
