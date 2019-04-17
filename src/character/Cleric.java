package character;

import java.util.Random;

import charutil.Dice;

public class Cleric extends BaseCharacter {

	private Dice healDice;
	
	public Cleric(boolean team)
	{
		super(8,2, team);
	}
	
	public Cleric(Random rand)
	{
		super(8,rand);
		healDice = new Dice(8, rand);
	}
	
	public Cleric(int [] givenStats, boolean team)
	{
		this(givenStats, 0, 0, team);
	}
	
	public Cleric(int[] givenStats, int x, int y, boolean team)
	{
		super(givenStats, 8, 2, x, y, team);
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
