package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import util.DataInitiator;

public class Sudoku {
	public static int[][] fileData;
	public static int[][] horizontals = new int[9][9];
	public static int[][] verticals = new int[9][9];
	public static int[][] circles = new int[9][9];
	
	public static Map<Integer, Map<Integer, Integer>> numbersIndicesR = new HashMap<Integer, Map<Integer, Integer>>();
	public static Map<Integer, Map<Integer, Integer>> numbersIndicesC = new HashMap<Integer, Map<Integer, Integer>>();
	
	public static int[][] sudoku = new int[9][9];
	public static String[][] couldBe = new String[9][9];
	
	public static DataInitiator dI = null;
	public static Map<String, Boolean> unknown = new HashMap<String, Boolean>();// rowIndex-colIndex
//	public static ArrayList<String> unknown = new ArrayList<String>();// rowIndex-colIndex
	public static Map<String, Map<String,int[]>> mySubords = 
								new HashMap<String, Map<String,int[]>>();// rowIndex-colIndex --> H-->Integer[], V-->Integer[], C-->Integer[]
	public static Map<Integer, Boolean> possibilities = new HashMap<Integer, Boolean>();
	
	static String pfName = "";
	public static BufferedWriter bw = null;
	
	public static void main(String[] args) {
		if(args.length != 1){
			System.out.println("Please enter the filename!!");
			System.out.println("Usage: java -jar GoodHeadoo.jar <sudoku file path>");
			System.exit(0);
		}
		if(!new File(args[0]).exists()){
			System.out.println("File not found!!!!");
			System.out.println("Usage: java -jar GoodHeadoo.jar <sudoku file path>");
			System.exit(0);
		}
		pfName = args[0];
		dI = new DataInitiator(pfName);
		dI.initNumbersIndices();
		dI.readFile();
		dI.updateData();	//first time initialize data with data from file;
		dI.updateData();
		dI.setMySubords();	//Specify which numbers fall in H V and C region of current cell
		
		System.out.println("Number of values to find: "+unknown.size()+" : "+unknown);
		dI.print2DArray(horizontals);
		
		int prevRemaining = 0;
		int currRemaining =0;
		boolean bb = false;
		while(true){
			bb = callRule12();
			prevRemaining = unknown.size();
			if(bb){
				System.out.println("....finally!!!");
				break;
			}else{
				rule3();
			}
			currRemaining = unknown.size();
			if(prevRemaining == currRemaining){
				System.out.println("Failed to solve :(");
				System.out.println("Remaining: "+currRemaining);
				System.out.println("Unknowns: "+unknown.size()+" --> "+unknown);
				dI.print2DArray(horizontals);
				System.out.println("Could be:");
				for (int i = 0; i < 9; i++) {
					System.out.print("\t");
					for (int j = 0; j < 9; j++) {
						System.out.print(couldBe[i][j]+"\t");
					}
					System.out.println();
				}
				System.out.println("!!FaIlUrE!!");
				break;
			}
		}
	}
	
	public static boolean callRule12(){
		boolean r1 = false;
		int prevRemaining = 0;
		int currRemaining =0;
		int numItr = 1;
		while(true){
			prevRemaining = unknown.size();
			if(numItr % 2 != 0){
				r1 = rule1();
			}else{
				r1 = rule2();
			}
			if(r1){	//When final solution is reached
				try {
					bw = new BufferedWriter(new FileWriter(pfName+".output"));
					bw.write("Answer:\n\n");
					for(int i=0;i<horizontals.length;i++){
						int[] arrX = horizontals[i];
						for(int j=0;j<arrX.length;j++){
							bw.write("\t"+horizontals[i][j]);
						}
						bw.write("\n");
					}
					bw.flush();
					bw.close();
					System.out.println("Answer: \nNumber of iterations of rules: "+numItr);
					dI.print2DArray(horizontals);
					System.out.println("!!SuCcEeSs!!");
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			currRemaining = unknown.size();
			if(prevRemaining == currRemaining){
				break;
			}
			numItr++;
		}	
		return r1;
	}
	
	/**
	 * RULE1: Check H-V-C
	 */
	public static boolean rule1(){
		System.out.println("CALLING RULE1:");
		int counter = 0;
		int prevRemaining=0;
		int currRemaining=0;
		while(true){
			counter++;
			prevRemaining=unknown.size();
			Map<String, Boolean> unknownX = unknown;
			for(Entry<String, Boolean> e : unknownX.entrySet()){
				String index = e.getKey();
				//System.out.print(index+" -->");

				possibilities = dI.init();

				Map<String, int[]> e1 = mySubords.get(index);
				dI.updatePossibilities(e1.get("H"));
				dI.updatePossibilities(e1.get("V"));
				dI.updatePossibilities(e1.get("C"));
				//System.out.println(index+" : "+possibilities);

				String[] fixedIndex = index.split("-");
				int x = Integer.parseInt(fixedIndex[0]);
				int y = Integer.parseInt(fixedIndex[1]);
				if(possibilities.size() == 1){
					fileData[x][y] = possibilities.keySet().iterator().next();
					dI.updateData();
					unknown.remove(index);
					//dI.print2DArray(horizontals);
					//System.out.println("Number of values to find: "+unknown.size()+" : "+unknown);
					break;
				}else{
					String ps = "";
					for(Object i : possibilities.keySet().toArray()){
						ps+="-"+i.toString();
					}
					Sudoku.couldBe[x][y] = ps.substring(1);
				}
			}
			currRemaining = unknownX.size();
			if(unknown.size() == 0){
				System.out.println("Unknowns: "+unknown.size()+" --> "+unknown);
				return true;
			}else if (prevRemaining == currRemaining){
				System.out.println("Unknowns: "+unknown.size()+" --> "+unknown);
				return false;
			}
		}		
	}

	/**
	 * RULE2: Slice n Dice 
	 */
	public static boolean rule2(){
		System.out.println("CALLING RULE2:");
		int counter = 0;
		int prevRemaining=0;
		int currRemaining=0;
		while(true){
			counter++;
			prevRemaining=unknown.size();
			for (int searchNumber = 1; searchNumber <= 9; searchNumber++) {
				for (int circleNumber = 0; circleNumber < 9; circleNumber++) {
					//System.out.println("searchFor: "+searchNumber +"\tcircle: "+circleNumber);
					Map<String, Boolean> unknownCoords = dI.getUnknownsForCircle(circleNumber);
					ArrayList<String> possibilities = new ArrayList<String>();
					if(!arrayContainsNumber(circles[circleNumber], searchNumber)){
						for(Entry<String, Boolean> e : unknownCoords.entrySet()){
							String[] indices = e.getKey().split("-");
							int rowIndex = Integer.parseInt(indices[0]);
							int colIndex = Integer.parseInt(indices[1]);
							if(numbersIndicesR.get(searchNumber).get(rowIndex) == -1 && numbersIndicesC.get(searchNumber).get(colIndex) == -1){
								possibilities.add(e.getKey());
							}
						}
						if(possibilities.size() == 1){
							String[] fixedIndex = possibilities.get(0).split("-");
							int x = Integer.parseInt(fixedIndex[0]);
							int y = Integer.parseInt(fixedIndex[1]);
							fileData[x][y] = searchNumber;
							dI.updateData();
							unknown.remove(possibilities.get(0));
						}
					}
				}
			}
			currRemaining = unknown.size();
			if(unknown.size() == 0){
				System.out.println("Unknowns: "+unknown.size()+" --> "+unknown);
				return true;
			}else if (prevRemaining == currRemaining){
				System.out.println("Unknowns: "+unknown.size()+" --> "+unknown);
				return false;
			}
		}
	}
	
	/**
	 * RULE3: Find Locked candidates
	 */
	public static boolean rule3(){
		System.out.println("CALLING RULE3:");
		for (int circleNumber = 0; circleNumber < 9; circleNumber++) {
			Map<String, Boolean> unknownCoords = dI.getUnknownsForCircle(circleNumber);
			//System.out.println("Circle "+circleNumber+": "+unknownCoords);
			//For time being "Locked candidates" rule applying to a scenario in which there are ONLY 2 squares with same possibilities
			// and none of these possibility belong to any other square in same circle
			Map<String, String> numbersPosInCircle = new HashMap<String, String>();
			String[] indicesOfUnknowns = new String[unknownCoords.size()];
			String[] unknownsAtIndices = new String[unknownCoords.size()];
			int ii = 0;
			for(Entry<String, Boolean> e1 : unknownCoords.entrySet()){
				String[] indices1 = e1.getKey().split("-");
				int rowIndex1 = Integer.parseInt(indices1[0]);
				int colIndex1 = Integer.parseInt(indices1[1]);
				/**
				 * If {1,2}=2,7
				 * AND {2,2}=2,7
				 * then,	2 => 1,2 + 2,2
				 * 			7 => 1,2 + 2,2
				 * 			[nope2,7 => 1,2 + 2,2]
				 * Hence they are locked candidates
				 */
				indicesOfUnknowns[ii] = e1.getKey();
				unknownsAtIndices[ii++] = couldBe[rowIndex1][colIndex1];
				if(null == numbersPosInCircle.get(couldBe[rowIndex1][colIndex1])){
					numbersPosInCircle.put(couldBe[rowIndex1][colIndex1], e1.getKey());
				}else{
					numbersPosInCircle.put(couldBe[rowIndex1][colIndex1], numbersPosInCircle.get(couldBe[rowIndex1][colIndex1])+" x "+e1.getKey());					
				}
				String[] arr = couldBe[rowIndex1][colIndex1].split("-");
				for(String s : arr){
					if(null == numbersPosInCircle.get(s)){
						numbersPosInCircle.put(s, e1.getKey());						
					}else{
						numbersPosInCircle.put(s, numbersPosInCircle.get(s)+" x "+e1.getKey());						
					}
				}
			}
			for (int i = 1; i <= 9; i++) {
				for (int j = 1; j <= 9; j++) {
					if(i < j){
						if(null != numbersPosInCircle.get(i+"") && null != numbersPosInCircle.get(j+"")){
							if(numbersPosInCircle.get(i+"").equals(numbersPosInCircle.get(j+""))){
								//CURRENTLY LOCKED INDICES RULE IS AAPLICABLE FOR 2 CANDIDATES AT A TIME ONLY
								if(numbersPosInCircle.get(i+"").split(" x ").length == 2){
									//System.out.println("Locked candidates found => values:"+i+" & "+j+"\tindices: "+numbersPosInCircle.get(i+""));
									//System.out.println("Fixing row/column/circle for locked candidates:");	
									//If candidates in a circle are locked then could be updated accordingly,
									//such as,
									//1. if 2&7 both are locked in a particular row/column then 2AND7 cannot appear in that whole row/column
									//2. possibility of having 2,7 in other cells of circle is removed.
									updateCouldBe(circleNumber, i, j, numbersPosInCircle.get(i+""));
								}
							}
						}
					}
				}
			}
		}		
		System.out.println("Unknowns: "+unknown.size()+" --> "+unknown);
		return false;
	}
	
	public static void updateCouldBe(int circleNumber, int lc1, int lc2, String lockedIndices){
		//1. possibility of having 2,7 in other cells of circle is removed.
		//IS this step required? OR is it covered indirectly in rule2??
		String[] lockedIndicesArr = lockedIndices.split(" x ");
		Map<String, Boolean> unknownCoords = dI.getUnknownsForCircle(circleNumber);
		for(Entry<String, Boolean> e1 : unknownCoords.entrySet()){
			if(!e1.getKey().equals(lockedIndicesArr[0]) && !e1.getKey().equals(lockedIndicesArr[1])){//skipping locked indices
				String[] indices1 = e1.getKey().split("-");
				int rowIndex1 = Integer.parseInt(indices1[0]);
				int colIndex1 = Integer.parseInt(indices1[1]);
				String[] ps = couldBe[rowIndex1][colIndex1].split("-");
				couldBe[rowIndex1][colIndex1]="";
				for(String s : ps){
					if(!s.equals(lc1+"") && !s.equals(lc2+"")){//removing locked candidates from other cells in circle
						couldBe[rowIndex1][colIndex1]+="-"+s;
					}
				}
				//System.out.println("\tupdated couldBe in circle step");
				couldBe[rowIndex1][colIndex1] = couldBe[rowIndex1][colIndex1].substring(1);
				if(couldBe[rowIndex1][colIndex1].length() == 1){
					fileData[rowIndex1][colIndex1] = Integer.parseInt(couldBe[rowIndex1][colIndex1]);
					dI.updateData();
					unknown.remove(rowIndex1+"-"+colIndex1);
				}
			}
		}				
		//2. if 2&7 both are locked in a particular row/column then 2AND7 cannot appear in that whole row/column
		//System.out.println("\tupdating couldBe in row/col step");
		String[] x1y1 = lockedIndicesArr[0].split("-");
		String[] x2y2 = lockedIndicesArr[1].split("-");
		if(x1y1[0].equals(x2y2[0])){
			int lockedRow = Integer.parseInt(x1y1[0]);
			for (int i = 0; i < 9; i++) {
				String currentIndex = lockedRow+"-"+i;
				String[] cellVal = couldBe[lockedRow][i].split("-");
				if(cellVal.length > 1 &&									//if its not then the cell already has single defined value
						!currentIndex.equals(lockedIndicesArr[0]) &&			//if its not the first locked cell
								!currentIndex.equals(lockedIndicesArr[1])){	//if its not the second locked cell
					couldBe[lockedRow][i] = "";
					for(String s : cellVal){
						if(!s.equals(lc1+"") && !s.equals(lc2+"")){//removing locked candidates from other cells in circle
							couldBe[lockedRow][i]+="-"+s;
						}
					}
					couldBe[lockedRow][i] = couldBe[lockedRow][i].substring(1);
					if(couldBe[lockedRow][i].length() == 1){
						fileData[lockedRow][i] = Integer.parseInt(couldBe[lockedRow][i]);
						dI.updateData();
						unknown.remove(lockedRow+"-"+i);
					}
				}
			}
		}else if(x1y1[1].equals(x2y2[1])){
			int lockedCol = Integer.parseInt(x1y1[1]);
			for (int i = 0; i < 9; i++) {
				String currentIndex = i+"-"+lockedCol;
				String[] cellVal = couldBe[i][lockedCol].split("-");
				if(cellVal.length > 1 &&									//if its not then the cell already has single defined value
						!currentIndex.equals(lockedIndicesArr[0]) &&			//if its not the first locked cell
								!currentIndex.equals(lockedIndicesArr[1])){	//if its not the second locked cell
					couldBe[i][lockedCol] = "";
					for(String s : cellVal){
						if(!s.equals(lc1+"") && !s.equals(lc2+"")){//removing locked candidates from other cells in circle
							couldBe[i][lockedCol]+="-"+s;
						}
					}
					couldBe[i][lockedCol] = couldBe[i][lockedCol].substring(1);
					if(couldBe[i][lockedCol].length() == 1){
						fileData[i][lockedCol] = Integer.parseInt(couldBe[i][lockedCol]);
						dI.updateData();
						unknown.remove(i+"-"+lockedCol);
					}
				}
			}
		}
	}
	
	
	public static boolean arrayContainsNumber(int[] arr, int number){
		boolean flag = false;
		for (int i = 0; i < arr.length; i++) {
			if(arr[i] == number){
				flag = true;
				break;
			}
		}
		return flag;
	}

}

/**
		for(Entry<String, Map<String, int[]>> e : mySubords.entrySet()){
			System.out.println(e.getKey()+" :");
			Map<String, int[]> val = e.getValue();
			for(Entry<String, int[]> e1 : val.entrySet()){
				System.out.println("key="+e1.getKey()+" : "+ Arrays.toString(e1.getValue()));
			}
			break;
		}
		System.out.println(Arrays.toString(circles[7]));
		
				for (int i=1; i<=9; i++){
			System.out.print(i+": [");
			for (int j = 0; j < 9; j++) {
				System.out.print(j+"->"+numbersIndices.get(i).get(j)+"\t");
			}
			System.out.println();
		}

*/