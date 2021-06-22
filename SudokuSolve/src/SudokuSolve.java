
import java.awt.Font;

public class SudokuSolve {

	public static int boardO[][] = new int[9][9];
	public static int boardG[][] = new int[9][9];
	public static boolean empty[][] = new boolean[9][9];
	public static final int size = 180;
	public static int boardPos[][][] = new int[9][9][2];
	public static final int displayDelay = 50;
	public static final int greenSquareWait = 50;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean textMode = true;
		String filename = "sudoku2.txt";
		populateBoard(filename);
		if (textMode) {
			boardPrintNice();
			StdOut.println();
			solve();
			boardPrintNice();
		} else {
			Font font = new Font("Arial", Font.BOLD, 24);
			StdDraw.enableDoubleBuffering();

			StdDraw.setFont(font);
			drawGrid();
			populateBoardPos();
			visBoard();
//			boardPrintNice();
//			StdOut.println("\n\n");
			solveVis();
//			boardPrintNice();
			visBoard();
			StdOut.println("Solved");
		}
	}

	public static void drawGrid() {
		StdDraw.setXscale(0, size);
		StdDraw.setYscale(0, size);
		StdDraw.setPenColor(StdDraw.BLACK);
		int s = size / 9;
		StdDraw.setPenRadius(0.004);
		StdDraw.line(0, 180, 180, 180);
		for (int i = 0; i < 9; i++) {
			StdDraw.setPenRadius(0.004);
			if (i % 3 == 0) {
				StdDraw.setPenRadius(0.008);
			}
			StdDraw.line(0, i * s, size, i * s);
			StdDraw.line(i * s, 0, i * s, size);
		}
		StdDraw.show();
	}

	public static void highlightSquare(int row, int col) {
		drawGrid();
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.square(boardPos[row][col][0], boardPos[row][col][1], size / 18);
	}

	public static void highlightSquareG(int row, int col) {
		drawGrid();
		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.square(boardPos[row][col][0], boardPos[row][col][1], size / 18);
		StdDraw.show();
		StdDraw.pause(greenSquareWait);
	}

	public static void populateBoardPos() {
		int s = size / 9;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				boardPos[i][j][1] = size - (i + 1) * s + s / 2;
				boardPos[i][j][0] = (j + 1) * s - s / 2;
			}
		}
	}

	public static void visBoard() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				StdDraw.setPenColor(StdDraw.WHITE);
				StdDraw.filledSquare(boardPos[i][j][0], boardPos[i][j][1], size / 18 - 4);
				if (boardG[i][j] != 0) {
					if (empty[i][j]) {
						StdDraw.setPenColor(StdDraw.GRAY);
					} else {
						StdDraw.setPenColor(StdDraw.BLACK);
					}
					StdDraw.text(boardPos[i][j][0], boardPos[i][j][1], "" + boardG[i][j]);
				}
			}
		}
		StdDraw.show();
		StdDraw.pause(displayDelay);
	}

	public static boolean complete() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (boardG[i][j] == 0) {
					return false;
				}
			}
		}
		return validBoard();
	}

	public static void solveVis() {
		int nRow = 9;
		int nCol = 9;
		for (int i = 0; i < 9 && nRow == 9; i++) {
			for (int j = 0; j < 9 && nRow == 9; j++) {
				if (boardG[i][j] == 0) {
					nRow = i;
					nCol = j;
				}
			}
		}
		if (nCol == 9) {
			return;
		}
		for (int i = 1; i < 10; i++) {
			boardG[nRow][nCol] = i;
			highlightSquare(nRow, nCol);
			visBoard();
			if (validBoard()) {
				highlightSquareG(nRow, nCol);
				solveVis();
			}
			if (complete()) {
				drawGrid();
				return;
			}
			boardG[nRow][nCol] = 0;
		}
		return;
	}

	public static void solve() {
		int nRow = 9;
		int nCol = 9;
		for (int i = 0; i < 9 && nRow == 9; i++) {
			for (int j = 0; j < 9 && nRow == 9; j++) {
				if (boardG[i][j] == 0) {
					nRow = i;
					nCol = j;
					break;
				}
			}
		}
		if (nCol == 9) {
			return;
		}
		for (int i = 1; i < 10; i++) {
			boardG[nRow][nCol] = i;
			if (validBoard()) {
				solve();
			}
			if (complete()) {
				return;
			}
			boardG[nRow][nCol] = 0;
		}
		return;
	}

	public static boolean validBoard() {
		return validRowsCols() && validBlocks();
	}

	public static boolean validBlocks() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (!validBlock(i * 3, j * 3)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean validBlock(int sRow, int sCol) {
		boolean ls[] = new boolean[9];
		for (int i = sRow; i < sRow + 3; i++) {
			for (int j = sCol; j < sCol + 3; j++) {
				int num = boardG[i][j];
				if (num != 0) {
					if (ls[num - 1]) {
						return false;
					}
					ls[num - 1] = true;
				}
			}
		}
		return true;
	}

	public static boolean validRowsCols() {
		for (int i = 0; i < 9; i++) {
			boolean lsR[] = new boolean[9];
			boolean lsC[] = new boolean[9];
			for (int j = 0; j < 9; j++) {
				int numR = boardG[i][j];
				if (numR != 0) {
					if (lsR[numR - 1]) {
						return false;
					}
					lsR[numR - 1] = true;
				}
				int numC = boardG[i][j];
				if (numC != 0) {
					if (lsC[numC - 1]) {
						return false;
					}
					lsC[numC - 1] = true;
				}
			}
		}
		return true;
	}

	public static void populateBoard(String filename) {
		In in = new In(filename);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				boardO[i][j] = in.readInt();
				empty[i][j] = boardO[i][j] == 0;
			}
		}
		boardG = boardO.clone();
	}

	public static void boardPrint() {
		String str = "";
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				str += boardG[i][j] + " ";
			}
			str += "\n";
		}
		StdOut.print(str);
	}

	public static void boardPrintNice() {
		String str = "";
		for (int i = 0; i < 9; i++) {
			if (i % 3 == 0) {
				str += "-------------------------\n";
			}
			for (int j = 0; j < 9; j++) {
				if (j % 3 == 0) {
					str += "| ";
				}
				str += boardG[i][j] + " ";
			}
			str += "|\n";
		}
		str += "-------------------------\n";
		StdOut.print(str);
	}

}
