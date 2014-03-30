package com.ap.LifeCycleMethods.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.ap.LifeCycleMethods.LifeCycleActivity;

public class LifeCycleActivityTest extends
ActivityInstrumentationTestCase2<LifeCycleActivity> {

	private static final String TAG = "hw1Test";
	private LifeCycleActivity mActivity;
	private Instrumentation mInstrumentation;
	private TextView mCreateCount, mRestartCount, mStartCount, mResumeCount, mPauseCount;
	private int countCreate, countRestart, countStart, countResume, countPause;
	private int assertCreate, assertRestart, assertStart, assertResume, assertPause;
	private Button mResetButton;

	public LifeCycleActivityTest() {
		super(LifeCycleActivity.class);
	}

	private void logValues() {
		Log.d(TAG, "logValues() Create: "+mCreateCount.getText());
		Log.d(TAG, "logValues() Restart: "+mRestartCount.getText());
		Log.d(TAG, "logValues() Start: "+mStartCount.getText());
		Log.d(TAG, "logValues() Resume: "+mResumeCount.getText());
		Log.d(TAG, "logValues() Pause: "+mPauseCount.getText());
	}
	private void setCounts() {
		Log.d(TAG, "setCounts()");
		logValues();
		countCreate  = Integer.parseInt(mCreateCount.getText().toString());
		countRestart = Integer.parseInt(mRestartCount.getText().toString());
		countStart   = Integer.parseInt(mStartCount.getText().toString());
		countResume  = Integer.parseInt(mResumeCount.getText().toString());
		countPause   = Integer.parseInt(mPauseCount.getText().toString());
	}
	private void initAsserts() {
		Log.d(TAG, "initAsserts()");
		logValues();
		assertCreate  = Integer.parseInt(mCreateCount.getText().toString());
		assertRestart = Integer.parseInt(mRestartCount.getText().toString());
		assertStart   = Integer.parseInt(mStartCount.getText().toString());
		assertResume  = Integer.parseInt(mResumeCount.getText().toString());
		assertPause   = Integer.parseInt(mPauseCount.getText().toString());
	}
	
	private void getActivityFields() { // Each time reset activity
		mResetButton = (Button) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.resetButton);

		mCreateCount= (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onCreateCount);
		mRestartCount= (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onRestartCount);
		mStartCount= (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onStartCount);
		mResumeCount= (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onResumeCount);
		mPauseCount= (TextView) mActivity.findViewById(com.ap.LifeCycleMethods.R.id.onPauseCount);
	}
	protected void setUp() throws Exception {
		super.setUp();
		Log.i(TAG, "setUp()");
		//setActivityInitialTouchMode(false); //per http://developer.android.com/tools/testing/activity_test.html
		mInstrumentation = getInstrumentation();
		mActivity = this.getActivity();
		getActivityFields();
	}
	public void xtest1Launch() {
		Log.i(TAG, "===test1Launch()===");
		assertNotNull(mResetButton);
		assertNotNull(mCreateCount);
		setCounts();
		// At launch assert values are expected (not calculated)
		Log.i(TAG, "---test1Launch() asserts 1,0,1,1,0");
		assertEquals(countCreate, 1);
		assertEquals(countRestart, 0);
		assertEquals(countStart, 1);
		assertEquals(countResume, 1);
		assertEquals(countPause, 0);
	}
	public void test2Pause() {
		Log.i(TAG, "===test2Pause()===");
		setCounts();
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				mInstrumentation.callActivityOnPause(mActivity);
				Log.d(TAG, "test2Pause().run() mPauseCount: "+mPauseCount.getText());
			}
		});
		mInstrumentation.waitForIdleSync();
		initAsserts(); // set & log assertPause to actual
		Log.i(TAG, "---test2Pause() assert Pause++.");
		assertEquals(countPause+1, assertPause);
	}
	
	@UiThreadTest
	public void test3DestroyResume() {
		Log.i(TAG, "===test3DestroyResume()===");
		setCounts();
		mInstrumentation.callActivityOnPause(mActivity);
		mActivity.finish();
		mActivity = this.getActivity();
		mInstrumentation.callActivityOnCreate(mActivity, null);
		mInstrumentation.callActivityOnStart(mActivity);
		mInstrumentation.callActivityOnResume(mActivity);
		getActivityFields(); // new activity means new fields/re-fetch
		initAsserts();
		Log.i(TAG, "---test3DestroyResume() assert +1(Pause,Create,Start,Resume), same(Restart)");
		assertEquals(countPause+1, assertPause);
		assertEquals(countCreate+1, assertCreate);
		assertEquals(countStart+1, assertStart);
		assertEquals(countResume+1, assertResume);
		assertEquals(countRestart, assertRestart);
	}
	@UiThreadTest
	public void Xtest4OrientLandscape() {
		Log.i(TAG, "===test4OrientLandscape()===");
		setCounts(); // set & log counts
		Log.d(TAG, "test4OrientLandscape() re-orient...");
		mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		mActivity = this.getActivity();
		mInstrumentation.callActivityOnPause(mActivity);
		initAsserts();
		Log.i(TAG, "---test4OrientLandscape() assert +1(Create,Start,Resume), same(Restart,Pause)");
	}
}