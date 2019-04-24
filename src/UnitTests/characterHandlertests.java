package UnitTests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import Multiplayer.CharacterHandler;
import character.Fighter;

class characterHandlertests {

	@Test
	void testConstructorNull() {
		CharacterHandler demo = new CharacterHandler(null,null,null);
		
		assertNotNull(demo);
		assertNull(demo.getCharacter());
		assertNull(demo.getTeam());
		assertNull(demo.getPoint());
		assertEquals(demo.getX(),-1);
		assertEquals(demo.getY(),-1);
		assertFalse(demo.getClicked());
	}
	

}
