package com.example.trex.listingexpense;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import com.example.trex.CategoryExpenseActivity;
import com.example.trex.R;
import com.example.trex.UpdateExpenseActivity;
import com.example.trex.adapters.ExpenseDbAdapter;
import android.app.Activity;
import android.content.ContentValues;
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

/*
 * This class is used to create adapter for 2 listViews present on CategoryExpenseActivity
 * 
 * */

public class ExpenseArrayAdapter extends ArrayAdapter<ExpenseObject>{

	
	/*
	 * Interface to update the another ListView when one ListView is modified.
	 *  
	 * */
	
	public static interface ListUpdateCallBack
	{
		void onUpdateList(int id,String name, int cid, float amount, long timeStamp,int flag ) ;
		int getCategoryId();
		
	}
	
	private ListUpdateCallBack mCallBack ;
	private String TAG=  "ExpenseArrayAdapter" ;
	private Context ctx ;
	private int rowLayoutId ;
	private ArrayList<ExpenseObject> expenseList ;
	int pos ;
	/*
	 * 
	 * Constructor
	 * 
	 * */
	
	public ExpenseArrayAdapter(Context context, int textViewResourceId,
			ArrayList<ExpenseObject> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		
		ctx = context ;
		rowLayoutId = textViewResourceId ;
		expenseList = objects ;
		Log.v(TAG,"In constructor before creating instance of interface" );
		mCallBack = (ListUpdateCallBack)(ctx) ; // Initialization of instance of interface 
	}


	/*
	 * Overriding of callback to make custom row for ListView
	 * 
	 * */
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		Log.v(TAG,"In getView. Line "+1 );
		View row = convertView ;
		ExpenseHolder eHolder = null;
		
		if(row == null)
		{
			Log.v(TAG,"In getView. Line "+2 );
			LayoutInflater li =  ((Activity)ctx).getLayoutInflater() ;
		
			Log.v(TAG,"In getView. Line "+2+"a" );
			
			row = li.inflate(rowLayoutId, parent, false);
			Log.v(TAG,"In getView. Line "+2+"b" );
			eHolder = new ExpenseHolder() ;
			
			Log.v(TAG,"In getView. Line "+3 );
			
			eHolder.eid = (TextView)row.findViewById( R.id.expense_id_F);
			eHolder.ename = (TextView)row.findViewById(R.id.expense_F) ;
			eHolder.eamount = (TextView)row.findViewById(R.id.amount_F) ;
			eHolder.etimestamp = (TextView)row.findViewById(R.id.time_stamp_F) ;
			eHolder.settle = (Button)row.findViewById(R.id.settle_F) ;
			eHolder.rowposition = (TextView)row.findViewById(R.id.row_position) ;
			eHolder.timeStampLong = (TextView)row.findViewById(R.id.timeStamp);
			eHolder.edit = (Button)row.findViewById(R.id.editExpense);
			
			Log.v(TAG,"In getView. Line "+4 );
			row.setTag(eHolder);
			Log.v(TAG,"In getView. Line "+5 );
		}
		else
		{
			Log.v(TAG,"In getView. Line "+6 );
			eHolder = (ExpenseHolder)row.getTag();
			Log.v(TAG,"In getView. Line "+7 );
		}
				
		pos = position ;
		ExpenseObject eo = (ExpenseObject)expenseList.get(position);
		
		Log.v(TAG,"In getView. Line "+8 );
		
		eHolder.eid.setText(""+eo.getId());
		eHolder.ename.setText(eo.getName()) ;
		eHolder.eamount.setText(""+eo.getAmount());
		eHolder.timeStampLong.setText(""+eo.getTimeStamp()) ;
		
		
		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()) ;
		c.setTimeInMillis(eo.getTimeStamp()) ;
		String _AM_PM ;
		
		if(c.get(Calendar.AM_PM) == 0) _AM_PM = "AM" ;
		else _AM_PM = "PM" ;
		
		String timeString = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR)+", "+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+_AM_PM ;
				
		eHolder.etimestamp.setText(timeString);
		eHolder.rowposition.setText(""+position) ;
		
		Log.v(TAG,"In getView. Line "+9 );
				
		if(eo.getSettledFlag() == 0)
		{
			
			/*
			 * 
			 * Implementing onClick functionality for edit button of unsettled expense of each expense
			 * 
			 * */
			
			eHolder.edit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					/*
					 * Retrieving first the parent of the expand button i.e. associated listview row
					 * to fetch values of all other related views in the same row 
					 * */
					
					RelativeLayout l = (RelativeLayout) v.getParent() ;					
					TextView eTag = (TextView)l.getChildAt(0);
					TextView eAmt = (TextView)l.getChildAt(1) ;
					TextView eId = (TextView)l.getChildAt(4);
					TextView eTimeStamp = (TextView)l.getChildAt(6);
					
					if(mCallBack != null)
					{
						int cId = mCallBack.getCategoryId() ; 
						Log.v(TAG,"In getView : before building intent for UpdateExpenseActivity" );
					
						Intent i = new Intent() ;
						i.setClass(ctx, UpdateExpenseActivity.class) ;
						i.putExtra(UpdateExpenseActivity.EXPENSE_ID, eId.getText().toString());
						i.putExtra(UpdateExpenseActivity.CAT_ID, ""+cId) ;
						i.putExtra(UpdateExpenseActivity.EXPENSE_TAG, eTag.getText().toString()) ;
						i.putExtra(UpdateExpenseActivity.EXPENSE_AMOUNT,eAmt.getText().toString()) ;
						i.putExtra(UpdateExpenseActivity.TIME_STAMP, eTimeStamp.getText().toString());
						i.putExtra(UpdateExpenseActivity.POSITION_LIST, ""+pos); 
						// Starting activity to edit expense
						((Activity)ctx).startActivityForResult(i, CategoryExpenseActivity.UPDATE_EXPENSE) ;
						
					}
					
					
				}
			});
			
			
			
			eHolder.settle.setText("S") ;
			
			/*
			 * 
			 * Implementing onClick functionality for Settle button of unsettled expense of each expense
			 * 
			 * */
			
			eHolder.settle.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
						
					/*
					 * Retrieving first the parent of the expand button i.e. associated listview row
					 * to fetch values of all other related views in the same row 
					 * */
					
					RelativeLayout r = (RelativeLayout)v.getParent() ;
					TextView eId = (TextView)r.getChildAt(4);
					int  eid = Integer.parseInt(eId.getText().toString()) ;
					
					TextView rowPosition = (TextView)r.getChildAt(5);
					int pos = Integer.parseInt(rowPosition.getText().toString()) ;
					
					TextView eTag = (TextView)r.getChildAt(0);
					String etag = eTag.getText().toString() ;
					TextView eAmt = (TextView)r.getChildAt(1) ;
					String eamt = eAmt.getText().toString() ;
					TextView eTimeStampLong = (TextView)r.getChildAt(6);
					String etimestamp = eTimeStampLong.getText().toString() ;
					
					//TextView eTimeStamp = (TextView)r.getChildAt(3) ;
					
					ContentValues updatedValues = new ContentValues() ;
					updatedValues.put(ExpenseDbAdapter.FLAG,1) ;
					ExpenseDbAdapter edb =  new ExpenseDbAdapter(ctx);
					edb.open() ;
					
					boolean result = edb.updateExpense(eid, updatedValues);
					
					if(result)
					{
						expenseList.remove(pos) ;
						Log.v(TAG,"In getView, After Updating row.");
						notifyDataSetChanged() ;
						if(mCallBack != null)
						{
							Log.v(TAG,"In getView, Before updating other list using interface");
							
							int cid = mCallBack.getCategoryId() ;
							Log.v(TAG,"Returned Category Id "+cid);
							mCallBack.onUpdateList(eid, etag,cid, Float.parseFloat(eamt), Long.parseLong(etimestamp),1) ;
						}
						
					}
					edb.close() ;
					
				}
			});
			
			
			
		}
		else
		{
			
			
			eHolder.edit.setVisibility(View.GONE) ;
			eHolder.settle.setText("D") ;
			
			/*
			 * 
			 * Implementing onClick functionality for Delete button of settled expense of each expense
			 * 
			 * */
			
			eHolder.settle.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					/*
					 * Retrieving first the parent of the expand button i.e. associated listview row
					 * to fetch values of all other related views in the same row 
					 * */
					
					RelativeLayout r = (RelativeLayout)v.getParent() ;
					TextView eId = (TextView)r.getChildAt(4);
					int  eid = Integer.parseInt(eId.getText().toString()) ;
					
					TextView rowPosition = (TextView)r.getChildAt(5);
					int pos = Integer.parseInt(rowPosition.getText().toString()) ;
					ExpenseDbAdapter edb =  new ExpenseDbAdapter(ctx);
					edb.open() ;
					boolean result = edb.deleteExpense(eid) ;
					if(result)
					{
						expenseList.remove(pos) ;
						Log.v(TAG,"In getView, After Deleting row.");
						notifyDataSetChanged() ;
								
					}
					
					edb.close();
					
					
				}
			});
			
		}
		
		return row ;	
	}
}
