package charutil;
import java.util.ArrayList;
import java.util.HashSet;

import GameBoard.Board.Square;
import character.BaseCharacter;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

public class CharacterAndBoardUtil {
	
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
		int width = board[board.length-1].length;
		int height = board.length;
		
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
	
	public static class Pair {
		int x, y;
		public Pair(int givenX, int givenY)
		{
			x = givenX;
			y = givenY;
		}
		public int getX()
		{
			return x;
		}
		public int getY()
		{
			return y;
		}
	}
	
	public boolean attackSquare(BaseCharacter attacker, BaseCharacter defender)
	{
		int toHit = attacker.weaponToHit();
		if(defender.getAC() <= toHit)
			defender.takeDamage(attacker.attack());
		return false;
	}
	
	//This will be deleted
	public static HashSet<Pair> tempMoveList(int x, int y, Square[][] board)
	{
		HashSet<Pair> moves = new HashSet<Pair>();
	
		if(x > 0)
			moves.add(new Pair(x-1, y));
		if(y > 0)
			moves.add(new Pair(x, y-1));
		if(y > 0 && x > 0)
			moves.add(new Pair(x-1, y-1));
		if(x < board[0].length-1 && y < board.length-1)
			moves.add(new Pair(x+1, y+1));
		if(x < board[0].length-1)
			moves.add(new Pair(x+1, y));
		if(y < board.length-1)
			moves.add(new Pair(x, y+1));
		if(x > 0 && y < board.length-1)
			moves.add(new Pair(x-1, y+1));
		if(x < board[0].length && y > 0)
			moves.add(new Pair(x+1, y-1));
		
		return moves;
	}
	
	public static HashSet<Pair> moveList(int x, int y, Square[][] board)
	{
		BaseCharacter c = board[x][y].getCharacter();
		int mv = c.getMove();
		return recursiveMoveList(x, y, board, mv);
	}
	
	//this is pretty naive, will rework - conor
	private static HashSet<Pair> recursiveMoveList(int x, int y, Square[][] board, int d)
	{
		HashSet<Pair> moves = new HashSet<Pair>();
		if(x >= 0 && y >= 0 && x < board[board.length-1].length && y < board.length)
		{
			moves.add(new Pair(x,y));
			if( d > 0)
			{
				//top left
				moves.addAll(recursiveMoveList(x-1, y-1, board, d-1));
				//top right
				moves.addAll(recursiveMoveList(x+1, y-1, board, d-1));
				//above
				moves.addAll(recursiveMoveList(x, y-1, board, d-1));
				//left
				moves.addAll(recursiveMoveList(x-1, y, board, d-1));
				//right
				moves.addAll(recursiveMoveList(x+1, y, board, d-1));
				//bottom left
				moves.addAll(recursiveMoveList(x-1, y+1, board, d-1));
				//below
				moves.addAll(recursiveMoveList(x, y+1, board, d-1));
				//bottom right
				moves.addAll(recursiveMoveList(x+1, y+1, board, d-1));
			}
				
		}
		return moves;
	}
}
