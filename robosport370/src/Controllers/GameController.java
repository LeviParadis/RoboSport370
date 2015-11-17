package Controllers;



import java.util.HashMap;

import com.badlogic.gdx.Game;

import Models.Robot;
import Models.Team;
import Views.mapView;

public class GameController extends Game { //Extend class Corey mentioned for pause and play
    
    /**
     * All of the teams to be run in the simulation.
     */
    //Team teams[];
    HashMap<Integer, Team> teams;
    
    
    /**
     * holds the value to which team goes next
     */
    long nextTeamIdx;

    /**
     * 
     */
    //LogContoller GameLog;
    
    /**
     * The size of the hexmap, the size is one side length
     */
    int hexSize;
    
    /**
     * CHANGE
     */
    boolean isHeadless;
    
    /**
     * Stores the type of map view
     */
    mapView view;
    
    /**
     * how long it takes for each animation to complete in milliseconds
     */
    int animationSpeed = 100;
    
    /**
     * initializes the teams and ??sets their position on the map??
     * @param allTeams an array that contains all of the teams playing the match
     * @param hexSize the size of the map on one side
     */
    public GameController(Team allTeams[], int hexSize){
        teams = new HashMap<Integer, Team>();
        
        for(int i = 0; i < allTeams.length; i++){
            teams.put((int) allTeams[i].getTeamNumber(), allTeams[i]);
        }
        this.hexSize = hexSize;
        mapView newMap = new mapView();
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
    
    public float getAnimationSpeed(int newSpeed){
        this.animationSpeed = newSpeed;
    }
    
    public void scanSpace(int hexPos){
        
    }
    
    /**
     * 
     */
    public void identifyRobot(int teamNumber, int range, Direction direction, int health ){
        
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
     * 
     */
    public void nextTurn(){
        
    }
    
    
    public void moveRobot(Robot robotToMove, long newPosition, int TeamNumber){
       Robot temp = this.teams.get(TeamNumber).getTeamMember((int) robotToMove.getMemberNumber());
       temp.setPosition(newPosition);
    }
    
    /**
     *  fire at the position passed in
     * @param shooter is the robot that will fire a shot
     * @param hexPos is the position that the robot is firing towards
     * @precond the robot must be alive
     * @precond the hexPos must be on the map
     */
    public void shootAtSpace(Robot shooter, int hexPos){
        
    }
    
    public static void main(){
        
    }


    @Override
    public void create() {
        // TODO Auto-generated method stub
        
    }
}
