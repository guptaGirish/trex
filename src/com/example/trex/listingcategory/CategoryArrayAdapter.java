package com.example.trex.listingcategory;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;

/*
 * This class is used to build adapter for spinner present in ExpenseCompleteActivity 
 * 
 * */

public class CategoryArrayAdapter extends ArrayAdapter<String> 
{
	String TAG = "CategoryArrayAdapter" ;
	Context ctx ;
	int rowLayoutId ;
	CategoryObject cob ;
	ArrayList<String> list ;
	
	/*
	 * 
	 * Constructor
	 * 
	 * */
	
	public CategoryArrayAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		ctx = context ;
		rowLayoutId = textViewResourceId ;
		list = objects ;
	}

	
	

}


