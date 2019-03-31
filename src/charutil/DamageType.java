package charutil;

import java.util.Random;

public enum DamageType {
	
	acid, bludgeoning, cold, fire, force, lightning, necrotic,
	piercing, poison, psychic, radiant, slashing, thunder;
	
	public static final int NUM_OF_TYPES = 13;
	
	public static DamageType generateRandomType()
	{
		Random rand = new Random();
		int index = rand.nextInt(NUM_OF_TYPES);
		return DamageType.values()[index];
	}
}
