package com.example.trex.listingexpense;

import java.util.ArrayList;
import java.util.List;
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
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.sax.StartElementListener;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExpenseArrayAdapter extends ArrayAdapter<ExpenseObject>{

	
	public static interface ListUpdateCallBack
	{
		void onUpdateList(int id,String name, int cid, float amount, long timeStamp,int flag ) ;
		int getCategoryId();
		
	}
	
	private ListUpdateCallBack mCallBack ;
	String TAG=  "ExpenseArrayAdapter" ;
	Context ctx ;
	int rowLayoutId ;
	ArrayList<ExpenseObject> expenseList ;
	ViewGroup parent_row ;
	
	public ExpenseArrayAdapter(Context context, int textViewResourceId,
			ArrayList<ExpenseObject> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		
		ctx = context ;
		rowLayoutId = textViewResourceId ;
		expenseList = objects ;
		Log.v(TAG,"In constructor before creating instance of interface" );
		mCallBack = (ListUpdateCallBack)(ctx) ;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		parent_row = parent ;
		Log.v(TAG,"In getView. Line "+1 );
		View row = convertView ;
		
		ExpenseHolder eHolder = null;
		
		
		if(row == null)
		{
			Log.v(TAG,"In getView. Line "+2 );
			LayoutInflater li =  ((Activity)ctx).getLayoutInflater() ;
		
			Log.v(TAG,"In getView. Line "+2+"a" );
			//LayoutParams l = new LayoutParams(get, height)
			//parent.setLayoutParams(params)
			//li.inf
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
				
		
		ExpenseObject eo = (ExpenseObject)expenseList.get(position);
		
		Log.v(TAG,"In getView. Line "+8 );
		
		eHolder.eid.setText(""+eo.getId());
		eHolder.ename.setText(eo.getName()) ;
		eHolder.eamount.setText(""+eo.getAmount());
		eHolder.timeStampLong.setText(""+eo.getTimeStamp()) ;
		
		TimeZone tz = TimeZone.getDefault() ;
		Time t = new Time(tz.getDisplayName(false,TimeZone.SHORT,Locale.getDefault()));
		t.set(eo.getTimeStamp());
		String timeString = t.monthDay+"/"+(t.month+1)+"/"+t.year+", "+t.hour+":"+t.minute+":"+t.second ;
		
		eHolder.etimestamp.setText(timeString);
		eHolder.rowposition.setText(""+position) ;
		
		Log.v(TAG,"In getView. Line "+9 );
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		if(eo.getSettledFlag() == 0)
		{
			
			

			eHolder.edit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					RelativeLayout l = (RelativeLayout) v.getParent() ;
					
					TextView eTag = (TextView)l.getChildAt(0);
					TextView eAmt = (TextView)l.getChildAt(1) ;
					TextView eId = (TextView)l.getChildAt(4);
					
					
					if(mCallBack != null)
					{
						int cId = mCallBack.getCategoryId() ; 
						Log.v(TAG,"In getView : before building intent for UpdateExpenseActivity" );
					
						Intent i = new Intent() ;
						i.setClass(ctx, UpdateExpenseActivity.class) ;
						i.putExtra(UpdateExpenseActivity.EXPENSE_ID, eId.getText().toString());
						i.putExtra(UpdateExpenseActivity.CAT_ID, ""+cId) ;
						i.putExtra(UpdateExpenseActivity.EXPENSE_TAG, ""+eTag.getText().toString()) ;
						i.putExtra(UpdateExpenseActivity.EXPENSE_AMOUNT,""+eAmt.getText().toString()) ;
						
						
						((Activity)ctx).startActivityForResult(i, CategoryExpenseActivity.UPDATE_EXPENSE) ;
						
					}
					
					
				}
			});
			
			
			
			eHolder.settle.setText("S") ;
			
			eHolder.settle.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
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
					
					TextView eTimeStamp = (TextView)r.getChildAt(3) ;
					
					
					
					ContentValues updatedValues = new ContentValues() ;
					updatedValues.put(ExpenseDbAdapter.FLAG,1) ;
					//initialValues.put(ExpenseDbAdapter.AMOUNT, eamt) ;
					//initialValues.put(ExpenseDbAdapter.CATEGORY_ID, );
					
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
					
					
					
					
					//ListView l = (ListView)r.getChildAt(2) ;
					//((Activity)ctx).fini
					
				}
			});
			
			
			
		}
		else
		{
			eHolder.edit.setVisibility(View.GONE) ;
			eHolder.settle.setText("D") ;
			
			eHolder.settle.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
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
						
						//if(mCallBack != null)
						//{
							//Log.v(TAG,"In getView, Before updating other list using interface");
							//mCallBack.onUpdateList() ;
						//}
						
					}
					
					edb.close();
					
					
				}
			});
			
		}
		
		
		
		
		
		
		
		
		return row ;
		//return super.getView(position, convertView, parent);
		
		
		
		
		
	}

	
	
	
}
