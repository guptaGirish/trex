package com.example.trex;

import java.util.ArrayList;

import com.example.trex.adapters.CategoryDbAdapter;
import com.example.trex.adapters.ExpenseDbAdapter;
import com.example.trex.adapters.UnreviewedExpenseDbAdapter;
import com.example.trex.listingcategory.CategoryArrayAdapter;
import com.example.trex.listingcategory.CategoryObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ExpenseCompleteActivity extends Activity{
	
	String TAG = "ExpenseCompleteActivity" ;

	Button addNewCategory,commitExpense ;
	EditText expenseContent, amountExpense ;
	TextView pageHeader ;
    Spinner choosedCategory ;
    static int GET_CATEGORY_CODE = 2 ;
	
	private String EDIT_HEADER = "Complete Your Expense" ;
	private String ADD_NEW_HEADER = "Add New Expnese" ;
    
    public static String ACTION = "operation" ;
    public static String EDIT_ACTION = "complete_expense" ;
    public static String ADD_NEW_ACTION = "add_new_expense" ;
    public static String EXPENSE_TAG = "expense_tag" ;
    public static String EXPENSE_AMOUNT = "expense_amount" ;
    public static String TIME_STAMP = "timestamp" ;
    public static String EXPENSE_ID = "id" ;
    public static String POSITION_LIST = "position" ;
    //public static String EXPENSE_TAG = "empense_tag" ;
    //public static String AMOUNT = "amount" ;
    ArrayList<CategoryObject> colist ;
    ArrayList<String> clist ;
    CategoryArrayAdapter cap ;
    long timeStamp = -1;
    int tagId = -1 ;
	int pos = -1 ;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Log.v(TAG,"In onCreate");
		setContentView(R.layout.actvity_expense_complete);
		
		initializeControls();
		
		populateCategorySpinner();
		
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
		else
		{
			pageHeader.setText(ADD_NEW_HEADER) ;
			
			
		}
		
		Log.v(TAG,"");
		
		
		
		populateCategorySpinner() ;
		
	}
	
	
	private void populateCategorySpinner() {
		// TODO Auto-generated method stub
		developCategoryList();
		
		cap = new CategoryArrayAdapter(this, android.R.layout.simple_list_item_1, clist);
		cap.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		choosedCategory.setAdapter(cap);
		
	}
	
	
	
	private void initializeControls() {
		// TODO Auto-generated method stub
		
		 pageHeader = (TextView)findViewById(R.id.page_header) ;
		 choosedCategory = (Spinner)findViewById(R.id.choosed_Category);	 
		 addNewCategory = (Button)findViewById(R.id.add_new_category);
		 expenseContent = (EditText)findViewById(R.id.expense_content) ;
		 amountExpense = (EditText)findViewById(R.id.amount_expense);
		 commitExpense = (Button)findViewById(R.id.commit_expense) ;
		 
		 
		 
		addNewCategory.setOnClickListener(new  View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(ExpenseCompleteActivity.this, AddNewCategoryActivity.class);
				startActivityForResult(i, GET_CATEGORY_CODE);
				
				
			}
		});
		
		
		commitExpense.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				
					int posSpinner = choosedCategory.getSelectedItemPosition() ;
					int cId = colist.get(posSpinner).getCategoryId() ;
					String cName = colist.get(posSpinner).getCategoryName() ;
					Log.v(TAG,"On commit, Category id is - "+cId+" , Category Name is - "+cName) ;
					
					String expenseTag = expenseContent.getText().toString() ;
					float expenseAmt = Float.parseFloat(amountExpense.getText().toString()) ;
					
					
					
					
					
					ExpenseDbAdapter edb = new ExpenseDbAdapter(ExpenseCompleteActivity.this) ;
					edb.open();
						if(timeStamp == -1){
							Time t = new Time();
							t.setToNow() ;
							long timeStamp = t.toMillis(false);
						
						}
					long x = edb.insertExpense(expenseTag, expenseAmt, cId,timeStamp );
					edb.close() ;
						if(x!= -1)
						{
							Log.v(TAG,"Expense inserted successfully");
							if(tagId != -1)
							{
									UnreviewedExpenseDbAdapter udb = new UnreviewedExpenseDbAdapter(ExpenseCompleteActivity.this);
									udb.open() ;
									boolean r = udb.deleteExpenseTag(tagId);
									udb.close();
									if(r)
									{
										Log.v(TAG, "Deleted : Tagged Expense with id "+tagId);
										Intent i = new Intent() ;
										
										i.putExtra(POSITION_LIST, pos) ;
										setResult(RESULT_OK,i);
										Log.v(TAG, "Result set has been done. List position "+pos);
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
		}) ;
		
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
					CategoryDbAdapter cdb = new CategoryDbAdapter(ExpenseCompleteActivity.this);
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
		
		
		
	}


	private void developCategoryList() {
		// TODO Auto-generated method stub
		
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
