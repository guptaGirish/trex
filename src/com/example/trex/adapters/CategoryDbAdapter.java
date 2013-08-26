package com.example.trex.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * This class is used to provide interface to database queries related to database table
 * 'categories' 
 * 
 * */

public class CategoryDbAdapter {
	
	static String NAME = "cname" ;
	
	private String TAG = "CategoryDbAdapter";
    private static final String DATABASE_NAME = "trex1";
    private static final String DATABASE_TABLE = "categories";
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    
    /*
     *This class is used to get reference to database so that database can be opened 
     *or closed for performing queries  
     * */
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

    	private String TAG = "DatabaseHelper" ;
    	
    	/*
    	 * Constructor 
    	 * 
    	 * */
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
         //   Log.v(TAG, "In Constructor") ;
            
           
        }

		@Override
		public void onCreate(SQLiteDatabase arg0) {
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			
			
		}

    }

	/*
	 * constructor 
	 * 
	 * */
    public CategoryDbAdapter(Context ctx) {
        this.mCtx = ctx;
        //Log.v(TAG, "In constructor") ;
    }

    /*
     * To obtain reference to database to perform queries  
     * */

    public CategoryDbAdapter open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        //Log.v(TAG, "Database opened") ;
        return this;
    }
    
	/*
	 *To close reference to database after performing query to avoid any data flaw  
	 * */
   
    public void close() {
        this.mDbHelper.close();
        //Log.v(TAG, "Database closed") ;
    }

    /*
     * To insert a new category in table provided its name
     * 
     * */
    
    
    public long insertCategory(String cname )
    {
    	//Log.v(TAG, "In insertCategory") ;
    	ContentValues initialValues = new ContentValues() ;
    	initialValues.put(NAME, cname);
    	
    	
		return this.mDb.insert(DATABASE_TABLE, null, initialValues);
    	
    }

   /*
    * To delete a category from table provided its id and name
    * 
    * */
    
    
    public boolean deleteCategory(long cId, String cName) {

    	//Log.v(TAG, "In deleteCategory") ;
    	
    	boolean result = this.mDb.delete(DATABASE_TABLE, "_id = " + cId, null) > 0 ;
    	
    	if(result)
    	{
    		ExpenseDbAdapter edb = new ExpenseDbAdapter(mCtx) ;
    		edb.open() ;
    		boolean r = edb.deleteExpenseWithCAT(cId);
    		edb.close() ;
    		return r ;
    	}
    	else
    	{
    		return false ;
    	}
    	
        
    }


    /*
     * To fetch all the categories stored in the table
     * */
    
    
    public Cursor fetchAllCategories( ) {

    	//Log.v(TAG, "In fetchAllCategories") ;
        Cursor mCursor = this.mDb.query(DATABASE_TABLE, new String[] { "_id",NAME},
        		null, null, null, null, null);
        return mCursor ;
    }


    
	

}
