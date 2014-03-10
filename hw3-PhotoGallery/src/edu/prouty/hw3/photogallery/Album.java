package edu.prouty.hw3.photogallery;

import java.util.ArrayList;

import android.content.Context;

public class Album {


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
}
