package charutil;
import java.util.ArrayList;

import GameBoard.Board.Square;

public class CombatUtil {
	
	/**
	 * Gives list of all locations the character at x,y can attack
	 * @param x - x coordinate of a character
	 * @param y - y coordinate of a character
	 * @param board - Square object array as used in Board.java
	 * @return - ArrayList of type Pair, that is empty if there is no other character the
	 * character at the given location can attack. Also empty if no character at x,y. Otherwise
	 * it each Pair in the list has a x,y pair that is where another character is.
	 */
	public ArrayList<Pair> canAttack(int x, int y, Square[][] board)
	{
		int width = board.length;
		int height = board[0].length;
		
		ArrayList<Pair> locations = new ArrayList<Pair>();
		
		if(x >= 0 && y >= 0 && x < width && y < height && board[x][y].getCharacter() != null)
		{
			//top left
			if(x > 0 && y > 0 && board[x-1][y-1].getCharacter() != null)
				locations.add(new Pair(x-1, y-1));
			
			if(x > 0)
			{
				//left
				if(board[x-1][y].getCharacter() != null)
					locations.add(new Pair(x-1, y));
				//top right
				if(x-1 < width && y > 0 && board[x+1][y-1].getCharacter() != null)
					locations.add(new Pair(x+1, y-1));
				//right
				if(x+1 < width && board[x+1][y].getCharacter() != null)
					locations.add(new Pair(x+1, y));
			}
			
			if(y > 0)
			{
				//above
				if(board[x][y-1].getCharacter() != null)
					locations.add(new Pair(x, y-1));
				//bottom left
				if(y-1 < height && x > 0 && board[x-1][y+1].getCharacter() != null)
					locations.add(new Pair(x-1, y+1));
				//below
				if(y+1 < height && board[x][y+1].getCharacter() != null)
					locations.add(new Pair(x, y+1));
			}
			
			//bottom right
			if(x+1 < width && y+1 < height && board[x+1][y+1].getCharacter() != null)
				locations.add(new Pair(x+1, y+1));
		}
		
		return locations;
	}
	
	public class Pair {
		int x, y;
		public Pair(int givenX, int givenY)
		{
			x = givenX;
			y = givenY;
		}
	}
}
