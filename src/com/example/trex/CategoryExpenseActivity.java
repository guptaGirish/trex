package com.example.trex;

import java.util.ArrayList;
import com.example.trex.adapters.ExpenseDbAdapter;
import com.example.trex.listingexpense.ExpenseArrayAdapter;
import com.example.trex.listingexpense.ExpenseObject;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/*
 * 
 * This activity is to list both kinds of expenses (Unsettled and Settled) in 
 * different Lists.
 * 
 * */


public class CategoryExpenseActivity extends Activity implements ExpenseArrayAdapter.ListUpdateCallBack {

	public static int UPDATE_EXPENSE = 2 ;
	public static String CATEGORY_ID = "cid" ;
	public static String CATEGORY_NAME = "cname" ;
	private ArrayList<ExpenseObject> UsExpenseArrayList, SExpenseArrayList ;
	private ListView UsExpenseList, SExpenseList ;
	private TextView categoryName ;
	private Button sendMail ;
	private ExpenseArrayAdapter UsEap,SEap ;
	static int cId ;
	static String cName ;
	private String TAG = "CategoryExpenseActivity";
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_categoryexpense);
		initializeControls() ;  // initialize view elements
		
		Intent i = getIntent() ;
		cId = Integer.parseInt(i.getStringExtra(CATEGORY_ID)) ; // Extract category Id from intent 
		cName = i.getStringExtra(CATEGORY_NAME) ; // Extract category Name from intent 
		populateExpenseList(cId) ;  // populate expenses list of the asked category 
	
	}
	

	
	/*
	 * This method is to populate the lists with all expenses of asked category.
	 * 
	 * 2 separate Lists are for - 
	 *  - Settled Expenses
	 *  - Unsettled Expenses
	 * 
	 * */
	
	private void populateExpenseList(int cId) {
		
		
		fillArrayLists(cId);	// Fill the arraylists with expense details
		
		
		
		//  Associate the created arraylists of expenses with their relative adapters 
		 
		UsEap = new ExpenseArrayAdapter(this, R.layout.layout_expense, UsExpenseArrayList);
		SEap = new ExpenseArrayAdapter(this, R.layout.layout_expense, SExpenseArrayList);
		
		
		 //  Set adapters to relative Listviews 
		
		UsExpenseList.setAdapter(UsEap) ;
		SExpenseList.setAdapter(SEap);
		
	}

	
	
	/*
	 * This method is to fill the ArrayLists with objects of expenses stored in the DB 
	 * which are associated with cId.
	 * 
	 * In this first all expenses of 'cId' are fetch from DB then
	 * iterating through all fetched row, settle flag of every row is checked
	 * to know whether it is settled or unsettled expense and based on that flag
	 * that expense row's content in the form of object is added to related ArrayList 
	 * 
	 * 
	 * */
	
	private void fillArrayLists(int cId)
	{
		
		ExpenseDbAdapter edb = new ExpenseDbAdapter(this) ;
		edb.open() ;
		Cursor c = edb.fetchAllExpenses(cId);
		SExpenseArrayList = new ArrayList<ExpenseObject>();
		UsExpenseArrayList = new ArrayList<ExpenseObject>();
		if(c!=null)
		{
			if(c.getCount() > 0)
			{
				c.moveToFirst() ;
				do{
					int id = c.getInt(0);
					String name = c.getString(1);
					float amount = c.getFloat(2);
					int cid = c.getInt(3);
					long timeStamp = c.getLong(4);
		//			Log.v(TAG, "id is "+id+", name is "+name+", Amt is "+amount+", cid is "+cid+", timeStamp is "+timeStamp);
					
					int settleFlag = c.getInt(5) ;
					
					ExpenseObject eo =  new ExpenseObject(id, name, cid, amount, timeStamp,settleFlag);
					
					// If settleFlag is 0 it means expense is not settled
					
					if(settleFlag == 0)
					    UsExpenseArrayList.add(eo) ;  
					else
						SExpenseArrayList.add(eo);
					
				}while(c.moveToNext()) ;
				
				
			}
			
			
		}
		
		c.close() ;
		edb.close() ;
		
	}
	
	
	/*
	 * This method is to initialize all views available in associated xml of 
	 * current activity
	 * 
	 * */
	
	private void initializeControls() {
		// TODO Auto-generated method stub
		
		UsExpenseList = (ListView)findViewById(R.id.us_expense_list) ;
		SExpenseList  = (ListView)findViewById(R.id.s_expense_list) ;
 		categoryName = (TextView)findViewById(R.id.category_name) ;
		sendMail = (Button)findViewById(R.id.send_mail) ;
		
		
		categoryName.setText(cName);
		
		
		 //Implementing the functionality associated with sendMail Button
		
		sendMail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				
			}
		});
		
	}

	/*
	 *If one expense is settled from unsettled expenses lists then this function is
	 * called to update the settled expenses lists by adding that expense to settled 
	 * expenses list which has been removed from unsettled expenses list
	 *
	 * This method is called from 'ExpenseArrayAdapter' using instance of 
	 * 'ListUpdateCallBack' interface
	 * 
	 * 
	 * */
	

	@Override
	public void onUpdateList(int id,String name, int cid, float amount, long timeStamp,int flag ) {
		
		//Log.v(TAG, "On Updation of both lists");
		ExpenseObject eo = new ExpenseObject(id, name, cid, amount, timeStamp, flag) ;
		//Log.v(TAG, "before adding object to list");
		SExpenseArrayList.add(eo) ;
		//Log.v(TAG, "before notifying adapter");
		SEap.notifyDataSetChanged() ;
		
		
	}

	/*
	 * This function is used to provide cId to ExpenseArrayAdapter using instance of 
	 * 'ListUpdateCallBack' interface 
	 * 
	 * */
		
	@Override
	public int getCategoryId() {
		// TODO Auto-generated method stub
		return cId;
	}

	
	
	/*
	 * This callback method is to update the expense list with updated expenses.
	 * 
	 * */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == UPDATE_EXPENSE)
		{
			if(resultCode == RESULT_OK)
			{
				
				populateExpenseList(cId) ;
			}
			
			
		}
		
	}
	
	
	
	
	

}
