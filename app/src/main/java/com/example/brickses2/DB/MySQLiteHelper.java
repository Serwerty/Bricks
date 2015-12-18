package com.example.brickses2.DB;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PlayerDB";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create player table
        String CREATE_PLAYER_TABLE = "CREATE TABLE players ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, "+
                "highScore INT )";

        // create players table
        db.execSQL(CREATE_PLAYER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older players table if existed
        db.execSQL("DROP TABLE IF EXISTS players");

        // create fresh players table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) player + get all players + delete all players
     */

    // players table name
    private static final String TABLE_PLAYERS = "players";

    // players Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_HIGHSCORE = "highScore";

    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_HIGHSCORE};

    public void addPlayer(Player player){
        Log.d("addPlayer", player.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, player.getName()); // get name
        values.put(KEY_HIGHSCORE, player.getHighScore()); // get highScore

        // 3. insert
        db.insert(TABLE_PLAYERS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public void deleteAllPlayers(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM players");
        db.close();
    }

    public Player getPlayer(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_PLAYERS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build player object
        Player player = new Player();
        player.setId(Integer.parseInt(cursor.getString(0)));
        player.setName(cursor.getString(1));
        player.setHighScore(cursor.getInt(2));

        Log.d("getPlayer("+id+")", player.toString());

        // 5. return player
        return player;
    }

    // Get All players
    public List<Player> getAllPlayers() {
        List<Player> players = new LinkedList<Player>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_PLAYERS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build player and add it to list
        Player player = null;
        if (cursor.moveToFirst()) {
            do {
                player = new Player();
                player.setId(Integer.parseInt(cursor.getString(0)));
                player.setName(cursor.getString(1));
                player.setHighScore(cursor.getInt(2));

                // Add player to players
                players.add(player);
            } while (cursor.moveToNext());
        }

        Log.d("getAllPlayers()", players.toString());

        // return players
        return players;
    }

    // Updating single player
    public int updatePlayer(Player player) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("name", player.getName()); // get
        values.put("highScore", player.getHighScore()); // get highScore

        // 3. updating row
        int i = db.update(TABLE_PLAYERS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(player.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single player
    public void deletePlayer(Player player) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_PLAYERS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(player.getId()) });

        // 3. close
        db.close();

        Log.d("deletePlayer", player.toString());

    }
}