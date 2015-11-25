package Controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Interpreters.JsonInterpreter;
import Models.Robot;
import Models.Team;
import Views.AddRobotView;
import Views.ManageRobotView;
import Views.mainMenuView;
import Views.setupView;
import Views.mapView;
import Views.teamCreationView;
import Views.EditTeamView;

/**
 * @author Corey
 * @author Levi
 * setupController handles the main menu and setup screens while interfacing with the models
 *
 */
public class setupController {
	private Music introMusic;
	public int mapSize;
	public boolean isTournament,isSimulation;
public List<Team> selectedTeams;
	
	

    public setupController(){
        introMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/Bit Quest.mp3"));
        introMusic.setLooping(true);
        introMusic.setVolume(0.6f);
        introMusic.play();
        mapSize = 5;
        isTournament = false;
        isSimulation = false;

        //This is for testing my first screen
        this.selectedTeams = new LinkedList<Team>();
    }

	/**
	 * Gets called when the Main Menu view selects tournament
	 * 
	 */
	public void notifyTournament(){
    UIManager manager = UIManager.sharedInstance();
    
    manager.pushScreen(new setupView(this));
    System.out.println("Tournament");
	}
	
	/**
	 * Gets called when the Main Menu view selects a simulation
	 */
	public void notifySim(){
	      UIManager manager = UIManager.sharedInstance();
	      manager.pushScreen(new setupView(this));
      gameVariables.isSim = true;
	}
	public void notifyDebug(){
	    gameVariables.isDebug = true;
	}
	
	public void notifyNewRobot() {
		ManageRobotController cont = new ManageRobotController();
	    ManageRobotView view = new ManageRobotView(cont);
	    UIManager manager = UIManager.sharedInstance();
	    manager.pushScreen(view);
	}
	
	public void notifyNewTeam() {
        EditTeamController cont = new EditTeamController();
        EditTeamView view = new EditTeamView(cont);
        UIManager manager = UIManager.sharedInstance();
        manager.pushScreen(view);
    }
	
	/**
	 * gets called when the Main Menu view selects exit
	 */
	public void notifyExit(){
	    Gdx.app.exit();
	}
	/**
	 * gets called when Setup view selects return
	 */
	public void notifyReturn(){
    UIManager manager = UIManager.sharedInstance();
    manager.popScreen();
    this.selectedTeams.clear();
	}
	/**
     * gets called when Setup view selects return
     */
	/**
	 * changes the screen when continue is pressed
	 */
	//public void notifyAddTeam(){
    
	 
	 /*if(this.selectedTeams.size() < 6){
	         
            Queue<Robot> robotList = JsonInterpreter.listRobots(true, null, null, null, null, null, null, null, null);
	         Team newTeam = new Team(robotList, this.selectedTeams.size());
	         this.selectedTeams.add(newTeam);
	         
	         System.out.println(this.selectedTeams);
    } else {
        System.out.println("already 6 teams");
    }*/
	//}

public void notifyDeleteTeam(){
    if(!this.selectedTeams.isEmpty()){
        this.selectedTeams.remove(this.selectedTeams.size()-1);
        System.out.println(this.selectedTeams);
    } else {
        System.out.println("already empty");
    }
}

public void notifyContinue(){
    try{
        GameController game = new GameController(this.selectedTeams);
        mapView map = new mapView(game, this.selectedTeams);
        
         UIManager manager = UIManager.sharedInstance();
         manager.pushScreen(map);

    } catch (RuntimeException e){
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
	/**
	 * Handles storing the mapsize data
	 */
	public void notifyMapSize(){
	    if(this.mapSize < 11) {
            this.mapSize = this.mapSize + 2;
        }
        else if (this.mapSize >= 11) {
            this.mapSize = this.mapSize - 6;
        }   
	    gameVariables.mapSize = this.mapSize;
	}


}
