package character;

import charutil.Dice;

public class Wizard extends BaseCharacter {

	private Dice fireballDice;
	
	public Wizard()
	{
		super(6,5);
	}
	
	public Wizard(int[] givenStats) {
		super(givenStats, 6, 5);
		fireballDice = new Dice (6);
	}

	@Override
	public String specialAbilityName() {
		return "Fireball";
	}

	@Override
	public int useSpecialAbility() {
		return fireballDice.roll();
	}

}
