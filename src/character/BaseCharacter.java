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
	private int usesOfSpecial;
	private int rangeOfSpecial;
	
	protected BaseCharacter(int[] givenStats, int hpDiceSides, int specialRange)
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
		usesOfSpecial = 1;
		rangeOfSpecial = specialRange;
		hpDice = new Dice(hpDiceSides);
		hp = hpDice.roll() + mods[AbilityScore.con.ordinal()];
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
			if(prime instanceof ArcaneFocus)
			{
				return d20.roll() + mods[AbilityScore.intl.ordinal()];
			}
			else if(((Weapon) prime).isFinesse())
			{
				return d20.roll() + mods[AbilityScore.dex.ordinal()];
			}
			else
			{
				return d20.roll() + mods[AbilityScore.str.ordinal()];
			}
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
	
	protected int generateHP()
	{
		return hpDice.roll();
	}
	
	public boolean canUseSpecialAbility()
	{
		return (usesOfSpecial > 0);
	}
	
	public int useSpecialAbility()
	{
		return usesOfSpecial--;
	}
	
	public int rangeOfSpecial()
	{
		return rangeOfSpecial;
	}
	
	public int attack() 
	{
		Item prime = equipped[Item.PRIMARY];
		if(prime instanceof Weapon)
		{
			int bonus;
			
			if(prime instanceof ArcaneFocus)
			{
				bonus = 0;
			}
			else if(((Weapon) prime).isFinesse())
			{
				bonus = mods[AbilityScore.dex.ordinal()];
			}
			else
			{
				bonus = mods[AbilityScore.str.ordinal()];
			}
			
			return prime.use() + bonus;
		}
		else
		{
			return -1;
		}
	}
	
	public abstract String specialAbilityName();
	
	
	
}
