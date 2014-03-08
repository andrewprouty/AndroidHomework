package edu.prouty.hw3.photogallery;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

public class UserListActivity extends SingleFragmentActivity {
	private static final String TAG = "UserListActivity";
	private UserItem mUserItem;
	
	protected void launchPhotoListActivity() {
		Intent i = new Intent (UserListActivity.this, PhotoListActivity.class);
		i.putExtra("UserId", mUserItem.getUserId().toString());
		i.putExtra("UserName", mUserItem.getUserName().toString());
		startActivity(i);
	}
	
	@Override
	public Fragment createFragment() {
		//return new PhotoListFragment();
		 return new UserListFragment();
		// return new PhotoGalleryFragment();
	}

	public UserItem getUserItem () {
		return mUserItem;
	}
	protected void setUserItem (UserItem userItem) {
		mUserItem = userItem;
		Log.d(TAG, "setUserItem() user: "
				+ mUserItem.getUserId() + "-"
				+ mUserItem.getUserName());
	}
}
