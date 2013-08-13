package com.example.trex.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ExpenseDbAdapter {
	
	public static String ETAG = "tag" ;
	public static String AMOUNT = "amount" ;
	public static String CATEGORY_ID = "cat_id" ;
	public static String TIME_STAMP = "timestamp" ;
	public static String FLAG = "settle_flag" ;  // 0 for unsettled(default), 1 for settled
	

    private String TAG = "ExpenseDbAdapter";
    private static final String DATABASE_NAME = "trex1";
    private static final String DATABASE_TABLE = "expenses";
    private static final int DATABASE_VERSION = 1;


   

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

    	private String TAG = "DatabaseHelper";
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.v(TAG, "In constructor") ;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

   
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       //     Log.w(TAG, "Upgrading database from version " + oldVersion + " to " //$NON-NLS-1$//$NON-NLS-2$
         //           + newVersion + ", which will destroy all old data"); //$NON-NLS-1$
            //db.execSQL("DROP TABLE IF EXISTS usersinfo"); //$NON-NLS-1$
     //       onCreate(db);
        }
    }


    public ExpenseDbAdapter(Context ctx) {
        this.mCtx = ctx;
        Log.v(TAG, "In constructor") ;
    }


    public ExpenseDbAdapter open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        Log.v(TAG, "Database opened") ;
        return this;
    }

   
    public void close() {
        this.mDbHelper.close();
        Log.v(TAG, "Database closed") ;
    }


    public long insertExpense(String expenseTag, float expenseAmount,int catId,long timeStamp )
    {
    	int settle_flag = 0 ;
    	
    	Log.v(TAG, "In insertExpense") ;
    	ContentValues initialValues = new ContentValues() ;
    	initialValues.put(ETAG, expenseTag);
    	initialValues.put(AMOUNT, expenseAmount) ;
    	initialValues.put(CATEGORY_ID, catId);
    	initialValues.put(TIME_STAMP, timeStamp) ;
    	initialValues.put(FLAG,settle_flag);
    	
		return this.mDb.insert(DATABASE_TABLE, null, initialValues);
    	
    }


    public boolean deleteExpense(long ExpenseId) {
    	Log.v(TAG, "In deleteExpense") ;

        return this.mDb.delete(DATABASE_TABLE, "_id = " + ExpenseId, null) > 0; //$NON-NLS-1$
    }


    public boolean deleteExpenseWithCAT( long catId)
    {
    	Log.v(TAG, "In deleteExpense") ;
    	return this.mDb.delete(DATABASE_TABLE, CATEGORY_ID + "="+catId , null) > 0 ;
    	
    }
    
    
    public Cursor fetchAllExpenses(int cat_id) {

    	Log.v(TAG, "In fetchAllExpenses") ;
        Cursor mCursor = this.mDb.query(DATABASE_TABLE, new String[] { "_id",
                ETAG,AMOUNT,CATEGORY_ID,TIME_STAMP,FLAG}, CATEGORY_ID + "="+ cat_id, null, null, null, null);
        return mCursor ;
    }
    

    
    
    public boolean updateExpense(int ExpenseId, ContentValues updatedValues)
    {
    	Log.v(TAG, "In updateExpense") ;
    	return this.mDb.update(DATABASE_TABLE, updatedValues,"_id ="+ ExpenseId , null) > 0 ;
    	
    }
    
    
    
    
    /*

    public boolean updateUser(long rowId, String name) {
        ContentValues args = new ContentValues();
        args.put(NAME, name);
        return this.mDb
                .update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }
    
    */
}
	


