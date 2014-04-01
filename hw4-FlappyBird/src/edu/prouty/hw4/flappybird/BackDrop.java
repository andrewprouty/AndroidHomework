package edu.prouty.hw4.flappybird;

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

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mShift >= mScreenWidth) { // draw new
			mShift = 0;
		}
		else {
			mShift=mShift+mAdjust;
		}
		Log.d(TAG, "onDraw() shif: "+mShift);

		float width = 40;				// always the same

		float x = mScreenWidth-width-mShift;	// upper left corner

		float gap = 250;				// bird space to fly through
		float random = 300;				// height of top rectangle, random from 300-(mScreenHeight-300)

		float r1y = 0;					// top
		float r1height = random;		// by definition

		float r2y = random+gap;					//
		float r2height = mScreenHeight-r1height-gap;

		paint.setColor(Color.BLUE);

		canvas.drawRect(x, r1y, x+width, r1y+r1height, paint);
		canvas.drawRect(x, r2y, x+width, r2y+r2height, paint);
		//left, top, right, bottom, paint
	}
}