package edu.prouty.hw3.photogallery;

import android.content.Context;
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

	int fragPosition;
	FetchImageTask mFetchImageTask;

	View view;
	TextView mUserTextView;
	TextView mPhotoTextView;
	ImageView mImageView;

	public static ImageFragment init(int position) {
		ImageFragment frag = new ImageFragment();
		// set position input as an argument available onCreate
		Bundle b = new Bundle();
		b.putInt("position", position);
		frag.setArguments(b);
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setRetainInstance(true); // survive across Activity re-create (i.e. orientation)
		//fragPosition = getArguments() != null ? getArguments().getInt("position") : 1;
		mPhotoItem=((ImagePagerActivity) getActivity()).popDisplayItem();
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

		return view;
	}

	private void setupImage(PhotoItem photoItem) {
		if (getActivity() == null || mImageView == null) {
			return;
		}
		// Async downloads to [cache] file, use the file

//		mPhotoItem=((ImagePagerActivity) getActivity()).getPhotoItem();
		Log.d(TAG, "setupImage() request:"+photoItem.getPhotoId()+"-"+photoItem.getPhotoName()+";"
				+" returned: "+mPhotoItem.getPhotoId()+"-"+mPhotoItem.getPhotoName());
		if (mImageFileName == null) {
			mImageView.setImageResource(R.drawable.image_not_available);
			Log.d(TAG, "setupImage() null filename");
		}
		else {
			Log.d(TAG, "setupImage():"+mImageFileName);
			//Bitmap bmImage = BitmapFactory.decodeFile(mImageFileName);
			
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize = 3;
			Bitmap bmImage=BitmapFactory.decodeFile(mImageFileName, options);
			Log.d(TAG, "setupImage() built");
			mImageView.setImageBitmap(bmImage);
//			bmImage.recycle();
			Log.d(TAG, "setupImage() displayed");
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
			setupImage(photoItem);
			cancel(true); // done !
			//Log.d(TAG, "FetchImageTask onPostExecute()-cancel");
		}
		@Override
		protected void onCancelled() {
			Log.d(TAG, "FetchImageTask onCancelled()");
		}
	}
}
