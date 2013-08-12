package com.example.trex;

import java.util.ArrayList;

import com.example.trex.adapters.CategoryDbAdapter;
import com.example.trex.adapters.ExpenseDbAdapter;
import com.example.trex.adapters.UnreviewedExpenseDbAdapter;
import com.example.trex.listingcategory.CategoryArrayAdapter;
import com.example.trex.listingcategory.CategoryObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateExpenseActivity extends Activity {

	
	String TAG = "UpdateExpenseActivity" ;

	Button addNewCategory,updateExpense ;
	EditText expenseContent, amountExpense ;
	TextView pageHeader ;
    Spinner choosedCategory ;
    static int GET_CATEGORY_CODE = 2 ;
    ArrayList<CategoryObject> colist ;
    ArrayList<String> clist ;
    CategoryArrayAdapter cap ;
    long timeStamp = -1;
    int tagId = -1 ;
	int pos = -1 ;
	int posSpinner = -1 ;
	int catId ;
	int eid ;
	 private String CATEGORY_LIST = "category_list" ;
	 private String CATEGORY_OBJECT_LIST = "category_object_list" ;
	public static String EXPENSE_TAG = "expense_tag" ;
    public static String EXPENSE_AMOUNT = "expense_amount" ;
    private static String POSITION_SPINNER = "position_spinner" ;
    public static String CAT_ID = "cat_id" ;
    public static String TIME_STAMP = "timestamp" ;
    public static String EXPENSE_ID = "id" ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvity_expense_complete);
		initializeControls();
		
		
		if(savedInstanceState != null)
		{
			eid = Integer.parseInt(savedInstanceState.getString(EXPENSE_ID)) ;
			String eTag = savedInstanceState.getString(EXPENSE_TAG) ;
			float eAmt = Float.parseFloat(savedInstanceState.getString(EXPENSE_AMOUNT)) ;
			catId = Integer.parseInt(savedInstanceState.getString(CAT_ID)) ;
			clist = (ArrayList<String>)savedInstanceState.getStringArrayList(CATEGORY_LIST) ;
			colist = savedInstanceState.getParcelableArrayList(CATEGORY_OBJECT_LIST);
			posSpinner = Integer.parseInt(savedInstanceState.getString(POSITION_SPINNER)) ;
			ValuesAssignmentToControls(eTag,eAmt);
			
		}
		else
		{
		
		
		
		Intent i = getIntent() ;
		eid = Integer.parseInt(i.getStringExtra(EXPENSE_ID)) ;
		String eTag = i.getStringExtra(EXPENSE_TAG) ;
		float eAmt = Float.parseFloat(i.getStringExtra(EXPENSE_AMOUNT)) ;
		catId = Integer.parseInt(i.getStringExtra(CAT_ID)) ;
		
		ValuesAssignmentToControls(eTag,eAmt);
		
		
		
		
		}
		
		
	}
	
	private void ValuesAssignmentToControls(String eTag, float eAmt) {
		// TODO Auto-generated method stub
		pageHeader.setText("Update Expense") ;
		expenseContent.setText(eTag) ;
		amountExpense.setText(""+eAmt) ; 
		
		updateExpense.setText("Update");
		populateCategorySpinner(posSpinner);
	}

	private void initializeControls() {
		// TODO Auto-generated method stub
		
		 pageHeader = (TextView)findViewById(R.id.page_header) ;
		 choosedCategory = (Spinner)findViewById(R.id.choosed_Category);	 
		 addNewCategory = (Button)findViewById(R.id.add_new_category);
		 expenseContent = (EditText)findViewById(R.id.expense_content) ;
		 amountExpense = (EditText)findViewById(R.id.amount_expense);
		 updateExpense = (Button)findViewById(R.id.commit_expense) ;
		 
		 
		
		 
		 
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
				// TODO Auto-generated method stub
				
				posSpinner = choosedCategory.getSelectedItemPosition() ;
				int cId = colist.get(posSpinner).getCategoryId() ;
				String cName = colist.get(posSpinner).getCategoryName() ;
				Log.v(TAG,"On commit, Category id is - "+cId+" , Category Name is - "+cName) ;			
				String expenseTag = expenseContent.getText().toString() ;
				float expenseAmt = Float.parseFloat(amountExpense.getText().toString()) ;
				
								
				ExpenseDbAdapter edb = new ExpenseDbAdapter(UpdateExpenseActivity.this) ;
				edb.open();
				Time t = new Time();
				t.setToNow() ;
				long timeStamp = t.toMillis(false);
				
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
		}) ;
		 
		 		
	}

	
	
	private void populateCategorySpinner(int posSpinner) {
		// TODO Auto-generated method stub
		if(posSpinner != -1)
		{
			cap = new CategoryArrayAdapter(this, android.R.layout.simple_list_item_1, clist);
			cap.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
			choosedCategory.setAdapter(cap);
			choosedCategory.setSelection(posSpinner) ;
		}
		else
		{
		int positionInSpinner =  developCategoryList();
		cap = new CategoryArrayAdapter(this, android.R.layout.simple_list_item_1, clist);
		cap.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		choosedCategory.setAdapter(cap);
		posSpinner = positionInSpinner ;
		Log.v(TAG,"In populateCategorySpinner, posSpinner is - "+ posSpinner);
		
		choosedCategory.setSelection(posSpinner) ;
		}
		
		
	}
	
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
//		outState.putString(PAGE_HEADER, pageHeader.getText().toString());
		outState.putString(EXPENSE_ID, ""+eid) ;
		outState.putString(EXPENSE_TAG, expenseContent.getText().toString()) ;
		outState.putString(EXPENSE_AMOUNT, amountExpense.getText().toString()) ;
		outState.putString(POSITION_SPINNER, ""+choosedCategory.getSelectedItemPosition());
		//outState.putString(TIME_STAMP, ""+timeStamp) ;
		outState.putString(CAT_ID, ""+catId) ;
		outState.putStringArrayList(CATEGORY_LIST, clist) ;
		outState.putParcelableArrayList(CATEGORY_OBJECT_LIST, colist) ;
	//	outState.putString(POSITION_LIST, ""+pos) ;
		super.onSaveInstanceState(outState);
		
		
		
	}

	
	
	
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
						Log.v(TAG, "In onActivityResult, category inserted") ;
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
