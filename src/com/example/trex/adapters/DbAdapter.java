package com.example.trex.adapters;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
	
	
	public static final String DATABASE_NAME = "trex1";

    public static final int DATABASE_VERSION = 1;

    
    /*
     * Query to create DB table 'expenses'
     * */
    private static final String CREATE_TABLE_EXPENSES = "create table expenses"
    +"(_id INTEGER primary key autoincrement NOT NULL, " 
    + ExpenseDbAdapter.ETAG+ " TEXT NOT NULL," 
    + ExpenseDbAdapter.AMOUNT+ " REAL NOT NULL," 
    + ExpenseDbAdapter.CATEGORY_ID + " INTEGER NOT NULL, "
    + ExpenseDbAdapter.TIME_STAMP+ " INTEGER NOT NULL, " 
    + ExpenseDbAdapter.FLAG + " INTEGER NOT NULL " + ");"; 


    /*
     * Query to create DB table 'unreviewed_expenses'
     * */
    private static final String CREATE_TABLE_UNREVIEWED_EXPENSES = "create table unreviewed_expenses"
    +"(_id integer primary key autoincrement NOT NULL, " 
    +UnreviewedExpenseDbAdapter.ETAG + " TEXT NOT NULL," 
    +UnreviewedExpenseDbAdapter.TIME_STAMP + " INTEGER NOT NULL"+ ");" ; 


    /*
     * Query to create DB table 'unreviewed_expenses'
     * */
    private static final String CREATE_TABLE_CATEGORIES = "create table categories"
    +" (_id integer primary key autoincrement NOT NULL, " 
    +CategoryDbAdapter.NAME + " TEXT NOT NULL" + ");" ;
    

    private final Context context; 
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    private String TAG = "DbAdapter" ;
    /*
     * Constructor
     * 
     **/
    public DbAdapter(Context ctx)
    {
    	
        this.context = ctx;
        this.DBHelper = new DatabaseHelper(this.context);
        //Log.v(TAG, "In DbAdapter Constructor") ;
    }

    
    /*
     *This class is used to get reference to database so that database can be opened 
     *or closed for performing queries  
     *
     *Here, this class is being used primarily for Database creation
     * 
     * */
    
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
    	private String TAG = "DatabaseHelper" ; 
        
    	/*
    	 *Constructor 
    	 * */
    	
    	DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
          //  Log.v(TAG, "In Constructor") ;
        }

        /*
         * Execution of queries to create DB tables
         * 
         * */
        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(CREATE_TABLE_CATEGORIES);
            //Log.v(TAG, "Categories table created") ;
            db.execSQL(CREATE_TABLE_EXPENSES);
            //Log.v(TAG, "expense table created") ;
            db.execSQL(CREATE_TABLE_UNREVIEWED_EXPENSES);
            //Log.v(TAG, "unreviewed table created") ;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {               
        	

        }
    } 

    /*
     * open the db
     **/
    public DbAdapter open() throws SQLException 
    {
        this.db = this.DBHelper.getWritableDatabase();
        //Log.v(TAG, "Database opened") ;
        return this;
    }

    /*
     * close the db
     **/
    public void close() 
    {
        this.DBHelper.close();
        //Log.v(TAG, "Database closed") ;
    }
	
	

}
