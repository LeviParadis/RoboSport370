package Models;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Team {
    // the list of robots in the team. It's a queue so we can keep track of turn
    // orders if we choose to
    private Queue<Robot> robotList;
    // a number assigned to this team by the simulator for id purposes
    private int teamNumber;
    // teams starting direction on map
    private int teamStartDirection;

    /**
     * Default Constructor
     * 
     * @param robots
     *            a list of robots to make up the team
     * @param uniqueNumber
     *            a number assigned to this team by the simulator for id
     *            purposes
     */
    public Team(Queue<Robot> robots, int uniqueNumber) {

        this.robotList = robots;
        this.teamNumber = uniqueNumber;

        // assign each robot it's id information for this game
        Iterator<Robot> robotIterator = robots.iterator();
        int i = 0;
        while (robotIterator.hasNext()) {
            Robot thisRobot = robotIterator.next();
            thisRobot.setTeamIDs(uniqueNumber, i);
            i++;
        }
        // increment unique number, so the next team will have a different
        // number than this
        uniqueNumber++;
    }

    /**
     * @return an ordered list of all robots on the team
     */
    public Queue<Robot> getAllRobots() {
        return this.robotList;
    }

    /**
     * @return an ordered list of all robots on the team that are still alive
     */
    public Queue<Robot> getLivingRobots() {
        Queue<Robot> allRobots = this.getAllRobots();

        Queue<Robot> livingSet = new LinkedList<Robot>();
        Iterator<Robot> robotIterator = allRobots.iterator();
        while (robotIterator.hasNext()) {
            Robot thisRobot = robotIterator.next();
            if (thisRobot.isAlive()) {
                livingSet.add(thisRobot);
            }
        }
        return livingSet;
    }

    /**
     * @return the number of robots that are still alive on the team
     */
    public int numberOfLivingRobots() {
        Queue<Robot> livingSet = this.getLivingRobots();
        return livingSet.size();
    }

    /**
     * @return the team's unique number
     */
    public int getTeamNumber() {
        return teamNumber;
    }

    public Robot getTeamMember(int memberNumber) throws IndexOutOfBoundsException {
        Robot[] array = (Robot[]) this.robotList.toArray();
        int size = array.length;
        if (memberNumber >= size || memberNumber < 0) {
            throw new IndexOutOfBoundsException();
        } else {
            return array[memberNumber];
        }
    }

    /**
     * 
     * Sets which part of the map that the team starts on
     * 
     * @param dir
     *            the direction to se the team
     */
    public void setTeamDirection(int dir) {
        this.teamStartDirection = dir;
    }

    public int getTeamDirection() {
        return this.teamStartDirection;
    }

    /**
     * @return the team's name in a format for display
     */
    public String getTeamName() {
        return "Team " + this.teamNumber;

    }

}
