package edu.prouty.hw3.photogallery;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UploadFileActivity extends Activity {

	private static final String TAG = "UploadFileActivity";
	private static int RESULT_LOAD_IMAGE = 1;
	private EditText mFileNameEditText;
	private Button mButtonUploadFile;
	private String mPicturePath;
	private String mPictureName;
	FileUploadTask mFileUploadTask;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_file);


		mFileNameEditText = (EditText)findViewById(R.id.upload_file_name_editText);
		mFileNameEditText.setEnabled(false);

		Button buttonViewGallery = (Button) findViewById(R.id.buttonViewGallery);
		buttonViewGallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		mButtonUploadFile = (Button) findViewById(R.id.buttonUploadFile);
		mButtonUploadFile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String fname = mFileNameEditText.getText().toString();
				if (fname == null || fname.length() == 0) {
					fname = mPictureName;
				}
				startFileUpload(fname);
			}
		});

		mButtonUploadFile.setEnabled(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			mPicturePath = cursor.getString(columnIndex);
			cursor.close();
			Log.d(TAG,"onActivityResult() mPicturePath: "+mPicturePath);  //contains the path of selected Image

			ImageView imageView = (ImageView) findViewById(R.id.upload_imageView);

			Bitmap bmImage;
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize = 3;
			bmImage=BitmapFactory.decodeFile(mPicturePath, options);
			imageView.setImageBitmap(bmImage);

			String plus = mPicturePath.substring(mPicturePath.lastIndexOf('/')+1, mPicturePath.length() );
			mPictureName = plus.substring(0, plus.lastIndexOf('.')); // no extension

			Log.d(TAG,"onActivityResult() name.ext: "+plus);
			Log.d(TAG,"onActivityResult() name: "+mPictureName);

			mButtonUploadFile.setEnabled(true);
			mFileNameEditText.setEnabled(true);
			mFileNameEditText.setText(mPictureName);
		}
	}
	private void resultToast(Boolean success, String msg) {

		int messageResId = 0;
		if (success) {
			messageResId = R.string.upload_success;
		} else {
			messageResId = R.string.upload_failure;
		}
		Log.d(TAG,"resultToast() messageResId "+messageResId);
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
	}
	private void startFileUpload(String fname) {
		mButtonUploadFile.setEnabled(false);
		mFileNameEditText.setEnabled(false);

		Log.d(TAG,"startFileUpload() Tasking...");
		mFileUploadTask = new FileUploadTask(mPicturePath, fname);
		mFileUploadTask.execute(); //arg for doInBackground()
		Log.d(TAG,"startFileUpload() Task launched");
	}

	private class FileUploadTask extends AsyncTask<String,Void,String> {
		//<x,y,z> params: 1-doInBackground(x); 2-onProgressUpdate(y); 3-onPostExecute(z) 
		private String fileWithPath;
		private String fname;
		//Constructor
		public FileUploadTask (String path, String fname) {
			this.fileWithPath = path;
			this.fname = fname;
		}
		@Override
		protected String doInBackground(String... params) {
			Log.d(TAG, "FileUploadTask doInBackground(): "+fileWithPath);
			String msg = null;
			try {
				msg = FileUpload.upload(fileWithPath, fname); 
			} catch (Exception e) {
				Log.e(TAG, "FileUploadTask() Exception.", e);
			}
			return msg;
		}
		@Override
		protected void onPostExecute(String result) {
			resultToast(!result.startsWith("Exception"), result);
			cancel(true);
			Log.d(TAG, "FileUploadTask onPostExecute()");
		}
	}

}
