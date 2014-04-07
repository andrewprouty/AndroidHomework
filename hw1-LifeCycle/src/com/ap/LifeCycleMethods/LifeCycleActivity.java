package com.ap.LifeCycleMethods;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class LifeCycleActivity extends Activity {

	public static final String PREFS_NAME = "MyPrefsFile";
	TextView countOutput;
	TextView countCreateOutput;
	TextView countRestartOutput;
	TextView countStartOutput;
	TextView countResumeOutput;
	TextView countPauseOutput;
	TextView countStopOutput;
	TextView countDestroyOutput;

	private static final String TAG = "LifeCycle";
	int countCreate = 0;
	int countRestart = 0;
	int countStart = 0;
	int countResume = 0;
	int countPause = 0;
	int countStop = 0;
	int countDestroy = 0;

	//onClick within XML
	public void resetCounts(View button) {
		Log.i(TAG, "resetCount()==========");
		countCreate = 0;
		countCreateOutput = (TextView) this.findViewById(R.id.onCreateCount);
		countCreateOutput.setText(String.valueOf(countCreate));
		countRestart = 0;
		countRestartOutput = (TextView) this.findViewById(R.id.onRestartCount);
		countRestartOutput.setText(String.valueOf(countRestart));
		countStart = 0;
		countStartOutput = (TextView) this.findViewById(R.id.onStartCount);
		countStartOutput.setText(String.valueOf(countStart));
		countResume = 0;
		countResumeOutput = (TextView) this.findViewById(R.id.onResumeCount);
		countResumeOutput.setText(String.valueOf(countResume));
		countPause = 0;
		countPauseOutput = (TextView) this.findViewById(R.id.onPauseCount);
		countPauseOutput.setText(String.valueOf(countPause));
		countStop = 0;
		countStopOutput = (TextView) this.findViewById(R.id.onStopCount);
		countStopOutput.setText(String.valueOf(countStop));
		countDestroy = 0;
		countDestroyOutput = (TextView) this.findViewById(R.id.onDestroyCount);
		countDestroyOutput.setText(String.valueOf(countDestroy));
		/*Log.i(TAG, "resetCount() calling FooActivity");
		Intent i = new Intent (LifeCycleActivity.this, FooActivity.class);
		Log.d(TAG, "resetCount() calling 1");
		startActivity(i);
		Log.i(TAG, "resetCount() Call complete");*/
	}
	public void refreshScreen() {
		Log.d(TAG, "refreshScreen()");
		countCreateOutput = (TextView) this.findViewById(R.id.onCreateCount);
		countCreateOutput.setText(String.valueOf(countCreate));
		countRestartOutput = (TextView) this.findViewById(R.id.onRestartCount);
		countRestartOutput.setText(String.valueOf(countRestart));
		countStartOutput = (TextView) this.findViewById(R.id.onStartCount);
		countStartOutput.setText(String.valueOf(countStart));
		countResumeOutput = (TextView) this.findViewById(R.id.onResumeCount);
		countResumeOutput.setText(String.valueOf(countResume));
		countPauseOutput = (TextView) this.findViewById(R.id.onPauseCount);
		countPauseOutput.setText(String.valueOf(countPause));
		countStopOutput = (TextView) this.findViewById(R.id.onStopCount);
		countStopOutput.setText(String.valueOf(countStop));
		countDestroyOutput = (TextView) this.findViewById(R.id.onDestroyCount);
		countDestroyOutput.setText(String.valueOf(countDestroy));
	}
	
	/*************** Activity Events ******************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		countCreate++;
		Log.i(TAG, "onCreate count: "+countCreate);
		countCreateOutput = (TextView) this.findViewById(R.id.onCreateCount);
		countCreateOutput.setText(String.valueOf(countCreate));

		// Restore preferences (stop/destroy cannot use ...InstanceState())
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		countStop   +=settings.getInt("stop",0);
		countDestroy+=settings.getInt("destroy",0);
		Log.d(TAG, "stop: "   +countStop);
		Log.d(TAG, "destroy: "+countDestroy);
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		countRestart++;
		Log.i(TAG, "onRestart count: "+countRestart);
		countRestartOutput = (TextView) this.findViewById(R.id.onRestartCount);
		countRestartOutput.setText(String.valueOf(countRestart));
	}
	@Override
	protected void onStart() {
		super.onStart();
		countStart++;
		Log.i(TAG, "onStart count: "+countStart);
		countStartOutput = (TextView) this.findViewById(R.id.onStartCount);
		countStartOutput.setText(String.valueOf(countStart));
	}
	@Override
	protected void onResume() {
		super.onResume();
		countResume++;
		Log.i(TAG, "onResume count: "+countResume);
		refreshScreen();
		countResumeOutput = (TextView) this.findViewById(R.id.onResumeCount);
		countResumeOutput.setText(String.valueOf(countResume));
	}
	@Override
	protected void onPause() {
		super.onPause();
		countPause++;
		Log.i(TAG, "onPause count: "+countPause);
		countPauseOutput = (TextView) this.findViewById(R.id.onPauseCount);
		countPauseOutput.setText(String.valueOf(countPause));
	}

	// To see the other related Activities in LogCat
	@Override
	protected void onStop() {
		super.onStop();
		countStop++;
		Log.i(TAG, "onStop count: "+countStop);
		countStopOutput = (TextView) this.findViewById(R.id.onStopCount);
		countStopOutput.setText(String.valueOf(countStop));
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		countDestroy++;
		Log.i(TAG, "onDestroy count: "+countDestroy);
		countDestroyOutput = (TextView) this.findViewById(R.id.onDestroyCount);
		countDestroyOutput.setText(String.valueOf(countDestroy));
		// onStop/onDestroy come after onSaveInstanceState - thus alternate
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		Log.d(TAG, "stop: "    +countStop);
		Log.d(TAG, "destroy: " +countDestroy);
		editor.putInt("stop", countStop);
		editor.putInt("destroy", countDestroy);
		editor.commit();
	}
	/*************** Other Built-in Methods ******************/
	@Override
	public void onSaveInstanceState(Bundle setState) {
		super.onSaveInstanceState(setState);
		Log.i(TAG, "onSaveInstanceState()");
		Log.d(TAG, "create: "  +countCreate);
		Log.d(TAG, "restart: " +countRestart);
		Log.d(TAG, "start: "   +countStart);
		Log.d(TAG, "resume: "  +countResume);
		Log.d(TAG, "pause: "   +countPause);
		setState.putInt("create",  countCreate);
		setState.putInt("restart", countRestart);
		setState.putInt("start",   countStart);
		setState.putInt("resume",  countResume);
		setState.putInt("pause",   countPause);
	}

	@Override
	public void onRestoreInstanceState(Bundle getState) {
		super.onRestoreInstanceState(getState);
		Log.i(TAG, "onRestoreInstanceState()");
		countCreate +=getState.getInt("create");
		countRestart+=getState.getInt("restart");
		countStart  +=getState.getInt("start");
		countResume +=getState.getInt("resume");
		countPause  +=getState.getInt("pause");
		Log.d(TAG, "create: " +countCreate);
		Log.d(TAG, "restart: "+countRestart);
		Log.d(TAG, "start: "  +countStart);
		Log.d(TAG, "resume: " +countResume);
		Log.d(TAG, "pause: "  +countPause);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}