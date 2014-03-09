package edu.prouty.hw3.photogallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class PhotoListActivity extends FragmentActivity {
	private static final String TAG = "PhotoListActivity";
	private UserItem mUserItem = new UserItem();
	private PhotoItem mPhotoItem;

	protected void launchPhotoDisplayActivity() {
		/*Toast.makeText(this,mPhotoItem.getUserId() + "-"
						  + mPhotoItem.getUserName() + "; "
						  + mPhotoItem.getPhotoId() + "-"
						  + mPhotoItem.getPhotoName()
				,Toast.LENGTH_SHORT).show();*/
		Intent i = new Intent (PhotoListActivity.this, ImageActivity.class);
		i.putExtra("UserId",   mPhotoItem.getUserId().toString());
		i.putExtra("UserName", mPhotoItem.getUserName().toString());
		i.putExtra("PhotoId",  mPhotoItem.getPhotoId().toString());
		i.putExtra("PhotoName",mPhotoItem.getPhotoName().toString());
		startActivity(i);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		Log.d(TAG, "onCreate()");

		String id = getIntent().getStringExtra("UserId");
		String name = getIntent().getStringExtra("UserName");
		initUserItem(id, name);

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

	public void initUserItem (String id, String name) {
		mUserItem.setUserId(id);
		mUserItem.setUserName(name);
		Log.d(TAG, "initUserItem() user: "
				+ mUserItem.getUserId() + "-"
				+ mUserItem.getUserName());
	}
	public UserItem getUserItem () {
		return mUserItem;
	}
	public void setPhotoItem (PhotoItem photoItem) {
		mPhotoItem = photoItem;
		Log.d(TAG, "setPhotoItem() photo: "
				+ mPhotoItem.getUserId() + "-"
				+ mPhotoItem.getUserName() + "; "
				+ mPhotoItem.getPhotoId() + "-"
				+ mPhotoItem.getPhotoName());
	}
	public PhotoItem getPhotoItem () {
		return mPhotoItem;
	}
}