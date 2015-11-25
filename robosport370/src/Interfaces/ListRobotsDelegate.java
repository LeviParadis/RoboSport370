package Interfaces;

import java.util.Queue;

import Models.Robot;

public interface ListRobotsDelegate {
    
    
    void robotsListCancelled();
    
    void robotListFinished(Queue<Robot> listSelected); 

}
