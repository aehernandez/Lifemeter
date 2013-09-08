package com.example.Lifemeter; 

import java.lang.*; 

public class Data {

    InternalStorage Test = new InternalStorage();
	
	
	public static String[] PlacesTracked(){
		String[] categories = new String[10];
		categories[0] = "Home";
		categories[1] = "Work";
		categories[2] = "Shopping";
		categories[3] = "Eating";
		categories[4] = "Transit";
		categories[5] = "Studying";
        categories[6] = "Gym";
		
		return categories; 
	}
	
	public static String[] Places(){
        String[] categories = new String[10];

        //try {
             //Test.WriteCategory("Home");
             //Test.WriteCategory("Work");
             //Test.WriteCategory("Home");
             //Test.WriteCategory("Shopping");
             //Test.WriteCategory("Eating");
             //Test.WriteCategory("Transit");
             //Test.WriteCategory("Home");
             //Test.WriteCategory("Studying");
             //Test.WriteCategory("Work");
             //Test.WriteCategory("Gym");

             //categories = Test.getActivityList(0);

        //}
        //catch(Exception e){

        //}
        categories[0] = "Home";
        categories[1] = "Work";
        categories[2] = "Home";
        categories[3] = "Shopping";
        categories[4] = "Eating";
        categories[5] = "Transit";
        categories[6] = "Home";
        categories[7] = "Studying";
        categories[8] = "Work";
        categories[9] = "Gym";

		return categories;
	}

	

	public static double[][] DataRandomizer(){
		
		double[][] timespent = new double[10][400]; 

		
		
		for (int i=0; i< 10; i++){
			timespent[0][i] = Math.random()*400;
			
		}
		return timespent; 
		
	}
}
