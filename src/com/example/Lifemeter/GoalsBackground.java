package com.example.Lifemeter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

public class GoalsBackground extends View {
	
	Paint p;
	static CountDownTimer cdt;
	public static int x=0;
	public static int type=0;
	
	public GoalsBackground(Context context, AttributeSet attrs) {
		super(context,attrs);
		p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setColor(Color.rgb(0, 0, 0));
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
		RectF oval = new RectF(540-430,640-430,540+430,640+430);
		int [] color = RGB();
		p.setColor(Color.rgb(255-x,x,0));
		canvas.drawArc(oval, 135, x, true, p);
		x+=4;
	}
	
	public void restart() {
		int limit=0;
		switch(type) {
		case 0: 
			limit=88; break;
		case 1:
			limit =221; break;
		case 2: limit=149; break;
		default: break;
		}
		if(x<(limit+1)) {
			invalidate();
			cdt.start();
		}
	}
	
	public static void update(int arg) {
		type=arg;
		x=0;
		cdt.start();
	}
	
	public int[] RGB() {
		int[] temp=new int[3];
		x+=10;
		if(x<210){
			temp[0]=x;
			temp[1]=0;
			temp[2]=0;
		}
		else if (x<460) {
			temp[0]=210;
			temp[1]=x;
			temp[2]=0;
		}
		return temp;
	}
}
