package Controllers;



import com.badlogic.gdx.Game;

import Views.mapView;

public class GameController extends Game { //Extend class Corey mentioned for pause and play
	
    /**
     * All of the teams to be run in the simulation.
     */
	Team teams[];
	
    /**
     * holds the value to which team goes next
     */
	int nextTeamIdx;

	/**
	 * 
	 */
	LogContoller GameLog;
	
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
	 * how long it takes for each animation to complete in ??miliseconds??
	 */
	int animationSpeed = 100; // hard coded?
	
	/**
	 * initializes the teams and ??sets their position on the map??
	 * @param allTeams an array that contains all of the teams playing the match
	 * @param hexSize the size of the map on one side
	 * @precond hexSize cannot be null
	 */
	public GameController(Team allTeams[], int hexSize){
	    this.hexSize = hexSize;
	    mapView newMap = new mapView();
	    GameLog gameLog = new GameLog();
	    
	    if(allTeams == null){
	        throw new RuntimeException("there must be teams to have a tournement");
	    }
	    else{
	        this.teams = allTeams;	        
	    }
	    
	    
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
	    new EndController();
	}
	
	/**
	 * Changes the animation speed to a new one in milliseconds.
	 * @param newSpeed
	 */
	public void setAnimationSpeed(int newSpeed){
	    this.animationSpeed = newSpeed;
	}
	
	public void scanSpace(int hexPos){
	    for(int i = 0; i < teams.length; i++){
	        for(int j = 0; j < 5[; j++){
	            if(team[i].robotlist[j].hexPosition){
	                
	            }
	        }
	    }
	}
	
	/**
	 * Gets the robot that currently has the turn to play
	 * @param teamNum the number that represents the team that the robot
	 * to be played is on
	 * @param robotNum the s/n of the robot who's turn it is
	 */
	public void getRobot(int teamNum, int robotNum){
	    
	}
	
	public void nextTurn(){
	    
	}
	
	
	public void moveRobot(){
	    
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
