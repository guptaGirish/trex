package com.example.trex;

import java.util.ArrayList;

import com.example.trex.adapters.ExpenseDbAdapter;
import com.example.trex.listingexpense.ExpenseArrayAdapter;
import com.example.trex.listingexpense.ExpenseObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryExpenseActivity extends Activity {

	public static String CATEGORY_ID = "cid" ;
	public static String CATEGORY_NAME = "cname" ;
	ArrayList<ExpenseObject> expenseArrayList ;
	ListView expenseList ;
	TextView categoryName ;
	Button sendMail ;
	ExpenseArrayAdapter eap ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_categoryexpense);
		
		
		initializeControls() ;
		
		Intent i = getIntent() ;
		int cId = Integer.parseInt(i.getStringExtra(CATEGORY_ID)) ;
		String cName = i.getStringExtra(CATEGORY_NAME) ;
		
		
		populateExpenseList(cId) ;
		
	}
	

	private void populateExpenseList(int cId) {
		// TODO Auto-generated method stub
		ExpenseDbAdapter edb = new ExpenseDbAdapter(this) ;
		edb.open() ;
		Cursor c = edb.fetchAllExpenses(cId);
		expenseArrayList = new ArrayList<ExpenseObject>();
		
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
					
					ExpenseObject eo =  new ExpenseObject(id, name, cid, amount, timeStamp);
					expenseArrayList.add(eo) ;
					
					
					
				}while(c.moveToNext()) ;
				
				
			}
			
			
		}
		
		c.close() ;
		edb.close() ;
		
		
		eap = new ExpenseArrayAdapter(this, R.layout.layout_expense, expenseArrayList);
		expenseList.setAdapter(eap) ;
		
		
		
	}

	private void initializeControls() {
		// TODO Auto-generated method stub
		
		expenseList = (ListView)findViewById(R.id.expense_list);
		categoryName = (TextView)findViewById(R.id.category_name) ;
		sendMail = (Button)findViewById(R.id.send_mail) ;
		
		sendMail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	
	

}
