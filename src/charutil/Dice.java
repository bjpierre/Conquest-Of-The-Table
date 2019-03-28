package charutil;

import java.util.Random;

public class Dice {
	private int sides;
	Random roller;
	
	public Dice(int numSides)
	{
		sides = numSides;
		roller = new Random();
	}
	
	public int roll()
	{
		return roller.nextInt(sides) + 1;
	}
}
