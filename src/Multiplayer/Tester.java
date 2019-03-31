package Multiplayer;

import java.io.IOException;

public class Tester {
	public static void main(String args[]) {
		MultiplayerHandler handler = null;
		try {
			handler = new MultiplayerHandler();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handler.leaveServer();
	}
}
