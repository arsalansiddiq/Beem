package com.example.arsalansiddiq.beem.databases.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.arsalansiddiq.beem.databases.room.interfaces.DAOBeemDatabase;
import com.example.arsalansiddiq.beem.databases.room.tables.LoginInfoTable;
import com.example.arsalansiddiq.beem.databases.room.tables.SalesStatus;


@Database(entities = {LoginInfoTable.class, SalesStatus.class}, version = 2, exportSchema = false)
public abstract class BeemAppDatabase extends RoomDatabase {


    private static final String DB_NAME = "beem-database";
    private static volatile BeemAppDatabase beemAppDatabaseInstance;

    public static synchronized BeemAppDatabase getInstance(Context context) {
        if (beemAppDatabaseInstance == null) {
            beemAppDatabaseInstance = create(context);
        }
        return beemAppDatabaseInstance;
    }

    private static BeemAppDatabase create(final Context context) {
        return Room.databaseBuilder(context,
                BeemAppDatabase.class,
                DB_NAME).build();
    }

    public abstract DAOBeemDatabase daoBeemDatabase();

}
