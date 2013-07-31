package com.example.trex.adapters;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
	
	
	public static final String DATABASE_NAME = "trex";

    public static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_EXPENSES = "create table expenses"
    +"(_id INTEGER primary key autoincrement NOT NULL, " 
    + ExpenseDbAdapter.ETAG+ " TEXT NOT NULL," 
    + ExpenseDbAdapter.AMOUNT+ " REAL NOT NULL," 
    + ExpenseDbAdapter.CATEGORY_ID + " INTEGER NOT NULL, "
    + ExpenseDbAdapter.TIME_STAMP+ " INTEGER NOT NULL" + ");"; 

    private static final String CREATE_TABLE_UNREVIEWED_EXPENSES = "create table unreviewed_expenses"
    +"(_id integer primary key autoincrement NOT NULL, " 
    +UnreviewedExpenseDbAdapter.ETAG + " TEXT NOT NULL," 
    +UnreviewedExpenseDbAdapter.TIME_STAMP + " INTEGER NOT NULL"+ ");" ; 


    private static final String CREATE_TABLE_CATEGORIES = "create table categories"
    +" (_id integer primary key autoincrement NOT NULL, " //$NON-NLS-1$
    +CategoryDbAdapter.NAME + " TEXT NOT NULL" + ");" ;//$NON-NLS-1$
    

    private final Context context; 
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    private String TAG = "DbAdapter" ;
    /**
     * Constructor
     * @param ctx
     */
    public DbAdapter(Context ctx)
    {
    	
        this.context = ctx;
        this.DBHelper = new DatabaseHelper(this.context);
        Log.v(TAG, "In DbAdapter Constructor") ;
    }

    
    
    
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
    	private String TAG = "DatabaseHelper" ; 
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.v(TAG, "In Constructor") ;
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(CREATE_TABLE_CATEGORIES);
            Log.v(TAG, "Categories table created") ;
            db.execSQL(CREATE_TABLE_EXPENSES);
            Log.v(TAG, "expense table created") ;
            db.execSQL(CREATE_TABLE_UNREVIEWED_EXPENSES);
            Log.v(TAG, "unreviewed table created") ;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {               
            // Adding any table mods to this guy here
        }
    } 

   /**
     * open the db
     * @return this
     * @throws SQLException
     * return type: DBAdapter
     */
    public DbAdapter open() throws SQLException 
    {
        this.db = this.DBHelper.getWritableDatabase();
        Log.v(TAG, "Database opened") ;
        return this;
    }

    /**
     * close the db 
     * return type: void
     */
    public void close() 
    {
        this.DBHelper.close();
        Log.v(TAG, "Database closed") ;
    }
	
	

}
