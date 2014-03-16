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
import android.widget.ImageView;

public class ImageBismarck {
	private static final String TAG = "ImageBismarck";
	// http://bismarck.sdsu.edu/photoserver/userphotos/2
	private static final String ENDPOINT = "http://bismarck.sdsu.edu/photoserver/photo/";
	private Context context;
	private int width;
	private int height;

	public String fetchImage(PhotoItem photoItem, Context appContext, int width, int height) {
		Log.d(TAG, "fetchImage()");
		context = appContext; // Sets class variable
		String fName = "Image-"+photoItem.getPhotoId()+".jpg";
		String sURL = Uri.parse(ENDPOINT).toString() + photoItem.getPhotoId();
		this.width = width;
		this.height = height;
		try {
			if (!isCachedImage(fName)) {
				if(!GETImage(sURL, fName)) {
					fName = null;
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "fetchImage() Exc: "+e.getMessage(),e);
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
				return false;
			}
			else {
				Log.i(TAG, "GETImage YES: "+fName);
			}
		} catch (IOException e) {
			Log.e(TAG, "GETImage() IOException: "+ e.getMessage(), e);
			return false;
		} catch (Exception e) {
			Log.e(TAG, "GETImage() Exc: " +e.getMessage(), e);
			return false;
		}
		// #2 - save to cache (file)... yes must be space
		if (!cacheImage(fName, mBitmap)) {
			return false;
		}
		return true;
	}
	public Boolean cacheImage (String fName, Bitmap photo){
		FileOutputStream cFile;
	    try {
	    	cFile =context.openFileOutput(fName, Context.MODE_PRIVATE);
   			photo.compress(Bitmap.CompressFormat.JPEG,50,cFile);
   			cFile.flush();
	    	cFile.close();
	    } catch (IOException e) {
	        Log.e(TAG, "cacheImage() IOException: "+e.getMessage(), e);
	        return null;
	    } catch (Exception e) {
	        Log.e(TAG, "cacheImage() Exc: "+e.getMessage(), e);
	        return null;
	    }
		Log.i(TAG, "cacheImage():" +context.getFileStreamPath(fName));
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
			//Log.d(TAG, "getBitmapFromUrl before");
	        //BitmapFactory.decodeStream(input); //TODO scale this too
			//Log.d(TAG, "getBitmapFromUrl still alive");
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
			//Log.d(TAG, "getBitmapFromUrl now?");

	        return myBitmap;

	    } catch (IOException e) {
	        Log.e(TAG, "getBmpFromUrl() IOException: "+e.getMessage(), e);
	    } catch (OutOfMemoryError e) {
	        Log.e(TAG, "getBmpFromUrl() OutOfMemory: "+e.getMessage(), e);
	    } catch (Exception e) {
	        Log.e(TAG, "getBmpFromUrl() Exc: "+e.getMessage(), e);
	    }
        Log.e("getBmpFromUrl url: ", urlSpec);
        return null;
	}
	/*
	private Bitmap decodeBitmapFromStream(String fullURL, ImageView imageView) {
		// Based upon http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
		// First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fullURL, options); // To set options width & height
        
        // Calculate inSampleSize
        // Raw height and width of image
        final int width = options.outWidth;
        final int height = options.outHeight;
        final int reqWidth = this.width;
        final int reqHeight = this.height;
        Log.i(TAG,"decodeBitmapFromFilename() field w="+reqWidth+" x h="+reqHeight);

        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            long totalPixels = width * height / inSampleSize;

            // Anything more than 2x the requested pixels we'll sample down further
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Log.i(TAG,"decodeBitmapFromFilename() pixels w="+width+" x h="+height+"=> sample: < "+inSampleSize+" >");
        return BitmapFactory.decodeFile(fullURL, options);
	} */

}