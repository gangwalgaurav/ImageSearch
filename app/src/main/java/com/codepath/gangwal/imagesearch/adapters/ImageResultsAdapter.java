package com.codepath.gangwal.imagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.gangwal.imagesearch.R;
import com.codepath.gangwal.imagesearch.models.ImageResult;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gangwal on 2/14/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    ImageResult imageInfo;
    private class ViewHolder{
        DynamicHeightImageView ivImage;
        TextView tvTitle;

    }


    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context, android.R.layout.simple_list_item_1, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        imageInfo = getItem(position);
        ViewHolder viewHolder;
        if(convertView==null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result,parent,false);
            viewHolder.ivImage = (DynamicHeightImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.tvTitle=(TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        }  else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tvTitle.setText(Html.fromHtml(imageInfo.title));
        double positionHeight = getPositionRatio(position);
        viewHolder.ivImage.setHeightRatio(positionHeight);

        viewHolder.ivImage.setImageResource(0);

        Picasso.with(getContext())
                .load(imageInfo.tbUrl)
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.ivImage);



        return convertView;
    }
    private double getPositionRatio(final int position) {
        ImageResult result = getItem(position);
        return (Double.parseDouble(String.valueOf(result.height)) / Double.parseDouble(String.valueOf(result.width)));
    }
}

