package com.example.harikakonagala.twitterclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by Harika Konagala on 8/6/2017.
 */

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("c1214dcf0b285a7252f6822c9c82bcc9a55a8870")
                .clientKey( "6a3a56ac1048b14d2c52ce4856e5326031719c97")
                .server("http://ec2-18-220-106-173.us-east-2.compute.amazonaws.com:80/parse/")
                .build()
        );


        //disabling automaticUser gives us the control over sign up and login in parse server
        //ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
