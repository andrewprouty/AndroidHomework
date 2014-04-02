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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private AnimationDrawable mBirdAnimation;
	private View view;
	private Button mButton;
	private TextView mScore;
	
	private BackDrop mBackDrop;
	
	boolean mIsDead=false;
	boolean mIsInMotion=false;
	boolean mIsRising=false;
	ImageView bird;
	
	float initialHeight = -1;
	int screenWidth;
	int screenHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		view = findViewById(R.id.container);

		mScore = (TextView) findViewById(R.id.score);
		bird = (ImageView) findViewById(R.id.bird_square);
		mButton = (Button) findViewById(R.id.button);

		bird.setBackgroundResource(R.anim.bird_motion);
		mBirdAnimation = (AnimationDrawable) bird.getBackground();
		Resources resources = getResources();
		XmlPullParser parser = resources.getXml(R.layout.activity_main);
		AttributeSet attributes = Xml.asAttributeSet(parser);
		mBackDrop = new BackDrop(this, attributes);
		mBackDrop = (BackDrop)findViewById(R.id.rect_back_drop);

		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (mIsDead)
					return false;
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
		Log.d(TAG, "Screen width(x)="+screenWidth+" height(y)="+screenHeight);
		mBackDrop.setScreenWidth(screenWidth);
		mBackDrop.setScreenHeight(screenHeight);
		
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mButton.setText(R.string.play_text);
				mButton.setEnabled(false);
				bird.setY(initialHeight);
				mIsDead=false;
				mBackDrop.reset(); // reset rectangle
				mBackDrop.invalidate();
			}
		});
		mButton.setEnabled(false);
		Log.i(TAG, "onCreate() Still there?");
	}
	
	public void birdStartFlapping() {
		Log.d(TAG, "birdStartFlapping()");
		mBirdAnimation.start();
		if (initialHeight < 0) {
			initialHeight = bird.getY();
			Log.d(TAG, "birdStartFlapping() initial height "+initialHeight);
		}
	}
	public void birdStopFlapping() {
		Log.d(TAG, "birdStopFlapping()");
		mBirdAnimation.stop();
	}
	public void touchDown() {
		Log.d(TAG, "touchDown()");
		mIsRising=true;
		if (!mIsInMotion) {
			birdStartFlapping();
			mIsInMotion=true;
			mScore.setText("0");
			move();
		}
	}
	public void touchUp() {
		Log.d(TAG, "touchUp()");
		mIsRising=false;
	}
	private boolean isCollision() {
		float y = bird.getY();
		if ((y < 0) || y + bird.getHeight() + 150> screenHeight) {
			return true;
		}
		else {
			return false;
		}
	}
	protected void setScore() {
		String s = mScore.getText().toString();
		int tmp;
		try {
			Integer.parseInt (s);
			tmp=Integer.parseInt(s) + 1;
		}
		catch (Exception e) {
			tmp=0;
		}
		Log.d(TAG, "nextRect() new score: "+tmp);
		s = String.valueOf(tmp);
		mScore.setText(s);
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
		if (isCollision()){
			Log.d(TAG, "birdUpDown() died y="+bird.getY() + " Y height="+screenHeight);
			mIsDead=true;
			mIsInMotion=false;	// stop moving - dead
			birdStopFlapping();	// stop flapping - dead
			mButton.setText(R.string.replay_text);
			mButton.setEnabled(true);
			Log.d(TAG, "birdUpDown() RIP y="+bird.getY());
		}
		if (mIsInMotion) {
			bird.postDelayed(new Mover(), 70);
			mBackDrop.invalidate();
		}
	}
	private class Mover implements Runnable {
		@Override
		public void run() {
			move();
		}
	}}