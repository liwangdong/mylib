package com.wonder.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class IndicatorView extends View{

	public static final float RADIUS=5;
	private Paint paint;
	private Paint paintStroke;
	private int num=5;
	private float positionOffset;
	private int position;
	//设置指示器个数
	public void setNum(int num)
	{
		this.num = num;
	}
	//传入位移和position
	public void setPositionOffset(int position, float positionOffset)
	{
		this.position = position;
		this.positionOffset = positionOffset;
		//重绘
		invalidate();
	}

	
	
	public IndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	private void initPaint() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		paint.setTextSize(20);
		
		paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintStroke.setColor(Color.WHITE);
		paintStroke.setTextSize(20);
		paintStroke.setStyle(Paint.Style.STROKE);
		paintStroke.setStrokeWidth(1);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//空心圆
		for (int i = 0; i < num; i++) {
			canvas.drawCircle(50 + RADIUS * 3* i, 50, RADIUS, paintStroke);
		}
		//实心圆
		canvas.drawCircle(50 + positionOffset * RADIUS * 3 
				+ position * RADIUS * 3 , 50, RADIUS, paint);
		
	}
}
