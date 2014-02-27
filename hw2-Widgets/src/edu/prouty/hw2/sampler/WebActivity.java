package edu.prouty.hw2.sampler;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import edu.prouty.hw2.sampler.R;

public class WebActivity extends Activity {

	private static final String TAG = "hw2-Web";

	private EditText mParamTextUri;
	private WebView mWebView;
	private Button mBrowseButton;


	private void loadWeb() {
		mParamTextUri = (EditText)findViewById(R.id.param_textUri);
		mWebView.loadUrl(mParamTextUri.getText().toString());
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		//getActionBar().setHomeButtonEnabled(true);	//other way to enable.. but no cool left arrow 
        getActionBar().setDisplayHomeAsUpEnabled(true);
		mParamTextUri = (EditText)findViewById(R.id.param_textUri);
		mParamTextUri.setText(getIntent().getStringExtra(MainActivity.EXTRA_PARAM_TEXT));

		mWebView = (WebView)findViewById(R.id.my_webView);
		mWebView.getSettings().setJavaScriptEnabled(true);
		loadWeb();

		mBrowseButton = (Button)findViewById(R.id.browse_button);
		mBrowseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "mBrowseButton.onClick() called");
				mParamTextUri.setText("http://www.google.com"); //Andy remove before turning in... faster than typing it
				loadWeb();
			}
		});
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
		Log.i(TAG, "onOptionsItemSelected() BEG");
		if(android.R.id.home == item.getItemId()) {
			Log.i(TAG, "onOptionsItemSelected toFinish");
			toFinish();
		}
		Log.i(TAG, "onOptionsItemSelected() END");
		return super.onOptionsItemSelected(item);
	}
	private void toFinish() {
		Log.i(TAG, "exitActivity() called");
		Intent i = new Intent();
		mParamTextUri = (EditText)findViewById(R.id.param_textUri);
		Log.d(TAG, "RESULT: "+RESULT_OK +" " + mParamTextUri.getText().toString());
		i.putExtra(MainActivity.EXTRA_PARAM_RETURN, mParamTextUri.getText().toString());
		setResult(RESULT_OK, i);
		finish();
		Log.i(TAG, "exitActivity() end");
	}
}