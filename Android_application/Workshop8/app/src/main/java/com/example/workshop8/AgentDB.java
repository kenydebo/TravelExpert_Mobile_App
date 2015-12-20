package com.example.workshop8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;


/*
 * Purpose: Agent class Db methods for TravelExperts Database
 * Author: Mark Poffenroth
 * Date: Sept 2015
 */
public class AgentDB {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private final String dbTableName = "agents";

    public AgentDB(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    // private methods
    private void openReadableDB() {
        database = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {  database = dbHelper.getWritableDatabase();   }

    private void closeDB() {
        if (database != null)
            database.close();
    }

    // Fill table w/ dummy data
    public void fillAgentsTableDummy() {
        Agent agent = new Agent (1, "Adam", "A", "Auch", "555-555-1001", "aa@travele.com", "Senior Agent", 1, "Active");
        Agent agent2 = new Agent (2, "Bob",  "B","Bannf", "555-555-1002", "bb@travele.com", "Senior Agent", 2, "Active");
        Agent agent3 = new Agent (3, "Carl",  "C", "Crow", "555-555-1003", "cc@travele.com", "Senior Agent", 3, "Active");
        Agent agent4 = new Agent (4, "Dennis", "D", "Dion", "555-555-1004", "dd@travele.com", "Senior Agent", 4, "Active");
        Agent agent5 = new Agent (5, "Ed",  "E", "Edwards", "555-555-1005", "ee@travele.com", "Senior Agent", 5, "Active");

        insertAgent(agent);
        insertAgent(agent2);
        insertAgent(agent3);
        insertAgent(agent4);
        insertAgent(agent5);
    }

    // Fill table w/ data
    public void fillAgentsTable(ArrayList<Agent> agentsList) {
        for (Agent temp : agentsList) {
            insertAgent(temp);
        }
    }

    // Clear table data
    public void clearAgentsTable() {
        openWriteableDB();
        database.execSQL("delete from " + dbTableName);
        this.closeDB();
    }

    // Get count of total number of records
    public int getAgentCount() {
        String countQuery = "SELECT  * FROM " + dbTableName;
        openReadableDB();
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        this.closeDB();
        return cnt;
    }

    // Get list of all Agent records in db
    public ArrayList<Agent> getAgents(String filter) {

        String where = "AgentId = ?";
        if (filter.equals("0"))
            where = "AgentId > ?";
        String[] whereArgs = { filter };

        this.openReadableDB();
        Cursor cursor = database.query("agents", null, where, whereArgs, null, null, null);

        ArrayList<Agent> agents = new ArrayList<Agent>();
        while (cursor.moveToNext()) {
            agents.add(getAgentFromCursor(cursor));
        }

        if (cursor != null)
            cursor.close();
        this.closeDB();
        return agents;
    }


    // Get list of all Agent records in db
    public ArrayList<Agent> getAgentsInAgency(String filter) {

        String where = "AgencyId = ?";
        if (filter.equals("0"))
            where = "AgencyId > ?";
        String[] whereArgs = { filter };

        this.openReadableDB();
        Cursor cursor = database.query("agents", null, where, whereArgs, null, null, null);

        ArrayList<Agent> agents = new ArrayList<Agent>();
        while (cursor.moveToNext()) {
            agents.add(getAgentFromCursor(cursor));
        }

        if (cursor != null)
            cursor.close();
        this.closeDB();
        return agents;
    }


    // Get single Agent record
    public Agent getAgent(int id) {

        String where = "AgentId= ?";
        String[] whereArgs = { Integer.toString(id) };

        this.openReadableDB();
        Cursor cursor = database.query("Agents", null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        //Log.e("webserv", "getagent cursor " + cursor.getInt(cursor.getColumnIndex("agentId")));

        Agent agent = getAgentFromCursor(cursor);
        //Log.e("webserv", "agentid in db " + agency.getAgentId());

        if (cursor != null)
            cursor.close();
        this.closeDB();
        return agent;
    }


    // Used by getAgent and getAgents
    private static Agent getAgentFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        else {
            try {
                Agent agent = new Agent(
                    // where # is index; ex 0=agentid, 7=acencyid
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getString(8));
                return agent;
            } catch (Exception e) {
                return null;
            }
        }
    }



    // Insert Agent
    public long insertAgent(Agent agent) {

        // Stores key value pairs being the column name and the data
        // ContentValues data type is needed because the database
        // requires its data type to be passed
        ContentValues values = new ContentValues();

        values.put("agtFirstName", agent.getAgtFirstName());
        values.put("agtMiddleInitial", agent.getAgtMiddleInitial());
        values.put("agtLastName", agent.getAgtLastName());
        values.put("agtBusPhone", agent.getAgtBusPhone());
        values.put("agtEmail", agent.getAgtEmail());
        values.put("agtPosition", agent.getAgtPosition());
        values.put("agencyId", agent.getAgencyId());
        values.put("agentStatus", agent.getAgencyId());

        // Open a database for reading and writing
        openWriteableDB();

        // Inserts data in form of ContentValues into table name provided
        // insert() returns row ID of newly inserted row, or -1 if  error occurred
        long rowID = database.insert("Agents", null, values);

        // Release reference to SQLiteDatabase object
        this.closeDB();

        return rowID;
    }


    // Update Agent
    public int updateAgent(Agent agent) {

        // Stores key value pairs being the column name and the data
        ContentValues values = new ContentValues();

        values.put("agtFirstName", agent.getAgtFirstName());
        values.put("agtMiddleInitial", agent.getAgtMiddleInitial());
        values.put("agtLastName", agent.getAgtLastName());
        values.put("agtBusPhone", agent.getAgtBusPhone());
        values.put("agtEmail", agent.getAgtEmail());
        values.put("agtPosition", agent.getAgtPosition());
        values.put("agencyId", agent.getAgencyId());
        values.put("agentStatus", agent.getAgencyId());

        String where = "AgentId= ?";
        String[] whereArgs = { String.valueOf(agent.getAgentId()) };

        // Open database for reading and writing
        openWriteableDB();

        // update(TableName, ContentValueForTable, WhereClause, ArgumentForWhereClause)
        //return database.update("agents", values, where, whereArgs);
        // insert() returns row ID of newly inserted row, or -1 if  error occurred
        int rowID = database.update("Agents", values, where, whereArgs);

        // Release reference to SQLiteDatabase object
        this.closeDB();

        return rowID;
    }


    // Delete Agent w/ this Id
    public int deleteAgent(String id) {

        String where = "AgentId= ?";
        String[] whereArgs = { String.valueOf(id) };
        //String query = "DELETE FROM Agents where AgentId='"+ id +"'";

        // Open database for reading and writing
        openWriteableDB();

        // Executes query
        int rowCount = database.delete("Agents:", where, whereArgs);
        this.closeDB();

        return rowCount;
    }

}
