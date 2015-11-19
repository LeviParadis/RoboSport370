package Controllers;

import java.util.HashMap;
import java.util.Queue;

import com.badlogic.gdx.Game;

import Models.Robot;
import Models.Team;
import Models.Map.DIRECTION;
import Models.Map;
import Views.mapView;

public class GameController extends Game{
    
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
    public GameController(Team allTeams[], int hexSize){
        
        teams = new HashMap<Integer, Team>();
        gameMap = new Map(hexSize);
        
        //adds the teams into the game controller
        for(int i = 0; i < allTeams.length; i++){
            teams.put((int) allTeams[i].getTeamNumber(), allTeams[i]);
        }
        
        for(int i = 0; i < allTeams.length; i++){
            this.nextTeamIdx.add(allTeams[i]);            
        }
        
        
        //TODO // GameLog gameLog = new GameLog();
    }
    
    /**
     * puts the game into a paused state
     */
    public void pause(){
        this.pause();
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
       int count =0;
       
       for(int i = 0; i < teams.size(); i++){
    	   
       }
       return 0;
    }
    
    /**
     * 
     */
    public void identifyRobot(int teamNumber, int range, DIRECTION direction, int health ){
    	teams.get(teamNumber);//TODO
    	
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
    
    public void moveRobot(Robot robotToMove, long newPosition, int TeamNumber){
       Robot temp = this.teams.get(TeamNumber).getTeamMember((int) robotToMove.getMemberNumber());
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
     * @precond the robot must be alive
     * @precond the hexPos must be on the map
     */
    public void shootAtSpace(Robot shooter, int hexPos){
        for(int i = 0; i < teams.size(); i++){
        	if ()
        }
    }
    
    public static void main(){
        
    }


    @Override
    public void create() {
        // TODO Auto-generated method stub
        
    }
}
