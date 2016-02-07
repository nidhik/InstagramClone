package com.spitfireathlete.nidhi.instagramclone;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by nidhikulkarni on 2/4/16.
 */

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    private static class ViewHolder {
        String type;
    }

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = this.getItem(position);

        // FIXME: do this properly with subclasses for video/photo types
        if (convertView == null || !viewIsCorrectType(convertView, photo)) {

            if (photo.getType().equals("image")) {
                convertView = inflatePhotoView(parent);
            } else {
                convertView = inflateVideoView(parent);
            }
        }

        //FIXME: do this properly with subclasses for different types of content
        if (photo.getType().equals("image")) {
            updatePhotoViews(convertView, photo);
        }
        else {
            updateVideoView(convertView, photo);
        }

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

    public boolean viewIsCorrectType(View recycleView, InstagramPhoto photo) {
        ViewHolder vh = (ViewHolder) recycleView.getTag();
        return photo.getType().equals(vh.type);
    }
    public View reinflateCorrectView(InstagramPhoto photo, ViewGroup parent) {
        if (photo.getType().equals("image")) {
            return inflatePhotoView(parent);
        } else {
            return inflateVideoView(parent);
        }
    }

    public View inflatePhotoView(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.type = "image";
        view.setTag(vh);
        return view;
    }

    public View inflateVideoView(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_video, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.type = "video";
        view.setTag(vh);
        return view;
    }

    public void updateVideoView(View convertView, InstagramPhoto photo) {

        final VideoView vvVideo =(VideoView) convertView.findViewById(R.id.vvVideo);
        vvVideo.setVideoPath(photo.getURL());

        vvVideo.getLayoutParams().height = photo.getHeight();
        vvVideo.getLayoutParams().width = photo.getWidth();


        final android.widget.MediaController mediaController = new android.widget.MediaController(getContext(), false); // false for no fast forward ability

        mediaController.hide();
        mediaController.setVisibility(View.GONE);
        mediaController.setAnchorView(vvVideo);
        vvVideo.setMediaController(mediaController);

        vvVideo.requestFocus();
        vvVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int end = mp.getDuration();
                vvVideo.seekTo(end);
                // TODO: display play button overlay

            }

        });

        vvVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                vvVideo.start();
                return vvVideo.performClick();

            }
        });


        TextView tvUsername = (TextView) convertView.findViewById(R.id.video_tvUsername);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.video_tvCaption);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.video_tvLikes);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.video_tvRelativeTime);
        ImageView ivProfilePic = (ImageView) convertView.findViewById(R.id.video_ivProfilePic);


        tvCaption.setText(photo.getCaption());
        tvUsername.setText(photo.getUsername() + "--");
        tvLikes.setText(photo.getLikesCount() + " likes");
        CharSequence relTime = DateUtils.getRelativeTimeSpanString(photo.getCreatedTime() * 1000, System.currentTimeMillis(), 0);
        tvRelativeTime.setText(relTime);

        // clear image view while we wait for new image to load
        ivProfilePic.setImageResource(0);


        Picasso.with(getContext())
                .load(photo.getProfilePicture())
                .fit()
                .transform(getCircleTransformation())
                .into(ivProfilePic);
    }

    public void updatePhotoViews(View convertView, InstagramPhoto photo) {
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
        ImageView ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);

        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);

        tvCaption.setText(photo.getCaption());
        tvUsername.setText(photo.getUsername() + "--");
        tvLikes.setText(photo.getLikesCount() + " likes");
        CharSequence relTime = DateUtils.getRelativeTimeSpanString(photo.getCreatedTime()*1000, System.currentTimeMillis(), 0);
        tvRelativeTime.setText(relTime);

        // clear image view while we wait for new image to load
        ivPhoto.setImageResource(0);
        ivProfilePic.setImageResource(0);


        Picasso.with(getContext()).load(photo.getURL()).into(ivPhoto);

        Picasso.with(getContext())
                .load(photo.getProfilePicture())
                .fit()
                .transform(getCircleTransformation())
                .into(ivProfilePic);
    }


}
