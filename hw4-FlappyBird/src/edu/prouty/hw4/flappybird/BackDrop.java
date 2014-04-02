package edu.prouty.hw4.flappybird;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class BackDrop extends View {
	private static final String TAG = "BackDrop";
	private Paint paint = new Paint();
	private int mScreenWidth;
	private int mScreenHeight;
	private float mShift;
	private float mAdjust = 10;
	private float mRandom = 0;

	public BackDrop(Context context, AttributeSet attr) {
		super(context, attr);
	}
	public void setScreenWidth (int x) {
		this.mScreenWidth = x;
		mShift = x;
	}
	public void setScreenHeight (int y) {
		this.mScreenHeight = y;
	}

	public void reset() {
		mShift = mScreenWidth; //reset bar to RHS
	}
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mShift >= mScreenWidth) { // draw new
			mShift = 0;
			((MainActivity)getContext()).setScore();
			Random r = new Random();
			mRandom = r.nextInt((mScreenHeight-300) - 300 + 1) + 300;
			// f(x) = r.nextInt(max - min + 1) + min;
			// max = (mScreenHeight-x) & min = (x)
			Log.d(TAG, "onDraw() redraw random: "+mRandom);
		}
		else {
			mShift=mShift+mAdjust;
		}
		Log.d(TAG, "onDraw() shift: "+mShift);

		float width = 40;				// always the same

		float x = mScreenWidth-width-mShift;	// upper left corner

		float gap = 225;				// bird space to fly through
		
		float r1y = 0;					// top
		float r1height = mRandom;		// height of top rectangle

		float r2y = mRandom+gap;					//
		float r2height = mScreenHeight-r1height-gap;

		paint.setColor(Color.BLUE);

		canvas.drawRect(x, r1y, x+width, r1y+r1height, paint);
		canvas.drawRect(x, r2y, x+width, r2y+r2height, paint);
		//left, top, right, bottom, paint
	}
}