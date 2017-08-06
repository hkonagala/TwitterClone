package com.example.harikakonagala.twitterclone;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.checked;

public class HomeActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("user's list");
        //users.add("harika");

        //check for isFollowing col
        if(ParseUser.getCurrentUser().get("isFollowing") == null){
            List<String> emptyList = new ArrayList<>();
            ParseUser.getCurrentUser().put("isFollowing", emptyList);
        }
        listView = (ListView) findViewById(R.id.user_list);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, users);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //check on right side
                CheckedTextView checkedTextView = (CheckedTextView) view;
                if(checkedTextView.isChecked()){

                    ParseUser.getCurrentUser().getList("isFollowing").add(users.get(position));
                    ParseUser.getCurrentUser().saveInBackground();
                    Toast.makeText(getApplicationContext(), "selected", Toast.LENGTH_LONG).show();
                }else {

                    ParseUser.getCurrentUser().getList("isFollowing").remove(users.get(position));
                    ParseUser.getCurrentUser().saveInBackground();
                    Toast.makeText(getApplicationContext(), "not selected", Toast.LENGTH_LONG).show();
                }
            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null ){
                    if(objects.size() > 0){
                        for (ParseUser object : objects){
                            users.add(object.getUsername());
                        }
                        arrayAdapter.notifyDataSetChanged();
                        //save user list
                        for(String usernames : users){
                            if(ParseUser.getCurrentUser().getList("isFollowing").contains(usernames)){
                                listView.setItemChecked(users.indexOf(usernames),true);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.tweet:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("send a tweet");
                final EditText tweetContent = new EditText(this);
                builder.setView(tweetContent);
                builder.setPositiveButton("send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getApplicationContext(), tweetContent.getText().toString(), Toast.LENGTH_LONG).show();
                        ParseObject tweet = new ParseObject("Tweet");
                        tweet.put("username", ParseUser.getCurrentUser().getUsername());
                        tweet.put("tweet", tweetContent.getText().toString());
                        tweet.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    Toast.makeText(getApplicationContext(), "tweet sent successfully!", Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), "tweet failed, please try again later!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            case R.id.feed:
                Intent intent = new Intent(HomeActivity.this, FeedActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                ParseUser.logOut();
                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
