package com.example.arsalansiddiq.beem.databases;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by jellani on 9/1/2018.
 */

public class RealmApplicationInstance extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("mybeemdatabase.realm")
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
