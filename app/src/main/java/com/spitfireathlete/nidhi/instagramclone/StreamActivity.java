package com.spitfireathlete.nidhi.instagramclone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class StreamActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private static final String POPULAR = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

    private List<InstagramPhoto> photos;
    private ArrayAdapter<InstagramPhoto> aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_photos);

        photos = new ArrayList<InstagramPhoto>(); // data source
        aPhotos = new InstagramPhotosAdapter(this, photos); // adapter
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos); // view
        lvPhotos.setAdapter(aPhotos);

        // send out API request to popular photos
        fetchPopularPhotos();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stream, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchPopularPhotos() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(POPULAR, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        JSONObject imageJSON = photoJSON.getJSONObject("images").getJSONObject("standard_resolution");
                        JSONObject userJSON = photoJSON.getJSONObject("user");

                        InstagramPhoto photo = new InstagramPhoto();

                        photo.setUsername(userJSON.getString("username"));
                        photo.setProfilePicture(userJSON.getString("profile_picture"));
                        photo.setCaption(photoJSON.getJSONObject("caption").getString("text"));
                        photo.setLikesCount(photoJSON.getJSONObject("likes").getInt("count"));
                        photo.setType(photoJSON.getString("type"));
                        photo.setImageURL(imageJSON.getString("url"));
                        photo.setImageHeight(imageJSON.getInt("height"));
                        photo.setCreatedTime(Long.parseLong(photoJSON.getString("created_time")));

                        photos.add(photo);

                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                aPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                //FIXME: handle request failure
            }
        });


    }
}
