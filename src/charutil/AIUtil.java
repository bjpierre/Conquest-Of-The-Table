package charutil;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import GameBoard.Board.Square;
import character.BaseCharacter;

public class AIUtil {
	
	public static void AIUpdateBoard(BaseCharacter[][] characters, Square[][] box)
	{
		Random rand = new Random();
		int next = rand.nextInt(5);
		BaseCharacter bc = characters[1][next];
		Node[] choices = AIChoices(bc, characters[0], box);
		
		int min = choices[0].dist;
		int loc = 0;
		
		//find closest
		for(int i = 0; i < choices.length; i++)
		{
			if(min > choices[i].dist)
			{
				min = choices[i].dist;
				loc = i;
			}
		}
		
		Node choice = choices[loc];
		Node move = getNextMove(choice);
		Square toMove = box[move.y][move.x];
		Square fromMove = box[bc.getY()][bc.getX()];
		
		if(choice != null && toMove.getCharacter() == null)
		{
			if(choice.x == move.x && choice.y == move.y)
				System.out.println("It is the same");
			
			//Just move
			toMove.addCharacter(bc, fromMove.getChildren().get(fromMove.getChildren().size()-1));
			fromMove.removeCharacter();
		}
		else
		{
			//Combat
			if(CharacterAndBoardUtil.handleCombat(bc, toMove.getCharacter()))
			{
				System.out.println("AI HITs!");
				toMove.removeCharacter();
				toMove.getChildren().remove(toMove.getChildren().size()-1);
				characters[0][loc] = null;
			}
			else
			{
				System.out.println("AI MISSes!");
			}
		}
		
	}
	
	public static Node[] AIChoices(BaseCharacter attacker, BaseCharacter[] enemies, Square[][] box)
	{
		Node[] distances = new Node[5];
		
		for(int i = 0; i < distances.length; i++)
		{
			BaseCharacter c = enemies[i];
			if(c != null)
			{
				Node n = Dijkstra(c.getX(), c.getY(), box, attacker.getX(), attacker.getY());
				distances[i] = n;
			}
			else
			{
				distances[i] = new Node(-1,-1, Integer.MAX_VALUE);
			}
		}
		
		return distances;
	}
	
	public static Node Dijkstra(int startX, int startY, Square[][] box, int endX, int endY)
	{
		Node source = new Node(startY, startX, 0);
		int height = box.length;
		int width = box[0].length;
		
		boolean[][] visited = new boolean[height][width];
		//if we add terrain, here I would set visited to true for any impassible ones
		
		Queue<Node> q = new LinkedList<Node>();
		q.add(source);
		visited[startY][startX] = true;
		
		while(!q.isEmpty())
		{
			Node cur = q.poll();
			int curX = cur.x;
			int curY = cur.y;
			
			if(curX == endX && curY == endY)
			{
				return cur;
			}
			
			// moving up 
	        if (cur.y - 1 >= 0 && visited[cur.y - 1][cur.x] == false) { 
	            q.add(new Node(cur.y - 1, cur.x, cur.dist + 1).addPrev(cur)); 
	            visited[cur.y - 1][cur.x] = true; 
	        } 
	  
	        // moving down 
	        if (cur.y + 1 < height && visited[cur.y + 1][cur.x] == false) { 
	            q.add(new Node(cur.y + 1, cur.x, cur.dist + 1).addPrev(cur)); 
	            visited[cur.y + 1][cur.x] = true; 
	        } 
	  
	        // moving left 
	        if (cur.x - 1 >= 0 && visited[cur.y][cur.x - 1] == false) { 
	            q.add(new Node(cur.y, cur.x - 1, cur.dist + 1).addPrev(cur)); 
	            visited[cur.y][cur.x - 1] = true; 
	        } 
	  
	        // moving right 
	        if (cur.x + 1 < width && visited[cur.y][cur.x + 1] == false) { 
	            q.add(new Node(cur.y, cur.x + 1, cur.dist + 1).addPrev(cur)); 
	            visited[cur.y][cur.x + 1] = true; 
	        } 
			
		}
		
		//Couldn't reach that attacker
		return null;
	}

	public static Node getNextMove(Node n)
	{
		return (n.prev == null) ? n : n.prev;
	}
}

class Node
{
	public int x, y, dist;
	public Node prev;
	
	Node(int givenY, int givenX, int givenDist)
	{
		x = givenX;
		y = givenY;
		dist = givenDist;
		prev = null;
	}
	
	public Node addPrev(Node n)
	{
		prev = n;
		return this;
	}
	
	public String toString()
	{
		return "(" + x + ", " + y + ", " + dist + ")";
	}
}