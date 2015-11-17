package Models;

import Enums.JSONConstants;

public class RobotGameStats {
    
    private long wins,losses, executions, gamesSurvived, gamesDied, damageGiven, damageAbsorbed, kills, distanceMoved;
    private long numDiedThisGame;

    /**
     * Create a new robot stats instance to store the statistics for a robot file
     * @param wins   the number of wins this robot has in its history from previous matches
     * @param losses the number of losses this robot has in its history from previous matches
     * @param executions the number of times this robot has been executed in it's history
     * @param gamesDied the number of times this robot has died in it's previous games
     * @param gamesSurvived the number of times this robot has survived it's previous games
     * @param damageGiven the total amount of damage this robot has caused in it's history
     * @param damageRecieved the total amount of damage the robot has received in it's history
     * @param kills the total number of kills the robot has achieved in it's history
     * @param distanceMoved the total distance the robot has moved in it's history
     */
    public RobotGameStats(long wins, long losses, long executions, long gamesDied, long gamesSurvived, long damageGiven, long damageRecieved, long kills, long distanceMoved){
        this.wins = wins;
        this.losses = losses;
        this.executions = executions;
        this.gamesSurvived = gamesSurvived;
        this.gamesDied = gamesDied;
        this.damageGiven = damageGiven;
        this.damageAbsorbed = damageRecieved;
        this.kills = kills;
        this.distanceMoved = distanceMoved;
        this.numDiedThisGame = 0;
    }
    
    /**
     * create a game stats object for a brand new robot script
     */
    public RobotGameStats(){
        this.wins = 0;
        this.losses = 0;
        this.executions = 0;
        this.gamesSurvived = 0;
        this.gamesDied = 0;
        this.damageGiven = 0;
        this.damageAbsorbed = 0;
        this.kills = 0;
        this.distanceMoved = 0;
        this.numDiedThisGame = 0;
    }
    
    /**
     * at the end of a match, this function should be called to update the robot's win stats
     * @param numWins the amount of instances of this robot that was on the winning team
     * @param numLosses the amount of instances of this robot that was on losing teams
     */
    public void finishMatch(long numWins, long numLosses){
      //TODO: this needs to be set up
        long totalNumInMatch = numWins + numLosses;
        this.executions = this.executions + totalNumInMatch;
        this.gamesDied = this.gamesDied + numDiedThisGame;
        this.gamesSurvived = this.gamesSurvived +  (totalNumInMatch - numDiedThisGame);
        this.wins = this.wins + numWins;
        this.losses = this.losses + numLosses;
    }
    
    /**
     * mark that this robot has died this game
     */
    protected void markAsDied(){
        this.numDiedThisGame = this.numDiedThisGame + 1;
    }
    
    /**
     * mark that this robot has caused some damage
     */
    protected void incrementDamageGiven(long amountDamage){
        //TODO: this needs to be set up
        this.damageGiven = this.damageGiven + amountDamage;
    }
    
    /**
     * mark that this robot has received some damage
     */
    protected void incrementDamageReceived(long amountDamage){
        this.damageAbsorbed = this.damageAbsorbed + amountDamage;
    }

    /**
     * mark that this robot has killed another
     */
    protected void incrementKills(){
      //TODO: this needs to be set up
        this.kills = this.kills + 1;
    }
    
    /**
     * mark the distance that this robot has moved
     */
    protected void incrementDistanceMoved(){
      //TODO: this needs to be set up
        this.distanceMoved = this.distanceMoved + 1;
    }
    
    
    public long getWins() {
        return wins;
    }

    public long getLosses() {
        return losses;
    }

    public long getExecutions() {
        return executions;
    }

    public long getGamesSurvived() {
        return gamesSurvived;
    }

    public long getGamesDied() {
        return gamesDied;
    }

    public long getDamageGiven() {
        return damageGiven;
    }

    public long getDamageAbsorbed() {
        return damageAbsorbed;
    }

    public long getKills() {
        return kills;
    }

    public long getDistanceMoved() {
        return distanceMoved;
    }

}
