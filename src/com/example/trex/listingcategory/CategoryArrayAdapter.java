package com.example.trex.listingcategory;

import java.util.ArrayList;
import java.util.List;

import com.example.trex.CategoryExpenseActivity;

import com.example.trex.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CategoryArrayAdapter extends ArrayAdapter<String> 
{
	String TAG = "CategoryArrayAdapter" ;
	Context ctx ;
	int rowLayoutId ;
	CategoryObject cob ;
	
	ArrayList<String> list ;
	public CategoryArrayAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		ctx = context ;
		rowLayoutId = textViewResourceId ;
		list = objects ;
	}

	
	

}


