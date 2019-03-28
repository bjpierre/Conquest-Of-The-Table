package character;

import charutil.Dice;

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
	protected void attack() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void levelUp()
	{
		// TODO Auto-generated method stub
		
	}

}
