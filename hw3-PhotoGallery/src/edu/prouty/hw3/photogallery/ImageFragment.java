package edu.prouty.hw3.photogallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageFragment extends Fragment{
    public static final String EXTRA_PHOTO_ID = "photogallery.PHOTO_ID";
    private static final String TAG = "ImageFragment";
	//private ArrayList<UserItem> mUserItems; // TODO part of test
	private String mImageFileName;
	private PhotoItem mPhotoItem;

	FetchImageTask mFetchImageTask;

	View view;
	TextView mUserTextView;
	TextView mPhotoTextView;
	ImageView mImageView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		super.onCreate(savedInstanceState);

		setRetainInstance(true); // survive across Activity re-create (i.e. orientation)

		mPhotoItem=((ImagePagerActivity) getActivity()).getPhotoItem();

		mFetchImageTask = new FetchImageTask(mPhotoItem);
		mFetchImageTask.execute();
	}

	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{       
		Log.d(TAG, "onCreateView()");
		view = inflater.inflate(R.layout.fragment_image, container,false);
		
		mUserTextView = (TextView)view.findViewById(R.id.image_user_name);
		mPhotoTextView = (TextView)view.findViewById(R.id.image_photo_name);
		mImageView = (ImageView)view.findViewById(R.id.image_imageView);

		mUserTextView.setText(mPhotoItem.getUserName());
		mPhotoTextView.setText(mPhotoItem.getPhotoName());
        mImageView.setImageResource(R.drawable.image_pending);

    	ViewPager pager;
    	pager = (ViewPager)view.findViewById(R.id.viewPager);
    	pager.setCurrentItem(((ImagePagerActivity) getActivity()).getPosition());

		return view;
	}
	
	private void setupImage() {
		if (getActivity() == null || mImageView == null) {
			return;
		}
		// Async downloads to [cache] file, use the file
		if (mImageFileName == null) {
	        mImageView.setImageResource(R.drawable.image_not_available);
			Log.d(TAG, "setupImage() null filename");
		}
		else {
			Bitmap bmImage = BitmapFactory.decodeFile(mImageFileName);
			mImageView.setImageBitmap(bmImage);
			Log.d(TAG, "setupImage():"+mImageFileName);
	    }
	}

	private class FetchImageTask extends AsyncTask<PhotoItem,Void,String> {
		
		private Context c;
		private PhotoItem photoItem;
		//Constructor
		public FetchImageTask (PhotoItem photoItem) {
			this.c = getActivity().getApplicationContext();
			this.photoItem = photoItem;
		}
		@Override
		protected void onPreExecute() {
		    super.onPreExecute();
		}
		@Override
		protected String doInBackground(PhotoItem... params) {
        	Log.d(TAG, "FetchImageTask doInBackground()");
    		String fname = null;
    		try {
        		fname = new ImageBismarck().fetchImage(photoItem, c);
    			
    		} catch (Exception e) {
    			Log.e(TAG, "doInBackground() Exception.", e);
    		}
        	return fname;
		}
		@Override
		protected void onPostExecute(String fName) {
			if (fName == null) {
				mImageFileName = "";
			}
			else {
				mImageFileName = c.getFilesDir().getPath()+"/"+fName;
			}
			setupImage();
            cancel(true); // done !
        	Log.d(TAG, "FetchImageTask onPostExecute()-cancel");
		}
        @Override
        protected void onCancelled() {
        	Log.d(TAG, "FetchImageTask onCancelled()");
        }
	}
}
