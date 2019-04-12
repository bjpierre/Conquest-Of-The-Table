package charutil;

import java.util.Random;

public class Dice {
	private int sides;
	Random roller;
	
	public Dice(int numSides, Random r)
	{
		sides = numSides;
		roller = r;
	}
	
	public Dice(int numSides)
	{
		this(numSides, new Random());
	}
	
	public int roll()
	{
		return roller.nextInt(sides) + 1;
	}
}
