package com.example.trex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class AddNewCategoryActivity extends Activity {
	Button cancel, addCategory ;
	EditText newCategoryName ;
	static String CATEGORY_NAME = "category_name" ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnewcategory);
		initializeControls();
		
		
	}

	private void initializeControls() {
		// TODO Auto-generated method stub
		
		newCategoryName = (EditText)findViewById(R.id.new_category_name);
		addCategory = (Button)findViewById(R.id.add_category);
		cancel  = (Button)findViewById(R.id.cancel);
		
		
		addCategory.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String categoryName = newCategoryName.getText().toString() ;
				Intent i = new Intent() ;
				i.putExtra(CATEGORY_NAME, categoryName) ;
				setResult(Activity.RESULT_OK, i);
				finish();
			 
			}
		});
		
		newCategoryName.requestFocus();

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish() ;
			}
		});
		
	}

	
	
}
