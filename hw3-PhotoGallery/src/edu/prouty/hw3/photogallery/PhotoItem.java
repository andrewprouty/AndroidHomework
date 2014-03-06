package edu.prouty.hw3.photogallery;

public class PhotoItem {
    private String mPhotoName;
    private String mPhotoId;
    private String mUserId;
    private String mUrl;
    /// TODO: obsolete/delete extra data elements
    private String mCaption;
    private String mId;

    public String getCaption() {
        return mCaption;
    }
    public void setCaption(String caption) {
        mCaption = caption;
    }
    public String getId() {
        return mId;
    }
    public void setId(String id) {
        mId = id;
    }
    
    public String getPhotoName() {
        return mPhotoName;
    }
    public void setPhotoName(String photoName) {
        mPhotoName = photoName;
    }
    
    public String getPhotoId() {
        return mPhotoId;
    }
    public void setPhotoId(String PhotoId) {
        mPhotoId = PhotoId;
    }
    
    public String getUserId() {
        return mUserId;
    }
    public void setUserId(String userId) {
        mUserId = userId;
    }
    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String toString() {
        return mCaption;
    }
}
