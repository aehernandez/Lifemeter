package com.example.Lifemeter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class InternalStorage extends Activity {

	public  void WriteDay(int day) throws IOException{
		String FILENAME = "InternalStorage";
		String daystring = getString(day) + "+"; 
		
		FileOutputStream fos;
		
		fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

		fos.write(daystring.getBytes());

		fos.close();
		
		    
		}
		

	public  void WriteCategory(String input) throws IOException{
		String FILENAME = "Categories";
		String category = input + "+";
				
		FileOutputStream fos;
		
		fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

		fos.write(category.getBytes());

		fos.close();

		     
		}
	

	public  void WriteTimes(double times) throws IOException{
		String FILENAME = "times";
		int inttimes =  (int) (times); 
		String TimeString = getString(inttimes)+"+"; 
		
		FileOutputStream fos;
		

		fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

	    fos.write(TimeString.getBytes());

	    fos.close();

		     

		}
		
	public String[] Separator(String temp){
		int arrayholder = 0; 
		int start = 0; 
		
		String tempreturn[] = new String[100]; 
		for (int i=0; i < temp.length(); i++){
			if (temp.substring(i) == "+"){
				tempreturn[arrayholder] = temp.substring(start, i-1);
				start = i+1; 
				arrayholder++; 
			}
		}
		return tempreturn;
	}
	
	public int[] SeparatorInt(String temp){
		int arrayholder = 0; 
		int start = 0; 
		
		int tempreturn[] = new int[500]; 
		for (int i=0; i < temp.length(); i++){
			if (temp.substring(i) == "+"){
				tempreturn[arrayholder] = Integer.parseInt(temp.substring(start, i-1));
				start = i+1; 
				arrayholder++; 
			}
		}
		return tempreturn;
	}
	public  String[] getActivityList(int day) throws FileNotFoundException{
		String[] DataArray = new String[100];
		String FILENAME = "Categories";
		int temp;
		StringBuffer fileContent = new StringBuffer("");
		FileInputStream fis;
		try {
			fis = openFileInput(FILENAME);
		    
		    while( (temp = fis.read()) != -1)
		            fileContent.append((char)temp);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

		String data = new String(fileContent);
				
		return DataArray = Separator(data);

	}
	

	public int[] getTimesList(int day) throws FileNotFoundException{
		int[] DataArray = new int[100];
		String FILENAME = "times";
		int temp;
		StringBuffer fileContent = new StringBuffer("");
		FileInputStream fis;
		try {
			fis = openFileInput(FILENAME);
		    
		    while( (temp = fis.read()) != -1)
		            fileContent.append((char)temp);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

		String data = new String(fileContent);
				
		return DataArray = SeparatorInt(data);

	}
	
}
