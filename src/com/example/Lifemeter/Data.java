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
<<<<<<< HEAD
		categories[6] = "Gym"; 
=======
        categories[6] = "Gym";
>>>>>>> a0936e5272a2a434f90e5ec2cd2495f9d22ebc27
		
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

<<<<<<< HEAD
		String[] categories = new String[10];
		categories[0] = "Home";
    	categories[1] = "Work";
    	categories[2] = "Home";
    	categories[3] = "Shopping";
    	categories[4] = "Eating";
    	categories[5] = "Transit";
    	categories[6] = "Gym";
    	categories[7] = "Studying";
    	categories[8] = "Work";
    	categories [9] = "Home";
=======
>>>>>>> a0936e5272a2a434f90e5ec2cd2495f9d22ebc27
		return categories;
	}

	

	public static double[][] DataRandomizer(){
		
		double[][] timespent = new double[10][400]; 

<<<<<<< HEAD
		double[] temp = new double[10];
		temp[0] = Math.random();
		temp[1] = Math.random();
		temp[2] = Math.random();
		temp[3] = Math.random();
		temp[4] = Math.random();
		temp[5] = Math.random();
		temp[6] = Math.random();
		temp[7] = Math.random();
		for (int i=0; i< 365; i++){
=======
		
		
		for (int i=0; i< 10; i++){
			timespent[0][i] = Math.random()*400;
>>>>>>> a0936e5272a2a434f90e5ec2cd2495f9d22ebc27
			
			timespent[0][i] = Math.random()*20; 
			timespent[1][i] = Math.random()*30;
			timespent[2][i] = Math.random()*15;
			timespent[3][i] = Math.random()*30;
			timespent[4][i] = Math.random()*40;
			timespent[5][i] = Math.random()*50;
			timespent[6][i] = Math.random()*25;
			timespent[7][i] = Math.random()*35;
			
		
		 
		
	}
		return timespent;
}
}
