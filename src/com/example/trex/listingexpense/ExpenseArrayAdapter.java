package com.example.trex.listingexpense;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.example.trex.R;
import com.example.trex.adapters.ExpenseDbAdapter;



import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExpenseArrayAdapter extends ArrayAdapter<ExpenseObject>{

	String TAG=  "ExpenseArrayAdapter" ;
	Context ctx ;
	int rowLayoutId ;
	ArrayList<ExpenseObject> expenseList ;
	
	
	public ExpenseArrayAdapter(Context context, int textViewResourceId,
			ArrayList<ExpenseObject> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		
		ctx = context ;
		rowLayoutId = textViewResourceId ;
		expenseList = objects ;
		
	}


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
		
		TimeZone tz = TimeZone.getDefault() ;
		Time t = new Time(tz.getDisplayName(false,TimeZone.SHORT,Locale.getDefault()));
		//Time  t = new Time(tz.getDisplayName()) ;
	
		t.set(eo.getTimeStamp());
		
		String timeString = t.monthDay+"/"+(t.month+1)+"/"+t.year+", "+t.hour+":"+t.minute+":"+t.second ;
		
		
		eHolder.etimestamp.setText(timeString);
		eHolder.rowposition.setText(""+position) ;
		
		Log.v(TAG,"In getView. Line "+9 );
		
		
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
					
				}
				
				edb.close();
				
				
			}
		});
		
		
		
		
		return row ;
		//return super.getView(position, convertView, parent);
		
		
		
		
		
	}

	
	
	
}
