package com.example.Lifemeter;

import android.content.Context; 
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.*;

public class DbList extends SQLiteOpenHelper {

	
	private static final String DatabaseName = "Activities.db";
	private static final int DatabaseVersion = 1;
	
	public static final String TableName = "Activities";

	
	public DbList(Context context){
		 super(context, DatabaseName, null, DatabaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + TableName + "(id INTEGER, activity TEXT);");
	}
	

	public void Insert(SQLiteDatabase db, String activity){

		db.execSQL("INSERT INTO TableName (id integer primary key autoincrement,"+activity+")" +
			       "VALUES (activity);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TableName);
	    onCreate(db);
	}

}
