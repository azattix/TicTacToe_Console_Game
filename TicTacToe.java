import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;

class Board {
 
	private int[] squares;
	private int boardSize; 

	public Board(int boardSize) {
		this.boardSize = boardSize;
		squares = new int[boardSize * boardSize];
 		Arrays.fill(squares, -1); 
	}

	public String markupSquare(int i) {
		return (squares[i] == -1) ? " ": (squares[i] == 1) ? "X": "O";
	}

	public boolean fillSquare(int i, int playerId) {
		if (i < 1 || i > squares.length) {
			System.out.println("Warning! Valid numbers are from 1 to " + squares.length + "!");
			return false;
		}

		if (squares[i-1] != -1) {
			System.out.println("Warning! This square is busy!");
			return false;
		}

		squares[i-1] = playerId;

		return true;
	}

	public void printBoard() {
		String[] row = new String[boardSize];

		for (int i = 0; i < squares.length; i++) {
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
	private int maxMoves;

	public Player(int maxMoves) {
		xIsNext = true;
		this.maxMoves = maxMoves;
	}

	public void decrementMoves() {
		maxMoves--;
	}

	public int getMaxMoves() {
		return maxMoves;
	}
 
	public void printInfo() {
		System.out.println("Next player: " +  (xIsNext ? "X": "O"));
	}

	public void changePlayer() {
		xIsNext = !xIsNext;
	}

	public boolean isX() {
		return xIsNext;
	}

	public int currentPlayer() {
		return xIsNext ? 1: 0;
	}

	public String getWinner(int result) {
		return "Winner: " + ((result == -1) ? "Draw": (result==1) ? "X": "O");
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

class Console {
	private Scanner in;
	private String input; 

	public Console() {
		in = new Scanner(System.in);
	}
  
	public int input(String out) {
		System.out.print(out);

		input = in.nextLine();

        while (!Helper.isNumeric(input)) {
        	System.out.print("Only numbers are allowed! " + out);
        	input = in.nextLine(); 
        }

        return Integer.parseInt(input);
	}
}

class Game {

	private Board board;
	private Player player;
	private Winner winner;
	private Console console;

	Game() {
 		this.console = new Console();
	}

	public void reset(int boardSize) {
		board = new Board(boardSize);
		winner = new Winner(boardSize);
		player = new Player(boardSize * boardSize);
	}

	public void run() {
		int boardSize = console.input("Choose the board size: ");

		reset(boardSize);
		
		board.printBoard();
		player.printInfo();

		int result;

		while ((result = winner.calculateWinner(board.getSquares())) == -1) {
			if (player.getMaxMoves() == 0) {
    			break;
    		}
  
			int squareNumber = console.input("Your choice: ");

    		if (board.fillSquare(squareNumber, player.currentPlayer())) {
				player.changePlayer();
				player.decrementMoves();
    		} 

    		board.printBoard();
			player.printInfo();
		}

		System.out.println(player.getWinner(result));
	}
}

public class TicTacToe {
	public static void main(String[] args) {
		Game game = new Game();
		game.run(); 
	}
}