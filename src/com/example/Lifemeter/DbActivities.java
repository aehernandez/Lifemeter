package com.example.Lifemeter;

import android.content.Context; 
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.*;

public class DbActivities extends SQLiteOpenHelper {

	//old code
	//public static final double longitude = 0;
	//public static final double lattitude = 0;
	//public static final double timestamp = 0;
	
	public static final String DatabaseName = "LifeMeterActivity.db";
	public static final int DatabaseVersion = 1;
	
	public static final String TableName = "LifeMeterActivities";

	public SQLiteDatabase db;
	/*private static final String DbCreate = "create table"
		            + longitude + lattitude  + timestamp
		            + " text not null);";
		            */
	
	public DbActivities(Context context){
		 super(context, DatabaseName, null, DatabaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + TableName + "(id INTEGER,day INTEGER, activity TEXT, time REAL);");
	}
	

	public void Insert(int day, String activity, double time){

		db.execSQL("INSERT INTO TableName (_id integer primary key autoincrement,"+day+","+activity+","+time+")" +
			       "VALUES (id,day,activity,time);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TableName);
	    onCreate(db);
	}

}
