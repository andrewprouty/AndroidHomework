package com.ap.LifeCycleMethods.test;

import android.app.Instrumentation;
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
		Log.i(TAG, "---test1Launch_CreateStartResume() asserts 1,0,1,1,0");
		assertEquals(countCreate, 1);
		assertEquals(countRestart, 0);
		assertEquals(countStart, 1);
		assertEquals(countResume, 1);
		assertEquals(countPause, 0);
	}
	public void test2PauseToResume() {
		Log.i(TAG, "===test2PauseToResume()===");
		setCounts();
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				mInstrumentation.callActivityOnPause(mActivity);
				mInstrumentation.callActivityOnResume(mActivity);
				Log.d(TAG, "test2PauseToResume().run() mPauseCount: "+mPauseCount.getText());
			}
		});
		mInstrumentation.waitForIdleSync();
		initAsserts(); // set & log assertPause to actual
		Log.i(TAG, "---test2PauseToResume() assert +(Pause,Resume), same(others)");
		assertEquals(countCreate, assertCreate);
		assertEquals(countPause+1, assertPause);
		assertEquals(countStart, assertStart);
		assertEquals(countResume+1, assertResume);
		assertEquals(countRestart, assertRestart);
	}

	@UiThreadTest
	public void test3DestroyToResume() {
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
	public void test4Integration_ResetButton() {
		Log.i(TAG, "===test4Integration_ResetButton()===");

		Log.i(TAG, "---test4Integration() ONE: 1,0,1,1,0");
		setCounts(); // At launch assert values can be expected (vs calculated)
		assertEquals(countCreate, 1);
		assertEquals(countRestart, 0);
		assertEquals(countStart, 1);
		assertEquals(countResume, 1);
		assertEquals(countPause, 0);

		Log.i(TAG, "---test4Integration() TWO: +(Pause,Resume), same(others)");
		mInstrumentation.callActivityOnPause(mActivity);
		mInstrumentation.callActivityOnResume(mActivity);
		initAsserts(); // set & log assertPause to actual
		assertEquals(countCreate, assertCreate);
		assertEquals(countPause+1, assertPause);
		assertEquals(countStart, assertStart);
		assertEquals(countResume+1, assertResume);
		assertEquals(countRestart, assertRestart);

		Log.i(TAG, "---test4Integration 333: +1(Pause,Create,Start,Resume), same(Restart)");
		setCounts();
		mInstrumentation.callActivityOnPause(mActivity);
		mActivity.finish();
		mActivity = this.getActivity();
		mInstrumentation.callActivityOnCreate(mActivity, null);
		mInstrumentation.callActivityOnStart(mActivity);
		mInstrumentation.callActivityOnResume(mActivity);
		getActivityFields(); // new activity means new fields/re-fetch
		initAsserts();
		assertEquals(countPause+1, assertPause);
		assertEquals(countCreate+1, assertCreate);
		assertEquals(countStart+1, assertStart);
		assertEquals(countResume+1, assertResume);
		assertEquals(countRestart, assertRestart);

		Log.i(TAG, "---test4Integration 000: Reset should be zeros");
		mResetButton.performClick();
		initAsserts();
		assertEquals(0,assertCreate);
		assertEquals(0,assertRestart);
		assertEquals(0,assertStart);
		assertEquals(0,assertResume);
		assertEquals(0,assertPause);
	}
}