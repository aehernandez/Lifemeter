package com.example.Lifemeter;

/**
 * Created with IntelliJ IDEA.
 * User: Alain
 * Date: 9/7/13
 * Time: 1:50 AM
 * To change this template use File | Settings | File Templates.
 */
public enum Categories {
    Home("Home", 0),
    Work("Work", 1),
    Eat("Eat", 2),
    Gym("Gym", 3),
    Travel("Travel", 4),
    Shopping("Shopping", 5),
    Study("Study", 6);

    private String stringValue;
    private int intValue;

     Categories(String toString, int value) {
        stringValue = toString;
        intValue = value;
     }
    @Override
    public String toString() {
        return stringValue;
    }


}
