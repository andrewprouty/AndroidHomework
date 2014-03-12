package edu.prouty.hw3.photogallery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GalleryDatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "SQLLiteOpenHelper";
	private static final String DB_NAME = "gallery.sqlite";
	private static final int VERSION = 1;

	private static final String TABLE_USER = "user";
	private static final String COLUMN_USER_USER_ID = "user_id";
	private static final String COLUMN_USER_USER_NAME = "user_name";

	private static final String TABLE_PHOTO = "photo";
	private static final String COLUMN_PHOTO_PHOTO_ID = "photo_id";
	private static final String COLUMN_PHOTO_PHOTO_NAME = "photo_name";
	private static final String COLUMN_PHOTO_USER_ID = "user_id";
	private static final String COLUMN_PHOTO_USER_NAME = "user_name";
	//private static final String COLUMN_PHOTO_PHOTO_COUNT = "photo_count";

	public GalleryDatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate()");
		//db.execSQL("create table user (_id integer primary key autoincrement, start_date integer)");
		db.execSQL("create table user (" +
				" user_id varchar(10) primary key, user_name varchar(100))");
		db.execSQL("create table photo (" +
				" photo_id varchar(10) primary key, photo_name varchar(100),"+
				" user_id varchar(10) references user(user_id), user_name varchar(100)," +
				" photo_count integer)"); //TODO remove count if not needed
		Log.d(TAG, "onCreate()ed");
		// TODO user_id & photo_id (in the table) will probably need to be integer to sort properly
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// implement schema changes and data massage here when upgrading
	}

	public long deleteUsers() {
		Log.d(TAG, "deleteUsers()");
		return getWritableDatabase().delete(TABLE_USER, null, null);
	}

	public long insertUser(UserItem user) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_USER_USER_ID, user.getUserId());
		cv.put(COLUMN_USER_USER_NAME, user.getUserName());
		return getWritableDatabase().insert(TABLE_USER, null, cv);
	}

	public long insertPhoto(PhotoItem photo) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_PHOTO_PHOTO_ID, photo.getPhotoId());
		cv.put(COLUMN_PHOTO_PHOTO_NAME, photo.getPhotoName());
		cv.put(COLUMN_PHOTO_USER_ID, photo.getUserId());
		cv.put(COLUMN_PHOTO_USER_NAME, photo.getUserName());
		//cv.put(COLUMN_PHOTO_PHOTO_COUNT, photo.getPhotoCount());
		return getWritableDatabase().insert(TABLE_PHOTO, null, cv);
	}

	public long deletePhotosforUserId(String user) {
		Log.d(TAG, "deletePhotosforUserId()");
		return getWritableDatabase().delete(TABLE_PHOTO,
				COLUMN_PHOTO_USER_ID + " = ?", // THAT User Id
				new String[]{ String.valueOf(user) }); // with this value
	}

	public UserCursor queryUsers() {
		// equivalent to "select * from user order by user_id asc"
		Cursor wrapped = getReadableDatabase().query(TABLE_USER,
				null, null, null, null, null, COLUMN_USER_USER_ID + " asc");
		return new UserCursor(wrapped);
	}

	public PhotoCursor queryPhotosForUser(String user) {
		Cursor wrapped = getReadableDatabase().query(TABLE_PHOTO, 
				null, // all columns 
				COLUMN_PHOTO_USER_ID + " = ?", // THAT User Id
				new String[]{ String.valueOf(user) }, // with this value
				null, // group by
				null, // having
				COLUMN_PHOTO_PHOTO_ID + " asc", // order by
				null); // limit of rows
		return new PhotoCursor(wrapped);
	}

	/**
	 * A convenience class to wrap a cursor that returns rows from the table.
	 * The {@link getUser()} method will give you a UserItem instance for the current row.
	 */
	public static class UserCursor extends CursorWrapper {

		public UserCursor(Cursor c) {
			super(c);
		}

		/**
		 * Returns a Run object configured for the current row, or null if the current row is invalid.
		 */
		public UserItem getUser() {
			if (isBeforeFirst() || isAfterLast())
				return null;
			UserItem item = new UserItem();
			item.setUserId(getString(getColumnIndex(COLUMN_USER_USER_ID)));
			item.setUserName(getString(getColumnIndex(COLUMN_USER_USER_NAME)));
			return item;
		}
	}

	public static class PhotoCursor extends CursorWrapper {

		public PhotoCursor(Cursor c) {
			super(c);
		}

		public PhotoItem getPhoto() {
			if (isBeforeFirst() || isAfterLast())
				return null;
			PhotoItem item = new PhotoItem();
			item.setPhotoId(getString(getColumnIndex(COLUMN_PHOTO_PHOTO_ID)));
			item.setPhotoName(getString(getColumnIndex(COLUMN_PHOTO_PHOTO_NAME)));
			item.setUserId(getString(getColumnIndex(COLUMN_PHOTO_USER_ID)));
			item.setUserName(getString(getColumnIndex(COLUMN_PHOTO_USER_NAME)));
			return item;
		}
	}

}
