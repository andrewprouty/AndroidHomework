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
		countStop   = Integer.parseInt(mStopCount.getText().toString());
		countDestroy   = Integer.parseInt(mDestroyCount.getText().toString());
	}
	private void initAsserts() {
		Log.d(TAG, "initAsserts()");
		logValues();
		assertCreate  = Integer.parseInt(mCreateCount.getText().toString());
		assertRestart = Integer.parseInt(mRestartCount.getText().toString());
		assertStart   = Integer.parseInt(mStartCount.getText().toString());
		assertResume  = Integer.parseInt(mResumeCount.getText().toString());
		assertPause   = Integer.parseInt(mPauseCount.getText().toString());
		assertStop   = Integer.parseInt(mStopCount.getText().toString());
		assertDestroy   = Integer.parseInt(mDestroyCount.getText().toString());
	}
	private void getActivityFields() { // Each time reset activity
		mResetButton = (Button) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.resetButton);

		mCreateCount= (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onCreateCount);
		mRestartCount= (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onRestartCount);
		mStartCount= (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onStartCount);
		mResumeCount= (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onResumeCount);
		mPauseCount= (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onPauseCount);
		mStopCount= (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onStopCount);
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
		Log.i(TAG, "---test1Launch_CreateStartResume()=1(Create, Start, Resume), same(others)");
		initAsserts();
		// expected, actual
		assertEquals(1, assertCreate);
		assertEquals(countPause, assertPause);
		assertEquals(1, assertStart);
		assertEquals(1, assertResume);
		assertEquals(countRestart, assertRestart);
		assertEquals(countStop, assertStop);
		assertEquals(countDestroy, assertDestroy);
	}
	@UiThreadTest
	// To be call-able switched to UIThreadTest so don't need Runnable() or waitForIdleSync()
	public void test2PauseToResume() {
		Log.i(TAG, "===test2PauseToResume()===");
		setCounts();
		mInstrumentation.callActivityOnPause(mActivity);
		mInstrumentation.callActivityOnResume(mActivity);
		/* mActivity.runOnUiThread(new Runnable() {
			public void run() {
				mInstrumentation.callActivityOnPause(mActivity);
				mInstrumentation.callActivityOnResume(mActivity);
				Log.d(TAG, "test2PauseToResume().run() mPauseCount: "+mPauseCount.getText());
			}
		});
		mInstrumentation.waitForIdleSync(); */
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
	//@UiThreadTest
	public void test3DestroyToResume() {
		Log.i(TAG, "===test3DestroyResume()===");
		setCounts();
		//mInstrumentation.callActivityOnPause(mActivity);
		//mActivity.finish();
		//Since we have turned on "Don't Keep Activities" this will kill the main activity
		Activity foo = this.launchActivity("com.ap.LifeCycleMethods", FooActivity.class, null);
		/*mActivity.runOnUiThread(new Runnable() {
			public void run() {
				Log.d(TAG, "test3().Runnable() ");
			}
		});*/
		//mInstrumentation.waitForIdleSync();
		Log.i(TAG, "test3DestroyResume() -2");
		foo.finish();
		//Now we can recreate the MainActivity 
		Log.i(TAG, "test3DestroyResume() -1");
		mInstrumentation = getInstrumentation();
		Log.i(TAG, "test3DestroyResume() 0");
		//mActivity = this.launchActivity("com.ap.LifeCycleMethods", LifeCycleActivity.class, null);
		Log.i(TAG, "test3DestroyResume() 0.5");
		mActivity = getActivity();
		/*mActivity = this.getActivity();
		Log.i(TAG, "test3DestroyResume() 0.5");
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				Log.i(TAG, "test3DestroyResume().run()");
				mInstrumentation.callActivityOnCreate(mActivity, null);
				Log.i(TAG, "test3DestroyResume().run() 1");
				mInstrumentation.callActivityOnStart(mActivity);
				Log.i(TAG, "test3DestroyResume().run() 2");
				mInstrumentation.callActivityOnResume(mActivity);
				Log.i(TAG, "test3DestroyResume().run() 3");
			}
		});
		Log.i(TAG, "test3DestroyResume() 1");
		mActivity = this.getActivity();
		Log.i(TAG, "test3DestroyResume() 2");
		mInstrumentation.callActivityOnCreate(mActivity, null);
		Log.i(TAG, "test3DestroyResume() 3");
		mInstrumentation.callActivityOnStart(mActivity);
		Log.i(TAG, "test3DestroyResume() 4");
		mInstrumentation.callActivityOnResume(mActivity);*/
		Log.i(TAG, "test3DestroyResume() 5");
		getActivityFields(); // new activity means new fields/re-fetch
		Log.i(TAG, "test3DestroyResume() 6");
		initAsserts();
		Log.i(TAG, "---test3DestroyResume() assert +1(Pause,Create,Start,Resume), same(Restart)");
		assertEquals(countPause+1, assertPause);
		assertEquals(countCreate+1, assertCreate);
		assertEquals(countStart+1, assertStart);
		assertEquals(countResume+1, assertResume);
		assertEquals(countRestart, assertRestart);
	}
	@UiThreadTest
	public void xtest4Integration_ResetButton() {
		Log.i(TAG, "===test4Integration_ResetButton()===");

		Log.d(TAG, "---test4Integration() ONE: 1,0,1,1,0");
		test1Launch_CreateStartResume();

		Log.d(TAG, "---test4Integration() TWO: +(Pause,Resume), same(others)");
		test2PauseToResume();

		Log.d(TAG, "---test4Integration() 333: +1(Pause,Create,Start,Resume), same(Restart)");
		test3DestroyToResume();

		Log.i(TAG, "---test4Integration() 000: Reset should be zeros");
		mResetButton.performClick();
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