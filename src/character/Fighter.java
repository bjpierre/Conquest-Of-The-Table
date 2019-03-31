package character;

import charutil.Dice;
import item.Item;
import item.Weapon;

public class Fighter extends BaseCharacter {

	public Fighter(int[] givenStats) 
	{
		super(givenStats);
		super.hpDice = new Dice(10);
		super.hp = generateHP();
	}

	@Override
	protected int generateHP() 
	{
		return super.hpDice.roll();
	}

	@Override
	public int attack() 
	{
		if(super.equipped[Item.PRIMARY] instanceof Weapon)
		{
			return super.equipped[Item.PRIMARY].use();
		}
		else if (super.equipped[Item.SECONDARY] instanceof Weapon)
		{
			return super.equipped[Item.SECONDARY].use();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public void levelUp()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public String specialAbilityName() {
		return "Extra Attack";
	}

	@Override
	public void useSpecialAbility() {
	}

}
