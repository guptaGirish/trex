package com.example.trex;

import java.util.Locale;
import java.util.TimeZone;

import com.example.trex.adapters.UnreviewedExpenseDbAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class AddExpenseTagActivity extends Activity {
	

	String TAG = "AddExpenditureHintActivity";
	EditText tag ;
	Button addTag ;
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Log.v(TAG,"Line 1") ;
		setContentView(R.layout.activity_add_expense_tag);
		Log.v(TAG,"Line 2") ;
		tag = (EditText)findViewById(R.id.tagText) ;
		addTag = (Button)findViewById(R.id.addTag);
		//tag.setFocusableInTouchMode(true);
		tag.requestFocus();
		Log.v(TAG,"Line 3") ;
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		Log.v(TAG,"Line 4") ;
		//InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	//imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    	//imm.showSoftInput(tag, InputMethodManager.SHOW_IMPLICIT);
    	//imm.show
		//imm.showSoftInput(tag, 0);
	
		addTag.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v(TAG,"Line 5") ;
				
				String expense_tag = tag.getText().toString() ;
				
				TimeZone tz  = TimeZone.getDefault() ;
				
				
				//Time. tm = new Time(tz.getDisplayName());
				Time tm = new Time(tz.getDisplayName(false,TimeZone.SHORT,Locale.getDefault()));
				//tm.setToNow();
				tm.set(java.lang.System.currentTimeMillis());
				long timeStamp = tm.toMillis(false);
				
				Log.v(TAG,"timeStamp value is "+timeStamp);
				Log.v(TAG,"Day is "+tm.monthDay+", month is "+ (tm.month+1) +", year is "+tm.year);
				
				UnreviewedExpenseDbAdapter udb = new UnreviewedExpenseDbAdapter(AddExpenseTagActivity.this);
				udb.open();
				int result = (int)udb.insertExpenseTag(expense_tag, timeStamp);
				if(result == -1)
				{
					Toast.makeText(AddExpenseTagActivity.this, "Expense Tag" 
							+ "could not inserted, plz try again", Toast.LENGTH_SHORT).show() ;
					
				}
				else
				{
					Toast.makeText(AddExpenseTagActivity.this, "Expense Tag Inserted" 
							, Toast.LENGTH_SHORT).show() ;
					
				}
				udb.close();
				finish();
				
			}
		});
		
		
	}


}
