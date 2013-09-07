package com.example.Lifemeter;

import android.content.Context; 
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.*;

public class DbClass extends SQLiteOpenHelper {
	
	public SQLiteDatabase db;
	private static final String DatabaseName = "LifeMeterActivity.db";
	private static final int DatabaseVersion = 1;
	
	public static final String TableName = "BasicLifeMeterInfo";

		
	public DbClass(Context context){
		 super(context, DatabaseName, null, DatabaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + TableName + "(Name TEXT, latt REAL, longt REAL, rad REAL, id TEXT);");
	}
	

	public void Insert(String Name, double latittude, double longitude, double rad, String id){

		db.execSQL("INSERT INTO TableName ("+Name+","+latittude+","+longitude+","+rad+","+id+")" +
			       "VALUES (Name,latt,longt,rad, id);");
	}
	
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TableName);
	    onCreate(db);
	}

}
