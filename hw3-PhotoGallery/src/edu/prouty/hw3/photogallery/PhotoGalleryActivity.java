package edu.prouty.hw3.photogallery;

import android.support.v4.app.Fragment;
import android.util.Log;

public class PhotoGalleryActivity extends SingleFragmentActivity {
	public static final String TAG = "PhotoGalleryActivity";
	UserItem mUserItem;
	public void setUserItem (UserItem userItem) {
		mUserItem = userItem;
		Log.i(TAG, "setUserItem() user: "
				+ mUserItem.getUserId() + " "
				+ mUserItem.getUserName());
	}
	@Override
	public Fragment createFragment() {
		return new UserListFragment();
		// return new PhotoGalleryFragment();
	}
}
