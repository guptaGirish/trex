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
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class UpdateExpenseActivity extends Activity {

	
	private String TAG = "UpdateExpenseActivity" ;

	private Button addNewCategory,updateExpense, expenseDate, expenseTime  ;
	private EditText expenseContent, amountExpense ;
	private TextView pageHeader ;
	private Spinner choosedCategory ;
    static int GET_CATEGORY_CODE = 2 ;
    private ArrayList<CategoryObject> colist ;
    private ArrayList<String> clist ;
    private CategoryArrayAdapter cap ;
    long timeStamp ;
	int pos = -1 ;
	int posSpinner = -1 ;
	int catId ;
	int eid = -1 ;
	 private String CATEGORY_LIST = "category_list" ;
	 private String CATEGORY_OBJECT_LIST = "category_object_list" ;
	 public static String POSITION_LIST = "position_in_settle_expenses_list" ;
	public static String EXPENSE_TAG = "expense_tag" ;
    public static String EXPENSE_AMOUNT = "expense_amount" ;
    private static String POSITION_SPINNER = "position_spinner" ;
    public static String CAT_ID = "cat_id" ;
    public static String TIME_STAMP = "timestamp" ;
    public static String EXPENSE_ID = "id" ;
    private int mYear, mMonth, mDay, mHour, mMinute, mAM_PM ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_complete);
		initializeControls();
		
		String eTag ;
		Float eAmt ;
		
		if(savedInstanceState != null)
		{
			eid = Integer.parseInt(savedInstanceState.getString(EXPENSE_ID)) ;
			timeStamp = Long.parseLong(savedInstanceState.getString(TIME_STAMP)) ;
			pos = Integer.parseInt(savedInstanceState.getString(POSITION_LIST)) ;
			//String
			eTag = savedInstanceState.getString(EXPENSE_TAG) ;
			//float 
			eAmt = Float.parseFloat(savedInstanceState.getString(EXPENSE_AMOUNT)) ;
			catId = Integer.parseInt(savedInstanceState.getString(CAT_ID)) ;
			clist = (ArrayList<String>)savedInstanceState.getStringArrayList(CATEGORY_LIST) ;
			colist = savedInstanceState.getParcelableArrayList(CATEGORY_OBJECT_LIST);
			posSpinner = Integer.parseInt(savedInstanceState.getString(POSITION_SPINNER)) ;
			//ValuesAssignmentToControls(eTag,eAmt);
			
		}
		else
		{
		
				Intent i = getIntent() ;
				eid = Integer.parseInt(i.getStringExtra(EXPENSE_ID)) ;
				//String 
				eTag = i.getStringExtra(EXPENSE_TAG) ;
				//float 
				eAmt = Float.parseFloat(i.getStringExtra(EXPENSE_AMOUNT)) ;
				timeStamp = Long.parseLong(i.getStringExtra(TIME_STAMP));
				pos = Integer.parseInt(i.getStringExtra(POSITION_LIST)) ;
				catId = Integer.parseInt(i.getStringExtra(CAT_ID)) ;
				//ValuesAssignmentToControls(eTag,eAmt);
				
		}
		
		ValuesAssignmentToControls(eTag,eAmt);
		
		
	}
	
	private void ValuesAssignmentToControls(String eTag, float eAmt) {
		// TODO Auto-generated method stub
		pageHeader.setText("Update Expense") ;
		expenseContent.setText(eTag) ;
		amountExpense.setText(new StringBuilder().append(eAmt)) ; 
		updateExpense.setText("Update");
		populateCategorySpinner(posSpinner);
		
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
	
	
	
	private void initializeControls() {
		// TODO Auto-generated method stub
		
		 pageHeader = (TextView)findViewById(R.id.page_header) ;
		 choosedCategory = (Spinner)findViewById(R.id.choosed_Category);	 
		 addNewCategory = (Button)findViewById(R.id.add_new_category);
		 expenseContent = (EditText)findViewById(R.id.expense_content) ;
		 amountExpense = (EditText)findViewById(R.id.amount_expense);
		 updateExpense = (Button)findViewById(R.id.commit_expense) ;
		 expenseDate = (Button)findViewById(R.id.expense_date) ;
		 expenseTime = (Button)findViewById(R.id.expense_time) ;
		 
		 
//To change date, implementing DatePicker
		 
		 expenseDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					
				DatePickerDialog dpd = new DatePickerDialog(UpdateExpenseActivity.this, mDListener, mYear, mMonth, mDay) ;
				dpd.show() ;
				
			}
		});

		  // To change Time, implementing TimePicker		 
		 
		 expenseTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				TimePickerDialog tpd = new TimePickerDialog(UpdateExpenseActivity.this, mTListener, mHour, mMinute, false) ;
				tpd.show() ;
			}
		});
		 
		 addNewCategory.setOnClickListener(new  View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(UpdateExpenseActivity.this, AddNewCategoryActivity.class);
				startActivityForResult(i, GET_CATEGORY_CODE);
				
				
			}
		});
		
		
		 updateExpense.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				posSpinner = choosedCategory.getSelectedItemPosition() ;
				int cId = colist.get(posSpinner).getCategoryId() ;
				String cName = colist.get(posSpinner).getCategoryName() ;
				//Log.v(TAG,"On commit, Category id is - "+cId+" , Category Name is - "+cName) ;			
				String expenseTag = expenseContent.getText().toString() ;
				
				String eAmt  = amountExpense.getText().toString().trim() ; // Trimming of fetched String value expense amount 
				float expenseAmt = 0.0f ;
				
				if(eAmt.length() > 0)   
					expenseAmt = Float.parseFloat(eAmt) ;
				
				if(expenseTag.length() > 0 && eAmt.length() > 0) // checked to see whether all elements of form are filled 
				{
								
						ExpenseDbAdapter edb = new ExpenseDbAdapter(UpdateExpenseActivity.this) ;
						edb.open();
								
						ContentValues updatedValues = new ContentValues() ;
						updatedValues.put(ExpenseDbAdapter.CATEGORY_ID,cId) ;
						updatedValues.put(ExpenseDbAdapter.TIME_STAMP, timeStamp) ;
						updatedValues.put(ExpenseDbAdapter.ETAG, expenseTag) ;
						updatedValues.put(ExpenseDbAdapter.AMOUNT, expenseAmt) ;
						boolean result = edb.updateExpense(eid, updatedValues) ;
						if(result)
						{
							Toast.makeText(UpdateExpenseActivity.this, "Expense Updated Successfully", Toast.LENGTH_SHORT).show() ;
							setResult(RESULT_OK) ;
							finish();
							
							
						}
						
				}
				else
				{
					Toast.makeText(UpdateExpenseActivity.this, "Complete Expense Details", Toast.LENGTH_SHORT).show() ;
					
				}
				
				
			}
		}) ;
		 
		 		
	}

	
	
	private void populateCategorySpinner(int posSpinner) {
		// TODO Auto-generated method stub
		if(posSpinner != -1)
		{
			cap = new CategoryArrayAdapter(this, android.R.layout.simple_spinner_item, clist);
			cap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			choosedCategory.setAdapter(cap);
			choosedCategory.setSelection(posSpinner) ;
		}
		else
		{
			int positionInSpinner =  developCategoryList();
			cap = new CategoryArrayAdapter(this, android.R.layout.simple_spinner_item, clist);
			cap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			choosedCategory.setAdapter(cap);
			posSpinner = positionInSpinner ;
			//Log.v(TAG,"In populateCategorySpinner, posSpinner is - "+ posSpinner);
			
			choosedCategory.setSelection(posSpinner) ;
		}
		
		
	}
	
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {

//		outState.putString(PAGE_HEADER, pageHeader.getText().toString());
		outState.putString(EXPENSE_ID, new StringBuilder().append(eid).toString()) ;
		outState.putString(EXPENSE_TAG, expenseContent.getText().toString()) ;
		outState.putString(EXPENSE_AMOUNT, amountExpense.getText().toString()) ;
		outState.putString(POSITION_SPINNER,new StringBuilder().append(choosedCategory.getSelectedItemPosition()).toString());
		outState.putString(TIME_STAMP,new StringBuilder().append(timeStamp).toString()) ;
		outState.putString(CAT_ID, new StringBuilder().append(catId).toString()) ;
		outState.putStringArrayList(CATEGORY_LIST, clist) ;
		outState.putParcelableArrayList(CATEGORY_OBJECT_LIST, colist) ;
		outState.putString(POSITION_LIST, new StringBuilder().append(pos).toString()) ;
		super.onSaveInstanceState(outState);
		
		
		
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
				// TODO Auto-generated method stub
				updateTimeValuesWithTimeStamp(hourOfDay, minute) ;
			}
		};
		
		
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == GET_CATEGORY_CODE)
		{
			if(resultCode == RESULT_OK)
			{
				String cname = data.getStringExtra(AddNewCategoryActivity.CATEGORY_NAME);
				//pageHeader.setText(cname) ;
				if(cname.length()>0)
				{
					CategoryDbAdapter cdb = new CategoryDbAdapter(UpdateExpenseActivity.this);
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

	
	private void ModifyCategoryList(String cname) {
		// TODO Auto-generated method stub
		CategoryObject cob = colist.get(colist.size()-1) ;
		int id = cob.getCategoryId() + 1 ;
		clist.add(cname) ;
		
		colist.add(new CategoryObject(id, cname));
		cap.notifyDataSetChanged();
		posSpinner = colist.size();
		//choosedCategory.setSelection(choosedCategory.getLastVisiblePosition()) ;
		
		choosedCategory.setSelection(posSpinner) ;
		
		
		
	}


	private int developCategoryList() {
		// TODO Auto-generated method stub
		int positionInSpinner = 0;
		colist = new ArrayList<CategoryObject>();
		clist = new ArrayList<String>();
		CategoryDbAdapter cdb = new CategoryDbAdapter(UpdateExpenseActivity.this) ;
		cdb.open();
		Cursor c = cdb.fetchAllCategories() ;
		if(c != null)
		{
			if(c.getCount()>0)
			{
				c.moveToFirst();
				do{
					int cid = c.getInt(0);
					if(cid == catId)
					{
						positionInSpinner = clist.size()  ;
						Log.v(TAG,"In developeCategoryList, posSpinner is "+positionInSpinner);
					}
					String cname = c.getString(1) ;
					CategoryObject co = new CategoryObject(cid, cname);
					colist.add(co);
					clist.add(cname);
					
				}while(c.moveToNext());
				
			}
			
		}
		
		cdb.close();
		Log.v(TAG,"In developeCategoryList, posSpinner is "+positionInSpinner);
		return positionInSpinner ;
	}
	
	
	
	
	

}
