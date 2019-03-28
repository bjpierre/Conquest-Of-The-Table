package item;

import charutil.DamageType;
import charutil.Dice;

public abstract class Weapon implements Item {
	protected Dice dmgDice;
	protected String name;
	protected DamageType dmgType;
	
	public int use()
	{
		return dmgDice.roll();
	}
}
