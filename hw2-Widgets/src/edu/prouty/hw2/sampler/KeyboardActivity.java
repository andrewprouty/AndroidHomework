package edu.prouty.hw2.sampler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import edu.prouty.hw2.sampler.R;

public class KeyboardActivity extends Activity{

	private static final String TAG = "hw2-Key";
	private Button mKeyBackButton;
	private Button mHideButton;
	private EditText mKeyParamEditText;
	@SuppressWarnings("unused")
	private EditText mMiddleEditText;
	@SuppressWarnings("unused")
	private EditText mBottomEditText;
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_keyboard);
		//getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        /* using http://www.androidhive.info/2013/11/android-working-with-action-bar skipping step 9
         * "up" shows left arrow.. but omitting the parent declare avoided the side affect of not passing back the parameter
         * to solution to that was also adding to the manifest under activity for MainActivity: android:launchMode="singleTop" 
         */
		mKeyBackButton = (Button)findViewById(R.id.key_back_button);
		mKeyBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "mKeyBackButton.onClick() called");
				toFinish();
				Log.i(TAG, "mKeyBackButton.onClick() end");
			}
		});
	
		mKeyParamEditText = (EditText)findViewById(R.id.key_param_editText);
		mKeyParamEditText.setText(getIntent().getStringExtra(MainActivity.EXTRA_PARAM_TEXT));
		mMiddleEditText = (EditText)findViewById(R.id.key_middle_editText);
		mBottomEditText = (EditText)findViewById(R.id.key_bottom_editText);
	
		mHideButton = (Button)findViewById(R.id.key_hide_button);
		mHideButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "mHideButton.onClick() called");
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mKeyParamEditText.getWindowToken(), 0);
			}
		});
	}

	@Override
	//Allow back key to return parameters, etc...
	public void onBackPressed() { 
		Log.d(TAG, "onBackPressed() called");
		toFinish();
		super.onBackPressed();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(android.R.id.home == item.getItemId()) {
			toFinish();
		}
		return super.onOptionsItemSelected(item);
	}
	private void toFinish() {
		Intent i = new Intent();
		mKeyParamEditText = (EditText)findViewById(R.id.key_param_editText);
		i.putExtra(MainActivity.EXTRA_PARAM_RETURN, mKeyParamEditText.getText().toString());
		setResult(RESULT_OK, i);
		finish();
	}
}