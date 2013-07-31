package com.example.trex;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.w3c.dom.Text;

import com.example.trex.adapters.CategoryDbAdapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CategoryListingActivity extends Activity{

	private String TAG = "CategoryListingActivity" ;
	ListView listCategory ;
	ArrayList<CategoryObject> clist ;
	CategoryArrayAdapter cad ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_categories_listing);
		Log.v(TAG,"");
		listCategory = (ListView)findViewById(R.id.list_category);
		populateList();
		
	}
	
	private void populateList() {
		// TODO Auto-generated method stub
		CategoryDbAdapter cdb = new CategoryDbAdapter(this);
		cdb.open();
		Cursor c = cdb.fetchAllCategories() ;
		clist = new ArrayList<CategoryObject>();
		if(c != null)
		{
			if(c.getCount() > 0 )
			{
				Log.v(TAG,"In populateList, starting buiding ArrayList");
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
		Log.v(TAG,"In populateList, After building ArrayList");
		cad = new CategoryArrayAdapter(CategoryListingActivity.this, R.layout.layout_category_row, clist);
		listCategory.setAdapter(cad);
		
		
	}
	
	

}

class CategoryObject 
{
		
		int cId ;
		String cName ;
		
		public CategoryObject(int id, String name) {
			// TODO Auto-generated constructor stub
			cId = id ;
			cName = name ;
		}
		
		int getId()
		{
			return cId ;
		}
		String getName()
		{
			return cName ;
		}
}


class CategoryHolder
{
	TextView cId ;
	TextView cName ;
	Button expend ;
}


class CategoryArrayAdapter extends ArrayAdapter<CategoryObject> 
{
	String TAG = "CategoryArrayAdapter" ;
	Context ctx ;
	int rowLayoutId ;
	CategoryObject cob ;
	CategoryHolder cHolder ;
	ArrayList<CategoryObject> list ;
	public CategoryArrayAdapter(Context context, int textViewResourceId,
			ArrayList<CategoryObject> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		ctx = context ;
		rowLayoutId = textViewResourceId ;
		list = objects ;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.v(TAG,"In getView. Line "+1 );
		View row = convertView ;
		
		
		if(row == null)
		{
			Log.v(TAG,"In getView. Line "+2 );
			LayoutInflater li =  ((Activity)ctx).getLayoutInflater() ;
			
			Log.v(TAG,"In getView. Line "+2+"a" );
			row = li.inflate(rowLayoutId, parent,false);
			Log.v(TAG,"In getView. Line "+2+"b" );
			cHolder = new CategoryHolder() ;
			
			Log.v(TAG,"In getView. Line "+3 );
			
			cHolder.cName = (TextView)row.findViewById(R.id.name_category) ;
			cHolder.expend = (Button)row.findViewById(R.id.expend) ;
			cHolder.cId = (TextView)row.findViewById(R.id.id_category);
			Log.v(TAG,"In getView. Line "+4 );
			row.setTag(cHolder);
			Log.v(TAG,"In getView. Line "+5 );
		}
		else
		{
			Log.v(TAG,"In getView. Line "+6 );
			cHolder = (CategoryHolder)row.getTag();
			Log.v(TAG,"In getView. Line "+7 );
		}
				
		Log.v(TAG,"In getView. Line position"+ position );
		cob = (CategoryObject)list.get(position);
		
		Log.v(TAG,"In getView. Line "+8 + cob.getName());
		
		
		cHolder.cName.setText(cob.getName());
		cHolder.cId.setText(""+cob.getId());
		cHolder.expend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				RelativeLayout r = (RelativeLayout)v.getParent() ;
				TextView cId = (TextView)r.getChildAt(0) ;
				TextView cName = (TextView)r.getChildAt(1) ;
				Intent i = new Intent() ;
				i.setClass(ctx, CategoryExpenseActivity.class) ;
				i.putExtra(CategoryExpenseActivity.CATEGORY_ID, cId.getText().toString());
				i.putExtra(CategoryExpenseActivity.CATEGORY_NAME, cName.getText().toString());
				((Activity)ctx).startActivity(i) ;
				
			}
		});
			
		
		/*
		eTagHolder.title.setText(lo.getTitle());
		eTagHolder.description.setText(lo.getDescription());
		dHolder.nid.setText(lo.getnid());
		dHolder.imageURL.setText(lo.getImageURL());
		*/
		Log.v(TAG,"In getView. Line "+9 );
		
		
		
		return row ;
	}
	

}