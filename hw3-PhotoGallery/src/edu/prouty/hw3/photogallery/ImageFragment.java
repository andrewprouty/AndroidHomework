package edu.prouty.hw3.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageFragment extends Fragment{
	private static final String TAG = "ImageFragment";
	private String mImageFileName;
	private PhotoItem mPhotoItem;

	View view;
	TextView mUserTextView;
	TextView mPhotoTextView;
	ImageView mImageView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		super.onCreate(savedInstanceState);

		setRetainInstance(true); // survive across Activity re-create (i.e. orientation)
		mPhotoItem=((ImageActivity) getActivity()).getPhotoItem();
		new FetchImageTask().execute(mPhotoItem);
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

		return view;
	}
	
	private void setupImage() {
		if (getActivity() == null || mImageView == null) {
			return;
		}
		// Async downloads to [cache] file, use the file
		if (mImageFileName == null) {
	        mImageView.setImageResource(R.drawable.image_not_available);
		}
		else {
			Bitmap bmImage = BitmapFactory.decodeFile("mImageFileName");
			mImageView.setImageBitmap(bmImage);
	    }
	}

	private class FetchImageTask extends AsyncTask<PhotoItem,Void,String> {
		@Override
		protected String doInBackground(PhotoItem... params) {
        	Log.d(TAG, "FetchImageTask doInBackground()");
    		String fname = null;
    		try {
    			// pass context for app dir to cache file
        		//fname = new ImageBismarck().fetchItems(mPhotoItem, getActivity().getApplicationContext());
    			Thread.sleep(1000);
    		} catch (Exception e) {
    			Log.e(TAG, "doInBackground() Exception.", e);
    		}
        	return fname;
		}
		@Override
		protected void onPostExecute(String fileName) {
			mImageFileName = fileName;
			//mUserTextView.setText("I'm back");
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