package com.example.harikakonagala.twitterclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Build.VERSION_CODES.M;

public class FeedActivity extends AppCompatActivity {

    ListView listView;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        listView = (ListView) findViewById(R.id.feed_list);
        
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
        query.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));
        query.orderByDescending("createdAt");
        query.setLimit(20);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size() > 0){
                        List<Map<String, String>> tweetData = new ArrayList<Map<String, String>>();
                        for(ParseObject object : objects){

                                Map<String,String> tweetInfo = new HashMap<String, String>();
                                tweetInfo.put("content",object.getString("tweet"));
                                tweetInfo.put("username", object.getString("username"));
                                tweetData.add(tweetInfo);

                        }
                        adapter = new SimpleAdapter(FeedActivity.this, tweetData, android.R.layout.simple_list_item_2, new String[]{"content", "username"},
                                new int[] {android.R.id.text1, android.R.id.text2});
                        listView.setAdapter(adapter);
                    }
                }
            }
        });


    }
}
