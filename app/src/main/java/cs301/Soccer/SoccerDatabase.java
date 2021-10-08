package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable<String, SoccerPlayer> database = new Hashtable<>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        String fullname = firstName + " ## " + lastName;
        SoccerPlayer temp = database.get(fullname);
        if(temp != null){
            return false;
        }

        SoccerPlayer player = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
        database.put(fullname, player);
        return true;
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        String fullname = firstName + " ## " + lastName;
        SoccerPlayer temp =  database.get(fullname);
        if(temp == null){
            return false;
        }
        database.remove(fullname);
        return true;
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */



    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        String fullname = firstName + " ## " + lastName;
        SoccerPlayer temp = database.get(fullname);
        if(temp == null){
            return null;
        }
        return temp;
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        String fullname = firstName + " ## " + lastName;
        SoccerPlayer temp = database.get(fullname);
        if(temp == null){
            return false;
        }
        temp.bumpGoals();
        return true;
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        String fullname = firstName + " ## " + lastName;
        SoccerPlayer temp = database.get(fullname);
        if(temp == null){
            return false;
        }
        temp.bumpYellowCards();
        return true;
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        String fullname = firstName + " ## " + lastName;
        SoccerPlayer temp = database.get(fullname);
        if(temp == null){
            return false;
        }
        temp.bumpRedCards();
        return true;
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        if(teamName == null){
            return database.size();
        }
        int totalPlayers = 0;
        for(SoccerPlayer current: database.values()){
            if(current.getTeamName().equals(teamName)){
                totalPlayers ++;
            }
        }
        return totalPlayers;
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
        int totalPlayers = numPlayers(teamName);
        if(idx > totalPlayers){
            return null;
        }
        int playerIndex = 0;
        if(teamName == null){
            for(SoccerPlayer current: database.values()){
                    if (playerIndex == idx) {
                        return current;
                    } else {
                        playerIndex++;
                    }
            }
        }else {
            for (SoccerPlayer current : database.values()) {
                if (current.getTeamName().equals(teamName)) {
                    if (playerIndex == idx) {
                        return current;
                    } else {
                        playerIndex++;
                    }
                }
            }
        }
        return null;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
        String first;
        String last;
        int uniform;
        String team;
        int goals;
        int redcards;
        int yellowcards;

        try {
            Scanner scan = new Scanner(file);
            while(scan != null) {
                first = scan.nextLine();
                last = scan.nextLine();
                team = scan.nextLine();
                uniform = scan.nextInt();
                addPlayer(first, last, uniform, team);
                goals = scan.nextInt();
                for(int i=0; i<goals; i++){
                    bumpGoals(first, last);
                }
                redcards = scan.nextInt();
                for(int i=0; i<redcards; i++){
                    bumpRedCards(first, last);
                }
                yellowcards = scan.nextInt();
                for(int i=0; i<yellowcards; i++){
                    bumpYellowCards(first, last);
                }

            }
        } catch (FileNotFoundException e) {
            return false;
        }
        return false;
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file){
        try {
            PrintWriter pw = new PrintWriter(file);
            for(SoccerPlayer current: database.values()){
                pw.write(logString(current.getFirstName()));
                pw.write(logString(current.getLastName()));
                pw.write(logString(current.getTeamName()));
                pw.write(logString(String.valueOf(current.getUniform())));
                pw.write(logString(String.valueOf(current.getGoals())));
                pw.write(logString(String.valueOf(current.getRedCards())));
                pw.write(logString(String.valueOf(current.getYellowCards())));
            }
        } catch (FileNotFoundException e) {
            return false;
        }

        return true;
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        HashSet<String> tempSet = new HashSet<String>();
        for(SoccerPlayer current: database.values()){
            if(!tempSet.contains(current.getTeamName())){
                tempSet.add(current.getTeamName());
            }
        }
        return tempSet;
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
