package edu.prouty.hw2.widgets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class WebActivity extends Activity {

	private static final String TAG = "hw2-Web";

	private EditText mParamTextUri;
	private WebView mWebView;
	private Button mBrowseButton;


	private void loadWeb() {
		mParamTextUri = (EditText)findViewById(R.id.param_textUri);
		mWebView.loadUrl(mParamTextUri.getText().toString());
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

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
				mParamTextUri.setText("http://www.google.com"); //Andy remove before turning in
				loadWeb();
			}
		});
	}

	private void exitActivity() {
		Log.i(TAG, "exitActivity() called");
		Intent i = new Intent();
		mParamTextUri = (EditText)findViewById(R.id.param_textUri);
		Log.d(TAG, "RESULT: "+RESULT_OK +" " + mParamTextUri.getText().toString());
		i.putExtra(MainActivity.EXTRA_PARAM_RETURN, mParamTextUri.getText().toString());
		setResult(RESULT_OK, i);
		finish();
		Log.i(TAG, "exitActivity() end");
	}

	@Override
	public void onBackPressed() { 
		Log.d(TAG, "onBackPressed() called");
		exitActivity();
		super.onBackPressed();
	}
}