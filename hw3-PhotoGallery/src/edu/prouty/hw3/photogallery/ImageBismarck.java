package edu.prouty.hw3.photogallery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class ImageBismarck {
	private static final String TAG = "ImageBismarck";
	// http://bismarck.sdsu.edu/photoserver/userphotos/2
	private static final String ENDPOINT = "http://bismarck.sdsu.edu/photoserver/photo/";
	private Context context;

	public String fetchImage(PhotoItem photoItem, Context appContext) {
		Log.d(TAG, "fetchImage()");
		context = appContext; // Sets class variable
		String fName = "Image-"+photoItem.getPhotoId()+".jpg";
		String sURL = Uri.parse(ENDPOINT).toString() + photoItem.getPhotoId();
		try {
			if (!isCachedImage(fName)) {
				if(!GETImage(sURL, fName)) {
					fName = null;
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "fetchImage() Exc:"+e.getMessage(),e);
		}
		return fName;
	}

	private Boolean isCachedImage(String fName) {
		// does the file exist? - true/false
		
		String filePath = context.getFilesDir().getPath()+"/"+fName;
		File file = new File(filePath);
		if(file.exists()) {
			Log.i(TAG, "isCachedImage() YES: "+fName);
			return true;
		}
		else	{
			return false;
		}
	}
	private Boolean GETImage(String sURL, String fName) {
		Bitmap mBitmap;
		try { // #1- download
			mBitmap = getBitmapFromUrl(sURL);
			if (mBitmap == null) {
				Log.d(TAG, "GETImage null");
			}
			else {
				Log.i(TAG, "GETImage YES: "+fName);
			}
			
		} catch (IOException ioe) {
			Log.e(TAG, "GETImage() IOException.", ioe);
			return false;
		}
		catch (Exception e) {
			Log.e(TAG, "GETImage() Exc:"+e.getMessage(),e);
			return false;
		}
		// #2 - save to cache (file)... yes must be space
		if (!cacheImage(fName, mBitmap)) {
			return false;
		}
		return true;
	}
	public Bitmap getBitmapFromUrl (String urlSpec) throws IOException {
	    try {
	        URL url = new URL(urlSpec);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);

	        return myBitmap;

	    } catch (IOException e) {
	        Log.e("getBmpFromUrl error: ", e.getMessage().toString());
	        return null;
	    }
	}
	public Boolean cacheImage (String fName, Bitmap photo){
		FileOutputStream cFile;
	    try {
	    	cFile =context.openFileOutput(fName, Context.MODE_PRIVATE);
   			photo.compress(Bitmap.CompressFormat.JPEG,50,cFile);
   			cFile.flush();
	    	cFile.close();
	    } catch (IOException e) {
	        Log.e("cacheImage: ", e.getMessage().toString());
	        return null;
	    }
		Log.i(TAG, "cacheImage():" +context.getFileStreamPath(fName));
    	return true;
	}
}