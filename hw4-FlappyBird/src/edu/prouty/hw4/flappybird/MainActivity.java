package edu.prouty.hw4.flappybird;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private AnimationDrawable mBirdAnimation;
	private Button mButton;

	boolean mIsInMotion=false;
	boolean mIsRising=false;
	ImageView bird;
	
	float initialHeight = -1;
	int screenHeight;
	int deltaX = 10;
	int deltaY = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		View view = findViewById(R.id.container);

		bird = (ImageView) findViewById(R.id.bird_square);
		bird.setBackgroundResource(R.anim.bird_motion);
		mBirdAnimation = (AnimationDrawable) bird.getBackground();

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
		screenHeight = screenSize.y;
		Log.d(TAG, "noCreate() ready");
		
		mButton = (Button) findViewById(R.id.button);
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mButton.setText(R.string.play_text);
				mButton.setEnabled(false);
				bird.setY(initialHeight);
			}
		});
		mButton.setEnabled(false);

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
		mIsInMotion=true;
		birdStartFlapping();
		birdUpDown();
	}
	public void touchUp() {
		Log.d(TAG, "touchUp()");
		mIsRising=false;
		birdUpDown();
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
	private void birdUpDown() {
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
			mIsInMotion=false;	// stop moving - dead
			birdStopFlapping();	// stop flapping - dead
			mButton.setText(R.string.replay_text);
			mButton.setEnabled(true);
			Log.d(TAG, "birdUpDown() RIP y="+bird.getY());
		}
		if (mIsInMotion) {
			bird.postDelayed(new Mover(), 100);
		}
	}
	private class Mover implements Runnable {
		@Override
		public void run() {
			birdUpDown();
		}
	}}