package com.ap.LifeCycleMethods;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class LifeCycleActivity extends Activity {

	TextView countOutput;
	TextView countCreateOutput;
	TextView countRestartOutput;
	TextView countStartOutput;
	TextView countResumeOutput;
	TextView countPauseOutput;

	private static final String TAG = "LifeCycle";
	int countCreate = 0;
	int countRestart = 0;
	int countStart = 0;
	int countResume = 0;
	int countPause = 0;

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
	}
	public void refreshScreen() {
		Log.i(TAG, "refreshScreen()");
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
	}
	
	/*************** Activity Events ******************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate(Bundle) called");
		setContentView(R.layout.activity_main);
		countCreate++;
		Log.d(TAG, "onCreate count: "+countCreate);
		countCreateOutput = (TextView) this.findViewById(R.id.onCreateCount);
		countCreateOutput.setText(String.valueOf(countCreate));
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		countRestart++;
		Log.d(TAG, "onRestart count: "+countRestart);
		countRestartOutput = (TextView) this.findViewById(R.id.onRestartCount);
		countRestartOutput.setText(String.valueOf(countRestart));
	}
	@Override
	protected void onStart() {
		super.onStart();
		countStart++;
		Log.d(TAG, "onStart count: "+countStart);
		countStartOutput = (TextView) this.findViewById(R.id.onStartCount);
		countStartOutput.setText(String.valueOf(countStart));
	}

	@Override
	protected void onResume() {
		super.onResume();
		countResume++;
		Log.d(TAG, "onResume count: "+countResume);
		refreshScreen();
		countResumeOutput = (TextView) this.findViewById(R.id.onResumeCount);
		countResumeOutput.setText(String.valueOf(countResume));
	}

	@Override
	protected void onPause() {
		super.onPause();
		countPause++;
		Log.d(TAG, "onPause count: "+countPause);
		countPauseOutput = (TextView) this.findViewById(R.id.onPauseCount);
		countPauseOutput.setText(String.valueOf(countPause));
	}

	// To see the other related Activities in LogCat
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop called");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy called");
	}
	/*************** Other Built-in Methods ******************/
	@Override
	public void onSaveInstanceState(Bundle setState) {
		super.onSaveInstanceState(setState);
		Log.i(TAG, "onSaveInstanceState begin");
		Log.i(TAG, "create: " +countCreate);
		Log.i(TAG, "restart: "+countRestart);
		Log.i(TAG, "start: "  +countStart);
		Log.i(TAG, "resume: " +countResume);
		Log.i(TAG, "pause: "  +countPause);
		setState.putInt("create",  countCreate);
		setState.putInt("restart", countRestart);
		setState.putInt("start",   countStart);
		setState.putInt("resume",  countResume);
		setState.putInt("pause",   countPause);
	}

	@Override
	public void onRestoreInstanceState(Bundle getState) {
		super.onRestoreInstanceState(getState);
		Log.i(TAG, "onRestoreInstanceState begin");
		countCreate +=getState.getInt("create");
		countRestart+=getState.getInt("restart");
		countStart  +=getState.getInt("start");
		countResume +=getState.getInt("resume");
		countPause  +=getState.getInt("pause");
		Log.i(TAG, "create: " +countCreate);
		Log.i(TAG, "restart: "+countRestart);
		Log.i(TAG, "start: "  +countStart);
		Log.i(TAG, "resume: " +countResume);
		Log.i(TAG, "pause: "  +countPause);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}