import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;

class Board {
 
	private int[] squares;
	private int boardSize; 
	private int emptySquaresCount;
	private boolean xIsNext;

	public Board(int boardSize) {
		this.boardSize = boardSize;
 		this.emptySquaresCount = boardSize * boardSize;
 		this.xIsNext = true;
		this.squares = new int[emptySquaresCount];
 		Arrays.fill(squares, -1); 
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

	public String markupSquare(int i) {
		return (squares[i] == -1) ? " ": (squares[i] == 1) ? "X": "O";
	}

	public void printBoard() {
		String[] row = new String[boardSize];

		System.out.println();

		for (int i = 0; i < squares.length; i++) {
			int col = i % boardSize;
			row[col] = markupSquare(i);

			if (col == (boardSize-1)) {
				System.out.println(String.join(" | ", row));
			}
		}
	}

	public void setEmptySquaresCount(int i) {
		this.emptySquaresCount += i;
	}

	public int getEmptySquaresCount() {
		return this.emptySquaresCount;
	}

	public int[] getSquares() {
		return squares;
	}
}

class Winner {
	private int[][] lines; 
	private int result;

	public Winner(int cols) {
		int rows = cols*2+2;
		lines = new int[rows][cols];
		setLines(rows, cols);
		result = -1;
	} 

	public void setLines(int rows, int cols) { 
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
				result = squares[num];
				break;
			}
		}

		return result;
	}

	public void showResult() {
		System.out.println("Winner: " + ((result == -1) ? "Draw": (result==1) ? "X": "O"));
	}

	public int getResult() {
		return result;
	}
}

class Player {

	private boolean xIsNext;

	public Player(int maxMoves) {
		xIsNext = true;
	} 

	public void changePlayer() {
		xIsNext = !xIsNext;
	}

	public int getId() {
		return xIsNext ? 1: 0;
	}

	public void printInfo() {
		System.out.println("Next player: " +  (xIsNext ? "X": "O"));
	} 
}

final class Validator {
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

        while (!Validator.isNumeric(input)) {
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

	public void initialize(int boardSize) {
		board = new Board(boardSize);
		winner = new Winner(boardSize);
		player = new Player(boardSize * boardSize);
	}

	public boolean over() {
		if (board.getEmptySquaresCount() == 0) {
			return true;
		}
		if (winner.calculateWinner(board.getSquares()) != -1) {
			return true;
		}
		return false;
	}

	public void start() {
		int boardSize = console.input("Choose the board size: ");

		initialize(boardSize);

		board.printBoard();
		player.printInfo(); 
 
		while(!over()) {
			int number = console.input("Your choice: ");

    		if (board.fillSquare(number, player.getId())) {
				player.changePlayer();
				board.setEmptySquaresCount(-1);
    		} 

    		board.printBoard();
			player.printInfo();
		}
  
		winner.showResult();
	}
}

public class TicTacToe {
	public static void main(String[] args) {
		Game game = new Game();
		game.start(); 
	}
}