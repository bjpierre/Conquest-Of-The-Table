package item;

import charutil.DamageType;

public class ShortSword extends Weapon{
	public ShortSword()
	{
		super(6, "ShortSword", DamageType.slashing, true, 1);
	}
}
