package edu.prouty.hw3.photogallery;

import android.support.v4.app.Fragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {
	@Override
	public Fragment createFragment() {
		return new PhotoGalleryFragment();
	}
}
