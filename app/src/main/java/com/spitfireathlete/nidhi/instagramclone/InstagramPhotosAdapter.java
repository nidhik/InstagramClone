package com.spitfireathlete.nidhi.instagramclone;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by nidhikulkarni on 2/4/16.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {


    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = this.getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ImageView ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);


        tvCaption.setText(photo.getCaption());
        tvUsername.setText(photo.getUsername() + "--");
        tvLikes.setText(photo.getLikesCount() + " likes");
        // clear image view while we wait for new image to load
        ivPhoto.setImageResource(0);
        ivPhoto.setImageResource(0);


        Picasso.with(getContext()).load(photo.getImageURL()).into(ivPhoto);

        Picasso.with(getContext())
                .load(photo.getProfilePicture())
                .fit()
                .transform(getCircleTransformation())
                .into(ivProfilePic);

        return convertView;
    }

    private Transformation getCircleTransformation() {
        return new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(false)
                .build();


    }
}
