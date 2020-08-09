import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;

class Board {

	private int[] squares;
	private boolean won;

	Board() {
 		squares = new int[9];
 		Arrays.fill(squares, -1);
 		won = false;
	}

	private String markupSquare(int i) {
		return (squares[i] == 1) ? "x": (squares[i] == 0) ? "o": " ";
	}

	public void printBoard() {
		System.out.println(markupSquare(0) + " | " + markupSquare(1) + " | " + markupSquare(2));
		System.out.println(markupSquare(3) + " | " + markupSquare(4) + " | " + markupSquare(5));
		System.out.println(markupSquare(6) + " | " + markupSquare(7) + " | " + markupSquare(8));
	}

	public boolean fillSquare(int i, int player) {
		if (i < 1 || i > 9) {
			System.out.println("Warning! Valid numbers are from 1 to 9!");
			return false;
		}

		if (squares[i-1] != -1) {
			System.out.println("Warning! Please, choose another square!");
			return false;
		} 

		if (won) {
			return false;   
		}

		squares[i-1] = player;

		int winner = Helper.calculateWinner(squares);

	    if (winner != -1) {
	      	System.out.println("Winner: " + (winner == 1 ? "X" : "O"));
	      	won = true;
	      	return false;
		}

		return true;
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

	public static int calculateWinner(int[] squares) {
		int[][] lines = {
			{ 1, 2, 3 }, 
			{ 4, 5, 6 },
			{ 7, 8, 9 },
			{ 1, 4, 7 },
			{ 2, 5, 8 },
			{ 3, 6, 9 },
			{ 1, 5, 9 },
			{ 3, 5, 7 }
		};

		for (int i = 0; i < lines.length; i++) {
			int a = lines[i][0] - 1;
			int b = lines[i][1] - 1;
			int c = lines[i][2] - 1;

			if (squares[a] != -1 && squares[a] == squares[b] && squares[a] == squares[c]) {
		      	return squares[a];
		    }
		}

		return -1;
	}
}

class Game {

	private Board board;
	private Player player;
	private Scanner in;

	Game(Board board, Player player) {
		this.board = board;
 		this.player = player;
 		this.in = new Scanner(System.in);
	}

	public void refresh() {
		board.printBoard();
		player.printInfo();
	}
  
	public void run() {
		refresh();

		String input = "";

        while (!input.equals("exit")) {
			System.out.print("Your choice: ");
        	input = in.nextLine(); 

        	if (Helper.isNumeric(input)) {
        		System.out.print("\n\n");

        		int squareNumber = Integer.parseInt(input);
        		int currentPlayer = player.isX() ? 1: 0;

        		if (board.fillSquare(squareNumber, currentPlayer)) {
					player.changePlayer();
        		}

				refresh(); 
        	} 
        }
	}
}

public class TicTacToe {
	public static void main(String[] args) {
		Game game = new Game(new Board(), new Player());
		game.run(); 
	}
}