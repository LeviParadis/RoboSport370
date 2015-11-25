package Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Exceptions.ForthParseException;
import Exceptions.ForthRunTimeException;
import Interpreters.ForthInterpreter;
import Interpreters.JsonInterpreter;
import Models.Robot;
import Models.Team;
import Models.Tile;
import Models.ForthWord;
import Models.Map;
import Models.Map.DIRECTION;
import Views.mapView;

public class GameController{
    
    /** All of the teams to be run in the simulation.*/
    private ArrayList<Team> teams;
    /** The map that holds the information for calculations and the size*/
    private Map gameMap;
    
    
    private Thread executionThread;
    
    /** Stores the type of map view */
    mapView view;
    
    /** how long it takes for each animation to complete in milliseconds */
    int animationSpeed = 100;
    
    /**
     * initializes the teams and ??sets their position on the map??
     * @param allTeams an array that contains all of the teams playing the match
     * @param hexSize the size of the map on one side
     */
    public GameController(List<Team> allTeams) throws RuntimeException{
        
        teams = new ArrayList<Team>();

        gameMap = new Map();
        
        
        
//        introMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/Bit Quest.mp3"));
//        introMusic.setLooping(true);
//        introMusic.setVolume(0.6f);
//        introMusic.play();

        if(allTeams == null){
            throw new RuntimeException("There must be teams added to begin the game");
     
        } else if(allTeams.size() != 2 && allTeams.size() != 3 && allTeams.size() != 6){
            throw new RuntimeException("You must select either 2, 3, or 6 teams");
        } else {
            Iterator<Team> it = allTeams.iterator();
            while(it.hasNext()){
                Team nextTeam = it.next();
                teams.add((int) nextTeam.getTeamNumber(), nextTeam);
                //init robots
                Iterator<Robot> robotIt = nextTeam.getAllRobots().iterator();
                while(robotIt.hasNext()){
                    Robot nextRobot = robotIt.next();
                    try {
                        ForthInterpreter.initRobot(nextRobot, this);
                    } catch (ForthRunTimeException | ForthParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        
        this.executionThread = new Thread(){
            public void run(){
                int i = 1;
                while(teamsAlive() > 1){
                    System.out.println("turn: " + i);
                    executeNextTurn();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
          };

          executionThread.start();
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
     * puts the game into a paused state
     */
    public void pause(){
        try {
            this.executionThread.wait();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * resumes the game from the paused state
     */
    public void resume(){
        this.executionThread.notify();
    }
    
    /**
     * ends game stops all game threads from running and calls the endgame controller
     */
    public void endGame(){
        //TODO new EndController();
    }
    
    /**
     * Changes the animation speed to a new one in milliseconds.
     * @param newSpeed
     */
    public void setAnimationSpeed(int newSpeed){
        this.animationSpeed = newSpeed;
    }
    
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
    public Robot getRobot(int teamNum, int robotNum){
        return teams.get(teamNum).getTeamMember(robotNum);
    }
    
    /**
     * executes a round of turns
     */
    public void executeNextTurn(){
        Iterator<Team> teamIt = this.teams.iterator();
        while(teamIt.hasNext()){
            Team nextTeam = teamIt.next();
            Queue<Robot> robotList = nextTeam.getLivingRobots();
            Iterator<Robot> robotIt = robotList.iterator();
            while(robotIt.hasNext()){
                Robot nextRobot = robotIt.next();
                try {
                    ForthInterpreter.executeTurn(nextRobot, this);
                } catch (ForthRunTimeException | ForthParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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
       
      Robot temp = this.teams.get(TeamNumber).getTeamMember((int) robotToMove.getMemberNumber());
       
      Tile[][] allTiles = this.gameMap.getTiles();
       
      //Removing the robot from it's current tile
      allTiles[temp.getXPosition()][temp.getYPoisition()].removeRobot(temp);
       
      if(movesLeft < range){
          throw new RuntimeException("range to move cannot be higher than the amount of moves remaining");
      }
      
      temp.setXPosition(newX);
      temp.setYPosition(newY);
        
      //Adding the robot to the new tile
      allTiles[temp.getXPosition()][temp.getYPoisition()].addRobot(temp);
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
     * @param receiver the robot receiving the message
     * @param value the value to send
     * @return a bool indicating whether the operation was a success. 
     *         Will fail if robots aren't on the same team, if the receiver is destroyed, or if the receiver's mailbox is full
     */
    public boolean sendMail(Robot sender, Robot receiver, ForthWord value){
        return false;
    }

    
    /**
     * Will be called by the forth interpreter to show new actions for display in the interface
     * Will be called anytime the robot does anything, so the user can be updated as to what is happening
     * @param newActionMessage the latest action being run by a robot
     * @param lowPriority a flag indicating how important the message is. Low priority messages could be only shown in debug mode, for example
     */
    public void displayNewAction(String newActionMessage, boolean lowPriority){
        System.out.println(newActionMessage);
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


