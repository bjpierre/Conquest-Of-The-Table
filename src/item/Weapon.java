package item;

import charutil.DamageType;
import charutil.Dice;

public abstract class Weapon implements Item {
	protected Dice dmgDice;
	protected String name;
	protected DamageType dmgType;
	protected boolean isFinesse;
	protected int reach;
	
	protected Weapon(int diceSides, String givenName, DamageType givenType, boolean finesse, int givenReach)
	{
		dmgDice = new Dice(diceSides);
		name = givenName;
		dmgType = givenType;
		isFinesse = finesse;
		reach = givenReach;
	}
	
	public int use()
	{
		return dmgDice.roll();
	}
	
	public boolean isFinesse()
	{
		return isFinesse;
	}
}
