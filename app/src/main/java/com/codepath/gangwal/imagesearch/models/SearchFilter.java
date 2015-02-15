package com.codepath.gangwal.imagesearch.models;

import java.io.Serializable;

/**
 * Created by gangwal on 2/14/15.
 */
public class SearchFilter implements Serializable {
    String mImageSize="";
    String mColorFilter="";
    String mImageType="";
    String mSiteFilter="";


    public  SearchFilter(String imageSize, String colorFilter,String imageType,String siteFilter)
    {
        mImageSize = imageSize;
        mColorFilter = colorFilter;
        mImageType = imageType;
        mSiteFilter = siteFilter;
    }
    public String getImageSize() {
        return mImageSize;
    }

    public void setImageSize(String mImageSize) {
        this.mImageSize = mImageSize;
    }

    public String getColorFilter() {
        return mColorFilter;
    }

    public void setColorFilter(String mColorFilter) {
        this.mColorFilter = mColorFilter;
    }

    public String getImageType() {
        return mImageType;
    }

    public void setImageType(String mImageType) {
        this.mImageType = mImageType;
    }

    public String getSiteFilter() {
        return mSiteFilter;
    }

    public void setSiteFilter(String mSiteFilter) {
        this.mSiteFilter = mSiteFilter;
    }



}
