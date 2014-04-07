package com.ap.LifeCycleMethods.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.ap.LifeCycleMethods.FooActivity;
import com.ap.LifeCycleMethods.LifeCycleActivity;

public class LifeCycleActivityTest extends
ActivityInstrumentationTestCase2<LifeCycleActivity> {

	private static final String TAG = "hw1Test";
	private LifeCycleActivity mActivity;
	private Instrumentation mInstrumentation;
	private TextView mCreateCount, mRestartCount, mStartCount, mResumeCount, mPauseCount, mStopCount, mDestroyCount;
	private int countCreate, countRestart, countStart, countResume, countPause, countStop, countDestroy;
	private int assertCreate, assertRestart, assertStart, assertResume, assertPause, assertStop, assertDestroy;
	private Button mResetButton;

	public LifeCycleActivityTest() {
		super(LifeCycleActivity.class);
	}
	// Helpers
	private void logValues() {
		Log.d(TAG, "logValues() Create: "+mCreateCount.getText());
		Log.d(TAG, "logValues() Restart: "+mRestartCount.getText());
		Log.d(TAG, "logValues() Start: "+mStartCount.getText());
		Log.d(TAG, "logValues() Resume: "+mResumeCount.getText());
		Log.d(TAG, "logValues() Pause: "+mPauseCount.getText());
		Log.d(TAG, "logValues() Stop: "+mStopCount.getText());
		Log.d(TAG, "logValues() Destroy: "+mDestroyCount.getText());
	}
	private void setCounts() {
		Log.d(TAG, "setCounts()");
		logValues();
		countCreate  = Integer.parseInt(mCreateCount.getText().toString());
		countRestart = Integer.parseInt(mRestartCount.getText().toString());
		countStart   = Integer.parseInt(mStartCount.getText().toString());
		countResume  = Integer.parseInt(mResumeCount.getText().toString());
		countPause   = Integer.parseInt(mPauseCount.getText().toString());
		countStop    = Integer.parseInt(mStopCount.getText().toString());
		countDestroy = Integer.parseInt(mDestroyCount.getText().toString());
	}
	private void initAsserts() {
		Log.d(TAG, "initAsserts()");
		logValues();
		assertCreate  = Integer.parseInt(mCreateCount.getText().toString());
		assertRestart = Integer.parseInt(mRestartCount.getText().toString());
		assertStart   = Integer.parseInt(mStartCount.getText().toString());
		assertResume  = Integer.parseInt(mResumeCount.getText().toString());
		assertPause   = Integer.parseInt(mPauseCount.getText().toString());
		assertStop    = Integer.parseInt(mStopCount.getText().toString());
		assertDestroy = Integer.parseInt(mDestroyCount.getText().toString());
	}
	private void getActivityFields() { // Each time reset activity
		mResetButton = (Button) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.resetButton);

		mCreateCount=  (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onCreateCount);
		mRestartCount= (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onRestartCount);
		mStartCount=   (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onStartCount);
		mResumeCount=  (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onResumeCount);
		mPauseCount=   (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onPauseCount);
		mStopCount=    (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onStopCount);
		mDestroyCount= (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onDestroyCount);
	}
	// setUp and Tests
	protected void setUp() throws Exception {
		super.setUp();
		Log.i(TAG, "setUp()");
		setActivityInitialTouchMode(false); //per http://developer.android.com/tools/testing/activity_test.html
		mInstrumentation = getInstrumentation();
		mActivity = this.getActivity();
		getActivityFields();
	}
	public void test1Launch_CreateStartResume() {
		Log.i(TAG, "===test1Launch_CreateStartResume()===");
		assertNotNull(mResetButton);
		assertNotNull(mCreateCount);
		setCounts();
		// At launch assert values can be expected (not calculated)
		Log.i(TAG, "---test1Launch_CreateStartResume()=1(Create, Start, Resume),=0(Pause,Restart), same(Stop/Destroy)");
		initAsserts();
		// expected, actual
		assertEquals(1, assertCreate);
		assertEquals(0, assertPause);
		assertEquals(1, assertStart);
		assertEquals(1, assertResume);
		assertEquals(0, assertRestart);
		assertEquals(countStop, assertStop); // zero if just installed
		assertEquals(countDestroy, assertDestroy);
	}
	@UiThreadTest
	public void test2PauseToResume() {
		Log.i(TAG, "===test2PauseToResume()===");
		setCounts();
		mInstrumentation.callActivityOnPause(mActivity);
		mInstrumentation.callActivityOnResume(mActivity);
		initAsserts(); // set & log assertPause to current
		Log.i(TAG, "---test2PauseToResume() assert=+1(Pause,Resume), same(others)");
		// expected, actual
		assertEquals(countCreate, assertCreate);
		assertEquals(countPause+1, assertPause);
		assertEquals(countStart, assertStart);
		assertEquals(countResume+1, assertResume);
		assertEquals(countRestart, assertRestart);
		assertEquals(countStop, assertStop);
		assertEquals(countDestroy, assertDestroy);
	}
	public void test3DestroyToResume() {
		Log.i(TAG, "===test3DestroyResume()===");
		setCounts();
		mActivity.finish();
		//Since we have turned on "Don't Keep Activities" this will kill the main activity
		Activity foo = this.launchActivity("com.ap.LifeCycleMethods", FooActivity.class, null);
		foo.finish();
		//Now we can recreate the MainActivity 
		mInstrumentation = getInstrumentation();
		mActivity = this.getActivity();
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				mInstrumentation.callActivityOnRestart(mActivity);
				mInstrumentation.callActivityOnStart(mActivity);
				mInstrumentation.callActivityOnResume(mActivity);
			}
		});
		mInstrumentation.waitForIdleSync();
		getActivityFields(); // new activity means new fields/re-fetch
		initAsserts();
		Log.i(TAG, "---test3DestroyResume() assert=+1(most), same(Create)");
		// expected, actual
		assertEquals(countCreate, assertCreate);
		assertEquals(countPause+1, assertPause);
		assertEquals(countStart+1, assertStart);
		assertEquals(countResume+1, assertResume);
		assertEquals(countRestart+1, assertRestart);
		assertEquals(countStop+1, assertStop);
		assertEquals(countDestroy+1, assertDestroy);
	}
	@UiThreadTest
	public void test4ResetButton() {
		Log.i(TAG, "===test4ResetButton()===");
		mResetButton.performClick();
		Log.i(TAG, "---test4ResetButton() assert =0 (all)");
		initAsserts(); // =(expected,actual)
		assertEquals(0,assertCreate);
		assertEquals(0,assertRestart);
		assertEquals(0,assertStart);
		assertEquals(0,assertResume);
		assertEquals(0,assertPause);
		assertEquals(0,assertStop);
		assertEquals(0,assertDestroy);
	}
}