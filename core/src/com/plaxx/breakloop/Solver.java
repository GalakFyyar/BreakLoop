package com.plaxx.breakloop;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

@SuppressWarnings("WeakerAccess")
public class Solver{
	private static int width = 0;
	private static int height = 0;
	private static boolean rotating = false;
	private static int rotationsToGo = 0;
	private static Tile tileToRotate = null;
	private static LinkedList<Tile> unlockedLeft = null;
	private static Iterator<Tile> iter = null;
	private static boolean solved = false;

	public static void init(Tile[][] grid){
		width = grid[0].length;
		height = grid.length;
		unlockedLeft = new LinkedList<Tile>();

		//Add non empty Tiles to unlockedLeft, also lock crosses
		for(int j = 0; j < height; j++){
			for(int i = 0; i < width; i++){
				Tile t = grid [j][i];
				TileTypes type = t.getType();

				if(type == TileTypes.cros){
					t.lock();
					continue;
				}

				if(type != TileTypes.emty)
					unlockedLeft.add(t);
			}
		}
		iter = unlockedLeft.iterator();
	}

	public static void solveStep(Tile[][] grid){
		if(solved && !rotating){
			return; //Stop doing stuff
		}

		if(rotating){
			rotateTileAnimation();
		}else{
			//System.out.println("START");

			//do stuff
			Tile t;
			if(iter.hasNext())
				t = iter.next();
			else{
				if(unlockedLeft.size() == 0)
					solved = true;
				else{
					iter = unlockedLeft.iterator(); //reset back to beginning.
					System.out.println("START");
				}
				return;
			}

			TileTypes type = t.getType();

			ArrayList<ArrayList<Integer>> orientations = calcLegalOrientations(t);
			ArrayList<Integer> legalOrientations = orientations.get(0);
			ArrayList<Integer> mandatoryOrientations = orientations.get(1);

			System.out.println("t= " + type + " x= " + t.x + " y= " + t.y + "  lo= " + legalOrientations + "  mo= " + mandatoryOrientations);

			int legalOrientationsLen = legalOrientations.size();
			if(legalOrientationsLen == 1){
				t.lock();
				iter.remove();
				startTileRotatation(t, legalOrientations.get(0));
				return;
			}
			else if(legalOrientationsLen == 2 && type == TileTypes.line){
				t.lock();
				iter.remove();
				startTileRotatation(t, legalOrientations.get(0));
				return;
			}

			if(mandatoryOrientations.size() == 1){
				t.lock();
				iter.remove();
				startTileRotatation(t, mandatoryOrientations.get(0));
				return;
			}

			legalOrientations.retainAll(mandatoryOrientations);
			if(legalOrientations.size() == 1){
				t.lock();
				iter.remove();
				startTileRotatation(t, legalOrientations.get(0));
			}
		}
	}

	//can't calc empty
	private static ArrayList<ArrayList<Integer>> calcLegalOrientations(Tile t){
		ArrayList<Integer> legalOrientations = new ArrayList<Integer>(4);        //based on what's not impossible
		ArrayList<Integer> mandatoryOrientations = new ArrayList<Integer>(4);    //based on what's necessary

		//possible orientations, starting from orientation 0 to orientation 3.
		for(int po = 0; po < 4; po++){
			boolean legalOrientation = true;                                    //assume true, prove false
			boolean mandatoryConnection = false;                                //assume false, prove true

			//tile sides from north to west
			for(int s = 0; s < 4; s++){
				if(t.isConnecting(s)){
					Tile adjacentTile = t.adjecent(s);
					TileTypes adjacentType = adjacentTile.getType();

					//Tile is connecting to an empty Tile
					if(adjacentType == TileTypes.emty){
						legalOrientation = false;
						break;
					}

					if(adjacentTile.isLocked()){
						//is the adjacentTile connecting with the current Tile?
						boolean connectingBack = adjacentTile.isConnecting((s + 2) % 4);

						if(!connectingBack){
							legalOrientation = false;				//Tile is connecting to a locked Tile that is not connecting back
							break;
						}else
							mandatoryConnection = true;				//Tile is connecting to a locked Tile that is connecting back
					}
				}
			}

			if(legalOrientation)
				legalOrientations.add(po);
			if(mandatoryConnection)
				mandatoryOrientations.add(po);


			t.rotateCW(1);
		}


		//Calc total adjacent Tiles that are locked and connecting to this Tile.
		int lockedAndConnectingSides = 0;
		ArrayList<Integer> lockedAdjacents = new ArrayList<Integer>(4);
		for(int s = 0; s < 4; s++){
			Tile adjacentTile = t.adjecent(s);

			if(adjacentTile.isLocked() && adjacentTile.isConnecting((s + 2) % 4)){
				lockedAdjacents.add(1);
				lockedAndConnectingSides++;
			}else
				lockedAdjacents.add(0);
		}

		//if the connections of a tile are equal to the lockAndConnectingSides then there is only one possible orientation.
		if(t.getConnections() == lockedAndConnectingSides){
			int firstIndex = -1;
			int i = 0;
			boolean foundZero = false;
			while(true){
				int locked = lockedAdjacents.get(i);

				if(locked == 0)
					foundZero = true;

				else if(locked == 1 && foundZero){
					firstIndex = i;
					break;
				}

				i = (i + 1) % 4;
			}

			mandatoryOrientations.clear();
			mandatoryOrientations.add(t.absoluteToRelative(firstIndex));
		}

		ArrayList<ArrayList<Integer>> orientations = new ArrayList<ArrayList<Integer>>(2);
		orientations.add(legalOrientations);
		orientations.add(mandatoryOrientations);
		return orientations;
	}

	private static void startTileRotatation(Tile t, int nTimes){
		if(nTimes == 0)
			return;

		tileToRotate = t;
		rotating = true;
		rotationsToGo = nTimes;
		tileToRotate.rotateCW(nTimes);
	}

	private static void rotateTileAnimation(){
		tileToRotate.addDegreesCW(3);
		float degCurrent = tileToRotate.getDegrees() % 90;

		if(degCurrent == 0){
			if(rotationsToGo > 1)
				rotationsToGo--;
			else{
				//tileToRotate.lock();
				rotating = false;        //Stop rotating
			}
		}
	}

	public static void scramble(Tile[][] grid){
		int rand;
		for(int aj = 0; aj < height; aj++){
			for(int ai = 0; ai < width; ai++){
				Tile t = grid[aj][ai];

				if(t.getType() == TileTypes.emty || t.getType() == TileTypes.cros)
					continue;

				rand = MathUtils.random(3);

				t.addDegreesCW(90 * rand);

				t.rotateCW(rand);
			}
		}
	}
}
