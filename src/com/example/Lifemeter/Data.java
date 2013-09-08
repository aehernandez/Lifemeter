package com.example.Lifemeter; 

import java.lang.*; 

public class Data {
	
	
	public static String[] PlacesTracked(){
		String[] categories = new String[10];
		categories[0] = "Home";
		categories[1] = "Work";
		categories[2] = "Shopping";
		categories[3] = "Eating";
		categories[4] = "Transit";
		categories[5] = "Studying";
		
		return categories; 
	}
	
	public static String[] Places(){

		String[] categories = new String[10];
		categories[0] = "Home";
    	categories[1] = "Work";
    	categories[2] = "Home";
    	categories[3] = "Shopping";
    	categories[4] = "Eating";
    	categories[5] = "Transit";
    	categories[6] = "Home";
    	categories[7] = "Studying";
    	categories[8] = "Work";

		return categories;
	}
	

	public static double[][] DataRandomizer(){
		
		double[][] timespent = new double[10][400]; 

		
		
		for (int i=0; i< 365; i++){
			timespent[0][i] = Math.random()*400; 
			timespent[1][i] = Math.random()*400;
			timespent[2][i] = Math.random()*400;
			timespent[3][i] = Math.random()*400;
			timespent[4][i] = Math.random()*400;
			timespent[5][i] = Math.random()*400;
			timespent[6][i] = Math.random()*400;
			timespent[7][i] = Math.random()*400;
			timespent[8][i] = Math.random()*400;
			timespent[9][i] = Math.random()*400; 
			
		}
		return timespent; 
		
	}
}
