package com.example.trex;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import com.example.trex.adapters.UnreviewedExpenseDbAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/*
 * 
 * This activity is related to widget 
 * To assign layout and implementation to widget press  
 * 
 * */

public class AddExpenseTagActivity extends Activity {
	

	private String TAG = "AddExpenditureHintActivity"; // A Tag to mention Activity's name in Log
	private EditText tag ;
	private Button addTag ;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		//Log.v(TAG,"Line 1") ;
		setContentView(R.layout.activity_add_expense_tag);
		//Log.v(TAG,"Line 2") ;
		
		initializeControls() ;
				
		
	}

/* 
 * 
 * A function to initialize view elements of associated xml layout of activity
 * 
 * */
	
	private void initializeControls()
	{
		
		tag = (EditText)findViewById(R.id.tagText) ;
		addTag = (Button)findViewById(R.id.addTag);
		
		tag.requestFocus();
		//Log.v(TAG,"Line 3") ;
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		//Log.v(TAG,"Line 4") ;
		
	
		/*
		 * 
		 * Adding functionality to addTag Button, This will insert expense tag details
		 * into the database table using 'UnreviewedExpenseDbAdapter' 
		 * 
		 * */
		
		addTag.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			
				//Log.v(TAG,"Line 5") ;
				String expense_tag = tag.getText().toString() ;
				
				if(expense_tag.length() > 0)
				{
				
					// Fetching the time value in milliseconds of the moment when expense is tagged
					Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()) ;
					long timeStamp = c.getTimeInMillis();
					
					//Log.v(TAG,"timeStamp value is "+timeStamp);
					//Log.v(TAG,"Day is "+c.get(Calendar.DAY_OF_MONTH)+", month is "+ (c.get(Calendar.MONTH)+1) +", year is "+c.get(Calendar.YEAR));
				
					UnreviewedExpenseDbAdapter udb = new UnreviewedExpenseDbAdapter(AddExpenseTagActivity.this);
					udb.open();
					int result = (int)udb.insertExpenseTag(expense_tag, timeStamp);
		
						if(result == -1)
						{
							// If expense tag is not inserted in DB
							Toast.makeText(AddExpenseTagActivity.this, "Expense Tag could not inserted, plz try again", Toast.LENGTH_SHORT).show() ;
						
						}
						else
						{
							// If expense tag is successfully inserted in DB
							Toast.makeText(AddExpenseTagActivity.this, "Expense Tag Inserted" 
									, Toast.LENGTH_SHORT).show() ;
							
						}
					udb.close();
					finish();
				}
				else
				{
					// If any expenses tag is not inserted, but Tagging button is pressed
					Toast.makeText(AddExpenseTagActivity.this, "Any Expense Tag Not Inserted", Toast.LENGTH_SHORT).show() ;
					
				}
				
			}
		});

		
		
	}
	
	
}
