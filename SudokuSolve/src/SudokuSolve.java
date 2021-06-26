
import java.awt.Font;

public class SudokuSolve {

	private static int[][] board = new int[9][9];
	private static int[][] boardTest = new int[9][9];
	private static boolean[][] empty = new boolean[9][9];
	private static boolean[][] emptyTest = new boolean[9][9];
	private static final int SIZE = 180;
	private static int[][][] boardPos = new int[9][9][2];
	private static final int DISPLAYDELAY = 0;
	private static final int GREENWAIT = 0;
	private static final int KEYCODE_ENTER = 10;

	public static void main(String[] args) {
		final boolean textMode = false;
		StdDraw.enableDoubleBuffering();
		StdDraw.setFont(new Font("Arial", Font.BOLD, 24));
		populateBoardPos();
		drawGrid();

		String filename = args[0];
		populateBoard(filename, boardTest, emptyTest);
		Stopwatch sw = new Stopwatch();
		StdOut.println(checkSolvable(boardTest));
		// createBoard();
		boardPrintNice(boardTest);
		visBoard();
		// if (textMode) {
		// boardPrintNice();
		// StdOut.println();
		// solve();
		// boardPrintNice();
		// } else {
		// StdDraw.enableDoubleBuffering();
		// StdDraw.setFont(new Font("Arial", Font.BOLD, 24));
		// drawGrid();
		// populateBoardPos();
		// visBoard();
		// boardPrintNice();
		// StdOut.println("\n\n");
		// solveVis();
		// boardPrintNice();
		// visBoard();
		// }
		// StdOut.println("Solved");
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
		StdDraw.square(boardPos[row][col][0], boardPos[row][col][1], SIZE / 18.0);
	}

	public static void highlightSquareG(int row, int col) {
		drawGrid();
		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.square(boardPos[row][col][0], boardPos[row][col][1], SIZE / 18.0);
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

	public static boolean complete(int boardS[][]) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (boardS[i][j] == 0) {
					return false;
				}
			}
		}
		return validBoard(boardS);
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

	public static void solve(int boardS[][]) {
		int nRow = 9;
		int nCol = 9;
		for (int i = 0; i < 9 && nRow == 9; i++) {
			for (int j = 0; j < 9 && nRow == 9; j++) {
				if (boardS[i][j] == 0) {
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
			boardS[nRow][nCol] = i;
			if (validBoard(boardS)) {
				solve(boardS);
			}
			if (complete(boardS)) {
				return;
			}
			boardS[nRow][nCol] = 0;
		}

	}

	public static boolean checkSolvable(int boardT[][]) {
		int copy[][] = boardCopy(boardT);
		solve(copy);
		return complete(copy);
	}

	public static boolean sameBoard(int boardT[][], int boardS[][]) {
		for (int i = 0; i < 9; i++) 
			for (int j = 0; j < 9; j++) 
				if (boardT[i][j] != boardS[i][j])
					return false;
		return true;
	}

	public static int[][] boardCopy(int boardT[][]) {
		int[][] copy = new int[9][9];
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				copy[i][j] = boardT[i][j];
		return copy;
	}

	public static boolean validBoard() {
		return validRowsCols() && validBlocks();
	}

	public static boolean validBoard(int boardT[][]) {
		return validRowsCols(boardT) && validBlocks(boardT);
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

	public static boolean validBlocks(int boardT[][]) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (!validBlock(i * 3, j * 3, boardT)) {
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

	public static boolean validBlock(int sRow, int sCol, int boardT[][]) {
		boolean ls[] = new boolean[9];
		for (int i = sRow; i < sRow + 3; i++) {
			for (int j = sCol; j < sCol + 3; j++) {
				int num = boardT[i][j];
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
			boolean[] numsRow = new boolean[10];
			boolean[] numsCol = new boolean[10];
			for (int j = 0; j < 9; j++) {
				int numR = board[i][j];
				if (numsRow[numR])
					return false;

				numsRow[numR] = numR != 0;
				int numC = board[j][i];
				if (numsCol[numC])
					return false;
				numsCol[numC] = numC != 0;
			}
		}
		return true;
	}

	public static boolean validRowsCols(int boardT[][]) {
		for (int i = 0; i < 9; i++) {
			boolean[] numsRow = new boolean[10];
			boolean[] numsCol = new boolean[10];
			for (int j = 0; j < 9; j++) {
				int numR = boardT[i][j];
				if (numsRow[numR])
					return false;

				numsRow[numR] = numR != 0;
				int numC = boardT[j][i];
				if (numsCol[numC])
					return false;
				numsCol[numC] = numC != 0;
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

	public static void populateBoard(String filename, int boardT[][], boolean emptyT[][]) {
		In in = new In(filename);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				boardT[i][j] = in.readInt();
				emptyT[i][j] = boardT[i][j] == 0;
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

	public static void boardPrintNice(int boardT[][]) {
		String str = "";
		for (int i = 0; i < 9; i++) {
			if (i % 3 == 0) {
				str += "-------------------------\n";
			}
			for (int j = 0; j < 9; j++) {
				if (j % 3 == 0) {
					str += "| ";
				}
				str += boardT[i][j] + " ";
			}
			str += "|\n";
		}
		str += "-------------------------\n";
		StdOut.print(str);
	}

	public static void createBoard() {
		boolean exit = false;
		int i = 0;
		int j = 0;
		while (!exit) {
			// StdOut.println("i: " + i + " j: " + j);
			highlightSquare(i, j);
			StdDraw.show();
			while (!StdDraw.hasNextKeyTyped() && !exit) {
				exit = StdDraw.isKeyPressed(KEYCODE_ENTER);
			}
			if (StdDraw.hasNextKeyTyped()) {
				char ch = StdDraw.nextKeyTyped();
				switch (ch) {
					case 'w':
						i = (i == 0) ? 0 : i - 1;
						break;
					case 's':
						i = (i == 8) ? 8 : i + 1;
						break;
					case 'a':
						j = (j == 0) ? 0 : j - 1;
						break;
					case 'd':
						j = j == 8 ? 8 : j + 1;
						break;
					default:
						String in = "" + ch;
						try {
							int num = Integer.parseInt(in);
							board[i][j] = num;
							// StdOut.println(board[i][j]);
							// boardPrint();

							boolean valid = validBoard();
							if (valid) {
								j++;
								i = (i + j / 9 + 9) % 9;
								j = (j + 9) % 9;
							} else {
								StdOut.println("Makes board invalid");
								board[i][j] = 0;
							}
							visBoard();

						} catch (Exception e) {
							StdOut.println("Invalid input");
						}
				}
			}
		}

	}

}
