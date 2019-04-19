package character;

import java.util.Random;

import charutil.Dice;

public class Rogue extends BaseCharacter {
	
	private int sneakAtkNum;
	private Dice sneakAtkDice;

	public Rogue(Random rand)
	{
		super(8, rand);
		sneakAtkDice = new Dice(6, rand);
		sneakAtkNum = 1;
	}
	
	public Rogue(int x, int y, boolean team)
	{
		super(8,1, x, y, team);
	}
	
	public Rogue(int[] givenStats, boolean team)
	{
		this(givenStats, 0, 0, team);
	}
	
	public Rogue(int[] givenStats, int x, int y, boolean team) {
		super(givenStats, 8, 1, x, y, team);
		sneakAtkNum = 1;
		sneakAtkDice = new Dice(6);
	}

	@Override
	public String specialAbilityName() {
		return "Sneak Attack";
	}

	@Override
	public int useSpecialAbility() {
		int sum = 0;
		for(int i = 0; i < sneakAtkNum; i++)
		{
			sum += sneakAtkDice.roll();
		}
		return sum;
	}
	
	@Override
	public boolean canUseSpecialAbility() {
		return super.canUseSpecialAbility() || allyNearBy();
	}
	
	private boolean allyNearBy() {
		//TODO
		return false;
	}
}
