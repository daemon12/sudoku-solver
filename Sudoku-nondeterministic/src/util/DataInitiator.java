package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import sudoku.SudokuSolver;

public class DataInitiator {

	
	
	
	public void readFile(String path){
		BufferedReader br = null;
		try {
			System.out.println("Started reading file....");
			SudokuSolver.fileData =  new int[4][4];
			br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			String[] splitLine = null;
			int i=0;
			while(line != null){
				splitLine = line.split("\t");
				for(int j=0; j<splitLine.length; j++){
					if(splitLine[j].trim().equals("") || !splitLine[j].trim().matches("[1-9]")){
						int index = 0;
						for (int k = 1; k <= 4; k++) {
							SudokuSolver.couldBe[i][j][index++]= k;						
						}						
					}else{
						SudokuSolver.couldBe[i][j][0]= Integer.parseInt(splitLine[j].trim());						
					}
				}
				line=br.readLine();
				i++;
			}
			System.out.println("....read file data!!");
		} catch (IOException e) {
			try {
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	
}
