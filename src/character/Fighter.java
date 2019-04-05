package character;

public class Fighter extends BaseCharacter {

	public Fighter()
	{
		super(10,1);
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
