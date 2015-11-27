package Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Queue;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

import Enums.ConsoleMessageType;
import Enums.GameSpeed;
import Exceptions.ForthParseException;
import Exceptions.ForthRunTimeException;
import Interfaces.ForthWord;
import Interpreters.ForthInterpreter;
import Interpreters.JsonInterpreter;
import Models.Robot;
import Models.Team;
import Models.Tile;
import Models.Map;
import Models.Map.DIRECTION;
import Views.endView;
import Views.mapView;

public class GameController{
    
    /** All of the teams to be run in the simulation.*/
    private ArrayList<Team> teams;
    /** The map that holds the information for calculations and the size*/
    private Map gameMap;
    
    private mapView view;
    
    private boolean isPaused;
    
    
    private Thread executionThread;
    

    /**
     * setting this enum will automatically change the animation speed and the delay duration
     */
    private GameSpeed speedMultiplier;
    
    /** how long it takes for each animation to complete in milliseconds */
    private int animationSpeed = 100;
    
    /** how long it waits in between actions in milliseconds */
    private int delayDuration = 500;
    
    private static int TURN_LIMIT = 2;
    
    /**
     * initializes the teams and ??sets their position on the map??
     * @param allTeams an array that contains all of the teams playing the match
     * @param hexSize the size of the map on one side
     */
    public GameController(List<Team> allTeams) throws RuntimeException{
        if(allTeams == null){
            throw new RuntimeException("There must be teams added to begin the game");
        } else if(allTeams.size() != 2 && allTeams.size() != 3 && allTeams.size() != 6){
            throw new RuntimeException("You must select either 2, 3, or 6 teams");
        }
        
        this.speedMultiplier = GameSpeed.GAME_SPEED_1X;
        gameMap = new Map();
        this.view = new mapView(this, allTeams);
   
        teams = new ArrayList<Team>();
        Iterator<Team> it = allTeams.iterator();
        while(it.hasNext()){
            Team nextTeam = it.next();
            teams.add((int) nextTeam.getTeamNumber(), nextTeam);
        }

        UIManager manager = UIManager.sharedInstance();
        manager.pushScreen(this.view);
        
        this.executionThread = new Thread(){
            public void run(){
                //init robots
                initRobots();
                
                int i = 1;
                while(teamsAlive() > 1 && i < TURN_LIMIT){
                    executeNextTurn(i);
                    i++;
                }
               displayMessage("done", ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
               this.join();
            }
          };
          executionThread.start();
    }
    
    private void initRobots(){
        displayMessage("Init", ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
        Iterator<Team> it = this.teams.iterator();
        while(it.hasNext()){
            Team nextTeam = it.next();
            displayMessage(nextTeam.getTeamName(), ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
            Iterator<Robot> robotIt = nextTeam.getAllRobots().iterator();
            while(robotIt.hasNext()){
                Robot nextRobot = robotIt.next();
                view.updateRobotInfo(nextRobot, 0);
                displayMessage(nextRobot.getName(), ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
                try {
                    ForthInterpreter.initRobot(nextRobot, this);
                } catch (ForthRunTimeException | ForthParseException e) {
                    e.printStackTrace();
                    displayMessage("Error: " + e.getMessage(), ConsoleMessageType.CONSOLE_ERROR);
                    displayMessage("Ending Init", ConsoleMessageType.CONSOLE_ERROR);
                }
            }
        }
    }
    
    public int teamsAlive(){
        int livingNum = 0;
        Iterator<Team> it = this.teams.iterator();
        while(it.hasNext()){
            Team thisTeam = it.next();
            int livingRobotCount = thisTeam.numberOfLivingRobots();
            if(livingRobotCount > 0){
                livingNum++;
            }
        }
        return livingNum;
    }
   
    /**
     * Called when the user pushed the fast forward button. 
     * Finds the current speed state, and switches to the next one
     * @return the new state, for updating ui elements
     */
    public GameSpeed switchGameSpeed(){
        switch(this.speedMultiplier){
            case GAME_SPEED_1X:
                this.speedMultiplier = GameSpeed.GAME_SPEED_2X;
                this.delayDuration = 250;
                this.animationSpeed = 50;
                 break;
            case GAME_SPEED_2X:
                this.speedMultiplier = GameSpeed.GAME_SPEED_4X;
                this.delayDuration = 125;
                this.animationSpeed = 25;
                break;
            case GAME_SPEED_4X:
                this.speedMultiplier = GameSpeed.GAME_SPEED_16X;
                this.delayDuration = 30;
                this.animationSpeed = 7;
                break;
            case GAME_SPEED_16X:
                this.speedMultiplier = GameSpeed.GAME_SPEED_1X;
                this.delayDuration = 500;
                this.animationSpeed = 100;
                break;
        }
        return this.speedMultiplier;
    }
    
    
    /**
     * @return whether the game should be paused
     */
    public boolean isPaused(){
        return this.isPaused;
    }
    
    /**
     * puts the game into a paused state
     */
    public void pause(){
        this.isPaused = true;
    }
    
    /**
     * resumes the game from the paused state
     */
    public void resume(){
        this.isPaused = false;
    }
    
    /**
     * calls the endgame controller
     */
    public void endGame(){
        //TODO new EndController();
        UIManager manager = UIManager.sharedInstance();
        EndController cont = new EndController();
        endView endView = new endView(cont, teams);
        manager.pushScreen(endView);
    }
    

    /**
     * @return the animation speed
     */
    public int getAnimationSpeed(){
        return this.animationSpeed;
    }
    
    
    public int scan(){
       int count = 0;
       
       for(int i = 0; i < teams.size(); i++){
    	   
       }
       return 0;
    }
    
    /**
     * 
     * @param robotSN the S/N of the robot that we need info about
     * @return a queue with the required info in the order
     * 1: Team Number int
     * 2: Robot health int
     * 3: distance/range to robot int
     * 4: direction to robot DIRECTION
     */
    public LinkedList<Object> identifyRobot(int robotSN, int xPos, int yPos){
        LinkedList<Object> toRet = new LinkedList<Object>();
//        boolean exists = false;
        
    	for(int i = 0; i < teams.size(); i++){
    	   Iterator<Team> iter = teams.iterator();
    	   while(iter.hasNext()){
    	       Team temp = (Team) iter.next();
    	       if(temp.getAllRobots().contains(robotSN)){
//    	           exists = true;
    	           toRet.add(temp.getTeamNumber());
    	           Robot tempRobot = temp.getTeamMember(robotSN);
    	           
    	           toRet.add((int) tempRobot.getHealth());
    	           //TODO add original robot position
    	           //Calculate distance
    	           int distance = gameMap.calcDistance( xPos,tempRobot.getXPosition(), 
    	                   yPos,tempRobot.getYPoisition());
    	           toRet.add(distance);
    	           
    	           
    	           //Calculate direction
    	           toRet.add(gameMap.getDirection(xPos)); 
    	       
    	       }
    	   }
    	}
    	return toRet;
    }
    
    
    /**
     * Gets the robot that currently has the turn to play
     * @param teamNum the number that represents the team that the robot
     * to be played is on
     * @param robotNum the s/n of the robot who's turn it is
     */
    public Robot getRobot(int teamNum, int robotNum) throws IndexOutOfBoundsException{
        return teams.get(teamNum).getTeamMember(robotNum);
    }
    
    /**
     * executes a round of turns
     */
    public void executeNextTurn(int turnNum){
        displayMessage("Turn " + turnNum, ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
        Iterator<Team> teamIt = this.teams.iterator();
        while(teamIt.hasNext()){
            Team nextTeam = teamIt.next();
            displayMessage(nextTeam.getTeamName(), ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
            Queue<Robot> robotList = nextTeam.getLivingRobots();
            Iterator<Robot> robotIt = robotList.iterator();
            while(robotIt.hasNext()){
                Robot nextRobot = robotIt.next();
                view.updateRobotInfo(nextRobot, turnNum);
                displayMessage(nextRobot.getName(), ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
                try {
                    ForthInterpreter.executeTurn(nextRobot, this);
                } catch (ForthRunTimeException | ForthParseException e) {
                    e.printStackTrace();
                    //this is thrown when forth encounters an error that it can't handle. 
                    //Display the error, and end the turn
                    displayMessage("Error: " + e.getMessage(), ConsoleMessageType.CONSOLE_ERROR);
                    displayMessage("Ending Turn", ConsoleMessageType.CONSOLE_ERROR);
                }
            }
        }
    }


    public int moveRobot(Robot robotToMove, int TeamNumber, int range, int Direction, int movesLeft) throws RuntimeException{
           
      int newX;
      int newY;

      DIRECTION dir = gameMap.getDirection(Direction);
      newX = dir.getXCoordinate();
      newY = dir.getYCoordinate();
        
      newX = newX*range;
      newY = newY*range;
      
//      for(int i = 0; i < teams.size(); i++){
//          Team temp = teams.get(i);
//          if(teams.get(i).getTeamNumber() == TeamNumber){
//              if(temp.getTeamDirection() == 5){
//                  newX = newX
//              }
//              newX = newX+teams.get(i).getTeamDirection();
//              
//          }
//      }
       
       
      Tile[][] allTiles = this.gameMap.getTiles();
       
      //Removing the robot from it's current tile
      allTiles[robotToMove.getXPosition()][robotToMove.getYPoisition()].removeRobot(robotToMove);
       
      if(movesLeft < range){
          throw new RuntimeException("range to move cannot be higher than the amount of moves remaining");
      }
      
      robotToMove.setXPosition(newX);
      robotToMove.setYPosition(newY);
        
      //Adding the robot to the new tile
      allTiles[robotToMove.getXPosition()][robotToMove.getYPoisition()].addRobot(robotToMove);
      return range;
        
   }
        
    
    /**
     *  fire at the position passed in
     * @param shooter is the robot that will fire a shot
     * @param range the distance away to shoot
     * @param direction the direction to shoot
     */
    public void shootAtSpace(Robot shooter, int range, int direction){
        Tile[][] allTiles = this.gameMap.getTiles();
        
        DIRECTION dir = gameMap.getDirection(direction);
        
        int xPos = dir.getXCoordinate()*range;
        int yPos = dir.getYCoordinate()*range;
        
        LinkedList<Robot> robots = allTiles[xPos][yPos].getRobots();
        
        Iterator<Robot> iter = robots.iterator();
        
        while(iter.hasNext()){
            Robot temp = iter.next();
            temp.inflictDamage(shooter.getStrength());
            if(temp.getHealth() <= 0){
                temp.destroy();
                robots.remove(temp);

            }
            
        }
    }
    
    /**
     * Finds a returns a list of the closest robots to input robot r
     * Will return up to 4 robots, in a range up to 3 spaces
     * Called by the ForthInterpreter
     * @param r the robot asking for closest robots
     * @return
     */
    public List<Robot> getClosest(Robot r) {
        return new LinkedList<Robot>();
    }
    
    /**
     * Sends a forth word from one robot to the mailbox of another
     * Robots must be on the same team, and the forth word must be either an int, a string, or a boolean
     * Called by the forth interpreter
     * @param sender  the robot sending the message
     * @param receiverNumber the member number of the robot to recieve the message (on the same team)
     * @param value the value to send
     * @return a bool indicating whether the operation was a success. 
     *         Will fail if the receiverNumber is invalid, if the receiver is destroyed, or if the receiver's mailbox is full
     */
    public boolean sendMail(Robot sender, int receiverNumber, ForthWord value){
        int teamNum = (int) sender.getTeamNumber();
        Team thisTeam = this.teams.get(teamNum);
        try{
            Robot reciever = thisTeam.getTeamMember(receiverNumber);
            //attempt to send mail. Will return true if it worked, or false if it failed
            return reciever.addMailFromMember((int)sender.getMemberNumber(), value);
        } catch (IndexOutOfBoundsException e){
            //if there is no team mate with the number requested, return false
            return false;
        }
    }

    
    /**
     * Will be called by the forth interpreter to show new actions for display in the interface
     * Will be called anytime the robot does anything, so the user can be updated as to what is happening
     * @param newActionMessage the latest action being run by a robot
     * @param type the type of message to display
     */
    public void displayMessage(String newActionMessage, ConsoleMessageType type){
        this.view.displayMessage(newActionMessage, type);
        //sleep in between messages, so that we get an even printing speed
        //do not progress to the next action if we are paused
        try {
            do{
                Thread.sleep(this.delayDuration);
            } while (this.isPaused);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Tells us the number of robots at a given square from a reference robot
     * Called but the Forth Interpreter
     * @param referencePosition the robot asking for the population
     * @param direction the direction to look in
     * @param range the range to look in
     * @return the number of robots on the space
     */
    public int populationAtPosition(Robot referencePosition, int direction, int range){
        return 0;
    }
    
    
    /**
     * Tells us the direction between two robots
     * @param from the robot to start from
     * @param to the robot we are finding the direction to
     */
    public int directionBetweenRobots(Robot from, Robot to){
        return 0;
    }
    
    /**
     * Tells us the range between two robots
     * @param from the robot to start from
     * @param to the robot we are finding the range to
     */
    public int rangeBetweenRobots(Robot from, Robot to){
        return 0;
    }
    

}


