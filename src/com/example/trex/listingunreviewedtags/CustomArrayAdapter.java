package com.example.trex.listingunreviewedtags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.example.trex.ExpenseCompleteActivity;
import com.example.trex.HomePageActivity;
import com.example.trex.R;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<UnreviewedTagObject>{

	
	private Context ctx ;
	private int rowLayoutId ;
	private ArrayList<UnreviewedTagObject> list;
	private String TAG = "CustomArrayAdapter" ;
	UnreviewedTagObject uob ;
	ExpenseTagHolder eTagHolder = null;
	
	
	public CustomArrayAdapter(Context context, int textViewResourceId,
			ArrayList<UnreviewedTagObject> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		ctx = context ;
		rowLayoutId = textViewResourceId ; 
		list = objects ;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		
		
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
			eTagHolder.amount.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
			
			eTagHolder.editExpense = (Button)row.findViewById(R.id.edit_expense);
			
			eTagHolder.tagId = (TextView)row.findViewById(R.id.tag_id) ;
			eTagHolder.timeString = (TextView)row.findViewById(R.id.time_string);
			eTagHolder.timeStamp = (TextView)row.findViewById(R.id.time_stamp) ;
			eTagHolder.positionList = (TextView)row.findViewById(R.id.position_list) ;
			
			/*
			dHolder.title = (TextView)row.findViewById(R.id.title);
			dHolder.nid = (TextView)row.findViewById(R.id.nid);
			dHolder.description = (TextView)row.findViewById(R.id.description);
			dHolder.imageURL = (TextView)row.findViewById(R.id.imageURL);
			*/
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
		else eTagHolder.amount.setText("\u200b");
		long timeStamp = uob.getTimeStamp() ;
		eTagHolder.timeStamp.setText(""+timeStamp) ;
		TimeZone tz = TimeZone.getDefault() ;
		Time t = new Time(tz.getDisplayName(false,TimeZone.SHORT,Locale.getDefault()));
		//Time  t = new Time(tz.getDisplayName()) ;
	
		t.set(timeStamp);
		
		String timeString = t.monthDay+"/"+(t.month+1)+"/"+t.year+", "+t.hour+":"+t.minute+":"+t.second ;
		//String timeString = t.MONTH_DAY+"/"+(t.MONTH+1)+"/"+t.YEAR+", "+t.HOUR+":"+t.MINUTE+":"+t.SECOND ;
		
		eTagHolder.timeString.setText(timeString) ;
		eTagHolder.tagId.setText(""+uob.getId());
		eTagHolder.positionList.setText(""+position);
		
		eTagHolder.editExpense.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Log.v(TAG, "In getView on click"+ position) ;
					RelativeLayout l = (RelativeLayout) v.getParent() ;
				//View l = (View) v.getParent() ;
					
					/*TextView exTag = (TextView)l.findViewById(R.id.expense_tag);
					EditText exAmt = (EditText)l.findViewById(R.id.amount);
					TextView exTimeString = (TextView)l.findViewById(R.id.time_string);
					TextView exTimeStamp = (TextView)l.findViewById(R.id.time_stamp);
					TextView exTagId = (TextView)l.findViewById(R.id.tag_id);
					*/
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
				
				((Activity)ctx).startActivityForResult(i, HomePageActivity.EXPENSE_COMPLETE_CODE);
				
				
			}
		});
		/*
		eTagHolder.title.setText(lo.getTitle());
		eTagHolder.description.setText(lo.getDescription());
		dHolder.nid.setText(lo.getnid());
		dHolder.imageURL.setText(lo.getImageURL());
		*/
		Log.v(TAG,"In getView. Line "+9 );
		
		
		
		return row ;

		
	}
	
	
	
	 String getAmount(String tag)
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
