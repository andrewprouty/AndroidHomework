package edu.prouty.hw4.flappybird;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private AnimationDrawable mBirdAnimation;
	private View view;
	private TextView mScore;
	private TextView mHigh;
	private int score=-1, high=0;
	
	private BackDrop mBackDrop;
	
	boolean mIsDead=true;
	boolean mIsInMotion=false;
	boolean mIsRising=false;
	ImageView bird, howToImage;
	
	float initialHeight = -1;
	int screenWidth;
	int screenHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		view = findViewById(R.id.container);

		mScore = (TextView) findViewById(R.id.score);
		mHigh = (TextView) findViewById(R.id.high);
		mHigh.setText(String.valueOf(high));

		bird = (ImageView) findViewById(R.id.bird_square);
		bird.setBackgroundResource(R.anim.bird_motion);
		mBirdAnimation = (AnimationDrawable) bird.getBackground();

		howToImage = (ImageView) findViewById(R.id.instruction_image);
		howToImage.setBackgroundResource(R.drawable.touch_to_start);

		Resources resources = getResources();
		XmlPullParser parser = resources.getXml(R.layout.activity_main);
		AttributeSet attributes = Xml.asAttributeSet(parser);
		mBackDrop = new BackDrop(this, attributes);
		mBackDrop = (BackDrop)findViewById(R.id.rect_back_drop);

		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
					touchDown();
				} else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
					touchUp();
				}
				return true;
			}
		});
		Display display = getWindowManager().getDefaultDisplay();
		Point screenSize = new Point();
		display.getSize(screenSize);
		screenWidth = screenSize.x;
		screenHeight = screenSize.y;
		Log.i(TAG, "Screen width(x)="+screenWidth+" height(y)="+screenHeight);
		mBackDrop.setScreenWidth(screenWidth);
		mBackDrop.setScreenHeight(screenHeight);
	}
	@Override
	public void onSaveInstanceState(Bundle setState) {
		super.onSaveInstanceState(setState);
		Log.i(TAG, "onSaveInstanceState() high score: "+high);
		setState.putInt("high", high);
	}
	@Override
	public void onRestoreInstanceState(Bundle getState) {
		super.onRestoreInstanceState(getState);
		high = getState.getInt("high");
		Log.i(TAG, "onRestoreInstanceState() high score: "+high);
		mHigh.setText(String.valueOf(high));
	}
	
	public void birdStartFlapping() {
		Log.d(TAG, "birdStartFlapping()");
		mBirdAnimation.start();
	}
	public void birdStopFlapping() {
		Log.d(TAG, "birdStopFlapping()");
		mBirdAnimation.stop();
	}
	private void phoenixBird() {
		Log.d(TAG, "phoenixBird()");
		howToImage.setBackgroundResource(0);
		if (initialHeight < 0) { // first instance
			initialHeight = bird.getY();
			Log.d(TAG, "phoenixBird() set initial height "+initialHeight);
		}
		else { // true rebirth
			score=-1;
			mHigh.setText(String.valueOf(high)); // saved on death; display on continuation
			mBackDrop.reset(); // reset rectangle
			mBackDrop.invalidate();
			bird.setBackgroundResource(R.anim.bird_motion);
			mBirdAnimation = (AnimationDrawable) bird.getBackground();
		}
		bird.setY(initialHeight);
		birdStartFlapping();
	}
	public void touchDown() {
		Log.d(TAG, "touchDown()");
		mIsRising=true;
		if (mIsDead) {
			mIsDead=false;
			phoenixBird();
			move();
		}
	}
	public void touchUp() {
		Log.d(TAG, "touchUp()");
		mIsRising=false;
	}
	protected void setScore() {
		score++;
		Log.d(TAG, "setScore() new score: "+score);
		mScore.setText(String.valueOf(score));
	}
	protected float[] getBirdLocation() {
		float[] f = {bird.getX(), bird.getY(), bird.getHeight(), bird.getWidth()}; 
		return f;
	}
	// BackDrop knows about rectangle & top/bottom - it knows obstacle hazard... collision
	protected void killBird() {
		Log.d(TAG, "killBird() y="+bird.getY() + " Y height="+screenHeight);
		mIsDead=true;
		birdStopFlapping();	// stop flapping - dead
		bird.setBackgroundResource(R.drawable.red_bird);
		howToImage.setBackgroundResource(R.drawable.touch_to_start);
		if (score > high) {
			high = score; // save in case of orientation change NOW
		}

	}
	private void move() {
		if (mIsRising) {
			Log.d(TAG, "birdUpDown() rising y="+bird.getY());
			bird.setY(bird.getY()-15);
		}
		else {
			Log.d(TAG, "birdUpDown() falling y="+bird.getY());
			bird.setY(bird.getY()+15);
		}
		if (!mIsDead) {
			bird.postDelayed(new Mover(), 50);
			mBackDrop.invalidate();
		}
	}
	private class Mover implements Runnable {
		@Override
		public void run() {
			move();
		}
	}}