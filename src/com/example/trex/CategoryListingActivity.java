package com.example.trex;

import java.util.ArrayList;
import com.example.trex.adapters.CategoryDbAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 
 * This activity is to List all categories of expenses in a ListView
 * and providing facility to delete and expand each row item(category) of ListView 
 *  
 * 
 * */


public class CategoryListingActivity extends Activity{

	private String TAG = "CategoryListingActivity" ; // A Tag to mention Activity's name in Log
	private ListView listCategory ;
	private ArrayList<CategoryObject> clist ;
	private CategoryArrayAdapter cad ;

	/*
	 * This callback implementation is to assign xml layout to activity and 
	 * initiating population of ListView
	 * 
	 * */
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_categories_listing);
		//Log.v(TAG,"");
		listCategory = (ListView)findViewById(R.id.list_category);
		populateList();
		
	}
	
	/*
	 * This method is to populate category listview with already available 
	 * categories in the DB
	 * 
	 * */
	
	
	private void populateList() {
		
		fillArrayList() ;// fill arraylist with all the aviable categories in DB
		//Log.v(TAG,"In populateList, After building ArrayList");
		
		// Build adapter for listview with filled arraylist 
		
		cad = new CategoryArrayAdapter(CategoryListingActivity.this, R.layout.layout_category_row, clist);
		listCategory.setAdapter(cad);  // set adapter for listview 
		
	}
	
	
	/*
	 * This method is to fill ArrayList for ListView to build its adapter
	 * To fill ArrayList, first all categories are fetched from DB table 'categories'
	 * and iteration is done through each category's row content, then object is build for 
	 * each row and added to ArrayList 
	 * 
	 * */
	
	
	private void fillArrayList()
	{
		CategoryDbAdapter cdb = new CategoryDbAdapter(this);
		cdb.open();
		Cursor c = cdb.fetchAllCategories() ;
		clist = new ArrayList<CategoryObject>();
		if(c != null)
		{
			if(c.getCount() > 0 )
			{
				//Log.v(TAG,"In fillArrayList, starting buiding ArrayList");
				c.moveToFirst() ;
				do{
					int id = c.getInt(0) ;
					String name = c.getString(1) ;
					CategoryObject co = new CategoryObject(id, name) ;
					clist.add(co) ;
					
				}while(c.moveToNext()) ;
				
			}
			
			
		}
		cdb.close() ;
		c.close();
		
	}
	
	

}


/*
 * Inner class 
 * It is needed to build objects of ArrayList's items so that information related to 
 * category can be kept in integrated form
 * 
 * */


class CategoryObject 
{
		int cId ;
		String cName ;
		
		/*
		 * Constructor to initialize object with Category Id and Name
		 * 
		 * */
		
		public CategoryObject(int id, String name) {
			cId = id ;
			cName = name ;
		}
		
		/*
		 * Method to fetch Id of category object  
		 * 
		 * */
		
		int getId()
		{
			return cId ;
		}
		
		/*
		 * Method to fetch Name of category object  
		 * 
		 * */
		
		String getName()
		{
			return cName ;
		}
}



/*
 *
 *  Inner Class
 *  It is used to hold view elements of row of ListView so that rows can be filled  
 *  in fast manner
 * 
 * */


class CategoryHolder
{
	TextView cId ;
	TextView cName ;
	TextView cPos ;
	Button expend ;
	Button delete ;
}


/*
 * CategoryArrayAdapter is developed for ListView of Categories
 * 
 * */


class CategoryArrayAdapter extends ArrayAdapter<CategoryObject> 
{
	String TAG = "CategoryArrayAdapter" ;
	Context ctx ; 
	int rowLayoutId ;  //xml layout Id for row of ListView's row
	CategoryObject cob ;  
	CategoryHolder cHolder ;
	ArrayList<CategoryObject> list ;
	public CategoryArrayAdapter(Context context, int textViewResourceId,
			ArrayList<CategoryObject> objects) {
		super(context, textViewResourceId, objects);
		
		ctx = context ;
		rowLayoutId = textViewResourceId ;
		list = objects ;
	}
	
	/*
	 * Method to obtain customized rows of ListView
	 * 
	 * */
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//Log.v(TAG,"In getView. Line "+1 );
		View row = convertView ;
				
		if(row == null)
		{
			//Log.v(TAG,"In getView. Line "+2 );
			LayoutInflater li =  ((Activity)ctx).getLayoutInflater() ;
			
			//Log.v(TAG,"In getView. Line "+2+"a" );
			row = li.inflate(rowLayoutId, parent,false);
			//Log.v(TAG,"In getView. Line "+2+"b" );
			cHolder = new CategoryHolder() ;
			
			//Log.v(TAG,"In getView. Line "+3 );
			
			cHolder.cName = (TextView)row.findViewById(R.id.name_category) ;
			cHolder.expend = (Button)row.findViewById(R.id.expend) ;
			cHolder.cId = (TextView)row.findViewById(R.id.id_category) ;
			cHolder.delete = (Button)row.findViewById(R.id.delete) ;
			cHolder.cPos = (TextView)row.findViewById(R.id.pos_list) ;
			//Log.v(TAG,"In getView. Line "+4 );
			row.setTag(cHolder);
			//Log.v(TAG,"In getView. Line "+5 );
		}
		else
		{
			//Log.v(TAG,"In getView. Line "+6 );
			cHolder = (CategoryHolder)row.getTag();
			//Log.v(TAG,"In getView. Line "+7 );
		}
				
		//Log.v(TAG,"In getView. Line position"+ position );
		cob = (CategoryObject)list.get(position);
		
		//Log.v(TAG,"In getView. Line "+8 + cob.getName());
		
		
		cHolder.cPos.setText(new StringBuilder().append(position)) ;
		cHolder.cName.setText(cob.getName());
		cHolder.cId.setText(new StringBuilder().append(cob.getId()));
		
		// OnClick implementation for each row's associated delete button. On clicking this
		// button, a dialog appeard to get confirmation whether category is to be deleted
		// or not.
		
		
		cHolder.delete.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {
				
				RelativeLayout r = (RelativeLayout)v.getParent() ;
				final TextView cId = (TextView)r.getChildAt(0) ;
				final TextView cName = (TextView)r.getChildAt(1) ;
				final TextView cPos = (TextView)r.getChildAt(4) ;
				
				//Building Dialog to ask confirmation
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						deleteCategory( Integer.parseInt(cId.getText().toString()), cName.getText().toString() ) ;
						
						}

							private void deleteCategory(int cId, String cName) {
						
								CategoryDbAdapter cdb = new CategoryDbAdapter(ctx);
								cdb.open() ;
								boolean result = cdb.deleteCategory(cId,cName) ;
								if(result)
								{
									int pos = Integer.parseInt(cPos.getText().toString());
									//Log.v(TAG, "In get View, removing category item from postion "+pos );
									list.remove(pos) ;
									notifyDataSetChanged();
									Toast.makeText(ctx, "Category Deleted", Toast.LENGTH_SHORT).show() ;
								}
								
							}
					} ; 
				
				
				AlertDialog.Builder ab = new AlertDialog.Builder(ctx) ;
				ab.setTitle("TreX") ;
				ab.setMessage(new StringBuilder("Are you sure to delete category ").append(cName.getText().toString())) ;
				ab.setPositiveButton("Yes", positiveListener) ;
				ab.setNegativeButton("No", null) ;
				ab.show() ;					
				
				
			}
			
			
			
		}) ;
		
		
		/*
		 * Adding implementation for expand button associated with every row
		 * 
		 * */
		
		
		cHolder.expend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
							
				 // Retrieving first the parent of the expand button i.e. associated 
				 // ListView row to fetch values of all other related views in the same row 
				 
				RelativeLayout r = (RelativeLayout)v.getParent() ;
				TextView cId = (TextView)r.getChildAt(0) ;
				TextView cName = (TextView)r.getChildAt(1) ;
				Intent i = new Intent() ;
				i.setClass(ctx, CategoryExpenseActivity.class) ;
				i.putExtra(CategoryExpenseActivity.CATEGORY_ID, cId.getText().toString());
				i.putExtra(CategoryExpenseActivity.CATEGORY_NAME, cName.getText().toString());
				((Activity)ctx).startActivity(i) ;  // starts activity to explore expenses for related category
				
			}
		});
			
		
		//Log.v(TAG,"In getView. Line "+9 );
		
		
		
		return row ;
	}
	

}