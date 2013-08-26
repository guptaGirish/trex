package com.example.trex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/*
 * 
 * This activity is to Add New Category in the DB Table 'categories'
 * 
 * 
 * 
 * */


public class AddNewCategoryActivity extends Activity {
	private Button cancel, addCategory ;
	private EditText newCategoryName ;

	static String CATEGORY_NAME = "category_name" ; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnewcategory);
		initializeControls();
		
		
	}

	/*
	 * 
	 * This method is to initialize all views which are in associated xml layout of activity 
	 * 
	 * */
	
	private void initializeControls() {
		
		newCategoryName = (EditText)findViewById(R.id.new_category_name);
		addCategory = (Button)findViewById(R.id.add_category);
		cancel  = (Button)findViewById(R.id.cancel);
		
		
		/*
		 * Implementing functionality when addCategory button is clicked
		 *
		 * */
		
		addCategory.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
		
				String categoryName = newCategoryName.getText().toString() ;
				Intent i = new Intent() ;
				i.putExtra(CATEGORY_NAME, categoryName) ; // Storing new category name in intent 
				setResult(Activity.RESULT_OK, i);  // Setting intent as result from this activity
				                                   // to parent activity 
				finish();  // finishing current activity
			 
			}
		});
		
		
		
		newCategoryName.requestFocus(); // Setting Focus to enter new category name to avoid extra click from user 
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); // calling soft input keyboard

		
		
		/*
		 * 
		 * Adding onClick implementation  to cancel button
		 * 
		 * */
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				finish() ;  // Finishing current activity, no result is returned back to parent activity
			}
		});
		
	}

	
	
}
