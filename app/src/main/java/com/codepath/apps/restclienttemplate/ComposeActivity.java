package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public  static final String TAG="ComposeActivity";
    public  static final int MAX_TWEET_LENGTH=280;
    Button btnTweet;
    EditText etCompose;
    TwitterClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        //Toast.makeText(ComposeActivity.this, "here!", Toast.LENGTH_SHORT);
        client=TwitterApp.getRestClient(this);
        etCompose=findViewById(R.id.etCompose);
        btnTweet=findViewById(R.id.btnTweet);

        //Log.i("etCompose", etCompose.getText().toString());
        //Set click listener on button

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("messageunique2","here");
                String tweetContent=etCompose.getText().toString();
                Log.i("messageunique3",tweetContent);
                if (tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Tweet cannot be empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(tweetContent.length()>MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Tweet is too long!", Toast.LENGTH_LONG).show();
                    return;

                }

                Toast.makeText(ComposeActivity.this,tweetContent, Toast.LENGTH_LONG).show();
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG,"on success to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Intent intent=new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK,intent);
                            finish();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG,"on failure to publish tweet",throwable);
                    }
                });
            }
        });
        // Make an API call to Twitter to pull

    }
}
