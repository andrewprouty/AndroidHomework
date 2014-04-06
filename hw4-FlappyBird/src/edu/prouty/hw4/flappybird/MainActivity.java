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
	//private TextView mHigh;
	
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
		//mHigh = (TextView) findViewById(R.id.high);
		//mHigh.setText("0");

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
			mScore.setText("");
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
		String s = mScore.getText().toString();
		//String h = mHigh.getText().toString();
		int tmp, high;
		try {
			tmp=Integer.parseInt(s) + 1;
			//high=Integer.parseInt(h);
		}
		catch (Exception e) {
			tmp=0;
			//high=0;
		}
		Log.d(TAG, "nextRect() new score: "+tmp);

		s = String.valueOf(tmp);
		mScore.setText(s);
		/*if (tmp > high) {
			high = tmp;
			mHigh.setText(s);
		}*/
	}
	protected float[] getBirdLocation() {
		float[] f = {bird.getX(), bird.getY(), bird.getHeight(), bird.getWidth()}; 
		return f;
	}
	// BackDrop knows about rectangle & top/bottom - it knows obstacle hazard... collision
	protected void killBird() {
		Log.d(TAG, "killBird() y="+bird.getY() + " Y height="+screenHeight);
		mIsDead=true;
		//mIsInMotion=false;	// stop moving - dead
		birdStopFlapping();	// stop flapping - dead
		bird.setBackgroundResource(R.drawable.red_bird);
		howToImage.setBackgroundResource(R.drawable.touch_to_start);
	}
	private void move() {
		if (mIsRising) {
			Log.d(TAG, "birdUpDown() rising y="+bird.getY());
			bird.setY(bird.getY()-10);
		}
		else {
			Log.d(TAG, "birdUpDown() falling y="+bird.getY());
			bird.setY(bird.getY()+10);
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