package Controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Models.Robot;
import Models.Team;
import Models.Tile;
import Models.Map;
import Views.mapView;

public class GameController extends Game{
    
    private Music introMusic;
    
    /** All of the teams to be run in the simulation.*/
    private HashMap<Integer, Team> teams;
    
    /** The map that holds the information for calculations and the size*/
    private Map gameMap;
    
    
    /** holds the value to which team goes next*/
    private Queue<Team> nextTeamIdx;
    //TODO how do we choose which team goes first?
    
    /** CHANGE*/
    boolean isHeadless;
    
    /** Stores the type of map view */
    mapView view;
    
    /** how long it takes for each animation to complete in milliseconds */
    int animationSpeed = 100;
    
    /**
     * initializes the teams and ??sets their position on the map??
     * @param allTeams an array that contains all of the teams playing the match
     * @param hexSize the size of the map on one side
     */
    public GameController() throws RuntimeException{
        
        teams = new HashMap<Integer, Team>();
        gameMap = new Map();
        
        Team[] allTeams = gameVariables.allTeams;
        
        
//        introMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/Bit Quest.mp3"));
//        introMusic.setLooping(true);
//        introMusic.setVolume(0.6f);
//        introMusic.play();
        
        if(allTeams == null){
            throw new RuntimeException("There must be teams added to begin the game");
        }
        else{
            
            //adds the teams into the game controller
            for(int i = 0; i < allTeams.length; i++){
                teams.put((int) allTeams[i].getTeamNumber(), allTeams[i]);
            }
            
            for(int i = 0; i < allTeams.length; i++){
                this.nextTeamIdx.add(allTeams[i]);            
            }
        }
        
        //TODO // GameLog gameLog = new GameLog();
    }

    
    /**
     * puts the game into a paused state
     */
    public void pause(){
    }
    
    /**
     * resumes the game from the paused state
     */
    public void resume(){
        this.resume();
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
    	   Iterator<Entry<Integer, Team>> iter = teams.entrySet().iterator();
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
    	           toRet.add(gameMap.getDirection(xPos, tempRobot.getXPosition(), 
    	                   yPos, tempRobot.getYPoisition())); 
    	       
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
     * figures out which team gets to move next based off of the queue
     */
    public Team nextTurn(){
        while(this.nextTeamIdx.peek().numberOfLivingRobots() == 0){
            this.nextTeamIdx.remove();
        }
        Team temp = this.nextTeamIdx.remove();
        if(temp.numberOfLivingRobots() != 0){
            this.nextTeamIdx.add(temp);            
        }
        return temp;
    }


    public void moveRobot(Robot robotToMove, int TeamNumber ,int newX, int newY, int range, ){
       Robot temp = this.teams.get(TeamNumber).getTeamMember((int) robotToMove.getMemberNumber());
       
       
       
       Tile[][] allTiles = this.gameMap.getTiles();
               
       allTiles[temp.getXPosition()][temp.getYPoisition()].removeRobot(temp);
       
       temp.setXPosition(newX);
       temp.setYPosition(newY);
       
       allTiles[temp.getXPosition()][temp.getYPoisition()].addRobot(temp);
       
       int movesLeft = (int) temp.getMovesPerTurn();
       boolean doneTurn = false;
       while(!doneTurn){ 
           if(movesLeft == 0){
               doneTurn = true;
           }
           else{ //distance to new position is greater then turns remaining, then fail.
               temp.setPosition(newPosition);
               movesLeft--;
           }
       }
    }
        

    /**
     *  fire at the position passed in
     * @param shooter is the robot that will fire a shot
     * @param hexPos is the position that the robot is firing towards
     */
    public void shootAtSpace(Robot shooter, int hexPos){

        

    }
    /**
     * Called every frame
     */
   
    

    public void render(){
        super.render();
    }
    
    public static void main(String[] args){
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "RobotSport370";
        config.height = 800;
        config.width = 1280;
        new LwjglApplication(new GameController(), config);
    }

    
    @Override
    public void create() {
        
    }
}


