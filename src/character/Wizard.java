package character;

import java.util.Random;

import charutil.Dice;

public class Wizard extends BaseCharacter {

	private Dice fireballDice;
	
	public Wizard(Random rand)
	{
		super(6, rand);
		fireballDice = new Dice(6, rand);
	}
	
	public Wizard(boolean team)
	{
		super(6,5);
		super.team = team;
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
		super.useSpecialAbility();
		return fireballDice.roll();
	}

}
