package com.example.trex.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class UnreviewedExpenseDbAdapter {
	
	static String ETAG = "tag" ;
	static String TIME_STAMP = "timestamp" ;
	

    private static final String TAG = "UnreviewedExpenseDbAdapter";
    private static final String DATABASE_NAME = "trex";
    private static final String DATABASE_TABLE = "unreviewed_expenses";
    private static final int DATABASE_VERSION = 1;


   

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
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


    public UnreviewedExpenseDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }


    public UnreviewedExpenseDbAdapter open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        return this;
    }

   
    public void close() {
        this.mDbHelper.close();
    }


    public long insertExpenseTag(String expenseTag, long timeStamp )
    {
    	ContentValues initialValues = new ContentValues() ;
    	initialValues.put(ETAG, expenseTag);
    	initialValues.put(TIME_STAMP, timeStamp) ;
    	
		return this.mDb.insert(DATABASE_TABLE, null, initialValues);
    	
    }


    public boolean deleteExpenseTag(long TagId) {

        return this.mDb.delete(DATABASE_TABLE, "_id = " + TagId, null) > 0; //$NON-NLS-1$
    }


    public Cursor fetchAllExpensesTags( ) {

        Cursor mCursor = this.mDb.query(DATABASE_TABLE, new String[] { "_id",
                ETAG,TIME_STAMP}, null, null, null, null, null);
        return mCursor ;
    }


	

}
