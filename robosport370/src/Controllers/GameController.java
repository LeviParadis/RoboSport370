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

import Interpreters.JsonInterpreter;
import Models.Robot;
import Models.Team;
import Models.Tile;
import Models.Map;
import Models.Map.DIRECTION;
import Views.mapView;

public class GameController extends Game{
    
    /** All of the teams to be run in the simulation.*/
    private ArrayList<Team> teams;
    /** The map that holds the information for calculations and the size*/
    private Map gameMap;
    /** holds the value to which team goes next*/
    private Queue<Team> nextTeamIdx;
    //TODO how do we choose which team goes first?
    
    
    
    /** Stores the type of map view */
    mapView view;
    
    /** how long it takes for each animation to complete in milliseconds */
    int animationSpeed = 100;
    
    /**
     * initializes the teams and ??sets their position on the map??
     * @param allTeams an array that contains all of the teams playing the match
     * @param hexSize the size of the map on one side
     */
    public GameController(Queue<Team> allTeams) throws RuntimeException{
        
        teams = new ArrayList<Team>();
        this.nextTeamIdx = new LinkedList<Team>();

        gameMap = new Map();
        
        
        
//        introMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/Bit Quest.mp3"));
//        introMusic.setLooping(true);
//        introMusic.setVolume(0.6f);
//        introMusic.play();

        if(allTeams == null){
            throw new RuntimeException("There must be teams added to begin the game");
     
        } else if(allTeams.size() != 2 && allTeams.size() != 3 && allTeams.size() != 6){
            throw new RuntimeException("Not a valid number of teams");
        } else {
            Iterator<Team> it = allTeams.iterator();
            while(it.hasNext()){
                Team nextTeam = it.next();
                teams.add((int) nextTeam.getTeamNumber(), nextTeam);
                this.nextTeamIdx.add(nextTeam);
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


    public void moveRobot(Robot robotToMove, int TeamNumber, int range, int Direction, int movesLeft){
           
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
        
   }
        
    
    /**
     *  fire at the position passed in
     * @param shooter is the robot that will fire a shot
     * @param hexPos is the position that the robot is firing towards
     */
    public void shootAtSpace(Robot shooter, int range, int Direction){
        Tile[][] allTiles = this.gameMap.getTiles();
        
        DIRECTION dir = gameMap.getDirection(Direction);
        
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
        
        Queue<Team> teamList = new LinkedList<Team>();
        for(int i=0; i<6; i++){
            Queue<Robot> robotList = JsonInterpreter.listRobots(true, null, null, null, null, null, null, null, null);
            Team newTeam = new Team(robotList, i);
            teamList.add(newTeam);
        }
        
        new LwjglApplication(new GameController(teamList), config);
    }

    
    @Override
    public void create() {
        
    }
}


