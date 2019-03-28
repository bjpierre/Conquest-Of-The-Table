package character;

import charutil.Dice;

public abstract class BaseCharacter {

	protected final int NUM_OF_STATS = 6;
	
	protected int[] stats = new int[NUM_OF_STATS];
	protected int[] saves = new int[NUM_OF_STATS];
	protected int hp;
	protected Dice hpDice;

	private int move;

	private int ac;
	
	public BaseCharacter(int[] givenStats)
	{
		if(givenStats.length == NUM_OF_STATS)
		{
			for(int i = 0; i < 6; i++)
			{
				stats[i] = givenStats[i];
			}
		}
	}
	
	protected abstract int generateHP();
	protected abstract void attack();
	protected abstract void levelUp();
	
}
