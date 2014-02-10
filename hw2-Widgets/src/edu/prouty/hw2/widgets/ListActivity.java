package edu.prouty.hw2.widgets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ListActivity extends FragmentActivity {

	private static final String TAG = "hw2-L_Act";

	private Button mListBackButton;
	private TextView mListParamTextView;

	public void setOS(String OS) {
		mListParamTextView.setText(OS);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate() called");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_list);
		
 		mListParamTextView = (TextView)findViewById(R.id.list_param_textView);

		mListBackButton = (Button)findViewById(R.id.list_back_button);
		mListBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "mBackButtonList.onClick() called");
				Intent i = new Intent();
				mListParamTextView = (TextView)findViewById(R.id.list_param_textView);
				i.putExtra(MainActivity.EXTRA_PARAM_RETURN, mListParamTextView.getText().toString());
				setResult(RESULT_OK, i);
				finish();
				Log.i(TAG, "mListBackButton.onClick() end");
			}
		});

		FragmentManager fm = getSupportFragmentManager();
		if (fm.findFragmentById(R.id.os_listFragment) == null) {
			ListFrag mList = new ListFrag();
			fm.beginTransaction().add(R.id.os_listFragment, mList).commit();
			//		if (fm.findFragmentById(R.id.fragmentContainer) == null) {
			//			ListFrag list = new ListFrag();
			//			fm.beginTransaction().add(R.id.fragmentContainer, list).commit();
		}
	}
}