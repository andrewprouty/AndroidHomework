package edu.prouty.hw3.photogallery;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {


	private ArrayList<PhotoItem> mPhotos;

	private static Album sAlbum;

	private Album (Context appContext) {
		mPhotos = new ArrayList<PhotoItem>();
		/*for (int i = 0; i < 100; i++) {
			PhotoItem c = new PhotoItem();
			c.setTitle("Crime #" + i);
			c.setSolved(i % 2 == 0); // every other one
			mCrimes.add(c);
		}*/
	}
	public static final Parcelable.Creator<Album> CREATOR
		= new Parcelable.Creator<Album>() {
		public Album createFromParcel(Parcel in) {
			return new Album(in);
		}

		public Album[] newArray(int size) {
			return new Album[size];
		}
	};
	
	private Album (Parcel in) {
		;
	}
	public static Album get(Context c) {
		if (sAlbum == null) {
			sAlbum = new Album(c.getApplicationContext());
		}
		return sAlbum;
	}

	public PhotoItem getPhotoItem(String id) {
		for (PhotoItem p : mPhotos) {
			if (p.getPhotoId().equals(id))
				return p;
		}
		return null;
	}

	public ArrayList<PhotoItem> getPhotoItems() {
		return mPhotos;
	}
	
	public void loadAlbum(ArrayList<PhotoItem> items) {
		mPhotos = items;
		return;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
