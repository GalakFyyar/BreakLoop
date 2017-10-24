package com.plaxx.breakloop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Loader{
	public static Tile[][] loadGrid(String file){
		Scanner sc;
		try{
			sc = new Scanner(new File(file));
		}catch(FileNotFoundException e){
			//e.printStackTrace();
			System.out.println("File not found.");
			return null;
		}

		String line;
		ArrayList<ArrayList<TileTypes>> preGrid = new ArrayList<ArrayList<TileTypes>>();
		while(sc.hasNextLine()){
			line = sc.nextLine();
			ArrayList<TileTypes> pregridline = new ArrayList<TileTypes>();

			for(int i = 0; i < line.length(); i++){
				char c = line.charAt(i);
				if(Character.isDigit(c))
					pregridline.add(TileTypes.values()[Integer.parseInt(c + "")]);
				else
					pregridline.add(TileTypes.emty);
			}
			preGrid.add(0, pregridline);
		}
		int x = preGrid.get(0).size();
		int y = preGrid.size();
		Tile[][] grid = new Tile[y][x];

		for(int j = 0; j < y; j++){
			for(int i = 0; i < x; i++){
				grid[j][i] = new Tile(grid, preGrid.get(j).get(i), i ,j);
			}
		}
		return grid;
	}
}
