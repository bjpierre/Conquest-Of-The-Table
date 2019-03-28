package item;

import charutil.DamageType;
import charutil.Dice;

public class ShortSword extends Weapon{
	public ShortSword()
	{
		super.dmgDice = new Dice(6);
		super.dmgType = DamageType.slashing;
	}
}
