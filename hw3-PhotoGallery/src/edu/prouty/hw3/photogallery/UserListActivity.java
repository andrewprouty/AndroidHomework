package edu.prouty.hw3.photogallery;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import edu.prouty.hw3.photogallery.GalleryDatabaseHelper.UserCursor;

public class UserListActivity extends FragmentActivity {
	private static final String TAG = "UserListActivity";
	private UserItem mUserItem;
	private GalleryDatabaseHelper mHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate()");
        setContentView(R.layout.activity_fragment);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
        }
        mHelper = new GalleryDatabaseHelper(getApplicationContext());
    }

    public void onDestroy() {
		super.onDestroy();
    }

	protected void launchPhotoListActivity() {
		//Toast.makeText(this,"Selected "+mUserItem.getUserId() + "-" + mUserItem.getUserName(),Toast.LENGTH_SHORT).show();
		Intent i = new Intent (UserListActivity.this, PhotoListActivity.class);
		i.putExtra("UserId", mUserItem.getUserId().toString());
		i.putExtra("UserName", mUserItem.getUserName().toString());
		startActivity(i);
	}
	
	public Fragment createFragment() {
		 return new UserListFragment();
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
    protected void insertUserItems(ArrayList<UserItem> users) {
        UserItem user;
        Log.d(TAG, "insertUserItem()");
		mHelper.deleteUsers();
        for (int i=0; i<users.size(); i++) {
    		user=users.get(i);
    		Log.d(TAG, "insertUserItem() user: "
    				+ user.getUserId() + "-"
    				+ user.getUserName());
            mHelper.insertUser(user);
        }
        return;
    }
    protected ArrayList<UserItem> fetchUserItems() {
    	UserCursor cursor;
    	ArrayList<UserItem> items = new ArrayList<UserItem>();
    	cursor = mHelper.queryUsers();
    	cursor.moveToFirst();
    	while(!cursor.isAfterLast()) {
    		UserItem item = cursorToUserItem(cursor);
    		items.add(item);
    		cursor.moveToNext();
    		Log.d(TAG, "fetchUserItem() user: "
    				+ item.getUserId() + "-"
    				+ item.getUserName());
    	}
    	return items;
    }
    private UserItem cursorToUserItem(UserCursor cursor) {
    	UserItem item = new UserItem();
    	item.setUserId(cursor.getString(0));   // TODO cursor.getInt
    	item.setUserName(cursor.getString(1));
		return item;
    }
}
