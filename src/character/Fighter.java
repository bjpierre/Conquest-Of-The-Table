package character;

public class Fighter extends BaseCharacter {

	
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

	@Override
	public boolean canUseSpecialAbility() {
		// TODO Auto-generated method stub
		return false;
	}

}
