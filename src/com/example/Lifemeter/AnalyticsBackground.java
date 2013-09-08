package com.example.Lifemeter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class AnalyticsBackground extends View {
	
	Paint background;
	Paint pieChart;
	Paint lines;
	RectF oval;
	static CountDownTimer cdt;
	static int x=0;
	public static int y=0;
	public double [] percentage;
	public static double [] line;
	static int periodType=0;
	static int period = 7;
	static String currentActivity;
	static boolean changedActivity=false;
	private boolean nullView=false;
	
	public AnalyticsBackground(Context context, AttributeSet attrs) {
		super(context,attrs);
		background = new Paint(Paint.ANTI_ALIAS_FLAG);
		background.setStyle(Paint.Style.FILL);
		background.setColor(Color.rgb(198,198,198));
		pieChart = new Paint(Paint.ANTI_ALIAS_FLAG);
		pieChart.setStyle(Paint.Style.FILL);
		lines = new Paint(Paint.ANTI_ALIAS_FLAG);
		lines.setColor(Color.BLACK);
		lines.setStrokeWidth(5);
		oval = new RectF(90,120,610,640);
		cdt = new CountDownTimer(10, 1) {
			@Override
			public void onFinish() {
				restart();
			}
			@Override
			public void onTick(long millisUntilFinished) {
			}
		}.start();
	}

	public void onDraw(Canvas canvas) {
		
		canvas.drawRect(50, 50, 1030, 700, background);
		int startAngle=0;
		if(x==0)
			percentage = Lifemeter.calculatePieChart(Lifemeter.whatToday()-period, Lifemeter.whatToday());
		pieChart.setColor(Color.rgb(244,164,96));
		canvas.drawRect(new RectF(730,560,770,600),pieChart);
		canvas.drawArc(oval, startAngle, (int)((percentage[6]/100)*360), true, pieChart);
		startAngle+=(int)(percentage[6]*360/100);
		pieChart.setColor(Color.rgb(142,56,142));
		canvas.drawRect(new RectF(730,490,770,530),pieChart);
		canvas.drawArc(oval, startAngle, (int)((percentage[5]/100)*360), true, pieChart);
		startAngle+=(int)(percentage[5]*360/100);
		pieChart.setColor(Color.rgb(0,205,102));
		canvas.drawRect(new RectF(730,420,770,460),pieChart);
		canvas.drawArc(oval, startAngle, (int)((percentage[4]/100)*360), true, pieChart);
		startAngle+=(int)(percentage[4]*360/100);
		pieChart.setColor(Color.rgb(239,239,33));
		canvas.drawRect(new RectF(730,350,770,390),pieChart);
		canvas.drawArc(oval, startAngle, (int)((percentage[3]/100)*360), true, pieChart);
		startAngle+=(int)(percentage[3]*360/100);
		pieChart.setColor(Color.rgb(141,238,238));
		canvas.drawRect(new RectF(730,280,770,320),pieChart);
		canvas.drawArc(oval, startAngle, (int)((percentage[2]/100)*360), true, pieChart);
		startAngle+=(int)(percentage[2]*360/100);
		pieChart.setColor(Color.rgb(185,23,50));
		canvas.drawRect(new RectF(730,210,770,250),pieChart);
		canvas.drawArc(oval, startAngle, (int)((percentage[1]/100)*360), true, pieChart);
		startAngle+=(int)(percentage[1]*360/100);
		pieChart.setColor(Color.rgb(0,0,153));
		canvas.drawRect(new RectF(730,140,770,180),pieChart);
		canvas.drawArc(oval, startAngle, 360-startAngle, true, pieChart);
		if(!changedActivity) {
			canvas.drawArc(new RectF(80,110, 620,650), x, 360-x, true, background);
			if(period==365)
				x+=6;
			else
				x+=4;
		}
		canvas.drawRect(new RectF(0,1580,1080,1920),background);
		
		canvas.drawRect(new RectF(50,750,1030,1530), background);
		int o=-50;
		float [] points = {150,933+o,150,1448+o,150,1448+o,980,1448+o};
		canvas.drawLines(points,lines);
		
		if(y==0)
			line=Lifemeter.calculateLineGraph(currentActivity, periodType);
		float [] graphPoints = new float[line.length*4];
		
		graphPoints[1]=1448-(int)((line[0]/findMax())*515);
		int distance=0;
		nullView=(AnalyticsTab.timeInterval==null);
		switch (periodType) {
			case 0: distance=127;
					graphPoints[0]=182;
					if(!nullView) AnalyticsTab.timeInterval.setText(getResources().getString(R.string.week));
					break;
			case 1: distance=25;
					graphPoints[0]=155;
					if(!nullView) AnalyticsTab.timeInterval.setText(getResources().getString(R.string.month));
					break;
			case 2: distance =70;
					graphPoints[0]=182;
					if(!nullView) AnalyticsTab.timeInterval.setText(getResources().getString(R.string.year));
					break;
			default: distance=5; break;
		}
		float [] graphPoints2 = new float[graphPoints.length-4];
			for(int j=2;j<graphPoints.length;j+=4) {
				graphPoints[j]=((j+2)/4)*distance+graphPoints[0];
				if((j+2)<graphPoints.length)
					graphPoints[j+2]=graphPoints[j];
			}
			for(int k=3;(k+1)<graphPoints.length;k+=4) {
				graphPoints[k]=1448-50-(int)((line[(k+1)/4]/findMax())*515);
				if((k+2)<graphPoints.length)
					graphPoints[k+2]=graphPoints[k];
			}
			
			for(int n=0;n<graphPoints2.length;n++)
				graphPoints2[n]=graphPoints[n];
		for(int m=0;m<graphPoints.length;m++){
			Log.d("",""+graphPoints[m]);
		}
		canvas.drawLines(graphPoints2,lines);
		//changedActivity=false;
		if(AnalyticsTab.maxValue!=null)
			AnalyticsTab.maxValue.setText(update());
		
		canvas.drawRect(new RectF(153+y, 933-50-5,985,1448-50-3), background);
		if(periodType==2)
			y+=14;
		else
			y+=9;
	}
	
	private void restart() {
		if(x<361) {
			invalidate();
			cdt.start();
		}
		if(changedActivity){
			if(y<986) {
				invalidate();
				cdt.start();
			}else {
				changedActivity=false;
			}
		}
	}
	
	public static void changePeriod(int type) {
		changedActivity=false;
		periodType=type;
		if(type==0)
			period=7;
		if(type==1)
			period=30;
		if(type==2)
			period=365;
		x=0;
		y=0;
		cdt.start();
	}
	
	public static void changeActivity(int type) {
		switch (type) {
			case 0: currentActivity="Home";break;
			case 1: currentActivity="Work";break;
			case 2: currentActivity="Gym";break;
			case 3: currentActivity="Shopping";break;
			case 4: currentActivity="Eating";break;
			case 5: currentActivity="Transit";break;
			case 6: currentActivity="Studying";break;
			default: currentActivity="Home"; break;
		}
		changedActivity=true;
		y=0;
		cdt.start();
	}
	
	public static double findMax() {
		double max = 0.0;
		for (int x=0; x<line.length; x++)
		{
			if (max<line[x]) {
				max = line[x];
			}
		}
		return max;
	}
	
	public static String update() {
		return "" + (int)findMax();
	}
}
