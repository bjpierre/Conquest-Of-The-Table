package character;

import java.util.Random;

public class Fighter extends BaseCharacter {

	public Fighter(Random rand)
	{
		super(10, rand);
	}
	
	public Fighter(boolean team)
	{
		super(10,1);
		super.team = team;
	}
	
	public Fighter(int[] givenStats) 
	{
		super(givenStats, 10, 1);
	}

	@Override
	public String specialAbilityName() {
		return "Extra Attack";
	}

	@Override
	public int useSpecialAbility() {
		super.useSpecialAbility();
		return attack();
	}
}
