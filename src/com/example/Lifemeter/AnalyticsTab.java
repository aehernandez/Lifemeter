package com.example.Lifemeter;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AnalyticsTab extends Fragment implements OnItemSelectedListener{
	
	public static Spinner timePeriod;
	
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  View v = inflater.inflate(R.layout.analytics, container, false);
	  timePeriod = (Spinner)v.findViewById(R.id.time_interval);
	  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.time_interval, android.R.layout.simple_spinner_item);
	  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  timePeriod.setAdapter(adapter);
	  timePeriod.setOnItemSelectedListener(this);
	  return v;
  	}
  
  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	  	AnalyticsBackground.changePeriod(pos);
  }

  public void onNothingSelected(AdapterView<?> parent) {
  }
}
