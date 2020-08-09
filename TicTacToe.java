import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;

class Board {
 
	private int[] squares;
	private int boardSize; 
	private int squareNumber;

	public Board(int boardSize) {
		this.boardSize = boardSize;
		squareNumber = boardSize * boardSize;
		squares = new int[squareNumber];
 		Arrays.fill(squares, -1); 
	}

	public String markupSquare(int i) {
		return (squares[i] == 1) ? "x": (squares[i] == 0) ? "o": " ";
	}

	public boolean fillSquare(int i, int player) {
		if (i < 1 || i > squareNumber) {
			System.out.println("Warning! Valid numbers are from 1 to " + squareNumber + "!");
			return false;
		}

		if (squares[i-1] != -1) {
			System.out.println("Warning! This square is busy!");
			return false;
		}

		squares[i-1] = player;

		return true;
	}

	public void printBoard() {
		String[] row = new String[boardSize];

		for (int i = 0; i < squareNumber; i++) {
			int col = i % boardSize;
			row[col] = markupSquare(i);

			if (col == (boardSize-1)) {
				System.out.println(String.join(" | ", row));
			}
		}
	}

	public int[] getSquares() {
		return squares;
	}
}

class Winner {
	private int[][] lines;

	public Winner(int cols) {
		int rows = cols*2+2;
		setLines(rows, cols);
	}

	public void setLines(int rows, int cols) { 
		lines = new int[rows][cols];

		int n = 0;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (i < cols) {
					n++;
				} else if (i < cols*2) {
					n = lines[j][i % cols];
				} else if (i == (cols*2)) {
					n = lines[j][j];
				} else {
					n = lines[j][cols-j-1];
				}

				lines[i][j] = n;
			}
		}
	}

	public int calculateWinner(int[] squares) {
		int cols = lines[0].length;

		for (int i = 0; i < lines.length; i++) {
			boolean isWinner = true;
			int num = lines[i][0] - 1;

			for (int j = 1; j < cols; j++) {
				int n = lines[i][j];

				if (squares[num] != squares[n-1] || squares[num] == -1) {
					isWinner = false;
					break;
				}
			}

			if (isWinner == true) {
				return squares[num];
			}
		}

		return -1;
	}
}

class Player {

	private boolean xIsNext;

	public Player() {
		xIsNext = true;
	}
 
	public void printInfo() {
		System.out.println("Next player: " +  (xIsNext ? "x": "o"));
	}

	public void changePlayer() {
		xIsNext = !xIsNext;
	}

	public boolean isX() {
		return xIsNext;
	}
}

final class Helper {
	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);  
			return true;
		} catch(NumberFormatException e){  
			return false;  
		}  
	} 
}

class Game {

	private Board board;
	private Player player;
	private Scanner in;
	private Winner winner;
	private int tries;

	Game(Player player) {
		this.board = null;
 		this.player = player;
 		this.in = new Scanner(System.in);
	}

	public void refresh() {
		board.printBoard();
		player.printInfo();
	}

	public void init() {
		String input = "";

        while (true) {
			System.out.print("Welcome! Choose board size: ");
        	input = in.nextLine(); 

        	if (Helper.isNumeric(input)) {
        		int boardSize = Integer.parseInt(input);
        		this.tries = boardSize*boardSize;
        		this.board = new Board(boardSize);
        		this.winner = new Winner(boardSize);
        		break;
        	} 
        }
	}

	public void run() {
		init();

		refresh();

		String input = ""; 

        while (!input.equals("exit")) {
        	int win = winner.calculateWinner(board.getSquares());

        	if (win == -1) {
        		if (tries == 0) {
        			System.out.println("Draw");
        			break;
        		}

        		System.out.print("Your choice: ");
	        	input = in.nextLine(); 

	        	if (Helper.isNumeric(input)) {
	        		System.out.print("\n\n");

	        		int boardSize = Integer.parseInt(input);
	        		int currentPlayer = player.isX() ? 1: 0;

	        		if (board.fillSquare(boardSize, currentPlayer)) {
						player.changePlayer();
						tries--;
	        		}

					refresh(); 
	        	} 
        	} else {
        		System.out.println("Winner: " + ((win == 1) ? "x": "o"));
        		break;
        	}
        }
	}
}

public class TicTacToe {
	public static void main(String[] args) {
		Game game = new Game(new Player());
		game.run(); 
	}
}