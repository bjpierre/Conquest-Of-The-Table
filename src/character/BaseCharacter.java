package character;

import java.util.Random;

import charutil.AbilityScore;
import charutil.Dice;
import item.ArcaneFocus;
import item.Item;
import item.Weapon;

public class BaseCharacter {

	protected final int NUM_OF_STATS = 6;
	protected Dice d20;
	public static final int[] BASE_ARRAY = {10, 10, 10, 10, 10, 10};
	
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
	private boolean isSelected;
	protected boolean team;
	private int x, y;
	
	public BaseCharacter(int hpDiceSides, Random r)
	{
		d20 = new Dice(20,r );
		for(int i = 0; i < 6; i++)
		{
			stats[i] = BASE_ARRAY[i];
			mods[i] = AbilityScore.getModifier(BASE_ARRAY[i]);
		}
		move = 1;
		ac = 10 + mods[AbilityScore.dex.ordinal()];
		usesOfSpecial = 1;
		rangeOfSpecial = 1;
		hpDice = new Dice(hpDiceSides, r);
		hp = hpDice.roll() + mods[AbilityScore.con.ordinal()];
	}
	
	protected BaseCharacter(int hpDiceSides, int SpecialRange, int x, int y, boolean team)
	{
		this(BASE_ARRAY, hpDiceSides, SpecialRange, x, y, team);
	}
	
	protected BaseCharacter(int[] givenStats, int hpDiceSides, int specialRange, int x, int y, boolean team)
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
		this.x = x;
		this.y = y;
		this.team = team;
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
	
	public int generateHP()
	{
		return hpDice.roll();
	}
	
	public int getHP()
	{
		return hp;
	}
	
	public boolean canUseSpecialAbility()
	{
		return (usesOfSpecial > 0);
	}
	
	public int useSpecialAbility()
	{
		return --usesOfSpecial;
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
	
	public boolean takeDamage(int dmg)
	{
		hp -= dmg;
		return true;
	}
	
	public String specialAbilityName()
	{
		return "NONE";
	}

	public boolean getClicked()
	{
		return isSelected;
	}

	public void setClicked(boolean b)
	{
		isSelected = b;
	}
	
	public boolean getTeam()
	{
		return team;
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
