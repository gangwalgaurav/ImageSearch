package com.codepath.gangwal.imagesearch.activities;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.codepath.gangwal.imagesearch.R;
import com.codepath.gangwal.imagesearch.adapters.ImageResultsAdapter;
import com.codepath.gangwal.imagesearch.fragments.SettingsDailogFragment;
import com.codepath.gangwal.imagesearch.helper.EndlessScrollListener;
import com.codepath.gangwal.imagesearch.helper.Utilities;
import com.codepath.gangwal.imagesearch.models.ImageResult;
import com.codepath.gangwal.imagesearch.models.SearchFilter;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity implements SettingsDailogFragment.onFilterListener{
    public String TAG = SearchActivity.class.getSimpleName();

    private StaggeredGridView gvResults;
    private ArrayList<ImageResult> imageResults;
    JSONArray imageResultJSON = null;
    ImageResultsAdapter aImageResults;
    public String mSearchQuery="";

    private static final String QUERY_ARGUMENT = "q";
    private static final String VERSION_ARGUMENT = "v";
    private static final String VERSION_VALUE = "1.0";

    private static final String RETURN_SIZE_ARGUMENT = "rsz";
    private static final int RETURN_SIZE_VALUE = 8;
    private static final String QUERY_SIZE_START_ARGUMENT = "start";


    private static final String IMAGE_SIZE_ARGUMENT = "imgsz";
    private static final String COLOR_FILTER_ARGUMENT = "imgcolor";
    private static final String IMAGE_TYPE_ARGUMENT = "imgtype";
    private static final String SITE_FILTER_ARGUMENT = "as_sitesearch";

    private static String BUNDLE_SEARCH_QUERY = "searchQuery";
    private static String BUNDLE_SEARCH_FILTER = "filters";



    private SearchFilter mFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
//        getSupportActionBar().setCustomView(R.layout.actionbar_title);
        setUpViews();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this,imageResults);
        gvResults.setEmptyView(findViewById(R.id.empty));
        gvResults.setAdapter(aImageResults);
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadMoreData(totalItemsCount);
            }
        });
    }

    private void setUpViews() {
        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, ImageDisplay.class);
                ImageResult imageInfo = imageResults.get(position);
                intent.putExtra("imageInfo",imageInfo);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchQuery = query;
                onImageSearch(null);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            showFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showFilterDialog() {
        SettingsDailogFragment dailog = SettingsDailogFragment.
                getInstance( this,getSupportFragmentManager(),mFilter);
        dailog.show(getSupportFragmentManager(),"");
    }

    public void loadMoreData(int offset)
    {

        if(mSearchQuery.isEmpty()) {
            return;
        }
        if(!Utilities.isNetworkAvailable(this)){
            Toast.makeText(this,R.string.NO_INTERNET_CONNECTION,Toast.LENGTH_SHORT).show();
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        String url = null;
        RequestParams params = new RequestParams();

        try {
            url = "https://ajax.googleapis.com/ajax/services/search/images";//?v=1.0&rsz=8&q="+ URLEncoder.encode(mSearchQuery, "UTF-8");
            params.put(QUERY_ARGUMENT, mSearchQuery);
            params.put(VERSION_ARGUMENT, VERSION_VALUE);
            params.put(RETURN_SIZE_ARGUMENT, RETURN_SIZE_VALUE);
            params.put(QUERY_SIZE_START_ARGUMENT, offset);
            if(mFilter!=null)
            {
                params.put(IMAGE_SIZE_ARGUMENT, mFilter.getImageSize());
                params.put(COLOR_FILTER_ARGUMENT, mFilter.getColorFilter());
                params.put(IMAGE_TYPE_ARGUMENT, mFilter.getImageType());
                params.put(SITE_FILTER_ARGUMENT, mFilter.getSiteFilter());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            //On Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    imageResultJSON = response.getJSONObject("responseData").getJSONArray("results");
                    aImageResults.addAll(ImageResult.fromJsonArray(imageResultJSON));

                } catch (JSONException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
//                    aImageResults.notifyDataSetChanged();
//                swipeContainer.setRefreshing(false);

            }

            //On Failure
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Error " + responseString);
                Toast.makeText(SearchActivity.this, responseString, Toast.LENGTH_LONG).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onRetry(int retryNo) {
                Toast.makeText(SearchActivity.this,
                        String.format("Request is retried, retry no. %d", retryNo),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
    public void onImageSearch(View v)
    {
        aImageResults.clear();
        loadMoreData(0);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(BUNDLE_SEARCH_QUERY, mSearchQuery);
        savedInstanceState.putSerializable(BUNDLE_SEARCH_FILTER, mFilter);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSearchQuery = savedInstanceState.getString(BUNDLE_SEARCH_QUERY);
        mFilter = (SearchFilter) savedInstanceState.getSerializable(BUNDLE_SEARCH_FILTER);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
//        gvResults.setNumColumns(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 4);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void setFilter(SearchFilter filter) {
        mFilter = filter;
        onImageSearch(null);
    }
}
