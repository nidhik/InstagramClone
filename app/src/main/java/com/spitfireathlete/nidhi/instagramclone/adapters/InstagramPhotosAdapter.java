package com.spitfireathlete.nidhi.instagramclone.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.spitfireathlete.nidhi.instagramclone.models.InstagramPhoto;
import com.spitfireathlete.nidhi.instagramclone.R;
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


        final ImageView play = (ImageView) convertView.findViewById(R.id.ivPlay);
        final VideoView vvVideo =(VideoView) convertView.findViewById(R.id.vvVideo);

        vvVideo.setVideoPath(photo.getURL());

        vvVideo.getLayoutParams().height = photo.getHeight();
        vvVideo.getLayoutParams().width = photo.getWidth();


        final android.widget.MediaController mediaController = new android.widget.MediaController(getContext(), false); // false for no fast forward ability

        mediaController.hide();
        mediaController.setVisibility(View.GONE);
        mediaController.setAnchorView(vvVideo);
        vvVideo.setMediaController(mediaController);
        vvVideo.clearFocus();

        vvVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vvVideo.seekTo(mp.getDuration());
                play.setVisibility(View.VISIBLE);

            }

        });



        vvVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (vvVideo.isPlaying()) {
                    vvVideo.pause();
                    play.setVisibility(View.VISIBLE);

                } else {
                    play.setVisibility(View.GONE);
                    vvVideo.seekTo(0);
                    vvVideo.start();
                }

                return vvVideo.performClick();
            }
        });


        TextView tvProfileUsername = (TextView) convertView.findViewById(R.id.video_tvProfileUsername);
        TextView tvCaptionAndUsername = (TextView) convertView.findViewById(R.id.video_tvCaptionAndUsername);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.video_tvLikes);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.video_tvRelativeTime);
        ImageView ivProfilePic = (ImageView) convertView.findViewById(R.id.video_ivProfilePic);


        tvCaptionAndUsername.setText(getStyledCaption(photo));
        tvProfileUsername.setText(photo.getUsername());
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

        TextView tvProfileUsername = (TextView) convertView.findViewById(R.id.tvProfileUsername);
        TextView tvCaptionAndUsername = (TextView) convertView.findViewById(R.id.tvCaptionAndUsername);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
        ImageView ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);

        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);


        tvCaptionAndUsername.setText(getStyledCaption(photo));
        tvProfileUsername.setText(photo.getUsername());
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

    private CharSequence getStyledCaption(InstagramPhoto photo) {
        SpannableStringBuilder captionBuidler = new SpannableStringBuilder();
        captionBuidler.append(photo.getUsername());
        captionBuidler.setSpan(new StyleSpan(Typeface.BOLD), 0, captionBuidler.length(), 0);
        captionBuidler.setSpan(new ForegroundColorSpan(Color.argb(255, 18, 86, 136)), 0, captionBuidler.length(), 0);
        captionBuidler.append(" ");
        captionBuidler.append(photo.getCaption());
        return captionBuidler;
    }


}
