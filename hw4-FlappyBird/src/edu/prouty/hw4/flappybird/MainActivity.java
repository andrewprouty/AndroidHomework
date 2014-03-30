package edu.prouty.hw4.flappybird;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private AnimationDrawable sceneAnimation;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView scene = (ImageView) findViewById(R.id.bird_square);
		scene.setBackgroundResource(R.anim.bird_motion);
		this.sceneAnimation = (AnimationDrawable) scene.getBackground();
		go(scene);
	}
	public void go(View source) {
		this.sceneAnimation.start();
	}
	public void stop(View source) {
		this.sceneAnimation.stop();
	}
	
}