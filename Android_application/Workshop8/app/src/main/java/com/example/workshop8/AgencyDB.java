package com.example.workshop8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/*
 * Purpose: Agency Db methods for TravelExperts Database
 * Author: Mark Poffenroth
 * Date: Sept 2015
 */
public class AgencyDB {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private final String dbTableName = "agencies";

    public AgencyDB(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }


    // private methods
    private void openReadableDB() { database = dbHelper.getReadableDatabase();    }

    private void openWriteableDB() { database = dbHelper.getWritableDatabase();     }

    private void closeDB() {
        if (database != null)
            database.close();
    }


    // Fill table w/ dummy data
    public void fillAgenciesTableDummy() {
        Agency agency = new Agency (1, "1 St", "Bannf", "AB", "T2T2T2", "Canada", "555-555-1001", "555-555-2001");
        Agency agency2 = new Agency (2, "2 St", "Bannf", "AB", "T2T2T2", "Canada", "555-555-1002", "555-555-2002");
        Agency agency3 = new Agency (3, "3 St", "Bannf", "AB", "T2T2T2", "Canada", "555-555-1003", "555-555-2003");
        Agency agency4 = new Agency (4, "4 St", "Bannf", "AB", "T2T2T2", "Canada", "555-555-1004", "555-555-2004");
        Agency agency5 = new Agency (5, "5 St", "Bannf", "AB", "T2T2T2", "Canada", "555-555-1005", "555-555-2005");

        insertAgency(agency);
        insertAgency(agency2);
        insertAgency(agency3);
        insertAgency(agency4);
        insertAgency(agency5);
    }

    // Fill table w/ data
    public void fillAgenciesTable(ArrayList<Agency> agenciesList) {
        for (Agency temp : agenciesList) {
            long id = insertAgency(temp);
        }
    }

    // Clear table data
    public void clearAgenciesTable() {
        openWriteableDB();
        database.execSQL("delete from " + dbTableName);
        this.closeDB();
    }

    // Get count of total number of records
    public int getAgencyCount() {
        String countQuery = "SELECT  * FROM " + dbTableName;
        openReadableDB();
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        this.closeDB();
        return cnt;
    }


    // Get list of all Agency records in db
    public ArrayList<Agency> getAgencies() {

        this.openReadableDB();
        Cursor cursor = database.query("agencies", null, null, null, null, null, null);

        ArrayList<Agency> agencies = new ArrayList<Agency>();
        while (cursor.moveToNext()) {
            agencies.add(getAgencyFromCursor(cursor));
        }

        if (cursor != null)
            cursor.close();
        this.closeDB();
        return agencies;
    }


    // Get single Agency record
    public Agency getAgency(String id) {

        String where = "agencyId = ?";
        String[] whereArgs = { id };
        openReadableDB();
        Cursor cursor = database.query("agencies", null, where, whereArgs, null, null, null);

        //String countQuery = "SELECT  * FROM agencies WHERE agencyId > 2 "; // + id;
        //SQLiteDatabase db = this.getReadableDatabase();
        //openReadableDB();
        //Cursor cursor = database.rawQuery(countQuery, null);

        cursor.moveToFirst();
        //Log.e("webserv", "getagency cursor " + cursor.getInt(cursor.getColumnIndex("agencyId")));

        Agency agency = getAgencyFromCursor(cursor);
        //Log.e("webserv", "agencyid in db " + agency.getAgencyId());

        if (cursor != null)
            cursor.close();
        this.closeDB();

        return agency;
    }


    // Used by getAgency and getAgencies
    private static Agency getAgencyFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        else {
            try {
                Agency agency = new Agency(
                    // where # is index; ex 0=agencyid, 7=acncyFax
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7));
                return agency;
            } catch (Exception e) {
                return null;
            }
        }
    }


    // Insert Agency
    public long insertAgency(Agency agency) {

        // Stores key value pairs being the column name and the data
        // ContentValues data type is needed because the database
        // requires its data type to be passed
        ContentValues values = new ContentValues();

        values.put("agncyAddress", agency.getAgncyAddress());
        values.put("agncyCity", agency.getAgncyCity());
        values.put("agncyProv", agency.getAgncyProv());
        values.put("agncyPostal", agency.getAgncyPostal());
        values.put("agncyCountry", agency.getAgncyCountry());
        values.put("agncyPhone", agency.getAgncyPhone());
        values.put("agncyFax", agency.getAgncyFax());

        // Open a database for reading and writing
        openWriteableDB();

        // Inserts data in form of ContentValues into table name provided
        // insert() returns row ID of newly inserted row, or -1 if  error occurred
        long rowID = database.insert("agencies", null, values);

        // Release reference to SQLiteDatabase object
        this.closeDB();

        return rowID;
    }


    // Update Agency
    public int updateAgency(Agency agency) {

        // Stores key value pairs being the column name and the data
        ContentValues values = new ContentValues();

        values.put("agncyAddress", agency.getAgncyAddress());
        values.put("agncyCity", agency.getAgncyCity());
        values.put("agncyProv", agency.getAgncyProv());
        values.put("agncyPostal", agency.getAgncyPostal());
        values.put("agncyCountry", agency.getAgncyCountry());
        values.put("agncyPhone", agency.getAgncyPhone());
        values.put("agncyFax", agency.getAgncyFax());

        String where = "agencyId= ?";
        String[] whereArgs = { String.valueOf(agency.getAgencyId()) };

        // Open database for reading and writing
        openWriteableDB();

        // insert() returns row ID of newly inserted row, or -1 if  error occurred
        int rowID = database.update("agencies", values, where, whereArgs);

        // Release reference to SQLiteDatabase object
        this.closeDB();

        return rowID;
    }


    // Delete Agency w/ this Id
    public int deleteAgency(String id) {

        String where = "agencyId= ?";
        String[] whereArgs = { String.valueOf(id) };

        // Open database for reading and writing
        openWriteableDB();

        // Executes query
        int rowCount = database.delete("agencies:", where, whereArgs);
        this.closeDB();

        return rowCount;
    }

}
