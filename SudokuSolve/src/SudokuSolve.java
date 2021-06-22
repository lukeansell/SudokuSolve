
import java.awt.Font;

public class SudokuSolve {

	private static int[][] board = new int[9][9];
	private static boolean[][] empty = new boolean[9][9];
	private static final int SIZE = 180;
	private static int[][][] boardPos = new int[9][9][2];
	private static final int DISPLAYDELAY = 0;
	private static final int GREENWAIT = 0;

	public static void main(String[] args) {
		final boolean textMode = true;
		String filename = "sudoku3.txt";
		populateBoard(filename);
		Stopwatch sw = new Stopwatch();
		if (textMode) {
			boardPrintNice();
			StdOut.println();
			solve();
			boardPrintNice();
		} else {
			StdDraw.enableDoubleBuffering();
			StdDraw.setFont(new Font("Arial", Font.BOLD, 24));
			drawGrid();
			populateBoardPos();
			visBoard();
			// boardPrintNice();
			// StdOut.println("\n\n");
			solveVis();
			// boardPrintNice();
			visBoard();
		}
		StdOut.println("Solved");
		StdOut.println(sw.elapsedTime());
	}

	public static void drawGrid() {
		StdDraw.setXscale(0, SIZE);
		StdDraw.setYscale(0, SIZE);
		StdDraw.setPenColor(StdDraw.BLACK);
		int s = SIZE / 9;
		StdDraw.setPenRadius(0.004);
		StdDraw.line(0, 180, 180, 180);
		for (int i = 0; i < 9; i++) {
			StdDraw.setPenRadius(0.004);
			if (i % 3 == 0) {
				StdDraw.setPenRadius(0.008);
			}
			StdDraw.line(0, i * s, SIZE, i * s);
			StdDraw.line(i * s, 0, i * s, SIZE);
		}
		StdDraw.show();
	}

	public static void highlightSquare(int row, int col) {
		drawGrid();
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.square(boardPos[row][col][0], boardPos[row][col][1], SIZE / 18);
	}

	public static void highlightSquareG(int row, int col) {
		drawGrid();
		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.square(boardPos[row][col][0], boardPos[row][col][1], SIZE / 18);
		StdDraw.show();
		StdDraw.pause(GREENWAIT);
	}

	public static void populateBoardPos() {
		int s = SIZE / 9;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				boardPos[i][j][1] = SIZE - (i + 1) * s + s / 2;
				boardPos[i][j][0] = (j + 1) * s - s / 2;
			}
		}
	}

	public static void visBoard() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				StdDraw.setPenColor(StdDraw.WHITE);
				StdDraw.filledSquare(boardPos[i][j][0], boardPos[i][j][1], SIZE / 18 - 4);
				if (board[i][j] != 0) {
					if (empty[i][j]) {
						StdDraw.setPenColor(StdDraw.GRAY);
					} else {
						StdDraw.setPenColor(StdDraw.BLACK);
					}
					StdDraw.text(boardPos[i][j][0], boardPos[i][j][1], "" + board[i][j]);
				}
			}
		}
		StdDraw.show();
		StdDraw.pause(DISPLAYDELAY);
	}

	public static boolean complete() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] == 0) {
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
				if (board[i][j] == 0) {
					nRow = i;
					nCol = j;
				}
			}
		}
		if (nCol == 9) {
			return;
		}
		for (int i = 1; i < 10; i++) {
			board[nRow][nCol] = i;
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
			board[nRow][nCol] = 0;
		}
	}

	public static void solve() {
		int nRow = 9;
		int nCol = 9;
		for (int i = 0; i < 9 && nRow == 9; i++) {
			for (int j = 0; j < 9 && nRow == 9; j++) {
				if (board[i][j] == 0) {
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
			board[nRow][nCol] = i;
			if (validBoard()) {
				solve();
			}
			if (complete()) {
				return;
			}
			board[nRow][nCol] = 0;
		}
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
				int num = board[i][j];
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
				int numR = board[i][j];
				if (numR != 0) {
					if (lsR[numR - 1]) {
						return false;
					}
					lsR[numR - 1] = true;
				}
				int numC = board[j][i];
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
				board[i][j] = in.readInt();
				empty[i][j] = board[i][j] == 0;
			}
		}
	}

	public static void boardPrint() {
		String str = "";
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				str += board[i][j] + " ";
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
				str += board[i][j] + " ";
			}
			str += "|\n";
		}
		str += "-------------------------\n";
		StdOut.print(str);
	}

}
