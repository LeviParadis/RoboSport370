package Interfaces;

import java.util.Queue;

import Models.Robot;

public interface PickRobotsDelegate {
    
    
    void robotsListCancelled();
    
    void robotListFinished(Queue<Robot> listSelected); 

}
