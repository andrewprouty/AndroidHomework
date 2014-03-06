package edu.prouty.hw3.photogallery;

import android.support.v4.app.Fragment;
import android.util.Log;

public class UserListActivity extends SingleFragmentActivity {
	public static final String TAG = "UserListActivity";
	UserItem mUserItem;
	public void setUserItem (UserItem userItem) {
		mUserItem = userItem;
		Log.i(TAG, "setUserItem() user: "
				+ mUserItem.getUserId() + " "
				+ mUserItem.getUserName());
	}
	
	public UserItem getUserItem () {
		return mUserItem;
	}
	
	@Override
	public Fragment createFragment() {
		return new PhotoListFragment();
		// return new UserListFragment();
		// return new PhotoGalleryFragment();
	}
}
