package edu.prouty.hw2.widgets;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ListActivity extends FragmentActivity {

	private static final String TAG = "hw2-L_Act";

	private Button mListBackButton;
	private TextView mListParamTextView;
	private Boolean setOS = false;

	public void setOS(String OS) {
		mListParamTextView.setText(OS);
		setOS = true; //prevent default (i.e. nothing selected) 
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate() called");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_list);
		//getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
		
 		mListParamTextView = (TextView)findViewById(R.id.list_param_textView);

		mListBackButton = (Button)findViewById(R.id.list_back_button);
		mListBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "mBackButtonList.onClick() called");
				toFinish();
				Log.i(TAG, "mListBackButton.onClick() end");
			}
		}); 

		FragmentManager fm = getSupportFragmentManager();
		if (fm.findFragmentById(R.id.os_listFragment) == null) {
			ListFrag mList = new ListFrag();
			fm.beginTransaction().add(R.id.os_listFragment, mList).commit();
		}
	}
	@Override
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
		mListParamTextView = (TextView)findViewById(R.id.list_param_textView);
		if (setOS) {
			i.putExtra(MainActivity.EXTRA_PARAM_RETURN, mListParamTextView.getText().toString());
			}
		setResult(RESULT_OK, i);
		finish();
	}
}