package character;

import java.util.Random;

public class Fighter extends BaseCharacter {

	public Fighter(Random rand)
	{
		super(10, rand);
	}
	
	public Fighter(int x, int y, boolean team)
	{
		super(10,1, x, y, team);
	}
	
	public Fighter(int[] givenStats, boolean team) 
	{
		this(givenStats, 0, 0, team);
	}
	public Fighter(int[] givenStats, int x, int y, boolean team)
	{
		super(givenStats, 10, 1, x, y, team);
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
