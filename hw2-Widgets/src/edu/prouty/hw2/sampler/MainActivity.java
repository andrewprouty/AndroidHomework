package edu.prouty.hw2.sampler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import edu.prouty.hw2.sampler.R;

public class MainActivity extends Activity {

	private static final String TAG = "hw2-Main";

	public static final String EXTRA_PARAM_TEXT   = "edu.prouty.hw2.sampler.param_text";
	public static final String EXTRA_PARAM_RETURN =	"edu.prouty.hw2.sampler.param_text";

	private static final int ACTIVITY_KEYBOARD = 0;
	private static final int ACTIVITY_WEB = 1;
	private static final int ACTIVITY_LIST = 2;
	private int mActivitySelection;
	private Spinner mActivitySpinner; 
	private Button mGoButton;
	private EditText mParamEditText;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent i) {
		Log.d(TAG,"onActivityResult called()");
		if (i == null) {
			Log.d(TAG,"intent=null");
			return;
		}
		String tmp = i.getStringExtra(EXTRA_PARAM_RETURN);
		Log.d(TAG,"Returned value: "+tmp);
		mParamEditText.setText(i.getStringExtra(EXTRA_PARAM_RETURN));
	}

	private void launchActivity(int act) {
		if (ACTIVITY_WEB == act) {
			Intent i = new Intent (MainActivity.this, WebActivity.class);
			i.putExtra(EXTRA_PARAM_TEXT, mParamEditText.getText().toString());
			startActivityForResult(i, 0);
		}
		else if (ACTIVITY_KEYBOARD == act) {
			Intent i = new Intent (MainActivity.this, KeyboardActivity.class);
			i.putExtra(EXTRA_PARAM_TEXT, mParamEditText.getText().toString());
			startActivityForResult(i, 0);
		}
		else if (ACTIVITY_LIST == act) {
			Intent i = new Intent (MainActivity.this, ListActivity.class);
			i.putExtra(EXTRA_PARAM_TEXT, mParamEditText.getText().toString());
			startActivityForResult(i, 0);
		}
	}

	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate(Bundle) called");
		setContentView(R.layout.activity_main);
		
		mActivitySpinner = (Spinner) findViewById(R.id.activity_spinner);
		mActivitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				mActivitySelection = position;
			}
			@Override
			public void onNothingSelected(AdapterView<?> parentView) { }
		});

		mParamEditText = (EditText)findViewById(R.id.main_param_editText);

		mGoButton = (Button)findViewById(R.id.go_button);
		mGoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "GoButton.onClick Activity: "+ mActivitySelection + " Param: " + mParamEditText.getText().toString());
				launchActivity(mActivitySelection);
			}
		});
	} // End onCreate()

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		Log.i(TAG, "onSaveInstanceState");
	}
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart() called");
	}
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause() called");
	}
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume() called");
	}
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop() called");
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy() called");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// http://www.androidhive.info/2013/11/android-working-with-action-bar
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.act_main_actions, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_key:
			launchActivity(ACTIVITY_KEYBOARD);
			return true;
		case R.id.action_web:
			launchActivity(ACTIVITY_WEB);
			return true;
		case R.id.action_list:
			launchActivity(ACTIVITY_LIST);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}