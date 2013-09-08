package com.example.Lifemeter;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeTab extends Fragment {
	
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  return inflater.inflate(R.layout.home, container, false);
  	}
  
  public void onResume() {
	  super.onResume();
	  try{
		  Lifemeter.updateHome();
	  }catch (Exception e) {
		  Log.d("",""+e);
	  }
  }
}