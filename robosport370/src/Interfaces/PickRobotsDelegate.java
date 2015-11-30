package Interfaces;

import java.util.Queue;

import Models.Robot;

public interface PickRobotsDelegate {

    /**
     * Called when the user cancels the robot selection action
     */
    void robotsListCancelled();

    /**
     * called when the user is finished selecting a list of robots
     * 
     * @param listSelected
     *            the list of robots the user has selected
     */
    void robotListFinished(Queue<Robot> listSelected);

}
