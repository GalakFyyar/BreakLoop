package com.plaxx.breakloop;

//orientation
//0 = north
//1 = east
//2 = south
//3 = west

//connectors
//{north, east, south, west}

public class Tile{
	public int x;
	public int y;
	private Tile[][] grid;
	private boolean locked;
	private boolean marked;
	private boolean empty;
	private TileTypes type;
	private float degrees;	//for display purposes only

	private int orientation;
	private int connections;
	private boolean[] connectors;

	public Tile(Tile[][] g, TileTypes t, int ax, int ay){
		grid = g;
		x = ax;
		y = ay;
		type = t;
		locked = false;
		empty = t == TileTypes.emty;
		degrees = 0;
		orientation = 0;

		switch(t){
			case endp:
				connections = 1;
				connectors = new boolean[]{true, false, false, false};
				break;
			case line:
				connections = 2;
				connectors = new boolean[]{true, false, true, false};
				break;
			case corn:
				connections = 2;
				connectors = new boolean[]{true, true, false, false};
				break;
			case bone:
				connections = 3;
				connectors = new boolean[]{true, true, true, false};
				break;
			case cros:
				connections = 4;
				connectors = new boolean[]{true, true, true, true};
				break;
		}
	}

	public void lock(){
		locked = true;
	}

	public void mark(){
		marked = true;
	}

	public void addDegreesCW(float deg){
		degrees = (degrees + deg) % 360;
	}

	public void rotateCW(int nTimes){
		orientation = (orientation + nTimes) % 4;

		int n = nTimes % 4;

		boolean[] tempConnectors = {connectors[(4 - n) % 4], connectors[(5 - n) % 4], connectors[(6 - n) % 4], connectors[(7 - n) % 4]};

		connectors[0] = tempConnectors[0];
		connectors[1] = tempConnectors[1];
		connectors[2] = tempConnectors[2];
		connectors[3] = tempConnectors[3];
	}

	public boolean isLocked(){
		return locked;
	}

	public boolean isMarked(){
		return marked;
	}

	public boolean isEmpty(){
		return empty;
	}

	public TileTypes getType(){
		return type;
	}

	public float getDegrees(){
		return degrees;
	}

	public int getConnections(){
		return connections;
	}

	public boolean isConnecting(int cardinalDirection){
		return connectors[cardinalDirection];
	}

	public int absoluteToRelative(int absPos){
		return ((absPos - orientation) % 4 + 4) % 4;
	}

	public Tile adjecent(int cardinalDirection){
		switch(cardinalDirection){
			case 0:
				return north();
			case 1:
				return east();
			case 2:
				return south();
			case 3:
				return west();
			default:
				return null;
		}
	}

	public Tile north(){
		return grid[y+1][x];
	}

	public Tile east(){
		return grid[y][x+1];
	}

	public Tile south(){
		return grid[y-1][x];
	}

	public Tile west(){
		return grid[y][x-1];
	}

	public String toString(){
		return "" + type;
	}
}
