package com.example.Lifemeter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class AnalyticsTab extends Fragment implements OnItemSelectedListener{
	
	public static Spinner timePeriod;
	public static Spinner activity;
	public static TextView maxValue;
	public static TextView timeInterval;
	
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  View v = inflater.inflate(R.layout.analytics, container, false);
	  timePeriod = (Spinner)v.findViewById(R.id.time_interval);
	  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.time_interval, android.R.layout.simple_spinner_item);
	  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  timePeriod.setAdapter(adapter);
	  timePeriod.setOnItemSelectedListener(this);
	  activity = (Spinner)v.findViewById(R.id.activity);
	  ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.activity, android.R.layout.simple_spinner_item);
	  adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  activity.setAdapter(adapter2);
	  activity.setOnItemSelectedListener(this);
	  maxValue = (TextView)v.findViewById(R.id.max_value);
	  timeInterval = (TextView)v.findViewById(R.id.week);
	  return v;
  	}
  
  public void onResume() {
	  super.onResume();
	  AnalyticsBackground.x=0;
	  AnalyticsBackground.y=0;
  }
  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)  {
  		if(parent.getCount()>3) {
  			AnalyticsBackground.changeActivity(pos);
  		}else {	
  			AnalyticsBackground.changePeriod(pos);
  		}
  }

  
  public void onNothingSelected(AdapterView<?> parent) {
  }
}
