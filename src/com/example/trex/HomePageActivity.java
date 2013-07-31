package com.example.trex;

import java.util.ArrayList;

import com.example.trex.adapters.DbAdapter;
import com.example.trex.adapters.UnreviewedExpenseDbAdapter;
import com.example.trex.listingunreviewedtags.CustomArrayAdapter;
import com.example.trex.listingunreviewedtags.UnreviewedTagObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class HomePageActivity extends Activity  {
	
	private ListView tagList ;
	private Button addNewExpense, viewCategorizedExpense, settings ;
	ArrayList<UnreviewedTagObject> list ;
	CustomArrayAdapter cad;
	
	String TAG = "HomePageActivity" ;
	public static int EXPENSE_COMPLETE_CODE = 4 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Log.v(TAG,"In onCreate");
        createDatabase();
        initializeControls();
        populateTagList();
        
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        
    }


	private void initializeControls() {
		// TODO Auto-generated method stub
		Log.v(TAG,"In initializeControls ");
    	
    	tagList = (ListView)findViewById(R.id.tag_list);  	
    	addNewExpense = (Button)findViewById(R.id.add_new_expense);
    	addNewExpense.setOnClickListener(onClickListener);
    	viewCategorizedExpense = (Button)findViewById(R.id.view_categorized_expense);
    	viewCategorizedExpense.setOnClickListener(onClickListener);
    	settings = (Button)findViewById(R.id.settings);
    	settings.setOnClickListener(onClickListener);
		
	}

	

	private void createDatabase() {
		// TODO Auto-generated method stub
		Log.v(TAG,"In createDatabase : Creating DB");
		DbAdapter dbAdapter = new DbAdapter(this);
		dbAdapter.open();
		dbAdapter.close();
    	
	}


	private void populateTagList() {
		// TODO Auto-generated method stub
		
		Log.v(TAG,"In populateTagList");
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
						Log.v(TAG,"id is "+id+", tag is "+tag+", timestamp is "+timestamp);
						
					}while(c.moveToNext());
					
			}
			udb.close();
			c.close() ;
		}
		
		
		cad = new CustomArrayAdapter(this, R.layout.layout_tagged_expense, list);
		tagList.setAdapter(cad);
		
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.v(TAG,"In onActivityResult");
		Log.v(TAG,"request code is "+requestCode+" , result code is "+resultCode);
		if(requestCode == EXPENSE_COMPLETE_CODE)
		{
			if(resultCode == RESULT_OK)
			{
				Log.v(TAG,"In processing partt");
				
				int pos = data.getIntExtra(ExpenseCompleteActivity.POSITION_LIST, -1) ;
				Log.v(TAG, "In onActivityResult: position in list is "+pos) ;
				if(pos != -1)
				{
					Log.v(TAG, "In onActivityResult: List content is - "+list.get(pos).getTag().toString()) ;
					list.remove(pos) ;
					
					cad.notifyDataSetChanged();
					
				}
				
			}
			
		}
		
		
		
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }


	OnClickListener onClickListener = new OnClickListener() {
		
		Intent i = new Intent() ;
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
				case R.id.add_new_expense :
				{
					i.setClass(HomePageActivity.this,com.example.trex.ExpenseCompleteActivity.class );
					i.putExtra(ExpenseCompleteActivity.ACTION, ExpenseCompleteActivity.ADD_NEW_ACTION);
					HomePageActivity.this.startActivity(i);
					break ;
				}
				case R.id.view_categorized_expense :
				{
					i.setClass(HomePageActivity.this,com.example.trex.CategoryListingActivity.class );
					HomePageActivity.this.startActivity(i);
					break ;
				}
				case R.id.settings :
				{
					i.setClass(HomePageActivity.this,com.example.trex.SettingsActivity.class );
					HomePageActivity.this.startActivity(i);
					break ;
				}
			}
			
			
		}
	};
	
	
}
