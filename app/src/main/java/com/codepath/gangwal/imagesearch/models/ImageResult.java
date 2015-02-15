package com.codepath.gangwal.imagesearch.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gangwal on 2/14/15.
 */
public class ImageResult implements Serializable{

    private static final long serialVersionUID = 5177222050535318633L;

    public static String TAG = ImageResult.class.getSimpleName();
    public String fullUrl;
    public String tbUrl;
    public String title;
    public int height;
    public int width;
    public int tbWidth;
    public int tbHeight;


    public ImageResult(JSONObject json) {

        try {

            this.fullUrl = json.getString("url");
            this.tbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
            this.height = json.getInt("height");
            this.width = json.getInt("width");
            this.tbWidth = json.getInt("tbWidth");
            this.tbWidth  = json.getInt("tbWidth");

        } catch (JSONException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

    }

    public static ArrayList<ImageResult> fromJsonArray(JSONArray array) {
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();

            for (int i = 0; i < array.length(); i++) {
                try {
                    results.add(new ImageResult(array.getJSONObject(i)));
                } catch (JSONException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        return results;

    }


    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getTbUrl() {
        return tbUrl;
    }

    public void setTbUrl(String tbUrl) {
        this.tbUrl = tbUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

//    private void writeToFile(String data) {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//            Log.i(TAG, "File Written");
//        } catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }
}
