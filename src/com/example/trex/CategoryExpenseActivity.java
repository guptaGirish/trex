package com.example.trex;

import java.util.ArrayList;

import com.example.trex.adapters.CategoryDbAdapter;
import com.example.trex.adapters.ExpenseDbAdapter;
import com.example.trex.listingexpense.ExpenseArrayAdapter;
import com.example.trex.listingexpense.ExpenseObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.p2p.WifiP2pManager.UpnpServiceResponseListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryExpenseActivity extends Activity implements ExpenseArrayAdapter.ListUpdateCallBack {

	public static int UPDATE_EXPENSE = 2 ;
	public static String CATEGORY_ID = "cid" ;
	public static String CATEGORY_NAME = "cname" ;
	ArrayList<ExpenseObject> UsExpenseArrayList, SExpenseArrayList ;
	ListView UsExpenseList, SExpenseList ;
	TextView categoryName ;
	Button sendMail ;
	ExpenseArrayAdapter UsEap,SEap ;
	static int cId ;
	private String TAG = "CategoryExpenseActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_categoryexpense);
		initializeControls() ;
		
		Intent i = getIntent() ;
		cId = Integer.parseInt(i.getStringExtra(CATEGORY_ID)) ;
		String cName = i.getStringExtra(CATEGORY_NAME) ;
		
		populateExpenseList(cId) ;
	
		
		
		
	}
	

	private void populateExpenseList(int cId) {
		// TODO Auto-generated method stub
		
		
		fillArrayLists(cId);
		
		UsEap = new ExpenseArrayAdapter(this, R.layout.layout_expense, UsExpenseArrayList);
		SEap = new ExpenseArrayAdapter(this, R.layout.layout_expense, SExpenseArrayList);
		UsExpenseList.setAdapter(UsEap) ;
		SExpenseList.setAdapter(SEap);
		
		
		
	}

	void fillArrayLists(int cId)
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
					Log.v(TAG, "id is "+id+", name is "+name+", Amt is "+amount+", cid is "+cid+", timeStamp is "+timeStamp);
					
					int settleFlag = c.getInt(5) ;
					
					ExpenseObject eo =  new ExpenseObject(id, name, cid, amount, timeStamp,settleFlag);
					
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
	
	
	private void initializeControls() {
		// TODO Auto-generated method stub
		
		UsExpenseList = (ListView)findViewById(R.id.us_expense_list) ;
		SExpenseList  = (ListView)findViewById(R.id.s_expense_list) ;
 		categoryName = (TextView)findViewById(R.id.category_name) ;
		sendMail = (Button)findViewById(R.id.send_mail) ;
		
		sendMail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	

	@Override
	public void onUpdateList(int id,String name, int cid, float amount, long timeStamp,int flag ) {
		// TODO Auto-generated method stub
		Log.v(TAG, "On Updation of both lists");
		ExpenseObject eo = new ExpenseObject(id, name, cid, amount, timeStamp, flag) ;
		Log.v(TAG, "before adding object to list");
		SExpenseArrayList.add(eo) ;
		Log.v(TAG, "before notifying adapter");
		SEap.notifyDataSetChanged() ;
		//populateExpenseList(cId);
		
	}


	@Override
	public int getCategoryId() {
		// TODO Auto-generated method stub
		return cId;
	}


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
