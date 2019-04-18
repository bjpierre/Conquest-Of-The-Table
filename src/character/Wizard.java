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
	
	public Wizard(int x, int y, boolean team)
	{
		super(6,5, x, y, team);
	}
	
	public Wizard(int[] givenStats, boolean team)
	{
		this(givenStats, 0, 0, team);
	}
	
	public Wizard(int[] givenStats, int x, int y, boolean team) {
		super(givenStats, 6, 5, x, y, team);
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
