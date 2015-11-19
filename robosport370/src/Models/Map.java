package Models;

public class Map {
   
//	public enum DIRECTION{
//	    
//		NORTH(0),
//		SOUTH(3),
//	    NORTH_WEST(5),
//	    NORTH_EAST(1),
//	    SOUTH_WEST(4),
//	    SOUTH_EAST(2);
//		
//		public final int direction;
//		
//		
//		DIRECTION(int direction){
//			this.direction = direction;
//		}
//		
//		public int getDirection(){
//			return direction;
//		}
//		
//	}
    
	
	private int maxNorth;
	private int maxSouth;
	private int maxEast;
	private int maxWest;
	public static int center = 0;
	
	
	public enum DIRECTION{
//		EAST(1,0),
//	    WEST(-1, 0),
		NORTH(0,1),
		SOUTH(0,-1),
	    NORTH_WEST(-1,1),
	    NORTH_EAST(1,0),
	    SOUTH_WEST(-1,0),
	    SOUTH_EAST(1,-1);
	    
	    public final int northCoordinate;
		public final int southCoordinate;
		
		DIRECTION(int northCoordinate, int southCoordinate){
			this.northCoordinate = northCoordinate;
			this.southCoordinate = southCoordinate;
		}
		
		public int[] getXY(){
			int[] toRet = new int[2];
			toRet[0] = northCoordinate;
			toRet[1] = southCoordinate;
			return toRet;
		}
	   
	}
	
    private int boardSize;  
    
    public Map(int mapSideSize){
    	this.boardSize 	= mapSideSize;
    	this.maxNorth 	= mapSideSize;
    	this.maxSouth 	= -mapSideSize;
    	this.maxEast 	= mapSideSize;
    	this.maxWest	= -mapSideSize;
    	
    	
    }
    
    public int getMapSize(){
    	return this.boardSize;
    }
    
    
    
    public static void main(int args[]){
        
    }
}
