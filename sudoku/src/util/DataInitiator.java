package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.Sudoku;

public class DataInitiator {
	String path = "";
	BufferedReader br = null;
	public DataInitiator(String path) {
		this.path = path;
	}

	public void readFile(){
		try {
			System.out.println("Started reading file....");
			Sudoku.fileData =  new int[9][9];
			br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			String[] splitLine = null;
			int i=0;
			while(line != null){
				splitLine = line.split("\t");
				for(int j=0; j<splitLine.length; j++){
					
					if(splitLine[j].trim().equals("") || !splitLine[j].trim().matches("[1-9]")){
						splitLine[j] = "0";
					}
					Sudoku.fileData[i][j] = Integer.parseInt(splitLine[j].trim());
					Sudoku.couldBe[i][j] = Integer.parseInt(splitLine[j].trim())+"-";
				}
				line=br.readLine();
				i++;
			}
			System.out.println(".....read file data!!");
		} catch (IOException e) {
			try {
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	
	public void updateData(){
		int[] splitLine = null;
		int cntR1=0;
		int circleNumX=0, circleNum, startPositionInCircleX=0, startPositionInCircle;
		Integer unknownIndex=1;
		for (int ii = 0; ii < Sudoku.fileData.length; ii++) {
			splitLine = Sudoku.fileData[ii];
			startPositionInCircle = startPositionInCircleX;
			circleNum =circleNumX;
			for (int jj = 0; jj < Sudoku.fileData[ii].length; jj++) {
				if(splitLine[jj] == 0){
//					Sudoku.unknown.put(unknownIndex++, ii+"-"+jj);
					Sudoku.unknown.put(ii+"-"+jj, true);
				}else if(splitLine[jj] != 0){	//Put indices for solved positions
					Sudoku.numbersIndicesR.get(splitLine[jj]).put(ii, jj);
					Sudoku.numbersIndicesC.get(splitLine[jj]).put(jj, ii);
					//Update possibilities array for numbers found
					Sudoku.couldBe[ii][jj] = splitLine[jj]+"";
				}
				//for horizontal array update
				Sudoku.horizontals[cntR1][jj] = splitLine[jj];							//horizontal lines
				//for vertical array update
				Sudoku.verticals[jj][cntR1] = splitLine[jj];							//vertical lines
				//for circle array update
				Sudoku.circles[circleNum][startPositionInCircle] = splitLine[jj];		//squares
				startPositionInCircle++;
				if((startPositionInCircle) % 3 ==0){
					startPositionInCircle=startPositionInCircleX;
					circleNum+=1;
				}
				//Initializing FINAL SUDOKU ANSWER
				Sudoku.sudoku[ii][jj] =  splitLine[jj];
			}
			cntR1++;
			startPositionInCircleX+=3;
			
			if((ii+1) % 3 ==0){
				circleNumX+=3;
				startPositionInCircleX=0;
			}
		}
	}
	
	public void setMySubords(){
		String index = "";
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				index = i+"-"+j;
				Map<String, int[]> subs = new HashMap<String, int[]>();// H-->Integer[], V-->Integer[], C-->Integer[]
				subs.put("H", Sudoku.horizontals[i]);
				subs.put("V", Sudoku.verticals[j]);
				subs.put("C", Sudoku.circles[getCircleNumber(i, j)]);
				
				Sudoku.mySubords.put(index, subs);
			}
		}
	}
	
	public void print2DArray(int[][] arr){
		System.out.println("SUDOKU:");
		for(int i=0;i<arr.length;i++){
			int[] arrX = arr[i];
			for(int j=0;j<arrX.length;j++){
				System.out.print("\t"+arr[i][j]);
			}
			System.out.println();
		}
	}
	
	private int getCircleNumber(int i, int j){
		int circleNumber = 3 * (i/3) + ((j/3) + 1);
		return circleNumber-1;
	}

	public void initNumbersIndices(){
		for (int i = 1; i <= 9; i++) {
			Map<Integer, Integer> fillItR = new HashMap<Integer, Integer>();
			Map<Integer, Integer> fillItC = new HashMap<Integer, Integer>();
			for (int j = 0; j < 9; j++) {
				fillItR.put(j, -1);
				fillItC.put(j, -1);
			}
			Sudoku.numbersIndicesR.put(i, fillItR);
			Sudoku.numbersIndicesC.put(i, fillItC);
		}
	}
	
	public Map<Integer, Boolean> init(){
		Sudoku.possibilities.put(1, true);
		Sudoku.possibilities.put(2, true);
		Sudoku.possibilities.put(3, true);
		Sudoku.possibilities.put(4, true);
		Sudoku.possibilities.put(5, true);
		Sudoku.possibilities.put(6, true);
		Sudoku.possibilities.put(7, true);
		Sudoku.possibilities.put(8, true);
		Sudoku.possibilities.put(9, true);
		return Sudoku.possibilities;
	}

	public void updatePossibilities(int[] ps){
		for (int i : ps){
			if(Sudoku.possibilities.containsKey(i)){
				Sudoku.possibilities.remove(i);
			}
		}
	}

	public Map<String, Boolean> getUnknownsForCircle(int circleNumber){
		Map<String, Boolean> coords = new HashMap<String, Boolean>();
		int[] arr = Sudoku.circles[circleNumber];
		int x1 = 3* (circleNumber/3);
		int y1 = 3* (circleNumber%3);
		int x,y;
		int cnt = 0;
		for (int i = 0; i < 3; i++) {
			x = x1 + i;
			for (int j = 0; j < 3; j++) {
				y = y1 + j;
				if (arr[cnt] == 0) {
					coords.put(x+"-"+y, true);
				}
				cnt++;
			}
		}
		return coords;
	}
}


/**
 * //					if(null == Sudoku.numberIndex.get(splitLine[i]))){
//						ArrayList<String> x = new ArrayList<String>();
//						x.add(ii + "-" + i);
//						Sudoku.numberIndex.put(splitLine[i]), x);
//					}else{
//						ArrayList<String> x = Sudoku.numberIndex.get(splitLine[i]));
//						x.add(ii + "-" + i);
//						Sudoku.numberIndex.put(splitLine[i]), x);						
//					}
*/
