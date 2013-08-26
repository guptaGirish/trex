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
 * 'unreviewed_expenses' 
 * 
 * */

public class UnreviewedExpenseDbAdapter {
	
	static String ETAG = "tag" ;
	static String TIME_STAMP = "timestamp" ;
	

    private String TAG = "UnreviewedExpenseDbAdapter";
    private static final String DATABASE_NAME = "trex1";
    private static final String DATABASE_TABLE = "unreviewed_expenses";
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;

    
    /*
     *This class is used to get reference to database so that database can be opened 
     *or closed for performing queries  
     * */
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

    	private String TAG = "DatabaseHelper";
    	
    	/*
    	 * Constructor
    	 * */
    	
        DatabaseHelper(Context context) {
        	super(context, DATABASE_NAME, null, DATABASE_VERSION);
        	//Log.v(TAG, "In constructor") ;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


    /*
     * Constructor
     * */
    
    public UnreviewedExpenseDbAdapter(Context ctx) {
        this.mCtx = ctx;
        //Log.v(TAG, "In constructor") ;
    }

    
    /*
     * To obtain reference to database to perform queries  
     * */

    public UnreviewedExpenseDbAdapter open() throws SQLException {
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
     * To insert new Expenses Tag in table
     * */
    
    public long insertExpenseTag(String expenseTag, long timeStamp )
    {
    	//Log.v(TAG, "insertExpeseTag") ;
    	ContentValues initialValues = new ContentValues() ;
    	initialValues.put(ETAG, expenseTag);
    	initialValues.put(TIME_STAMP, timeStamp) ;
    	
		return this.mDb.insert(DATABASE_TABLE, null, initialValues);
    	
    }

    
    /*
     * To delete a tagged expense
     * */

    public boolean deleteExpenseTag(long TagId) {

    	//Log.v(TAG, "deleteExpenseTag") ;
        return this.mDb.delete(DATABASE_TABLE, "_id = " + TagId, null) > 0; //$NON-NLS-1$
    }

    /*
     * To fetch all tagged expenses
     * */

    public Cursor fetchAllExpensesTags( ) {

    	//Log.v(TAG, "fetchAllExpenseTags") ;
        Cursor mCursor = this.mDb.query(DATABASE_TABLE, new String[] { "_id",
                ETAG,TIME_STAMP}, null, null, null, null, null);
        return mCursor ;
    }

}
