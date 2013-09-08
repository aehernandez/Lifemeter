package com.example.Lifemeter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.android.gms.internal.j;

public class AnalyticsBackground extends View {
	
	Paint background;
	Paint pieChart;
	RectF oval;
	
	public AnalyticsBackground(Context context, AttributeSet attrs) {
		super(context,attrs);
		background = new Paint();
		background.setStyle(Paint.Style.FILL);
		background.setColor(Color.rgb(198,198,198));
		pieChart = new Paint(Paint.ANTI_ALIAS_FLAG);
		pieChart.setStyle(Paint.Style.FILL);
		pieChart.setColor(Color.rgb(23,42,222));
		oval = new RectF(90,120,610,640);
	}

	public void onDraw(Canvas canvas) {
		canvas.drawRect(50, 50, 1020, 700, background);
		double [] percentage = Lifemeter.calculatePieChart(Lifemeter.whatToday()-7, Lifemeter.whatToday());
		int startAngle=0;
		
		canvas.drawArc(oval, startAngle, (int)((percentage[0]/100)*360), true, pieChart);
		startAngle+=(int)(percentage[0]*360/100);
		pieChart.setColor(Color.rgb(185,23,50));
		canvas.drawArc(oval, startAngle, (int)((percentage[1]/100)*360), true, pieChart);
		startAngle+=(int)(percentage[1]*360/100);
		pieChart.setColor(Color.rgb(255,236,139));
		canvas.drawArc(oval, startAngle, (int)((percentage[2]/100)*360), true, pieChart);
		startAngle+=(int)(percentage[2]*360/100);
		pieChart.setColor(Color.rgb(0,205,102));
		canvas.drawArc(oval, startAngle, (int)((percentage[3]/100)*360), true, pieChart);
		startAngle+=(int)(percentage[3]*360/100);
		pieChart.setColor(Color.rgb(141,238,238));
		canvas.drawArc(oval, startAngle, (int)((percentage[4]/100)*360), true, pieChart);
		startAngle+=(int)(percentage[4]*360/100);
		pieChart.setColor(Color.rgb(142,56,142));
		canvas.drawArc(oval, startAngle, (int)((percentage[5]/100)*360), true, pieChart);
		startAngle+=(int)(percentage[5]*360/100);
		pieChart.setColor(Color.rgb(244,164,96));
		canvas.drawArc(oval, startAngle, 360-startAngle, true, pieChart);
	}
}
