package com.example.Lifemeter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class AnalyticsBackground extends View {
	
	Paint background;
	Paint pieChart;
	RectF oval;
	static CountDownTimer cdt;
	static int x=0;
	public double [] percentage;
	static int period = 7;
	
	public AnalyticsBackground(Context context, AttributeSet attrs) {
		super(context,attrs);
		background = new Paint(Paint.ANTI_ALIAS_FLAG);
		background.setStyle(Paint.Style.FILL);
		background.setColor(Color.rgb(198,198,198));
		pieChart = new Paint(Paint.ANTI_ALIAS_FLAG);
		pieChart.setStyle(Paint.Style.FILL);
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
		canvas.drawRect(50, 50, 1020, 700, background);
		int startAngle=0;
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
		
		canvas.drawArc(new RectF(80,110, 620,650), x, 360-x, true, background);
		if(period==365)
			x+=6;
		else
			x+=4;
		canvas.drawRect(new RectF(0,1580,1080,1920),background);
	}
	
	private void restart() {
		if(x<361) {
			invalidate();
			cdt.start();
		}
	}
	
	public static void changePeriod(int type) {
		if(type==0)
			period=7;
		if(type==1)
			period=30;
		if(type==2)
			period=365;
		x=0;
		cdt.start();
	}
}
