package Interpreters;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Enums.JSONConstants;
import Models.Robot;
import Models.RobotGameStats;

import java.io.*;
import java.util.HashMap;

public class JsonInterpreter {
    
    //test method
    public static void main(String[] args) {
        JSONParser parser=new JSONParser(); 
        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader("resources/RobotExample.JSON"));
            Robot newRobot = robotFromJSON(json, null);
            System.out.println(json);
            System.out.println(newRobot);
        } catch (IOException | ParseException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Creates a robot object from a standard robot JSON file
     * Accepts a cache of robot stats files, so we can map similar robots to the same stats object
     * @param json a JSONObject representing the robot
     * @param statsCache a hashmap of previous robot stats objects, mapted to serial number
     * @return a robot object with the information saved in the JSON file
     */
    public static Robot robotFromJSON(JSONObject json, HashMap<Integer, RobotGameStats> statsCache){
        
        
      
        JSONObject root = (JSONObject) json.get(JSONConstants.BASE_TAG);
        
        //get the identification information
        String name = (String) root.get(JSONConstants.NAME);
        long serial = (long) root.get(JSONConstants.SERIAL);
        
        RobotGameStats stats;
        //check if a stats object was already created for this robot type
        if(statsCache != null && statsCache.get(new Integer((int) serial)) != null){
            stats = statsCache.get(new Integer((int) serial));
        } else {
            //get the robot's stats record
            long wins = (long) root.get(JSONConstants.WINS);
            long losses = (long) root.get(JSONConstants.LOSSES);
            long executions = (long) root.get(JSONConstants.EXECUTIONS);
            long gamesDied = (long)root.get(JSONConstants.GAMES_DIED);
            long gamesSurvived = (long)root.get(JSONConstants.GAMES_LIVED);
            long damageGiven = (long)root.get(JSONConstants.DAMAGE_GIVEN);
            long damageRecieved = (long)root.get(JSONConstants.DAMAGE_TAKEN);
            long kills = (long)root.get(JSONConstants.KILLS);
            long distanceMoved = (long)root.get(JSONConstants.DISTANCE_MOVED);
            stats = new RobotGameStats(wins, losses, executions, gamesDied, gamesSurvived, damageGiven, damageRecieved, kills, distanceMoved);
            statsCache.put(new Integer((int) serial), stats);
        }
        
        //get the robot's attributes
        long health = (long) root.get(JSONConstants.HEALTH);
        long strength = (long) root.get(JSONConstants.STRENGTH);
        long moves = (long) root.get(JSONConstants.MOVES_PER_TURN);
    
    
        //find the forth code from the json
        JSONArray forth = (JSONArray) root.get(JSONConstants.FORTH_CODE);
        HashMap<String, String> variableList = new HashMap<String, String>();
        HashMap<String, String> wordList = new HashMap<String, String>();
        for(int i=0; i < forth.size(); i++){
            JSONObject thisObject = (JSONObject) forth.get(i);
        
            //check to see if this forth element is a variable
            if(thisObject.get(JSONConstants.FORTH_VAR) != null){
                //store the variable with an empty assignment
                String varName = (String)thisObject.get(JSONConstants.FORTH_VAR);
                variableList.put(varName, "0");
            
                //otherwise, it should be a word
            }else if (thisObject.get(JSONConstants.FORTH_WORD) != null){
                JSONObject word = (JSONObject) thisObject.get(JSONConstants.FORTH_WORD);
                String wordName = (String) word.get(JSONConstants.FORTH_WORD_NAME);
                String wordBody = (String) word.get(JSONConstants.FORTH_WORD_BODY);
                wordList.put(wordName, wordBody);  
            }
        }
        
    
        //create a robot from the information in the json
        Robot newRobot = new Robot( name, serial, health, strength, moves, 
                                    variableList, wordList, stats);
        return newRobot;
    }
    
    //the json library we use creates warnings when we try to write to JSON files, so we will
    //have to suppress warnings in this function where it writes to JSON
    @SuppressWarnings("unchecked")
    /**
     * @return a formated JSON object from all the stats from a robot
     */
    public JSONObject statsToJSON(RobotGameStats stats){
        JSONObject root = new JSONObject();
        root.put(JSONConstants.LOSSES, stats.getLosses());
        root.put(JSONConstants.WINS, stats.getWins());
        root.put(JSONConstants.EXECUTIONS, stats.getExecutions());
        root.put(JSONConstants.GAMES_LIVED, stats.getGamesSurvived());
        root.put(JSONConstants.GAMES_DIED, stats.getGamesSurvived());
        root.put(JSONConstants.DAMAGE_TAKEN, stats.getDamageAbsorbed());
        root.put(JSONConstants.DAMAGE_GIVEN, stats.getDamageGiven());
        root.put(JSONConstants.KILLS, stats.getKills());
        root.put(JSONConstants.DISTANCE_MOVED, stats.getDistanceMoved());
        return root;
    }
    

    

}
