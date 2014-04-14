package sudoku;

import java.util.Arrays;

import util.DataInitiator;

public class SudokuSolver {

	public static int[][] fileData;
	public static int[][][] couldBe = new int[4][4][4];
	private static String path = "";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		path = args[0];
		DataInitiator dI = new DataInitiator();
		dI.readFile(path);
		printData();
//		int[] arr = new int[]{1,9,3,4,5,6,8,1};
//		System.out.println("DUP: "+ hasDuplicate(arr));
		int itr=0;
		for (int i = 0; i < couldBe[0][0].length; i++) {
			for (int j = 0; j < couldBe[0][1].length; j++) {
			for (int k = 0; k < couldBe[0][2].length; k++) {
			for (int l = 0; l < couldBe[0][3].length; l++) {
			for (int m = 0; m < couldBe[1][0].length; m++) {
			for (int n = 0; n < couldBe[1][1].length; n++) {
			for (int o = 0; o < couldBe[1][2].length; o++) {
			for (int p = 0; p < couldBe[1][3].length; p++) {
			for (int q = 0; q < couldBe[2][0].length; q++) {
			for (int r = 0; r < couldBe[2][1].length; r++) {
			for (int s = 0; s < couldBe[2][2].length; s++) {
			for (int t = 0; t < couldBe[2][3].length; t++) {
			for (int u = 0; u < couldBe[3][0].length; u++) {
			for (int v = 0; v < couldBe[3][1].length; v++) {
			for (int w = 0; w < couldBe[3][2].length; w++) {
			for (int x = 0; x < couldBe[3][3].length; x++) {

			int[] row1 = new int[]{couldBe[0][0][i], couldBe[0][1][j], couldBe[0][2][k], couldBe[0][3][l] };
			int[] row2 = new int[]{couldBe[1][0][m], couldBe[1][1][n], couldBe[1][2][o], couldBe[1][3][p] };
			int[] row3 = new int[]{couldBe[2][0][q], couldBe[2][1][r], couldBe[2][2][s], couldBe[2][3][t] };
			int[] row4 = new int[]{couldBe[3][0][u], couldBe[3][1][v], couldBe[3][2][w], couldBe[3][3][x] };

			int[] c1 = row1.clone(), c2 = row2.clone(), c3 = row3.clone(), c4 = row4.clone(); 
			Arrays.sort(row1);
			Arrays.sort(row2);
			Arrays.sort(row3);
			Arrays.sort(row4);
			if(row1[0]!=0 && row2[0]!=0 && row3[0]!=0 && row4[0]!=0){
				if(hasNoDuplicate(row1) && hasNoDuplicate(row2) && hasNoDuplicate(row3) && hasNoDuplicate(row4)){
					//solution found
					System.out.println("FOUND SOLUTION:");
					System.out.println(Arrays.toString(c1)+"\n"+Arrays.toString(c2)+"\n"+
							Arrays.toString(c3)+"\n"+Arrays.toString(c4)+"\n");
					System.exit(0);	
				}
			}
			System.out.println("itr: "+itr++);
			}}}}}}}}}}}}}}}}
	}

	public static void printData(){
		for (int i = 0; i < couldBe.length; i++) {
			for (int j = 0; j < couldBe.length; j++) {
				System.out.println((i+1)+", "+(j+1)+" : "+ Arrays.toString(couldBe[i][j]));
			}
		}
	}
	
	public static boolean hasNoDuplicate(int[] arr){
		String x = "-";
		for(int i : arr){
			if(x.contains(i+"")){
				return false;
			}
			x+=i+"-";
		}
		return true;
	}
}
