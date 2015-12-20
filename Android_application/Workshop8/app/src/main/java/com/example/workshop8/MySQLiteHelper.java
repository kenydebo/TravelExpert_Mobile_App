package com.example.workshop8;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * Purpose: Main DB methods for TravelExperts Database
 * Author: Mark Poffenroth
 * Date: Sept 2015
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    // NOTE: device location
    // //data/data/<Your-Application-Package-Name>/databases/<your-database-name>

    // Database Version
    // NOTE: !! MUST increment this number after any change to database schema !!
    private static final int DB_VERSION = 6;

    // Database Name
    // NOTE: If AndroidManifest application activity android:label="@string/app_name" >
    // changes you will have to recreate DB for new application name
    private static final String DB_NAME = "agents.db";

    private static final String CREATE_TABLE_AGENTS =
        "CREATE TABLE agents ( " +
            " agentId INTEGER PRIMARY KEY, " +
            " agtFirstName TEXT NOT NULL, " +
            " agtMiddleInitial TEXT NOT NULL, " +
            " agtLastName TEXT NOT NULL, " +
            " agtBusPhone TEXT NOT NULL, " +
            " agtEmail TEXT NOT NULL, " +
            " agtPosition TEXT NOT NULL, " +
            " agencyId TEXT NOT NULL, " +
            " agentStatus TEXT NOT NULL)";

    private static final String CREATE_TABLE_AGENCIES =
        "CREATE TABLE agencies ( " +
            " agencyId INTEGER PRIMARY KEY, " +
            " agncyAddress TEXT NOT NULL, " +
            " agncyCity TEXT NOT NULL, " +
            " agncyProv TEXT NOT NULL, " +
            " agncyPostal TEXT NOT NULL, " +
            " agncyCountry TEXT NOT NULL, " +
            " agncyPhone TEXT NOT NULL, " +
            " agncyFax TEXT NOT NULL)";

    public MySQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        // creating required tables
        // execSQL Executes query if not a select or if query doesn't return any data
        database.execSQL(CREATE_TABLE_AGENCIES);
        database.execSQL(CREATE_TABLE_AGENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.d("Task list", "Upgrading db from version " + oldVersion + " to " + newVersion);

        // drop old table
        database.execSQL("DROP TABLE IF EXISTS agencies");
        database.execSQL("DROP TABLE IF EXISTS agents");

        // create new tables
        onCreate(database);
    }

}
