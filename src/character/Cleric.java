package character;

import java.util.Random;

import charutil.Dice;

public class Cleric extends BaseCharacter {

	private Dice healDice;
	
	public Cleric(boolean team)
	{
		super(8,2);
		super.team = team;
	}
	
	public Cleric(Random rand)
	{
		super(8,rand);
		healDice = new Dice(8, rand);
	}
	
	public Cleric(int[] givenStats) {
		super(givenStats, 8, 2);
		healDice = new Dice(8);
	}

	@Override
	public String specialAbilityName() {
		return "Heal";
	}

	@Override
	public int useSpecialAbility() {
		return healDice.roll();
	}
}
