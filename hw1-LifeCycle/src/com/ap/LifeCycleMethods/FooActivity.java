package com.ap.LifeCycleMethods;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class FooActivity extends Activity {

	private static final String TAG = "FooActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foo);
        EditText mFooText = (EditText)findViewById(R.id.editFoo);
        mFooText.setText("just reset");
	}
}
