package charutil;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

import GameBoard.Board.Square;

public class AIUtil {
	
	public static void AIUpdateBoard()
	{
		
	}
	
	public static Point[] AIChoices()
	{
		Point[] points = new Point[4];
		
		return points;
	}
	
	public static int Dijkstra(int startX, int startY, Square[][] box, int endX, int endY)
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
				return cur.dist;
			}
			
			// moving up 
	        if (cur.y - 1 >= 0 && visited[cur.y - 1][cur.x] == false) { 
	            q.add(new Node(cur.y - 1, cur.x, cur.dist + 1)); 
	            visited[cur.y - 1][cur.x] = true; 
	        } 
	  
	        // moving down 
	        if (cur.y + 1 < height && visited[cur.y + 1][cur.x] == false) { 
	            q.add(new Node(cur.y + 1, cur.x, cur.dist + 1)); 
	            visited[cur.y + 1][cur.x] = true; 
	        } 
	  
	        // moving left 
	        if (cur.x - 1 >= 0 && visited[cur.y][cur.x - 1] == false) { 
	            q.add(new Node(cur.y, cur.x - 1, cur.dist + 1)); 
	            visited[cur.y][cur.x - 1] = true; 
	        } 
	  
	        // moving right 
	        if (cur.x + 1 < width && visited[cur.y][cur.x + 1] == false) { 
	            q.add(new Node(cur.y, cur.x + 1, cur.dist + 1)); 
	            visited[cur.y][cur.x + 1] = true; 
	        } 
			
		}
		
		//Couldn't reach that attacker
		return -1;
	}

}

class Node
{
	public int x, y, dist;
	
	Node(int givenY, int givenX, int givenDist)
	{
		x = givenX;
		y = givenY;
		dist = givenDist;
	}
}