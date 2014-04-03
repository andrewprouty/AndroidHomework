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
	private float mAdjust = 40; //TODO 10;
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
		int gap = 200;				// bird space to fly through
		if (mShift >= mScreenWidth) { // draw new
			mShift = 0;
			((MainActivity)getContext()).setScore();
			int max = mScreenHeight-gap*3/2;
			int min = gap/2;
			Random r = new Random();
			mRandom = r.nextInt(max - min + 1) + min;
			Log.i(TAG, "onDraw() redraw max & min; random: "+max+"&"+min+"; "+mRandom);
		}
		else {
			mShift=mShift+mAdjust;
		}
		Log.d(TAG, "onDraw() shift: "+mShift);

		float width = 50;						// always the same

		float x = mScreenWidth-width-mShift;	// upper left corner

		float r1y = 0;							// top
		float r1height = mRandom;				// height of top rectangle

		float r2y = mRandom+gap;
		float r2height = mScreenHeight-r1height-gap;

		paint.setColor(Color.BLUE);
		canvas.drawRect(x, r1y, x+width, r1y+r1height, paint);
		canvas.drawRect(x, r2y, x+width, r2y+r2height, paint);
		Log.i(TAG, "onDraw() rect2 y="+r2y+" height "+r2height);
		//top-left-x, top-left-y, x+width, y+height, paint
	}
}