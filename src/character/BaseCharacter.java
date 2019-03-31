package character;

import charutil.AbilityScore;
import charutil.Dice;
import item.ArcaneFocus;
import item.Item;
import item.Weapon;

public abstract class BaseCharacter {

	protected final int NUM_OF_STATS = 6;
	protected Dice d20;
	
	protected int[] stats = new int[NUM_OF_STATS];
	protected int[] mods = new int[NUM_OF_STATS];
	protected int[] saves = new int[NUM_OF_STATS];
	protected int hp;
	protected Dice hpDice;
	protected Item[] equipped = new Item[2];

	private int move;
	private int ac;
	
	protected BaseCharacter(int[] givenStats)
	{
		d20 = new Dice(20);
		if(givenStats.length == NUM_OF_STATS)
		{
			for(int i = 0; i < 6; i++)
			{
				stats[i] = givenStats[i];
				mods[i] = AbilityScore.getModifier(givenStats[i]);
			}
		}
		move = 6;
		ac = 10 + mods[AbilityScore.dex.ordinal()];
	}
	
	public int rollInit()
	{
		return d20.roll() + mods[AbilityScore.dex.ordinal()];
	}
	
	public int weaponToHit()
	{
		Item prime = equipped[Item.PRIMARY];
		if(prime instanceof Weapon)
		{
			if(((Weapon) prime).isFinesse())
			{
				return d20.roll() + mods[AbilityScore.dex.ordinal()];
			}
			else
			{
				return d20.roll() + mods[AbilityScore.str.ordinal()];
			}
		}
		else if(prime instanceof ArcaneFocus)
		{
			return d20.roll() + stats[AbilityScore.intl.ordinal()];
		}
		else
		{
			//signals an inability to do so with primary item
			return -1;
		}
	}
	
	public int getMove()
	{
		return move;
	}
	
	public int getAC()
	{
		return ac;
	}
	
	protected abstract int generateHP();
	public abstract int attack();
	public abstract void levelUp();
	public abstract String specialAbilityName();
	public abstract void useSpecialAbility();
	
	
	
}
