package edu.prouty.hw3.photogallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class PhotoListActivity extends FragmentActivity {
	private static final String TAG = "PhotoListActivity";
	//UserItem mUsrItem;
	private UserItem mplaUserItem = new UserItem();
	// TODO rename back to mUsrItem.  get rid of new
	public void setUserItem (UserItem userItem) {
		mplaUserItem = userItem;
		Log.i(TAG, "setUserItem()1 user: "
				+ mplaUserItem.getUserId() + "-"
				+ mplaUserItem.getUserName());
	}

	public UserItem getUserItem () {
		return mplaUserItem;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);

		Log.d(TAG, "onCreate()");
		Log.d(TAG, "onCreate() Id-middle");
		mplaUserItem.setUserId(getIntent().getStringExtra("UserId"));
		Log.d(TAG, "onCreate() Id-after "+mplaUserItem.getUserId());

		mplaUserItem.setUserName(getIntent().getStringExtra("UserName"));

		Log.d(TAG, "onCreate() after Name"+mplaUserItem.getUserName());
		Log.d(TAG, "onCreate() - done!!");

		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

		if (fragment == null) {
			fragment = createFragment();
			manager.beginTransaction()
			.add(R.id.fragmentContainer, fragment)
			.commit();
		}
	}

	public Fragment createFragment() {
		return new PhotoListFragment();
	}
}