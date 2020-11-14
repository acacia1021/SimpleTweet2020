package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    EditText etCompose;
    Button btnTweet;
    public static final int MAX_TWEET_LENGTH = 140;
    public static final String TAG = "ComposeActivity";
    TextView tvCharacters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        tvCharacters = findViewById(R.id.tvCharacters);

        tvCharacters.setText(String.valueOf(0) + "/" + String.valueOf(MAX_TWEET_LENGTH));


        final TwitterClient client;

        client = TwitterApp.getRestClient(this);

        //Set click listener on the button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();
                      if (tweetContent.isEmpty()){
                          Toast.makeText(ComposeActivity.this, "Sorry, your tweet cannot be empty", Toast.LENGTH_LONG).show();
                          return;
                      }
                      if (tweetContent.length() > MAX_TWEET_LENGTH){
                          Toast.makeText(ComposeActivity.this, "Sorry your tweet is too long", Toast.LENGTH_LONG).show();
                          return;
                      }
                      Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();
                //Make an API call to Twitter to publish the tweet
client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
    @Override
    public void onSuccess(int statusCode, Headers headers, JSON json) {
        //Difference between Log.e and Log.i is that i is on the info level, while e is on the error level
        Log.i(TAG, "onSuccess to publish tweet");
        try {
            Tweet tweet = Tweet.fromJson(json.jsonObject);
            //Make sure you add tweet.body and not just tweet, so that the content of the tweet is published, and not just the model
            Log.i(TAG, "Published tweet says: " + tweet.body);
            Intent intent = new Intent();
            intent.putExtra("tweet", Parcels.wrap(tweet));
            //Set the result code and bundle data for response
            setResult(RESULT_OK, intent);
            //closes the activity, passes the data on to the parent
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
Log.e(TAG, "onFailure to publish tweet", throwable);
    }
});
            }
        });

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = s.toString().length();
                Log.i("textCounter", String.valueOf(count));
                tvCharacters.setText(String.valueOf(count) + "/" + String.valueOf(MAX_TWEET_LENGTH));
                if (count > MAX_TWEET_LENGTH) {
                    tvCharacters.setTextColor(-65000);
                }

            }
        });

    }


}