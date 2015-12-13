package Controllers;

import Interfaces.PickRobotsDelegate;

import java.util.Queue;
import Interpreters.JsonInterpreter;
import Models.Robot;

public class PickRobotsController {

    private int minSelection;
    private int maxSelection;

    private PickRobotsDelegate delegate;

    /**
     * Construct a new instance of the robot picker class
     * 
     * @param minSelection
     *            the min number of robots the user can select
     * @param maxSelection
     *            the max number of robots the user can select
     * @param delegate
     *            the class that is set up to recieve the selected robots
     */
    public PickRobotsController(int minSelection, int maxSelection, PickRobotsDelegate delegate) {
        this.minSelection = minSelection;
        this.maxSelection = maxSelection;
        this.delegate = delegate;
    }

    /**
     * @return the min number of robots the user can select
     */
    public int getMinimumSelectable() {
        return this.minSelection;
    }

    /**
     * @return the max number of robots the user can select
     */
    public int getMaxSelectable() {
        return this.maxSelection;
    }

    /**
     * Called when the user presses the cancel button
     */
    public void notifyCancel() {
        // tell the delegate the user has cancelled the operation
        this.delegate.robotsListCancelled();
    }

    /**
     * called when the when presses the confirm button
     * 
     * @param robotList
     *            the list of robots that were selected by the user
     */
    public void notifyConfirm(Queue<Robot> robotList) {
        // if the robot list matches our requirements, we tell the delegate that
        // we have successfully picked some robots
        if (robotList.size() >= this.getMinimumSelectable()) {
            this.delegate.robotListFinished(robotList);
        }
    }

    /**
     * Called when the user presses the search button. Should return a list of
     * robots to update the view with
     * 
     * @param name
     *            the name to search
     * @param team
     *            the team name to search
     * @param minWins
     *            the max wins for the robot
     * @param maxWins
     *            the min wins for the robot
     * @param minLosses
     *            the max losses for the robot
     * @param maxLosses
     *            the min wins for the robot
     * @param minGamesPlayed
     *            the min number of matches played for the robot
     * @param maxGamesPlayed
     *            the max number of matches played for the robot
     * @param currentOnly
     *            whether to return all versions, or just the current
     * @return A queue of robots representing all robots on the server with
     *         those parameters
     * @throws NumberFormatException
     *             thrown if one of the text fields does not contain an integer
     */
    public Queue<Robot> notifySearch(String name, String team, String minWins, String maxWins, String minLosses,
            String maxLosses, String minGamesPlayed, String maxGamesPlayed, boolean currentOnly)
                    throws NumberFormatException {
        int minWinsInt = Integer.parseInt(minWins);
        int maxWinsInt = Integer.parseInt(maxWins);
        int minLossesInt = Integer.parseInt(minLosses);
        int maxLossesInt = Integer.parseInt(maxLosses);
        int minGamesInt = Integer.parseInt(minGamesPlayed);
        int maxGamesInt = Integer.parseInt(maxGamesPlayed);

        // delegate this task to the JsonInterpreter
        Queue<Robot> results = JsonInterpreter.listRobots(currentOnly, name, team, minWinsInt, maxWinsInt, minLossesInt,
                maxLossesInt, minGamesInt, maxGamesInt);
        return results;
    }
}
