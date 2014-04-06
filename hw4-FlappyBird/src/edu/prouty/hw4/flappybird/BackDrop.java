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
	private final float birdOffset = 125;
	private float birdX;
	private float birdY;
	private float birdHeight;
	private float birdWidth;

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
		int gap = 250;				// bird space to fly through
		if (mShift >= mScreenWidth) { // draw new
			mShift = 0;
			((MainActivity)getContext()).setScore(); //TODO
			int max = mScreenHeight-gap*3/2;
			int min = gap/2;
			Random r = new Random();
			mRandom = r.nextInt(Math.abs(max - min + 1)) + min;
			Log.i(TAG, "onDraw() redraw max & min; random: "+max+"&"+min+"; "+mRandom);
		}
		else {
			mShift=mShift+mAdjust;
		}
		Log.d(TAG, "onDraw() shift: "+mShift);

		float width = 50;						// always the same

		float x = mScreenWidth-width-mShift;	// upper left corner x coordinate

		float r1y = 0;							// top
		float r1height = mRandom;				// height of rectangle #1 (top)

		float r2y = mRandom+gap;
		float r2height = mScreenHeight-r1height-gap;

		paint.setColor(Color.BLUE);
		canvas.drawRect(x, r1y, x+width, r1y+r1height, paint);
		canvas.drawRect(x, r2y, x+width, r2y+r2height, paint);
		Log.d(TAG, "onDraw() rect2 y="+r2y+" height "+r2height);
		//top-left-x, top-left-y, x+width, y+height, paint
		
		//float[] f = {1,2,3,4};
		float[] f = ((MainActivity)getContext()).getBirdLocation(); // TODO
		birdX = f[0];
		birdY = f[1];
		birdHeight = f[2];
		birdWidth = f[3];
		Log.d(TAG, "onDraw() bird x= "+birdX+", y="+birdY+" w="+birdHeight+" h="+birdWidth);
		if (tooHighLow()) {
			((MainActivity)getContext()).killBird(); //TODO
		}
		if ((birdX+birdWidth > x      ) &&
			(birdX           < x+width)) {
			// Danger bird IS IN the rectangle's X dimension space
			if ((birdY            < r1height) ||
				(birdY+birdHeight > r1height+gap)) {
				// and NOT in the gap's space...
				((MainActivity)getContext()).killBird();
			}
		}
	}
	private boolean tooHighLow() {
		if (birdY < 0) { // remember top corner
			Log.i(TAG, "too HIGH Low() height="+birdY);
			return true;
		}
		if (birdY + birdHeight + birdOffset > mScreenHeight) {
			Log.i(TAG, "tooHigh LOW() y:"+birdY+"+birdHeight:" +birdHeight+ "+buffer:"+birdOffset+ " > screenHeight:"+mScreenHeight);
			return true;
		}
		else {
			return false;
		}
	}
}