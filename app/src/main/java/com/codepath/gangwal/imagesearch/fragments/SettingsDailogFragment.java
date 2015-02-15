package com.codepath.gangwal.imagesearch.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.gangwal.imagesearch.R;
import com.codepath.gangwal.imagesearch.models.SearchFilter;

/**
 * Created by gangwal on 2/14/15.
 */
public class SettingsDailogFragment extends DialogFragment{

    static FragmentManager sFragmentManager;
    private Spinner spImageSize;
    private Spinner spColorFilter;
    private Spinner spImageType;
    private EditText etSiteFilter;
    private Button btSave;
    private Button btCancel;
    private static SearchFilter mFilter;
    private onFilterListener mCallback;
    private static Context mContext;

    public interface onFilterListener {
        public void setFilter(SearchFilter filter);
    }

    public static SettingsDailogFragment getInstance(Context context,FragmentManager fragmentManager,SearchFilter filter) {
        mContext = context;
        mFilter = filter;
        sFragmentManager = fragmentManager;
        return new SettingsDailogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_dialog, container, false);
        getDialog().setTitle(getString(R.string.advanced_filer));
        setUpViews(view);
        return view;
    }

    private void setUpViews(View view) {
        spImageSize = (Spinner) view.findViewById(R.id.spImageSize);
        spColorFilter= (Spinner) view.findViewById(R.id.spColorFilter);
        spImageType = (Spinner) view.findViewById(R.id.spImageType);
        etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);
        btSave = (Button)view.findViewById(R.id.btSave);



        if(mFilter!=null)
        {
            spImageSize.setSelection(((ArrayAdapter)spImageSize.getAdapter()).getPosition(mFilter.getImageSize()));
            spColorFilter.setSelection(((ArrayAdapter)spColorFilter.getAdapter()).getPosition(mFilter.getColorFilter()));
            spImageType.setSelection(((ArrayAdapter)spImageType.getAdapter()).getPosition(mFilter.getImageType()));
            etSiteFilter.setText(mFilter.getSiteFilter());

        }

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave(view);
            }
        });

        btCancel = (Button)view.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsDailogFragment.this.dismiss();
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (onFilterListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onFilterListener");
        }
    }

    private void onSave(View view) {
            //TODO check for Callback
            if(mCallback !=null)
            {
                String imageSize = spImageSize.getSelectedItem().toString();
                String colorFilter = spColorFilter.getSelectedItem().toString();
                String imageType = spImageType.getSelectedItem().toString();
                String siteFilter = etSiteFilter.getText().toString().trim();

                mFilter = new SearchFilter(imageSize,colorFilter,imageType,siteFilter);
                mCallback.setFilter(mFilter);
            }
        this.dismiss();


    }

}
