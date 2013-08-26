package com.example.trex.listingunreviewedtags;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import com.example.trex.ExpenseCompleteActivity;
import com.example.trex.HomePageActivity;
import com.example.trex.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * This class is used to create adapter for listView present on HomePageActivity
 * 
 * */

public class CustomArrayAdapter extends ArrayAdapter<UnreviewedTagObject>{

	private Context ctx ;
	private int rowLayoutId ;
	private ArrayList<UnreviewedTagObject> list;
	private String TAG = "CustomArrayAdapter" ;
	private UnreviewedTagObject uob ;
	private ExpenseTagHolder eTagHolder = null;
	
	/*
	 * 
	 * Constructor
	 * 
	 * */
	
	public CustomArrayAdapter(Context context, int textViewResourceId,
			ArrayList<UnreviewedTagObject> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		ctx = context ;
		rowLayoutId = textViewResourceId ; 
		list = objects ;
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
		
		if(row == null)
		{
			Log.v(TAG,"In getView. Line "+2 );
			LayoutInflater li =  ((Activity)ctx).getLayoutInflater() ;	
			Log.v(TAG,"In getView. Line "+2+"a" );
			row = li.inflate(rowLayoutId, parent, false);
			Log.v(TAG,"In getView. Line "+2+"b" );
			eTagHolder = new ExpenseTagHolder() ;
			Log.v(TAG,"In getView. Line "+3 );
			
			eTagHolder.expenseTag = (TextView)row.findViewById(R.id.expense_tag);
			eTagHolder.amount = (EditText)row.findViewById(R.id.amount) ;
			
			eTagHolder.amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);	
			eTagHolder.editExpense = (Button)row.findViewById(R.id.edit_expense);
			eTagHolder.tagId = (TextView)row.findViewById(R.id.tag_id) ;
			eTagHolder.timeString = (TextView)row.findViewById(R.id.time_string);
			eTagHolder.timeStamp = (TextView)row.findViewById(R.id.time_stamp) ;
			eTagHolder.positionList = (TextView)row.findViewById(R.id.position_list) ;
			Log.v(TAG,"In getView. Line "+4 );
			row.setTag(eTagHolder);
			Log.v(TAG,"In getView. Line "+5 );
		}
		else
		{
			Log.v(TAG,"In getView. Line "+6 );
			eTagHolder = (ExpenseTagHolder)row.getTag();
			Log.v(TAG,"In getView. Line "+7 );
		}
				
		Log.v(TAG,"In getView. Line position"+ position );
		uob = (UnreviewedTagObject)list.get(position);
		Log.v(TAG,"In getView. Line "+8 + uob.getTag());
	
		eTagHolder.expenseTag.setText(uob.getTag());
		String amount = getAmount(uob.getTag());

		if(amount.length() > 0) eTagHolder.amount.setText(amount);
		else eTagHolder.amount.setText("");
	
		eTagHolder.amount.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		long timeStamp = uob.getTimeStamp() ;
		eTagHolder.timeStamp.setText(""+timeStamp) ;
		
		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()) ;
		c.setTimeInMillis(timeStamp);
		String _AM_PM ;
		
		if(c.get(Calendar.AM_PM) == 0) _AM_PM = "AM" ;
		else	_AM_PM = "PM" ;
		String timeString = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR)+", "+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+_AM_PM ;
		
		
		eTagHolder.timeString.setText(timeString) ;
		eTagHolder.tagId.setText(""+uob.getId());
		eTagHolder.positionList.setText(""+position);
		
		/*
		 * 
		 * Implementing onClick functionality for Edit button of each tagged expense
		 * 
		 * */
		
		eTagHolder.editExpense.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
					RelativeLayout l = (RelativeLayout) v.getParent() ;
					TextView exTag = (TextView)l.getChildAt(0);
					EditText exAmt = (EditText)l.getChildAt(1) ;
					TextView exTimeString = (TextView)l.getChildAt(3);
					TextView exTimeStamp = (TextView)l.getChildAt(4);
					TextView exTagId = (TextView)l.getChildAt(5);
					TextView positionList = (TextView)l.getChildAt(6);
					Log.v(TAG, "In onClick "+exTag.getText().toString());
					Log.v(TAG, "In onClick "+exAmt.getText().toString()) ;
					Log.v(TAG, "In onClick "+exTimeString.getText().toString()) ;
					Log.v(TAG, "In onClick "+exTimeStamp.getText().toString()) ;
					Log.v(TAG, "In onClick "+exTagId.getText().toString()) ;
				
				Intent i = new Intent();
				i.setClass(ctx, ExpenseCompleteActivity.class);
				i.putExtra(ExpenseCompleteActivity.ACTION, ExpenseCompleteActivity.EDIT_ACTION);
				i.putExtra(ExpenseCompleteActivity.EXPENSE_TAG,exTag.getText().toString() );
				i.putExtra(ExpenseCompleteActivity.EXPENSE_AMOUNT, exAmt.getText().toString()) ;
				i.putExtra(ExpenseCompleteActivity.TIME_STAMP, exTimeStamp.getText().toString());
				i.putExtra(ExpenseCompleteActivity.EXPENSE_ID, exTagId.getText().toString());
				i.putExtra(ExpenseCompleteActivity.POSITION_LIST, positionList.getText().toString());				
				// starting ExpenseCompleteActivity to complete expense
				((Activity)ctx).startActivityForResult(i, HomePageActivity.EXPENSE_COMPLETE_CODE);
				
				
			}
		});
		Log.v(TAG,"In getView. Line "+9 );
		
		
		
		return row ;

		
	}
	
	
	/*
	 * 
	 * A method to extract numeric value from tag of a expense
	 * 
	 * */
	
	private String getAmount(String tag)
	{
		
		int n = tag.length() ;
		String amount = "";
		
		for(int i=0; i<n ; i++)
		{
			if( tag.charAt(i)>= '0' && tag.charAt(i) <= '9')
			{
				int j = i ;		
				do{
					amount = amount + tag.charAt(j);
					if(j<n-1) 	j++ ;
					else break ;
				}while( ( tag.charAt(j)>= '0' && tag.charAt(j) <= '9' ) || tag.charAt(j)=='.'  ) ;
				
				break ;
			}
		}
		
		return amount ;
	}

}
