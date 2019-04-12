package charactertests;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import charutil.AbilityScore;
import charutil.DamageType;

public class CharacterUtilJUnit {

	@Test
	public void AbilitScoreTests()
	{
		assertEquals(-5, AbilityScore.getModifier(1));
		assertEquals(-4, AbilityScore.getModifier(2));
		assertEquals(-4, AbilityScore.getModifier(3));
		assertEquals(-3, AbilityScore.getModifier(4));
		assertEquals(-3, AbilityScore.getModifier(5));
		assertEquals(-2, AbilityScore.getModifier(6));
		assertEquals(-2, AbilityScore.getModifier(7));
		assertEquals(-1, AbilityScore.getModifier(8));
		assertEquals(-1, AbilityScore.getModifier(9));
		assertEquals(0, AbilityScore.getModifier(10));
		assertEquals(0, AbilityScore.getModifier(11));
		assertEquals(1, AbilityScore.getModifier(12));
		assertEquals(1, AbilityScore.getModifier(13));
		assertEquals(2, AbilityScore.getModifier(14));
		assertEquals(2, AbilityScore.getModifier(15));
		assertEquals(3, AbilityScore.getModifier(16));
		assertEquals(3, AbilityScore.getModifier(17));
		assertEquals(4, AbilityScore.getModifier(18));
		assertEquals(4, AbilityScore.getModifier(19));
		assertEquals(5, AbilityScore.getModifier(20));
	}
	
	@Test
	public void RandomTypeTests()
	{
		//4, 0, 7
		Random rand = new Random(1);
		assertEquals(DamageType.force, DamageType.generateRandomType(rand));
		assertEquals(DamageType.acid, DamageType.generateRandomType(rand));
		assertEquals(DamageType.piercing, DamageType.generateRandomType(rand));
	}
	
}
