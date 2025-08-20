package com.example.torneouniversitario.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.torneouniversitario.models.Team;
import com.example.torneouniversitario.models.Player;
import com.example.torneouniversitario.models.MatchModel;
import com.example.torneouniversitario.models.EventModel;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "torneosuppue.db"; //Cambie el nombre de mi DB a uppue en lugar de upp (Por si da fallo)
    private static final int DB_VERSION = 1;

    // Tablas
    public static final String TABLE_USERS = "users";
    public static final String TABLE_TEAMS = "teams";
    public static final String TABLE_PLAYERS = "players";
    public static final String TABLE_MATCHES = "matches";
    public static final String TABLE_EVENTS = "events";
    public static final String TABLE_SPORTS = "sports";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT, role TEXT, team_id INTEGER)";
        String createTeams = "CREATE TABLE " + TABLE_TEAMS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, sport TEXT)";
        String createPlayers = "CREATE TABLE " + TABLE_PLAYERS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, position TEXT, number INTEGER, team_id INTEGER)";
        String createMatches = "CREATE TABLE " + TABLE_MATCHES + " (id INTEGER PRIMARY KEY AUTOINCREMENT, teamA_id INTEGER, teamB_id INTEGER, date TEXT, time TEXT, place TEXT)";
        String createEvents = "CREATE TABLE " + TABLE_EVENTS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, match_id INTEGER, player_id INTEGER, type TEXT, minute INTEGER, team_id INTEGER)";
        String createSports = "CREATE TABLE " + TABLE_SPORTS + "(id INTEGER PRIMARY KEY AUTOINCREMENT, sport TEXT)";

        db.execSQL(createUsers);
        db.execSQL(createTeams);
        db.execSQL(createPlayers);
        db.execSQL(createMatches);
        db.execSQL(createEvents);
        db.execSQL(createSports);

        // Insert demo user: admin / referee / player
        ContentValues admin = new ContentValues();
        admin.put("email", "admin@uppuebla.edu.mx");
        admin.put("password", "Admin123");
        admin.put("role", "ADMIN");
        db.insert(TABLE_USERS, null, admin);

        ContentValues ref = new ContentValues();
        ref.put("email", "arbitro@uppuebla.edu.mx");
        ref.put("password", "Arbi_123");
        ref.put("role", "REFEREE");
        db.insert(TABLE_USERS, null, ref);

        ContentValues sports = new ContentValues();
        sports.put("sport", "Fútbol");
        sports.put("sport", "Baloncesto");
        sports.put("sport", "Voleibol");
        db.insert(TABLE_SPORTS, null, sports);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // --- Users ---
    public String login(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{"role"},
                "email = ? AND password = ?", new String[]{email, password},
                null, null, null);
        String role = null;
        if (c.moveToFirst()) role = c.getString(0);
        c.close();
        return role;
    }

    // --- Teams CRUD ---
    public long insertTeam(Team t) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", t.getName());
        cv.put("sport", t.getSport());
        return db.insert(TABLE_TEAMS, null, cv);
    }

    public List<Team> getAllTeams() {
        List<Team> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, name, sport FROM " + TABLE_TEAMS + " ORDER BY name", null);
        while (c.moveToNext()) {
            Team t = new Team(c.getInt(0), c.getString(1), c.getString(2));
            list.add(t);
        }
        c.close();
        return list;
    }

    public int updateTeam(Team t) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", t.getName());
        cv.put("sport", t.getSport());
        return db.update(TABLE_TEAMS, cv, "id = ?", new String[]{String.valueOf(t.getId())});
    }

    public int deleteTeam(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_TEAMS, "id = ?", new String[]{String.valueOf(id)});
    }

    // --- Players CRUD (simplified) ---
    public long insertPlayer(Player p) {
        ContentValues cv = new ContentValues();
        cv.put("name", p.getName());
        cv.put("position", p.getPosition());
        cv.put("number", p.getNumber());
        cv.put("team_id", p.getTeamId());
        return getWritableDatabase().insert(TABLE_PLAYERS, null, cv);
    }

    public List<Player> getPlayersByTeam(int teamId) {
        List<Player> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT id,name,position,number,team_id FROM " + TABLE_PLAYERS + " WHERE team_id=?", new String[]{String.valueOf(teamId)});
        while (c.moveToNext()) {
            list.add(new Player(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4)));
        }
        c.close();
        return list;
    }

    // --- Matches CRUD (simplified) ---
    public long insertMatch(MatchModel m) {
        ContentValues cv = new ContentValues();
        cv.put("teamA_id", m.getTeamAId());
        cv.put("teamB_id", m.getTeamBId());
        cv.put("date", m.getDate());
        cv.put("time", m.getTime());
        cv.put("place", m.getPlace());
        return getWritableDatabase().insert(TABLE_MATCHES, null, cv);
    }

    public List<MatchModel> getAllMatches() {
        List<MatchModel> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT id,teamA_id,teamB_id,date,time,place FROM " + TABLE_MATCHES + " ORDER BY date, time", null);
        while (c.moveToNext()) {
            list.add(new MatchModel(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getString(4), c.getString(5)));
        }
        c.close();
        return list;
    }

    // --- Events (referee) ---
    public long insertEvent(EventModel e) {
        ContentValues cv = new ContentValues();
        cv.put("match_id", e.getMatchId());
        cv.put("player_id", e.getPlayerId());
        cv.put("type", e.getType());
        cv.put("minute", e.getMinute());
        cv.put("team_id", e.getTeamId());
        return getWritableDatabase().insert(TABLE_EVENTS, null, cv);
    }

    public List<EventModel> getEventsByMatch(int matchId) {
        List<EventModel> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT id,match_id,player_id,type,minute,team_id FROM " + TABLE_EVENTS + " WHERE match_id=?", new String[]{String.valueOf(matchId)});
        while (c.moveToNext()) {
            list.add(new EventModel(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getInt(4), c.getInt(5)));
        }
        c.close();
        return list;
    }

    public long registerUser(String name, String email, String password, String role) {
        if (role == null || role.trim().isEmpty()) role = "PLAYER";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("email", email);
        cv.put("password", password); // en producción deberías usar hash
        cv.put("role", role);

        try {
            return db.insertOrThrow(TABLE_USERS, null, cv);
        } catch (SQLiteConstraintException e) {
            // Email duplicado o restricción incumplida
            return -1;
        }
    }

    // Obtener usuario por email */
    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
    }

    // Obtener todos los usuarios
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users", null);
    }

    //   Obtener todos los árbitros (role = REFEREE)
    public Cursor getAllReferees() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE role = 'REFEREE'", null);
    }


// ================== Equipos ==================

    //  Obtener todos los equipos
    public Cursor getAllTeamsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM teams", null);
    }

    //   Obtener equipo por ID
    public Cursor getTeamById(int teamId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM teams WHERE id = ?", new String[]{String.valueOf(teamId)});
    }

    //Obtener partidos de un equipo (sea local o visitante)
    public Cursor getMatchesByTeam(int teamId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM matches WHERE team1_id = ? OR team2_id = ?",
                new String[]{String.valueOf(teamId), String.valueOf(teamId)});
    }

    //Obtener partido por ID
    public Cursor getMatchById(int matchId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM matches WHERE id = ?", new String[]{String.valueOf(matchId)});
    }

    // ==================== JUGADORES ====================

    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, name, position, number, team_id FROM players", null);
        if (c.moveToFirst()) {
            do {
                Player p = new Player(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getInt(3),
                        c.getInt(4)
                );
                players.add(p);
            } while (c.moveToNext());
        }
        c.close();
        return players;
    }

    public int updatePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", player.getName());
        cv.put("position", player.getPosition());
        cv.put("number", player.getNumber());
        cv.put("team_id", player.getTeamId());
        return db.update("players", cv, "id=?", new String[]{String.valueOf(player.getId())});
    }

    public int deletePlayer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("players", "id=?", new String[]{String.valueOf(id)});
    }

    //    Para el arbitro
    public List<Player> getPlayersByMatch(int matchId) {
        List<Player> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Ejemplo: asumiendo que la tabla matches tiene team1_id y team2_id
        Cursor c = db.rawQuery(
                "SELECT p.* FROM players p " +
                        "JOIN matches m ON (p.team_id = m.teamA_id OR p.team_id = m.teamB_id) " +
                        "WHERE m.id = ?",
                new String[]{String.valueOf(matchId)}
        );

        if (c.moveToFirst()) {
            do {
                list.add(new Player(
                        c.getInt(c.getColumnIndexOrThrow("id")),
                        c.getString(c.getColumnIndexOrThrow("name")),
                        c.getString(c.getColumnIndexOrThrow("position")),
                        c.getInt(c.getColumnIndexOrThrow("number")),
                        c.getInt(c.getColumnIndexOrThrow("team_id"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }


}
