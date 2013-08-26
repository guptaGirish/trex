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
 * 'expenses' 
 * 
 * */


public class ExpenseDbAdapter {
	
	public static String ETAG = "tag" ;
	public static String AMOUNT = "amount" ;
	public static String CATEGORY_ID = "cat_id" ;
	public static String TIME_STAMP = "timestamp" ;
	public static String FLAG = "settle_flag" ;  // 0 for unsettled expense(default), 1 for settled expense
	

    private String TAG = "ExpenseDbAdapter";
    private static final String DATABASE_NAME = "trex1";
    private static final String DATABASE_TABLE = "expenses";
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
    
    public ExpenseDbAdapter(Context ctx) {
        this.mCtx = ctx;
        //Log.v(TAG, "In constructor") ;
    }


    /*
     * To obtain reference to database to perform queries  
     * */
    
    public ExpenseDbAdapter open() throws SQLException {
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
     * To insert a new expense in DB table 'expenses' 
     * */
    
    public long insertExpense(String expenseTag, float expenseAmount,int catId,long timeStamp )
    {
    	int settle_flag = 0 ;
    	
    	//Log.v(TAG, "In insertExpense") ;
    	ContentValues initialValues = new ContentValues() ;
    	initialValues.put(ETAG, expenseTag);
    	initialValues.put(AMOUNT, expenseAmount) ;
    	initialValues.put(CATEGORY_ID, catId);
    	initialValues.put(TIME_STAMP, timeStamp) ;
    	initialValues.put(FLAG,settle_flag);
    	
		return this.mDb.insert(DATABASE_TABLE, null, initialValues);
    	
    }


    /*
     * To delete a expense from Db table 'expenses'
     * */
    
    public boolean deleteExpense(long ExpenseId) {
    	//Log.v(TAG, "In deleteExpense") ;

        return this.mDb.delete(DATABASE_TABLE, "_id = " + ExpenseId, null) > 0; //$NON-NLS-1$
    }


    /*
     * To delete all expenses of a category
     * */
    
    public boolean deleteExpenseWithCAT( long catId)
    {
    	//Log.v(TAG, "In deleteExpenseWithCAT") ;
    	return this.mDb.delete(DATABASE_TABLE, CATEGORY_ID + "="+catId , null) > 0 ;
    	
    }
    
    
    /*
     * To fetch all expenses of a category
     * */
    
    public Cursor fetchAllExpenses(int cat_id) {

    	//Log.v(TAG, "In fetchAllExpenses") ;
        Cursor mCursor = this.mDb.query(DATABASE_TABLE, new String[] { "_id",
                ETAG,AMOUNT,CATEGORY_ID,TIME_STAMP,FLAG}, CATEGORY_ID + "="+ cat_id, null, null, null, null);
        return mCursor ;
    }
    

    /*
     * To update a particular expense
     * */
    
    public boolean updateExpense(int ExpenseId, ContentValues updatedValues)
    {
    	//Log.v(TAG, "In updateExpense") ;
    	return this.mDb.update(DATABASE_TABLE, updatedValues,"_id ="+ ExpenseId , null) > 0 ;
    	
    }
    
    
}
	


