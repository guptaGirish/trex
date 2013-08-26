package com.example.trex;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import com.example.trex.adapters.CategoryDbAdapter;
import com.example.trex.adapters.ExpenseDbAdapter;
import com.example.trex.adapters.UnreviewedExpenseDbAdapter;
import com.example.trex.listingcategory.CategoryArrayAdapter;
import com.example.trex.listingcategory.CategoryObject;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/*
 * 
 * This activity is to complete an already tagged expense
 * and 
 * to create an entirely new complete expense 
 * 
 * */

public class ExpenseCompleteActivity extends Activity{
	
	private String TAG = "ExpenseCompleteActivity" ;

	private Button addNewCategory,commitExpense, expenseDate, expenseTime  ;
	private EditText expenseContent, amountExpense ;
	private TextView pageHeader ;
    private Spinner choosedCategory ;

    static int GET_CATEGORY_CODE = 2 ;

    private String PAGE_HEADER = "page header" ;
	private String EDIT_HEADER = "Complete Your Expense" ;
	private String ADD_NEW_HEADER = "Add New Expnese" ;
    private String CATEGORY_LIST = "category_list" ;
    private String CATEGORY_OBJECT_LIST = "category_object_list" ;
    
    
    public static String ACTION = "operation" ;
    public static String EDIT_ACTION = "complete_expense" ;
    public static String ADD_NEW_ACTION = "add_new_expense" ;
    public static String EXPENSE_TAG = "expense_tag" ;
    public static String EXPENSE_AMOUNT = "expense_amount" ;
    private static String POSITION_SPINNER = "position_spinner" ;
    public static String TIME_STAMP = "timestamp" ;
    public static String EXPENSE_ID = "id" ;
    public static String POSITION_LIST = "position" ; // position of passed Unreviewed expense
     											      //in Unreviewed Expense List
    private ArrayList<CategoryObject> colist ;
    private ArrayList<String> clist ;
    private CategoryArrayAdapter cap ;
    private long timeStamp ;
    private int tagId = -1 ;
	private int pos = -1 ;
	private int posSpinner = -1 ; // to maintain the position when orientation change occurs
	private int mYear, mMonth, mDay, mHour, mMinute, mAM_PM ;
	
	@Override
	


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.v(TAG,"In onCreate");
		setContentView(R.layout.activity_expense_complete);
		
		initializeControls();
	 	
		
		
		if(savedInstanceState != null) //  Extracting values when orientation of devices changes
		{  
			
			pageHeader.setText(savedInstanceState.getString(PAGE_HEADER)) ;	
			timeStamp = Long.parseLong(savedInstanceState.getString(TIME_STAMP));
			expenseContent.setText(savedInstanceState.getString(EXPENSE_TAG)) ;
			amountExpense.setText(savedInstanceState.getString(EXPENSE_AMOUNT)) ;
			
			tagId = Integer.parseInt(savedInstanceState.getString(EXPENSE_ID)) ;
			
			posSpinner = Integer.parseInt(savedInstanceState.getString(POSITION_SPINNER));
			
			clist = (ArrayList<String>)savedInstanceState.getStringArrayList(CATEGORY_LIST) ;
			colist = savedInstanceState.getParcelableArrayList(CATEGORY_OBJECT_LIST);
			pos = Integer.parseInt(savedInstanceState.getString(POSITION_LIST)) ;
			
		}
		else // Extracting values of tagged_expense and filling under related views to complete it
		{
			Intent i = getIntent();
			String action = i.getStringExtra(ACTION);
		
				if(action.equals(EDIT_ACTION))
				{
					
					pageHeader.setText(EDIT_HEADER);
					
					String etag = i.getStringExtra(EXPENSE_TAG);
					String amt = i.getStringExtra(EXPENSE_AMOUNT) ;
					timeStamp = Long.parseLong(i.getStringExtra(TIME_STAMP));
					expenseContent.setText(etag) ;
					amountExpense.setText(amt) ;
					tagId = Integer.parseInt(i.getStringExtra(EXPENSE_ID));
					pos = Integer.parseInt(i.getStringExtra(POSITION_LIST));
					
				}
				else //If new expense is needed to be added
				{
					pageHeader.setText(ADD_NEW_HEADER) ;
					
					Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()) ;
					timeStamp = c.getTimeInMillis();
						
				}
		
				//Log.v(TAG,"");
				
		
		}
		populateCategorySpinner(this.posSpinner) ; // populate the spinner with all available categories 
		
		updateDateValuesWithTimeStamp() ; // Update date String and values - mYear, mMonth, mDay 
		updateTimeValuesWithTimeStamp() ; // Update time String and values - mHour, mMinute, mAM_PM
		
		
	}
	
	
	/*
	 * This method is to update values related to Date 
	 * 
	 * */
	
	
	private void updateDateValuesWithTimeStamp()
	{
		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()) ;
		c.setTimeInMillis(timeStamp) ;
		
		mYear = c.get(Calendar.YEAR) ;
		mMonth = c.get(Calendar.MONTH) ;
		mDay = c.get(Calendar.DAY_OF_MONTH) ;
		expenseDate.setText(new StringBuilder().append(mDay).append("/").append(mMonth+1).append("/").append(mYear)) ;
		
		
	}
	/*
	 * This method is to update values related to Date
	 * (called from DatePicker Listener's onDateSet() 
	 * 
	 * */
	
	
	private void updateDateValuesWithTimeStamp(int year, int month, int day)
	{
		
		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()) ;
		mYear = year ;
		mMonth = month ;
		mDay = day ;
		
		c.set(Calendar.YEAR, mYear) ;
		c.set(Calendar.MONTH, mMonth) ;
		c.set(Calendar.DAY_OF_MONTH, mDay) ;
		c.set(Calendar.HOUR, mHour) ;
		c.set(Calendar.MINUTE, mMinute) ;
		c.set(Calendar.AM_PM, mAM_PM) ;
		
		timeStamp = c.getTimeInMillis() ;
		
		expenseDate.setText(new StringBuilder().append(mDay).append("/").append(mMonth+1).append("/").append(mYear)) ;
			
	}

	/*
	 * This method is to update values related to Date 
	 * 
	 * */	
	private void updateTimeValuesWithTimeStamp()
	{
		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()) ;
		c.setTimeInMillis(timeStamp) ;
		
		mHour = c.get(Calendar.HOUR) ;
		mMinute = c.get(Calendar.MINUTE) ;
		mAM_PM = c.get(Calendar.AM_PM) ;
		
		
		
		
		if(mAM_PM == 0)
			expenseTime.setText(new StringBuilder().append(mHour).append(":").append(mMinute).append(" AM")) ;
		else 
			expenseTime.setText(new StringBuilder().append(mHour).append(":").append(mMinute).append(" PM")) ;
					
	}
	/*
	 * This method is to update values related to Date
	 * (called from DatePicker Listener's onDateSet() 
	 * 
	 * */
	
	private void updateTimeValuesWithTimeStamp(int hourofday, int minute)
	{
		Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault()) ;
		c.set(Calendar.HOUR_OF_DAY,hourofday) ;
		
		mHour = c.get(Calendar.HOUR) ;
		mMinute = minute ;
		mAM_PM = c.get(Calendar.AM_PM) ;
		
		c.set(Calendar.YEAR, mYear) ;
		c.set(Calendar.MONTH, mMonth) ;
		c.set(Calendar.DAY_OF_MONTH, mDay) ;
		c.set(Calendar.HOUR, mHour) ;
		c.set(Calendar.MINUTE, mMinute) ;
		c.set(Calendar.AM_PM, mAM_PM) ;
		
		timeStamp = c.getTimeInMillis() ;
		
		if(mAM_PM == 1)
			expenseTime.setText(new StringBuilder().append(mHour).append(":").append(mMinute).append(" AM")) ;
		else 
			expenseTime.setText(new StringBuilder().append(mHour).append(":").append(mMinute).append(" PM")) ;
		
	}
	
	/*
	 * Implementation of onSavedInstanceState() callback to deal with orientation change
	 * so that values in views fields can be maintained while completing expense form 
	 * */
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putString(PAGE_HEADER, pageHeader.getText().toString());
		outState.putString(EXPENSE_ID, new StringBuilder().append(tagId).toString()) ;
		outState.putString(EXPENSE_TAG, expenseContent.getText().toString()) ;
		outState.putString(EXPENSE_AMOUNT, amountExpense.getText().toString()) ;
		outState.putString(POSITION_SPINNER, new StringBuilder().append(choosedCategory.getSelectedItemPosition()).toString());
		outState.putString(TIME_STAMP, new StringBuilder().append(timeStamp).toString()) ;
		
		outState.putStringArrayList(CATEGORY_LIST, clist) ;
		outState.putParcelableArrayList(CATEGORY_OBJECT_LIST, colist) ;
		outState.putString(POSITION_LIST, new StringBuilder().append(pos).toString()) ;
		super.onSaveInstanceState(outState);
		
		
	}

/*
 * This method is to populate spinner with all available categories
 * 
 * */

	private void populateCategorySpinner(int posSpinner) {
		// TODO Auto-generated method stub
		if(posSpinner != -1)
		{
			
			//when orientation change occurs then this is used to populate spinner
			 
			cap = new CategoryArrayAdapter(this, android.R.layout.simple_spinner_item, clist);
			cap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			choosedCategory.setAdapter(cap);
			choosedCategory.setSelection(posSpinner) ;
		}
		else
		{
			
			 // When Activity is started first time
			 
			developCategoryList();
			cap = new CategoryArrayAdapter(this, android.R.layout.simple_spinner_item, clist);
			cap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			choosedCategory.setAdapter(cap);
		}
		
		
	}
	
	
	/*
	 * This method is to initialize all views available in related xml layout of current activity
	 * */
	
	private void initializeControls() {
		// TODO Auto-generated method stub
		
		 pageHeader = (TextView)findViewById(R.id.page_header) ;
		 choosedCategory = (Spinner)findViewById(R.id.choosed_Category);	 
		 addNewCategory = (Button)findViewById(R.id.add_new_category);
		 expenseContent = (EditText)findViewById(R.id.expense_content) ;
		 amountExpense = (EditText)findViewById(R.id.amount_expense);
		 commitExpense = (Button)findViewById(R.id.commit_expense) ;
		 expenseDate = (Button)findViewById(R.id.expense_date) ;
		 expenseTime = (Button)findViewById(R.id.expense_time) ; 
		  //To change date, implementing DatePicker
		 
		 expenseDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					
				DatePickerDialog dpd = new DatePickerDialog(ExpenseCompleteActivity.this, mDListener, mYear, mMonth, mDay) ;
				dpd.show() ;
				
			}
		});

		  // To change Time, implementing TimePicker		 
		 
		 expenseTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				TimePickerDialog tpd = new TimePickerDialog(ExpenseCompleteActivity.this, mTListener, mHour, mMinute, false) ;
				tpd.show() ;
			}
		});
		  		 
		 //To add new category 
		 
		 addNewCategory.setOnClickListener(new  View.OnClickListener() {
			@Override
			public void onClick(View v) {
			
				Intent i = new Intent();
				i.setClass(ExpenseCompleteActivity.this, AddNewCategoryActivity.class);
				startActivityForResult(i, GET_CATEGORY_CODE); // Start activity to get new category		
			}
		});

		 // To commit new expense in the database
		
		commitExpense.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
					posSpinner = choosedCategory.getSelectedItemPosition() ;
					int cId = colist.get(posSpinner).getCategoryId() ; // Extracting category Id of expense
					String cName = colist.get(posSpinner).getCategoryName() ;
					//Log.v(TAG,"On commit, Category id is - "+cId+" , Category Name is - "+cName) ;			
					String expenseTag = expenseContent.getText().toString().trim() ;
				//	Log.v(TAG,"On commit, Expense amount is " +amountExpense.getText().toString()) ;	
					String eAmt  = amountExpense.getText().toString().trim() ; // Trimming of fetched String value expense amount 
					float expenseAmt = 0.0f ;
					
					if(eAmt.length() > 0)   
						expenseAmt = Float.parseFloat(eAmt) ;
					
				
				if(expenseTag.length()>0 && eAmt.length() > 0) // checked to see whether all elements of form are filled 
				{
					
					ExpenseDbAdapter edb = new ExpenseDbAdapter(ExpenseCompleteActivity.this) ;
					edb.open();
					
					long x = edb.insertExpense(expenseTag, expenseAmt, cId,timeStamp );
					edb.close() ;
						if(x!= -1) // check to see if expense inserted successfully
						{
							
							//Log.v(TAG,"Expense inserted successfully");
							if(tagId != -1) //check to see if inserted expense was tagged expense of not
							{
								 // If inserted expense was tagged expense then it is needed to be remove from
								 // unreviewed expenses DB table
								
									UnreviewedExpenseDbAdapter udb = new UnreviewedExpenseDbAdapter(ExpenseCompleteActivity.this);
									udb.open() ;
									boolean r = udb.deleteExpenseTag(tagId);
									udb.close();
									if(r)
									{
										//Log.v(TAG, "Deleted : Tagged Expense with id "+tagId);
										Intent i = new Intent() ;
										
										i.putExtra(POSITION_LIST, pos) ; // Setting position of tagged expense in unreviewed tag list  
										setResult(RESULT_OK,i);        // so that that expense can be removed from list
																		// after returning result
										//Log.v(TAG, "Result set has been done. List position "+pos);
										finish() ;
									}
									else
									{
										
										
										setResult(RESULT_CANCELED);
										finish() ;
									}
									
									
							}
							else
							{
								
								setResult(RESULT_OK);
								finish() ;
							}
						}
						else
						{
							setResult(RESULT_CANCELED);
							finish();
						}
					
					
				}	
				
				
			else
			{
				Toast.makeText(ExpenseCompleteActivity.this, "Complete Expense Details", Toast.LENGTH_SHORT).show() ;
				
			}
				
			
			}
		}) ;
		
	}

	// Creation of Listener for DatePicker so that Date values can be updated	
	private DatePickerDialog.OnDateSetListener  mDListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			updateDateValuesWithTimeStamp(year, monthOfYear, dayOfMonth) ;
			
		}
	};
	
	
	// Creation of Listener for TimePicker so that Time values can be updated
	private TimePickerDialog.OnTimeSetListener mTListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			updateTimeValuesWithTimeStamp(hourOfDay, minute) ;
		}
	};
	
	
	
	/*
	 * This callback is implemented to get new category and inserting new category into
	 * category list DB table
	 * */ 
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == GET_CATEGORY_CODE)
		{
			if(resultCode == RESULT_OK)
			{
				
				String cname = data.getStringExtra(AddNewCategoryActivity.CATEGORY_NAME);			
				if(cname.length()>0)
				{
					CategoryDbAdapter cdb = new CategoryDbAdapter(ExpenseCompleteActivity.this);
					cdb.open() ;
					long x = cdb.insertCategory(cname);
					if(x!= -1){
						//Log.v(TAG, "In onActivityResult, category inserted") ;
						ModifyCategoryList(cname);
					}
				}
			}
			
			
		}
			
		
		
	}

/*
 * This method is used to modify the category listing in spinner when new category is inserted
 * 
 * */
	private void ModifyCategoryList(String cname) {

		if(colist.size() >=1 ) // If there is already some categories are available
		{	 
			CategoryObject cob = colist.get(colist.size()-1) ;
			int id = cob.getCategoryId() + 1 ;
			clist.add(cname) ;
			
			colist.add(new CategoryObject(id, cname));
			cap.notifyDataSetChanged();
			int s = colist.size();
			//choosedCategory.setSelection(choosedCategory.getLastVisiblePosition()) ;
			choosedCategory.setSelection(s-1) ;
		}
		else if(colist.size() == 0 ) // If inserted category is first category
		{
			
			  CategoryDbAdapter cdb = new CategoryDbAdapter(this) ;
			  cdb.open() ;
			  Cursor c = cdb.fetchAllCategories() ;
			  c.moveToFirst() ;
			  int cid = c.getInt(0);
			  cdb.close() ;
			  
			  colist.add(new CategoryObject(cid, cname));
			  clist.add(cname) ;
			  cap.notifyDataSetChanged() ;
			  
			  
		}
		
	}

	
	/*
	 * To fill array list with categories available in database
	 * 
	 * */

	private void developCategoryList() {
		
		colist = new ArrayList<CategoryObject>();
		clist = new ArrayList<String>();
		CategoryDbAdapter cdb = new CategoryDbAdapter(ExpenseCompleteActivity.this) ;
		cdb.open();
		Cursor c = cdb.fetchAllCategories() ;
		if(c != null)
		{
			if(c.getCount()>0)
			{
				c.moveToFirst();
				do{
					int cid = c.getInt(0); 
					String cname = c.getString(1) ;
					CategoryObject co = new CategoryObject(cid, cname);
					colist.add(co);
					clist.add(cname);
					
				}while(c.moveToNext());
				
			}
			
		}
		
		cdb.close();
	}
	
	

}
