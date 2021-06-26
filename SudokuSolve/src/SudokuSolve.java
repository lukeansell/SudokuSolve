
import java.awt.*;
// import java.awt.Color.*;

public class SudokuSolve {

	private static int[][] board = new int[9][9];
	private static int[][] boardSolved;
	private static boolean[][] empty = new boolean[9][9];
	private static final int SIZE = 180;
	private static int[][][] boardPos = new int[9][9][2];
	private static final int DISPLAYDELAY = 0;
	private static final int GREENWAIT = 0;
	private static final int KEYCODE_ENTER = 10;
	private static final int KEYCODE_SHIFT = 16;
	private static final int KEYCODE_ESC = 27;
	private static final Font FONT = new Font("Arial", Font.BOLD, 24);
	private static final Font SMALLFONT = new Font("Arial", Font.BOLD, 8);

	public static void main(String[] args) {
		Stopwatch sw = new Stopwatch();
		final boolean textMode = false;
		// visMessage("hello");
		visClearMsg();
		if (!textMode) {
			initVis();
			visMessage("hello");
		}
		if (args.length == 0) {
			createBoard();
		} else {
			String filename = args[0];
			populateBoard(filename);
		}

		if (checkSolvable(board)) {
			boardPrintNice();
			boolean win = playBoard();
			if (!win) {
				visMessage("enter for vis solve, shift for instant solve");
				int vis = -1;
				while (vis == -1)
					if (StdDraw.isKeyPressed(KEYCODE_ENTER))
						vis = 1;
					else if (StdDraw.isKeyPressed(KEYCODE_SHIFT))
						vis = 0;
					else if (StdDraw.isKeyPressed(KEYCODE_ESC))
						System.exit(0);
				visClearMsg();
				String msg = "";

				if (!checkSolvable(board))
					msg = "reset board";
				resetBoard();
				if (vis == 1) {
					visMessage(msg + " vis solve");
					solveVis();
					visMessage("Solved");
				} else {
					solve();
					visBoard();
				}
			}
		} else {
			StdOut.println("cannot be solved");
		}
		StdOut.println(sw.elapsedTime() + "s");
	}

	public static void initVis() {
		StdDraw.enableDoubleBuffering();
		StdDraw.setFont(FONT);
		populateBoardPos();
		drawGrid();
	}

	public static void visMessage(String text) {
		visClearMsg();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(SIZE / 2, SIZE + 5, text);
		StdDraw.show();
	}

	public static void visClearMsg() {
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.filledRectangle(SIZE / 2, SIZE + 5, SIZE / 2, 5);
	}

	public static void drawGrid() {
		StdDraw.setXscale(0, SIZE);
		StdDraw.setYscale(0, SIZE + 10);
		// StdDraw.setCanvasSize(SIZE*10, SIZE * 10);
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

	public static void highlightSquare(int row, int col, Color c) {
		drawGrid();
		StdDraw.setPenColor(c);
		StdDraw.square(boardPos[row][col][0], boardPos[row][col][1], SIZE / 18.0);
	}

	public static void highlightSquareNoClear(int row, int col, Color c) {
		StdDraw.setPenColor(c);
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
				visBoard();
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

		if (nCol == 9)
			return;

		for (int i = 1; i < 10; i++) {
			boardS[nRow][nCol] = i;
			if (validBoard(boardS))
				solve(boardS);
			if (complete(boardS))
				return;
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
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++) {
				board[i][j] = in.readInt();
				empty[i][j] = board[i][j] == 0;
			}

	}

	public static void resetBoard() {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (empty[i][j])
					board[i][j] = 0;
	}

	public static void populateEmpty() {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				empty[i][j] = board[i][j] == 0;
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
				if (StdDraw.isKeyPressed(KEYCODE_ENTER)) {
					populateEmpty();
					return;
				}
			}
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
						boolean validBoard = validBoard();
						boolean solvable = checkSolvable(board);
						StdOut.println(validBoard && solvable);
						if (validBoard && solvable) {
							j++;
							i = (i + j / 9 + 9) % 9;
							j = (j + 9) % 9;
						} else {
							board[i][j] = 0;
							if (!validBoard)
								StdOut.println("Makes board invalid");
							else
								StdOut.println("Unsolvable");
						}
						visBoard();

					} catch (Exception e) {
						StdOut.println("Invalid input");
					}
			}
		}

	}

	public static boolean playBoard() {
		// StdOut.println("here2");
		boardSolved = boardCopy(board);
		solve(boardSolved);
		// StdOut.println("here2");
		boardPrintNice(boardSolved);
		boolean solved = false;
		int i = 0;
		int j = 0;
		Color c = StdDraw.GREEN;
		while (!complete()) {
			if (isCorrect(board[i][j], boardSolved[i][j]))
				c = StdDraw.GREEN;
			else if (checkSolvable(board))
				c = StdDraw.ORANGE;
			else
				c = StdDraw.RED;

			highlightSquare(i, j, c);
			visBoard();
			while (!StdDraw.hasNextKeyTyped()) {
				if (StdDraw.isKeyPressed(KEYCODE_ESC))
					return false;
			}
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
					if (empty[i][j]) {
						try {
							int num = Integer.parseInt(in);
							board[i][j] = num;
							visBoard();

						} catch (Exception e) {
							StdOut.println("Invalid input");
						}
					}
			}
		}

		visMessage("Winnner!!");
		return true;
	}

	public static boolean isCorrect(int boardT[][], int boardS[][]) {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (boardT[i][j] != boardS[i][j] && boardT[i][j] != 0)
					return false;

		return true;
	}

	public static boolean isCorrect(int t, int s) {
		return t == s || t == 0;
	}

}
