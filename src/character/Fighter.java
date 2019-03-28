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
	protected int attack() 
	{
		if(super.equipped[Item.LEFT] instanceof Weapon)
		{
			return super.equipped[Item.LEFT].use();
		}
		else if (super.equipped[Item.RIGHT] instanceof Weapon)
		{
			return super.equipped[Item.RIGHT].use();
		}
		else
		{
			return 0;
		}
	}

	@Override
	protected void levelUp()
	{
		// TODO Auto-generated method stub
		
	}

}
