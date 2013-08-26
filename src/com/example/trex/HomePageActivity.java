package com.example.trex;

import java.util.ArrayList;

import com.example.trex.adapters.DbAdapter;
import com.example.trex.adapters.UnreviewedExpenseDbAdapter;
import com.example.trex.listingunreviewedtags.CustomArrayAdapter;
import com.example.trex.listingunreviewedtags.UnreviewedTagObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
/*
 * 
 * This is the welcome Activity of App.
 * This activity lists all tagged expenses
 * and provide 3 buttons to 
 *  - add new category
 *  - manage categories
 *  - settings
 * 
 * */


public class HomePageActivity extends Activity  {
	
	private ListView tagList ;
	private Button addNewExpense, viewCategorizedExpense, settings ;
	private ArrayList<UnreviewedTagObject> list ;
	private CustomArrayAdapter cad;
	private String TAG = "HomePageActivity" ;
	public static int EXPENSE_COMPLETE_CODE = 4 ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //Log.v(TAG,"In onCreate");
        createDatabase();
        initializeControls();
        populateTagList(); // populate list of all the tagged expenses 
        
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getWindow().setSoftInputMode(Configuration.KEYBOARD_12KEY); 
        
    }

/*
 * This method is to initialize views of associated xml layout 
 * 
 * */
	private void initializeControls() {
		
		//Log.v(TAG,"In initializeControls ");
    	
		tagList = (ListView)findViewById(R.id.tag_list);  	
    	addNewExpense = (Button)findViewById(R.id.add_new_expense);

    	viewCategorizedExpense = (Button)findViewById(R.id.view_categorized_expense);

    	settings = (Button)findViewById(R.id.settings);
		
    	addNewExpense.setOnClickListener(onClickListener);
    	
    	
    	viewCategorizedExpense.setOnClickListener(onClickListener);
    	
    	settings.setOnClickListener(onClickListener);
		
	}
	
	/*
	 * If this activity is opened first time after installation of this app in the device then
	 * creation of DB schema for this app is done using this method 
	 * 
	 * */
		private void createDatabase() {
			
			//Log.v(TAG,"In createDatabase : Creating DB");
			DbAdapter dbAdapter = new DbAdapter(HomePageActivity.this);
			dbAdapter.open();
			dbAdapter.close();
	    	
		}

		/*
		 * This method is to populate listView with all uncomplete tagged expenses stored in DB
		 * */

		private void populateTagList() {
			
			//Log.v(TAG,"In populateTagList");
			
			developTagListArray() ;
			cad = new CustomArrayAdapter(HomePageActivity.this, R.layout.layout_tagged_expense, list);
			tagList.setAdapter(cad);
			
			
			
		}
		/*
		 * This method is to develop ArrayList of objectst of tagged expenses for Listview 
		 * 
		 * */
		
		void developTagListArray()
		{
			UnreviewedExpenseDbAdapter udb = new UnreviewedExpenseDbAdapter(HomePageActivity.this);
			udb.open();
			Cursor c = udb.fetchAllExpensesTags();
			list = new ArrayList<UnreviewedTagObject>();
			
			if(c != null)
			{
				if(c.getCount() > 0)
				{
						//int nEntries = c.getCount() ;
						c.moveToFirst() ;
						//c.moveToNext();
						do{
							int id = c.getInt(0);
							String tag = c.getString(1);
							long timestamp = c.getLong(2);
							UnreviewedTagObject uob = new UnreviewedTagObject(id, tag, timestamp);
							list.add(uob);
							//Log.v(TAG,"id is "+id+", tag is "+tag+", timestamp is "+timestamp);
							
						}while(c.moveToNext());
						
				}
				udb.close();
				c.close() ;
			}
			
			
		}
		
		/*
		 * This callback is to update Unreviewed expenses tags list, when one of them gets completed
		 *
		 * */
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
			super.onActivityResult(requestCode, resultCode, data);
			//Log.v(TAG,"In onActivityResult");
			//Log.v(TAG,"request code is "+requestCode+" , result code is "+resultCode);
			if(requestCode == EXPENSE_COMPLETE_CODE)
			{
				if(resultCode == RESULT_OK)
				{
					//Log.v(TAG,"In processing partt");
					
					int pos = data.getIntExtra(ExpenseCompleteActivity.POSITION_LIST, -1) ;
					//Log.v(TAG, "In onActivityResult: position in list is "+pos) ;
					if(pos != -1)
					{
						//Log.v(TAG, "In onActivityResult: List content is - "+list.get(pos).getTag().toString()) ;
						list.remove(pos) ;
						cad.notifyDataSetChanged();
						
					}
					
				}
				
			}
			
			
			
		}


		

		
		/*
		 * Listener to start related Activities of button -
		 *  - Add New Expense
		 *  - View Categoried Expense
		 *  - Settings
		 *  
		 * */

		OnClickListener onClickListener = new OnClickListener() {
			
			Intent i = new Intent() ;
			
			@Override
			public void onClick(View v) {
				switch(v.getId())
				{
					case R.id.add_new_expense :
					{
						i.setClass(HomePageActivity.this,com.example.trex.ExpenseCompleteActivity.class );
						i.putExtra(ExpenseCompleteActivity.ACTION, ExpenseCompleteActivity.ADD_NEW_ACTION);
						startActivity(i);
						break ;
					}
					case R.id.view_categorized_expense :
					{
						i.setClass(HomePageActivity.this,com.example.trex.CategoryListingActivity.class );
						startActivity(i);
						break ;
					}
					case R.id.settings :
					{
						i.setClass(HomePageActivity.this,com.example.trex.SettingsActivity.class );
						startActivity(i);
						break ;
					}
				}
				
				
			}
		};
		
		
	
}
