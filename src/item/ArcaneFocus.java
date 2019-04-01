package item;

import charutil.DamageType;

public class ArcaneFocus extends Weapon {

	protected ArcaneFocus() {
		super(10, "Focus", DamageType.generateRandomType(), false, 5);
	}
	
}
