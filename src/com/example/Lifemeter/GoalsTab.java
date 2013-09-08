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

public class GoalsTab extends Fragment implements OnItemSelectedListener {
	
	Spinner goals;
	TextView goalProgress;
	TextView activity;
	
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  View v= inflater.inflate(R.layout.goals, container, false);
	  goals = (Spinner)v.findViewById(R.id.goals_spinner);
	  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.goals, android.R.layout.simple_spinner_item);
	  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  goals.setAdapter(adapter);
	  goals.setOnItemSelectedListener(this);
	  goalProgress=(TextView)v.findViewById(R.id.goal_progress);
	  activity=(TextView)v.findViewById(R.id.activity_goal);
	  return v;
  	}

@Override
public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	GoalsBackground.update(arg2);
	switch(arg2) {
	case 0:
		activity.setText("Gym");
		goalProgress.setText("13/40 hrs");
		break;
	case 1:
		activity.setText("Study");
		goalProgress.setText("8.2/10 hrs");
		break;
	case 2:
		activity.setText("Work/Life");
		goalProgress.setText("55% Work");
		break;
	default: break;
	}
}

public void onResume() {
	super.onResume();
	GoalsBackground.x=0;
}
@Override
public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
	
}
}