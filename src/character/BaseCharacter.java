package character;

import charutil.AbilityScore;
import charutil.Dice;
import item.Item;

public abstract class BaseCharacter {

	protected final int NUM_OF_STATS = 6;
	
	protected int[] stats = new int[NUM_OF_STATS];
	protected int[] saves = new int[NUM_OF_STATS];
	protected int hp;
	protected Dice hpDice;
	protected Item[] equipped = new Item[2];

	private int move;
	private int ac;
	private int remove;
	
	public BaseCharacter(int[] givenStats)
	{
		if(givenStats.length == NUM_OF_STATS)
		{
			for(int i = 0; i < 6; i++)
			{
				stats[i] = givenStats[i];
			}
		}
		move = 6;
		ac = 10 + AbilityScore.getModifier(stats[AbilityScore.dex.ordinal()]);
	}
	
	protected abstract int generateHP();
	protected abstract int attack();
	protected abstract void levelUp();
	
}
